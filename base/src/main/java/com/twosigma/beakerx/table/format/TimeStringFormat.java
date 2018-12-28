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

public class TimeStringFormat extends TableDisplayStringFormat {
  private TimeUnit unit = TimeUnit.MILLISECONDS;
  private boolean humanFriendly;

  public TimeStringFormat() {
  }

  public TimeStringFormat(TimeUnit unit, boolean humanFriendly) {
    this.unit = unit;
    this.humanFriendly = humanFriendly;
  }

  public TimeStringFormat(TimeUnit unit) {
    this.unit = unit;
  }

  public TimeStringFormat(boolean humanFriendly) {
    this.humanFriendly = humanFriendly;
  }

  public TimeUnit getUnit() {
    return unit;
  }

  public Boolean getHumanFriendly() {
    return humanFriendly;
  }
}
