package com.googlecode.tawus.test;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.func.Predicate;

public class Traversal
{

   public static Element down(Element e, final String tagName)
   {
      return down(e, tagName, 1);
   }

   public static Element down(Element e, final String tagName, final Integer index)
   {
      assert e != null;

      return e.getElement(new Predicate<Element>()
      {
         int currentIndex = 0;

         @Override
         public boolean accept(Element e)
         {
            if(e.getName().equals(tagName))
            {
               currentIndex++;
            }
            return index == currentIndex;
         }

      });
   }

   public static Element down(Element e, final String attribute, final String value)
   {
      return down(e, attribute, value, 1);
   }

   public static Element down(Element e, final String attribute, final String value, final Integer index)
   {
      assert e != null;

      return e.getElement(new Predicate<Element>()
      {
         int currentIndex = 0;

         @Override
         public boolean accept(Element e)
         {
            if(e.getAttribute(attribute) != null && e.getAttribute(attribute).matches(value))
            {
               currentIndex++;
            }
            return index == currentIndex;
         }

      });
   }

   public static Element child(Element e, int index)
   {
      return (Element) e.getChildren().get(index);
   }

   public static boolean contains(Element e, String content)
   {
      return e.getChildMarkup().contains(content);
   }

   public static Element root(Document doc)
   {
      return doc.getRootElement();
   }
}
