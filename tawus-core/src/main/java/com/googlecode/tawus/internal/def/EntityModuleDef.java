package com.googlecode.tawus.internal.def;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.ioc.def.ContributionDef;
import org.apache.tapestry5.ioc.def.DecoratorDef;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.ioc.def.ServiceDef;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.googlecode.tawus.services.EntityLocator;

public class EntityModuleDef implements ModuleDef {
   private Map<String, ServiceDef> serviceDefs = new HashMap<String, ServiceDef>();
   
   public EntityModuleDef(EntityLocator locator){
      for(EntityDef entityDef: locator.getEntityDefs()){
         serviceDefs.put(entityDef.getServiceId(), new EntityDAOServiceDef(entityDef));
      }
   }

   public Set<String> getServiceIds() {
      return serviceDefs.keySet();
   }

   public ServiceDef getServiceDef(String serviceId) {
      return serviceDefs.get(serviceId);
   }

   public Set<DecoratorDef> getDecoratorDefs() {
      return CollectionFactory.newSet();
   }

   public Set<ContributionDef> getContributionDefs() {
      return CollectionFactory.newSet();
   }

   @SuppressWarnings("rawtypes")
   public Class getBuilderClass() {
      return null;
   }

   public String getLoggerName() {
      return EntityModuleDef.class.getName();
   }

}
