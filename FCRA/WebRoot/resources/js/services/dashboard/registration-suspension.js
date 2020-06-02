function toggleSearch(){
	if ( $('#app-search').is(':visible') ){		
		$('#app-search').hide();
		$('#name-search').show();	
	}else if($('#name-search').is(':visible')){		
		$('#app-search').show();	
		$('#name-search').hide();		
	}
}
function populateSelectBox(list, listHeadText, id) {
	var selectBox = $('#'+id);
	selectBox.empty();
	if(listHeadText != null && listHeadText != '')
	selectBox.append($('<option/>', {value:'', text:listHeadText}));
	$.each(list, function(index, item) {
		selectBox.append($('<option/>', {value:item.li, text:item.ld}));
	});
};
function clearModal(){
	$('#cancelRemark').val('');
	$('#revokeRemark').val('');
	$('#cancellationType').val('');
	$('input[type=checkbox]').each(function () {
        $(this).removeAttr('checked');
	});	
	$('#requestDate').val('');
	$('#bar-notify').html('');
}
function getApplicationList(){	
/* Initialize Hide and Show for Application Details */
	$("#app-list").html('');	
	$('#bar-notify').html('');
	$('#app-info').hide();
	$('#actions').hide();
	$("#app-list").bootgrid(
    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'get-application-list-registration-suspension?applicationId='+$('#applicationId').val()+'&applicationName='+$('#applicationName').val(),
	    		columndetails:
				[
					{title:'Registration Number', name:'assoRegnNumber',sortable:true},					
					{title:'Section File No', name:'sectionFileNo',sortable:true},
					{title:'Applicant / Association Name', name:'applicantName'},					
				],
				onRowSelect:function(rowdata){getApplicationDetails(rowdata);}
    });	
	$('#application-list-div').show();
}

function getApplicationDetails(data){	
	$('#bar-notify').html('');
	var params = 'appId='+data.assoRegnNumber;
	var action = 'get-application-details-registration-suspension';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');						
			initApplicationDetails(JSON.parse(data[0])[0]);			
		//	prepareCheckboxList(JSON.parse(data[2]), 'cancellationReason-div', 'cancellationReason', 'cancellationReason');
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}
function initApplicationDetails(data){	
	/* Initialize Hide and Show for Application Details */
	//alert(data.assoRegnStatus);
		$('#app-info').show();	
		$('#application-list-div').hide();	
		$('#actions').show();
	//	if(data.assoRegnStatus=="" || data.assoRegnStatus==null ||data.assoRegnStatus=='3'){
		if(data.assoRegnStatus=='0'){
			$('#revoke-btn').hide();
			$('#cancel-btn').show();
		}
		//else if(data.assoRegnStatus=="1" || data.assoRegnStatus=="0" ){
		else if(data.assoRegnStatus=="3" ){
			$('#cancel-btn').hide();
			$('#revoke-btn').show();
		}
		else {
			$('#revoke-btn').hide();
			$('#cancel-btn').hide();
			
		}
		
	/* Initialize Hide and Show for Application Details */
		prepareBasicInfo(data);	
}
function prepareBasicInfo(data){	
	$('#regnNumber').html(data.assoRegnNumber);
	$('#secFileNumber').html(data.sectionFileNo);
	$('#applicantName').html(data.applicantName);
	$('#currentStatus').html(data.assoRegnStatusDesc);	
	$('#assoAddress').html(data.assoAddress);
	$('#assoNature').html(data.assoNature);
	$('#assoBank').html(data.assoBankAddress);	
	if(data.assoRegnStatus=="0")
		$("#document-container").hide();
	else if (data.assoRegnStatus == "1" && data.historyStatus =="1")
		$("#document-container").show();
	else
		$("#document-container").hide();

}
function cancelRegistration1(){	
	$('#bar-notify').html('');
	if($('#cancel-form').validationEngine('validate')){		
		var params=$('#cancel-form').serialize();
		params=params+'&appId='+$('#regnNumber').html()+'&requestToken='+$('#requestToken').val();		
		var action = 'cancel-registration-registration-suspension';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[0]=="success"){
					notifyList(JSON.parse(data[1]),'');
					  var token = JSON.parse(data[2]).li;
						if(token != null && token != '')
						$('#requestToken').val(token);
					postWork();
					//getCancelReport();
				}else{
					notifyList(JSON.parse(data[1]),'');
				}
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function revokeRegistration(){
	$('#bar-notify').html('');
	if($('#revoke-form').validationEngine('validate')){
		var params = 'appId='+$('#regnNumber').html()+'&remark='+$('#revokeRemark').val()+'&requestToken='+$('#requestToken').val();
		var action = 'revoke-registration-registration-suspension';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[0]=="success"){
					notifyList(JSON.parse(data[1]),'');		
					  var token = JSON.parse(data[2]).li;
						if(token != null && token != '')
						$('#requestToken').val(token);
					postWork();
				}else{
					notifyList(JSON.parse(data[1]),'');
				}
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function postWork(){	
	$('#actions').hide();
	$('#app-info').hide();
	$('#cancelModal-close-btn').click();
	$('#revokeModal-close-btn').click();
	$('#cancelRemark').val('');
	$('#revokeRemark').val('');
	$('#cancellationType').val('');
	$('#cancellationReason').val('');
	$('#requestDate').val('');
	$('#applicationName').val('');
	$('#applicationId').val('');
}	
	
	
function getCancelReport(){
	alert(2);
	var tUrl = 'download-cancel-report-registration-suspension?'+'appId='+$('#regnNumber').text();
	openLink(tUrl);
}
	
	
	
/*function prepareCheckboxList(list, containerId, id, name) {	
	var divObj = $('#'+containerId);
	var table = $('<table/>', {'class':'checkbox-table'}).append($('<tbody/>', {}));
	var tbody = table.find('tbody');
	var numCols = 1;

	divObj.empty();
	$.each(list, function(index, item){
		if(index%numCols == 0)
			tbody.append($('<tr/>', {}));
		tbody.find('tr:last').append(
				$('<td/>', {}).append(
						$('<label/>', {'class':'checkbox-inline', 'text':item.ld}).prepend(
								$('<input/>', {'type':'checkbox', 'id':id+'-'+item.li, 'name':name, 'value':item.li}))));
	});
	divObj.append(table);
}

function cancelRegistration(){
	//alert($('#regnNumber').text());  
	if($('#cancel-form').validationEngine('validate')){		
		var params=$('#cancel-form').serialize();
		params=params+'&appId='+$('#regnNumber').html();	
		var tUrl = 'cancel-report-registration-suspension?'+params;
		openLink(tUrl);
		postWork();
	}
}

function getRequestDate(value){
	if(value=="R"){
		$('#request-date-div').show();
	}else{
		$('#request-date-div').hide();		
	}
}*/

