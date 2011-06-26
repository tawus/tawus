/**
 * 
 */
package com.googlecode.tawus.hibernate.internal.advices;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;

public class RequiredTransactionAdvice implements
  MethodAdvice {

   private HibernateSessionManager sessionManager;
   private String factoryID;

   public RequiredTransactionAdvice(String factoryID,
      HibernateSessionManager sessionManager) {
      this.sessionManager = sessionManager;
      this.factoryID = factoryID;
   }

   @Override
   public void advise(Invocation invocation) {
      boolean isNew = sessionManager.beginOrContinue(factoryID);
      if(!isNew){
         invocation.proceed();
         return;
      }

      try{
         invocation.proceed();
         sessionManager.commit(factoryID);
      }catch(Exception ex){
         sessionManager.rollback(factoryID);
         throw new RuntimeException(ex);
      }
   }
}