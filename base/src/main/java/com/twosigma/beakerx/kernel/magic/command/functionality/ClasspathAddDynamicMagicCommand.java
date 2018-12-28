/*
 *  Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beakerx.kernel.magic.command.functionality;

import com.twosigma.beakerx.TryResult;
import com.twosigma.beakerx.jvm.object.SimpleEvaluationObject;
import com.twosigma.beakerx.kernel.KernelFunctionality;
import com.twosigma.beakerx.kernel.magic.command.MagicCommandExecutionParam;
import com.twosigma.beakerx.kernel.magic.command.outcome.MagicCommandOutcomeItem;
import com.twosigma.beakerx.kernel.magic.command.outcome.MagicCommandOutput;

import java.util.Collection;

import static com.twosigma.beakerx.kernel.PlainCode.createSimpleEvaluationObject;
import static com.twosigma.beakerx.kernel.magic.command.functionality.MagicCommandUtils.splitPath;

public class ClasspathAddDynamicMagicCommand extends ClasspathMagicCommand {

  private static final String ADD = "add";
  private static final String DYNAMIC = "dynamic";
  public static final String CLASSPATH_ADD_DYNAMIC = CLASSPATH + " " + ADD + " " + DYNAMIC;

  public ClasspathAddDynamicMagicCommand(KernelFunctionality kernel) {
    super(kernel);
  }

  @Override
  public String getMagicCommandName() {
    return CLASSPATH_ADD_DYNAMIC;
  }

  @Override
  public boolean matchCommand(String command) {
    String[] commandParts = MagicCommandUtils.splitPath(command);
    return commandParts.length > 2 && commandParts[0].equals(CLASSPATH) && commandParts[1].equals(ADD) && commandParts[2].equals(DYNAMIC);
  }

  @Override
  public MagicCommandOutcomeItem execute(MagicCommandExecutionParam param) {
    String command = param.getCommand();
    String[] split = splitPath(command);
    if (split.length < 4) {
      return new MagicCommandOutput(MagicCommandOutput.Status.ERROR, WRONG_FORMAT_MSG + CLASSPATH_ADD_DYNAMIC);
    }

    String codeToExecute = command.substring(command.indexOf(DYNAMIC) + DYNAMIC.length()).trim();
    SimpleEvaluationObject seo = createSimpleEvaluationObject(codeToExecute, kernel, param.getCode().getMessage(), param.getExecutionCount());
    TryResult either = kernel.executeCode(codeToExecute, seo);
    if (either.isResult()) {
      try {
        return addJars(either.result());
      } catch (Exception e) {
        return new MagicCommandOutput(MagicCommandOutput.Status.ERROR, "There occurs problem during execution of " + CLASSPATH_ADD_DYNAMIC + " : " + e.getMessage());
      }
    } else {
      return new MagicCommandOutput(MagicCommandOutput.Status.ERROR, "There occurs problem during execution of " + CLASSPATH_ADD_DYNAMIC + " : " + either.error());
    }
  }

  @SuppressWarnings("unchecked")
  private MagicCommandOutcomeItem addJars(Object path) {
    if (path instanceof String) {
      return handleAddedJars((String) path);
    } else if (path instanceof Collection) {
      return handleAddedJars((Collection<String>) path);
    } else {
      throw new RuntimeException("Classpath dynamic handles String or Collection.");
    }
  }

}
