package com.googlecode.tawus;

import org.apache.tapestry5.ValueEncoder;

import com.googlecode.tawus.services.EntityDAO;

/**
 * A value encoder for an entity
 * @author taha
 * @param <E> Entity
 */
public class EntityValueEncoder<E> implements ValueEncoder<E> {
   private EntityDAO<E> entityDAO;
   
   public EntityValueEncoder(EntityDAO<E> entityDAOSource){
      this.entityDAO = entityDAOSource;
   }

   public String toClient(E entity){
      return entityDAO.idString(entity);
   }

   public E toValue(String clientValue){
      return entityDAO.get(clientValue);
   }
}

