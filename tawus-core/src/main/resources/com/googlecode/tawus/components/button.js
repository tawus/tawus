ButtonEvent = Class.create();
ButtonEvent.prototype = {
   initialize : function(spec) {
      if (!$(spec.id)) {
         throw ("Element " + spec.id + " does not exist");
      }

      this.element = $(spec.id);
      $T(this.element).zoneId = spec.zone;
      this.url = spec.url;
      Event.observe(this.element, spec.event, function(event) {
         if (!event.stopped) {
            if (spec.zone != null) {
               var zoneManager = Tapestry.findZoneManager($(spec.id));
               if (zoneManager) {
                  zoneManager.updateFromURL(spec.url);
                  return;
               }
            }
            document.location = spec.url;
         }
      });
   }
};
