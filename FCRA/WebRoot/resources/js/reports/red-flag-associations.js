
$(document).ready(function (){
	initializeRedFlagAssociationTable();
	$("#red-flag-associations").validationEngine({promptPosition: 'bottomRight'});
	$("#delete-remark-form").validationEngine({promptPosition: 'bottomRight'});
	$("#add-remark-form").validationEngine({promptPosition: 'bottomRight'});
});
function initializeRedFlagAssociationTable() {
	$('#headingforredyellowflag').show();
	if($('#roleIdAddGrant').val()==null || $('#roleIdAddGrant').val()=="" ){
		$('#bar-notify').html('');
		$("#bar-notify").html('<div class="alert alert-info"><strong>Info: </strong>You are not authorized to perform  action. You can only view details.</div>');
	}
	$("#red-flag-associations-table").html('');
	$("#red-flag-associations-table").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-list-red-flag-associations',
    		defaultsortcolumn:'statusDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				/*{title:'Association Id', name:'assoId'}, */
				{title:'Association Name', name:'assoName',sortable:true}, 
				{title:'Address', name:'assoAddress'},
				{title:'State', name:'assoStateName',sortable:true},
				{title:'Category', name:'categoryDesc'},
				{title:'Remark', name:'remarks'},
				{title:'Red/Yellow Flagged By', name:'actionBy'},
				{title:'Red/Yellow Flagged On', name:'statusDate',sortable:true },
				{title:'Flag Type', name:'categoryType'}
			],
			onRowSelect:function(rowdata){getRedFlagNgoDetailsTemp1(rowdata)}
    		});	
}

function  getRedFlagNgoDetailsTemp1(rowdata){

	$('#headingforredyellowflag').hide();
	$('#assoId').val(rowdata.assoId);//hidden field
	$('#assoName').val(rowdata.assoName);
	$('#assoAddress').val(rowdata.assoAddress);
	$('#assoState').val(rowdata.assoState);
	$('#originator_office').val(rowdata.originatorOffice);
	$('#originatorOrderNo').val(rowdata.originatorOrderNo);
	$('#originatorOrderDate').val(rowdata.originatorOrderDate);
	$('#categoryDesc').val(rowdata.categoryCode);
	$('#remarkOriginatorOffice').val(rowdata.remarks);
	$('#assoName').prop('readonly', true);
	$('#assoAddress').prop('readonly', true);
	//$('#assoState').prop('readonly', true);
	$('#assoState').attr("disabled", true); 
	$('#originator_office').prop('readonly', true);
	$('#originatorOrderNo').prop('readonly', true);
	$('#originatorOrderDate').prop('readonly', true);
	//$('#categoryDesc').prop('readonly', true);
	$('#categoryDesc').attr("disabled", true); 
	$('#remarkOriginatorOffice').prop('readonly', true);
	if(($('#roleIdDeleteGrant').val()==null || $('#roleIdDeleteGrant').val()=="" ) && ($('#roleYellowFlagRemove').val()!=true) ){
		$('#bar-notify').html('');
		$("#bar-notify").html('<div class="alert alert-info"><strong>Info: </strong>You are not authorized to perform any action. You can only view details.</div>');
	}
	//alert(rowdata.categoryType);
	 populateGetDetails1(rowdata.categoryType);
		$('#appid-search').hide();
}

