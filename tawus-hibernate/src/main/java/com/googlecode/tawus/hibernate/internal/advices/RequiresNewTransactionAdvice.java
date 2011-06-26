/**
 * 
 */
package com.googlecode.tawus.hibernate.internal.advices;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;

public class RequiresNewTransactionAdvice implements
   MethodAdvice {

   private HibernateSessionManager sessionManager;
   private String factoryID;

   public RequiresNewTransactionAdvice(String factoryID,
      HibernateSessionManager sessionManager) {
      this.sessionManager = sessionManager;
      this.factoryID = factoryID;
   }

   @Override
   public void advise(Invocation invocation) {
      if(sessionManager.isWithinTransaction()){
         Session oldSession = sessionManager.getSession(factoryID);
         Session newSession = sessionManager.createSession(factoryID);
         Transaction tx = null;
         try{
            tx = newSession.beginTransaction();
            invocation.proceed();
            tx.commit();
         }catch(Exception ex){
            if(tx != null){
               tx.rollback();
            }
            throw new RuntimeException(ex);
         }finally {
            newSession.close();
            sessionManager.setSession(factoryID, oldSession);
         }
      }else{
         
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
}