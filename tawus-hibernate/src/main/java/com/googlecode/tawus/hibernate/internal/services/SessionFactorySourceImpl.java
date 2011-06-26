package com.googlecode.tawus.hibernate.internal.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.googlecode.tawus.hibernate.services.SessionFactoryConfiguration;
import com.googlecode.tawus.hibernate.services.SessionFactorySource;

/**
 * Session Factory Source Implementation.
 */
public class SessionFactorySourceImpl implements SessionFactorySource,
   RegistryShutdownListener {
   private final Map<String, SessionFactory> factoryMap = new HashMap<String, SessionFactory>();
   private final Map<Class<?>, String> entityMap = new HashMap<Class<?>, String>();
   private final ClassNameLocator classNameLocator;

   /**
    * Constructor
    * 
    * @param configurations
    *           database configurations
    */
   public SessionFactorySourceImpl(final ClassNameLocator classNameLocator,
       final List<SessionFactoryConfiguration> configurations) {
      this.classNameLocator = classNameLocator;
      for(final SessionFactoryConfiguration configuration : configurations){
         setupDB(configuration);
      }
   }

   private void setupDB(SessionFactoryConfiguration configuration) {
      setupSessionFactory(configuration);
   }

   private void setupSessionFactory(
      final SessionFactoryConfiguration configuration) {
      final Configuration hibernateConfig = new Configuration();
      final List<Class<?>> entities = loadEntityClasses(configuration);

      // Load entity classes
      for(final Class<?> entityClass : entities){
         hibernateConfig.addAnnotatedClass(entityClass);
         entityMap.put(entityClass, configuration.getFactoryId());
      }

      configuration.configure(hibernateConfig);
      
      final SessionFactory sf = hibernateConfig.buildSessionFactory();
      if(configuration.getFactoryId() != null){
         factoryMap.put(configuration.getFactoryId(), sf);
      }
   }

   private List<Class<?>> loadEntityClasses(
      final SessionFactoryConfiguration configuration) {
      final ClassLoader classLoader = Thread.currentThread()
         .getContextClassLoader();

      final List<Class<?>> entityClasses = new ArrayList<Class<?>>();

      for(final String packageName : configuration.getPackageNames()){
         for(final String className : classNameLocator
            .locateClassNames(packageName)){
            try{
               Class<?> entityClass = null;
               entityClass = classLoader.loadClass(className);
               if(entityClass.getAnnotation(javax.persistence.Entity.class) != null ||
                   entityClass.getAnnotation(javax.persistence.MappedSuperclass.class) != null){
                  entityClasses.add(entityClass);
               }
            }catch(ClassNotFoundException e){
               throw new RuntimeException(e);
            }
         }
      }
      return entityClasses;
   }
   
   public boolean hasSessionFactory(final Class<?> entityClass){
      String factoryId = entityMap.get(entityClass);
      if(factoryId == null){
         return false;
      }
      return hasSessionFactory(factoryId);
   }
   
   public boolean hasSessionFactory(final String factoryId){
      return factoryMap.get(factoryId) != null;
   }

   public SessionFactory getSessionFactory(final String factoryId) {
      SessionFactory sf = factoryMap.get(factoryId);
      if(sf == null){
         throw new RuntimeException("No session factory found for factoryId: "
            + factoryId);
      }
      return sf;
   }

   public SessionFactory getSessionFactory(final Class<?> entityClass) {
      SessionFactory sf = getSessionFactory(entityMap.get(entityClass));
      if(sf == null){
         throw new RuntimeException("No session factory found for entity: "
            + entityClass);
      }
      return sf;
   }

   public void registryDidShutdown() {
      for(final SessionFactory sessionFactory : factoryMap.values()){
         sessionFactory.close();
      }
   }

   public Session createSession(Class<?> entityClass) {
      return createSession(getFactoryId(entityClass));
   }

   public Session createSession(String factoryId) {
      final Session session = getSessionFactory(factoryId).openSession();
      return session;
   }

   public String getFactoryId(Class<?> entityClass) {
      return entityMap.get(entityClass);
   }
}
