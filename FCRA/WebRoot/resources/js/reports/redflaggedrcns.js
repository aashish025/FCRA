$(document).ready(function (){
	initializeRedFlaggedList();
	$("#remove-form").validationEngine({promptPosition: 'bottomRight'});
	$("#add-form").validationEngine({promptPosition: 'bottomRight'});
	
	
});

function initializeRedFlaggedList() {
	$('#red-flag-div').show();
	$('#headingforredyellowflag').show();
	$("#add-btn").show();
	$("#red-flagged-list").html('');
	$("#red-flagged-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-red-flagged-rcn',
    		defaultsortcolumn:'redflaggedOn',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:' Registration Number ', name:'rcn'},
				{title:'Association Name', name:'associationName', sortable:true},
		        {title:'State', name:'state' , sortable:true},
				{title:'District', name:'district', sortable:true},
				{title:'Red Flagged Category', name:'redFlagCategory'},
				{title:'Remark', name:'remarks'},
				{title:'Red Flagged By', name:'redflaggedBy'},
				{title:'Red Flagged On', name:'redflaggedOn', sortable:true},
				{title:'Flag Type', name:'cateogryType'}
				
				
			],
			onRowSelect:function(rowdata){OnclickApplicationDetails(rowdata);}
    		});	
	clearForm();
}





function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});

}

function  getRegistrationDetails(rcn){
	 var url = 'popup-registration-tracking?appId='+rcn;
	 openLink(url);
}
function PrintPdf(){
////for js
      var params='reportFormat=1'+'&reportType=redflagged';
		var url = 'print-red-flagged-rcn?'+params;
		$("#report-print-form").attr('action', url);
		$("#report-print-form").submit();	
	}
	
function PrintCSV(){
////for js
      var params='reportFormat=4'+'&reportType=redflagged';
		var url = 'print-red-flagged-rcn?'+params;
		$("#report-print-form").attr('action', url);
		$("#report-print-form").submit();	
	}
	
function PrintExcel(){
////for js
      var params='reportFormat=2'+'&reportType=redflagged';
		var url = 'print-red-flagged-rcn?'+params;
		$("#report-print-form").attr('action', url);
		$("#report-print-form").submit();	
	}
	
function addDetails(){
	$('#headingforredyellowflag').hide();
	$('#back-section').show();
	$('#searching-onclick-button').show();
	$('#report-button').hide();
	$('#red-flagged-list').hide();
	$('#add-btn').hide();
	$("#appid-search").show();
	$("#adv-search-button-div").show();
	$('#advance-search').hide();
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
		    		dataurl:'get-application-list-red-flagged-rcn?applicationId='+$('#applicationId').val()+'&applicationName='+$('#applicationName').val(),
		    		columndetails:
					[
						{title:'Registration Number', name:'assoRegnNumber',sortable:true},					
						{title:'Association Name', name:'applicantName'},					
					],
					onRowSelect:function(rowdata){getApplicationDetails(rowdata);}
	    });	
		$('#application-list-div').show();
	}

function toggleSearch(){	
	resetAll();
	if ( $('#appid-search').is(':visible') ){		
		$('#advance-search').show();
		$('#appid-search').hide();
		$('#toggle-btn').html('<span class="fa fa-search"></span>&nbsp;Basic Search');	
	}else if($('#advance-search').is(':visible')){		
		$('#appid-search').show();	
		$('#advance-search').hide();
		$('#toggle-btn').html('<span class="fa fa-search"></span>&nbsp;Advance Search');
	}
}
	
