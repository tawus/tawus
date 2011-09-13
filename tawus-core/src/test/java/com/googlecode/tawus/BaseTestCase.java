package com.googlecode.tawus;

import org.apache.tapestry5.test.SeleniumTestCase;

public class BaseTestCase extends SeleniumTestCase
{

   protected void assertTextUsingJS(String id, String text) {
       waitForCondition(String.format(
               "selenium.browserbot.getCurrentWindow().document.getElementById('%s').innerHTML=='%s'", id, text),
               PAGE_LOAD_TIMEOUT);
   }

   protected void assertTextNotPresentUsingJS(String id, String text) {
       waitForCondition(String.format(
               "selenium.browserbot.getCurrentWindow().document.getElementById('%s').innerHTML!='%s'", id, text),
               PAGE_LOAD_TIMEOUT);
   }

   protected void assertValueUsingJS(String id, String text) {
       waitForCondition(String.format(
               "selenium.browserbot.getCurrentWindow().document.getElementById('%s').value =='%s'", id, text),
               PAGE_LOAD_TIMEOUT);
   }
   
}
