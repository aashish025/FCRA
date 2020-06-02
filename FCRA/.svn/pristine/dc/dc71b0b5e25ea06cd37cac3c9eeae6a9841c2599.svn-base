$(document).ready(function(){
	if(proceed!="" || proceed==null){
		switchContent(proceed);
	}
});	
function trackLoad(){
	$('#total-time').html('... s');
	var s=new StopWatch();
	s.start();
	$('#body-frame').one('load', function(){
		s.stop();
		$('#total-time').html(s.duration()+' s');
		/*var iframe = $('#body-frame').get(0);
		var script=document.createElement('script');
	    script.setAttribute('src','resources/js/all.js');
	    script.setAttribute('type','text/javascript');
	    $('#body-frame').contents().find('body').get(0).appendChild(script);*/
		try{
		    setTimeout(function(){
				$('#body-frame')[0].contentWindow.trackUnload();
			},500);
		}catch(e){
		}
	});
}
var cp='';
function switchContent(whichContent){
	/*var s=new StopWatch();
	s.start();*/
	if(cp==''){
		$('#home-div').hide();
		trackLoad();
	}
	cp=whichContent;
	
	// Set the page heading	
	var pageHeading = ($("#cssmenu > ul > li > ul > li > a#"+whichContent).text());
	if(pageHeading == '' || pageHeading == 'undefined')
		pageHeading = 'Home page';
	$("#pageHeading > span").text(pageHeading);
	
	
	var $iframe = $('#body-frame');
	if(cp.substring(0,7)=="unused-"){
		$iframe.contents().find('html').html('This page is out of service. <b>'+cp.substring(7)+'</b>');
		$('#total-time').html('0 s');
	}
	else{
		//window.history.pushState({path:'home?proceed=Loading...'},'','home?proceed=Loading...');
		window.history.pushState({path:'home?proceed='+cp},'','home?proceed='+cp);
		var ml=$(".navigation a[id='"+cp+"']");
		var oc=ml.attr('onclick');
		var h=ml.find('.text').html();
		//ml.attr('onclick','').find('.text').html('<b>Loading...</b>');
		$iframe.one('load', function(){
			ml.attr('onclick',oc).find('.text').html(h);
			//window.history.pushState({path:'home?proceed='+cp},'','home?proceed='+cp);
			$('#reload').unbind("click").click(function(){
				switchContent(cp);
			});
		});
		$iframe.parent().show();
		$iframe.attr('src',cp);
	}
}
function remove(id){
	$.getJSON('removePage-pagination','removeId='+id,function(data){
	});
}