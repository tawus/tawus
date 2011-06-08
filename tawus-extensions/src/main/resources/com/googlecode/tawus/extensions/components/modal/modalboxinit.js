ModalBoxInit = Class.create( {

   /* Initialize Function */
   initialize : function(spec) {
      var options = spec.params;
      if (spec.type == "page") {
         Event.observe($(spec.id), spec.event, function() {
            Modalbox.show(spec.href, options);
         });
      } else {
         Event.observe($(spec.id), spec.event, function() {
            var successHandler = function(transport) {
               var node = new Element('div')
                     .update(transport.responseJSON.content);
               options.onContentLoaded = function() {
                  Tapestry.loadScriptsInReply(transport.responseJSON, function() {
                  });
               };
               options.onAfterHide = function(){
                  if(spec.closeHref){
                     Tapestry.ajaxRequest(spec.closeHref, function(){});
                  }
               };
               Modalbox.show(node, options);
            }.bind(this);

            Tapestry.ajaxRequest(spec.href, {
               method : 'get',
               onSuccess : successHandler
            });
         }.bind(this));/* Event.observe */
      }
   }
});
