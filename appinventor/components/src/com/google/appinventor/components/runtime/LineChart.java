// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.TextViewUtil;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;

@DesignerComponent(version = YaVersion.LINECHART_COMPONENT_VERSION,
    description = "Line Chart Component",
    category = ComponentCategory.USERINTERFACE,
    nonVisible = false,
    iconName = "images/lineChart.png")
@SimpleObject
@UsesLibraries(libraries =
    "MPAndroidChart-v3.0.1.jar")
public final class LineChart extends AndroidViewComponent {

  // change to reflect the view of the component
  //private final TextView view;
  private final com.github.mikephil.charting.charts.LineChart lineChart;
  private final ComponentContainer container;
  private List<Integer> colors;
  private List<ILineDataSet> dataSets;
  private List< List<Entry> > entries;
  private float fontSize;
  /**
   * Creates a new component.
   *
   * @param container  container, component will be placed in
   */
  public LineChart(ComponentContainer container) {
    super(container.$form());
    this.container = container;
    lineChart=new com.github.mikephil.charting.charts.LineChart(container.$context());
    container.$add(this);
    
    
    colors = new ArrayList<Integer>();
    entries = new ArrayList< List<Entry> >();
    addColorTemplates(colors);
    
    // initialize an empty lineDataSets arraylist.
    dataSets = new ArrayList<ILineDataSet>();
    LineData lineData=new LineData(dataSets);
    lineData.setValueTextSize(11f);
    lineData.setValueTextColor(Color.BLACK);

    lineChart.setData(lineData);
    lineChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
    lineChart.getDescription().setEnabled(false);
    lineChart.setDrawBorders(false);
    lineChart.invalidate();
    // Default property values
  }

  @Override
  public View getView() {
    return lineChart;
  }

  /*
   * return the title of chart
   * @return the title of chart
   * 
   
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
  defaultValue = ""
  ) 
  @SimpleProperty
  public String DescriptionText()
  {
    return lineChart.getDescription().getText();
  }
  /**
   * Specifies the title of chart
   * NO API PROVIDED. Don't do anything now.
   * probably delete later.
   * @param text  title of the chart
   
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void Title(String text) {
    lineChart.getDescription().setText(text);
  }
  */
  /**
   * Add an empty dataset to the line chart
   * @param label label of the dataset
   */
  @SimpleFunction(description = "Add an empty dataset to chart")
  public void AddDataSet(String label){
    List<Entry> dataset=new ArrayList<Entry>();
    entries.add(dataset);
    lineChart.getData().addDataSet(new LineDataSet(dataset,label));
    lineChart.invalidate();
  }
  /**
   * Add an entry to the chart
   *
   * @param index the index of dataset
   * @param xVal the x value of point
   * @param yVal the y value of point
   */
  @SimpleFunction(description = "Add an entry to the chart")
  public void AddEntry(int index, float xVal, float yVal){
    entries.get(index).add(new Entry(xVal,yVal));
    lineChart.getData().addEntry(new Entry(xVal,yVal),index);
    lineChart.getData().notifyDataChanged();
    lineChart.notifyDataSetChanged();
    lineChart.invalidate();
  }

  /**
   * Set colors of the chart
   *
   * @param colors a list of colors (integer values).
   */
  @SimpleFunction(description = "set colors of each dataset in the chart")
  public void SetColorList(List<Integer> colors){
    ArrayList<ILineDataSet> newDataSets = new ArrayList<ILineDataSet>();

    for(int i=0;i<entries.size();i++){
      List<Entry> dataset = entries.get(i);
      String label=dataSets.get(i).getLabel();
      LineDataSet lineDataSet=new LineDataSet(dataset,label);
      lineDataSet.setColor(colors.get(i));
      newDataSets.add(lineDataSet);
    }
    LineData lineData=new LineData(newDataSets);
    lineChart.setData(lineData);
    lineChart.invalidate();
    dataSets=newDataSets;
    SetFontSize(fontSize);
  }

  /**
   * Set x axis text of each value in the chart
   *
   * @param values a list of string
   */
  @SimpleFunction(description = "set the x axis text of each integer value in the chart(0,1,2...)")
  public void SetXAxisValues(final List<String> values){
    IAxisValueFormatter formatter = new IAxisValueFormatter() {

      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        return values.get((int)value);
      }
    };
    lineChart.getXAxis().setValueFormatter(formatter);
    lineChart.getXAxis().setGranularity(1f);
  }
  /**
   * Specifies whether the legend is shown
   *
   * @param enable a boolean value 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty
  public void LegendEnabled(boolean enabled) {
    Legend legend=lineChart.getLegend();
    legend.setEnabled(enabled);
    lineChart.invalidate();
  }


  /**
   * If enabled, the background rectangle behind the chart
   * drawing-area will be drawn.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty
  public void DrawGridBackground(boolean enabled) {
    lineChart.setDrawGridBackground(enabled);
    lineChart.invalidate();
  }

  /**
   * If enabled, axis grid lines will be drawn
   * 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty
  public void DrawAxisLine(boolean enabled) {
    XAxis xaxis=lineChart.getXAxis();
    xaxis.setDrawGridLines(enabled);
    lineChart.invalidate();
  }

  /**
   * Set the font size of every text in the chart
   *
   * @param size font size
   */
  @SimpleFunction(description = "set size of text in the chart")
  public void SetFontSize(float size){
    fontSize=size;
    Legend legend=lineChart.getLegend();
    legend.setTextSize(size);
    lineChart.getXAxis().setTextSize(size);
    lineChart.getAxisLeft().setTextSize(size);
    lineChart.getAxisRight().setTextSize(size);
    for(ILineDataSet lineDataSet : lineChart.getData().getDataSets()){
      lineDataSet.setValueTextSize(size);
    }
    lineChart.notifyDataSetChanged();
    lineChart.invalidate();
  }
  /**
   * Specifies the value's text color of the chart as an 
   * alpha-red-green-blue integer.
   *
   * @param argb  text RGB color with alpha
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR,
      defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
  @SimpleProperty
  public void ValueTextColor(int argb) {
    lineChart.getData().setValueTextColor(argb);
  }



  /**
   * Specifies the value text's size, measured in sp
   * (scale-independent pixels).
   *
   * @param size  font size in sp(scale-independent pixels)
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT,
      defaultValue = Component.FONT_DEFAULT_SIZE + "")
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE)
  public void ValueTextSize(float size) {
    lineChart.getData().setValueTextSize(size);
  }



  private void addColorTemplates(List<Integer> colors){
    if (colors == null)
      colors=new ArrayList<Integer>();
    for (int c : ColorTemplate.VORDIPLOM_COLORS)
        colors.add(c);

    for (int c : ColorTemplate.JOYFUL_COLORS)
        colors.add(c);

    for (int c : ColorTemplate.COLORFUL_COLORS)
        colors.add(c);

    for (int c : ColorTemplate.LIBERTY_COLORS)
        colors.add(c);

    for (int c : ColorTemplate.PASTEL_COLORS)
        colors.add(c);
  }
}
