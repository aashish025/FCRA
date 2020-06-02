var yearList = null;
$(document).ready(function(){
	
	hideAll();
	$(document).ajaxError(function( event, jqxhr, settings, thrownError ) {
		  if(jqxhr.status == 401) {
			  window.location.reload();
		  }
		});
	$("#report-type").on('change', reportTypeChange);
	$("#cancel-button").on('click', cancelReportGeneration);
	$("#generate-button").on('click', generateReport);
	
   
	var action ='get-lists-esp-mis-report';
	var params = '';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			inilializeLists(data);
		},
		error: function(textStatus,errorThrown){
		}
	});
	
});	


// Keep all the lists required for population of static select box here 
var countryList = null;
var serviceTypeList = null;
var redFlagTypeList = null;
var YearListstatus=null;
var purposeList=null;
var userList=null;
var religionList=null;
var officeId=null;
var assoNatureList =null;

function inilializeLists(data) {
  countryList = data['countryList'];
  serviceTypeList = data['serviceTypeList'];
  redFlagTypeList = data['redFlagTypeList'];
  YearListstatus= data['YearListstatus'];
  purposeList=data['purposeList'];
  blockYearList= data['blockYearList'];
  stateList= data['stateList'];
  userList= data['userList'];
  religionList=data['religionList'];
  assoNatureList =data['assoNatureList'];
  officeId=data['myLoginofficeId'][0];
  myofficeId=JSON.stringify(officeId.v);
// alert(myofficeId);
}

function populateSelectBox(list, listHeadText, id) {
	var selectBox = $('#'+id);
	selectBox.empty();	
	selectBox.append($('<option/>', {value:'ALL', text:'ALL'}));
	$.each(list, function(index, item) {		
		selectBox.append($('<option/>', {value:item.k, text:item.v}));
	});
};
function populateSelectBoxWithoutAll(list, listHeadText, id) {
	var selectBox = $('#'+id);
	selectBox.empty();
	if(listHeadText != null && listHeadText != '')
		selectBox.append($('<option/>', {value:'', text:listHeadText}));
	$.each(list, function(index, item) {		
		selectBox.append($('<option/>', {value:item.k, text:item.v}));
	});
};
function reportTypeChange(e) {	
	var reportType = $(this).val();	
	if(reportType == '' || reportType == 'undefined') {
		hideAll();
	}
	else {
		clearParameters();
		showReportParams(reportType);
		$("#report-parameters-section").show();
		$("#report-submission").show();
		$("#report-data").html('');
	}
};

function showReportParams(reportType) {
	//alert("report"+reportType);
	var selectedSection = 'mis-report-'+reportType;		
	$('#report-parameters').load('resources/mis-reports.html #' + selectedSection, function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success'){
			// Any post loading action can be done here according to the report type
			if(reportType == '1') {				
				populateSelectBox(serviceTypeList, 'ALL', 'service-type');
				populateSelectBox(YearListstatus, 'ALL', 'statusYear-List');
				//selectAll();
			}
			else if(reportType == '2'){
				populateSelectBox(serviceTypeList, 'Select Service', 'service-type');
				populateSelectBox(YearListstatus, 'Select Year', 'statusYear-List');
				//selectAll();
			}else if(reportType == '3'){
				populateSelectBox(blockYearList, 'Select Block Year', 'blockYear-List');
				populateSelectBox(stateList, 'Select State', 'state-List');
				$("#reportDisplyTypeId").on('change', showRecordCount);
			}
			else if(reportType == '4'){                       
				populateSelectBox(blockYearList, 'Select Block Year', 'blockYear-List');
		         populateSelectBox(stateList, 'Select State', 'state-List');
		    	 populateSelectBox(countryList, 'ALL', 'country');
		    	 populateSelectBox(purposeList, 'Select Purpose', 'purpose');			    	
			}
			else if(reportType == '5'){
				populateSelectBoxWithoutAll(blockYearList, 'Select Block Year', 'blockYear-List');
				populateSelectBox(stateList, 'Select State', 'state-List');
				populateSelectBox(countryList, 'Select Country', 'country-List');
			}
			else if(reportType == '6'){
				populateSelectBoxWithoutAll(blockYearList, 'Select Block Year', 'block-year-6');
				populateSelectBox(countryList, 'Select Country', 'country-6');
				populateSelectBox(purposeList, 'Select Purpose', 'purpose-6');
			}
			else if(reportType == '8'){
				populateSelectBox(blockYearList, 'Select Block Year', 'block-year-8');
				populateSelectBox(stateList, 'Select State', 'state-list-8');
				populateSelectBox(countryList, 'Select Country', 'country-list-8');
				populateSelectBox(purposeList, 'Select Purpose', 'purpose-6');
			}			
			else if(reportType == '9'){ 
		         populateSelectBoxWithoutAll(blockYearList, 'Select Block Year', 'blockYear-List');
		         populateSelectBox(stateList, 'Select State', 'state-List');
		    	 populateSelectBox(countryList, 'ALL', 'country');
		    			    	
			}
			else if(reportType == '7'){
				populateSelectBox(blockYearList, 'Select Block Year', 'blockYear-List');
				populateSelectBox(stateList, 'Select State', 'state-List');	
				populateSelectBox(religionList, 'Select Religion', 'religion-List');	
			}
			else if(reportType == '10'){
		
				populateSelectBoxWithoutAll(blockYearList, 'Select Block Year', 'blockYear-List');
				populateSelectBox(countryList, 'Select Country', 'country-List');				
			}
			else if(reportType == '11'){				
				populateSelectBox(serviceTypeList, 'Select Service', 'service-type');
				populateSelectBox(userList, 'Select User', 'user-List');				
			}
			else if(reportType == '12'){				
				populateSelectBox(stateList, 'Select State', 'state-List');			
			}
			else if(reportType == '13'){ 				
				populateSelectBox(redFlagTypeList, 'Select Service', 'service-type');
				populateSelectBox(stateList, 'Select State', 'state-List');	
				$("#reportDisplayType").on('change', showDetailed);
			}
			else if(reportType == '14'){
				//populateSelectBox(blockYearList, 'Select Block Year', 'blockYear-List');
				populateSelectBoxWithoutAll(blockYearList, 'Select Block Year', 'blockYear-List');
				
			}
			else if(reportType == '15'){
				populateSelectBox(serviceTypeList, 'ALL', 'service-type');
				$("#reportPendancyDisplayType").on('change', showPendancyDetailed);
			}
			
			else if(reportType == '26'){
				 populateSelectBox(stateList, 'Select State', 'state-List');
				populateSelectBox(religionList, 'Select Religion', 'religion-List');
				 populateSelectBox(assoNatureList, 'Select Nature', 'nature-List');
				 
				
				
			}
			else if(reportType == '17'){
				showExpiryDateBetween();
			}
			else if(reportType =='18'){
				populateSelectBox(serviceTypeList, 'Select Service', 'service-type');
			}
		
			
			else if(reportType =='29'){
				populateSelectBox(stateList, 'Select State', 'state-List');
			}
		}				
		$("#fromDate").datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
		$("#toDate").datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});	

	});	
}

function initParamsReport8(val){	
	if(val=="4"){
		$('#state-selection').hide();
		$('#country-selection').hide();
	}else{
		$('#state-selection').show();
		$('#country-selection').hide();
	}	
	if(val=="3"){
		$('#state-selection').show();
		$('#country-selection').show();
	}
}

function emptySelectBox(id) {	
	$('#'+id).empty();
	$('#'+id).append($('<option/>', {value:'ALL', text:'All'}));
}

function getDonorR6(){	
	$('#donor-div').show();
	emptySelectBox('donor-6');	
	var params = 'country6='+$('#country-6').val()+'&blockYear6='+$('#block-year-6').val();
	var countryCode=$('#country-6').val();	
	var action = 'get-donor-list-mis-report';
	$('#donor-6').multiselect('destroy');
	$('#donor-6').multiselect({
        includeSelectAllOption: true,
        enableFiltering: true,
        enableCaseInsensitiveFiltering: true,
        buttonWidth: '300px',
        numberDisplayed: 1000,
        serverUrl: 'get-donor-list-mis-report',
        targetSearch: '',
        queryParams: params,
        searchTextMinLength: 3,
        filterPlaceholder: 'search for donor...'
    });
}


