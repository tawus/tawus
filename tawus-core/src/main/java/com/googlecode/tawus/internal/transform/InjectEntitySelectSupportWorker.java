package com.googlecode.tawus.internal.transform;

import java.lang.reflect.Modifier;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.FieldAccess;
import org.apache.tapestry5.services.TransformField;
import org.apache.tapestry5.services.TransformMethod;
import org.apache.tapestry5.services.TransformMethodSignature;

import com.googlecode.tawus.EntitySelectModel;
import com.googlecode.tawus.EntityValueEncoder;
import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.annotations.InjectEntitySelectSupport;
import com.googlecode.tawus.services.EntityDAOLocator;

public class InjectEntitySelectSupportWorker implements ComponentClassTransformWorker
{

   private EntityDAOLocator locator;

   public InjectEntitySelectSupportWorker(final EntityDAOLocator locator)
   {
      this.locator = locator;
   }

   public void transform(ClassTransformation transform, MutableComponentModel model)
   {
      for(final TransformField field : transform.matchFieldsWithAnnotation(InjectEntitySelectSupport.class))
      {
         addModelMethod(transform, field);
         addEncoderMethod(transform, field);
      }
   }

   private void addEncoderMethod(ClassTransformation transform, TransformField field)
   {
      String name = stripCriteriaIfPresent(field.getName());
      String methodName = "get" + capitalize(name) + "Encoder";

      TransformMethodSignature sig = new TransformMethodSignature(Modifier.PUBLIC, EntityValueEncoder.class.getName(),
            methodName, null, null);

      final TransformMethod method = transform.getOrCreateMethod(sig);
      final FieldAccess access = field.getAccess();
      method.addAdvice(new ComponentMethodAdvice()
      {

         @SuppressWarnings({ "unchecked", "rawtypes" })
         public void advise(ComponentMethodInvocation invocation)
         {
            SearchCriteria criteria = (SearchCriteria) access.read(invocation.getInstance());
            invocation.overrideResult(new EntityValueEncoder(locator.get(criteria.getType())));
         }

      });
   }

   private void addModelMethod(ClassTransformation transform, TransformField field)
   {
      String name = stripCriteriaIfPresent(field.getName());

      String methodName = "get" + capitalize(name) + "Model";

      TransformMethodSignature sig = new TransformMethodSignature(Modifier.PUBLIC, EntitySelectModel.class.getName(),
            methodName, null, null);

      final TransformMethod method = transform.getOrCreateMethod(sig);
      final FieldAccess access = field.getAccess();
      method.addAdvice(new ComponentMethodAdvice()
      {

         @SuppressWarnings({ "unchecked", "rawtypes" })
         public void advise(ComponentMethodInvocation invocation)
         {
            SearchCriteria criteria = (SearchCriteria) access.read(invocation.getInstance());
            invocation.overrideResult(new EntitySelectModel(locator.get(criteria.getType()).list(criteria)));
         }

      });
   }

   private String stripCriteriaIfPresent(String name)
   {
      if(name.endsWith("Criteria"))
      {
         name = name.replaceAll("Criteria$", "");
      }
      return name;
   }

   private String capitalize(String name)
   {
      return Character.toUpperCase(name.charAt(0)) + name.substring(1);
   }

}
