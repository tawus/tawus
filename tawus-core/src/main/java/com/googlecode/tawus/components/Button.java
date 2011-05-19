package com.googlecode.tawus.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = "button.js")
public class Button implements ClientElement {
   @Inject
   private ComponentResources resources;

   @Inject
   private PageRenderLinkSource pageRenderLinkSource;

   public static final String CANCEL_TYPE = "cancel";
   public static final String BUTTON_TYPE = "button";
   public static final String PAGE_TYPE = "page";
   public static final String SUBMIT_TYPE = "submit";

   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   private String assignedClientId;

   @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private boolean disabled;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
   private String type;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false, value = "click")
   private String event;

   @Parameter
   private List<?> context;

   @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
   private String pageName;
   
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   private Object[] contextArray;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "click")
   private Object clientEvent;
   
   public String getClientId() {
      return assignedClientId;
   }

   void setupRender() {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
      contextArray = context == null ? new Object[0] : context.toArray();
   }

   void beginRender(MarkupWriter writer) {
      writer.element("button", "type", type, "id", getClientId());
      resources.renderInformalParameters(writer);
   }

   void afterRender(MarkupWriter writer) {
      if (!disabled
            && (PAGE_TYPE.equalsIgnoreCase(type) || BUTTON_TYPE
                  .equalsIgnoreCase(type))) {
         Link link;

         if (PAGE_TYPE.equalsIgnoreCase(type)) {
            if (pageName == null) {
               throw new RuntimeException("pageName is required if type is "
                     + PAGE_TYPE);
            }
            link = pageRenderLinkSource.createPageRenderLinkWithContext(
                  pageName, resources.isBound("context"), contextArray);
         } else {
            link = resources.createEventLink(event, contextArray);
         }
         final JSONObject spec = new JSONObject();
         spec.put("url", link.toAbsoluteURI());
         spec.put("id", getClientId());
         spec.put("zone", zone);
         spec.put("event", clientEvent);
         javaScriptSupport.addScript(String.format("new ButtonEvent(%s);", spec
               .toString()));
      }

      writer.end();// button
   }

}
