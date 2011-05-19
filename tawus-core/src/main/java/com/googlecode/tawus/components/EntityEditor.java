package com.googlecode.tawus.components;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.components.PropertyDisplay;
import org.apache.tapestry5.corelib.internal.InternalMessages;
import org.apache.tapestry5.internal.BeanValidationContext;
import org.apache.tapestry5.internal.BeanValidationContextImpl;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.FormSupport;

import com.googlecode.tawus.annotations.NonUpdatable;
import com.googlecode.tawus.internal.table.TableColumn;
import com.googlecode.tawus.internal.table.TableColumnEncoder;
import com.googlecode.tawus.services.EntityDAOLocator;

/**
 * An alternative bean editor which uses table instead of divs The reorder
 * parameter has been modified to accomidate the same. A reorder parameter can
 * take a list of row configurations. separated by comma(,)
 * <p/>
 * Each row configuration consists of a list of column configurations separated
 * by semicolon(',')
 * <p/>
 * Each column configuration consisting of four fields separated by
 * <strong>'/'</strong>. The order is
 * fieldName/columnSeparator/rowSeparator/rowClass
 */
@SupportsInformalParameters
public class EntityEditor {

   /**
    * Component action to prepare the model and object
    */
   static class Prepare implements ComponentAction<EntityEditor> {
      private static final long serialVersionUID = 1L;

      /**
       * {@inheritDocs}
       */
      public void execute(EntityEditor component) {
         component.doPrepare();
      }

      /**
       * {@inheritDocs}
       */
      public String toString() {
         return "BeanEditor.doPrepare()";
      }

   }

   /**
    * Component action to cleanup after rendering
    */
   static class CleanupEnvironment implements ComponentAction<EntityEditor> {
      private static final long serialVersionUID = 1L;

      /**
       * {@inheritDocs}
       */
      public void execute(EntityEditor component) {
         component.cleanupEnvironment();
      }

      /**
       * {@inheritDocs}
       */
      public String toString() {
         return "BeanEditor.cleanupEnviroment()";
      }

   }

   /** Cleanup component environment instance */
   private final CleanupEnvironment CLEANUP_ENVIRONMENT = new CleanupEnvironment();

   /** Object to be edited */
   @Parameter(autoconnect = true)
   private Object object;

   @SuppressWarnings("unused")
   @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
   @Property
   private boolean showHelp;

   @Parameter(value = "true", allowNull = false)
   @Property
   private boolean updatable;

   @Inject
   private Environment environment;

   @Inject
   @Property(write = false)
   private ComponentResources resources;

   /** Bean model, if not provided one is generated at setup */
   @SuppressWarnings("rawtypes")
   @Parameter
   @Property
   private BeanModel model;

   /** Properties to be added to the editor */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String add;

   /** Properties to be included in the editor for removal */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String include;

   /** Colspan multiple to be used **/
   @Parameter(value = "2", allowNull = false)
   private int colspanMultiple;

   /** Property field to be used */
   @SuppressWarnings("unused")
   @Component(parameters = { "object=object", "overrides=overrides", "model=model",
         "showHelp=prop:showHelp", "cssClassPrefix=currentColumn.property",
         "columnspan=prop:fieldColspan", "rowspan=prop:currentColumn.rowspan",
         "property=prop:currentColumn.property" })
   private EntityPropertyEditor propertyField;
   
   @SuppressWarnings("unused")
   @Component(id = "propertyDisplay", parameters = { "overrides=overrides",
         "object=object", "model=propertyModel" })
   private PropertyDisplay propertyDisplay;
   

   /** Property Overrides to be used, by default use container's resources */
   @Parameter(value = "this", allowNull = false)
   @Property(write = false)
   private PropertyOverrides overrides;

   /** Current row */
   @Property
   private String currentRow;

   /** Current column */
   @Property
   private TableColumn currentColumn;

   /** Column encoder */
   @SuppressWarnings("unused")
   @Property
   private TableColumnEncoder columnEncoder;

   private Object cachedObject;

   /* Form support for executing ComponentActions */
   @Environmental
   private FormSupport formSupport;

   /* Bean model source to generate model */
   @Inject
   @Property
   private BeanModelSource modelSource;

   @Inject
   private EntityDAOLocator locator;

   @Inject
   private Block _read;

   @Inject
   private Block _write;

   public Block getComponent() {
      return getReadOnly() ? _read : _write;
   }

   /**
    * Get object being edited
    * 
    * @return object being edited
    */
   public Object getObject() {
      return cachedObject;
   }

   public boolean getReadOnly() {
      return !updatable
            || (locator.get(object.getClass()).getIdentifier(getObject()) != null && model.get(
                  currentColumn.getProperty()).getAnnotation(NonUpdatable.class) != null);
   }

   /**
    * Get rows
    */
   @SuppressWarnings("unchecked")
   public List<String> getRows() {
      if (include == null) {
         return model.getPropertyNames();
      } else {
         return Arrays.asList(include.trim().split(TableColumn.ROW_SEPARATOR));
      }
   }

   /**
    * Get columns of a particular row
    */
   public List<TableColumn> getColumns() {
      final List<TableColumn> columns = new ArrayList<TableColumn>();
      for (String col : currentRow.split(TableColumn.COLUMN_SEPARATOR)) {
         String[] colParts = col.split(TableColumn.FIELD_SEPARATOR);
         columns.add(new TableColumn(colParts, colspanMultiple));
      }

      return columns;
   }
   
   public PropertyModel getPropertyModel(){
      return model.get(currentColumn.getProperty());
   }
   
   public int getFieldColspan() {
      return currentColumn.getColspan() - 1;
   }

   @SuppressWarnings("unchecked")
   void doPrepare() {
      if (model == null) {
         @SuppressWarnings("rawtypes")
         Class type = object != null ? object.getClass() : resources.getBoundType("object");
         model = modelSource.createEditModel(type, overrides.getOverrideMessages());
         BeanModelUtils.modify(model, add, null, null, null);
      }

      if (object == null) {
         try {
            object = model.newInstance();
         } catch (Exception ex) {
            String message = InternalMessages.failureInstantiatingObject(model.getBeanType(),
                  resources.getCompleteId(), ex);
            throw new TapestryException(message, resources.getLocation(), ex);
         }
         refreshBeanValidationContext();
      }

      /** Push bean context */
      BeanEditContext context = new BeanEditContext() {
         public Class<?> getBeanClass() {
            return model.getBeanType();
         }

         public <T extends Annotation> T getAnnotation(Class<T> type) {
            return getBeanClass().getAnnotation(type);
         }
      };

      cachedObject = object;
      environment.push(BeanEditContext.class, context);
   }

   private void refreshBeanValidationContext() {
      if (environment.peek(BeanValidationContext.class) != null) {
         environment.pop(BeanValidationContext.class);
         environment.push(BeanValidationContext.class, new BeanValidationContextImpl(object));
      }
   }

   void setupRender() {
      formSupport.storeAndExecute(this, new Prepare());
   }

   void cleanupRender() {
      formSupport.storeAndExecute(this, CLEANUP_ENVIRONMENT);
   }

   public void cleanupEnvironment() {
      environment.pop(BeanEditContext.class);
   }

   void inject(ComponentResources resources, PropertyOverrides overrides,
         BeanModelSource source, Environment env) {
      this.resources = resources;
      this.overrides = overrides;
      this.modelSource = source;
      this.environment = env;
      
   }

}
