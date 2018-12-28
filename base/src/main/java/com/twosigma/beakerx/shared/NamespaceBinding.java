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

package com.twosigma.beakerx.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class NamespaceBinding {

  private String name;
  private String session;
  private Object value;
  private Boolean defined;
  private String error;

  public String getName() {
    return this.name;
  }
  public String getSession() {
    return this.session;
  }
  public Object getValue() {
    return this.value;
  }
  public Boolean getDefined() {
    return this.defined;
  }
  public String getError() { return error; }
  public void setName(String s) {
    this.name = s;
  }
  public void setSession(String s) {
    this.session = s;
  }
  public void setValue(Object o) {
    this.value = o;
  }
  public void setDefined(Boolean b) {
    this.defined = b;
  }
  public void setError(String error) { this.error = error; }

  public NamespaceBinding() {
  }
  public NamespaceBinding(String session, String name, Object value, Boolean defined) {
    this.session = session;
    this.name = name;
    this.value = value;
    this.defined = defined;
  }
  public NamespaceBinding(String session, String error) {
    this.session = session;
    this.error = error;
  }
}
