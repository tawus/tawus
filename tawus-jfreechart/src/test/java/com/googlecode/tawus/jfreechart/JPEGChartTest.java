// 
// Copyright 2011 Taha Hafeez Siddiqi
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

package com.googlecode.tawus.jfreechart;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class JPEGChartTest extends SeleniumTestCase
{
   @Test
   public void test_image_is_plotted_without_map_by_default()
   {
      openBaseURL();
      clickAndWait("link=JPEGChart Demo");
      
      assertTrue(isElementPresent("//div[@id='chartWithoutMap']"));
      assertTrue(isElementPresent("//div[@id='chartWithoutMap']/img"));
      assertFalse(isElementPresent("//div[@id='chartWithoutMap']/img[@useMap='#chartWithoutMap_map']"));

   }
   
   @Test
   public void test_image_is_plotted_with_map()
   {
      openBaseURL();
      clickAndWait("link=JPEGChart Demo");
      
      assertTrue(isElementPresent("//div[@id='chartWithMap']"));
      assertTrue(isElementPresent("//div[@id='chartWithMap']/img"));
      assertTrue(isElementPresent("//div[@id='chartWithMap']/img[@useMap='#chartWithMap_map']"));

      
      click("//div[@id='chartWithMap']/map/area[1]");
      assertTextUsingJS("zone", "Series = Sales, Category = 2008");

   }
   
   protected void assertTextUsingJS(String id, String text) {
      waitForCondition(String.format(
              "selenium.browserbot.getCurrentWindow().$('%s').innerHTML=='%s'", id, text),
              PAGE_LOAD_TIMEOUT);
  }

}
