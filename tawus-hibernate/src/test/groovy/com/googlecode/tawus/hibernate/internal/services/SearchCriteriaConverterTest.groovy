package com.googlecode.tawus.hibernate.internal.services

import org.apache.tapestry5.ioc.RegistryBuilder
import org.apache.tapestry5.ioc.annotations.Inject
import org.apache.tapestry5.ioc.annotations.SubModule
import org.apache.tapestry5.services.TapestryModule
import org.hibernate.Criteria
import org.hibernate.Session

import spock.lang.Shared
import spock.lang.Specification

import com.googlecode.tawus.hibernate.internal.services.HibernateSearchCriteriaConverter.Context
import com.googlecode.tawus.hibernate.models.Address
import com.googlecode.tawus.hibernate.models.Gender
import com.googlecode.tawus.hibernate.models.User
import com.googlecode.tawus.hibernate.services.SessionFactorySource
import com.googlecode.tawus.hibernate.services.TestModule

@SubModule([])
class SearchCriteriaConverterTest extends Specification {
   @Shared converter
   @Shared context;
    
   @Inject
   Session session;
   
   @Inject
   SessionFactorySource source
   
   def beforeRegistryCreated(){
      RegistryBuilder builder = new RegistryBuilder();
      builder.add(TapestryModule.class, TestModule.class);
      return builder.build();
   }
   
   def setup(){
      session = source.createSession("default")
      converter = new HibernateSearchCriteriaConverter()
      context = new Context();
   }
   
   def "check loadPropertyValues with empty object"(){
      setup:
      User user = new User();
      expect:source.hasSessionFactory(User.class) == true
      when:
      converter.loadPropertyValues(user, null, session, context);
      then:
      context.getValueMap().size() == 0
   }
   
   def "check loadPropertyValues() with filled object"(){
      setup:
      User user = new User();
      user.userName = "tawus"
      user.email = "tawushafeez@gmail.com"
      user.gender = Gender.Male
      user.id = 1L
      when:
      converter.loadPropertyValues(user, null, session, context);
      then:
      context.getValueMap().size() == 4
      context.getValue("userName").equals(user.userName)
      context.getValue("email").equals(user.email)
      context.getValue("gender").equals(user.gender)
      context.getValue("id").equals(user.id)
   }
   
   def "check loadPropertyValues() with nested objects"(){
      setup:
      User user = new User();
      Address address = new Address();
      address.city = "Srinagar"
      address.country = "Kashmir"
      address.id = 1L
      user.address = address
      when:
      converter.loadPropertyValues(user, null, session, context);
      then:
      context.getValueMap().size() == 1
      context.getValue("address.id").equals(address.id)
      context.getValueMap().containsKey("address.country") == false
      context.getValue("address.id").equals(address.id)
   }
   
   def "check getPropertyName for alias creation"(){
      setup:
      User user = new User()
      Address address = new Address();
      address.city = "Srinagar"
      address.country = "Kashmir"
      address.id = 1L
      user.address = address
      Criteria criteria = Mock(Criteria)
      when:
      def value = converter.getPropertyName(criteria, "address.city", context)
      then:
      value == "address.city"
      
      context.getAlias("address") == "address"
   }
   
   def cleanup(){
      session.close();
   }
}
