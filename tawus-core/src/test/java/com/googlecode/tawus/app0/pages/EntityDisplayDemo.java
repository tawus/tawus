package com.googlecode.tawus.app0.pages;

import java.util.Date;

import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.app0.models.User;

public class EntityDisplayDemo
{
   @Property
   private User user;
   
   void setupRender()
   {
      user = new User();
      user.setAddress("Srinagar, J&K");
      user.setAge(32);
      user.setDob(new Date());
      user.setName("Taha Hafeez");
   }
   
}
