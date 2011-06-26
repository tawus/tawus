package com.googlecode.tawus.hibernate.utils;

import org.hibernate.cfg.ImprovedNamingStrategy;

@SuppressWarnings("serial")
public class NewAndImprovedNamingStrategy extends ImprovedNamingStrategy {
   @Override
   public String foreignKeyColumnName(String propertyName,
         String propertyEntityName, String propertyTableName,
         String referencedColumnName) {
      String s = super.foreignKeyColumnName(propertyName, propertyEntityName,
            propertyTableName, referencedColumnName);
      return s.endsWith("_id") ? s : s + "_id";
   }
}
