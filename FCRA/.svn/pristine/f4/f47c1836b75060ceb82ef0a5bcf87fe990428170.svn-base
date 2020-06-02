var particles = false,
         particleDensity,			
         options = {
            effectWeight: 1,
            outerBuffer: 1.08,
            elementDepth: 220
        };
$(document).ready(function(){	
	//$("#distort").logosDistort(options);
	 if (particles) {
         particleDensity = window.outerWidth * 7.5;
         if (particleDensity < 13000) {
            particleDensity = 13000;
         } else if (particleDensity > 20000) {
            particleDensity = 20000;
         }
         return $('#particle-target').particleground({
            dotColor: '#1ec5ee',
            lineColor: '#0a4e90',
            density: particleDensity.toFixed(0),
            parallax: false
         });
      }
	if(proceed!="" || proceed==null){
		switchContent(proceed);
	}
	$('#reload').click(function(){
		window.location.href=window.location;
	});
	windowHeight=$(window).height();
	initializeDOMObjects();	
	toggleDrawer('show');
	initializeUserActions();
});
var tl=1000;
var init=0;
var istD;
var myD;
var $myIframe = null;
var myIframe = null;
var MutationObserver = null;
var windowHeight=null;
var user="User";
function initializeUserActions(){
	$('#user-icon').popover({
		container:"body",
		content:function(){
				return generateUserContent(this);
			},
		html:true,
		placement:'left',		
		trigger:'click focus',
		viewport:{ "selector": "#viewport", "padding": 1 }
		});
}
function generateUserContent(obj){	
	var userInfo="<span>"+userName+"</span> [ <span class=''>"+userId+"</span> ]"+", "+
				"<span class='text-info'>"+userDesignation+"</span><br/>" +				
				"<span class='text-danger' onclick='jacascript:initLogout();' style='cursor:pointer;'>Logout</span>";
	return userInfo; 
}
function initLogout(){
	var url='logout';
	$("#logout-form").attr('action', url);
	$('#logout-form').submit();
}
function initializeDOMObjects(){
	 var nav = $('#menu-bar');	 
	//$('#nav-drawer').css("height",$(window).height()-120);
	$(window).scroll(function () {		
		var t=0;
		var h=0;
		if($(this).scrollTop() > 70) {
			nav.addClass("f-nav");
			t=50;
			h=windowHeight-50;
		}else{
			//$('body').append($(this).scrollTop());
			nav.removeClass("f-nav");
			h=(windowHeight-120)+$(this).scrollTop();
			t=windowHeight-h;
//			$('body').append('HT - '+h+' - '+t);
		}
		//$('#nav-drawer').css("height",h);
		//$('#nav-drawer').css("top",t);
		$('#cssmenu').css("top",t);
	});
}
function updateMenuLocation(t,h){
	//$('#nav-drawer').css("height",h);
	//$('#nav-drawer').css("top",t);
	$('#cssmenu').css("top",t);
}
/*function toggleDrawer(sH){
	if(sH=='show'){
		$('#nav-drawer').show("slide", { direction: "left" }, 200, function(){
			$('#dummy').show();
			$('#content-frame').removeClass('col-xs-12 col-xs-10').addClass('col-xs-10');
		});
	}else if(sH=='hide'){
		$('#nav-drawer').hide("slide", { direction: "left" }, 200,function(){});
		$('#dummy').hide();
		$('#content-frame').removeClass('col-xs-10 col-xs-12').addClass('col-xs-12');
	}else{
		if($('#nav-drawer').is(":visible")){
				toggleDrawer('hide');
		}else{
			toggleDrawer('show');
		}
	}
}*/
function toggleDrawer(sH){
	if(sH=='show'){
		$('#cssmenu').show("slide", { direction: "left" }, 200, function(){
			$('#dummy').show();
			$('#content-frame').removeClass('col-xs-12 col-xs-10').addClass('col-xs-10');
		});
	}else if(sH=='hide'){
		$('#cssmenu').hide("slide", { direction: "left" }, 200,function(){});
		$('#dummy').hide();
		$('#content-frame').removeClass('col-xs-10 col-xs-12').addClass('col-xs-12');
	}else{
		if($('#cssmenu').is(":visible")){
				toggleDrawer('hide');
		}else{
			toggleDrawer('show');
		}
	}
}
function goHome(){	
	$('#home-form').submit();
}

function clock(){
	setTimeout('clocker()',tl);
}
function clocker(){
	initClock();
	istL=istL+tl;
	istD.setTime(istL);
	var istS=istD.getDate()+'-'+(istD.getMonth()+1)+'-'+istD.getFullYear()+' '+istD.getHours()+':'+istD.getMinutes()+':'+istD.getSeconds();
	$('#ist-time').html(istS);
	myL=myL+tl;
	myD.setTime(myL);
	var myS=myD.getDate()+'-'+(myD.getMonth()+1)+'-'+myD.getFullYear()+' '+myD.getHours()+':'+myD.getMinutes()+':'+myD.getSeconds();
	$('#my-time').html(myS);
	
	setTimeout('clocker()',tl);
}
function initClock(){
	if(init==0){
		init=1;
		var date=new Date();
		istL=istL+(date.getTimezoneOffset()*60000);
		myL=myL+(date.getTimezoneOffset()*60000);
		istD = new Date(istL);
		myD = new Date(myL);
		
	}
}
function toHome(){
	window.location.href="home";
}
function createModal(header,modalBody){
	$('.modal-header h3').html(header);
	$('.modal-body').html(modalBody);
	initModal();
}
function hideModal(){
	$(".modal").modal('hide');
}
function initModal(){
	$(".modal").modal(); // initialized with defaults
	$(".modal").modal({ keyboard: true }); // initialized with no keyboard
	$(".modal").modal('show');
}
function openLink(tUrl){
	window.open(tUrl,tUrl,'channelmode,resizable=no');
}