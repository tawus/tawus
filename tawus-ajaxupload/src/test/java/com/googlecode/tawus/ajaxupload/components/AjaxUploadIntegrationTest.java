package com.googlecode.tawus.ajaxupload.components;

import java.io.File;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

public class AjaxUploadIntegrationTest extends SeleniumTestCase
{
   @Test
   public void check_ajax_upload_works_for_single_file() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Single Ajax Upload");
      
      File file = new File("src/test/data/hello.txt");
      
      type("textValue", file.getCanonicalPath());
      type("uploads_file", file.getCanonicalPath());
      Thread.sleep(2000);
      clickAndWait("//input[@type='submit'][1]");
      
      assertText("message", "File uploaded");
      assertText("content", "Hello World");
   }
   
   @Test
   public void check_ajax_upload_works_on_resubmission_after_validation_error() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Single Ajax Upload");
      
      File file = new File("src/test/data/hello.txt");
      
      type("textValue", "Fail");
      type("uploads_file", file.getCanonicalPath());
      Thread.sleep(2000);
      clickAndWait("//input[@type='submit'][1]");
      
      type("//input[@type='text']", "Success");
      clickAndWait("//input[@type='submit'][1]");
      
      assertText("message", "File uploaded");
      assertText("content", "Hello World");
   }
   
   @Test
   public void check_if_remove_works_after_validation_error() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Single Ajax Upload");
      
      File file = new File("src/test/data/hello.txt");
      
      type("textValue", "Fail");
      type("uploads_file", file.getCanonicalPath());
      Thread.sleep(2000);
      clickAndWait("//input[@type='submit'][1]");
      
      click("link=Remove");
      Thread.sleep(2000);
      click("//input[@type='submit'][1]");
      
      assertText("message", "");
      assertText("content", "");
   }
   
   @Test
   public void check_if_remove_resets_the_file_count() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Single Ajax Upload");
      
      File file = new File("src/test/data/hello.txt");
      
      type("textValue", "Some Text");
      type("uploads_file", file.getCanonicalPath());
      Thread.sleep(2000);
      click("link=Remove");
      Thread.sleep(2000);
      type("uploads_file", file.getCanonicalPath());
      Thread.sleep(2000);
      type("uploads_file", file.getCanonicalPath());
      Thread.sleep(2000);
      clickAndWait("//input[@type='submit'][1]");
      
      assertText("message", "File uploaded");
      assertText("content", "Hello WorldHello World");
   }
   
   
   @Test
   public void check_ajax_upload_works_for_two_files() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Single Ajax Upload");
      
      File file = new File("src/test/data/hello.txt");
      File file2 = new File("src/test/data/goodbye.txt");
      
      type("textValue", file.getCanonicalPath() + ", " + file2.getCanonicalPath());
      type("uploads_file", file.getCanonicalPath());
      type("uploads_file", file2.getCanonicalPath());
      
      Thread.sleep(2000);
      clickAndWait("//input[@type='submit'][1]");
      
      assertText("message", "File uploaded");
      assertText("content", "Hello World Goodbye");
   }

   @Test
   public void check_ajax_upload_fails_for_three_files() throws Exception
   {
      openBaseURL();
      clickAndWait("link=Single Ajax Upload");
      
      File file = new File("src/test/data/hello.txt");
      File file2 = new File("src/test/data/goodbye.txt");
      
      type("textValue", file.getCanonicalPath() + ", " + file2.getCanonicalPath());
      type("uploads_file", file.getCanonicalPath());
      Thread.sleep(1000);
      type("uploads_file", file2.getCanonicalPath());
      Thread.sleep(1000);
      type("uploads_file", file2.getCanonicalPath());
      Thread.sleep(1000);
      
      assertEquals(getAlert(), "Cannot upload file. Only 2 files can be uploaded.");
      Thread.sleep(2000);
      clickAndWait("//input[@type='submit'][1]");
      
      assertText("message", "File uploaded");
      assertText("content", "Hello World Goodbye");
   }
   
}
