package com.googlecode.tawus.hibernate.services;

import org.apache.tapestry5.ioc.MethodAdviceReceiver;

/**
 * Transaction Advisor 
 * @author Taha Hafeez
 *
 */
public interface TransactionAdvisor {
   /**
    * Add Transaction advice to methods with
    * {@link com.googlecode.tawus.annotations.Transactional} annotation
    * 
    * @param methodAdviceReceiver
    */
   void addTransactionAdvice(MethodAdviceReceiver methodAdviceReceiver);

   void addTransactionAdvice(final MethodAdviceReceiver receiver,
         String factoryID);
}
