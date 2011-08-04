package com.googlecode.tawus.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.internal.table.TdEditorContext;

@SuppressWarnings("unused")
public class TdEditor
{

   @Environmental(false)
   @Property
   private TdEditorContext originalContext;

   @Property
   private TdEditorContext context;

   @Parameter(required = true, defaultPrefix = BindingConstants.BLOCK)
   @Property
   private Block label;

   @Parameter(required = true, defaultPrefix = BindingConstants.BLOCK)
   @Property
   private Block editor;

   @Parameter(value = "context.rowSpan")
   @Property
   private int rowSpan;

   @Parameter(value = "context.columnSpan")
   @Property
   private int columnSpan;

   @Parameter(value = "context.cssClassPrefix")
   @Property
   private String cssClassPrefix;

   @Parameter(value = "context.showHelp")
   @Property
   private boolean showHelp;

   @Parameter(value = "context.helpText")
   @Property
   private String helpText;

   void setupRender()
   {
      context = originalContext;
      if(context == null)
      {
         context = new TdEditorContext()
         {

            public int getColumnSpan()
            {
               return 1;
            }

            public String getCssClassPrefix()
            {
               return "";
            }

            public String getHelpText()
            {
               return "";
            }

            public int getRowSpan()
            {
               return 1;
            }

            public boolean getShowHelp()
            {
               return false;
            }

         };
      }
   }

}
