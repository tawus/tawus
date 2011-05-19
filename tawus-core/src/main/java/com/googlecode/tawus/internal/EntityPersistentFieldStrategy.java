package com.googlecode.tawus.internal;

import java.io.Serializable;

import org.apache.tapestry5.internal.services.AbstractSessionPersistentFieldStrategy;
import org.apache.tapestry5.services.Request;

import com.googlecode.tawus.EntityNotFoundException;
import com.googlecode.tawus.TawusUtils;
import com.googlecode.tawus.services.EntityDAOLocator;


public class EntityPersistentFieldStrategy extends AbstractSessionPersistentFieldStrategy {
   private final EntityDAOLocator locator;

   public EntityPersistentFieldStrategy(final Request request, 
         final EntityDAOLocator locator){
      super("entity:", request);
      this.locator = locator;
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   protected Object convertApplicationValueToPersisted(Object newValue){
      if(!TawusUtils.isEntity(newValue)){
         throw new RuntimeException(newValue.getClass().getName() + " is not an entity");
      }

      Serializable id = locator.get(newValue.getClass()).getIdentifier(newValue);
      if(id == null){
         return super.convertApplicationValueToPersisted(newValue);
      }
      return new PersistedEntity(newValue.getClass(), id);
   }

   @SuppressWarnings("unchecked")
   protected Object convertPersistedToApplicationValue(Object persistedValue){
      if(!(persistedValue instanceof PersistedEntity)){
         return super.convertPersistedToApplicationValue(persistedValue);
      }
      
      @SuppressWarnings("rawtypes")
      PersistedEntity e = (PersistedEntity)persistedValue;
      Object entity = locator.get(e.getEntityClass()).find(e.getId());
      if(entity == null){
         throw new EntityNotFoundException(
               "Could not find entity of type = " + 
               e.getEntityClass() + " with id = " + e.getId());
      }
      return entity;
   }
   
}
