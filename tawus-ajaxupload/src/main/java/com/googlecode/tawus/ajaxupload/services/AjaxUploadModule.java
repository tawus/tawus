package com.googlecode.tawus.ajaxupload.services;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileCleaner;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.upload.internal.services.MultipartDecoderImpl;
import org.apache.tapestry5.upload.services.MultipartDecoder;

import com.googlecode.tawus.ajaxupload.internal.AjaxUploadServletRequestFilter;
import com.googlecode.tawus.ajaxupload.internal.services.AjaxUploadDecoderImpl;

@SuppressWarnings("deprecation")
public class AjaxUploadModule
{
   private static final AtomicBoolean needToAddShutdownListener = new AtomicBoolean(true);

   public static void bind(ServiceBinder binder)
   {
      binder.bind(AjaxUploadDecoder.class, AjaxUploadDecoderImpl.class).scope(ScopeConstants.PERTHREAD);
   }

   @Contribute(ComponentClassResolver.class)
   public void provideComponentClassResolver(Configuration<LibraryMapping> configuration)
   {
      configuration.add(new LibraryMapping("tawus", "com.googlecode.tawus.ajaxupload"));
   }

   @Scope(ScopeConstants.PERTHREAD)
   public static MultipartDecoder buildMultipartDecoder2(RegistryShutdownHub shutdownHub,
         @Autobuild MultipartDecoderImpl multipartDecoder)
   {

      if (needToAddShutdownListener.getAndSet(false))
      {
         shutdownHub.addRegistryShutdownListener(new RegistryShutdownListener()
         {
            @Override
            public void registryDidShutdown()
            {
               FileCleaner.exitWhenFinished();
            }
         });
      }

      return multipartDecoder;
   }

   public static void contributeServiceOverride(@InjectService("MultipartDecoder2") MultipartDecoder multipartDecoder,
         @SuppressWarnings("rawtypes") MappedConfiguration<Class, Object> overrides)
   {
      overrides.add(MultipartDecoder.class, multipartDecoder);
   }

   public static void contributeHttpServletRequestHandler(OrderedConfiguration<HttpServletRequestFilter> configuration,
         AjaxUploadDecoder ajaxUploadDecoder)
   {
      configuration.add("AjaxUploadFilter", new AjaxUploadServletRequestFilter(ajaxUploadDecoder), "after:IgnoredPaths");
   }

}
