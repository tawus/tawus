package com.googlecode.tawus.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for graceful ajax degradation. If an event-handler is annotated
 * with this annotation, the event handler returns null when the request is
 * non-ajax.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XHR
{

}