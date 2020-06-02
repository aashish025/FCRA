var barContent=[];
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
function toggleSearchByInput(){
	if ( $('#association-search').is(':visible') ){		
		$('#association-search').hide();
		$('#functionary-search').show();	
	}else if($('#functionary-search').is(':visible')){		
		$('#association-search').show();	
		$('#functionary-search').hide();		
	}
}
function getDistrict(){
	var params = 'state='+$('#state').val();
	var action = 'get-district-registration-tracking';	
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
function prepareCheckboxList(list, containerId, id, name) {	
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
/*function resetAll(){
	$('#return-table').html('');
	$('#bar-notify').html('');
	$('#state').val('');
	$('#district').val('');
	$('#applicationName').val('');
	$('#applicationId').val('');
}*/
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
	    		dataurl:'get-application-list-registration-tracking?applicationId='+$('#applicationId').val()+'&applicationName='+$('#applicationName').val(),
	    		columndetails:
				[
					{title:'Registration Number', name:'assoRegnNumber',sortable:true},					
					{title:'Association Name', name:'applicantName'},					
				],
				onRowSelect:function(rowdata){getApplicationDetails(rowdata);}
    });	
	$('#application-list-div').show();
}

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
		    		dataurl:'get-advance-application-list-registration-tracking?state='+$('#state').val()+'&district='+$('#district').val()+'&applicationName='+$('#applicationName').val()+'&functionaryName='+$('#functionaryName').val(),
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
	$('#rcn-history-section').hide();	
	$('#committee-section').hide();
	$('#annual-return-section').hide();
	$('#bar-notify').html('');
	barContent=[];
	$('#return-table').html('');
	resetRedFlag();	
	oplr();
	var regn=data.assoRegnNumber;
	var params = 'appId='+data.assoRegnNumber;
	var action = 'get-application-details-registration-tracking';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');			
			initApplicationDetails(JSON.parse(data[0])[0]);			
			if(!(data[2]==null || data[2]==""))
				prepareAnnualReturnData(JSON.parse(data[2]),regn);			
			if(!(data[3]==null || data[3]==""))
				prepareCommitteeMembers(JSON.parse(data[3]));
						
			// To check and put Red Flag action
			if(data[5]=="YES"){  // Having Red Flag Role
				if(data[4]=="NO"){	// not added in red flag list				
					$('#add-red-flag-btn').show();
				}
			}	
			
			
			if(data[6]=="YES"){  // Having Red Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list			
					if(data[11]=="YES"){ // Checking Category Type- RED Category
						$('#remove-red-flag-btn').show();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-danger');
						$('#remove-red-flag-btn').text('Remove from Adverse [ RED ] List');
					}
				}
			}	
			
			if(data[10]=="YES"){  // Having Yellow Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list	
					if(data[12]=="YES"){	// Checking Category Type- YELLOW Category
						$('#remove-red-flag-btn').show();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-warning');
						$('#remove-red-flag-btn').text('Remove from Adverse [ YELLOW ] List');
					}
				}
			}	
			
			if(!(data[7]==null || data[7]==""))
				populateSelectBox(JSON.parse(data[7]),'Select Reason','redFlagCategory');			
			
			if(!(data[8]==null || data[8]==""))
				prepareRedFlagDetails(JSON.parse(data[8]),data[11],data[12],data[16]);			
			if(!(data[9]==null || data[9]==""))
				prepareRCNHistoryDetails(JSON.parse(data[9]));	
			
			if(!(data[13]==null || data[13]==""))
				prepareRCNCancellationDetails(JSON.parse(data[13]));
			
			if(!(data[14]==null || data[14]==""))
				prepareSMSDetails(JSON.parse(data[14]));
			
			if(!(data[15]==null || data[15]==""))
				prepareEMAILDetails(JSON.parse(data[15]));
			
			roplr();
		},
		error: function(textStatus,errorThrown){
			roplr();
			alert('error');			
		}
	});	
}
function resetRedFlag(){
	$('#remove-red-flag-btn').hide();
	$('#add-red-flag-btn').hide();
	$('#red-flag-details').hide();	
	postWork();
}
function prepareRedFlagDetails(data,redFlag,yellowFlag,blueFlag){
	//alert(data);
	//alert(yellowFlag=="YES");
	$('#asso-details-title').html('Details of Association ');
	/*if(data=="" || data==null){		
		return;
	}*/	
	
	if(redFlag=="YES"){
		$('#asso-details-title').html('Details of Association [ <span class="text-danger blink_me_red" style="font-size:18px;font-weight: bold;">RED FLAGGED</span> ]');
	}
	if(yellowFlag=="YES"){
		$('#asso-details-title').html('Details of Association [ <span class="text-warning blink_me_yellow" style="font-size:18px;font-weight: bold;">YELLOW FLAGGED</span> ]');
	}
	if(blueFlag=="YES"){
		//alert('YYYYY');
		var span_Text = document.getElementById("asso-details-title").innerHTML;
	//	alert(span_Text);
		var htmltext=span_Text+'[ <span class="text-warning blink_me_blue" style="font-size:18px;font-weight: bold;">BLUE FLAGGED</span> ]';
		
		//alert(htmltext);
		$('#asso-details-title').html(htmltext);
	}
	
	if(data==null || data==""){		
		return;
	}
	else{
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
}
function prepareRCNHistoryDetails(tableData){	
	$('#history-table').html('');	
	if(tableData=="" || tableData==null){	
		$('#history-table').html('');
		return;
	}		
	var content='<table class="table table-bordered"><tr><th>Application Id</th><th>Submission Date(dd-mm-yyyy)</th><th>Service</th><th>Status</th></tr>';		
	$.each(tableData, function(index, item){			
			var appId="'"+item.p1+"'";
			content+='<tr><td><a href="javascript:showApplication('+appId+');"><span>'+item.p1+'</span></a></td>'+
					 '<td>'+item.p2+'</td><td>'+item.p3+'</td><td>'+item.p4+'</td>'+						 
					 '</tr>';							
	});		
	content+='</table>';	
	$('#history-table').html(content);	
	$('#rcn-history-section').show();	
}

function showApplication(appId){	
	var url = 'popup-application-tracking-workspace?applicationId='+appId;
	openLink(url);	
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
	$('#appid-search').show();	
	$('#advance-search').hide();
	$('#toggle-btn').html('<span class="fa fa-search"></span>&nbsp;Advance Search');	
}
function resetAll(){
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
	$('#return-table').html('');
	$('#bar-notify').html('');
	$('#state').val('');
	$('#district').val('');
	$('#applicationName').val('');
	$('#applicationId').val('');
	
	
}
function removeFromRedFlagList(){
	var params = 'appId='+$('#regnNumber').html()+'&remark='+$('#removeStatusRemark').val()
				+'&office='+$('#originatorOfficeR').val()+'&order='+$('#orderNumberR').val()+'&date='+$('#orderDateR').val();
	var action = 'remove-red-flag-registration-tracking';	
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
	var params = 'appId='+$('#regnNumber').html()+'&remark='+$('#addStatusRemark').val()+'&category='+$('#redFlagCategory').val()
				+'&office='+$('#originatorOffice').val()+'&order='+$('#orderNumber').val()+'&date='+$('#orderDate').val();
	var action = 'add-red-flag-registration-tracking';	
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
function initApplicationDetails(data){	
	/* Initialize Hide and Show for Application Details */
		$('#app-info').show();	
		//$('#application-list-div').hide();	
		$('#actions').show();		
	/* Initialize Hide and Show for Application Details */
		prepareBasicInfo(data);	
		
}
function prepareBasicInfo(data){	
	$('#regnNumber').html(data.assoRegnNumber);
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
	//$('#exempted').html(data.exmpted);
	
	if(data.assoRegnNumber.endsWith("P")){
		$('#exemptedDiv').hide();		
	}else{
		$('#exempted').html(data.exmpted);
		$('#exemptedDiv').show();
	}
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
function prepareRCNCancellationDetails(data){
	$.each(data, function(index, item){
		$('#canc-date').html(item.p1);
		$('#canc-type').html(item.p2);
		$('#canc-remark').html(item.p3);
		$('#canc-reason').html(item.p4);
	});	
}
function prepareSMSDetails(data){	
	$("#sms-table").html('');
	if(data==null || data=="")
		return;
	$("#sms-table").initLocalgrid({
		columndetails : [ {
			title : 'Mobile',
			name : 'p1'
		}, {
			title : 'Message',
			name : 'p2'
		}, {
			title : 'Dated',
			name : 'p3'
		}, {
			title : 'Status',
			name : 'p4'
		}, {
			title : 'Status Date',
			name :  'p5'
		}]
	});
	$("#sms-table").addListToLocalgrid(data);
	$('#sms-section').show();
}
function prepareEMAILDetails(tableData){	
	if(tableData=="" || tableData==null){		
		return;
	}		
	var content='<table class="table table-bordered table-hover">'
				+'<tr>'
				+'<th>Mail Address</th>'
				+'<th>Subject</th>'
				+'<th>Mail Body</th>'
				+'<th>Dated</th>'
				+'<th>Status</th>'
				+'<th>Status Date</th>'				
				+'</tr>';		
	$.each(tableData, function(index, item){		
			content+='<tr>'+
					 '<td>'+item.p2+'</td>'+
					 '<td>'+item.p3+'</td>'+
					 '<td>\"'+item.p4+'\"</td>'+						 
					 '<td>'+item.p5+'</td>'+
					 '<td>'+item.p6+'</td>'+
					 '<td>'+item.p7+'</td>'+					 					 
					 '</tr>';							
	});		
	content+='</table>';	
	$('#mail-table').html(content);
	$('#mail-section').show();	
}
function viewMailBody(data){
	$('#mail-body-section').html('');
	$('#mail-body-section').html(data);
	$('#mailBodyModal').modal('show');	
}
function prepareAnnualReturnData(tableData,regnNumber){	
	if(tableData=="" || tableData==null){
		$("#bar-chart-btn").hide();
		$("#line-chart-btn").hide();
		$('#chart-div').html('');
		return;
	}		
	var content='<table class="table table-bordered table-hover">'
				+'<tr>'
				+'<th>Block Year</th>'
				+'<th>Foreign Amt Received (INR)</th>'
				+'<th>Interest Earned (INR)</th>'
				+'<th>Contribution Received from Local Source (INR)</th>'
				+'<th>Total Amount (INR) [1]</th>'
				+'<th>DonorWise Total (INR) [2]</th>'
				+'<th>Diff [1]-[2]</th>'				
				+'<th>Submission Date</th>'
				+'</tr>';		
	$.each(tableData, function(index, item){	
			var regn="'"+regnNumber+"'";
			var date="'"+item.p6+"'";
			var appId="'"+item.p7+"'";
			content+='<tr>'+
					 '<td><a href="javascript:getAnnualReturnDetails('+regn+','+date+','+appId+');"><span>'+item.p1+'</span></a></td>'+
					 '<td>'+item.p3+'</td>'+
					 '<td>'+item.p4+'</td>'+						 
					 '<td>'+item.p5+'</td>'+
					 '<td>'+item.p2+'</td>'+
					 '<td>'+item.p8+'</td>'+
					 '<td>'+item.p9+'</td>'+
					 '<td>'+item.p6+'</td>'+					 
					 '</tr>';
			var a=[];		
			a[0]=item.p1;
			a[1]=parseInt(item.p2);
			barContent[tableData.length-index-1]=a;						
	});		
	content+='</table>';	
	$('#return-table').html(content);
	$('#annual-return-section').show();
	$("#bar-chart-btn").click();
}
function prepareCommitteeMembers(data){	
	$("#executive-committee-table").html('');
	if(data==null || data=="")
		return;
	$("#executive-committee-table").initLocalgrid({
		columndetails : [ {
			title : 'Name',
			name : 'name'
		}, {
			title : 'Name Of Father/Husband',
			name : 'nameOfFatherSpouse'
		}, {
			title : 'Nationality',
			name : 'nationalityDesc'
		}, {
			title : 'Aadhar No. If Any',
			name : 'aadhaarNumber'
		}, {
			title : 'Occupation',
			name :  'occupationDesc'
		}, {
			title : 'Post Held in the Association',
			name :  'designationInAssociation'
		}, {
			title : 'Relationship with other member',
			name :  'relationWithOfficeBearers'
		}, {
			title : 'Address',
			name : 'officeAddress'
		}, {
			title : 'Mobile No.',
			name : 'mobile'
		}],
		onRowSelect:function(rowId, rowdata){getMemberDetails(rowdata);}
	});
	$("#executive-committee-table").addListToLocalgrid(data);
	$('#committee-section').show();
}
function getMemberDetails(data){	
	$('#name').html(data.name);
	$('#fhName').html(data.nameOfFatherSpouse);
	$('#nationality').html(data.nationalityDesc);
	$('#aadhar').html(data.aadhaarNumber);
	$('#occupation').html(data.occupationDesc);
	$('#assoDesig').html(data.designationInAssociation);
	$('#relnWthBrer').html(data.relationWithOfficeBearers);
	$('#offAdd').html(data.officeAddress);
	$('#resiAdd').html(data.residenceAddress);
	$('#email').html(data.email);
	$('#phone').html(data.phoneNumber);
	$('#mobile').html(data.mobile);
	$('#dob').html(data.dateOfBirth);
	$('#pob').html(data.placeOfBirth);
	$('#passport').html(data.passportNumber);
	$('#addFrgn').html(data.addressInForeignCountry);
	$('#indianOrigin').html(data.personOfIndianOrigin);
	$('#pici').html(data.pioOciCardNumber);
	$('#resiIndia').html(data.residentInIndia);
	$('#dateResiIndia').html(data.dateFromWhichResidingInIndia);
	
	$('#comm-mem-modal-btn').click();		
}
function prepareBarchart(){
	$("#bar-chart-btn").hide();
	$("#line-chart-btn").show();
	$('#chart-div').html('');	
    $('#chart-div').jqplot([barContent], {
        title:'',
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,            
            pointLabels: { show: true},
            rendererOptions: {               
                varyBarColor: true
            }        	
        },
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer,
                label: 'Year Range',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,                
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30                    
                }
            },
            yaxis: {
                label: 'Amount',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
              }
        }
    });
}
function prepareTrendchart(){
	$("#bar-chart-btn").show();
	$("#line-chart-btn").hide();
	$('#chart-div').html('');	
    $('#chart-div').jqplot([barContent], {
        title:'',
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
        	pointLabels: { show: true}     	
        },
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer,
                label: 'Year Range',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,                
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30                    
                }
            },
            yaxis: {
                label: 'Amount',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
              }
        }
    });
}
function printFullReportPDF(regnNumber){	
	var params='rcnNumber='+regnNumber+'&reportFormat=1'+'&reportType=tracking';
	var url = 'download-registration-tracking?'+params;
	$("#report-download-form").attr('action', url);
	$("#report-download-form").submit();	
}
function printFullReportExcel(regnNumber){	
	var params='rcnNumber='+regnNumber+'&reportFormat=2'+'&reportType=tracking';
	var url = 'download-registration-tracking?'+params;
	$("#report-download-form").attr('action', url);
	$("#report-download-form").submit();	
}
function printFullReportCSV(regnNumber){	
	var params='rcnNumber='+regnNumber+'&reportFormat=4'+'&reportType=tracking';
	var url = 'download-registration-tracking?'+params;
	$("#report-download-form").attr('action', url);
	$("#report-download-form").submit();	
}
function getAnnualReturnDetails(regNumber,date,appId){	
	if(date==null || date=="")
		showOldAnnualReturnDetails(regNumber);
	else
		showApplicationDetails(appId);
}
function showApplicationDetails(appId){	
	var url = 'popup-application-tracking?applicationId='+appId;
	openLink(url);	
}

