


$(document).ready(function (){
	$("#authority-mgt-form").validationEngine({promptPosition: 'bottomRight'});
	$("#authority-mgt-office-to-user-forwarding-form").validationEngine({promptPosition: 'bottomRight'});
	$("#userId").on('change', showHide);
	$("#serviceType").on('change', showHide);
});

function hideSearchDiv(){
	  $('#userId').val('');
	  $('#serviceType').val('');
	  $('#bar-notify').html('');
	$('#toUser-selection-div').show();
	$('#applicationId-detail-table').hide();
	$("#user-forwarding-detail").hide();
}

	function getApplicationDetail(){
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		$("#search-button").button('loading');
		opld();
		if($('#authority-mgt-form').validationEngine('validate'))
		{
		var params = 'userId='+$('#userId').val()+'&serviceType='+$('#serviceType').val()+'&state='+$('#stateList').val();
		var action = 'get-application-detail-authority-management';	
	
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){
				
				//alert(JSON.stringify(data.list));
            initializeTable(data.list);
            $("#search-button").button('reset');
            ropld();
          },
			error: function(textStatus,errorThrown){
				$("#search-button").button('reset');
				ropld();
				alert('error');			
			}
		});
		//clearForm();
		} else {
			$("#search-button").button('reset');
			ropld();
		}
	}
	function getReApplicationDetail(userId,serviceType,state){
		$('#userId').val(userId);
		$('#serviceType').val(serviceType);
		$('#stateList').val(state)
		var params = 'userId='+userId+'&serviceType='+serviceType+'&state='+state;
		var action = 'get-application-detail-authority-management';	
	
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){
				//alert(JSON.stringify(data.list));
            initializeTable(data.list);
          },
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		//clearForm();
		
	}
function initializeTable(data){
	 $('#tableBody').html('');
	for(var i = 0; i < data.length; i++){
		var data1=data[i];
		var count=i+1;
          //  var  html = '<tr><td>' +'<input type="checkbox" class="tdCheckBox" id="'+data1.applicationId+'" onchange="javascript:addApplicationIdDetail();"/>' + '</td><td>' + data1.applicationId + '</td><td>' + data1.applicantName + '</td><td>' + data1.serviceDesc+ '</td><td>' + data1.currentStatus + '</td></tr>';
		  var  html = '<tr><td>' +'<input type="checkbox" class="tdCheckBox" id="'+data1.applicationId+'" onchange="javascript:addApplicationIdDetail();"/>' + '</td><td>' +count+ '</td><td>' +'<a onclick="getApplicationDetails(\''+data1.applicationId+'\');"><span class="text-info">'+data1.applicationId+'</span></a>' + '</td><td>' + data1.applicantName + '</td><td>' + data1.state+ '</td><td>' +'</td><td>' + data1.serviceDesc+ '</td><td>' + data1.currentStatus + '</td></tr>';
		$('#tableBody').append(html) ;	
	}
	if(data.length>0){
		$('#custom-notification-area').html('');
		$('#table-info-to-user-selection').html('');
	    $('#headCheckBox').prop('checked', false);
        $('#table-info-to-user-selection').append(' <button type="button" class="btn btn-warning btn-xs" onclick="javascript:hideSearchDiv();"><span class="glyphicon glyphicon-menu-left"></span>&nbsp;</button>&nbsp;&nbsp;<span>Applications assigned to <font color="red">'+$('#userId option:selected').text()+'</font> for Service <font color="red">'+$('#serviceType option:selected').text()+'</font><span style="padding-left:200px;">[Only Fresh/Reply Recieved/Pending for Mail cases]</span></span>');
	    //$('#table-info-to-user-selection')append('<span>AAAAAA</span>')
	    $('#applicationId-detail-table').show();
	    $('#toUser-selection-div').hide();
	}
	else{
		$('#custom-notification-area').html('');
		$('#custom-notification-area').append('<div class="alert alert-info"><span><b>Info !!</b> No Applications found for <font color="red">'+$('#userId option:selected').text()+'</font> for Service <font color="red">'+$('#serviceType option:selected').text()+'</font></span></div>');
		$('#applicationId-detail-table').hide();
		$("#user-forwarding-detail").hide();
		$('#toUser-selection-div').show();
	}
}
function showHide(){
	$("#user-forwarding-detail").hide();
	$('#applicationId-detail-table').hide();
	
	 $('#forwardToUser option').prop('disabled', false);
	var selectUser=$('#userId').val();
	$("#forwardToUser option[value='"+selectUser+"']").prop('disabled', true);
}
function addApplicationIdDetail(){
	
	var inputFields='';
    $(".tdCheckBox").each(function(index,object){	
  		if(object.checked){
  			inputFields+=object.id+"-";
  		}	    
	});
    $("#selected-applicationId-list").val(inputFields);
    ShowUserForwardDetail();
}

