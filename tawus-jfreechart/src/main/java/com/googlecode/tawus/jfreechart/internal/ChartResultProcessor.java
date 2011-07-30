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

package com.googlecode.tawus.jfreechart.internal;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.Response;

import com.googlecode.tawus.jfreechart.ChartModel;
import com.googlecode.tawus.jfreechart.services.ChartWriter;

public class ChartResultProcessor implements ComponentEventResultProcessor<ChartModel>
{
   public Response response;
   private ChartWriter chartWriter;

   public ChartResultProcessor(Response response, ChartWriter chartWriter)
   {
      this.response = response;
      this.chartWriter = chartWriter;
   }

   public void processResultValue(ChartModel chartModel) throws IOException
   {
      response.disableCompression();
      
      OutputStream out = response.getOutputStream(chartModel.getContentType());
      chartWriter.writeChart(out, chartModel);
      
      response.setHeader("Pragma", "no-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
   }

}
