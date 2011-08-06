package com.googlecode.tawus.app0.pages;

import org.apache.tapestry5.annotations.Property;

import com.googlecode.tawus.annotations.InjectDAO;
import com.googlecode.tawus.app0.models.User;
import com.googlecode.tawus.services.EntityDAO;

public class InjectDAODemo
{
   @InjectDAO(User.class)
   private EntityDAO<User> userDAO;
   
   @SuppressWarnings("unused")
   @Property
   private int count;
   
   void onActivate()
   {
      count = userDAO.list().size();
   }
}
