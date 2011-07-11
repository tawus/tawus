package com.googlecode.tawus.addons.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

public class DateDisplayTest extends TapestryTestCase
{
   @Test
   public void test_get_style_works_for_styles()
   {
      DateDisplay component = new DateDisplay();
      
      assertEquals(component.getStyle("long"), DateFormat.LONG);
      assertEquals(component.getStyle("short"), DateFormat.SHORT);
      assertEquals(component.getStyle("full"), DateFormat.FULL);
      assertEquals(component.getStyle("medium"), DateFormat.MEDIUM);
   }
   
   @Test
   public void test_get_style_works_for_styles_with_no_suffix()
   {
      Locale locale = defaultLocale();
      DateDisplay component = new DateDisplay("medium", new Date(), locale);
      
      DateFormat dateFormat = component.getDateFormat();
      
      assertEquals(dateFormat, DateFormat.getDateInstance(DateFormat.MEDIUM, locale));
   }
 
   @Test
   public void test_get_style_works_for_styles_with_time_suffix()
   {
      Locale locale = defaultLocale();
      DateDisplay component = new DateDisplay("short_t", new Date(), locale);

      
      DateFormat dateFormat = component.getDateFormat();
      
      assertEquals(dateFormat, DateFormat.getTimeInstance(DateFormat.SHORT, locale));
   }
   
   @Test
   public void test_get_style_works_for_styles_with_date_time_suffix()
   {
      Locale locale = defaultLocale();
      DateDisplay component = new DateDisplay("long_dt", new Date(), locale);
      
      DateFormat dateFormat = component.getDateFormat();
      
      assertEquals(dateFormat, DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale));
   }
   
   
   @Test
   public void test_get_style_works_for_patterns()
   {
      Locale locale = defaultLocale();
      String pattern = "dd/mm/yyyy";
      DateDisplay component = new DateDisplay(pattern, new Date(), locale);
      
      DateFormat dateFormat = component.getDateFormat();
      
      assertEquals(dateFormat, new SimpleDateFormat(pattern, locale));
   }
   
   @Test
   public void test_get_style_works_for_patterns_with_underscores()
   {
      Locale locale = defaultLocale();
      String pattern = "dd_mm_yyyy";
      DateDisplay component = new DateDisplay(pattern, new Date(), locale);
      
      DateFormat dateFormat = component.getDateFormat();
      
      assertEquals(dateFormat, new SimpleDateFormat(pattern, locale));
   }
   
   Locale defaultLocale()
   {
      return Locale.getDefault();
   }
}
