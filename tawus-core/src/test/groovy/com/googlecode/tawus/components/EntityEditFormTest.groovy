package com.googlecode.tawus.components

import static com.googlecode.tawus.internal.predicates.CommonPredicates.*

import org.apache.tapestry5.dom.Document
import org.apache.tapestry5.dom.Element

import spock.lang.Ignore
import spock.lang.Shared

import com.googlecode.tawus.TestSpecification


class EntityEditFormTest extends TestSpecification {
   @Shared Document doc;

   def "beaneditor is rendered"(){
      when:
      List<Element> trs = doc.getElementById("form").getChildren();

      then:
      notThrown(NullPointerException)
      doc.getRootElement().getElement(like("cancel")).getName().equals("button")
      doc.getRootElement().getElement(like("cancel")).getAttribute("type").equals("button")
      doc.getRootElement().getElement(like("delete")) == null
      trs.size() == 3

      expect:
      Element element = trs.get(i)
      element.getName().equals(tagName)
      where:
      i << [0, 1, 2]
      tagName << ["div", "table", "div"]
      
   }

   def "text_fields are rendered"(){
      when:
      def name = doc.getRootElement().getElementByAttributeValue("name", "name")
      def id  = doc.getRootElement().getElementByAttributeValue("name", "id_0")
      then:
      name.getName().equals("input")
      name.getAttribute("type").equals("text")
      id.getName().equals("input")
      id.getAttribute("type").equals("text")
   }

   def "enum based select are rendered"(){
      when:
      Element enumSelect = doc.getRootElement().getElementByAttributeValue("name", "gender")
      then:
      enumSelect != null
      enumSelect.getName().equals("select")
      enumSelect.getChildren().size() == 3

      when:
      then:
      enumSelect.getChildren().get(i).getName().equals("option")
      enumSelect.getChildren().get(i).getAttribute("value").equals(value)
      where:
      i << [0, 1, 2]
      value << ["", "Male", "Female"]
   }

   def "date field are rendered"(){
      when:
      Element dateField = doc.getRootElement().getElementByAttributeValue("name", "dob")
      then:
      dateField != null
      dateField.getName().equals("input")
      dateField.getAttribute("type").equals("text")
   }

   def "textarea are rendered on override"(){
      when:
      Element dateField = doc.getRootElement().getElementById("address")
      then:
      dateField != null
      dateField.getName().equals("textarea")
   }

   def "entity select is rendered"(){
      when:
      Element entitySelect = doc.getRootElement().getElementByAttributeValue("name", "department")
      then:
      entitySelect != null
      entitySelect.getName().equals("select")
      entitySelect.getChildren().size() == 3

      when:
      then:
      entitySelect.getChildren().get(i).getName().equals("option")
      entitySelect.getChildren().get(i).getAttribute("value").equals(value)
      where:
      i << [0, 1, 2]
      value << ["", "1", "2"]
   }

   def "entity list select is rendered"(){
      when:
      Element entitySelect = doc.getRootElement().getElementByAttributeValue("name", "otherDepartments")
      Element entitySelectAvail = doc.getRootElement().getElementByAttributeValue("name", "otherDepartments-avail")
      then:
      entitySelect != null
      entitySelectAvail != 0

      expect:
      entitySelectAvail.getChildren().get(i).getName().equals("option")
      entitySelectAvail.getChildren().get(i).getAttribute("value").equals(value)
      where:
      i << [0, 1]
      value << ["1", "2"]
   }
   
   def "readonly form is rendered"(){
      when:
      doc = pageTester.renderPage("EntityEditFormDemo/readonly");
      then:
      //Now there should be inputs or selects
      doc.getRootElement().getElementById("name") == null
      doc.getRootElement().getElementById("address") == null
      doc.getRootElement().getElementById("age") == null
   }

   
   def "fill form containing bean editor"(){
      setup:
      Element form = doc.getRootElement().getElementById("form");
      Map<String, String> parameters = new HashMap<String, String>();

      parameters.put("name", "Taha");
      parameters.put("address", "Srinagar")
      parameters.put("department", "1")
      parameters.put("age", "20")
      parameters.put("otherDepartments-values", "[1,2]")
      parameters.put("gender", "Male")
      Document doc = pageTester.submitForm(form, parameters)
      expect:
      doc.getRootElement().getElementById("message") != null
      doc.getRootElement().getElementById("message").getChildMarkup().equals("Taha/Srinagar/Computers/20/[Computers, English]/Male");
   }

   def "validate event is triggered"(){
      setup:
      Element form = doc.getRootElement().getElementById("form");
      Map<String, String> parameters = new HashMap<String, String>();

      parameters.put("name", "Taha");
      parameters.put("address", "Taha")
      parameters.put("department", "1")
      parameters.put("age", "20")
      parameters.put("otherDepartments-values", "[1,2]")
      parameters.put("gender", "Male")
      Document doc = pageTester.submitForm(form, parameters)
      expect:
      doc.getRootElement().getElementByAttributeValue("class", "t-error").getChildMarkup().contains("name and address cannot be same");
   }
   
   def setup(){
      doc = pageTester.renderPage("EntityEditFormDemo/updatable");
      
   }
   
}
