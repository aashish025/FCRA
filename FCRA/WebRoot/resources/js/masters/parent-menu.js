$(document).ready(function (){
	initializeParentMenuList();
	$("#pmen").validationEngine({promptPosition: 'bottomRight'});
});

function initializeParentMenuList() {
	$("#parentmenu-list").html('');
	$("#parentmenu-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-PMenu-type-parent-menu',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Parent Menu Id', name:'pmenuId',sortable:true},
			
				{title:'Parent Menu Name', name:'pmenuName', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getParentMenu(rowdata);}
    		});	
	clearForm();
}

function getParentMenu(rowdata){
$('#pmenuId').val(rowdata.pmenuId);
$('#pmenuName').val(rowdata.pmenuName);
$('#createdDate').val(rowdata.createdDate);
$('#parentmenu-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();
}

function populateGetDetails(){
	$('#parentmenu-list').show();
	$('#table-btn').hide();
	$('#edit-actions').show();
	$('#form-div').hide();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
}

function initForm4(){
	$('#parentmenu-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();
}

function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#parentmenu-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});
}

function showaAllTable(){
	$('#parentmenu-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editPMenu(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'pmenuId='+$('#pmenuId').val()+'&pmenuName='+$('#pmenuName').val()+'&requestToken='+$('#requestToken').val();
	var action = 'edit-PMenu-details-parent-menu';
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
			initializeParentMenuList();
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

function deletePMenu(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'pmenuId='+$('#pmenuId').val()+'&requestToken='+$('#requestToken').val();
	var action = 'delete-PMenu-details-parent-menu';
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
			initializeParentMenuList();
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

function addPMenu(){
	$('#sticky-notify').html('');
	if($('#pmen').validationEngine('validate')){
	$('#bar-notify').html('');
	var params='pmenuName='+$('#pmenuName').val()+'&requestToken='+$('#requestToken').val();
	$.ajax({
		url: 'add-PMenu-details-parent-menu',
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val(token);
			initializeParentMenuList();
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