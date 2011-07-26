package com.googlecode.tawus.ajaxupload.components;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.upload.internal.services.UploadedFileItem;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.googlecode.tawus.ajaxupload.services.AjaxUploadDecoder;

/**
 * An ajax file uploader using <a
 * href='https://github.com/valums/file-uploader'>Valums File Uploader</a>
 * 
 */
@SupportsInformalParameters
@Import(library = "ajaxupload.js", stylesheet = "ajaxupload.css")
public class AjaxUpload extends AbstractField
{

   public static final String FILE_PARAMETER = "qqfile";
   private static final String STYLE_TO_HIDE_INPUT_TEXT = "display:inline;"
         + "color:transparent;background:transparent;" + "border:0;height:1px;width:1px;";

   @Inject
   private JavaScriptSupport javaScriptSupport;

   @Inject
   private ComponentResources resources;

   @Parameter(required = true, autoconnect = true, principal = true)
   private List<UploadedFile> value;

   @SuppressWarnings("unused")
   @Parameter
   private boolean uploaded;

   @Symbol(UploadSymbols.REQUESTSIZE_MAX)
   @Inject
   private int maxSize;

   @Parameter(value = "1", defaultPrefix = BindingConstants.LITERAL)
   private int maxFiles;

   @Inject
   private Request request;

   @Inject
   private Messages messages;

   @Inject
   private MultipartDecoder multipartDecoder;

   @Inject
   private AjaxUploadDecoder ajaxDecoder;

   /**
    * The object that will perform input validation. The "validate:" binding
    * prefix is generally used to provide this object in a declarative fashion.
    */
   @Parameter(defaultPrefix = BindingConstants.VALIDATE)
   private FieldValidator<Object> validate;

   @Environmental
   private ValidationTracker tracker;

   @SuppressWarnings("unused")
   @Environmental
   private FormSupport formSupport;

   @Inject
   private ComponentDefaultProvider defaultProvider;

   @Inject
   private FieldValidationSupport fieldValidationSupport;

   @SuppressWarnings("unused")
   @Mixin
   private RenderDisabled renderDisabled;

   /**
    * Computes a default value for the "validate" parameter using
    * {@link FieldValidatorDefaultSource}.
    */
   final Binding defaultValidate()
   {
      return defaultProvider.defaultValidatorBinding("value", resources);
   }

   public AjaxUpload()
   {
   }

   // For testing
   AjaxUpload(List<UploadedFile> value, FieldValidator<Object> validate, MultipartDecoder multipartDecoder,
         AjaxUploadDecoder ajaxDecoder, ValidationTracker tracker, ComponentResources resources,
         FieldValidationSupport fieldValidationSupport, JavaScriptSupport javaScriptSupport)
   {
      this.value = value;
      if (validate != null)
         this.validate = validate;
      this.multipartDecoder = multipartDecoder;
      this.tracker = tracker;
      this.resources = resources;
      this.fieldValidationSupport = fieldValidationSupport;
      this.javaScriptSupport = javaScriptSupport;
      this.ajaxDecoder = ajaxDecoder;
      maxFiles = 1;
   }

   void beginRender(MarkupWriter writer)
   {
      writer.element("input", "type", "text", "id", getClientId(), "style", STYLE_TO_HIDE_INPUT_TEXT, "name",
            getControlName());
      validate.render(writer);
      decorateInsideField();
   }

   private String getWrapperClientId()
   {
      return getClientId() + "_wrapper";
   }

   private String getFileControlName()
   {
      return getControlName() + "_file";
   }

   void afterRender(final MarkupWriter writer)
   {
      writer.end();
      writer.element("span", "id", getWrapperClientId(), "style", "display:inline-block");
      writer.end();
   }

   @AfterRender
   void addJavaScript()
   {
      JSONObject arguments = fillArguments();
      javaScriptSupport.addScript("new qq.FileUploader(%s);", arguments);
   }

