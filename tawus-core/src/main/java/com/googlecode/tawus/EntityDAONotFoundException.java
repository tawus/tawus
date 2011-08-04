package com.googlecode.tawus;

public class EntityDAONotFoundException extends RuntimeException
{

   private static final long serialVersionUID = 1L;

   public EntityDAONotFoundException()
   {
   }

   public EntityDAONotFoundException(String message)
   {
      super(message);
   }

   public EntityDAONotFoundException(Throwable cause)
   {
      super(cause);
   }

   public EntityDAONotFoundException(String message, Throwable cause)
   {
      super(message, cause);
   }

}
