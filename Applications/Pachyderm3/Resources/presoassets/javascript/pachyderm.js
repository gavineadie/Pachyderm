/**
*
*  Javascript trim, ltrim, rtrim
*  http://www.webtoolkit.info/
*
**/
 
function trim(str, chars) {
	return ltrim(rtrim(str, chars), chars);
}
 
function ltrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
}
 
function rtrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
}

function is_safari(){
	if(navigator.userAgent.indexOf("Safari") != -1) return 1;
	return 0;
}

function is_moz_one(){
	if (navigator.userAgent.indexOf("Mozilla/5.0") != -1 && navigator.userAgent.indexOf("rv:1.0") != -1) return 1;
	return 0;
}

function click_button(){
	if (is_safari()){
		hide_form();
		document.getElementById('progressActionForm').submit();
	}
	return true;
}

function submit_form(){
	if (!is_safari() && !is_moz_one()) hide_form();
	return true;
}

function hide_form(){
	//document.getElementById('progressActionInitiator').style.display = 'none';
	document.getElementById('progressDisplayDiv').style.display = 'block';
}

function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

/*--------- GOOGLE ANALYTICS CODE ADDED FOR CSU CDL ------------*/

/***A more elegant implementation of google analytics - by Al Stevens 20 July 2008***/


/* Simplifies onload, you will no longer have to add an onload event call just call addLoadEvent */
function addLoadEvent(func,arg){
	if (!arg){
		var oldonload = window.onload;
		if (typeof window.onload != 'function') {
			window.onload = func;
		} else {
			window.onload = function() {
				oldonload();
				func();
			}
		}
	} else { /*if the onload event has an argument/parameter cater for that*/
		if (arg){
			oldonload = window.onload;
			if (typeof window.onload != 'function') {
				window.onload = func(arg); 
			} else {
				window.onload = function() {
					oldonload();
					func(arg); 
				}
			}
		}
	}
}

function loadGAScript(){
	/*Check browser for Dom compatibility*/
	if (!document.getElementsByTagName) return false;

	/*Determines whether the page is using a secure or unsecure protocol*/
	var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");

	/*Writes in the script to the document head*/
	var gaScript = document.createElement("script");
	gaScript.setAttribute("src",gaJsHost +"google-analytics.com/ga.js");
	gaScript.setAttribute("type","text/javascript");
	var domHead = document.getElementsByTagName("head")[0]
	domHead.appendChild(gaScript);
}

/*Calls the analytics function*/
function callGA(){	
	var pageTracker = _gat._getTracker("UA-4969726-3");
	pageTracker._initData();
	pageTracker._trackPageview();
}

var ga_code=""; //Add your Google Analytics ID here.
if (ga_code != null && ga_code != ""){
	loadGAScript();
	addLoadEvent(callGA);
}

function fieldFocus(){
	if( document.getElementById( 'default_text_box' ) ) document.getElementById( 'default_text_box' ).focus();
}

addLoadEvent(fieldFocus);
addLoadEvent(finalizeCollapseExpand);

