package com.googlecode.tawus.internal.def;

import java.util.Set;

import org.apache.tapestry5.ioc.ObjectCreator;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBuilderResources;
import org.apache.tapestry5.ioc.def.ServiceDef;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.googlecode.tawus.EntityDAONotFoundException;
import com.googlecode.tawus.services.EntityDAO;
import com.googlecode.tawus.services.EntityDAOSource;

public class EntityDAOServiceDef implements ServiceDef
{

   private EntityDef entityDef;

   public EntityDAOServiceDef(EntityDef entityDef)
   {
      this.entityDef = entityDef;
   }

   public ObjectCreator<Object> createServiceCreator(final ServiceBuilderResources resources)
   {
      return new ObjectCreator<Object>()
      {

         public Object createObject()
         {
            Object object = resources.getService(EntityDAOSource.class).get(entityDef.getType());

            if(object == null)
            {
               throw new EntityDAONotFoundException("Could not find EntityDAO implementation for "
                     + entityDef.getType());
            }
            return object;
         }

      };
   }

   public String getServiceId()
   {
      return entityDef.getServiceId();
   }

   @SuppressWarnings("rawtypes")
   public Set<Class> getMarkers()
   {
      return CollectionFactory.newSet();
   }

   @SuppressWarnings("rawtypes")
   public Class getServiceInterface()
   {
      return EntityDAO.class;
   }

   public String getServiceScope()
   {
      return ScopeConstants.DEFAULT;
   }

   public boolean isEagerLoad()
   {
      return false;
   }

}
