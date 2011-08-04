package com.googlecode.tawus.internal.services;

import java.util.Map;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.services.CriteriaSource;

@SuppressWarnings("unchecked")
public class CriteriaSourceImpl implements CriteriaSource
{
   @SuppressWarnings("rawtypes")
   private final Map<Class, SearchCriteria> criterias;

   @SuppressWarnings("rawtypes")
   public CriteriaSourceImpl(final Map<Class, SearchCriteria> criterias)
   {
      this.criterias = criterias;
   }

   public <E> SearchCriteria<E> get(final Class<E> type)
   {
      SearchCriteria<E> criteria = (SearchCriteria<E>) criterias.get(type);
      if(criteria == null)
      {
         criteria = new SearchCriteria<E>(type);
      }

      return criteria;
   }
}
