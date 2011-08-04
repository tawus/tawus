package com.googlecode.tawus.internal.services;

import com.googlecode.tawus.services.EntityServiceMapper;

public class SimpleEntityServiceMapper implements EntityServiceMapper
{

   public String getServiceId(Class<?> value)
   {
      return value.getSimpleName() + "DAO";
   }

   public String getServiceOverrideId(Class<?> value)
   {
      return value.getSimpleName() + "OverrideDAO";
   }

}
