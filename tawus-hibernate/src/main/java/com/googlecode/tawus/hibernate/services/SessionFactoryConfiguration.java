package com.googlecode.tawus.hibernate.services;

import org.hibernate.cfg.Configuration;

/**
 * Database Configuration. Each database to be used has to be configured
 * using this configuration.
 */
public interface SessionFactoryConfiguration {
   /**
    * List of package names containing the model objects
    * 
    * @return list of package names
    */
   String[] getPackageNames();

   /**
    * String marker to identify DAOs of this database
    * 
    * @return factory id
    */
   String getFactoryId();
   
   /**
    * Configure hibernate configuration
    * 
    * @param configuration
    */
   void configure(Configuration configuration);
}
