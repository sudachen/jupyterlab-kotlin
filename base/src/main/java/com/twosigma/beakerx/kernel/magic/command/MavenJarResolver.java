/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx.kernel.magic.command;

import com.twosigma.beakerx.kernel.commands.MavenInvocationSilentOutputHandler;
import com.twosigma.beakerx.kernel.commands.MavenJarResolverSilentLogger;
import com.twosigma.beakerx.kernel.magic.command.functionality.MvnLoggerWidget;
import com.twosigma.beakerx.util.Preconditions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class MavenJarResolver {

  public static final String MVN_DIR = File.separator + "mvnJars";
  public static final String POM_XML = "PomTemplateMagicCommand.xml";
  public static final String GOAL = "validate";
  public static final String MAVEN_BUILT_CLASSPATH_FILE_NAME = "mavenclasspathfilename.txt";

  private final String pathToMavenRepo;
  private ResolverParams commandParams;
  private String mavenLocation;
  private PomFactory pomFactory;

  public MavenJarResolver(final ResolverParams commandParams,
                          PomFactory pomFactory) {
    this.commandParams = Preconditions.checkNotNull(commandParams);
    this.pathToMavenRepo = getOrCreateFile(commandParams.getPathToNotebookJars()).getAbsolutePath();
    this.pomFactory = pomFactory;
  }

  public AddMvnCommandResult retrieve(Dependency dependency, MvnLoggerWidget progress) {
    List<Dependency> dependencies = singletonList(dependency);
    return retrieve(dependencies, progress);
  }

  public AddMvnCommandResult retrieve(List<Dependency> dependencies, MvnLoggerWidget progress) {
    File finalPom = null;
    try {
      String pomAsString = pomFactory.createPom(new PomFactory.Params(pathToMavenRepo, dependencies, commandParams.getRepos(), GOAL, MAVEN_BUILT_CLASSPATH_FILE_NAME));
      finalPom = saveToFile(commandParams.getPathToNotebookJars(), pomAsString);
      InvocationRequest request = createInvocationRequest();
      request.setOffline(commandParams.getOffline());
      request.setPomFile(finalPom);
      request.setUpdateSnapshots(true);
      Invoker invoker = getInvoker(progress);
      progress.display();
      InvocationResult invocationResult = invoker.execute(request);
      progress.close();
      return getResult(invocationResult, dependencies);
    } catch (Exception e) {
      return AddMvnCommandResult.error(e.getMessage());
    } finally {
      deletePomFolder(finalPom);
    }
  }

  private File saveToFile(String pathToNotebookJars, String pomAsString)
          throws IOException {
    File finalPom = new File(pathToNotebookJars + "/poms/pom-" + UUID.randomUUID() + "-" + "xml");

    FileUtils.writeStringToFile(finalPom, pomAsString, StandardCharsets.UTF_8);
    return finalPom;
  }

  private Invoker getInvoker(MvnLoggerWidget progress) {
    Invoker invoker = new DefaultInvoker();
    String mvn = findMvn();
    System.setProperty("maven.home", mvn);
    invoker.setLogger(new MavenJarResolverSilentLogger());
    invoker.setOutputHandler(new MavenInvocationSilentOutputHandler(progress));
    invoker.setLocalRepositoryDirectory(getOrCreateFile(this.commandParams.getPathToCache()));
    return invoker;
  }

  public String findMvn() {
    if (mavenLocation == null) {

      if (System.getenv("M2_HOME") != null) {
        mavenLocation = System.getenv("M2_HOME") + "/bin/mvn";
        return mavenLocation;
      }

      for (String dirname : System.getenv("PATH").split(File.pathSeparator)) {
        File file = new File(dirname, "mvn");
        if (file.isFile() && file.canExecute()) {
          mavenLocation = file.getAbsolutePath();
          return mavenLocation;
        }
      }
      throw new RuntimeException("No mvn found, please install mvn by 'conda install maven' or setup M2_HOME");
    }
    return mavenLocation;
  }

  private AddMvnCommandResult getResult(InvocationResult invocationResult, List<Dependency> dependencies) {
    if (invocationResult.getExitCode() != 0) {
      if (invocationResult.getExecutionException() != null) {
        return AddMvnCommandResult.error(invocationResult.getExecutionException().getMessage());
      }
      StringBuilder errorMsgBuilder = new StringBuilder("Could not resolve dependencies for:");
      for (Dependency dependency : dependencies) {
        errorMsgBuilder
                .append("\n").append(dependency.groupId).append(" : ")
                .append(dependency.artifactId).append(" : ")
                .append(dependency.version).append(" : ")
                .append(dependency.type);
      }
      return AddMvnCommandResult.error(errorMsgBuilder.toString());
    }

    return AddMvnCommandResult.success(transformFromMavenRepoToKernelRepo(mavenBuildClasspath(), jarsFromRepo()));
  }

  private List<String> transformFromMavenRepoToKernelRepo(List<String> jarNamesFromBuildClasspath, Map<String, Path> jarNames) {
    List<String> result = new ArrayList<>();
    jarNamesFromBuildClasspath.forEach(jarName -> {
      result.add(jarNames.get(jarName).toAbsolutePath().toString());
    });
    return result;
  }

  private Map<String, Path> jarsFromRepo() {
    try {
      List<Path> collect = Files.list(Paths.get(pathToMavenRepo)).collect(Collectors.toList());
      return collect.stream().collect(Collectors.toMap(x -> x.getFileName().toString(), x -> x));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> mavenBuildClasspath() {
    String jarPathsAsString = null;
    try {
      File fileToClasspath = new File(pathToMavenRepo, MAVEN_BUILT_CLASSPATH_FILE_NAME);
      InputStream fileInputStream = new FileInputStream(fileToClasspath);
      jarPathsAsString = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Stream<String> stream = Arrays.stream(jarPathsAsString.split(File.pathSeparator));
    return stream.map(x -> Paths.get(x).getFileName().toString()).collect(Collectors.toList());
  }

  private InvocationRequest createInvocationRequest() {
    InvocationRequest request = new DefaultInvocationRequest();
    request.setGoals(singletonList(GOAL));
    return request;
  }

  private void deletePomFolder(File finalPom) {
    if (finalPom != null) {
      File parentFile = new File(finalPom.getParent());
      try {
        FileUtils.deleteDirectory(parentFile);
      } catch (IOException e) {
      }
    }
  }

  public String getPathToMavenRepo() {
    return pathToMavenRepo;
  }

  public static class Dependency {

    static final String DEFAULT_TYPE = "jar";

    private String groupId;
    private String artifactId;
    private String version;
    private String type = DEFAULT_TYPE;
    private Optional<String> classifier = Optional.empty();

    private Dependency(String groupId, String artifactId, String version) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
    }

    public static Dependency create(List<String> args) {
      Dependency dependency = new Dependency(args.get(0), args.get(1), args.get(2));
      if (args.size() > 3) {
        dependency.type = args.get(3);
      }
      if (args.size() > 4) {
        dependency.classifier = Optional.of(args.get(4));
      }
      return dependency;
    }

    public String getGroupId() {
      return groupId;
    }

    public String getArtifactId() {
      return artifactId;
    }

    public String getVersion() {
      return version;
    }

    public String getType() {
      return type;
    }

    public Optional<String> getClassifier() {
      return classifier;
    }
  }

  public static class AddMvnCommandResult {

    private boolean jarRetrieved;
    private String errorMessage;
    private List<String> addedJarsPaths;

    private AddMvnCommandResult(boolean retrieved, String errorMessage, List<String> addedJarsPaths) {
      this.jarRetrieved = retrieved;
      this.errorMessage = errorMessage;
      this.addedJarsPaths = addedJarsPaths;
    }

    public boolean isJarRetrieved() {
      return jarRetrieved;
    }

    public String getErrorMessage() {
      return errorMessage;
    }

    public static AddMvnCommandResult success(List<String> addedJarsPaths) {
      return new AddMvnCommandResult(true, "", addedJarsPaths);
    }

    public static AddMvnCommandResult error(String msg) {
      return new AddMvnCommandResult(false, msg, new ArrayList<>());
    }

    public List<String> getAddedJarPaths() {
      return addedJarsPaths;
    }
  }

  public static class ResolverParams {

    private String pathToCache;
    private String pathToNotebookJars;
    private boolean offline = false;
    private Map<String, String> repos = Collections.emptyMap();

    public ResolverParams(String pathToCache, String pathToNotebookJars, boolean offline) {
      this.pathToCache = Preconditions.checkNotNull(pathToCache);
      this.pathToNotebookJars = Preconditions.checkNotNull(pathToNotebookJars);
      this.offline = offline;
    }

    public ResolverParams(String pathToCache, String pathToNotebookJars) {
      this(pathToCache, pathToNotebookJars, false);
    }

    public String getPathToCache() {
      return pathToCache;
    }

    public String getPathToNotebookJars() {
      return pathToNotebookJars;
    }

    public boolean getOffline() {
      return offline;
    }

    public Map<String, String> getRepos() {
      return repos;
    }

    public void setRepos(Map<String, String> repos) {
      this.repos = repos;
    }
  }

  private File getOrCreateFile(String pathToMavenRepo) {
    File theDir = new File(pathToMavenRepo);
    if (!theDir.exists()) {
      try {
        theDir.mkdirs();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return theDir;
  }
}
