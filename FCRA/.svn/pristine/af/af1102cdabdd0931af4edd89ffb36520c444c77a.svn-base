$(document).ready(function (){
	initializeUserList();
	$("#file-status-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeUserList() {
	$("#User-list").html('');
	$("#User-list").bootgrid(
    		{
    
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-fileStatus-list-details-file-status-details',
    		defaultsortcolumn:'createdDate1',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'File Id', name:'filestatusId',sortable:true}, 
				{title:'File Name', name:'filestatusDesc', sortable:true}, 
			    {title:'Entered On', name:'createdDate1', sortable:true}
			],			
			onRowSelect:function(rowdata){getUserDetails(rowdata);}
    		});	

}
function getUserDetails(rowdata){
	
	$('#filestatusId').val(rowdata.filestatusId);
	$('#filestatusName').val(rowdata.filestatusDesc);
	$('#enteredOn').val(rowdata.createdDate1);
	$('#add-btn').hide();
	$('#details-div').show();
	$('#edit-actions').show();
	$('#add-details-btn').hide();
	
	}

function initForm(){	
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();

}
function showTable1(){
	$('#edit-actions').hide();
	$('#user-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
	
	}
function showadd()
{
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}


function clearForm(){	
	$(document).find('.clear').each(function (){
		$(this).val('');
	});
}
function adduserdetails()
{
	$('#bar-notify').html('');
	if($('#file-status-form').validationEngine('validate'))
		{
		var params = 'filestatusName='+$('#filestatusName').val();
		var action = 'add-filestatus-details-file-status-details';
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeUserList();
				initForm();
				showadd();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		 clearForm();
		
		
		}
	
}

function editfileStatus()
{	
	$('#bar-notify').html('');
	if($('#file-status-form').validationEngine('validate'))
	{
	var params = 'filestatusName='+$('#filestatusName').val()+'&filestatusId='+$('#filestatusId').val();
	var action = 'edit-filestatus-details-file-status-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeUserList();
			showadd();
			
			
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
			
		}
	});
	clearForm();
}
}
function deletefilestatus()
{
	$('#bar-notify').html('');
	if($('#file-status-form').validationEngine('validate'))
	{
	var params = 'filestatusName='+$('#filestatusName').val()+'&filestatusId='+$('#filestatusId').val();
	var action = 'delete-filestatus-details-file-status-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeUserList();
			showadd();
								
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
}
}
