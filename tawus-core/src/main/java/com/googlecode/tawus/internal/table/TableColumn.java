package com.googlecode.tawus.internal.table;

public class TableColumn implements java.io.Serializable {
   private static final long serialVersionUID = 1L;
   public static final String ROW_SEPARATOR = ";";
   public static final String COLUMN_SEPARATOR = ",";
   public static final String FIELD_SEPARATOR = "/";
   
   private String property;
   private int colspan;
   private int rowspan;
   private String cssClass;
   private boolean literal;
   private int colspanMultiple;

   public TableColumn(String [] parts, int colspanMultiple){
      property = parts[0].trim();
      literal = Character.isUpperCase(property.charAt(0));
      colspan = parts.length > 1 ? Integer.parseInt(parts[1].trim()):1;
      rowspan = parts.length > 2 ? Integer.parseInt(parts[2].trim()):1;
      cssClass= parts.length > 3 ? parts[3].trim():"";
      this.colspanMultiple = colspanMultiple;
   }

   public String getProperty(){
      return property;
   }

   public int getColspan(){
      return colspan * colspanMultiple;
   }

   public int getRowspan(){
      return rowspan;
   }

   public String getCssClass(){
      return cssClass;
   }

   public boolean getLiteral(){
      return literal;
   }

   public int getColspanMultiple(){
      return colspanMultiple;
   }
}
