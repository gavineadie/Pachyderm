var WidgetThumbnail = Backbone.View.extend({
	tagName: 'div',
	className: 'thumbnail',
	xmlObj: null,
	index: null,
	imageMetaXML: null,
	image: null,
	linkText: null,
	linkURL: null,
	linkMarkup: null,
	mediaLink: null,
	movieXML: null,
	parent: null,
	aspect: false,
	active: false,
	events: {
		"click a.video-swap": 					"on_video_swap",
		"mouseover .thumbnail a": 			"on_thumbnail_over",
		"mouseout .thumbnail a": 				"on_thumbnail_out"
	},
	initialize: function(){
		var that = this;
		
		this.index = null;
		this.imageMetaXML = null;
		this.image = null;
		this.linkText = null;
		this.linkURL = null;
		this.linkMarkup = null;
		this.mediaLink = null;
		this.movieXML = null;
		this.parent = null;
		this.aspect = null;
		this.active = false;
		
		this.aspect = this.options.aspectTemplate;
		this.xmlObj = $(this.options.xmlNode);
		this.index = this.xmlObj.attr('index');
		$(this.el).addClass('thumb_' + this.index);
		
		var data_elements = this.xmlObj.children('data_element');
		$(data_elements).each(function(index) {
			var el_name = $(this).attr('name');
			switch(el_name){
				case 'image':
				case 'thumbnail':
						that.imageMetaXML = $(this).text();
						that.image = pachyderm.parse_asset_file_name(that.imageMetaXML);
					break;
				case 'thumbCaption':
				case 'linkText':
				case 'caption':
				case 'text':
						if(!that.aspect || (that.aspect && el_name == 'thumbCaption')){
							that.linkText = $(this).text();
						}
					break;
				case 'movie':
						that.movieXML = $(this).text();
						movieURL = pachyderm.parse_asset_file_name(that.movieXML);

						if(movieURL && movieURL != ''){
							that.linkURL = movieURL;
							that.linkClass = 'movie-link';
							that.linkMarkup = '<a href="javascript:;" class="video-swap" meta_xml="' + that.movieXML + '">';
							$(that.el).addClass('movie-link');
						}
					break;
				case 'screenLink':
				case 'link':
						if(!that.aspect){
							var type = $(this).attr('type');
							var screenLinkURL = $(this).text();

							if(screenLinkURL && screenLinkURL != ''){
								that.linkURL = screenLinkURL;
								if(type == 'pachyderm.screen'){
									that.linkClass = 'internal-link';
									that.linkMarkup = '<a href="#screen/' + that.linkURL + '" tabindex="' + (parseInt(that.index) + 1) + '">';
									
									var template_name = 'default';
									if(pachyderm.presentation.get(that.linkURL)){
										template_name = pachyderm.parse_template_name(pachyderm.presentation.get(that.linkURL).get('template'));
										template_name = (template_name == 'zoom') ? 'zoom' : '-template';
										that.linkClass += ' ' + template_name + '-template';
									}
									
									$(that.el).addClass(template_name + '-template');
								}else{
								
									var file = false;
									if(fileExtensions.length){
										for(var i = 0; i < fileExtensions.length; i++){
											if(that.linkURL.indexOf(fileExtensions[i])){
												file = true;
											}
										}
									}
								
									that.linkClass = 'external-link';
								
									if(file){
										that.linkClass += ' document';
									}
									that.linkMarkup = '<a href="' + that.linkURL + '" target=_blank tabindex="' + that.index + '">';
								}
							
								$(that.el).addClass(that.linkClass);
							}
						}
					break;
			}
		});
		
		if(this.aspect){
			this.linkURL = '#';
			this.linkClass = 'movie-link';
			this.linkMarkup = '<a href="javascript:;" class="video-swap" meta_xml="' + this.index + '">';
			$(this.el).addClass('movie-link');
			
			var aspects_image_component = this.xmlObj.find('component[type=pachyderm.aspects.image_item][name=imageItem] data_element[name=thumbnail]');
			if(aspects_image_component.text()){
				this.imageMetaXML = aspects_image_component.text();
				this.image = pachyderm.parse_asset_file_name(this.imageMetaXML);
			}
		}
		
		if(this.image){
			this.model = new PachydermWidgetModel({xmlFile: this.imageMetaXML});
			this.model.fetch({'error': function(m,r){that.onMetaData(r.responseText);}});
		}else{
			$(this.el).addClass('no-image');
		}
		
		this.render();
	},
	onMetaData: function(x){
		var resources = ($($(x)[2]).find('resource')).children();
		
		var alt = null;
		$(resources).each(function(index,value){
			if($(value)[0].nodeName.toLowerCase() == 'accessibility:alt'){
				alt = $($(value)[0]).text();
			}
		});
		
		if(alt && alt != ''){
			$(this.el).find('img').attr('alt',alt);
		}
	},
	render: function(){
		var markup = '';
		
		if(this.linkText || this.image){
			if(this.linkURL != '' && this.linkMarkup){
				markup += this.linkMarkup;
			}
			
				if(this.image && this.image != ''){
					markup += '<img src="' + this.image + '" alt=""/>';
				}

				markup += '<span class="title">' + this.linkText + '</span>';

			if(this.linkURL != ''){
					if(this.image && this.image != ''){
						markup += '<span class="icon ' + this.linkClass + '"></span>';
					}
				markup += '</a>';
			}
		
			$(this.el).html(markup);

			if(this.options.onImageLoad){
				var that = this;

				if(this.image && this.image != ''){					
					if(!pachyderm.loadImages){
						this.options.parent.imageLoaded();
					}
					
					this.$("img").load(function(){
						that.centerThumbnail(that,this);
					});
				}else{
					this.options.parent.imageLoaded();
				}
			}
		}else{
			if(this.options.onImageLoad){
				this.options.parent.imageLoaded();
			}
		}
		
		return this;
	},
	reset: function(){
		$(this.el).removeClass('active');
		this.active = false;
	},
	setActive: function(){
		$(this.el).addClass('active');
		this.active = true;
	},
	centerThumbnail: function(widget,image){
		var $thumbnail = $(image);
		var w = image.width;
		var h = image.height;
		var l = Math.floor((165 - w) * .5);

		$thumbnail.siblings('.icon').css({'left': l, 'top': h - 20});
		$thumbnail.css('margin-left',l);
		
		widget.options.parent.imageLoaded();
	},
	on_thumbnail_over: function(e){
		if(!$(this.el).hasClass('movie-link')){
			var $e = $(e.currentTarget);
		
			if(!$e.attr('enlarging') || $e.attr('enlarging') == 'false'){
				$e.addClass('hover');
				$e.attr('enlarging','true');
				$e.stop(true,true).animate({
				    fontSize: "17px"
				  }, 250 );
			}
		}else{
			$(this.el).addClass('active');
		}
	},
	on_thumbnail_out: function(e){
		if(!$(this.el).hasClass('movie-link')){
			var l = $(e.currentTarget).offset().left;
			var t = $(e.currentTarget).offset().top;
			var w = $(e.currentTarget).width();
			var h = $(e.currentTarget).height();
			var x = e.pageX;
			var y = e.pageY;
		
			if(x < l || (x > l + w) || y < t || (y > t + h)){
				var font_size = '12px';
				var $e = $(e.currentTarget);
		
				if($e.hasClass('no-image')){
					font_size = '15px';
				}
			
				$e.removeClass('hover');
				$e.attr('enlarging','false');
				$e.stop(true,true).animate({
				    fontSize: font_size
				  }, 250 );
			}
		}else if(!this.active){
			$(this.el).removeClass('active');
		}
	},
	on_video_swap: function(){
		this.setActive();
		this.options.parent.swapMainMedia(this);
	}
});