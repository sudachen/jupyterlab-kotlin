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

package com.twosigma.beakerx.chart.xychart.plotitem;

import com.twosigma.beakerx.chart.Color;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * Crosshair
 *
 */
public class Crosshair implements Serializable{
  private StrokeType style;
  private Float width;
  private Color color;

  public Crosshair setStyle(StrokeType style) {
    this.style = style;
    return this;
  }

  public StrokeType getStyle() {
    return this.style;
  }

  public Crosshair setWidth(float width) {
    this.width = width;
    return this;
  }

  public Float getWidth() {
    return this.width;
  }

  public Crosshair setColor(Color color) {
    this.color = color;
    return this;
  }

  public Crosshair setColor(java.awt.Color color) {
    return setColor(new Color(color));
  }

  public Color getColor() {
    return this.color;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return SerializationUtils.clone(this);
  }
}
