


$(document).ready(function (){
	initializeDesginationList();
	$("#designation-type-form").validationEngine({promptPosition: 'bottomRight'});

});

function initializeDesginationList() {
	$("#Designation-list").html('');
	$("#Designation-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-designation',
    		defaultsortcolumn:'enteredOn',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Designation Id', name:'designationId', sortable:true}, 
				{title:'Designation Name', name:'designationName',sortable:true}, 
				{title:'Short Designation Name',name:'shortDesignation',sortable:true},
			    {title:'Entered On', name:'enteredOn',sortable:true},
							],			
			onRowSelect:function(rowdata){getDesignationDetails(rowdata);}
    		});	

	
}	
	function getDesignationDetails(rowdata){
		
		$('#designationId').val(rowdata.designationId);
		$('#designationName').val(rowdata.designationName);
		$('#shortDesignation').val(rowdata.shortDesignation);
		$('#enteredOn').val(rowdata.enteredOn);
		$('#table-btn').show();
        $('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#Designation-list').show();
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function adddesignationdetails(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#designation-type-form').validationEngine('validate'))
		{
		var  params = 'designationName='+$('#designationName').val()+'&shortDesignation='+$('#shortDesignation').val()+'&requestToken='+$('#requestToken').val();
		var action = 'add-designation';		
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){	
				$.each(JSON.parse(data[0]),function(index,item){
					if(item.s=="e"){
						$('#close-modal-btn').click();
						notifyList(JSON.parse(data[0]),'');	
					      var token = JSON.parse(data[2]).li;
							if(token != null && token != '')
							$('#requestToken').val(token);
						    hideAll();
					}
					else if(item.s=="w"){
						$('#close-modal-btn').click();
						notifyList(JSON.parse(data[0]),'');	
					      var token = JSON.parse(data[2]).li;
							if(token != null && token != '')
							$('#requestToken').val(token);
						    hideAll();
					}
					else if(item.s=="s"){
						notifyList(JSON.parse(data[0]),'');	
					      var token = JSON.parse(data[2]).li;
							if(token != null && token != '')
							$('#requestToken').val(token);
							$('#designationId').val(JSON.parse(data[1]));
						initDesignation();
					}
				});		
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		
		
		}

	}
	function initDesignation(){
		$('#details-div').hide();
		$('#Designation-list').hide();
		$('#add-btn').hide();
		$('#close-modal-btn').click();
		$('#des-div').show();
		$('#add-details-btn').hide();
		initializeDesginationList();
		clearForm();
		initDesignationList();
	}
	function initDesignationList(){
		var params = 'designationId='+$('#designationId').val();
		var action = 'get-desassign-designation';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				populateDesignationList(JSON.parse(data[0]),JSON.parse(data[1]));			
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		clearForm();
	}
	function populateDesignationList(availableList,assignedList){
		
		var avpl = document.getElementById("avpl");
		var aspl = document.getElementById("aspl");
		avpl.length = 0;
		aspl.length = 0;		
	    $.each(availableList, function(index, item) {	    	
	    		avpl[avpl.length] = new Option(item.v, item.k);
	    	});
	    $.each(assignedList, function(index, item) {
	    		aspl[aspl.length] = new Option(item.v, item.k);
	    	}); 		
	}
	function addRemoveRoles(fromObjName, toObjName){	
		var value=$('#'+fromObjName+' option:selected').index();	
	    $('#'+fromObjName+' option:selected').each(function() {
	       	$("#"+toObjName).append($(this).clone());
	       	$(this).remove();
	    });    
	   $('#'+fromObjName+' option:nth('+value+')').attr("selected", "selected");  
	   $('#'+toObjName).each(function() {	   
	     	$('#'+toObjName+' option:selected').removeAttr("selected", "selected");      	
	  });
	}
	
	

	function editdesignationtype()	{	
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#designation-type-form').validationEngine('validate'))	
			{
		var  params = 'designationName='+$('#designationName').val()+'&shortDesignation='+$('#shortDesignation').val()+'&designationId='+$('#designationId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-designation';
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
			      initializeDesginationList();
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

	function deletedesignationtype()
	{  
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		var params = 'designationId='+$('#designationId').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-designation';
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
			      initializeDesginationList();
		         showadd();
			
		        
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		clearForm();
	}
	function editassignoffice(){
		$('#fresh-des-save').hide();
		$('#edit-des-save').show();
		$('#des-div').show();	
		$('#Designation-list').hide();
		$('#details-div').hide();
		$('#edit-actions').hide();
		initDesignationList();
	}
	function saveDetails(flag){	
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
	    for (var i=aspl.length - 1; i >= 0; i--){
	    	aspl.options[i].selected = true;
	    } 
	   
	    var params = 'designationId='+$('#designationId').val()+'&aspl='+$('#aspl').val()+'&requestToken='+$('#requestToken').val();
		var action = 'save-designation';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				notifyList(JSON.parse(data[0]),'');
			      var token = JSON.parse(data[1]).li;
					if(token != null && token != '')
					$('#requestToken').val(token);
				if(flag=="FRS")
					hideAll();			
				else if(flag=="ERS")			
					$('#des-div').hide();
				initializeDesginationList();
				 showview();
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		}); 
	}
	
	

function initForm(){	
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();

}
function showview(){
	$('#edit-actions').hide();
	$('#Designation-list').show();
	$("#form-div").hide();	
	$('#add-btn').show();
	$('#des-div').hide();
	$('#table-btn').hide();
	$('#details-div').hide();
	$('#add-details-btn').hide();
	clearForm();

	}
function showadd()
{
	$('#table-btn').hide();
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}

