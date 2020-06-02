$(document).ready(function() {	
	$("#add-occupation-type").on('click', addOccupationType);
	$("#cancel-btn").on('click', cancelOccupationAdd);
	initializeOccupationTypeList();
});

function initializeOccupationTypeList() {	
	$("#occupation-type-list").html('');
	$("#occupation-type-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-occupation-type-occupation-details',
    		columndetails:
			[
				{title:'Occupation Code', name:'occupationCode', sortable:true}, 
				{title:'Occupation Name', name:'occupationName', sortable:true}, 
				{title:'Entered On', name:'createdDate', sortable:true}
			],
			defaultsortcolumn:'createdDate',
			defaultsortorder:'desc',
			onRowSelect:function(rowdata){getOccupationType(rowdata.occupationCode);}
    		});	
}

function getOccupationType(occupationCode){		
	var params = 'occupationCode='+occupationCode;		
	var action = 'get-occupation-details';		
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){				
				$('#occupationNameForEdit').val(item.li); 
				$('#occupationCodeForEdit').val(item.ld);				
				populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}

function populateGetDetails(){		
	$('#add-occupation-type').hide();
	$('#add-occupation-type-form').hide();
	$('#form-div').show();	
	$('#edit-actions').show();	
}

function addOccupationType() {		
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
	$("#add-occupation-type-form").show();
}

function cancelOccupationAdd() {
	resetForm();	
	hideAddForm();
}

function resetForm() {
	clearForm('#add-occupation-type-form');
}

function hideAddForm() {
	$("#add-occupation-type-form").hide();
}

function deleteOccupationType(){
	$('#bar-notify').html('');
	var params = 'occupationCode='+$('#occupationCodeForEdit').val();
	var action = 'delete-occupation-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){	
			notifyList(JSON.parse(data),'');
			initializeOccupationTypeList();
			$('#form-div').hide();		
			$('#add-occupation-type').show();
		},
		error: function(textStatus,errorThrown){
			alert('error');		
			alert(errorThrown.val);
		}
	});
}

function hideOccupationValidationMsg() {
	$('#add-occupation-type-form').validationEngine('hide');
}