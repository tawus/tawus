package com.googlecode.tawus.addons.integration;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class InjectSelectSupportTest extends SeleniumTestCase {

    @Test
    public void inject_select_support_should_insert_encoder_and_model_into_the_page(){
        openBaseURL();
        clickAndWait("link=InjectSelectSupport Demo");
        
        assertTrue(isElementPresent("user"));
        select("user", "label=haya");
        clickAndWait("//input[@type='submit']");
        assertEquals(getText("selected"), "haya");
    }
}
