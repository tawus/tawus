package com.googlecode.tawus;

/**
 * This exception is thrown if an entity is not found
 * in the database
 */
public class EntityNotFoundException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   /**
    * Constructor
    * @param message Exception message
    * @param cause Exception cause
    */
   public EntityNotFoundException(String message, Throwable cause){
      super(message, cause);
   }
   
   /**
    * Constructor
    * @param message Exception message
    */
   public EntityNotFoundException(String message){
      super(message);
   }

}
