// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
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
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;

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
    PieData pieData=new PieData();
    pieData.setDataSet(pieDataSet);
    IPieDataSet ipds=pieData.getDataSet();
    ipds.addEntry(new PieEntry((float) ((Math.random() * 1) + 1 / 5),
            (float)(ipds.getEntryCount()))
    );
    pieData.setDataSet(ipds);
    pieChart.setData(pieData);
    pieChart.invalidate();
    Title("12345");
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
}
