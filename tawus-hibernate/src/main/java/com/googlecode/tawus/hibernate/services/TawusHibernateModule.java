package com.googlecode.tawus.hibernate.services;

import java.util.List;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.hibernate.Session;

import com.googlecode.tawus.hibernate.TawusHibernateConstants;
import com.googlecode.tawus.hibernate.internal.services.HibernateEntityDAOSource;
import com.googlecode.tawus.hibernate.internal.services.HibernateSearchCriteriaConverter;
import com.googlecode.tawus.hibernate.internal.services.HibernateSessionManagerImpl;
import com.googlecode.tawus.hibernate.internal.services.SessionFactorySourceImpl;
import com.googlecode.tawus.hibernate.internal.services.SessionShadowBuilderImpl;
import com.googlecode.tawus.hibernate.internal.services.TransactionAdvisorImpl;
import com.googlecode.tawus.services.EntityDAOSource;
import com.googlecode.tawus.services.EntityValidator;
import com.googlecode.tawus.services.TawusModule;

@SubModule(TawusModule.class)
public class TawusHibernateModule {
   public static void bind(ServiceBinder binder) {
      binder.bind(TransactionAdvisor.class, TransactionAdvisorImpl.class);
      binder.bind(SessionShadowBuilder.class, SessionShadowBuilderImpl.class);
      binder.bind(SearchCriteriaConverter.class, HibernateSearchCriteriaConverter.class);
   }

   public void contributeEntityDAOSource(OrderedConfiguration<EntityDAOSource> daoSources) {
      daoSources.addInstance("hibernateSource", HibernateEntityDAOSource.class);
   }

   public static void contributeFactoryDefaults(MappedConfiguration<String, String> defaults) {
      defaults.add(TawusHibernateConstants.DEFAULT_FACTORY_ID, "default");
   }

   @Scope(value = ScopeConstants.PERTHREAD)
   public HibernateSessionManager buildHibernateSessionManager(
         @Symbol(TawusHibernateConstants.DEFAULT_FACTORY_ID) String defaultFactoryID,
         SessionFactorySource sessionFactorySource, PerthreadManager threadManager) {
      HibernateSessionManagerImpl sessionManager = new HibernateSessionManagerImpl(
            defaultFactoryID, sessionFactorySource);
      threadManager.addThreadCleanupListener(sessionManager);
      return sessionManager;
   }

   @ServiceId("default")
   @Primary
   public Session buildDefaultSession(SessionShadowBuilder sessionShadowBuilder,
         HibernateSessionManager sessionManager) {
      return sessionShadowBuilder.build(sessionManager, "default");
   }

   public SessionFactorySource buildSessionFactorySource(ClassNameLocator classNameLocator,
         List<SessionFactoryConfiguration> configuration, RegistryShutdownHub hub) {
      SessionFactorySourceImpl sessionFactorySource = new SessionFactorySourceImpl(
            classNameLocator, configuration);
      hub.addRegistryShutdownListener(sessionFactorySource);
      return sessionFactorySource;
   }

   public void contributeEntityValidator(HibernateSessionManager sessionManager,
         PropertyAccess propertyAccess, OrderedConfiguration<EntityValidator> contribution) {
      contribution.add("unique", new UniqueEntityValidator(sessionManager, propertyAccess));
   }

}
