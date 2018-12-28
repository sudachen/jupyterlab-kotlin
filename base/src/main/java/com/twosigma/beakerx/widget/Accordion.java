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
package com.twosigma.beakerx.widget;

import java.util.List;

public class Accordion extends SelectionContainer {
  
  public static final String VIEW_NAME_VALUE = "AccordionView";
  public static final String MODEL_NAME_VALUE = "AccordionModel";


  public Accordion(List<Widget> children) {
    super(children);
    openComm();
  }
  
  public Accordion(List<Widget> children, List<String> labels) {
    super(children, labels);
    openComm();
    sendUpdate(TITLES, this.titles);
  }

  
  @Override
  public String getModelNameValue() {
    return MODEL_NAME_VALUE;
  }

  @Override
  public String getViewNameValue() {
    return VIEW_NAME_VALUE;
  }

  @Override
  public void updateValue(Object value) {
    // TODO Auto-generated method stub

  }

}