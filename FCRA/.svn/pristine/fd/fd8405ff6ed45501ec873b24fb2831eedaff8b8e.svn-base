
$(document).ready(function (){
	initializeNotificationList();
	$("#office-form").validationEngine({promptPosition: 'bottomRight'});
});
function initializeNotificationList() {
	$("#office-list").html('');
	$("#office-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-office-list-office-details-office-detail',
    		columndetails:
			[
				{title:'Office Code', name:'officeCode',sortable:true}, 
				{title:'Office Name', name:'officeName', sortable:true}, 
				{title:'Address', name:'address'},
				{title:'Country', name:'countryName', sortable:true},
				{title:'Contact No.', name:'contactNo'},
				{title:'Email Id', name:'emailId'}
			],
			onRowSelect:function(rowdata){getNotificationDetails(rowdata.officeCode)}
    		});	
}

function getNotificationDetails(officeCode){
	$('#bar-notify').html('');
	var params = 'officeCode='+officeCode;
	var action = 'get-office-details-office-detail';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$.each(JSON.parse(data[0]),function(index,item){
				 $('#officeCode').val(officeCode);
				$('#officeCodeLebel').text(item.p2);
		        $('#officeName').val(item.p3);
				$('#officeAdd').val(item.p4);
				$('#officeCity').val(item.p5);
				$('#officeState').val(item.p6);
	            $('#officeCountry').val(item.p7);
				$('#officeZipCode').val(item.p8);
				$('#officeContact').val(item.p9);
				$('#officeEmail').val(item.p10);
				$('#currencyType').val(item.p11);
				$('#officeSigntory').val(item.p12);
			//	$('#officeTimeZone').val(item.p13);
           populateGetDetails();
			});					
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
}
function editoffice(){
	$('#bar-notify').html('');
	if($('#office-form').validationEngine('validate'))
	{
	var params = 'officeCode='+$('#officeCode').val()
	+'&officeName='+$('#officeName').val()+'&officeCity='+$('#officeCity').val()+'&officeState='+$('#officeState').val()+'&officeCountry='+$('#officeCountry').val()
	+'&officeZipCode='+$('#officeZipCode').val()+'&officeContact='+$('#officeContact').val()+'&officeEmail='+$('#officeEmail').val()+'&currencyType='+$('#currencyType').val()
	+'&officeTimeZone='+$('#officeTimeZone').val()+'&officeSigntory='+$('#officeSigntory').val()+'&officeAdd='+$('#officeAdd').val();
	var action = 'edit-office-details-office-detail';
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			notifyList(JSON.parse(data),'');
			initializeNotificationList();
			showTable();							
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	$('#office-form').trigger("reset");
	}
}
function deleteOffice(){
	
	$('#bar-notify').html('');
	if($('#office-form').validationEngine('validate'))
	{
	var params = 'officeCode='+$('#officeCode').val();
	var action = 'delete-office-details-office-detail';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){	
			notifyList(JSON.parse(data),'');
			initializeNotificationList();
			showTable();						
		},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
	$('#office-form').trigger("reset");
	}
}

function populateGetDetails(){
	$('#office-list').hide();
	$('#table-btn').show();
	$('#edit-actions').show();
	$('#form-div').show();
	$('#add-btn').hide();	
	$('#add-details-btn').hide();
	$( "#officeTypeDiv" ).hide();
	$( "#officeCodeDiv" ).hide();
	$( "#officeSigntorydiv" ).hide();
	$( "#officeTypeDiv-edit" ).show();
	$( "#officeCodeDiv-edit" ).show();
	
}
function initForm(){
	$('#office-form').trigger("reset");
	$('#office-list').hide();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').show();
	$("#form-div").show();	
	$('#add-details-btn').show();
	$( "#officeCodeDiv" ).show();
	$( "#officeCodeDiv-edit" ).hide();
}
function showTable(){
	$('#table-btn').hide();
	$('#edit-actions').hide();
	$('#office-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
	$( "#officeTypeDiv" ).show();
	$( "#officeCodeDiv" ).show();
	$( "#officeSigntorydiv" ).show();
	$( "#officeCodeDiv-edit" ).hide();
}
//function addOffice(){	
//	var url='add-office-details';
//	$("#office-form").attr('action', url);
//	$('#office-form').submit();
//}
function addOffice()
{
	$('#bar-notify').html('');
	if($('#office-form').validationEngine('validate'))
		{
		var params = 'officeId='+$('#officeType').val()+'&officeCode='+$('#officeCode').val()
		+'&officeName='+$('#officeName').val()+'&officeCity='+$('#officeCity').val()+'&officeState='+$('#officeState').val()+'&officeCountry='+$('#officeCountry').val()
		+'&officeZipCode='+$('#officeZipCode').val()+'&officeContact='+$('#officeContact').val()+'&officeEmail='+$('#officeEmail').val()+'&currencyType='+$('#currencyType').val()
		+'&officeTimeZone='+$('#officeTimeZone').val()+'&officeSigntory='+$('#officeSigntory').val()+'&officeAdd='+$('#officeAdd').val();
		var action = 'add-office-details-office-detail';
		$.ajax({
			url: action,
			method:'GET',
			data: params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data),'');
				initializeNotificationList();			
				initForm();
				showTable();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('erroraa');
				$("#gate-add-button").button('reset');
			}
		});
		$('#office-form').trigger("reset");
		 clearForm();
		}

	
}

