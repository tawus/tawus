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
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.addons.internal.TabContext;

/**
 * Tab Panel which acts as a container for the {@Tab tabs}
 * 
 */
@Import(stylesheet = "tab-panel.css")
public class TabPanel implements ClientElement
{
   /**
    * Javascript id. If not provided, one is generated from it using the
    * component id
    */
   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String clientId;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String active;

   /**
    * Zone to be updated on each click
    */
   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   /**
    * Tabs to be displayed
    */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String tabs;

   private String assignedClientId;

   @Property
   private String currentTabId;
   
   @SuppressWarnings("unused")
   @Property
   private int index;

   @Inject
   private JavaScriptSupport javaScriptSupport;

   @Inject
   private ComponentResources resources;

   @Inject
   private Environment environment;
   
   @Inject
   private Request request;

   private String [] tabsCache;

   public TabPanel()
   {

   }

   TabPanel(String tabs,
         String active,
         String currentTabId,
         JavaScriptSupport javaScriptSupport,
         ComponentResources resources,
         Environment environment)
   {
      this.tabs = tabs;
      this.active = active;
      this.currentTabId = currentTabId;
      this.javaScriptSupport = javaScriptSupport;
      this.resources = resources;
      this.environment = environment;
   }

   void setupRender()
   {
      if(tabs == null || getTabs().length == 0)
      {
         throw new IllegalArgumentException("You must specify atleast one tab");
      }
      
      if(active == null)
      {
         active = getTabs()[0];
      }

      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   void beginRender()
   {
      environment.push(TabContext.class, new TabContext()
      {

         public boolean isActiveTab(String tabId)
         {
            return active != null && active.equals(tabId);
         }

      });
   }

   void afterRender()
   {
      environment.pop(TabContext.class);
   }

   public String getClientId()
   {
      return assignedClientId;
   }

   Object onSelectTab(String selected)
   {
      active = selected;
      
      CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
      
      boolean handled = resources.triggerEvent(EventConstants.SELECTED, new Object[]{selected}, callback);
      if(request.isXHR() & !handled)
      {
         throw new TapestryException(String.format("Event %s not handled", EventConstants.SELECTED), null);
      }
      return callback.getResult();
   }

   public String getCssClass()
   {
      return isActiveTab() ? "t-tab-active" : "t-tab-default";
   }

   public boolean isActiveTab()
   {
      return currentTabId.equals(active);
   }
   
   public String getActive()
   {
      return active;
   }

   public Tab getCurrentTab()
   {
      return getTab(currentTabId);
   }

   private Tab getTab(String tabId)
   {
      return (Tab) resources.getContainerResources().getEmbeddedComponent(tabId);
   }
   
   public String [] getTabs()
   {
      if(tabsCache == null)
      {
         tabsCache = TapestryInternalUtils.splitAtCommas(tabs);
      }
      return tabsCache;
   }

}
