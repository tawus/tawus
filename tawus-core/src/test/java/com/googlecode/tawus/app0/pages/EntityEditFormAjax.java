package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.app0.models.User;

public class EntityEditFormAjax {

   @Persist
   @Property
   private User user;
   
   @SuppressWarnings("unused")
   @Persist
   @Property
   private String message;
   
   void onActivate(){
      if(user == null){
         user = new User();
      }
   }
   
   void onSave(){
      message = user.getName() + " created";
   }
   
   void onCancel(){
      message = "Message cleared";
   }
}
