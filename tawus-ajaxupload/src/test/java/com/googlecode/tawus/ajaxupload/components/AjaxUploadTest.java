package com.googlecode.tawus.ajaxupload.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.tapestry5.BaseValidationDecorator;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.test.TapestryTestCase;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.testng.annotations.Test;

import com.googlecode.tawus.ajaxupload.AjaxUploadConstants;
import com.googlecode.tawus.ajaxupload.services.AjaxUploadDecoder;

public class AjaxUploadTest extends TapestryTestCase
{
   @Test
   public void upload_is_field() throws Exception
   {
      assertTrue(Field.class.isAssignableFrom(AjaxUpload.class));
   }

   @Test
   public void begin_render_writes_input_tag()
   {
      MarkupWriter writer = createMarkupWriter();
      writer.element("form");

      FormSupport formSupport = mockFormSupport();
      ComponentResources resources = mockComponentResources();
      @SuppressWarnings("unchecked")
      FieldValidator<Object> validator = mockFieldValidator();
      Request request = mockRequest();

      validator.render(writer);
      
      replay();

      AjaxUpload component = new AjaxUpload(null, null, null, null, null, resources, null, null);
      component.injectDecorator(new BaseValidationDecorator()).injectFormSupport(formSupport).injectFieldValidator(
            validator).injectRequest(request);
      component.beginRender(writer);
      Element element = writer.getElement();
      assertNotNull(element);
      assertEquals(element.getName(), "input");

      verify();
   }

   @Test
   public void begin_render_invokes_field_validator() throws Exception
   {
      getMocksControl().checkOrder(true);

      @SuppressWarnings("unchecked")
      FieldValidator<Object> validate = mockFieldValidator();
      ComponentResources resources = mockComponentResources();
      AjaxUpload component = new AjaxUpload(null, validate, null, null, null, resources, null, null);
      MarkupWriter writer = createMarkupWriter();
      writer.element("form");
      Request request = mockRequest();

      FormSupport formSupport = mockFormSupport();

      ValidationDecorator decorator = mockValidationDecorator();

      component.injectDecorator(decorator).injectRequest(request).injectFormSupport(formSupport);

      validate.render(writer);

      decorator.insideField(component);

      replay();

      component.beginRender(writer);

      verify();
   }

   @Test
   public void after_render_closes_element()
   {
      ComponentResources resources = mockComponentResources();
      AjaxUpload component = new AjaxUpload(null, null, null, null, null, resources, null, null);
      MarkupWriter writer = mockMarkupWriter();

      expect(writer.end()).andReturn(null).times(2);
      expect(writer.element("span", "id", "null_wrapper", "style", "display:inline-block")).andReturn(null);

      replay();

      component.afterRender(writer);

      verify();
   }

   @Test
   public void check_upload_link()
   {
      ComponentResources resources = mockComponentResources();

      AjaxUpload component = new AjaxUpload();
      component.injectValue(new Vector<UploadedFile>());
      component.injectResources(resources);
      setupCreateEventLink(resources, "upload", "uploadLink");

      replay();
      assertEquals(component.getUploadLink(), "uploadLink");
      verify();
   }

   @Test
   public void check_js_cancel_function()
   {
      ComponentResources resources = mockComponentResources();

      AjaxUpload component = new AjaxUpload();
      component.injectValue(new Vector<UploadedFile>());
      component.injectResources(resources);
      setupCreateEventLink(resources, "cancelUpload", "cancelUploadLink");

      replay();

      assertEquals(component.getCancelLink(), "cancelUploadLink");

      verify();
   }

   @Test
   public void check_js_remove_function()
   {
      ComponentResources resources = mockComponentResources();

      AjaxUpload component = new AjaxUpload();
      component.injectValue(new Vector<UploadedFile>());
      component.injectResources(resources);
      setupCreateEventLink(resources, "removeUpload", "removeUploadLink");

      replay();

      assertEquals(component.getRemoveLink(), "removeUploadLink");

      verify();
   }

   @Test
   public void check_current_upload_list()
   {
      AjaxUpload component = new AjaxUpload();
      
      List<UploadedFile> value = new ArrayList<UploadedFile>();
      UploadedFile uploadedFile = mockUploadedFile();
      value.add(uploadedFile);

      expect(uploadedFile.getFilePath()).andReturn("test");

      component.injectValue(value);

      replay();

      JSONArray array = ((JSONObject)component.onInitializeUploads()).getJSONArray("uploads");
      assertEquals(array.length(), 1);
      assertTrue(array.get(0) instanceof JSONObject);

      JSONObject element = (JSONObject) array.get(0);
      assertEquals(element.get("serverIndex"), 0);
      assertEquals(element.get("fileName"), "test");

      verify();
   }

   private void setupCreateEventLink(ComponentResources resources, String event, String url)
   {
      Link link = mockLink();
      expect(link.toAbsoluteURI()).andReturn(url);
      expect(resources.createEventLink(event)).andReturn(link);
   }

