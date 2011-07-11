// 
// Copyright 2011 Taha Hafeez Siddiqi (tawushafeez@gmail.com)
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.googlecode.tawus.addons.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * A component to display a date in a given format. The format can be specified as a style like
 * short, medium, long or full which correspond to {@link java.text.DateFormat#SHORT}, 
 * {@link java.text.DateFormat#MEDIUM}, {@link java.text.DateFormat#LONG} or 
 * {@link java.text.DateFormat#FULL} respectively.
 * 
 * A suffix of "_t" to a style will correspond to a time format where as a suffix of "_dt" will
 * correspond to a date time format.
 * 
 * You can also use a date pattern like "dd/mm/yyyy"
 */
@SupportsInformalParameters
public class DateDisplay
{
   /**
    * Date to display
    */
   @Parameter(required = true)
   private Date value;

   /**
    * Date format to use for displaying the date.
    */
   @Parameter(value = "medium", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
   private String format;

   @Inject
   private Locale locale;

   @Inject
   private ComponentResources resources;

   public DateDisplay()
   {
      
   }
   
   DateDisplay(String format, Date value, Locale locale)
   {
      this.format = format;
      this.value = value;
      this.locale = locale;
   }
   
   boolean beginRender(MarkupWriter writer)
   {
      writer.element("span");
      resources.renderInformalParameters(writer);
      if(value != null)
      {
         writer.write(getDateFormat().format(value));
      }
      writer.end();
      return false;
   }
   
   private boolean isSuffixPresent()
   {
      return format.indexOf("_") != -1;
   }
   
   private String formatWithoutSuffix()
   {
      return format.substring(0, format.indexOf("_"));
   }

   DateFormat getDateFormat()
   {
      int style = getStyle(isSuffixPresent() ? formatWithoutSuffix() :format);
      
      DateFormat dateFormat;
      if(style < 0)
      {
         //Suffix has no significance
         return new SimpleDateFormat(format, locale);
      }

      if(format.endsWith("_t"))
      {
         dateFormat = DateFormat.getTimeInstance(style, locale);
      }
      else if(format.endsWith("_dt"))
      {
         dateFormat = DateFormat.getDateTimeInstance(style, style, locale);
      }
      else
      {
         dateFormat = DateFormat.getDateInstance(style, locale);
      }
      
      return dateFormat;
   }

   int getStyle(String style)
   {
      int formatType = -1;
      if("long".equals(style))
      {
         formatType = DateFormat.LONG;
      }
      else if("medium".equals(style))
      {
         formatType = DateFormat.MEDIUM;
      }
      else if("full".equals(style))
      {
         formatType = DateFormat.FULL;
      }
      else if("short".equals(style))
      {
         formatType = DateFormat.SHORT;
      }
      return formatType;
   }

}

