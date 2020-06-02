
var myOfficeId=null;

$(document).ready(function (){	
	$("#investigation-received-form").validationEngine({promptPosition: 'bottomRight'});
});

function getApplicationList(){	
    $('#bar-notify').html('');	
    $('#application-list-div').show();
	$("#app-list").html('');	
	$("#app-list").bootgrid(
    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'get-application-list-investigation-agency-report?applicationId='+$('#applicationId').val()+'&applicationName='+$('#applicationName').val(),
	    		columndetails:
				[
					{title:'Application Id', name:'applicationId',sortable:true}, 
					{title:'Temp File No', name:'tempFileNo',sortable:true},
					{title:'Section File No', name:'sectionFileNo',sortable:true},
					{title:'Applicant / Association Name', name:'applicantName'},
					{title:'Service', name:'serviceName', sortable:true},					
					{title:'Submission Date', name:'submissionDate', sortable:true}
				],
			  onRowSelect:function(rowdata){getApplicationDetails(rowdata);}
    });	
}

function showProjectDetails(appId){	
	var url = 'popup-application-tracking-workspace?applicationId='+appId;
	openLink(url);	
}

function getApplicationDetails(data){

	$('#bar-notify').html('');
	var params = 'appId='+data.applicationId;
	var action = 'get-application-details-investigation-agency-report';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			  initApplicationDetails(JSON.parse(data[0],data[2]));	
			 if(JSON.parse(data[1])!=null)
			    prepareOfficeStatusList(JSON.parse(data[1]));
	         // alert(JSON.stringify(data[1]));	         
	          
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}


function getFullApplication(appId){
	var url='https://fcraonline.nic.in/fc_generate_pdf.aspx?app_id='+appId;
	openLink(url);	
}

function initApplicationDetails(data,finalStatus){	
	$('#app-info').show();
	prepareBasicInfo(data);		
}



function prepareOfficeStatusList(data){	

	$('#office-status-list').html('');
   $('#office-status-list').show();
	var htmlContent='';
	if(data != ''){		
		htmlContent+='<table class="table table-bordered"><thead><tr class="active"><th>Office</th><th>Status</th></thead><tbody>';
		$.each(data,function(index,item){			
			htmlContent+='<tr><td>'+item.li+'</td><td>'+item.ld+'</td></tr>';
		});		
		htmlContent+='</tbody></table>';		
		$('#office-status-list').html(htmlContent);
	}
}

function prepareBasicInfo(data){
	//alert(data[0].state);
	$('#bi-appId').html(data[0].applicationId);
	$('#bi-tempFileNo').html(data[0].tempFileNo);
	$('#bi-sectionFileNo').html(data[0].sectionFileNo);
	$('#bi-appName').html(data[0].applicantName);
	$('#bi-office').html(data[0].toOfficeInfo);
	$('#bi-service').html(data[0].serviceName);
	$('#bi-date').html(data[0].submissionDate);
	$('#bi-phase').html(data[0].currentStageDesc);	
	$('#svcCode').html(data[0].serviceId);
	$('#bi-state').html(data[0].state);
	$('#bi-district').html(data[0].district);
	var htmlContent='<button type="button" title="Click to see application details" onclick="showProjectDetails(\''+data[0].applicationId+'\');"'+ 
	'class="btn btn-warning active btn-sm ">'+
	'<span class="glyphicon glyphicon-info-sign"></span></button>';
	$('#bi-more-options').html(htmlContent);
	$('#off-status-info').show();
	$('#actions').show();
	
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
	$('#investAgencyList').val('');
	$('#reportNo').val('');
	$('#reportDate').val('');
	
}
function resetModal(){
	
	$("#addInvestigationModal").modal('hide');
	$('#addInvestigationModal-error').html('');
	$('#application-list-div').hide();
	$('#app-info').hide();
	$('#off-status-info').hide();
	$('#actions').hide();
	$('#appid-search').show();
	$('#applicationId').val('');
}


function submitInvestigationReport() {	
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var validationResult = $('#investigation-received-form').validationEngine('validate');
	if(validationResult==true) {
		
		var params = 'appId='+$('#bi-appId').html()+'&investAgencyList='+$('#investAgencyList').val()+'&reportNo='+$('#reportNo').val()+'&reportDate='+$('#reportDate').val();

		var action = 'submit-investigation-agency-report';		
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){	
				notifyList(JSON.parse(data[0]),'');
				resetModal();
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
					$('#requestToken').val(token);						  			
			},
			error: function(textStatus,errorThrown){
				alert('error');				
			}
		});

	}
}