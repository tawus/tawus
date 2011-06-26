package com.googlecode.tawus.ajaxupload.integration.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;

import com.googlecode.tawus.ajaxupload.services.AjaxUploadModule;

@SubModule(AjaxUploadModule.class)
public class TestModule
{

   public static void contributeApplicationDefaults(final MappedConfiguration<String, Object> defaults)
   {
      defaults.add(SymbolConstants.DEFAULT_JAVASCRIPT, "classpath:/media/tapestry.js");
   }

   public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
   {
      configuration.add("media", "media");
   }

}
