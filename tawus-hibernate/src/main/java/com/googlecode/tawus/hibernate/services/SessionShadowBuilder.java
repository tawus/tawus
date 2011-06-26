package com.googlecode.tawus.hibernate.services;

import org.hibernate.Session;

public interface SessionShadowBuilder {
   Session build(HibernateSessionManager sm, String factoryId);
}
