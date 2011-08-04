package com.googlecode.tawus.internal.dataanalyzers;

import javax.persistence.Entity;

import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;

/**
 * Entity analyzer This analyzer is inserted into DataTypeAnalyzer Service to
 * select a block based on the entity type
 * 
 * @author taha
 */
public class EntityAnalyzer implements DataTypeAnalyzer
{
   private final String dataType;

   /**
    * Constructor
    * 
    * @param dataType
    *           data type
    */
   public EntityAnalyzer(String dataType)
   {
      this.dataType = dataType;
   }

   /**
    * Get type for which this data analyzer is to be used
    * 
    * @return data type
    */
   public String getType()
   {
      return dataType;
   }

   /**
    * {@inheritDoc} This selects any collection type which has parameter of type
    * entity
    */
   public String identifyDataType(PropertyAdapter propertyAdapter)
   {
      Class<?> propertyType = propertyAdapter.getType();
      if(propertyType.getAnnotation(Entity.class) != null)
      {
         return dataType;
      }
      return null;
   }
}
