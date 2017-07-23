// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.client.editor.simple.components;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * LineChart component.
 *
 * @author ids@pku.edu.cn (Liancheng Jia)
 */
public final class MockLineChart extends MockVisibleComponent{

/**
 * Component type name.
 */
public static final String TYPE = "LineChart";
private static final int DEFAULT_WIDTH = 100;


/**
 * Creates a new MockLineChart component.
 *
 * @param editor  editor of source file the component belongs to
 */

//Widget for showing the mock component
 private InlineLabel labelWidget;


 /**
  * Creates a new MockLineChart component.
  *
  * @param editor editor of source file the component belongs to
  */
 public MockLineChart(SimpleEditor editor) {
   super(editor, TYPE, images.lineChart());

   // Initialize mock label UI
   labelWidget = new InlineLabel();
   labelWidget.setStylePrimaryName("ode-SimpleMockComponent");
   labelWidget.setText("your new LineChart");
   initComponent(labelWidget);
   refreshForm();
 }


 @Override
 protected boolean isPropertyVisible(String propertyName) {

   return super.isPropertyVisible(propertyName);
 }

 @Override
 public int getPreferredWidth() {
   // The superclass uses getOffsetWidth, which won't work for us.
   return DEFAULT_WIDTH;
 }

 // PropertyChangeListener implementation
 @Override
 public void onPropertyChange(String propertyName, String newValue) {
   super.onPropertyChange(propertyName, newValue);
   if(propertyName.equals(PROPERTY_NAME_CHART_TITLE)){
     setTitle(newValue);
   }
 }
 
 /*
  * set the title of the chart.
  */
  public void setTitle(String text){
    labelWidget.setText(text);
  }
}