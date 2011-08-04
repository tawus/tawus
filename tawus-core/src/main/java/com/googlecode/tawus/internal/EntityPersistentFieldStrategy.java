package com.googlecode.tawus.internal;

import java.io.Serializable;

import org.apache.tapestry5.internal.services.AbstractSessionPersistentFieldStrategy;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.tawus.TawusUtils;
import com.googlecode.tawus.services.EntityDAOLocator;

public class EntityPersistentFieldStrategy extends AbstractSessionPersistentFieldStrategy
{
   private final EntityDAOLocator locator;
   private static final Logger logger = LoggerFactory.getLogger(EntityPersistentFieldStrategy.class);

   public EntityPersistentFieldStrategy(final Request request, final EntityDAOLocator locator)
   {
      super("entity:", request);
      this.locator = locator;
   }

   @Override
   @SuppressWarnings({ "unchecked", "rawtypes" })
   protected Object convertApplicationValueToPersisted(Object newValue)
   {
      if(!TawusUtils.isEntity(newValue))
      {
         throw new RuntimeException(newValue.getClass().getName() + " is not an entity");
      }

      Serializable id = locator.get(newValue.getClass()).getIdentifier(newValue);
      if(id == null)
      {
         logger.debug("Using default persistence strategy as id is null");
         return super.convertApplicationValueToPersisted(newValue);
      }
      PersistedEntity persistedEntity = new PersistedEntity(newValue.getClass(), id);
      logger.debug("converted entity " + newValue + " of type " + newValue.getClass() + " to " + persistedEntity);
      return persistedEntity;
   }

   @Override
   @SuppressWarnings("unchecked")
   protected Object convertPersistedToApplicationValue(Object persistedValue)
   {
      if(!(persistedValue instanceof PersistedEntity))
      {
         return super.convertPersistedToApplicationValue(persistedValue);
      }

      @SuppressWarnings("rawtypes")
      PersistedEntity persistedEntity = (PersistedEntity) persistedValue;
      Object entity = locator.get(persistedEntity.getEntityClass()).find(persistedEntity.getId());
      logger.debug("converted persisted entity " + persistedEntity + " to " + entity + " of type "
            + (entity != null ? entity.getClass() : "unknown"));
      return entity;
   }

}
