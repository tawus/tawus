package com.googlecode.tawus.app0.services;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.tawus.app0.models.Department;
import com.googlecode.tawus.app0.models.Gender;
import com.googlecode.tawus.app0.models.User;

public class UserDAOImpl extends DummyEntityDAO<User> implements UserDAO {

   public UserDAOImpl(Class<User> type) {
      super(type);
      User user = new User();
      user.setId(1L);
      user.setName("Taha");
      user.setAddress("Srinagar");
      user.setGender(Gender.Male);
      user.setDob(new Date());
      user.setAge(32);
      
      Department d = new Department();
      d.setId(1L);
      d.setName("Computers");
      user.setDepartment(d);
      
      save(user);
   }

   @Override
   public User find(Serializable id) {
      for(User user: getEntities()){
         if(user.getId().equals(id)){
            return user;
         }
      }
      return null;
   }

   @Override
   public Serializable getIdentifier(Object object) {
      return ((User)object).getId();
   }

   @Override
   public void setIdentifier(User entity, Object value) {
      entity.setId((Long)value);
   }
   
   public void clear(){
      this.getEntities().clear();
   }
   
}
