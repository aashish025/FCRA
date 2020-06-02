
$(document).ready(function (){
	initializeSubStageList();
	$("#substageOfc").validationEngine({promptPosition: 'bottomReft'});
});
function initializeSubStageList() {
	$("#substageofc-list").html('');
	$("#substageofc-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-substage-officetype-details',
    		defaultsortcolumn:'proposalDesc',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Proposal Type Name', name:'proposalDesc',sortable:true},
			
				{title:'Sub Stage Name ', name:'subStageDesc', sortable:true},
			
				{title:'Office Type Name', name:'officeType',sortable:true}
			],
			onRowSelect:function(rowdata){getSubStage(rowdata);}
    		});	
	clearForm();
}

function getSubStage(rowdata){
	$("#rowId").val(rowdata.rowId);
	$('#proposalTypeId').val(rowdata.proposalTypeId);
	$('#subStageId').val(rowdata.subStageId);
	$('#officeId').val(rowdata.officeId);
	$('#substageofc-list').show();
	$('#add-btn').hide();
	$('#edit-actions').show();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").show();	
	$('#add-details-btn').hide();
}

function populateGetDetails(){
	$('#substageofc-list').show();
	$('#table-btn').hide();
	$('#edit-actions').show();
	$('#form-div').hide();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
}

function initForm5(){
	$('#substageofc-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();
}

function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#substageofc-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});
}

function showaAllTable(){
	$('#substageofc-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editSubStageofc(){
	if($('#substageOfc').validationEngine('validate')){
	$('#bar-notify').html('');
	var params = 'rowId='+$('#rowId').val()+'&officeId='+$('#officeId').val()+'&subStageId='+$('#subStageId').val()+'&proposalTypeId='+$('#proposalTypeId').val();
	var action = 'edit-substage-officetype-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			initializeSubStageList();
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
}

function deleteSubStageofc(){
	$('#bar-notify').html('');
	var params = 'rowId='+$('#rowId').val();
	var action = 'delete-substage-officetype-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeSubStageList();
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

function addSubStageofc(){
	if($('#substageOfc').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='officeId='+$('#officeId').val()+'&subStageId='+$('#subStageId').val()+'&proposalTypeId='+$('#proposalTypeId').val();
		$.ajax({
			url: 'add-substage-officetype-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeSubStageList();
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

