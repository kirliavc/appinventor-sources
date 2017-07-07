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
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.TextViewUtil;


import android.view.View;
import android.widget.TextView;


@DesignerComponent(version = YaVersion.PIECHART_COMPONENT_VERSION,
    description = "A new component ",
    category = ComponentCategory.USERINTERFACE,
    nonVisible = false,
    iconName = "images/pieChart.png")
@SimpleObject
public final class PieChart extends AndroidViewComponent {

  // change to reflect the view of the component
  private final TextView view;
  private String title;

  /**
   * Creates a new component.
   *
   * @param container  container, component will be placed in
   */
  public PieChart(ComponentContainer container) {
    super(container);
    view = new TextView(container.$context());

    // Adds the component to its designated container
    container.$add(this);

    // Default property values
    TextViewUtil.setText(view, "your new PieChart");
  }

  @Override
  public View getView() {
    return view;
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
      return TextViewUtil.getText(view);
  }
  /**
   * Specifies the title of chart
   *
   * @param text  title of the chart
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void Text(String text) {
    TextViewUtil.setText(view, text);
  }
}
