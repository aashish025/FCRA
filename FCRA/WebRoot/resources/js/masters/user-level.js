$(document).ready(function (){
	initializeUserList();
	$("#user-type-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeUserList() {
	$("#User-list").html('');
	$("#User-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-user-list-details-user-level-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Level Id', name:'userlevelid',sortable:true}, 
				{title:'Level Name', name:'userlevelName', sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getUserDetails(rowdata);}
    		});	

}
function getUserDetails(rowdata){
	
	$('#userTypeId').val(rowdata.userlevelid);
	$('#userTypeName').val(rowdata.userlevelName);
	$('#enteredOn').val(rowdata.createdDate);
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
	if($('#user-type-form').validationEngine('validate'))
		{
		var params = 'userlavelName='+$('#userTypeName').val();
		var action = 'add-user-type-details-user-level-details';
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

function editUsertype()
{	
	$('#bar-notify').html('');
	if($('#user-type-form').validationEngine('validate'))
	{
	var params = 'userlavelName='+$('#userTypeName').val()+'&userlavelId='+$('#userTypeId').val();
	var action = 'edit-userlavel-details-user-level-details';
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

function deleteUsertype()
{
	$('#bar-notify').html('');
	if($('#user-type-form').validationEngine('validate'))
	{
	var params = 'userlavelName='+$('#userTypeName').val()+'&userlavelId='+$('#userTypeId').val();
	var action = 'delete-userlavel-details-user-level-details';
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
