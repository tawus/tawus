package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.app0.models.User;

public class EntityGridWithSearchDemo {

   @Persist
   @Property
   private SearchCriteria<User> criteria;
   
   @SuppressWarnings("unused")
   @Property
   @Persist
   private String message;

   @SuppressWarnings("unused")
   @Property
   private User user;
   
   void onActivate(){

      if(criteria == null){
         criteria = new SearchCriteria<User>(User.class);
      }
   }
   
   public String getSortColumn(){
      return criteria.getOrder().size() == 0? "none": criteria.getOrder().keySet().iterator().next();
   }
   
   void onSearch(){
      message = criteria.getEntity().getName() + "/" + criteria.getEntity().getAddress();
   }
   
   void onCancelSearch(){
      message = "none";
   }
   
}
