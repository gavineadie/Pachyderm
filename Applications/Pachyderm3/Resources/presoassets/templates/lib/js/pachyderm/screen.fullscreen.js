var FullScreenSWF = Backbone.View.extend({
	screenContainer: null,
	tagName: 'section',
	className: 'fullscreen screen',
	xmlName: null,
	xml: null,
	contentXML: null,
	t: null,
	swfID: null,
	swfFileName: null,
	preloader: null,
	initialize: function(options) {
		
		this.contentXML = null;
		this.t = null;
		this.swfID = null;
		this.swfFileName = null;
		this.preloader = null;
		
		// cache for later
		this.t = $(this.el);
    this.xmlName = options.xmlName;

		this.preloader = new WidgetPreloader();
		this.t.append(this.preloader.el);
		
		this.t.append('<div class="screen-container fullscreen"></div>');
		pachyderm.screenContainer.append(this.el);
		
		this.screenContainer = this.$(".screen-container");
		this.screenContainer.hide();
		
		var that = this;
		this.model.fetch({'error': function(m,r){that.onXML(r.responseText);}});
    },
	onXML: function(x){
		this.xml = x;
		this.contentXML = $(this.xml).find('content');
		var $component = $(this.xml).find('content component[type="pachyderm.fullscreen_swf"]');
		
		this.swfFileName = pachyderm.parse_asset_file_name($component.children('data_element[name="image"]').text());
		var xOffset = $component.children('data_element[name="xoffset"]').text();
		var yOffset = $component.children('data_element[name="yoffset"]').text();

		this.swfID = 'fullscreen_' + pachyderm.get_nonce();
		this.$(".screen-container").append('<div class="fullscreen-swf-container" style="left: ' + xOffset + 'px; top: ' + yOffset + 'px;"><div id="' + this.swfID + '">Flash Required</div></div>');
		
		this.render();
	},
	render: function(){
		pachyderm.shell.update(this.contentXML);
		
		this.$(".preloader").fadeOut(500,function(){$(this).remove()});
		this.$(".screen-container").fadeIn(500);
		
		var flashvars = {};
		var params = {};
			params.quality = "high";
			params.scale = "default";
		var attributes = {};
			attributes.id = this.swfID;
			
		swfobject.embedSWF(this.swfFileName, this.swfID, "100%", "100%", "8.0.0", "expressInstall.swf", flashvars, params, attributes);
		
		return this;
	},
	reset: function(){
		this.$(".fullscreen-swf-container").html('<div id="' + this.swfID + '">Flash Required</div>');
	}
});