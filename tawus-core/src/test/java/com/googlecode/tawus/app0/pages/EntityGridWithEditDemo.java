package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.app0.models.User;

public class EntityGridWithEditDemo {

   @Persist
   @Property
   private SearchCriteria<User> criteria;

   @SuppressWarnings("unused")
   @Property
   @Persist
   private String message;
   
   @SuppressWarnings("unused")
   @Property
   @Persist
   private User user;

   void onActivate() {
      if (criteria == null) {
         criteria = new SearchCriteria<User>(User.class);
         criteria.setEnabled(true);
      }
   }

   void onDelete() {
      message = "deleted";
   }

   void onSave() {
      message = criteria.getEntity().getName() + "/" + criteria.getEntity().getAddress();
   }

   void onCanceled() {
      message = "none";
   }

   public String getSortColumn() {
      return criteria.getOrder().size() == 0 ? "none"
            : criteria.getOrder().keySet().iterator().next();
   }

}
