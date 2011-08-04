package com.googlecode.tawus.internal.dataanalyzers;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;

public class TimeAnalyzer implements DataTypeAnalyzer
{
   private String dataType;

   public TimeAnalyzer(String dataType)
   {
      this.dataType = dataType;
   }

   public String identifyDataType(PropertyAdapter adapter)
   {
      Class<?> clazz = adapter.getType();
      Temporal annotation = adapter.getAnnotation(Temporal.class);
      if(clazz.equals(Date.class) && annotation != null && annotation.value() == TemporalType.TIME)
      {
         return dataType;
      }

      return null;
   }

}
