
$(document).ready(function (){
	initializeLocalityTypeList();
	$("#loc").validationEngine({promptPosition: 'bottomRight'});
});
function initializeLocalityTypeList() {
	$("#locality-list").html('');
	$("#locality-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-locality-type-locality',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Locality Type Id', name:'localityTypeId',sortable:true},
			
				{title:' Locality Name', name:'localityTypeName', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getLocalityType(rowdata);}
    		});	
	clearForm();
}



function getLocalityType(rowdata){

$('#localityTypeId').val(rowdata.localityTypeId);
$('#localityTypeName').val(rowdata.localityTypeName);
$('#createdDate').val(rowdata.enteredOn);
$('#locality-list').show();
$('#add-btn').hide();
$('#edit-actions').show();

$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();


}


function populateGetDetails(){
	$('#locality-list').show();
	$('#table-btn').hide();
	
	$('#edit-actions').show();
	$('#form-div').hide();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
}
function initForm3(){
	$('#locality-list').show();

	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();

}
function showTable(){
	$('#table-btn').hide();

	$('#edit-actions').hide();
	$('#locality-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});

}
function showaAllTable()
{
	$('#locality-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();

	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editLocalitytype(){
	$('#bar-notify').html('');
	var params = 'localityTypeId='+$('#localityTypeId').val()+'&localityTypeName='+$('#localityTypeName').val();
	var action = 'edit-Locality-details-locality';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			initializeLocalityTypeList();
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
function deleteLocalityType(){
	$('#bar-notify').html('');
	var params = 'localityTypeId='+$('#localityTypeId').val();
	var action = 'delete-Locality-details-locality';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeLocalityTypeList();
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
function addLocality(){
	if($('#loc').validationEngine('validate')){
	$('#bar-notify').html('');
	var params='localityTypeName='+$('#localityTypeName').val();
	$.ajax({
		url: 'add-Locality-details-locality',
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			initializeLocalityTypeList();
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm()
	showaAllTable();

}}

