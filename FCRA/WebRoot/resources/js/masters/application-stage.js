


$(document).ready(function (){
	initializeApplicationList();
	$("#application-type-form").validationEngine({promptPosition: 'bottomLeft'});

});

function initializeApplicationList() {
	$("#stage-list").html('');
	$("#stage-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-application-stage-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:' Stage Id', name:'stageId', sortable:true}, 
				{title:'Stage Description', name:'stageDesc',sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getStageDetail(rowdata);}
    		});	
	clearForm();
	
}	
	function getStageDetail(rowdata){
		
		$('#stageId').val(rowdata.stageId);
		$('#stageDesc').val(rowdata.stageDesc);
		$('#createdDate').val(rowdata.createdDate);
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#stage-list').show();
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addstagedetails(){
		$('#sticky-notify').html('');
		 $('#bar-notify').html('');
		if($('#application-type-form').validationEngine('validate')){

	  var params = 'stageDesc='+$('#stageDesc').val()+'&requestToken='+$('#requestToken').val();
		var action = 'add-application-stage-details';		
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
                initializeApplicationList();
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
	function editstage()
	{			
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#application-type-form').validationEngine('validate'))
			{
		var params = 'stageDesc='+$('#stageDesc').val()+'&stageId='+$('#stageId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-application-stage-details';
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
				initializeApplicationList();
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
	function deletestage()
	{
		
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		var params = 'stageDesc='+$('#stageDesc').val()+'&stageId='+$('#stageId').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-application-stage-details';
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
				initializeApplicationList();
			
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
function showTable1(){
	$('#edit-actions').hide();
	$('#boundary-list').show();
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

