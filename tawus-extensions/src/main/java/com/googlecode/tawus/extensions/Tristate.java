package com.googlecode.tawus.extensions;

public enum Tristate {
   True, False, Both;

   public static Boolean toBoolean(Tristate state) {
      switch (state) {
      case True:
         return true;

      case False:
         return false;

      default:
         return null;
      }
   }

   public static Tristate fromBoolean(Boolean verificationRequired) {
      if(verificationRequired == null){
         return Both;
      }else if(verificationRequired){
         return True;
      }else {
         return False;
      }
   }
}
