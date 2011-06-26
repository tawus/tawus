package com.googlecode.tawus.hibernate.services

import javax.persistence.Entity

import org.apache.tapestry5.annotations.Service
import org.apache.tapestry5.ioc.RegistryBuilder
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.ioc.annotations.SubModule
import org.apache.tapestry5.ioc.internal.util.CollectionFactory
import org.apache.tapestry5.services.TapestryModule

import spock.lang.Specification

import com.googlecode.tawus.SearchCriteria
import com.googlecode.tawus.hibernate.models.Gender
import com.googlecode.tawus.hibernate.models.User
import com.googlecode.tawus.internal.AbstractEntityLocator
import com.googlecode.tawus.internal.def.EntityModuleDef
import com.googlecode.tawus.services.EntityDAO
import com.googlecode.tawus.services.EntityLocator

@SubModule([])
class HibernateEntityDAOIntegrationTest extends Specification {
   @Service("UserDAO")
   @Inject
   private EntityDAO<User> userDAO

   def beforeRegistryCreated(){
      RegistryBuilder builder = new RegistryBuilder();
      EntityLocator entityLocator = new AbstractEntityLocator(CollectionFactory.newSet(
            "com.googlecode.tawus.hibernate.models")){
               public boolean isEntity(@SuppressWarnings("rawtypes") Class entityType){
                  System.out.println(entityType)
                  return entityType.getAnnotation(Entity.class) != null;
               }
            };
      
      builder.add(TapestryModule.class, TestModule.class);
      builder.add(new EntityModuleDef(entityLocator))
      return builder.build();
   }

   def "test operations"(){
      User user = new User();
      user.userName = "tawus"
      user.email = "tawushafeez@gmail.com"
      user.id = 1L
      user.gender = Gender.Male

      expect: userDAO.list().size() == 0
      when: "Check saving entity"
      userDAO.save user
      then:
      userDAO.list().size() == 1
      userDAO.find(user.id).equals(user)
      userDAO.getIdentifier(user) == user.id
      
      when:
      def criteria = new SearchCriteria<User>(User.class)
      criteria.getEntity().setUserName("tawus")
      then:
      userDAO.list(criteria).size() == 1
      userDAO.count(criteria) == 1
      userDAO.list().size() == 1
      userDAO.count() == 1
      
      when:
      criteria = new SearchCriteria<User>(User.class)
      criteria.getEntity().setUserName("taha")
      then:
      userDAO.list(criteria).size() == 0
      userDAO.count(criteria) == 0
      userDAO.list().size() == 1
      userDAO.count() == 1

      when: "Check removing entity"
      userDAO.remove(user)
      then:
      userDAO.list().size() == 0
      
   }
}
