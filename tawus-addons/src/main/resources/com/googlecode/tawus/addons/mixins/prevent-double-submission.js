Tapestry.Initializer.preventDoubleSubmission = function(id)
{
   var formElement = $(id);
   
   Event.observe(formElement, "submit", function(event)
   {
      if(formElement.submission)
      {
         event.stop();   
      }  
      
      formElement.submission = true;
   });
   
};