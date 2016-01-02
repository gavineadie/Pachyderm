var WidgetScrollableText = Backbone.View.extend({
	tagName: 'div',
	className: 'vscrollable',
	xmlObj: null,
	scrollID: null,
	text: '',
	scrollObj: null,
	initialize: function(){		
		
		this.scrollID = null;
		this.text = '';
		this.scrollObj = null;
		
		this.text = this.prepareText(this.options.text);
		this.scrollID = 'scroll-text-' + pachyderm.get_nonce();
		
		this.render();
	},
	render: function(){
		$(this.el).addClass(this.options.scrollClass);
		$(this.el).html('<div class="text-field" id="' + this.scrollID + '"><div class="scroll-content">' + this.text + '</div></div>');
		
		return this;
	},
	refresh: function(){
		var that = this;
		
		if(!this.scrollObj){
			this.scrollObj = new iScroll(this.scrollID,{hScroll: false, vScrollbar: true, hideScrollbar: false});
			setTimeout(function () {
				that.scrollObj.refresh();
			}, 50);
		}
		
		this.scrollObj.refresh();
		this.scrollObj.scrollTo(0,0,0);
	},
	prepareText: function(content){
		var nl = /\n/g;
		
		if(content){
			// iPad finds more \n's than on desktop. trip it down manually
			content = content.replace(nl, '<br>');
			content = content.replace(/<br><br><br><br><br>/g, '<br><br>');
			content = content.replace(/<br><br><br><br>/g, '<br><br>');
			content = content.replace(/<br><br><br>/g, '<br><br>');
			
			return content;
		}else{
			return '';
		}
	}
});