function clearParameters() {
	$("#report-parameters").empty();
	clearForm('#report-submission');
}

function clearCompleteForm() {
	clearForm('#report-selection');
	$("#report-parameters").empty();
	clearForm('#report-submission');
}

function hideAll() {
	clearCompleteForm();
	$("#report-parameters-section").hide();
	$("#report-submission").hide();
}

function cancelReportGeneration() {
	hideAll();
}

function generateReport() {
	
	/*var optionsA = $('#service-type > option:selected');
	var optionsB = $('#service-type > option:selected');
	alert(options.length);*/
/*	&& optionsA.length>0 && optionsB.length>0*/

	if($('#report-form').validationEngine('validate') ) {		
		var params = $("#report-form :input").not("input:radio[name=reportFormat]").serialize();
		var reportFormat = $("#report-submission input:radio[name=reportFormat]:checked").attr('val');
		params += '&reportFormat='+reportFormat;
		//alert(reportFormat);
		var reportTypeValue= $('#report-type').val();
		//alert(reportTypeValue);
		if(reportFormat == '3') {
			// HTML
				$('#output-section').show();
				$('#back-section').show();
				$('#report-input-section').hide();								
				if(reportTypeValue=='1'){
					  var reportDisplayType=$('#reportDisplyType').val();
					  showReportPendencyReport(params,reportDisplayType);
				}
				else if(reportTypeValue=='2'){	
					showReportStatusReport(params);
			     }
				else if(reportTypeValue=='3'){
					var reportDisplayType=$('#reportDisplyTypeId').val();
					showReturnFieldReport(params,reportDisplayType);
				}
		    	else if(reportTypeValue=='4'){
		    		showPurposeWise(params);	
		    	 }
		    	 else if(reportTypeValue=='5'){
		    		//showCountryWiseReceiptReport(params);
		    		 showCountryWiseReciptReportSeparater(params);
		    		 
		    	}	
		    	else if(reportTypeValue=='6'){
		    		showCountryPurposeDonorReport(params);
		    	}
		    	else if(reportTypeValue=='9'){
		    		showCountryStateReport(params);
		    	}
		    	else if(reportTypeValue=='8'){		    		
		    		if($("input[name=districtDonor8]:checked").val()=="0"){
		    			showDonorReceiptReport(params);
		    		}else if($("input[name=districtDonor8]:checked").val()=="1"){
		    			showDistrictReceiptReport(params);
		    		}
		    		else if($("input[name=districtDonor8]:checked").val()=="2"){
		    			showStateWiseReceiptReport(params);
		    		}
		    		else if($("input[name=districtDonor8]:checked").val()=="3"){
		    			showCountryWiseReceiptReport(params);
		    		}
		    		else if($("input[name=districtDonor8]:checked").val()=="4"){
		    			showAssociationWiseReceiptReport(params);
		    		}
		    	}
		    	else if(reportTypeValue=='7'){
		    		showReligionWiseReport(params);
		    	}
		    	else if(reportTypeValue=='10'){
		    		showDonorListReport(params);
		    	}
		    	else if(reportTypeValue=='11'){
		    		showUserActivityReport(params);
		    	}
		    	else if(reportTypeValue=='12'){
		    		showSuddenRiseInIncomeReport(params);
		    	}
		    	else if(reportTypeValue=='13'){ 
		    		//var reportDisplayType=$('#reportDisplyTypeId').val();
		    		var reportDisplayType=$('#reportDisplayType').val(); 
		    		var reportStatusDisplayType=$('#reportStatusDisplayType').val();
		    		showRedFlaggCategoryReport(params,reportDisplayType,reportStatusDisplayType);
		    	}
		    	else if(reportTypeValue=='27'){ 
		    		var reportDisplayType=$('#reportDisplayType').val(); 
		    		var reportStatusDisplayType=$('#reportStatusDisplayType').val();
		    		showBlueFlaggCategoryReport(params,reportDisplayType,reportStatusDisplayType);
		    	}
		    	else if(reportTypeValue=='14'){
		    		showAssociationsNotFiledAnnualReturnsReport(params);
		    	}
		    	else if(reportTypeValue=='15'){
		    		//alert(reportFormat);
		    		var reportDisplayPendancyType = $('#reportPendancyDisplayType').val();
		    		
		    		var pRange1=$('#days-of-pendancy1').val();var pRange2=$('#days-of-pendancy2').val();
		    		var pRange3=$('#days-of-pendancy3').val();var pRange4=$('#days-of-pendancy4').val();
		    		//showApplicationPendancyTimeRangewiseReport(params,reportDisplayPendancyType);
		    		var rangeArray= new Array();
		    		//alert('1');
		    		if(pRange1!=''){rangeArray.push(pRange1);}
		    		if(pRange2!=''){rangeArray.push(pRange2);}
		    		if(pRange3!=''){rangeArray.push(pRange3);}
		    		if(pRange4!=''){rangeArray.push(pRange4);}
		    		//params += '&reportFormat='+reportFormat;
		    		//params += '&days-of-pendancy1='+days-of-pendancy1+'&days-of-pendancy2='+days-of-pendancy2+'&days-of-pendancy3='+days-of-pendancy3+'&days-of-pendancy4='+'days-of-pendancy4';
		    		var action ='check-input-timerange-order-mis-report';
		    		//var params = rangeArray;
		    		//alert(params);
		    		$.ajax({
		    			url: action,
		    			method:'GET',
		    			data:params,
		    			dataType:'json',
		    			success: function(data){
		    				//alert(data[1]);
		    				if(data[1]=="success"){
		    					showApplicationPendancyTimeRangewiseReport(params,reportDisplayPendancyType);
		    				}
		    				else{
		    					//alert("else");
		    					notifyList(JSON.parse(data[0]));
		    					$('#go-back-btn').click();
		    				}
		    			},
		    			error: function(textStatus,errorThrown){
		    			}
		    		});
		    		
		    	}

		    	else if(reportTypeValue=='16'){
		    	     var noOfYear= $('#noOfYear').val();
		    		showAnnualReturnStatus(params,noOfYear);
		    	
		    	}

		    	else if(reportTypeValue=='26'){
		    		showAssociationDetailsReport(params);
		    	}
		    	else if(reportTypeValue=='17'){
		    		showRegistrationExpiryReport(params);
		    	}
		
		    	else if(reportTypeValue=='18'){
		    		showDisposalDetails(params);
		    	}
		    	else if(reportTypeValue=='28'){
		    		//alert("SSS");
		    		showCovid19EmergencyStateWiseReport(params);
		    	}
		    	else if(reportTypeValue=='29'){
		    		//alert("SSS");
		    		showCovid19EmergencyResponseReport(params);
		    	}
		}
    	else if(reportTypeValue=='15' && (reportFormat=='1' || reportFormat=='2' || reportFormat=='4')){
    		//alert(reportFormat);
    		var reportDisplayPendancyType = $('#reportPendancyDisplayType').val();
    		
    		var pRange1=$('#days-of-pendancy1').val();var pRange2=$('#days-of-pendancy2').val();
    		var pRange3=$('#days-of-pendancy3').val();var pRange4=$('#days-of-pendancy4').val();
    		//showApplicationPendancyTimeRangewiseReport(params,reportDisplayPendancyType);
    		var rangeArray= new Array();
    		//alert('1');
    		if(pRange1!=''){rangeArray.push(pRange1);}
    		if(pRange2!=''){rangeArray.push(pRange2);}
    		if(pRange3!=''){rangeArray.push(pRange3);}
    		if(pRange4!=''){rangeArray.push(pRange4);}
    		//params += '&reportFormat='+reportFormat;
    		//params += '&days-of-pendancy1='+days-of-pendancy1+'&days-of-pendancy2='+days-of-pendancy2+'&days-of-pendancy3='+days-of-pendancy3+'&days-of-pendancy4='+'days-of-pendancy4';
    		var action ='check-input-timerange-order-mis-report';
    		//alert("66");
    		//var params = rangeArray;
    		//alert(params);
    		$.ajax({
    			url: action,
    			method:'GET',
    			data:params,
    			dataType:'json',
    			success: function(data){
    				if(data[1]=="success"){
    					//showApplicationPendancyTimeRangewiseReport(params,reportDisplayPendancyType);
    					downloadReport(params);

    				}
    				else{
    					//alert(data[0]);
    					notifyList(JSON.parse(data[0]));
    					$('#go-back-btn').click();
    				}
    			},
    			error: function(textStatus,errorThrown){
    			}
    		});
    		
    	}
		
		else if( reportFormat == '5') {			// chart
		
		}else{
			//Pdf, csv
			//alert('downloadReport(params);');
			downloadReport(params);
		}
		
	}
}

