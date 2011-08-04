Tapestry.Initializer.cancelForm = function(params)
{
   var element = $(params.elementId);
   
   Event.observe(element, "click", function(event)
   {
      event.preventDefault();
      var form = $(element).up("form");
      
      if($T(form).zoneId)
      {
         var zoneManager = Tapestry.findZoneManager(form);
         if(zoneManager != null)
         {
            zoneManager.updateFromURL(params.url);   
            return;
         }
      }
      
      // Not within a form
      window.location.href = params.url;
      
   });
};