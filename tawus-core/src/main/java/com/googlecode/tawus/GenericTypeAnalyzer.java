package com.googlecode.tawus;

import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;

public class GenericTypeAnalyzer implements DataTypeAnalyzer {

   private Class<?> type;
   private String dataType;

   public GenericTypeAnalyzer(Class<?> type, String dataType) {
      this.type = type;
      this.dataType = dataType;
   }

   public String identifyDataType(PropertyAdapter adapter) {
      if(type.equals(adapter.getType())){
         return dataType;
      }
      return null;
   }

}