function downloadReport(params){
	$("#generate-button").button('loading');
	
	var userActivity= $("#activity-details-div input:radio[name=userActivityFormat]:checked").attr('val');
	
	params += '&userActivity='+userActivity;
	
	var url = 'download-mis-report?'+params;
	$("#report-download-form").attr('action', url);
	$("#report-download-form").submit();
	$("#generate-button").button('reset');
}

function showReportPendencyReport(params, reportDisplayType) {
	$("#report-data").html('');
	if (reportDisplayType == 'd') {
		$("#report-data")
				.bootgrid(
						{
							title : '',
							recordsinpage : '10',
							dataobject : 'propertyList',
							dataurl : 'show-pendency-mis-report?' + params,
							columndetails : [

									
									
									{
										title : 'STATE',
										name : 'state'
									},
									{
										title : 'District',
										name : 'district'
									},
									{
										title : 'YEAR',
										name : 'serviceYear'
									},
									{
										title : 'Submission Date',
										name : 'submissionDate'
									},
									{
										title : 'SERVICE',
										name : 'serviceDesc'
									},
									{
										title : 'APPLICATION ID',
										name : 'applicationId'
									},
									{
										title : 'APPLICANT NAME',
										name : 'applicantName',
									},
									{
										title : 'SECTION FILE NO',
										name : 'sectionFileNo'
									},
									{
										title : 'MHA',
										name : 'mhaData',
									},
									{
										title : 'IB',
										name : 'ibData',
									},
									{
										title : 'RAW',
										name : 'rawData',
									},
									{
										title : 'APPLICANT',
										name : 'appliciant',
									},
									
									{
										title : 'USER',
										name : 'userName',
									},
									{
										title : 'DESIGNATION',
										name : 'designation',
									},
									{
										title : 'Number of Days since application Pending',
										name : 'pending',
									}],
						// onRowSelect:function(rowdata){getNotificationDetails(rowdata.officeCode)}
						});

	} else {
		$("#report-data").bootgrid({
			title : '',
			recordsinpage : '10',
			dataobject : 'propertyList',
			dataurl : 'show-pendency-mis-report?' + params,
			columndetails : [ {
				title : 'Year',
				name : 'serviceYear'
			}, {
				title : 'Service',
				name : 'serviceDesc'
			},
			{
				title : 'Applications Received',
				name : 'total'
			},
			{
				title : 'Applications Pending',
				name : 'pending'
			},
			 {
				title : 'MHA',
				name : 'mhaData'
			},
			{
				title : 'IB',
				name : 'ibData'
			}, {
				title : 'RAW',
				name : 'rawData'
			}, {
				title : 'Applicant',
				name : 'appliciant'
			}, {
				title : 'ASO',
				name : 'aso'
			}, {
				title : 'SO',
				name : 'so'
			} , {
				title : 'US',
				name : 'us'
			} , {
				title : 'DIR',
				name : 'dir'
			}, {
				title : 'Other',
				name : 'other'
			}   ],
		});
	}

}

function showReportStatusReport(params){
	//$("#generate-button").button('loading');

	var reportdisplayType=$('#app-status-reportDisplyType').val();
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');
		
		if(reportdisplayType=="s"){
			$("#report-data").bootgrid(
		    		{
		    		title:'',
		    		recordsinpage:'10',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-StatusReport-mis-report?'+params,
		    		columndetails:
					[
					    {title:'Year', name:'serviceYear'}, 
						{title:'Service', name:'serviceDesc'}, 
						{title:'Fresh', name:'fresh'},
						{title:'Pending', name:'pending'},
						{title:'Approved', name:'approved'}, 
						{title:'Denied', name:'denied'},
						{title:'Closed', name:'closed'},
						{title:'Clarification Requested', name:'clarificationRequested'}
					],
					//onRowSelect:function(rowdata){getNotificationDetails(rowdata.officeCode)}
		    		});
		}
		else {
			$("#report-data").bootgrid(
		    		{
		    		title:'',
		    		recordsinpage:'10',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-StatusReport-mis-report?'+params,
		    		columndetails:
					[
					    {title:'Year', name:'serviceYear'},
						{title:'Service', name:'serviceDesc'}, 
						{title:'Application Id', name:'applicationId'},
						{title:'Section File No.', name:'sectionFileNo'},
						{title:'Submission Date', name:'submissionDate'},
						{title:'Applicant Name', name:'applicantName'},
						{title:'State', name:'state'}, 
						{title:'District', name:'district'},
						{title:'Status', name:'status'}
						
					],
					//onRowSelect:function(rowdata){getNotificationDetails(rowdata.officeCode)}
		    		});
		}
	}
	
	
}

