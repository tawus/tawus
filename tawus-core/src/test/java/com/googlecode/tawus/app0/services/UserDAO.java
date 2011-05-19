package com.googlecode.tawus.app0.services;

import com.googlecode.tawus.app0.models.User;
import com.googlecode.tawus.services.EntityDAO;

public interface UserDAO extends EntityDAO<User>{
   void clear();
}
