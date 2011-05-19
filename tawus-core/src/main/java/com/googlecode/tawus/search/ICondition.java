package com.googlecode.tawus.search;

import java.io.Serializable;
import java.util.List;

/**
 * ICondition interface
 */
public interface ICondition extends Serializable {
   /**
    * Get condition type
    */
   ConditionType getType();

   /**
    * Get condition arguments
    */
   List<?> getArgs();

   /**
    * If this is a join
    */
   boolean isJoin();

   boolean isProperty();
}