function populateGetDetails1(categoryType){
	//alert(categoryType);
	$('#red-flag-associations-table').hide();
	$('#table-btn').show();
	$('#form-div').show();
	$('#add-btn').hide();
	$('#add-details-btn').hide();
	$('#delete-actions').hide();
	$('#removeYellowFlag-actions').hide();
	if(categoryType=='Red'){
		$('#delete-actions').show();
		$('#removeYellowFlag-actions').hide();
	}
	else if(categoryType=='Yellow'){
		$('#removeYellowFlag-actions').show();
		$('#delete-actions').hide();
	}
		
		
	
	
}
function initForm(){
	$('#assoName').prop('readonly', false);
	$('#assoAddress').prop('readonly', false);
	//$('#assoState').prop('readonly', true);
	$('#assoState').attr("disabled", false); 
	$('#originator_office').prop('readonly', false);
	$('#originatorOrderNo').prop('readonly', false);
	$('#originatorOrderDate').prop('readonly', false);
	//$('#categoryDesc').prop('readonly', true);
	$('#categoryDesc').attr("disabled", false); 
	$('#remarkOriginatorOffice').prop('readonly', false);
	$('#headingforredyellowflag').hide();
	$('#red-flag-associations').trigger("reset");
	$('#red-flag-associations-table').hide();
	$('#add-btn').hide();
	$('#delete-actions').hide();
	$('#table-btn').show();
	$("#form-div").show();	
	$('#add-details-btn').show();
	$('#removeYellowFlag-actions').hide();
	$('#appid-search').hide();

}
function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#red-flag-associations-table').show();
	$("#form-div").hide();	
	$('#add-btn').show();
	$( "#officeTypeDiv" ).show();
	$( "#officeCodeDiv" ).show();
	$( "#officeSigntorydiv" ).show();
	$( "#officeCodeDiv-edit" ).hide();
	$('#headingforredyellowflag').show();
	$('#appid-search').show();
}
//function addOffice(){	
//	var url='add-office-details';
//	$("#office-form").attr('action', url);
//	$('#office-form').submit();
//}
function addRedFlagAssociations(flagValue)
{
	$('#bar-notify').html('');
	$('#sticky-notify').html('');
	if($('#red-flag-associations').validationEngine('validate'))
		{
		var params = 'assoName='+$('#assoName').val()+'&assoAddress='+$('#assoAddress').val()
		+'&assoState='+$('#assoState').val()+'&originatorOffice='+$('#originator_office').val()+'&originatorOrderNo='+$('#originatorOrderNo').val()+'&originatorOrderDate='+$('#originatorOrderDate').val()
		+'&categoryDesc='+$('#categoryDesc').val()+'&remarkOriginatorOffice='+$('#remarkOriginatorOffice').val()+'&requestToken='+$('#requestToken').val()+'&flagValue='+flagValue;
		var action = 'add-red-flag-associations';
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
				initializeRedFlagAssociationTable();			
				initForm();
				showTable();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		$('#red-flag-associations').trigger("reset");
		 clearForm();
		}

	
}
function addYellowFlagAssociations(flagValue)
{
	$('#bar-notify').html('');
	$('#sticky-notify').html('');
	if($('#red-flag-associations').validationEngine('validate'))
		{
		var params = 'assoName='+$('#assoName').val()+'&assoAddress='+$('#assoAddress').val()
		+'&assoState='+$('#assoState').val()+'&originatorOffice='+$('#originator_office').val()+'&originatorOrderNo='+$('#originatorOrderNo').val()+'&originatorOrderDate='+$('#originatorOrderDate').val()
		+'&categoryDesc='+$('#categoryDesc').val()+'&remarkOriginatorOffice='+$('#remarkOriginatorOffice').val()+'&requestToken='+$('#requestToken').val()+'&flagValue='+flagValue;
		var action = 'add-yellow-red-flag-associations';
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
				initializeRedFlagAssociationTable();			
				initForm();
				showTable();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		$('#red-flag-associations').trigger("reset");
		 clearForm();
		}

	
}
function deleteRedFlagAssociations(){
$('#bar-notify').html('');
$('#sticky-notify').html('');
	if($('#delete-remark-form').validationEngine('validate') )
	{
	var params = 'assoId='+$('#delete-assoId').val()+'&deloriginatorOffice='+$('#delete-originator_office').val()+'&deloriginatorOrderNo='+$('#delete-originatorOrderNo').val()+'&deloriginatorOrderDate='+$('#delete-originatorOrderDate').val()
	+'&delremarkOriginatorOffice='+$('#delete-remarkOriginatorOffice').val()+'&requestToken='+$('#requestToken').val()+'&flagdelete='+$('#flagdelete').val();
	//alert(params);

	var action = 'delete-red-flag-associations';
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
			initializeRedFlagAssociationTable();
			$('#delete-remark-details-modal').modal('hide');
			showTable();						
		},
		error: function(textStatus,errorThrown){
			alert('error');
			/*$("#gate-add-button").button('reset');*/
		}
	});
	$('#red-flag-associations').trigger("reset");
	}
	
}
function deleteRemarkModel(flagdelete){
	
	if($('#red-flag-associations').validationEngine('validate') ){
	$('#delete-remark-form').trigger("reset");
		 $('#delete-assoId').val($('#assoId').val());
			$('#flagdelete').val(flagdelete);//hidden field 
			$("#delete-remark-details-modal").modal({
				show : true
			});
	}
   
}

function RemoveYellowaddModel(){
	if($('#red-flag-associations').validationEngine('validate') ){
		$('#add-remark-form').trigger("reset");
		 $('#add-assoId').val($('#assoId').val());
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
		var params = 'assoId='+$('#add-assoId').val()+'&originatorOffice='+$('#originator_officeadd').val()+'&originatorOrderNo='+$('#originatorOrderNoadd').val()+'&originatorOrderDate='+$('#originatorOrderDateadd').val()
		+'&categoryDesc='+$('#categoryDescadd').val()+'&remarkOriginatorOffice='+$('#remarkOriginatorOfficeadd').val()+'&requestToken='+$('#requestToken').val()+'&flagaddred='+flagaddred;
		var action = 'add-red-remove-yellow-red-flag-associations';
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
				initializeRedFlagAssociationTable();
				showTable();						
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		$('#red-flag-associations').trigger("reset");
		}
		
	}
function getApplicationList(){	
	/* Initialize Hide and Show for Application Details */
		$("#red-flag-associations-table").html('');	
		$('#bar-notify').html('');
		$('#app-info').hide();
		$('#actions').hide();
		//alert(""+$('#applicationId').val());
		$("#red-flag-associations-table").bootgrid(
	    		{
		    		title:'',
		    		recordsinpage:'10',
		    		dataobject:'propertyList',    		
		    		dataurl:'get-search-list-red-flag-associations?applicationId='+$('#applicationId').val()+'&applicationName='+$('#applicationName').val(),
		    		columndetails:
					[
						/*{title:'Association Id', name:'assoId'}, */
						{title:'Association Name', name:'assoName'}, 
						{title:'Address', name:'assoAddress'},
						{title:'State', name:'assoStateName'},
						{title:'Category', name:'categoryDesc'},
						{title:'Remark', name:'remarks'},
						{title:'Red/Yellow Flagged By', name:'actionBy'},
						{title:'Red/Yellow Flagged On', name:'statusDate'},
						{title:'Flag Type', name:'categoryType'}
					],
					onRowSelect:function(rowdata){getRedFlagNgoDetailsTemp1(rowdata)}
		    		});	
		}
