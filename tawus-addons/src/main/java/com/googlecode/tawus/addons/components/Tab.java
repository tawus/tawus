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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.googlecode.tawus.addons.internal.TabContext;

public class Tab
{
   @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String title;
   
   @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private boolean disabled;
   
   @Environmental
   private TabContext tabContext;
   
   @Inject
   private ComponentResources resources;
   
   boolean beginRender()
   {
      return isActiveAndEnabled();
   }
   
   private boolean isActiveAndEnabled()
   {
      return tabContext.isActiveTab(resources.getId()) && !disabled;
   }

   public String getTitle()
   {
      return title;
   }
   
   public void setTitle(String title)
   {
      this.title = title;
   }
   
   public boolean getDisabled()
   {
      return disabled;
   }
   
   public void setDisabled(boolean disabled)
   {
      this.disabled = disabled;
   }
}
