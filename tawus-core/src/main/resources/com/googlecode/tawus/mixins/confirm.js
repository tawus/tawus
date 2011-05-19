ConfirmDialog = Class.create( {
   initialize : function(spec) {
      if ("submit" == spec.event) {
         $(spec.id).up("form").selected = "";
         Event.observe($(spec.id), "click", function(event) {
            $(spec.id).up("form").selected = Event.element(event).id;
         });

         Event.observe($(spec.id).up("form"), spec.event, function(event) {            
            if (Event.element(event).selected && spec.id == Event.element(event).selected) {
               Event.element(event).selected = null;
               var result = confirm(spec.message);
               if (result == false) {
                  Event.stop(event);
                  return false;
               }
            }
         });
      } else {
        
         Event.observe($(spec.id), spec.event, function(event) {
            var result = confirm(spec.message);
            if (result == false) {
               Event.stop(event);
               return false;
            }
         });
      }
   }
});
