package com.googlecode.tawus.internal.transform;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodDescription;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.googlecode.tawus.annotations.XHR;

public class XHRWorker implements ComponentClassTransformWorker2
{
   private Request request;

   public XHRWorker(Request request)
   {
      this.request = request;
   }

   public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model)
   {
      for(final PlasticMethod method : plasticClass.getMethodsWithAnnotation(XHR.class))
      {
         MethodDescription description = method.getDescription();

         if(!"void".equals(description.returnType))
         {
            method.addAdvice(new MethodAdvice()
            {

               public void advise(MethodInvocation invocation)
               {
                  invocation.proceed();
                  Object result = invocation.getReturnValue();
                  if(!request.isXHR())
                  {
                     if(result != null)
                     {
                        result = defaultForReturnType(result.getClass());
                     }
                  }
                  invocation.setReturnValue(result);
               }

            });
         }
         else
         {
            throw new RuntimeException("XHR can be applied to non-void event handlers only");
         }
      }

   }

   private Object defaultForReturnType(Class<?> returnType)
   {
      if(!returnType.isPrimitive())
         return null;
      if(returnType.equals(boolean.class))
         return false;
      return 0;
   }

}