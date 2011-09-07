package com.googlecode.tawus.addons.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectSelectSupport {
    /**
     * Expression to be used as label for select model. This takes an
     * expression as a parameter. The expression can be any property
     * value with <code>${}</code>.
     */
    String label();
  
    /**
     * Property to be used as index. It's value must be unique for each
     * item
     */
    String index();
  
    /**
     * This suffix is appended to the generated method name.
     * Default is <code>Support</code>
     */
    String methodSuffix() default "Support";
  
    /**
     * Item type.
     */
    Class<?> type();
  
 }
