
$(document).ready(function (){
	$("#notification-form").validationEngine({promptPosition: 'bottomRight'});
	initializeNotificationList();
});
function initializeNotificationList() {
	$("#notification-list").html('');
	$("#notification-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-notification-list-notification-details',
    		columndetails:
			[
				{title:'Notification Id', name:'notificationId',sortable:true}, 
				{title:'Notification Type', name:'notificationTypeId', sortable:true}, 
				{title:'Title', name:'messageTitle'},
				{title:'Details', name:'messageDetails'},
				{title:'Entered On', name:'enteredOn',sortable:true}
			],
			onRowSelect:function(rowdata){getNotificationDetails(rowdata.notificationId)}
    		});	
}

function getNotificationDetails(notificationId){
	var params = 'notificationId='+notificationId;
	var action = 'get-notification-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){
				$('#notificationType').val(item.p1);
				$('#notificationTitle').val(item.p2);
				$('#notificationDetails').val(item.p3);
				populateGetDetails();
			});			
			if(data[1].length!=2)
				populateAttachmentList(JSON.parse(data[1]));
			$('#notificationId').val(notificationId);			
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
}
function editNotification(){
	var params = 'notificationId='+$('#notificationId').val()+'&notificationType='+$('#notificationType').val()
				+'&notificationTitle='+$('#notificationTitle').val()+'&notificationDetail='+$('#notificationDetails').val();
	var action = 'edit-notification-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			initializeNotificationList();
			showTable();							
			$(document).find('div[up-file-list="rc-upload"]').html('');	
			prepareFileList("rc-upload");
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
}
function deleteNotification(){
	var params = 'notificationId='+$('#notificationId').val();
	var action = 'delete-notification-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			initializeNotificationList();
			showTable();						
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
}
function populateAttachmentList(attachmentList){	
	$('#attachment-list').html('');
	if(attachmentList != ''){
		var content='<label class="text-success">Available Attachments: </label><small><table class="table table-condensed table-bordered"></small>';
		$.each(attachmentList,function(index,item){		
			content+='<tr class="active"><td><a href="javascript:getNotificationAttachment(\''+item.li+'\');">Document '+(++index)+'</a></td>'+
					'<td><button type="button" onclick="javascript:deleteAttachment(\''+item.li+'\');" class="btn btn-default btn-sm">'+
					'<span class="glyphicon glyphicon-trash"></span></button></td></tr>';
		});
		content+='</table>'
		$('#attachment-list').html(content);
	}
}
function populateGetDetails(){
	$('#notification-list').hide();
	$('#table-btn').show();
	$('#edit-actions').show();
	$('#form-div').show();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
}
function getNotificationAttachment(rowId){
	var url='get-notification-attachment-home';
	$("#attachment-download-form").attr('action', url);
	$("#attachment-download-form #rowId").val(rowId);
	$("#attachment-download-form").submit();
} 
function deleteAttachment(rowId){				
		$.ajax({	    	
	        url: 'delete-available-attachment-notification-details?rowId='+rowId+'&notificationId='+$('#notificationId').val(),  			//Server script to process data
	        type: 'POST', 
	        xhr: function() {  												// Custom XMLHttpRequest
	            var myXhr = $.ajaxSettings.xhr();            
	            return myXhr;
	        },    
	        beforeSend: function (e){},										//Ajax events
	        success: function (data){	        	
	        	populateAttachmentList(JSON.parse(data));
	        },
	        error: function (json){},                																
	        cache: false,													//Options to tell jQuery not to process data or worry about content-type.
	        contentType: false,
	        processData: false
	    });	
}
function initForm(){
	$('#notification-list').hide();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').show();
	$("#form-div").show();	
	$('#add-details-btn').show();
}
function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#notification-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
}
function addNotification(){	
	var url='add-notification-details';
	$("#notification-form").attr('action', url);
	if($('#notification-form').validationEngine('validate')){
		$('#notification-form').submit();
	}
}
function openFile(){
	$('#attachment').click();
}