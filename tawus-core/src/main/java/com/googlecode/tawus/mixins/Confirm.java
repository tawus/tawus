package com.googlecode.tawus.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Confirm mixin. Prompts a javascript confirmation dialog box
 * when the associated button is clicked
 * 
 * @author Taha Hafeez
 */
@Import(library = "confirm.js")
public class Confirm {

   @InjectContainer
   private ClientElement container;
   
   @Inject
   private JavaScriptSupport javaScriptSupport;
   
   @Parameter(value = "confirm.message", defaultPrefix = BindingConstants.MESSAGE)
   private String confirmMessage;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "click")
   private String clientEvent;
   
   @org.apache.tapestry5.annotations.BeforeRenderTemplate
   void addScript(){
      JSONObject json = new JSONObject();
      json.put("id", container.getClientId());
      json.put("message", confirmMessage);
      json.put("event", clientEvent);
      
      javaScriptSupport.addScript("new ConfirmDialog(%s);", json.toString());
   }
}