   JSONObject fillArguments()
   {
      JSONObject spec = new JSONObject();
      for (String informalParameter : resources.getInformalParameterNames())
      {
         spec.put(informalParameter, resources.getInformalParameter(informalParameter, String.class));
      }

      spec.put("element", getElementId());
      spec.put("sizeField", getControlName());
      spec.put("uploadText", messages.get("ajaxupload.upload-text"));
      spec.put("dropText", messages.get("ajaxupload.drop-text"));
      spec.put("action", getUploadLink());
      spec.put("cancelLink", getCancelLink());
      spec.put("removeLink", getRemoveLink());
      spec.put("initializeUploadsLink", getInitializeUploadsLink());
      spec.put("sizeLimit", maxSize < 0 ? 0 : maxSize);
      spec.put("maxFiles", maxFiles);
      spec.put("messages", getErrorMessages());
      spec.put("name", getFileControlName());
      spec.put("id", getFileControlName());
      return spec;
   }

   private Object getElementId()
   {
      return new JSONLiteral("document.getElementById('" + getWrapperClientId() + "')");
   }

   String getUploadLink()
   {
      final Link link = resources.createEventLink("upload");
      return link.toAbsoluteURI();
   }

   String getCancelLink()
   {
      final Link cancelLink = resources.createEventLink("cancelUpload");
      return cancelLink.toAbsoluteURI();
   }

   String getRemoveLink()
   {
      final Link removeLink = resources.createEventLink("removeUpload");
      return removeLink.toAbsoluteURI();
   }

   String getInitializeUploadsLink()
   {
      final Link initializeUploadsLink = resources.createEventLink("initializeUploads");
      return initializeUploadsLink.toAbsoluteURI();
   }
   
   JSONObject getErrorMessages()
   {
      JSONObject errorMessages = new JSONObject();
      
      errorMessages.put("typeError", getErrorMessages("ajaxupload.type-error"));
      errorMessages.put("sizeError", getErrorMessages("ajaxupload.size-error"));
      errorMessages.put("minSizeError", getErrorMessages("ajaxupload.min-size-error"));
      errorMessages.put("emptyError", getErrorMessages("ajaxupload.empty-error"));
      errorMessages.put("onLeave", getErrorMessages("ajaxupload.on-leave"));
      errorMessages.put("maxFilesError", getErrorMessages("ajaxupload.max-files-error"));
     
      return errorMessages;
   }

   private Object getErrorMessages(String key)
   {
      return messages.get(key);
   }

   /**
    * Converts the current list of uploaded files to a JSON object containing a json array
    *  with each element containing the name of the file and a unique key for
    * identification. The unique key is index of the uploaded file in parameter
    * <code>value</code>
    * 
    * @return
    */
   JSONObject onInitializeUploads()
   {
      JSONArray array = new JSONArray();
      if (value != null)
      {
         for (int i = 0; i < value.size(); ++i)
         {
            if (value.get(i) == null)
            {
               continue;
            }
            JSONObject indexWithFileName = new JSONObject();
            indexWithFileName.put("serverIndex", i);
            indexWithFileName.put("fileName", value.get(i).getFilePath());
            array.put(indexWithFileName);
         }
      }
      return new JSONObject().put("uploads", array);
   }

   Object onUpload()
   {
      if (hasMaximumFileUploadCountReached())
      {
         return createFailureResponse(messages.format("ajaxupload.maxfiles", maxFiles));
      }

      final UploadedFile uploadedFile;
      if (isAjaxUpload())
      {
         uploadedFile = createUploadedFileFromRequestInputStream();
      }
      else
      {
         uploadedFile = createUploadedFileFromMultipartForm();
      }

      if (value == null)
      {
         value = new ArrayList<UploadedFile>();
      }

      value.add(uploadedFile);

      return createSuccessResponse(value.size() - 1); // Last index
   }

   private boolean hasMaximumFileUploadCountReached()
   {
      if (value == null)
      {
         return maxFiles <= 0;
      }

      // Can't rely on value's size as some of the values can be null
      int size = 0;
      for (UploadedFile uploadedFile : value)
      {
         if (uploadedFile != null)
         {
            size++;
         }
      }
      return this.maxFiles <= size;
   }

