$(document).ready(function (){
	initializeUserStatusList();
	$("#users").validationEngine({promptPosition: 'bottomReft'});
});

function initializeUserStatusList() {
	$("#userstatus-list").html('');
	$("#userstatus-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-UserSt-type-user-status-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'User Status Id', name:'actionId',sortable:true},
			
				{title:'User Status Name', name:'actionName', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getUserStatus(rowdata);}
    		});	
	clearForm();
}

function getUserStatus(rowdata){
$('#actionId').val(rowdata.actionId);
$('#actionName').val(rowdata.actionName);
$('#createdDate').val(rowdata.createdDate);
$('#userstatus-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();
}

function populateGetDetails(){
	$('#userstatus-list').show();
	$('#table-btn').hide();
	$('#edit-actions').show();
	$('#form-div').hide();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
}

function initForm5(){
	$('#userstatus-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();
}

function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#userstatus-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});

}

function showaAllTable(){
	$('#userstatus-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editUserSt(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'actionId='+$('#actionId').val()+'&actionName='+$('#actionName').val()+'&requestToken='+$('#requestToken').val();
	var action = 'edit-UserSt-details-user-status-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val(token);
			initializeUserStatusList();
			showTable();
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
	showaAllTable();
}

function deleteUserSt(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'actionId='+$('#actionId').val()+'&requestToken='+$('#requestToken').val();
	var action = 'delete-UserSt-details-user-status-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val(token);
			initializeUserStatusList();
			showTable();						
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
	showaAllTable();
}

function addUserSt(){
	$('#sticky-notify').html('');
	if($('#users').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='actionName='+$('#actionName').val()+'&requestToken='+$('#requestToken').val();
		$.ajax({
			url: 'add-UserSt-details-user-status-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				initializeUserStatusList();
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		clearForm()
		showaAllTable();
	}
}