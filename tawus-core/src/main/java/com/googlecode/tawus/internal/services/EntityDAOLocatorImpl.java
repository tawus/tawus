package com.googlecode.tawus.internal.services;

import org.apache.tapestry5.ioc.ObjectLocator;

import com.googlecode.tawus.services.EntityDAO;
import com.googlecode.tawus.services.EntityDAOLocator;
import com.googlecode.tawus.services.EntityServiceMapper;

public class EntityDAOLocatorImpl implements EntityDAOLocator {
   
   private ObjectLocator locator;
   private EntityServiceMapper entityServiceMapper;

   public EntityDAOLocatorImpl(ObjectLocator locator, EntityServiceMapper entityServiceMapper){
      this.locator = locator;
      this.entityServiceMapper = entityServiceMapper;
   }

   @SuppressWarnings("unchecked")
   public <T> EntityDAO<T> get(Class<T> entityClass) {
      return locator.getService(entityServiceMapper.getServiceId(entityClass), EntityDAO.class);
   }

}
