package com.googlecode.tawus.hibernate.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Source for session factory
 */
public interface SessionFactorySource {
   /**
    * Get session factory for a given string marker
    * @param factoryID string marker
    * @return session factory
    */
   public SessionFactory getSessionFactory(String factoryID);
   
   /**
    * Get session factory for a given annotation marker
    * @param entityClass marker annotation
    * @return session factory
    */
   public SessionFactory getSessionFactory(Class<?> entityClass);
   
   public Session createSession(Class<?> entityClass);
   
   public Session createSession(String factoryID);

   public String getFactoryId(Class<?> entityClass);
   
   public boolean hasSessionFactory(String factoryId);
   
   public boolean hasSessionFactory(Class<?> entityClass);
}
