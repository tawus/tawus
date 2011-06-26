/**
 * 
 */
package com.googlecode.tawus.hibernate.internal.advices;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.hibernate.Session;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;

public class NotSupportedTransactionAdvice implements
   MethodAdvice {

   private HibernateSessionManager sessionManager;
   private String factoryID;

   public NotSupportedTransactionAdvice(String factoryID,
      HibernateSessionManager sessionManager) {
      this.sessionManager = sessionManager;
      this.factoryID = factoryID;
   }

   @Override
   public void advise(Invocation invocation) {
      if(sessionManager.isWithinTransaction()){
         Session oldSession = sessionManager.getSession(factoryID);
         Session newSession = sessionManager.createSession(factoryID);
         invocation.proceed();
         newSession.close();
         sessionManager.setSession(factoryID, oldSession);
      }else{
         invocation.proceed();
      }
   }
}