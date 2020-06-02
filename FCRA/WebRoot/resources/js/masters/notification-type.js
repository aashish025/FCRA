$(document).ready(function(){	
	hideAll();	
	$("#add-notification-type").on('click', addNotificationType);
	$("#cancel-btn").on('click', cancelNotificationAdd);
	initializeNotificationTypeList();
});

function initializeNotificationTypeList() {	
	$("#notification-type-list").html('');
	$("#notification-type-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-notifications-list-notifications-type-notification-type-details',
    		columndetails:
			[
				{title:'Notification Type ID', name:'notificationType',sortable:true}, 
				{title:'Notification Name', name:'notificationName', sortable:true}, 
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			defaultsortcolumn:'createdDate',
			defaultsortorder:'desc',
			onRowSelect:function(rowdata){getNotificationType(rowdata.notificationType);}
    		});	
}

function populateGetDetails() {	
	$('#notification-type-search').hide();
	$('#add-notification-type').hide();
	$('#add-notification-type-form').hide();
	$('#form-div').show();	
	$('#form-div2').show();	
	$('#edit-actions').show();	
}

function getNotificationType(notificationType) {
	var params = 'notificationType='+notificationType;
	var action = 'get-notification-type-notification-type-details';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item) {				
				$('#notificationNameForEdit').val(item.li);
				$('#notificationIdForEdit').val(item.ld);
				populateGetDetails();
				$('#add-notification-type').hide();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}

function toggleSubmit(value) {	
	if(value == 'add') {
		$('#submit-btn').show();
		$('#edit-btn').hide();
	}else {
		$('#submit-btn').hide();
		$('#edit-btn').show();		
	}
}

function addNotificationType() {	
	toggleSubmit('add');
	showAddForm();
}

function showAddForm() {	
	$("#add-notification-type-form").show();
}

function hideAddForm() {
	$("#add-notification-type-form").hide();
}

function resetForm() {
	clearForm('#add-notification-type-form');
}

function cancelNotificationAdd() {
	resetForm();
	hideAll();
	hideAddForm();
}

function deleteNotificationType() {
	$('#bar-notify').html('');
	var params = 'notificationTypeId='+$('#notificationIdForEdit').val()+'&requestToken='+$('#requestToken').val();	
	var action = 'delete-notification-Type-notification-type-details';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){		
			//notifyList(JSON.parse(data),'');		
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val(token);
			
			initializeNotificationTypeList();
			$('#form-div').hide();	
			$('#add-notification-type').show();
		},
		error: function(textStatus,errorThrown){
			alert('error');		
			alert(errorThrown.val);
		}
	});	
}

function validateNotificationType() {	
	if($('#add-notification-type-form').validationEngine('validate')) {
		$('#add-notification-type-form').submit();
	}
}

function validateEditNotificationType() {	
	if($('#notification-form').validationEngine('validate')) {
		$('#notification-form').submit();
	}
}

function hideValidationMsg() {
	$('#add-notification-type-form').validationEngine('hide');
}