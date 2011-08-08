package com.googlecode.tawus.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.components.PropertyDisplay;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import com.googlecode.tawus.internal.table.TableColumn;
import com.googlecode.tawus.internal.table.TableColumnEncoder;

/**
 * An alternative bean display which uses &lt;tr&gt;/&lt;td&gt; tags instead of
 * &lt;div&gt; tags. The <code>include</code> parameter has been modified to
 * accomidate the same. A include parameter can take a list of row
 * configurations. separated by semicolon(',')
 * 
 * <p/>
 * Each row configuration consists of a list of column configurations separated
 * by comma(,)
 * <p/>
 * 
 * Each column configuration consisting of four fields separated by
 * <strong>'/'</strong>. The order is
 * fieldName/columnSeparator/rowSeparator/rowClass
 */
@SupportsInformalParameters
public class EntityDisplay
{

   /* Bean model */
   @SuppressWarnings("rawtypes")
   @Property
   @Parameter
   private BeanModel model;

   /** Bean to be displayed */
   @Parameter(required = true, allowNull = false)
   private Object object;

   /** Properties to be added to the editor */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String add;

   /** Properties to be included in the editor */
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String include;

   /** Column multiple */
   @Parameter(value = "2", allowNull = false)
   private int colspanMultiple;

   /** Property overrides */
   @SuppressWarnings("unused")
   @Parameter(value = "resources", required = true, allowNull = false)
   @Property
   private PropertyOverrides overrides;

   /** If set to true, help text will be show */
   @SuppressWarnings("unused")
   @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
   @Property
   private boolean showHelp;

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

   /* Modal source service */
   @Inject
   @Property
   private BeanModelSource modelSource;

   @Inject
   @Property(write = false)
   private ComponentResources resources;

   @SuppressWarnings("unused")
   @Component(id = "propertyDisplay", parameters = { "overrides=overrides", "object=object", "model=propertyModel" })
   private PropertyDisplay propertyDisplay;

   public PropertyModel getPropertyModel()
   {
      return model.get(currentColumn.getProperty());
   }

   public Object getObject()
   {
      return object;
   }

   @SuppressWarnings("unchecked")
   public List<String> getRows()
   {
      if(include == null)
      {
         return model.getPropertyNames();
      }
      else
      {
         return Arrays.asList(include.trim().split(TableColumn.ROW_SEPARATOR));
      }
   }

   public List<TableColumn> getColumns()
   {
      final List<TableColumn> columns = new ArrayList<TableColumn>();
      for(String col : currentRow.split(TableColumn.COLUMN_SEPARATOR))
      {
         String[] colParts = col.split(TableColumn.FIELD_SEPARATOR);
         columns.add(new TableColumn(colParts, colspanMultiple));
      }

      return columns;
   }

   @SuppressWarnings("unchecked")
   void setupRender()
   {
      if(model == null)
      {
         @SuppressWarnings("rawtypes")
         Class type = resources.getBoundType("object");
         model = modelSource.createDisplayModel(type, resources.getContainerMessages());
         BeanModelUtils.modify(model, add, null, null, null);
      }
   }

   public int getFieldColspan()
   {
      return currentColumn.getColspan() - 1;
   }

}
