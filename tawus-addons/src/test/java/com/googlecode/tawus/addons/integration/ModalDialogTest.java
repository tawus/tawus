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
package com.googlecode.tawus.addons.integration;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class ModalDialogTest extends SeleniumTestCase
{
   
   @Test
   public void check_if_dialog_is_displayed() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Modal Dialog Demo");
      
      click("link=Open Dialog");      
      Thread.sleep(1000);
      
      assertTrue(isElementPresent("MB_window"));
      assertText("MB_caption", "Hello");
      assertText("hellomessage", "Hello World");
      
      click("MB_close");
      Thread.sleep(1000);
      assertFalse(isElementPresent("MB_window"));
   }
   
   @Test
   public void check_if_dialog_acts_as_wizard_in_forward_direction() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Modal Dialog Demo");
      
      click("link=Open Dialog");      
      Thread.sleep(1000);
      
      assertTrue(isElementPresent("MB_window"));
      assertText("MB_caption", "Hello");
      assertText("hellomessage", "Hello World");
      
      click("link=Continue");
      Thread.sleep(1000);
      
      assertTrue(isElementPresent("MB_window"));
      assertText("MB_caption", "Goodbye");
      assertText("goodbyemessage", "Goodbye");
      assertFalse(isElementPresent("hellomessage"));
      
      click("MB_close");
      Thread.sleep(1000);
      assertFalse(isElementPresent("MB_window"));
   }
   
   @Test
   public void check_if_dialog_acts_as_wizard_in_backward_direction() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Modal Dialog Demo");
      
      click("link=Open Dialog");      
      Thread.sleep(1500);
      
      assertTrue(isElementPresent("MB_window"));
      assertText("MB_caption", "Hello");
      assertText("hellomessage", "Hello World");
      
      click("link=Continue");
      Thread.sleep(1500);
      
      assertTrue(isElementPresent("MB_window"));
      assertText("MB_caption", "Goodbye");
      assertText("goodbyemessage", "Goodbye");
      assertFalse(isElementPresent("hellomessage"));
      
      click("link=Back");
      Thread.sleep(1500);
      
      assertTrue(isElementPresent("MB_window"));
      assertText("MB_caption", "Hello");
      assertText("hellomessage", "Hello World");
      
      click("MB_close");
      Thread.sleep(1500);
      assertFalse(isElementPresent("MB_window"));
   }

}
