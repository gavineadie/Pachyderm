var WidgetPreloader = Backbone.View.extend({
	tagName: 'span',
	className: 'preloader',
	initialize: function(){
		this.render();
	},
	render: function(){
		$(this.el).html('<div class="spinner"><div class="bar1 bar"></div><div class="bar2 bar"></div><div class="bar3 bar"></div><div class="bar4 bar"></div><div class="bar5 bar"></div><div class="bar6 bar"></div><div class="bar7 bar"></div><div class="bar8 bar"></div><div class="bar9 bar"></div><div class="bar10 bar"></div><div class="bar11 bar"></div><div class="bar12 bar"></div></div>Loading...');
		return this;
	}
});