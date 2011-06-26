/**
 * 
 */
package com.googlecode.tawus.hibernate.internal.advices;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;

public class NestedTransactionAdvice implements MethodAdvice {

   private HibernateSessionManager sessionManager;
   private String factoryID;

   public NestedTransactionAdvice(String factoryID,
      HibernateSessionManager sessionManager) {
      this.sessionManager = sessionManager;
      this.factoryID = factoryID;
   }

   @Override
   public void advise(Invocation invocation) {
      
      try{
         sessionManager.begin(factoryID);
         invocation.proceed();
         sessionManager.commit(factoryID);
      }catch(Exception ex){
         sessionManager.rollback(factoryID);
         throw new RuntimeException(ex);
      }
   }
}