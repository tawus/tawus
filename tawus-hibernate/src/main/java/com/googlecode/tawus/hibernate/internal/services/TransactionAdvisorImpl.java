package com.googlecode.tawus.hibernate.internal.services;

import java.lang.reflect.Method;

import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.googlecode.tawus.Propagation;
import com.googlecode.tawus.annotations.Transactional;
import com.googlecode.tawus.hibernate.TawusHibernateConstants;
import com.googlecode.tawus.hibernate.internal.advices.MandatoryTransactionAdvice;
import com.googlecode.tawus.hibernate.internal.advices.NestedTransactionAdvice;
import com.googlecode.tawus.hibernate.internal.advices.NeverTransactionAdvice;
import com.googlecode.tawus.hibernate.internal.advices.NotSupportedTransactionAdvice;
import com.googlecode.tawus.hibernate.internal.advices.RequiredTransactionAdvice;
import com.googlecode.tawus.hibernate.internal.advices.RequiresNewTransactionAdvice;
import com.googlecode.tawus.hibernate.internal.advices.TransactionIsolationAdvice;
import com.googlecode.tawus.hibernate.services.HibernateSessionManager;
import com.googlecode.tawus.hibernate.services.TransactionAdvisor;

public class TransactionAdvisorImpl implements TransactionAdvisor {

   private HibernateSessionManager sessionManager;
   private String defaultFactoryID;

   public TransactionAdvisorImpl(HibernateSessionManager sessionManager, 
         @Symbol(TawusHibernateConstants.DEFAULT_FACTORY_ID)String defaultFactoryID) {
      this.sessionManager = sessionManager;
      this.defaultFactoryID = defaultFactoryID;
   }

   public void addTransactionAdvice(
         final MethodAdviceReceiver receiver, String factoryID) {
      if(factoryID == null){
         factoryID = defaultFactoryID;
      }
      
      for (Method method : receiver.getInterface().getMethods()) {
         Transactional transactional = method
               .getAnnotation(Transactional.class);

         if (transactional != null) {
            adviceMethod(transactional.propagation(), transactional.isolation(), factoryID, method, receiver);
         }
      }
   }
   
   public void addTransactionAdvice(final MethodAdviceReceiver receiver) {
      addTransactionAdvice(receiver, "");
   }

   private void adviceMethod(final Propagation propagation,  int isolation, 
         String factoryID,
         Method method,
         MethodAdviceReceiver receiver) {

      switch (propagation) {
      case REQUIRED:
         receiver.adviseMethod(method, new RequiredTransactionAdvice(factoryID,
               sessionManager));
         break;

      case MANDATORY:
         receiver.adviseMethod(method, new MandatoryTransactionAdvice(factoryID,
               sessionManager));
         break;

      case NESTED:
         receiver.adviseMethod(method, new NestedTransactionAdvice(factoryID,
               sessionManager));
         break;

      case NEVER:
         receiver.adviseMethod(method, new NeverTransactionAdvice(factoryID,
               sessionManager));
         break;

      case SUPPORTS:
         break;

      case REQUIRES_NEW:
         receiver.adviseMethod(method, new RequiresNewTransactionAdvice(
               factoryID, sessionManager));
         break;

      case NOT_SUPPORTED:
         receiver.adviseMethod(method, new NotSupportedTransactionAdvice(
               factoryID, sessionManager));
         break;
      }

      if (isolation != 0) {
         receiver.adviseMethod(method,
               new TransactionIsolationAdvice(factoryID,
                     isolation, sessionManager));
      }

   }


}
