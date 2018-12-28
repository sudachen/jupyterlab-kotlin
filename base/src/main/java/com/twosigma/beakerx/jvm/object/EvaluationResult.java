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
package com.twosigma.beakerx.jvm.object;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.twosigma.beakerx.jvm.serialization.BeakerObjectConverter;

public class EvaluationResult {

  private final Object value;

  public EvaluationResult(Object v) {
    value = v;
  }

  public Object getValue() {
    return value;
  }

  public static class Serializer extends JsonSerializer<EvaluationResult> {

    private final Provider<BeakerObjectConverter> objectSerializerProvider;

    @Inject
    public Serializer(Provider<BeakerObjectConverter> osp) {
      objectSerializerProvider = osp;
    }

    private BeakerObjectConverter getObjectSerializer() {
      return objectSerializerProvider.get();
    }

    @Override
    public void serialize( EvaluationResult evalResult, JsonGenerator jgen,
        SerializerProvider sp) throws IOException, JsonProcessingException {

      Object obj = evalResult.getValue();
      if (!getObjectSerializer().writeObject(obj, jgen, true))
        jgen.writeObject(obj.toString());
    }
  }
}
