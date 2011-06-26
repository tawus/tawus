package com.googlecode.tawus.hibernate.internal.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ThreadCleanupListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.tawus.hibernate.TawusHibernateConstants;
import com.googlecode.tawus.hibernate.services.HibernateSessionManager;
import com.googlecode.tawus.hibernate.services.SessionFactorySource;

public class HibernateSessionManagerImpl implements HibernateSessionManager,
   ThreadCleanupListener {

   private SessionFactorySource sessionFactorySource;
   private String defaultFactoryID;
   private Map<String, Session> sessions = new HashMap<String, Session>();
   private Map<String, Stack<Transaction>> transactions = new HashMap<String, Stack<Transaction>>();

   public HibernateSessionManagerImpl(
      @Symbol(TawusHibernateConstants.DEFAULT_FACTORY_ID) String defaultFactoryID,
      SessionFactorySource sessionFactorySource) {
      this.sessionFactorySource = sessionFactorySource;
      this.defaultFactoryID = defaultFactoryID;
   }

   public Session getSession(Class<?> entityClass) {
      return getSession(sessionFactorySource.getFactoryId(entityClass));
   }
   
   public Session getSession(String factoryID) {
      factoryID = nvl(factoryID);
      Session session = sessions.get(factoryID);
      if( session == null){
         session = createSession(factoryID);
      }
      
      return session;
   }

   public Session getSession() {
      return getSession(defaultFactoryID);
   }

   public void threadDidCleanup() {
      for(final Stack<Transaction> transactionStack: transactions.values()){
         while(!transactionStack.isEmpty()){
            transactionStack.pop().rollback();
         }
      }
      
      for(final Session session: sessions.values()){
         session.close();
      }
   }

   public void begin(String factoryID) {
      factoryID = nvl(factoryID);
      Transaction transaction = getSession(factoryID).beginTransaction();
      Stack<Transaction> transactionStack = transactions.get(factoryID);
      if(transactionStack == null){
         transactionStack = new Stack<Transaction>();
         transactions.put(factoryID, transactionStack);
      }
      transactionStack.push(transaction);
   }
   
   public void begin() {
      begin(defaultFactoryID);
   }

   public boolean beginOrContinue(String factoryID) {
      factoryID = nvl(factoryID);
      if(!isWithinTransaction(factoryID)){
         begin(factoryID);
         return true;
      }
      return false;
   }
   
   public boolean beginOrContinue(){
      return beginOrContinue(defaultFactoryID);
   }

   public void commit(String factoryID) {
      factoryID = nvl(factoryID);
      if(!isWithinTransaction(factoryID)){
         throw new RuntimeException("Not within a transaction");
      }
      transactions.get(factoryID).pop().commit();
   }
   
   public void commit(){
      commit(defaultFactoryID);
   }

   public boolean isWithinTransaction(String factoryID){
      Stack<Transaction> transactionStack = transactions.get(factoryID);
      return transactionStack != null && transactionStack.size() != 0;
   }
   
   public boolean isWithinTransaction() {
      return isWithinTransaction(defaultFactoryID);
   }

   public void rollback(String factoryID) {
      factoryID = nvl(factoryID);
      if(isWithinTransaction(factoryID)){
         transactions.get(factoryID).pop().rollback();
      }
   }
   
   private String nvl(String factoryID) {
      return (factoryID == null || "".equals(factoryID)) ? defaultFactoryID : factoryID;
   }

   public void rollback(){
      rollback(defaultFactoryID);
   }
   
   public Transaction getCurrentTransaction(String factoryID){
      factoryID = nvl(factoryID);
      if(!isWithinTransaction(factoryID)){
         throw new RuntimeException("Not within a transaction");
      }
      return transactions.get(factoryID).peek();
   }

   public Session createSession(String factoryID) {
      factoryID = nvl(factoryID);
      final Session session = sessionFactorySource.createSession(factoryID);
      sessions.put(factoryID, session);
      return session;
   }

   public Session createSession() {
      return createSession(defaultFactoryID);
   }
   
   public void setSession(String factoryID, Session session) {
      sessions.put(nvl(factoryID), session);      
   }   

}
