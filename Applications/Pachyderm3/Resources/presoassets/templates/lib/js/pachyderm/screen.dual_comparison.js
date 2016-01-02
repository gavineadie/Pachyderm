var DualComparison = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'dual-comparison screen',
	xmlName: null,
	xml: null,
	contentXML: null,
	t: null,
	preloader: null,
	currentLayer: 'original',
	leftMediaWidget: null,
	rightMediaWidget: null,
	leftScrollWidget: null,
	rightScrollWidget: null,
	mainScrollWidget: null,
	events: {
		"click .layer-navigation a"			:"layerNavClick"
	},
	initialize: function(options) {
		this.preloader = null;
		this.currentLayer = 'original';
		this.leftMediaWidget = null;
		this.rightMediaWidget = null;
		this.leftScrollWidget = null;
		this.rightScrollWidget = null;
		this.mainScrollWidget = null;
		
		this.t = $(this.el);
    this.xmlName = options.xmlName;

		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container dual-comparison"></div>');
		pachyderm.screenContainer.append(this.el);
		
		this.screenContainer = this.$(".screen-container");
		this.screenContainer.hide();
		
		var that = this;
		this.model.fetch({'error': function(m,r){that.onXML(r.responseText);}});
  },
	onXML: function(x){
		this.xml = x;
		this.contentXML = $(this.xml).find('content');
		var $component = $(this.xml).find('content component[type="pachyderm.dual_comparison"]');
		
		var title = new WidgetTitle({xml: this.contentXML, parent: this});
		this.$(".screen-container").append(title.el);
		
		var navMarkup = '<div class="layer-navigation"><a href="javascript:;" class="original active">Overview</a><a href="javascript:;" class="left">Item 1</a><a href="javascript:;" class="right">Item 2</a></div>'
		this.$(".screen-container").append(navMarkup);
		
		var textArea = $(this.xml).find('data_element[name=mainText]');
		this.mainScrollWidget = new WidgetScrollableText({text: $(textArea[0]).text(), scrollClass: 'main-text', parent: this});
		this.$(".screen-container").append(this.mainScrollWidget.el);
		
		var $leftContent = $(this.xml).find('content component[type="pachyderm.dual_comparison"] component[index="1"]');
			this.leftMediaWidget = new WidgetMainMedia({xmlNode: $leftContent, parent: this, imageNode: 'image',mediaNode: 'movie',captionNode: 'caption',linkNode: 'link',containerClass: 'dual-comparison-media-one',onPlayCallback: 'mediaStarted'});
			this.$(".screen-container").append(this.leftMediaWidget.el);
			
			var t = $leftContent.find('data_element[name=text]').text();
			this.leftScrollWidget = new WidgetScrollableText({text: t, scrollClass: 'main-text', parent: this});
			this.$(".screen-container").append(this.leftScrollWidget.el);
			$(this.leftScrollWidget.el).hide();
		
		var $rightContent = $(this.xml).find('content component[type="pachyderm.dual_comparison"] component[index="2"]');
			this.rightMediaWidget = new WidgetMainMedia({xmlNode: $rightContent, parent: this, imageNode: 'image',mediaNode: 'movie',captionNode: 'caption',linkNode: 'link',containerClass: 'dual-comparison-media-two',onPlayCallback: 'mediaStarted'});
			this.$(".screen-container").append(this.rightMediaWidget.el);
			
			var t = $rightContent.find('data_element[name=text]').text();
			this.rightScrollWidget = new WidgetScrollableText({text: t, scrollClass: 'main-text', parent: this});
			this.$(".screen-container").append(this.rightScrollWidget.el);
			$(this.rightScrollWidget.el).hide();
		
		this.render();
	},
	render: function(){
		pachyderm.shell.update(this.contentXML);
			
		this.$(".preloader").fadeOut(500,function(){$(this).remove()});
		this.$(".screen-container").fadeIn(500);
		
		this.mainScrollWidget.refresh();
		
		this.leftMediaWidget.setupMedia();
		this.rightMediaWidget.setupMedia();
		
		return this;
	},
	reset: function(){
		this.changeLayer('original');
	},
	layerNavClick: function(e){
		this.$('.layer-navigation a').removeClass('active');
		this.changeLayer($(e.currentTarget).attr('class'));
	},
	changeLayer: function(layer){
		this.$('.layer-navigation a').removeClass('active');
		
		if(layer == 'original'){
			this.rightMediaWidget.reset();
			$(this.rightScrollWidget.el).css('display','none');
			
			this.leftMediaWidget.reset();
			$(this.leftScrollWidget.el).css('display','none');
			
			$(this.mainScrollWidget.el).css('display','block');
			this.mainScrollWidget.refresh();
		}else if(layer == 'left'){
			this.rightMediaWidget.reset();
			$(this.rightScrollWidget.el).css('display','none');
			
			$(this.mainScrollWidget.el).css('display','none');
			
			$(this.leftScrollWidget.el).css('display','block');
			this.leftScrollWidget.refresh();
		}	else if(layer == 'right'){
				this.leftMediaWidget.reset();
				$(this.leftScrollWidget.el).css('display','none');

				$(this.mainScrollWidget.el).css('display','none');

				$(this.rightScrollWidget.el).css('display','block');
				this.rightScrollWidget.refresh();
		}
		
		this.$('.layer-navigation a.' + layer).addClass('active');
	},
	mediaStarted: function(widget){
		if($(widget.el).hasClass('dual-comparison-media-one')){
			this.changeLayer('left');
		}else{
			this.changeLayer('right');
		}
	}
});