$(document).ready(function (){
	initializeBlueFlaggedList();
	$("#remove-form").validationEngine({promptPosition: 'bottomRight'});
	$("#add-form").validationEngine({promptPosition: 'bottomRight'});
	
	
});

function initializeBlueFlaggedList() {
	$('#headingforredyellowflag').show();
	$("#add-btn").show();
	$("#blue-flagged-list").html(''); 
	$("#blue-flagged-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-blue-flagged-rcn',
    		defaultsortcolumn:'redflaggedOn',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:' Registration Number ', name:'rcn'},
				{title:'Association Name', name:'associationName', sortable:true},
		        {title:'State', name:'state' , sortable:true},
				{title:'District', name:'district', sortable:true},
				{title:'Remark', name:'remarks'},
				{title:'Blue Flagged On', name:'blueflaggedOn', sortable:true},
			
				
				
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
      var params='reportFormat=1'+'&reportType=blueflagged';
		var url = 'print-blue-flagged-rcn?'+params;
		$("#report-print-form").attr('action', url);
		$("#report-print-form").submit();	
	}
	
function PrintCSV(){
////for js
      var params='reportFormat=4'+'&reportType=blueflagged';
      //alert(params);
		var url = 'print-blue-flagged-rcn?'+params;
		$("#report-print-form").attr('action', url);
		$("#report-print-form").submit();	
	}
	
function PrintExcel(){
////for js
      var params='reportFormat=2'+'&reportType=blueflagged';
     // alert(params);
		var url = 'print-blue-flagged-rcn?'+params;
		$("#report-print-form").attr('action', url);
		$("#report-print-form").submit();	
	}
	
function addDetails(){
	$('#headingforredyellowflag').hide();
	$('#back-section').show();
	$('#searching-onclick-button').show();
	$('#report-button').hide();
	$('#blue-flagged-list').hide();
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
		    		dataurl:'get-application-list-blue-flagged-rcn?applicationId='+$('#applicationId').val()+'&applicationName='+$('#applicationName').val(),
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
function getDistrict(state){
	var params = 'state='+state;
	var action = 'get-district-blue-flagged-rcn';	
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
		    		dataurl:'get-advance-application-list-blue-flagged-rcn?state='+$('#state').val()+'&district='+$('#district').val()+'&applicationName='+$('#applicationName').val()+'&functionaryName='+$('#functionaryName').val(),
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
	$('#delete-blueFlag').hide();	
	$('#add-details-btn').show();
	$('#bar-notify').html('');
	$('#blue-flagged-list').hide();
	$('#application-list-div').hide();
	barContent=[];
	resetRedFlag();	
	oplr();
	var regn=data.assoRegnNumber;
	var params = 'appId='+data.assoRegnNumber;
	var action = 'get-application-details-blue-flagged-rcn';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');			
				initApplicationDetails(JSON.parse(data[0])[0]);	
				initializeAnuualStatusList(JSON.parse(data[8]));
			//	alert(data[10]);
			// To check and put Blue Flag action
			if(JSON.parse(data[10])=="YES"){  // Having ANNUAL 
			
				if(JSON.parse(data[11])=="YES"){
					$('#delete-blueFlag').show();	
				}
				else{
					$('#delete-blueFlag').hide();	
				}
			
			}	
			
			
			if(!(data[8]==null || data[8]==""))
				prepareBlueFlagDetails(JSON.parse(data[9]),JSON.parse(data[10]));			
			
			
			
			roplr();
		},
		error: function(textStatus,errorThrown){
			roplr();
			alert('error');			
		}
	});	
}
function prepareBlueFlagDetails(data,blueFlag){	
	$('#asso-details-title').html("Details of Association");
	if(data=="" || data==null){		
		return;
	}	
//	alert(blueFlag);
	if(blueFlag=="YES"){
		
		$('#asso-details-title').html('Details of Association [ <span class="text-danger blink_me_blue" style="font-size:18px;font-weight: bold;">BLUE FLAGGED</span> ]');
	}
	
	$('#red-flag-details').show();	
	$.each(data, function(index, item){
		$('#actionBy').html(item.p3);
		$('#remark').html(item.p1);	
		$('#statusDate').html(item.p2);
		
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
	$('#advance-search').hide();
//	$('#toggle-btn').html('<span class="fa fa-search"></span>&nbsp;Advance Search');	
	$('#back-section').hide();
	$('#add-btn').show();
	$('#adv-search-button-div').hide();
	$('#appid-search').hide();
	$("#blue-flagged-list").show();
	$("#red-flag-div").show();
	initializeBlueFlaggedList();
	}


function goBack(){
	$('#headingforredyellowflag').show();
	$('#blue-flagged-list').show();
	$('#searching-onclick-button').hide();
	$('#add-btn').show();
	$('#back-section').hide();
	$('#app-info').hide();
	$('#application-list-div').hide();
	$("#red-flag-div").show();
	$('#report-button').show();
}
function removeFromRedFlagList(){
	var params = 'appId='+$('#regnNumber').text()+'&remark='+$('#removeStatusRemark').val();
		var action = 'remove-blue-flagged-rcn';	
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
	var action = 'get-application-details-blue-flagged-rcn';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');			
				initApplicationDetails(JSON.parse(data[0])[0]);	
				//alert(JSON.parse(data[8]));
				initializeAnuualStatusList(JSON.parse(data[8]));
				// To check and put Blue Flag action
				if(JSON.parse(data[10])=="YES"){  // Having Blue Flag Role
				
					if(JSON.parse(data[11])=="YES"){
						$('#delete-blueFlag').show();	
					}
					else{
						$('#delete-blueFlag').hide();	
					}
				
				}	
				
			
			if(!(data[8]==null || data[8]==""))
				prepareBlueFlagDetails(JSON.parse(data[9]),JSON.parse(data[10]));			
			
			
			
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
	var url = 'download-blue-flagged-rcn?'+params;
	$("#report-print-form22").attr('action', url);
	$("#report-print-form22").submit();	
}
function printFullReportExcel(regnNumber){	
	var params='rcnNumber='+regnNumber+'&reportFormat=2'+'&reportType=tracking';
	var url = 'download-blue-flagged-rcn?'+params;
	$("#report-print-form22").attr('action', url);
	$("#report-print-form22").submit();	
}
function printFullReportCSV(regnNumber){	
	var params='rcnNumber='+regnNumber+'&reportFormat=4'+'&reportType=tracking';
	var url = 'download-blue-flagged-rcn'+params;
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
	cont = '<ul class="popover-menu" style="list-style-type:none; padding-left:0px; padding-right:0px;"><li><a href="javascript:annualOpenModel();">Blue Flagged Associations who has filed Annual Returns </a></li></ul>';
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
    		dataurl:'get-annual-blue-flagged-rcn',
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
				{title:'Expiry On', name:'expiryOn'}
			
				
				
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
