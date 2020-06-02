$(document).ready(function (){
	initAdminList();	
});

function initAdminList() {
	$("#admin-list").html('');
	$("#admin-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-admin-user-management',
    		columndetails:
			[
				{title:'Office Name', name:'officeName', sortable:true}, 
				{title:'Office Type', name:'officeType', sortable:true},
				
			],
			onRowSelect:function(rowdata){getUser(rowdata);}
    		});
}

function getUser(rowdata){	
	$('#edit-actions').show();	
	$('#userid').val(rowdata.userid);
	$('#statusId').val(rowdata.statusId);
		if(rowdata.statusId=='3')
				$('#unlock-adm-btn').show();
		clearForm();
	$('#user-info-head-div').html('<div class="col-sm-4"><h3 class="text-info">'+rowdata.userid+'</h3></div>');
	$('#p-officeName').html(rowdata.officeName);
	$('#p-officeType').html(rowdata.officeType);
	$('#p-countryCode').html(rowdata.countryName);
	$('#table-btn').show();
	$('#user-info-div').show();
	$('#admin-list').hide();
	$('#bar-notify').html('');
}

function resetPassword(){
	$('#sticky-notify').html('');
			$('#bar-notify').html('');	
	var params = 'userid='+$('#userid').val()+'&requestToken='+$('#requestToken').val();
	var action = 'reset-password-admin-user-management';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){	
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val(token);
			initAdminList();
			clearForm();
					},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
	showTable();
}

function unlockAdmin(){
	$('#sticky-notify').html('');
		$('#bar-notify').html('');	
	var params = 'userid='+$('#userid').val()+'&requestToken='+$('#requestToken').val();
	var action = 'unlock-admin-user-management';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val(token);
			initAdminList();
			clearForm();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
	$('#unlock-adm-btn').hide();
}

function showTable(){
	$('#table-btn').hide();
	$('#user-info-div').hide();
	$('#admin-list').show();
	$('#edit-actions').hide();
	$('#edit-user-div').hide();
	$('#create-user-div').show();
	$('#fresh-role-save').show();
	$('#edit-role-save').hide();
	$('#bar-notify').html('');	
	clearForm();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
		$(this).val('');
	});
}