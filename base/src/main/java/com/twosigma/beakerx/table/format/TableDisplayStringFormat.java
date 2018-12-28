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
package com.twosigma.beakerx.table.format;

import java.util.concurrent.TimeUnit;

public class TableDisplayStringFormat {

  // Get a formatter that works with BigDecimal, double, and float
  public static TableDisplayStringFormat getDecimalFormat(int minDecimals, int maxDecimals) {
    return new DecimalStringFormat(minDecimals, maxDecimals);
  }

  // Get a formatter that shows strings as formatted HTML
  public static TableDisplayStringFormat getHTMLFormat() {
    return new HTMLStringFormat();
  }

  // Get a formatter that shows strings as formatted HTML with specified width
  public static TableDisplayStringFormat getHTMLFormat(int width) {
    return new HTMLStringFormat(width);
  }

  // Get a formatter that will show Date in a timestamp format with millisecond precision
  public static TableDisplayStringFormat getTimeFormat() {
    return new TimeStringFormat();
  }

  // Get a formatter that will show Date in a timestamp format with millisecond precision
  public static TableDisplayStringFormat getTimeFormat(boolean humanFriendly) {
    return new TimeStringFormat(humanFriendly);
  }

  // Get a formatter that will show Date in a timestamp format with the specified precision
  public static TableDisplayStringFormat getTimeFormat(TimeUnit unit) {
    return new TimeStringFormat(unit);
  }

  // Get a formatter that will show Date in a timestamp format with the specified precision
  public static TableDisplayStringFormat getTimeFormat(TimeUnit unit, boolean humanFriendly) {
    return new TimeStringFormat(unit, humanFriendly);
  }

  // Get a formatter that will show derived source as an image
  public static TableDisplayStringFormat getImageFormat() {
    return new ImageStringFormat();
  }

  // Get a formatter that will show derived source as an image with specified width
  public static TableDisplayStringFormat getImageFormat(int width) {
    return new ImageStringFormat(width);
  }

}
