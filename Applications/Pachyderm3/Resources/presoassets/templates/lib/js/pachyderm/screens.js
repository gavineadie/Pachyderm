var Screen = Backbone.Model.extend({
	id: null,
	template: null,
	xml: null
});

var PachydermPageModel = Backbone.Model.extend({
	url: function(){
		return this.get('xmlName') + ".xml";
	}
});

var PachydermWidgetModel = Backbone.Model.extend({
	url: function(){
		return this.get('xmlFile');
	}
});

var PachydermPresentation = Backbone.Collection.extend({
	model: Screen
});