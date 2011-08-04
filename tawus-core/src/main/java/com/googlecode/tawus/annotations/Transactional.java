package com.googlecode.tawus.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.googlecode.tawus.Propagation;

/**
 * This annotation when placed on a service method commits the transaction after
 * the method is executed.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Transactional
{

   /**
    * Propagation type
    */
   Propagation propagation() default Propagation.REQUIRED;

   /**
    * Factory Id
    */
   String factoryId() default "";

   /**
    * Isolation level.
    */
   int isolation() default 0;
}
