


$(document).ready(function (){
	initializeActList();
	$("#acts-type-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeActList() {
	$("#acts-list").html('');
	$("#acts-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-society-acts-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Society Act Code', name:'actCode', sortable:true}, 
				{title:'Society Act Name', name:'actName',sortable:true}, 
				{title:'Entered On', name:'createdDate',sortable:true},
							],			
			onRowSelect:function(rowdata){getactDetails(rowdata);}
    		});	
	
	
}	
	function getactDetails(rowdata){
		
		$('#actCode').val(rowdata.actCode);
		$('#actName').val(rowdata.actName);
		$('#createdDate').val(rowdata.createdDate);
        $('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#acts-list').show();
		$('#act').hide();
	
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addactdetails(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#acts-type-form').validationEngine('validate'))
	       {
			      	
					var  params = 'actCode='+$('#actCode').val()+'&actName='+$('#actName').val()+'&requestToken='+$('#requestToken').val();
					var action = 'add-society-acts-details';		
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
					      initializeActList();
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
    function editacttype()	{
    	$('#sticky-notify').html('');
    		$('#bar-notify').html('');
    		if($('#acts-type-form').validationEngine('validate'))
 	       {
    		 	
    	var  params = 'actCode='+$('#actCode').val()+'&actName='+$('#actName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-society-acts-details';
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
				 initializeActList();
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
   

	function deleteacttype()
	{
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
		var  params = 'actCode='+$('#actCode').val()+'&actName='+$('#actName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'delete-society-acts-details';
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
				 initializeActList();
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

	

function initForm(){	
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();
	$('#act').show();
	

}
function showadd()
{
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}
