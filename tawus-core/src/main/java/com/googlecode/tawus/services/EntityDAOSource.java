package com.googlecode.tawus.services;

/**
 * This service acts as a source for entity DAOs
 */
public interface EntityDAOSource
{
   /**
    * Get DAO for a specific entity class
    * 
    * @param <E>
    *           entity type
    * @param entityClass
    *           entity class type
    * @return entity DAO
    */
   public <E> EntityDAO<E> get(Class<E> entityClass);
}
