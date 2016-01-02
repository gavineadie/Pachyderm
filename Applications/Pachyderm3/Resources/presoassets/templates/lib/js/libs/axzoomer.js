/*!
 * jQuery axzoomer
 * Alban Xhaferllari
 * albanx@gmail.com
 * Copyright 2011, AUTHORS.txt (http://www.albanx.com)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * Please preserve this heading always
 * http://jquery.org/license
 */

(function($)
{
	var methods =
	{
		init : function(options)
		{
    	    return this.each(function() 
    	    {
    		    var settings = 
    		    {
    		    	'maxZoom':4,
    		    	'zoomIn':'zoom-in.png',
    		    	'zoomOut':'zoom-out.png',
    		    	'reset':false,
    		    	'showControls':true,
    		    	'controlsPos':['center','bottom'],
    		    	'opacity':0.5,
    		    	'sensivity':10,
    		    	'overrideMousewheel':true,
    		    	'mousewheel':true,
    		    	'resetImage':false
    		    };
				if(this.tagName!='IMG')//apply only to tag image
				{
					$.error('Zoomer plugin applies only to img tag.');
					return false;
				}
				
				var $this=$(this);
				var _this=this;
				var OLD_SRC=_this.src;
				if($this.hasClass('ax-zoom'))	return;
				
				
				/*============================================================*\
				 Extend options with global settings then with image settings
				 \*===========================================================*/
    	    	try
    	    	{
    	    		eval('var inlineOpts = {' + $(this).attr('title') + '}');
    	    	}catch(err){}
    	    	
    	    	//extend with global settings
    	    	if(options) $.extend(settings,options);
    	    	
    	    	//extend with image title settings if specified
    	    	if(typeof(inlineOpts)!='undefined') $.extend(settings,inlineOpts);
    	    	
				//mem old settings for destroy call
				$this.data('init-status',{
					parent:$this.parent(),
					src:$this.attr('src')
				})
				 .addClass('ax-zoom')
				 .css({'position':'absolute','top':0,'left':0})
				 .data('settings',settings);
 	    	
    	    	
				var MAIN_WIDTH=0;
				var MAIN_HEIGHT=0;
			
				/*============================================================*\
				 Detect device type, normal mouse or touchy(ipad android)
				\*============================================================*/
	            var touchy=("ontouchstart" in document.documentElement)?true:false;
				var move='touchmove.axzoomer mousemove.axzoomer';
				var end='touchend.axzoomer mouseup.axzoomer';
				var down='touchstart.axzoomer mousedown.axzoomer';

				
				/*============================================================*\
				 create the container div for image and toolbar and append to image parent
				\*============================================================*/
				var container=$('<div class="ax-container" style="position:relative;left:0;top:0;" />') // REMOVED OVERFLOW: HIDDEN; EDITED FOR PACHYDERM
							  .appendTo($this.parent())
							  .hover(function(){
								  //if(settings.showControls)	controlsDiv.show('fade'); // REMOVED
							  },function(){
								  //if(settings.showControls)	controlsDiv.hide('fade'); // REMOVED
							  });
				$this.data('container',container);//mem for desotroy method
				

				/*============================================================*\
				 controls div
				\*============================================================*/
				
				if (Modernizr.touch){
					var controlsDiv=$('<div class="ax-controls"/>')
									.appendTo(container).hide()
									.css({'z-index':1000,'position':'absolute','opacity':settings.opacity});
				}else{
					var controlsDiv=$('<div class="ax-controls"/>')
									.appendTo(container) // REMOVED .hide()
									.css({'z-index':1000,'position':'absolute','opacity':settings.opacity});
				}
								
				/*============================================================*\
				 function to zoom on button down using timers
				\*============================================================*/
				var zoomtimer;
				
				$this.controlZoom=function(x)
				{
					$this.zoomInOut(x);
					zoomtimer=setTimeout(function(){
						$this.controlZoom(x);
					},30); 
				};
				
				
				/*============================================================*\
				 controls buttons
				\*============================================================*/
				if(settings.zoomIn)
				{
					var zoomIn=false;
					if(typeof(settings.zoomIn)=='string')
					{
						zoomIn=$('<img src="'+settings.zoomIn+'" alt="+" />').appendTo(controlsDiv).css('cursor','pointer');
					}
					else if(typeof(settings.zoomIn)=='object')
					{
						zoomIn=settings.zoomIn;
					}
					if(zoomIn)
						zoomIn.bind(down,function(e) {
								  e.stopPropagation();
								  MOUSEX=MAIN_WIDTH/2;
								  MOUSEY=MAIN_HEIGHT/2;
								  $this.controlZoom(0.05);
						  })
						  .bind('dblclick',function(e){e.stopPropagation(); })
						  .bind(end+' mouseout',function(){  clearTimeout(zoomtimer);});			
				}
				
				if(settings.zoomOut)
				{
					//zoom out button
					var zoomOut=false;
					if(typeof(settings.zoomOut)=='string')
					{
						zoomOut=$('<img src="'+settings.zoomOut+'" alt="+" />').appendTo(controlsDiv).css('cursor','pointer');
					}
					else if(typeof(settings.zoomOut)=='object')
					{
						zoomOut=settings.zoomOut;
					}
					if(zoomOut)
						zoomOut.bind(down,function(e) {
								  e.stopPropagation();
								  MOUSEX=MAIN_WIDTH/2;
								  MOUSEY=MAIN_HEIGHT/2;
								  $this.controlZoom(-0.05);
						  })
						  .bind('dblclick',function(e){e.stopPropagation(); })
						  .bind(end+' mouseout',function(){  clearTimeout(zoomtimer);});	
				}

				if(settings.reset)
				{
					var reset=false;
					if(typeof(settings.reset)=='string')
					{
						reset=$('<img src="'+settings.reset+'" alt="+" />').appendTo(controlsDiv).css('cursor','pointer');
					}
					else if(typeof(settings.reset)=='object')
					{
						reset=settings.reset;
					}
					if(reset)
						reset.bind(down,function(e) {
								  e.stopPropagation();
								  MOUSEX=MAIN_WIDTH/2;
								  MOUSEY=MAIN_HEIGHT/2;
								  $this.zoomInOut(-$this.zoomLevel);
						  })
						  .bind('dblclick',function(e){e.stopPropagation(); });	
				}
				
				
				
				//sources
				var MAIN_SRC=$this.attr('src');//normal size source
				var ZOOM_SRC=$this.attr('src-big');//hight resolution source of image, to deprecheate 
				if(settings.srcBig) ZOOM_SRC=settings.srcBig; //hight resolution source of image
				
				//wait image preload for setup
				var imgLoad=new Image();
				imgLoad.onload=function(){
					
					MAIN_WIDTH=_this.width;
					MAIN_HEIGHT=_this.height;
					$this.data('dims',{'width':MAIN_WIDTH,'height':MAIN_HEIGHT});
					container.css({'width':MAIN_WIDTH, 'height':MAIN_HEIGHT}).append($this);
					//setup controls position
					var cpox=settings.controlsPos[0];
					var cpoy=settings.controlsPos[1];
					if(typeof(cpox)=='string')	cpox=(cpox=='right')?MAIN_WIDTH-controlsDiv.width():0;
					if(typeof(cpoy)=='string')	cpoy=(cpoy=='bottom')?MAIN_HEIGHT-controlsDiv.height():0;
					controlsDiv.css({'left':parseInt(MAIN_WIDTH * .5),'top':523}); // EDITED FOR PACHYDERM
					//controlsDiv.css({'left':cpox,'top':cpoy}); EDITED FOR PACHYDERM
				};
				imgLoad.src=_this.src;			
				
				
				/*============================================================*\
				 function that calculate mouse position relative to given element
				\*============================================================*/
				var TOUCHNUM=0;
				function medium_coors(e,rel2obj)
				{
					var fingers=1;
					var otop=0,oleft=0;
					otop=$(rel2obj).offset().top;
					oleft=$(rel2obj).offset().left;
					
					if(touchy)
					{
						e=e.originalEvent;
						fingers=e.touches.length;
						var xsum=0,ysum=0;
						//calculate centroid for touches
						for(var i=0;i<fingers;i++)
						{
							xsum+=(e.touches[i].pageX-oleft);
							ysum+=(e.touches[i].pageY-otop);
						}
					
						return [xsum/fingers,ysum/fingers,fingers];
					}
					else
					{
						return [e.pageX-oleft,e.pageY-otop,fingers];
					}
				}
				
				var START_X=0;//start X move position
				var START_Y=0;//start Y move position
				
				var MOUSEX=0;//mouse X relative to window DIV
				var MOUSEY=0;//mouse Y relative to window DIV

				var LIMIT_X=0,LIMIT_Y=0;
				
				$this.ENABLE_DRAG=false;
				//bind events to window div, not to image tag, we leave image tag free
				container
				.bind(down,function(e){
					e.preventDefault();

					var coors=medium_coors(e,this);
					MOUSEX=coors[0];
					MOUSEY=coors[1];
					START_X=_this.offsetLeft-MOUSEX;
					START_Y=_this.offsetTop-MOUSEY;
					TOUCHNUM=coors[2];

					if(e.shiftKey)
						$this.controlZoom(0.05);
					else if(e.altKey)
						$this.controlZoom(-0.05);
					else
						$this.ENABLE_DRAG=true;					
				})
				.bind(move,function(e)
				{
					//mouse move mouse coords and drag
					e.preventDefault();
					var coors=medium_coors(e,this);
					MOUSEX=coors[0];
					MOUSEY=coors[1];
					TOUCHNUM=coors[2];
					if(TOUCHNUM<=1 && $this.ENABLE_DRAG)	$this.drag();
				})
				.bind(end,function(e)
				{
					$this.ENABLE_DRAG=false;
					clearTimeout(zoomtimer);
				})
				.bind('dblclick',function(e)
				{
					if(!e.altKey && !e.shiftKey)
					{
						var coors=medium_coors(e,this);
						MOUSEX=coors[0];
						MOUSEY=coors[1];
						$this.zoomInOut(1);
					}
				})
				.bind('mousewheel',function(e,delta) 
				{ 
					if(settings.mousewheel)
					{
						e.preventDefault();
						if(settings.overrideMousewheel)		e.stopPropagation();
						$this.zoomInOut(delta/settings.sensivity);			
					}
				});
				_this.ongesturechange=function(e)//not support by android 2.x
				{
					e.preventDefault();
					if(TOUCHNUM>=2)	
					{
						var delta=(e.scale<1)?-1:1;
						$this.zoomInOut(delta/settings.sensivity);
					}
				};
				
				
				$(document).bind(end,function(ev)
				{
					$this.ENABLE_DRAG=false;
					clearTimeout(zoomtimer);
				});
				
				$this.data('ENABLE-AXZ',true);
				$this.drag=function()//simple drag function, no jquery
				{
					if($this.data('ENABLE-AXZ')!=true)	return;
					var new_x=MOUSEX+START_X;
					var new_y=MOUSEY+START_Y;
					//x:[LIMIT_X,0]		
					if(new_x<=0 && new_x>=LIMIT_X)	$this.css({'left':new_x});
					//x:[LIMIT_Y,0]
					if(new_y<=0 && new_y>=LIMIT_Y)	$this.css({'top':new_y});
				};			
												
				/******************************ZOOOM FUNCTIONS****************************/	
				$this.zoomLevel=1;
				var timeoutloader;
			    
				$this.zoomInOut=function (zoom)//zoom is fracntion, percent, not level
				{
					if($this.data('ENABLE-AXZ')!=true)	return;
					//calculate new dims
					var new_w=$this.width()*(1+zoom);
					var new_h=$this.height()*(1+zoom);
					if(new_w<=MAIN_WIDTH) new_w=MAIN_WIDTH;//limit dimension			
					if(new_h<=MAIN_HEIGHT) new_h=MAIN_HEIGHT;
					
					var newzoom=new_w/MAIN_WIDTH;
					if(newzoom>>0<=settings.maxZoom)//a bit faster newzoom<<0 == Math.round(newzoom)
					{
						$this.zoomLevel=newzoom;
						//calculate new position
						
						//limits for maintaing zoom inside window
						LIMIT_X=-MAIN_WIDTH*($this.zoomLevel-1);
						LIMIT_Y=-MAIN_HEIGHT*($this.zoomLevel-1);
						var new_x=zoom*(_this.offsetLeft-MOUSEX)+_this.offsetLeft;
						var new_y=zoom*(_this.offsetTop-MOUSEY)+_this.offsetTop;
						
						if(new_x<LIMIT_X) new_x=LIMIT_X;//limits
						if(new_x>=0) new_x=0;//in this case the limit is 0 but for multiple 3d images may have a limit
					
						if(new_y<=LIMIT_Y) new_y=LIMIT_Y;
						if(new_y>=0) new_y=0;//same
						
						$this.css({'width':new_w,'height':new_h,'top':new_y,'left':new_x});
					}		
					
					if($this.zoomLevel>1.2 && ZOOM_SRC!='' && ZOOM_SRC!=null && ZOOM_SRC!=$this.attr('src'))
					{
						var load=new Image();
						load.onload=function()
						{
							_this.src=ZOOM_SRC;
						};
						load.src=ZOOM_SRC;
					}
					else if($this.zoomLevel<=1.2 && settings.resetImage && OLD_SRC!=$this.attr('src'))
					{
						_this.src=OLD_SRC;
					}
				};
    	    });
		},
		enable:function()
		{
			return this.each(function()
			{
				var $this = $(this);
				$this.data('ENABLE-AXZ',true);
			});
		},
		disable:function()
		{
			return this.each(function()
			{
				var $this = $(this);
				$this.data('ENABLE-AXZ',false);
			});
		},
		destroy : function()
		{
			return this.each(function()
			{
				var $this = $(this);
				$this.removeData('settings').removeClass('ax-zoom');
				var old=$this.data('init-status');
				var dims=$this.data('dims');

				$this.css({'width':dims.width,'height':dims.height}) //reset init css status
				     .appendTo(old.parent)//append to orginal parent
				     .attr('src',old.src)
				     .data('container').remove();//remove container created by plugin
				$(document).unbind('.axzoomer');//unbind events attach to document by plugin
			});
		},
		option : function(option, value)
		{
			return this.each(function(){
				var $this=$(this);
				var curr_setts=$this.data('settings');
				if(value!=null && value!=undefined)
				{
					curr_setts[option]=value;
					$this.data('settings',curr_setts);
				}
				else
					return curr_setts[option];
				
			});
		}
	};

	$.fn.axzoomer = function(method, options)
	{
		if(methods[method])
		{
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		}
		else if(typeof method === 'object' || !method)
		{
			return methods.init.apply(this, arguments);
		}
		else
		{
			$.error('Method ' + method + ' does not exist on jQuery.zoomer3d');
		}
	};

})(jQuery);