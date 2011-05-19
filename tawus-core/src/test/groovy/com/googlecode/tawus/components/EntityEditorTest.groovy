package com.googlecode.tawus.components

import org.apache.tapestry5.dom.Document
import org.apache.tapestry5.dom.Element

import spock.lang.Shared

import com.googlecode.tawus.TestSpecification

class EntityEditorTest extends TestSpecification {
   @Shared Document doc;

   def setupSpec(){
      doc = pageTester.renderPage("EntityEditorDemo");
   }

   def "beaneditor is rendered"(){
      when:
      List<Element> trs = doc.getElementById("form").getChildren();

      then:
      notThrown(NullPointerException)
      trs.size() == 6
      
      when:
      then:
      trs.get(i).getChildren().size() == size

      where:
      i << [1, 2, 3, 4, 5]
      size << [1, 4, 4, 4, 4]
   }

   def "caption is rendered"(){
      when:
      List<Element> trs = doc.getElementById("form").getChildren();
      Element tr = trs.get(1);
      then:
      tr.getChildren().size() == 1
      tr.getName().equals("tr")

      when:
      Element td = tr.getChildren().get(0)
      then:
      td.getName().equals("td")
      td.getChildren().size() == 1
      td.getAttribute("colspan").equals("4")
      td.getAttribute("rowspan").equals("1")
      td.getAttribute("class").equals("caption")
      td.getChildMarkup().trim().equals("User Details")
   }

   def "text_fields are rendered"(){
      when:
      List<Element> trs = doc.getElementById("form").getChildren();
      Element tr = trs.get(2);
      then:
      tr.getChildren().size() == 4
      tr.getName().equals("tr")

      when:
      Element tdLabel = tr.getChildren().get(0)
      then:
      tdLabel.getName().equals("td")
      tdLabel.getChildren().size() == 2
      tdLabel.getAttribute("colspan") == null
      tdLabel.getAttribute("rowspan") == null
      tdLabel.getChildren().size() == 2

      when:
      Element label = tdLabel.getChildren().get(0)
      Element divHelp = tdLabel.getChildren().get(1)
      then:
      label.getName().equals("label")
      divHelp.getName().equals("div")
      divHelp.getAttribute("class").equals("help-text")
      divHelp.getChildMarkup().equals("")
      label.getChildMarkup().contains("Name")

      when:
      Element tdInput = tr.getChildren().get(1)
      then:
      tdInput.getName().equals("td")
      tdInput.getChildren().size() == 2 //image icon is also there
      System.out.println(trs.toString());
      tdInput.getAttribute("colspan").equals("1")
      tdInput.getAttribute("rowspan").equals("1")

      when:
      Element input = tdInput.getChildren().get(0)
      then:
      input.getName().equals("input")
      input.getAttribute("type").equals("text")
      label.getAttribute("for").equals(input.getAttribute("name"))
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
      System.out.println(doc)
      Document doc = pageTester.submitForm(form, parameters)
      expect:
      doc.getRootElement().getElementById("message").getChildMarkup().equals("Taha/Srinagar/Computers/20/[Computers, English]/Male");
   }
}
