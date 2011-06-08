package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.app0.models.User;

public class EntityGridWithSearchAjax {

   @Persist
   @Property
   private SearchCriteria<User> criteria;

   @SuppressWarnings("unused")
   @Property
   @Persist
   private String searchFields;
   
   @SuppressWarnings("unused")
   @Property
   private User user;

   void onActivate() {

      if (criteria == null) {
         criteria = new SearchCriteria<User>(User.class);
      }
   }

   void onSearch() {
      searchFields = criteria.getEntity().getName() + "/" + criteria.getEntity().getAddress();
   }

   void onCancelSearch() {
      searchFields = "none";
   }

}
