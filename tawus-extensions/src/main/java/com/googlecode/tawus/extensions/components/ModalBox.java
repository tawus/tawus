package com.googlecode.tawus.extensions.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * ModalBox support for Tapestry5 Check <a
 * href='http://okonet.ru/projects/modalbox/index.html'>ModalBox</a> for details
 * This component uses version 1.5.5
 * 
 * @author Taha Hafeez
 * 
 */
@Import(library = { "modal/effects.js", "modal/builder.js",
      "modal/modalbox.js", "modal/modalboxinit.js" }, stylesheet = "modal/modalbox.css")
@SupportsInformalParameters
public class ModalBox implements ClientElement {

   public static final String EVENT_TYPE = "event";
   public static final String PAGE_TYPE = "page";

   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   @Inject
   private JavaScriptSupport javaScriptSupport;

   private String assignedClientId;

   @Parameter(value = EVENT_TYPE, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String type;

   @Parameter(value = "event", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String event;
   
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String closeEvent;

   @Parameter(value = "click", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String clientEvent;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String pageName;

   @Inject
   private ComponentResources resources;

   @Inject
   private PageRenderLinkSource pageRenderLinkSource;

   @Parameter
   private List<?> context;

   private Object[] contextArray;

   @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
   private boolean disabled;

   /**
    * Setup render
    */
   void setupRender() {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
      contextArray = context == null ? new Object[] {} : context.toArray();

      if (!type.equals(EVENT_TYPE) && !type.equals(PAGE_TYPE)) {
         throw new RuntimeException("Parameter type can only be " + EVENT_TYPE
               + " or " + PAGE_TYPE);
      }

      if (type.equals(PAGE_TYPE) && pageName == null) {
         throw new RuntimeException(
               "Parameter pageName cannot be null if parameter type is "
                     + PAGE_TYPE);
      }
   }

   void beginRender(final MarkupWriter writer) {
      writer.element("a", "href", "#", "id", getClientId());
   }

   void afterRender(final MarkupWriter writer) {
      writer.end();

      if (disabled) {
         return;
      }

      final Link link;
      if (EVENT_TYPE.equalsIgnoreCase(type)) {
         link = resources.createEventLink(event, contextArray);
      } else { // if(PAGE_TYPE.equals(type)){
         link = pageRenderLinkSource.createPageRenderLinkWithContext(pageName,
               contextArray);
      }
      
      String closeLink;
      if(closeEvent != null){
         closeLink = resources.createEventLink(event).toAbsoluteURI();
      }else {
         closeLink = null;
      }
      
      final JSONObject params = new JSONObject();
      for (String informalParameter : resources.getInformalParameterNames()) {
         params.put(informalParameter, resources.getInformalParameter(
               informalParameter, String.class));
      }

      final JSONObject spec = new JSONObject();
      spec.put("id", getClientId());
      spec.put("href", link.toAbsoluteURI());
      spec.put("event", clientEvent);
      spec.put("closeHref", closeLink);
      spec.put("type", type);
      spec.put("params", params);
      javaScriptSupport.addScript("new ModalBoxInit(%s);", spec);

   }

   /**
    * {@inheritDoc}
    */
   public String getClientId() {
      return assignedClientId;
   }

}