function showReturnFieldReport(params,reportDisplayType){
	//$("#generate-button").button('loading');
	if($('#report-form').validationEngine('validate')) {
		
		if (reportDisplayType == 's'){
			$("#report-data").html('');
			$("#report-data").bootgrid(
		    		{
		    		title:'',
		    		recordsinpage:'5',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-returnField-mis-report?'+params,
		    		columndetails:
					[
                          {title:'Block Year', name:'blockYear'}, 
					     {title:'State Name', name:'stateName'}, 
						{title:'Total Reported', name:'totalReported'}, 
						{title:'Total Nil Reported', name:'totalNilReported'},
						{title:'Foreign Amount(INR)', name:'foreignAmt',
							formatter : function(rowdata) {
								var value = rowdata['foreignAmt'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
					},
						{title:'Total Amount(INR)', name:'totalAmt',
							formatter : function(rowdata) {
								var value = rowdata['totalAmt'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
						}, 
					],
					//onRowSelect:function(rowdata){getNotificationDetails(rowdata.officeCode)}
		    		});	
		}
		else{
			$("#report-data").html('');
			$("#report-data").bootgrid(
		    		{
		    		title:'',
		    		recordsinpage:'5',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-returnField-mis-report?'+params,
		    		columndetails:
					[
					    {title:'Block Year', name:'blockYear'},
					    {title:'State Name', name:'stateName'}, 
						{title:'District', name:'districtName'},
                        {title : 'FCRA Registration No.',
			             name : 'fcraRcn',
			             formatter : function(rowdata) {
				var apfcraRcnid=rowdata.fcraRcn;
               if(apfcraRcnid.includes('R'))
				var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.fcraRcn+'</a>';
				else link= apfcraRcnid;
				return link;
			      }},
						{title:'Association Name', name:'associationName'}, 
						{title:'Website', name:'website',
							formatter : function(rowdata) {
								var web=rowdata.website;
				               var	link = '';
				               if(web != null && web != '')
				            	   link = '<a onclick=getApplicationDocument(\"'+web+'\");>'+rowdata.website+'</a>';
							   return link;
							   }
						},
						{title:'Submission Date', name:'submission_Date'},
						{title:'Foreign Amount(INR)', name:'foreignAmt',
							formatter : function(rowdata) {
								var value = rowdata['foreignAmt'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
						},
						{title:'Total Amount(INR)', name:'totalAmt',
							formatter : function(rowdata) {
								var value = rowdata['totalAmt'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
						}, 
					],
					//onRowSelect:function(rowdata){getNotificationDetails(rowdata.officeCode)}
		    		});	
			
			
		}
	}	
}
function showCountryPurposeDonorReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-country-purpose-donor-mis-report?'+params,
	    		columndetails:
				[
				 	{title:'Block Year', name:'blockYear'},
				 	{title:'Country', name:'country'},
				 	{title:'Donor', name:'donor'},
				 	{title:'Purpose', name:'purpose'},
				    {title:'FCRA Registration No.', name:'assoRcn'}, 
					{title:'Association Name', name:'assoName'}, 
					{title:'State', name:'state'},
					{title:'District', name:'district'},
					{
						title:'Amount( INR )', name:'amount',
						formatter : function(rowdata) {
							var value = rowdata['amount'];							
							value=value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							return value;
						}
					}  
				],				
	    		});		
	}	
}
function showDonorReceiptReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-district-donor-receipt-mis-report?'+params,
	    		columndetails:
				[
				 	{title:'Block Year', name:'blockYear'},
				    {title:'Donor Name', name:'donorName'}, 
					{title:'Country', name:'countryName'}, 					
					{
						title:'Amount( INR )', name:'amount',
						formatter : function(rowdata) {
							var value = rowdata['amount'];							
							value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							return value;
						}
					} 
				],				
	    		});		
	}	
}
function showDistrictReceiptReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-district-donor-receipt-mis-report?'+params,
	    		columndetails:
				[
				 	{title:'Block Year', name:'blockYear'},
				    {title:'District', name:'district'}, 
					{title:'State', name:'state'}, 
					{title:'No. of Associations', name:'assoNumber'},					
					{
						title:'Amount( INR )', name:'amount',
						formatter : function(rowdata) {
							var value = rowdata['amount'];							
							value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							return value;
						}
					} 
				],				
	    		});		
	}	
}
function showStateWiseReceiptReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
		    		title:'',
		    		recordsinpage:'10',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-district-donor-receipt-mis-report?'+params,
		    		columndetails:
					[				     
					 	{title:'Block Year', name:'blockYear'},
						{title:'State', name:'state'}, 
						{title:'No. of Associations', name:'assoNumber'},					
						{
							title:'Amount( INR )', name:'amount',
							formatter : function(rowdata) {
								var value = rowdata['amount'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
						} 
					],				
	    		});		
	}	
}
function showAssociationWiseReceiptReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
		    		title:'',
		    		recordsinpage:'10',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-district-donor-receipt-mis-report?'+params,
		    		columndetails:
					[				     
						{title:'Block Year', name:'blockYear'},
						{title:'State Name', name:'state'}, 
						{title:'District', name:'district'},
						{title : 'FCRA Registration No.', name : 'assoNumber',
						 formatter : function(rowdata) {
							var apfcraRcnid=rowdata.assoNumber;
							if(apfcraRcnid.includes('R'))
								var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.assoNumber+'</a>';
							else link= apfcraRcnid;
							return link;
						 }
						},
						{
							title:'Association Name', name:'assoName'
						}, 						
						{
							title:'Foreign Amount(INR)', name:'amount',
							formatter : function(rowdata) {
								var value = rowdata['amount'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
						}
					],				
	    		});		
	}	
}

function goBack(){
	$('#back-section').hide();
	$('#output-section').hide();
	$('#report-input-section').show();
}

function showCountryWiseReciptReportSeparater(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		//dataurl:'show-district-donor-receipt-mis-report?'+params,
	    		dataurl:'show-CountryWiseReceipt-mis-report?'+params,
	    		columndetails:
				[
				    {title:'S.NO.', name:'sino'},
				    {title:'Block Year', name:'blockYear'},
				    {title:'Country ', name:'country'}, 
					 {title:'FCRA Registration No.', name:'fcraRegNo',
						 formatter : function(rowdata) {
					var apfcraRcnid=rowdata.fcraRegNo;
					//alert(1);
					//alert(apfcraRcnid);
					if(apfcraRcnid.includes('R'))
					var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.fcraRegNo+'</a>';
					else link=apfcraRcnid;
					return link;
					
				
					
				             }  },
				   {title:'FCRA Registration No.', name:'fcraRegNo'},
					{title:'Association Name', name:'associationName'},
					{
						title:'Amount (INR)', name:'amount',
						formatter : function(rowdata) {
							var value = rowdata['amount'];							
							value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							return value;
						}
					}, 
				],
			
	    		});		
	}
}

function showCountryWiseReceiptReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-district-donor-receipt-mis-report?'+params,
	    		//dataurl:'show-CountryWiseReceipt-mis-report?'+params,
	    		columndetails:
				[
				  //  {title:'S.NO.', name:'sino'},
				    {title:'Block Year', name:'blockYear'},
				    {title:'Country ', name:'countryName'}, 
				/*	 {title:'FCRA Registration No.', name:'fcraRegNo',
						 formatter : function(rowdata) {
					var apfcraRcnid=rowdata.fcraRegNo;
					alert(1);
					alert(apfcraRcnid);
					if(apfcraRcnid.includes('R'))
					var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.fcraRegNo+'</a>';
					else link=apfcraRcnid;
					return link;
					
				
					
				             }  },*/
				  /*  {title:'FCRA Registration No.', name:'fcraRegNo'},*/
					//{title:'Association Name', name:'associationName'},
					{
						title:'Amount (INR)', name:'amount',
						formatter : function(rowdata) {
							var value = rowdata['amount'];							
							value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							return value;
						}
					}, 
				],
			
	    		});		
	}
}


function showReligionWiseReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'5',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-ReligionWise-mis-report?'+params,
	    		columndetails:
				[
				   {title:'SI. NO.', name:'sino'}, 
				   {title:'Block Year',name:'blkYear'},
					{title:'Religion ', name:'rName'}, 
					 {title:'FCRA Registration No.',name: 'rCN',
					  formatter : function(rowdata) {
							var apfcraRcnid=rowdata.rCN;
							
							var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rCN+'</a>';
							
							return link;
					  }  },
					{title:'Associations Name ', name:'association'}, 
					  {title:'State ',name: 'stateName'},
					  {title:'District ',name: 'distName'},
					{
						title:'Amount (INR)', name:'amount',
						formatter : function(rowdata) {
							var value = rowdata['amount'];							
							value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							return value;
						}
					}, 
				],
			
	    		});		
	}
}

