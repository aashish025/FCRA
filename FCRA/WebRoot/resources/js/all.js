$(document).ready(function(){
	if(!(window.name=="body-frame")){
		var u=window.location.href.split('/').pop();
		if(u.substring(0,6)!="popup-" && u.substring(0,8)!="limited-" && u.substring(0,4)!="home"){
			window.location.href="home?proceed="+u;
		}
	}
	else{
		window.history.pushState({path:parent.cp},'',parent.cp);
	}
});
function trackUnload(){
	window.onbeforeunload = function(){ parent.trackLoad(); }
}
function home(){
	var x=false;
	try{
		if(!x){
			parent.window.location="home";
			x=true;
		}
	}catch (e) {
	}
	try{
		if(!x){
			window.location="home";
			x=true;
		}
	}catch (e) {
	}
}