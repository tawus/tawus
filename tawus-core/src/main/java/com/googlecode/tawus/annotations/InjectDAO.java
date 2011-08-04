package com.googlecode.tawus.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a {@link com.googlecode.tawus.services.GenericDAO GenericDAO} to be
 * injected in a page or component
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface InjectDAO
{

   /**
    * Entity class for which the DAO is to be injected.
    */
   Class<?> value();

}