function showDonorListReport(params){	
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');		
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'5',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-showDonorListReport-mis-report?'+params,
	    		columndetails:
				[
				   {title:'SI. NO.', name:'sino'}, 
					{title:'Donor Name ', name:'donorName'}, 
					{title:'Donor Address ', name:'address'}, 
					
				],
			
	    		});		
	}
}
//showUserActivityReport
function showUserActivityReport(params){
	var reportDisplayType=$('#UserReportDisplyType').val();
	var userActivity = $("#activity-details-div input:radio[name=userActivityFormat]:checked").attr('val');
	params +='&userActivity='+userActivity;
if(userActivity=='P'){
	if (reportDisplayType == 's'){
			if($('#report-form').validationEngine('validate')) {
				$("#report-data").html('');		
				$("#report-data").bootgrid(
			    		{
			    		title:'',
			    		recordsinpage:'5',
			    		dataobject:'propertyList',    		
			    		dataurl:'show-User_Activity-Report-mis-report?'+params,
			    		columndetails:
						[
						   /*{title:'SI. NO.', name:'sino'}, */
							{title:'User Name ', name:'userName'}, 
							{title:'State ', name:'state'},
							{title:'Service Name ', name:'serviceName'},
							{title:'Pending For Processing ', name:'pendProcessing'}, 
							{title:'Pending For Mailing ', name:'pendMail'},
						],
			    		});		
			}
		}else{
			if($('#report-form').validationEngine('validate')) {
				$("#report-data").html('');		
				$("#report-data").bootgrid(
			    		{
			    		title:'',
			    		recordsinpage:'5',
			    		dataobject:'propertyList',    		
			    		dataurl:'show-User_Activity-Report-mis-report?'+params,
			    		columndetails:
						[
						   /*{title:'SI. NO.', name:'sino'}, */
							{title:'User Name ', name:'userName'}, 
							{title:'Application Id ', name:'applicationId',
								  formatter : function(rowdata) {
										var apfcraRcnid=rowdata.applicationId;
										var	link = '<a onclick=getApplicationDetails(\"'+apfcraRcnid+'\");>'+rowdata.applicationId+'</a>';
										return link;
								  }  },
						    {title:'Applicant Name ', name:'applicantName'},
							{title:'State ', name:'state'},	  
							{title:'Service Name ', name:'serviceName'},
							{title:'Status ', name:'statusName'},
							{title:'Pending For Days ', name:'pendDays'}, 
							
						],
			    		});		
			}
		}
}
else if(userActivity=='A') {
			if (reportDisplayType == 's'){
				if($('#report-form').validationEngine('validate')) {
					$("#report-data").html('');		
					$("#report-data").bootgrid(
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-User_Activity-Report-mis-report?'+params,
				    		columndetails:
							[
							  /* {title:'S.No.', name:'sino'}, */
								{title:'User Name ', name:'userName'}, 
								{title:'State ', name:'state'}, 
								{title:'Service Name ', name:'serviceName'},
								{title:'Received Count ', name:'receivedCount'},
								{title:'Processed Count ', name:'processedCount'},
							],
				    		});		
				}
			}
			else{
				if($('#report-form').validationEngine('validate')) {
					$("#report-data").html('');		
					$("#report-data").bootgrid(
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-User_Activity-Report-mis-report?'+params,
				    		columndetails:
							[
							  /* {title:'SI. NO.', name:'sino'}, */
								{title:'User Name ', name:'userName'}, 
								{title:'Application Id ', name:'applicationId',
								  formatter : function(rowdata) {
										var apfcraRcnid=rowdata.applicationId;
										
										var	link = '<a onclick=getApplicationDetails(\"'+apfcraRcnid+'\");>'+rowdata.applicationId+'</a>';
										
										return link;
								  }  },
								{title:'Applicant Name ', name:'applicantName'},
								{title:'State ', name:'state'},
								{title:'Service Name ', name:'serviceName'},
								{
									title : 'Received ',
									name : 'receivedCount',
									formatter : function(rowdata) {
										var link = '';
										if (rowdata['receivedCount'] == '1')
											link = '<span class="glyphicon glyphicon-remove text-danger"></span>';
										else
											link = '-';
										return link;
									}
								},
								{title:'Processed ', name:'processedCount',
									formatter : function(rowdata) {
										var link = '';
										if (rowdata['processedCount'] == '1')
											link = '<span class="glyphicon glyphicon-remove text-danger"></span>';
										else
											link = '-';
										return link;
									}},
							],
							});		
							}
						} 
			}
else if(userActivity=='D') {
	if (reportDisplayType == 's'){
	  if(myofficeId=='"IB"' || myofficeId=='"RAW"' ){
			if($('#report-form').validationEngine('validate')) {
				$("#report-data").html('');		
				$("#report-data").bootgrid(
			    		{
			    		title:'',
			    		recordsinpage:'5',
			    		dataobject:'propertyList',    		
			    		dataurl:'show-User_Activity-Report-mis-report?'+params,
			    		columndetails:
						[
						   /*{title:'SI. NO.', name:'sino'}, */
							{title:'User Name ', name:'userName'}, 
							{title:'Service Name ', name:'serviceName'},
							{title:'Disposed ', name:'disposed'},
							
						],
			    		});		
			}
		 }
	  else{
			 if($('#report-form').validationEngine('validate')) {
					$("#report-data").html('');		
					$("#report-data").bootgrid(
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-User_Activity-Report-mis-report?'+params,
				    		columndetails:
							[
							  /* {title:'SI. NO.', name:'sino'}, */
								{title:'User Name ', name:'userName'}, 
								{title:'State', name:'state'},
								{title:'Service Name ', name:'serviceName'},
								{title:'Granted ', name:'granted'},
								{title:'Denied ', name:'denied'},
								{title:'Close ', name:'closed'},
							],
				    		});		
				}
		 }
		
	}
	else{
		 if(myofficeId=='"IB"'|| myofficeId=='"RAW"'){
			if($('#report-form').validationEngine('validate')) {
				$("#report-data").html('');		
				$("#report-data").bootgrid(
			    		{
			    		title:'',
			    		recordsinpage:'5',
			    		dataobject:'propertyList',    		
			    		dataurl:'show-User_Activity-Report-mis-report?'+params,
			    		columndetails:
						[
						   /*{title:'SI. NO.', name:'sino'}, */
						   {title:'User Name ', name:'userName'},
   						   {title:'Application Id ', name:'applicationId',
								  formatter : function(rowdata) {
										var apfcraRcnid=rowdata.applicationId;
										
										var	link = '<a onclick=getApplicationDetails(\"'+apfcraRcnid+'\");>'+rowdata.applicationId+'</a>';
										
										return link;
								  }  },
								{title:'Applicant Name', name:'applicantName'},
								{title:'State ', name:'state'},
								{title:'Disposed On', name:'activityOn'},
								{title:'Service Name ', name:'serviceName'},
								{title:'Disposed ', name:'disposed',
									formatter : function(rowdata) {
										var link = '';
										if (rowdata['disposed'] == '1')
											link = '<span class="glyphicon glyphicon-remove text-danger"></span>';
										else
											link = '-';
										return link;
									}},
							
						],
			    		});		
					}
				}
				else{
					if($('#report-form').validationEngine('validate')) {
						$("#report-data").html('');		
						$("#report-data").bootgrid(
					    		{
					    		title:'',
					    		recordsinpage:'5',
					    		dataobject:'propertyList',    		
					    		dataurl:'show-User_Activity-Report-mis-report?'+params,
					    		columndetails:
								[
								   /*{title:'SI. NO.', name:'sino'}, */
								   {title:'User Name ', name:'userName'},
								   {title:'Application Id ', name:'applicationId',
										  formatter : function(rowdata) {
												var apfcraRcnid=rowdata.applicationId;
												
												var	link = '<a onclick=getApplicationDetails(\"'+apfcraRcnid+'\");>'+rowdata.applicationId+'</a>';
												
												return link;
										  }  },
										{title:'Applicant Name', name:'applicantName'},
										{title:'State ', name:'state'},
										{title:'Disposed On', name:'activityOn'},
										{title:'Service Name ', name:'serviceName'},
										{title:'Granted ', name:'granted',
											formatter : function(rowdata) {
												var link = '';
												if (rowdata['granted'] == '1')
													link = '<span class="glyphicon glyphicon-remove text-danger"></span>';
												else
													link = '-';
												return link;
											}},
										{title:'Denied ', name:'denied',
											formatter : function(rowdata) {
												var link = '';
												if (rowdata['denied'] == '1')
													link = '<span class="glyphicon glyphicon-remove text-danger"></span>';
												else
													link = '-';
												return link;
											}},
										{title:'Close ', name:'closed',
											formatter : function(rowdata) {
												var link = '';
												if (rowdata['closed'] == '1')
													link = '<span class="glyphicon glyphicon-remove text-danger"></span>';
												else
													link = '-';
												return link;
											}},
								],
					    		});		
					}
				}			
			}
	}
	
}




function showPurposeWise(params){
//	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-purpose-mis-report?'+params,
	    		columndetails:
				[
				 {title:'Serial No.', name:'sino'},  
				 {title:'Block Year',name:'blkYear'},
				 {title:'Purpose Name', name:'purposeName'},   
				 {
					 title:'Amount (INR)', name:'amount',
						formatter : function(rowdata) {
							var value = rowdata['amount'];							
							value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
							return value;
						}
				},   
				 {title:'No of Association', name:'noOfAssociation'},   
				],
			
	    		});		
	}

