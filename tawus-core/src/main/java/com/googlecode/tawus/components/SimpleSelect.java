package com.googlecode.tawus.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.data.BlankOption;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A simple implementation of select which takes a list and creates a select
 * component from it. The content of <code>&lt;option&gt;</code> tag is supplied
 * by the label parameter and the value by key parameter.
 * 
 */
@SupportsInformalParameters
@Events({ EventConstants.VALIDATE,
      EventConstants.VALUE_CHANGED + " when 'zone' parameter is 'bound'" })
public class SimpleSelect extends AbstractField {
   public static final String CHANGE_EVENT = "change";

   @Parameter(required = true, allowNull = false)
   private List<?> items;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String label;

   @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
   private String key;

   @Parameter(autoconnect = true, required = true)
   private Object value;

   @Parameter(value = "auto", defaultPrefix = BindingConstants.LITERAL)
   private BlankOption blankOption;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String blankLabel;

   @Parameter(defaultPrefix = BindingConstants.VALIDATE)
   private FieldValidator<Object> validate;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   @Inject
   private Request request;

   @Inject
   private ComponentResources resources;

   @Inject
   private ComponentDefaultProvider defaultProvider;

   @Environmental
   private ValidationTracker tracker;

   @Inject
   private PropertyAccess propertyAccess;

   @Inject
   private TypeCoercer typeCoercer;

   @Inject
   private FieldValidationSupport fieldValidationSupport;

   @Inject
   private JavaScriptSupport javaScriptSupport;

   @SuppressWarnings("unused")
   @Mixin
   private RenderDisabled renderDisabled;

   private Map<String, PropertyAdapter> adapterMap;

   private final static Pattern PROPERTY_PATTERN = Pattern.compile("#\\{([\\w.$_]+)\\}");

   @Override
   protected void processSubmission(String elementName) {
      String submittedValue = request.getParameter(elementName);
      tracker.recordInput(this, submittedValue);

      Object selectedValue = toValue(submittedValue);
      putPropertyNameIntoBeanValidationContext("value");

      try {
         fieldValidationSupport.validate(selectedValue, resources, validate);
         value = selectedValue;
      } catch (ValidationException ex) {
         tracker.recordError(this, ex.getMessage());
      }

      removePropertyNameFromBeanValidationContext();
   }

   private Object toValue(String submittedValue) {
      if (InternalUtils.isBlank(submittedValue)) {
         return null;
      } else {
         Object submittedKey = typeCoercer.coerce(submittedValue, getPropertyType(key));

         for (Object item : items) {
            Object itemKey = getPropertyValue(item, key);
            if (itemKey.equals(submittedKey)) {
               return item;
            }
         }
         return null;
      }
   }
   
   Object onChange(@RequestParameter(value = "t:selectvalue", allowBlank = true)final String selectValue){
      final Object newValue = toValue(selectValue);
      CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
      this.resources.triggerEvent(EventConstants.VALUE_CHANGED, new Object[]
      { newValue }, callback);
      this.value = newValue;
      return callback.getResult();
  }

   void beginRender(MarkupWriter writer) {
      writer.element("select", "id", getClientId(), "name", getControlName());
      putPropertyNameIntoBeanValidationContext("value");
      validate.render(writer);
      removePropertyNameFromBeanValidationContext();

      resources.renderInformalParameters(writer);
      decorateInsideField();
      if (zone != null) {
         Link link = resources.createEventLink(CHANGE_EVENT);
         JSONObject spec = new JSONObject("selectId", getClientId(), "zoneId", zone, "url",
               link.toURI());
         javaScriptSupport.addInitializerCall("linkSelectToZone", spec);
      }
   }

   void afterRender(MarkupWriter writer) {
      writer.end();
   }

   @BeforeRenderTemplate
   void options(MarkupWriter writer) {
      if (showBlankOption()) {
         writer.element("option", "value", "");
         writer.write(blankLabel);
         writer.end();
      }

      for (Object item : items) {
         writer.element("option", "value", getPropertyValue(item, key));

         if (value != null && getPropertyValue(key).equals(getPropertyValue(item, key))) {
            writer.attributes("selected", "selected");
         }
         writer.write(getLabel(item));
         writer.end();
      }
   }

   private String getLabel(Object item) {
      if (label.contains("#")) {
         if (adapterMap == null) {
            adapterMap = new HashMap<String, PropertyAdapter>();
            Matcher matcher = PROPERTY_PATTERN.matcher(label);
            while (matcher.find()) {
               adapterMap.put(
                     matcher.group(0),
                     propertyAccess.getAdapter(resources.getBoundType("value")).getPropertyAdapter(
                           matcher.group(1)));
            }
         }

         String text = this.label;
         for (String key : adapterMap.keySet()) {
            String value = adapterMap.get(key) == null ? ""
                  : adapterMap.get(key).get(item).toString();
            text = text.replace(key, value);
         }
         return text;
      } else {
         Object propertyValue = getPropertyValue(item, label);
         if(propertyValue == null){
            return "";
         }else {
            return propertyValue.toString();
         }
      }
   }

   private boolean showBlankOption() {
      switch (blankOption) {
         case ALWAYS:
            return true;

         case NEVER:
            return false;

         default:
            return !isRequired();
      }
   }

   String defaultBlankLabel() {
      Messages containerMessages = resources.getContainerMessages();
      String key = resources.getId() + "-blanklabel";
      if (containerMessages.contains(key)) {
         return containerMessages.get(key);
      }
      return null;
   }

   Binding defaultValidate() {
      return defaultProvider.defaultValidatorBinding("value", resources);
   }

   private Object getPropertyValue(String property) {
      return getPropertyValue(value, property);
   }

   private Object getPropertyValue(Object object, String property) {
      if (object == null) {
         return null;
      }
      return propertyAccess.getAdapter(object).getPropertyAdapter(property).get(object);
   }

   private Class<?> getPropertyType(String property) {
      return propertyAccess.getAdapter(resources.getBoundType("value")).getPropertyAdapter(property).getType();
   }

}
