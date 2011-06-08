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
package com.googlecode.tawus.extensions.internal.tabs;

import java.util.Map;

import org.apache.tapestry5.ClientElement;

public class TabContext {
   private final String currentTab;
   private final String method;
   private final Map<String, ClientElement> linkMap;

   public TabContext(String currentTab, String method, Map<String, ClientElement> linkMap){
      this.currentTab = currentTab;
      this.method = method;
      this.linkMap = linkMap;
   }

   public String getCurrentTab(){
      return currentTab;
   }

   public Map<String, ClientElement> getLinkMap(){
      return linkMap;
   }

   public String getMethod(){
      return method;
   }
}
