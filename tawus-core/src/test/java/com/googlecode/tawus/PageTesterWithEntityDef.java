package com.googlecode.tawus;

import javax.persistence.Entity;

import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.test.PageTester;

import com.googlecode.tawus.internal.AbstractEntityLocator;
import com.googlecode.tawus.internal.def.EntityModuleDef;
import com.googlecode.tawus.services.EntityLocator;

public class PageTesterWithEntityDef extends PageTester {
   public PageTesterWithEntityDef(String appPackage, String appName, String contextPath,
         @SuppressWarnings("rawtypes") Class ... moduleClasses) {
      super(appPackage, appName, contextPath, moduleClasses);
   }
   
   public PageTesterWithEntityDef(String appPackage, String appName){
      this(appPackage, appName, "src/test/webapp");
   }

   @Override
   public ModuleDef [] provideExtraModuleDefs(){
      EntityLocator entityLocator = new AbstractEntityLocator(CollectionFactory.newSet(
            "com.googlecode.tawus.app0.models")){
         @Override
         @SuppressWarnings("unchecked")
         public boolean isEntity(@SuppressWarnings("rawtypes") Class entityType){
            return entityType.getAnnotation(Entity.class) != null;
         }
      };

      return new ModuleDef[]{new EntityModuleDef(entityLocator)};
   }
}
