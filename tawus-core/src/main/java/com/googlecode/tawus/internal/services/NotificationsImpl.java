package com.googlecode.tawus.internal.services;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.tawus.services.Notifications;

public class NotificationsImpl implements Notifications {
   private List<String> informations = new ArrayList<String>();
   private List<String> warnings = new ArrayList<String>();
   private List<String> errors =new ArrayList<String>();

   public void inform(String message) {
      informations.add(message);      
   }

   public void warn(String message) {
      warnings.add(message);      
   }
   
   public void error(String message) {
      errors.add(message);      
   }
   
   public boolean getHasMessages(){
      return informations.size() != 0 || warnings.size() != 0 ||
         errors.size() != 0;
   }
   
   public List<String> getInformations(){
      return informations;
   }
   
   public List<String> getWarnings(){
      return warnings;
   }
   
   public List<String> getErrors(){
      return errors;
   }
   
   public void clear(){
      informations.clear();
      warnings.clear();
      errors.clear();
   }

}
