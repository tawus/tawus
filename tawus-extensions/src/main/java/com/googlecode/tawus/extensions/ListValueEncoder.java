package com.googlecode.tawus.extensions;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;

public class ListValueEncoder<T> implements ValueEncoder<T>{
   
   private List<T> list;

   public ListValueEncoder(List<T> list){
      this.list = list;
   }

   public String toClient(T object) {
      return String.valueOf(list.indexOf(object));
   }

   public T toValue(String index) {
      return list.get(Integer.parseInt(index));
   }

}
