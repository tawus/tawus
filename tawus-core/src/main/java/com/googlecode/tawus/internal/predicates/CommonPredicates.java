package com.googlecode.tawus.internal.predicates;

import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.func.Predicate;

public class CommonPredicates
{
   public static Predicate<Element> like(final String attribute, final String match)
   {
      return new Predicate<Element>()
      {

         @Override
         public boolean accept(Element element)
         {
            return element.getAttribute(attribute) != null && element.getAttribute(attribute).matches(match);
         }

      };
   }

   public static Predicate<Element> nameLike(final String match)
   {
      return new Predicate<Element>()
      {

         @Override
         public boolean accept(Element element)
         {
            return element.getName().matches(match);
         }

      };
   }

   public static Predicate<Element> like(final String match)
   {
      return like("id", match);
   }

}
