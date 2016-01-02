var Shell = Backbone.View.extend({
	tagName: "section",
	assistance_container: null,
	events: {
		"mouseover #navigation-wrapper a": 	"on_navigation_hover",
		"mouseout #navigation-wrapper a": 	"on_navigation_hover_out",
		"click #navigation-wrapper a": 			"on_navigation_hover_out",
		"click a#back":											"on_back",
		"click a#home":											"on_home",
		"click a#contrast": 								"toggle_contrast",
		"click a#help": 										"toggle_help",
		"click a#info": 										"toggle_info",
		"click a.help-close": 							"close_assistance_container"
	},
	initialize: function(){
		this.model.bind('change', this.render, this);
	},
	render: function() {
		// Set Background Color
		$(this.el).css('background-color',this.model.get_bg_color());

		// Set Navigation Color
		this.$('#navigation-wrapper a').css('background-color',this.model.get_nav_color());
		
		// Set Midground Image
		var im = this.model.get_midground_image();
		if(im != '' && im != null){
			var image = this.make('img',{'src': im});
			this.$("#content-background").html(image);
		}else{
			this.$("#content-background").html('&nbsp;');
		}
		
		// Set Midground Image Opacity
		this.$("#content-background").children('img').css('opacity', this.model.get_midground_opacity());
		
		// Set Background Image
		var im = this.model.get_background_image();
		
		if(im != '' && im != null){
			var presentationHeight = $(window).height();
			presentationHeight = (presentationHeight <= 672) ? 672 : 748;
			var im_height = 748;
			var im_width = 1024;
			
			var image = this.make('img',{'src': im, 'width': im_width, 'height': im_height, 'alt': ''});
			$(image).css({'top': this.model.get_background_image_y(), 'left': this.model.get_background_image_x()});
			this.$("#presentation-background").html(image);
			
			$('#pachyderm-wrapper').height(presentationHeight);
		}
		
		var isiPad = navigator.userAgent.match(/iPad/i) != null;
		var ua = navigator.userAgent;
		var isiPad = /iPad/i.test(ua) || /iPhone OS 3_1_2/i.test(ua) || /iPhone OS 3_2_2/i.test(ua);
		
		if(isiPad){
			$('body').addClass('ipad');
		}
		
		return this;
	},
	update: function(xml){
		var $midground_image = $(xml).find('component[name="midground_image"]');
		
		if($midground_image.length){
			$midground_image = $($midground_image[0]);
			
			var midground_image = $midground_image.children('data_element[name="image"]').text();
			var midground_opacity = $midground_image.children('data_element[name="alpha"]').text();
			
			this.model.set({'midground_image': midground_image, 'midground_opacity': midground_opacity});
		}
	},
	toggleNavigation: function(back,home,zoom){		
		if(back){
			this.$('#navigation-wrapper #back').show();
		}else{
			this.$('#navigation-wrapper #back').hide();
		}
		
		if(home){
			this.$('#navigation-wrapper #home').show();
		}else{
			this.$('#navigation-wrapper #home').hide();
		}
		
		if(zoom){
			this.$('#navigation-wrapper').addClass('zoom-position');
		}else{
			this.$('#navigation-wrapper').removeClass('zoom-position');
		}
	},
	on_navigation_hover: function(e){
		$(e.currentTarget).css({'background-color': '#ffffff', 'color': this.model.get_nav_color()});
	},
	on_navigation_hover_out: function(e){
		$(e.currentTarget).css({'color': '#ffffff', 'background-color': this.model.get_nav_color()});
	},
	get_background_color: function(){
		return this.model.get_bg_color();
	},
	toggle_contrast: function(){
		$(this.el).removeClass('help');
		$(this.el).removeClass('info');
		
		if($(this.el).hasClass('contrast')){
			$(this.el).removeClass('contrast');
		}else{
			$(this.el).addClass('contrast');
		}
	},
	toggle_help: function(){
		$(this.el).removeClass('info');
		$(this.el).removeClass('help');
		
		$assistance_container = this.get_help_container();
		$assistance_container.removeClass('info');
		
		if($assistance_container.hasClass('help')){
			$assistance_container.removeClass('help');
			$assistance_container.addClass('hidden');
		}else{
			if($assistance_container.hasClass('hidden')){
				$assistance_container.removeClass('hidden');
			}
			
			$(this.el).addClass('help');
			$assistance_container.addClass('help');
		
			this.$('#help-content').html('');
			this.$('#help-content').load('help.html');
		}
	},
	toggle_info: function(){
		$(this.el).removeClass('help');
		$(this.el).removeClass('info');
		
		$assistance_container = this.get_help_container();
		$assistance_container.removeClass('help');
		
		if($assistance_container.hasClass('info')){
			$assistance_container.removeClass('info');
			$assistance_container.addClass('hidden');
		}else{
			if($assistance_container.hasClass('hidden')){
				$assistance_container.removeClass('hidden');
			}
			
			var curScreenXML = pachyderm.currentScreen.xmlName;
			var template_name = pachyderm.parse_template_name(pachyderm.presentation.get(curScreenXML).get('template'));
			
			
			$(this.el).addClass('info');
			$assistance_container.addClass('info');
		
			this.$('#help-content').html('');
			this.$('#help-content').attr('class','');
			this.$('#help-content').addClass('template-' + template_name);
			this.$('#help-content').load('info.html');
		}
	},
	close_assistance_container: function(){
		$(this.el).removeClass('help');
		$(this.el).removeClass('info');
		
		$assistance_container.removeClass('help');
		$assistance_container.removeClass('info');
		$assistance_container.addClass('hidden');
	},
	get_help_container: function(){
		if(!this.assistance_container){
			$(this.el).append('<div id="pachyderm-help" class="hidden"><div id="help-content"></div><a href="javascript:;" class="help-close">Close</a></div>');
			this.assistance_container = this.$("#pachyderm-help");
		}
		
		return this.assistance_container;
	},
	on_back: function(){
		pachyderm.goBackToPreviousScreen();
		return false;
	},
	on_home: function(){
		pachyderm.clearHistory();
	}
});

