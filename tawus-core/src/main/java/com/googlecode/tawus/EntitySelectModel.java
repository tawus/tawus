package com.googlecode.tawus;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * Select Model for entity The description is used for labeling
 */
public class EntitySelectModel<E> extends AbstractSelectModel
{
   private List<E> entityList;

   /**
    * Constructor
    * 
    * @param entityList
    *           list of entities
    */
   public EntitySelectModel(final List<E> entityList)
   {
      this.entityList = entityList;
   }

   /**
    * {@inheritDocs}
    */
   public List<OptionGroupModel> getOptionGroups()
   {
      return null;
   }

   /**
    * {@inheritDocs}
    */
   public List<OptionModel> getOptions()
   {
      List<OptionModel> options = new ArrayList<OptionModel>();
      for(Object entity : entityList)
      {
         options.add(new OptionModelImpl(entity.toString(), entity));
      }
      return options;
   }
}
