// 
// Copyright 2011 Taha Hafeez Siddiqi
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// 

package com.googlecode.tawus.jfreechart.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.googlecode.tawus.jfreechart.JPEGChartModel;

public class ChartPage
{
   @Inject
   private ComponentResources resources;
   
   @SuppressWarnings("unused")
   @Property
   private int index;

   public JFreeChart getChart()
   {
      DefaultPieDataset pieDataset = new DefaultPieDataset();

      pieDataset.setValue("One", new Integer(10));
      pieDataset.setValue("Two", new Integer(20));
      pieDataset.setValue("Three", new Integer(30));
      pieDataset.setValue("Four", new Integer(10));
      pieDataset.setValue("Five", new Integer(20));
      pieDataset.setValue("Six", new Integer(10));

      return ChartFactory.createPieChart("Pie Chart using JFreeChart", pieDataset, true, true, true);
   }

   public String getMyLink()
   {
      return resources.createEventLink("showImage2").toAbsoluteURI();
   }

   Object onShowImage2()
   {
      return new JPEGChartModel(getChart(), 200, 200, 0.9f);
   }
}
