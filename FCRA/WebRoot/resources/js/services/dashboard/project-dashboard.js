var tabStatus=null;
var tabStatusId=null;
var myOfficeId=null;
var serviceId=null;
// My Dashboard - 1
// Office - 2

var pdfBytes=null;
$(document).ready(function (){	
	$("#forward-user-form").validationEngine({promptPosition: 'bottomRight'});	
	$("#next-stage-form").validationEngine({promptPosition: 'bottomRight'});
	$("#user-note-form").validationEngine({promptPosition: 'bottomRight'});
	$("#clarification-form").validationEngine({promptPosition: 'bottomRight'});
	$("#hold-form").validationEngine({promptPosition: 'bottomRight'});
	$("#resume-form").validationEngine({promptPosition: 'bottomRight'});
	$("#forward-office-form").validationEngine({promptPosition: 'bottomRight'});
	$("#reject-form").validationEngine({promptPosition: 'bottomRight'});
	$("#close-form").validationEngine({promptPosition: 'bottomRight'});
	$("#showcause-form").validationEngine({promptPosition: 'bottomRight'});
	$("#hos-pdf-format-form").validationEngine({promptPosition: 'bottomRight'});
	$("#pre-approval-form").validationEngine({promptPosition: 'topRight'});
	$("input:radio[name=proceedToApproval]").click(proceedToApproval);
	initDashboard('1','f');	
	tabStatus="1";
	tabStatusId="f";
	$.each(JSON.parse(myDetails),function(index,item){	
		myOfficeId=item.p3;
		if(item.p3 == "2" || item.p3 == "3"){			
			$('#pfm-li').hide();			
		}			
	});	
	if(JSON.parse(recordsPendingForMail)>0){		
		$('#pending-mail-btn').click();
		var content='You have <b><span class="text-danger">'+JSON.parse(recordsPendingForMail)+'</span></b> cases pending for mail. Please clear these cases on priority basis.';
		$('#mailAlertMoadl-body').html(content);
	}
		
});
function getTabStatus(tabStatusId){
	if(tabStatusId=="f" || tabStatusId=="r-r" || tabStatusId=="r-s" || tabStatusId=="o-r" || tabStatusId=="d" || tabStatusId=="p-f-m"){	
		return true; // For MyDashboard
	}else if(tabStatusId=="o-f" || tabStatusId=="o-r-r" || tabStatusId=="o-u-p" || tabStatusId=="o-r-s" || tabStatusId=="o-d"){
		return false; // For Office Tab
	}		
}
function getFilteredDashboard(){		
	if(getTabStatus(tabStatusId)==true){		
		initDashboard(tabStatus,tabStatusId);
	}		
	else if(getTabStatus(tabStatusId)==false){		
		initOfficeDashboard(tabStatus,tabStatusId);
	}	
}
function getSectionFilteredService(){
	var params = 'section='+$('#sectionFilter').val();
	var action = 'get-section-filtered-service-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			populateSelectBox(data,'Select Service','serviceFilter');
			getFilteredDashboard();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
//My Dashboard Tab
function initDashboard(stage,id){	
	tabStatus=stage;
	tabStatusId=id;	
	resetApplication();
	$('#userFilter').show();
	$('#bar-notify').html('');	
	$('#my-tab-details').show();
	var currentPage = $("#"+id).getCurrentPage();
	if(currentPage == null || currentPage == "" ||  isNaN(currentPage))
		currentPage = 1;
	$("#"+id).html('');	
	$("#"+id).bootgrid(
			{
				title:'',
				recordsinpage:'10',
				dataobject:'propertyList',    		
				dataurl:'init-workspace?stage='+stage+'&state='+$('#stateFilter').val()+'&section='+$('#sectionFilter').val()+'&service='+$('#serviceFilter').val()+'&user='+$('#userFilter').val(),
				columndetails:
					[
					 {title:'Application Id', name:'applicationId',sortable:true}, 
					 {title:'Temp File No', name:'tempFileNo'},
					 {title:'Section File No', name:'sectionFileNo'},
					 {title:'Applicant / Association Name', name:'applicantName'},
					 {title:'Service', name:'serviceName'},					
					 {title:'Submission Date', name:'submissionDate', sortable:true},
					 {title:'Last Action By', name:'lastAccessedBy', sortable:true},
					 {title:'Last Action On', name:'lastAccessedOn', sortable:true}
					 ],
					 pageonload:currentPage,
					 onRowSelect:function(rowdata){getApplicationDetails(rowdata,stage,id);}


			});	
}
//Office Tab
function initOfficeDashboard(stage,id){	
	tabStatus=stage;
	tabStatusId=id;
	resetApplication();	
	if(id == 'o-f'){
		$('#userFilter').hide();
	}else{
		$('#userFilter').show();
	}
	$('#bar-notify').html('');
	$('#office-tab-details').show();
	$("#"+id).html('');
	$("#"+id).bootgrid(
    		{
    		title:'',
    		recordsinpage:'10',
    		dataobject:'propertyList',    		
    		dataurl:'init-office-workspace?stage='+stage+'&state='+$('#stateFilter').val()+'&section='+$('#sectionFilter').val()+'&service='+$('#serviceFilter').val()+'&user='+$('#userFilter').val(),
    		columndetails:
			[
				{title:'Application Id', name:'applicationId',sortable:true}, 
				{title:'Temp File No', name:'tempFileNo'},
				{title:'Section File No', name:'sectionFileNo'},
				{title:'Applicant / Association Name', name:'applicantName'},
				{title:'Service', name:'serviceName'},					
				{title:'Submission Date', name:'submissionDate', sortable:true},
				{title:'Last Action By', name:'lastAccessedBy', sortable:true},
				{title:'Last Action On', name:'lastAccessedOn', sortable:true}			
			],
			onRowSelect:function(rowdata){getApplicationDetails(rowdata,stage,id);}
    });	
}
function clearAll(){
	$('#chat-list').html('');
	$('#bar-notify').html('');
}
function initPPInstallmentFlag(){
	if($('#pp-amount-flag').is(":checked")){
		$('#ppInsFlag').val('YES');
		$('#pp-installment-div').show();	
	}else{
		$('#ppInsFlag').val('NO');
		$('#pp-installment-div').hide();
		$('#installment-table').html('');
		$('#installmentNumbers').val('');
	}
}
function initPPInstallments(){	
	var count=parseInt($('#installmentNumbers').val());	
	var content='<table class="table table-bordered"><thead><tr><th>Installment No.</th><th>Amount [ '+$('#ppAmountCurrency').val()+' ]</th></tr></thead>';
	var j=1;
	for(var i=0;i<count;i++){
		content+='<tr><td>'+j+++'</td><td><input type="text" class="form-control input-sm validate[required,custom[integer] max[9999999999] min[1]]" name="installments" /></td></tr>';
	}
	content+='</table>';	
	$('#installment-table').html(content);
	$("#next-stage-form").validationEngine({promptPosition: 'bottomRight'});
}
// My Dashboard tab
function getProject(data,id,stage){	
	initGetProject(id,stage);	
	prepareBasicInfo(data);
	$('#svcCode').val(data.serviceId);
	if(data.serviceId=="07"){
		$('#pdf-format-div').show();
		if(stage=="7" || stage=="4"){
			$('#hos-pdf-format-div').show();
		}			
	}
	if(data.serviceId=="03"){
		$('#validity-div').show();		
	}
	if(data.serviceId=="02"){
		$('#pp-amount-section').show();		
		$('#pp-amount-flag-div').show();		
	}
	if(data.currentStatus=="8"){		
		$('#actions').hide();
	}
	if(serviceId=="12"){
		$('#show-cause-btn').show();
		$('#clarification-btn').hide();
	}else{
		$('#show-cause-btn').hide();
		$('#clarification-btn').show();
	}
	if(data.serviceId=="01"||data.serviceId=="02"||data.serviceId=="03"||data.serviceId=="05"||data.serviceId=="06"||data.serviceId=="04"||data.serviceId=="13"||data.serviceId=="15"||data.serviceId=="16"||data.serviceId=="17"){
		if(id=="f" || id=="r-r" ){
			$('#office-mark-list').show();
		}		
		else{				
				$('#office-mark-list').hide();
		}	
	}
	else{
		$("#office-mark-list").hide();
	}
	getChat(data.applicationId);
}
function prepareBasicInfo(data){	
	$('#bi-appId').html(data.applicationId);
	$('#bi-office').html(data.toOfficeInfo);
	$('#bi-service').html(data.serviceName);
	$('#bi-date').html(data.submissionDate);
	$('#bi-phase').html(data.currentStageDesc);	
	$('#svcCode').val(data.serviceId);
	serviceId=data.serviceId; // Setting value in global Variable
	var htmlContent='<button type="button" title="Click to see application details" onclick="showProjectDetails(\''+data.applicationId+'\');"'+ 
	'class="btn btn-warning active btn-sm ">'+
	'<span class="glyphicon glyphicon-info-sign"></span></button>';
	$('#bi-more-options').html(htmlContent);
	var docContent='<button type="button" title="Click to see full Application"'+ 
	' class="btn btn-default active btn-sm" onclick="javascript:getFullApplication(\''+data.applicationId+'\');">'+
	'<span class="glyphicon glyphicon-file"></span></button>&nbsp;<button type="button" title="Click to see supporting documents"'+ 
	'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
	'<span class="glyphicon glyphicon-paperclip"></span></button>'
	$('#bi-doc').html(docContent);
	if(data.serviceId=="01" || data.serviceId=="02" || data.serviceId=="03" || data.serviceId=="17"){
		$('#cert-divv').show();
		var docContentt='<button type="button" title="Click to see affidavit documents"'+ 
	'data-toggle="modal" data-target="#affidavitModal" class="btn btn-info active btn-sm">'+
	'<span class="glyphicon glyphicon-cloud-download"></span></button>';
	$('#bi-docc').html(docContentt);
		}
	else{
		$('#cert-divv').hide();
	}
	if(data.serviceId=="01" || data.serviceId=="02" || data.serviceId=="03" || data.serviceId=="07"|| data.serviceId=="13"|| data.serviceId=="15"|| data.serviceId=="16"|| data.serviceId=="17"){
		$('#del-divv').show();
		var docContentt='<button type="button" title="Click to see deleted documents"'+ 
	'data-toggle="modal" data-target="#deletedocumentModal" class="btn btn-info active btn-sm">'+
	'<span class="glyphicon glyphicon-envelope"></span></button>';
	$('#del-docc').html(docContentt);
		}
	else{
		$('#del-divv').hide();
	}
	
	if(data.serviceId=="01" || data.serviceId=="02" || data.serviceId=="03" || data.serviceId=="07" || data.serviceId=="06"||data.serviceId=="13"||data.serviceId=="15"||data.serviceId=="16"||data.serviceId=="17"){
		if(data.currentStatus=="9" || data.currentStatus=="10"){
			$('#cert-div').show();
			var certContent='<button type="button" title="Click to see signed certificate"'+ 
			' class="btn btn-link btn-sm" onclick="javascript:initPreviewSignedCertificate(\''+data.applicationId+'\');">'+
			'<span class="text-danger fa fa-file-pdf-o fa-2x"></span></button>';
			$('#bi-cert').html(certContent);
		}
	}		
}
function getFullApplication(appId){
	var url='https://fcraonline.nic.in/fc_generate_pdf.aspx?app_id='+appId;
	openLink(url);
	
}
// Office Tab
function getOfficeProject(data,id,stage){	
	initGetOfficeProject(id,stage);	
	prepareBasicInfo(data);
	getOfficeResources(data.applicationId);	
	if(data.currentStatus=="8"){		
		$('#office-tab-actions').hide();
		$('#office-tab-user-list-div').hide();		
	}
	getChat(data.applicationId);
	if(data.serviceId=="01"||data.serviceId=="02"||data.serviceId=="03"||data.serviceId=="05"||data.serviceId=="06"||data.serviceId=="04"||data.serviceId=="13"||data.serviceId=="15"||data.serviceId=="16"||data.serviceId=="17"){
		if(id=="o-f" || id=="o-r-r" || id == "o-u-p" ){
			$('#office-mark-list').show();
		}		
		else{				
			$('#office-mark-list').hide();
		}	
	}
	else{
		$("#office-mark-list").hide();
	}
}
// Office Tab
function initGetOfficeProject(id,stage){
	$('#office-tab-details').hide();
	$('#back-btn').show();	
	$('#back-btn').attr('stage',stage);
	$('#back-btn').attr('content-id',id);
	$('#app-info').show();	
	$('#office-tab-actions').show();
	$('#office-tab-user-list-div').show();
	if(id == "o-f"){
		$('#off-tab-accept-app-btn').show();		
		//$('#off-tab-reject-app-btn').show();
		$('#off-tab-pull-app-btn').hide();
	}
	else if(id == "o-r-r" || id == "o-u-p" || id == "o-r-s"){
		$('#off-tab-pull-app-btn').show();
		$('#off-tab-accept-app-btn').hide();
		//$('#off-tab-reject-app-btn').hide();
	}else if(id == "o-d"){
		$('#office-tab-user-list-div').hide();
		$('#off-tab-pull-app-btn').hide();
		$('#off-tab-accept-app-btn').hide();
		//$('#off-tab-reject-app-btn').hide();
	}
}

// My Dashboard Tab
function initGetProject(id,stage){
	$('#my-tab-details').hide();
	$('#back-btn').show();
	$('#back-btn').attr('stage',stage);
	$('#back-btn').attr('content-id',id);
	$('#app-info').show();
	$('#timer-info').show();	
	if(id=="f" || id=="r-r" ){		
			$('#actions').show();
			$('#office-mark-list').show();
	}		
	else{
			$('#actions').hide();
			$('#office-mark-list').hide();
	}	
	if(id=="p-f-m" || id=="d"){		
		if(myOfficeId == "1"){			
			$('#mail-actions').show();
			$('#timer-info').hide();	
		}
	}
}

function initBack(){
	resetAll();	
	if ($('#my-tab').hasClass("active")){		
		initDashboard($('#back-btn').attr('stage'),$('#back-btn').attr('content-id'));
	}else if($('#office-tab').hasClass("active")){				
		initOfficeDashboard($('#back-btn').attr('stage'),$('#back-btn').attr('content-id'));
	}else{		
		resetApplication();
	}		
}
function resetHideShow(){
	$('#back-btn').hide();
	$('#app-info').hide();
	$('#timer-info').hide();
	$('#actions').hide();	
	$('#pdf-format-div').hide();
	$('#hos-pdf-format-div').hide();
	$('#validity-div').hide();	
	$('#mail-actions').hide();
	$('#project-doc-div').hide();
	$('#office-status-list').hide();
	$('#office-mark-list').hide();
	$('#office-tab-actions').hide();
	$('#office-tab-user-list-div').hide();
	$('#reset-btn').hide();
	$('#cert-div').hide();
	$('#reminder-div').hide();
	$('#pp-amount-section').hide();
	$('#pp-amount-flag-div').hide();	
	$('#pp-installment-div').hide();	
	
	$('#forward-user-btn').hide();
	$('#forward-office-btn').hide();
	$('#add-note-btn').hide();
	$('#onhold-btn').hide();
	$('#clarification-btn').hide();
	$('#resume-btn').hide();
	$('#next-stage-btn').hide();
	$('#reject-btn').hide();
	$('#close-btn').hide();		
	$('#reminder-div').hide();
}
function resetValues(){
	$('#pdf-Format').val('');
	$('#hos-pdf-Format').val('');	
	$('#pp-amount-flag').removeAttr('checked');
	$('#installmentNumbers').val('');
	clearModal();
}
function resetHtml(){	
	$('#stage-doc-list').html('');
	$('#chat-list').html('');
	$('#project-doc-div').html('');	
	$('#sticky-notify').html('');		
	$('#installment-table').html('');
}
function resetAll(){
	resetHideShow();
	resetValues();
	resetHtml();
}
function resetApplication(){
	resetAll();	
	$('#applicationId').val('');
	$('#tab-div').show();
	$('#application-list-div').hide();
	$('#stateFilter').show();
	$('#sectionFilter').show();
	$('#serviceFilter').show();
	$('#appid-search').show();
	$('#toggle-btn').show();	
}

function toggleSearch(){
	if ( $('#app-search').is(':visible') ){		
		$('#app-search').hide();
		$('#name-search').show();	
	}else if($('#name-search').is(':visible')){		
		$('#app-search').show();	
		$('#name-search').hide();		
	}
}
function getApplicationList(){	
	resetAll();
	$('#bar-notify').html('');
	/* Initialize Hide and Show for Application Details */
	$('#application-list-div').show();
	$('#tab-div').hide();		
	$('#stateFilter').hide();
	$('#sectionFilter').hide();
	$('#serviceFilter').hide();
	$('#appid-search').hide();
	$('#toggle-btn').hide();
	$('#userFilter').hide();
/* Initialize Hide and Show for Application Details */
	$("#app-list").html('');	
	$("#app-list").bootgrid(
    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'get-application-list-workspace?applicationId='+$('#applicationId').val()+'&applicationName='+$('#applicationName').val(),
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
function backToWorkSpace(){
	$('#bar-notify').html('');
	resetApplication();
}
function getApplicationDetails(data,stage,id){
	resetAll();	
	opld();
	$('#back-btn').show();
	$('#back-btn').attr('stage',stage);
	$('#back-btn').attr('content-id',id);
	
	$('#bar-notify').html('');
	$('#userFilter').hide();
	var params = 'appId='+data.applicationId;
	var action = 'get-application-details-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){	
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');			
			
			if(data[2]=="true")			
				initApplicationDetails(JSON.parse(data[0])[0],data[11]);	
			else
				$('#my-tab-details').show();			
			
			if(data[3]=="true"){
				resetOfficeTab(data[5]);
			}
			if(data[4]=="true"){
				resetMyTab(data[6],data[7],data[8],JSON.parse(data[0])[0]);	
				
				if(data[12]=="true" && myOfficeId=="1"){	// Only for MHA
					$('#office-mark-list').show();					
				}
				else{
					$("#office-mark-list").hide();
				}
			}			
			if(data[9]=="true"){
				resetMyTabToFetch(data[10]);
			}	
			ropld();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}

function getChat(appId){	
	//alert("Dwe");
	var params = 'appId='+appId;
	var action = 'get-chat-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){	
			prepareChat(JSON.parse(data[0]),'chat-list',JSON.parse(data[5]),appId);			
			populateSelectBox(JSON.parse(data[1]),'Select Office','forwardOffice');		
			populateSelectBox(JSON.parse(data[3]),'Select User','forwardUser');		
			if(JSON.parse(data[6])!=null)
				prepareOfficeStatusList(JSON.parse(data[6]));	
			if(JSON.parse(data[7])!=null)
				populateSelectBox(JSON.parse(data[7]),'Select Section','userSection');
			if(JSON.parse(data[8])!=null && JSON.parse(data[9])!=null)
				prepareTimeStatus(JSON.parse(data[8]),JSON.parse(data[9]));
			
			//initHideShow(JSON.parse(data[4]),JSON.parse(data[10]));			
			prepareDocuments(JSON.parse(data[11]).uploadedDocuments,JSON.parse(data[11]).deletedDocuments);
			//alert(JSON.parse(data[11]).uploadedDocuments,JSON.parse(data[11]).deletedDocuments);
			prepareDeleteDocument(JSON.parse(data[11]).deletedSupportDocuments,JSON.parse(data[11]).deletedSupportDocumentsEc);
			if(qserviceId='1'){
			//alert("qserviceId"+qserviceId);
				affidavitDocument(JSON.parse(data[11]).affiddavidDocument);}
			if(data[12]!=null && data[13]!=null)
				setRenewalValidity(data[12],data[13],data[22]);
			prepareCurrentUser(data[14]);
			$("#match-found").val('N');
			if(data[15]!=null)
				checkRedFlagStatus(data[15]);
			checkRedFlagAssociationMatch(JSON.parse(data[16]));
			checkRedFlagDonorMatch(JSON.parse(data[17]));
			if(data[18]!=null)
				notifyList(JSON.parse(data[18]),'');			
			
			initOfficialActions(data[19],data[20],data[27],JSON.parse(data[10]),data[21]);
			$('#ppAmount').val(data[23]);
			$('#ppAmountDesc').html(data[24]);
			$('#ppAmountCurrency').val(data[25]);
			if(JSON.parse(data[26])!=null)
				prepareOfficeMarkList(JSON.parse(data[26]));	
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}

function initApplicationDetails(data,finalStatus){	
	/* Initialize Hide and Show for Application Details */
		$('#app-info').show();					
		$('#tab-div').hide();	
		$('#application-list-div').hide();
		$('#stateFilter').hide();
		$('#sectionFilter').hide();
		$('#serviceFilter').hide();
		$('#appid-search').hide();
	/* Initialize Hide and Show for Application Details */
	prepareBasicInfo(data);	
	
	if(myOfficeId=="1"){		// Only for MHA
		if(data.serviceId=="07"){
			$('#pdf-format-div').show();					
		}
		if(data.serviceId=="03"){
			$('#validity-div').show();	
		}	
		if(data.serviceId=="02"){
			$('#pp-amount-section').show();		
			$('#pp-amount-flag-div').show();		
		}
	}
	
	if(myOfficeId=="1"){	// Only for MHA	
		if(finalStatus=="G" && (data.serviceId=="01" || data.serviceId=="02" || data.serviceId=="03" || data.serviceId=="07" || data.serviceId=="06"||data.serviceId=="13"||data.serviceId=="15"||data.serviceId=="16"||data.serviceId=="17")){			
			if(data.serviceId=="07"){				
				$('#hos-pdf-format-div').show();				
			}
			$('#mail-actions').show();
		}
		if(finalStatus=="D"){
			$('#mail-actions').show();
		}
		if(finalStatus=="CR"){
			$('#office-tab-actions').hide();			
		}
	}	
	getChat($('#bi-appId').html());
}
function resetMyTab(answd,toAns,toMail,data){	
	$('#mail-actions').hide();
	$('#actions').hide();	
	$('#timer-info').show();
	if(answd=="true"){
		$('#actions').hide();
	}
	if(toAns=="true"){
		$('#actions').show();
	}	
	if(toMail=="true"){	
		if(data.serviceId=="07"){				
			$('#hos-pdf-format-div').show();				
		}
		$('#mail-actions').show();
	}	
}
function resetMyTabToFetch(toFetch){
	$('#office-tab-actions').hide();
	if(toFetch=="true"){
		$('#office-tab-actions').show();
		$('#office-tab-user-list-div').show();		
		$('#off-tab-accept-app-btn').hide();		
		$('#off-tab-pull-app-btn').show();
		getOfficeResources($('#bi-appId').html());
		getChat($('#bi-appId').html());
	}
}
function resetOfficeTab(mySection){	
	if(mySection=="true"){
		$('#office-tab-user-list-div').show();
		$('#office-tab-actions').show();
		$('#off-tab-pull-app-btn').hide();
		getOfficeResources($('#bi-appId').html());
		//getChat($('#bi-appId').html());
	}	
}
function initOfficialActions(gFlag,pFlag,cFlag,finalStatus,ClarificationReminderFlag){
	// Put on Hold functionality
	if(finalStatus=="11"){		
		$('#resume-btn').show();
		return;
	}else{		
		$('#resume-btn').hide();		
	}
	
	// Processing Flag 
	if(pFlag=="Y"){
		$('#forward-user-btn').show();
		$('#add-note-btn').show();
	}
	// Granting Flag
	if(gFlag=="Y"){		
		$('#forward-user-btn').show();
		$('#forward-office-btn').show();		
		$('#onhold-btn').show();	
		$('#add-note-btn').show();
		$('#next-stage-btn').show();
		$('#reject-btn').show();
		$('#close-btn').show();	
		
		if(serviceId=="12"){
			$('#show-cause-btn').show();			
		}else{
			$('#show-cause-btn').hide();
		}
	}
	
	// Clarification functionlaity
	if(cFlag=="Y" && !(serviceId=="12")){			
		$('#clarification-btn').show();		
	}
	
	// Clarification Reminder functionality
	if(cFlag=="Y" && ClarificationReminderFlag=="Y"){		
		$('#reminder-div').show();		
	}
	
	// IB and RAW
	initActionForOtherThanMHA();
}
function initActionForOtherThanMHA(){	
		if(myOfficeId == "2" || myOfficeId == "3"){			
			$('#add-note-btn').show();
			$('#onhold-btn').show();			
			$('#close-btn').show();	
			$('#next-stage-btn').show();
			$('#show-cause-btn').hide();
			$('#clarification-btn').hide();
			$('#reject-btn').hide();
			$('#forward-office-btn').hide();
			$("#next-stage-btn").html('<span class="glyphicon glyphicon-ok"></span>&nbsp;Disposed Off');
		}	
}
function checkRedFlagStatus(status){	
	if(status=="YES"){				
		$('#next-stage-btn').hide();
	}
}
function checkRedFlagAssociationMatch(matchingRedFlagAssociationList){	
	$("#red-flag-list-header").hide();
	if(matchingRedFlagAssociationList.length > 0) {
		$("#match-found").val('Y');
		prepareRedFlagAssociationList(matchingRedFlagAssociationList);
		$("#red-flag-list-header").show();
		$('#red-flag-footer').hide();
		$("#red-flag-list-modal").modal('show');
	}
}
function checkRedFlagDonorMatch(matchingRedFlagDonorList){
	$("#red-flag-list-donor-header").hide();
	if(matchingRedFlagDonorList.length > 0) {
		$("#match-found").val('Y');
		prepareRedFlagDonorList(matchingRedFlagDonorList);
		$("#red-flag-list-donor-header").show();
		$('#red-flag-footer').hide();
		$("#red-flag-list-modal").modal('show');
	}
}
function prepareRedFlagAssociationList(matchingRedFlagAssociationList) {
	$("#red-flag-list-table").empty();
	$("#red-flag-list-table").initLocalgrid(
			{
				columndetails:[
				{title:'Name of Association', name:'assoName'},
				{title:'Address', name:'assoAddress'},
				{title:'State', name:'assoStateName'}
							]//,
				//onRowSelect:function(rowId, rowdata){getPropertyDetails(applicationId, rowdata.propertySerialNo, rowdata.propertyTypeDesc)}
			});
	$("#red-flag-list-table").addListToLocalgrid(matchingRedFlagAssociationList);	
}
function prepareRedFlagDonorList(matchingRedFlagDonorList) {
	$("#red-flag-list-donor-table").empty();
	$("#red-flag-list-donor-table").initLocalgrid(
			{
				columndetails:[
				{title:'Name of Donor', name:'donorName'},
				{title:'Country', name:'donorCountryName'}
							]//,
				//onRowSelect:function(rowId, rowdata){getPropertyDetails(applicationId, rowdata.propertySerialNo, rowdata.propertyTypeDesc)}
			});
	$("#red-flag-list-donor-table").addListToLocalgrid(matchingRedFlagDonorList);	
}
function prepareCurrentUser(user){
	$('#basic-header').html('Application Details '+"     <b>[</b> Currently Available With: <b>"+user+"</b><b> ]</b>");	
}
function setRenewalValidity(from,to,current){
	$('#currentDateString').val(current);
	$('#validFrom').val(from);
	$('#validityFrom').val(from);
	$('#validityUpTo').val(to);
}
function initValidity(){
	var from=parseDate($('#validityFrom').val());	
	var d = new Date(from.setMonth((from.getMonth() + 60))-1);		
	var dd = d.getDate();
    var mm = d.getMonth()+1; //January is 0!
    var yyyy = d.getFullYear();
    if(dd<10){
        dd='0'+dd
    } 
    if(mm<10){
        mm='0'+mm
    } 
    var validity = dd+'-'+mm+'-'+yyyy;	
	$('#validityUpTo').val(validity);
}
function prepareDocuments(uploadedDocs,deletedDocs){	
	//alert(uploadedDocs);
	var delCon='';
	if(uploadedDocs != ''){				
		var htmlContent='<table class="table">';
		$.each(uploadedDocs,function(index,item){			
			htmlContent+='<tr><td><a class="" href="javascript:getApplicationDocument(\''+item.docPath+'\');">'+
			'<span class="text-info">'+item.docName+'</span> [<i><span class="text-danger">'+item.docDetails+'</span></i>]</a></td>';
			if(deletedDocs!=''){
				var docId=item.docId;
				delCon='<td><div class="btn-group"> <button type="button" class="btn btn-xs btn-default active dropdown-toggle" data-toggle="dropdown">View previous versions<span class="caret"></span></button>'+
				'<ul class="dropdown-menu">';
				var liCon='';
				$.each(deletedDocs,function(index1,item1){					
					if(item1.docId == docId){						
						liCon+='<li><a href="javascript:getApplicationDocument(\''+item1.docPath+'\');">'+item1.docDetails+'</a></li>';								
					}					
				});	
				if(liCon!='')
					delCon+=liCon+'</ul></div></td>';
				else
					delCon='<td></td>';
			}
			htmlContent+=delCon+'</tr>';		
		});	
		htmlContent+='</table>';	
		$('#project-doc-list').html(htmlContent);
	}else{
		$('#project-doc-list').html('<p class="text-danger">No document is uploaded by applicant.</p>');
	}
}

function affidavitDocument(affiddavidDocument){
	
	if(affiddavidDocument != ''){
		var Fathername="Father's Name" ;
		var i=1;
		var htmlContent='<table class="table table-bordered" cellpadding="10000"><thead style="background-color: #9E9EB9;"><tr><th>S. no.</th><th>Name</th><th>'+Fathername+'</th><th>Desination in the Association</th><th>Link</th></tr></thead>';
		$.each(affiddavidDocument,function(index,item){			
			htmlContent+='<tr><td><span class="text-danger">'+i+++'</span></a></td><td><span class="text-danger">'+item.name+'</span></td><td><span class="text-danger">'+item.fname+'</span></td><td><span class="text-danger">'+item.post+'</span></td><td><a class="" href="javascript:getApplicationDocument(\''+item.docPath+'\');"> <span class="glyphicon glyphicon-cloud-download" title="Affidavit Document"></span></a></td></tr>';
		});	
		htmlContent+='</table>';	
		$('#affidavit-doc-list').html(htmlContent);
	}else{
		$('#affidavit-doc-list').html('<p class="text-danger">No affidavit is uploaded by applicant.</p>');
	}
}
function prepareDeleteDocument(deletedSupportDocuments,deletedSupportDocumentsEc){
	if(deletedSupportDocuments != '' | deletedSupportDocumentsEc != ''){
		
		var i=1;
		var htmlContent='<table class="table table-bordered" cellpadding="10000"><thead style="background-color: #9E9EB9;"><tr><th>S. no.</th><th>File Name</th><th>Link</th></tr></thead>';
		if(deletedSupportDocuments != '' ){
		$.each(deletedSupportDocuments,function(index,item){			
			htmlContent+='<tr><td><span class="text-danger">'+i+++'</span></a></td><td><span class="text-danger">'+item.docDetails+'</span></td><td><a class="" href="javascript:getApplicationDocument(\''+item.docPath+'\');"> <span class="glyphicon glyphicon-cloud-download" title="Deleted Document"></span></a></td></tr>';
		});	}
		if(deletedSupportDocumentsEc != '' ){
		$.each(deletedSupportDocumentsEc,function(index,item){			
			htmlContent+='<tr><td><span class="text-danger">'+i+++'</span></a></td><td><span class="text-danger">'+item.docDetails+'</span></td><td><a class="" href="javascript:getApplicationDocument(\''+item.docPath+'\');"> <span class="glyphicon glyphicon-cloud-download" title="Deleted Document"></span></a></td></tr>';
		});	}
		htmlContent+='</table>';	
		$('#delete-doc-list').html(htmlContent);
	}else{
		$('#delete-doc-list').html('<p class="text-danger">No Deleted Document.</p>');
	}
}
function prepareOfficeDocuments(data){	
	if(data != ''){				
		var htmlContent='<div class="list-group">';
		$.each(data,function(index,item){
			htmlContent+='<a class="list-group-item list-group-item-default" href="javascript:getApplicationDocument(\''+item.docPath+'\');">'+
			'<span class="text-info">'+item.docName+'</span> [<i><span class="text-danger">'+item.docDetails+'</span></i>]</a>'
		});	
		htmlContent+='</div>';	
		$('#project-off-doc-list').html(htmlContent);
	}else{
		$('#project-off-doc-list').html('<p class="text-danger">No document is uploaded by applicant.</p>');
	}
}
function getApplicationDocument(docPath){	
	window.open(docPath);
}
function prepareTimeStatus(days,status){	
	if(status=="Y"){
		$('#timer-text').html(days);
		$('#timer-status').html('Running');
		$('#timer-text').removeClass('text-danger').addClass("text-success");
		$('#timer-status').addClass("text-warning");
	}
	else{
		$('#timer-text').html(days);
		$('#timer-status').html('Stopped');	
		$('#timer-text').removeClass('text-success').addClass("text-danger");
		$('#timer-status').addClass("text-warning");
	}
}

function getSectionList(){
	$('#section-btn').hide();
	$('#userSection').show();
}
function getSectionUsers(sectionId){
	if(sectionId!=''){		
		var params = 'sectionId='+sectionId;
		var action = 'get-section-users-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){						
				populateSelectBox(JSON.parse(data[1]),'Select User','forwardUser');			
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function resetModal(){
	$('#on-hold-error').html('');
	$('#clarificationModal-error').html('');
	$('[up-file-list="clarification-upload"] tbody').html('');
	$('#noteModal-error').html('');
	$('#userModal-error').html('');
	$('#resume-error').html('');
	$('#officeModal-error').html('');
	$('#nextStageModal-error').html('');
	$('#rejectModal-error').html('');
	$('#closeModal-error').html('');
	$('#reminderModal-error').html('');
	$('#fetchModal-error').html('');
	$('#pp-amount-flag').removeAttr('checked');
	$('#installment-table').html('');		
	$('#pp-installment-div').hide();	
	$('#markOfficeModal-error').html('');
	resetValues();
	
	$('#section-btn').show();
	$('#userSection').hide();
	
	// Deleting attachments from t_upload_cache	
	var params = '';
	var action = 'delete-upload-cache-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'text',
		success: function(data){			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}
function getOfficeResources(appId){
	var params = 'appId='+appId;
	var action = 'get-office-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			if(data[0]!=null)
				populateSelectBox(JSON.parse(data[0]),'Select User','officeTabForwardUser');	
			if(data[1]!=null)
				prepareDocuments(JSON.parse(data[1]).uploadedDocuments);
			
			if(data[2]!=null)
				prepareChat(JSON.parse(data[2]),'chat-list',JSON.parse(data[3]));
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
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
function prepareOlderSubStages(data){
	var htmlContent='';
	if(data != ''){
		$('#project-doc-div').show();
		var htmlContent='<div class="list-group"><a href="#" class="list-group-item list-group-item-danger"><strong>Completed Stage(s)</strong></a>';
		$.each(data,function(index,item){
			htmlContent+='<a class="list-group-item list-group-item-default" style="cursor: pointer;" data-toggle="modal" data-target="#olderStageModal"'+
				'onclick="javascript:getOlderChat(\''+item.applicationId+'\',\''+item.subStageId+'\',\''+item.subStageDesc+'\');">'+
				'<span class="text-info">'+item.subStageDesc+'</span></a>'
		});	
		htmlContent+='</div>';	
		$('#older-substage-list').html(htmlContent);
	}
}

function prepareOlderLatestSubStage(oldChat,currentChat){
	var appId,stageId;
	if(oldChat == '')
		return;
	if(currentChat == ''){
		$.each(oldChat,function(index,item){
			appId=item.applicationId;
			stageId=item.subStageId;
		});
		var params = 'appId='+appId+'&subStageId='+stageId;
		var action = 'get-substage-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				prepareChat(JSON.parse(data[0]),'chat-list',JSON.parse(data[1]));					
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}

function getOlderChat(appId,stageId,subStageDesc){
	$('#olderStageModalLabel').html('Conversation Details: <span class="text-danger">'+subStageDesc+'</span>');
	var params = 'appId='+appId+'&subStageId='+stageId;
	var action = 'get-substage-chat-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			prepareChat(JSON.parse(data[0]),'sub-stage-chat-list',JSON.parse(data[1]));					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}


function populateDocuments(data){
	if(data != ''){
		var htmlContent='<label class="control-label pp-form-field">Documents to be uploaded for next stage are as follows:</label>';
		$.each(data,function(index,item){
			htmlContent+='<div class="row"><div class="col-sm-10"><p class="help-block">'+(++index)+'. '+item.ld+'</p>'+
						 '<input type="file" class="stage-files" id="'+item.li+'"></div></div>'
		});	
		$('#stage-doc-list').html(htmlContent);
		$('#office-stage-doc-list').html(htmlContent);
	}
}
function populateUploadedDocuments(data){
	if(data != ''){
		var htmlContent='<label class="control-label">Uploaded Documents for Current Stage:</label>'+
						'<div class="list-group">';
		$.each(data,function(index,item){
			htmlContent+='<a class="list-group-item list-group-item-info" href="javascript:getProjectDocument(\''+item.li+'\');"><i>'+item.ld+'</i></a>'
		});	
		htmlContent+='</div>';
		$('#uploaded-stage-doc-list').html(htmlContent);		
	}
}
function showProjectDocuments(){
	var params = 'appId='+$('#bi-appId').html();
	var action = 'get-project-documents-chat-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			prepareProjectDocuments(data);					
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}
function showProjectDetails(appId){
		var url = 'popup-application-tracking-workspace?applicationId='+appId;
		openLink(url);	
}
function prepareProjectDocuments(data){
	if(data != ''){
		var htmlContent='<div class="list-group">';
		$.each(data,function(index,item){
			htmlContent+='<a class="list-group-item list-group-item-default" href="javascript:getProjectDocument(\''+item.p1+'\');">'+
			'<span class="text-info">'+item.p2+'</span> [<i><span class="text-danger">'+item.p3+'</span></i>]</a>'
		});	
		htmlContent+='</div>';	
		$('#project-doc-list').html(htmlContent);
	}else{
		$('#project-doc-list').html('<p class="text-danger">No project document is uploaded yet.</p>');
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

function prepareChat(data,chatDivId,attachmentData,appId){	
	var stageDesc;
	var htmlContent='<ul class="chat-container">';
	$.each(data,function(index,item){
		var liClass=prepareLiClass(item.fromOfficeId,item.toOfficeId);		
		
		/*var senderTitle=prepareTitle(item.fromOfficeId,item.fromOfficeCode,item.fromOffice,item.fromUserId,item.fromUserName,item.fromUserDesignation);
		var sender=prepareUser(item.fromOfficeId,item.fromOffice,item.fromUserId,item.fromUserName,item.fromUserDesignation);		
		var receiverTitle=prepareTitle(item.toOfficeId,item.toOfficeCode,item.toOffice,item.toUserId,item.toUserName,item.toUserDesignation);
		var receiver=prepareUser(item.toOfficeId,item.toOffice,item.toUserId,item.toUserName,item.toUserDesignation);*/
		
		
		var senderTitle=prepareTitle(item.fromOfficeId,item.fromOffice,item.fromUserId,item.fromUserName,item.fromUserDesignation,item.toOfficeId,"s");
		var sender=prepareUser(item.fromOfficeId,item.fromOffice,item.fromUserName,item.fromUserDesignation,item.toOfficeId,"s");		
		var receiverTitle=prepareTitle(item.fromOfficeId,item.toOffice,item.toUserId,item.toUserName,item.toUserDesignation,item.toOfficeId,"r");
		var receiver=prepareUser(item.fromOfficeId,item.toOffice,item.toUserName,item.toUserDesignation,item.toOfficeId,"r");	
		
		var summary=item.statusRemark.substr(0,110);
		var attachments=prepareAttachment(item.chatId,attachmentData);	
		var finalStatus=prepareFinalStatus(item.statusId,item.chatId,appId);
		var parentDesc='';var childDesc='';			
		stageDesc=item.subStageDesc;
		htmlContent+='<li class="'+liClass+'">'+
				         '<div class="chat-bubble">'+
				         	'<div class="chat-header">'+
				         		'<span class="sender" title="'+senderTitle+'"><strong>'+sender+'</strong> to </span>'+
				         		'<span class="receiver" title="'+receiverTitle+'">'+receiver+'</span>'+
				         		'<span class="actions no-print">'+attachments+
				         			/*'<i class="glyphicon glyphicon-share-alt"></i>'+*/
				         			/*	'<i class="glyphicon glyphicon-paperclip"></i>'+*/
				         			'<i class="toggle glyphicon glyphicon-chevron-up"></i>'+
				         		'</span>'+
				         		'<span class="time">&nbsp;'+item.statusDate+'</span>'+finalStatus+
				         	'</div>'+
				         	'<div class="chat-summary">'+summary+'.....</div>'+
				         	'<div class="chat-desc">'+
				         		'<div class="chat-parent">'+parentDesc+'</div>'+
				         		'<div class="chat-this">'+item.statusRemark+'</div>'+
				         		'<div class="chat-child">'+childDesc+'</div>'+
				         	'</div>'+				         	
				         '</div>'+
				      '</li>';			
		});
		htmlContent+='</ul>';		
	/*	var docContent='<button type="button" title="Click to edit contractor/architect and other details." onclick="editProjectDetails(\''+$('#bi-appId').html()+'\');"'+ 
		' class="btn btn-danger btn-sm">'+
		'<span class="glyphicon glyphicon-pencil"></span></button>&nbsp;&nbsp;'+
		'<button type="button" title="Click to see project documents uploaded during various sub stages." onclick="showProjectDocuments();"'+ 
		'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
		'<span class="glyphicon glyphicon-paperclip"></span></button>';	
		$('#project-doc-div').html(docContent);	*/	
		if(data!=''){
			//$('#project-doc-div').show();
			$('#'+chatDivId).html(htmlContent);
		}	
		$('.final-status').parent().parent().addClass('chat-bubble-final');
		$('.chat-desc').show();
		$('.chat-summary').hide();
		initChatFeatures();	
}
function editProjectDetails(appId){	
	var url = 'popup-edit-project-details-workspace?applicationId='+appId;
	openLink(url);	
}
function prepareFinalStatus(status,chatId,appId){
	var content=null;
	if(status=="8")
		content='<span class="final-status text-warning"><strong>CLARIFICATION REQUESTED</strong></span>';
	else if(status=="10")
		content='<span class="final-status text-danger"><strong>DENIED</strong></span>';
	else if(status=="9")
		content='<span class="final-status text-success"><strong>GRANTED</strong></span>';
	else if(status=="12")
		content='<span class="final-status text-default"><strong>CLOSED</strong></span>';
	else if(status=="13")
		content='<span class="final-status text-warning"><strong>SHOW CAUSE NOTICE GENERATED</strong></span><button type="button" title="Click to download notice" class="btn btn-link" onclick="javascript:generateShowCauseNotice(\''+chatId+'\',\''+appId+'\');"><span class="fa fa-print fa-lg text-primary">&nbsp;</span></button>';
	else
		content='';
	return content;
}
function prepareAttachment(chatId,attachmentData){	
	var content='';		
	var liContent='';
	$.each(attachmentData,function(index,item){		
		if(chatId==item.chatId){
			var rowId=item.rowId;
			var doc=item.attachmentName;
			liContent+='<li><a href="javascript:getChatAttachment(\''+encodeURIComponent(rowId)+'\');">'+doc+'</a></li>';
		}
	});	
	if(liContent == null || liContent == ''){
		content='';
		return content;
	}
	var content='<span class="dropdown">'+
							'<i class="glyphicon glyphicon-paperclip" id="attach"'+ 
						    ' data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></i>'+
							'<ul class="dropdown-menu" aria-labelledby="attach">'
						    +liContent+							
							'</ul>'+
						'</span>';		
		
	return content;
}
function getChatAttachment(rowId){	
	var url='get-attachment-chat-workspace';
	$("#chat-attachment-form").attr('action', url);
	$("#chat-attachment-form #rowId").val(rowId);
	$("#chat-attachment-form").submit();
}
function generateShowCauseNotice(chatId,appId){
	var tUrl = 'download-show-cause-notice-workspace?'+'appId='+appId+'&chatId='+chatId;
	openLink(tUrl);
}
function getProjectDocument(rowId){	
	var url='get-project-doc-chat-workspace';
	$("#chat-attachment-form").attr('action', url);
	$("#chat-attachment-form #rowId").val(encodeURIComponent(rowId));
	$("#chat-attachment-form").submit();
}
function initChatFeatures(){
	$('.chat-container .chat-header .actions .toggle').each(function(){
		$(this).unbind('click');
		$(this).on('click',function(){
			hideDesc(this);
		});
	});
}
function prepareLiClass(fromOffice,toOffice){
	var liClass='chat-li others';
	liClass+=' fo'+fromOffice;
	liClass+=' to'+toOffice;	
	return liClass;
}


function prepareTitle(fromOfficeId,office,userId,userName,userDsg,toOfficeId,userType){	
	if(fromOfficeId == toOfficeId){
		return userId;
	}else{		
		if(userType=="s"){			
			if((fromOfficeId == myOfficeId))
				return userId;
			else
				return userName+"("+userDsg+")"+"["+userId+"]";
		}
		if(userType=="r"){			
			if((toOfficeId == myOfficeId))
				return userId;
			else
				return userName+"("+userDsg+")"+"["+userId+"]";
		}
	}	
	/*if(officeId == "7"){
		return userId;
	}else if(officeId == "1" || officeId == "2" || officeId == "3" || officeId == "4"){
		return userName+"("+userDsg+")"+"["+userId+"]";
	}*/
}
function prepareUser(fromOfficeId,office,userName,userDsg,toOfficeId,userType){	
	if(fromOfficeId == toOfficeId){
		return userName+", "+userDsg;
	}else{
		if(userType=="s"){
			if((fromOfficeId == myOfficeId))
				return userName+", "+userDsg;
			else			
				return office;
		}
		if(userType=="r"){
			if((toOfficeId == myOfficeId)){
				if(userName!=null && userName!='')
					return userName+", "+userDsg;
				else
					return office;
			}				
			else			
				return office;
		}		
	}
	/*if(officeId == "7"){
		return userName+", "+userDsg;
	}else if(officeId == "1" || officeId == "2" || officeId == "3" || officeId == "4"){
		return office;
	}*/
}


/*function prepareTitle(officeId,officeCode,office,userId,userName,userDsg){
	if(userId == null || userId=='')
		return officeCode;
	else 
		return userId;
}

function prepareUser(officeId,office,userId,userName,userDsg){	
	if(userId == null || userId=='')
		return office;
	else 
		return userName+", "+userDsg;	
}*/


// ACCEPT APPLICATION
function acceptApplication(){
	$('#bar-notify').html('');
	if($('#office-tab-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&user='+$('#officeTabForwardUser').val();
		var action = 'accept-app-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#office-tab-actions').hide();
					$('#office-tab-user-list-div').hide();
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
// PULL Application
function pullApplication(){
	$('#bar-notify').html('');
	if($('#office-tab-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&user='+$('#officeTabForwardUser').val()+'&remark='+$('#pullRemark').val();
		var action = 'pull-app-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){					
					notifyList(JSON.parse(data[0]),'');					
					$('#office-tab-actions').hide();
					$('#fetchModal-close-btn').click();
					$('#office-tab-user-list-div').hide();
					getChat($('#bi-appId').html());	
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
function clearModal(){
	$('#forwardUser').val('');
	$('#forwardUserRemark').val('');
	$('[up-file-list="user-upload"] tbody').html('');
	$('#forwardOffice').val('');
	$('#forwardOfficeRemark').val('');
	$('#pullRemark').val('');
	$('#officeModal').find('.stage-files').each(function() {			
		$(this).val(null);			
	});	
	$('[up-file-list="office-upload"] tbody').html('');
	$('#forwardAgent').val('');
	$('#forwardAgentRemark').val('');
	$('#otherRemark').val('');
	$('#rejectRemark').val('');
	$('#clarificationRemark').val('');	
	$('#reminderRemark').val('');
	$('#closeRemark').val('');
	$('#userNote').val('');	
	$('#nextStageModal').find('.stage-files').each(function() {			
		$(this).val(null);		
	});
	$('[up-file-list="office-upload"] tbody').html('');
	$("#red-flag-remarks").val('');
	$("input:radio[name=proceedToApproval]").prop('checked',false);
	$('[up-file-list="clarification-upload"] tbody').html('');
	$('[up-file-list="reminder-upload"] tbody').html('');
	
	$('#noticeBody').val('');
	$('#noticeSubject').val('');
	$('#markRemark').val('');
	
}
function proceedToApprovalModal() {
	clearModal();
	var matchFound = $("#match-found").val();
	if(matchFound == 'Y') {
		openRedFlagModal();
	} else {
		openApprovalModal();
	}
}
function proceedToApproval(e) {
	var value = $(this).val();
	if(value=='Y') {
		$("#red-flag-remarks-section").show();
	} else {
		$("#red-flag-remarks-section").hide();
	}		
}
function finalizeProceedToApproval(e) {
	if($('#pre-approval-form').validationEngine('validate')){
		var value = $("input:radio[name=proceedToApproval]:checked").val();
		$("#red-flag-list-modal").modal('hide');
		if(value=='Y') {
			var redFlagRemarks = $("#red-flag-remarks").val();
			$("#red-flag-clearing-remarks").val(redFlagRemarks);
			openApprovalModal();
		} else {
			$("#red-flag-clearing-remarks").val('');
		}
	}
}
function openRedFlagModal() {
	clearModal();
	$('#red-flag-footer').show();
	$("#red-flag-list-modal").modal('show');
}
function openApprovalModal() {
	var serviceId=$('#svcCode').val();
	clearModal();
	if(serviceId=="01" || serviceId=="02" || serviceId=="03"){
		
		$('#otherRemark-div').show();
		if(serviceId=="01"){
			 document.getElementById("other-remark-text").innerHTML = "Other Remark [8(B)]";
			}else if(serviceId=="02"){
				document.getElementById("other-remark-text").innerHTML = "Other Remark [4(A)]";
				}
			else if(serviceId=="03"){
				document.getElementById("other-remark-text").innerHTML = "Other Remark [10(B)]";
				}
	}
	else{
		$('#otherRemark-div').hide();
	}
	$("#nextStageModal").modal('show');
}
function submitForwardUser(){	
	$('#bar-notify').html('');	
	if($('#forward-user-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&user='+$('#forwardUser').val()+'&remark='+encodeURIComponent($('#forwardUserRemark').val())
		+'&sectionId='+$('#userSection').val()+'&requestToken='+$('#requestToken').val();
		var action = 'submit-user-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#userModal-close-btn').click();
					$('#actions').hide();
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());					
				}
				else{
					notifyList(JSON.parse(data[0]),'');
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	} 
}
function EnableConfirmationPane() {
	$('#overlapwnd').remove();
	//$('#'+ovrlp).remove();
	$('#sign-btn').show();
	$('#PrintFrame').show();
	$("#PrintStatusForm").show();
}
function initPreviewCertificate(){
	$('#sign-btn').hide();
	if($('#svcCode').val()=="07"){
		if($('#hos-pdf-format-form').validationEngine('validate')){
			$('#PrintFrame').attr("src",'');	
			var pdfFormat=checkHosPdfFormat($('#pdf-Format').val(),$('#hos-pdf-format').val());	
			var url='generate-pdf-workspace';
			var frameSrc = 'generate-pdf-workspace?appId='+$('#bi-appId').html()+'&svcCode='+$('#svcCode').val()+'&hosPdfFormat='+pdfFormat;	
			$('#PrintFrame').attr("src",frameSrc);
			$('#pdfModal').modal({show:true});
		}
	}else{		
		$('#PrintFrame').attr("src",'');	
		var pdfFormat=checkHosPdfFormat($('#pdf-Format').val(),$('#hos-pdf-format').val());	
		var url='generate-pdf-workspace';
		var frameSrc = 'generate-pdf-workspace?appId='+$('#bi-appId').html()+'&svcCode='+$('#svcCode').val()+'&hosPdfFormat='+pdfFormat;	
		$('#PrintFrame').attr("src",frameSrc);
		$('#pdfModal').modal({show:true});
	}
	
}
function initPreviewSignedCertificate(){		 	
		var url = 'generate-signed-pdf-workspace?appId='+$('#bi-appId').html()+'&svcCode='+$('#svcCode').val();
		$("#signed-pdf-form").attr('action', url);
		$("#signed-pdf-form").submit();
				 
}
function checkHosPdfFormat(v1,v2){	
	if((v1==null || v1=='') && (v2==null || v2=='')) 
		return '';
	else if(v1==null || v1==''){
		return v2;
	}else if(v2==null || v2=='')
		return v1;
}
function initCertificateSignAndMail(){
	var params = '';
	var action = 'check-digital-sign-flag-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){				
			if(data[0]=="success"){				
				if(data[2]=="Y"){
					GeneratePdfBytes();
				}else if(data[2]=="N"){
					sendCertificateWithoutSign();
				}
			}
			else{
				notifyList(JSON.parse(data[1]),'');
			}			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	}); 
}
/* Functions for generating PDF Bytes and Sign and Send */
function GeneratePdfBytes(){
	$('#bar-notify').html('');
	var pdfFormat=checkHosPdfFormat($('#pdf-Format').val(),$('#hos-pdf-format').val());	
	var params = 'appId='+$('#bi-appId').html()+'&svcCode='+$('#svcCode').val()+'&hosPdfFormat='+pdfFormat;
	var action = 'generate-pdf-bytes-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){				
			if(data[2]=="success"){
				notifyList(JSON.parse(data[0]),'');	
				$('#actions').hide();
				$('#mail-actions').hide();
				$('#hos-pdf-format-div').hide();
				$('#pdfModal-close').click();	
				initSignerApplet(data[1],$('#bi-appId').html(),"Online FCRA Services","Ministry of Home Affairs, New Delhi");
			}
			else{
				notifyList(JSON.parse(data[0]),'');
			}			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	}); 
}
function initSignerApplet(data,appId,reason,location){	
	$("#applet-container").empty();	
	$('#applet-div').hide();
	$("#applet-container").append($('<applet/>', 
			//{'code':'AppletLauncher.class', 'archive':'PDFSigning.jar,plugin.jar', 'id':'SignerApplet', 'width':'570px'})
			{'code':'SignerApplet.class', 'id':'SignerApplet', 'width':'700px', 'height':'35px'})
			.append($('<param/>',{'name':'cache_option', 'value':'plugin'}))
			.append($('<param/>',{'name':'cache_archive', 'value':'bcprov-jdk15on-1.54.jar,bcpkix-jdk15on-1.54.jar,itextpdf-5.5.9.jar,esapi-2.0.1.jar,plugin.jar,PDFSigning.jar'}))
			.append($('<param/>',{'name':'cache_version', 'value':'1.5.5, 1.5.5, 5.5.10, 2.0.2, 1.0.1, 1.0.3'}))
			.append($('<param/>',{'name':'document', 'value':data}))
			.append($('<param/>',{'name':'applicationId', 'value':appId}))
			.append($('<param/>',{'name':'reason', 'value':reason}))
			.append($('<param/>',{'name':'location', 'value':location}))
			.append($('<param/>',{'name':'tokenType', 'value':''}))
			.append($('<param/>',{'name':'signatureFieldName', 'value':'SIGNATURE_FIELD'}))
			.append($('<param/>',{'name':'embedCRL', 'value':'false'}))
			//.append($('<param/>',{'name':'signatureBlockHeight', 'value':'40'}))
			.append($('<param/>',{'name':'callbackFunction', 'value':'sendSignedDocument'})));			
}
function sendSignedDocument(applicationId, status, signedData) {
		$('#sticky-notify').html('');
		if(status == "error") {				
			stickyNotify('Oops!','Error in signing document. Try again.','error','sticky-notify',true,0);						
		} else if(status == "cancelled") {			
			stickyNotify('Info!','User has cancelled signing operation.','warning','sticky-notify',true,0);
			$("#applet-container").empty();	
			$('#applet-div').show();
		} 
		else {
			stickyNotify('Success!','Certificate has been signed successfully.','success','sticky-notify',true,0);
			SignCertificateAndSendWithSign(signedData);
		}
}
function SignCertificateAndSendWithSign(signedData){	
	$('#bar-notify').html('');
	var pdfFormat=checkHosPdfFormat($('#pdf-Format').val(),$('#hos-pdf-format').val());	
	var params = 'appId='+$('#bi-appId').html()+'&svcCode='+$('#svcCode').val()+'&hosPdfFormat='+pdfFormat+'&signedData='+encodeURIComponent(signedData);
	var action = 'init-mail-with-sign-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			if(data[1]=="success"){
				notifyList(JSON.parse(data[0]),'');				
				$('#actions').hide();
				$('#mail-actions').hide();
				$('#applet-div').show();
				$('#hos-pdf-format-div').hide();
				$('#pdfModal-close').click();		
				$("#applet-container").empty();
			}
			else{
				notifyList(JSON.parse(data[0]),'');
			}			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	}); 
}
/* Functions for generating PDF Bytes and Sign and Send */

/* Functions for sending PDF without signing it */
function sendCertificateWithoutSign(){	
	$('#bar-notify').html('');
	var pdfFormat=checkHosPdfFormat($('#pdf-Format').val(),$('#hos-pdf-format').val());	
	var params = 'appId='+$('#bi-appId').html()+'&svcCode='+$('#svcCode').val()+'&hosPdfFormat='+pdfFormat;
	var action = 'init-mail-workspace';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			if(data[1]=="" +
					""){
				notifyList(JSON.parse(data[0]),'');				
				$('#actions').hide();
				$('#mail-actions').hide();
				$('#hos-pdf-format-div').hide();
				$('#pdfModal-close').click();				
			}
			else{
				notifyList(JSON.parse(data[0]),'');
			}			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	}); 
}

/* Functions for sending PDF without signing it */


function submitUserNote(){
	$('#bar-notify').html('');
	if($('#user-note-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+encodeURIComponent($('#userNote').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-user-note-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#noteModal-close-btn').click();
					$('#actions').hide();
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}
				else{
					notifyList(JSON.parse(data[0]),'');
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function submitUserClarification(){
	$('#bar-notify').html('');
	if($('#clarification-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+encodeURIComponent($('#clarificationRemark').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-user-clarification-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#clarificationModal-close-btn').click();
					$('#actions').hide();
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}
				else{
					notifyList(JSON.parse(data[0]),'');
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function submitShowCause(){
	$('#bar-notify').html('');
	if($('#showcause-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&noticeSubject='+$('#noticeSubject').val()+'&noticeBody='+encodeURIComponent($('#noticeBody').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-showcause-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#showCauseModal-close-btn').click();
					$('#actions').hide();
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}
				else{
					notifyList(JSON.parse(data[0]),'');
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function submitUserOnHold(){
	$('#bar-notify').html('');
	if($('#hold-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+encodeURIComponent($('#onholdRemark').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-user-onhold-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#onhold-close-btn').click();
					$('#actions').hide();
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}
				else{
					notifyList(JSON.parse(data[0]),'');
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function submitResumeProcess(){
	$('#bar-notify').html('');
	if($('#resume-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+encodeURIComponent($('#resumeRemark').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-resume-process-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#resume-close-btn').click();
					$('#actions').show();
					$('#resume-btn').hide();
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}
				else{
					notifyList(JSON.parse(data[0]),'');
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}
function submitForwardOffice(){
	$('#bar-notify').html('');
	if($('#forward-office-form').validationEngine('validate')){		
		var formData = new FormData();		
		formData.append('appId',$('#bi-appId').html());
		formData.append('office',$('#forwardOffice').val());
		formData.append('remark',$('#forwardOfficeRemark').val());	
		formData.append('requestToken',$('#requestToken').val());
		
		$('#officeModal').find('.stage-files').each(function() {			
			formData.append('files',$(this)[0].files[0]);
			formData.append('ids',$(this).attr('id'));			
		});
		var action = 'submit-office-chat-workspace';		
		$.ajax({
			url: action,
			method:'POST',
			data:formData,
			dataType:'json',
			cache: false,													
	        contentType: false,	
	        enctype: 'multipart/form-data',
	        processData: false,
			success: function(data){	
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#officeModal-close-btn').click();
					$('#actions').hide();
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}else{
					notifyList(JSON.parse(data[0]),'');
					if(token != null && token != '')
						$('#requestToken').val(token);
				}					
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}

}
function submitNextStage(){	
	var proceed=true;
	$('#bar-notify').html('');	
	if($('#next-stage-form').validationEngine('validate')){		
		if($('#svcCode').val() == "02" && $('#pp-amount-flag').is(":checked")){
			var ppAmountValues = []; var ppAmount=0;
			$("input[name='installments']").each(function() {
				ppAmountValues.push($(this).val());
				ppAmount+=parseInt($(this).val());
			});		
			var totalAmount=parseInt($('#ppAmount').val());			
			if(ppAmount!=totalAmount){
				proceed=false;
				alert('Total contribution is not matching with sum of installments. Please re-check the amounts.');
			}
		}
		if(proceed == true){
			var formData = new FormData();		
			$('#pdfFormat').val($('#pdf-Format').val());		
			formData.append('appId',$('#bi-appId').html());		
			formData.append('remark',$('#forwardAgentRemark').val());				
			formData.append('pdfFormat',$('#pdf-Format').val());
			formData.append('validityFrom',$('#validityFrom').val());
			formData.append('requestToken',$('#requestToken').val());
			formData.append('redFlagClearingRemarks',$('#red-flag-clearing-remarks').val());
			formData.append('installments',ppAmountValues);
			formData.append('installmentNumbers',$('#installmentNumbers').val());
			formData.append('ppInsFlag',$('#ppInsFlag').val());
			formData.append('otherRemarks',$('#otherRemark').val());
			var action = 'submit-next-stage-chat-workspace';		
			$.ajax({
				url: action,
				method:'POST',
				data:formData,
				dataType:'json',
				cache: false,													
		        contentType: false,	
		        enctype: 'multipart/form-data',
		        processData: false,
				success: function(data){		
					if(data[1]=="success"){
						notifyList(JSON.parse(data[0]),'');	
						if(data[4]!=null)
							notifyList(JSON.parse(data[4]),'');
						$('#nextStageModal-close-btn').click();				
						$('#actions').hide();
						if(data[2] == "Y"){						
							initPreviewCertificate();
						}
						var token = data[6];
						if(token != null && token != '')
							$('#requestToken').val( token);
						getChat($('#bi-appId').html());
					}
					else{
						if(data[5]!=null || data[5]=="YES"){
							$('#next-stage-btn').hide();
						}						
						notifyList(JSON.parse(data[0]),'');
						var token = data[6];
						if(token != null && token != '')
							$('#requestToken').val( token);
					}
				},
				error: function(textStatus,errorThrown){
					alert('error');			
				}
			});
		}
	}
}
function submitReject(){	
	$('#bar-notify').html('');
	if($('#reject-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+encodeURIComponent($('#rejectRemark').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-reject-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					if(data[3]!=null)
						notifyList(JSON.parse(data[3]),'');
					$('#rejectModal-close-btn').click();
					$('#actions').hide();
					if(data[2] == "Y"){						
						initPreviewCertificate();
					}	
					var token = data[4];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}else{
					notifyList(JSON.parse(data[0]),'');
					var token = data[4];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}

function submitClose(){	
	$('#bar-notify').html('');
	if($('#close-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+encodeURIComponent($('#closeRemark').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-close-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');					
					$('#closeModal-close-btn').click();
					$('#actions').hide();		
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}else{
					notifyList(JSON.parse(data[0]),'');		
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}

function submitCLarificationReminder(){
	$('#bar-notify').html('');
	if($('#close-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+encodeURIComponent($('#reminderRemark').val())+'&requestToken='+$('#requestToken').val();
		var action = 'submit-reminder-chat-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');					
					$('#reminderModal-close-btn').click();
					$('#actions').hide();		
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					getChat($('#bi-appId').html());
				}else{
					notifyList(JSON.parse(data[0]),'');		
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}
}

function showDesc(obj){	
	$(obj).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up').attr('data-original-title','Collapse').tooltip('hide').tooltip('show');
	var cbo=$(obj).closest('.chat-bubble');
	cbo.attr('padding-bottom','10px');
	cbo.find('.chat-summary').hide();
	cbo.find('.chat-desc').show();	
	$(obj).unbind('click').click(function(){
		hideDesc(obj);
	});
}
function hideDesc(obj){	
	$(obj).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down').attr('data-original-title','Expand').tooltip('hide').tooltip('show');
	var cbo=$(obj).closest('.chat-bubble');
	cbo.attr('padding-bottom','0');
	cbo.find('.chat-summary').show();
	cbo.find('.chat-desc').hide();	
	$(obj).unbind('click').click(function(){
		showDesc(obj);
	});
}
function resetFields(){
	$('#forwardAgentRemark').val('');	
	$('#rejectRemark').val('');	
	$('#pdf-format-div').val('');	
	$('#nextStageModal-error').html('');	
	$('#rejectModal-error').html('');
}

function prepareOfficeMarkList(data){
	$('#office-mark-list').html('');
	//$('#office-mark-list').show();
	var htmlContent='';
	if(data != ''){		
		htmlContent+='<div class = "col-xs-12">'; 
		$.each(data,function(index,item){
			if(item.ld == "2")
				htmlContent+='<div class = "row"><div class = "col-xs-12"><button type="button" id="mark-ib-btn" class="btn btn-warning" onclick="markOffice('+item.ld+');" data-toggle="modal" '
					+'data-target="#markOfficeModal"><span class="glyphicon glyphicon-ok"></span>&nbsp;Mark to IB</button></div></div><br />';
			if(item.ld == "3")
				htmlContent+='<div class = "row"><div class = "col-xs-12"><button type="button" id="mark-ib-btn" class="btn btn-warning" onclick="markOffice('+item.ld+');" data-toggle="modal" '
					+'data-target="#markOfficeModal"><span class="glyphicon glyphicon-ok"></span>&nbsp;Mark to RAW</button></div></div>';
		});		
		htmlContent+='</div>'; 
		$('#office-mark-list').html(htmlContent);
	}

}

function markOffice(officeId){
	//alert(officeId);
	$("#markOfficeId").val(officeId);
	clearModal();
}

function submitMarkRemark(){
	$('#bar-notify').html('');
	if($('#markOfficeModal-form').validationEngine('validate')){
		var params = 'appId='+$('#bi-appId').html()+'&remark='+$('#markRemark').val()+'&markOfficeId='+$('#markOfficeId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'mark-office-workspace';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){
					$('#markOfficeModal').modal({show:false});
					notifyList(JSON.parse(data[0]),'');					
					$('#markOfficeModal-close-btn').click();
					//$('#actions').hide();		
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
					//getChat($('#bi-appId').html());
					getApplicationDetails({'applicationId':$('#bi-appId').html()});
				}else{
					$('#markOfficeModal').modal({show:true});
					notifyList(JSON.parse(data[0]),'');		
					var token = data[2];
					if(token != null && token != '')
						$('#requestToken').val(token);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
	}

}