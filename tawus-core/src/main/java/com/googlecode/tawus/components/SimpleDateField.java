package com.googlecode.tawus.components;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;

/**
 * A simple date field which does not contain a date picker. It is sometimes
 * required for have a date picker which is required for time only
 */
public class SimpleDateField extends AbstractField {

   @Parameter(required = true, principal = true, autoconnect = true)
   private Date value;

   @Parameter(allowNull = false, defaultPrefix = BindingConstants.LITERAL)
   private DateFormat format;

   @Parameter(defaultPrefix = BindingConstants.VALIDATE)
   private FieldValidator<Object> validate;

   @Environmental
   private ValidationTracker validationTracker;

   @Inject
   private FieldValidationSupport fieldValidationSupport;

   @Inject
   private ComponentDefaultProvider defaultProvider;

   @Inject
   private ComponentResources resources;

   @Inject
   private Request request;

   @Inject
   private Messages messages;

   @Inject
   private Locale locale;

   DateFormat defaultFormat() {
      final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
      dateFormat.setLenient(false);
      return dateFormat;
   }

   final Binding defaultValidate() {
      return defaultProvider.defaultValidatorBinding("value", resources);
   }

   @Override
   protected void processSubmission(String elementName) {
      String value = request.getParameter(elementName);
      validationTracker.recordInput(this, value);

      Date parseValue = null;
      try {
         if (InternalUtils.isNonBlank(value)) {
            parseValue = format.parse(value);
         }
      } catch (ParseException pe) {
         validationTracker.recordError(this, messages.format(
               "date-value-not-parseable", value));
         return;
      }
      putPropertyNameIntoBeanValidationContext("value");
      try {
         fieldValidationSupport.validate(parseValue, resources, validate);
         this.value = parseValue;
      } catch (ValidationException ve) {
         validationTracker.recordError(this, ve.getMessage());
      }

      removePropertyNameFromBeanValidationContext();
   }

   void beginRender(MarkupWriter writer) {
      String value = validationTracker.getInput(this);

      if (value == null) {
         value = formatCurrentValue();
      }

      writer.element("input", "type", "text", "value", value, "id",
            getClientId(), "name", getControlName());
      if (isDisabled()) {
         writer.attributes("disabled", "disabled");
      }

      putPropertyNameIntoBeanValidationContext("value");
      validate.render(writer);
      removePropertyNameFromBeanValidationContext();
      resources.renderInformalParameters(writer);
      decorateInsideField();

      writer.end();
   }

   private String formatCurrentValue() {
      final String value;
      if (this.value == null) {
         value = "";
      } else {
         value = format.format(this.value);
      }
      return value;
   }

   @Override
   public boolean isRequired() {
      return validate.isRequired();
   }

}
