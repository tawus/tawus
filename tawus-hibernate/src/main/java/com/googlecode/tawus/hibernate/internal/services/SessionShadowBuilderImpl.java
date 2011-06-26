package com.googlecode.tawus.hibernate.internal.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.apache.tapestry5.ioc.services.Builtin;
import org.apache.tapestry5.ioc.services.ClassFab;
import org.apache.tapestry5.ioc.services.ClassFactory;
import org.apache.tapestry5.ioc.services.MethodSignature;
import org.apache.tapestry5.ioc.util.BodyBuilder;
import org.hibernate.Session;

import com.googlecode.tawus.hibernate.services.HibernateSessionManager;
import com.googlecode.tawus.hibernate.services.SessionShadowBuilder;

public class SessionShadowBuilderImpl implements SessionShadowBuilder {
   private final ClassFactory classFactory;

   public SessionShadowBuilderImpl(@Builtin ClassFactory classFactory) {
      this.classFactory = classFactory;
   }

   public Session build(HibernateSessionManager sm, String sessionFactoryId) {
      @SuppressWarnings("rawtypes")
      Class sourceClass = sm.getClass();
      ClassFab cf = classFactory.newClass(Session.class);

      cf.addField("_source", Modifier.PRIVATE | Modifier.FINAL, sourceClass);
      cf.addConstructor(new Class[] { sourceClass }, null, "_source = $1;");

      BodyBuilder body = new BodyBuilder();
      body.begin();

      body.addln("%s result = _source.getSession(\"%s\");", sourceClass.getName(), sessionFactoryId);

      body.addln("if (result == null)");
      body.begin();
      body.addln("throw new NullPointerException(%s.buildMessage(_source, \"getSession(%s)\"));",
            getClass().getName(), sessionFactoryId);
      body.end();
      body.addln("return result;");
      body.end();

      MethodSignature sig = new MethodSignature(Session.class, "_delegate", null, null);
      cf.addMethod(Modifier.PRIVATE, sig, body.toString());

      String toString = String.format("<Shadow: getSession(%s) of HibernateSessionManager>",
            sessionFactoryId);

      cf.proxyMethodsToDelegate(Session.class, "_delegate()", toString);

      @SuppressWarnings("rawtypes")
      Class shadowClass = cf.createClass();
      try {
         @SuppressWarnings("rawtypes")
         Constructor cc = shadowClass.getConstructors()[0];
         Object instance = cc.newInstance(sm);
         return (Session) instance;
      } catch (Exception ex) {
         // Should not be reachable
         throw new RuntimeException(ex);
      }

   }

   public static final String buildMessage(Object source, String propertyName) {
      return String.format(
            "Unable to delegate method invocation to property '%s' of %s, because the property is null.",
            propertyName, source);
   }
}
