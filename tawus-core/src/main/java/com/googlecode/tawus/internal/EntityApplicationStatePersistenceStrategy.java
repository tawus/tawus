package com.googlecode.tawus.internal;

import java.io.Serializable;

import org.apache.tapestry5.internal.services.SessionApplicationStatePersistenceStrategy;
import org.apache.tapestry5.services.ApplicationStateCreator;
import org.apache.tapestry5.services.Request;

import com.googlecode.tawus.EntityNotFoundException;
import com.googlecode.tawus.TawusUtils;
import com.googlecode.tawus.services.EntityDAOLocator;

public class EntityApplicationStatePersistenceStrategy extends
      SessionApplicationStatePersistenceStrategy {
   private final EntityDAOLocator locator;

   public EntityApplicationStatePersistenceStrategy(final Request request,
         final EntityDAOLocator locator) {
      super(request);
      this.locator = locator;
   }

   @SuppressWarnings("unchecked")
   public <T> T get(final Class<T> ssoClass,
         final ApplicationStateCreator<T> creator) {
      final Object object = getOrCreate(ssoClass, creator);
      if (object instanceof PersistedEntity) {
         @SuppressWarnings("rawtypes")
         final PersistedEntity entity = (PersistedEntity) object;
         final Object restoredEntity;
         restoredEntity = locator.get(entity.getEntityClass()).find(
               entity.getId());

         if (restoredEntity == null) {
            throw new EntityNotFoundException("Entity of type = "
                  + entity.getEntityClass() + " and id = " + entity.getId()
                  + " not found");
         }

         return (T) restoredEntity;
      }
      return (T) object;
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public <T> void set(Class<T> ssoClass, T sso) {
      final String key = buildKey(ssoClass);
      final Object entity;
      if (sso != null) {
         if (!TawusUtils.isEntity(sso)) {
            entity = sso;
         } else {
            Serializable id = locator.get(ssoClass).getIdentifier(sso);
            entity = new PersistedEntity(sso.getClass(), id);
         }
      } else {
         entity = null;
      }
      getSession().setAttribute(key, entity);
   }
}
