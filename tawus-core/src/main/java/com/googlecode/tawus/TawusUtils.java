package com.googlecode.tawus;

import javax.persistence.Entity;

import org.apache.tapestry5.internal.TapestryInternalUtils;

public class TawusUtils {

   public static boolean isEntity(Object entity) {
      return entity.getClass().getAnnotation(Entity.class) != null;
   }

   public static String toUserPresentable(Class<?> clazz) {
      String className = clazz.getName();
      int index = className.lastIndexOf('.') + 1;
      if(index > 0){
         className = className.substring(index);
      }
      return TapestryInternalUtils.toUserPresentable(className);
   }
   
   public static String stripExceptionPrefix(String message) {
      if(message != null && message.matches("Exception\\s*:")){
         return message.substring(message.indexOf(":")).trim();
      }
      return message;
    }
}
