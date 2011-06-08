package com.googlecode.tawus.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.FormValidationControl;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.BeanModelSource;

import com.googlecode.tawus.ActionType;
import com.googlecode.tawus.TawusEvents;
import com.googlecode.tawus.TawusUtils;
import com.googlecode.tawus.annotations.XHR;
import com.googlecode.tawus.internal.GridRuntime;
import com.googlecode.tawus.services.EntityDAOLocator;
import com.googlecode.tawus.services.EntityValidator;

/**
 * An alternative for BeanEditorForm which uses tables instead of divs The
 * reorder parameter has been modified to accomidate the same. A reorder
 * parameter can take a list of row configurations. separated by semicolon(',')
 * <p/>
 * Each row configuration consists of a list of column configurations separated
 * by comma(,)
 * <p/>
 * Each column configuration consisting of four fields separated by
 * <strong>'/'</strong>. The order is
 * fieldName/columnSeparator/rowSeparator/rowClass
 * 
 */
@SupportsInformalParameters
public class EntityEditForm implements FormValidationControl, ClientElement {
   @Inject
   @Property(write = false)
   private ComponentResources resources;

   /** Model to be used for the current object */
   @SuppressWarnings({ "rawtypes" })
   @Property
   private BeanModel model;

   @SuppressWarnings("unused")
   @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
   @Property
   private boolean showHelp;

   /** Object to be edited */
   @Parameter(value = "prop:grid.object", allowNull = false)
   @Property
   private Object object;

   @Parameter(defaultPrefix = BindingConstants.COMPONENT)
   @Property
   private GridRuntime grid;

   /** New properties to be add */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   @Property
   private String add;

   /** Properties to be included */
   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   @Property
   private String include;

   /** Whether the object is deletable */
   @SuppressWarnings("unused")
   @Parameter(value = "false", allowNull = false)
   @Property
   private boolean deletable;

   @SuppressWarnings("unused")
   @Parameter(value = "true", allowNull = false)
   @Property
   private boolean cancelable;

   @SuppressWarnings("unused")
   @Parameter(value = "true", allowNull = false)
   @Property
   private boolean updatable;

   /** Button panel to use. By default the defaultButtonPanel will be used */
   @SuppressWarnings("unused")
   @Parameter(value = "block:defaultButtonPanel", defaultPrefix = BindingConstants.LITERAL)
   @Property
   private Block buttonPanel;

   @Inject
   private EntityDAOLocator locator;

   @Component(parameters = "validationId=componentResources.id")
   private Form form;

   @Inject
   @Symbol(SymbolConstants.FORM_CLIENT_LOGIC_ENABLED)
   private boolean clientLogicDefaultEnabled;

   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   @Property(write = false)
   private boolean clientValidation = clientLogicDefaultEnabled;

   @SuppressWarnings("unused")
   @Parameter
   @Property(write = false)
   private boolean autoFocus = clientLogicDefaultEnabled;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String _zone;

   @Property
   @Persist
   private String zone;
   
   @Inject
   private EntityValidator entityValidator;

   @Inject
   private BeanModelSource beanModelSource;

   /**
    * On prepare
    */
   @SuppressWarnings("unchecked")
   void onPrepareFromForm() {
      resources.triggerEvent(EventConstants.PREPARE, null, null);
      if (model == null) {
         @SuppressWarnings("rawtypes")
         Class beanType = object.getClass();
         model = beanModelSource.createEditModel(beanType, resources.getContainerMessages());
         BeanModelUtils.modify(model, add, null, null, null);
      }
   }
   
   void onPrepareForRenderFromForm(){
      resources.triggerEvent(EventConstants.PREPARE_FOR_RENDER, null, null);
   }
   
   void onPrepareForSubmitFromForm(){
      resources.triggerEvent(EventConstants.PREPARE_FOR_SUBMIT, null, null);
   }

   /**
    * On validate, trigger ENTITY_VALIDATE trigger
    * 
    * @throws ValidationException
    */
   void onValidateFromForm() throws ValidationException {
      entityValidator.validate(object);
      resources.triggerEvent(EventConstants.VALIDATE, new Object[] { object }, null);
   }

   private Object returnValue(ActionType actionType) {
      CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
      resources.triggerEvent(TawusEvents.FINISHED, new Object[]{actionType}, callback);
      if (callback.getResult() != null) {
         return callback.getResult();
      }

      if (grid != null) {
         if (!form.getHasErrors() && !getHasErrors()) {
            grid.showGrid();
         }
         if (zone == null) {
            zone = grid.getZone();
         }
      }

      if (zone != null) {
         return resources.getContainerResources().getEmbeddedComponent(zone);
      } else {
         return null;
      }
   }
   
   public String getZoneId(){
      return zone == null ? null : 
         ((Zone)resources.getContainerResources().getEmbeddedComponent(zone)).getClientId();
   }

   /**
    * If cancel button is hit
    */
   @XHR
   public Object onCancelFromCancel() {
      CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
      resources.triggerEvent(TawusEvents.CANCEL, null, callback);
      if(callback.getResult() != null){
         return callback.getResult();
      }
      
      return returnValue(ActionType.CANCEL);
   }

   /**
    * When delete button is hit
    */
   @SuppressWarnings("unchecked")
   public Object onDeleteFromDelete() {
      try {
         CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
         resources.triggerEvent(TawusEvents.DELETE, null, callback);
         if (callback.getResult() == null) {
            locator.get(getObjectType()).remove(object);
         }
      } catch (Exception ex) {
         ex.printStackTrace();
         this.recordError(ex.getMessage());
      }
      return returnValue(ActionType.DELETE);
   }

   /**
    * On successful submission
    */
   @SuppressWarnings("unchecked")
   public Object onSuccessFromForm() {
      try {
         CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
         resources.triggerEvent(TawusEvents.SAVE, null, callback);
         if (callback.getResult() == null) {
            locator.get(getObjectType()).saveOrUpdate(object);
         }
         return returnValue(ActionType.SAVE);
      } catch (Exception ex) {
         //locator.get(getObjectType()).setIdentifier(object, null);
         recordError(TawusUtils.stripExceptionPrefix(ex.getMessage()));
         return returnValue(ActionType.SAVE);
      }

   }
   
   public Object onFailureFromForm(){
      return returnValue(ActionType.SAVE);
   }

   @SuppressWarnings("rawtypes")
   private Class getObjectType() {
      return object.getClass();
   }

   /**
    * Returns the client id of the embedded form. {@inheritDoc}
    */
   public String getClientId() {
      return form.getClientId();
   }

   /**
    * {@inheritDoc}
    */
   public void clearErrors() {
      form.clearErrors();
   }

   /**
    * {@inheritDoc}
    */
   public boolean getHasErrors() {
      return form.getHasErrors();
   }

   /**
    * {@inheritDoc}
    */
   public boolean isValid() {
      return form.isValid();
   }

   /**
    * {@inheritDoc}
    */
   public void recordError(Field field, String errorMessage) {
      form.recordError(field, TawusUtils.stripExceptionPrefix(errorMessage));
   }

   /**
    * {@inheritDoc}
    */
   public void recordError(String errorMessage) {
      form.recordError(TawusUtils.stripExceptionPrefix(errorMessage));
   }
   
   void setupRender(){
      zone = (_zone != null ? _zone : (grid != null ? grid.getZone(): null));
   }

}
