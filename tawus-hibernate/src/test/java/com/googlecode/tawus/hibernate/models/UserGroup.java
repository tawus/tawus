package com.googlecode.tawus.hibernate.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class UserGroup implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue
   private Long id;
   
   @NotNull
   private String name;
   
   @NotNull
   @OneToMany(mappedBy = "userGroup")
   @Size(max=1)
   @Valid
   private List<User> users = new ArrayList<User>();

   public void setUsers(List<User> users) {
      this.users = users;
   }

   public List<User> getUsers() {
      return users;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getId() {
      return id;
   }
   

}
