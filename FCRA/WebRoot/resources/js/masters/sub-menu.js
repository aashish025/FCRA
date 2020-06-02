$(document).ready(function (){
	initializeSubMenuList();
	$("#submenu").validationEngine({promptPosition: 'bottomReft'});
});

function initializeSubMenuList() {
	$("#submenu-list").html('');
	$("#submenu-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-SubMenu-type-sub-menu-details',
    		defaultsortcolumn:'smenuName',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Sub Menu Name', name:'smenuName',sortable:true},
			
				{title:'Action Path ', name:'actionPath', sortable:true},
			
				{title:'Parent Menu Name', name:'pMenuName',sortable:true}
			],
			onRowSelect:function(rowdata){getSubMenu(rowdata);}
    		});	
	clearForm();
}

function getSubMenu(rowdata){
	$('#smenuName').val(rowdata.smenuName);
	$('#actionPath').val(rowdata.actionPath);
	$('#pMenuId').val(rowdata.pMenuId);
	$('#smenuId').val(rowdata.smenuId);
	$('#submenu-list').show();
	$('#add-btn').hide();
	$('#edit-actions').show();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").show();	
	$('#add-details-btn').hide();
}

function populateGetDetails(){
	$('#submenu-list').show();
	$('#table-btn').hide();
	$('#edit-actions').show();
	$('#form-div').hide();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
}

function initForm5(){
	$('#submenu-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();
}

function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#submenu-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});
}

function showaAllTable(){
	$('#submenu-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editSubMenu(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'smenuId='+$('#smenuId').val()+'&smenuName='+$('#smenuName').val()+'&actionPath='+$('#actionPath').val()+'&pMenuId='+$('#pMenuId').val()+'&requestToken='+$('#requestToken').val();
	var action = 'edit-SubMenu-details-sub-menu-details';
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
			initializeSubMenuList();
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

function deleteSubMenu(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'smenuId='+$('#smenuId').val()+'&requestToken='+$('#requestToken').val();
	var action = 'delete-SubMenu-details-sub-menu-details';
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
			initializeSubMenuList();
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

function addSubMenu(){
	$('#sticky-notify').html('');
	if($('#submenu').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='smenuName='+$('#smenuName').val()+'&actionPath='+$('#actionPath').val()+'&pMenuId='+$('#pMenuId').val()+'&requestToken='+$('#requestToken').val();
		$.ajax({
			url: 'add-SubMenu-details-sub-menu-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				initializeSubMenuList();
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