package com.googlecode.tawus.hibernate.internal.services;

import java.io.Serializable;
import java.util.List;

import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.hibernate.services.HibernateSessionManager;
import com.googlecode.tawus.hibernate.services.SearchCriteriaConverter;
import com.googlecode.tawus.services.EntityDAO;

/**
 * Hibernate implementation of EnityDAO
 */
public class HibernateEntityDAOImpl<T> implements EntityDAO<T> {
   private HibernateSessionManager sessionManager;

   private Class<T> type;

   private TypeCoercer typeCoercer;

   private Class<?> idType;

   private String idPropertyName;

   private PropertyAccess propertyAccess;

   private SearchCriteriaConverter converter;

   /**
    * Constructor
    * 
    * @param locator
    *           Hibernate session factory
    * @param type
    *           entity type
    */
   public HibernateEntityDAOImpl(HibernateSessionManager sessionManager, PropertyAccess propertyAccess,
         TypeCoercer typeCoercer, SearchCriteriaConverter converter, Class<T> type) {
      this.sessionManager = sessionManager;
      this.type = type;
      this.typeCoercer = typeCoercer;
      this.propertyAccess = propertyAccess;
      this.converter = converter;
      setIdentityAttributes();
   }
   
   HibernateEntityDAOImpl(HibernateSessionManager sessionManager, PropertyAccess propertyAccess,
         TypeCoercer typeCoercer, Class<T> type, Class<?> idType, String idPropertyName) {
      this.sessionManager = sessionManager;
      this.type = type;
      this.typeCoercer = typeCoercer;
      this.propertyAccess = propertyAccess;
      this.idType = idType;
      this.idPropertyName = idPropertyName;
   }

   /**
    * Setup type and name of the entity id.
    */
   private void setIdentityAttributes() {
      ClassMetadata classMetadata = getSession().getSessionFactory().getClassMetadata(type);
      if (classMetadata == null) {
         throw new RuntimeException(type + " not found in hibernate");
      }      
      this.idType = (Class<?>) classMetadata.getIdentifierType().getReturnedClass();
      this.idPropertyName = classMetadata.getIdentifierPropertyName();
   }

   /**
    * Get current session
    * 
    * @return session
    */
   protected Session getSession() {
      return sessionManager.getSession(type);
   }

   /**
    * {@inheritDocs}
    */
   @Override
   @SuppressWarnings("unchecked")
   public T find(Serializable id) {
      return (T) getSession().get(type, id);
   }

   /**
    * {@inheritDocs}
    */
   @Override
   public void save(T entity) {
      getSession().save(entity);
   }

   /**
    * {@inheritDocs}
    */
   @Override
   public void update(T entity) {
      getSession().update(entity);
   }

   /**
    * {@inheritDocs}
    */
   @Override
   public void saveOrUpdate(T entity) {
      getSession().saveOrUpdate(entity);
   }

   @Override
   public void merge(T entity) {
      getSession().merge(entity);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int count(SearchCriteria<T> sc) {
      Criteria criteria = converter.toCriteria(sc, getSession(), false, false);
      criteria.setProjection(Projections.rowCount());
      long size = (Long) criteria.uniqueResult();
      return (int) size;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int count() {
      Criteria criteria = getSession().createCriteria(type);
      criteria.setProjection(Projections.rowCount());
      long size = (Long) criteria.uniqueResult();
      return (int) size;
   }

   /**
    * To flush the current session
    */
   public void flush() {
      getSession().flush();
   }

   /**
    * To clear current session
    */
   public void clear() {
      getSession().clear();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void remove(T entity) {
      getSession().delete(entity);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("unchecked")
   public T find(SearchCriteria<T> sc) {
      Criteria criteria = converter.toCriteria(sc, getSession(), false, false);
      return (T) criteria.setCacheable(true).setCacheRegion(sc.getType().getName()).uniqueResult();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("unchecked")
   public List<T> list(SearchCriteria<T> sc) {
      Criteria criteria = converter.toCriteria(sc, getSession(), true, true);
      return criteria.setCacheable(true).setCacheRegion(sc.getType().getName()).list();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Class<T> getType() {
      return type;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<T> list() {
      return getSession().createCriteria(type).list();
   }

   @Override
   public Serializable getIdentifier(Object entity) {
      return (Serializable) propertyAccess.getAdapter(type).getPropertyAdapter(idPropertyName).get(
            entity);
   }

   @Override
   public void setIdentifier(T entity, Object value) {
      propertyAccess.getAdapter(type).getPropertyAdapter(idPropertyName).set(entity, value);
   }

   @Override
   public String idString(T entity) {
      return typeCoercer.coerce(getIdentifier(entity), String.class);
   }

   @Override
   public T get(String id) {
      return find((Serializable) typeCoercer.coerce(id, idType));
   }
}
