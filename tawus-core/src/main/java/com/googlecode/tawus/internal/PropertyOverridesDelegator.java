package com.googlecode.tawus.internal;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.ioc.Messages;

/**
 * Delegates local properties to local {@link org.apache.tapestry5.PropertyOverrides} 
 * and other properties to main {@link org.apache.tapestry5.PropertyOverrides}
 */
public class PropertyOverridesDelegator implements PropertyOverrides {
   private final PropertyOverrides main;
   private final PropertyOverrides local;
   private final List<String> localProperties;

   /**
    * Constructor
    * @param main main {@link org.apache.tapestry5.PropertyOverrides}
    * @param local local {@link org.apache.tapestry5.PropertyOverrides}
    * @param localProperties properties to be locally overridden
    */
   public PropertyOverridesDelegator(final PropertyOverrides main,
         final PropertyOverrides local, final String[] localProperties) {
      this.main = main;
      this.local = local;
      this.localProperties = Arrays.asList(localProperties);
   }

   /**
    * {@inheritDoc}
    */
   public Block getOverrideBlock(final String name) {
      if (localProperties.contains(name)) {
         return local.getOverrideBlock("actionCell");
      } else {
         return main.getOverrideBlock(name);
      }
   }

   /**
    * {@inheritDoc}
    * 
    * Local Messages are not used
    * @return main messages
    */
   public Messages getOverrideMessages() {
      return main.getOverrideMessages();
   }

}
