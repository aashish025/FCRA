$(document).ready(function (){
	initializeGenderList();
	$("#gender-type-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeGenderList() {
	$("#Gender-list").html('');
	$("#Gender-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-gender-details',
    		defaultsortcolumn:'enteredOn',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Gender Code', name:'genderId',sortable:true}, 
				{title:'Gender Type', name:'genderType', sortable:true}, 
			    {title:'Entered On', name:'enteredOn',sortable:true}
			],			
			onRowSelect:function(rowdata){getGenderDetails(rowdata);}
    		});	

}
function getGenderDetails(rowdata){
	$('#genderId').val(rowdata.genderId);
	$('#genderType').val(rowdata.genderTypeId);
	$('#enteredOn').val(rowdata.enteredOn);
	$('#add-btn').hide();
	$('#details-div').show();
	$('#edit-actions').show();
	$('#add-details-btn').hide();
	$('#add-details-btn').hide();
	$('#genderId').hide();
	$('#lgc').hide();
	}

function initForm(){	
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();
	$('#genderId').show();
	$('#lgc').show();
}
function showTable1(){
	$('#edit-actions').hide();
	$('#gender-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
	
	}
function showadd()
{
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}


function clearForm(){	
	$(document).find('.clear').each(function (){
		$(this).val('');
	});
}
function addgenderdetails() {
	if($('#gender-type-form').validationEngine('validate'))
		{
		$('#bar-notify').html('');
		var params = 'genderType='+$('#genderType').val()+'&genderid='+$('#genderId').val();
		
		var action = 'add-gender-type-gender-details';
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeGenderList();
				initForm();
				showadd();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		 clearForm();
		}	
}

function editGendertype()
{	
	$('#bar-notify').html('');
	if($('#gender-type-form').validationEngine('validate'))
	{
	var params =  'genderType='+$('#genderType').val()+'&genderid='+$('#genderId').val();
	var action = 'edit-gender-type-gender-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeGenderList();
			showadd();
			
			
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
			
		}
	});
	clearForm();
	}
}

function deleteGendertype()
{
	$('#bar-notify').html('');
	if($('#gender-type-form').validationEngine('validate'))
	{
	var params = 'genderType='+$('#genderType').val()+'&genderid='+$('#genderId').val();
	var action = 'delete-gender-type-gender-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeGenderList();
			showadd();
								
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	clearForm();
	}
}
