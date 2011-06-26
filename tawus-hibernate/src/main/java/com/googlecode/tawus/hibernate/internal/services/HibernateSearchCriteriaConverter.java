package com.googlecode.tawus.hibernate.internal.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

import com.googlecode.tawus.SearchCriteria;
import com.googlecode.tawus.hibernate.annotations.DefaultOrder;
import com.googlecode.tawus.hibernate.services.SearchCriteriaConverter;
import com.googlecode.tawus.search.Condition;
import com.googlecode.tawus.search.ConditionType;
import com.googlecode.tawus.search.ICondition;
import com.googlecode.tawus.search.SearchType;

public class HibernateSearchCriteriaConverter implements SearchCriteriaConverter {
   private static class Context {
      private Map<String, Object> valueMap = new HashMap<String, Object>();
      private Map<String, String> aliasMap = new HashMap<String, String>();
      
      public void setValue(String name, Object value){
         valueMap.put(name, value);
      }
      
      public Map<String, Object> getValueMap(){
         return valueMap;
      }
      
      public void setAlias(String name, String alias){
         aliasMap.put(name, alias);
      }
      
      public Object getValue(String name){
         return valueMap.get(name);
      }
      
      public String getAlias(String name){
         return aliasMap.get(name);
      }
   }

   public Criteria toCriteria(SearchCriteria<?> sp, Session session, boolean sort, boolean paginate) {
      final Criteria criteria = session.createCriteria(sp.getType());
      criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

      //If paginate is specified, then paginate
      if (paginate && sp.getFirstResult() >= 0 && sp.getMaxResults() > 0) {
         criteria.setFirstResult(sp.getFirstResult());
         criteria.setMaxResults(sp.getMaxResults());
      }
      
      Context context = new Context();
      loadPropertyValues(sp.getEntity(), null, session, context);
      
      ICondition condition = sp.getCondition();
      if (condition == null) {
         List<ICondition> conditions = new ArrayList<ICondition>();
         for (String propertyName : context.getValueMap().keySet()) {
            conditions.add(Condition.eq(propertyName));
         }
         condition = Condition.and(conditions.toArray(new ICondition[] {}));
      }

      if (condition != null) {
         Criterion c = applyCondition(criteria, condition, sp.getSearchType(), context);
         if (c != null) {
            criteria.add(c);
         }
      }
      if (sort) {
         Map<String, Boolean> sorting = sp.getOrder();
         if (sorting == null || sorting.size() == 0) {
            DefaultOrder annotation = sp.getType().getAnnotation(DefaultOrder.class);
            if (annotation != null) {
               String colAlias = getPropertyName(criteria, annotation.column(), context);
               criteria.addOrder(annotation.ascending() ? Order.asc(colAlias)
                     : Order.desc(colAlias));
            }
         } else {
            for (String col : sorting.keySet()) {
               boolean order = sorting.get(col);
               String colAlias = getPropertyName(criteria, col, context);
               criteria.addOrder(order ? Order.asc(colAlias) : Order.desc(colAlias));
            }
         }
      }
      return criteria;
   }

