var Pachyderm = Backbone.Router.extend({
	shell: null,
	go: false,
	kickstartItems: 2,
	presentation: null,
	initialScreenURL: null,
	initialScreenSlideURL: null,
	startingScreen: null,
	currentScreen: null,
	currentQuery: null,
	currentSlide: null,
	nonce: 0,
	urlHistory: [],
	screens: {},
	screenContainer: $('#slide-wrapper'),
	wrapper: $('#pachyderm-wrapper'),
	loadImages: true,
	options: {
		root_xml: 'root_border.xml',
		presentation_xml: 'presentation.xml'
	},
 	routes: {
		"": 						"index",	// #
		"screen/:query":        	"screen", 	// #screen/00745053-6003164-225001478-128-114-13-100-47120-60
		"screen/:query/s:slide": 	"screen"  	// #screen/00745053-6003164-225001478-128-114-13-100-47120-60/s7
	},
	initialize: function(opts) {
		
		if(opts){
			this.options = _.defaults(opts, this.options);
		}
		
		this.loadBorder();
		this.loadPresentation();
	},
	loadBorder: function(){
		$.ajax({
				url: this.options.root_xml,
				dataType: "xml",
				error: function(xhr, ajaxOptions, thrownError){
					alert("Root XML Load Error: " + thrownError + " (" + this.url +")");
				},
				success: function(xml){
					var $xml = $(xml);
					var frame = $xml.children('frame');

					if(frame.length){
						var $frame = $(frame);

						var xml_bg_color = $frame.attr('bg_color');
						var xml_nav_color = $frame.attr('nav_color');

						var xml_midground_image = $frame.attr('midground_image');
						var xml_midground_opacity = $frame.attr('midground_opacity');						
						
						var xml_background_image = '';
						var xml_background_image_x = '';
						var xml_background_image_y = '';
						
						var $xml_image = $frame.find('image');
						if($xml_image.length){
							xml_background_image = pachyderm.parse_asset_file_name($xml_image.attr('file'));
							xml_background_image_x = $xml_image.attr('x');
							xml_background_image_y = $xml_image.attr('y');
						}

						var shellModel = new ShellM({
													'background_image': xml_background_image,
													'background_image_x': xml_background_image_x,
													'background_image_y': xml_background_image_y,
													'bg_color': xml_bg_color,
													'nav_color': xml_nav_color,
													'midground_image': xml_midground_image,
													'midground_opacity': xml_midground_opacity
												});
												
						pachyderm.shell = new Shell({el: '#pachyderm-wrapper', model: shellModel});
						pachyderm.shell.render();
						pachyderm.kickstart();
					}
				}
			});
			
		return this;
	},
	loadPresentation: function(){
		$.ajax({
				url: this.options.presentation_xml,
				dataType: "xml",
				error: function(xhr, ajaxOptions, thrownError){
					alert("Presentation XML Load Error: " + thrownError + " (" + this.url +")");
				},
				success: function(xml){
					var $xml = $(xml);
					
					var startingScreen = null;
					var startingScreenNode = $xml.find('description starting_screen');
					if(startingScreenNode.length){
						startingScreen = $(startingScreenNode).text(); // First Page ID
					}
					
					var screens = $xml.find('content screen'); 
					if(screens){
						pachyderm.presentation = new PachydermPresentation();
						
						$(screens).each(function(index,value){
							var i = $(value).attr('id');
							var t = $(value).attr('template');
							var x = $(value).attr('xml');
							
							pachyderm.presentation.add([{id: i, template: t, xml: x}]);
						});
						
						pachyderm.startingScreen = startingScreen;
						pachyderm.kickstart();
					}
				}
			});
	},
	kickstart: function(){
		this.kickstartItems--;
		
		if(this.kickstartItems === 0){
			this.go = true;
			
			if(this.initialScreenURL){
				this.screen(this.initialScreenURL,this.initialScreenSlideURL);
			}else{
				this.index();
			}
		}
	},
	index: function(){
		if(this.go){
			pachyderm.navigate('screen/' + this.startingScreen, true);
		}
	},
	screen: function(query, s) {
		var that = this;
		var series_screen = s;
		var slide = (s) ?  s : 1;

		if(this.go){
			this.logScreen(query,s);
			var model = this.presentation.get(query);
			var template_name = this.parse_template_name(model.get('template'));
			var formatted_screen = this.format_screen_name(query);
			
			if(this.currentScreen && this.currentScreen.xmlName != query){
				var oldCurrentScreen = this.currentScreen;
				$(this.currentScreen.el).fadeOut(500,function(){oldCurrentScreen.reset();});
			}
			
			if(eval("this.screens." + formatted_screen)){
				v = eval("this.screens." + formatted_screen);
				if (typeof v.setSlide === 'function') {
					v.setActiveSlide(slide);
				}else if(typeof v.setLayer === 'function'){
					v.setLayer(slide);
				}else if(typeof v.setAspect === 'function'){
					v.setAspect(slide);
				}else if(typeof v.setSeries === 'function'){
					v.setActiveSeries(series_screen);
				}
				
				v.render();
				$(v.el).stop().fadeIn();
			}else{
				var v;
				var m = new PachydermPageModel({xmlName: query});
				
				switch(template_name){
					case 'commentary':
							v = new Commentary({xmlName: query, model: m});
						break;
					case 'exploration_entry':
							v = new Exploration_Entry({xmlName: query, model: m});
						break;
					case 'enlargement':
							v = new Enlargement({xmlName: query, model: m});
						break;
					case 'phonedial':
							v = new PhoneDial({xmlName: query, model: m});
						break;
					case 'dual_comparison':
							v = new DualComparison({xmlName: query, model: m});
						break;
					case 'media_focus':
							v = new MediaFocus({xmlName: query, model: m});
						break;
					case 'slider':
							v = new Slider({activeSlide: slide, xmlName: query, model: m});
						break;
					case 'layers':
							v = new Layers({activeLayer: slide, xmlName: query, model: m});
						break;
					case 'fullscreen_swf':
							v = new FullScreenSWF({xmlName: query, model: m});
						break;
					case 'aspects':
							v = new Aspects({activeAspect: slide, xmlName: query, model: m});
						break;
					case 'series':
							v = new Series({activeSeries: series_screen, xmlName: query, model: m});
						break;
					case 'zoom':
							v = new Zoom({xmlName: query, model: m});
						break;
				}
				
				if(v){
					$(v.el).fadeIn();
					eval("this.screens." + formatted_screen + " = v");
				}else{
					console.log('trying to load a view that does not yet exist: ' + template_name);
				}
			}

			this.currentScreen = v;
			
		}else{
			this.initialScreenURL = query;
			this.initialScreenSlideURL = s;
		}
	},
	/*
	 * Log URL's, and managing the Back/Home buttons based on this
	 */
	logScreen: function(xml,zoomOverride){
		this.currentQuery = xml;
		this.currentSlide = zoomOverride;
		
		var screenCount = this.urlHistory.length;
		if(!screenCount || (screenCount && this.urlHistory[screenCount - 1] != xml)){
			this.urlHistory.push(xml);
			screenCount++;
		}
		
		var back = false;
		var home = false;
		var zoom = false;
		
		if(screenCount > 1){
			back = true;
			home = true;
		}
		
		var template_name = this.parse_template_name(pachyderm.presentation.get(xml).get('template'));
		if(template_name == 'zoom' || (template_name == 'series' && zoomOverride)){
			zoom = true;
			
			if(template_name == 'series' && zoomOverride){
				back = true;
			}
		}

		this.shell.toggleNavigation(back,home, zoom);
	},
	/*
	 * Go back to the previous Screen
	*/
	goBackToPreviousScreen: function(){
		var template_name = this.parse_template_name(pachyderm.presentation.get(this.currentQuery).get('template'));
		
		if(template_name == 'series' && parseInt(this.currentSlide) > 0){
			pachyderm.navigate('screen/' + this.currentQuery, true);
		}else{
			var xml = this.urlHistory.pop();
			xml = this.urlHistory.pop();
			pachyderm.navigate('screen/' + xml, true);
		}
	},
	/*
	 * When user clicks 'Home', clear history
	 */
	clearHistory: function(){
		this.urlHistory = [];
	},
	/*
	 * Convert flash color to css friendly
	 */
	convert_uint_to_hex: function(uint){
		var color = uint.replace('0x', '');
		color = color.replace('#', '');
		return '#' + color;
	},
	/*
	 * Unique Number
	 */
	get_nonce: function(){
		this.nonce++;
		return this.nonce;
	},
	/*
	 * Most asset file names will point to an XML file as opposed to the real file
	 * Check if this is the case, and chop off the _metadata.xml
	 */
	parse_asset_file_name: function(file_name){
		var new_file_name = file_name.replace('_metadata.xml', '');
		
		if(new_file_name.substr(-3) == 'flv' || new_file_name.substr(-3) == 'mov'){
			new_file_name = new_file_name.substr(0,new_file_name.length - 3) + "mp4";
		}
		
		return new_file_name;
	},
	/*
	 * Template names within the XML often have the associated swf file extension.
	 * This will strip it
	 */
	parse_template_name: function(template_name){
		return template_name.replace('.swf', '');
	},
	/*
	 * Format screen ID by stripping out dashes
	 */
	format_screen_name: function(screen_name){
		return 'screen_' + screen_name.replace(/-/g, '');
	},
	/*
	 * Get markup for a horizontal scrollable text area
	 */
	get_horizontal_scrolling: function (text,id,el_class){
		return '<div class="hscrollable ' + el_class + '"><div class="horizontal-field" id="' + id + '"><div class="scroll-content">' + text + '</div></div></div>';
	},
	/*
	 * Adds 'has_scrollbar' class to a text area if it's needed
	 */
	set_scroll: function(el_id,type) {
		var myScroll;
		
		myScroll = new iScroll(el_id,{vScroll: false, vScrollbar: false, hScrollbar: false, hideScrollbar: true});
		
		setTimeout(function () {
			myScroll.refresh();
		}, 0);
		
		return myScroll;
	}
});

$(document).ready(function() {
	
	var $html = $('html');
	var ua = $.browser;
	var ver = parseInt(ua.version.slice(0,3),10);
	
	if($html.hasClass('oldie') || (ua.mozilla &&  ver <= 9)){
		window.location.href = 'old-browser.html';
		return;
	}
	
	pachyderm = new Pachyderm();
	Backbone.history.start({pushState: false});
});