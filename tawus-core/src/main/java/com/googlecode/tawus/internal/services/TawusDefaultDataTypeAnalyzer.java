package com.googlecode.tawus.internal.services;

import java.util.Map;

import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.apache.tapestry5.services.DataTypeAnalyzer;

public class TawusDefaultDataTypeAnalyzer implements DataTypeAnalyzer {
   private StrategyRegistry<String> strategyRegistry;

   public TawusDefaultDataTypeAnalyzer(
         @SuppressWarnings("rawtypes") Map<Class, String> configuration) {
      strategyRegistry = StrategyRegistry.newInstance(String.class,
            configuration);
   }

   public String identifyDataType(PropertyAdapter propertyAdapter) {
      @SuppressWarnings("rawtypes")
      Class propertyType = propertyAdapter.getType();
      String dataType = strategyRegistry.get(propertyType);

      if (dataType.equals("")) {
         return null;
      }

      return dataType;
   }

}
