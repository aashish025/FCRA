


$(document).ready(function (){
	initializeRoleList();
	$("#role-type-form").validationEngine({promptPosition: 'bottomRight'});

});

function initializeRoleList() {
	$("#Role-list").html('');
	$("#Role-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-role-details',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Role Id', name:'roleId', sortable:true}, 
				{title:'Role Name', name:'roleName',sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getroleDetails(rowdata);}
    		});	
	clearForm();
	
}	
	function getroleDetails(rowdata){
		
		$('#roleId').val(rowdata.roleId);
		$('#roleName').val(rowdata.roleName);
		$('#createdDate').val(rowdata.createdDate);
		$('#table-btn').show();
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#Role-list').show();
		
		}	
	
	function Createrole(){
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
		if($('#role-type-form').validationEngine('validate'))
		{
	     var params = 'roleName='+$('#roleName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'create-role-details';	
	
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
							$('#roleId').val(JSON.parse(data[1]));
							initActions();
					}
				});		

			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
	}
	function editassignrole(){
		$('#fresh-role-save').hide();
		$('#edit-role-save').show();
		$('#role-div').show();	
		$('#Role-list').hide();
		$('#details-div').hide();
		$('#edit-actions').hide();
		initRole();	
	}
	function editassignlevel(){
		$('#fresh-user-save').hide();
		$('#edit-user-save').show();
		$('#user-div').show();	
		$('#Role-list').hide();
		$('#details-div').hide();
		$('#edit-actions').hide();
		initLevel();	
	}
	function initLevel(){
		var params = 'roleId='+$('#roleId').val();
		var action = 'get-assignlevel-role-details';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				populateUserList(JSON.parse(data[0]),JSON.parse(data[1]));			
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		
	}
	
	function populateUserList(availableList,assignedList){
		var avpl1 = document.getElementById("avpl1");
		var aspl1 = document.getElementById("aspl1"); 
		
      	avpl1.length = 0;
		aspl1.length = 0;
	    $.each(availableList, function(index, item) {	    	
	    		avpl1[avpl1.length] = new Option(item.v, item.k);
	    	});
	    $.each(assignedList, function(index, item) {
	    		aspl1[aspl1.length] = new Option(item.v, item.k);
	    	});	    
	}
	function addRemoveUser(fromObjName, toObjName){	
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
	function saveuserDetails(flag){	
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
	    for (var i=aspl1.length - 1; i >= 0; i--){
	    	aspl1.options[i].selected = true;
	    }    
	    var params = 'roleId='+$('#roleId').val()+'&aspl1='+$('#aspl1').val()+'&requestToken='+$('#requestToken').val();
		var action = 'save-userlevel-role-details';	
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
					$('#user-div').hide();
				initializeRoleList();
				 showView();
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		}); 
	}


	function deleterole(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');	
		var params = 'roleId='+$('#roleId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'delete-role-details';	
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
				initializeRoleList();
				 showadd();
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
	
	function initRole(){
		var params = 'roleId='+$('#roleId').val();
		var action = 'get-assign-role-details';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				populateRoleList(JSON.parse(data[0]),JSON.parse(data[1]));			
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		clearForm();
	}
	
	function populateRoleList(availableList,assignedList){
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
	function saveDetails(flag){	
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
	    for (var i=aspl.length - 1; i >= 0; i--){
	    	aspl.options[i].selected = true;
	    }    
	    var params = 'roleId='+$('#roleId').val()+'&aspl='+$('#aspl').val()+'&requestToken='+$('#requestToken').val();
		var action = 'save-role-details';	
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
					$('#role-div').hide();
				initializeRoleList();
				 showView();
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		}); 
	}
	function editrole()
	{
		$('#bar-notify').html('');
		$('#sticky-notify').html('');
		var params = 'roleId='+$('#roleId').val()+'&roleName='+$('#roleName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-role-details';
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
				initializeRoleList();
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
function showView(){
	$('#edit-actions').hide();
    $("#form-div").hide();	
	$('#role-div').hide();
	$('#table-btn').hide();
	$('#details-div').hide();
	$('#add-details-btn').hide();
	$('#user-div').hide();
	$('#Role-list').show();
	$('#add-btn').show();
	
	
	}
function showadd()
{
	$('#table-btn').hide();
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}



function populateGetDetails(){
	$('#Role-list').show();
	$('#edit-actions').show();
	$('#form-div').show();
	$('#add-btn').show();	
	$('#add-details-btn').show();
}
function initActions(){
	
	$('#details-div').hide();
	$('#Role-list').hide();
	$('#add-btn').hide();
	$('#close-modal-btn').click();
	$('#role-div').show();
	$('#add-details-btn').hide();
   initializeRoleList();
	clearForm();
	initRole();
}
function clearForm(){	
	$(document).find('.clear').each(function (){
		$(this).val('');
		
		
	});
}