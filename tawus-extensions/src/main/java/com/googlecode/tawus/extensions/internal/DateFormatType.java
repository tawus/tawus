package com.googlecode.tawus.extensions.internal;

import java.text.DateFormat;

public enum DateFormatType
{
   LONG(DateFormat.LONG), MEDIUM(DateFormat.MEDIUM), SHORT(DateFormat.SHORT);

   private int type;

   private DateFormatType(int type)
   {
      this.type = type;
   }

   public int type()
   {
      return type;
   }
}
