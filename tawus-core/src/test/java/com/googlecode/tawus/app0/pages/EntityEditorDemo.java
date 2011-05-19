package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.app0.models.User;

public class EntityEditorDemo {
   @Property
   private User user;
   
   @SuppressWarnings("unused")
   @Persist
   @Property
   private String message;
   
   void onSubmit(){
      message = user.getName() + "/" + user.getAddress() + "/" + user.getDepartment() + "/" + user.getAge() + "/" + 
      user.getOtherDepartments() + "/" + user.getGender();
   }
   
}
