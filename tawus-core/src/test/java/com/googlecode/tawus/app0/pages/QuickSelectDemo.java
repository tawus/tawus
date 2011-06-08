package com.googlecode.tawus.app0.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

import com.googlecode.tawus.app0.models.User;

public class QuickSelectDemo {

   @Property
   private List<User> users;
   
   @SuppressWarnings("unused")
   @Property
   @Persist(PersistenceConstants.FLASH)
   private User user;

   @InjectComponent
   private Zone zone;
   
   void onActivate(){
      users = new ArrayList<User>();
      User user1 = new User();
      user1.setName("Tawus");
      user1.setAddress("Srinagar");
      user1.setId(1L);
      users.add(user1);
      
      User user2 = new User();
      user2.setName("Taha");
      user2.setAddress("Kashmir");
      user2.setId(2L);
      users.add(user2);
   }
   
   Zone onValueChanged(){
      return zone;
   }
}