function showOldAnnualReturnDetails(registrationNumber){	
	var url = 'https://fcraonline.nic.in/fc_annual_returns_rcn.aspx?reg_no='+registrationNumber;
	openLink(url);	
}
function getRequestDate(value){
	if(value=="R"){
		$('#request-date-div').show();
	}else{
		$('#request-date-div').hide();		
	}
}

//pup-up tracking
function getRegistrationDetails(regNo){	
	$('#bar-notify').html('');
  var params = 'appId='+regNo;
	var action = 'get-application-details-registration-tracking';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){	
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');			
			initApplicationDetails(JSON.parse(data[0])[0]);			
			if(!(data[2]==null || data[2]==""))
				prepareAnnualReturnData(JSON.parse(data[2]),regNo);
			if(!(data[3]==null || data[3]==""))
				prepareCommitteeMembers(JSON.parse(data[3]));
						
			// To check and put Red Flag action
			/*if(data[5]=="YES"){  // Having Red Flag Role
				if(data[4]=="NO"){	// not added in red flag list				
					$('#add-red-flag-btn').show();
				}
			}	
			
			
			if(data[6]=="YES"){  // Having Red Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list			
					if(data[11]=="YES"){ // Checking Category Type- RED Category
						$('#remove-red-flag-btn').show();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-danger');
						$('#remove-red-flag-btn').text('Remove from Adverse [ RED ] List');
					}
				}
			}	
			
			if(data[10]=="YES"){  // Having Yellow Flag Remove Role
				if(data[4]=="YES"){ // already added in red flag list	
					if(data[12]=="YES"){	// Checking Category Type- YELLOW Category
						$('#remove-red-flag-btn').show();
						$('#remove-red-flag-btn').removeClass().addClass('btn btn-warning');
						$('#remove-red-flag-btn').text('Remove from Adverse [ YELLOW ] List');
					}
				}
			}	*/
			$('#remove-red-flag-btn').hide();$('#add-red-flag-btn').hide();
			if(!(data[7]==null || data[7]==""))
				populateSelectBox(JSON.parse(data[7]),'Select Reason','redFlagCategory');			
			
			if(!(data[8]==null || data[8]==""))
				prepareRedFlagDetails(JSON.parse(data[8]),data[11],data[12],data[16]);			
			if(!(data[9]==null || data[9]==""))
				prepareRCNHistoryDetails(JSON.parse(data[9]));	
			
			if(!(data[13]==null || data[13]==""))
				prepareRCNCancellationDetails(JSON.parse(data[13]));
			
			if(!(data[14]==null || data[14]==""))
				prepareSMSDetails(JSON.parse(data[14]));
			
			if(!(data[15]==null || data[15]==""))
				prepareEMAILDetails(JSON.parse(data[15]));
			//getApplicationDetails(JSON.parse(data[0])[0]);
			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}