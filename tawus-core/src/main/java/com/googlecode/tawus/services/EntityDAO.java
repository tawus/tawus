package com.googlecode.tawus.services;

import java.io.Serializable;
import java.util.List;

import com.googlecode.tawus.Propagation;
import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.annotations.Transactional;


public interface EntityDAO<E> {
   List<E> list();
   
   @Transactional
   void save(E entity);
   
   @Transactional   
   void saveOrUpdate(E entity);
   
   @Transactional
   void merge(E entity);
   
   @Transactional
   void update(E entity);
   
   @Transactional
   void remove(E entity);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   List<E> list(SearchCriteria<E> criteria);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   int count();
   
   @Transactional(propagation = Propagation.SUPPORTS)
   int count(SearchCriteria<E> criteria);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   E find(Serializable id);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   E find(SearchCriteria<E> criteria);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   E get(String id);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   Class<E> getType();
   
   @Transactional(propagation = Propagation.SUPPORTS)
   String idString(E entity);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   Serializable getIdentifier(Object object);
   
   @Transactional(propagation = Propagation.SUPPORTS)
   void setIdentifier(E entity, Object value);

}