   private boolean isAjaxUpload()
   {
      return ajaxDecoder.isAjaxUploadRequest(request);
   }

   private UploadedFile createUploadedFileFromMultipartForm()
   {
      return multipartDecoder.getFileUpload(FILE_PARAMETER);
   }

   private UploadedFile createUploadedFileFromRequestInputStream()
   {
      return ajaxDecoder.getFileUpload();
   }

   private Object createFailureResponse(String errorMessage)
   {
      JSONObject response = new JSONObject();
      response.put("success", false);
      response.put("error", errorMessage);
      if (!request.isXHR())
      {
         return new StatusResponse(response.toString());
      }
      else
      {
         return response;
      }
   }

   private Object createSuccessResponse(int serverIndex)
   {
      JSONObject response = new JSONObject();
      response.put("success", true);
      response.put("serverIndex", serverIndex);
      if (!request.isXHR())
      {
         return new StatusResponse(response.toString());
      }
      else
      {
         return response;
      }
   }

   void onRemoveUpload(int serverIndex)
   {
      // We use index of an uploadedFile in 'value' as a key at
      // the client side and if the uploaded file is removed we cleanup and set
      // the element at that index to null. As the 'value' may contain null, we
      // need to remove those entries in processSubmission()
      if (value != null && serverIndex >= 0 && serverIndex < value.size())
      {
         UploadedFile item = value.get(serverIndex);
         if (item != null && (item instanceof UploadedFileItem))
         {
            ((UploadedFileItem) item).cleanup();
         }
         value.set(serverIndex, null);
      }
   }

   void onCancelUpload(String fileName)
   {
      // TODO: Some how remove the partially uploaded file
   }

   @Override
   protected void processSubmission(String elementName)
   {
      // Nothing to process from current request as the uploads have already
      // been received and stored in value
      if (value != null)
      {
         // Remove any null values in 'value'
         removeNullsFromValue();
      }

      try
      {
         fieldValidationSupport.validate(value, resources, validate);
      }
      catch (ValidationException ex)
      {
         tracker.recordError(this, ex.getMessage());
      }
   }

   private void removeNullsFromValue()
   {
      List<UploadedFile> uploads = new ArrayList<UploadedFile>();
      for (UploadedFile upload : value)
      {
         if (upload != null)
         {
            uploads.add(upload);
         }
      }
      value = uploads;
   }

   public List<UploadedFile> getValue()
   {
      return value;
   }

   @Override
   public boolean isRequired()
   {
      return value != null && value.size() > 0;
   }

   AjaxUpload injectDecorator(ValidationDecorator decorator)
   {
      setDecorator(decorator);

      return this;
   }

   AjaxUpload injectRequest(Request request)
   {
      this.request = request;

      return this;
   }

   AjaxUpload injectFormSupport(FormSupport formSupport)
   {
      // We have our copy ...
      this.formSupport = formSupport;

      // As does AbstractField
      setFormSupport(formSupport);

      return this;
   }

   AjaxUpload injectFieldValidator(FieldValidator<Object> validator)
   {
      this.validate = validator;

      return this;
   }

   void injectResources(ComponentResources resources)
   {
      this.resources = resources;
   }

   void injectValue(List<UploadedFile> value)
   {
      this.value = value;
   }

   void injectMessages(Messages messages)
   {
      this.messages = messages;
   }

   static class StatusResponse implements StreamResponse
   {

      private String text;

      StatusResponse(String text)
      {
         this.text = text;
      }

      @Override
      public String getContentType()
      {
         return "text/html";
      }

      @Override
      public InputStream getStream() throws IOException
      {
         return new ByteArrayInputStream(text.toString().getBytes());
      }

      @Override
      public void prepareResponse(Response response)
      {

      }

      public JSONObject getJSON()
      {
         return new JSONObject(text);
      }
   }

   public void injectFieldValidationSupport(FieldValidationSupport support)
   {
      this.fieldValidationSupport = support;
   }

}