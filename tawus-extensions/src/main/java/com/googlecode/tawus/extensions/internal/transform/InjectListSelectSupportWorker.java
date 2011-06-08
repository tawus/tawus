package com.googlecode.tawus.extensions.internal.transform;

import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.FieldAccess;
import org.apache.tapestry5.services.TransformField;
import org.apache.tapestry5.services.TransformMethod;
import org.apache.tapestry5.services.TransformMethodSignature;

import com.googlecode.tawus.extensions.ListSelectModel;
import com.googlecode.tawus.extensions.annotations.InjectListSelectSupport;

public class InjectListSelectSupportWorker implements ComponentClassTransformWorker {
   
   private PropertyAccess propertyAccess;

   public InjectListSelectSupportWorker(PropertyAccess propertyAccess){
      this.propertyAccess = propertyAccess;
   }

   public void transform(ClassTransformation transform, MutableComponentModel model) {
      for(final TransformField field: transform.matchFieldsWithAnnotation(InjectListSelectSupport.class)){
         final InjectListSelectSupport annotation = field.getAnnotation(InjectListSelectSupport.class);
         
         String methodName = annotation.name();
         if(methodName.trim().equals("")){
            methodName = field.getName() + "Select";
         }
         
         methodName = "get" + InternalUtils.capitalize(methodName);
         
         TransformMethodSignature sig = new TransformMethodSignature(Modifier.PUBLIC, 
            ListSelectModel.class.getName(), methodName, null, null);
         
         final TransformMethod method = transform.getOrCreateMethod(sig);
         final FieldAccess access = field.getAccess();
         method.addAdvice(new ComponentMethodAdvice(){

            @SuppressWarnings({ "unchecked", "rawtypes" })
            public void advise(ComponentMethodInvocation invocation) {
               
               invocation.overrideResult(
                  new ListSelectModel(
                     (List) access.read(invocation.getInstance()), 
                     annotation.label(), annotation.value(), propertyAccess));
            }
            
         });
      }
   }

}
