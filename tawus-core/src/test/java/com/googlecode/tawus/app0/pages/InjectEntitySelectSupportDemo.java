package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.annotations.InjectEntitySelectSupport;
import com.googlecode.tawus.app0.models.User;

public class InjectEntitySelectSupportDemo
{
   @SuppressWarnings("unused")
   @InjectEntitySelectSupport
   @Persist
   private SearchCriteria<User> userCriteria;
   
   @SuppressWarnings("unused")
   @Property
   @Persist(PersistenceConstants.FLASH)
   private User user;

   void onActivate(boolean initialize)
   {
       if(initialize)
       {
           userCriteria = new SearchCriteria<User>(User.class);
       }else {
          userCriteria = null;
       }
   }
   
}
