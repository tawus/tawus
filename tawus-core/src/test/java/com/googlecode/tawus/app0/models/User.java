package com.googlecode.tawus.app0.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class User {

   private Long id;

   @NotNull
   private String name;
   
   private String address;
   
   private Gender gender;
   
   private Date dob;
   
   private Integer age;
   
   private Department department;
   
   private List<Department> otherDepartments = new ArrayList<Department>();

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

   public void setAddress(String address) {
      this.address = address;
   }

   public String getAddress() {
      return address;
   }

   public void setGender(Gender gender) {
      this.gender = gender;
   }

   public Gender getGender() {
      return gender;
   }

   public void setDob(Date dob) {
      this.dob = dob;
   }

   public Date getDob() {
      return dob;
   }

   public void setAge(Integer age) {
      this.age = age;
   }

   public Integer getAge() {
      return age;
   }

   public void setDepartment(Department department) {
      this.department = department;
   }

   public Department getDepartment() {
      return department;
   }
   
   public String toString(){
      if(name == null){
         return "unnamed";
      }
      return name;
   }
   
   public boolean equals(Object object){
      if(object == this){
         return true;
      }
      
      if(object == null || !(object instanceof User)){
         return false;
      }
      
      User user = (User)object;
      if(user.getName() == null){
         return false;
      }
      
      return user.getName().equals(name);
   }
   
   public int hashCode(){
      return name.hashCode();
   }

   public void setOtherDepartments(List<Department> otherDepartments) {
      this.otherDepartments = otherDepartments;
   }

   public List<Department> getOtherDepartments() {
      return otherDepartments;
   }
}