function changeEffect(){
    // alert($('#headCheckBox').prop('checked'));
	//$('#headCheckBox').on('change', function() {
	    if ($('#headCheckBox').prop('checked')) {
	        $('.tdCheckBox').prop('checked', true);  
	        addApplicationIdDetail();
	       
	    } else {
	        $('.tdCheckBox').prop('checked', false);
	    }
	    ShowUserForwardDetail();
}
function ShowUserForwardDetail(){
    var checkFlag="";	
     $(".tdCheckBox").each(function(index,object){	
      		if(object.checked){
      		    checkFlag="true";	      			
      		}	    
		});	
    if(checkFlag=="true"){
        document.getElementById("user-forwarding-detail").style.display="block";
    }
    else{
   	    document.getElementById("user-forwarding-detail").style.display="none";
    }
 }
function forwardApplicationId(){
	opld();
	$('#bar-notify').html('');
	if($('#authority-mgt-form').validationEngine('validate')){
	var selectedApplicaionId=$('#selected-applicationId-list').val();
	var params = 'forwardingUserId='+$('#forwardToUser').val()+'&applicationIdString='+$('#selected-applicationId-list').val()
	+'&remark='+$('#userRemark').val()+'&fromUser='+$('#userId').val();
	var action = 'forward-application-detail-authority-management';	
    var fromuserId=$('#userId').val();
    var serviceType=$('#serviceType').val();
    var state=$('#stateList').val();
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
		$('#custom-notification-area').html('');
		$("#user-forwarding-detail").hide();
        notifyList(JSON.parse(data[0]),'');
			getReApplicationDetail(fromuserId,serviceType,state);
		ropld();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
	clearForm();
	
	}
}
function getApplicationDetails(appId){
	 var url = 'popup-application-tracking?applicationId='+appId;
	 openLink(url);
}


function clearForm(){	
	$(document).find('.clear').each(function (){
		$(this).val('');
		
		
	});
}
//office to user fetch date  on basis of service and office on button click
function getFreshApplicationDetail(){
	var state= $('#stateList').val();
	var serviceType=$('#serviceType-office-to-user').val();
	var params = 'state='+state+'&serviceType='+serviceType;
	var action = 'get-fresh-application-detail-authority-management';	

	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
        initializeTableOfficetoUser(data.list);
      },
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
	//clearForm();
	
}

