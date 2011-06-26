/**
 * 
 */
package com.googlecode.tawus.hibernate.internal.advices;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;

public class NeverTransactionAdvice implements MethodAdvice {

   private HibernateSessionManager sessionManager;
   private String factoryID;

   public NeverTransactionAdvice(String factoryID,
      HibernateSessionManager sessionManager) {
      this.sessionManager = sessionManager;
      this.factoryID = factoryID;
   }

   @Override
   public void advise(Invocation invocation) {
      if(sessionManager.isWithinTransaction(factoryID)){
         throw new RuntimeException("Cannot proceed within a transaction");
      }
      invocation.proceed();
   }
}