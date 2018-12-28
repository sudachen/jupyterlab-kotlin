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
package com.twosigma.beakerx.chart.serializer;

import com.twosigma.beakerx.chart.xychart.plotitem.BasedXYGraphics;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BasedXYGraphicsSerializer<T extends BasedXYGraphics> extends XYGraphicsSerializer<T> {

  @Override
  public void serialize(T basedXYGraphics, JsonGenerator jgen, SerializerProvider sp)
    throws IOException, JsonProcessingException {

    super.serialize(basedXYGraphics, jgen, sp);

    if (basedXYGraphics.getBases() != null) {
      jgen.writeObjectField("bases", basedXYGraphics.getBases());
    } else {
      jgen.writeObjectField("base", basedXYGraphics.getBase());
    }

  }

}
