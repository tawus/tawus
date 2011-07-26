package com.googlecode.tawus.addons.mixins;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A mixin which when added to a form prevents double submission.
 * As the mixin is based on javascript, it is only effective if the javascript
 * is enabled.
 * 
 */
@Import(library = "prevent-double-submission.js")
public class PreventDoubleSubmission
{
   @Inject
   private JavaScriptSupport javaScriptSupport;
   
   @InjectContainer
   private Form form;
   
   @AfterRender
   void addJavaScript()
   {
      javaScriptSupport.addInitializerCall("preventDoubleSubmission", form.getClientId());
   }

}
