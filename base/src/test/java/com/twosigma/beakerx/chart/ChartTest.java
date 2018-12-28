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

package com.twosigma.beakerx.chart;

import com.twosigma.beakerx.KernelTest;
import com.twosigma.beakerx.chart.actions.CategoryGraphicsActionObject;
import com.twosigma.beakerx.chart.legend.LegendLayout;
import com.twosigma.beakerx.chart.legend.LegendPosition;
import com.twosigma.beakerx.chart.serializer.LegendPositionSerializer;
import com.twosigma.beakerx.chart.xychart.XYChart;
import com.twosigma.beakerx.kernel.KernelManager;
import com.twosigma.beakerx.message.Message;
import com.twosigma.beakerx.widget.BeakerxPlot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.twosigma.beakerx.chart.Chart.PLOT_GRIDLINE;
import static com.twosigma.beakerx.chart.Chart.PLOT_LABEL;
import static com.twosigma.beakerx.chart.Chart.PLOT_LABEL_X;
import static com.twosigma.beakerx.chart.Chart.PLOT_LABEL_Y;
import static com.twosigma.beakerx.chart.Chart.PLOT_TITLE;
import static com.twosigma.beakerx.chart.serializer.ChartSerializer.CHART_TITLE;
import static com.twosigma.beakerx.chart.serializer.ChartSerializer.CUSTOM_STYLES;
import static com.twosigma.beakerx.chart.serializer.ChartSerializer.ELEMENT_STYLES;
import static com.twosigma.beakerx.chart.serializer.ChartSerializer.INIT_HEIGHT;
import static com.twosigma.beakerx.chart.serializer.ChartSerializer.INIT_WIDTH;
import static com.twosigma.beakerx.chart.serializer.ChartSerializer.LEGEND_LAYOUT;
import static com.twosigma.beakerx.chart.serializer.ChartSerializer.LEGEND_POSITION;
import static com.twosigma.beakerx.widget.TestWidgetUtils.getMessageUpdate;
import static com.twosigma.beakerx.widget.TestWidgetUtils.getValueForProperty;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ChartTest<T extends Chart> {

  protected KernelTest kernel;

  @Before
  public void setUp() throws Exception {
    kernel = new KernelTest();
    KernelManager.register(kernel);
  }

  @After
  public void tearDown() throws Exception {
    kernel.exit();
    KernelManager.register(null);
  }

  public abstract T createWidget();

  @Test
  public void shouldSendCommMsgWhenLegendPositionChange() throws Exception {
    //given
    Chart chart = createWidget();
    LegendPosition bottom = LegendPosition.BOTTOM;
    //when
    chart.setLegendPosition(bottom);
    //then
    assertThat(chart.getLegendPosition()).isEqualTo(bottom);
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    Map actual = (Map)model.get(LEGEND_POSITION);
    assertThat(actual.get(LegendPositionSerializer.TYPE)).isEqualTo(LegendPosition.class.getSimpleName());
    assertThat(actual.get(LegendPositionSerializer.POSITION)).isEqualTo(bottom.getPosition().toString());
  }

  @Test
  public void shouldSendCommMsgWhenLegendLayoutChange() throws Exception {
    //given
    Chart chart = createWidget();
    LegendLayout horizontal = LegendLayout.HORIZONTAL;
    //when
    chart.setLegendLayout(horizontal);
    //then
    assertThat(chart.getLegendLayout()).isEqualTo(horizontal);
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    String actual = (String)model.get(LEGEND_LAYOUT);
    assertThat(actual).isEqualTo(LegendLayout.HORIZONTAL.toString());
  }

  @Test
  public void shouldSendCommMsgWhenTitleChange() throws Exception {
    //given
    Chart chart = createWidget();
    String title = "title1";
    //when
    chart.setTitle(title);
    //then
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    assertThat(model.get(CHART_TITLE)).isEqualTo(title);
  }

  @Test
  public void createChartByEmptyConstructor_ChartHasInitHeightWidth() {
    //given
    Chart chart = createWidget();
    //when
    //then
    assertThat(chart.getInitHeight()).isGreaterThan(0);
    assertThat(chart.getInitWidth()).isGreaterThan(0);
  }

  @Test
  public void setCustomStyles_hasCustomStyles() {
    //given
    Chart chart = createWidget();
    List<String> customStyle = Arrays.asList("style1", "style2");
    //when
    chart.setCustomStyles(customStyle);
    //then
    assertThat(chart.getCustomStyles()).isNotEmpty();
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    List actual = (List)model.get(CUSTOM_STYLES);
    assertThat(actual).isEqualTo(customStyle);
  }

  @Test
  public void setGridLineStyle_hasGridLineStyle() {
    //given
    Chart chart = createWidget();
    String grid_style = "grid_style";
    //when
    chart.setGridLineStyle(grid_style);
    //then
    assertThat(chart.getGridLineStyle()).isEqualTo(grid_style);
    assertThat(chart.getElementStyles().get(PLOT_GRIDLINE)).isEqualTo(grid_style);
    Map actual = getElementStyles();
    assertThat(actual.get(PLOT_GRIDLINE)).isEqualTo(grid_style);
  }

  private Map getElementStyles() {
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    return (Map)model.get(ELEMENT_STYLES);
  }

  @Test
  public void setInitHeight_hasInitHeight() {
    //given
    Chart chart = createWidget();
    //when
    chart.setInitHeight(5);
    //then
    assertThat(chart.getInitHeight()).isEqualTo(5);
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    assertThat(model.get(INIT_HEIGHT)).isEqualTo(5);
  }

  @Test
  public void setInitWidth_hasInitWidth() {
    //given
    Chart chart = createWidget();
    //when
    chart.setInitWidth(10);
    //then
    assertThat(chart.getInitWidth()).isEqualTo(10);
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    assertThat(model.get(INIT_WIDTH)).isEqualTo(10);
  }

  @Test
  public void setLabelYStyle_hasLabelYStyle() {
    //given
    Chart chart = createWidget();
    String labely_style = "labely_style";
    //when
    chart.setLabelYStyle(labely_style);
    //then
    assertThat(chart.getLabelYStyle()).isEqualTo(labely_style);
    assertThat(chart.getElementStyles().get(PLOT_LABEL_Y)).isEqualTo(labely_style);
    Map actual = getElementStyles();
    assertThat(actual.get(PLOT_LABEL_Y)).isEqualTo(labely_style);
  }

  @Test
  public void setLabelXStyle_hasLabelXStyle() {
    //given
    Chart chart = createWidget();
    String labelx_style = "labelx_style";
    //when
    chart.setLabelXStyle(labelx_style);
    //then
    assertThat(chart.getLabelXStyle()).isEqualTo(labelx_style);
    assertThat(chart.getElementStyles().get(PLOT_LABEL_X)).isEqualTo(labelx_style);
    Map actual = getElementStyles();
    assertThat(actual.get(PLOT_LABEL_X)).isEqualTo(labelx_style);
  }

  @Test
  public void setLabelStyle_hasLabelStyle() {
    //given
    Chart chart = createWidget();
    String label_style = "label_style";
    //when
    chart.setLabelStyle(label_style);
    //then
    assertThat(chart.getLabelStyle()).isEqualTo(label_style);
    assertThat(chart.getElementStyles().get(PLOT_LABEL)).isEqualTo(label_style);
    Map actual = getElementStyles();
    assertThat(actual.get(PLOT_LABEL)).isEqualTo(label_style);
  }

  @Test
  public void setTitleStyle_hasTitleStyle() {
    //given
    Chart chart = createWidget();
    String style = "style";
    //when
    chart.setTitleStyle(style);
    //then
    assertThat(chart.getTitleStyle()).isEqualTo(style);
    assertThat(chart.getElementStyles().get(PLOT_TITLE)).isEqualTo(style);
    Map actual = getElementStyles();
    assertThat(actual.get(PLOT_TITLE)).isEqualTo(style);
  }

  @Test
  public void setDetails_hasDetails() {
    //given
    Chart chart = createWidget();
    CategoryGraphicsActionObject aObject = new CategoryGraphicsActionObject();
    //when
    chart.setDetails(aObject);
    //then
    assertThat(chart.getDetails()).isEqualTo(aObject);
  }

  @Test
  public void defaultChart_hasModelAndViewNameValues() {
    //given
    Chart chart = createWidget();
    //when
    //then
    assertThat(chart.getModelNameValue()).isEqualTo(BeakerxPlot.MODEL_NAME_VALUE);
    assertThat(chart.getViewNameValue()).isEqualTo(BeakerxPlot.VIEW_NAME_VALUE);
  }

  protected List getValueAsArray(final String field) {
    LinkedHashMap model = getModelUpdate();
    assertThat(model.size()).isEqualTo(1);
    return (List) model.get(field);
  }

  protected LinkedHashMap getModelUpdate() {
    Optional<Message> messageUpdate = getMessageUpdate(kernel);
    assertThat(messageUpdate).isPresent();
    return getModelUpdate(messageUpdate.get());
  }

  private LinkedHashMap getModelUpdate(Message message) {
    return getValueForProperty(message,  XYChart.MODEL_UPDATE, LinkedHashMap.class);
  }

}
