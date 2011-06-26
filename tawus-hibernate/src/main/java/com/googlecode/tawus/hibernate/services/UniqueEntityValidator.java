package com.googlecode.tawus.hibernate.services;

import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.googlecode.tawus.hibernate.annotations.Unique;
import com.googlecode.tawus.services.EntityValidator;

public class UniqueEntityValidator implements EntityValidator {

   private HibernateSessionManager sessionManager;
   private PropertyAccess propertyAccess;

   public UniqueEntityValidator(HibernateSessionManager sessionManager,
         PropertyAccess propertyAccess) {
      this.sessionManager = sessionManager;
      this.propertyAccess = propertyAccess;
   }

   @Override
   public void validate(Object entity) throws ValidationException {
      if (entity == null) {
         return;
      }
      Unique unique = entity.getClass().getAnnotation(Unique.class);
      if (unique == null) {
         return;
      }

      @SuppressWarnings("rawtypes")
      Class clazz = entity.getClass();
      Session session = sessionManager.getSession(clazz);
      ClassPropertyAdapter classAdapter = propertyAccess.getAdapter(clazz);

      for (String column : unique.columns()) {
         if (column != null) {
            Criteria criteria = session.createCriteria(clazz);
            for (String c : column.split(",")) {
               String col = c.trim();
               
               criteria.add(Restrictions.eq(col, classAdapter.get(entity, col)));
            }
            if (classAdapter.get(entity, "id") != null){
               criteria.add(Restrictions.not(Restrictions.idEq(classAdapter.get(entity, "id"))));
            }
            criteria.setProjection(Projections.rowCount());
            Long rowCount = (Long) criteria.uniqueResult();
            if (rowCount > 0L) {
               throw new ValidationException("Column(s) '" + column
                     + "' must be unique");
            }
         }
      }

   }
}
