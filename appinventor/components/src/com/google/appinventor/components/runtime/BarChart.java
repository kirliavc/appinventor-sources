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

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.components.AxisBase;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import com.google.appinventor.components.runtime.util.YailList;
import gnu.mapping.Symbol;
@DesignerComponent(version = YaVersion.PIECHART_COMPONENT_VERSION,
    description = "BarChart Component",
    category = ComponentCategory.USERINTERFACE,
    nonVisible = false,
    iconName = "images/barChart.png")
@SimpleObject
@UsesLibraries(libraries =
    "MPAndroidChart-v3.0.1.jar")
public final class BarChart extends AndroidViewComponent {

  // change to reflect the view of the component
  //private final TextView view;
  private final com.github.mikephil.charting.charts.BarChart barChart;
  private final ComponentContainer container;
  private List<Integer> colors=new ArrayList<Integer>();
  private List< List<BarEntry> > entries;
  private float fontSize=11f;
  /**
   * Creates a new component.
   *
   * @param container  container, component will be placed in
   */
  public BarChart(ComponentContainer container) {
    super(container.$form());
    this.container = container;
    barChart=new com.github.mikephil.charting.charts.BarChart(container.$context());
    container.$add(this);

    entries = new ArrayList< List<BarEntry> >();
    addColorTemplates(colors);
    
    BarData barData=new BarData();
    barChart.setData(barData);
    barChart.setFitBars(true);
    barChart.getDescription().setEnabled(false);
    barChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    barChart.getXAxis().setCenterAxisLabels(true);
    barChart.invalidate();
  }

  @Override
  public View getView() {
    return barChart;
  }

  /**
   * Add a dataset to the chart
   *
   * @param label label text of the dataset
   */
  @SimpleFunction(description = "Add a dataset to the bar chart")
  public void AddDataSet(String label){
    List<BarEntry> entryList=new ArrayList<BarEntry>();
    entries.add(entryList);
    BarDataSet dataSet=new BarDataSet(entryList,label);
    barChart.getData().addDataSet(dataSet);
    barChart.notifyDataSetChanged();
    adjust();
  }

  /**
   * Add an entry to the chart
   *
   * @param index the index of datasets
   * @param yVal the value of this entry
   */
  @SimpleFunction(description = "Add an entry to the chart")
  public void AddEntry(int index, float yVal){
    List<BarEntry> list=entries.get(index);
    BarEntry entry=new BarEntry(list.size()+1,yVal);
    //list.add(entry);
    barChart.getData().getDataSetByIndex(index).addEntry(entry);
    barChart.getData().notifyDataChanged();
    barChart.notifyDataSetChanged();
    barChart.invalidate();
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(userVisible=false)
  public void DrawVerticalAxisLine(boolean enabled) {
    XAxis xaxis=barChart.getXAxis();
    xaxis.setDrawGridLines(enabled);
    barChart.invalidate();
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(userVisible=false)
  public void DrawHorizontalAxisLine(boolean enabled) {
    barChart.getAxisLeft().setDrawGridLines(enabled);
    barChart.getAxisRight().setDrawGridLines(enabled);
    barChart.invalidate();
  }


  /**
   * Set colors of the chart, not supported!
   *
   * @param colors a list of colors (integer values).
   */
  @SimpleFunction(description = "set the color list of the bar chart")
  public void SetColorList(YailList colors){
    Object[] list=colors.toArray();
    ArrayList<IBarDataSet> newDataSets = new ArrayList<IBarDataSet>();

    for(int i=0;i<entries.size();i++){
      List<BarEntry> dataset = entries.get(i);
      String label=barChart.getData().getDataSets().get(i).getLabel();
      BarDataSet barDataSet=new BarDataSet(dataset,label);
      barDataSet.setColor(gnu.math.IntNum.intValue(list[i]));
      newDataSets.add(barDataSet);
    }
    BarData barData=new BarData(newDataSets);
    //lineData.setValueTextSize(11f);
    barChart.setData(barData);
    barChart.invalidate();

    
    adjust();
  }
  /*
  **
   * Set x axis text of each value in the chart
   *
   * @param values a list of string
   */
  @SimpleFunction(description = "set the x axis text of each integer value in the chart(0,1,2...)")
  public void SetXAxisValues(YailList list){
    final List<String> labels=new ArrayList<String>();
    for(Object obj : list.toArray()){
      labels.add(obj.toString());
    }
    IAxisValueFormatter formatter = new IAxisValueFormatter() {

      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        //value=value/2;
        if(value>=0&&labels.size()>(int)value&&labels.get((int)value)!=null)
          return labels.get((int)value).toString();
        else
          return String.valueOf(value);
      }
    };
    barChart.getXAxis().setValueFormatter(formatter);
    barChart.getXAxis().setGranularity(1f);
    
  }
  /**
   * Specifies whether the value number of entries is shown
   *
   * @param enable a boolean value 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(userVisible=false)
  public void DisplayValue(boolean enabled) {
    barChart.getData().setDrawValues(enabled);
    barChart.invalidate();
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT,
      defaultValue = "11f")
  @SimpleProperty(userVisible=false)
  public void FontSize(float size){
    fontSize=size;
    Legend legend=barChart.getLegend();
    legend.setTextSize(size);
    barChart.getXAxis().setTextSize(size);
    barChart.getAxisLeft().setTextSize(size);
    barChart.getAxisRight().setTextSize(size);
    for(IBarDataSet dataSet : barChart.getData().getDataSets()){
      dataSet.setValueTextSize(size);
    }
    barChart.notifyDataSetChanged();
    barChart.invalidate();
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
    Legend legend=barChart.getLegend();
    legend.setEnabled(enabled);
    barChart.invalidate();
  }
  

  

  /**
   * Specifies the value's text color of the chart as an 
   * alpha-red-green-blue integer.
   *
   * @param argb  text RGB color with alpha
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR,
      defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
  @SimpleProperty(userVisible=false)
  public void ValueTextColor(int argb) {
    barChart.getData().setValueTextColor(argb);
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

  private void adjust(){
    float groupSpace=0.15f;
    float barSpace=(1f-groupSpace)/entries.size()/10f;
    float barWidth=barSpace*9;
    if(entries.size()>=2){
      barChart.getBarData().setBarWidth(barWidth);
      barChart.groupBars(0f,groupSpace,barSpace);
      barChart.invalidate();
    }
    FontSize(fontSize);
  }
}
