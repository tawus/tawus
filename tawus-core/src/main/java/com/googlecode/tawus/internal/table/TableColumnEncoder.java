package com.googlecode.tawus.internal.table;

import org.apache.tapestry5.ValueEncoder;

public class TableColumnEncoder implements ValueEncoder<TableColumn> {
   public String toClient(TableColumn column){
      return String.format("%s/%d/%d/%s-%d", column.getProperty(),
            column.getRowspan(), column.getColspan(), column.getCssClass(),
            column.getColspanMultiple());
   }

   public TableColumn toValue(String clientValue){
      String [] values = clientValue.split("-");
      return new TableColumn(values[0].split("/"),
            Integer.parseInt(values[1]));
   }
}



