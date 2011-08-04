package com.googlecode.tawus.search;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Condition implements ICondition
{
   private static final long serialVersionUID = 1L;

   private ConditionType conditionType;
   @SuppressWarnings("rawtypes")
   private List args = new ArrayList();
   private boolean join;
   private boolean property;

   private Condition(ConditionType conditionType, String... exprs)
   {
      this.conditionType = conditionType;
      this.join = false;
      this.property = true;
      for(String expr : exprs)
      {
         getArgs().add(expr);
      }
   }

   private Condition(ConditionType conditionType, boolean property, Object... exprs)
   {
      this.conditionType = conditionType;
      this.join = false;
      this.property = property;
      for(Object expr : exprs)
      {
         getArgs().add(expr);
      }
   }

   private Condition(ConditionType conditionType, ICondition... conditions)
   {
      this.conditionType = conditionType;
      this.join = true;
      this.property = true;
      for(ICondition condition : conditions)
      {
         getArgs().add(condition);
      }
   }

   public Condition setProperty(boolean property)
   {
      this.property = property;
      return this;
   }

   public boolean isProperty()
   {
      return property;
   }

   public ConditionType getType()
   {
      return conditionType;
   }

   @SuppressWarnings("rawtypes")
   public List getArgs()
   {
      return args;
   }

   public boolean isJoin()
   {
      return join;
   }

   public static ICondition eq(final String expr1, final String expr2)
   {
      return new Condition(ConditionType.EQ, expr1, expr2);
   }

   public static ICondition eqValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.EQ, false, expr1, value);
   }

   public static ICondition eq(final String expr)
   {
      return new Condition(ConditionType.EQ, expr);
   }

   public static ICondition like(final String expr)
   {
      return new Condition(ConditionType.LIKE, expr);
   }

   public static ICondition likeValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.LIKE, false, expr1, value);
   }

   public static ICondition ne(final String expr1, final String expr2)
   {
      return new Condition(ConditionType.NE, expr1, expr2);
   }

   public static ICondition neValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.NE, false, expr1, value);
   }

   public static ICondition ne(final String expr)
   {
      return new Condition(ConditionType.NE, expr);
   }

   public static ICondition notLike(final String expr)
   {
      return new Condition(ConditionType.NOT_LIKE, expr);
   }

   public static ICondition notLikeValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.NOT_LIKE, false, expr1, value);
   }

   public static ICondition lt(final String expr1, final String expr2)
   {
      return new Condition(ConditionType.LT, expr1, expr2);
   }

   public static ICondition ltValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.LT, false, expr1, value);
   }

   public static ICondition lt(final String expr)
   {
      return new Condition(ConditionType.LT, expr);
   }

   public static ICondition le(final String expr1, final String expr2)
   {
      return new Condition(ConditionType.LE, expr1, expr2);
   }

   public static ICondition leValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.LE, false, expr1, value);
   }

   public static ICondition le(final String expr)
   {
      return new Condition(ConditionType.LE, expr);
   }

   public static ICondition gt(final String expr1, final String expr2)
   {
      return new Condition(ConditionType.GT, expr1, expr2);
   }

   public static ICondition gtValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.GT, false, expr1, value);
   }

   public static ICondition gt(final String expr)
   {
      return new Condition(ConditionType.GT, expr);
   }

   public static ICondition ge(final String expr1, final String expr2)
   {
      return new Condition(ConditionType.GE, expr1, expr2);
   }

   public static ICondition geValue(final String expr1, final Object value)
   {
      return new Condition(ConditionType.GE, false, expr1, value);
   }

   public static ICondition ge(final String expr)
   {
      return new Condition(ConditionType.GE, expr);
   }

   public static ICondition isNull(final String expr)
   {
      return new Condition(ConditionType.IS_NULL, expr);
   }

   public static ICondition isNotNull(final String expr)
   {
      return new Condition(ConditionType.NOT_NULL, expr);
   }

   public static ICondition and(final ICondition... conditions)
   {
      return new Condition(ConditionType.AND, conditions);
   }

   public static ICondition or(final ICondition... conditions)
   {
      return new Condition(ConditionType.OR, conditions);
   }

   public static ICondition not(final ICondition condition)
   {
      return new Condition(ConditionType.NOT, condition);
   }
}
