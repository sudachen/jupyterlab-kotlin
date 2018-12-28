/*
 *  Copyright 2014 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beakerx.jvm.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This class is used to deserialize output data that contain standard output or error in the notebook
 */

public class ResultsDeserializer implements ObjectDeserializer {
  private final static Logger logger = LoggerFactory.getLogger(ResultsDeserializer.class.getName());
  private final BeakerObjectConverter parent;

  public ResultsDeserializer(BeakerObjectConverter p) {
    parent = p;
    parent.addKnownBeakerType("Results");
  }

  @Override
  public boolean canBeUsed(JsonNode n) {
    return n.has("type") && n.get("type").asText().equals("Results");
  }

  @Override
  public Object deserialize(JsonNode n, ObjectMapper mapper) {
    Object o = null;
    try {
      if (n.has("payload")) {
        o = parent.deserialize(n.get("payload"), mapper);
      }      
    } catch (Exception e) {
      logger.error("exception deserializing Results ", e);
    }
    return o;

  }

}
