package com.googlecode.tawus.addons.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A simple tooltip implementation which displays a given block as a tooltip
 */
@SupportsInformalParameters
@Import(library = {"opentip/opentip.js", "opentip/opentip_init.js"}, stylesheet = "opentip/opentip.css")
public class ToolTip
{
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String tip;
   
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String tipTitle;
   
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String tipEvent;
   
   @InjectContainer
   private ClientElement element;
   
   @Inject
   private JavaScriptSupport javaScriptSupport;
   
   @Inject
   private ComponentResources resources;
   
   @AfterRender
   void addJavascript()
   {
      JSONObject params = new JSONObject();
      
      params.put("elementId", element.getClientId());
      params.put("tipTitle", tipTitle);
      params.put("tip", tip);
      
      if(tipEvent != null)
      {
         params.put("url", createAjaxTipEvent());
      }
      
      params.put("options", createOptionsFromInformalParameters());
      
      javaScriptSupport.addInitializerCall("setupOpentip", params);      
   }

   private String createAjaxTipEvent()
   {
      return resources.createEventLink(tipEvent).toURI();
   }

   private JSONObject createOptionsFromInformalParameters()
   {
      JSONObject options = new JSONObject();

      for(String parameterName: resources.getInformalParameterNames())
      {
         options.put(parameterName, resources.getInformalParameter(parameterName, String.class));
      }
      
      return options;
   }

}
