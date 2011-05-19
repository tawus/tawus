package com.googlecode.tawus;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import com.googlecode.tawus.services.EntityDAO;

/**
 * GridDataSource for entity. It delegates the functionality to corresponding
 * GenericDAO. In order to configure the search results, 
 * {@link com.googlecode.tawus.SearchCriteria} is to be configured
 * @param <E>
 */
public class EntityGridDataSource<E> implements GridDataSource {
   private EntityDAO<E> entityDAO;
   private SearchCriteria<E> criteria;
   private List<E> storage;
   private int startIndex;

   /**
    * Constructor
    * @param entityDAO GenericDAO for type T
    * @param criteria search criteria to be used for filtering search results
    */
   public EntityGridDataSource(EntityDAO<E> entityDAO, SearchCriteria<E> criteria){
      this.entityDAO = entityDAO;
      this.criteria = criteria;
   }

   /**
    * Get avaliable rows
    * @return available rows
    */
   public int getAvailableRows(){
      if(!criteria.getEnabled()){
         return 0;
      }
      
      int count = entityDAO.count(criteria);
      return count;
   }

   /**
    * Get entity type
    * @return entity type
    */
   public Class<E> getRowType(){
      return criteria.getType();
   }

   /**
    * Prepare the results
    * @param startIndex starting index of search results
    * @param endIndex last index of search results
    * @param sortConstraints list of sort constraints to be applied
    */
   public void prepare(int startIndex, int endIndex, final List<SortConstraint> sortConstraints){
      if(!criteria.getEnabled()){
         storage = new ArrayList<E>();
         return;
      }
      
      //Set the select range
      criteria.setFirstResult(startIndex);
      criteria.setMaxResults(endIndex - startIndex + 1);
      
      //Add constraints
      for(final SortConstraint constraint: sortConstraints){
         Boolean sort = null;

         switch(constraint.getColumnSort()){
            case ASCENDING:
               sort = true;
               break;
            case DESCENDING:
               sort = false;
               break;
         }

         criteria.getOrder().clear();
         criteria.addOrder(constraint.getPropertyModel().getPropertyName(), sort);
      }
      
      //Fetch the results
      storage = entityDAO.list(criteria);
      
      //Save start index
      this.startIndex = startIndex;
   }

   /**
    * Get row value at a particular index
    * @param index index of entity to be retrieved.
    */
   public Object getRowValue(int index){
      return storage.get(index - startIndex);
   }
}

