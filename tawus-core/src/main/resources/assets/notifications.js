var Gr0wl = {};

Gr0wl.Base = Class.create({
   
    queue:[],
    
   options: {
      image: 'var-logo-60.png',
      title: 'Growl.Smoke<br/>Script.aculo.us mod',
      text: 'http://blog.var.cc/static/growl/',
      duration: 1.5
   },
   
   initialize: function(image) {
       this.image = new Element('img',{src:image});
      this.create();
    },
   
   create: function(styles) {
       this.image.setStyle('position:absolute;display:none');
      Element.setOpacity(this.image, 0.0);
      this.block = new Element('div');
        this.block.setStyle('position:absolute;display:none;z-index:999;color:#fff;font: 12px/14px "Lucida Grande", Arial, Helvetica, Verdana, sans-serif;'+styles.div);
      this.block.setOpacity(0.0);
      this.block.insert(new Element('img').setStyle(styles.img));
      this.block.insert(new Element('h3').setStyle(styles.h3));
      this.block.insert(new Element('p').setStyle(styles.p));
   },
   
    show: function(options) {
       
        options = Object.extend(this.options, options);
       var elements = [this.image.cloneNode(true), this.block.cloneNode(true)];
        
        elements.each(function(e, i) {
            document.body.appendChild(e);
            e.setStyle(options.position);
            if(i) {
                var img = e.down(0);
                img.setAttribute('src', options.image);
                img.next().update(options.title).next().update(options.text);
            }
        });
      
        var fxOpts = {duration:0.8, from:0.0, sync:true};
        new Effect.Event({
            beforeStart: function() {
                new Effect.Parallel([
                    new Effect.Opacity(elements[0], Object.extend(fxOpts, {to:0.6})),
                    new Effect.Opacity(elements[1], Object.extend(fxOpts, {to:0.9}))
                ], {duration:0.8, queue:options.queue}
                
                );
            },
            
            duration: options.duration,
            queue: options.queue
        });
        
        this.hide(elements);
        
    },
   
   hide: function(elements) {
        var fxOpts = {
            duration:0.8, 
            to:0.0,
            afterFinish: function(o) {
                o.element.remove();
            },
            sync:true
        };
        
        new Effect.Parallel([
            new Effect.Opacity(elements[0], Object.extend(fxOpts, {from:0.6})),
            new Effect.Opacity(elements[1], Object.extend(fxOpts, {from:0.9}))
        ], {
            duration:0.8, 
            queue:'end', 
            afterFinish:(function() {
                this.queue.shift();
            }).bind(this)
           }
        );
    },
   
   getScrollTop: function() {
        var scrollTop = document.body.scrollTop;
        if (scrollTop == 0) {
            if (window.pageYOffset) {
                scrollTop = window.pageYOffset;
            } else {
                scrollTop = (document.body.parentElement) ? document.body.parentElement.scrollTop : 0;
            }
        }
        return scrollTop;
   },
    
    getScrollLeft: function() {
        var scrollLeft = document.body.scrollLeft;
        if (scrollLeft == 0) {
            if (window.pageXOffset) {
                scrollLeft = window.pageXOffset;
            } else {
                scrollLeft = (document.body.parentElement) ? document.body.parentElement.scrollLeft : 0;
            }
        }
        return scrollLeft;
    }
   
});


Gr0wl.Smoke = Class.create(Gr0wl.Base, {
   
   
   
   create: function($super, oArgs) {
      $super({
         div: 'width:298px;height:73px;',
         img: 'float:left;margin:12px;',
         h3: 'margin:0;padding:10px 0px;font-size:13px;',
         p: 'margin:0px 10px;font-size:12px;'
      });
   },
   
   show: function($super, options) {
      
      var last = this.queue.last();
      if (!last) {
        last = 0;
      }
      scrollTop = this.getScrollTop();
      delta = scrollTop+10+(last*83);
      options = Object.extend(options, {
            position: {'top':delta+'px', 'right':'10px', 'display':'block'},
            queue: 'parallel'
        }); 
        
        this.queue.push(last+1);
      $super(options);
   },
   
   hide: function($super, elements) {
      $super(elements,{'opacity': 0 });
   }
   
});

Gr0wl.Bezel = Class.create(Gr0wl.Base, {
   
    
   create: function($super) {
      this.i=0;
      $super({
         div: 'width:211px;height:206px;text-align:center;',
         img: 'margin-top:25px;',
         h3: 'margin:0;padding:0px;padding-top:22px;font-size:14px;',
         p: 'margin:15px;font-size:12px;'
      });
   },
   
   show: function($super, options) {
      var top = this.getScrollTop()+(document.body.offsetHeight /2)-105,
      left = this.getScrollLeft()+(document.body.offsetWidth / 2)-103;
        options = Object.extend(options, {
            position: {'top':top+'px', 'left':left+'px', 'display':'block'},
            queue: 'end'
        });
        $super(options);
    },
   
   hide: function($super, elements) {
      $super(elements, { 'opacity': 0, 'margin-top': [0,50] });
   }
   
});

var Growl =  {
    initialize: function(prefix){
        this.oBezel = new Gr0wl.Bezel(prefix + "bezel.png");
        this.Bezel =  this.oBezel.show.bind(this.oBezel);
        this.oSmoke = new Gr0wl.Smoke(prefix + "smoke.png");
        this.Smoke = this.oSmoke.show.bind(this.oSmoke);
    }
};


Notifications = {
   display:function(spec){  
      Growl.initialize(spec.url);
      for(var i = 0; i < spec.errors.length; ++i){
         Growl.Smoke({
            title: "Error",
            text: spec.errors[i],
            image: spec.url + "error.png",
            duration: 10.0
        });
      }
      for(var i = 0; i < spec.warnings.length; ++i){
         Growl.Smoke({
            title: "Warning",
            text: spec.warnings[i],
            image: spec.url + "warn.png",
            duration: 2.0
        });
      }
         for(var i = 0; i < spec.informations.length; ++i){
            Growl.Smoke({
               title: "Information",
               text: spec.informations[i],
               image: spec.url + "inform.png",
               duration: 5.0
           });
         }
      }
   
};
