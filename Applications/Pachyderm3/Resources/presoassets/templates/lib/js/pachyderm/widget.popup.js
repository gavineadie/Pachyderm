var WidgetPopup = Backbone.View.extend({
	tagName: 'div',
	className: 'info-popup',
	content: '',
	parent: null,
	type: null,
	image: false,
	scrollingTextWidget: null,
	textOverride: null,
	draggable: false,
	isOpen: false,
	events: {
		"click a.info-close"				:"reset"
	},
	initialize: function(options){		
		this.content = '';
		this.parent = null;
		this.type = null;
		this.image = false;
		this.isOpen = false;
		this.draggable = false;
		this.scrollingTextWidget = null;
		this.textOverride = null;
		
		this.parent = options.parent;
		this.type = options.type;
		this.image = options.image;
		this.draggable = options.draggable;
		this.textOverride = options.textOverride;

		if(this.textOverride){
			this.content = this.textOverride;
			this.render();
		}else{
			var that = this;
			this.model = new PachydermWidgetModel({xmlFile: options.xmlName});
			this.model.fetch({'error': function(m,r){that.onMetaData(r.responseText);}});
		}
	},
	onMetaData: function(x){
		var resources = ($($(x)[2]).find('resource')).children();
		
		if(this.type == 'info'){
			var that = this;
			var alt = '';
			$(resources).each(function(index,value){
				if($(value)[0].nodeName.toLowerCase() == 'label:line'){
					alt += $($(value)[0]).text();
					alt += '<br>';
				}else if($(value)[0].nodeName.toLowerCase() == 'accessibility:transcript'){
					that.parent.setTranscript($($(value)[0]).text());
				}
			});
		
			if(alt && alt != ''){
				this.content = alt;
			}
		}else if(this.type == 'transcript'){
			this.content = $(x).html();
		}
		
		this.render();
	},
	render: function(){
		if(this.content && this.content != '' && this.content != '<br>'){
			this.scrollingTextWidget = new WidgetScrollableText({text: this.content, scrollClass: 'info-scroll-text', parent: this});
			if(this.draggable){
				$(this.el).append('<p class="handle"></p>');
				$(this.el).addClass('with-handle');
			}
			
			$(this.el).append(this.scrollingTextWidget.el);
			$(this.el).append('<a href="javascript:;" class="info-close">X</a>');
			
			$(this.el).css('display','none');
			this.parent.addInfoPopup(this);
		}
		
		return this;
	},
	open: function(){
		if(!this.isOpen){
			this.isOpen = true;
			$(this.el).fadeIn(500);
		
			if(this.draggable){
				$(this.el).draggable({ handle: "p.handle" });
			}
		
			if(this.scrollingTextWidget){
				this.scrollingTextWidget.refresh();
			}
		}else{
			this.isOpen = false;
			this.reset();
		}
	},
	reset: function(){
		$(this.el).fadeOut(500);
		this.isOpen = false;
	}
});