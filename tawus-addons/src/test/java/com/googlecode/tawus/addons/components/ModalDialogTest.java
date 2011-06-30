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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

import com.googlecode.tawus.addons.TawusAddonsEventConstants;

public class ModalDialogTest extends TapestryTestCase
{
   
   @Test
   public void check_if_anchor_tag_is_rendered()
   {
      MarkupWriter writer = mockMarkupWriter();
      
      ModalDialog component = new ModalDialog();
      
      expect(writer.element("a", "href", "#", "id", null)).andReturn(null);
      replay();
      
      component.beginRender(writer);
      
      verify();
   }
   
   @Test
   public void check_anchor_tag_is_closed_in_after_render()
   {
      MarkupWriter writer = mockMarkupWriter();
      
      ModalDialog component = new ModalDialog();
      
      expect(writer.end()).andReturn(null);
      
      replay();
      
      component.afterRender(writer);
      
      verify();
   }
   
   @Test
   public void check_proper_call_to_javascript_function_is_made()
   {
      JavaScriptSupport javaScriptSupport = mockJavaScriptSupport();
      ComponentResources resources = mockComponentResources();

      ModalDialog dialog = new ModalDialog(javaScriptSupport, resources);
      
      expect(resources.getInformalParameterNames()).andReturn(CollectionFactory.newList("testparam"));
      expect(resources.getInformalParameter("testparam", String.class)).andReturn("testvalue");
      Link openLink = mockLink();
      expect(openLink.toAbsoluteURI()).andReturn("testpagelink");
      expect(resources.createEventLink(TawusAddonsEventConstants.SHOW_DIALOG, (Object[])null)).andReturn(openLink);
      
      Link closeLink = mockLink();
      expect(closeLink.toAbsoluteURI()).andReturn("closelink");
      expect(resources.createEventLink(TawusAddonsEventConstants.CLOSE_DIALOG)).andReturn(closeLink);
      
      JSONObject params = new JSONObject();
      params.put("id", null);
      params.put("openLink", "testpagelink");
      params.put("closeLink", "closelink");
      
      JSONObject options = new JSONObject();
      options.put("testparam", "testvalue");
      
      params.put("options", options);
      
      javaScriptSupport.addInitializerCall("setupModalDialog", params);
      
      replay();
      
      dialog.addJavaScript();
      
      verify();
   }

}
