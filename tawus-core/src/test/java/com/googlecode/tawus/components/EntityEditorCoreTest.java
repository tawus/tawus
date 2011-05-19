package com.googlecode.tawus.components;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;

import java.lang.annotation.Annotation;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.BeanValidationContext;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.test.TapestryTestCase;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.testng.annotations.Test;

import com.googlecode.tawus.RegistrationData;

public class EntityEditorCoreTest extends TapestryTestCase {
   @Test
   public void object_created_as_needed() {
      ComponentResources resources = mockComponentResources();
      BeanModelSource source = mockBeanModelSource();
      @SuppressWarnings("rawtypes")
      BeanModel model = mockBeanModel();
      RegistrationData data = new RegistrationData();
      Messages messages = mockMessages();
      PropertyOverrides overrides = mockPropertyOverrides();
      Environment env = EasyMock.createNiceMock(Environment.class);

      train_getBoundType(resources, "object", RegistrationData.class);

      train_createEditModel(source, RegistrationData.class, messages, model);

      train_getOverrideMessages(overrides, messages);

      expect(model.newInstance()).andReturn(data);

      replay();
      EasyMock.replay(env);

      EntityEditor component = new EntityEditor();

      component.inject(resources, overrides, source, env);

      component.doPrepare();

      assertSame(component.getObject(), data);

      verify();
   }

   @Test
   public void object_can_not_be_instantiated() {
      ComponentResources resources = mockComponentResources();
      BeanModelSource source = mockBeanModelSource();
      @SuppressWarnings("rawtypes")
      BeanModel model = mockBeanModel();
      Location l = mockLocation();
      Throwable exception = new RuntimeException("Fall down go boom.");
      PropertyOverrides overrides = mockPropertyOverrides();
      Messages messages = mockMessages();
      Environment env = EasyMock.createNiceMock(Environment.class);

      train_getOverrideMessages(overrides, messages);

      train_getBoundType(resources, "object", Runnable.class);

      train_createEditModel(source, Runnable.class, messages, model);

      expect(model.newInstance()).andThrow(exception);

      train_getCompleteId(resources, "Foo.bar");

      train_getLocation(resources, l);

      expect(model.getBeanType()).andReturn(Runnable.class);

      replay();
      EasyMock.replay(env);

      EntityEditor component = new EntityEditor();

      component.inject(resources, overrides, source, env);

      try {
         component.doPrepare();
         unreachable();
      } catch (TapestryException ex) {
         assertMessageContains(ex,
               "Exception instantiating instance of java.lang.Runnable (for component \'Foo.bar\'):");

         assertSame(ex.getLocation(), l);
      }

      verify();
   }

   private static BeanEditContext contextEq() {
      EasyMock.reportMatcher(new IArgumentMatcher() {
         public void appendTo(StringBuffer buf) {
            buf.append("BeanEditContextEq(RegistrationData.class)");
         }

         public boolean matches(Object argument) {
            return (argument instanceof BeanEditContext)
                  && ((BeanEditContext) argument).getBeanClass() == RegistrationData.class;
         }
      });

      return null;
   }

   @Test
   public void beaneditcontext_pushed_to_environment() {
      ComponentResources resources = mockComponentResources();
      BeanModelSource source = mockBeanModelSource();
      @SuppressWarnings("rawtypes")
      BeanModel model = mockBeanModel();
      Environment env = mockEnvironment();
      RegistrationData data = new RegistrationData();
      Messages messages = mockMessages();
      PropertyOverrides overrides = mockPropertyOverrides();

      train_getBoundType(resources, "object", RegistrationData.class);

      train_createEditModel(source, RegistrationData.class, messages, model);

      train_getOverrideMessages(overrides, messages);

      expect(model.newInstance()).andReturn(data);

      expect(env.peek(eq(BeanValidationContext.class))).andReturn(null);

      expect(model.getBeanType()).andReturn(RegistrationData.class);

      BeanEditContext ctxt = new BeanEditContext() {
         public Class<?> getBeanClass() {
            return RegistrationData.class;
         }

         public <T extends Annotation> T getAnnotation(Class<T> type) {
            return null;
         }
      };

      expect(env.push(eq(BeanEditContext.class), contextEq())).andReturn(ctxt);
      replay();

      EntityEditor component = new EntityEditor();

      component.inject(resources, overrides, source, env);

      component.doPrepare();

      verify();
   }

   @Test
   public void refresh_bean_validation_context() {
      ComponentResources resources = mockComponentResources();
      BeanModelSource source = mockBeanModelSource();
      @SuppressWarnings("rawtypes")
      BeanModel model = mockBeanModel();
      Environment env = mockEnvironment();
      RegistrationData data = new RegistrationData();
      Messages messages = mockMessages();
      PropertyOverrides overrides = mockPropertyOverrides();
      BeanValidationContext beanValidationContext = newMock(BeanValidationContext.class);

      train_getBoundType(resources, "object", RegistrationData.class);

      train_createEditModel(source, RegistrationData.class, messages, model);

      train_getOverrideMessages(overrides, messages);

      expect(model.newInstance()).andReturn(data);

      expect(env.peek(eq(BeanValidationContext.class))).andReturn(beanValidationContext);

      expect(env.pop(eq(BeanValidationContext.class))).andReturn(beanValidationContext);

      expect(env.push(eq(BeanValidationContext.class), isA(BeanValidationContext.class))).andReturn(
            beanValidationContext);

      expect(model.getBeanType()).andReturn(RegistrationData.class);

      BeanEditContext ctxt = new BeanEditContext() {
         public Class<?> getBeanClass() {
            return RegistrationData.class;
         }

         public <T extends Annotation> T getAnnotation(Class<T> type) {
            return null;
         }
      };

      expect(env.push(eq(BeanEditContext.class), contextEq())).andReturn(ctxt);
      replay();

      EntityEditor component = new EntityEditor();

      component.inject(resources, overrides, source, env);

      component.doPrepare();

      verify();
   }

   @Test
   public void beaneditcontext_popped_from_environment() {
      ComponentResources resources = mockComponentResources();
      BeanModelSource source = mockBeanModelSource();
      Environment env = mockEnvironment();
      PropertyOverrides overrides = mockPropertyOverrides();

      expect(env.pop(BeanEditContext.class)).andReturn(null);

      replay();

      EntityEditor component = new EntityEditor();

      component.inject(resources, overrides, source, env);

      component.cleanupEnvironment();

      verify();
   }
}