function showCountryStateReport(params){
	  //	if($('#report-form').validationEngine('validate')) {
			$("#report-data").html('');
			$("#report-data").bootgrid(
		    		{
		    		title:'',
		    		recordsinpage:'5',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-country-state-mis-report?'+params,
		    		columndetails:
					[

					 {title:'Serial No.', name:'sino'},    
					 {title:'FCRA Registration No.', name:'rcn',
					 formatter : function(rowdata) {
				var apfcraRcnid=rowdata.rcn;
				
				var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
				
				return link;
				
			
				
			             }  },
					 {title:'Amount (INR)', name:'amount',
							formatter : function(rowdata) {
								var value = rowdata['amount'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
					 },
					 {title:'Association Name and Address', name:'associationName'}, 
					 {title:'Bank Detail',name:'bankDetail'},
					    
					  
					],
				
		    		});		
		}

function showTestRedFlag(params){


//	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');
		$("#report-data").bootgrid(
	    		{
	    		title:'',
	    		recordsinpage:'10',
	    		dataobject:'propertyList',    		
	    		dataurl:'show-test-mis-report?'+params,
	    		columndetails:
				[
				 {title:'Serial No.', name:'rcn'}  
				
				],
			
	    		});		
		
	
}

function showSuddenRiseInIncomeReport(params){

	  //	if($('#report-form').validationEngine('validate')) {
			$("#report-data").html('');
			$("#report-data").bootgrid(
		    		{
		    		title:'',
		    		recordsinpage:'5',
		    		dataobject:'propertyList',    		
		    		dataurl:'show-sudden-rise-incom-mis-report?'+params,
		    		columndetails:
					[
					 {title:'FCRA Registration No.', name:'rcn'
						 ,
					 formatter : function(rowdata) {
				var apfcraRcnid=rowdata.rcn;
				
				var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
				
				return link;
				
			
				
			             }
					 },
					 {title:'Association Name', name:'assoName'}, 
					 {title:'State', name:'state'},
					 {title:'District', name:'district'},
					 {title:'Foreign Amount (INR)', name:'foreignAmt'
						 ,
							formatter : function(rowdata) {
								var value = rowdata['foreignAmt'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
					 },
					 {title:'Average(INR)',name:'averegeAmt'
						 ,
							formatter : function(rowdata) {
								var value = rowdata['averegeAmt'];							
								value=value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
								return value;
							}
					 },
					  
					],
				
		    		});		
			
}

function showApplicationPendancyTimeRangewiseReport(params,reportDisplayPendancyType){
	$("#report-data").html('');
	var pRange1=$('#days-of-pendancy1').val();var pRange2=$('#days-of-pendancy2').val();
	var pRange3=$('#days-of-pendancy3').val();var pRange4=$('#days-of-pendancy4').val();
	var rangeArray= new Array();
	if(pRange1!=''){rangeArray.push(pRange1);}
	if(pRange2!=''){rangeArray.push(pRange2);}
	if(pRange3!=''){rangeArray.push(pRange3);}
	if(pRange4!=''){rangeArray.push(pRange4);}
	var len = rangeArray.length;
	
	//	alert(rangeArray);
	//alert(len);
	//alert(pRange1);alert(pRange2);alert(pRange3);alert(pRange4);
	if(reportDisplayPendancyType=='s'){
		if(len==0){
			var heading0 = "No. of Days Pending";
			$("#report-data").bootgrid({
				title:'',
				recordsinpage:'10',
				dataobject:'propertyList',
				dataurl:'show-application-pendancy-time-rangewise-mis-report?'+params,
				columndetails:
					[
					 {title:'Service Name', name:'serviceDescription'},
					 {title:heading0, name:'pRange1'},
					 //{title:'All Other', name:'pRange2'},
					 //{title:'Pendancy Range 2', name:'pRange2'},
/*					 {title:'Pendancy Range 3', name:'pRange3'},
					 {title:'Pendancy Range 4', name:'pRange4'},*/
					 ],
			});	
		}
		if(len==1){
			var heading0 = "0 to "+rangeArray[0]+" days";
			var heading1 = "> "+rangeArray[0]+" days";
			$("#report-data").bootgrid({
				title:'',
				recordsinpage:'10',
				dataobject:'propertyList',
				dataurl:'show-application-pendancy-time-rangewise-mis-report?'+params,
				columndetails:
					[
					 {title:'Service Name', name:'serviceDescription'},
					 {title:heading0, name:'pRange1'},
					 {title:heading1, name:'pRange2'},
					 //{title:'Pendancy Range 2', name:'pRange2'},
/*					 {title:'Pendancy Range 3', name:'pRange3'},
					 {title:'Pendancy Range 4', name:'pRange4'},*/
					 ],
			});	
			
		}
		if(len==2){
			var heading0 = "0 to "+rangeArray[0]+" days";
			var num = parseInt(rangeArray[0])+1;
			var heading1 = ""+num+" to "+rangeArray[1]+" days";
			var heading2 = "> "+rangeArray[1]+" days";
			$("#report-data").bootgrid({
				title:'',
				recordsinpage:'10',
				dataobject:'propertyList',
				dataurl:'show-application-pendancy-time-rangewise-mis-report?'+params,
				columndetails:
					[
					 {title:'Service Name', name:'serviceDescription'},
					 {title:heading0, name:'pRange1'},
					 {title:heading1, name:'pRange2'},
					 {title:heading2, name:'pRange3'},
					 //{title:'Pendancy Range 4', name:'pRange4'},
					 ],
			});		
		}		
		if(len==3){
			var heading0 = "0 to "+rangeArray[0]+" days";
			var num = parseInt(rangeArray[0])+1;
			var heading1 = ""+num+" to "+rangeArray[1]+" days";
			var num1=parseInt(rangeArray[1])+1;
			var heading2=""+num1+" to "+rangeArray[2]+" days";
			//var heading2 = ""+rangeArray[1]+" to "+rangeArray[2]+"  ";
			var heading3 = "> "+rangeArray[2]+" days";
			$("#report-data").bootgrid({
				title:'',
				recordsinpage:'10',
				dataobject:'propertyList',
				dataurl:'show-application-pendancy-time-rangewise-mis-report?'+params,
				columndetails:
					[
					 {title:'Service Name', name:'serviceDescription'},
					 {title:heading0, name:'pRange1'},
					 {title:heading1, name:'pRange2'},
					 {title:heading2, name:'pRange3'},
					 {title:heading3, name:'pRange4'},
					 ],
			});		
		}		
		if(len==4){
			var heading0 = "0 to "+rangeArray[0]+" days";
			var num = parseInt(rangeArray[0])+1;
			var heading1 = ""+num+" to "+rangeArray[1]+" days";
			var num1=parseInt(rangeArray[1])+1;
			var heading2=""+num1+" to "+rangeArray[2]+" days";
			//var heading2 = ""+rangeArray[1]+" to "+rangeArray[2]+"";
			var num2=parseInt(rangeArray[2])+1;
			var heading3=""+num2+" to "+rangeArray[3]+" days";
			var heading4 = "> "+rangeArray[3]+" days";			
			$("#report-data").bootgrid({
				title:'',
				recordsinpage:'10',
				dataobject:'propertyList',
				dataurl:'show-application-pendancy-time-rangewise-mis-report?'+params,
				columndetails:
					[
					 {title:'Service Name', name:'serviceDescription'},
					 {title:heading0, name:'pRange1'},
					 {title:heading1, name:'pRange2'},
					 {title:heading2, name:'pRange3'},
					 {title:heading3, name:'pRange4'},
					 {title:heading4, name:'pRange5'},
					 ],
			});		
		}
		$("#service-selection input").val('');
	}
	else if(reportDisplayPendancyType=='d'){
		$("#report-data").bootgrid({
			title:'',
			recordsinpage:'10',
			dataobject:'propertyList',
			dataurl:'show-application-pendancy-time-rangewise-mis-report?'+params,
			columndetails:
				[
				 {title:'Year', name:'year'},
				 {title:'Service Name', name:'serviceName'},
				 {title:'Applicant Name', name:'applicantName'},
				 {title:'Section File No.', name:'sectionFileNo'},
				 {title:'Application Id', name:'applicationId'},
				 {title:'District', name:'districtName'},
				 {title:'State', name:'stateName'},
				 {title:'Submission Date', name:'submissionDate'},
				 {title:'Pending for Days', name:'days'},
				 ],
		});				
	}
	$("#service-selection input").val('');
}

function showAssociationsNotFiledAnnualReturnsReport(params){
	$("#report-data").html('');
	//alert(1);
	$("#report-data").bootgrid(
		{
		title:'',
		recordsinpage:'5',
		dataobject:'propertyList',
		dataurl:'show-asso-not-filed-returns-mis-report?'+params,
		columndetails:
						[
		               //{title:'Registration No.', name:'regNo'},
		               {title:'RCN', name:'rcn',
		            	   formatter : function(rowdata){
		            		   var apfcraRcnid=rowdata.rcn;
								
								var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
								
								return link;		            		   
		            	   }
		               },
		               {title:'Association Name', name:'assoName'},
		               {title:'Association Address', name:'assoAddress'},
		               {title:'State Name', name:'stateName'},
		               {title:'District Name', name:'districtName'},
		               ],
	});	
	//alert(2);
}

function showRedFlaggCategoryReport(params,reportDisplayType,reportStatusDisplayType){

		//if($('#report-form').validationEngine('validate')) {
		var userActivity = $("#activity-details-div input:radio[name=userActivityFormat]:checked").attr('val');
		params +='&userActivity='+userActivity;
			$("#report-data").html('');
			//alert(reportDisplayType);
			if(userActivity=='R'){
				if (reportDisplayType == 's'){
					$("#report-data").bootgrid( 
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-red_flagged-cat-mis-report?'+params,
				    		columndetails:
							[
							 {title:'Red/Yellow Flagged Category', name:'cat_desc'}, 
							 {title:'No. of Associations', name:'count'}, 
							],
						
				    		});
				}
				else{

					$("#report-data").bootgrid( 
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-red_flagged-cat-mis-report?'+params,
				    		columndetails:
							[
							 {title:'RCN', name:'rcn'
								 ,
								 formatter : function(rowdata) {
							var apfcraRcnid=rowdata.rcn;
							
							var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
							
							return link;
						    }	 
										 
							 },
							 {title:'Association Name', name:'asso_name'},
							 {title:'Red/Yellow Flagged Category', name:'cat_desc'},
							 {title:'Red/Yellow Flagged Date', name:'status_date'}, 
							 {title:'Remarks', name:'remarks'},
							 {title:'Red Flagged By', name:'action_by'},
							],
						
				    		});
								
				}				
			}
			else if(userActivity=='A'){
				if (reportDisplayType == 's'){
					$("#report-data").bootgrid( 
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-red_flagged-cat-mis-report?'+params,
				    		columndetails:
							[
							 {title:'Red/Yellow Flagged Category', name:'cat_desc'}, 
							 {title:'No. of Associations', name:'count'}, 
							],
						
				    		});
				}
				else{

					$("#report-data").bootgrid( 
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-red_flagged-cat-mis-report?'+params,
				    		columndetails:
							[
/*							 {title:'RCN', name:'rcn'
								 ,
								 formatter : function(rowdata) {
							var apfcraRcnid=rowdata.rcn;
							
							var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
							
							return link;
						    }	 
										 
							 }, */
							 {title:'Association Name', name:'asso_name'},
							 {title:'Red/Yellow Flagged Category', name:'cat_desc'},
							 {title:'State', name:'state'},
							 {title:'Remarks', name:'remarks'},
							 {title:'Red/Yellow Flagged Date', name:'status_date'}, 
							 {title:'Red/Yellow Flagged By', name:'action_by'},
							],
						
				    		});
								
				}				
			}
			else{
				if (reportDisplayType == 's'){
					$("#report-data").bootgrid( 
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-red_flagged-cat-mis-report?'+params,
				    		columndetails:
							[
							 {title:'Red/Yellow Flagged Category', name:'cat_desc'}, 
							 {title:'No. of Donors', name:'count'}, 
							],
						
				    		});
				}
				else{

					$("#report-data").bootgrid( 
				    		{
				    		title:'',
				    		recordsinpage:'5',
				    		dataobject:'propertyList',    		
				    		dataurl:'show-red_flagged-cat-mis-report?'+params,
				    		columndetails:
							[
							 {title:'Donor Name', name:'donor_name'},
							 {title:'Red/Yellow Flagged Category', name:'cat_desc'},
							 {title:'Country', name:'country'},
							 {title:'Remarks', name:'remarks'},
							 {title:'Red/Yellow Flagged Date', name:'status_date'}, 
							 {title:'Red/Yellow Flagged By', name:'action_by'}
							],
						
				    		});
								
				}				
			}
}
function showBlueFlaggCategoryReport(params,reportDisplayType,reportStatusDisplayType){
		$("#report-data").html('');
		//alert(reportDisplayType);
		if (reportDisplayType == 's'){
				$("#report-data").bootgrid( 
			    		{
			    		title:'',
			    		recordsinpage:'5',
			    		dataobject:'propertyList',    		
			    		dataurl:'show-blue_flagged-cat-mis-report?'+params,
			    		columndetails:
						[
						 {title:'State', name:'state'}, 
						 {title:'No. of Associations', name:'count'}, 
						],
					
			    		});
			}
			else{

				$("#report-data").bootgrid( 
			    		{
			    		title:'',
			    		recordsinpage:'5',
			    		dataobject:'propertyList',    		
			    		dataurl:'show-blue_flagged-cat-mis-report?'+params,
			    		columndetails:
						[
						 {title:'RCN', name:'rcn'
							 ,
							 formatter : function(rowdata) {
						var apfcraRcnid=rowdata.rcn;
						
						var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
						
						return link;
					    }	 
									 
						 },
						 
						 {title:'Association Name', name:'asso_name'},
						 {title:'Blue Flagged Date', name:'status_date'}, 
						 {title:'Remarks', name:'remarks'},
						 {title:'Blue Flagged By', name:'action_by'},
						],
					
			    		});
							
			}				
		
		
		
}
function showAnnualReturnStatus(params,noOfYear){
		var years;	
    	years = [{title:'RCN', name:'rcn',
    		formatter : function(rowdata) {
				var apfcraRcnid=rowdata.rcn;
				
				var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
				
				return link;
		  }},
   				 {title:'Association Name', name:'assoName'},
   				 {title:'State Name', name:'stateName'}, 
   				 {title:'District Name', name:'districtName'},
   				 {title:'Registered On', name:'regOn'},
   				 {title:'Last Renewed On',name:'lastRenewed'},
   				 {title:'Expiry On',name:'expiryOn'}];
  	
		   for(var i = 0; i<yearList.length; i++){	 
			 years.push({title:yearList[i],name:'year'+(i+1)});
		   }  
	
		$("#report-data").html('');
		$("#report-data").bootgrid(
		{
			title:'',
			recordsinpage:'10',
			dataobject:'propertyList',
			dataurl:'show-annual-status-mis-report?'+params,
			columndetails:years,							
		});
}


