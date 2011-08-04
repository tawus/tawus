package com.googlecode.tawus.internal;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;

import com.googlecode.tawus.EntityValueEncoder;
import com.googlecode.tawus.services.EntityDAO;

public class EntityValueEncoderFactory<E> implements ValueEncoderFactory<E>
{

   private EntityDAO<E> entityDAO;

   public EntityValueEncoderFactory(EntityDAO<E> entityDAO)
   {
      this.entityDAO = entityDAO;
   }

   public ValueEncoder<E> create(Class<E> clazz)
   {
      return new EntityValueEncoder<E>(entityDAO);
   }

}
