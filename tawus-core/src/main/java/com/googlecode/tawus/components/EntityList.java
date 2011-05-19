package com.googlecode.tawus.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.googlecode.tawus.EntityGridDataSource;
import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.services.EntityDAOLocator;

/**
 * Entity Grid A full CRUD component for a bean. It is presented as a list. A
 * new item can be added using the new button and existing items can be viewed
 * or modified
 */
public class EntityList {

   /**
    * Grid for displaying list of objects
    */
   @Component(publishParameters = "add, columnIndex,empty,encoder, include, exclude, "
         + "class, lean, pagerPosition, reorder, rowClass, rowsPerPage, overrides,row,volatile"
         + "sortModel, model", parameters = { "source=source", "rowIndex=prop:rowIndex" })
   private Grid grid;

   @Persist
   private EntityGridDataSource<?> source;

   /**
    * Get data source
    * 
    * @return data source
    */
   public EntityGridDataSource<?> getSource() {
      return source;
   }

   /**
    * Search criteria to be used for searching entities
    */
   @Parameter(principal = true, required = true, allowNull = false)
   private SearchCriteria<?> criteria;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private Boolean multiple;

   @Property
   private int rowIndex;

   @Inject
   private EntityDAOLocator locator;

   @Parameter
   private List<Object> selected;

   public Grid getGrid() {
      return grid;
   }
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void setupRender() {
      if(source == null){
        source = new EntityGridDataSource(locator.get(criteria.getType()),
            criteria);
      }
      
      if (getIsMultipleSelection() && selected == null) {
         throw new RuntimeException("In Multiple Selection, selected cannot be null");
      }
   }

   public boolean getIsMultipleSelection() {
      return multiple != null && multiple;
   }

   public boolean getIsSingleSelection() {
      return multiple != null && !multiple;
   }

   public void setSelection(boolean context) {
      Object row = source.getRowValue(rowIndex);
      if (context) {
         selected.add(row);
      } else {
         selected.remove(row);
      }
   }

   public boolean getSelection() {
      return selected.contains(source.getRowValue(rowIndex));
   }

}
