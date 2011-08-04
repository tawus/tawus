package com.googlecode.tawus.services;

import com.googlecode.tawus.SearchCriteria;

public interface CriteriaSource
{
   public <E> SearchCriteria<E> get(Class<E> type);
}
