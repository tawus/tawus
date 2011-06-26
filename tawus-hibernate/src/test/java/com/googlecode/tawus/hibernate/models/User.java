package com.googlecode.tawus.hibernate.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.googlecode.tawus.annotations.NonUpdatable;
import com.googlecode.tawus.hibernate.annotations.Unique;

@Entity
@Unique(columns = "userName")
public class User implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue
   private Long id;
   
   @NotNull
   @Size(max = 10)
   private String userName;
   
   private String password;

   @NotNull
   @Size(max = 64)
   private String email;
   
   private String signature;

   @NotNull
   @NonUpdatable
   private Gender gender;

   @ManyToOne
   private Address address;

   @ManyToOne
   private UserGroup userGroup;
   
   public User(){}
   
   public Long getId(){
      return id;
   }

   public void setId(Long id){
      this.id = id;
   }

   public String getUserName(){
      return userName;
   }

   public void setUserName(String userName){
      this.userName = userName;
   }

   public String getPassword(){
      return password;
   }

   public void setPassword(String password){
      this.password = password;
   }

   public String getEmail(){
      return email;
   }

   public void setEmail(String email){
      this.email = email;
   }

   public String getSignature(){
      return signature;
   }

   public void setSignature(String signature){
      this.signature = signature;
   }

   public Gender getGender(){
      return gender;
   }

   public void setGender(Gender gender){
      this.gender = gender;
   }

   @Override
   public boolean equals(Object o){
      if(o == null || !(o instanceof User)){
         return false;
      }
      User u = (User)o;
      if(getId() != null){
         return getId().equals(u.getId());
      }else {
         return super.equals(o);
      }
   }

   @Override
   public int hashCode(){
      if(getId() == null){
         return super.hashCode();
      }
      return getId().hashCode();
   }

   public String toString(){
      return userName + "(" + signature + ")";
   }

   public void setAddress(Address address) {
      this.address = address;
   }

   public Address getAddress() {
      return address;
   }

   public void setGroup(UserGroup userGroup) {
      this.userGroup = userGroup;
   }

   public UserGroup getGroup() {
      return userGroup;
   }
}

