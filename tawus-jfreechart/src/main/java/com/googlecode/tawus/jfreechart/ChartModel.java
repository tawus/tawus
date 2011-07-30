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

package com.googlecode.tawus.jfreechart;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;

public abstract class ChartModel
{
   private JFreeChart chart;
   
   private int width;
   
   private int height;
   
   private ChartRenderingInfo info;
   
   public ChartModel(JFreeChart chart, int width, int height)
   {
      this.chart = chart;
      this.width = width;
      this.height = height;
      info = new ChartRenderingInfo(new StandardEntityCollection());
   }
   
   public JFreeChart getChart()
   {
      return chart;
   }
   
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }
   
   public ChartRenderingInfo getInfo()
   {
      return info;
   }

   public abstract String getFormat();

   public abstract String getContentType();
   
}
