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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.Environment;

import com.googlecode.tawus.extensions.TabConstants;
import com.googlecode.tawus.extensions.internal.tabs.TabContext;

/**
 * A tab panel which can contain tabs and can respond to clicks with either
 * javascript, ajax or http-get
 */
@Import(library = "tab-panel.js", stylesheet = "tab-panel.css")
public class TabPanel {

   /**
    * Default request method to be used for all tabs
    */
   @Parameter(defaultPrefix = BindingConstants.LITERAL, value = TabConstants.GET, allowNull = false)
   private String method;

   /** Tab ids embedded in the tab panel */
   @Parameter(principal = true, defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
   private String tabs;

   /** Default tab */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String defaultTab;

   /** Get environment to put TabContext */
   @Inject
   private Environment environment;

   @Inject
   private ComponentResources resources;

   public String[] getTabs() {
      /** Split parameter 'tabs' into a string array */
      return TapestryInternalUtils.splitAtCommas(tabs);
   }

   @Property
   private String tab;

   @Persist
   private String currentTabId;

   @InjectComponent
   private Zone tabPanel;

   @InjectComponent
   private Any javaScriptLink;

   private Map<String, ClientElement> linkMap = new HashMap<String, ClientElement>();

   private TabContext tabContext;

   @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
   private boolean _volatile;

   public Tab getCurrentTab() {
      return (Tab) resources.getContainerResources().getEmbeddedComponent(tab);
   }

   public String getCurrentLinkClass() {
      String cssClass;
      if (getCurrentTab().getDisabled()) {
         cssClass = "t-tab-disabled";
      } else if (currentTabId.equals(((Component) getCurrentTab())
            .getComponentResources().getId())) {
         cssClass = "t-tab-active";
      } else {
         cssClass = "t-tab-enabled";
      }
      return cssClass;
   }

   public String getCurrentMethod() {
      String method = getCurrentTab().getMethod();
      if (method == null) {
         method = this.method;
      }
      if (TabConstants.JAVASCRIPT.equalsIgnoreCase(method)) {
         linkMap.put(((Component) getCurrentTab()).getComponentResources()
               .getId(), javaScriptLink);
      }
      return method;
   }

   public boolean isGetMethod() {
      return TabConstants.GET.equalsIgnoreCase(getCurrentMethod());
   }

   public boolean isAjaxMethod() {
      return TabConstants.AJAX.equalsIgnoreCase(getCurrentMethod());
   }

   public boolean isJavaScriptMethod() {
      return TabConstants.JAVASCRIPT.equalsIgnoreCase(getCurrentMethod());
   }

   void onActionFromLink(String id) {
      if (_volatile) {
         resources.getPage().getComponentResources().discardPersistentFieldChanges();
      }
      currentTabId = id;
   }

   Block onActionFromAjaxLink(String id) {
      currentTabId = id;
      return tabPanel.getBody();
   }

   void setupRender() {
      if (currentTabId == null) {
         currentTabId = defaultTab != null ? defaultTab : getTabs()[0];
      }
   }

   void beforeRenderBody() {
      tabContext = new TabContext(currentTabId, method, linkMap);
      environment.push(TabContext.class, tabContext);
   }

   void afterRenderBody() {
      environment.pop(TabContext.class);
   }

}
