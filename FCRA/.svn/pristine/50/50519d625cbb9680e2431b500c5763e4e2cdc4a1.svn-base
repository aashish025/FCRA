$(document).ready(function() {	
	$("#add-religion-type").on('click', addReligionType);
	$("#cancel-btn").on('click', cancelReligionAdd);
	initializeReligionTypeList();
});

function initializeReligionTypeList() {	
	$("#religion-type-list").html('');
	$("#religion-type-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-religion-type-religion-details',
    		columndetails:
			[
				{title:'Religion Code', name:'religionCode', sortable:true}, 
				{title:'Religion Desc', name:'religionDesc', sortable:true}, 
				{title:'Entered On', name:'createdDate', sortable:true}
			],
			defaultsortcolumn:'createdDate',
			defaultsortorder:'desc',
			onRowSelect:function(rowdata){getReligionType(rowdata.religionCode);}
    		});	
}

function getReligionType(religionCode){			
	var params = 'religionCode='+religionCode;		
	var action = 'get-religion-details';		
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){				
				$('#religionDescForEdit').val(item.li);
				$('#religionCodeForEdit').val(item.ld);				
				populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}

function populateGetDetails(){		
	$('#add-religion-type').hide();
	$('#add-religion-type-form').hide();
	$('#form-div').show();	
	$('#edit-actions').show();	
}

function addReligionType() {		
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
	$("#add-religion-type-form").show();
}

function cancelReligionAdd() {
	resetForm();	
	hideAddForm();
}

function resetForm() {
	clearForm('#add-religion-type-form');
}

function hideAddForm() {
	$("#add-religion-type-form").hide();
}

function validateEditReligionType() {	
	if($('#religion-form').validationEngine('validate')) {
		$('#religion-form').submit();
	}
}

function deleteReligionType(){
	$('#bar-notify').html('');
	var params = 'religionCodeForEdit='+$('#religionCodeForEdit').val();
	var action = 'delete-religion-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){	
			notifyList(JSON.parse(data),'');
			initializeReligionTypeList();
			$('#form-div').hide();		
			$('#add-religion-type').show();
		},
		error: function(textStatus,errorThrown){
			alert('error');		
			alert(errorThrown.val);
		}
	});
}

function hideReligionValidationMsg() {
	$('#add-religion-type-form').validationEngine('hide');
}

function validateReligionType() {	
	if($('#add-religion-type-form').validationEngine('validate')) {
		$('#add-religion-type-form').submit();
	}
}
