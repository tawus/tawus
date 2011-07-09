package com.googlecode.tawus.addons.internal;


/**
 * This is passed in the environment by a {@com.googlecode.tawus.addons.components.TabPanel}
 * for {@com.googlecode.tawus.addons.components.Tab} 
 */
public interface TabContext
{
   /**
    * Returns true if the given tab id is the active tab
    */
   boolean isActiveTab(String tabId);
}
