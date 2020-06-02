


$(document).ready(function (){
	initializepcList();
	$("#pcSection-type-form").validationEngine({promptPosition: 'bottomRight'});

});

function initializepcList() {
	$("#pcsection-list").html('');
	$("#pcsection-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-office-section-details',
    		defaultsortcolumn:'enteredOn',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Section Id', name:'sectionId', sortable:true}, 
				{title:'Section Name', name:'sectionName',sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getpcsection(rowdata);}
    		});
	
}	
	function getpcsection(rowdata){
		
		$('#sectionId').val(rowdata.sectionId);
		$('#sectionName').val(rowdata.sectionName);
		$('#enteredOn').val(rowdata.createdDate);
		$('#table-btn').show();
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#pcsection-list').show();
		$('#table-btn').hide();
		
		}	
	
	function CreateSection(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#pcSection-type-form').validationEngine('validate'))
		{
	
	     var params = 'sectionName='+$('#sectionName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'create-office-section-details';	
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
							$('#sectionId').val(JSON.parse(data[1]));
						     initActions();
					}
				});		
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
		clearForm();
	}
	
	
	
	function initService(){
		var params = 'sectionId='+$('#sectionId').val();
		var action = 'get-service-office-section-details';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				populateServiceList(JSON.parse(data[0]),JSON.parse(data[1]));			
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		clearForm();
	}
	function populateServiceList(availableList,assignedList){
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
	function addRemoveService(fromObjName, toObjName){	
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

	function saveDetails(flag){	
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
	    for (var i=aspl.length - 1; i >= 0; i--){
	    	aspl.options[i].selected = true;
	    }  
	    var params = 'sectionId='+$('#sectionId').val()+'&aspl='+$('#aspl').val()+'&requestToken='+$('#requestToken').val();
		var action = 'save-office-section-details';	
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
					$('#section-div').hide();
				initializepcList();
				 showlist();
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		}); 
	}
	
	function editassignservice(){
		$('#fresh-section-save').hide();
		$('#edit-section-save').show();
		$('#section-div').show();	
		$('#pcsection-list').hide();
		$('#details-div').hide();
		$('#edit-actions').hide();
		$('#table-btn').show();
		initService();	
	}
	
	
	function editsection()
	{
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		var params = 'sectionId='+$('#sectionId').val()+'&sectionName='+$('#sectionName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-office-section-details';
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
				initializepcList();
				showadd();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
				
			}
		});
		clearForm();
	}

	
	function deletesection(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');	
		var params = 'sectionId='+$('#sectionId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'delete-office-section-details';	
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
				initializepcList();
				 showadd();
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		clearForm();
	}
	

    function viewtable(){
    clearForm();
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();

}
    function showlist(){
	$('#edit-actions').hide();
    $("#form-div").hide();	
	$('#section-div').hide();
	$('#table-btn').hide();
	$('#details-div').hide();
	$('#add-details-btn').hide();
	$('#pcsection-list').show();
	$('#add-btn').show();
	
	
	}
function showadd()
{
	$('#table-btn').hide();
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#pcsection-list').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}

function initActions(){
	
	$('#table-btn').show();
	$('#details-div').hide();
	$('#pcsection-list').hide();
	$('#add-btn').hide();
	$('#close-modal-btn').click();
	$('#section-div').show();
	$('#add-details-btn').hide();
	initializepcList();
	initService();
}



function clearForm(){	
	$(document).find('.clear').each(function (){
		$(this).val('');
		
		
	});
}