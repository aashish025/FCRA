
$(document).ready(function (){
	initializeStateList();
	$("#state").validationEngine({promptPosition: 'bottomReft'});
});
function initializeStateList() {
	$("#state-list").html('');
	$("#state-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-state-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'State Code', name:'scode',sortable:true},
			
				{title:'State Name', name:'sname', sortable:true},
				
				{title:'L Code', name:'lcode', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getState(rowdata);}
    		});	
	clearForm();
}



function getState(rowdata){
$('#scode').val(rowdata.scode);
$('#sname').val(rowdata.sname);
$('#lcode').val(rowdata.lcode);
$('#createdDate').val(rowdata.createdDate);
$('#code').hide();
$('#state-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();
}

function initForm5(){
	$('#state-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$('#code').show();
	$("#add-details-btn").show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});

}

function showaAllTable()
{
	$('#state-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editState(){
	if($('#state').validationEngine('validate')){
	$('#bar-notify').html('');
	var params = 'scode='+$('#scode').val()+'&sname='+$('#sname').val()+'&lcode='+$('#lcode').val();
	var action = 'edit-state-details';
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',
				success: function(data){			
					notifyList(JSON.parse(data),'');
					initializeStateList();
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

function deleteState(){
	$('#bar-notify').html('');
	var params = 'scode='+$('#scode').val();
	var action = 'delete-state-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeStateList();
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
	showaAllTable();
}

function addState(){
		if($('#state').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='scode='+$('#scode').val()+'&sname='+$('#sname').val()+'&lcode='+$('#lcode').val();
		$.ajax({
			url: 'add-state-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeStateList();
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




