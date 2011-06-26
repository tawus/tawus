package com.googlecode.tawus.hibernate.services;

import java.util.Properties;

import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;

import com.googlecode.tawus.hibernate.services.TawusHibernateModule;
import com.googlecode.tawus.hibernate.services.SessionFactoryConfiguration;
import com.googlecode.tawus.hibernate.services.SessionFactorySource;

@SubModule(TawusHibernateModule.class)
public class TestModule {

   @Contribute(SessionFactorySource.class)
   public void providerSessionFactorySource(
         OrderedConfiguration<SessionFactoryConfiguration> configuration) {
      configuration.add("default",
            new SessionFactoryConfiguration(){
               public void configure(
                     org.hibernate.cfg.Configuration configuration) {
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

               public String[] getPackageNames() {
                  return new String[]{"com.googlecode.tawus.hibernate.models"};
               }

               public String getFactoryId() {
                  return "default";
               }
            });
   }

   @Match( { "*DAO" })
   public static void adviseTransaction(TransactionAdvisor transactionAdvisor,
         MethodAdviceReceiver receiver) {
      transactionAdvisor.addTransactionAdvice(receiver);
   }

}
