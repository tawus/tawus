package com.googlecode.tawus.app0.pages;

import com.googlecode.tawus.annotations.InjectDAO;
import com.googlecode.tawus.services.EntityDAO;

public class InjectDAONotPresentDemo
{

   @SuppressWarnings("unused")
   @InjectDAO(String.class)
   private EntityDAO<String>   stringDAO;
   
}
