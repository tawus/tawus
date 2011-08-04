package com.googlecode.tawus.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.TawusEvents;

/**
 * A simple form cancel button implementation
 * 
 */
@Import(library = "cancel-form.js")
public class Cancel implements ClientElement
{
   /**
    * JavaScript id to be used. If id is not supplied, it will be auto-generated
    */
   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   /**
    * Text to be displayed on the button. It is used at the content of the
    * &lt;button&gt; tag
    */
   @Parameter(value = "Cancel", defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
   private String value;

   /**
    * Zone to update
    */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   @Inject
   private ComponentResources componentResources;

   @Inject
   private JavaScriptSupport javaScriptSupport;

   private String assignedClientId;

   @Parameter
   private Object[] context;

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
      writer.element("button", "type", "button", "id", getClientId());
      writer.write(value);
      writer.end();
      
      addJavaScript();
      
      return false;
   }

   private void addJavaScript()
   {
      JSONObject params = new JSONObject();
      params.put("zone", zone);
      params.put("elementId", getClientId());
      params.put("url", getCancelURL());
      javaScriptSupport.addInitializerCall("cancelForm", params);
   }

   private String getCancelURL()
   {
      return componentResources.createEventLink(TawusEvents.CANCEL, context).toAbsoluteURI();
   }

}
