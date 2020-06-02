$(document).ready(function() {	
	$("#add-committee-designation-type").on('click', addCommitteeDesignationType);
	$("#cancel-btn").on('click', cancelCommitteeDesignationAdd);
	initializeCommitteeDesignationTypeList();
});

function initializeCommitteeDesignationTypeList() {	
	$("#committee-desg-type-list").html('');
	$("#committee-desg-type-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-committee-designation-type-committee-designation-details',
    		columndetails:
			[
				{title:'Designation Code', name:'designationCode', sortable:true}, 
				{title:'Designation Name', name:'designationName', sortable:true}, 
				{title:'Entered On', name:'createdDate', sortable:true}
			],
			defaultsortcolumn:'createdDate',
			defaultsortorder:'desc',
			onRowSelect:function(rowdata){getCommitteeDesignationType(rowdata.designationCode);}
    		});	
}

function getCommitteeDesignationType(designationCode){		
	var params = 'designationCode='+designationCode;		
	var action = 'get-committee-designation-details';		
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){				
				$('#committeeDesignationNameForEdit').val(item.li);
				$('#committeeDesignationCodeForEdit').val(item.ld);				
				populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}

function populateGetDetails(){		
	$('#add-committee-designation-type').hide();
	$('#add-committee-designation-type-form').hide();
	$('#form-div').show();	
	$('#edit-actions').show();	
}

function addCommitteeDesignationType() {		
	toggleSubmit('add');
	showAddForm();
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

function showAddForm() {		
	$("#add-committee-designation-type-form").show();
}

function cancelCommitteeDesignationAdd() {
	resetForm();	
	hideAddForm();
}

function resetForm() {
	clearForm('#add-committee-designation-type-form');
}

function hideAddForm() {
	$("#add-committee-designation-type-form").hide();
}

function validateCommitteeDesignationType() {	
	if($('#add-committee-designation-type-form').validationEngine('validate')) {
		$('#add-committee-designation-type-form').submit();
	}
}

function hideCommitteeDesignationValidationMsg() {
	$('#add-committee-designation-type-form').validationEngine('hide');
}

function deleteCommitteeDesignationType(){
	$('#bar-notify').html('');
	var params = 'committeeDesignationCode='+$('#committeeDesignationCodeForEdit').val();
	var action = 'delete-committee-designation-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){	
			notifyList(JSON.parse(data),'');
			initializeCommitteeDesignationTypeList();
			$('#form-div').hide();		
			$('#add-committee-designation-type').show();
		},
		error: function(textStatus,errorThrown){
			alert('error');		
			alert(errorThrown.val);
		}
	});
}

function validateEditCommitteeDesignationType() {	
	if($('#committee-designation-form').validationEngine('validate')) {
		$('#committee-designation-form').submit();
	}
}