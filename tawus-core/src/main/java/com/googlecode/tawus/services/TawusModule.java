package com.googlecode.tawus.services;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.ChainBuilder;
import org.apache.tapestry5.ioc.services.StrategyBuilder;
import org.apache.tapestry5.ioc.util.UnknownValueException;
import org.apache.tapestry5.services.ApplicationStatePersistenceStrategy;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.EditBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.PersistentFieldStrategy;

import com.googlecode.tawus.internal.EntityApplicationStatePersistenceStrategy;
import com.googlecode.tawus.internal.EntityPersistentFieldStrategy;
import com.googlecode.tawus.internal.dataanalyzers.EntityAnalyzer;
import com.googlecode.tawus.internal.dataanalyzers.EntityListAnalyzer;
import com.googlecode.tawus.internal.dataanalyzers.TimeAnalyzer;
import com.googlecode.tawus.internal.services.DefaultCriteriaSource;
import com.googlecode.tawus.internal.services.EntityDAOLocatorImpl;
import com.googlecode.tawus.internal.services.InjectDAOWorker;
import com.googlecode.tawus.internal.services.SimpleEntityServiceMapper;
import com.googlecode.tawus.internal.services.TawusBeanBlockOverrideSourceImpl;
import com.googlecode.tawus.internal.services.TawusBeanBlockSourceImpl;
import com.googlecode.tawus.internal.services.TawusDefaultDataTypeAnalyzer;
import com.googlecode.tawus.internal.transform.InjectEntitySelectSupportWorker;
import com.googlecode.tawus.internal.transform.XHRWorker;

/**
 * Tawus Module Configuration File
 */
public class TawusModule
{
   /**
    * Service binder
    * 
    * @param binder
    */
   public static void bind(ServiceBinder binder)
   {
      binder.bind(TawusBeanBlockSource.class, TawusBeanBlockSourceImpl.class);
      binder.bind(TawusBeanBlockOverrideSource.class, TawusBeanBlockOverrideSourceImpl.class);
      binder.bind(EntityServiceMapper.class, SimpleEntityServiceMapper.class);
      binder.bind(EntityDAOLocator.class, EntityDAOLocatorImpl.class);
   }

   /*
    */
   public DataTypeAnalyzer buildTawusDefaultDataTypeAnalyzer(@Autobuild TawusDefaultDataTypeAnalyzer service)
   {
      return service;
   }

   /**
    * Contribute the library which will have 'tawus' as prefix
    * 
    * @param configuration
    */
   @Contribute(ComponentClassResolver.class)
   public void provideComponentClassResolver(Configuration<LibraryMapping> configuration)
   {
      configuration.add(new LibraryMapping("tawus", "com.googlecode.tawus"));
   }

   /**
    * Contribute data type analyzers
    * 
    * @param configuration
    * @param tawusDefaultDataTypeAnalyzer
    */
   public static void contributeDataTypeAnalyzer(OrderedConfiguration<DataTypeAnalyzer> configuration)
   {
      configuration.add("entity", new EntityAnalyzer("entity"), "before:Default");
      configuration.add("entity_list", new EntityListAnalyzer("entity_list"), "before:Default");
      configuration.add("time", new TimeAnalyzer("time"), "before:Default");
   }

   /**
    * Contributing our own text types. This is necessary because we want to form
    * fields without labels so that we can easily put them inside the tables
    * 
    * @param configuration
    */
   public static void contributeTawusBeanBlockSource(Configuration<BeanBlockContribution> configuration)
   {
      addEditBlock(configuration, "text");
      addEditBlock(configuration, "number");
      addEditBlock(configuration, "date");
      addEditBlock(configuration, "enum");
      addEditBlock(configuration, "boolean");
      addEditBlock(configuration, "password");
      addEditBlock(configuration, "calendar");
      addEditBlock(configuration, "longtext");
      addEditBlock(configuration, "entity");
      addEditBlock(configuration, "entity_list");
      addEditBlock(configuration, "time");
   }

   private static void addEditBlock(Configuration<BeanBlockContribution> configuration, String dataType)
   {
      configuration.add(new EditBlockContribution(dataType, "tawus/PropertyEditBlocksWithoutLabel", dataType));
   }

   public static void contributePersistentFieldManager(MappedConfiguration<String, PersistentFieldStrategy> configuration)
   {
      configuration.addInstance("entity", EntityPersistentFieldStrategy.class);
   }

   @Contribute(ComponentClassTransformWorker.class)
   public static void provideWorkers(OrderedConfiguration<ComponentClassTransformWorker> workers)
   {
      workers.addInstance("injectEntitySelectSupport", InjectEntitySelectSupportWorker.class);
      workers.addInstance("injectDAOWorker", InjectDAOWorker.class);
      workers.addInstance("XHRWorker", XHRWorker.class);
   }

   public EntityDAOSource buildEntityDAOSource(ChainBuilder chainBuilder, List<EntityDAOSource> commands)
   {
      return chainBuilder.build(EntityDAOSource.class, commands);
   }

   public void contributeEntityDAOSource(final ObjectLocator locator,
         final EntityServiceMapper entityServiceMapper,
         OrderedConfiguration<EntityDAOSource> contributions)
   {
      EntityDAOSource daoSource = new EntityDAOSource()
      {
         @SuppressWarnings("unchecked")
         public <E> EntityDAO<E> get(Class<E> type)
         {
            try
            {
               return locator.getService(entityServiceMapper.getServiceOverrideId(type), EntityDAO.class);
            }
            catch(UnknownValueException e)
            {
               return null;
            }
         }

      };
      contributions.add("Default", daoSource, "after:*");
   }

   public void contributeApplicationStatePersistenceStrategySource(MappedConfiguration<String, ApplicationStatePersistenceStrategy> configuration)
   {
      configuration.addInstance("entity", EntityApplicationStatePersistenceStrategy.class);
   }

   public EntityValidator build(ChainBuilder chainBuilder, List<EntityValidator> contribution)
   {
      return chainBuilder.build(EntityValidator.class, contribution);
   }

   public CriteriaSource buildCriteriaSource(StrategyBuilder builder,
         @SuppressWarnings("rawtypes") Map<Class, CriteriaSource> registrations)
   {
      registrations.put(Object.class, new DefaultCriteriaSource());
      return builder.build(CriteriaSource.class, registrations);
   }
}
