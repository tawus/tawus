package com.googlecode.tawus.internal.services;

import org.apache.tapestry5.services.ApplicationStateManager;

import com.googlecode.tawus.services.Notifications;
import com.googlecode.tawus.services.NotificationsManager;

public class NotificationsManagerImpl implements NotificationsManager {
   
   private ApplicationStateManager stateManager;

   public NotificationsManagerImpl(ApplicationStateManager stateManager){
      this.stateManager = stateManager;
   }

   public Notifications getNotifications() {
      if(!stateManager.exists(Notifications.class)){
         stateManager.set(Notifications.class, new NotificationsImpl());
      }
      return stateManager.get(Notifications.class);
   }

}
