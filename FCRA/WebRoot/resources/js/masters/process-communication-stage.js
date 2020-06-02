


$(document).ready(function (){
	initializeProcessComunnicationList();
	$("#processcommunication-type-form").validationEngine({promptPosition: 'bottomRight'});

});

function initializeProcessComunnicationList() {
	$("#pcstage-list").html('');
	$("#pcstage-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-process-communication-stage-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:' Stage Id', name:'stageId', sortable:true}, 
				{title:'Stage Description', name:'stageDesc',sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getPcStageDetail(rowdata);}
    		});	
	clearForm();
	
}	
	function getPcStageDetail(rowdata){
		
		$('#stageId').val(rowdata.stageId);
		$('#stageDesc').val(rowdata.stageDesc);
		$('#createdDate').val(rowdata.createdDate);
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#pcstage-list').show();
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addpcstage(){
		 $('#sticky-notify').html('');
		 $('#bar-notify').html('');

		if($('#processcommunication-type-form').validationEngine('validate'))
			{
			
	    var params = 'stageDesc='+$('#stageDesc').val()+'&requestToken='+$('#requestToken').val();
		var action = 'add-process-communication-stage-details';		
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
		      initializeProcessComunnicationList();
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
	function editpcstage()
	{			
		$('#bar-notify').html('');
		 $('#sticky-notify').html('');
		if($('#processcommunication-type-form').validationEngine('validate'))
			{
		var params = 'stageDesc='+$('#stageDesc').val()+'&stageId='+$('#stageId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-process-communication-stage-details';
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
				initializeProcessComunnicationList();
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
	function deletepcstage()
	{
		$('#bar-notify').html('');
		 $('#sticky-notify').html('');
		var params = 'stageDesc='+$('#stageDesc').val()+'&stageId='+$('#stageId').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-process-communication-stage-details';
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
				initializeProcessComunnicationList();			
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