   Criterion applyCondition(final Criteria criteria, final ICondition condition, SearchType searchType, 
         Context context) {
      Criterion c = null;
      if (condition.isJoin()) {
         switch (condition.getType()) {
            case AND:
               c = Restrictions.conjunction();
               for (Object object : condition.getArgs()) {
                  Criterion child = applyCondition(criteria, (ICondition) object, searchType, context);
                  if (child != null) {
                     ((Conjunction) c).add(child);
                  }
               }
               break;

            case OR:
               c = Restrictions.disjunction();
               for (Object object : condition.getArgs()) {
                  Criterion child = applyCondition(criteria, (ICondition) object, searchType, context);
                  if (child != null) {
                     ((Disjunction) c).add(child);
                  }
               }
               break;

            case NOT:
               c = Restrictions.not(applyCondition(criteria,
                     (ICondition) condition.getArgs().get(0), searchType, context));
               break;
         }
      } else {
         String firstArg = getPropertyName(criteria, (String) condition.getArgs().get(0), context);
         int argCount = condition.getArgs().size();
         Object secondArg = null;
         if (condition.getType() == ConditionType.NOT_NULL
               || condition.getType() == ConditionType.IS_NULL) {
            // do nothing
         } else if (argCount == 1) {
            secondArg = context.getValue((String) condition.getArgs().get(0));
            if (secondArg == null) {
               return null;
            }
         } else {
            if (condition.isProperty()) {
               secondArg = getPropertyName(criteria, (String) condition.getArgs().get(1), context);
            } else {
               secondArg = condition.getArgs().get(1);
               if (secondArg == null) {
                  return null;
               }
            }
         }

         switch (condition.getType()) {
            case EQ:
               if ((argCount == 1) && (searchType == SearchType.Like)
                     && (secondArg instanceof String)) {
                  c = Restrictions.like(firstArg, "%" + secondArg + "%");
               } else {
                  c = argCount == 1 || !condition.isProperty() ? Restrictions.eq(firstArg,
                        secondArg) : Restrictions.eqProperty(firstArg, (String) secondArg);
               }
               break;

            case NE:
               c = argCount == 1 || !condition.isProperty() ? Restrictions.ne(firstArg, secondArg)
                     : Restrictions.neProperty(firstArg, (String) secondArg);
               break;

            case LE:
               c = argCount == 1 || !condition.isProperty() ? Restrictions.le(firstArg, secondArg)
                     : Restrictions.leProperty(firstArg, (String) secondArg);
               break;

            case LT:
               c = argCount == 1 || !condition.isProperty() ? Restrictions.lt(firstArg, secondArg)
                     : Restrictions.ltProperty(firstArg, (String) secondArg);
               break;

            case GE:
               c = argCount == 1 || !condition.isProperty() ? Restrictions.ge(firstArg, secondArg)
                     : Restrictions.geProperty(firstArg, (String) secondArg);
               break;

            case GT:
               c = argCount == 1 || !condition.isProperty() ? Restrictions.gt(firstArg, secondArg)
                     : Restrictions.gtProperty(firstArg, (String) secondArg);
               break;

            case LIKE:
               c = Restrictions.like(firstArg, secondArg);
               break;

            case NOT_LIKE:
               c = Restrictions.not(Restrictions.like(firstArg, secondArg));
               break;

            case IS_NULL:
               c = Restrictions.isNull(firstArg);
               break;

            case NOT_NULL:
               c = Restrictions.isNotNull(firstArg);
               break;
         }
         if (c != null && SimpleExpression.class.isAssignableFrom(c.getClass())) {
            if (secondArg instanceof String) {
               c = ((SimpleExpression) c).ignoreCase();
            }
         }
      }
      return c;
   }

   String getPropertyName(final Criteria criteria, final String expr, Context context) {
      if (!expr.contains(".")) {
         return expr;
      }
      final String[] beans = expr.split("\\.");
      String beanAlias = "";
      String beanName = "";
      for (int i = 0; i < beans.length - 1; ++i) {
         if (beanAlias != "") {
            beanAlias += "_" + beans[i];
            beanName += "." + beans[i];
         } else {
            beanAlias = beans[i];
            beanName = beans[i];
         }

         if (context.getAlias(beanName) == null) {
            context.setAlias(beanName, beanAlias);
            criteria.createAlias(beanName, beanAlias, CriteriaSpecification.LEFT_JOIN);
         }
      }
      return beanAlias + "." + beans[beans.length - 1];
   }

   void loadPropertyValues(Object object, String namePrefix, Session session, Context context) {
      final ClassMetadata meta = getMetadata(object, session);
      assert meta != null;

      final String[] propertyNames = meta.getPropertyNames();
      String idName = meta.getIdentifierPropertyName();
      if (idName != null) {
         final Serializable id = meta.getIdentifier(object, (SessionImplementor) session);
         if (id != null) {
            if (namePrefix != null) {
               idName = namePrefix + "." + idName;
            }
            context.setValue(idName, id);
            if(namePrefix != null){
               return; //If it is a nested object and id is given, don't bother to add other properties
               //of the nested object
            }
         }
      }

      for (int i = 0; i < propertyNames.length; ++i) {
         final Type type = meta.getPropertyType(propertyNames[i]);
         final Object value = meta.getPropertyValue(object, propertyNames[i], EntityMode.POJO);
         if (value == null) {
            continue;
         }
         if (value instanceof Number && ((Number) value).longValue() == 0) {
            continue;
         }

         String name;
         if (namePrefix == null) {
            name = propertyNames[i];
         } else {
            name = namePrefix + "." + propertyNames[i];
         }

         if (type.isCollectionType()) {
            Collection<?> val = (Collection<?>) value;
            if (val.size() == 1) {
               Iterator<?> iterator = val.iterator();
               Object obj = iterator.next();
               if (obj != null) {
                  loadPropertyValues(obj, name, session, context);
               }
            }
         } else if (type.isEntityType()) {
            loadPropertyValues(value, name, session, context);
         } else {
            context.setValue(name, value);
         }
      }
   }

   //For testing purposes set the scope to package
   ClassMetadata getMetadata(Object object, Session session) {
      return session.getSessionFactory().getClassMetadata(object.getClass());
   }
}
