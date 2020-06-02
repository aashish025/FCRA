


$(document).ready(function (){
	initializeContributionList();
	$("#contribution-type-form").validationEngine({promptPosition: 'bottomRight'});
});

function initializeContributionList() {
	$("#contribution-list").html('');
	$("#contribution-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-contribution-nature-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Contribution Type', name:'contributionType', sortable:true}, 
				{title:'Contribution Description', name:'contributionName',sortable:true}, 
				{title:'Entered On', name:'createdDate',sortable:true},
							],			
			onRowSelect:function(rowdata){getcontributionDetails(rowdata);}
    		});	
	
	
}	
	function getcontributionDetails(rowdata){
		
		$('#contributionType').val(rowdata.contributionType);
		$('#contributionName').val(rowdata.contributionName);
		$('#createdDate').val(rowdata.createdDate);
        $('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#contribution-list').show();
		$('#act').hide();
	
	
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addcontributiondetails(){
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
		if($('#contribution-type-form').validationEngine('validate'))
	       {
			      	
					var  params = 'contributionType='+$('#contributionType').val()+'&contributionName='+$('#contributionName').val()+'&requestToken='+$('#requestToken').val();
					var action = 'add-contribution-nature-details';		
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
					      initializeContributionList();
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
    function editcontributiontype()	{
    	$('#sticky-notify').html('');
    		$('#bar-notify').html('');
    		if($('#contribution-type-form').validationEngine('validate'))
 	       {
    		 	
        var  params = 'contributionType='+$('#contributionType').val()+'&contributionName='+$('#contributionName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-contribution-nature-details';
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
				initializeContributionList();
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
   

	function deletecontributiontype()
	{
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
		var  params = 'contributionType='+$('#contributionType').val()+'&contributionName='+$('#contributionName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'delete-contribution-nature-details';
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
				initializeContributionList();
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
