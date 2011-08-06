package com.googlecode.tawus.internal.transform;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.FieldHandle;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodDescription;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.googlecode.tawus.EntitySelectModel;
import com.googlecode.tawus.EntityValueEncoder;
import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.annotations.InjectEntitySelectSupport;
import com.googlecode.tawus.services.EntityDAOLocator;

public class InjectEntitySelectSupportWorker implements ComponentClassTransformWorker2
{

   private EntityDAOLocator locator;

   public InjectEntitySelectSupportWorker(final EntityDAOLocator locator)
   {
      this.locator = locator;
   }

   public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model)
   {
      for(final PlasticField field : plasticClass.getFieldsWithAnnotation(InjectEntitySelectSupport.class))
      {
         addModelMethod(plasticClass, field);
         addEncoderMethod(plasticClass, field);
      }
   }

   void addEncoderMethod(PlasticClass plasticClass, PlasticField field)
   {
      String name = stripCriteriaIfPresent(field.getName());

      PlasticMethod method = createEncoderMethod(plasticClass, name);
      PlasticField encoderField = plasticClass.introduceField(EntityValueEncoder.class, getFieldName(name, "Encoder"));

      final FieldHandle handle = field.getHandle();
      final FieldHandle encoderHandle = encoderField.getHandle();

      method.addAdvice(new MethodAdvice()
      {

         @SuppressWarnings({ "unchecked", "rawtypes" })
         public void advise(MethodInvocation invocation)
         {
            Object instance = invocation.getInstance();

            SearchCriteria criteria = (SearchCriteria) handle.get(instance);
            if(criteria == null)
            {
               throw new RuntimeException("SearchCriteria cannot be null when annotated with @"
                     + InjectEntitySelectSupport.class.getSimpleName());
            }

            if(encoderHandle.get(instance) == null)
            {
               encoderHandle.set(instance, new EntityValueEncoder(locator.get(criteria.getType())));
            }

            invocation.setReturnValue(encoderHandle.get(instance));
         }

      });
   }

   private PlasticMethod createEncoderMethod(PlasticClass plasticClass, String name)
   {
      String methodName = "get" + capitalize(name) + "Encoder";

      MethodDescription description = new MethodDescription(EntityValueEncoder.class.getName(), methodName);

      return plasticClass.introduceMethod(description);
   }

   void addModelMethod(PlasticClass plasticClass, PlasticField field)
   {
      String name = stripCriteriaIfPresent(field.getName());
      PlasticMethod method = createModelMethod(plasticClass, name);

      PlasticField selectField = plasticClass.introduceField(EntitySelectModel.class, getFieldName(name, "Model"));

      final FieldHandle handle = field.getHandle();
      final FieldHandle selectHandle = selectField.getHandle();

      method.addAdvice(new MethodAdvice()
      {

         @SuppressWarnings({ "unchecked", "rawtypes" })
         public void advise(MethodInvocation invocation)
         {
            Object instance = invocation.getInstance();

            SearchCriteria criteria = (SearchCriteria) handle.get(instance);
            if(criteria == null)
            {
               throw new RuntimeException("SearchCriteria cannot be null when annotated with @"
                     + InjectEntitySelectSupport.class.getSimpleName());
            }

            if(selectHandle.get(instance) == null)
            {
               selectHandle.set(instance, new EntitySelectModel(locator.get(criteria.getType()).list(criteria)));
            }

            invocation.setReturnValue(selectHandle.get(instance));
         }

      });
   }

   private PlasticMethod createModelMethod(PlasticClass plasticClass, String name)
   {
      String methodName = "get" + capitalize(name) + "Model";

      MethodDescription description = new MethodDescription(EntitySelectModel.class.getName(), methodName);

      return plasticClass.introduceMethod(description);
   }

   String stripCriteriaIfPresent(String name)
   {
      if(name.endsWith("Criteria") && !name.startsWith("Criteria"))
      {
         name = name.replaceAll("Criteria$", "");
      }
      return name;
   }

   private String capitalize(String name)
   {
      return Character.toUpperCase(name.charAt(0)) + name.substring(1);
   }

   private String getFieldName(String name, String prefix)
   {
      return "_$" + name + prefix;
   }

}
