var Series = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'series screen',
	xmlName: null,
	xml: null,
	t: null,
	scrollingTextWidget: null,
	mainMediaWidget: null,
	horizontal_iScrollObj: null,
	seriesItemsCount: 0,
	activeSeries: null,
	seriesElements: [],
	currentSeriesElement: null,
	animationInterval: null,
	zoomPopUp: false,
	events: {
		"mousedown a.prev"			:"startSlideHScrollLeft",
		"mouseup a.prev"				:"stopSlideHScroll",
		"touchstart a.prev"			:"startSlideHScrollLeft",
		"touchend a.prev"				:"stopSlideHScroll",
		"mousedown a.next"			:"startSlideHScrollRight",
		"mouseup a.next"				:"stopSlideHScroll",
		"touchstart a.next"			:"startSlideHScrollRight",
		"touchend a.next"				:"stopSlideHScroll"
	},
	initialize: function(options) {
		
		this.scrollingTextWidget = null;
		this.mainMediaWidget = null;
		this.horizontal_iScrollObj = null;
		this.seriesItemsCount = 0;
		this.activeSeries = null;
		this.seriesElements = [];
		this.currentSeriesElement = null;
		this.animationInterval = null;
		this.zoomPopUp = false;
		
		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;
		this.activeSeries = options.activeSeries;
		
		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container series"></div>');
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
		
		var $component = $(this.xml).find('content component[type="pachyderm.series"]');
		
		var title = new WidgetTitle({xml: this.contentXML, parent: this});
		this.$(".screen-container").append(title.el);
		
		var series = $(this.xml).find('component[name=page]');
		var $seriesButtonsContainer = $('<div class="slider-navigation-container"></div>');
		var $seriesButtonsList = $('<ul class="slider-navigation"></ul>');
		
		$(series).each(function(index,e) {
			var data_elements = $(e).children('data_element, component');
			var title, image;
			
			$(data_elements).each(function(index) {
				var el_name = $(this).attr('name');

				switch(el_name){
					case 'title':
							title = $(this).text();
						break;
					case 'imageItem':
							var image_node = $(this).children('data_element[name=thumbnail]');
							if(image_node.length){
								image = pachyderm.parse_asset_file_name($(image_node).text()); // Image or poster
							}
						break;
				}
			});
			
			if(title){
				that.seriesItemsCount++;
				title = '<span>' + title + '</span>';
				var image_tag = (!image) ? '' : '<img src="' + image + '" alt=""/>';
				var li = '<li id="li_' + that.xmlName + '_' + that.seriesItemsCount + '"><a href="#screen/' + that.xmlName + '/s' + that.seriesItemsCount + '">' + image_tag + title + '</a></li>';
				$seriesButtonsList.append(li);
			}
		});

		var hScrollWidth = that.seriesItemsCount * 154;
		var hScrollerID = 'horizontal-'+ this.xmlName;
		var hScroller = pachyderm.get_horizontal_scrolling('<ul class="slider-navigation" style="width: ' + hScrollWidth + 'px;">' + $seriesButtonsList.html() + '</ul>',hScrollerID,'slider-nav-scroller');

		$seriesButtonsContainer.append(hScroller);
		
		var $seriesButtonsWrapper;
		
		if(that.seriesItemsCount <= 6){
			$seriesButtonsWrapper = $('<div class="slider-navigation-wrapper"></div>');
		}else{
			$seriesButtonsWrapper = $('<div class="slider-navigation-wrapper hasScroll"><a href="javascript:;" class="prev">Prev</a><a href="javascript:;" class="next">Next</a></div>');
		}

		$seriesButtonsWrapper.append($seriesButtonsContainer);
		this.$(".screen-container").append($seriesButtonsWrapper);
		
		this.$('.hscrollable .scroll-content').width(hScrollWidth);
		this.horizontal_iScrollObj = pachyderm.set_scroll(hScrollerID,'horizontal');
		
		var textArea = $(this.xml).find('data_element[name=text]');
		if(textArea.length){
			this.scrollingTextWidget = new WidgetScrollableText({text: $(textArea[0]).text(), scrollClass: 'main-text', parent: this});
			this.$(".screen-container").append(this.scrollingTextWidget.el);
		}
		
		this.mainMediaWidget = new WidgetMainMedia({xmlNode: $component, parent: this, imageNode: 'image',mediaNode: 'media',captionNode: 'caption',linkNode: 'screenLink'});
		this.$(".screen-container").append(this.mainMediaWidget.el);
		
		this.render();
	},
	render: function(){	
		pachyderm.shell.update(this.contentXML);
			
		this.$(".preloader").fadeOut(500,function(){$(this).remove()});
		this.$(".screen-container").fadeIn(500);
		
		if(this.activeSeries){
			this.setSeries(this.activeSeries);
		}else{
			this.$(".screen-container").css('display','block');
			if(this.currentSeriesElement){
				this.turnOffZoom(this.seriesElements[this.currentSeriesElement]);
				this.currentSeriesElement = null;
			}
		}
		
		this.horizontal_iScrollObj.refresh();
		
		if(this.scrollingTextWidget){
			this.scrollingTextWidget.refresh();
		}
		
		if(this.mainMediaWidget){
			this.mainMediaWidget.setupMedia();
		}
		
		return this;
	},
	reset: function(){
		if(this.mainMediaWidget){
			this.mainMediaWidget.reset();
		}
		
		if(this.currentSeriesElement){
			this.turnOffZoom(this.seriesElements[this.currentSeriesElement]);
			this.currentSeriesElement = null;
		}
	},
	setActiveSeries: function(id){
		this.activeSeries = id;
	},
	setSeries: function(s){
		this.reset();
		if(this.seriesElements[s]){
			this.seriesElements[s].render();
		}else{
			var x = $(this.xml).find('component[name=page][index=' + s + ']');
			if(x){
				var z = new Zoom({seriesItemNumber: s, seriesItemsTotal: this.seriesItemsCount, seriesXML: x, seriesPath: '#screen/' + this.xmlName + '/s', parentSeries: this, autoPopUpText: this.zoomPopUp});
				this.seriesElements[s] = z;
			}
		}
		
		if(this.seriesElements[s]){
			this.currentSeriesElement = s;
			$(this.seriesElements[s].el).fadeIn(0);
		}
		
		this.$(".screen-container").css('display','none');
	},
	turnOffZoom: function(z){
		var oldCurrentScreen = z;
		$(z.el).fadeOut(0,function(){oldCurrentScreen.reset();});
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
	setPopup: function(e){
		this.zoomPopUp = e;
	}
});