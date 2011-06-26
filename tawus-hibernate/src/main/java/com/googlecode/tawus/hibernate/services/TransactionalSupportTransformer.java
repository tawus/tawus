package com.googlecode.tawus.hibernate.services;

/**
 * Adds transactional support to a service
 * 
 * @author Taha Hafeez
 * 
 */
public interface TransactionalSupportTransformer {
   /**
    * Transforms a class by adding support for
    * {@link com.googlecode.tawus.hibernate.annotation.Transactional}
    * 
    * @param <T>
    * @param clazz
    * @return
    */
   public <T> T transform(T clazz);
}
