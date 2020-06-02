$(document).ready(function (){
	initializeOfficeList();
	$("#office-type-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeOfficeList() {
	$("#Office-list").html('');
	$("#Office-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-office-list-details-office-type-details',
    		defaultsortcolumn:'enteredOn',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Office Code', name:'officeId',sortable:true}, 
				{title:'Office Type', name:'officeType', sortable:true},
				{title:'Office Name', name:'officeName', sortable:true},
			    {title:'Entered On', name:'enteredOn',sortable:true}
			],			
			onRowSelect:function(rowdata){getOfficeDetails(rowdata);}
    		});	

}
function getOfficeDetails(rowdata){
	$('#officeId').val(rowdata.officeId);
	$('#officeType').val(rowdata.officeType);
	$('#officeName').val(rowdata.officeName);
	$('#enteredOn').val(rowdata.enteredOn);
	$('#add-btn').hide();
	$('#details-div').show();
	$('#edit-actions').show();
	$('#add-details-btn').hide();
	$('#add-details-btn').hide();
	}

function initForm(){	
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();
}
function showTable1(){
	$('#edit-actions').hide();
	$('#office-list').show();
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
function addofficedetails() {
	if($('#office-type-form').validationEngine('validate'))
		{
		$('#bar-notify').html('');
		var params = 'officeType='+$('#officeType').val()+'&officeName='+$('#officeName').val();
		var action = 'add-office-type-details-office-type-details';
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeOfficeList();
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

function editOfficetype()
{	
	$('#bar-notify').html('');
	if($('#office-type-form').validationEngine('validate'))
	{
	var params =  'officeId='+$('#officeId').val()+'&officeType='+$('#officeType').val() +'&officeName='+$('#officeName').val();
	var action = 'edit-officetype-details-office-type-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeOfficeList();
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

function deleteOfficetype()
{
	$('#bar-notify').html('');
	if($('#office-type-form').validationEngine('validate'))
	{
	var params = 'officeType='+$('#officeType').val()+'&officeId='+$('#officeId').val();
	var action = 'delete-officetype-details-office-type-details';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeOfficeList();
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