function resetAll(){
	$('#applicationId').val('');
	$('#bar-notify').html('');
	$('#state').val('');
	$('#district').val('');
	$('#applicationName').val('');
	$('#applicationId').val('');
	
}
function getDistrict(){
	var params = 'state='+$('#state').val();
	var action = 'get-district-red-flagged-rcn';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			populateSelectBox(data,'Select District','district');			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
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
function getAdvanceSearchApplicationList(){	
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
		    		dataurl:'get-advance-application-list-red-flagged-rcn?state='+$('#state').val()+'&district='+$('#district').val()+'&applicationName='+$('#applicationName').val()+'&functionaryName='+$('#functionaryName').val(),
		    		columndetails:
					[
						{title:'Registration Number', name:'assoRegnNumber',sortable:true},					
						//{title:'Section File No', name:'sectionFileNo',sortable:true},
						{title:'Applicant / Association Name', name:'applicantName'},					
					],
					onRowSelect:function(rowdata){getApplicationDetails(rowdata);}
	    });	
		$('#application-list-div').show();
}
function getApplicationDetails(data){
	$('#add-details-btn').show();
	$('#bar-notify').html('');
	$('#red-flagged-list').hide();
	$('#application-list-div').hide();
	barContent=[];
	resetRedFlag();	
	oplr();
	var regn=data.assoRegnNumber;
	var params = 'appId='+data.assoRegnNumber;
	var action = 'get-application-details-red-flagged-rcn';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');			
				initApplicationDetails(JSON.parse(data[0])[0]);						
			// To check and put Red Flag action
			if(data[5]=="YES"){  // Having Red Flag Role
				if(data[4]=="NO"){	// not added in red flag list				
					$('#add-red-flag-btn').show();
					$('#remove-red-flag-btn').hide();
					$('#add-yellow-flag-btn').show();
				}
				$('#asso-details-title').html('Details of Association');
				$('#add-details-btn').show();
				$('#delete-YellowFlag-donor').hide();
				$('#delete-red-actions').hide();
				
			}	
			
		/*	
			if(data[6]=="YES"){  // Having Red Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list			
					if(data[11]=="YES"){ // Checking Category Type- RED Category
						$('#remove-red-flag-btn').show();
						$('#add-red-flag-btn').hide();
						$('#add-yellow-flag-btn').hide();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-danger');
						$('#remove-red-flag-btn').text('Remove RED Flagging');
					}
				}
			}	
			
			if(data[10]=="YES"){  // Having Yellow Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list	
					if(data[12]=="YES"){	// Checking Category Type- YELLOW Category
						$('#remove-red-flag-btn').show();
						$('#add-red-flag-btn').show();
						$('#add-yellow-flag-btn').hide();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-warning');
						$('#remove-red-flag-btn').text('Remove  YELLOW Flagging');
					}
				}
			}	
			if(data[16]=="YES"){  // Having Yellow Add Flag Role
				if(data[4]=="YES"){ // already added in red flag list			
					if(data[11]=="YES"){ // Checking Category Type- RED Category
						$('#remove-red-flag-btn').hide();
						$('#add-red-flag-btn').show();
						$('#add-yellow-flag-btn').show();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-danger');
						$('#remove-red-flag-btn').text('Remove  Yellow Flagging');
					}
				}
			}	
			*/
			
			if(!(data[7]==null || data[7]==""))
				populateSelectBox(JSON.parse(data[7]),'Select Reason','redFlagCategory');	
			
			if(!(data[8]==null || data[8]==""))
				prepareRedFlagDetails(JSON.parse(data[8]),data[11],data[12]);			
			
			
			
			roplr();
		},
		error: function(textStatus,errorThrown){
			roplr();
			alert('error');			
		}
	});	
}
function prepareRedFlagDetails(data,redFlag,yellowFlag){
	

	if(data=="" || data==null){		
		return;
	}	
	if(redFlag=="YES"){
		$('#add-details-btn').hide();
		$('#delete-red-actions').show();
		$('#delete-YellowFlag-donor').hide();
		$('#asso-details-title').html('Details of Association [ <span class="text-danger blink_me_red" style="font-size:18px;font-weight: bold;">RED FLAGGED</span> ]');
	}
	if(yellowFlag=="YES"){
		$('#add-details-btn').hide();
		$('#delete-YellowFlag-donor').show();
		$('#delete-red-actions').hide();
		$('#asso-details-title').html('Details of Association [ <span class="text-warning blink_me_yellow" style="font-size:18px;font-weight: bold;">YELLOW FLAGGED</span> ]');
	}
	$('#red-flag-details').show();	
	$.each(data, function(index, item){
		$('#orgOffice').html(item.p4);
		$('#orgOrderNumber').html(item.p5);
		$('#orgOrderDate').html(item.p6);
		$('#actionBy').html(item.p3);
		$('#remark').html(item.p1);	
		$('#statusDate').html(item.p2);
		$('#category').html(item.p7);
	});
}
function resetRedFlag(){
	$('#remove-red-flag-btn').hide();
	$('#add-red-flag-btn').hide();
	$('#red-flag-details').hide();	
	//postWork();
}
function initApplicationDetails(data){	
		$('#app-info').show();	
		prepareBasicInfo(data);	
}
function prepareBasicInfo(data){
	$('#remove-red-flag-btn').show();
	$('#add-red-flag-btn').show();
	$('#regnNumber').html('<a href="javascript:getRegistrationDetails(\''+data.assoRegnNumber+'\');">'+data.assoRegnNumber+'</a>');
	$('#regnDate').html(data.assoRegnDate);
	$('#secFileNumber').html(data.sectionFileNo);
	$('#applicantName').html(data.applicantName);
	$('#currentStatus').html(data.assoRegnStatusDesc);	
	if(data.assoRegnStatus == 1){		// Cancelled
		$('#reg-cancellation-details').show();
	}else{
		$('#reg-cancellation-details').hide();
	}
	$('#assoAddress').html(data.assoAddress);
	$('#assoNature').html(data.assoNature);
	$('#assoBank').html(data.assoBank);	
	$('#assoBankAddress').html(data.assoBankAddress);
	$('#assoAccNumber').html(data.assoAccNumber);	
	$('#lastRenewed').html(data.lastRenewed);
	$('#validUpTo').html(data.validUpTo);

	var certContent='<button type="button" title="Download details as PDF"'+ 
	' class="btn btn-link btn-sm" onclick="javascript:printFullReportPDF(\''+data.assoRegnNumber+'\');">'+
	'<span class="text-danger fa fa-file-pdf-o fa-2x"></span></button>&nbsp;&nbsp;'+
	'<button type="button" title="Download details as Excel File"'+ 
	'class="btn btn-link btn-sm" onclick="javascript:printFullReportExcel(\''+data.assoRegnNumber+'\');">'+
	'<span class="text-success fa fa-file-excel-o fa-2x"></span></button>&nbsp;&nbsp;'+
	'<button type="button" title="Download details as CSV File"'+ 
	'class="btn btn-link btn-sm" onclick="javascript:printFullReportCSV(\''+data.assoRegnNumber+'\');">'+
	'<span class="text-info fa fa-file-excel-o fa-2x"></span></button>';
	$('#bi-doc').html(certContent);
}
function postWork(){
	$("#app-list").html('');
	$('#app-info').hide();
	$('#remove-red-flag-btn').hide();
	$('#add-red-flag-btn').hide();
	$('#application-list-div').hide();
	$('#removeStatusRemark').val('');
	$('#addStatusRemark').val('');	
	$('#applicationId').val('');
	$('#originatorOffice').val('');
	$('#orderNumber').val('');
	$('#orderDate').val('');
	$('#redFlagCategory').val('');
	$('#originatorOfficeR').val('');
	$('#orderNumberR').val('');
	$('#orderDateR').val('');	
	//$('#appid-search').show();	
	$('#advance-search').hide();
	$('#report-button').show();
	$('#back-section').hide();
	$('#add-btn').show();
	$('#adv-search-button-div').hide();
	$('#appid-search').hide();
	$("#red-flagged-list").show();
	initializeRedFlaggedList();
	}


