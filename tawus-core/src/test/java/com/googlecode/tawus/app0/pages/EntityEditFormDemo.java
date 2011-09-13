package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.app0.models.User;
import com.googlecode.tawus.components.EntityEditForm;

public class EntityEditFormDemo {
   @Property
   @Persist
   private User user;

   @SuppressWarnings("unused")
   @Persist
   @Property
   private String message;

   @SuppressWarnings("unused")
   @Property
   @Persist
   private boolean updatable;
   
   @InjectComponent
   private EntityEditForm form;
   
   void onActivate(String param) {
      if(user == null){
         user = new User();
      }

      if("updatable".equals(param)){
          updatable = true;
      }else {
         updatable = false;
      }
   }
   
   void onValidate(Object object) {
      if (user.getName().equals(user.getAddress())) {
         form.recordError("name and address cannot be same");
      }
   }

   void onSave() {
      message = user.getName() + "/" + user.getAddress() + "/" + user.getDepartment() + "/"
            + user.getAge() + "/" + user.getOtherDepartments() + "/" + user.getGender();
   }
   
   void onCancel(){
      message = "Message cleared";
   }

}
