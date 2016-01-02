var Enlargement = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'enlargement screen',
	xmlName: null,
	xml: null,
	contentXML: null,
	t: null,
	scrollingTextWidget: null,
	mainMediaWidget: null,
	preloader: null,
	initialize: function(options) {
		this.contentXML = null;
		this.t = null;
		this.scrollingTextWidget = null;
		this.mainMediaWidget = null;
		this.preloader = null;
		
		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;

		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container enlargement"></div>');
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
		
		this.mainMediaWidget = new WidgetMainMedia({xmlNode: $component, parent: this, imageNode: 'image',mediaNode: 'movie',captionNode: 'caption',linkNode: 'screenLink'});
		this.$(".screen-container").append(this.mainMediaWidget.el);
		
		var textArea = $(this.xml).find('data_element[name=text]');
		if(textArea.length){
			this.scrollingTextWidget = new WidgetScrollableText({text: $(textArea[0]).text(), scrollClass: 'main-text', parent: this});
			this.$(".screen-container").append(this.scrollingTextWidget.el);
		}
		
		this.render();
	},
	render: function(){
		pachyderm.shell.update(this.contentXML);
			
		this.$(".preloader").fadeOut(500,function(){$(this).remove()});
		this.$(".screen-container").fadeIn(500);
		
		if(this.scrollingTextWidget){
			this.scrollingTextWidget.refresh();
		}
		
		if(this.mainMediaWidget){
			this.mainMediaWidget.setupMedia();
		}
		
		return this;
	},
	reset: function(){
		this.mainMediaWidget.reset();
	}
});