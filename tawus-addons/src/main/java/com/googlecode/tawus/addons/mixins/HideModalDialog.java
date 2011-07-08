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
package com.googlecode.tawus.addons.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A simple mixin to hide {@link com.googlecode.tawus.addons.mixnis}
 */
@Import(library = {
      "../components/modalbox/builder.js", 
      "../components/modalbox/effects.js", 
      "../components/modalbox/modalbox.js", 
      "../components/modalbox/modalboxinit.js"
      } , 
      
      stylesheet = "../components/modalbox/modalbox.css"
)
public class HideModalDialog
{
  
   @Parameter(value = "click", defaultPrefix = BindingConstants.LITERAL)
   private String clientEvent;
   
   @Inject
   private JavaScriptSupport javaScriptSupport;
   
   @InjectContainer
   private ClientElement element;
   
   public HideModalDialog()
   {
      
   }
   
   // For Testing purpose
   HideModalDialog(JavaScriptSupport javaScriptSupport, ClientElement element)
   {
      this.javaScriptSupport = javaScriptSupport;
      this.element = element;
   }

   @AfterRender
   void addJavaScript()
   {
      JSONObject params = new JSONObject();
      params.put("id", element.getClientId());
      params.put("event", clientEvent);
      javaScriptSupport.addInitializerCall("hideModalDialog", params);
   }
}
