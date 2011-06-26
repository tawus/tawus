package com.googlecode.tawus.hibernate.internal.services;

import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;
import com.googlecode.tawus.hibernate.services.SearchCriteriaConverter;
import com.googlecode.tawus.services.EntityDAO;
import com.googlecode.tawus.services.EntityDAOSource;

public class HibernateEntityDAOSource implements EntityDAOSource {
   private TypeCoercer typeCoercer;
   private PropertyAccess propertyAccess;
   private SearchCriteriaConverter converter;
   private HibernateSessionManager sessionManager;

   public HibernateEntityDAOSource(HibernateSessionManager sessionManager,
         PropertyAccess propertyAccess, TypeCoercer typeCoercer, SearchCriteriaConverter converter) {
      this.sessionManager = sessionManager;
      this.propertyAccess = propertyAccess;
      this.typeCoercer = typeCoercer;
      this.converter = converter;
   }

   @Override
   public <E> EntityDAO<E> get(Class<E> entityClass) {
      return new HibernateEntityDAOImpl<E>(sessionManager, propertyAccess, typeCoercer, converter,
            entityClass);

   }

}
