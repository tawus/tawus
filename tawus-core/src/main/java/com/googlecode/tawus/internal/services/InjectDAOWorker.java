package com.googlecode.tawus.internal.services;

import java.util.List;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.TransformField;

import com.googlecode.tawus.annotations.InjectDAO;
import com.googlecode.tawus.services.EntityDAOLocator;

public class InjectDAOWorker implements ComponentClassTransformWorker {

   private EntityDAOLocator locator;

   public InjectDAOWorker(EntityDAOLocator locator) {
      this.locator = locator;
   }

   /**
    * {@inheritDoc}
    */
   public void transform(final ClassTransformation transformation,
         final MutableComponentModel model) {
      final List<TransformField> fields = transformation
            .matchFieldsWithAnnotation(InjectDAO.class);
      for (final TransformField field : fields) {
         InjectDAO annotation = field.getAnnotation(InjectDAO.class);
         field.claim(annotation);
         Class<?> entityClass = (Class<?>) annotation.value();
         field.inject(locator.get(entityClass));
      }
   }

}
