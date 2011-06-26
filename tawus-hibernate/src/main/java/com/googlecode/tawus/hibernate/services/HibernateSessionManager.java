package com.googlecode.tawus.hibernate.services;

import org.hibernate.Session;

public interface HibernateSessionManager {
   
   public Session getSession(Class<?> entityClass);
   
   public Session getSession(String factoryID);
   
   public Session getSession();
   
   public void rollback();
   
   public void rollback(String factoryID);
   
   public void commit();
   
   public void commit(String factoryID);

   public boolean isWithinTransaction();
   
   public boolean isWithinTransaction(String factoryID);
   
   public void begin();
   
   public void begin(String factoryID);
   
   public boolean beginOrContinue();
   
   public boolean beginOrContinue(String factoryID);

   public Session createSession(String factoryID);

   public void setSession(String factoryID, Session oldSession);

}
