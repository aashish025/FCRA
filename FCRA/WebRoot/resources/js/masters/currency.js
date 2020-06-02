


$(document).ready(function (){
	initializeCurrencyList();
	$("#currency-type-form").validationEngine({promptPosition: 'bottomLeft'});
});

function initializeCurrencyList() {
	$("#Currency-list").html('');
	$("#Currency-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-currency',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Currency Code', name:'currencyCode', sortable:true}, 
				{title:'Currency Name', name:'currencyName',sortable:true}, 
				{title:'Entered On', name:'createdDate',sortable:true},
							],			
			onRowSelect:function(rowdata){getcurrencyDetails(rowdata);}
    		});	
	clearForm();
	
	
}	
	function getcurrencyDetails(rowdata){
		
		$('#currencyCode').val(rowdata.currencyCode);
		$('#currencyName').val(rowdata.currencyName);
		$('#createdDate').val(rowdata.createdDate);
        $('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#Currency-list').show();
		$('#curr-code').hide();
		$('#curr-val').hide();
	
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addcurrencydetails(){
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
		if($('#currency-type-form').validationEngine('validate'))
	       {
			      	
					var  params = 'currencyCode='+$('#currencyCode').val()+'&currencyName='+$('#currencyName').val()+'&requestToken='+$('#requestToken').val();
					var action = 'add-currency';		
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
					      initializeCurrencyList();
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
    function editcurrencytype()	{
    	$('#sticky-notify').html('');
    		$('#bar-notify').html('');
    		if($('#currency-type-form').validationEngine('validate'))
    			{
		var  params = 'currencyCode='+$('#currencyCode').val()+'&currencyName='+$('#currencyName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-currency';
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
				 initializeCurrencyList();
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

	function deletecurrencytype()
	{    $('#sticky-notify').html('');
		$('#bar-notify').html('');
		var  params = 'currencyCode='+$('#currencyCode').val()+'&currencyName='+$('#currencyName').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-currency';
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
				 initializeCurrencyList();
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
	$('#curr-code').show();
	$('#curr-val').show();


}
function showTable1(){
	$('#edit-actions').hide();
	$('#Currency-list').show();
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

