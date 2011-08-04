package com.googlecode.tawus.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Show Loading in any of the zones that come inside the component to which this
 * mixin is attached
 * 
 * This mixin can be attached to any component implementing
 * {@link org.apache.tapestry5.ClientElement}
 * 
 * @author Taha Hafeez
 * 
 */
@Import(library = "show-loading.js")
public class ShowLoading
{

   @InjectContainer
   private ClientElement container;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   void afterRender()
   {
      JSONObject spec = new JSONObject();
      spec.put("id", container.getClientId());
      javaScriptSupport.addInitializerCall("ShowLoading", spec);
   }
}
