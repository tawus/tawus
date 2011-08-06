package com.googlecode.tawus.internal.transform;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.googlecode.tawus.annotations.InjectDAO;
import com.googlecode.tawus.services.EntityDAOLocator;

public class InjectDAOWorker implements ComponentClassTransformWorker2
{

   private EntityDAOLocator locator;

   public InjectDAOWorker(EntityDAOLocator locator)
   {
      this.locator = locator;
   }

   public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model)
   {
      for(final PlasticField field : plasticClass.getFieldsWithAnnotation(InjectDAO.class))
      {
         InjectDAO annotation = field.getAnnotation(InjectDAO.class);
         field.claim(annotation);
         Class<?> entityClass = (Class<?>) annotation.value();
         field.inject(locator.get(entityClass));
      }
   }

}
