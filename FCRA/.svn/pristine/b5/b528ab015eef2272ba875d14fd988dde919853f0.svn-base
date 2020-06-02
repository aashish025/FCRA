$(document).ready(
		function() {		
			$("#add-time-zone").on('click',
					addTimeZone);
			$("#cancel-btn").on('click', cancelPropertyAdd);
			initializeTimeZoneTypeList();
		});

function initializeTimeZoneTypeList() {	
	$("#time-zone-type-list").html('');
	$("#time-zone-type-list").bootgrid(
			{
				title : '',
				recordsinpage : '5',
				dataobject : 'propertyList',
				dataurl : 'get-time-zone-list-type-time-zone-details',
				columndetails : [ {
					title : 'Zone ID',
					name : 'zoneId',
					sortable : true
				}, {
					title : 'Zone',
					name : 'zoneName',
					sortable : true
				}, {
					title : 'Country',
					name : 'countryName',
					sortable : true
				}, {
					title : 'Entered On',
					name : 'createdDate',
					sortable : true
				} ],
				defaultsortcolumn : 'createdDate',
				defaultsortorder : 'desc',
				onRowSelect : function(rowdata) {
					getTimeZoneType(rowdata.zoneId);
				}
			});
}

/*function getTimeZoneType(zoneId) {
	var params = 'timeZone='+zoneId;	
	var action = 'get-time-zone-type';		
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){				
				$('#abcd').val(item.li);
				$('#abcde').val(item.ld);				
				populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}*/

function getTimeZoneType(zoneId) {
	var params = 'timeZone='+zoneId;	
	var action = 'get-time-zone-type-time-zone-details';		
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){				
				$('#abcd').val(item.p1);
				$('#abcde').val(item.p2);	
				$('#timeZoneId').val(item.p3);
				populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}

function populateGetDetails(){		
	$('#add-time-zone').hide();
	$('#add-time-zone-form').hide();
	$('#form-div').show();	
	$('#edit-actions').show();	
}

function resetForm() {
	clearForm('#add-time-zone-form');
}
function hideAddForm() {
	$("#add-time-zone-form").hide();
}

function cancelPropertyAdd() {
	resetForm();
	hideAddForm();
}

function toggleSubmit(value) {
	if (value == 'add') {
		$('#submit-btn').show();
		$('#edit-btn').hide();
	} else {
		$('#submit-btn').hide();
		$('#edit-btn').show();
	}
}

function showAddForm() {
	$("#add-time-zone-form").show();
}

function addTimeZone() {
	toggleSubmit('add');
	showAddForm();
}

function validateTimeZoneType() {	
	if($('#add-time-zone-form').validationEngine('validate')) {
		$('#add-time-zone-form').submit();
	}
}

function hideTimeZoneValidationMsg() {
	$('#add-time-zone-form').validationEngine('hide');
}

function validateEditTimeZoneType() {	
	if($('#time-zone-form').validationEngine('validate')) {
		$('#time-zone-form').submit();
	}
}

function deleteTimeZoneType(){
	$('#bar-notify').html('');
	var params = 'timeZoneTypeId='+$('#timeZoneId').val();
	var action = 'delete-time-zone-Type-time-zone-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			initializeTimeZoneTypeList();
			$('#form-div').hide();	
			$('#add-time-zone').show();	
			
		},
		error: function(textStatus,errorThrown){
			alert('error');		
			alert(errorThrown.val);
		}
	});
}