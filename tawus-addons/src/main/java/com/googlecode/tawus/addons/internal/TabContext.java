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
