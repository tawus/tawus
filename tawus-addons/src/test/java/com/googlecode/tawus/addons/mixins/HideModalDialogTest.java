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

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

public class HideModalDialogTest extends TapestryTestCase
{
   
   @Test
   public void check_javascript_support_is_called()
   {
      JavaScriptSupport javaScriptSupport = mockJavaScriptSupport();
      ClientElement element = mockClientElement();
      HideModalDialog component = new HideModalDialog(javaScriptSupport, element);
      
      JSONObject params = new JSONObject();
      params.put("id", "testId");
      javaScriptSupport.addInitializerCall("hideModalDialog", params);
      expect(element.getClientId()).andReturn("testId");
      
      replay();
      
      component.addJavaScript();
      
      verify();
   }
   
   private ClientElement mockClientElement()
   {
      return newMock(ClientElement.class);
   }

}