function goBack(){
	$('#headingforredyellowflag').show();
	$('#red-flagged-list').show();
	$('#searching-onclick-button').hide();
	$('#add-btn').show();
	$('#back-section').hide();
	$('#app-info').hide();
	$('#application-list-div').hide();
	$("#red-flag-div").show();
	$('#report-button').show();
}
function removeFromRedFlagList(){
	var params = 'appId='+$('#regnNumber').text()+'&remark='+$('#removeStatusRemark').val()
				+'&office='+$('#originatorOfficeR').val()+'&order='+$('#orderNumberR').val()+'&date='+$('#orderDateR').val()+'&flagdelete='+$('#flagdelete').val();
	
	var action = 'remove-red-yellow-red-flagged-rcn';	
	if($('#remove-form').validationEngine('validate')){
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){
					notifyList(JSON.parse(data[0]),'');
					$('#removeModal-close-btn').click();	
					postWork();
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
}


function addToRedFlagList(){	
	var params = 'appId='+$('#regnNumber').text()+'&remark='+$('#addStatusRemark').val()+'&category='+$('#redFlagCategory').val()
				+'&office='+$('#originatorOffice').val()+'&order='+$('#orderNumber').val()+'&date='+$('#orderDate').val()+'&flagvalue='+$('#flagvalue').val();
	var action = 'add-red-flagged-rcn';	
	if($('#add-form').validationEngine('validate')){
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[1]=="success"){					
					notifyList(JSON.parse(data[0]),'');
					$('#addModal-close-btn').click();	
					postWork();
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
}
function addFlagging(a){
	$("#addModal").modal({show:true});
	$('#flagvalue').val(a);

	
}




function OnclickApplicationDetails(data){
	$("#red-flag-div").hide();
	$('#add-details-btn').hide();
	$('#back-section').show();
	$("#add-btn").hide();
	$('#bar-notify').html('');
	barContent=[];
	resetRedFlag();	
	oplr();
	var regn=data.assoRegnNumber;
	var params = 'appId='+data.rcn;
	var action = 'get-application-details-red-flagged-rcn';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');			
				initApplicationDetails(JSON.parse(data[0])[0]);	
				initializeAnuualStatusList(JSON.parse(data[17]));
				
				
		/*	// To check and put Red Flag action
			if(data[5]=="YES"){  // Having Red Flag Role
				if(data[4]=="NO"){	// not added in red flag list				
					$('#add-red-flag-btn').show();
					$('#remove-red-flag-btn').hide();
					$('#add-yellow-flag-btn').show();
				}
				$('#asso-details-title').html('Details of Association');
			}	
			
			
			if(data[6]=="YES"){  // Having Red Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list			
					if(data[11]=="YES"){ // Checking Category Type- RED Category
						$('#remove-red-flag-btn').show();
						$('#add-red-flag-btn').hide();
						$('#add-yellow-flag-btn').hide();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-danger');
						$('#remove-red-flag-btn').text('Remove RED Flagging');
					}
				}
			}	
			
			if(data[10]=="YES"){  // Having Yellow Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list	
					if(data[12]=="YES"){	// Checking Category Type- YELLOW Category
						$('#remove-red-flag-btn').show();
						$('#add-red-flag-btn').show();
						$('#add-yellow-flag-btn').hide();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-warning');
						$('#remove-red-flag-btn').text('Remove  YELLOW Flagging');
					}
				}
			}	
	
			if(data[16]=="YES"){  // Having Yellow Add Flag Role
				if(data[4]=="YES"){ // already added in red flag list			
					if(data[11]=="YES"){ // Checking Category Type- RED Category
						$('#remove-red-flag-btn').hide();
						$('#add-red-flag-btn').show();
						$('#add-yellow-flag-btn').show();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-danger');
						$('#remove-red-flag-btn').text('Remove  Yellow Flagging');
					}
				}
			}*/
					
			if(!(data[7]==null || data[7]==""))
				populateSelectBox(JSON.parse(data[7]),'Select Reason','redFlagCategory');	
			
			if(!(data[8]==null || data[8]==""))
				prepareRedFlagDetails(JSON.parse(data[8]),data[11],data[12]);			
			
			
			
			roplr();
		},
		error: function(textStatus,errorThrown){
			roplr();
			alert('error');			
		}
	});	
}