/*function selectAll() {
    $('#service-type').multiselect({
        includeSelectAllOption: true
    });
    $('#statusYear-List').multiselect({
        includeSelectAllOption: true
    });
}*/
function showRecordCount(){
   var displaytype= $('#reportDisplyTypeId').val();
	if(displaytype=='d'){
		  $('#from-amount-div').show();	
		  $('#to-amount-div').show();
		  $('#requireRowCount-div').show();	
	}
   else{
	   $('#requireRowCount').val('');
	   $('#requireRowCount-div').hide();
	   $('#from-amount').val('');
	   $('#from-amount-div').hide();
	   $('#to-amount').val('');
	   $('#to-amount-div').hide();
     }
	 
}

function showPendancyDetailed(){
	var pendancyDisplaytype = $('#reportPendancyDisplayType').val();
	//alert(pendancyDisplaytype);
	if(pendancyDisplaytype=='d'){
		$('#service-selection').hide();
	}
	else{
		$('#service-selection').show();
	}
}

function showDetailed(){
	   var displaytype= $('#reportDisplayType').val();
	   //alert(displaytype);
		if(displaytype=='d'){
			  $('#service-selection').show();
		}
	   else{
		   $('#service-selection').hide();
	     }
}

function  getRegistrationDetails(fcraRcn){
	 var url = 'popup-registration-tracking?appId='+fcraRcn;
	 openLink(url);
}

