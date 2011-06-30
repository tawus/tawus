package com.googlecode.tawus.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.corelib.data.GridPagerPosition;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;

import com.googlecode.tawus.EntityGridDataSource;
import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.TawusEvents;
import com.googlecode.tawus.TawusUtils;
import com.googlecode.tawus.annotations.XHR;
import com.googlecode.tawus.internal.GridRuntime;
import com.googlecode.tawus.internal.PropertyOverridesDelegator;
import com.googlecode.tawus.services.EntityDAOLocator;

/**
 * Entity Grid A full CRUD component for a bean. It is presented as a list with
 * a search option and a new item can be added using the new button and existing
 * items can be viewed or modified
 */
@SupportsInformalParameters
public class EntityGrid implements GridRuntime
{

   /**
    * Grid for displaying list of objects
    */
   @Component(publishParameters = "add, columnIndex,empty,encoder, include, exclude, "
         + "class, lean, reorder, row,rowClass, rowIndex, rowsPerPage, " + "sortModel, volatile, model", parameters = {
         "source=source", "overrides=overrides", "row=inherit:row", "inplace='inplace'",
         "pagerPosition=prop:pagerPosition" })
   private Grid grid;

   private EntityGridDataSource<?> source;

   /**
    * Get data source
    * 
    * @return data source
    */
   public EntityGridDataSource<?> getSource()
   {
      return source;
   }

   /**
    * Search criteria to be used for searching entities
    */
   @Parameter(principal = true, required = true, allowNull = false)
   private SearchCriteria<?> criteria;

   /**
    * Show Details
    */
   @Persist
   private boolean showDetails;

   @SuppressWarnings("unused")
   @Parameter(allowNull = false, defaultPrefix = BindingConstants.BLOCK)
   @Property
   private Block search;

   @Inject
   private Block defaultSearchBlock;

   public Block defaultSearch()
   {
      return this.defaultSearchBlock;
   }

   @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
   private String actionProperty;

   @SuppressWarnings("unused")
   @Parameter
   private Object row;

   @Inject
   private EntityDAOLocator locator;

   @Inject
   @Property(write = false)
   private ComponentResources resources;

   @Inject
   private TypeCoercer typeCoercer;

   @Parameter(allowNull = false)
   private PropertyOverrides overrides;

   @Inject
   private Block gridBlock;

   @Parameter(allowNull = false, defaultPrefix = BindingConstants.BLOCK)
   private Block editor;

   @Inject
   private Block defaultEditorBlock;

   public Block defaultEditor()
   {
      return this.defaultEditorBlock;
   }

   @Parameter(value = "true")
   @Property(write = false)
   private boolean insertable;

   @Parameter(value = "true")
   @Property(write = false)
   private boolean editable;

   @SuppressWarnings("unused")
   @Parameter(value = "true")
   @Property(write = false)
   private boolean searchable;

   @Parameter(required = true)
   private Object object;

   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   @Property
   private GridPagerPosition pagerPosition;

   GridPagerPosition defaultPagerPosition()
   {
      return GridPagerPosition.BOTH;
   }

   /**
    * Default property override
    * 
    * @return default {@link org.apache.tapestry5.PropertyOverrides}
    */
   PropertyOverrides defaultOverrides()
   {
      return new PropertyOverridesDelegator(typeCoercer.coerce(resources, PropertyOverrides.class), typeCoercer.coerce(
            grid, PropertyOverrides.class), actionProperty + "Cell");
   }

   public PropertyOverrides getOverrides()
   {
      return overrides;
   }

   public boolean getInPlace()
   {
      return zone != null;
   }

   public Object getActiveBlock()
   {
      return showDetails ? editor : gridBlock;
   }

   public String getEditActionContent()
   {
      Object value = grid.getDataModel().get(actionProperty).getConduit().get(grid.getRow());
      return value == null ? "" : value.toString();
   }

   public Object getEditActionContext()
   {
      return locator.get(criteria.getType()).getIdentifier(grid.getRow());
   }

   @XHR
   Object onActionFromEditActionLink(String id)
   {
      setShowDetails(true);
      resources.triggerEvent(TawusEvents.SHOW_DETAILS,
            new Object[] { object = locator.get(criteria.getType()).get(id) }, null);
      return returnValue();
   }

   private Object returnValue()
   {
      if(zone != null)
      {
         return resources.getContainerResources().getEmbeddedComponent(zone);
      }
      else
      {
         return null;
      }
   }

   @XHR
   Object onActionFromNewActionLink()
   {
      setShowDetails(true);
      resources.triggerEvent(TawusEvents.SHOW_DETAILS, new Object[] { object = newInstance() }, null);
      return returnValue();
   }

   public Object newInstance()
   {
      try
      {
         return criteria.getType().newInstance();
      }
      catch(InstantiationException e)
      {
         throw new RuntimeException("Could not create instance for type: " + criteria.getType(), e);
      }
      catch(IllegalAccessException e)
      {
         throw new RuntimeException("Could not create instance for type: " + criteria.getType(), e);
      }
   }

   public void setupRender()
   {
      setShowDetails(showDetails);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public void setShowDetails(boolean showDetails)
   {
      this.showDetails = showDetails;
      if(!showDetails)
      {
         source = new EntityGridDataSource(locator.get(criteria.getType()), criteria);
      }
   }

   public Object getEntity()
   {
      return object;
   }

   public String getNewMessage()
   {
      Messages messages = overrides.getOverrideMessages();
      if(!messages.contains("new"))
      {
         messages = resources.getMessages();
      }
      return messages.format("new", TawusUtils.toUserPresentable(criteria.getType()));
   }

   void onInPlaceUpdate(String zone)
   {
      setShowDetails(false);
      resources.triggerEvent(TawusEvents.SORT, null, null);
   }

   public void showGrid()
   {
      setShowDetails(false);
   }

   public void enableSearch()
   {
      criteria.setEnabled(true);
   }

   public void cancel()
   {
      criteria.setEnabled(false);
   }

   public Object getSearchObject()
   {
      return criteria.getEntity();
   }

   public Object getObject()
   {
      if(object == null)
      {
         try
         {
            object = criteria.getType().newInstance();
         }
         catch(Exception e)
         {
            e.printStackTrace();
            throw new RuntimeException(e);
         }
      }
      return object;
   }

   public String getZone()
   {
      return zone;
   }

   public String getZoneId()
   {
      return zone == null ? null : ((Zone) resources.getContainerResources().getEmbeddedComponent(zone)).getClientId();
   }

   public boolean getShowNewLink()
   {
      return editor != defaultEditorBlock && insertable;
   }

   public boolean getShowEditLink()
   {
      return editor != defaultEditorBlock && editable;
   }

   public boolean getShowDetails()
   {
      return showDetails;
   }
}
