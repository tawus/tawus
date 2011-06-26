package com.googlecode.tawus.hibernate.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


@Entity
public class Address implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue
   private Long id;

   @NotNull
   private String street;
   @NotNull
   private String city;
   @NotNull
   private String state;
   @NotNull
   private String country;

   @ManyToOne
   private User user;

   public Address(){}

   public Address(String street, String city, String state, String country){
      setStreet(street);
      setCity(city);
      setState(state);
      setCountry(country);
   }

   public String getStreet(){
      return street;
   }

   public void setStreet(String street){
      this.street = street;
   }

   public String getCity(){
      return city;
   }

   public void setCity(String city){
      this.city = city;
   }

   public String getState(){
      return state;
   }

   public void setState(String state){
      this.state = state;
   }

   public String getCountry(){
      return country;
   }

   public void setCountry(String country){
      this.country = country;
   }

   public User getUser(){
      return user;
   }

   public void setUser(User user){
      this.user = user;
   }

   public String toString(){
      return street + ", " + state + ", " + country;
   }

   public Long getId(){
      return id;
   }

   public void setId(Long id){
      this.id = id;
   }

   @Override
   public boolean equals(Object o){
      if(o == null || !(o instanceof User)){
         return false;
      }
      Address a = (Address)o;
      if(getId() != null && getId().equals(a.getId())){
         return true;
      }
      return false;
   }

   @Override
   public int hashCode(){
      if(getId() == null){
         return super.hashCode();
      }
      return getId().hashCode();
   }
}

