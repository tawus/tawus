package com.googlecode.tawus.addons.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.addons.internal.TabContext;

/**
 * Tab Panel which acts as a container for the {@Tab tabs}
 * 
 */
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

   Object onSelectTab(int index)
   {
      if(index >= getTabs().length)
      {
         throw new IllegalArgumentException("Invalid tab index : " + index);
      }
      active = getTabs()[index];
      
      if(request.isXHR())
      {
         return ((Zone)resources.getContainerResources().getEmbeddedComponent(zone)).getBody();
      }else {
         return null;
      }
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
