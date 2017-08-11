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
import com.google.appinventor.components.runtime.util.YailList;

import gnu.mapping.Symbol;

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

    lineChart.setData(lineData);
    lineChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
    lineChart.getDescription().setEnabled(false);
    lineChart.setDrawBorders(false);
    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    lineChart.invalidate();
    // Default property values
  }

  @Override
  public View getView() {
    return lineChart;
  }

  
  
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
  public void SetColorList(YailList colors){
    Object[] list=colors.toArray();
    ArrayList<ILineDataSet> newDataSets = new ArrayList<ILineDataSet>();

    for(int i=0;i<entries.size();i++){
      List<Entry> dataset = entries.get(i);
      String label=dataSets.get(i).getLabel();
      LineDataSet lineDataSet=new LineDataSet(dataset,label);
      lineDataSet.setColor(gnu.math.IntNum.intValue(list[i]));
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
  public void SetXAxisValues(YailList list){
    //Toast.makeText(container.$context(), values.get(1), Toast.LENGTH_SHORT).show();
    final List<String> labels=new ArrayList<String>();
    for(Object obj : list.toArray()){
      labels.add(obj.toString());
    }
    IAxisValueFormatter formatter = new IAxisValueFormatter() {

      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        //return values.get(0).toString();
        if(value>=0&&labels.size()>(int)value&&labels.get((int)value)!=null)
          return labels.get((int)value).toString();
        else
          return String.valueOf(value);
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
  @SimpleProperty(userVisible=false)
  public void LegendEnabled(boolean enabled) {
    Legend legend=lineChart.getLegend();
    legend.setEnabled(enabled);
    lineChart.invalidate();
  }

  /**
   * Specifies whether the value of each entry is shown in the chart
   *
   * @param enable a boolean value 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(userVisible=false)
  public void DisplayValue(boolean enabled) {
    for(ILineDataSet dataset : lineChart.getData().getDataSets()){
      dataset.setDrawValues(enabled);
    }
  }

  /**
   * Specifies whether the chart can be scaled
   *
   * @param enable a boolean value 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(userVisible=false)
  public void ScaleEnabled(boolean enabled) {
    lineChart.setScaleEnabled(enabled);
  }

  /**
   * Specifies whether the chart can be dragged (on x and y axis)
   *
   * @param enable a boolean value 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(userVisible=false)
  public void DragEnabled(boolean enabled) {
    lineChart.setDragEnabled(enabled);
  }

  /**
   * If enabled, the background rectangle behind the chart
   * drawing-area will be drawn.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(userVisible=false)
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
  @SimpleProperty(userVisible=false)
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
