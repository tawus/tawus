package com.googlecode.tawus.internal.dataanalyzers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.persistence.Entity;

import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;

/**
 * Entity list analyzer This analyzer is inserted into DataTypeAnalyzer Service
 * to select a block based on the entity type
 * 
 * @author taha
 */
public class EntityListAnalyzer implements DataTypeAnalyzer
{
   private final String dataType;

   /**
    * Constructor
    * 
    * @param dataType
    *           data type
    */
   public EntityListAnalyzer(String dataType)
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
      if(Collection.class.isAssignableFrom(propertyType))
      {
         try
         {
            Type[] type = ((ParameterizedType) propertyAdapter.getDeclaringClass().getDeclaredField(propertyAdapter.getName()).getGenericType()).getActualTypeArguments();
            if(type.length == 1 && ((Class<?>) type[0]).getAnnotation(Entity.class) != null)
            {
               return dataType;
            }
         }
         catch(NoSuchFieldException nsme)
         {
            throw new RuntimeException(nsme);
         }

      }
      return null;
   }
}
