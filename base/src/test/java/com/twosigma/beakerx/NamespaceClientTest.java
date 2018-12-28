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

package com.twosigma.beakerx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twosigma.beakerx.kernel.KernelManager;
import com.twosigma.beakerx.kernel.comm.Comm;
import com.twosigma.beakerx.kernel.comm.TargetNamesEnum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class NamespaceClientTest {

  private NamespaceClient namespaceClient;
  private KernelTest kernel;

  @Before
  public void setUp() {
    CommRepository commRepository = new BeakerXCommRepositoryMock();
    namespaceClient = new NamespaceClient(new AutotranslationServiceTestImpl(), new DefaultBeakerXJsonSerializer(), commRepository);
    kernel = new KernelTest(commRepository);
    KernelManager.register(kernel);
    commRepository.addComm("commId", new Comm(TargetNamesEnum.BEAKER_AUTOTRANSLATION));
  }

  @After
  public void tearDown() throws Exception {
    kernel.exit();
    KernelManager.register(null);
  }

  @Test
  public void setData_returnValue() throws Exception {
    //given
    //when
    Object value = namespaceClient.set("x", new Integer(10));
    //then
    assertThat(value).isNotNull();
    assertThat(value).isEqualTo(new Integer(10));
  }

  @Test
  public void setData_setAutotranslationData() throws Exception {
    //given
    //when
    namespaceClient.set("x", new Integer(10));
    //then
    assertThat(kernel.getPublishedMessages()).isNotEmpty();
    Map data = (Map) kernel.getPublishedMessages().get(1).getContent().get("data");
    Map state = (Map) data.get("state");
    assertThat(state.get("name")).isEqualTo("x");
    assertThat(state.get("value")).isEqualTo("10");
    assertThat(state.get("sync")).isEqualTo(Boolean.TRUE);
  }

  @Test
  public void setBigInt() throws Exception {
    //given
    long millis = new Date().getTime();
    long nanos = millis * 1000 * 1000L;

    List<Map<String, String>> table = asList(
            new HashMap<String, String>() {{
              put("time", (nanos + 7 * 1) + "");
              put("next_time", ((nanos + 77) * 1) + "");
              put("temp", 14.6 + "");
            }},
            new HashMap<String, String>() {{
              put("time", (nanos + 7 * 1) + "");
              put("next_time", ((nanos + 88) * 2) + "");
              put("temp", 18.1 + "");
            }},
            new HashMap<String, String>() {{
              put("time", (nanos + 7 * 1) + "");
              put("next_time", ((nanos + 99) * 3) + "");
              put("temp", 23.6 + "");
            }}
    );

    //when
    namespaceClient.set("table_with_longs", table);
    //then
    assertThat(kernel.getPublishedMessages()).isNotEmpty();
    Map data = (Map) kernel.getPublishedMessages().get(2).getContent().get("data");
    Map state = (Map) data.get("state");
    assertThat(state.get("name")).isEqualTo("table_with_longs");
    assertThat(isJSONValid(state.get("value"))).isTrue();
  }

  @Test
  public void setData_sendCommMessage() throws Exception {
    //given
    //when
    namespaceClient.set("x", new Integer(10));
    //then
    assertThat(kernel.getPublishedMessages()).isNotEmpty();
  }

  public static boolean isJSONValid(Object jsonInString) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree((String) jsonInString);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public static class AutotranslationServiceTestImpl implements AutotranslationService {

    private ConcurrentMap<String, String> beakerx = new ConcurrentHashMap();

    @Override
    public String update(String name, String json) {
      return beakerx.put(name, json);
    }

    @Override
    public String get(String name) {
      return beakerx.get(name);
    }

    @Override
    public String close() {
      return null;
    }

    @Override
    public String getContextAsString() {
      return null;
    }
  }
}
