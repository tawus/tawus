package com.googlecode.tawus.components;

import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.PropertyEditor;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanBlockSource;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.FormSupport;

import com.googlecode.tawus.internal.table.TdEditorContext;
import com.googlecode.tawus.services.TawusBeanBlockSource;

@SuppressWarnings("unused")
public class EntityPropertyEditor {
   
   static public class SetupEnvironment implements ComponentAction<EntityPropertyEditor> {
      private static final long serialVersionUID = 1L;
      private int columnSpan;
      private String cssClassPrefix;
      private int rowSpan;
      private boolean showHelp;

      public SetupEnvironment(int rowSpan, int columnSpan,
            String cssClassPrefix, boolean showHelp) {
         this.rowSpan = rowSpan;
         this.columnSpan = columnSpan;
         this.cssClassPrefix = cssClassPrefix;
         this.showHelp = showHelp;
      }

      public void execute(EntityPropertyEditor editor) {
         editor.setupEnvironment(rowSpan, columnSpan, cssClassPrefix, showHelp);         
      }
      
   }
   
   static public class CleanupEnvironment implements ComponentAction<EntityPropertyEditor>{
      private static final long serialVersionUID = 1L;

      public void execute(EntityPropertyEditor editor) {
         editor.cleanupEnvironment();
      }
      
   }
   
   private static CleanupEnvironment CLEANUP_ENVIRONMENT = new CleanupEnvironment();
   
   @Component(id = "propertyEditor",  parameters  = {"overrides=inherit:overrides",
         "object=inherit:object", "model=inherit:model", "property=inherit:property", "beanBlockSource=prop:beanBlockSource"})
   private PropertyEditor propertyEditor;

   @Parameter(required = true)
   private int rowSpan;

   @Parameter(required = true)
   private int columnSpan;

   @Parameter(required = true)
   private String cssClassPrefix;

   @Parameter(required = true)
   private boolean showHelp;
   
   @Parameter
   private PropertyOverrides overrides;
   
   @Parameter
   private Object object;
   
   @Parameter
   private BeanModel<?> model;
   
   @Parameter
   private String property;

   @Inject
   private Environment environment;
   
   @Environmental
   private FormSupport formSupport;
   
   @Inject
   private TawusBeanBlockSource beanBlockSource;

   public void setupEnvironment(final int rowSpan, final int columnSpan,
         final String cssClassPrefix,  final boolean showHelp) {
      environment.push(TdEditorContext.class, new TdEditorContext() {

         public int getColumnSpan() {
            return columnSpan;
         }

         public String getCssClassPrefix() {
            return cssClassPrefix;
         }

         public String getHelpText() {
            return EntityPropertyEditor.this.getHelpText();
         }

         public int getRowSpan() {
            return rowSpan;
         }

         public boolean getShowHelp() {
            return showHelp;
         }

      });
   }

   public void cleanupEnvironment() {
      environment.pop(TdEditorContext.class);
   }

   void setupRender(){
      formSupport.storeAndExecute(this, 
            new SetupEnvironment(rowSpan, columnSpan, cssClassPrefix, showHelp
            ));
   }
   
   void cleanupRender(){
      formSupport.storeAndExecute(this, CLEANUP_ENVIRONMENT);
   }
   
   public BeanBlockSource getBeanBlockSource(){
      return beanBlockSource.getBeanBlockSource();
   }
   
   public String getHelpText() {
      final Messages messages = overrides.getOverrideMessages();
      final String message;
      if (messages.contains(property + "-help")) {
         message = messages.get(property + "-help");
      } else {
         message = "";
      }
      return message;
   }
}
