package com.googlecode.tawus.hibernate.utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class MD5SumType implements UserType {
   @Override
   public int [] sqlTypes(){
      return new int []{ Types.VARCHAR };
   }

   @Override
   @SuppressWarnings("rawtypes")
   public Class returnedClass(){
      return String.class;
   }

   @Override
   public boolean equals(Object x, Object y) throws HibernateException {
      return (x == y) || (x != null && y != null && x.equals(y));
   }

   @Override
   @SuppressWarnings("deprecation")
   public Object nullSafeGet(final ResultSet rs, final String [] names, final Object owner) throws 
      HibernateException, SQLException {
      final String val = (String)Hibernate.STRING.nullSafeGet(rs, names[0]);
      return val == null ? null: val.trim();
   }

	@Override
   public Object replace(Object original, Object target, Object owner) throws HibernateException{
      return original;
   }

   @Override
   @SuppressWarnings("deprecation")
   public void nullSafeSet(final PreparedStatement ps, Object value, int index)
      throws HibernateException, SQLException {
      String password = null;
      if(value != null){
         password = encrypt((String)value);
      }
      Hibernate.STRING.nullSafeSet(ps, password, index);
   }

   @Override
   public Object deepCopy(Object value) throws HibernateException {
      if(value == null){
         return null;
      }

      return new String((String)value);
   }

   @Override
   public boolean isMutable(){
      return true;
   }

	@Override
   public Serializable disassemble(Object value) throws HibernateException{
      return (Serializable)value;
   }

	@Override
   public Object assemble(Serializable cached, Object owner) throws HibernateException{
      return cached;
   }

	@Override
   public int hashCode(Object x) throws HibernateException{
      return x.hashCode();
   }

   private String encrypt(String plainText) throws HibernateException {
      if(plainText.length() == 32){
         return plainText;
      }

      try {
         final MessageDigest md = MessageDigest.getInstance("MD5");
         md.reset();
         String clearPassword = plainText;
         md.update(clearPassword.getBytes("UTF-8"));
         BigInteger hash = new BigInteger(1, md.digest());
         return pad(hash.toString(16),32,'0');
      }catch(Exception ex){
         throw new HibernateException(ex.getMessage(), ex);
      }
   }

   private String pad(String s, int length, char pad) {
      StringBuffer buffer = new StringBuffer(s);
      while (buffer.length() < length) {
         buffer.insert(0, pad);
      }
      return buffer.toString();
   }
}

