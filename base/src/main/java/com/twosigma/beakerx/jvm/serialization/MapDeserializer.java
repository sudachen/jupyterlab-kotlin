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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This class is used to deserialize output data that contain standard output or error in the notebook
 */

public class MapDeserializer implements ObjectDeserializer {
  private final static Logger logger = LoggerFactory.getLogger(MapDeserializer.class.getName());
  private final BeakerObjectConverter parent;

  public MapDeserializer(BeakerObjectConverter p) {
    parent = p;
  }

  @Override
  public boolean canBeUsed(JsonNode n) {
    return n.isObject() && (!n.has("type") || !parent.isKnownBeakerType(n.get("type").asText()));
  }

  @Override
  public Object deserialize(JsonNode n, ObjectMapper mapper) {
    HashMap<String, Object> o = new HashMap<String,Object>();
    try {
      logger.debug("using custom map deserializer");
      Iterator<Entry<String, JsonNode>> e = n.fields();
      while(e.hasNext()) {
        Entry<String, JsonNode> ee = e.next();
        o.put(ee.getKey(), parent.deserialize(ee.getValue(),mapper));
      }
    } catch (Exception e) {
      logger.error("exception deserializing Map ", e);
      o = null;
    }
    return o;
  }

}
