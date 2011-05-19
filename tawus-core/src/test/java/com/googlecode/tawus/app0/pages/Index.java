package com.googlecode.tawus.app0.pages;

import java.util.Date;

import org.apache.tapestry5.ioc.annotations.InjectService;

import com.googlecode.tawus.app0.models.Department;
import com.googlecode.tawus.app0.models.Gender;
import com.googlecode.tawus.app0.models.User;
import com.googlecode.tawus.app0.services.UserDAO;
import com.googlecode.tawus.services.EntityDAO;

public class Index {
   
   @InjectService("UserOverrideDAO")
   private UserDAO userDAO;
   
   @InjectService("UserDAO")
   private EntityDAO<User> entityDAO;
   
   void onActivate(){
      ((UserDAO)userDAO).clear();
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
      entityDAO.save(user);
   }

}
