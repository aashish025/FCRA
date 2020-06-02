$(document).ready(function() {		
	$("#add-donor-type").on('click', addDonorType);
	$("#cancel-btn").on('click', cancelDonorAdd);
	initializeDonorTypeList();
});

function initializeDonorTypeList() {	
	$("#donor-type-list").html('');
	$("#donor-type-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-donor-type-donor-type-details',
    		columndetails:
			[
				{title:'Donor Code', name:'donorId', sortable:true}, 
				{title:'Donor Name', name:'donorName', sortable:true}, 
				{title:'Entered On', name:'createdDate', sortable:true}
			],
			defaultsortcolumn:'createdDate',
			defaultsortorder:'desc',
			onRowSelect:function(rowdata){getDonorType(rowdata.donorId);}
    		});	
}

function getDonorType(donorId){		
	var params = 'donorCode='+donorId;	
	var action = 'get-donor-type-details';		
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){				
				$('#donorCodeForEdit').val(item.li);
				$('#donorNameForEdit').val(item.ld);				
				populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}

function populateGetDetails(){		
	$('#add-donor-type').hide();
	$('#add-donor-type-form').hide();
	$('#form-div').show();	
	$('#edit-actions').show();	
}

function addDonorType() {		
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
	$("#add-donor-type-form").show();
}

function cancelDonorAdd() {
	resetForm();	
	hideAddForm();
}

function resetForm() {
	clearForm('#add-donor-type-form');
}

function hideAddForm() {
	$("#add-donor-type-form").hide();
}

function validateEditDonorType() {	
	if($('#donor-form').validationEngine('validate')) {
		$('#donor-form').submit();
	}
}

function deleteDonorType(){
	$('#bar-notify').html('');
	var params = 'donorCodeForEdit='+$('#donorCodeForEdit').val();
	var action = 'delete-donor-type-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){	
			notifyList(JSON.parse(data),'');
			initializeDonorTypeList();
			$('#form-div').hide();		
			$('#add-donor-type').show();
		},
		error: function(textStatus,errorThrown){
			alert('error');		
			alert(errorThrown.val);
		}
	});
}

function hideDonorValidationMsg() {
	$('#add-donor-type-form').validationEngine('hide');
}

function validateDonorType() {	
	if($('#add-donor-type-form').validationEngine('validate')) {
		$('#add-donor-type-form').submit();
	}
}
