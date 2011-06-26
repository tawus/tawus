
package com.googlecode.tawus.hibernate.internal.services

import org.apache.tapestry5.ioc.services.PropertyAccess
import org.apache.tapestry5.ioc.services.TypeCoercer
import org.hibernate.Session

import spock.lang.Shared
import spock.lang.Specification

import com.googlecode.tawus.hibernate.internal.services.HibernateEntityDAOImpl
import com.googlecode.tawus.hibernate.models.User

class HibernateEntityDAOTest extends Specification {
   @Shared dao
   @Shared session
   @Shared propertyAccess
   @Shared typeCoercer
   @Shared user
   
   static class DAO extends HibernateEntityDAOImpl {
      def session;
      
      DAO(Session session, PropertyAccess propertyAccess, TypeCoercer typeCoercer, Class<?> type, Class<?> idType, String idName){
         super(null, propertyAccess, typeCoercer, type, idType, idName);
         this.session = session;
      }
      
      @Override
      public Session getSession(){
         return session;
      }
   }
   
   def setup(){
      session = Mock(Session)
      propertyAccess = Mock(PropertyAccess)
      typeCoercer = Mock(TypeCoercer)
      
      dao = new DAO(session, propertyAccess, typeCoercer, User.class, Long.class, "id");
      user = new User()
      user.id = 1
      user.userName = "someone"
   }
   
   def "getSession works"(){
      expect: dao.getSession() != null
   }
   
   def "find works"(){
      when:
      dao.find(1) == user
      then:
      1 * session.get(User.class, 1) >> user
   }
   
   def "save works"(){
      when: dao.save(user)
      then: 1 * session.save(user)
   }
   
   def "update works"(){
      when: dao.update(user)
      then: 1 * session.update(user)
   }
   
   def "saveOrUpdate works"(){
      when: dao.saveOrUpdate(user)
      then: 1 * session.saveOrUpdate(user)
   }
   
   def "merge works"(){
      when:dao.merge(user)
      then:1 * session.merge(user)
   }
   
   def "flush() works"(){
      when:dao.flush()
      then: 1 * session.flush()
   }

   def "remove() works"(){
      when:dao.remove(user)
      then: 1 * session.delete(user)
   }
   
   def "clear() works"(){
      when:dao.clear()
      then: 1 * session.clear()
   }

}