function removeListforModal(flagdelete){
	if($('#submit-button-form').validationEngine('validate') ){
		$('#remove-form').trigger("reset");
		 $('#flagdelete').val(flagdelete);//hidden field 
			$("#removeModal").modal({
				show : true
			});
	}
   
}


function printFullReportPDF(regnNumber){
	var params='rcnNumber='+regnNumber+'&reportFormat=1'+'&reportType=tracking';
	var url = 'download-red-flagged-rcn?'+params;
	$("#report-print-form22").attr('action', url);
	$("#report-print-form22").submit();	
}
function printFullReportExcel(regnNumber){	
	var params='rcnNumber='+regnNumber+'&reportFormat=2'+'&reportType=tracking';
	var url = 'download-red-flagged-rcn?'+params;
	$("#report-print-form22").attr('action', url);
	$("#report-print-form22").submit();	
}
function printFullReportCSV(regnNumber){	
	var params='rcnNumber='+regnNumber+'&reportFormat=4'+'&reportType=tracking';
	var url = 'download-red-flagged-rcn'+params;
	$("#report-print-form22").attr('action', url);
	$("#report-print-form22").submit();	
}

function initializeAnuualStatusList(reg) {
	$("#annual-status-list").html("");
	$("#annual-status-list").initLocalgrid(
			{
				columndetails:[

				               {title:'Block Year', name:'annualyear'},
				               {title:'Uploaded?', name:'status', formatter : function(rowdata) {
				            	   var apfcraRcnid=rowdata.status; var        link ;
				            	   if(apfcraRcnid=="No"|| apfcraRcnid=='NO') {
				            		   link = '<p class = "text-danger" ><b>'+apfcraRcnid+'</b></a>';

				            	   } 
				            	   else
				            		   link = '<p class = "text-success"><b>'+apfcraRcnid+'</b></a>';

				            	   return link;
				               }},

				               ],

			});
	$("#annual-status-list").addListToLocalgrid(reg); 



}



