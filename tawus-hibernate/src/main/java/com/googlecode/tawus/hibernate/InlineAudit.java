package com.googlecode.tawus.hibernate;

import java.util.Date;


public interface InlineAudit<T> {
   /**
    * Get login class which is stored in tapestry session
    * @return login class
    */
   Class<?> getLoginClass();
   
   /**
    * Set creator
    * @param currentLogin
    */
   void setCreator(T currentLogin);
   
   /**
    * Set creation date
    * @param date
    */
   void setCreationDate(Date date);
   
   /**
    * Set modification date
    * @param date
    */
   void setModificationDate(Date date);
   
   /**
    * Set modifier
    * @param currentLogin
    */
   void setModifier(T currentLogin);

   /**
    * Has the record been modified
    * @return
    */
   boolean isModified();
}
