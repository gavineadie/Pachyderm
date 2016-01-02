var WidgetTitle = Backbone.View.extend({
	tagName: 'div',
	className: 'title-container',
	initialize: function(){
		this.render();
	},
	render: function(){
		var $compoundTitle = $(this.options.xml).find('component[name="compoundTitle"]');
		if($compoundTitle.length){
			$compoundTitle = $($compoundTitle[0]);

			var title = $compoundTitle.children('data_element[name="title"]').text();
			var subtitle = $compoundTitle.children('data_element[name="subtitle"]').text();
			
			var markup = '<h1>' + title + '</h1>';
			
			if(subtitle){
				markup += '<h2>' + subtitle + '</h2>';
			}
			
			$(this.el).html(markup);
		}
		return this;
	}
});