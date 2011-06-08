package com.googlecode.tawus.extensions.components;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;


/**
 * A simple file uploader using <a
 * href='https://github.com/valums/file-uploader'>Valums File Uploader</a>
 * 
 * @author Taha Hafeez
 * 
 */
@SupportsInformalParameters
@Import(library = "fileuploader/fileuploader.js", stylesheet = "fileuploader/fileuploader.css")
public class FileUploader implements ClientElement {

   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   @Inject
   private JavaScriptSupport javaScriptSupport;

   @Inject
   private ComponentResources resources;

   @Parameter(required = true, allowNull = false)
   private List<String> filenames;

   @Parameter
   private String tempDir;

   @Parameter
   private String clientFileName;

   @SuppressWarnings("unused")
   @Parameter
   private boolean uploaded;

   @Parameter(value = "2000000", defaultPrefix = BindingConstants.LITERAL)
   private int maxSize;

   @Parameter(value = "0", defaultPrefix = BindingConstants.LITERAL)
   private int minSize;

   @Parameter(value = "1", defaultPrefix = BindingConstants.LITERAL)
   private int maxFiles;

   @Inject
   private Request request;

   @Inject
   private Messages messages;

   @Inject
   private RequestGlobals globals;

   private String assignedClientId;

   @Parameter(value = "tmp", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
   private String prefix;

   public void beginRender() {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   public void afterRender(final MarkupWriter writer) {
      writer.element("div", "id", getClientId());
      writer.end();

      final Link link = resources.createEventLink("upload");
      final Link cancelLink = resources.createEventLink("cancelUpload");
      JSONObject spec = new JSONObject();
      spec.put("element", new JSONLiteral("document.getElementById('"
            + getClientId() + "')"));
      spec.put("onCancel", new JSONLiteral("function(index){"
            + "Tapestry.ajaxRequest('" + cancelLink.toAbsoluteURI()
            + "/' + index, {method:'get'});" + "}"));
      spec.put("action", link.toAbsoluteURI());
      spec.put("sizeLimit", maxSize);
      spec.put("minSizeLimit", minSize);
      for (String informalParameter : resources.getInformalParameterNames()) {
         spec.put(informalParameter, resources.getInformalParameter(
               informalParameter, String.class));
      }
      javaScriptSupport.addScript("new qq.FileUploader(%s);", spec);
   }

   void onCancelUpload(int index) {
      String tempDirectory = tempDir;
      if (tempDir == null) {
         tempDirectory = System.getProperty("java.io.tmpdir");
      }
      try {
         File file = new File(tempDirectory + File.separator
               + filenames.get(index));
         file.delete();
      } catch (Exception ex) {
         ex.printStackTrace();
      } finally {
         filenames.remove(index);
      }
   }

   Object onUpload() {
      uploaded = false;
      String errorMessage = null;
      clientFileName = request.getParameter("qqfile");
      String suffix = ".tmp";
      if (clientFileName.lastIndexOf('.') != -1) {
         suffix = clientFileName.substring(clientFileName.lastIndexOf('.'));
      }

      if (maxFiles <= filenames.size()) {
         JSONObject spec = new JSONObject();
         spec.put("success", false).put("error",
               messages.format("fileuploader.maxfiles", maxFiles));
         return spec;
      }

      try {
         InputStream in = globals.getHTTPServletRequest().getInputStream();
         ByteArrayOutputStream bout = new ByteArrayOutputStream();
         byte[] buf = new byte[8096];
         while (true) {
            int bytesRead = in.read(buf);
            if (bytesRead == -1) {
               break;
            }
            bout.write(buf, 0, bytesRead);
         }
         bout.close();

         if (bout.size() < minSize) {
            errorMessage = messages.get("fileupload.minsize");
         } else if (bout.size() > maxSize) {
            errorMessage = messages.get("fileupload.maxsize");
         } else {

            FileOutputStream fout = null;
            try {
               File dir = null;
               if (tempDir != null) {
                  dir = new File(tempDir);
                  dir.mkdir();
               }
               File tempFile = File.createTempFile(prefix, suffix, dir);
               fout = new FileOutputStream(tempFile);
               fout.write(bout.toByteArray(), 0, bout.size());
               fout.close();
               uploaded = true;
               filenames.add(tempFile.getName());
            } catch (FileNotFoundException e) {
               errorMessage = e.getMessage();
            } catch (IOException e) {
               errorMessage = e.getMessage();
            } finally {
               if (fout != null) {
                  fout.close();
               }
            }
         }
      } catch (IOException e) {
         errorMessage = messages.get("fileupload.writeerror");
      }

      JSONObject result = new JSONObject();
      if (errorMessage != null) {
         result.put("success", false);
         result.put("error", errorMessage);
      } else {
         result.put("success", true);
      }

      return result;
   }

   public String getClientId() {
      return assignedClientId;
   }
}
