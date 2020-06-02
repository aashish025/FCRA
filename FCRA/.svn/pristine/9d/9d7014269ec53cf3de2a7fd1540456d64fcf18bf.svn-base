$(document).ready(function (){
	initializePurposeList();
	$("#purpose").validationEngine({promptPosition: 'bottomReft'});
});

function initializePurposeList() {
	$("#purpose-list").html('');
	$("#purpose-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-purpose-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Purpose Code', name:'purposeCode',sortable:true},
			
				{title:'Purpose Name', name:'purposeName', sortable:true},
							
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getPurpose(rowdata);}
    		});	
	clearForm();
}

function getPurpose(rowdata){
$('#purposeCode').val(rowdata.purposeCode);
$('#purposeName').val(rowdata.purposeName);
$('#createdDate').val(rowdata.createdDate);
$('#code').hide();
$('#purpose-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();
}

function initForm5(){
	$('#purpose-list').show();
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

function showaAllTable(){
	$('#purpose-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editPurpose(){
	$('#sticky-notify').html('');
	if($('#purpose').validationEngine('validate')){
	$('#bar-notify').html('');
	var params = 'purposeCode='+$('#purposeCode').val()+'&purposeName='+$('#purposeName').val()+'&requestToken='+$('#requestToken').val();
	var action = 'edit-purpose-details';
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
					initializePurposeList();
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

function deletePurpose(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'purposeCode='+$('#purposeCode').val()+'&requestToken='+$('#requestToken').val();
	var action = 'delete-purpose-details';
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
			initializePurposeList();
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
	showaAllTable();
}

function addPurpose(){
	$('#sticky-notify').html('');
		if($('#purpose').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='purposeCode='+$('#purposeCode').val()+'&purposeName='+$('#purposeName').val()+'&requestToken='+$('#requestToken').val();
		$.ajax({
			url: 'add-purpose-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				initializePurposeList();
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