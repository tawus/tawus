package com.googlecode.tawus.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation when placed on a {@link com.googlecode.tawus.SearchCriteria
 * SearchCriteria} field in a page or component creates getters for
 * {@link com.googlecode.tawus.EntitySelectModel EntitySelectModel} and
 * {@link com.googlecode.tawus.EntityValueEncoder} which are ready-to-use for
 * {@link org.apache.tapestry.corelib.components.Select select} component
 * 
 * @see org.apache.tapestry.corelib.components.Select
 * @see org.apache.tapestry.ValueEncoder
 * @see org.apache.tapestry.SelectModel
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface InjectEntitySelectSupport
{
}
