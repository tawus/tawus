/**
 * 
 */
package com.googlecode.tawus.hibernate.internal.advices;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;

public final class TransactionIsolationAdvice implements MethodAdvice {

   private String factoryID;
   private HibernateSessionManager sessionManager;
   private int isolation;

   public TransactionIsolationAdvice(final String factoryID, int isolation,
         HibernateSessionManager sessionManager) {
      this.factoryID = factoryID;
      this.sessionManager = sessionManager;
      this.isolation = isolation;
   }

   @Override
   @SuppressWarnings("deprecation")
   public void advise(Invocation invocation) {
      Connection connection = sessionManager.getSession(factoryID)
            .connection();
      int oldIsolation = setIsolation(connection, isolation);
      invocation.proceed();
      setIsolation(connection, oldIsolation);
   }

   private int setIsolation(Connection connection, int isolation) {
      try {
         int oldIsolation = connection.getTransactionIsolation();
         connection.setTransactionIsolation(isolation);
         return oldIsolation;
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }
}