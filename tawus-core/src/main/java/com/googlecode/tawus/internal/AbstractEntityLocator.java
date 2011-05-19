package com.googlecode.tawus.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry5.ioc.internal.services.ClassNameLocatorImpl;
import org.apache.tapestry5.ioc.internal.services.ClasspathURLConverterImpl;
import org.apache.tapestry5.ioc.services.ClassNameLocator;

import com.googlecode.tawus.internal.def.EntityDef;
import com.googlecode.tawus.services.EntityLocator;

public abstract class AbstractEntityLocator implements EntityLocator {
   private Set<EntityDef> entityDefs = new HashSet<EntityDef>();
   
   public AbstractEntityLocator(Set<String> packageNames){
      ClassNameLocator locator = new ClassNameLocatorImpl(new ClasspathURLConverterImpl());
      for(String packageName: packageNames){
         for(String className: locator.locateClassNames(packageName)){
            try {
               final Class<?> entityClass = Class.forName(className);
               if(isEntity(entityClass)){
                  entityDefs.add(new EntityDef(){

                     public String getServiceId() {
                        return entityClass.getSimpleName() + "DAO";
                     }

                     public Class<?> getType() {
                        return entityClass;
                     }
                     
                     @Override
                     public String toString(){
                        return "Entity Definition for " + getServiceId();
                     }
                     
                  });
               }
            } catch (ClassNotFoundException e) {
               throw new RuntimeException(e);
            }
         }
      }
   }

   public abstract boolean isEntity(Class<?> entityClass);

   public Set<EntityDef> getEntityDefs() {
      return entityDefs;
   }

}
