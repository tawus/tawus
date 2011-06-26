package com.googlecode.tawus.extensions.internal.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.internal.services.StringInterner;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.PropertyConduitSource;

public class RuntimePropBindingFactory implements BindingFactory
{
   private final PropertyConduitSource source;

   private final StringInterner interner;

   public RuntimePropBindingFactory(PropertyConduitSource propertyConduitSource, StringInterner interner)
   {
      source = propertyConduitSource;
      this.interner = interner;
   }

   public Binding newBinding(String description, ComponentResources container, ComponentResources component,
         String expression, Location location)
   {
      Object target = container.getComponent();
      @SuppressWarnings("rawtypes")
      Class targetClass = target.getClass();

      PropertyConduit conduit = source.create(targetClass, expression);

      String toString = interner.format("RuntimePropBinding[%s %s(%s)]", description, container.getCompleteId(),
            expression);

      return new RuntimePropBinding(location, target, conduit, toString);

   }

}
