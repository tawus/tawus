package com.googlecode.tawus.extensions.internal.bindings;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.internal.bindings.PropBinding;
import org.apache.tapestry5.ioc.Location;

public class RuntimePropBinding extends PropBinding
{

   public RuntimePropBinding(Location location, Object root, PropertyConduit conduit, String toString)
   {
      super(location, root, conduit, toString);
   }

   @SuppressWarnings("rawtypes")
   public Class getBindingType()
   {
      return get().getClass();
   }
}
