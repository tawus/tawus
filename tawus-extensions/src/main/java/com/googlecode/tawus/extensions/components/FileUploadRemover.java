package com.googlecode.tawus.extensions.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class FileUploadRemover {
   @SuppressWarnings("unused")
   @Parameter(required = true, allowNull = false)
   @Property
   private List<String> fileNames;
   
   @SuppressWarnings("unused")
   @Property
   private String fileName;
   
   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
   private String event;

}
