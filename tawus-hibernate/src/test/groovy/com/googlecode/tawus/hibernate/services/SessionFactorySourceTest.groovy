package com.googlecode.tawus.hibernate.services

import java.util.Properties

import org.apache.tapestry5.ioc.services.ClassNameLocator
import org.hibernate.cfg.Configuration

import spock.lang.Specification

import com.googlecode.tawus.hibernate.internal.services.SessionFactorySourceImpl
import com.googlecode.tawus.hibernate.models.Gender
import com.googlecode.tawus.hibernate.models.User


/**
 * This test is for SessionFactorySource
 */
class SessionFactorySourceTest extends Specification {

   def "check configuration is used"(){
      setup:
      ClassNameLocator classNameLocator = Mock(ClassNameLocator)
      SessionFactoryConfiguration configuration =new SessionFactoryConfiguration(){
        
         public String [] getPackageNames(){
            return ["com.googlecode.tawus.hibernate.models"];
         } 
         
         public String getFactoryId(){
            return "default";
         }
         
         public void configure(Configuration configuration){
            Properties prop = new Properties();
            prop.put("hibernate.dialect",
                  "org.hibernate.dialect.HSQLDialect");
            prop.put("hibernate.connection.driver_class",
                  "org.hsqldb.jdbcDriver");
            prop
                  .put("hibernate.connection.url",
                        "jdbc:hsqldb:mem:testdb");
            prop.put("hibernate.connection.username", "sa");
            prop.put("hibernate.connection.password", "");
            prop.put("hibernate.connection.pool_size", "1");
            prop.put("hibernate.connection.autocommit", "false");
            prop.put("hibernate.hbm2ddl.auto", "create-drop");
            prop.put("hibernate.show_sql", "true");
            prop.put("hibernate.current_session_context_class", "thread");
            configuration.addProperties(prop);
         }
      };
      List<SessionFactoryConfiguration> configurations = new ArrayList<SessionFactoryConfiguration>()
      configurations.add(configuration);
      when:
      SessionFactorySource source = new SessionFactorySourceImpl(classNameLocator, configurations);
      then:
      1 * classNameLocator.locateClassNames("com.googlecode.tawus.hibernate.models") >> 
         ["com.googlecode.tawus.hibernate.models.User", "com.googlecode.tawus.hibernate.models.Address",
            "com.googlecode.tawus.hibernate.models.UserGroup",
            "com.googlecode.tawus.hibernate.models.Gender"
            ]
       source.getFactoryId(User.class).equals("default");
       source.hasSessionFactory(User.class) == true
       source.hasSessionFactory(Gender.class) == false  
   }
}
