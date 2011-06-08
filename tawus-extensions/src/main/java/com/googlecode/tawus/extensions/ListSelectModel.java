package com.googlecode.tawus.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.util.AbstractSelectModel;

public class ListSelectModel<T> extends AbstractSelectModel implements
   ValueEncoder<T> {

   private final List<?> list;
   private Map<String, PropertyAdapter> adapterMap = new HashMap<String, PropertyAdapter>();
   private String label;
   private final static Pattern WORD_PATTERN = Pattern.compile("#(\\w+)");

   public ListSelectModel(final List<?> list, final String label,
      final Class<?> clazz, final PropertyAccess access) {
      this.list = list;
      this.label = label;
      Matcher matcher = WORD_PATTERN.matcher(label);
      while(matcher.find()){
         adapterMap.put(matcher.group(1), access.getAdapter(clazz)
            .getPropertyAdapter(matcher.group(1)));
      }
   }

   public List<OptionGroupModel> getOptionGroups() {
      return null;
   }

   public List<OptionModel> getOptions() {
      final List<OptionModel> options = new ArrayList<OptionModel>();

      if(list != null){
         for(int i = 0; i < list.size(); ++i){
            options.add(new OptionModelImpl(toLabel(list.get(i)), list.get(i)));
         }
      }

      return options;
   }

   private String toLabel(Object object) {
      String label = this.label;
      if(label.trim().equals("")){
         return object.toString();
      }
      
      for(String key : adapterMap.keySet()){
         label = label.replaceAll("#" + key, adapterMap.get(key).get(object)
            .toString());
      }
      return label;
   }

   public String toClient(T value) {
      return String.valueOf(list.indexOf(value));
   }

   @SuppressWarnings("unchecked")
   public T toValue(String index) {
      return (T) list.get(Integer.parseInt(index));
   }
}
