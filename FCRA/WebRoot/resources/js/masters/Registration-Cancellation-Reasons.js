$(document).ready(function (){
	
	initializeRegistrationCancellationReasonsList();
	$("#RegistrationCancellation").validationEngine({promptPosition: 'bottomReft'});
});

function initializeRegistrationCancellationReasonsList() {
	
	$("#regiCanclReasons-list").html('');
	$("#regiCanclReasons-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-registration-cancellation-reasons',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Id', name:'reasonId',sortable:true},
			
				{title:' Cancellation Reasons', name:'reasonDesc', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getUserStatus(rowdata);}
    		});	
	clearForm();
}

function getUserStatus(rowdata){
$('#reasonId').val(rowdata.reasonId);
$('#reasonDesc').val(rowdata.reasonDesc);
$('#createdDate').val(rowdata.createdDate);
$('#regiCanclReasons-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();
$("#code").hide();

$('#add-details-btn').hide();
}

/*function populateGetDetails(){
	$('#RegiCanclReasons-list').show();
	$('#table-btn').hide();
	$('#edit-actions').show();
	$('#form-div').hide();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
}*/

function initFormReasons(){
	$('#regiCanclReasons-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();
	$("#code").show();
	
}

function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#regiCanclReasons-list').show();
	$("#form-div").hide();	
	$("#code").hide();	
	$('#add-btn').show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});

}

function showaAllTable(){
	$('#regiCanclReasons-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editCancelReasons(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'reasonId='+$('#reasonId').val()+'&reasonDesc='+$('#reasonDesc').val()+'&requestToken='+$('#requestToken').val();
	var action = 'edit-registration-cancellation-reasons';
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
			initializeRegistrationCancellationReasonsList();
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

function deleteCancelReasons(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'reasonId='+$('#reasonId').val()+'&requestToken='+$('#requestToken').val();
	var action = 'delete-registration-cancellation-reasons';
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
			initializeRegistrationCancellationReasonsList();
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

function addCancelReasons(){
	$('#sticky-notify').html('');
	if($('#RegistrationCancellation').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='reasonDesc='+$('#reasonDesc').val()+'&reasonId='+$('#reasonId').val()+'&requestToken='+$('#requestToken').val();
		
		$.ajax({
			url: 'add-registration-cancellation-reasons',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				initializeRegistrationCancellationReasonsList();
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
