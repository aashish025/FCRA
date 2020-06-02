


$(document).ready(function (){
	initializeSubstageList();
	$("#substagedocument-type-form").validationEngine({promptPosition: 'bottomRight'});

});

function initializeSubstageList() {
	$("#substagedocument-list").html('');
	$("#substagedocument-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-substage-document-details',
    		defaultsortcolumn:'proposalDesc',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:' Proposal Type', name:'proposalDesc', sortable:true}, 
				{title:' Sub Stage', name:'substageDesc',sortable:true}, 
			    {title:'Project Document Type', name:'documentDesc',sortable:true}
			],			
			onRowSelect:function(rowdata){getSubstageDocumentDetail(rowdata);}
    		});	
	clearForm();
	
}	
	function getSubstageDocumentDetail(rowdata){
		
		$('#proposalDesc').val(rowdata.proposalId);
		$('#substageDesc').val(rowdata.substageId);
		$('#documentDesc').val(rowdata.documentId);
		$('#rowId').val(rowdata.rowIdentifier);
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#substagedocument-list').show();
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function adddocument(){
		 $('#bar-notify').html('');
		 $('#sticky-notify').html('');
		if($('#substagedocument-type-form').validationEngine('validate')){
		var params = 'substageDesc='+$('#substageDesc').val()+'&proposalDesc='+$('#proposalDesc').val()+'&documentDesc='+$('#documentDesc').val()+'&requestToken='+$('#requestToken').val();
		var action = 'add-substage-document-details';		
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
		      initializeSubstageList();
		      MyinitForm();
				showadd();				
			},
			error: function(textStatus,errorThrown){
				alert('error');				
			}
		});
		clearForm();
		 	
	}
	}

	function editdocument()
	{	$('#sticky-notify').html('');		
		$('#bar-notify').html('');
		 if($('#substagedocument-type-form').validationEngine('validate'))
			{

				var params = 'substageDesc='+$('#substageDesc').val()+'&proposalDesc='+$('#proposalDesc').val()+'&documentDesc='+$('#documentDesc').val()+'&rowId='+$('#rowId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-substage-document-details';
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
				initializeSubstageList();
				MyinitForm();
				showadd();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
				
			}
		});
		
	}
	}
	
	function deletedocument()
	{ $('#sticky-notify').html('');
		$('#bar-notify').html('');
		var params = 'rowId='+encodeURIComponent($('#rowId').val())+'&requestToken='+$('#requestToken').val();
		var action = 'delete-substage-document-details';
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
				initializeSubstageList();
				MyinitForm()
				showadd();
									
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		clearForm();
	}



	
function showadd()
{
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}


function MyinitForm(){	
	  
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();

}
