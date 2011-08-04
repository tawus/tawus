package com.googlecode.tawus.services;

public interface EntityDAOLocator
{
   <T> EntityDAO<T> get(Class<T> entityClass);
}
