var Aspects = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'aspects screen',
	xmlName: null,
	activeAspect: 1,
	xml: null,
	t: null,
	thumbnailWidgets: [],
	thumbnailsLoaded: 0,
	thumbnailsToLoad: null,
	scrollingTextWidgets: [],
	mainMediaWidgets: [],
	aspectContainers: [],
	preloader: null,
	initialize: function(options) {
		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;
		this.activeAspect = options.activeAspect;
		
		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container aspect"></div>');
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
		
		var aspects = $(this.xml).find('component[type="pachyderm.aspects.media_item"]');
		
		$(aspects).each(function(index,e) {
			var data_elements = $(e).children('data_element');
			var $l = $('<div class="aspect-element hide"></div>');
			
			var thumbnail = new WidgetThumbnail({xmlNode: e, parent: that, onImageLoad: true, aspectTemplate: true});
			that.$(".screen-container").append(thumbnail.el);
			that.thumbnailWidgets.push(thumbnail);
			
			var media = new WidgetMainMedia({xmlNode: $(e), parent: that, imageNode: 'image',mediaNode: 'movie',captionNode: 'imageCaption',linkNode: 'screenLink', aspectTemplate: true});
			that.mainMediaWidgets.push(media);
			$l.append(media.el);
			
			var aspectText = $(e).children('data_element[name=text]').text();
			var scrollingTextWidget = new WidgetScrollableText({text: aspectText, scrollClass: 'aspect-side-text', parent: that});
			that.scrollingTextWidgets.push(scrollingTextWidget);
			$l.append(scrollingTextWidget.el);
			
			that.$(".screen-container").append($l);
		});
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

		this.setAspect(this.activeAspect);
		
		return this;
	},
	reset: function(){
		this.resetAspect(this.activeAspect);
	},
	setAspect: function(s){
		this.resetAspect(this.activeAspect);
		
		this.activeAspect = parseInt(s);
		if(this.thumbnailWidgets[this.activeAspect - 1]){
			this.thumbnailWidgets[this.activeAspect - 1].setActive();
		}
		
		this.$('.aspect-element').eq(this.activeAspect - 1).stop(true,true).fadeIn(500);
		
		if(this.scrollingTextWidgets[this.activeAspect - 1]){
			this.scrollingTextWidgets[this.activeAspect - 1].refresh();
		}
		
		if(this.mainMediaWidgets[this.activeAspect - 1]){
			this.mainMediaWidgets[this.activeAspect - 1].setupMedia();
		}
	},
	resetAspect: function(s){
		if(this.thumbnailWidgets[s - 1]){
			this.thumbnailWidgets[s - 1].reset();
			this.mainMediaWidgets[s - 1].reset();
		}
		this.$('.aspect-element').eq(s - 1).stop(true,true).fadeOut(500).hide();
	},
	swapMainMedia: function(widget){
		pachyderm.navigate('screen/' + this.xmlName + '/s' + widget.index, true);
	},
});