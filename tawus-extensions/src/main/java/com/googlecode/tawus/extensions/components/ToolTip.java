package com.googlecode.tawus.extensions.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * ToolTip component. It uses {@linkplain http
 * ://www.nickstakenburg.com/projects/prototip prototip} Please check the
 * licenses before using it. It is free for non-commercial purposes!!
 */
@SupportsInformalParameters
@Import(library = "prototip/js/prototip.js", stylesheet = "prototip/css/prototip.css")
public class ToolTip implements ClientElement
{
   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false, required = true)
   private String text;

   private String assignedClientId;

   @Inject
   private JavaScriptSupport javaScriptSupport;

   @Inject
   private ComponentResources resources;

   void setupRender()
   {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   void beginRender(final MarkupWriter writer)
   {
      writer.element("span", "id", getClientId());
   }

   void afterRender(final MarkupWriter writer)
   {
      writer.end();
      JSONObject params = new JSONObject();

      for (String name : resources.getInformalParameterNames())
      {
         params.put(name, resources.getInformalParameter(name, String.class));
      }

      javaScriptSupport.addScript("new Tip('%s', '%s', %s)", getClientId(), text.replace('\'', ' '), params.toString());
   }

   public String getClientId()
   {
      return assignedClientId;
   }
}
