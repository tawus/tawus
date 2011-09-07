package com.googlecode.tawus.addons.internal.transform;

import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.FieldAccess;
import org.apache.tapestry5.services.TransformField;
import org.apache.tapestry5.services.TransformMethod;
import org.apache.tapestry5.services.TransformMethodSignature;

import com.googlecode.tawus.addons.annotations.InjectSelectSupport;
import com.googlecode.tawus.addons.internal.SelectSupport;

public class InjectSelectSupportWorker implements ComponentClassTransformWorker {

    private PropertyAccess propertyAccess;
    private TypeCoercer typeCoercer;

    public InjectSelectSupportWorker(PropertyAccess propertyAccess, TypeCoercer typeCoercer) {
       this.propertyAccess = propertyAccess;
       this.typeCoercer = typeCoercer;
    }

    public void transform(ClassTransformation transform, MutableComponentModel model) {
       for (final TransformField field : transform.matchFieldsWithAnnotation(InjectSelectSupport.class)) {
          final InjectSelectSupport annotation = field.getAnnotation(InjectSelectSupport.class);

          String selectSupportPropertyName = field.getName() + annotation.methodSuffix();
          String methodName = "get" + InternalUtils.capitalize(selectSupportPropertyName);

          TransformMethodSignature sig = new TransformMethodSignature(Modifier.PUBLIC,
                SelectSupport.class.getName(), methodName, null, null);

          final TransformMethod method = transform.getOrCreateMethod(sig);

          // Add a field to cache the result
          final TransformField selectSupportField = transform.createField(Modifier.PRIVATE,
                SelectSupport.class.getName(), "_$" + selectSupportPropertyName);
          final FieldAccess selectSupportFieldAccess = selectSupportField.getAccess();

          final FieldAccess access = field.getAccess();
          method.addAdvice(new ComponentMethodAdvice() {

             @SuppressWarnings({ "unchecked", "rawtypes" })
             public void advise(ComponentMethodInvocation invocation) {
                Object instance = invocation.getInstance();
                if (selectSupportFieldAccess.read(instance) == null) {
                   selectSupportFieldAccess.write(
                         instance,
                         new SelectSupport((List) access.read(invocation.getInstance()),
                               annotation.label(), annotation.index(), annotation.type(),
                               propertyAccess, typeCoercer));
                }
                invocation.overrideResult(selectSupportFieldAccess.read(instance));
             }

          });
       }
    }
}