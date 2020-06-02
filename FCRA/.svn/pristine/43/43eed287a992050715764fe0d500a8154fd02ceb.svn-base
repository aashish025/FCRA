
$(document).ready(function (){
	initializeRedFlagDonorsTable();
	$("#red-flag-donors").validationEngine({promptPosition: 'bottomRight'});
	$("#delete-remark-form").validationEngine({promptPosition: 'bottomRight'});
});
function initializeRedFlagDonorsTable() {
	$('#headingforredyellowflag').show();
	if($('#roleIdAddGrant').val()==null || $('#roleIdAddGrant').val()=="" ){
		$('#bar-notify').html('');
		$("#bar-notify").html('<div class="alert alert-info"><strong>Info: </strong>You are not authorized to perform any action. You can only view details.</div>');
	}
	$("#red-flag-donors-table").html('');
	$("#red-flag-donors-table").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-list-red-flag-donors',
    		defaultsortcolumn:'statusDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				/*{title:'Association Id', name:'assoId'}, */
				{title:'Donor Name', name:'donorName',sortable:true}, 
				{title:'Country', name:'donorCountryName',sortable:true},
				{title:'Category', name:'categoryDesc'},
				{title:'Remark', name:'remarks'},
				{title:'Red/Yellow Flagged By', name:'actionBy'},
				{title:'Red/Yellow Flagged On', name:'statusDate',sortable:true},
				{title:'Flag Type', name:'categoryType'}
			],
			onRowSelect:function(rowdata){getRedFlagNgoDetailsTemp(rowdata)}
    		});	
}

function  getRedFlagNgoDetailsTemp(rowdata){
	$('#headingforredyellowflag').hide();
	$('#donorId').val(rowdata.donorId);//hidden field
	$('#donorName').val(rowdata.donorName);
	$('#rbiCircularIssueDate').val(rowdata.rbiCircularIssueDate);
	$('#donorCountry').val(rowdata.donorCountry);
	$('#originator_office').val(rowdata.originatorOffice);
	$('#originatorOrderNo').val(rowdata.originatorOrderNo);
	$('#originatorOrderDate').val(rowdata.originatorOrderDate);
	$('#rbiCircularIssueDate').val(rowdata.rbiCircularIssueDate);
	$('#categoryDesc').val(rowdata.categoryCode);
	$('#remarkOriginatorOffice').val(rowdata.remarks);
			
	$('#donorName').prop('readonly', true);
	$('#rbiCircularIssueDate').prop('readonly', true);
	$('#donorCountry').attr("disabled", true); 
	$('#originator_office').prop('readonly', true);
	$('#originatorOrderNo').prop('readonly', true);
	$('#originatorOrderDate').prop('readonly', true);
	$('#rbiCircularIssueDate').prop('readonly', true);
	$('#categoryDesc').attr("disabled", true); 
	$('#remarkOriginatorOffice').prop('readonly', true);
	
	$('#remarkOriginatorOffice').val(rowdata.remarks);
	if(($('#roleIdDeleteGrant').val()==null || $('#roleIdDeleteGrant').val()=="" ) && ($('#roleYellowFlagRemove').val()!=true)){
		$('#bar-notify').html('');
		$("#bar-notify").html('<div class="alert alert-info"><strong>Info: </strong>You are not authorized to perform any action. You can only view details.</div>');
	}
	 populateGetDetails(rowdata.categoryType);
	 
}

