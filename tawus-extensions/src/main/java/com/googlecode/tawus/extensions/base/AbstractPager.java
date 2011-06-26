/*
 * Copyright 2010 Taha Hafeez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.tawus.extensions.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.googlecode.tawus.extensions.TawusExtrasEvents;
import com.googlecode.tawus.extensions.components.Pager;
import com.googlecode.tawus.extensions.internal.Pageable;

/**
 * Base class for LoopPagers. Implements basic functionality
 * 
 * @author Taha Hafeez
 * @see Pager
 */
@Events({ TawusExtrasEvents.PAGE_CHANGED })
public abstract class AbstractPager implements Pageable, ClientElement
{
   /* Grid data source */
   @Parameter(required = true, allowNull = false)
   private GridDataSource source;

   /* Rows Per Page to show */
   @Parameter(value = "10")
   private int rowsPerPage;

   /* Page to display at the start */
   @Parameter(value = "1")
   private int startPage;

   /** Current page */
   @Persist
   private int currentPage;

   /** How many pages to show */
   @Parameter(value = "10")
   private int showPages;

   /** pageNumber used by loop */
   @Property
   private int pageNumber;

   /** Zone to set */
   @SuppressWarnings("unused")
   @Parameter(defaultPrefix = BindingConstants.LITERAL)
   private String zone;

   /** Last page number */
   private int lastPage;

   /** Page numbers */
   private List<Integer> pageNumbers;

   /** Component resources */
   @Inject
   private ComponentResources resources;

   @Environmental
   private JavaScriptSupport javaScriptSupport;

   @Parameter(value = "componentResources.id", required = true, defaultPrefix = BindingConstants.LITERAL)
   private String clientId;

   private String assignedClientId;

   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Iterable getPagedSource()
   {
      if (currentPage == 0)
      {
         currentPage = startPage;
      }

      List list = new ArrayList();
      int fromRow = getFromRow();
      int toRow = getToRow();
      source.prepare(fromRow, toRow, null);
      for (int i = fromRow; i < toRow; ++i)
      {
         list.add(source.getRowValue(i));
      }

      return list;
   }

   @SetupRender
   public void setup()
   {
      assignedClientId = javaScriptSupport.allocateClientId(clientId);
   }

   /**
    * Initialize page numbers.
    */
   @BeforeRenderTemplate
   boolean initPageNumbers()
   {
      ;
      pageNumbers = new ArrayList<Integer>();
      lastPage = (source.getAvailableRows() + rowsPerPage - 1) / rowsPerPage;

      // If it is only a single page, don't show the pager
      if (lastPage <= 1)
      {
         currentPage = 1; // Correct currentPage if incorrect
         return false;
      }

      int startPage = currentPage - showPages / 2;
      if (startPage < 1)
      {
         startPage = 1;
      }

      int endPage = startPage + showPages - 1;
      if (endPage > lastPage)
      {
         endPage = lastPage;
         startPage = lastPage - showPages;
         if (startPage < 1)
         {
            startPage = 1;
         }
      }

      for (int i = startPage; i <= endPage; ++i)
      {
         pageNumbers.add(i);
      }
      return true;
   }

   /**
    * Is it the current page
    * 
    * @return is it the current page
    */
   public boolean isCurrent()
   {
      return pageNumber == currentPage;
   }

   /**
    * Get last page number
    * 
    * @return last page number
    */
   public int getLastPage()
   {
      return lastPage;
   }

   /**
    * Get page numbers
    * 
    * @return page numbers to be displayed
    */
   public List<Integer> getPageNumbers()
   {
      return pageNumbers;
   }

   /**
    * Is it the first page
    * 
    * @return true if it is the first page otherwise false
    */
   public boolean getShowPrevious()
   {
      return currentPage == 1;
   }

   /**
    * Is it the last page
    * 
    * @return true if it is the last page otherwise false
    */
   public boolean getShowNext()
   {
      return currentPage == lastPage;
   }

   /**
    * Returns the same page if zone is not set otherwise returns the
    * internalZone and the zone parameter.
    * 
    * @return page or zone.
    */
   private Object pageOrZone()
   {
      CaptureResultCallback<Object> callback = CaptureResultCallback.create();
      resources.triggerEvent(TawusExtrasEvents.PAGE_CHANGED, new Object[] { currentPage }, callback);
      return callback.getResult();
   }

   /**
    * Event handler for a next link
    * 
    * @return this page or zone depending upon the zone parameter
    */
   public Object onActionFromNext()
   {
      currentPage++;
      return pageOrZone();
   }

   /**
    * Event handler for previous link
    * 
    * @return this page or zone depending upon the zone parameter
    */
   public Object onActionFromPrevious()
   {
      currentPage--;
      if (currentPage < 1)
      {
         currentPage = 1;
      }
      return pageOrZone();
   }

   /**
    * Event handler for page link
    * 
    * @return this page or zone depending upon the zone parameter
    */
   public Object onActionFromPage(int page)
   {
      currentPage = page;
      return pageOrZone();
   }

   /**
    * Event handler for first link
    * 
    * @return this page or zone depending upon the zone parameter
    */
   public Object onActionFromFirst()
   {
      currentPage = 1;
      return pageOrZone();
   }

   /**
    * Event handler for last link
    * 
    * @return this page or zone depending upon the zone parameter
    */
   public Object onActionFromLast(int lastPage)
   {
      currentPage = lastPage;
      return pageOrZone();
   }

   public int getRowsPerPage()
   {
      return rowsPerPage;
   }

   public int getFromRow()
   {
      return (currentPage - 1) * rowsPerPage;
   }

   public int getToRow()
   {
      int toRow = getFromRow() + rowsPerPage;
      if (toRow > source.getAvailableRows())
      {
         toRow = source.getAvailableRows();
      }
      return toRow;
   }

   public String getClientId()
   {
      return assignedClientId;
   }
}
