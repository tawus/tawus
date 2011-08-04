package com.googlecode.tawus.internal.services;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.services.CriteriaSource;

public class DefaultCriteriaSource implements CriteriaSource
{

   public <E> SearchCriteria<E> get(Class<E> type)
   {
      return new SearchCriteria<E>(type);
   }

}
