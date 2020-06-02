$(document).ready(function() {	
	$("#add-member-category-type").on('click', addMemberCategoryType);
	$("#cancel-btn").on('click', cancelMemberCategoryAdd);
	initializeMemberCategoryTypeList();
});

function initializeMemberCategoryTypeList() {	
	$("#member-category-type-list").html('');
	$("#member-category-type-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-member-category-details',
    		columndetails:
			[
				{title:'Category Code', name:'categoryCode', sortable:true}, 
				{title:'Category Name', name:'categoryName', sortable:true}, 
				{title:'Entered On', name:'createdDate', sortable:true}
			],
			defaultsortcolumn:'createdDate',
			defaultsortorder:'desc',
			onRowSelect:function(rowdata){getMemberCategoryType(rowdata.categoryCode);}
    		});	
}

function getMemberCategoryType(categoryCode){		
	var params = 'categoryCode='+categoryCode;		
	var action = 'get-member-category-type-member-category-details';		
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){				
				$('#categoryCodeForEdit').val(item.li);
				$('#categoryNameForEdit').val(item.ld);				
				populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}


function populateGetDetails(){		
	$('#add-member-category-type').hide();
	$('#add-member-category-type-form').hide();
	$('#form-div').show();	
	$('#edit-actions').show();	
}

function addMemberCategoryType() {		
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
	$("#add-member-category-type-form").show();
}

function cancelMemberCategoryAdd() {
	resetForm();	
	hideAddForm();
}

function resetForm() {
	clearForm('#add-member-category-type-form');
}

function hideAddForm() {
	$("#add-member-category-type-form").hide();
}

function deleteMemberCategoryType(){
	$('#bar-notify').html('');
	var params = 'categoryCode='+$('#categoryCodeForEdit').val();
	var action = 'delete-member-category-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){	
			notifyList(JSON.parse(data),'');
			initializeMemberCategoryTypeList();
			$('#form-div').hide();		
			$('#add-member-category-type').show();
		},
		error: function(textStatus,errorThrown){
			alert('error');		
			alert(errorThrown.val);
		}
	});
}

function validateMemberCategoryType() {	
	if($('#add-member-category-type-form').validationEngine('validate')) {
		$('#add-member-category-type-form').submit();
	}
}

function hideMemberCategoryValidationMsg() {
	$('#add-member-category-type-form').validationEngine('hide');
}

function validateEditMemberCategoryType() {	
	if($('#member-category-form').validationEngine('validate')) {
		$('#religion-form').submit();
	}
}