function initializeTableOfficetoUser(data){
	
	 $('#freshtableBody').html('');
	for(var i = 0; i < data.length; i++){
		var data1=data[i];
		var count=i+1;
         //  var  html = '<tr><td>' +'<input type="checkbox" class="tdCheckBox" id="'+data1.applicationId+'" onchange="javascript:addApplicationIdDetail();"/>' + '</td><td>' + data1.applicationId + '</td><td>' + data1.applicantName + '</td><td>' + data1.serviceDesc+ '</td><td>' + data1.currentStatus + '</td></tr>';
		  var  html = '<tr><td>' +'<input type="checkbox" class="tdCheckBox-office-to-user" id="'+data1.applicationId+'" onchange="javascript:addApplicationIdDetailOfficeToUser();"/>' + '</td><td>' +count+ '</td><td>' +'<a onclick="getApplicationDetails(\''+data1.applicationId+'\');"><span class="text-info">'+data1.applicationId+'</span></a>' + '</td><td>' + data1.sectionFileNo + '</td><td>' + data1.applicantName+ '</td><td>' + data1.serviceDesc + '</td></tr>';
		$('#freshtableBody').append(html) ;	
	}
	if(data.length>0){
		$('#custom-notification-area').html('');
		$('#table-info-selection').html('');
	    $('#headCheckBox-office-to-user').prop('checked', false);
        $('#table-info-selection').append(' <button type="button" class="btn btn-warning btn-xs" onclick="javascript:hideSearchDivOfficeToUser();"><span class="glyphicon glyphicon-menu-left"></span>&nbsp;</button>&nbsp;&nbsp;<span>Applications assigned for Service <font color="red">'+$('#serviceType-office-to-user option:selected').text()+'</font> And State <font color="red">'+$('#stateList option:selected').text()+'</font><span style="padding-left:200px;">[Only Fresh]</span></span>');
	    $('#office-to-user-applicationId-detail-table').show();
	    $('#offoce-to-user-fetch-fresh-data-filter').hide();
	}
	else{
		$('#custom-notification-area').html('');
		$('#custom-notification-area').append('<div class="alert alert-info"><span><b>Info !!</b> No Applications found for <font color="red">'+$('#state option:selected').text()+'</font> for Service <font color="red">'+$('#serviceType-office-to-user option:selected').text()+'</font></span></div>');
		$('#office-to-user-applicationId-detail-table').hide();
		$("#user-forwarding-detail-office-to-user").hide();
		$('#offoce-to-user-fetch-fresh-data-filter').show();
	}
}

function changeEffectOfficeToUser(){
	
    // alert($('#headCheckBox').prop('checked'));
	//$('#headCheckBox').on('change', function() {
	    if ($('#headCheckBox-office-to-user').prop('checked')) {
	        $('.tdCheckBox-office-to-user').prop('checked', true);  
	        addApplicationIdDetailOfficeToUser();
	       
	    } else {
	        $('.tdCheckBox-office-to-user').prop('checked', false);
	    }
	   ShowUserForwardDetailOfficeToUser();
}
function addApplicationIdDetailOfficeToUser(){
	
	var inputFields='';
    $(".tdCheckBox-office-to-user").each(function(index,object){	
  		if(object.checked){
  			inputFields+=object.id+"-";
  		}	    
	});
    $("#selected-applicationId-list-office-to-user").val(inputFields);
    ShowUserForwardDetailOfficeToUser();
}
function ShowUserForwardDetailOfficeToUser(){
    var checkFlag="";	
     $(".tdCheckBox-office-to-user").each(function(index,object){	
      		if(object.checked){
      		    checkFlag="true";	      			
      		}	    
		});	
    if(checkFlag=="true"){
        document.getElementById("user-forwarding-detail-office-to-user").style.display="block";
    }
    else{
   	    document.getElementById("user-forwarding-detail-office-to-user").style.display="none";
    }
 }
function hideSearchDivOfficeToUser(){
	$('#bar-notify').html('');
	$('#offoce-to-user-fetch-fresh-data-filter').show();
	$('#office-to-user-applicationId-detail-table').hide();
	$("#user-forwarding-detail-office-to-user").hide();
	document.getElementById("stateList").value = "All";
	document.getElementById("serviceType-office-to-user").value = "All";
}

function forwardApplicationIdOfficeToUser(){
	opld();
	$('#bar-notify').html('');
	if($('#authority-mgt-office-to-user-forwarding-form').validationEngine('validate')){
	var selectedApplicaionId=$('#selected-applicationId-list').val();
	var params = 'forwardToUserFresh='+$('#forwardToUserFresh').val()+'&applicationIdString='+$('#selected-applicationId-list-office-to-user').val();
	var action = 'forward-office-to-user-authority-management';	
    var state=$('#stateList').val();
    var serviceType=$('#serviceType-office-to-user').val();
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
		$('#custom-notification-area').html('');
		$("#offoce-to-user-fetch-fresh-data-filter").hide();
        notifyList(JSON.parse(data[0]),'');
			getReFreshApplicationDetail(state,serviceType);
			ropld();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
	clearForm();
	
	}
}

function getReFreshApplicationDetail(state,serviceType){
	$('#stateList').val(state);
	$('#serviceType-office-to-user').val(serviceType);
	var params = 'state='+state+'&serviceType='+serviceType;
	var action = 'get-fresh-application-detail-authority-management';	

	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
        initializeTableOfficetoUser(data.list);
      },
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
	//clearForm();
	
}
