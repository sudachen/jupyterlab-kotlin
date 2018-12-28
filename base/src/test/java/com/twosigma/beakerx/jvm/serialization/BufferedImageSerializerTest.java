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

package com.twosigma.beakerx.jvm.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.twosigma.beakerx.KernelTest;
import com.twosigma.beakerx.kernel.KernelManager;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageSerializerTest {
  private BufferedImage bufferedImage;
  private static BufferedImageSerializer serializer;
  private static SerializationTestHelper<BufferedImageSerializer, BufferedImage> helper;

  @BeforeClass
  public static void setUpClass() throws IOException {
    serializer = new BufferedImageSerializer();
    helper = new SerializationTestHelper<>(serializer);
  }

  @Before
  public void setUp() throws Exception {
    KernelManager.register(new KernelTest());
    bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
  }

  @After
  public void tearDown() throws Exception {
    KernelManager.register(null);
  }

  @Test
  public void serializeBufferedImage_resultJsonHasType() throws IOException {
    //when
    JsonNode actualObj = helper.serializeObject(bufferedImage);
    //then
    Assertions.assertThat(actualObj.has("type")).isTrue();
    Assertions.assertThat(actualObj.get("type").asText()).isEqualTo("ImageIcon");
  }

  @Test
  public void serializeImageData_resultJsonHasImageData() throws IOException {
    //when
    JsonNode actualObj = helper.serializeObject(bufferedImage);
    //then
    Assertions.assertThat(actualObj.has("imageData")).isTrue();
    Assertions.assertThat(actualObj.get("imageData").asText()).isNotEmpty();
  }

  @Test
  public void serializeWidth_resultJsonHasWidth() throws IOException {
    int width = bufferedImage.getWidth();
    //when
    JsonNode actualObj = helper.serializeObject(bufferedImage);
    //then
    Assertions.assertThat(actualObj.has("width")).isTrue();
    Assertions.assertThat(actualObj.get("width").asInt()).isEqualTo(width);
  }

  @Test
  public void serializeHeight_resultJsonHasHeight() throws IOException {
    int height = bufferedImage.getHeight();
    //when
    JsonNode actualObj = helper.serializeObject(bufferedImage);
    //then
    Assertions.assertThat(actualObj.has("height")).isTrue();
    Assertions.assertThat(actualObj.get("height").asInt()).isEqualTo(height);
  }

}
