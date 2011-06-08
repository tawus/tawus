
Tapestry.Initializer.ShowLoading = function(spec){
   this._div = Element.createElement("div");
   this._div.hide();
   this._div.absolute = true;
   
   var show = function(event){
      this._div.   
   };
   
   var hide = function(event){
      
   };
   
   Event.observe(Tapestry.ZONE_UPDATED_EVENT, hide);   
   Event.observe(Tapestry.FORM_PROCESS_SUBMIT_EVENT, show);
   Event.observe(Tapestry.TRIGGER_ZONE_UPDATE_EVENT, show);
}