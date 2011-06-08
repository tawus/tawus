package com.googlecode.tawus.extensions.services;

import org.apache.tapestry5.internal.services.StringInterner;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.PropertyConduitSource;

import com.googlecode.tawus.extensions.internal.bindings.PagerBindingFactory;
import com.googlecode.tawus.extensions.internal.bindings.RuntimePropBindingFactory;
import com.googlecode.tawus.extensions.internal.transform.InjectListSelectSupportWorker;

public class TawusExtensionsModule {
   @Contribute(ComponentClassResolver.class)
   public void provideComponentClassResolver(Configuration<LibraryMapping> configuration) {
      configuration.add(new LibraryMapping("tawus", "com.googlecode.tawus.extensions"));
   }
   
   @Contribute(ComponentClassTransformWorker.class)
   public static void provideWorkers(OrderedConfiguration<ComponentClassTransformWorker> workers) {
      workers.addInstance("injectListSelectSupport", InjectListSelectSupportWorker.class);
   }

   public static void contributeBindingSource(
         PropertyConduitSource propertyConduitSource, StringInterner interner,
         MappedConfiguration<String, BindingFactory> configuration) {
      configuration.add("rprop", new RuntimePropBindingFactory(
            propertyConduitSource, interner));
      configuration.add("pager", new PagerBindingFactory());
   }

}
