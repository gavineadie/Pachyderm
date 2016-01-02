var Exploration_Entry = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'exploration_entry screen',
	xmlName: null,
	xml: null,
	contentXML: null,
	preloader: null,
	thumbnailWidgets: [],
	thumbnailsLoaded: 0,
	thumbnailsToLoad: null,
	mainMediaWidgets: [],
	currentMainMedia: 0,
	t: null,
	initialize: function(options) {
		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;

		this.thumbnailWidgets = [];
		this.thumbnailsLoaded = 0;
		this.thumbnailsToLoad = null;
		this.mainMediaWidgets = [];
		this.currentMainMedia = 0;
		
		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container exploration"></div>');
		pachyderm.screenContainer.append(this.el);
		
		this.screenContainer = this.$(".screen-container");
		this.screenContainer.hide();
		
		var that = this;
		this.model.fetch({'error': function(m,r){that.onXML(r.responseText);}});
  },
	onXML: function(x){
		this.xml = x;
		this.contentXML = $(this.xml).find('content');
		var that = this;

		var title = new WidgetTitle({xml: this.contentXML, parent: this});
		this.$(".screen-container").append(title.el);
		
		var largeMedia = $(this.xml).find('component[name=imageItem]');
		if(largeMedia.length){
			var media = new WidgetMainMedia({xmlNode: largeMedia, parent: this, imageNode: 'image',mediaNode: 'movie',captionNode: 'text',linkNode: 'screenLink'});
			this.$(".screen-container").append(media.el);
			this.mainMediaWidgets.push(media);
		}
		
		var thumbnails = $(this.xml).find('component[name=linkItem]');
		this.thumbnailsToLoad = thumbnails.length;
		$(thumbnails).each(function(i,e) {
			var thumbnail = new WidgetThumbnail({xmlNode: e, parent: that, onImageLoad: true});
			that.$(".screen-container").append(thumbnail.el);
			that.thumbnailWidgets.push(thumbnail);
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
		
		if(this.mainMediaWidgets.length){
			this.mainMediaWidgets[0].setupMedia();
			$(this.mainMediaWidgets[0].el).css('display','block');
		}
				
		return this;
	},
	reset: function(){
		if(this.mainMediaWidgets.length){
			for(var i = 0; i < this.mainMediaWidgets.length; i++){
				if(this.mainMediaWidgets[i]){
					this.mainMediaWidgets[i].reset();
				
					if(i == 0){
						$(this.mainMediaWidgets[i].el).fadeIn();
					}else{
						$(this.mainMediaWidgets[i].el).css('display','none');
					}
				}
			}
		}
		
		this.currentMainMedia = 0;
	},
	swapMainMedia: function(widget){
		this.mainMediaWidgets[this.currentMainMedia].reset();
		$(this.mainMediaWidgets[this.currentMainMedia].el).fadeOut();
		
		this.currentMainMedia = widget.index;
		
		if(!this.mainMediaWidgets[this.currentMainMedia]){
			var media = new WidgetMainMedia({parent: this, mediaFile: widget.movieXML});
			this.mainMediaWidgets[this.currentMainMedia] = media;
			this.$(".screen-container").append(this.mainMediaWidgets[this.currentMainMedia].el);
			this.mainMediaWidgets[this.currentMainMedia].setupMedia();
		}
		
		$(this.mainMediaWidgets[this.currentMainMedia].el).fadeIn();
	},
	closeQuickVideo: function(){
		this.mainMediaWidgets[this.currentMainMedia].reset();
		$(this.mainMediaWidgets[this.currentMainMedia].el).fadeOut();
		
		this.currentMainMedia = 0;
		
		$(this.mainMediaWidgets[this.currentMainMedia].el).fadeIn();
	}
});