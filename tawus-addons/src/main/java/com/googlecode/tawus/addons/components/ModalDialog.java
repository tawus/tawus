// 
// Copyright 2011 Taha Hafeez Siddiqi (tawushafeez@gmail.com)
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// 
package com.googlecode.tawus.addons.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.addons.TawusAddonsEventConstants;

/**
 * A page based modal dialog based on {@linkplain http://okonet.ru/projects/modalbox/index.html ModalBox} 
 * It is very similar to {@org.apache.tapestry5.corelib.components.PageLink  PageLink} but instead of
 * redirection it opens the window in a modal dialog
 * 
 * You can use the informal parameters to pass arguments to the ModalBox
 */
@SupportsInformalParameters
@Import(library = {
      "modalbox/builder.js", 
      "modalbox/effects.js", 
      "modalbox/modalbox.js", 
      "modalbox/modalboxinit.js"
      } , 
      
      stylesheet = "modalbox/modalbox.css"
)
@Events({TawusAddonsEventConstants.SHOW_DIALOG})
public class ModalDialog implements ClientElement
{
   /**
    * Javascript id, if not supplied is auto-generated
    */
   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String clientId;
  
   /**
    * Client event that will trigger the modal dialog
    */
   @Parameter(value = "click", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String clientEvent;
   
   /**
    * Zone to update when dialog is closed
    */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   /**
    * Context to be passed to the page
    */
   @Parameter
   private Object[] context;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   @Inject
   private ComponentResources resources;

   private String assignedClientId;

   public ModalDialog()
   {

   }

   ModalDialog(JavaScriptSupport javaScriptSupport,
         ComponentResources resources)
   {
      this.javaScriptSupport = javaScriptSupport;
      this.resources = resources;
   }

   void setupRender()
   {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   void beginRender(MarkupWriter writer)
   {
      writer.element("a", "href", "#", "id", getClientId());
   }

   void afterRender(MarkupWriter writer)
   {
      writer.end();
   }

   @AfterRender
   void addJavaScript()
   {
      JSONObject params = new JSONObject();

      params.put("id", getClientId());
      params.put("event", clientEvent);
      params.put("openLink", getPageLink());
      params.put("closeLink", getCloseLink());
      params.put("zone", zone);
      params.put("options", getInformalParametersAsJSON());

      javaScriptSupport.addInitializerCall("setupModalDialog", params);
   }

   private JSONObject getInformalParametersAsJSON()
   {
      JSONObject modalboxOptions = new JSONObject();
      for(String parameter : resources.getInformalParameterNames())
      {
         modalboxOptions.put(parameter, resources.getInformalParameter(parameter, String.class));
      }
      return modalboxOptions;
   }

   private Object getPageLink()
   {
      Link link = resources.createEventLink(TawusAddonsEventConstants.SHOW_DIALOG, context);
      return link.toAbsoluteURI();
   }

   private String getCloseLink()
   {
      return resources.createEventLink(TawusAddonsEventConstants.CLOSE_DIALOG).toAbsoluteURI();
   }

   public String getClientId()
   {
      return assignedClientId;
   }
}
