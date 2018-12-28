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

import com.twosigma.beakerx.chart.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorDeserializer implements ObjectDeserializer {
  private final static Logger logger = LoggerFactory.getLogger(ColorDeserializer.class.getName());

  public ColorDeserializer(BeakerObjectConverter p) {
    p.addKnownBeakerType("Color");
  }

  @Override
  public boolean canBeUsed(JsonNode n) {
    return n.asText().matches("^#[0-9A-Fa-f]{8}$");
  }

  @Override
  public Object deserialize(JsonNode n, ObjectMapper mapper) {
    Object o = null;
    try {
      long i = Long.parseLong(n.asText().substring(1), 16);
      o = new Color((int)((i >> 16) & 0xFF),
                    (int)((i >> 8)  & 0xFF),
                    (int)( i        & 0xFF),
                    (int)((i >> 24) & 0xFF));
    } catch (Exception e) {
      logger.error("exception deserializing Color {}", e.getMessage());
    }
    return o;
  }

}
