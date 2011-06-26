package com.googlecode.tawus.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.BeanValidationContext;
import org.apache.tapestry5.internal.BeanValidationContextImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.TawusEvents;

@SupportsInformalParameters
public class ListEditor implements ClientElement {

   @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
   private String clientId;
   private String assignedClientId;

   @Inject
   @Property(write = false)
   private ComponentResources resources;

   /** Model to be used for the current object */
   @SuppressWarnings( { "unused" })
   @Property
   @Parameter
   private BeanModel<?> model;

   /**
    * If the edit form has to be read only. This is to dynamically choose
    * between BeanTableDisplay and BeanTableEditor based on if the object is to
    * be displayed readonly or editable.
    */
   @Parameter(allowNull = false, value = "false", defaultPrefix = BindingConstants.LITERAL)
   @Property
   private boolean readOnly;

   /** New properties to be add */
   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String add;

   /** Properties to be included */
   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String include;

   @SuppressWarnings( {  "rawtypes" })
   @Parameter(required = true)
   @Property
   private List source;

   @Property
   @Parameter(required = true)
   private Object value;

   @SuppressWarnings("unused")
   @Parameter
   @Property
   private ValueEncoder<?> encoder;
   
   @SuppressWarnings("unused")
   @Parameter
   private int index;
   
   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.BLOCK)
   private Block addRow;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   @Inject
   private Block write;

   @Inject
   private Block read;

   @Inject
   private Environment environment;
   

   /**
    * Return read or write block based on whether the parameter readOnly is true
    * or false
    * 
    * @return read block if readOnly is true otherwise write block
    */
   public Block getReadWriteBlock() {
      return readOnly ? read : write;
   }

   public void doPrepare() {
      if (value != null) {

      }
   }

   private Class<?> getEntityType() {
      return resources.getBoundType("value");

   }

   void setupRender() {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
      if(source != null){
         index = source.size();
      }
   }

   public String getClientId() {
      return assignedClientId;
   }

   @SuppressWarnings("unchecked")
   Object onAddRow() {
      Object obj;
      try {
         obj = getEntityType().newInstance();
         resources.triggerEvent(TawusEvents.LIST_ADD_ROW, new Object[]{obj}, null);
         source.add(obj);
         index = source.size();
         environment.push(BeanValidationContext.class,
               new BeanValidationContextImpl(obj));
         return obj;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   void onRemoveRow(Object object) {
      source.remove(object);
      index = source.size();
   }
}
