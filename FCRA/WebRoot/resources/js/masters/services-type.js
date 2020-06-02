


$(document).ready(function (){
	initializeServicesList();
	$("#services-type-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeServicesList() {
	$("#services-list").html('');
	$("#services-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-service-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Service Code', name:'servicesCode', sortable:true}, 
				{title:'Services Description', name:'servicesDesc',sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getServicesDetails(rowdata);}
    		});	
	clearForm();
	
	
}	
	function getServicesDetails(rowdata){
		
		$('#servicesCode').val(rowdata.servicesCode);
		$('#servicesDesc').val(rowdata.servicesDesc);
		$('#createdDate').val(rowdata.createdDate);
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#services-list').show();
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addservicesdetails(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('')
		if($('#services-type-form').validationEngine('validate'))
			{
		var params = 'servicesDesc='+$('#servicesDesc').val()+'&requestToken='+$('#requestToken').val();
		var action = 'add-service-details';		
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
		      initializeServicesList();
				initForm();
				showadd();				
			},
			error: function(textStatus,errorThrown){
				alert('error');				
			}
		});
		 clearForm();
		 	
	}
	}
	
	function editservice()
	{	
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#services-type-form').validationEngine('validate'))
			{
		var params = 'servicesCode='+$('#servicesCode').val()+'&servicesDesc='+$('#servicesDesc').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-service-details';
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
				initializeServicesList();
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

	function deleteservice()
	{
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		var params = 'servicesCode='+$('#servicesCode').val()+'&servicesDesc='+$('#servicesDesc').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-service-details';
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
				initializeServicesList();
		       showadd();
									
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		clearForm();
	}
	

function initForm(){	
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();

}

function showadd()
{
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}
