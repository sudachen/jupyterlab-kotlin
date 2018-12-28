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
package com.twosigma.beakerx.widget.floats;

import com.twosigma.beakerx.kernel.KernelManager;
import com.twosigma.beakerx.KernelTest;
import com.twosigma.beakerx.widget.FloatProgress;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static com.twosigma.beakerx.widget.TestWidgetUtils.verifyMsgForProperty;
import static com.twosigma.beakerx.widget.TestWidgetUtils.verifyOpenCommMsg;

public class FloatProgressTest {

  private KernelTest groovyKernel;

  @Before
  public void setUp() throws Exception {
    groovyKernel = new KernelTest();
    KernelManager.register(groovyKernel);
  }

  @After
  public void tearDown() throws Exception {
    KernelManager.register(null);
  }

  @Test
  public void shouldSendCommOpenWhenCreate() throws Exception {
    //given
    //when
    new FloatProgress();
    //then
    verifyOpenCommMsg(groovyKernel.getPublishedMessages(), FloatProgress.MODEL_NAME_VALUE, FloatProgress.VIEW_NAME_VALUE);
  }

  @Test
  public void shouldSendCommMsgWhenOrientationChange() throws Exception {
    //given
    FloatProgress floatProgress = floatProgress();
    //when
    floatProgress.setOrientation("vertical");
    //then
    verifyMsgForProperty(groovyKernel, FloatProgress.ORIENTATION, "vertical");
  }

  @Test
  public void setOrientation_hasThatOrientation() throws Exception {
    String expected = "test";
    //given
    FloatProgress floatProgress = floatProgress();
    //when
    floatProgress.setOrientation(expected);
    //then
    Assertions.assertThat(floatProgress.getOrientation()).isEqualTo(expected);
  }

  private FloatProgress floatProgress() throws NoSuchAlgorithmException {
    FloatProgress progress = new FloatProgress();
    groovyKernel.clearPublishedMessages();
    return progress;
  }


  @Test
  public void shouldSendCommMsgWhenBarStyleChange() throws Exception {
    //given
    FloatProgress floatProgress = floatProgress();
    //when
    floatProgress.setBarStyle(FloatProgress.BarStyle.SUCCESS);
    //then
    verifyMsgForProperty(groovyKernel, FloatProgress.BAR_STYLE, FloatProgress.BarStyle.SUCCESS.getValue());
  }

}