package com.googlecode.tawus;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.tawus.search.ICondition;
import com.googlecode.tawus.search.SearchType;

/**
 * Search criteria used by {@link com.googlecode.tawus.services.EntityDAO} for
 * searching
 * 
 * @param <E>
 */
public class SearchCriteria<E>
{
   private E entity;
   private final Class<E> type;
   private Map<String, Boolean> order = new HashMap<String, Boolean>();
   private int firstResult;
   private int maxResults;
   private ICondition condition;
   private SearchType searchType = SearchType.Exact;
   private boolean enabled;

   /**
    * Constructor
    * 
    * @param object
    *           search object
    * @param firstResult
    *           index of first result
    * @param maxResults
    *           maximum results to fetch
    */
   @SuppressWarnings("unchecked")
   public SearchCriteria(E object, int firstResult, int maxResults)
   {
      this.entity = object;
      setFirstResult(firstResult);
      setMaxResults(maxResults);
      this.type = (Class<E>) object.getClass();
   }

   /**
    * Constructor The results will be sorted in increasing order. You can change
    * it
    * 
    * @param object
    *           search object
    * @param firstResult
    *           index of first result to be fetched
    */
   public SearchCriteria(E object, int firstResult)
   {
      this(object, firstResult, 0);
   }

   /**
    * Constructor
    * 
    * @param object
    *           search criteria
    */
   public SearchCriteria(E object)
   {
      this(object, 0, 0);
   }

   /**
    * Constructor Since the object is not specified, a search object will be
    * created using default constructor
    * 
    * @param type
    *           search object type
    * @param firstResult
    *           index of first result to be fetched
    * @param maxResults
    *           maximum results to be fetched
    */
   public SearchCriteria(Class<E> type, int firstResult, int maxResults)
   {
      try
      {
         entity = type.newInstance();
      }
      catch(Exception ex)
      {
         throw new RuntimeException(ex);
      }
      this.type = type;
      setFirstResult(firstResult);
      setMaxResults(maxResults);
   }

   /**
    * Constructor
    * 
    * @param type
    *           search obejct type
    * @param firstResult
    *           index of first result to be fetched
    */
   public SearchCriteria(Class<E> type, int firstResult)
   {
      this(type, firstResult, 0);
   }

   /**
    * Constructor Search Criteria with no sorting
    * 
    * @param type
    *           search object type
    */
   public SearchCriteria(Class<E> type)
   {
      this(type, 0, 0);
   }

   /**
    * Get search object type
    */
   public Class<E> getType()
   {
      return type;
   }

   /**
    * Get search object
    * 
    * @return search object
    */
   public E getEntity()
   {
      return entity;
   }

   public void setEntity(E entity)
   {
      this.entity = entity;
   }

   /**
    * Get the index of first result
    * 
    * @param firstResult
    *           the firstResult to set
    */
   public void setFirstResult(int firstResult)
   {
      this.firstResult = firstResult;
   }

   /**
    * Get the index of first result to show
    * 
    * @return firstResult
    */
   public int getFirstResult()
   {
      return firstResult;
   }

   /**
    * Set maximum number of results to show
    * 
    * @param maxResults
    *           maxResults to show
    */
   public void setMaxResults(int maxResults)
   {
      this.maxResults = maxResults;
   }

   /**
    * Get maximum number of results to show
    * 
    * @return maxResults to show
    */
   public int getMaxResults()
   {
      return maxResults;
   }

   /**
    * Set condition to be used for searching. If null, then the condition will
    * be generated
    * 
    * @param condition
    *           the condition to set
    */
   public void setCondition(ICondition condition)
   {
      this.condition = condition;
   }

   /**
    * Get condition to be used for searching
    * 
    * @return the condition
    */
   public ICondition getCondition()
   {
      return condition;
   }

   /**
    * Set search type
    * 
    * @param searchType
    *           searchType to set
    */
   public void setSearchType(SearchType searchType)
   {
      this.searchType = searchType;
   }

   /**
    * Get Search type
    * 
    * @return searchType
    */
   public SearchType getSearchType()
   {
      return searchType;
   }

   /**
    * @return sort order
    */
   public Map<String, Boolean> getOrder()
   {
      return order;
   }

   public void addOrder(String property, boolean ascending)
   {
      getOrder().put(property, ascending);
   }

   public void setEnabled(boolean enabled)
   {
      this.enabled = enabled;
   }

   public boolean getEnabled()
   {
      return enabled;
   }
}
