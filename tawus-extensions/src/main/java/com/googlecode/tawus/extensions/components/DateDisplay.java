package com.googlecode.tawus.extensions.components;

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

@SupportsInformalParameters
public class DateDisplay {
   @Parameter(required = true, allowNull = false)
   private Date value;

   @Parameter(value = "medium", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
   private String format;

   @Inject
   private Locale locale;

   @Inject
   private ComponentResources resources;

   boolean beginRender(MarkupWriter writer) {
      writer.element("span");
      resources.renderInformalParameters(writer);      
      writer.write(getDateFormat().format(value));
      writer.end();
      return false;
   }
   
   DateFormat getDateFormat(){
      String actualFormat;
      
      if(format.endsWith("_t")){
         actualFormat = format.substring(0, format.length() - 2);
      }else if(format.endsWith("_dt")){
         actualFormat = format.substring(0, format.length() - 3);
      }else {
         actualFormat = format;
      }
      
      DateFormat dateFormat;
      int style = getStyle(actualFormat);
      if(style < 0){
         dateFormat = new SimpleDateFormat(actualFormat, locale);
      }else {
         if(format.endsWith("_t")){
            dateFormat = DateFormat.getTimeInstance(style, locale);
         }else if(format.endsWith("_dt")){
            dateFormat = DateFormat.getDateTimeInstance(style, style, locale);
         }else {
            dateFormat = DateFormat.getDateInstance(style, locale);
         }
      }
      return dateFormat;
   }
   
   private int getStyle(String style){
      int formatType = -1;
      if ("long".equals(style)) {
         formatType = DateFormat.LONG;
      } else if ("medium".equals(style)) {
         formatType = DateFormat.MEDIUM;
      } else if ("full".equals(style)) {
         formatType = DateFormat.FULL;
      } else if ("short".equals(style)) {
         formatType = DateFormat.SHORT;
      }
      return formatType;
   }
      
      
}
