$(document).ready(function (){
	initializeBlockYearList();
	$("#blockyear").validationEngine({promptPosition: 'bottomLeft'});
});

function initializeBlockYearList() {
	$("#blockyear-list").html('');
	$("#blockyear-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-block-year-details',
    		defaultsortcolumn:'blkyr',
    		defaultsortorder:'desc',
    		columndetails:
			[
			 	{title:'Block Year', name:'blkyr',sortable:true},
			 	
				{title:'Status', name:'status',sortable:true},
			],
			onRowSelect:function(rowdata){getBlockYear(rowdata);}
    		});	
	clearForm();
}

function getBlockYear(rowdata){
	$('#blkyr').val(rowdata.blkyr);
	$('#status').val(rowdata.status);
	$('#blockyear-list').show();
	$('#add-btn').hide();
	$('#code').hide();
	$('#edit-actions').show();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").show();	
	$('#add-details-btn').hide();
}

function initblock(){
	$('#code').show();
	$('#blockyear-list').show();
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

function showaAllTable(){
	$('#blockyear-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editBlockYear(){
	$('#sticky-notify').html('');
	if($('#blockyear').validationEngine('validate')){
	$('#bar-notify').html('');
	var params = 'blkyr='+$('#blkyr').val()+'&status='+$('#status').val()+'&requestToken='+$('#requestToken').val();
	var action = 'edit-block-year-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val( token);
			initializeBlockYearList();
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

function deleteBlockYear(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'blkyr='+$('#blkyr').val()+'&requestToken='+$('#requestToken').val();
	var action = 'delete-block-year-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data[0]),'');
			var token = JSON.parse(data[1]).li;
			if(token != null && token != '')
			$('#requestToken').val( token);
			initializeBlockYearList();
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

function addBlockYear(){
	$('#sticky-notify').html('');
	if($('#blockyear').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='blkyr='+$('#blkyr').val()+'&status='+$('#status').val()+'&requestToken='+$('#requestToken').val();
		$.ajax({
			url: 'add-block-year-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val( token);
				initializeBlockYearList();
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