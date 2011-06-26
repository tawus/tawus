package com.googlecode.tawus.ajaxupload.integration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils
{

   public static String convertStreamToString(InputStream is)
   {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String line = null;
      try
      {
         while ((line = reader.readLine()) != null)
         {
            sb.append(line);
         }
         is.close();
      }
      catch (Exception ex)
      {
         throw new RuntimeException(ex);
      }
      return sb.toString();
   }

}
