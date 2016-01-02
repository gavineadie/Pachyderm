var Layers = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'layers screen',
	xmlName: null,
	activeLayer: 1,
	xml: null,
	t: null,
	thumbnailWidgets: [],
	thumbnailsLoaded: 0,
	thumbnailsToLoad: null,
	scrollingTextWidgets: [],
	mainMediaWidgets: [],
	subMediaWidgets: [],
	aspectContainers: [],
	staticScrollingTextWidget: null,
	activeThumbnailWidget: null,
	currentMainMedia: null,
	preloader: null,
	initialize: function(options) {
		
		this.thumbnailWidgets = [];
		this.thumbnailsLoaded = 0;
		this.thumbnailsToLoad = null;
		this.scrollingTextWidgets = [];
		this.mainMediaWidgets = [];
		this.subMediaWidgets = [];
		this.aspectContainers = [];
		this.staticScrollingTextWidget = null;
		this.activeThumbnailWidget = null;
		this.currentMainMedia = null;
		this.preloader = null;
		
		// cache for later
		this.t = $(this.el);
		this.xmlName = options.xmlName;
		this.activeLayer = options.activeLayer;
		
		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container layer"></div>');
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
		
		var title = new WidgetTitle({xml: this.contentXML, parent: this});
		this.$(".screen-container").append(title.el);
		
		var scrollText = $(this.xml).find('data_element[name="text"]:not(component[type="pachyderm.layers.layer"] data_element[name="text"])').text();
		this.staticScrollingTextWidget = new WidgetScrollableText({text: scrollText, scrollClass: 'main-text', parent: this});
		this.$(".screen-container").append(this.staticScrollingTextWidget.el);
		
		var layers = $(this.xml).find('component[type="pachyderm.layers.layer"]');
		var $layersList = $('<ul class="layers-navigation"></ul>');
		
		$(layers).each(function(index,e) {
			var data_elements = $(e).children('data_element');
			var layerCount = parseInt(index) + 1;
			var $l = $('<div class="layer-element hide"></div>');
			
			var title = $(e).children('data_element[name=layerTitle]').text();
			var text = $(e).children('data_element[name=text]').text();
			
			if(title){
				var li = '<li id="li_' + that.xmlName + '_' + layerCount + '"><a href="#screen/' + that.xmlName + '/s' + layerCount + '">' + title + '</a></li>';
				$layersList.append(li);
				var $m = $(e).find('component[name=mainMediaItem]');
				var media = new WidgetMainMedia({xmlNode: $m, parent: that, imageNode: 'image',mediaNode: 'movie',captionNode: 'caption',linkNode: 'link'});
				that.mainMediaWidgets.push(media);
				$l.append(media.el);
				
				var thumbnails = $(e).children('component[type="pachyderm.layers.ancillary_media_item"]');
				$(thumbnails).each(function(tindex,t) {
					var thumbnailWidget = new WidgetThumbnail({xmlNode: t, parent: that, onImageLoad: true});
					that.thumbnailWidgets.push(thumbnailWidget);
					$l.append(thumbnailWidget.el);
				});
				
				var scrollingTextWidget = new WidgetScrollableText({text: text, scrollClass: 'layer-side-text', parent: this});
				that.scrollingTextWidgets.push(scrollingTextWidget);
				$l.append(scrollingTextWidget.el);

				that.$(".screen-container").append($l);
			}
		});
		
		this.$(".screen-container").append($layersList);
		
		this.render();
	},
	imageLoaded: function(){
		this.thumbnailsLoaded++;

		if(this.thumbnailsLoaded >= this.thumbnailsToLoad){
			this.render();
		}
	},
	render: function(){
		pachyderm.shell.update(this.contentXML);
			
		this.$(".preloader").fadeOut(500,function(){$(this).remove()});
		this.$(".screen-container").fadeIn(500);
		
		this.staticScrollingTextWidget.refresh();
		this.setLayer(this.activeLayer);
		
		return this;
	},
	reset: function(){
		this.resetLayer(this.activeLayer);
	},
	resetLayer: function(s){
		this.closeQuickVideo();
		if(this.mainMediaWidgets[s - 1]){
			this.mainMediaWidgets[s - 1].reset();
		}
		this.$('.layer-element').eq(s - 1).stop(true,true).fadeOut(500).hide();
	},
	setLayer: function(s){
		this.resetLayer(this.activeLayer);
		
		this.$('.layers-navigation li').removeClass('active');
		this.$('.layers-navigation li').eq(s - 1).addClass('active');
		
		this.activeLayer = parseInt(s);
		
		this.$('.layer-element').eq(this.activeLayer - 1).stop(true,true).fadeIn(500);
		if(this.scrollingTextWidgets[this.activeLayer - 1]){
			this.scrollingTextWidgets[this.activeLayer - 1].refresh();
		}
		
		if(this.mainMediaWidgets[this.activeLayer - 1]){
			this.mainMediaWidgets[this.activeLayer - 1].setupMedia();
		}
	},
	swapMainMedia: function(widget){
		this.closeQuickVideo();
		
		this.resetActiveThumbnail();
		this.activeThumbnailWidget = widget;
		
		this.mainMediaWidgets[this.activeLayer - 1].reset();
		$(this.mainMediaWidgets[this.activeLayer - 1].el).stop(true,true).fadeOut();
		
		this.currentMainMedia = widget.index;

		if(!this.subMediaWidgets[this.activeLayer - 1]){
			this.subMediaWidgets[this.activeLayer - 1] = [];
		}
		
		if(!this.subMediaWidgets[this.activeLayer - 1][this.currentMainMedia]){
			var media = new WidgetMainMedia({parent: this, mediaFile: widget.movieXML});
			this.subMediaWidgets[this.activeLayer - 1][this.currentMainMedia] = media;
			this.$('.layer-element').eq(this.activeLayer - 1).append(media.el);
			media.setupMedia();
		}
		
		$(this.subMediaWidgets[this.activeLayer - 1][this.currentMainMedia].el).stop(true,true).fadeIn();
	},
	closeQuickVideo: function(){
		if(this.subMediaWidgets[this.activeLayer - 1] && this.subMediaWidgets[this.activeLayer - 1][this.currentMainMedia]){
			this.subMediaWidgets[this.activeLayer - 1][this.currentMainMedia].reset();
			$(this.subMediaWidgets[this.activeLayer - 1][this.currentMainMedia].el).stop(true,true).fadeOut();
			
			this.currentMainMedia = 0;
			
			$(this.mainMediaWidgets[this.activeLayer - 1].el).stop(true,true).fadeIn();
			
			this.resetActiveThumbnail();
		}
	},
	resetActiveThumbnail: function(){
		if(this.activeThumbnailWidget){
			this.activeThumbnailWidget.reset();
			this.activeThumbnailWidget = null;
		}
	}
});