function  getApplicationDetails(applicationId){
	 var url = 'popup-application-tracking-workspace?applicationId='+applicationId;
	 openLink(url);
}
function hideDateField(){
	 $('#form-to-date').hide();
	 $('#details-div').show();
	// $('#to-date-div').hide();
}

function showDateField(){
	 $('#form-to-date').show();
	 	
}
function UserActivityField(){
	 $('#form-to-date').show();
	 $('#details-div').show();
	 	
}
function DisposedUserActivity(){
	 $('#form-to-date').show();
	 $('#details-div').show();
	 	
}
function getApplicationDocument(docPath){	
	window.open(docPath);
}
/*
calender initializer
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
}*/
function showAssociationDetailsReport(params){
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');

		$("#report-data").bootgrid(
				{
					title:'',
					recordsinpage:'10',
					dataobject:'propertyList',    		
					dataurl:'show-AssociationDetails-mis-report?'+params,
					columndetails:
						[
						  {title:'RCN', name:'rcn' ,
							 formatter : function(rowdata) {
							var apfcraRcnid=rowdata.rcn;
							var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+rowdata.rcn+'</a>';
							return link;
						    }	 
										 
							 },
						 {title:'Association Name', name:'asso_name'}, 
						 {title:'Account No.', name:'account_no'},
						 {title:'Bank Name', name:'bankname'},
						 {title:'Bank Address', name:'bankAddress'}, 
						 {title:'Cheif Functionary Name', name:'cheif_functionary_name'},
						 {title:'Association Cheif Mobile', name:'asso_cheif_mobile'},
						 {title:'Association Official Email', name:'asso_official_email'},
						 {title:'PAN No.', name:'pan_no'}, 
						 {title:'Bank IFSC Code', name:'source_for_amt'}, 
						/* {title:'Total Amount', name:'total_amt'},*/
						 {title:'Association Address', name:'assoAddress'},
						 {title:'State', name:'state'}
						 
						 ],
				});
	}
}


function generateYear(){
	if(noOfYear!=""){
		var params = 'noOfYear='+$('#noOfYear').val();
		var action = 'get-year-Block-mis-report';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				yearList = data;
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	
		
	}
	
}

function showRegistrationExpiryReport(params){
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');
		$("#report-data").bootgrid(
				{
					title:'',
					recordsinpage:'10',
					dataobject:'propertyList',    		
					dataurl:'show-RegistrationEntryDetails-mis-report?'+params,
					columndetails:
						[
						 {title:'RCN', name:'rcn'}, 
						 {title:'Association Name', name:'asso_name'}, 
						 {title:'Expiring On', name:'expiryOn'},
						 {title:'Account No.', name:'account_no'},
						 {title:'Bank Name', name:'bankname'},
						 {title:'Bank Address', name:'bankAddress'}, 
						 {title:'Association Address', name:'assoAddress'}
						 ],
				});
	}

	
}


function showDisposalDetails(params){

	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');
		var reportTypewise= $('input[name="reportTypewise"]:checked').val();	       
		if (reportTypewise == 'M') {
			$("#report-data").bootgrid(
					{
						title:'',
						recordsinpage:'10',
						dataobject:'propertyList',    		
						dataurl:'show-DisposeDetails-mis-report?'+params,
						columndetails:
							[
							 {title:'Year', name:'year'}, 
							 {title:'Month', name:'month'},
							 {title:'Service', name:'serviceDesc'}, 
							 {title:'Granted', name:'granted'},
							 {title:'Denied', name:'denied'},
							 {title:'Closed', name:'closed'}						
							 ],
					});
		}
		else if (reportTypewise == 'Y') {
				$("#report-data").bootgrid(
						{
							title:'',
							recordsinpage:'10',
							dataobject:'propertyList',    		
							dataurl:'show-DisposeDetails-mis-report?'+params,
							columndetails:
								[
									
                                    {title:'Year', name:'year'}, 
									{title:'Service', name:'serviceDesc'}, 
									{title:'Granted', name:'granted'},
									{title:'Denied', name:'denied'},
									{title:'Closed', name:'closed'}
								],
						});
			}
		

	}
}





function showExpiryDateBetween()
{
	var action = 'expiry-date-mis-report';		
	$.ajax({
		url: action,
		method:'POST',
		dataType:'json',
		success: function(data){
			var tt = JSON.parse(data[0]);
			var ExpiryfromDate = tt[0].k;
			var TofromDate = tt[0].v;
			$('#fromDate').val(ExpiryfromDate);
			$('#toDate').val(TofromDate);
			

		},
		error: function(textStatus,errorThrown){
			alert('error');				
		}
	});
	
	
	}
	
function showCovid19EmergencyStateWiseReport(params){
       //  alert(params);
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');
		var reportTypewise= $('input[name="reportTypewise"]:checked').val();	       
		//alert("reportTypewise"+reportTypewise);
			$("#report-data").bootgrid(
					{
						title:'',
						recordsinpage:'10',
						dataobject:'propertyList',    		
						dataurl:'show-Covid19Emergency-mis-report?'+params,
						columndetails:
							[
							 {title:'S.No.', name:'sino'}, 
							 {title:'State', name:'blkYear'},
							 {title:'No. of Association Requested', name:'rName'}, 
							 {title:'No. of Association Responded', name:'association'},
							 {title:'Total Cumulative Amount for COVID-19 Support provided', name:'stateName'},
							 {title:'Total No. of Weeks for COVID-19 Support provided', name:'amount'}						
							 ],
					});
		
				
		

	}
}

function showCovid19EmergencyResponseReport(params){
    //  alert(params);
	if($('#report-form').validationEngine('validate')) {
		$("#report-data").html('');
		var reportTypewise= $('input[name="reportTypewise"]:checked').val();	       
		
		if(reportTypewise=='Requested'){
			//alert("reportTypewise----- "+reportTypewise);
			$("#report-data").bootgrid(
					{
						title:'',
						recordsinpage:'10',
						dataobject:'propertyList',    		
						dataurl:'show-Covid19Emergency-not-requested-mis-report?'+params,
						columndetails:
							[
							 {title:'S.No.', name:'sino'}, 
							 {title:'RCN', name:'rCN'},
							 {title:'Association Name', name:'association'}, 
							 {title:'Association EmailId', name:'assoMail'},
							 {title:'Association Mobile', name:'assoMobile'},
							 {title:'Association Address', name:'address'}	,
							 {title:'District', name:'distname'},
							 {title:'State', name:'stateName'}	
							 ],
					});
		}
		if(reportTypewise=='Response'){
			//alert("reportTypewise----- "+reportTypewise);
			$("#report-data").bootgrid(
					{
						title:'',
						recordsinpage:'10',
						dataobject:'propertyList',    		
						dataurl:'show-Covid19Emergency-requested-mis-report?'+params,
						columndetails:
							[
							 {title:'S.No.', name:'sino'}, 
							 {title:'RCN', name:'rCN'},
							 {title:'Association Name', name:'association'}, 
							 {title:'Chief Functionary Name', name:'assochieffunName'},
							 {title:'Association EmailId', name:'assoMail'},
							 {title:'Association Mobile', name:'assoMobile'}	,
							 {title:'Address', name:'address'},
							 {title:'District', name:'distname'},
							 {title:'State', name:'stateName'},
							 {title:'Amount Spent', name:'estAmount'},
							 {title:'No. of Week Support Provided', name:'duration'},
							 {title:'Place Action Response', name:'placeRes'},
							 {title:'Last Updated On', name:'lastUpdate'}	
							
							 ],
					});
		}
		

	}
}


