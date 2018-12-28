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
package com.twosigma.beakerx.evaluator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempFolderFactoryImpl implements TempFolderFactory {

  Logger logger = LoggerFactory.getLogger(TempFolderFactoryImpl.class.getName());

  @Override
  public Path createTempFolder() {
    Path ret = null;
    try {
      ret = Files.createTempDirectory("beaker");
    } catch (IOException e) {
      logger.error("No temp folder set for beaker", e);
    }
    return ret.toAbsolutePath();
  }

}
