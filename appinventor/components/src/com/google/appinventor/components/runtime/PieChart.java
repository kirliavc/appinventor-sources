// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

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

//import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Legend;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import android.graphics.Color;

@DesignerComponent(version = YaVersion.PIECHART_COMPONENT_VERSION,
    description = "PieChart Component",
    category = ComponentCategory.USERINTERFACE,
    nonVisible = false,
    iconName = "images/pieChart.png")
@SimpleObject
@UsesLibraries(libraries =
    "MPAndroidChart-v3.0.1.jar")
public final class PieChart extends AndroidViewComponent {

  // change to reflect the view of the component
  //private final TextView view;
  private final com.github.mikephil.charting.charts.PieChart pieChart;
  private final ComponentContainer container;
  private ArrayList<Integer> colors;
  /**
   * Creates a new component.
   *
   * @param container  container, component will be placed in
   */
  public PieChart(ComponentContainer container) {
    super(container.$form());
    this.container = container;
    pieChart=new com.github.mikephil.charting.charts.PieChart(container.$context());
    container.$add(this);
    
    //view = new TextView(container.$context());

    // Adds the component to its designated container
    
    PieDataSet pieDataSet=new PieDataSet(new ArrayList<PieEntry>(),"");
    colors = new ArrayList<Integer>();

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

    colors.add(ColorTemplate.getHoloBlue());
    pieDataSet.setColors(colors);
    PieData pieData=new PieData();
    pieData.setDataSet(pieDataSet);
    pieData.setValueTextSize(11f);
    pieData.setValueTextColor(Color.WHITE);
    /*
    IPieDataSet ipds=pieData.getDataSet();
    ipds.addEntry(new PieEntry((float) ((Math.random() * 1) + 1 / 5),
            (float)(ipds.getEntryCount()))
    );
    

    
    pieData.setDataSet(ipds);
    */
    pieChart.setData(pieData);
    pieChart.setDrawHoleEnabled(false);
    pieChart.invalidate();
    pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
    Toast.makeText(container.$context(), Title(), Toast.LENGTH_SHORT).show();
    // Default property values
  }

  @Override
  public View getView() {
    return pieChart;
  }

  /*
   * return the title of chart
   * @return the title of chart
   * 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
  defaultValue = ""
  ) 
  @SimpleProperty
  public String Title()
  {
    //return "";
    return pieChart.getData().getDataSet().getLabel();
  }
  /**
   * Specifies the title of chart
   *
   * @param text  title of the chart
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void Title(String text) {
    pieChart.getData().getDataSet().setLabel(text);
  }

  @SimpleFunction(description = "Add an entry to the chart")
  public void AddEntry(float value,String label){
    IPieDataSet iPieDataSet = pieChart.getData().getDataSet();
    iPieDataSet.addEntry(new PieEntry(value,label));
    pieChart.getData().notifyDataChanged();
    pieChart.notifyDataSetChanged();
    pieChart.invalidate();
  }

  /**
   * Specifies whether the label of entries is shown
   *
   * @param enable a boolean value 
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty
  public void LabelEnabled(boolean enabled) {
    pieChart.setDrawEntryLabels(enabled);
    pieChart.invalidate();
  }
  /**
   * returns whether the label is shown in chart
   *
   * @return a boolean balue
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty
  public boolean LabelEnabled() {
    return pieChart.isDrawEntryLabelsEnabled();
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
    Legend legend=pieChart.getLegend();
    legend.setEnabled(enabled);
    pieChart.invalidate();
  }
  /**
   * returns whether the label is shown in chart
   *
   * @return a boolean balue
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty
  public boolean LegendEnabled() {
    return pieChart.getLegend().isEnabled();
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
    pieChart.getData().setValueTextColor(argb);
  }

  /**
   * Specifies the label's text color of the chart as an 
   * alpha-red-green-blue integer.
   *
   * @param argb  text RGB color with alpha
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR,
      defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
  @SimpleProperty
  public void LabelTextColor(int argb) {
    pieChart.setEntryLabelColor(argb);
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
    pieChart.getData().setValueTextSize(size);
  }

  /**
   * Specifies the label text's size, measured in sp
   * (scale-independent pixels).
   *
   * @param size  font size in sp(scale-independent pixels)
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT,
      defaultValue = Component.FONT_DEFAULT_SIZE + "")
  @SimpleProperty(
      category = PropertyCategory.APPEARANCE)
  public void LabelTextSize(float size) {
    pieChart.setEntryLabelTextSize(size);
  }
}
