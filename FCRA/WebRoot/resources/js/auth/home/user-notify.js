function closeNotify(nn){
	$('#'+nn+'-n-list').slideUp(300);
}
function closeMe(p){
	$(p).remove();
}
var nt=0;
var nc='';
var nl='';
function calcTypeClass(nn){
	if(nn=='general'){
		nt=0;
		//nc='badge-warning';
		nl='';
	}
	else if(nn=='referral'){
		nt=4;
		//nc='badge-important';5,130; 80,48
		nl='<span style="position:absolute; top:500px; left: 500px; z-index:2;" onclick="javascript:closeMe(\'.referral-label\');" class="referral-label label label-important"><b style="font-size:15px;">'+$('#'+nn+'-badge').html()+'</b> new referral communications have been received.<br>Kindly look into them and reply and process the cases soon.</span>';
	}
	else if(nn=='pts'){
		nt=5;	
		//nc='badge-success';35,500; 80,82
		nl='<span style="position:absolute; top:500px; left: 500px; z-index:2;" onclick="javascript:closeMe(\'.pts-label\');" class="pts-label label label-success"><b style="font-size:15px;">'+$('#'+nn+'-badge').html()+'</b> new replies have been found for PTS.<br>Kindly look into the said for Pakistani case(s).</span>';
	}
}
function toggleNotify(nn){
	if(!$('#'+nn+'-n-list').is(':visible')){
		calcTypeClass(nn);
		$('#'+nn+'-badge').html('0').removeClass(nc);
		closeMe('.'+nn+'-label');
		pullUserNotification(nn,'pull-user-notification-dochangePassword','notificationType='+nt);
	}
	$('#'+nn+'-n-list').slideToggle(300);
}
$(document).ready(function(){
	/*bounceUserNotification('general',1000);
	bounceUserNotification('referral',2000);
	bounceUserNotification('pts',3000);*/
});
function bounceUserNotification(nn,t){
	var cTime=new Date().getTime();
	calcTypeClass(nn);
	if(parseInt($('#'+nn+'-badge').html())>0){
		/*$('#'+nn+'-badge').effect("bounce", { times:4,distance:21}, 315);
		$('#'+nn+'-badge').effect("bounce", { times:4,distance:15,direction:"down" }, 315);*/
		$('body').append(nl);
		if(nn=='referral'){
			$('.'+nn+'-label').animate({left: '-=435', top: '-=433'}, 1000);
		}
		else if(nn=='pts'){
			$('.'+nn+'-label').animate({left: '-=403', top: '-=433'}, 1000);
		}
	}
	window.setTimeout('countUserNotification(\''+nn+'\')',t);
}
function countUserNotification(nn){
	calcTypeClass(nn);
	var params = 'notificationType='+nt;
	$.getJSON('count-user-notification-dochangePassword', params, function(data) {
		$('#'+nn+'-badge').html(data.userNotificationCount);
		calcTypeClass(nn);
		if(parseInt($('#'+nn+'-badge').html())>0){
			$('#'+nn+'-badge').addClass(nc);
		}
		else{
			$('#'+nn+'-badge').removeClass(nc);
		}
		window.setTimeout('bounceUserNotification(\''+nn+'\',1234567)',1000);
	});
}
function pullUserNotification(nn,action,params){
	//var params = '';
	$('#'+nn+'-n-list .notification-list').html('Loading...');
	$.getJSON(action, params, function(data) {
		if(data.userNotificationList.length==0){
			$('#'+nn+'-n-list .notification-list').html('No notifications this time.');
		}
		else{
			$.each(data.userNotificationList, function(index, item) {
				if(index==0){
					$('#'+nn+'-n-list .notification-list').html('');
				}
				var bg="read";
				if(item.seenFlag=="1"){
					bg="unseen";
				}
				else if(item.readFlag=="1"){
					bg="unread";
				}
				$('#'+nn+'-n-list .notification-list').append("<li id=\"notify-list-"+index+"\" class=\"notification-list-item "+bg+"\"><div><span class=\"message\">"+
					"<b>"+item.applicationId+"</b>: "+item.message+"</span></div><div><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10px;\"><tr>"+
					"<td style=\"width:50%; text-align: left;\">By: "+item.sourceUserid+"</td>"+
					"<td style=\"width:50%; text-align: right;\">At: "+item.notifiedDate+"</td>"+
					"</tr></table></div></li>");
				//var jNotifyMsg="<table style=\"max-width:550px;\"><tr><td>For Application Id <b>"+item.applicationId+"</b>, "+item.message+"<br><b>User Remark</b>: "+item.userRemark+"</td></tr><tr><td><b>Whom</b>: "+item.sourceUserid+" - "+item.sourceOfficeName+"("+item.sourceOfficeCode+")</td></tr><tr><td><b>When</b>:  "+item.notifiedDate+"</td></tr></table>";
				var jNotifyMsg="<table><tr><td>For Application Id <b>"+item.applicationId+"</b>, "+item.message+"<br><b>User Remark</b>: "+item.userRemark+"</td></tr><tr><td><b>Whom</b>: "+item.sourceUserid+" - "+item.sourceOfficeName+"("+item.sourceOfficeCode+")</td></tr><tr><td><b>When</b>:  "+item.notifiedDate+"</td></tr></table>";
				$('#notify-list-'+index).click(function(){
					fullUserNotification(jNotifyMsg,item.rowid,item.applicationId);
				});
			});
		}
	});
}
function fullUserNotification(jNotifyMsg,rowid,header){	
	var params='rowid='+rowid;
	$.getJSON('read-user-notification-dochangePassword', params, function(data){
		//createjNotify(jNotifyMsg);
		createModal(header,jNotifyMsg);
	});
}
