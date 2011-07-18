Tapestry.Initializer.setupOpentip = function(params)
{
   if(params.url)
   {
      params.options.ajax = {};
      params.options.ajax.url = params.url;  
   }
   
   $(params.elementId).addTip(params.tip, params.tipTitle, params.options);
};