package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

import com.googlecode.tawus.app0.models.User;

public class EntityEditorAjax {

   @Persist
   @Property
   private User user;
   
   @InjectComponent
   private Zone zone;
   
   @SuppressWarnings("unused")
   @Persist
   @Property
   private String message;
   
   void onActivate(){
      if(user == null){
         user = new User();
      }
   }
   
   Zone onSubmit(){
      message = user.getName() + " created";
      return zone;
   }
}
