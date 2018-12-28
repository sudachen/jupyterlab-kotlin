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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twosigma.beakerx.chart.legend.LegendLayout;
import com.twosigma.beakerx.chart.legend.LegendPosition;

import static com.twosigma.beakerx.widget.BeakerxPlot.MODEL_NAME_VALUE;
import static com.twosigma.beakerx.widget.BeakerxPlot.VIEW_NAME_VALUE;

public abstract class Chart extends ChartDetails {

  public static final String PLOT_GRIDLINE = ".plot-gridline";
  public static final String PLOT_LABEL_Y = ".plot-label-y";
  public static final String PLOT_LABEL_X = ".plot-label-x";
  public static final String PLOT_LABEL = ".plot-label";
  public static final String PLOT_TITLE = ".plot-title";

  private int initWidth = 640;
  private int initHeight = 480;
  private List<String> customStyles = new ArrayList();
  private Map<String, String> elementStyles = new HashMap<>();

  private String title;
  private Boolean showLegend;
  private boolean useToolTip = true;
  private LegendPosition legendPosition = new LegendPosition(LegendPosition.Position.TOP_RIGHT);
  private LegendLayout legendLayout = LegendLayout.VERTICAL;

  public Chart setInitWidth(int w) {
    this.initWidth = w;
    sendModelUpdate(ChartToJson.serializeInitWidth(this.initWidth));
    return this;
  }

  public Integer getInitWidth() {
    return this.initWidth;
  }

  public Chart setInitHeight(int h) {
    this.initHeight = h;
    sendModelUpdate(ChartToJson.serializeInitHeight(this.initHeight));
    return this;
  }

  public Integer getInitHeight() {
    return this.initHeight;
  }

  public Chart setTitle(String title) {
    this.title = title;
    sendModelUpdate(ChartToJson.serializeTitle(this.title));
    return this;
  }

  public String getTitle() {
    return this.title;
  }

  public Chart setShowLegend(Boolean showLegend) {
    this.showLegend = showLegend;
    return this;
  }

  public Boolean getShowLegend() {
    return this.showLegend;
  }

  public Chart setUseToolTip(boolean useToolTip) {
    this.useToolTip = useToolTip;
    return this;
  }

  public Boolean getUseToolTip() {
    return this.useToolTip;
  }

  public LegendPosition getLegendPosition() {
    return legendPosition;
  }

  public Chart setLegendPosition(LegendPosition legendPosition) {
    this.legendPosition = legendPosition;
    sendModelUpdate(ChartToJson.serializeLegendPosition(this.legendPosition));
    return this;
  }

  public LegendLayout getLegendLayout() {
    return legendLayout;
  }

  public Chart setLegendLayout(LegendLayout legendLayout) {
    this.legendLayout = legendLayout;
    sendModelUpdate(ChartToJson.serializeLegendLayout(this.legendLayout));
    return this;
  }

  public List<String> getCustomStyles() {
    return customStyles;
  }

  public void setCustomStyles(List<String> customStyle) {
    this.customStyles = customStyle;
    sendModelUpdate(ChartToJson.serializeCustomStyles(this.customStyles));
  }

  public String getLabelStyle() {
    return this.elementStyles.get(PLOT_LABEL);
  }

  public void setLabelStyle(String style) {
    this.elementStyles.put(PLOT_LABEL, style);
    sendModelUpdate(ChartToJson.serializeElementStyles(this.elementStyles));
  }

  public String getLabelXStyle() {
    return this.elementStyles.get(PLOT_LABEL_X);
  }

  public void setLabelXStyle(String style) {
    this.elementStyles.put(PLOT_LABEL_X, style);
    sendModelUpdate(ChartToJson.serializeElementStyles(this.elementStyles));
  }

  public String getLabelYStyle() {
    return this.elementStyles.get(PLOT_LABEL_Y);
  }

  public void setLabelYStyle(String style) {
    this.elementStyles.put(PLOT_LABEL_Y, style);
    sendModelUpdate(ChartToJson.serializeElementStyles(this.elementStyles));
  }

  public String getGridLineStyle() {
    return this.elementStyles.get(PLOT_GRIDLINE);
  }

  public void setGridLineStyle(String style) {
    this.elementStyles.put(PLOT_GRIDLINE, style);
    sendModelUpdate(ChartToJson.serializeElementStyles(this.elementStyles));
  }

  public String getTitleStyle() {
    return this.elementStyles.get(PLOT_TITLE);
  }

  public void setTitleStyle(String style) {
    this.elementStyles.put(PLOT_TITLE, style);
    sendModelUpdate(ChartToJson.serializeElementStyles(this.elementStyles));
  }

  public Map<String, String> getElementStyles() {
    return this.elementStyles;
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
  protected Map serializeToJsonObject() {
    return ChartToJson.toJson(this);
  }

  @Override
  protected Map serializeToJsonObject(Object item) {
    return ChartToJson.toJson(item);
  }
}
