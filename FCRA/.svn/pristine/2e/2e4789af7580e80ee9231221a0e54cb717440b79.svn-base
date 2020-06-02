$(document).ready(function (){
	$("#user-form").validationEngine({promptPosition: 'bottomRight'});
    initcheckoffice();
	initUserList();	
});
function initUserList() {
	$("#user-list").html('');
	$("#user-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-user-list-user-details',
    		columndetails:
			[
				{title:'User Id', name:'userid',sortable:true}, 
				{title:'Name', name:'userName', sortable:true}, 
				{title:'Gender', name:'genderDesc'},
				{title:'Designation', name:'designationName'},
				{title:'User Staus', name:'statusName',sortable:true}
			],
			onRowSelect:function(rowdata){getUser(rowdata);}
    		});	
}
function initcheckoffice(){
var action = 'office-user-details';	

	$.ajax({
		url: action,
		method:'GET',
		dataType:'json',
		success: function(data){
			if(data=="true")
				{
				$('#edit-sec-btn').show();
				}
			else
				{
				$('#edit-sec-btn').hide();
				}
				
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
function createUser(){
	$('#bar-notify').html('');
	$('#user-error').html('');
	var params = 'name='+$('#name').val()+'&email='+$('#email').val()+'&gender='+$('#gender').val()+'&designation='+$('#designation').val();
	var action = 'create-user-details';
	if($('#user-form').validationEngine('validate')){
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				$.each(JSON.parse(data[0]),function(index,item){
					if(item.s=="e"){						
						notifyList(JSON.parse(data[0]),'');
						hideAll();
					}
					else if(item.s=="s"){
						notifyList(JSON.parse(data[0]),'');			
						$('#userid').val(JSON.parse(data[1]));
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
function getUser(rowdata){	
	initcheckoffice();
	$('#add-btn').hide();
	$('#edit-actions').show();	
	$('#userid').val(rowdata.userid);
	$('#name').val(rowdata.userName);
	$('#email').val(rowdata.emailId);
	$('#gender').val(rowdata.genderCode);
	$('#designation').val(rowdata.designationId);	
	if(rowdata.statusName=="Locked")
		$('#unlock-usr-btn').show();
	
	// for user info
	$('#user-info-head-div').html('<div class="col-sm-4"><h3 class="text-info">'+rowdata.userid+'</h3></div>');
	$('#p-name').html(rowdata.userName);
	$('#p-email').html(rowdata.emailId);
	$('#p-gender').html(rowdata.genderDesc);
	$('#p-designation').html(rowdata.designationName);
	$('#table-btn').show();
	$('#user-info-div').show();
	$('#user-list').hide();
    $('#bar-notify').html('');
}
function editRoles(){
	$('#fresh-role-save').hide();
	$('#edit-role-save').show();
	$('#role-div').show();	
	$('#sec-div').hide();
	initUserRole();	
}
function openEditUserModel(){	
	$('#create-user-div').hide();
	$('#edit-user-div').show();
	$('#addBtn').click();
}

function editUser(){
	$('#bar-notify').html('');	
	$('#user-error').html('');	
	var params = 'name='+$('#name').val()+'&email='+$('#email').val()+'&gender='+$('#gender').val()+'&designation='+$('#designation').val()
				+'&userid='+$('#userid').val();
	var action = 'edit-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			$.each(JSON.parse(data),function(index,item){
				if(item.s=="e"){						
					notifyList(JSON.parse(data),'');
					hideAll();					
				}
				else if(item.s=="s"){
					notifyList(JSON.parse(data),'');			
					$('#close-modal-btn').click();
					clearForm();
					hideAll();
					initUserList();	
				}
			});			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
function resetPassword(){
	$('#role-div').hide();
	$('#bar-notify').html('');	
	var params = 'userid='+$('#userid').val();
	var action = 'reset-password-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');	
			$('#role-div').hide();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
function unlockUser(){
	$('#bar-notify').html('');	
	var params = 'userid='+$('#userid').val();
	var action = 'unlock-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');			
			initUserList();	
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
function deleteUser(){
	$('#bar-notify').html('');	
	var params = 'userid='+$('#userid').val();
	var action = 'delete-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			initUserList();
			hideAll();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
function editSection(){
	$('#fresh-sec-save').hide();
	$('#edit-sec-save').show();
	$('#role-div').hide();
	$('#sec-div').show();	
	initSection();	
}
function initSection(){
	var params = 'userid='+$('#userid').val();
	var action = 'get-section-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			populateSectionList(JSON.parse(data[0]),JSON.parse(data[1]));			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
	
}

function populateSectionList(availableList,assignedList){
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
function addRemoveSection(fromObjName, toObjName){	
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
function showTable(){
	$('#table-btn').hide();
	$('#user-info-div').hide();
	$('#user-list').show();
	$('#role-div').hide();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$('#edit-user-div').hide();
	$('#create-user-div').show();
	$('#fresh-role-save').show();
	$('#edit-role-save').hide();
	$('#sec-div').hide();
	$('#bar-notify').html('');	
	clearForm();
}

function initActions(){
	$('#user-list').hide();
	$('#add-btn').hide();
	$('#close-modal-btn').click();
	$('#role-div').show();	
	clearForm();
	initUserRole();
}
function clearForm(){	
	$(document).find('.clear').each(function (){
		$(this).val('');
	});
}
function initUserRole(){	
	var params = 'userid='+$('#userid').val();
	var action = 'get-role-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			populateRoleLists(JSON.parse(data[0]),JSON.parse(data[1]));			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
function hideAll(){
	$('#table-btn').hide();
	$('#user-info-div').hide();
	$('#user-list').show();
	$('#role-div').hide();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$('#sec-div').hide();
}
function populateRoleLists(availableList,assignedList){
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
    for (var i=aspl.length - 1; i >= 0; i--){
    	aspl.options[i].selected = true;
    }    
    var params = 'userid='+$('#userid').val()+'&aspl='+$('#aspl').val();
	var action = 'save-role-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			if(flag=="FRS")
				hideAll();			
			else if(flag=="ERS")			
				$('#role-div').hide();
			initUserList();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	}); 
}
function savesectionDetails(flag){	
	
	$('#bar-notify').html('');
    for (var i=aspl1.length - 1; i >= 0; i--){
    	aspl1.options[i].selected = true;
    }    
    var params = 'userid='+$('#userid').val()+'&aspl1='+$('#aspl1').val();
	var action = 'save-section-user-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			notifyList(JSON.parse(data),'');
			if(flag=="FRS")
				hideAll();			
			else if(flag=="ERS")			
				$('#sec-div').hide();
			initUserList();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	}); 
}

