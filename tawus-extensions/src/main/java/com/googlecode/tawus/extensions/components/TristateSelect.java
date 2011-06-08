package com.googlecode.tawus.extensions.components;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

import com.googlecode.tawus.extensions.Tristate;

public class TristateSelect {
   @SuppressWarnings("unused")
   @Parameter(required = true, autoconnect = true)
   private Tristate value;
   
   @Inject
   private Messages messages;

   public ValueEncoder<Tristate> getEncoder() {
      return new EnumValueEncoder<Tristate>(Tristate.class);
   }

   public SelectModel getModel() {
      return new EnumSelectModel(Tristate.class, messages);
   }

}
