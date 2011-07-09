package com.googlecode.tawus.addons;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

public class ModalDialogUtils
{
   public static JSONObject JSONToCloseDialog()
   {
      return new JSONObject().put("inits", getInitializerArray());
   }

   private static Object getInitializerArray()
   {
     return new JSONArray().put(getHideModalDialogInitializer());
   }

   private static Object getHideModalDialogInitializer()
   {
      return new JSONObject().put("hideModalDialog", getParameterList());
   }

   private static Object getParameterList()
   {
      return new JSONArray().put(getEmptyParameter());
   }

   private static Object getEmptyParameter()
   {
      return new JSONObject();
   }
}
