/*
 * Copyright 2010 Taha Hafeez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.tawus.extensions.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.extensions.TabConstants;
import com.googlecode.tawus.extensions.internal.tabs.TabContext;

public class Tab implements ClientElement
{
   /** Request method to use */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String method;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
   private String title;

   @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
   private boolean disabled;

   @Inject
   private ComponentResources resources;

   @Environmental
   private TabContext tabContext;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   private String assignedClientId;

   boolean setupRender()
   {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
      if (method == null)
      {
         method = tabContext.getMethod();
      }

      /* Render only if it is the current tab or the method is javaScript */
      if (TabConstants.JAVASCRIPT.equalsIgnoreCase(method))
      {
         javaScriptSupport.addScript(String.format("TabPanel.showTab('%s', '%s');",
               tabContext.getLinkMap().get(resources.getId()).getClientId(), getClientId()));
         return true;
      }

      if (tabContext.getCurrentTab().equals(resources.getId()))
      {
         return true;
      }

      return false;
   }

   void beginRender(MarkupWriter writer)
   {
      writer.element("div", "id", getClientId(), "class", getCssClass());
   }

   void afterRender(MarkupWriter writer)
   {
      writer.end();
   }

   public String getCssClass()
   {
      if (tabContext.getCurrentTab().equals(resources.getId()))
      {
         return "t-tab-show";
      }
      else
      {
         return "t-tab-hide";
      }
   }

   public String getMethod()
   {
      return method;
   }

   public String getTitle()
   {
      return title;
   }

   public boolean getDisabled()
   {
      return disabled;
   }

   public String getClientId()
   {
      return assignedClientId;
   }
}
