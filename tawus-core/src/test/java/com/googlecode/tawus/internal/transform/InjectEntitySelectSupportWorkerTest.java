package com.googlecode.tawus.internal.transform;

import java.util.ArrayList;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

import com.googlecode.tawus.annotations.InjectEntitySelectSupport;
import com.googlecode.tawus.services.EntityDAOLocator;

public class InjectEntitySelectSupportWorkerTest extends TapestryTestCase
{

   @Test
   public void annotation_not_present()
   {
      EntityDAOLocator locator = newMock(EntityDAOLocator.class);
      PlasticClass plasticClass = newMock(PlasticClass.class);
      TransformationSupport support = newMock(TransformationSupport.class);
      MutableComponentModel model = newMock(MutableComponentModel.class);

      expect(plasticClass.getFieldsWithAnnotation(InjectEntitySelectSupport.class)).andReturn(new ArrayList<PlasticField>());

      replay();

      new InjectEntitySelectSupportWorker(locator).transform(plasticClass, support, model);

      verify();
   }

   @Test
   public void strip_criteria_if_present()
   {
      EntityDAOLocator locator = newMock(EntityDAOLocator.class);
      InjectEntitySelectSupportWorker worker = new InjectEntitySelectSupportWorker(locator);
      
      assertEquals(worker.stripCriteriaIfPresent("something"), "something");
      assertEquals(worker.stripCriteriaIfPresent("myCriteria"), "my");
      assertEquals(worker.stripCriteriaIfPresent("criteria"), "criteria");
      assertEquals(worker.stripCriteriaIfPresent("Criteria"), "Criteria");
   }
}
