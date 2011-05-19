package com.googlecode.tawus.services;

import java.util.List;

public interface Notifications {
   void inform(String message);
   void warn(String message);
   void error(String error);
   boolean getHasMessages();
   List<String> getInformations();
   List<String> getWarnings();
   List<String> getErrors();
   void clear();
}