var ShellM = Backbone.Model.extend({
	default_background_image: 'default',
	default_background_image_x: 0,
	default_background_image_y: 0,
	default_bg_color: 'default',
	default_nav_color: 'default',
	default_midground_image: 'default',
	default_midground_opacity: 'default',
	background_image: null,
	bg_color: null,
	nav_color: null,
	midground_image: null,
	midground_opacity: null,
	initialize: function(opts){
		this.set({
					'default_background_image': this.get('background_image'),
					'default_background_image_x': this.get('background_image_x'),
					'default_background_image_y': this.get('background_image_y'),
					'default_bg_color': pachyderm.convert_uint_to_hex(this.get('bg_color')),
					'default_nav_color': pachyderm.convert_uint_to_hex(this.get('nav_color')),
					'default_midground_image': this.get('midground_image'),
					'default_midground_opacity': parseInt(this.get('midground_opacity')) * .01
				});
	},
	get_background_image: function(){
		var v = this.get('background_image');
		return (v === null || v == '') ? this.get('default_background_image') : pachyderm.parse_asset_file_name(v);
	},
	get_background_image_x: function(){
		var v = this.get('background_image_x');
		return (v === null || v == '') ? this.get('default_background_image_x') : v;
	},
	get_background_image_y: function(){
		var v = this.get('background_image_y');
		return (v === null || v == '') ? this.get('default_background_image_y') : v;
	},
	get_bg_color: function(){
		var v = this.get('bg_color');
		return '#FFFFFF';
		return (v === null || v == '') ? this.get('default_bg_color') : pachyderm.convert_uint_to_hex(v);
	},
	get_nav_color: function(){
		var v = this.get('nav_color');
		return (v === null || v == '') ? this.get('default_nav_color') : pachyderm.convert_uint_to_hex(v);
	},
	get_midground_image: function(){
		var v = this.get('midground_image');
		return (v === null || v == '') ? this.get('default_midground_image') : pachyderm.parse_asset_file_name(v);
	},
	get_midground_opacity: function(){
		var v = this.get('midground_opacity');
		return (v === null || v == '') ? this.get('default_midground_opacity') : parseInt(v) * .01;
	}
});