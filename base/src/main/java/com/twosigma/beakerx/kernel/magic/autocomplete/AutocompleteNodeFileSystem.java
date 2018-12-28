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
package com.twosigma.beakerx.kernel.magic.autocomplete;

import com.twosigma.beakerx.autocomplete.AutocompleteNode;
import com.twosigma.beakerx.autocomplete.AutocompleteResult;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AutocompleteNodeFileSystem extends AutocompleteNode {

  public AutocompleteNodeFileSystem(String name, List<AutocompleteNode> children) {
    super(name, children);
  }

  @Override
  public Optional<AutocompleteResult> matchToTheWord(String text, LinkedList<String> parts, String last) {
    List<String> result = list(parts, last);
    if (!result.isEmpty()) {
      return Optional.of(new AutocompleteResult(result, text.length() - last.length()));
    }
    return Optional.empty();
  }

  @Override
  public Optional<AutocompleteResult> findNextWord(String text, LinkedList<String> parts) {
    String dir = ".";
    List<String> result = list(parts, dir);
    if (!result.isEmpty()) {
      return Optional.of(new AutocompleteResult(result, text.length()));
    }
    return Optional.empty();
  }

  @NotNull
  private List<String> list(LinkedList<String> parts, String dir) {
    if (parts.isEmpty()) {
      try {
        Path path = Paths.get(dir);
        if (Files.exists(path)) {
          return listDirectory(path);
        } else {
          return listParentDirectory(path);
        }
      } catch (Exception e) {
        return new ArrayList<>();
      }
    }
    return new ArrayList<>();
  }

  @NotNull
  private List<String> listParentDirectory(Path path) throws IOException {
    List<String> result = new ArrayList<>();
    Path parent = path.getParent();
    Files.newDirectoryStream(parent, p -> {
      if (p.toString().startsWith(path.toString())) {
        if (p.toFile().isFile()) {
          return p.toString().endsWith(".jar");
        }
        return true;
      }
      return false;
    }).forEach(x -> result.add(x.toString()));
    return result;
  }

  @NotNull
  private List<String> listDirectory(Path path) throws IOException {
    List<String> result = new ArrayList<>();
    Files.newDirectoryStream(path, filter)
            .forEach(x -> result.add(x.toString()));
    return result;
  }

  private DirectoryStream.Filter<Path> filter = path -> {
    if (path.toFile().isFile()) {
      return path.toString().endsWith(".jar");
    }
    return true;
  };
}
