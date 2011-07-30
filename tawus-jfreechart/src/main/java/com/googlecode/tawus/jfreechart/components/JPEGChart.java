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

package com.googlecode.tawus.jfreechart.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.jfree.chart.JFreeChart;

import com.googlecode.tawus.jfreechart.ChartModel;
import com.googlecode.tawus.jfreechart.JPEGChartModel;
import com.googlecode.tawus.jfreechart.base.AbstractChart;

public class JPEGChart extends AbstractChart
{
   @Parameter(value = "0.9", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private float quality;

   @Override
   protected ChartModel createChart(JFreeChart chart, int width, int height)
   {
      return new JPEGChartModel(chart, width, height, quality);
   }
   
}
