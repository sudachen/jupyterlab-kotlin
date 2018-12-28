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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.twosigma.beakerx.BeakerXCommRepositoryMock;
import com.twosigma.beakerx.KernelTest;
import com.twosigma.beakerx.chart.Color;
import com.twosigma.beakerx.chart.GradientColor;
import com.twosigma.beakerx.chart.heatmap.HeatMap;
import com.twosigma.beakerx.kernel.KernelManager;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

public class HeatMapSerializerTest {

  static ObjectMapper mapper;
  static HeatMapSerializer heatMapSerializer;
  JsonGenerator jgen;
  StringWriter sw;

  @BeforeClass
  public static void initClassStubData() {
    mapper = new ObjectMapper();
    heatMapSerializer = new HeatMapSerializer();
  }

  @Before
  public void initTestStubData() throws IOException {
    sw = new StringWriter();
    jgen = mapper.getJsonFactory().createJsonGenerator(sw);
    KernelManager.register(new KernelTest(new BeakerXCommRepositoryMock()));
  }

  @After
  public void tearDown() throws Exception {
    KernelManager.register(null);
  }


  @Test
  public void serializeDataOfHeatMap_resultJsonHasGraphicsList() throws IOException {
    //when
    HeatMap heatMap = new HeatMap();
    heatMap.setData(
            new Integer[][]{
                    new Integer[]{new Integer(1), new Integer(2)},
                    new Integer[]{new Integer(3), new Integer(4)}
            });
    heatMapSerializer.serialize(heatMap, jgen, new DefaultSerializerProvider.Impl());
    jgen.flush();
    //then
    JsonNode actualObj = mapper.readTree(sw.toString());
    Assertions.assertThat(actualObj.has("graphics_list")).isTrue();
    Assertions.assertThat(actualObj.get("graphics_list")).isNotEmpty();
  }

  @Test
  public void serializeColorOfHeatMap_resultJsonHasColor() throws IOException {
    //when
    HeatMap heatMap = new HeatMap();
    heatMap.setColor(new GradientColor(Arrays.asList(Color.GREEN, Color.BLUE)));
    heatMapSerializer.serialize(heatMap, jgen, new DefaultSerializerProvider.Impl());
    jgen.flush();
    //then
    JsonNode actualObj = mapper.readTree(sw.toString());
    Assertions.assertThat(actualObj.has("color")).isTrue();
    Assertions.assertThat(actualObj.get("color").get("colors")).isNotEmpty();
  }
}
