function getApplicationManagementList(){	//alert(1);
	
	$('#bar-notify').html('');
	 $("#app-info").hide();
	 $("#reopen-div").hide();
		var applicationId=$('#applicationId').val();
		var params = 'applicationId='+applicationId;
		var action = 'get-application-status-management';	
		
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){
										
				$('#bar-notify').html('');
				notifyList(JSON.parse(data[1]),'');
				
						{
				$.each(JSON.parse(data[0]),function(index,item){
					$('#bar-notify').html('');
					var currentStatus=item.currentStatus;
					
					if(currentStatus=='12' || currentStatus=='10')
						{
						//alert();
						
							$('#appStatusId').val(item.applicationId);
							$('#appId').text(item.applicationId);
							$('#appName').text(item.applicationName);
							$('#serviceName').text(item.serviceName);
							$('#serviceCode').val(item.serviceCode);
							$('#SubmissionDate').text(item.submissionDate);
							$('#currentStatus').text(item.filestatusName);
							$('#sectionFile').text(item.sectionFileNo);	
							$('#TempFile').text(item.tempFileNo);
							  $("#app-info").show();
							  $("#reopen-div").show();
							  $("#cancel-div").hide();
							  
							  var mylist = null;
							  mylist = JSON.parse(data[2]);
							populateSelectBox(mylist, 'Select User', 'userlist');
						}
					else
						{
							$('#bar-notify').html('');
							notifyList(JSON.parse(data[1]),'');
							 $("#app-info").hide();
							 $("#reopen-div").hide();
							 $("#cancel-div").hide();
						}
	          
				});	
							}
			},
		error: function(textStatus,errorThrown){
			alert('error');
			$("#gate-add-button").button('reset');
		}
	});
			
			
}

function reopen(){
	$("#close-form").validationEngine({promptPosition: 'bottomRight'});
	$('#bar-notify').html('');	
	$('#sticky-notify').html('');	
	 $("#cancel-div").hide();
	 $("#app-info").show();
	 if($('#close-form').validationEngine('validate') ) {	
	var applicationId=$('#appStatusId').val();
	var serviceCode=$('#serviceCode').val();
	
	var remark=$('#statusRemark').val();
	var userId=$('#userlist').val();
	var params = 'applicationId='+applicationId+'&serviceCode='+serviceCode+'&remark='+remark+'&userId='+userId;
	var action = 'reopen-application-status-management';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data) {			
			if(data[1]=="success"){				
				notifyList(JSON.parse(data[0]),'');
				$("#app-info").hide();
				$("#reopen-div").hide();				
				$('#closeModal-close-btn').click();
			}else{
				notifyList(JSON.parse(data[0]),'');
			}			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
}	
function clearForm(){
	$('#applicationId').val('');
	$('#statusRemark').val('');
	$('#userlist').val('');	
	$('#reopenModal-error').html('');
}
function populateSelectBox(list, listHeadText, id) {
	//alert(list);
	var selectBox = $('#'+id);
	selectBox.empty();	
	selectBox.append($('<option/>', { value:'',  text:listHeadText}));
	$.each(list, function(index, item) {		
		selectBox.append($('<option/>', {value:item.k, text:item.v}));
	});
}


///populateSelectBox(serviceTypeList, 'ALL', 'service-type');
