package com.googlecode.tawus.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import com.googlecode.tawus.TawusEvents;
import com.googlecode.tawus.annotations.XHR;
import com.googlecode.tawus.internal.GridRuntime;

@SupportsInformalParameters
public class EntitySearchForm {
   @Inject
   @Property(write = false)
   private ComponentResources resources;

   @SuppressWarnings("unused")
   @Component(id = "editor", publishParameters = "include", parameters = { "readOnly=false",
         "validate=false", "showHelp=prop:showHelp", "overrides=this", "add=add", "model=model",
         "object=grid.searchObject" })
   private EntityEditor editor;

   @SuppressWarnings("rawtypes")
   @Property
   @Parameter
   private BeanModel model;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   @Property
   private String add;

   @Parameter(allowNull = false, required = true, defaultPrefix = BindingConstants.COMPONENT)
   @Property
   private GridRuntime grid;

   @SuppressWarnings("unused")
   @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
   @Property
   private boolean showHelp;

   @Parameter(defaultPrefix = BindingConstants.BLOCK)
   private Block buttonPanel;

   @Inject
   private Block defaultButtonPanel;

   Block defaultButtonPanel() {
      return defaultButtonPanel;
   }

   public Block getButtonPanel() {
      return buttonPanel;
   }

   @Inject
   private BeanModelSource beanModelSource;

   private boolean search;

   /**
    * On prepare
    */
   @SuppressWarnings("unchecked")
   void onPrepareFromForm() {
      resources.triggerEvent(EventConstants.PREPARE, null, null);
      if (model == null) {
         @SuppressWarnings("rawtypes")
         Class beanType = grid.getSearchObject().getClass();
         model = beanModelSource.createEditModel(beanType, resources.getContainerMessages());
         BeanModelUtils.modify(model, add, null, null, null);
      }
   }

   void onSelectedFromSearch() {
      search = true;
   }

   void onValidateFromForm() {
      resources.triggerEvent(EventConstants.VALIDATE, null, null);
   }

   void onSuccess() {
      if (search) {
         grid.showGrid();
         grid.enableSearch();
         resources.triggerEvent(TawusEvents.SEARCH, null, null);
      } else {
         grid.cancel();
         resources.triggerEvent(TawusEvents.CANCEL_SEARCH, null, null);
      }
   }

   @XHR
   Object onSubmitFromForm() {
      if (grid.getZone() != null) {
         return resources.getContainerResources().getEmbeddedComponent(grid.getZone());
      } else {
         return null;
      }
   }

}
