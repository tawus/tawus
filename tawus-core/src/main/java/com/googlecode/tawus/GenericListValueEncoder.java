package com.googlecode.tawus;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;

public class GenericListValueEncoder<T> implements ValueEncoder<T>
{

   private PropertyAdapter propertyAdapter;
   private List<T> list;

   public GenericListValueEncoder(List<T> list,
         PropertyAccess propertyAccess,
         @SuppressWarnings("rawtypes") Class type,
         String id)
   {
      this.propertyAdapter = propertyAccess.getAdapter(type).getPropertyAdapter(id);
      this.list = list;
   }

   public String toClient(T value)
   {
      return propertyAdapter.get(value).toString();
   }

   public T toValue(String clientValue)
   {
      for(T object : list)
      {
         if(clientValue.equals(propertyAdapter.get(object).toString()))
         {
            return object;
         }
      }
      return null;
   }

}
