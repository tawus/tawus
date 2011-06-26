package com.googlecode.tawus.hibernate.services;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.googlecode.tawus.SearchCriteria;

public interface SearchCriteriaConverter {
   Criteria toCriteria(SearchCriteria<?> searchCriteria, Session session, boolean sort, boolean paginate);
}
