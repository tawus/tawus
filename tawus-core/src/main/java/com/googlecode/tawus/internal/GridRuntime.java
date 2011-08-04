package com.googlecode.tawus.internal;

public interface GridRuntime
{
   void showGrid();

   void enableSearch();

   void cancel();

   Object getSearchObject();

   Object getObject();

   String getZone();

   String getZoneId();
}
