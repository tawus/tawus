package com.googlecode.tawus.components

import static com.googlecode.tawus.internal.predicates.CommonPredicates.*
import static com.googlecode.tawus.test.Traversal.*

import org.apache.tapestry5.dom.Element

import spock.lang.Ignore

import com.googlecode.tawus.TestSpecification

class EntityGridWithEditTest extends TestSpecification {

   def "check table is filled when criteria is enabled"(){
      when:
      def doc = pageTester.renderPage("EntityGridWithEditDemo");
      Element table = down(root(doc), "class", "t-data-grid", 2)
      then:
      table != null
      table.children.size() == 2;

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
      def doc = pageTester.renderPage("EntityGridWithEditDemo");
      Element table = down(root(doc), "class", "t-data-grid", 2)
      then:
      table != null
      table.children.size() == 2;//thead,tbody

      when:
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

   def "save a new entity"(){
      setup:
      def doc = pageTester.renderPage("EntityGridWithEditDemo");
      Element newLink = down(root(doc), "class", "t-entity-new-link")
      assert newLink != null
      doc = pageTester.clickLink(newLink)
      newLink = down(root(doc), "class", "t-entity-new-link")
      assert newLink == null
      def form = down(root(doc), "form")
      assert form != null
      expect:
      down(form, "name", "name") != null
      down(form, "name", "save") != null
      down(form, "name", "address") != null
      down(form, "name", "department") != null
      down(form, "name", "age") != null
      down(form, "name", "id_0") != null
      
      when:
      def parameters = ["name": "Tawus", "id_0": "25", "address":"Kashmir", "age":"32",  "gender":"Male", "department":"1", "save":"save"]
      doc = pageTester.submitForm(form, parameters)
      then:
      assert down(root(doc), "class", "t-entity-new-link") != null
      
      //Check if the new entity has been added
      when:
      def table = down(root(doc), "table")
      assert table != null
      def tr = down(table, "tr", 3);
      assert tr != null
      then:
      tr.children[0].childMarkup.contains("Tawus")
      tr.children[1].childMarkup.contains("25")
      tr.children[2].childMarkup.contains("Kashmir")
      tr.children[3].childMarkup.contains("32")
      tr.children[4].childMarkup.contains("Male")
      
      
      when:"add and modify entity"
      def a = down(tr, "a")
      a != null
      doc = pageTester.clickLink(a)
      System.out.println(doc);
      down(root(doc), "class", "t-entity-new-link") != null
      parameters = ["name": "TawusHafeez", "address":"Jammu and Kashmir", "age":"31", "id_0": "26", "gender":"Female", "save":"save"]
      doc = pageTester.submitForm(down(root(doc), "form"), parameters)
      then:
      down(root(doc), "class", "t-entity-new-link") != null
      
      //Check if the new entity has been added
      when:
      table = down(root(doc), "table")
      assert table != null
      assert table.children.children[1].size() == 2;
      tr = down(table, "tr", 3);
      assert tr != null
      then:
      tr.children[0].childMarkup.contains("TawusHafeez")
      tr.children[1].childMarkup.contains("26")
      tr.children[2].childMarkup.contains("Jammu and Kashmir")
      tr.children[3].childMarkup.contains("31")
      tr.children[4].childMarkup.contains("Female")
   }

}
