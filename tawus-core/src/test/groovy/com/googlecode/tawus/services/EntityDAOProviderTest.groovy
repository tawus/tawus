package com.googlecode.tawus.services

import javax.persistence.Entity

import org.apache.tapestry5.ioc.Registry
import org.apache.tapestry5.ioc.RegistryBuilder
import org.apache.tapestry5.ioc.internal.util.CollectionFactory
import org.apache.tapestry5.services.TapestryModule

import spock.lang.Specification

import com.googlecode.tawus.EntityDAONotFoundException
import com.googlecode.tawus.app0.services.AppModule;
import com.googlecode.tawus.internal.AbstractEntityLocator
import com.googlecode.tawus.internal.def.EntityModuleDef


class EntityDAOProviderTest extends Specification {
   
   def "EntityDAO is not present when not in registry"(){
      Registry registry = createRegistry(TapestryModule.class, TawusModule.class)
      when: registry.getService("UserDAO", EntityDAO.class).list();
      then:thrown(RuntimeException)
   }
   
   def "EntityDAO is present in registry"(){
      setup:
         Registry registry = createRegistry(TapestryModule.class, TawusModule.class, AppModule.class)
         def userDAO = registry.getService("UserDAO", EntityDAO.class) != null
      expect: userDAO != null
   }
   
   def Registry createRegistry(def ... classes){
      RegistryBuilder registerBuilder = new RegistryBuilder()
      EntityLocator entityLocator = new AbstractEntityLocator(CollectionFactory.newSet(
         "com.googlecode.tawus.app0.models")){
         public boolean isEntity(Class<?> entityType){
            return entityType.getAnnotation(Entity.class) != null;
         }
      };
   
      return RegistryBuilder.buildAndStartupRegistry(new EntityModuleDef(entityLocator), classes)
   }
}
