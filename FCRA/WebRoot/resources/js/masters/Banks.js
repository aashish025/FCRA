$(document).ready(function (){
	initializeBankList();
	$("#banks").validationEngine({promptPosition: 'bottomReft'});
});

function initializeBankList() {
	$("#bank-list").html('');
	$("#bank-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-bank-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Bank Code', name:'bankCode',sortable:true},
			
				{title:'Bank Name', name:'bankName', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getBank(rowdata);}
    		});	
	clearForm();
}

function getBank(rowdata){
$('#bankCode').val(rowdata.bankCode);
$('#bankName').val(rowdata.bankName);
$('#createdDate').val(rowdata.createdDate);
$('#bank-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();
}

function initForm5(){
	$('#bank-list').show();
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
	$('#bank-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editBank(){
	$('#sticky-notify').html('');
	if($('#banks').validationEngine('validate')){
	$('#bar-notify').html('');
	var params = 'bankCode='+$('#bankCode').val()+'&bankName='+$('#bankName').val()+'&requestToken='+$('#requestToken').val();
	var action = 'edit-bank-details';
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
					initializeBankList();
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

function deleteBank(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var params = 'bankCode='+$('#bankCode').val()+'&requestToken='+$('#requestToken').val();
	var action = 'delete-bank-details';
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
			initializeBankList();
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
	showaAllTable();
}

function addBank(){
	$('#sticky-notify').html('');
		if($('#banks').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='bankName='+$('#bankName').val()+'&requestToken='+$('#requestToken').val();
		$.ajax({
			url: 'add-bank-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				initializeBankList();
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