function populateGetDetails(categoryType){
	$('#red-flag-donors-table').hide();
	$('#table-btn').show();
	$('#form-div').show();
	$('#add-btn').hide();
	$('#add-details-btn').hide();
	if(categoryType=='Red'){
		$('#delete-actions').show();
		$('#delete-YellowFlag-donor').hide();
	}
	else if(categoryType=='Yellow'){
		$('#delete-YellowFlag-donor').show();
		$('#delete-actions').hide();
	}
	
}
function initForm(){
	$('#donorName').prop('readonly', false);
	$('#rbiCircularIssueDate').prop('readonly', false);
	$('#donorCountry').attr("disabled", false); 
	$('#originator_office').prop('readonly', false);
	$('#originatorOrderNo').prop('readonly', false);
	$('#originatorOrderDate').prop('readonly', false);
	$('#rbiCircularIssueDate').prop('readonly', false);
	$('#categoryDesc').attr("disabled", false); 
	$('#remarkOriginatorOffice').prop('readonly', false);
	$('#red-flag-donors').trigger("reset");
	$('#red-flag-donors-table').hide();
	$('#add-btn').hide();
	$('#delete-actions').hide();
	$('#table-btn').show();
	$("#form-div").show();	
	$('#add-details-btn').show();
	$('#delete-YellowFlag-donor').hide();
	$('#headingforredyellowflag').hide();

}
function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#red-flag-donors-table').show();
	$("#form-div").hide();	
	$('#add-btn').show();
	$( "#officeTypeDiv" ).show();
	$( "#officeCodeDiv" ).show();
	$( "#officeSigntorydiv" ).show();
	$( "#officeCodeDiv-edit" ).hide();
	$('#delete-YellowFlag-donor').hide();
	$('#delete-actions').hide();
	$('#headingforredyellowflag').show();
	
}
//function addOffice(){	
//	var url='add-office-details';
//	$("#office-form").attr('action', url);
//	$('#office-form').submit();
//}
function addRedFlagDonors(flagValue)
{
	$('#bar-notify').html('');
	$('#sticky-notify').html('');
	if($('#red-flag-donors').validationEngine('validate'))
		{
		var params = 'donorName='+$('#donorName').val()+'&rbiCircularIssueDate='+$('#rbiCircularIssueDate').val()
		+'&donorCountry='+$('#donorCountry').val()+'&originatorOffice='+$('#originator_office').val()+'&originatorOrderNo='+$('#originatorOrderNo').val()+'&originatorOrderDate='+$('#originatorOrderDate').val()
		+'&categoryDesc='+$('#categoryDesc').val()+'&remarkOriginatorOffice='+$('#remarkOriginatorOffice').val()+'&requestToken='+$('#requestToken').val()+'&flagValue='+flagValue;
		var action = 'add-red-flag-donors';
		$.ajax({
			url: action,
			method:'GET',
			data: params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				initializeRedFlagDonorsTable();			
				initForm();
				showTable();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		$('#red-flag-donors').trigger("reset");
		 clearForm();
		}

	
}
function deleteRedFlagDonors(){
$('#bar-notify').html('');
$('#sticky-notify').html('');
	if($('#delete-remark-form').validationEngine('validate') )
	{
	var params = 'donorId='+$('#delete-donorId').val()+'&deloriginatorOffice='+$('#delete-originator_office').val()+'&deloriginatorOrderNo='+$('#delete-originatorOrderNo').val()+'&deloriginatorOrderDate='+$('#delete-originatorOrderDate').val()
	+'&delremarkOriginatorOffice='+$('#delete-remarkOriginatorOffice').val()+'&requestToken='+$('#requestToken').val()+'&flagdelete='+$('#flagdelete').val();
	var action = 'delete-red-flag-donors';
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
			$('#delete-remark-details-modal').modal('hide');
			initializeRedFlagDonorsTable();
			showTable();						
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	$('#red-flag-donors').trigger("reset");
	}
	
}
function deleteRemarkModelDonors(flagdelete){
	if($('#red-flag-donors').validationEngine('validate') ){
		$('#delete-remark-form').trigger("reset");
		 $('#delete-donorId').val($('#donorId').val());
		 $('#flagdelete').val(flagdelete);//hidden field 
			$("#delete-remark-details-modal").modal({
				show : true
			});
	}
   
}
function RemoveYellowaddModel(){
	if($('#red-flag-donors').validationEngine('validate') ){
		$('#add-remark-form').trigger("reset");
		 $('#add-donorId').val($('#donorId').val());
			$("#add-remark-details-modal").modal({
				show : true
			});
	}
   
}
function RemoveYellowaddRedFlagDonors(flagaddred){
	$('#bar-notify').html('');
	$('#sticky-notify').html('');
		if($('#add-remark-form').validationEngine('validate') )
		{
		var params = 'donorId='+$('#add-donorId').val()+'&rbiCircularIssueDate='+$('#rbiCircularIssueDateadd').val()
		+'&originatorOffice='+$('#originator_officeadd').val()+'&originatorOrderNo='+$('#originatorOrderNoadd').val()+'&originatorOrderDate='+$('#originatorOrderDateadd').val()
		+'&categoryDesc='+$('#categoryDescadd').val()+'&remarkOriginatorOffice='+$('#remarkOriginatorOfficeadd').val()+'&requestToken='+$('#requestToken').val()+'&flagaddred='+flagaddred;
		var action = 'add-red-remove-yellow-red-flag-donors';
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
				$('#add-remark-details-modal').modal('hide');
				initializeRedFlagDonorsTable();
				showTable();						
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		$('#red-flag-donors').trigger("reset");
		}
		
	}
