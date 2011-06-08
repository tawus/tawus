package com.googlecode.tawus.components

import static com.googlecode.tawus.internal.predicates.CommonPredicates.*
import static com.googlecode.tawus.test.Traversal.*

import org.apache.tapestry5.dom.Element

import com.googlecode.tawus.TestSpecification

class EntityGridWithSearchTest extends TestSpecification {
   def "check search form embedded in a grid"(){
      when:
      def doc = pageTester.renderPage("EntityGridWithSearchDemo/disabled");
      def e = down(root(doc), "class", "t-data-grid", 1)
      then:
      e == null
      doc.rootElement.childMarkup.contains("There is no data to display");
      doc.rootElement.childMarkup.contains("User name(Unique)") == false

      //Now try a search and check if the elements are passed correctly
      when:
      Map<String, String> parameters = new HashMap<String, String>();
      parameters["name"] = "Taha"
      parameters["address"] = "Srinagar"
      parameters["search"]  = "Search"
      doc = pageTester.submitForm(down(root(doc), "form"), parameters)
      then:
      contains(down(root(doc), "id", "searchFields"), "Taha/Srinagar")
      
      //Try cancel button
      when:
      parameters.clear()
      parameters["cancel"] = "Cancel"
      doc = pageTester.submitForm(down(root(doc), "form"), parameters)
      then:
      contains(down(root(doc), "id", "searchFields"), "none")
   }

   def "check table is filled with criteria is enabled"(){
      when:
      def doc = pageTester.renderPage("EntityGridWithSearchDemo/enabled");
      Element table = down(root(doc), "class", "t-data-grid", 2)
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
      def doc = pageTester.renderPage("EntityGridWithSearchDemo/enabled");
      Element table = down(root(doc), "class", "t-data-grid", 2)
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
