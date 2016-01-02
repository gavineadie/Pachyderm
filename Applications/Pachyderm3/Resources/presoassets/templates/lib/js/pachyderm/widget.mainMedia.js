var WidgetMainMedia = Backbone.View.extend({
	tagName: 'div',
	className: 'media',
	mediaContainerWidth: 400,
	mediaContainerHalfWidth: 200, // Will use this 99% of time, might as well precalculate
	minimumAudioWidth: 150,
	imageFileName: null,
	imageXML: null,
	mediaFLV: null,
	mediaFileName: null,
	mediaXML: null,
	mediaType: null,
	captionText: null,
	linkURL: null,
	linkClass: null,
	linkType: null,
	videoID: null,
	audioID: null,
	mediaPlayer: null,
	imageInfoPopup: null,
	imageInfoPopupContent: false,
	mediaInfoPopup: null,
	mediaInfoPopupContent: false,
	transcriptPopUp: null,
	mediaFile: null,
	aspectTemplate: false,
	zoomTemplate: false,
	onPlayCallback: null,
	hasBeenSetup: false,			/* TYLER */
	parent: null,
	events: {
		"click a.tombstone.image"				:"openImageTombstone",
		"click a.tombstone.video"				:"openVideoTombstone",
		"click a.transcript"						:"openTranscript",
		"click a.stop-quick-video" 			:"closeQuickVideo"
	},
	initialize: function(){

		this.mediaContainerWidth = 400;
		this.mediaContainerHalfWidth = 200;
		this.minimumAudioWidth = 150;
		this.imageFileName = null;
		this.imageXML = null;
		this.mediaFLV = null;
		this.mediaFileName = null;
		this.mediaXML = null;
		this.mediaType = null;
		this.captionText = null;
		this.linkURL = null;
		this.linkClass = null;
		this.linkType = null;
		this.videoID = null;
		this.audioID = null;
		this.mediaPlayer = null;
		this.imageInfoPopup = null;
		this.imageInfoPopupContent = false;
		this.mediaInfoPopup = null;
		this.mediaInfoPopupContent = false;
		this.transcriptPopUp = null;
		this.mediaFile = null;
		this.aspectTemplate = false;
		this.zoomTemplate = false;
		this.onPlayCallback = null;
		this.hasBeenSetup = false;		/* TYLER */
		this.parent = null;

		this.parent = this.options.parent;
		this.aspectTemplate = this.options.aspectTemplate;
		this.zoomTemplate = this.options.zoomTemplate;
		this.onPlayCallback = this.options.onPlayCallback;

		if(this.options.containerClass){
			$(this.el).addClass(this.options.containerClass);
		}

		if(this.options.mediaFile){
			this.mediaXML = this.options.mediaFile;

			if(this.mediaXML){
				this.mediaFileName = pachyderm.parse_asset_file_name(this.mediaXML);
				if(this.mediaFileName.substr(-3) == 'mp3'){
					this.mediaType = 'audio';
				}else{
					this.mediaFLV = this.mediaXML.replace('_metadata.xml', '');
					this.mediaType = 'video';
				}
			}

		}else{
			var that = this;
			var data_elements = $(this.options.xmlNode).children('data_element');

			$(data_elements).each(function(index) {
				var el_name = $(this).attr('name');
				switch(el_name){
					case that.options.imageNode:
							that.imageXML = $(this).text();
							that.imageFileName = pachyderm.parse_asset_file_name(that.imageXML);
						break;
					case that.options.mediaNode:
							that.mediaXML = $(this).text();
							if(that.mediaXML){
								that.mediaFileName = pachyderm.parse_asset_file_name(that.mediaXML);
								if(that.mediaFileName.substr(-3) == 'mp3'){
									that.mediaType = 'audio';
								}else{
									that.mediaFLV = that.mediaXML.replace('_metadata.xml', '');
									that.mediaType = 'video';
								}
							}
						break;
					case that.options.captionNode:
							that.captionText = $(this).text();
						break;
					case that.options.linkNode:
							var type = $(this).attr('type');
							var screenLinkURL = $(this).text();

							if(screenLinkURL && screenLinkURL != ''){
								that.linkURL = screenLinkURL;

								if(type == 'public.text'){
									that.linkType = 'external';
									var file = false;
									if(fileExtensions.length){
										for(var i = 0; i < fileExtensions.length; i++){
											if(that.linkURL.indexOf(fileExtensions[i]) > 1){
												file = true;
											}
										}
									}

									that.linkClass = 'external-link';

									if(file){
										that.linkClass += ' document';
									}
								}else{
									that.linkType = 'internal';
									that.linkURL = '#screen/' + that.linkURL;

									if(type == 'pachyderm.screen'){
										that.linkClass = 'internal-link';

										var template_name = 'default-template';

										if(pachyderm.presentation.get(that.linkURL)){
											template_name = pachyderm.parse_template_name(pachyderm.presentation.get(that.linkURL).get('template'));
											template_name = (template_name == 'zoom') ? 'zoom' : 'default-template';
										}
										that.linkClass += ' ' + template_name + '-template';
									}
								}
							}
						break;
					}
			});

			if(this.aspectTemplate){
				var aspects_image_component = this.options.xmlNode.find('component[type=pachyderm.aspects.image_item][name=imageItem] data_element[name=image]');
				if(aspects_image_component.text()){
					this.imageXML = aspects_image_component.text();
					this.imageFileName = pachyderm.parse_asset_file_name(this.imageXML);
				}
			}
		}

		this.render();
	},
	render: function(){
		var markup = '';
		var that = this;

		if(this.mediaType == 'audio'){
			$(this.el).addClass('has-audio');
			markup = '<div class="media-asset-container has-audio">';
				markup += '<div class="audio-poster">';
					if(this.imageFileName){
						if(this.linkURL){
							markup += '<a href="' + this.linkURL + '"><img src="' + this.imageFileName + '" alt="" /></a>';
						}else{
							markup += '<img src="' + this.imageFileName + '" alt="" />';
						}
					}

				markup += '</div>';

				this.audioID = 'video-' + pachyderm.get_nonce();
				markup += '<audio controls id="' + this.audioID + '">';
					markup += '<source src="' + this.mediaFileName + '" />';
					//markup += '<source src="' + this.mediaFileName.replace('.mp3', '.ogg') + '" />';
				markup += '</audio>';
			markup += '</div>';
		}else if(this.mediaType == 'video'){
			$(this.el).addClass('has-video');
			this.videoID = 'video-' + pachyderm.get_nonce();
			markup = '<div class="media-asset-container has-video">';
				markup += '<div class="video-js-box">';
					if(this.imageFileName){
						markup += '<video id="' + this.videoID + '" width="400" height="300" poster="' + this.imageFileName + '">';
					}else{
						markup += '<video id="' + this.videoID + '" width="400" height="300">';
					}
						markup += '<source type="video/mp4" src="' + this.mediaFileName + '?' + new Date().getTime() + '" />';
					markup += '</video>';
				markup += '</div>';
			markup += '</div>';
		}else if(this.imageFileName){
			this.mediaType == 'image';

			if(this.linkType == 'internal'){
				markup = '<div class="media-asset-container image-only"><a href="'+this.linkURL+'"><img src="' + this.imageFileName + '" alt=""/></a></div>';
			}else if(this.linkType == 'external'){
				markup = '<div class="media-asset-container image-only"><a href="'+this.linkURL+'" target=_blank><img src="' + this.imageFileName + '" alt=""/></a></div>';
			}else{
				markup = '<div class="media-asset-container image-only"><img src="' + this.imageFileName + '" alt=""/></div>';
			}
		}else{
			markup = '<div class="media-asset-container"></div>';
		}

		var caption = '';
		if(this.captionText){
			caption += '<p>' + this.captionText + '</p>';
		}else{
			caption += '<p> </p>';
		}

		if(!this.zoomTemplate){
			markup += caption;
		}

		markup += '<div class="media-icon-container">';
			if(this.linkType == 'internal'){
				markup += '<a href="' + this.linkURL + '" class="media-link ' + this.linkClass + '">Related Screen Link</a>';
			}else if(this.linkType == 'external'){
				var linkTitle = (this.linkClass.indexOf('document') > -1) ? 'Related Document' : 'Related Website';
				markup += '<a href="' + this.linkURL + '" class="media-link ' + this.linkClass + '" target=_blank>' + linkTitle + '</a>';
			}

			if(this.options.mediaFile){
				markup += '<a href="javascript:;" class="stop-quick-video">Stop</a>';
			}

			markup += '<a href="javascript:;" class="tombstone image">Media Label Show / Hide</a>';
			markup += '<a href="javascript:;" class="tombstone video">Media Label Show / Hide</a>';
			markup += '<a href="javascript:;" class="transcript">Media Transcript Show / Hide</a>&nbsp;';
		markup += '</div>';

		if(this.zoomTemplate){
			markup += caption;
		}

		$(this.el).html(markup);

		var that = this;

		if(this.mediaType == 'audio' && this.imageFileName){
			this.$('.media-asset-container .audio-poster, .media-asset-container audio').fadeOut(0);

			this.$("img").load(function(){
				that.positionIcons(this);
			});
		}else if(this.mediaType == 'image'){
			this.$("img").load(function(){
				that.positionIcons(this);
			});
		}

		if(this.mediaType == 'audio'){
			this.$('audio').bind('play',function(){
				that.onMediaPlay();
			});
		}

		if(this.imageXML){
			this.imageInfoPopup = new WidgetPopup({xmlName: this.imageXML, parent: this, type: 'info',image: true});
		}

		if(this.mediaXML){
			this.mediaInfoPopup = new WidgetPopup({xmlName: this.mediaXML, parent: this, type: 'info',image: false});
		}

		return this;
	},
	setupMedia: function(){
		if (!this.hasBeenSetup) {
			this.hasBeenSetup = true;
			if(!this.mediaPlayer && this.mediaType == 'video'){
				var that = this;
				setTimeout(function() {
					that.mediaPlayer = new MediaElementPlayer('#' + that.videoID,{
						pluginPath: 'lib/mediaelement/',
						flashName: 'flashmediaelement.swf'
					});
				},200);
			}else if(!this.mediaPlayer && this.mediaType == 'audio'){
				this.mediaPlayer = new MediaElementPlayer('#' + this.audioID,{
					pluginPath: 'lib/mediaelement/',
					audioWidth: 260,
					flashName: 'flashmediaelement.swf'
				});
			}
		}
	},
	reset: function(){
		if(this.mediaType == 'audio'){
			this.$('audio').get(0).pause();
			if(this.$('audio').get(0).currentTime){
				this.$('audio').get(0).currentTime = 0;
			}
		}else if(this.mediaType == 'video'){
			if(this.mediaPlayer){
				this.mediaPlayer.pause();
				if(this.mediaPlayer.getCurrentTime() > 0){
					this.mediaPlayer.setCurrentTime(0.5);
				}
			}
		}

		if(this.imageInfoPopup){
			this.imageInfoPopup.reset();
		}

		if(this.mediaInfoPopup){
			this.mediaInfoPopup.reset();
		}

		if(this.transcriptPopUp){
			this.transcriptPopUp.reset();
		}

		return this;
	},
	positionIcons: function(image){
		var w = image.width;
		this.$('.media-icon-container').width(w);
	},
	addInfoPopup: function(popup){
		if(popup.type == 'info'){
			if(popup.image){
				this.imageInfoPopupContent = true;

				this.$('.tombstone.video').hide();
				this.$('.tombstone.image').show();

				if(this.mediaPlayer){
					var that = this;
				}
			}else{
				this.mediaInfoPopupContent = true;

				if(this.$('.tombstone.image').css('display') != 'block'){
					this.$('.tombstone.video').show();
				}
			}
		}else{
			this.$('.transcript').show();
		}

		$(this.el).append(popup.el);
	},
	openImageTombstone: function(e){
		this.imageInfoPopup.open();
	},
	openVideoTombstone: function(e){
		this.mediaInfoPopup.open();
	},
	openTranscript: function(e){
		this.transcriptPopUp.open();
	},
	onMediaPlay: function(e){
		if(this.mediaInfoPopupContent){
			this.$('.tombstone.image').hide();
			this.$('.tombstone.video').show();
		}

		if(this.onPlayCallback){
			this.options.parent[this.onPlayCallback](this);
		}
	},
	setTranscript: function(transcriptXML){
		this.transcriptPopUp = new WidgetPopup({xmlName: transcriptXML, parent: this, type: 'transcript',image: false});
	},
	closeQuickVideo: function(e){
		this.options.parent.closeQuickVideo(this);
	}
});