function showOldAnnualReturnDetails(registrationNumber){	
	var url = 'https://fcraonline.nic.in/fc_annual_returns_rcn.aspx?reg_no='+registrationNumber;
	openLink(url);	
}


function showOptions() {
	$("#options").popover('destroy');
	var cont = '';
	$(".popover-menu").html('');
	cont = '<ul class="popover-menu" style="list-style-type:none; padding-left:0px; padding-right:0px;"><li><a href="javascript:annualOpenModel();">Red Flagged Associations who has filed Annual Returns </a></li></ul>';
	$("#options").popover({title:'',content:cont,html:true,placement:'left',trigger:'focus'});
	$("#options").popover('show');
}


function annualOpenModel(){
	$("#annualModal").modal({show:true});
	$("#annual1-status-list").html('');
	$("#annual1-status-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-annual-red-flagged-rcn',
    		defaultsortcolumn:'associationName',
    		defaultsortorder:'asc',
    		columndetails:
			[
				{title:' Registration Number ', name:'rcn'},
				{title:'Association Name', name:'associationName',sortable:true},
		        {title:'State', name:'state' ,sortable:true},
				{title:'District', name:'district',sortable:true},
				{title:'Registered On', name:'registered_on'},
				{title:'Last Renewed On ', name:'lastrenewedOn'},
				{title:'Expiry On', name:'expiryOn'},
			/*	{title:'Start Year', name:'startYear'},
				{title:'End Year', name:'endYear'},
				{title:'To be Uploaded', name:'tobeUploaded'},
				{title:'Uploaded', name:'uploaded'}*/
				
				
			],
			onRowSelect:function(rowdata){OnclickAnnualApplicationDetails(rowdata);}
    		});	
	clearForm();
	
}


function OnclickAnnualApplicationDetails(rowdata){
	//$('#annualModal').click(function() {
	    $('#annualModal').modal('hide');
	    $("#red-flag-div").hide();
	//});
	OnclickApplicationDetails(rowdata);
	
}
