package com.googlecode.tawus.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Cancel implements ClientElement {
   @Inject
   private ComponentResources componentResources;

   @Parameter(value="prop:componentResources.id", defaultPrefix=BindingConstants.LITERAL)
   private String clientId;

   private String assignedClientId;

   @Parameter(value="Cancel", defaultPrefix=BindingConstants.LITERAL, required = true, allowNull = false)
   private String value;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   public String getClientId(){
      return assignedClientId;
   }

   void setupRender(){
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   void beginRender(MarkupWriter writer){
      String url = componentResources.createEventLink("action").toAbsoluteURI();
      writer.element("button", "type", "button", "id", getClientId(), 
            "onclick", "javascript:window.location='" + url+"'");
      writer.write(value);
   }

   void afterRender(MarkupWriter writer){
      writer.end();//button
   }

}