   @Test
   public void check_upload_calls_ajax_decoder_for_ajax_upload() throws Exception
   {
      AjaxUploadDecoder ajaxDecoder = mockAjaxUploadDecoder();
      UploadedFile uploadedFile = mockUploadedFile();
      Request request = mockRequest();

      AjaxUpload component = new AjaxUpload(null, null, null, ajaxDecoder, null, null, null, null);
      component.injectRequest(request);

      expect(request.isXHR()).andReturn(true).anyTimes();
      expect(ajaxDecoder.getFileUpload()).andReturn(uploadedFile);
      expect(ajaxDecoder.isAjaxUploadRequest(request)).andReturn(true);

      replay();

      Object response = component.onUpload();
      assertTrue(response instanceof JSONObject);

      JSONObject responseJSON = (JSONObject) response;
      assertTrue((Boolean) responseJSON.get("success"));
      assertEquals((Integer) responseJSON.get("serverIndex"), new Integer(0));

      verify();

      assertEquals(component.getValue().size(), 1);
      assertSame(component.getValue().get(0), uploadedFile);
   }

   @Test
   public void check_upload_calls_multipart_decoder_for_non_ajax_upload() throws Exception
   {
      MultipartDecoder multipartDecoder = mockMultipartDecoder();
      AjaxUploadDecoder ajaxDecoder = mockAjaxUploadDecoder();
      UploadedFile uploadedFile = mockUploadedFile();
      Request request = mockRequest();

      AjaxUpload component = new AjaxUpload(null, null, multipartDecoder, ajaxDecoder, null, null, null, null);
      component.injectRequest(request);

      expect(request.isXHR()).andReturn(false).anyTimes();
      expect(ajaxDecoder.isAjaxUploadRequest(request)).andReturn(false);

      expect(multipartDecoder.getFileUpload(AjaxUploadConstants.FILE_PARAMETER)).andReturn(uploadedFile);

      replay();

      Object response = component.onUpload();

      assertTrue(response instanceof AjaxUpload.StatusResponse);
      JSONObject responseJSON = ((AjaxUpload.StatusResponse) response).getJSON();
      assertTrue((Boolean) responseJSON.get("success"));
      assertEquals((Integer) responseJSON.get("serverIndex"), new Integer(0));

      verify();

      assertEquals(component.getValue().size(), 1);
      assertSame(component.getValue().get(0), uploadedFile);
   }

   @Test
   public void check_upload_fails_if_max_file_count_is_reached() throws Exception
   {
      AjaxUploadDecoder ajaxDecoder = mockAjaxUploadDecoder();
      UploadedFile uploadedFile = mockUploadedFile();
      Request request = mockRequest();

      AjaxUpload component = new AjaxUpload(null, null, null, ajaxDecoder, null, null, null, null);
      component.injectRequest(request);

      List<UploadedFile> value = new ArrayList<UploadedFile>();
      value.add(uploadedFile);
      component.injectValue(value);

      Messages messages = mockMessages();
      expect(messages.format("ajaxupload.maxfiles", 1)).andReturn("Max files reached");
      component.injectMessages(messages);

      expect(request.isXHR()).andReturn(true).anyTimes();
      replay();

      JSONObject response = (JSONObject) component.onUpload();

      verify();

      assertFalse((Boolean) response.get("success"));
      assertEquals((String) response.get("error"), "Max files reached");
   }

   @Test
   public void check_on_remove_works_for_null_value()
   {
      AjaxUpload component = new AjaxUpload();
      component.injectValue(new Vector<UploadedFile>());
      component.onRemoveUpload(0);
   }

   @Test
   public void check_on_remove_works_for_boundary_values()
   {
      AjaxUpload component = new AjaxUpload();
      
      // Set 2 elements
      List<UploadedFile> uploadedFiles = new Vector<UploadedFile>();
      uploadedFiles.add(mockUploadedFile());
      uploadedFiles.add(mockUploadedFile());

      component.injectValue(uploadedFiles);

      component.onRemoveUpload(-1);
      component.onRemoveUpload(0);
      component.onRemoveUpload(1);
      component.onRemoveUpload(2);
      component.onRemoveUpload(3);
   }

   @Test
   public void check_on_remove_works_if_same_value_is_removed_twice()
   {
      AjaxUpload component = new AjaxUpload();

      // Set 2 elements
      List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
      uploadedFiles.add(mockUploadedFile());
      uploadedFiles.add(mockUploadedFile());

      component.injectValue(uploadedFiles);

      component.onRemoveUpload(1);
      component.onRemoveUpload(1);

      // Assert the size stays same
      assertEquals(component.getValue().size(), 2);
   }

   @Test
   public void check_after_removal_process_submission_removes_all_null_values_from_list() throws ValidationException
   {
      ComponentResources resources = mockComponentResources();
      FieldValidationSupport support = mockFieldValidationSupport();

      @SuppressWarnings("unchecked")
      FieldValidator<Object> validate = mockFieldValidator();

      AjaxUpload component = new AjaxUpload(null, validate, null, null, null, resources, support, null);

      // Set 2 elements
      List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
      uploadedFiles.add(mockUploadedFile());
      uploadedFiles.add(mockUploadedFile());

      component.injectValue(uploadedFiles);
      
      component.onRemoveUpload(1);

      // Assert the size stays same
      assertEquals(component.getValue().size(), 2);
      component.processSubmission("test");
      assertEquals(component.getValue().size(), 1);
   }

   protected final UploadedFile mockUploadedFile()
   {
      return newMock(UploadedFile.class);
   }

   protected final MultipartDecoder mockMultipartDecoder()
   {
      return newMock(MultipartDecoder.class);
   }

   protected final AjaxUploadDecoder mockAjaxUploadDecoder()
   {
      return newMock(AjaxUploadDecoder.class);
   }
}
