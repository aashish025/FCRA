
$(document).ready(function (){
	initializeDistrictList();
	$("#district").validationEngine({promptPosition: 'bottomLeft'});
});
function initializeDistrictList() {
	$("#district-list").html('');
	$("#district-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-district-details',
    		defaultsortcolumn:'dname',
    		defaultsortorder:'desc',
    		columndetails:
			[
			 	{title:'District Code', name:'dcode',sortable:true},
			 	
				{title:'District Name', name:'dname',sortable:true},
								
				{title:'State Name', name:'sname',sortable:true}
			],
			onRowSelect:function(rowdata){getDistrict(rowdata);}
    		});	
	clearForm();
}

function getDistrict(rowdata){
	$('#dname').val(rowdata.dname);
	$('#sname').val(rowdata.sname);
	$('#scode').val(rowdata.scode);
	$('#dcode').val(rowdata.dcode);
	$('#district-list').show();
	$('#add-btn').hide();
	$('#code').hide();
	$('#edit-actions').show();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").show();	
	$('#add-details-btn').hide();
}

function initForm5(){
	$('#code').show();
	$('#district-list').show();
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
	$('#district-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function editDistrict(){
	if($('#district').validationEngine('validate')){
	$('#bar-notify').html('');
	var params = 'dcode='+$('#dcode').val()+'&dname='+$('#dname').val()+'&scode='+$('#scode').val();
	var action = 'edit-district-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			initializeDistrictList();
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

function deleteDistrict(){
	$('#bar-notify').html('');
	var params = 'dcode='+$('#dcode').val();
	var action = 'delete-district-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeDistrictList();
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

function addDistrict(){
	if($('#district').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='dcode='+$('#dcode').val()+'&dname='+$('#dname').val()+'&scode='+$('#scode').val();
		$.ajax({
			url: 'add-district-details',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeDistrictList();
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

