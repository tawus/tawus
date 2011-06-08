package com.googlecode.tawus;

/**
 * EasyCrudEvents
 * A set of events used by components in this module
 */
public class TawusEvents {
   /**
    * Show details
    * This event is triggered when a id link is clicked in the
    * {@link com.googlecode.tawus.components.EntityDisplay}
    * It can be captured using onShowDetails method
    */
   public static final String SHOW_DETAILS = "showDetails";
   
   /**
    * This event is triggered after {@link com.googlecode.tawus.components.EntityEditForm}
    * cancel button is clicked. 
    */
   public static final String AFTER_CANCEL = "afterCancel";
   
   /**
    * This event is triggered before entity is to be saved in 
    * {@link com.googlecode.tawus.components.EntityEditForm}
    */
   public static final String BEGIN_SAVE = "beginSave";
   
   /**
    * This event is trigged after entity has been saved in
    * {@link com.googlecode.tawus.components.EntityEditForm}
    */
   public static final String AFTER_SAVE = "afterSave";
   
   /**
    * This event is trigged before the entity is to be deleted
    * in {@link com.googlecode.tawus.components.EntityEditForm}
    */
   public static final String BEGIN_DELETE = "beginDelete";
   
   /**
    * This event is triggered after the entity is to be deleted
    * in {@link com.googlecode.tawus.components.EntityEditForm}
    */   
   public static final String AFTER_DELETE = "afterDelete";
   
   /**
    * This event is triggered after form has been submitted by
    * any submit button in 
    * {@link com.googlecode.tawus.components.EntityEditForm}
    */
   public static final String FINISHED = "finished";
   
   /**
    * This event is triggered after search form 
    * {@link com.googlecode.tawus.components.EntitySearchForm}
    * has been submitted
    * 
    */
   public static final String SEARCH = "search";

   public static final String PREPARE_SEARCH = "prepareSearch";

   public static final String PREPARE = "entityPrepare";

   public static final String LIST_ADD_ROW = "addRowToList";

   public static final String PREPARE_FOR_RENDER = "entityPrepareForRender";
   
   public static final String PREPARE_FOR_SUBMIT = "entityPrepareForSubmit";

   public static final String PAGE_CHANGE = "pageChange";

   public static final String SAVE = "save";

   public static final String DELETE = "delete";

   public static final String CANCEL = "cancel";

   public static final String SORT = "sort";

   public static final String CANCEL_SEARCH = "cancelSearch";
}

