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

import org.jfree.chart.JFreeChart;


public class JPEGChartModel extends ChartModel
{

   private float quality;

   public JPEGChartModel(JFreeChart chart, int width, int height, float quality)
   {
      super(chart, width, height);
      this.setQuality(quality);
   }

   @Override
   public String getFormat()
   {
      return "jpeg";
   }

   @Override
   public String getContentType()
   {
      return "image/jpeg";
   }

   public void setQuality(float quality)
   {
      this.quality = quality;
   }

   public float getQuality()
   {
      return quality;
   }

}
