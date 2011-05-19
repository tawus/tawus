package com.googlecode.tawus.app0.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;

import com.googlecode.tawus.services.EntityDAO;
import com.googlecode.tawus.services.TawusModule;

@SubModule(TawusModule.class)
public class AppModule {
   public static void bind(ServiceBinder binder){
      binder.bind(UserDAO.class, UserDAOImpl.class).withId("UserOverrideDAO");
      binder.bind(EntityDAO.class, DepartmentDAOImpl.class).withId("DepartmentOverrideDAO");
   }

   public static void contributeApplicationDefaults(
         MappedConfiguration<String, String> configuration) {
      configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
   }
   
}
