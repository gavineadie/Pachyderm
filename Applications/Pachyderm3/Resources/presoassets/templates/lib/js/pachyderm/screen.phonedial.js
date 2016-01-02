var PhoneDial = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'phonedial screen',
	xmlName: null,
	xml: null,
	contentXML: null,
	t: null,
	scrollingTextWidget: null,
	preloader: null,
	dialDefaultText: null,
	activeTouchButton: null,
	touchDevice: false,
	events: {
		"mouseover .dial-button-container"			:"onDialButtonOver",
		"mouseout .dial-button-container"				:"onDialButtonOut",
		"click .dial-button-container" 					:"onDialButtonClick"
	},
	initialize: function(options) {

		this.scrollingTextWidget = null;
		this.preloader = null;
		this.dialDefaultText = null;
		this.activeTouchButton = null;
		this.touchDevice = false;

		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;

		this.touchDevice = $('html').hasClass('touch');

		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);

		this.t.append('<div class="screen-container phonedial"></div>');
		pachyderm.screenContainer.append(this.el);

		this.screenContainer = this.$(".screen-container");
		this.screenContainer.hide();

		var that = this;
		this.model.fetch({'error': function(m,r){that.onXML(r.responseText);}});
    },
	onXML: function(x){
		this.xml = x;
		this.contentXML = $(this.xml).find('content');
		var $component = $(this.xml).find('content component[type="pachyderm.enlargement"]');

		var title = new WidgetTitle({xml: this.contentXML, parent: this});
		this.$(".screen-container").append(title.el);

		// Scrolling Text
		var textArea = $(this.xml).find('data_element[name=mainText]');
		if(textArea.length){
			this.scrollingTextWidget = new WidgetScrollableText({text: $(textArea[0]).text(), scrollClass: 'main-text', parent: this});
			this.$(".screen-container").append(this.scrollingTextWidget.el);
		}

		// Link Menu
		var that = this;
		$(this.xml).find('component[name=linkMenu] component').each(function(index,el){
			var $el = $(el);
			var type = $el.find('data_element[name=screenLink]').attr('type');	/* TYLER */
			var url = $el.find('data_element[name=screenLink]').text();
			var title = $el.find('data_element[name=linkText]').text();

			if(url == ''){
				screenlink = '';
			}else if(type == 'pachyderm.screen'){
				screenlink = '<a href="#screen/' + url + '" class="link-' + index + '">' + title + '</a>';
			}else{
				screenlink = '<a href="' + url + '" class="link-' + index + '" target=_blank>' + title + '</a>';
			}

			that.$(".screen-container").append(screenlink);
		});

		// Phone Dial Center
		this.dialDefaultText = $(this.xml).find('component[name=phoneDialMenu] data_element[name=text]').text();
		this.dialDefaultText = '<p>' + this.dialDefaultText + '</p>';
		that.$(".screen-container").append('<div class="dial-center-container"><div class="dial-center">' + this.dialDefaultText + '</div></div>');

		var dialButtonsMarkup = '';

		// Phone Dial Buttons
		$(this.xml).find('component[name=phoneDialMenu] component[name=item]').each(function(index,el){
			var $el = $(el);
			var thumbnail = $el.find('data_element[name=thumbnail]').text();
			var rolloverImage = $el.find('data_element[name=rollover]').text();
			var caption = $el.find('data_element[name=caption]').text();

			var type = $el.find('data_element[name=link]').attr('type');
			var url = $el.find('data_element[name=link]').text();

			if(caption){
				var markup = '<div class="dial-button-container dial-button-' + index + '" rollover="' + pachyderm.parse_asset_file_name(rolloverImage) + '"><div class="dial-button">';
						if(url == ''){
							markup += '<a href="javascript:;">';
						}else if(type == 'pachyderm.screen'){
							markup += '<a href="#screen/' + url + '">';
						}else{
							markup += '<a href="' + url + '" target=_blank>';
						}

							if(thumbnail){
								markup += '<img src="' + pachyderm.parse_asset_file_name(thumbnail) + '" alt="">';
							}
							markup += '<span class="caption">' + caption + '</span>';
						markup += '</a>';
					markup += '</div></div>';

					dialButtonsMarkup += markup;
			}
		});

		this.$(".screen-container").append('<div class="phone-dial-touch-container">' + dialButtonsMarkup + '</div>');

		this.$(".screen-container .dial-button-container a").focus(function(e){
			e.currentTarget = $(this).parents('div.dial-button-container');
			that.onDialButtonOver(e);
		});

		this.$(".screen-container .dial-button-container a").blur(function(e){
			e.currentTarget = $(this).parents('div.dial-button-container');
			that.onDialButtonOut(e);
		});

		this.$('.dial-button-container a img').load(function(){
			var iw = $(this).width();
			var ih = $(this).height();

			var w = $(this).parents('.dial-button').width();
			var h = $(this).parents('.dial-button').height();

			var l = (w * .5) - (iw * .5);
			var t = (h * .5) - (ih * .5);

			$(this).css({'margin-left': l, 'margin-top': t});
		});

		this.render();
	},
	render: function(){
		pachyderm.shell.update(this.contentXML);

		this.$(".preloader").fadeOut(500,function(){$(this).remove()});
		this.$(".screen-container").fadeIn(500);

		var that = this;
		this.$('.phone-dial-touch-container')[0].addEventListener('touchmove',function(e){
			e.preventDefault();
		    var touch = e.touches[0];
			var hit = false;

			that.$('.dial-button-container').each(function(index,el){
				var pos = $(el).offset();
				var w = $(el).width();
				var h = $(el).height();

				if(touch.pageX > pos.left && touch.pageX < pos.left + w && touch.pageY > pos.top && touch.pageY < pos.top + h){
					hit = true;
					that.activeTouchButton = {'currentTarget': el};
				}
			})

			if(hit){
				that.onDialButtonOver(that.activeTouchButton);
			}else{
				that.onDialButtonOut();
			}
		});

		this.$('.phone-dial-touch-container')[0].addEventListener('touchend',function(e){
			that.onDialButtonOut();
		});

		if(this.scrollingTextWidget){
			this.scrollingTextWidget.refresh();
		}

		return this;
	},
	reset: function(){

	},
	onDialButtonOver: function(e,forceIt){
		if(!this.touchDevice || forceIt){
			var rollover = $(e.currentTarget).attr('rollover');
			$(e.currentTarget).addClass('over');

			if(rollover){
				this.$('.dial-center').html('<img src="' + rollover + '">');
			}
		}
	},
	onDialButtonOut: function(e, forceIt){
		if(!this.touchDevice || forceIt){
			this.$('.dial-button-container').removeClass('over');
			this.$('.dial-center').html(this.dialDefaultText);
		}
	},
	onDialButtonClick: function(e){
		if(this.touchDevice){
			var state = $(e.currentTarget).attr('currentState');

			if(state == 'advance'){
				onDialButtonOut(true,true);
				$(e.currentTarget).attr('currentState','');
				return true;
			}

			this.$('.dial-button-container[currentState=advance]').removeClass('over').attr('currentState','');

			$(e.currentTarget).attr('currentState','advance')
			this.onDialButtonOver(e,true);

			return false;
		}
	}
});