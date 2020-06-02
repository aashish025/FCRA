var stickyId=0;
var spinner='data:image/gif;base64,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';
jQuery(document).ready(function(){
	/*if(!(window.name=="body-frame")){
		var u=window.location.href.split('/').pop();
		if(u.substring(0,6)!="popup-" && u.substring(0,8)!="limited-"){
			//window.location.href="home?proceed="+u;
		}
	}
	else{
		window.history.pushState({path:parent.cp},'',parent.cp);
	}*/
	initIframeResizer();
	initChangeSearch();
	initMultipleComboCheck();
	initBehaveAs();
	initLimitText();
	initConfirmBox();
	initToolTip();
	initPaging();
	initRefresh();
	initTableSorter();
	
	var cid='gar';
	//var thead=new Array("Application Id","Applicant Name","Surname","Nationality","Passport No.","Passport Expiry","Visa No.","Visa Expiry","Visa Desc","Service(s)","Service No.","Service Date","Expiry Date");
	/*var thead=[
		        {"text":"Application Id","css":""},
		        {"text":"Applicant Name","css":"width:115px"},
		        {"text":"Surname","css":"width:115px"},
		        {"text":"Nationality","css":"width:125px"},
		        {"text":"Passport No.","css":""},
		        {"text":"Passport Expiry","css":""},
		        {"text":"Visa No.","css":""},
		        {"text":"Visa Expiry","css":""},
		        {"text":"Visa Desc","css":""},
		        {"text":"Service(s)","css":""},
		        {"text":"Service No.","css":""},
		        {"text":"Service Date","css":""},
		        {"text":"Expiry Date","css":""}
		      ];
	$('#'+cid).append(
		$('<small/>',{}).append(
			$('<table/>',{
				'class': 'table table-bordered table-condensed', 
				css: {width: '1250px !important','overflow-x': 'auto'}
			}).append(
				$('<thead/>',{id: 'thead-'+cid})
			).append(
				$('<tbody/>',{id: 'tbody-'+cid})
			)
		)
	);
	$.each(thead,function(i, item){
		$('#thead-'+cid).append($('<th/>',{text: item.text, style: item.css}));
	});*/
	/*for(var i in thead){
		$('#thead-'+cid).append($('<th/>',{text: thead[i]}));
	}*/
	
	/*indc('minibutton');
	indc('classy');*/
	/*$('.submitbutton').each(function(){
		$(this).click(function(){
			$('.submitbutton').each(function(){$(this).hide();});
		});
	});*/
});
$.extend({
	'pullJSON' : function(action, params, callF){
		try{
			$.ajax({
				url: action+'?'+params,
				dataType:'json',
				success: function(data){
					if(typeof callF == 'function'){
						callF.call(this, data);
					}
				},
				error: function(textStatus,errorThrown){
					stickyNotify('Oops!','Unable to connect. Try logging in again..','w','uc',0);
					try{ropld();}catch(e){}
					try{roplr();}catch(e1){}
				}
			});
		}catch(e){
			stickyNotify('Error!','Unable to process. Try logging in again.','e','uc',0);
			try{ropld();}catch(e){}
			try{roplr();}catch(e1){}
		}
	}
});
function initHide(){
	hideAll('','.hide');
}
function initShow(){
	showAll('','.show');
}
function initToolTip(){
	$('.title-t').each(function(){$(this).tooltip({placement: 'top', html: true});});
	$('.title-b').each(function(){$(this).tooltip({placement: 'bottom', html: true});});
	$('.title-l').each(function(){$(this).tooltip({placement: 'left', html: true});});
	$('.title-r').each(function(){$(this).tooltip({placement: 'right', html: true});});
	
	$('.mtitle-t').each(function(){$(this).tooltip({placement: 'top', html: true, trigger: 'manual'});});
	$('.mtitle-b').each(function(){$(this).tooltip({placement: 'bottom', html: true, trigger: 'manual'});});
	$('.mtitle-l').each(function(){$(this).tooltip({placement: 'left', html: true, trigger: 'manual'});});
	$('.mtitle-r').each(function(){$(this).tooltip({placement: 'right', html: true, trigger: 'manual'});});
}
function hideAll(t,p){
	if(t=="obj"){
		p.each(function(){$(this).hide();});
	}
	else{
		$(p).each(function(){$(this).hide();});
	}
}
function showAll(t,p){
	if(t=="obj"){
		p.each(function(){$(this).show();});
	}
	else{
		$(p).each(function(){$(this).show();});
	}
}
function initLimitText(){
	$(document).find('[limit-text]').each(function(){
		$(this).addClass('validate[maxSize['+$(this).attr('limit-text')+']]');
		/*var p=$(this).position();
		$('body').append("<small style=\"position:absolute; top: "+p.top+"px; left: "+p.left+"px;\"><span class=\"help-inline\"><font class=\"\" title=\"Text Limit\"><b id=\"c"+this.id+"\">"+$(this).attr('limit-text')+"</b> of "+$(this).attr('limit-text')+"</font></span></small>");*/
		/*var a=$('<small/>',{}).append($('span/>',{'class':'help-inline'}).append($('<font/>',{title: 'Text Limit'}).append($('<b/>',{id:'c'+this.id,text:$(this).attr('limit-text')})).append(' of '+$(this).attr('limit-text'))));
		$(this).after($('#ih-'+this.id).html());*/
		$(this).after("<small><span class=\"help-inline\"><font class=\"\" title=\"Text Limit\"><b id=\"c"+this.id+"\">"+$(this).attr('limit-text')+"</b> of "+$(this).attr('limit-text')+"</font></span></small>");
		$(this).keyup(function(){limitText(this.id,'c'+this.id,$(this).attr('limit-text'));});
		$(this).keydown(function(){limitText(this.id,'c'+this.id,$(this).attr('limit-text'));});
	});
}
function limitText(limitField, countField, limit) {
	var lf=$('#'+limitField).get(0);
	var cf=$('#'+countField).get(0);
	var l=$('#'+limit).get(0);
	if (lf.value.length > limit) {
		cf.parentNode.className=cf.parentNode.className+' text-error';
		cf.parentNode.title="Limit Exceeded";
		//lf.value = lf.value.substring(0, limit);
	} else {
		cf.innerHTML = limit - lf.value.length;
		cf.parentNode.className='';
		cf.parentNode.title="Text Limit";
	}
}
function indc(ndcClass){
	$('.'+ndcClass).each(function(){
		//alert($(this).attr('onclick')+" - "+$(this).attr('href'));
		if($(this).attr('onclick')==null){
			if($(this).attr('href')!=null){
				$(this).attr('onclick',$(this).attr('href'))
				$(this).removeAttr('href');
			}
		}
		$(this).click(function(){
			//$('body').append('-A:CLICK-');
			$(this).addClass('ndc-'+$(this).attr('onclick'));
			$(this).attr('onclick','');
		});
	});
}
/*function showAllButtons(){
	$('.submitbutton').each(function(){$(this).show();});
}*/
function sab(){
	//window.setTimeout('showAllButtons()',200);
	//showAllButtons();
}
function showAllButtons(){
	//$('body').append('-S:CLICK-');
	$('.minibutton').each(function(){
		var ndcClass = $(this).attr('class').split(" ");
		for(var i in ndcClass) {
			ndcClass[i]=ndcClass[i].replace(/\s+/g, "");
			if(ndcClass[i].substring(0, 4) == "ndc-"){
				$(this).attr('onclick',ndcClass[i].substring(4,ndcClass[i].length));
				$(this).removeClass(ndcClass[i]);
				break;
			}
		}
	});
	$('.classy').each(function(){
		var ndcClass = $(this).attr('class').split(" ");
		for(var i in ndcClass) {
			ndcClass[i]=ndcClass[i].replace(/\s+/g, "");
			if(ndcClass[i].substring(0, 4) == "ndc-"){
				$(this).attr('onclick',ndcClass[i].substring(4,ndcClass[i].length));
				$(this).removeClass(ndcClass[i]);
				break;
			}
		}
	});
}
function openLink(tUrl){
	window.open(tUrl,tUrl,'width=1400,height=700,top=100,scrollbars=yes');
}
function viewFullApplicantDetails(applicationId,page){
	var tUrl='limited-applicant-details-tracking-dochangePassword?applicationId='+applicationId+'&limitedDetailsFlag=ppVAaaM';
	//window.open(tUrl,page+'-'+applicationId,'channelmode=yes,resizable=no,scrollbars=yes');
	window.open(tUrl,page+'-'+applicationId,'width=1400,height=700,top=100,scrollbars=yes');
}
function closePopover(id){
	$('#'+id).popover('hide');
}
function openPopover(id){
	$('#'+id).popover('show');
}
function closeAlert(id){
	$('#'+id).alert('close');
}
function closeNotify(id){
	$('#'+id).remove();
}
function openAlert(id){
	$('#'+id).alert();
}
function alarmS(func,time){
	window.setTimeout(func,time);
}
function alarm(func,params,time){
	var p = params.split(",");
	func+="(";
	for(var i in p) {
		if(i==0){
			func+="'"+p[i]+"'";
		}
		else{
			func+=","+"'"+p[i]+"'";
		}
	}
	func+=")";
	window.setTimeout(func,time);
}
function notifyList(notificationList){	
	$.each(notificationList,function(index,item){
		var status;
		var type;
		var location;
		var closeable;
		var custom;
		var head = item.h;
		var desc = item.d;
		var time = item.ms;
		switch(item.s){
			case 'i':
				status='info';
				break;
			case 'e':
				status='error';
				break;
			case 'w':
				status='warning';
				break;
			case 's':
				status='success';
				break;
		}
		switch(item.t){
			case 'b':
				type='bar';
				break;
			case 's':
				type='sticky';
				break;
			case 't':
				type='text';
				break;
			case 'n':
				type='none';
				break;
			default:
				alert("Error in data-notifications recieved from server");
		}
		switch(item.l){
			case 'd':
				location=type+"-notify";
				custom=0;
				if(type=='none')
					location='bar-notify';
				break;
			default:
				custom=1;
				location=item.l;
				break;
		}
		switch(item.c){
			case 'c':
				closeable=true;
				break;
			case 'uc':
				closeable=false;
				break;
		}
		if(custom==0)
			notify(head,desc,status,type,closeable,time);
		else
			notifyCustom(head,desc,status,type,closeable,time,location);
	});
}
function notify(head,desc,status,type,closeable,time){
	var s;
	var c;
	var t;
	if(typeof closeable !== "undefined") {
		if(closeable==true||closeable==false){
			c=closeable;
		}
		else{
			if(parseInt(closeable)!=='NaN'){
				t=parseInt(closeable);
			}
		}
	}
	else{
		c=true;
	}
	if(typeof time !== "undefined") {
		t=time;
	}
	else{
		t=0;
	}
	if(type=='bar')
		s=barNotify(head,desc,status,'bar-notify',c,t);
	else if(type=='sticky')
		s=stickyNotify(head,desc,status,'sticky-notify',c,t);
	else if(type=='text')
		s=textNotify(head,desc,status,'text-notify',c,t);
	else if(type=='none')
		s=barNotify(head,desc,status,'bar-notify',c,t);
	return s;
}
function notifyCustom(head,desc,status,type,closeable,time,location){
	var s;
	var c;
	var t;
	if(type=='bar')
		s=barNotify(head,desc,status,location,c,t);
	else if(type=='sticky')
		s=stickyNotify(head,desc,status,location,c,t);
	else if(type=='text')
		s=textNotify(head,desc,status,location,c,t);
	else if(type=='none')
		s=barNotify(head,desc,status,location,c,t);
	return s;
}
var barId=0;
function barNotify(head,desc,status,location,closeable,time){
	barId=barId+1;
	var id='bar-'+barId;
	var alert=alertNotification(head,desc,status,closeable,id);
	showNotification(location,alert);
	if(time>0){
		alarm('closeNotify',id,time);
	}
}
var stickyId=0;
function stickyNotify(head,desc,status,location,closeable,time){
	stickyId=stickyId+1;
	var id='sticky-'+barId;
	var alert=alertNotification(head,desc,status,closeable,id);
	showNotification(location,alert);
	if(time>0){
		alarm('closeNotify',id,time);
	}
}
var textId=0;
function textNotify(head,desc,status,location,closeable,time){
	textId=textId+1;
	var id='text-'+barId;
	var alert=textNotification(head,desc,status,closeable,id);
	showNotification(location,alert);
	if(time>0){
		alarm('closeNotify',id,time);
	}
}
function alertNotification(head,desc,status,closeable,id){
	var alertMsg="";
	alertMsg='<div id="'+id+'" class="alert ';
	switch(status){
		case 'error':
			alertMsg+=' alert-danger';
			break;
		case 'success':
			alertMsg+=' alert-success';
			break;
		case 'warning':
			alertMsg+=' alert-warning';
			break;
		case 'info':
			alertMsg+=' alert-info';
			break;
		case 'muted':
			alertMsg+=' alert-info';
			break;
		default:
			break;
	}
	alertMsg+='"><strong>'+head+'</strong>&nbsp;'+desc;
	if(closeable==true){
		alertMsg+="<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>";
	}
	alertMsg+="<br/></div>";
	return alertMsg;
}
function textNotification(head,desc,status,closeable,id){
	var alertMsg="";
	alertMsg='<div id="'+id+'" class="';
	switch(status){
		case 'error':
			alertMsg+=' text-danger';
			break;
		case 'success':
			alertMsg+=' text-success';
			break;
		case 'warning':
			alertMsg+=' text-warning';
			break;
		case 'info':
			alertMsg+=' text-info';
			break;
		case 'muted':
			alertMsg+=' text-muted';
			break;
		default:
			break;
	}
	alertMsg+='"><strong>'+head+'</strong>&nbsp;'+desc;
	if(closeable==true){
		alertMsg+="<button onclick=\"javascript:closeNotify('"+id+"');\" class=\"close\" type=\"button\">&times;</button>";
	}
	alertMsg+="<br/></div>";
	return alertMsg;
}
function showBarNotification(location,alert){
	showNotification(location,alert);
}
function showStickyNotification(location,alert){
	showNotification(location,alert);
}
function showTextNotification(location,alert){
	showNotification(location,alert);
}
function showNotification(location,alert){
	$('#'+location).append(alert);
	var position=$('#'+location).offset();
	$("html, body").animate({ scrollTop: position.top }, "slow");
}
function initConfirmBox(){
	$(document).find('[confirm-box]').each(function(){
		var oc=$(this).attr('onclick');
		$(this).removeAttr('onclick');
		var bc="<p><small>Are you sure of this?</small>&nbsp;"+
		"<button class=\"btn btn-sm btn-success title-t\" title=\"Yes\" type=\"button\" id=\""+this.id+"-ok\" ><i class=\"glyphicon glyphicon-ok\"></i></button>&nbsp;&nbsp;"+
		"<button class=\"btn btn-sm btn-danger pull-right title-t\" title=\"No\" type=\"button\" onclick=\"javascript:closePopover('"+this.id+"');\" id=\""+this.id+"-cancel\"><i class=\"glyphicon glyphicon-remove\"></i></button></p>";
		$(this).popover({placement: $(this).attr('confirm-box'),html: true, content: bc});
		var okid=this.id+"-ok";
		var okp=this.id;
		$(this).click(function(){
			initToolTip();
			$('#'+okid).click(function(){
				closePopover(okp);
				//$('#'+okp).button('loading');
				alarmS(oc,200);
			});
		});
	});
}
function generatePaging(s,e,id,pl){	
	var pc="<div id=\"page-list-"+id+"\"  style=\"margin:0 0 10px 0;\">"+
    "<ul class=\"pagination\">"+
    "<li><a style=\"cursor: pointer;\" onclick=\"javascript:refreshPaging('"+id+"',"+s+")\"><b>&laquo;</b></a></li>";
	var i=s;
	for(i=s;i<e;i++){
		if(i<=pl){
			pc+="<li><a id=\"p"+i+"\" style=\"cursor: pointer;\" onclick=\"javascript:pullPage('"+id+"',"+i+")\">"+i+"</a></li>";
		}
	}
	var l=i-1;
	if(l>pl){
		l=pl;
	}
    pc+="<li><a style=\"cursor: pointer;\" onclick=\"javascript:refreshPaging('"+id+"',"+l+")\"><b>&raquo;</b></a></li>"+
    "</ul>"+
    "</div>";
    $('#'+id).html(pc).attr('sp',s).attr('ep',(e-1));
    /*$('#'+id).html(pc);
    $('#'+id).attr('sp',s).attr('ep',(e-1));*/   
}
function refreshPaging(id,i){	
	var pl=parseInt($('#'+id).attr('paging-limit'));
	var p=parseInt($('#'+id).attr('paging'));
	var s=0;
	var e=0;
	if(i==$('#'+id).attr('sp')){
		if(i!=1){
			s=i-(p-2);
			e=i+2;
			generatePaging(s,e,id,pl);
		}
	}
	else if(i==$('#'+id).attr('ep')){
		if(i!=pl){
			s=i-1;
			e=(i-1)+p;
			generatePaging(s,e,id,pl);
		}
	}
	$('#'+id).find('a[id="'+$('#'+id).attr('cp')+'"]').closest('li').addClass('active');
}
function pullPage(id,i){	
	var theme='';
	if($('#'+id).attr('theme')){
		theme=$('#'+id).attr('theme');
	}
	else{
		theme='dynamic';
	}
	
	var cp=$('#'+id).attr('cp');
	var pf=$('#'+id).attr('paging-for');
	var vr=parseInt($('#'+id).attr('paging'));
	refreshPaging(id,i);
	$('#'+id).find('a[id="'+cp+'"]').closest('li').removeClass('active');
	$('#'+id).attr('cp','p'+i).find('a[id="p'+i+'"]').closest('li').addClass('active');
	$('#'+id).attr('cp','p'+i);
	$('#'+id).find('a[id="p'+i+'"]').closest('li').addClass('active');	
	if(theme=='static'){
		$('#'+pf+' .srow').hide();
		i=parseInt(i);
		var er=i*vr;
		var sr=er-(vr-1);
		var x=sr;
		for(x=sr;x<=er;x++){			
			$('#'+pf+' table tbody tr:nth-child('+x+')').show();
		}
	}
	else{
		$('#'+pf).html('<small>Loading...</small>');
		/*refreshPaging(id,i);
		$('#'+id).find('a[id="'+$('#'+id).attr('cp')+'"]').closest('li').removeClass('active');
		$('#'+id).attr('cp','p'+i).find('a[id="p'+i+'"]').closest('li').addClass('active');
		$('#'+id).attr('cp','p'+i);
		$('#'+id).find('a[id="p'+i+'"]').closest('li').addClass('active');*/
		alarm('pullPage'+id,id+','+i+','+pf,200);
	}	
}
function initRefresh(){
	$(document).find('[refresh]').each(function(){
		$(this).click(function(){
			forAllRefreshPage($(this).attr('refresh'));
		});
	});
}
function forAllRefreshPage(refreshId){
	var page=$('#'+refreshId).attr('cp');
	page=page.split("p");
	pullPage(refreshId,page[1]);
}
function initPaging(){
	$(document).find('[paging]').each(function(){
		doPaging($(this).attr('id'));
		/*var np=parseInt($(this).attr('paging'));
		generatePaging(1,(np+1),this.id,$(this).attr('paging-limit'));
		$(this).attr('cp','p1').attr('sp','1').attr('ep',np).find('a[id="p1"]').click().closest('li').addClass('active');*/
	});
}
function doPaging(id){
	$('#'+id).each(function(){
		var np=parseInt($(this).attr('paging'));
		generatePaging(1,(np+1),this.id,$(this).attr('paging-limit'));
		$(this).attr('cp','p1').attr('sp','1').attr('ep',np).find('a[id="p1"]').click().closest('li').addClass('active');
		//doTableSorter($(this).attr('paging-for'));
	});
}
function sleep(milliseconds) {
	var start = new Date().getTime();
	for (var i = 0; i < 1e7; i++) {
		if ((new Date().getTime() - start) > milliseconds){
			break;
		}
	}
}
function validateCheckBox(group){
	var c=$('[group="'+group+'"]').attr('count');
	if(c<=0){
		return false;
	}
	else{
		return true;
	}
}
function checkBoxValue(group){
	var f=$('[group="'+group+'"]').attr('form');
	var n=$('[group="'+group+'"]').attr('name');
	var x='';
	$('#'+f).find('input[name="'+n+'"]').each(function(index,item){
		if(index==0)
			x=$(this).val();
		else
			x+=','+$(this).val();
	});
	return x;
}
function doCheck(id){
	var p=$('[group="'+$('#'+id).parent().attr('checkbox-group')+'"]');
	var form=p.attr('form');
	var name=p.attr('name');
	var theme=p.attr('theme');
	$('#'+id).removeClass('btn-link').addClass('btn-success').removeClass('btn-small').addClass('btn-mini').attr('title','Unmark').find('i').removeClass('icon-plus').addClass('icon-ok').addClass('icon-white');
	if(theme=="full"){
		$('#l-'+id).removeClass('btn-link').addClass('btn-success').removeClass('btn-small').addClass('btn-mini');
	}
	$('<input>').attr({
	    type: 'hidden',
	    id: 'h-'+id,
	    name: name,
	    value: $('#'+id).attr('value')
	}).appendTo('#'+form);
	p.attr('count',(parseInt(p.attr('count'))+1));
}
function doUncheck(id){
	var p=$('[group="'+$('#'+id).parent().attr('checkbox-group')+'"]');
	var theme=p.attr('theme');
	$('#'+id).removeClass('btn-success').addClass('btn-link').removeClass('btn-mini').addClass('btn-small').attr('title','Mark').find('i').removeClass('icon-ok').addClass('icon-plus').removeClass('icon-white');
	if(theme=="full"){
		$('#l-'+id).removeClass('btn-success').addClass('btn-link').removeClass('btn-mini').addClass('btn-small');
	}
	$('#h-'+id).remove();
	p.attr('count',(parseInt(p.attr('count'))-1));
}
function clickCheckBox(id,onclick){
	$('#'+id).parent().fadeOut();
	if ($(".btn-link[id="+id+"]")[0]){
		doCheck(id);
	}
	else{
		doUncheck(id);
	}
	$('#'+id).parent().fadeIn();
	if(onclick!=""){
		alarm(onclick,id,200);
	}
}
function clickCheckBoxLabel(id,onclick){
	if(onclick==""){
		clickCheckBox(id,onclick);
	}
	else{
		alarm(onclick,'l-'+id,200);
	}
}
function initCheckBox(){
	$(document).find('[behave-as="checkbox"]').each(function(){
		var group=$(this).attr('group');
		var theme="";
		var themeClass="";
		if($(this).attr('theme')){
			if($(this).attr('theme')=="dual"){
				theme="dual";
				themeClass="";
			}
			else{
				theme="full";
				themeClass="btn-group";
			}
		}
		else{
			theme="full";
			themeClass="btn-group";
		}
		$(this).attr('theme',theme);
		var labelClick="";
		var lTitle="";
		if($(this).attr('label-click')){
			labelClick=$(this).attr('label-click');
			lTitle="View Details";
		}
		else{
			labelClick="";
			lTitle="Toggle";
		}
		var checkboxClick="";
		if($(this).attr('checkbox-click'))
			checkboxClick=$(this).attr('checkbox-click');
		else
			checkboxClick="";
		$(this).attr('count',0);
		$(document).find('[checkbox-group="'+group+'"]').each(function(){
			var label=$(this).attr('label');
			var value=$(this).attr('value');
			var id=$(this).attr('id');
			$(this).after(
				$('<div/>',{'class':themeClass,'checkbox-group':group}).append(
					$('<button/>',{style:'padding-left:3px;padding-right:3px;', id:'c-'+id, title:'Mark', click:function(){clickCheckBox('c-'+id,checkboxClick);}, 'class':'btn btn-small btn-link', type:'button', value: value}).append(
						$('<i/>',{'class':'icon-plus'})
					)
				).append(
					$('<button/>',{style:'padding-left:3px;padding-right:3px;', id:'l-c-'+id, title:lTitle, click:function(){clickCheckBoxLabel('c-'+id,labelClick);}, 'class':'btn btn-small btn-link', type:'button', value: value}).append(
						$('<b/>',{html:label})
					)
				)
			);
			$(this).remove();
		});
	});
}
function validateRadioButton(group){
	var c=$('[group="'+group+'"]').attr('current');
	//alert($('#s2').html()+'-'+$("[group='s1']").html()+'-'+group);
	if(c==''){
		return false;
	}
	else{
		return true;
	}
}
function radioButtonValue(group){
	var f=$('[group="'+group+'"]').attr('form');
	var n=$('[group="'+group+'"]').attr('name');
	return $('#'+f).find('input[name="'+n+'"]').val();
	
}
function clickRadioButton(id,onclick){
	$('#'+id).parent().fadeOut();
	var p=$('[group="'+$('#'+id).parent().attr('radiobutton-group')+'"]');
	var name=p.attr('name');
	var theme=p.attr('theme');
	if ($(".btn-link[id="+id+"]")[0]){
		$('#'+id).removeClass('btn-link').addClass('btn-success').removeClass('btn-small').addClass('btn-mini').attr('title','Unmark').find('i').removeClass('icon-plus').addClass('icon-ok').addClass('icon-white');
		if(theme=="full"){
			$('#l-'+id).removeClass('btn-link').addClass('btn-success').removeClass('btn-small').addClass('btn-mini');
		}
		$('#h-'+name).attr('value',$('#'+id).attr('value'));
		$('#'+p.attr('current')).removeClass('btn-success').addClass('btn-link').removeClass('btn-mini').addClass('btn-small').attr('title','Mark').find('i').removeClass('icon-ok').addClass('icon-plus').removeClass('icon-white');
		if(theme=="full"){
			$('#l-'+p.attr('current')).removeClass('btn-success').addClass('btn-link').removeClass('btn-mini').addClass('btn-small');
		}
		p.attr('current',id);
		//alert(p.attr('current'));
	}
	else{
		$('#'+id).removeClass('btn-success').addClass('btn-link').removeClass('btn-mini').addClass('btn-small').attr('title','Mark').find('i').removeClass('icon-ok').addClass('icon-plus').removeClass('icon-white');
		if(theme=="full"){
			$('#l-'+id).removeClass('btn-success').addClass('btn-link').removeClass('btn-mini').addClass('btn-small');
		}
		$('#h-'+name).attr('value','');
		p.attr('current','');
	}
	$('#'+id).parent().fadeIn();
	if(onclick!=""){
		alarm(onclick,id,200);
	}
}
function clickRadioButtonLabel(id,onclick){
	if(onclick==""){
		clickRadioButton(id,onclick);
	}
	else{
		alarm(onclick,'l-'+id,200);
	}
}
function initRadioButton(){
	$(document).find('[behave-as="radiobutton"]').each(function(){
		var group=$(this).attr('group');
		var form=$(this).attr('form');
		var name=$(this).attr('name');
		var theme="";
		var themeClass="";
		if($(this).attr('theme')){
			if($(this).attr('theme')=="dual"){
				theme="dual";
				themeClass="";
			}
			else{
				theme="full";
				themeClass="btn-group";
			}
		}
		else{
			theme="full";
			themeClass="btn-group";
		}
		$(this).attr('theme',theme);
		var labelClick="";
		var lTitle="";
		if($(this).attr('label-click')){
			labelClick=$(this).attr('label-click');
			lTitle="View Details";
		}
		else{
			labelClick="";
			lTitle="Toggle";
		}
		var radiobuttonClick="";
		if($(this).attr('radiobutton-click'))
			radiobuttonClick=$(this).attr('radiobutton-click');
		else
			radiobuttonClick="";
		//$(this).attr('count',0);
		$(this).attr('current','');
		$('<input>').attr({
		    type: 'hidden',
		    id: 'h-'+name,
		    name: name,
		    value: ''
		}).appendTo('#'+form);
		$(document).find('[radiobutton-group="'+group+'"]').each(function(){
			var label=$(this).attr('label');
			var value=$(this).attr('value');
			var id=$(this).attr('id');
			$(this).after(
				$('<div/>',{'class':themeClass,'radiobutton-group':group}).append(
					$('<button/>',{style:'padding-left:3px;padding-right:3px;', id:'r-'+id, title:'Mark', click:function(){clickRadioButton('r-'+id,radiobuttonClick);}, 'class':'btn btn-small btn-link', type:'button', value: value}).append(
						$('<i/>',{'class':'icon-plus'})
					)
				).append(
					$('<button/>',{style:'padding-left:3px;padding-right:3px;', id:'l-r-'+id, title:lTitle, click:function(){clickRadioButtonLabel('r-'+id,labelClick);}, 'class':'btn btn-small btn-link', type:'button', value: value}).append(
						$('<b/>',{html:label})
					)
				)
			);
			$(this).remove();
		});
	});
}
function initBehaveAs(){
	$(document).find('[behave-as]').each(function(){
		if($(this).attr('behave-as')=="checkbox"){
			initCheckBox();
		}
		else if($(this).attr('behave-as')=="radiobutton"){
			initRadioButton();
		}
	});
}
function initMultipleComboCheck(){
	$('select[multiple="multiple"]').each(function(){
		doMultipleComboCheck(this.id);
	});
}
function doMultipleComboCheck(id){
	$('#'+id).change(function(){
		var multivalue=$(this).val() || [];
		var value=multivalue[0];
		if(value=='ALL' || value=='all' || value=='All' ){
			$(this).removeAttr("multiple");	
		}
		else{
			$(this).attr('multiple','multiple');
		}
	});
}
function initChangeSearch(){
	$('[search-toggler]').each(function(index,item){
		var id=$(this).attr('search-toggler');
		var l=0;
		var f=0;
		$('['+id+']').each(function(index,item){
			var cs=parseInt($(this).attr(id));
			if(index==0){
				f=cs;
			}
			else{
				if(cs<f){
					f=cs;
				}
			}
			if(cs>l){
				l=cs;
			}
			$(this).hide();
		});
		$(this).addClass('title-b').attr('title','<b>Next</b>: '+$('['+id+'="'+(f+1)+'"]').attr('placeholder')).attr('f',f).attr('l',l).attr('current',f).attr('href','javascript:changeSearch("'+id+'");');
		$('['+id+'="'+f+'"]').show();
		/*window.setTimeout(function(){
			$('['+id+'="'+f+'"]').focus();
			},
		1000);*/
		//setTimeout(function(){$('['+id+'="'+f+'"]').tooltip('show');},1000);
		if($('#'+$(this).attr('search-cache')).val()!=""){
			var sf=parseInt($('#'+$(this).attr('search-cache')).val());
			var c=parseInt($(this).attr('current'));
			$('['+id+']').each(function(index,item){
				if($(this).attr(id)!=sf){
					$(this).val('');
				}
			});
			if(sf!=c){
				alarm('formatChangeSearch',id+','+c+','+sf,500);
				window.setTimeout(function(){
					$('['+id+'="'+sf+'"]').focus();
					},
				1000);
			}
			else{
				setTimeout(function(){$('['+id+'="'+f+'"]').tooltip('show').focus();},1000);
			}
		}
		else{
			setTimeout(function(){$('['+id+'="'+f+'"]').tooltip('show').focus();},1000);
		}
	});
}
function changeSearch(id){
	//var id="cs";
	//var c=parseInt($('#'+id).attr('current'));
	var st=$('[search-toggler="'+id+'"]');
	var f=parseInt(st.attr('f'));
	var l=parseInt(st.attr('l'));
	var c=parseInt(st.attr('current'));
	var next=f;
	if(c<l){
		next=(c+1);
	}
	else if(c==l){
		next=f;
	}
	formatChangeSearch(id,c,next);
}
function formatChangeSearch(id,c,next){
	//var id="cs";
	var st=$('[search-toggler="'+id+'"]');
	var f=parseInt(st.attr('f'));
	var l=parseInt(st.attr('l'));
	var title="Nothing to display";
	c=parseInt(c);
	next=parseInt(next);
	if(c<l){
		if(next<l){
			title=$('['+id+'="'+(next+1)+'"]').attr('placeholder');
		}
		else if(next==l){
			title=$('['+id+'="'+f+'"]').attr('placeholder');
		}
	}
	else if(c==l){
		title=$('['+id+'="'+(f+1)+'"]').attr('placeholder');
	}
	$('#'+st.attr('search-cache')).val(next);
	st.attr('data-original-title','<b>Next</b>: '+title);
	$('['+id+'="'+c+'"]').val('').tooltip('hide').hide();
	$('['+id+'="'+next+'"]').show().tooltip('show');
	st.attr('current',next).tooltip('hide').tooltip('show').closest('form').validationEngine('hide');
}
function parseDate(d) {
	var dateParts = d.split('-');
	if(dateParts==d)
		dateParts = d.split('/');
	return new Date(dateParts[2], (dateParts[1] - 1) ,dateParts[0]);
}
function dateRange(d1,d2){
	var one_day=1000*60*60*24;
	d1=parseDate(d1);
	d2=parseDate(d2);
	return Math.round((d1.getTime()-d2.getTime())/one_day);
}
/*function trackUnload(){
	window.onbeforeunload = function(){ parent.trackLoad(); }
}*/
var overlap=0;
function overlapObject(x,z){
	overlap++;
	$('body').append($('<div/>',{id:'overlap-'+overlap,css:{width:x.width(),height:x.height(),position:'absolute',left:x.offset().left,top:x.offset().top,'z-index':z,background:'rgba(255,255,255,0.5)'}}));
	return 'overlap-'+overlap;
}
function overlapObjectLoad(x,z){
	overlap++;
	$('body').append($('<div/>',{id:'overlap-'+overlap,css:{width:x.width(),height:x.height(),position:'absolute',left:x.offset().left,top:x.offset().top,'z-index':z,background:'rgba(255,255,255,0.5)'}}).append($('<span/>',{css:{position:'relative',left:'50%',top:'50%'}}).append($('<img/>',{src:spinner,width:'30px',height:'30px'}))));
	return 'overlap-'+overlap;
}
function overlapPage(z){
	overlap++;
	$('body').append($('<div/>',{id:'overlap-'+overlap,css:{width:$(document).width(),height:$(document).outerHeight(),position:'absolute',left:'0',top:'0','z-index':z,background:'rgba(255,255,255,0.4)'}}));
	return 'overlap-'+overlap;
}
function overlapPageLoad(z){
	overlap++;
	$('body').append($('<div/>',{id:'overlap-'+overlap,css:{width:$(document).width(),height:$(document).outerHeight(),position:'absolute',left:'0',top:'0','z-index':z,background:'rgba(255,255,255,0.9)'}}).append($('<span/>',{css:{position:'relative',left:'50%',top:'44%'}}).append($('<img/>',{src:spinner,width:'60px',height:'60px'}))));
	//$('body').append($('<div/>',{id:'overlap-'+overlap,css:{width:$(document).width(),height:$(document).outerHeight(),position:'absolute',left:'0',top:'0','z-index':z,background:'rgba(255,255,255,0.5)'}}).append($('<span/>',{css:{position:'relative',left:'50%',top:'2px'}}).append($('<img/>',{src:spinner,width:'45px',height:'45px'}))));
	return 'overlap-'+overlap;
}
function overlap(a){
	if(a=='overlap'){
		$(this).append($('<div/>',{id:'overlap-'+this.id,css:{width:this.width(),height:this.height(),position:'relative',left:this.offset().left,top:this.offset().top,'z-index':z,background:'rgba(255,255,255,0.5)'}}));
	}
	else if(a=='remove'){
		$('#overlap-'+this.id).remove();
	}
}
var opldv='';
function opld(z){
	opldv=overlapPageLoad(z);
	disable('body');
	return opldv;
}
function ropld(){
	enable('body');
	$('#'+opldv).remove();
}
function ropldId(id){
	enable('body');
	$('#'+id).remove();
}
var oplrv='';
function oplr(z){
	oplrv=overlapPageLoad(z);
	readOnly('body');
	return oplrv;
}
function roplr(){
	readWrite('body');
	$('#'+oplrv).remove();
}
function roplrId(id){
	readWrite('body');
	$('#'+id).remove();
}
function disable(id){
	$('#'+id+' select').each(function(){$(this).attr('disabled','disabled');});
	$('#'+id+' input').each(function(){$(this).attr('disabled','disabled');});
	$('#'+id+' textarea').each(function(){$(this).attr('disabled','disabled');});
	$('#'+id+' button').each(function(){$(this).attr('disabled','disabled');});
}
function enable(id){
	$('#'+id+' select').each(function(){$(this).removeAttr('disabled');});
	$('#'+id+' input').each(function(){$(this).removeAttr('disabled');});
	$('#'+id+' textarea').each(function(){$(this).removeAttr('disabled');});
	$('#'+id+' button').each(function(){$(this).removeAttr('disabled');});
}
function readOnly(id){
	$('#'+id+' select').each(function(){$(this).attr('readonly','readonly');});
	$('#'+id+' input').each(function(){$(this).attr('readonly','readonly');});
	$('#'+id+' textarea').each(function(){$(this).attr('readonly','readonly');});
	$('#'+id+' button').each(function(){$(this).attr('readonly','readonly');});
}
function readWrite(id){
	$('#'+id+' select').each(function(){$(this).removeAttr('readonly');});
	$('#'+id+' input').each(function(){$(this).removeAttr('readonly');});
	$('#'+id+' textarea').each(function(){$(this).removeAttr('readonly');});
	$('#'+id+' button').each(function(){$(this).removeAttr('readonly');});
}
function scrollUpTo(id){
  id = id.replace("link", "");
  $('html,body').animate({
      scrollTop: $("#"+id).offset().top},
      'slow');
}
function printList(dataList,cid,cols,spacing){
	$('#'+cid).append($('<small/>',{}).append($('<table/>',{width:'100%','cellspacing':spacing+'px','cellpadding':spacing+'px'}).append($('<tbody/>',{id: cid+'-tbody'}))));
	var c=0;
	var r=0;
	$.each(dataList, function(index, item) {
		if(c==0 || c==cols){
			r+=1;
			c=0;
			$('#'+cid+'-tbody').append($('<tr/>',{id:cid+'-r-'+r}));
		}
		$('#'+cid+'-r-'+r).append($('<td/>',{}).append($('<b/>',{html: item.li+': '})).append(item.ld));
		c+=1;
	});
}
function initTableSorter(){
	/*$('table').each(function(index,item){
		doPaging($(this).id);
	});*/
}
function doTableSorter(id){
	var pagerOptions = {		    
		    ajaxUrl: null,
		    customAjaxUrl: function(table, url) { return url; },  
		    ajaxProcessing: function(ajax){
		      if (ajax && ajax.hasOwnProperty('data')) {		       
		        return [ ajax.total_rows, ajax.data ];
		      }
		    },
		    output: '{startRow} to {endRow} ({totalRows})',		    
		    page: 0, 
		    size: 5
		  };
	$("#"+id)
    .tablesorter({    			
		sortReset      : true,
		sortRestart    : true
    });    
    //.tablesorterPager(pagerOptions);
}
function initIframeResizer(){
	
}
function clearForm(ele) {
	$(ele).find(':input').each(function() {
	    switch(this.type) {
	        case 'password':
	        case 'hidden':
	        case 'select-multiple':
	        case 'select-one':
	        case 'text':
	        case 'textarea':
	            $(this).val('');
	            break;
	        case 'checkbox':
	        case 'radio':
	            this.checked = false;
	            break;
	        case 'file':
	        	this.value = null;
	    }
	});
}