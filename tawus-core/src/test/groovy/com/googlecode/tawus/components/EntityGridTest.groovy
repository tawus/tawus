package com.googlecode.tawus.components

import static com.googlecode.tawus.internal.predicates.CommonPredicates.*

import org.apache.tapestry5.dom.Element

import com.googlecode.tawus.TestSpecification

class EntityGridTest extends TestSpecification {
   def "check empty table is rendered"(){
      when:
      def doc = pageTester.renderPage("EntityGridDemo/disabled");
      Element table = doc.getRootElement().getElement(nameLike("table"))
      then:
      table == null
      doc.rootElement.childMarkup.contains("There is no data to display");
   }

   def "check table is filled with criteria is enabled"(){
      when:
      def doc = pageTester.renderPage("EntityGridDemo/enabled");
      Element table = doc.getRootElement().getElement(nameLike("table"))
      then:
      table != null
      table.children.size() == 2;//thead,tbody

      when:
      def tr = table.children[0].children[0]
      def tr2 = table.children[1].children[0]
      then:
      tr.children[i].name.equals "th"
      tr.children[i].childMarkup.contains(header)
      tr2.children[i].childMarkup.contains(content)
      where:
      i << [0, 1, 2, 3, 4, 5]
      header << [
         "Name",
         "Id",
         "Address",
         "Age",
         "Gender",
         "Department"
      ]
      content << [
         "Taha",
         "1",
         "Srinagar",
         "32",
         "Male",
         "Computers"
      ]
   }

   def "checking sorting links on table"(){
      when:
      def doc = pageTester.renderPage("EntityGridDemo/enabled");
      Element table = doc.getRootElement().getElement(nameLike("table"))
      then:
      table != null
      table.children.size() == 2;//thead,tbody

      when:
      System.out.println(table.children[0].children[0].children[i].children[0])
      doc = pageTester.clickLink(table.children[0].children[0].children[i].children[0])
      then:
      doc.getElementById("sortColumn").childMarkup.contains(header)
      where:
      i << [0, 1, 2, 3, 4]
      header << [
         "name",
         "id",
         "address",
         "age",
         "gender",
      ]

   }
}
