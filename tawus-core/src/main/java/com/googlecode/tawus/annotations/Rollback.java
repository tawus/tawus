package com.googlecode.tawus.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * By placing this annotation on a method, the transaction will be rolled back
 * after the method is executed
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Rollback
{

   /**
    * Id to uniquely identify the SessionFactory
    */
   String factoryId() default "";
}
