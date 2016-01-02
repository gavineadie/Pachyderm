var Zoom = Backbone.View.extend({
	screenContainer: null,
	iconContainer: null,
	tagName: 'section',
	className: 'zoom screen',
	xmlName: null,
	xml: null,
	t: null,
	seriesItemNumber: null,
	seriesItemsTotal: null,
	seriesXML: null,
	seriesPath: null,
	audio1MediaWidget: null,
	audio2MediaWidget: null,
	captionPopUp: null,
	tombstonePopUp: null,
	parentSeries: null,
	autoPopUpText: false,
	events: {
		"click a.toggle-caption"		:"toggleText",
		"click a.info-link"					:"toggleTombstone",
		"click a.info-close"				:"resetAllPopUps"
	},
	initialize: function(options) {
		this.seriesItemNumber = null;
		this.seriesItemsTotal = null;
		this.seriesXML = null;
		this.seriesPath = null;
		this.audio1MediaWidget = null;
		this.audio2MediaWidget = null;
		this.captionPopUp = null;
		this.tombstonePopUp = null;	
		
		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;
		this.parentSeries = options.parentSeries;
		this.autoPopUpText = options.autoPopUpText;
		
		this.t.append('<div class="screen-container zoom"></div>');
		pachyderm.wrapper.append(this.el);
		
		this.screenContainer = this.$(".screen-container");
		this.screenContainer.hide();
		
		this.seriesItemNumber = parseInt(options.seriesItemNumber);
		this.seriesItemsTotal = parseInt(options.seriesItemsTotal);
		this.seriesXML = options.seriesXML;
		this.seriesPath = options.seriesPath;
		
		if(this.seriesXML){
			this.onXML(this.seriesXML);
		}else{
			var that = this;
			this.model.fetch({'error': function(m,r){that.onXML(r.responseText);}});
		}
  },
	onXML: function(x){
		this.xml = x;
		var $xml = $(this.xml);
		
		var seriesTitle = '';
		var imageXML = '';
		var link = '';
		var audio_and_meta = $('<div class="audio-and-meta"></div>');
		this.iconContainer = $('<div class="icon-container"></div>');
		
		if(this.seriesXML){
			var $x = $(this.seriesXML);
			seriesTitle = $x.children('data_element[name=title]').text();
			captionText = $x.children('data_element[name=text]').text();
			imageXML = $x.children('component[name=imageItem]').children('data_element[name=image]').text();			

			audio1Caption = $x.children('data_element[name=audioCaption]').text();
			link = $x.children('data_element[name=screenLink]').text();
			
			var prev = (this.seriesItemNumber == 1) ? this.seriesItemsTotal : this.seriesItemNumber - 1;
			var next = (this.seriesItemNumber == this.seriesItemsTotal) ? 1 : this.seriesItemNumber + 1;
			var $seriesNav = $('<div class="zoom-series-navigation"><a class="prev" href="' + this.seriesPath + prev + '">Prev</a> Page ' + this.seriesItemNumber + ' of ' + this.seriesItemsTotal + '<a href="' + this.seriesPath + next + '" class="next">Next</a> ' + seriesTitle + '</div>');
			$(audio_and_meta).append($seriesNav);
			
			this.audio1MediaWidget = new WidgetMainMedia({xmlNode: $x, parent: this, imageNode: 'image',mediaNode: 'audio',captionNode: 'audioCaption',linkNode: 'null', zoomTemplate: true});
			$(audio_and_meta).append(this.audio1MediaWidget.el);
		}else{
			var audio1Node = $xml.find('component[index=1]');
			if(audio1Node.length){
				this.audio1MediaWidget = new WidgetMainMedia({xmlNode: audio1Node, parent: this, imageNode: 'image',mediaNode: 'audio',captionNode: 'caption',linkNode: 'screenLink', zoomTemplate: true});
				$(audio_and_meta).append(this.audio1MediaWidget.el);
			}
			
			var audio2Node = $xml.find('component[index=2]');
			if(audio2Node.length){
				this.audio2MediaWidget = new WidgetMainMedia({xmlNode: audio2Node, parent: this, imageNode: 'image',mediaNode: 'audio',captionNode: 'caption',linkNode: 'screenLink', zoomTemplate: true});
				$(audio_and_meta).append(this.audio2MediaWidget.el);
			}
			
			captionText = $xml.find('component[type="pachyderm.zoom"]').children('data_element[name=caption]').text();
			imageXML = $xml.find('data_element[name=image]').text();
			
			link = $xml.find('data_element[name=screenLink]');
		}
		
		this.screenContainer.append(audio_and_meta);
		
		if(captionText){
			this.iconContainer.append('<a href="javascript:;" class="toggle-caption">Caption Show / Hide</a>');
			this.captionPopUp = new WidgetPopup({textOverride: captionText, parent: this, type: 'info',image: true, draggable: true});
		}

		this.iconContainer.append('<a href="javascript:;" class="toggle-tombstone info-link">Media Label Show / Hide</a>');
		this.tombstonePopUp = new WidgetPopup({xmlName: imageXML, parent: this, type: 'info',image: true});

		if(link){
			var screenLinkURL = $(link).text();
			var type = $(link).attr('type');
			var linkClass, linkMarkup;
			
			if(screenLinkURL && screenLinkURL != ''){
				if(type == 'pachyderm.screen'){
					linkClass = 'internal-link';
					linkMarkup = '<a class="zoom-screen-link internal-link" href="#screen/' + screenLinkURL + '">';
				}else{
					linkClass = 'external-link';
					linkMarkup = '<a class="zoom-screen-link external-link" href="' + screenLinkURL + '" target=_blank>';
				}
				
				linkMarkup += '<span class="icon ' + linkClass + '"></span></a>';
				
				this.iconContainer.append(linkMarkup);
			}
		}
		
		this.screenContainer.append(this.iconContainer);
		this.screenContainer.append('<span class="magnifying-text">Use magnifiers to Zoom</span><span class="pinch-text">Pinch to zoom in/out</span>');
		
		imageName = pachyderm.parse_asset_file_name(imageXML);
		var $imageContainer = $('<div class="zoom-image-container"><img style="visibility: hidden" src="' + imageName + '" alt=""></div>');
		
		this.screenContainer.append($imageContainer);
		var images_to_load = this.$(".zoom-image-container img").length;
		if(images_to_load > 0){
			this.screenContainer.append('<span class="loading-text">Loading Image...</span>');
			
			var that = this;
			this.$(".zoom-image-container img").load(function(){
					
					that.$('.zoom-image-container img').axzoomer({
					    'maxZoom':4,
					    'zoomIn':'lib/img/zoom-in.png',
					    'zoomOut':'lib/img/zoom-out.png',
					    'opacity':0.8,
					    'sensivity':10,
							'zoomInTabIndex': 5,
							'zoomOutTabIndex': 6
					});

					that.$('.loading-text').hide();
					that.$(".zoom-image-container img.ax-zoom").delay(250).queue(function() {
						$(this).css('visibility','visible');
					});
			});
		}
		
		this.render();
	},
	render: function(){	
		var border_color = pachyderm.shell.get_background_color();
		
		if(border_color){
			this.$('.zoom-image-container').css('border-color',border_color);
		}
			
		if(this.autoPopUpText){
			this.toggleText();
		}
		
		if(this.audio1MediaWidget){
			this.audio1MediaWidget.setupMedia();
		}
		
		if(this.audio2MediaWidget){
			this.audio2MediaWidget.setupMedia();
		}
		
		this.screenContainer.show();
		return this;
	},
	reset: function(){
		this.resetAllPopUps();
		
		if(this.audio1MediaWidget){
			this.audio1MediaWidget.reset();
		}
		
		if(this.audio2MediaWidget){
			this.audio2MediaWidget.reset();
		}
	},
	addInfoPopup: function(popup){
		if(popup == this.tombstonePopUp){
			this.$('.toggle-tombstone').show();
		}else{
			this.$('.toggle-caption').show();
		}
		
		this.screenContainer.append(popup.el);
	},
	toggleTombstone: function(){
		if(this.tombstonePopUp){
			if(this.captionPopUp){
				this.$('.toggle-caption').removeClass('active');
				this.captionPopUp.reset();
			}
			
			if(this.$('.toggle-tombstone').hasClass('active')){
				this.$('.toggle-tombstone').removeClass('active');
				this.tombstonePopUp.reset();
			}else{
				this.$('.toggle-tombstone').addClass('active');
				this.tombstonePopUp.open();
			}
		}
	},
	toggleText: function(){
		if(this.captionPopUp){
			if(this.tombstonePopUp){
				this.tombstonePopUp.reset();
				this.$('.toggle-tombstone').removeClass('active');
			}

			if(this.$('.toggle-caption').hasClass('active')){
				this.$('.toggle-caption').removeClass('active');
				this.captionPopUp.reset();
				
				if(this.parentSeries){
					this.parentSeries.setPopup(false);
				}
			}else{
				this.$('.toggle-caption').addClass('active');
				this.captionPopUp.open();
				
				if(this.parentSeries){
					this.parentSeries.setPopup(true);
				}
			}
		}
	},
	resetAllPopUps: function(){
		if(this.tombstonePopUp){
			this.tombstonePopUp.reset();
			this.$('.toggle-tombstone').removeClass('active');
		}
		
		if(this.captionPopUp){
			this.$('.toggle-caption').removeClass('active');
			this.captionPopUp.reset();
		}
	}
});