var Slider = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'slider screen',
	xmlName: null,
	activeSlide: 1,
	previousSlide: null,
	xml: null,
	t: null,
	horizontal_iScrollObj: null,
	sliderItemsCount: 0,
	mainMediaWidgets: [],
	scrollingTextWidgets: [],
	animationInterval: null,
	swipeSet: false,
	sliderElements: null,
	preloader: null,
	events: {
		"mousedown span.prev"			:"startSlideHScrollLeft",
		"mouseup span.prev"				:"stopSlideHScroll",
		"touchstart span.prev"			:"startSlideHScrollLeft",
		"touchend span.prev"				:"stopSlideHScroll",
		"mousedown span.next"			:"startSlideHScrollRight",
		"mouseup span.next"				:"stopSlideHScroll",
		"touchstart span.next"			:"startSlideHScrollRight",
		"touchend span.next"				:"stopSlideHScroll"
	},
	initialize: function(options) {
		
		this.horizontal_iScrollObj = null;
		this.sliderItemsCount = 0;
		this.mainMediaWidgets = [];
		this.scrollingTextWidgets = [];
		this.animationInterval = null;
		this.swipeSet = false;
		this.sliderElements = null;
		this.preloader = null;
		
		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;
		this.activeSlide = options.activeSlide;
		
		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container slider"></div>');
		pachyderm.screenContainer.append(this.el);
		
		this.screenContainer = this.$(".screen-container");
		this.screenContainer.hide();
		
		var that = this;
		this.model.fetch({'error': function(m,r){that.onXML(r.responseText);}});
    },
	onXML: function(x){
		var that = this;
		this.xml = x;
		this.contentXML = $(this.xml).find('content');
		this.iScrollObjs = new Array();
		
		var $component = $(this.xml).find('content component[type="pachyderm.slider"]');
		
		var title = new WidgetTitle({xml: this.contentXML, parent: this});
		this.$(".screen-container").append(title.el);
		
		var sliders = $(this.xml).find('component[name=sliderMediaItem]');
		var $sliderButtonsContainer = $('<div class="slider-navigation-container"></div>');
		var $sliderButtonsList = $('<ul class="slider-navigation"></ul>');
		
		$(sliders).each(function(index,e) {
			var data_elements = $(e).children('data_element');
			var title, image, movie, linkType, link, caption, text;
			var $sliderBox = $('<div class="slider-element hide"></div>');
			
			$(data_elements).each(function(index) {
				var el_name = $(this).attr('name');

				switch(el_name){
					case 'title':
							title = $(this).text();
						break;
					case 'image':
							image = pachyderm.parse_asset_file_name($(this).text()); // Image or poster
						break;
					case 'movie':
							movie = pachyderm.parse_asset_file_name($(this).text()); // Video
						break;
					case 'caption':
							caption = $(this).text(); // Caption to Image / Video / Audio
						break;
					case 'text':
							text = $(this).text(); // Scrollable Text
						break;
				}
			});
			
			if(title){
				that.sliderItemsCount++;
				
				if(text){
					var scrollingTextWidget = new WidgetScrollableText({"text": text, scrollClass: 'main-text', parent: that});
					$sliderBox.append(scrollingTextWidget.el);
					that.scrollingTextWidgets[that.sliderItemsCount - 1] = scrollingTextWidget;
				}
				
				var media = new WidgetMainMedia({xmlNode: $(this), parent: that, imageNode: 'image',mediaNode: 'movie',captionNode: 'caption',linkNode: 'link'});
				$sliderBox.append(media.el);
				that.mainMediaWidgets[that.sliderItemsCount - 1] = media;
				
				that.$(".screen-container").append($sliderBox);

				var li = '<li id="li_' + that.xmlName + '_' + that.sliderItemsCount + '"><a href="#screen/' + that.xmlName + '/s' + that.sliderItemsCount + '">' + title + '</a></li>';
				$sliderButtonsList.append(li);
			}
		});

		var hScrollWidth = that.sliderItemsCount * 120;
		var hScrollerID = 'horizontal-'+ this.xmlName;
		var hScroller = pachyderm.get_horizontal_scrolling('<ul class="slider-navigation">' + $sliderButtonsList.html() + '</ul>',hScrollerID,'slider-nav-scroller');

		$sliderButtonsContainer.append(hScroller);
		
		var $sliderButtonsWrapper;
		if(this.sliderItemsCount < 8){
			$sliderButtonsWrapper = $('<div class="slider-navigation-wrapper"></div>');
		}else{
			$sliderButtonsWrapper = $('<div class="slider-navigation-wrapper"><span class="prev">Prev</span><span class="next">Next</span></div>');
		}

		$sliderButtonsWrapper.append($sliderButtonsContainer);
		this.$(".screen-container").append($sliderButtonsWrapper);
		this.$(".screen-container").append('<div class="slider-swipe-background"></div>');
		
		this.$('.hscrollable .scroll-content').width(hScrollWidth);
		this.horizontal_iScrollObj = pachyderm.set_scroll(hScrollerID,'horizontal');
		
		this.sliderElements = this.$('.slider-element');
		
		this.render();
	},
	render: function(){
		pachyderm.shell.update(this.contentXML);
		
		this.$(".preloader").fadeOut(500,function(){$(this).remove()});
		this.$(".screen-container").fadeIn(500);
		
		if(Modernizr.touch && !this.swipeSet){
			this.swipeSet = true;
			var that = this;
			this.$('.screen-container').swipe({
			     swipeLeft: function(e){ that.swipeLeft(e); },
			     swipeRight: function(e){ that.swipeRight(e); }
			})
		}
		this.setSlide(this.activeSlide);
		
		return this;
	},
	reset: function(){
		if(this.mainMediaWidgets[this.activeSlide - 1]){
			this.mainMediaWidgets[this.activeSlide - 1].reset();
		}
		
		$(this.sliderElements[this.activeSlide - 1]).stop().fadeIn().hide();
	},
	setActiveSlide: function(id){
		this.previousSlide = this.activeSlide;
		this.activeSlide = id;
	},
	setSlide: function(s){
		if(this.mainMediaWidgets[this.previousSlide - 1]){
			this.mainMediaWidgets[this.previousSlide - 1].reset();
		}
		
		var sliderNavs = this.$('.slider-navigation li a');

		if(this.sliderElements.length){			
			$(this.sliderElements).stop().fadeOut();
			$(this.sliderElements[s - 1]).stop().fadeIn();

			if(this.scrollingTextWidgets[s - 1]){
				this.scrollingTextWidgets[s - 1].refresh();
			}
			
			this.activeSlide = s;
			
			$(sliderNavs).removeClass('active');
			$(sliderNavs[s - 1]).addClass('active');
			
			if(this.mainMediaWidgets[s - 1]){
				this.mainMediaWidgets[s - 1].setupMedia();
			}
		}
	},
	startSlideHScrollLeft: function(e){
		if(!this.animationInterval){
			var that = this;
			this.animationInterval = setInterval(function(){
				var x = that.horizontal_iScrollObj.x;
				if(x < 0){
					that.horizontal_iScrollObj.scrollTo(x + 2, 0, 0);
				}
			},10);
		}
		
		e.preventDefault();
	},
	startSlideHScrollRight: function(e){
		if(!this.animationInterval){
			var that = this;
			this.animationInterval = setInterval(function(){
				var x = that.horizontal_iScrollObj.x;
				if(x > that.horizontal_iScrollObj.maxScrollX){
					that.horizontal_iScrollObj.scrollTo(x - 2, 0, 0);
				}
			},10);
		}
		
		e.preventDefault();
	},
	stopSlideHScroll: function(){
		clearInterval(this.animationInterval);
		this.animationInterval = null;
	},
	swipeLeft: function(e){
		if(e.changedTouches[0].pageY < 550){
			var nextSlide = (this.activeSlide < this.sliderItemsCount) ? parseInt(this.activeSlide) + 1 : this.sliderItemsCount;
			//console.log("Swiping Left: " + nextSlide + " | " + this.activeSlide);
			pachyderm.navigate('#screen/' + this.xmlName + '/s' + nextSlide, true);
		}
	},
	swipeRight: function(e){
		if(e.changedTouches[0].pageY < 550){
			var nextSlide = (this.activeSlide > 1) ? this.activeSlide - 1 : 1;
			//console.log("Swiping Right: " + nextSlide + " | " + this.activeSlide);
			pachyderm.navigate('#screen/' + this.xmlName + '/s' + nextSlide, true);
		}
	}
});