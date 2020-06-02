


$(document).ready(function (){
	initializeNatureList();
	$("#nature-type-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeNatureList() {
	$("#nature-list").html('');
	$("#nature-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-association-nature-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Nature Code', name:'natureCode', sortable:true}, 
				{title:'Nature  Description', name:'natureName',sortable:true}, 
				{title:'Entered On', name:'createdDate',sortable:true},
							],			
			onRowSelect:function(rowdata){getnatureDetails(rowdata);}
    		});	
	
	
}	
	function getnatureDetails(rowdata){
		
		$('#natureCode').val(rowdata.natureCode);
		$('#natureName').val(rowdata.natureName);
		$('#createdDate').val(rowdata.createdDate);
        $('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#nature-list').show();
		$('#code').hide();

	
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addnaturedetails(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#nature-type-form').validationEngine('validate'))
	       {
			      	
					var  params = 'natureCode='+$('#natureCode').val()+'&natureName='+$('#natureName').val()+'&requestToken='+$('#requestToken').val();
					var action = 'add-association-nature-details';		
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
					      initializeNatureList();
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
    function editnaturetype()	{
    	$('#sticky-notify').html('');
    		$('#bar-notify').html('');
    		if($('#nature-type-form').validationEngine('validate'))
 	       {
    		 	
	    var  params = 'natureCode='+$('#natureCode').val()+'&natureName='+$('#natureName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-association-nature-details';
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
				 initializeNatureList();
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
   

	function deletenaturetype()
	{   $('#sticky-notify').html('');
		$('#bar-notify').html('');
	 	
		var  params = 'natureCode='+$('#natureCode').val()+'&natureName='+$('#natureName').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-association-nature-details';
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
				 initializeNatureList();
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
	$('#code').show();
	

}
function showTable1(){
	$('#edit-actions').hide();
	$('#Country-list').show();
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
