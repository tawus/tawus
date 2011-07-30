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

package com.googlecode.tawus.jfreechart.services;

import java.util.Map;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.StrategyBuilder;
import org.apache.tapestry5.services.ComponentEventResultProcessor;

import com.googlecode.tawus.jfreechart.ChartModel;
import com.googlecode.tawus.jfreechart.JPEGChartModel;
import com.googlecode.tawus.jfreechart.PNGChartModel;
import com.googlecode.tawus.jfreechart.internal.ChartResultProcessor;
import com.googlecode.tawus.jfreechart.internal.services.ChartWriterImpl;
import com.googlecode.tawus.jfreechart.internal.services.JPEGChartRenderer;
import com.googlecode.tawus.jfreechart.internal.services.PNGChartRenderer;

public class ChartModule
{
   public static void bind(ServiceBinder binder)
   {
      binder.bind(ChartWriter.class, ChartWriterImpl.class);
   }

   public ChartRenderer buildChartRender(StrategyBuilder builder,
         @SuppressWarnings("rawtypes") Map<Class, ChartRenderer> chartRenderers)
   {
      return builder.build(ChartRenderer.class, chartRenderers);
   }

   @Contribute(ComponentEventResultProcessor.class)
   public void provideResultProcessors(
         @SuppressWarnings("rawtypes") MappedConfiguration<Class,ComponentEventResultProcessor> configuration)
   {
      configuration.addInstance(ChartModel.class, ChartResultProcessor.class); 
   }
   
   @Contribute(ChartRenderer.class)
   public void provideChartRenderers(
         @SuppressWarnings("rawtypes") MappedConfiguration<Class, ChartRenderer> configuration)
   {
      configuration.addInstance(JPEGChartModel.class, JPEGChartRenderer.class);
      configuration.addInstance(PNGChartModel.class, PNGChartRenderer.class);
   }
}
