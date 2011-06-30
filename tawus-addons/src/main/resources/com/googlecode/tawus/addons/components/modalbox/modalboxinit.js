// 
// Copyright 2011 Taha Hafeez Siddiqi (tawushafeez@gmail.com)
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// 
Tapestry.Initializer.setupModalDialog = function(params)
{
   // Setup zone
   var element = $(params.id);
   $T(element).zoneId = params.zone;
   
   var showModalbox = function()
   {
         var loadContentWithScripts = function(transport) 
         {
            var node = new Element('div').update(transport.responseJSON.content);
            params.options.onContentLoaded = function()
            {
               Tapestry.loadScriptsInReply(transport.responseJSON, function() {});
            };
            
            params.options.afterHide = function()
            {
               if(params.zone)
               {
                  var zoneManager = Tapestry.findZoneManager(element);
                  zoneManager.updateFromURL(params.closeLink);    
               }
            };
            
            Modalbox.show(node, params.options);
         };

         Tapestry.ajaxRequest(params.openLink, {
            method : 'get',
            onSuccess : loadContentWithScripts
         });
   };
   
   Event.observe($(params.id), params.event, showModalbox);

};

