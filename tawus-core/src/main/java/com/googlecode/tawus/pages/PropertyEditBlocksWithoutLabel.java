package com.googlecode.tawus.pages;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.pages.PropertyEditBlocks;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.PropertyEditContext;

import com.googlecode.tawus.EntitySelectModel;
import com.googlecode.tawus.EntityValueEncoder;
import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.components.SimpleDateField;
import com.googlecode.tawus.internal.table.TdEditorContext;
import com.googlecode.tawus.services.CriteriaSource;
import com.googlecode.tawus.services.EntityDAOLocator;

public class PropertyEditBlocksWithoutLabel extends PropertyEditBlocks {
   @Component(id = "entitySelect", parameters = {
      "encoder=prop:entityValueEncoder", "value=prop:context.propertyValue",
      "label=prop:context.label",
      "model=prop:entitySelectModel", "clientId=prop:context.propertyId",
      "validate=prop:entityValidator" })
   private Select entitySelect;

   @Component(id = "entityPalette", parameters = {
      "encoder=prop:entityListValueEncoder",
      "label=prop:context.label",
      "selected=prop:context.propertyValue",
      "model=prop:entityListSelectModel", "clientId=prop:context.propertyId",
      "validate=prop:entityPaletteValidator" })
   private Palette entityPalette;

   @Component(id = "timeField", parameters = {
      "format=HH:MM:SS",
      "clientId=prop:context.propertyId", "value=context.propertyValue",
      "label=prop:context.label", "validate=prop:timeFieldValidator" })
   private SimpleDateField timeField;

   @Environmental
   private PropertyEditContext context;

   @SuppressWarnings("unused")
   @Environmental
   @Property
   private TdEditorContext editorContext;
   
   @Environmental
   private BeanEditContext beanEditContext;

   @Inject
   private EntityDAOLocator locator;
   
   @Inject
   private CriteriaSource criteriaSource;

   public PropertyEditContext getContext() {
      return context;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   public EntityValueEncoder getEntityValueEncoder() {
      return new EntityValueEncoder(locator.get(getPropertyType()));
   }
   
   @SuppressWarnings({ "rawtypes" })
   public Class getPropertyType(){
      if(Collection.class.isAssignableFrom(context.getPropertyType())){
         return getListEntityType();
      }else {
         return context.getPropertyType();
      }
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public EntitySelectModel getEntitySelectModel() {
      return new EntitySelectModel(locator.get(getPropertyType())
         .list(criteriaSource.get(context.getPropertyType())));
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public EntitySelectModel getEntityListSelectModel() {
      Class<?> type = getListEntityType();
      return new EntitySelectModel(locator.get(getPropertyType()).list(
         (SearchCriteria) criteriaSource.get(type)));
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public EntityValueEncoder getEntityListValueEncoder() {
      return new EntityValueEncoder(locator.get(getPropertyType()));
   }

   private Class<?> getListEntityType() {
      Class<?> clazz = context.getPropertyType();
      if(Collection.class.isAssignableFrom(clazz)){
         try{
            Type[] type = ((ParameterizedType) beanEditContext.getBeanClass()
               .getDeclaredField(context.getPropertyId()).getGenericType())
               .getActualTypeArguments();
            if(type.length == 1){
               return (Class<?>) type[0];
            }
         }catch(NoSuchFieldException nsfe){
            throw new RuntimeException(nsfe);
         }
      }
      throw new RuntimeException("Could not create SelectModel for "
         + getPropertyType());
   }

   public FieldValidator<?> getEntityValidator() {
      return context.getValidator(entitySelect);
   }

   public FieldValidator<?> getEntityPaletteValidator() {
      return context.getValidator(entityPalette);
   }

   public FieldValidator<?> getTimeFieldValidator() {
      return context.getValidator(timeField);
   }

}
