package com.googlecode.tawus.extensions.internal.transform;

import java.lang.reflect.Modifier;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.TransformMethodSignature;

import com.googlecode.tawus.extensions.annotations.DiscardOnPageReset;

public class DiscardOnPageResetWorker implements ComponentClassTransformWorker
{

   private final TransformMethodSignature PAGE_RESET_SIGNATURE = new TransformMethodSignature(Modifier.PUBLIC, "void",
         "pageReset", null, null);

   public void transform(ClassTransformation transformation, MutableComponentModel model)
   {
      DiscardOnPageReset annotation = transformation.getAnnotation(DiscardOnPageReset.class);
      if (annotation == null)
      {
         return;
      }

      transformation.getOrCreateMethod(PAGE_RESET_SIGNATURE).addAdvice(new ComponentMethodAdvice()
      {
         public void advise(ComponentMethodInvocation invocation)
         {
            invocation.getComponentResources().discardPersistentFieldChanges();
            invocation.proceed();
         }

      });

   }

}
