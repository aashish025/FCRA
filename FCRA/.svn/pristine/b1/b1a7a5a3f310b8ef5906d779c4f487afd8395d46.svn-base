


$(document).ready(function (){
	initializeCountryList();
	$("#country-type-form").validationEngine({promptPosition: 'bottomLeft'});
});

function initializeCountryList() {
	$("#Country-list").html('');
	$("#Country-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-country-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Country Code', name:'countryCode', sortable:true}, 
				{title:'Country Name', name:'countryName',sortable:true}, 
				{title:'Entered On', name:'createdDate',sortable:true},
							],			
			onRowSelect:function(rowdata){getcountryDetails(rowdata);}
    		});	
	clearForm();
	
	
}	
	function getcountryDetails(rowdata){
		
		$('#countryCode').val(rowdata.countryCode);
		$('#countryName').val(rowdata.countryName);
		$('#createdDate').val(rowdata.createdDate);
        $('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#Country-list').show();
		$('#coun-code').hide();
		$('#coun-val').show();
	
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addcountrydetails(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#country-type-form').validationEngine('validate'))
	       {
			      	
					var  params = 'countryCode='+$('#countryCode').val()+'&countryName='+$('#countryName').val()+'&requestToken='+$('#requestToken').val();
					var action = 'add-country-details';		
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
					      initializeCountryList();
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
    function editcountrytype()	{
    	$('#sticky-notify').html('');
    		$('#bar-notify').html('');
    		if($('#country-type-form').validationEngine('validate'))
 	       {
    		 	
	    var  params = 'countryCode='+$('#countryCode').val()+'&countryName='+$('#countryName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-country-details';
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
				 initializeCountryList();
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
   

	function deletecountrytype()
	{
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
		var  params = 'countryCode='+$('#countryCode').val()+'&countryName='+$('#countryName').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-country-details';
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
				 initializeCountryList();
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
	$('#coun-code').show();
	$('#coun-val').show();


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
