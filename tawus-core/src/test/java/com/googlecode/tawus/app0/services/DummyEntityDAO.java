package com.googlecode.tawus.app0.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.services.EntityDAO;

public abstract class DummyEntityDAO<T> implements EntityDAO<T>{
   private List<T> entities = new ArrayList<T>();
   private Class<T> type;
   
   public List<T> getEntities(){
      return entities;
   }
   
   public DummyEntityDAO(Class<T> type){
      this.type = type;
   }

   public List<T> list() {
      return entities;
   }

   public void save(T entity) {
      entities.add(entity);
   }

   public void saveOrUpdate(T entity) {
      if(entity.toString() == null){
         save(entity);
      }else {
         update(entity);
      }
   }

   public void merge(T entity) {
      update(entity);
   }

   public void update(T entity) {
      if(entities.contains(entity)){
         entities.remove(entity);
      }
      entities.add(entity);
   }

   public void remove(T entity) {
      entities.remove(entity);
   }

   public List<T> list(SearchCriteria<T> criteria) {
      return entities;
   }

   public int count() {
      return entities.size();
   }

   public int count(SearchCriteria<T> criteria) {
      return entities.size();
   }

   public abstract T find(Serializable id);
   public T find(SearchCriteria<T> criteria) {
      return find(getIdentifier(criteria.getEntity()));
   }

   public T get(String id) {
      for(T entity: entities){
         if(getIdentifier(entity).toString().equals(id)){
            System.out.println("For " + entity + " using idString " + getIdentifier(entity));
            return entity;
         }
      }
      return null;
   }

   public Class<T> getType() {
      return type;
   }

   public String idString(T entity) {
      System.out.println("For " + entity + " using idString " + getIdentifier(entity));
      return getIdentifier(entity).toString();
   }

   public abstract Serializable getIdentifier(Object object);

   public abstract void setIdentifier(T entity, Object value) ;

}
