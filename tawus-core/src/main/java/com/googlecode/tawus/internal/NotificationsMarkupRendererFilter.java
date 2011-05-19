package com.googlecode.tawus.internal;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.services.Notifications;
import com.googlecode.tawus.services.NotificationsManager;

public class NotificationsMarkupRendererFilter implements MarkupRendererFilter {
   private Environment environment;
   private Asset notificationsScript;
   private NotificationsManager notificationsManager;

   public NotificationsMarkupRendererFilter(Asset notificationsScript,
         Environment environment, NotificationsManager notificationsManager) {
      this.environment = environment;
      this.notificationsManager = notificationsManager;
      this.notificationsScript = notificationsScript;
   }

   public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer) {
      if (environment.peek(JavaScriptSupport.class) != null) {
         Notifications notifications = notificationsManager.getNotifications();
         JavaScriptSupport javaScriptSupport = environment
               .peek(JavaScriptSupport.class);
         if (notifications.getHasMessages()) {
            JSONObject spec = new JSONObject();
            spec.put("informations", new JSONArray(notifications
                  .getInformations().toArray()));
            spec.put("warnings", new JSONArray(notifications.getWarnings()
                  .toArray()));
            spec.put("errors", new JSONArray(notifications.getErrors()
                  .toArray()));
            String url = notificationsScript.toClientURL();
            if (!url.endsWith("/")) {
               url = url.substring(0, url.lastIndexOf("/") + 1);
            }
            spec.put("url", url);
            javaScriptSupport.importJavaScriptLibrary(notificationsScript);
            javaScriptSupport.addScript(InitializationPriority.LATE,
                  "Notifications.display(%s);", spec);
            notifications.clear();
         }
      }
      renderer.renderMarkup(writer);
   }
}
