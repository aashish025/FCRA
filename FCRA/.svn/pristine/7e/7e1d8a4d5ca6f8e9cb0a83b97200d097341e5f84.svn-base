


$(document).ready(function (){
	initializeSubApplicationList();
	$("#subapplication-type-form").validationEngine({promptPosition: 'bottomLeft'});

});

function initializeSubApplicationList() {
	$("#substage-list").html('');
	$("#substage-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-application-substage-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:' Sub Stage Id', name:'substageId', sortable:true}, 
				{title:' Sub Stage Description', name:'substageDesc',sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getSubStageDetail(rowdata);}
    		});	
	clearForm();
	
}	
	function getSubStageDetail(rowdata){
		
		$('#substageId').val(rowdata.substageId);
		$('#substageDesc').val(rowdata.substageDesc);
		$('#parentstageId').val(rowdata.parentstageId);
		$('#createdDate').val(rowdata.createdDate);
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#substage-list').show();
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addsubstagedetails(){
		$('#sticky-notify').html('');
		 $('#bar-notify').html('');
		 if($('#subapplication-type-form').validationEngine('validate')){
		var params = 'substageDesc='+$('#substageDesc').val()+'&parentstageId='+$('#parentstageId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'add-application-substage-details';		
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
			    initializeSubApplicationList();
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
	
	function editsubstage()
	{			
		
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		 if($('#subapplication-type-form').validationEngine('validate'))
			 {
		var params = 'substageDesc='+$('#substageDesc').val()+'&substageId='+$('#substageId').val()+'&parentstageId='+$('#parentstageId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-application-substage-details';
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
				initializeSubApplicationList();
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
	
	function deletesubstage()
	{
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		var params = 'substageDesc='+$('#substageDesc').val()+'&substageId='+$('#substageId').val()+'&parentstageId='+$('#parentstageId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'delete-application-substage-details';
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
				initializeSubApplicationList();
			
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
