
$(document).ready(function (){
	initializeComRelationList();
	$("#relation").validationEngine({promptPosition: 'bottomReft'});
});

function initializeComRelationList() {
	$("#relation-list").html('');
	$("#relation-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-committee-relation-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Relation Code', name:'relationCode',sortable:true},
			
				{title:'Relation Name', name:'relationName', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getComRelation(rowdata);}
    		});	
	clearForm();
}

function getComRelation(rowdata){
$('#relationCode').val(rowdata.relationCode);
$('#relationName').val(rowdata.relationName);
$('#createdDate').val(rowdata.createdDate);
$('#relation-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();
}

function initForm5(){
	$('#relation-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});
}

function showaAllTable()
{
	$('#relation-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editComRelation(){
	if($('#relation').validationEngine('validate')){
	$('#bar-notify').html('');
	var params = 'relationCode='+$('#relationCode').val()+'&relationName='+$('#relationName').val();
	var action = 'edit-committee-relation-details';
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',
				success: function(data){			
					notifyList(JSON.parse(data),'');
					initializeComRelationList();
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

function deleteComRelation(){
	$('#bar-notify').html('');
	var params = 'relationCode='+$('#relationCode').val();
	var action = 'delete-committee-relation-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeComRelationList();
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
	showaAllTable();
}

function addComRelation(){
		if($('#relation').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='relationName='+$('#relationName').val();
		$.ajax({
			url: 'add-committee-relation-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeComRelationList();
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




