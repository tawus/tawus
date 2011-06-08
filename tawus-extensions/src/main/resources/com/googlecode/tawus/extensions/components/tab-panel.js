TabPanel = Class.create();
TabPanel.showTab = function(link, tabToShow){
   Event.observe($(link), "click", function(event){
      $(link).parentNode.siblings().each(function(element){
         if(element.hasClassName("t-tab-active")){
            element.removeClassName("t-tab-active").addClassName("t-tab-enabled");
         }
      });
      $(link).parentNode.addClassName("t-tab-active");

      $(tabToShow).siblings().each(function(element){
         if(element.hasClassName("t-tab-show")){
            element.addClassName("t-tab-hide").removeClassName("t-tab-show");
         }
      });

      $(tabToShow).addClassName("t-tab-show").removeClassName("t-tab-hide");
      event.stop();
   }.bind($(link)));
};
