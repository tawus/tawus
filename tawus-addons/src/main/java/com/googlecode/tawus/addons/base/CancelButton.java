package com.googlecode.tawus.addons.base;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * An base class for buttons. These buttons represent the Http &lt;button&gt; tag
 */
public class CancelButton implements ClientElement
{
   @Inject
   private ComponentResources resources;

   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   private String assignedClientId;

   @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private boolean disabled;

   @Parameter
   private Object [] context;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   public String getClientId()
   {
      return assignedClientId;
   }

   void setupRender()
   {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   boolean beginRender(MarkupWriter writer)
   {
      writer.element("button", "type", "", "id", getClientId());
      resources.renderInformalParameters(writer);
      writer.end();
      return false;
   }

}

