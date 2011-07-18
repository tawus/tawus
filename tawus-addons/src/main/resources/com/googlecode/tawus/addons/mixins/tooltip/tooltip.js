var tooltip = function()
{
   var e;
   var top_e;
   var bottom_e;
   var content_e;
   var h;
   var current_alpha = 0;
   var end_alpha = 95;
   var left = 3;
   var top = 3;
   var width = 200;
   var height = 150;
   var timer = 20;
   var ie = document.all ? true : false;
   var speed = 10;
   var id = "_tooltip";

   function show(elementId, content, options)
   {
      if(!options)
      {
         options = {};
      }
      width = options.width ? options.width : width;
      height = options.height ? options.height : height;
      timer = options.timer ? options.timer : timer;
      speed = options.speed ? options.speed : speed;

      if(e == null)
      {
         e = document.createElement("div");
         e.setAttribute("id", id);

         top_e = document.createElement("div");
         top_e.setAttribute("id", id + "_top");

         content_e = document.createElement("div");
         content_e.setAttribute("id", id + "_content");

         bottom_e = document.createElement("div");
         bottom_e.setAttribute("id", id + "_bottom");

         e.appendChild(top_e);
         e.appendChild(content_e);
         e.appendChild(bottom_e);

         document.body.appendChild(e);
         e.style.opacity = 0;
         e.style.filter = 'alpha(opacity=0)';
         document.onmouseover = this.pos;
      }

      e.style.display = 'block';
      content_e.innerHTML = content;
      e.style.width = width ? width + "px" : "auto";
      if(!width && ie)
      {
         top_e.style.display = "none";
         bottom_e.style.display = "none";
         e.style.width = e.offsetWidth;
         top_e.style.display = "block";
         bottom_e.style.display = "block";
      }

      h = parseInt(e.offsetHeight) + top;
      clearInterval(e.timer);
      e.timer = setInterval(function(){tooltip.fade(1)}, timer);
   }

   function pos(event)
   {
      var u = ie ? event.clientY + document.documentElement.scrollTop : event.pageY;
      var l = ie ? event.clientX + document.documentElement.scrollLeft : event.pageX;

      e.style.top = (u - h) + 'px';
      e.style.left = (l + left) + 'px';
   }

   function fade(period)
   {
      var alpha = current_alpha;
      if((alpha != end_alpha && period == 1) || (alpha != 0 && period == -1))
      {
         var i = speed;
         if(end_alpha - alpha < speed && period == 1)
         {
            i = end_alpha - alpha;
         }
         else if(alpha < speed && period == -1)
         {
            i = alpha;
         }

         current_alpha = (alpha + (i * period));
         e.style.opacity = current_alpha * 0.01;
         e.style.filter = 'alpha(opacity=' + current_alpha + ')';
      }
      else 
      {
         clearInterval(e.timer);
         if(period == -1)
         {
            e.style.display = "none";
         }
      }
   }

   function hide()
   {
      clearInterval(e.timer);
      e.timer = setInterval(function(){tooltip.fade(-1);}, timer);
   }

   return {
      show: show,
      fade:fade,
      pos:pos,
      hide:hide
   };
}();
