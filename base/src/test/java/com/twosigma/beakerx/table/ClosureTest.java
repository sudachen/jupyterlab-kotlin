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

package com.twosigma.beakerx.table;

public abstract class ClosureTest {

  public Object call(Object arg1) {
    return null;
  }

  public Object call(Object arg1, Object arg2) {
    return null;
  }

  public Object call(Object arg1, Object arg2, Object arg3) {
    return null;
  }

  public Object call(Object arg1, Object arg2, Object arg3, Object arg4) {
    return null;
  }

  public abstract int getMaximumNumberOfParameters();
}
