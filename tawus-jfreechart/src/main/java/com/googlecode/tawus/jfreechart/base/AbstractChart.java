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

package com.googlecode.tawus.jfreechart.base;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.imagemap.StandardToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.ToolTipTagFragmentGenerator;
import org.jfree.chart.imagemap.URLTagFragmentGenerator;

import com.googlecode.tawus.jfreechart.ChartConstants;
import com.googlecode.tawus.jfreechart.ChartModel;
import com.googlecode.tawus.jfreechart.internal.DummyOutputStream;
import com.googlecode.tawus.jfreechart.services.ChartWriter;

/**
 * A JFreeChart chart
 */
@Import(library = "chart.js")
public abstract class AbstractChart implements ClientElement
{
   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String clientId;

   @Parameter(required = true, allowNull = false)
   private JFreeChart chart;

   @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private int width;

   @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private int height;

   @Parameter
   private Object[] context;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   @Parameter
   private ToolTipTagFragmentGenerator toolTipTagGenerator;

   @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
   private boolean useMap;

   ToolTipTagFragmentGenerator defaultToolTipTagGenerator()
   {
      return new StandardToolTipTagFragmentGenerator();
   }

   @Inject
   private ComponentResources resources;

   @Inject
   private JavaScriptSupport javaScriptSupport;

   @Inject
   private ChartWriter chartWriter;

   private String assignedClientId;

   private ChartModel internalChart;

   void setupRender()
   {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   boolean beginRender(MarkupWriter writer)
   {
      // Outer Div
      writer.element("div", "id", getClientId());

      // Write image tag
      writer.element("img", "src", getImageURL());

      // Add map if required
      if(useMap)
      {
         writer.attributes("useMap", "#" + getMapName());
      }
      writer.end(); // Close img tag

      String selectMapURL = getSelectMapURL();

      if(useMap)
      {
         createChart();
         initializeChart();

         writer.writeRaw(ChartUtilities.getImageMap(getMapName(),
               internalChart.getInfo(),
               toolTipTagGenerator,
               getURLTagGenerator(selectMapURL)));
      }

      writer.end();// Close Outer Div

      if(zone != null)
      {
         addJavaScript(selectMapURL);
      }
      
      return false;
   }

   private String getSelectMapURL()
   {
      return resources.createEventLink(ChartConstants.SELECT_MAP, context).toAbsoluteURI();
   }

   private URLTagFragmentGenerator getURLTagGenerator(final String url)
   {

      return new URLTagFragmentGenerator()
      {

         public String generateURLFragment(String text)
         {
            String[] parts = text.split("\\?");
            return String.format("href='%s?%s'", url, parts[1]);
         }

      };
   }

   private String getImageURL()
   {
      return resources.createEventLink(ChartConstants.SHOW_CHART, context).toAbsoluteURI();
   }

   private String getMapName()
   {
      return getClientId() + "_map";
   }

   public String getClientId()
   {
      return assignedClientId;
   }

   private void addJavaScript(String url)
   {

      JSONObject params = new JSONObject();
      params.put("zone", zone);
      params.put("id", getMapName());
      params.put("url", url);
      javaScriptSupport.addInitializerCall("mapToZone", params);
   }

   @OnEvent(ChartConstants.SHOW_CHART)
   Object showChart()
   {
      createChart();
      return internalChart;
   }

   private void createChart()
   {
      internalChart = createChart(chart, width, height);
   }

   private void initializeChart()
   {
      OutputStream out = new DummyOutputStream();
      try
      {
         chartWriter.writeChart(out, internalChart);
      }
      catch(IOException e)
      {
         throw new RuntimeException("Could not write chart : ", e);
      }
   }

   protected abstract ChartModel createChart(JFreeChart chart, int width, int height);

}
