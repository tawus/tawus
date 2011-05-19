package com.googlecode.tawus.app0.models;

import javax.persistence.Entity;

@Entity
public class Department {

   private Long id;
   
   private String name;

   public void setId(Long id) {
      this.id = id;
   }

   public Long getId() {
      return id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }
   
   public String toString(){
      return name;
   }
   
   public boolean equals(Object object){
      if(object == this){
         return true;
      }
      
      if(object == null || !(object instanceof User)){
         return false;
      }
      
      Department department = (Department)object;
      if(department.getName() == null){
         return false;
      }
      
      return department.getName().equals(name);
   }
   
   public int hashCode(){
      return name.hashCode();
   }
}
