package com.googlecode.tawus.internal;

import java.io.Serializable;

import org.apache.tapestry5.annotations.ImmutableSessionPersistedObject;

@ImmutableSessionPersistedObject
public class PersistedEntity<E> implements Serializable {
   private final Class<E> entityClass;
   private final Serializable id;
   private static final long serialVersionUID = 1L;

   public PersistedEntity(Class<E> entityClass, Serializable id){
     this.entityClass = entityClass;
     this.id = id;
   }

   public Class<E> getEntityClass(){
      return entityClass;
   }

   public Serializable getId(){
      return id;
   }
}

