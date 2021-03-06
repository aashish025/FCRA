$(document).ready(function() {
	$("#applicationId").keyup(function(event) {
		if (event.which == 13) {
			event.preventDefault();
			getApplicationDetails();
		}
	}).keydown(function(event) {
		if (event.which == 13) {
			event.preventDefault();
		}
	});

});

function showNotifications(notificationList) {
	$("#bar-notify").empty();
	$("#text-notify").empty();
	notifyList(notificationList);
}

function getApplicationDetails() {
	 $('#application-track-info').hide('');
	$("#app-form").validationEngine({
		promptPosition : 'bottomRight'
	});
	if ($('#app-form').validationEngine('validate') == true) {
		var action = 'pull-details-application-tracking';
		var params = 'applicationId=' + $('#applicationId').val();
		var aa = $('#applicationId').val();
		$('#application-id').val(aa);
		$.ajax({
			url : action,
			method : 'GET',
			data : params,
			dataType : 'json',

			success : function(data) {
				var status = JSON.parse(data[0]).li;
				var notificationList = JSON.parse(data[1]);
				var applicationDetails = JSON.parse(data[2]);
				var smsDetails = JSON.parse(data[3]);
				var mailDetails =JSON.parse(data[4]);
		        //alert(JSON.stringify(mailDetails));
				//alert(JSON.stringify(data[2]));
						
				$('#application-details').empty();
				showNotifications(notificationList);
				
				if(status == 'error') {
					
				}
				else {
					populateForm(applicationDetails,smsDetails,mailDetails);
				}
			},
			error : function(textStatus, errorThrown) {
			}
		});

	}
 }

function prepareDocuments(data){	
	if(data != ''){				
		var htmlContent='<div class="list-group">';
		$.each(data,function(index,item){
			htmlContent+='<a class="list-group-item list-group-item-default" href="javascript:getApplicationDocument(\''+item.docPath+'\');">'+
			'<span class="text-info">'+item.docName+'</span> [<i><span class="text-danger">'+item.docDetails+'</span></i>]</a>'
		});	
		htmlContent+='</div>';	
		$('#project-doc-list').html(htmlContent);
	}else{
		$('#project-doc-list').html('<p class="text-danger">No document is uploaded by applicant.</p>');
	}
}
function getApplicationDocument(docPath){	
	window.open(docPath);
}


function populateForm(applicationDetails,smsDetails,mailDetails) {

	var serviceCode = applicationDetails.serviceId;

	if(serviceCode == '01') {
		populateRegistrationForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '02') {
		populatePriorPermissionForm(applicationDetails,smsDetails,mailDetails);		
	} else if(serviceCode == '03') {
		populateRenewalForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '04') {
		populateAnnualReturnsForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '05') {	
		populateFundTransferForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '06') {
		populateChangeOfDetailsForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '07') {
		populateHospitalityForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '08') {
		populateGiftContributionForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '09') {
		populateArticleContributionForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '10') {
		populateSecurityContributionForm(applicationDetails,smsDetails,mailDetails);
	} else if(serviceCode == '11') {
		populateElectionContributionForm(applicationDetails,smsDetails,mailDetails);
	}else if(serviceCode == '12') {
		populateGrievancesForm(applicationDetails,smsDetails,mailDetails);
	}else if(serviceCode == '13') {
		populateChangeOfNamrAndAddressDetailsForm(applicationDetails,smsDetails,mailDetails);
	}else if(serviceCode == '15') {
		populateChangeOfFcReceiptCumUtilisationBankDetailsForm(applicationDetails,smsDetails,mailDetails);
	}else if(serviceCode == '16') {
		populateChangeOfOpeningUtilisationBankActDetailsForm(applicationDetails,smsDetails,mailDetails);
	}else if(serviceCode == '17') {
		populateChangeOfCommitteeMembersDetailsForm(applicationDetails,smsDetails,mailDetails);
	}
}

function getValue(val) {
	if (val == null || val == undefined)
		return '';
	else
		return val;
}

function getYesNoValue(val) {
	if (val == null || val == undefined)
		return '';
	else
		if(val=='Y')
			return 'Yes';
		else if(val=='N')
			return 'No';
		else
			return '';
}

function populateRegistrationForm(applicationDetails,smsDetails,mailDetails) {
	//alert('hi');

	$('#application-details').load('resources/application-details.html #registration-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			prepareSMSDetails(smsDetails);
			prepareEMAILDetails(mailDetails);
			$('#application-track-info').show();
			$('#applicationId-info').text(getValue(applicationDetails.applicationId));
			$('#service-info').text(getValue(applicationDetails.serviceName));
			$('#submitted-info').text(getValue(applicationDetails.submissionDate));
			
			//15-01-16
			var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
			'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
			'<span class="glyphicon glyphicon-paperclip"></span></button>';	
			$('#bi-doc').html(docContent);	
			
			var documents=applicationDetails.uploadedDocuments;
			prepareDocuments(documents);
			/////
			
			   
				var associationdetail= applicationDetails.associationDetails;
			     $('#chiefFunctionaryName').text(getValue(associationdetail.chiefFunctionaryName));
				 $('#associationName').text(getValue(associationdetail.associationName));
				 $('#address').text(getValue(associationdetail.address));
		         $('#town').text(getValue(associationdetail.town));
				 $('#state').text(getValue(associationdetail.stateDesc));
				//if (associationdetail.districtDesc == null || associationdetail.districtDesc == undefined) $('#district').text('-'); else  $('#district').text(associationdetail.districtDesc);
				 $('#district').text(getValue(associationdetail.districtDesc));
				 $('#pincode').text(getValue(associationdetail.pincode));
				 $('#associationPhoneNumber').text(getValue(associationdetail.associationPhoneNumber));
				 $('#email').text(getValue(associationdetail.email));
				 $('#website').text(getValue(associationdetail.website));
				 $('#chiefFunctionaryMobile').text(getValue(associationdetail.chiefFunctionaryMobile));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(associationdetail.chiefFunctionaryPhoneNumber));
				 $('#registeredUnderAct').text(getValue(associationdetail.registeredUnderActDesc));
				 $('#registrationNumber').text(getValue(associationdetail.registrationNumber));
				 $('#placeOfRegistration').text(getValue(associationdetail.placeOfRegistration));
				 $('#dateOfRegistration').text(getValue(associationdetail.dateOfRegistration));
				 $('#panNumber').text(getValue(associationdetail.panNumber));
				// $('#fcraRegistrationNumber').text(getValue(associationdetail.fcraRegistrationNumber));
				 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(associationdetail.fcraRegistrationNumber)+'\");>'+getValue(associationdetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(associationdetail.fcraRegistrationDate));
				 $('#nature').text(getValue(associationdetail.natureDesc));
				$('#associationAim').text(getValue(associationdetail.associationAim));
				$('#darpanId').text(getValue(associationdetail.darpanId));
				$('#darpanPanNo').text(getValue(associationdetail.darpanPanNo));
			    //executive-committee-table
			    var committeeMembers=associationdetail.committeeMembers;
			    $("#executive-committee-table").html('');
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
						name : 'occupationDesc'
					}, {
						title : 'Post Held in the Association',
						name : 'designationInAssociationDesc'
					}, {
						title : 'Relationship with other member',
						name : 'relationWithOfficeBearersDesc'
					}, {
						title : 'Address',
						name : 'officeAddress'
					}, {
						title : 'Residential Address',
						name : 'residenceAddress'
					}, {
						title : 'Email Id',
						name : 'email'
					}, {
						title : 'Landline',
						name : 'phoneNumber'
					}, {
						title : 'Mobile No.',
						name : 'mobile'
					}],
					
				});
				$("#executive-committee-table").addListToLocalgrid(associationdetail.committeeMembers);
			    //EC detail and Other
				$('#memberConvictedByCourt').text(getYesNoValue(applicationDetails.memberConvictedByCourt));
				$('#memberUnderProsecution').text(getYesNoValue(applicationDetails.memberUnderProsecution));
			
				$('#memberFoundGuiltyOfMisutilization').text(getYesNoValue(applicationDetails.memberFoundGuiltyOfMisutilization));
				$('#memberProhibitedFromAcceptingForeignContribution').text(getYesNoValue(applicationDetails.memberProhibitedFromAcceptingForeignContribution));
				$('#memberKeyFunctionaryOfOtherAssn').text(getYesNoValue(applicationDetails.memberKeyFunctionaryOfOtherAssn));
				$('#memberKeyFunctionaryOfOtherAssnSec1314').text(getYesNoValue(applicationDetails.memberKeyFunctionaryOfOtherAssnSec1314));
				$('#detailsOfDiscrepancies').text(getYesNoValue(applicationDetails.detailsOfDiscrepancies));
				
			    if (applicationDetails.branchOfForeignOrganisation == null || applicationDetails.branchOfForeignOrganisation == undefined)$('#branchOfForeignOrganisation').text('-'); 
			    else {
			    	if(applicationDetails.branchOfForeignOrganisation=='Y'){
			    		
			    		$('#branchOfForeignOrganisation').text('Yes');  
			    		$('#branchOfForeignOrganisation-detail').show();
			    		
			    		
			    	} else $('#branchOfForeignOrganisation').text('No');
			    	}
			    
			    
		         $('#nameOfForeignOrganisation').text(getValue(applicationDetails.nameOfForeignOrganisation));
			     $('#addressOfForeignOrganisation').text(getValue(applicationDetails.addressOfForeignOrganisation));
			     $('#regnOrPriorPermnNumberOfForeignOrganisation').text(getValue(applicationDetails.regnOrPriorPermnNumberOfForeignOrganisation));
			     $('#dateOfRegnOrPriorPermnForeignOrganisation').text(getValue(applicationDetails.dateOfRegnOrPriorPermnForeignOrganisation));
				 $('#attractsSection10').text(getYesNoValue(applicationDetails.attractsSection10));
			     $('#orderNumberUnderSection11_3').text(getValue(applicationDetails.orderNumberUnderSection11_3)); 
			     $('#dateOfOrderUnderSection11_3').text(getValue(applicationDetails.dateOfOrderUnderSection11_3)); 
			     $('#directedToSeekPriorPermission').text(getYesNoValue(applicationDetails.directedToSeekPriorPermission));
                 $('#orderNumberSeekingPriorPermission').text(getValue(applicationDetails.orderNumberSeekingPriorPermission)); 
			     $('#dateOfOrderSeekingPriorPermission').text(getValue(applicationDetails.dateOfOrderSeekingPriorPermission));
				
			     $('#proceededAgainstFCRA').text(getYesNoValue(applicationDetails.proceededAgainstFCRA));
			     $('#proceededAgainstFCRADetails').text(getValue(applicationDetails.proceededAgainstFCRADetails));
				
				//Bank Detail
			var receipientBankDetails= associationdetail.receipientBankDetails;
			if(receipientBankDetails != null) {
				//alert('hi '+receipientBankDetails.accountNumber)
			   $('#accountNumber').text(getValue(receipientBankDetails.accountNumber));
			   $('#bankId').text(getValue(receipientBankDetails.bankName));
			   $('#receipientBankDetails_address').text(getValue(receipientBankDetails.address));
		   	   $('#receipientBankDetails_town').text(getValue(receipientBankDetails.town));
		 	   $('#receipientBankDetails_state').text(getValue(receipientBankDetails.stateDesc));
			   $('#receipientBankDetails_district').text(getValue(receipientBankDetails.districtDesc));
			   $('#receipientBankDetails_pincode').text(getValue(receipientBankDetails.pincode));
			   $('#receipientBankDetails_ifscCode').text(getValue(receipientBankDetails.ifscCode));
			}
			
			// regis OTHER DETAILS
			  $('#grantedPriorPermissionEarlier').text(getYesNoValue(applicationDetails.grantedPriorPermissionEarlier));
			  $('#memberConvictedByCourt').text(getYesNoValue(applicationDetails.memberConvictedByCourt));
			  $('#letterNumberOfGrantingPriorPermission').text(getValue(applicationDetails.letterNumberOfGrantingPriorPermission));
			  $('#dateOfGrantingPriorPermission').text(getValue(applicationDetails.dateOfGrantingPriorPermission));
			  $('#filedAnnualReturnsForPriorPermissionGiven').text(getYesNoValue(applicationDetails.filedAnnualReturnsForPriorPermissionGiven));
			  $('#dateOfSubmissionOfAnnualReturnsForPriorPermissionGiven').text(getValue(applicationDetails.dateOfSubmissionOfAnnualReturnsForPriorPermissionGiven));	 	
			  $('#receivedForeignContributionWithoutPriorPermissionEarlier').text(getYesNoValue(applicationDetails.receivedForeignContributionWithoutPriorPermissionEarlier));
			  $('#dateOfSubmissionOfAnnualReturnsForPriorPermissionGiven').text(getValue(applicationDetails.dateOfSubmissionOfAnnualReturnsForPriorPermissionGiven));	 	
			  $('#receivedForeignContributionCurrencyDesc').text(getValue(applicationDetails.receivedForeignContributionCurrencyDesc));	 			  
			  $('#receivedForeignContributionCurrencyOther').text(getValue(applicationDetails.receivedForeignContributionCurrencyOther));	 				  
			  $('#receivedForeignContributionAmount').text(getValue(applicationDetails.receivedForeignContributionAmount));	 	
			  
			  $('#withoutpp-accountNumber').text(getValue(applicationDetails.bankDetailsForReceivingMoneyWithoutPriorPermission.accountNumber));	 	
			  
			  $('#withoutpp-bankName').text(getValue(applicationDetails.bankDetailsForReceivingMoneyWithoutPriorPermission.bankName));	 	
			  
			  $('#withoutpp-address').text(getValue(applicationDetails.bankDetailsForReceivingMoneyWithoutPriorPermission.address));
			  
			  $('#violationHasBeenCondoned').text(getYesNoValue(applicationDetails.violationHasBeenCondoned));
			  
			  $('#orderNumberForCondonation').text(getValue(applicationDetails.orderNumberForCondonation));
			  $('#dateOfOrderOfCondonation').text(getValue(applicationDetails.dateOfOrderOfCondonation));
			  
			  $('#associationProhibitedFromAcceptingForeignContribution').text(getYesNoValue(applicationDetails.associationProhibitedFromAcceptingForeignContribution));
			  
			  $('#associationProhibitedFromAcceptingForeignContributionDetails').text(getValue(applicationDetails.associationProhibitedFromAcceptingForeignContributionDetails));
			  
			  $('#appliedForRegistrationInPast').text(getYesNoValue(applicationDetails.appliedForRegistrationInPast));
				 
			  
			  $('#applicationNumberForRegistrationInPast').text(getValue(applicationDetails.applicationNumberForRegistrationInPast));
			  
			  $('#dateOfSubmissionForRegistrationInPast').text(getValue(applicationDetails.dateOfSubmissionForRegistrationInPast));
			  
			  $('#letterNumberOfLastCommnForRegistrationInPast').text(getValue(applicationDetails.letterNumberOfLastCommnForRegistrationInPast));
			  
			  $('#dateOfLastCommnForRegistrationInPast').text(getValue(applicationDetails.dateOfLastCommnForRegistrationInPast));
			  
		
			  $('#registrationInPastRefused').text(getYesNoValue(applicationDetails.registrationInPastRefused));
			  
		
			  $('#applicationOfRegistrationInPastPending').text(getYesNoValue(applicationDetails.applicationOfRegistrationInPastPending));
			  
			  $('#appliedForPriorPermissionInPast').text(getYesNoValue(applicationDetails.appliedForPriorPermissionInPast));
			  
			  
			  $('#applicationNumberForPriorPermissionInPast').text(getValue(applicationDetails.applicationNumberForPriorPermissionInPast));
			  $('#dateOfSubmissionForPriorPermissionInPast').text(getValue(applicationDetails.dateOfSubmissionForPriorPermissionInPast));
		
			  
			  $('#letterNumberOfLastCommnForPriorPermissionInPast').text(getValue(applicationDetails.letterNumberOfLastCommnForPriorPermissionInPast));
			  $('#dateOfLastCommnForPriorPermissionInPast').text(getValue(applicationDetails.dateOfLastCommnForPriorPermissionInPast));
			  
			  $('#priorPermissionInPastRefused').text(getYesNoValue(applicationDetails.priorPermissionInPastRefused));
			  
			  $('#applicationOfPriorPermissionInPastPending').text(getYesNoValue(applicationDetails.applicationOfPriorPermissionInPastPending));
			  
			  $('#associationHasLinkRefusedRegistration').text(getYesNoValue(applicationDetails.associationHasLinkRefusedRegistration));
			  
			  $('#associationHasLinkProhibitedFromAcceptingForeignContribution').text(getYesNoValue(applicationDetails.associationHasLinkProhibitedFromAcceptingForeignContribution));
			   
			  $('#associationHasLinkSuspendedOrCancelledRegistration').text(getYesNoValue(applicationDetails.associationHasLinkSuspendedOrCancelledRegistration));
			  
			  $('#linkAssociationName').text(getValue(applicationDetails.linkAssociationName));
			  $('#linkAssociationAddress').text(getValue(applicationDetails.linkAssociationAddress));
			  
			
			  
		}
	});
	
	
}

function populatePriorPermissionForm(applicationDetails,smsDetails,mailDetails) {

	$('#application-details').load('resources/application-details.html #prior-permission', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			prepareSMSDetails(smsDetails);
			prepareEMAILDetails(mailDetails);
			$('#application-track-info').show();
			$('#applicationId-info').text(getValue(applicationDetails.applicationId));
			$('#service-info').text(getValue(applicationDetails.serviceName));
			$('#submitted-info').text(getValue(applicationDetails.submissionDate));			
			//15-01-16
			var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
			'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
			'<span class="glyphicon glyphicon-paperclip"></span></button>';	
			$('#bi-doc').html(docContent);	
			
			var documents=applicationDetails.uploadedDocuments;
			prepareDocuments(documents);
			/////
			   
				var associationdetail= applicationDetails.associationDetails;
			     $('#chiefFunctionaryName').text(getValue(associationdetail.chiefFunctionaryName));
				 $('#associationName').text(getValue(associationdetail.associationName));
				 $('#address').text(getValue(associationdetail.address));
		         $('#town').text(getValue(associationdetail.town));
				 $('#state').text(getValue(associationdetail.stateDesc));
				//if (associationdetail.districtDesc == null || associationdetail.districtDesc == undefined) $('#district').text('-'); else  $('#district').text(associationdetail.districtDesc);
				 $('#district').text(getValue(associationdetail.districtDesc));
				 $('#pincode').text(getValue(associationdetail.pincode));
				 $('#associationPhoneNumber').text(getValue(associationdetail.associationPhoneNumber));
				 $('#email').text(getValue(associationdetail.email));
				$('#website').text(getValue(associationdetail.website));
				 $('#chiefFunctionaryMobile').text(getValue(associationdetail.chiefFunctionaryMobile));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(associationdetail.chiefFunctionaryPhoneNumber));
				 $('#registeredUnderAct').text(getValue(associationdetail.registeredUnderActDesc));
				 $('#registrationNumber').text(getValue(associationdetail.registrationNumber));
				 $('#placeOfRegistration').text(getValue(associationdetail.placeOfRegistration));
				 $('#dateOfRegistration').text(getValue(associationdetail.dateOfRegistration));
				 $('#panNumber').text(getValue(associationdetail.panNumber));
				// $('#fcraRegistrationNumber').text(getValue(associationdetail.fcraRegistrationNumber));
				 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(associationdetail.fcraRegistrationNumber)+'\");>'+getValue(associationdetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(associationdetail.fcraRegistrationDate));
				 $('#nature').text(getValue(associationdetail.natureDesc));
				$('#associationAim').text(getValue(associationdetail.associationAim));
				
				$('#darpanId').text(getValue(associationdetail.darpanId));
				$('#darpanPanNo').text(getValue(associationdetail.darpanPanNo));
			    //executive-committee-table
			    var committeeMembers=associationdetail.committeeMembers;
			    $("#executive-committee-table").html('');
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
						name : 'occupationDesc'
					}, {
						title : 'Post Held in the Association',
						name : 'designationInAssociationDesc'
					}, {
						title : 'Relationship with other member',
						name : 'relationWithOfficeBearersDesc'
					}, {
						title : 'Address',
						name : 'officeAddress'
					}, {
						title : 'Residential Address',
						name : 'residenceAddress'
					}, {
						title : 'Email Id',
						name : 'email'
					}, {
						title : 'Landline',
						name : 'phoneNumber'
					}, {
						title : 'Mobile No.',
						name : 'mobile'
					}],
					
				});
				$("#executive-committee-table").addListToLocalgrid(associationdetail.committeeMembers);
			    //EC detail and Other
				$('#memberConvictedByCourt').text(getYesNoValue(applicationDetails.memberConvictedByCourt));
				$('#memberUnderProsecution').text(getYesNoValue(applicationDetails.memberUnderProsecution));
			
				$('#memberFoundGuiltyOfMisutilization').text(getYesNoValue(applicationDetails.memberFoundGuiltyOfMisutilization));
				$('#memberProhibitedFromAcceptingForeignContribution').text(getYesNoValue(applicationDetails.memberProhibitedFromAcceptingForeignContribution));
				$('#memberKeyFunctionaryOfOtherAssn').text(getYesNoValue(applicationDetails.memberKeyFunctionaryOfOtherAssn));
				$('#memberKeyFunctionaryOfOtherAssnSec1314').text(getYesNoValue(applicationDetails.memberKeyFunctionaryOfOtherAssnSec1314));
				$('#detailsOfDiscrepancies').text(getYesNoValue(applicationDetails.detailsOfDiscrepancies));
				
			    if (applicationDetails.branchOfForeignOrganisation == null || applicationDetails.branchOfForeignOrganisation == undefined)$('#branchOfForeignOrganisation').text('-'); 
			    else {
			    	if(applicationDetails.branchOfForeignOrganisation=='Y'){
			    		
			    		$('#branchOfForeignOrganisation').text('Yes');  
			    		$('#branchOfForeignOrganisation-detail').show();
			    		
			    		
			    	} else $('#branchOfForeignOrganisation').text('No');
			    	}
			    
			    
		         $('#nameOfForeignOrganisation').text(getValue(applicationDetails.nameOfForeignOrganisation));
			     $('#addressOfForeignOrganisation').text(getValue(applicationDetails.addressOfForeignOrganisation));
			     $('#regnOrPriorPermnNumberOfForeignOrganisation').text(getValue(applicationDetails.regnOrPriorPermnNumberOfForeignOrganisation));
			     $('#dateOfRegnOrPriorPermnForeignOrganisation').text(getValue(applicationDetails.dateOfRegnOrPriorPermnForeignOrganisation));
				
			    $('#attractsSection10').text(getYesNoValue(applicationDetails.attractsSection10));
			  
                $('#orderNumberUnderSection11_3').text(getValue(applicationDetails.orderNumberUnderSection11_3)); 
			    $('#dateOfOrderUnderSection11_3').text(getValue(applicationDetails.dateOfOrderUnderSection11_3)); 
			    $('#directedToSeekPriorPermission').text(getYesNoValue(applicationDetails.directedToSeekPriorPermission));
                $('#orderNumberSeekingPriorPermission').text(getValue(applicationDetails.orderNumberSeekingPriorPermission)); 
			    $('#dateOfOrderSeekingPriorPermission').text(getValue(applicationDetails.dateOfOrderSeekingPriorPermission));
				
			    $('#proceededAgainstFCRA').text(getYesNoValue(applicationDetails.proceededAgainstFCRA));
			    $('#proceededAgainstFCRADetails').text(getValue(applicationDetails.proceededAgainstFCRADetails));
				
				//Bank Detail
			var receipientBankDetails= associationdetail.receipientBankDetails;
			if(receipientBankDetails != null) {
			   $('#accountNumber').text(getValue(receipientBankDetails.accountNumber));
			   $('#bankId').text(getValue(receipientBankDetails.bankName));
			   $('#receipientBankDetails_address').text(getValue(receipientBankDetails.address));
		   	   $('#receipientBankDetails_town').text(getValue(receipientBankDetails.town));
		 	   $('#receipientBankDetails_state').text(getValue(receipientBankDetails.stateDesc));
			   $('#receipientBankDetails_district').text(getValue(receipientBankDetails.districtDesc));
			   $('#receipientBankDetails_pincode').text(getValue(receipientBankDetails.pincode));
			   $('#receipientBankDetails_ifscCode').text(getValue(receipientBankDetails.ifscCode));
			}
			
			
			//donorcommit tabs data starts here
			
			$('#natureOfContributionDesc').text(getValue(applicationDetails.natureOfContributionDesc));
			$('#amount').text(getValue(applicationDetails.amount));
			$('#currencyDesc').text(getValue(applicationDetails.currencyDesc));
			$('#currencyOther').text(getValue(applicationDetails.currencyOther));
			$('#projectDetails').text(getValue(applicationDetails.projectDetails));
			$('#purposeDesc').text(getValue(applicationDetails.purposeDesc));
			$('#natureOfContributionDesc').text(getValue(applicationDetails.natureOfContributionDesc));
			
			//donordetail table 
		    //donor-details_table
		    var donorDetails=associationdetail.donorDetails;
		    $("#donor_details_table").html('');
			$("#donor_details_table").initLocalgrid({
				columndetails : [ {
					title : 'Donor type:(Institutional/Individual)',
					name : 'donorTypeDesc'
				}, {
					title : 'Name of foreign source(Donor Name)',
					name : 'donorName'
				}, {
					title : 'Office Address',
					name : 'officeAddress'
				}, {
					title : 'Donor Country',
					name : 'donorCountryDesc'
				}, {
					title : 'Email Id',
					name : 'email'
				}, {
					title : 'Name of father/spouse',
					name : 'nameOfFatherSpouse'
				}, {
					title : 'Donor Nationality',
					name : 'nationalityDesc'
				}, {
					title : 'Passport no',
					name : 'passportNumber'
				}, {
					title : 'Occupation',
					name : 'occupationDesc'
				}, {
					title : 'Occupation(Others)',
					name : 'occupationOther'
				}],
				
			});
			
			$("#donor_details_table").addListToLocalgrid(associationdetail.donorDetails);
			
		}		
	});
	
	
}

function populateRenewalForm(applicationDetails,smsDetails,mailDetails) {
	$('#application-details').load('resources/application-details.html #renewal-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
			 $('#application-track-info').show();
			   
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			 
				var associationdetail= applicationDetails.associationDetails;
			     $('#chiefFunctionaryName').text(getValue(associationdetail.chiefFunctionaryName));
				 $('#associationName').text(getValue(associationdetail.associationName));
				 $('#address').text(getValue(associationdetail.address));
		         $('#town').text(getValue(associationdetail.town));
				 $('#state').text(getValue(associationdetail.stateDesc));
				//if (associationdetail.districtDesc == null || associationdetail.districtDesc == undefined) $('#district').text('-'); else  $('#district').text(associationdetail.districtDesc);
				 $('#district').text(getValue(associationdetail.districtDesc));
				 $('#pincode').text(getValue(associationdetail.pincode));
				 $('#associationPhoneNumber').text(getValue(associationdetail.associationPhoneNumber));
				 $('#email').text(getValue(associationdetail.email));
				$('#website').text(getValue(associationdetail.website));
				 $('#chiefFunctionaryMobile').text(getValue(associationdetail.chiefFunctionaryMobile));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(associationdetail.chiefFunctionaryPhoneNumber));
				 $('#registeredUnderAct').text(getValue(associationdetail.registeredUnderActDesc));
				 $('#registrationNumber').text(getValue(associationdetail.registrationNumber));
				 $('#placeOfRegistration').text(getValue(associationdetail.placeOfRegistration));
				 $('#dateOfRegistration').text(getValue(associationdetail.dateOfRegistration));
				 $('#panNumber').text(getValue(associationdetail.panNumber));
				// $('#fcraRegistrationNumber').text(getValue(associationdetail.fcraRegistrationNumber));
				 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(associationdetail.fcraRegistrationNumber)+'\");>'+getValue(associationdetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(associationdetail.fcraRegistrationDate));
				 $('#nature').text(getValue(associationdetail.natureDesc));
				$('#associationAim').text(getValue(associationdetail.associationAim));
			    //executive-committee-table
			    var committeeMembers=associationdetail.committeeMembers;
			    $("#executive-committee-table").html('');
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
						name : 'occupationDesc'
					}, {
						title : 'Post Held in the Association',
						name : 'designationInAssociationDesc'
					}, {
						title : 'Relationship with other member',
						name : 'relationWithOfficeBearersDesc'
					}, {
						title : 'Address',
						name : 'officeAddress'
					}, {
						title : 'Residential Address',
						name : 'residenceAddress'
					}, {
						title : 'Email Id',
						name : 'email'
					}, {
						title : 'Landline',
						name : 'phoneNumber'
					}, {
						title : 'Mobile No.',
						name : 'mobile'
					}],
					
				});
				$("#executive-committee-table").addListToLocalgrid(associationdetail.committeeMembers);
			    //EC detail and Other
				$('#memberConvictedByCourt').text(getYesNoValue(applicationDetails.memberConvictedByCourt));
				$('#memberUnderProsecution').text(getYesNoValue(applicationDetails.memberUnderProsecution));
			
				$('#memberFoundGuiltyOfMisutilization').text(getYesNoValue(applicationDetails.memberFoundGuiltyOfMisutilization));
				$('#memberProhibitedFromAcceptingForeignContribution').text(getYesNoValue(applicationDetails.memberProhibitedFromAcceptingForeignContribution));
				$('#memberKeyFunctionaryOfOtherAssn').text(getYesNoValue(applicationDetails.memberKeyFunctionaryOfOtherAssn));
				$('#memberKeyFunctionaryOfOtherAssnSec1314').text(getYesNoValue(applicationDetails.memberKeyFunctionaryOfOtherAssnSec1314));
				$('#detailsOfDiscrepancies').text(getYesNoValue(applicationDetails.detailsOfDiscrepancies));
				
			    if (applicationDetails.branchOfForeignOrganisation == null || applicationDetails.branchOfForeignOrganisation == undefined)$('#branchOfForeignOrganisation').text('-'); 
			    else {
			    	if(applicationDetails.branchOfForeignOrganisation=='Y'){
			    		
			    		$('#branchOfForeignOrganisation').text('Yes');  
			    		$('#branchOfForeignOrganisation-detail').show();
			    		
			    		
			    	} else $('#branchOfForeignOrganisation').text('No');
			    	}
			    
			    
		         $('#nameOfForeignOrganisation').text(getValue(applicationDetails.nameOfForeignOrganisation));
			     $('#addressOfForeignOrganisation').text(getValue(applicationDetails.addressOfForeignOrganisation));
			     $('#regnOrPriorPermnNumberOfForeignOrganisation').text(getValue(applicationDetails.regnOrPriorPermnNumberOfForeignOrganisation));
			     $('#dateOfRegnOrPriorPermnForeignOrganisation').text(getValue(applicationDetails.dateOfRegnOrPriorPermnForeignOrganisation));
				
			    $('#attractsSection10').text(getYesNoValue(applicationDetails.attractsSection10));
			  
                $('#orderNumberUnderSection11_3').text(getValue(applicationDetails.orderNumberUnderSection11_3)); 
			    $('#dateOfOrderUnderSection11_3').text(getValue(applicationDetails.dateOfOrderUnderSection11_3)); 
			    $('#directedToSeekPriorPermission').text(getYesNoValue(applicationDetails.directedToSeekPriorPermission));
                $('#orderNumberSeekingPriorPermission').text(getValue(applicationDetails.orderNumberSeekingPriorPermission)); 
			    $('#dateOfOrderSeekingPriorPermission').text(getValue(applicationDetails.dateOfOrderSeekingPriorPermission));
				
			    $('#proceededAgainstFCRA').text(getYesNoValue(applicationDetails.proceededAgainstFCRA));
			    $('#proceededAgainstFCRADetails').text(getValue(applicationDetails.proceededAgainstFCRADetails));
				
				//Bank Detail
			var receipientBankDetails= associationdetail.receipientBankDetails;
			if(receipientBankDetails != null) {
			   $('#accountNumber').text(getValue(receipientBankDetails.accountNumber));
			   $('#bankId').text(getValue(receipientBankDetails.bankName));
			   $('#receipientBankDetails_address').text(getValue(receipientBankDetails.address));
		   	   $('#receipientBankDetails_town').text(getValue(receipientBankDetails.town));
		 	   $('#receipientBankDetails_state').text(getValue(receipientBankDetails.stateDesc));
			   $('#receipientBankDetails_district').text(getValue(receipientBankDetails.districtDesc));
			   $('#receipientBankDetails_pincode').text(getValue(receipientBankDetails.pincode));
			   $('#receipientBankDetails_ifscCode').text(getValue(receipientBankDetails.ifscCode));
			}
			
			 var fcradetail= applicationDetails.fcraDetails;
			 $('#fcra-association-name').text(getValue(fcradetail.associationName));
			 $('#fcra-address').text(getValue(fcradetail.address));
	         $('#fcra-town').text(getValue(fcradetail.town));
			 $('#fcra-state').text(getValue(fcradetail.stateDesc));
			 $('#fcra-district').text(getValue(fcradetail.districtDesc));
			 $('#fcra-pincode').text(getValue(fcradetail.pincode));
			 $('#fcra-nature').text(getValue(fcradetail.natureDesc));
			 //$('#fcra-rcn').text(getValue(fcradetail.fcraRegistrationNumber));
			 $('#fcra-rcn').html('<a onclick=getRegistrationDetails(\"'+getValue(fcradetail.fcraRegistrationNumber)+'\");>'+getValue(fcradetail.fcraRegistrationNumber)+'</a>');
			 $('#fcra-reg-date').text(getValue(fcradetail.fcraRegistrationDate));
			 $('#fcra-valid-upto').text(getValue(fcradetail.registrationValidUpto));

			var fcraBankDetails= fcradetail.receipientBankDetails;
			if(fcraBankDetails != null) {
				$('#fcra-account-number').text(getValue(fcraBankDetails.accountNumber));
				$('#fcra-bankId').text(getValue(fcraBankDetails.bankName));
				$('#fcra-bank-address').text(getValue(fcraBankDetails.address));
				$('#fcra-bank-town').text(getValue(fcraBankDetails.town));
				$('#fcra-bank-state').text(getValue(fcraBankDetails.stateDesc));
				$('#fcra-bank-district').text(getValue(fcraBankDetails.districtDesc));
				$('#fcra-bank-pincode').text(getValue(fcraBankDetails.pincode));
				//$('#fcra-bank-ifscCode').text(getValue(fcraBankDetails.ifscCode));
			}
			
			var annualReturns=applicationDetails.annualReturns;
		    $("#annual-returns-table").html('');
			$("#annual-returns-table").initLocalgrid({
				columndetails : [ {
					title : 'Application Id',
					name : 'applicationId',
					formatter:function(rowdata){var applicationId="'"+rowdata['applicationId']+"'";
					var link = '';
					var registrationNumber = "'"+rowdata['associationDetails']['fcraRegistrationNumber']+"'";
					if(rowdata['submissionDate'] == '' || rowdata['submissionDate'] == null)
						link = '<a onclick="javascript:showOldAnnualReturnDetails('+registrationNumber+');" >'+rowdata['applicationId']+'</button>';
					else
						link = '<a onclick="javascript:showApplicationDetails('+applicationId+');" >'+rowdata['applicationId']+'</button>';
					return link;}
				}, {
					title : 'Block Year',
					name : 'blockYear'
				}, {
					title : 'Application Status',
					name : 'currentStatusDesc'
				}],
				
			});
			$("#annual-returns-table").addListToLocalgrid(annualReturns);
		}
	});
	
}

function  getRegistrationDetails(fcraRegistrationNumber){
	 var url = 'popup-registration-tracking?appId='+fcraRegistrationNumber;
	 openLink(url);
}

function showApplicationDetails(appId){	
	var url = 'popup-application-tracking-workspace?applicationId='+appId;
	openLink(url);	
}

function showOldAnnualReturnDetails(registrationNumber){	
	var url = 'https://fcraonline.nic.in/fc_annual_returns_rcn.aspx?reg_no='+registrationNumber;
	openLink(url);	
}

function populateAnnualReturnsForm(applicationDetails,smsDetails,mailDetails) {
	

	$('#application-details').load('resources/application-details.html #annual-returns-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
			 $('#application-track-info').show();
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			 
				var associationdetail= applicationDetails.associationDetails;
			     $('#chiefFunctionaryName').text(getValue(associationdetail.chiefFunctionaryName));
				 $('#associationName').text(getValue(associationdetail.associationName));
				 $('#address').text(getValue(associationdetail.address));
		         $('#town').text(getValue(associationdetail.town));
				 $('#state').text(getValue(associationdetail.stateDesc));
				 $('#district').text(getValue(associationdetail.districtDesc));
				 $('#pincode').text(getValue(associationdetail.pincode));
				// $('#fcraRegistrationNumber').text(getValue(associationdetail.fcraRegistrationNumber));
				 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(associationdetail.fcraRegistrationNumber)+'\");>'+getValue(associationdetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(associationdetail.fcraRegistrationDate));
				 $('#nature').text(getValue(associationdetail.natureDesc));
				 $('#religionDesc').text(getValue(associationdetail.religionDesc));
				 $('#associationAim').text(getValue(associationdetail.associationAim));
			
				 //Fc Received
				 $('#blockYear').text(getValue(applicationDetails.blockYear));
				 $('#amountBroughtForward').text(getValue(applicationDetails.amountBroughtForward));
				 $('#otherReceipt').text(getValue(applicationDetails.otherReceipt));
				 $('#foreignContributionDirect').text(getValue(applicationDetails.foreignContributionDirect));
				 $('#foreignContributionTransfer').text(getValue(applicationDetails.foreignContributionTransfer));
				 $('#totalForeignContribution').text(getValue(applicationDetails.totalForeignContribution));
				 
				 
				 
				 
			    //purpose-wise-table
			
			    $("#purpose-wise-table").html('');
				$("#purpose-wise-table").initLocalgrid({
					columndetails : [ {
						title : 'Purpose',
						name : 'purposeDesc'
					}, {
						title : 'Amount',
						name : 'amount'
					}],
					
				});
				$("#purpose-wise-table").addListToLocalgrid(applicationDetails.purposeWiseContribution);
				//donor-wise-table
			    $("#donor-wise-table").html('');
				$("#donor-wise-table").initLocalgrid({
					columndetails : [ {
						title : 'Donor Name',
						name : 'donor.donorName'
					}, {
						title : 'Donor Type',
						name : 'donor.donorTypeDesc'
					}, {
						title : 'Country',
						name : 'donor.donorCountryDesc'
					},
					{
						title : 'Address',
						name : 'donor.officeAddress'
					}, {
						title : 'Email Id',
						name : 'donor.email'
					}, {
						title : 'Website Url',
						name : 'donor.website'
					},
					{
						title : 'Purpose',
						name : 'purposeDesc'
					}, {
						title : 'Ammount',
						name : 'amount'
					}],
					
				});
				$("#donor-wise-table").addListToLocalgrid(applicationDetails.donorWiseContribution);
			   
				//Utilisation
				$('#blockYear2').text(getValue(applicationDetails.blockYear));
				$('#totalUtilizationForProjects').text(getValue(applicationDetails.totalUtilizationForProjects));
			
				$('#totalAdministrativeExpenses').text(getValue(applicationDetails.totalAdministrativeExpenses));
				$('#totalInvestedInTermDeposits').text(getValue(applicationDetails.totalInvestedInTermDeposits));
				$('#totalPurchase').text(getValue(applicationDetails.totalPurchase));
				$('#totalUtilization').text(getValue(applicationDetails.totalUtilization));
				$('#balance').text(getValue(applicationDetails.balance));
				$('#totalNumberOfForeignersWorking').text(getValue(applicationDetails.totalNumberOfForeignersWorking));
				
				 //FC-bank-account
				// alert(associationdetail.receipientBankDetails);
				var fcbankaccount=applicationDetails.receipientBankDetails;
		         $('#blockYear3').text(getValue(applicationDetails.blockYear));
			     $('#receipientBankDetails-accountNumber').text(getValue(fcbankaccount.accountNumber));
			     $('#receipientBankDetails-bankName').text(getValue(fcbankaccount.bankName));
				 $('#receipientBankDetails-address').text(getValue(fcbankaccount.address));
			     $('#receipientBankDetails-town').text(getValue(fcbankaccount.town));
			     $('#receipientBankDetails-stateDesc').text(getValue(fcbankaccount.stateDesc)); 
			     $('#receipientBankDetails-districtDesc').text(getValue(fcbankaccount.districtDesc));
                 $('#receipientBankDetails-pincode').text(getValue(fcbankaccount.pincode)); 
			     $('#receipientBankDetails-ifscCode').text(getValue(fcbankaccount.ifscCode));
				
			   
				$("#fc-utilization-bank-accounts-table").html('');
				$("#fc-utilization-bank-accounts-table").initLocalgrid({
					columndetails : [ {
						title : 'Account No',
						name : 'accountNumber'
					}, {
						title : 'Name Of the Bank',
						name : 'bankName'
					}, {
						title : 'Address',
						name : 'address'
					},
					{
						title : 'Town/City',
						name : 'town'
					}, {
						title : 'State',
						name : 'stateDesc'
					}, {
						title : 'District',
						name : 'districtDesc'
					},
					{
						title : 'Pin Code',
						name : 'pincode'
					}, {
						title : 'IFSC Code',
						name : 'ifscCode'
					}],
					
				});
				$("#fc-utilization-bank-accounts-table").addListToLocalgrid(applicationDetails.utilizationBankDetails);   
					
					
					
		}
	});
	
	
}

function populateFundTransferForm(applicationDetails,smsDetails,mailDetails) {

	//alert(JSON.stringify(applicationDetails));

	$('#application-details').load('resources/application-details.html #fund-transfer-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			   //info bar
			  prepareSMSDetails(smsDetails);
			  prepareEMAILDetails(mailDetails);
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
			 $('#application-track-info').show();
			
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			
			 
			  var associationdetail=applicationDetails.associationDetails;
			     // alert(JSON.stringify(applicationDetails));
			     $('#chiefFunctionaryName').text(getValue(associationdetail.chiefFunctionaryName));
			     $('#associationName').text(getValue(associationdetail.associationName));
				 $('#address').text(getValue(associationdetail.address));
		         $('#town').text(getValue(associationdetail.town));
				 $('#state').text(getValue(associationdetail.stateDesc));
				 $('#district').text(getValue(associationdetail.districtDesc));
				 $('#pincode').text(getValue(associationdetail.pincode));
				// $('#fcraRegistrationNumber').text(getValue(associationdetail.fcraRegistrationNumber));
				 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(associationdetail.fcraRegistrationNumber)+'\");>'+getValue(associationdetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(associationdetail.fcraRegistrationDate));
				 $('#nature').text(getValue(associationdetail.natureDesc));
				 $('#religionDesc').text(getValue(associationdetail.religionDesc));
				 $('#associationAim').text(getValue(associationdetail.associationAim));
				 
				   var recBankDetail=associationdetail.receipientBankDetails;
				   
				     $('#receipientBankDetails-accountNumber').text(getValue(recBankDetail.accountNumber));
				     $('#receipientBankDetails-bankName').text(getValue(recBankDetail.bankName));
					 $('#receipientBankDetails-address').text(getValue(recBankDetail.address));
					 $('#receipientBankDetails-town').text(getValue(recBankDetail.town));
					
				     $('#receipientBankDetails-stateDesc').text(getValue(recBankDetail.stateDesc)); 
				     $('#receipientBankDetails-districtDesc').text(getValue(recBankDetail.districtDesc));
	                 $('#receipientBankDetails-pincode').text(getValue(recBankDetail.pincode)); 
				     $('#receipientBankDetails-ifscCode').text(getValue(recBankDetail.ifscCode));
			
				 //Basic Transfer Details
				  
				     var receipientAssociationDetail=applicationDetails.receipientAssociationDetails;
				  
				     
				 $('#receipientAssociationDetails-associationName').text(getValue(receipientAssociationDetail.associationName));
				 $('#receipientAssociationDetails-address').text(getValue(receipientAssociationDetail.address));
				 $('#receipientAssociationDetails-town').text(getValue(receipientAssociationDetail.town));
				 $('#receipientAssociationDetails-stateDesc').text(getValue(receipientAssociationDetail.stateDesc));
				 $('#receipientAssociationDetails-districtDesc').text(getValue(receipientAssociationDetail.districtDesc));
				 $('#receipientAssociationDetails-pincode').text(getValue(receipientAssociationDetail.pincode));
				 $('#receipientAssociationDetails-associationPhoneNumber').text(getValue(receipientAssociationDetail.associationPhoneNumber));
				 $('#receipientAssociationDetails-email').text(getValue(receipientAssociationDetail.email));
				 $('#receipientAssociationDetails-chiefFunctionaryPhoneNumber').text(getValue(receipientAssociationDetail.chiefFunctionaryPhoneNumber));
				 $('#receipientAssociationDetails-chiefFunctionaryMobile').text(getValue(applicationDetails.chiefFunctionaryMobile));
				 $('#receipientAssociationDetails-panNumber').text(getValue(receipientAssociationDetail.panNumber));
	             $('#receipientAssociationDetails-registeredUnderAct').text(getValue(receipientAssociationDetail.registeredUnderActDesc));
				 $('#receipientAssociationDetails-registrationNumber').text(getValue(receipientAssociationDetail.registrationNumber));
				 $('#receipientAssociationDetails-placeOfRegistration').text(getValue(receipientAssociationDetail.placeOfRegistration));
				  $('#receipientAssociationDetails-dateOfRegistration').text(getValue(receipientAssociationDetail.dateOfRegistration));
				 $('#amountToBeTransferred').text(getValue(applicationDetails.amountToBeTransferred));
				 $('#purposeDesc').text(getValue(applicationDetails.purposeDesc));
				 
				 //Bank Details
				//alert(JSON.stringify(applicationDetails.receipientAssociationDetails));
				 
				var bankaccount=applicationDetails.receipientAssociationDetails.receipientBankDetails;
				if(bankaccount != null) {
				     $('#accountNumber').text(getValue(bankaccount.accountNumber));
				     $('#bankName').text(getValue(bankaccount.bankName));
					 $('#bank-detail-address').text(getValue(bankaccount.address));
					 //alert(bankaccount.town);
				     $('#bank-detail-town').text(getValue(bankaccount.town));
				     $('#stateDesc').text(getValue(bankaccount.stateDesc)); 
				     $('#bank-detail-districtDesc').text(getValue(bankaccount.districtDesc));
	                 $('#bank-detail-pincode').text(getValue(bankaccount.pincode)); 
				     $('#ifscCode').text(getValue(bankaccount.ifscCode));
				     
					 //Utilasition Bank Detail Existing
					    $("#fc-utilization-bank-accounts-table").html('');
						$("#fc-utilization-bank-accounts-table").initLocalgrid({
							columndetails : [ {
								title : 'Account No',
								name : 'accountNumber'
							}, {
								title : 'Name Of the Bank',
								name : 'bankName'
							}, {
								title : 'Branch Address',
								name : 'address'
							},
							{
								title : 'Town/City',
								name : 'town'
							}, {
								title : 'State',
								name : 'stateDesc'
							}, {
								title : 'District',
								name : 'districtDesc'
							},
							{
								title : 'Pin Code',
								name : 'pincode'
							}, {
								title : 'IFSC Code',
								name : 'ifscCode'
							}],
							
						});
						$("#fc-utilization-bank-accounts-table").addListToLocalgrid(applicationDetails.utilizationBankDetails);   
				     
				     
				     
				}
		}
	});
	
	
}

function populateChangeOfCommitteeMembersDetailsForm(applicationDetails,smsDetails,mailDetails){
	//change-of-details
	//console.log(JSON.stringify(applicationDetails));
	$('#application-details').load('resources/application-details.html #change-of-committee-members-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			   //info bar
			 $('#application-track-info').show();
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			
				var applicantDetail= applicationDetails.associationDetails;
				//assocation details
				 $('#chiefFunctionaryName').text(getValue(applicantDetail.chiefFunctionaryName));
				// $('#fcraRegistrationNumber').text(getValue(applicantDetail.fcraRegistrationNumber));
			     $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(applicantDetail.fcraRegistrationNumber)+'\");>'+getValue(applicantDetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(applicantDetail.fcraRegistrationDate));
				 $('#associationPhoneNumber').text(getValue(applicantDetail.associationPhoneNumber));
		         $('#email').text(getValue(applicantDetail.email));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(applicantDetail.chiefFunctionaryPhoneNumber));
				 $('#chiefFunctionaryMobile').text(getValue(applicantDetail.chiefFunctionaryMobile));
				 //Details which may be Update
				 //EXISTING
				// changeOfNameOrAim
				   if (applicationDetails.changeOfNameOrAim == null || applicationDetails.changeOfNameOrAim == undefined)$('#changeOfNameOrAim').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNameOrAim=='Y'){
				    		
				    		$('#changeOfNameOrAim').text('Yes');  
				    		 $('#new-associationName-label').show();
							 $('#new-associationAim-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfNameOrAim').text('No');
				    
				    		 $('#new-associationName-label').hide();
				             $('#new-associationAim-label').hide();
							 
				    	}
				    	}
				 //change of Association Nature
				   if (applicationDetails.changeOfNature == null || applicationDetails.changeOfNature == undefined)$('#changeOfNature').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNature=='Y'){
				    		
				    		 $('#changeOfNature').text('Yes');  
				    		 $('#associationNature-label').show();
				    		 $('#new-associationNature-label').show();
							
				    	} else {
				    		$('#changeOfNature').text('No');
				    		$('#associationNature-label').hide();
				            $('#new-associationNature-label').hide();
				            
				           
				           }
				    	}
				 
				   if(applicantDetail.nature=='1'){
					   $('#associationReligion-label').css("visibility", 'visible'); 
					   if(applicantDetail.religion=='6'){
						   var religionString= applicantDetail.religionDesc+' ('+applicantDetail.religionOther+')';
						   $('#associationReligion').text(getValue(religionString)); //Existing 
						 }
						else {
						   $('#associationReligion').text(getValue(applicantDetail.religionDesc)); //Existing  
						 }
				   }
				 if(applicationDetails.newNature=='1'){	
					 $('#new-associationReligion-label').css("visibility", 'visible');
					 if(applicationDetails.newReligion=='6'){
						 var newReligionString= applicationDetails.newReligionDesc+' ('+applicationDetails.newReligionOther+')'; 
						$('#change-associationReligion').text(getValue(newReligionString)); //Updated
					 }
					 else {
						 $('#change-associationReligion').text(getValue(applicationDetails.newReligionDesc)); //Updated
					 }  
					   
				   }
				/* else {  
					 if(applicantDetail.nature=='1') {$('#dummyForShift-label').hide();} else {
				     $('#dummyForShift-label').show()};
				 }*/
				 
			                                                           
				 $('#associationNature').text(getValue(applicantDetail.natureDesc));//association Nature 
				   //END  ++++++++change of Association Nature++++++++ END 
				    if (applicationDetails.changeOfAddress == null || applicationDetails.changeOfAddress == undefined)$('#changeOfAddress').text('-'); 
				    else {
				    	if(applicationDetails.changeOfAddress=='Y'){
				    		
				    		$('#changeOfAddress').text('Yes');  
				    	     
				    		 $('#new-address-label').show();
							 $('#new-town-label').show();
							 $('#new-stateDesc-label').show();
							 $('#new-district-label').show();
							 $('#new-pincode-label').show();
				    		
				    		
				    	} else {
				    		$('#changeOfAddress').text('No');
				     
							 $('#new-address-label').hide();
							 $('#new-town-label').hide();
							 $('#new-stateDesc-label').hide();
							 $('#new-district-label').hide();
							 $('#new-pincode-label').hide();
				    	}
				    	}
				
				 $('#associationName').text(getValue(applicantDetail.associationName));
				 $('#associationAim').text(getValue(applicantDetail.associationAim));
				 $('#address').text(getValue(applicantDetail.address));
				 $('#town').text(getValue(applicantDetail.town));
				 $('#stateDesc').text(getValue(applicantDetail.stateDesc));
				 $('#districtDesc').text(getValue(applicantDetail.districtDesc));
				 $('#pincode').text(getValue(applicantDetail.pincode));
				 
				 //UPDATED
			     $('#new-associationName').text(getValue(applicationDetails.newAssociationName));
				 $('#new-associationAim').text(getValue(applicationDetails.newAssociationAim));
				 //new Association Nature 
				 $('#change-associationNature').text(getValue(applicationDetails.newNatureDesc));
				 
				 $('#new-address').text(getValue(applicationDetails.newAddress));
				 $('#new-town').text(getValue(applicationDetails.newTown));
				 $('#new-stateDesc').text(getValue(applicationDetails.newStateDesc));
				 $('#new-district').text(getValue(applicationDetails.newDistrictDesc));
				 $('#new-pincode').text(getValue(applicationDetails.newPincode));
				 
				 //designated bank/ branch/ bank account number for receipt and utilization of foreign contribution:
				// changeOfReceipientBank
				   if (applicationDetails.changeOfReceipientBank == null || applicationDetails.changeOfReceipientBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfReceipientBank=='Y'){
				    		$('#changeOfReceipientBank').text('Yes');  
							$('#new-accountNumber-label').show();
							 $('#new-bankName-label').show();
							 $('#new-branch-address-label').show();
							 $('#new-bank-town-label').show();
							 $('#new-bank-stateDesc-label').show();
							 $('#new-bank-districtDesc-label').show();
							 $('#new-bank-pincode-label').show();
							 $('#new-bank-ifscCode-label').show();
				    	} else {
				    		$('#changeOfReceipientBank').text('No');
				    
				    		 $('#new-accountNumber-label').hide();
							 $('#new-bankName-label').hide();
							 $('#new-branch-address-label').hide();
							 $('#new-bank-town-label').hide();
							 $('#new-bank-stateDesc-label').hide();
							 $('#new-bank-districtDesc-label').hide();
							 $('#new-bank-pincode-label').hide();
							 $('#new-bank-ifscCode-label').hide();
							 
				    	}
				    	}
				   var newrecpBankDetail=applicationDetails.newReceipientBankDetails;
				 //  alert(newrecpBankDetail.accountNumber);
				//EXISTING
				  
				 var recBankDetail=applicantDetail.receipientBankDetails;
				 if(recBankDetail!=null){				 
					 $('#accountNumber').text(getValue(recBankDetail.accountNumber));
					 $('#bankName').text(getValue(recBankDetail.bankName));
					 $('#branch-address').text(getValue(recBankDetail.address));
					 $('#bank-town').text(getValue(recBankDetail.town));
					 $('#bank-stateDesc').text(getValue(recBankDetail.stateDesc));
					 $('#bank-districtDesc').text(getValue(recBankDetail.districtDesc));
					 $('#bank-pincode').text(getValue(recBankDetail.pincode));
					 $('#bank-ifscCode').text(getValue(recBankDetail.ifscCode));
				 }
/*				
				 //UPDATED Detail
				 //$('#new-accountNumber').text(getValue(newrecpBankDetail.accountNumber));
				 $('#new-bankName').text(getValue(newrecpBankDetail.bankName));
				 $('#new-branch-address').text(getValue(newrecpBankDetail.address));
				 $('#new-bank-town').text(getValue(newrecpBankDetail.town));
				 $('#new-bank-stateDesc').text(getValue(newrecpBankDetail.stateDesc));
				 $('#new-bank-districtDesc').text(getValue(newrecpBankDetail.districtDesc));
				 $('#new-bank-pincode').text(getValue(newrecpBankDetail.pincode));
				 $('#new-bank-ifscCode').text(getValue(newrecpBankDetail.ifscCode));
*/				 
				 
				  //EXISTING UTILASITION Bank DETAIL
				 
				// changeOfUtilizationBank
				   if (applicationDetails.changeOfUtilizationBank == null || applicationDetails.changeOfUtilizationBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfUtilizationBank=='Y'){
				    		
				    		$('#changeOfUtilizationBank').text('Yes');  
							
				    		$('#updated-utilization-bank-accounts-table-label').show();

                      } else {
				    		$('#changeOfUtilizationBank').text('No');
				    
				    		 $('#updated-utilization-bank-accounts-table-label').hide();
					
				    	}
				    	}
				   
			
					 $("#existing-utilization-bank-accounts-table").html('');
						$("#existing-utilization-bank-accounts-table").initLocalgrid({
							columndetails : [ {
								title : 'Account No',
								name : 'accountNumber'
							}, {
								title : 'Name Of the Bank',
								name : 'bankName'
							}, {
								title : 'Branch Address',
								name : 'address'
							},
							{
								title : 'Town/City',
								name : 'town'
							}, {
								title : 'State',
								name : 'stateDesc'
							}, {
								title : 'District',
								name : 'districtDesc'
							},
							{
								title : 'Pin Code',
								name : 'pincode'
							}, {
								title : 'IFSC Code',
								name : 'ifscCode'
							}],
							
						});
						$("#existing-utilization-bank-accounts-table").addListToLocalgrid(applicantDetail.utilizationBankDetails);
				   
				   
				    $("#updated-utilization-bank-accounts-table").html('');
					$("#updated-utilization-bank-accounts-table").initLocalgrid({
						columndetails : [ {
							title : 'Account No',
							name : 'accountNumber'
						}, {
							title : 'Name Of the Bank',
							name : 'bankName'
						}, {
							title : 'Branch Address',
							name : 'address'
						},
						{
							title : 'Town/City',
							name : 'town'
						}, {
							title : 'State',
							name : 'stateDesc'
						}, {
							title : 'District',
							name : 'districtDesc'
						},
						{
							title : 'Pin Code',
							name : 'pincode'
						}, {
							title : 'IFSC Code',
							name : 'ifscCode'
						}],
						
					});
					$("#updated-utilization-bank-accounts-table").addListToLocalgrid(applicationDetails.newUtilizationBankDetails);
					
					

				 //association-member-table
					   if (applicationDetails.changeOfCommitteeMembers == null || applicationDetails.changeOfCommitteeMembers == undefined)$('#changeOfReceipientBank').text('-'); 
					    else {
					    	if(applicationDetails.changeOfCommitteeMembers=='Y'){
					    		
					    		$('#changeOfCommitteeMembers').text('Yes');  
								
					    	   $('#updated-association-member-table-lebel').show();

	                      } else {
					    		$('#changeOfCommitteeMembers').text('No');
					    
					    		$('#updated-association-member-table-lebel').hide();
						
					    	}
					    	}
				    $("#existing-association-member-table").html('');
					$("#existing-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#existing-association-member-table").addListToLocalgrid(applicantDetail.committeeMembers); 
				 
				   
				    $("#updated-association-member-table").html('');
					$("#updated-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#updated-association-member-table").addListToLocalgrid(applicationDetails.newCommitteeMemberDetails); 
					
					//UPDATED COMMITEE MEMBERS

				 
		}
	});
}


function populateChangeOfOpeningUtilisationBankActDetailsForm(applicationDetails,smsDetails,mailDetails){
	//change-of-details
	//alert(JSON.stringify(applicationDetails));
	$('#application-details').load('resources/application-details.html #change-of-opening-utilization-accounts-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			   //info bar
			 $('#application-track-info').show();
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			
				var applicantDetail= applicationDetails.associationDetails;
				//assocation details
				 $('#chiefFunctionaryName').text(getValue(applicantDetail.chiefFunctionaryName));
				// $('#fcraRegistrationNumber').text(getValue(applicantDetail.fcraRegistrationNumber));
			     $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(applicantDetail.fcraRegistrationNumber)+'\");>'+getValue(applicantDetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(applicantDetail.fcraRegistrationDate));
				 $('#associationPhoneNumber').text(getValue(applicantDetail.associationPhoneNumber));
		         $('#email').text(getValue(applicantDetail.email));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(applicantDetail.chiefFunctionaryPhoneNumber));
				 $('#chiefFunctionaryMobile').text(getValue(applicantDetail.chiefFunctionaryMobile));
				 //Details which may be Update
				 //EXISTING
				// changeOfNameOrAim
				   if (applicationDetails.changeOfNameOrAim == null || applicationDetails.changeOfNameOrAim == undefined)$('#changeOfNameOrAim').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNameOrAim=='Y'){
				    		
				    		$('#changeOfNameOrAim').text('Yes');  
				    		 $('#new-associationName-label').show();
							 $('#new-associationAim-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfNameOrAim').text('No');
				    
				    		 $('#new-associationName-label').hide();
				             $('#new-associationAim-label').hide();
							 
				    	}
				    	}
				 //change of Association Nature
				   if (applicationDetails.changeOfNature == null || applicationDetails.changeOfNature == undefined)$('#changeOfNature').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNature=='Y'){
				    		
				    		 $('#changeOfNature').text('Yes');  
				    		 $('#associationNature-label').show();
				    		 $('#new-associationNature-label').show();
							
				    	} else {
				    		$('#changeOfNature').text('No');
				    		$('#associationNature-label').hide();
				            $('#new-associationNature-label').hide();
				            
				           
				           }
				    	}
				 
				   if(applicantDetail.nature=='1'){
					   $('#associationReligion-label').css("visibility", 'visible'); 
					   if(applicantDetail.religion=='6'){
						   var religionString= applicantDetail.religionDesc+' ('+applicantDetail.religionOther+')';
						   $('#associationReligion').text(getValue(religionString)); //Existing 
						 }
						else {
						   $('#associationReligion').text(getValue(applicantDetail.religionDesc)); //Existing  
						 }
				   }
				 if(applicationDetails.newNature=='1'){	
					 $('#new-associationReligion-label').css("visibility", 'visible');
					 if(applicationDetails.newReligion=='6'){
						 var newReligionString= applicationDetails.newReligionDesc+' ('+applicationDetails.newReligionOther+')'; 
						$('#change-associationReligion').text(getValue(newReligionString)); //Updated
					 }
					 else {
						 $('#change-associationReligion').text(getValue(applicationDetails.newReligionDesc)); //Updated
					 }  
					   
				   }
				/* else {  
					 if(applicantDetail.nature=='1') {$('#dummyForShift-label').hide();} else {
				     $('#dummyForShift-label').show()};
				 }*/
				 
			                                                           
				 $('#associationNature').text(getValue(applicantDetail.natureDesc));//association Nature 
				   //END  ++++++++change of Association Nature++++++++ END 
				    if (applicationDetails.changeOfAddress == null || applicationDetails.changeOfAddress == undefined)$('#changeOfAddress').text('-'); 
				    else {
				    	if(applicationDetails.changeOfAddress=='Y'){
				    		
				    		$('#changeOfAddress').text('Yes');  
				    	     
				    		 $('#new-address-label').show();
							 $('#new-town-label').show();
							 $('#new-stateDesc-label').show();
							 $('#new-district-label').show();
							 $('#new-pincode-label').show();
				    		
				    		
				    	} else {
				    		$('#changeOfAddress').text('No');
				     
							 $('#new-address-label').hide();
							 $('#new-town-label').hide();
							 $('#new-stateDesc-label').hide();
							 $('#new-district-label').hide();
							 $('#new-pincode-label').hide();
				    	}
				    	}
				
				 $('#associationName').text(getValue(applicantDetail.associationName));
				 $('#associationAim').text(getValue(applicantDetail.associationAim));
				 $('#address').text(getValue(applicantDetail.address));
				 $('#town').text(getValue(applicantDetail.town));
				 $('#stateDesc').text(getValue(applicantDetail.stateDesc));
				 $('#districtDesc').text(getValue(applicantDetail.districtDesc));
				 $('#pincode').text(getValue(applicantDetail.pincode));
				 
				 //UPDATED
			     $('#new-associationName').text(getValue(applicationDetails.newAssociationName));
				 $('#new-associationAim').text(getValue(applicationDetails.newAssociationAim));
				 //new Association Nature 
				 $('#change-associationNature').text(getValue(applicationDetails.newNatureDesc));
				 
				 $('#new-address').text(getValue(applicationDetails.newAddress));
				 $('#new-town').text(getValue(applicationDetails.newTown));
				 $('#new-stateDesc').text(getValue(applicationDetails.newStateDesc));
				 $('#new-district').text(getValue(applicationDetails.newDistrictDesc));
				 $('#new-pincode').text(getValue(applicationDetails.newPincode));
				 
				 //designated bank/ branch/ bank account number for receipt and utilization of foreign contribution:
				// changeOfReceipientBank
				   if (applicationDetails.changeOfReceipientBank == null || applicationDetails.changeOfReceipientBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfReceipientBank=='Y'){
				    		$('#changeOfReceipientBank').text('Yes');  
							$('#new-accountNumber-label').show();
							 $('#new-bankName-label').show();
							 $('#new-branch-address-label').show();
							 $('#new-bank-town-label').show();
							 $('#new-bank-stateDesc-label').show();
							 $('#new-bank-districtDesc-label').show();
							 $('#new-bank-pincode-label').show();
							 $('#new-bank-ifscCode-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfReceipientBank').text('No');
				    
				    		 $('#new-accountNumber-label').hide();
							 $('#new-bankName-label').hide();
							 $('#new-branch-address-label').hide();
							 $('#new-bank-town-label').hide();
							 $('#new-bank-stateDesc-label').hide();
							 $('#new-bank-districtDesc-label').hide();
							 $('#new-bank-pincode-label').hide();
							 $('#new-bank-ifscCode-label').hide();
							 
				    	}
				    	}
				   var newrecpBankDetail=applicationDetails.newReceipientBankDetails;
				 //  alert(newrecpBankDetail.accountNumber);
				//EXISTING
				  
				 var recBankDetail=applicantDetail.receipientBankDetails;
				 if(recBankDetail!=null){				 
					 $('#accountNumber').text(getValue(recBankDetail.accountNumber));
					 $('#bankName').text(getValue(recBankDetail.bankName));
					 $('#branch-address').text(getValue(recBankDetail.address));
					 $('#bank-town').text(getValue(recBankDetail.town));
					 $('#bank-stateDesc').text(getValue(recBankDetail.stateDesc));
					 $('#bank-districtDesc').text(getValue(recBankDetail.districtDesc));
					 $('#bank-pincode').text(getValue(recBankDetail.pincode));
					 $('#bank-ifscCode').text(getValue(recBankDetail.ifscCode));
				 }
				
				 //UPDATED Detail
				 $('#new-accountNumber').text(getValue(newrecpBankDetail.accountNumber));
				 $('#new-bankName').text(getValue(newrecpBankDetail.bankName));
				 $('#new-branch-address').text(getValue(newrecpBankDetail.address));
				 $('#new-bank-town').text(getValue(newrecpBankDetail.town));
				 $('#new-bank-stateDesc').text(getValue(newrecpBankDetail.stateDesc));
				 $('#new-bank-districtDesc').text(getValue(newrecpBankDetail.districtDesc));
				 $('#new-bank-pincode').text(getValue(newrecpBankDetail.pincode));
				 $('#new-bank-ifscCode').text(getValue(newrecpBankDetail.ifscCode));
				 
				 
				  //EXISTING UTILASITION Bank DETAIL
				 
				// changeOfUtilizationBank
				   if (applicationDetails.changeOfUtilizationBank == null || applicationDetails.changeOfUtilizationBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfUtilizationBank=='Y'){
				    		
				    		$('#changeOfUtilizationBank').text('Yes');  
							
				    		$('#updated-utilization-bank-accounts-table-label').show();

                      } else {
				    		$('#changeOfUtilizationBank').text('No');
				    
				    		 $('#updated-utilization-bank-accounts-table-label').hide();
					
				    	}
				    	}
				   
			
					 $("#existing-utilization-bank-accounts-table").html('');
						$("#existing-utilization-bank-accounts-table").initLocalgrid({
							columndetails : [ {
								title : 'Account No',
								name : 'accountNumber'
							}, {
								title : 'Name Of the Bank',
								name : 'bankName'
							}, {
								title : 'Branch Address',
								name : 'address'
							},
							{
								title : 'Town/City',
								name : 'town'
							}, {
								title : 'State',
								name : 'stateDesc'
							}, {
								title : 'District',
								name : 'districtDesc'
							},
							{
								title : 'Pin Code',
								name : 'pincode'
							}, {
								title : 'IFSC Code',
								name : 'ifscCode'
							}],
							
						});
						$("#existing-utilization-bank-accounts-table").addListToLocalgrid(applicantDetail.utilizationBankDetails);
				   
				   
				    $("#updated-utilization-bank-accounts-table").html('');
					$("#updated-utilization-bank-accounts-table").initLocalgrid({
						columndetails : [ {
							title : 'Account No',
							name : 'accountNumber'
						}, {
							title : 'Name Of the Bank',
							name : 'bankName'
						}, {
							title : 'Branch Address',
							name : 'address'
						},
						{
							title : 'Town/City',
							name : 'town'
						}, {
							title : 'State',
							name : 'stateDesc'
						}, {
							title : 'District',
							name : 'districtDesc'
						},
						{
							title : 'Pin Code',
							name : 'pincode'
						}, {
							title : 'IFSC Code',
							name : 'ifscCode'
						}],
						
					});
					$("#updated-utilization-bank-accounts-table").addListToLocalgrid(applicationDetails.newUtilizationBankDetails);
					
					

				 //association-member-table
					   if (applicationDetails.changeOfCommitteeMembers == null || applicationDetails.changeOfCommitteeMembers == undefined)$('#changeOfReceipientBank').text('-'); 
					    else {
					    	if(applicationDetails.changeOfCommitteeMembers=='Y'){
					    		
					    		$('#changeOfCommitteeMembers').text('Yes');  
								
					    	   $('#updated-association-member-table-lebel').show();

	                      } else {
					    		$('#changeOfCommitteeMembers').text('No');
					    
					    		$('#updated-association-member-table-lebel').hide();
						
					    	}
					    	}
				    $("#existing-association-member-table").html('');
					$("#existing-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#existing-association-member-table").addListToLocalgrid(applicantDetail.committeeMembers); 
				 
				   
				    $("#updated-association-member-table").html('');
					$("#updated-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#updated-association-member-table").addListToLocalgrid(applicationDetails.newCommitteeMemberDetails); 
					
					//UPDATED COMMITEE MEMBERS

				 
		}
	});
}


function populateChangeOfFcReceiptCumUtilisationBankDetailsForm(applicationDetails,smsDetails,mailDetails) {
	//change-of-details
	//alert(JSON.stringify(applicationDetails));
	$('#application-details').load('resources/application-details.html #change-of-fc-cum-utl-bank-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			   //info bar
			 $('#application-track-info').show();
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			
				var applicantDetail= applicationDetails.associationDetails;
				//assocation details
				 $('#chiefFunctionaryName').text(getValue(applicantDetail.chiefFunctionaryName));
				// $('#fcraRegistrationNumber').text(getValue(applicantDetail.fcraRegistrationNumber));
			     $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(applicantDetail.fcraRegistrationNumber)+'\");>'+getValue(applicantDetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(applicantDetail.fcraRegistrationDate));
				 $('#associationPhoneNumber').text(getValue(applicantDetail.associationPhoneNumber));
		         $('#email').text(getValue(applicantDetail.email));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(applicantDetail.chiefFunctionaryPhoneNumber));
				 $('#chiefFunctionaryMobile').text(getValue(applicantDetail.chiefFunctionaryMobile));
				 //Details which may be Update
				 //EXISTING
				// changeOfNameOrAim

				 
				 //designated bank/ branch/ bank account number for receipt and utilization of foreign contribution:
				// changeOfReceipientBank
				   if (applicationDetails.changeOfReceipientBank == null || applicationDetails.changeOfReceipientBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfReceipientBank=='Y'){
				    		$('#changeOfReceipientBank').text('Yes');  
							$('#new-accountNumber-label').show();
							 $('#new-bankName-label').show();
							 $('#new-Bank-label').show();
							 $('#new-branch-address-label').show();
							 $('#new-bank-town-label').show();
							 $('#new-bank-stateDesc-label').show();
							 $('#new-bank-districtDesc-label').show();
							 $('#new-bank-pincode-label').show();
							 $('#new-bank-ifscCode-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfReceipientBank').text('No');
				    
				    		 $('#new-accountNumber-label').hide();
							 $('#new-bankName-label').hide();
							 $('#new-branch-address-label').hide();
							 $('#new-bank-town-label').hide();
							 $('#new-bank-stateDesc-label').hide();
							 $('#new-bank-districtDesc-label').hide();
							 $('#new-bank-pincode-label').hide();
							 $('#new-bank-ifscCode-label').hide();
							 
				    	}
				    	}
				   var newrecpBankDetail=applicationDetails.newReceipientBankDetails;
				 //  alert(newrecpBankDetail.accountNumber);
				//EXISTING
				  
				 var recBankDetail=applicantDetail.receipientBankDetails;
				 if(recBankDetail!=null){				 
					 $('#accountNumber').text(getValue(recBankDetail.accountNumber));
					 $('#bankName').text(getValue(recBankDetail.bankName));
					 $('#branch-address').text(getValue(recBankDetail.address));
					 $('#bank-town').text(getValue(recBankDetail.town));
					 $('#bank-stateDesc').text(getValue(recBankDetail.stateDesc));
					 $('#bank-districtDesc').text(getValue(recBankDetail.districtDesc));
					 $('#bank-pincode').text(getValue(recBankDetail.pincode));
					 $('#bank-ifscCode').text(getValue(recBankDetail.ifscCode));
				 }
				
				 //UPDATED Detail
				 $('#new-accountNumber').text(getValue(newrecpBankDetail.accountNumber));
				 $('#new-bankName').text(getValue(newrecpBankDetail.bankName));
				 $('#new-branch-address').text(getValue(newrecpBankDetail.address));
				 $('#new-bank-town').text(getValue(newrecpBankDetail.town));
				 $('#new-bank-stateDesc').text(getValue(newrecpBankDetail.stateDesc));
				 $('#new-bank-districtDesc').text(getValue(newrecpBankDetail.districtDesc));
				 $('#new-bank-pincode').text(getValue(newrecpBankDetail.pincode));
				 $('#new-bank-ifscCode').text(getValue(newrecpBankDetail.ifscCode));
				 
				 
				  //EXISTING UTILASITION Bank DETAIL
				 
				// changeOfUtilizationBank
				   if (applicationDetails.changeOfUtilizationBank == null || applicationDetails.changeOfUtilizationBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfUtilizationBank=='Y'){
				    		
				    		$('#changeOfUtilizationBank').text('Yes');  
							
				    		$('#updated-utilization-bank-accounts-table-label').show();

                      } else {
				    		$('#changeOfUtilizationBank').text('No');
				    
				    		 $('#updated-utilization-bank-accounts-table-label').hide();
					
				    	}
				    	}
				   
			
					 $("#existing-utilization-bank-accounts-table").html('');
						$("#existing-utilization-bank-accounts-table").initLocalgrid({
							columndetails : [ {
								title : 'Account No',
								name : 'accountNumber'
							}, {
								title : 'Name Of the Bank',
								name : 'bankName'
							}, {
								title : 'Branch Address',
								name : 'address'
							},
							{
								title : 'Town/City',
								name : 'town'
							}, {
								title : 'State',
								name : 'stateDesc'
							}, {
								title : 'District',
								name : 'districtDesc'
							},
							{
								title : 'Pin Code',
								name : 'pincode'
							}, {
								title : 'IFSC Code',
								name : 'ifscCode'
							}],
							
						});
						$("#existing-utilization-bank-accounts-table").addListToLocalgrid(applicantDetail.utilizationBankDetails);
				   
				   
				    $("#updated-utilization-bank-accounts-table").html('');
					$("#updated-utilization-bank-accounts-table").initLocalgrid({
						columndetails : [ {
							title : 'Account No',
							name : 'accountNumber'
						}, {
							title : 'Name Of the Bank',
							name : 'bankName'
						}, {
							title : 'Branch Address',
							name : 'address'
						},
						{
							title : 'Town/City',
							name : 'town'
						}, {
							title : 'State',
							name : 'stateDesc'
						}, {
							title : 'District',
							name : 'districtDesc'
						},
						{
							title : 'Pin Code',
							name : 'pincode'
						}, {
							title : 'IFSC Code',
							name : 'ifscCode'
						}],
						
					});
					$("#updated-utilization-bank-accounts-table").addListToLocalgrid(applicationDetails.newUtilizationBankDetails);
					
					

				 //association-member-table
					   if (applicationDetails.changeOfCommitteeMembers == null || applicationDetails.changeOfCommitteeMembers == undefined)$('#changeOfReceipientBank').text('-'); 
					    else {
					    	if(applicationDetails.changeOfCommitteeMembers=='Y'){
					    		
					    		$('#changeOfCommitteeMembers').text('Yes');  
								
					    	   $('#updated-association-member-table-lebel').show();

	                      } else {
					    		$('#changeOfCommitteeMembers').text('No');
					    
					    		$('#updated-association-member-table-lebel').hide();
						
					    	}
					    	}
				    $("#existing-association-member-table").html('');
					$("#existing-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#existing-association-member-table").addListToLocalgrid(applicantDetail.committeeMembers); 
				 
				   
				    $("#updated-association-member-table").html('');
					$("#updated-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#updated-association-member-table").addListToLocalgrid(applicationDetails.newCommitteeMemberDetails); 
					
					//UPDATED COMMITEE MEMBERS

				 
		}
	});
}


function populateChangeOfNamrAndAddressDetailsForm(applicationDetails,smsDetails,mailDetails) {
	//change-of-details
	alert(JSON.stringify(applicationDetails));
	$('#application-details').load('resources/application-details.html #change-of-name-address-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			   //info bar
			 $('#application-track-info').show();
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			
				var applicantDetail= applicationDetails.associationDetails;
				//assocation details
				 $('#chiefFunctionaryName').text(getValue(applicantDetail.chiefFunctionaryName));
				// $('#fcraRegistrationNumber').text(getValue(applicantDetail.fcraRegistrationNumber));
			     $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(applicantDetail.fcraRegistrationNumber)+'\");>'+getValue(applicantDetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(applicantDetail.fcraRegistrationDate));
				 $('#associationPhoneNumber').text(getValue(applicantDetail.associationPhoneNumber));
		         $('#email').text(getValue(applicantDetail.email));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(applicantDetail.chiefFunctionaryPhoneNumber));
				 $('#chiefFunctionaryMobile').text(getValue(applicantDetail.chiefFunctionaryMobile));
				 //Details which may be Update
				 //EXISTING
				// changeOfNameOrAim
				   if (applicationDetails.changeOfNameOrAim == null || applicationDetails.changeOfNameOrAim == undefined)$('#changeOfNameOrAim').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNameOrAim=='Y'){
				    		
				    		$('#changeOfNameOrAim').text('Yes');  
				    		 $('#new-associationName-label').show();
							 $('#new-associationAim-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfNameOrAim').text('No');
				    
				    		 $('#new-associationName-label').hide();
				             $('#new-associationAim-label').hide();
							 
				    	}
				    	}
				 //change of Association Nature
				   if (applicationDetails.changeOfNature == null || applicationDetails.changeOfNature == undefined)$('#changeOfNature').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNature=='Y'){
				    		
				    		 $('#changeOfNature').text('Yes');  
				    		 $('#associationNature-label').hide();
				    		 $('#new-associationNature-label').hide();
							
				    	} else {
				    		$('#changeOfNature').text('No');
				    		$('#associationNature-label').hide();
				            $('#new-associationNature-label').hide();
				            
				           
				           }
				    	}
				 
				   if(applicantDetail.nature=='1'){
					   $('#associationReligion-label').css("visibility", 'visible'); 
					   if(applicantDetail.religion=='6'){
						   var religionString= applicantDetail.religionDesc+' ('+applicantDetail.religionOther+')';
						   $('#associationReligion').text(getValue(religionString)); //Existing 
						 }
						else {
						   $('#associationReligion').text(getValue(applicantDetail.religionDesc)); //Existing  
						 }
				   }
				 if(applicationDetails.newNature=='1'){	
					 $('#new-associationReligion-label').css("visibility", 'visible');
					 if(applicationDetails.newReligion=='6'){
						 var newReligionString= applicationDetails.newReligionDesc+' ('+applicationDetails.newReligionOther+')'; 
						$('#change-associationReligion').text(getValue(newReligionString)); //Updated
					 }
					 else {
						 $('#change-associationReligion').text(getValue(applicationDetails.newReligionDesc)); //Updated
					 }  
					   
				   }
				/* else {  
					 if(applicantDetail.nature=='1') {$('#dummyForShift-label').hide();} else {
				     $('#dummyForShift-label').show()};
				 }*/
				 
			                                                           
				 $('#associationNature').text(getValue(applicantDetail.natureDesc));//association Nature 
				   //END  ++++++++change of Association Nature++++++++ END 
				    if (applicationDetails.changeOfAddress == null || applicationDetails.changeOfAddress == undefined)$('#changeOfAddress').text('-'); 
				    else {
				    	if(applicationDetails.changeOfAddress=='Y'){
				    		
				    		$('assoNature').hide();
				    		$('#changeOfAddress').text('Yes');  
				    	     
				    		 $('#new-address-label').show();
							 $('#new-town-label').show();
							 $('#new-stateDesc-label').show();
							 $('#new-district-label').show();
							 $('#new-pincode-label').show();
				    		
				    		
				    	} else {
				    		$('#changeOfAddress').text('No');
				     
							 $('#new-address-label').hide();
							 $('#new-town-label').hide();
							 $('#new-stateDesc-label').hide();
							 $('#new-district-label').hide();
							 $('#new-pincode-label').hide();
				    	}
				    	}
				
				 $('#associationName').text(getValue(applicantDetail.associationName));
				 $('#associationAim').text(getValue(applicantDetail.associationAim));
				 $('#address').text(getValue(applicantDetail.address));
				 $('#town').text(getValue(applicantDetail.town));
				 $('#stateDesc').text(getValue(applicantDetail.stateDesc));
				 $('#districtDesc').text(getValue(applicantDetail.districtDesc));
				 $('#pincode').text(getValue(applicantDetail.pincode));
				 
				 //UPDATED
			     $('#new-associationName').text(getValue(applicationDetails.newAssociationName));
				 $('#new-associationAim').text(getValue(applicationDetails.newAssociationAim));
				 //new Association Nature 
				 $('#change-associationNature').text(getValue(applicationDetails.newNatureDesc));
				 
				 $('#new-address').text(getValue(applicationDetails.newAddress));
				 $('#new-town').text(getValue(applicationDetails.newTown));
				 $('#new-stateDesc').text(getValue(applicationDetails.newStateDesc));
				 $('#new-district').text(getValue(applicationDetails.newDistrictDesc));
				 $('#new-pincode').text(getValue(applicationDetails.newPincode));
				 
				 //designated bank/ branch/ bank account number for receipt and utilization of foreign contribution:
				// changeOfReceipientBank
				   if (applicationDetails.changeOfReceipientBank == null || applicationDetails.changeOfReceipientBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfReceipientBank=='Y'){
				    		
				    		$('#changeOfReceipientBank').text('Yes');  
							
				    		$('#new-accountNumber-label').show();
							 $('#new-bankName-label').show();
							 $('#new-branch-address-label').show();
							 $('#new-bank-town-label').show();
							 $('#new-bank-stateDesc-label').show();
							 $('#new-bank-districtDesc-label').show();
							 $('#new-bank-pincode-label').show();
							 $('#new-bank-ifscCode-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfReceipientBank').text('No');
				    
				    		 $('#new-accountNumber-label').hide();
							 $('#new-bankName-label').hide();
							 $('#new-branch-address-label').hide();
							 $('#new-bank-town-label').hide();
							 $('#new-bank-stateDesc-label').hide();
							 $('#new-bank-districtDesc-label').hide();
							 $('#new-bank-pincode-label').hide();
							 $('#new-bank-ifscCode-label').hide();
							 
				    	}
				    	}
				   var newrecpBankDetail=applicationDetails.newReceipientBankDetails;
				 //  alert(newrecpBankDetail.accountNumber);
				//EXISTING
				  
				 var recBankDetail=applicantDetail.receipientBankDetails;
				 if(recBankDetail!=null){				 
					 $('#accountNumber').text(getValue(recBankDetail.accountNumber));
					 $('#bankName').text(getValue(recBankDetail.bankName));
					 $('#branch-address').text(getValue(recBankDetail.address));
					 $('#bank-town').text(getValue(recBankDetail.town));
					 $('#bank-stateDesc').text(getValue(recBankDetail.stateDesc));
					 $('#bank-districtDesc').text(getValue(recBankDetail.districtDesc));
					 $('#bank-pincode').text(getValue(recBankDetail.pincode));
					 $('#bank-ifscCode').text(getValue(recBankDetail.ifscCode));
				 }
				
/*				 //UPDATED Detail
				 //$('#new-accountNumber').text(getValue(newrecpBankDetail.accountNumber));
				 $('#new-bankName').text(getValue(newrecpBankDetail.bankName));
				 $('#new-branch-address').text(getValue(newrecpBankDetail.address));
				 $('#new-bank-town').text(getValue(newrecpBankDetail.town));
				 $('#new-bank-stateDesc').text(getValue(newrecpBankDetail.stateDesc));
				 $('#new-bank-districtDesc').text(getValue(newrecpBankDetail.districtDesc));
				 $('#new-bank-pincode').text(getValue(newrecpBankDetail.pincode));
				 $('#new-bank-ifscCode').text(getValue(newrecpBankDetail.ifscCode));
*/				 
				 
				  //EXISTING UTILASITION Bank DETAIL
				 
				// changeOfUtilizationBank
				   if (applicationDetails.changeOfUtilizationBank == null || applicationDetails.changeOfUtilizationBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfUtilizationBank=='Y'){
				    		
				    		$('#changeOfUtilizationBank').text('Yes');  
							
				    		$('#updated-utilization-bank-accounts-table-label').show();

                      } else {
				    		$('#changeOfUtilizationBank').text('No');
				    
				    		 $('#updated-utilization-bank-accounts-table-label').hide();
					
				    	}
				    	}
				   
			
					 $("#existing-utilization-bank-accounts-table").html('');
						$("#existing-utilization-bank-accounts-table").initLocalgrid({
							columndetails : [ {
								title : 'Account No',
								name : 'accountNumber'
							}, {
								title : 'Name Of the Bank',
								name : 'bankName'
							}, {
								title : 'Branch Address',
								name : 'address'
							},
							{
								title : 'Town/City',
								name : 'town'
							}, {
								title : 'State',
								name : 'stateDesc'
							}, {
								title : 'District',
								name : 'districtDesc'
							},
							{
								title : 'Pin Code',
								name : 'pincode'
							}, {
								title : 'IFSC Code',
								name : 'ifscCode'
							}],
							
						});
						$("#existing-utilization-bank-accounts-table").addListToLocalgrid(applicantDetail.utilizationBankDetails);
				   
				   
				    $("#updated-utilization-bank-accounts-table").html('');
					$("#updated-utilization-bank-accounts-table").initLocalgrid({
						columndetails : [ {
							title : 'Account No',
							name : 'accountNumber'
						}, {
							title : 'Name Of the Bank',
							name : 'bankName'
						}, {
							title : 'Branch Address',
							name : 'address'
						},
						{
							title : 'Town/City',
							name : 'town'
						}, {
							title : 'State',
							name : 'stateDesc'
						}, {
							title : 'District',
							name : 'districtDesc'
						},
						{
							title : 'Pin Code',
							name : 'pincode'
						}, {
							title : 'IFSC Code',
							name : 'ifscCode'
						}],
						
					});
					$("#updated-utilization-bank-accounts-table").addListToLocalgrid(applicationDetails.newUtilizationBankDetails);
					
					

				 //association-member-table
					   if (applicationDetails.changeOfCommitteeMembers == null || applicationDetails.changeOfCommitteeMembers == undefined)$('#changeOfReceipientBank').text('-'); 
					    else {
					    	if(applicationDetails.changeOfCommitteeMembers=='Y'){
					    						    		
					    		$('#changeOfCommitteeMembers').text('Yes');  
								
					    	   $('#updated-association-member-table-lebel').show();

	                      } else {
					    		$('#changeOfCommitteeMembers').text('No');
					    
					    		$('#updated-association-member-table-lebel').hide();
						
					    	}
					    	}
				    $("#existing-association-member-table").html('');
					$("#existing-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#existing-association-member-table").addListToLocalgrid(applicantDetail.committeeMembers); 
				 
				   
				    $("#updated-association-member-table").html('');
					$("#updated-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#updated-association-member-table").addListToLocalgrid(applicationDetails.newCommitteeMemberDetails); 
					
					//UPDATED COMMITEE MEMBERS

				 
		}
	});
	
	
}


function populateChangeOfDetailsForm(applicationDetails,smsDetails,mailDetails) {
	//change-of-details
	//alert(JSON.stringify(applicationDetails));
	$('#application-details').load('resources/application-details.html #change-of-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			   //info bar
			 $('#application-track-info').show();
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
			
				var applicantDetail= applicationDetails.associationDetails;
				//assocation details
				 $('#chiefFunctionaryName').text(getValue(applicantDetail.chiefFunctionaryName));
				// $('#fcraRegistrationNumber').text(getValue(applicantDetail.fcraRegistrationNumber));
			     $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(applicantDetail.fcraRegistrationNumber)+'\");>'+getValue(applicantDetail.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(applicantDetail.fcraRegistrationDate));
				 $('#associationPhoneNumber').text(getValue(applicantDetail.associationPhoneNumber));
		         $('#email').text(getValue(applicantDetail.email));
				 $('#chiefFunctionaryPhoneNumber').text(getValue(applicantDetail.chiefFunctionaryPhoneNumber));
				 $('#chiefFunctionaryMobile').text(getValue(applicantDetail.chiefFunctionaryMobile));
				 //Details which may be Update
				 //EXISTING
				// changeOfNameOrAim
				   if (applicationDetails.changeOfNameOrAim == null || applicationDetails.changeOfNameOrAim == undefined)$('#changeOfNameOrAim').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNameOrAim=='Y'){
				    		
				    		$('#changeOfNameOrAim').text('Yes');  
				    		 $('#new-associationName-label').show();
							 $('#new-associationAim-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfNameOrAim').text('No');
				    
				    		 $('#new-associationName-label').hide();
				             $('#new-associationAim-label').hide();
							 
				    	}
				    	}
				 //change of Association Nature
				   if (applicationDetails.changeOfNature == null || applicationDetails.changeOfNature == undefined)$('#changeOfNature').text('-'); 
				    else {
				    	if(applicationDetails.changeOfNature=='Y'){
				    		
				    		 $('#changeOfNature').text('Yes');  
				    		 $('#associationNature-label').show();
				    		 $('#new-associationNature-label').show();
							
				    	} else {
				    		$('#changeOfNature').text('No');
				    		$('#associationNature-label').hide();
				            $('#new-associationNature-label').hide();
				            
				           
				           }
				    	}
				 
				   if(applicantDetail.nature=='1'){
					   $('#associationReligion-label').css("visibility", 'visible'); 
					   if(applicantDetail.religion=='6'){
						   var religionString= applicantDetail.religionDesc+' ('+applicantDetail.religionOther+')';
						   $('#associationReligion').text(getValue(religionString)); //Existing 
						 }
						else {
						   $('#associationReligion').text(getValue(applicantDetail.religionDesc)); //Existing  
						 }
				   }
				 if(applicationDetails.newNature=='1'){	
					 $('#new-associationReligion-label').css("visibility", 'visible');
					 if(applicationDetails.newReligion=='6'){
						 var newReligionString= applicationDetails.newReligionDesc+' ('+applicationDetails.newReligionOther+')'; 
						$('#change-associationReligion').text(getValue(newReligionString)); //Updated
					 }
					 else {
						 $('#change-associationReligion').text(getValue(applicationDetails.newReligionDesc)); //Updated
					 }  
					   
				   }
				/* else {  
					 if(applicantDetail.nature=='1') {$('#dummyForShift-label').hide();} else {
				     $('#dummyForShift-label').show()};
				 }*/
				 
			                                                           
				 $('#associationNature').text(getValue(applicantDetail.natureDesc));//association Nature 
				   //END  ++++++++change of Association Nature++++++++ END 
				    if (applicationDetails.changeOfAddress == null || applicationDetails.changeOfAddress == undefined)$('#changeOfAddress').text('-'); 
				    else {
				    	if(applicationDetails.changeOfAddress=='Y'){
				    		
				    		$('#changeOfAddress').text('Yes');  
				    	     
				    		 $('#new-address-label').show();
							 $('#new-town-label').show();
							 $('#new-stateDesc-label').show();
							 $('#new-district-label').show();
							 $('#new-pincode-label').show();
				    		
				    		
				    	} else {
				    		$('#changeOfAddress').text('No');
				     
							 $('#new-address-label').hide();
							 $('#new-town-label').hide();
							 $('#new-stateDesc-label').hide();
							 $('#new-district-label').hide();
							 $('#new-pincode-label').hide();
				    	}
				    	}
				
				 $('#associationName').text(getValue(applicantDetail.associationName));
				 $('#associationAim').text(getValue(applicantDetail.associationAim));
				 $('#address').text(getValue(applicantDetail.address));
				 $('#town').text(getValue(applicantDetail.town));
				 $('#stateDesc').text(getValue(applicantDetail.stateDesc));
				 $('#districtDesc').text(getValue(applicantDetail.districtDesc));
				 $('#pincode').text(getValue(applicantDetail.pincode));
				 
				 //UPDATED
			     $('#new-associationName').text(getValue(applicationDetails.newAssociationName));
				 $('#new-associationAim').text(getValue(applicationDetails.newAssociationAim));
				 //new Association Nature 
				 $('#change-associationNature').text(getValue(applicationDetails.newNatureDesc));
				 
				 $('#new-address').text(getValue(applicationDetails.newAddress));
				 $('#new-town').text(getValue(applicationDetails.newTown));
				 $('#new-stateDesc').text(getValue(applicationDetails.newStateDesc));
				 $('#new-district').text(getValue(applicationDetails.newDistrictDesc));
				 $('#new-pincode').text(getValue(applicationDetails.newPincode));
				 
				 //designated bank/ branch/ bank account number for receipt and utilization of foreign contribution:
				// changeOfReceipientBank
				   if (applicationDetails.changeOfReceipientBank == null || applicationDetails.changeOfReceipientBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfReceipientBank=='Y'){
				    		$('#changeOfReceipientBank').text('Yes');  
							$('#new-accountNumber-label').show();
							 $('#new-bankName-label').show();
							 $('#new-branch-address-label').show();
							 $('#new-bank-town-label').show();
							 $('#new-bank-stateDesc-label').show();
							 $('#new-bank-districtDesc-label').show();
							 $('#new-bank-pincode-label').show();
							 $('#new-bank-ifscCode-label').show();

				    		
				    		
				    	} else {
				    		$('#changeOfReceipientBank').text('No');
				    
				    		 $('#new-accountNumber-label').hide();
							 $('#new-bankName-label').hide();
							 $('#new-branch-address-label').hide();
							 $('#new-bank-town-label').hide();
							 $('#new-bank-stateDesc-label').hide();
							 $('#new-bank-districtDesc-label').hide();
							 $('#new-bank-pincode-label').hide();
							 $('#new-bank-ifscCode-label').hide();
							 
				    	}
				    	}
				   var newrecpBankDetail=applicationDetails.newReceipientBankDetails;
				 //  alert(newrecpBankDetail.accountNumber);
				//EXISTING
				  
				 var recBankDetail=applicantDetail.receipientBankDetails;
				 if(recBankDetail!=null){				 
					 $('#accountNumber').text(getValue(recBankDetail.accountNumber));
					 $('#bankName').text(getValue(recBankDetail.bankName));
					 $('#branch-address').text(getValue(recBankDetail.address));
					 $('#bank-town').text(getValue(recBankDetail.town));
					 $('#bank-stateDesc').text(getValue(recBankDetail.stateDesc));
					 $('#bank-districtDesc').text(getValue(recBankDetail.districtDesc));
					 $('#bank-pincode').text(getValue(recBankDetail.pincode));
					 $('#bank-ifscCode').text(getValue(recBankDetail.ifscCode));
				 }
				
				 //UPDATED Detail
				 $('#new-accountNumber').text(getValue(newrecpBankDetail.accountNumber));
				 $('#new-bankName').text(getValue(newrecpBankDetail.bankName));
				 $('#new-branch-address').text(getValue(newrecpBankDetail.address));
				 $('#new-bank-town').text(getValue(newrecpBankDetail.town));
				 $('#new-bank-stateDesc').text(getValue(newrecpBankDetail.stateDesc));
				 $('#new-bank-districtDesc').text(getValue(newrecpBankDetail.districtDesc));
				 $('#new-bank-pincode').text(getValue(newrecpBankDetail.pincode));
				 $('#new-bank-ifscCode').text(getValue(newrecpBankDetail.ifscCode));
				 
				 
				  //EXISTING UTILASITION Bank DETAIL
				 
				// changeOfUtilizationBank
				   if (applicationDetails.changeOfUtilizationBank == null || applicationDetails.changeOfUtilizationBank == undefined)$('#changeOfReceipientBank').text('-'); 
				    else {
				    	if(applicationDetails.changeOfUtilizationBank=='Y'){
				    		
				    		$('#changeOfUtilizationBank').text('Yes');  
							
				    		$('#updated-utilization-bank-accounts-table-label').show();

                      } else {
				    		$('#changeOfUtilizationBank').text('No');
				    
				    		 $('#updated-utilization-bank-accounts-table-label').hide();
					
				    	}
				    	}
				   
			
					 $("#existing-utilization-bank-accounts-table").html('');
						$("#existing-utilization-bank-accounts-table").initLocalgrid({
							columndetails : [ {
								title : 'Account No',
								name : 'accountNumber'
							}, {
								title : 'Name Of the Bank',
								name : 'bankName'
							}, {
								title : 'Branch Address',
								name : 'address'
							},
							{
								title : 'Town/City',
								name : 'town'
							}, {
								title : 'State',
								name : 'stateDesc'
							}, {
								title : 'District',
								name : 'districtDesc'
							},
							{
								title : 'Pin Code',
								name : 'pincode'
							}, {
								title : 'IFSC Code',
								name : 'ifscCode'
							}],
							
						});
						$("#existing-utilization-bank-accounts-table").addListToLocalgrid(applicantDetail.utilizationBankDetails);
				   
				   
				    $("#updated-utilization-bank-accounts-table").html('');
					$("#updated-utilization-bank-accounts-table").initLocalgrid({
						columndetails : [ {
							title : 'Account No',
							name : 'accountNumber'
						}, {
							title : 'Name Of the Bank',
							name : 'bankName'
						}, {
							title : 'Branch Address',
							name : 'address'
						},
						{
							title : 'Town/City',
							name : 'town'
						}, {
							title : 'State',
							name : 'stateDesc'
						}, {
							title : 'District',
							name : 'districtDesc'
						},
						{
							title : 'Pin Code',
							name : 'pincode'
						}, {
							title : 'IFSC Code',
							name : 'ifscCode'
						}],
						
					});
					$("#updated-utilization-bank-accounts-table").addListToLocalgrid(applicationDetails.newUtilizationBankDetails);
					
					

				 //association-member-table
					   if (applicationDetails.changeOfCommitteeMembers == null || applicationDetails.changeOfCommitteeMembers == undefined)$('#changeOfReceipientBank').text('-'); 
					    else {
					    	if(applicationDetails.changeOfCommitteeMembers=='Y'){
					    		
					    		$('#changeOfCommitteeMembers').text('Yes');  
								
					    	   $('#updated-association-member-table-lebel').show();

	                      } else {
					    		$('#changeOfCommitteeMembers').text('No');
					    
					    		$('#updated-association-member-table-lebel').hide();
						
					    	}
					    	}
				    $("#existing-association-member-table").html('');
					$("#existing-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#existing-association-member-table").addListToLocalgrid(applicantDetail.committeeMembers); 
				 
				   
				    $("#updated-association-member-table").html('');
					$("#updated-association-member-table").initLocalgrid({
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
							name : 'occupationDesc'
						}, {
							title : 'Post Held in the Association',
							name : 'designationInAssociationDesc'
						}, {
							title : 'Relationship with other member',
							name : 'relationWithOfficeBearersDesc'
						}, {
							title : 'Address',
							name : 'officeAddress'
						}, {
							title : 'Residential Address',
							name : 'residenceAddress'
						}, {
							title : 'Email Id',
							name : 'email'
						}, {
							title : 'Landline',
							name : 'phoneNumber'
						}, {
							title : 'Mobile No.',
							name : 'mobile'
						}],
						
					});
					$("#updated-association-member-table").addListToLocalgrid(applicationDetails.newCommitteeMemberDetails); 
					
					//UPDATED COMMITEE MEMBERS

				 
		}
	});
	
	
}

function populateHospitalityForm(applicationDetails,smsDetails,mailDetails) {


	//alert(JSON.stringify(applicationDetails));

	$('#application-details').load('resources/application-details.html #hospitality-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			 prepareSMSDetails(smsDetails);
			 prepareEMAILDetails(mailDetails);
			   //info bar
			 $('#applicationId-info').text(getValue(applicationDetails.applicationId));
			 $('#service-info').text(getValue(applicationDetails.serviceName));
			 $('#submitted-info').text(getValue(applicationDetails.submissionDate));
			 $('#application-track-info').show();

				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				
				
				/////
			 
			  //var associationdetail=applicationDetails.associationDetails;
			     // Details of the Applicant:
			     $('#applicantName').text(getValue(applicationDetails.applicantName));
			     $('#dateOfBirth').text(getValue(applicationDetails.dateOfBirth));
				 $('#nameOfFatherSpouse').text(getValue(applicationDetails.nameOfFatherSpouse));
				 //Contact Details:
		         $('#address').text(getValue(applicationDetails.address));
				 $('#town').text(getValue(applicationDetails.town));
				 $('#stateDesc').text(getValue(applicationDetails.stateDesc));
				 $('#districtDesc').text(getValue(applicationDetails.districtDesc)); 
				 $('#pincode').text(getValue(applicationDetails.pincode));
				 $('#phoneNumber').text(getValue(applicationDetails.phoneNumber));
				 $('#email').text(getValue(applicationDetails.email));
				 $('#mobile').text(getValue(applicationDetails.mobile));
				 $('#applicantOrganization').text(getValue(applicationDetails.applicantOrganization));
				 $('#applicantDesignation').text(getValue(applicationDetails.applicantDesignation));
				 // Passport particulars:
				 if(applicationDetails.passportAvailable==null || applicationDetails.passportAvailable==undefined)
					 $('#passportAvailable').text('-');
				 else{
					 if(applicationDetails.passportAvailable=='Y')
					 {
						 $('#passportAvailable').text('Yes');
						 $('#passportNumber-label').show();
						 $('#passportPlaceOfIssue-label').show();
						 $('#passportDateOfIssue-label').show(); 
						 $('#passportDateOfExpiry-label').show();
					 }
					 else if(applicationDetails.passportAvailable=='N')
						 {
						 
						 $('#passportAvailable').text('No');
						 
						 $('#passportNumber-label').hide();
						 $('#passportPlaceOfIssue-label').hide();
						 $('#passportDateOfIssue-label').hide(); 
						 $('#passportDateOfExpiry-label').hide();
						 }
					 else
						 {
						 $('#passportAvailable').text('-');
						 }
				 }
				
			    
				 
				 $('#passportNumber').text(getValue(applicationDetails.passportNumber));
				 $('#passportPlaceOfIssue').text(getValue(applicationDetails.passportPlaceOfIssue));
				 $('#passportDateOfIssue').text(getValue(applicationDetails.passportDateOfIssue)); 
				 $('#passportDateOfExpiry').text(getValue(applicationDetails.passportDateOfExpiry));
				 // Forwording Details
				 $('#forwardingLetterNumber').text(getValue(applicationDetails.forwardingLetterNumber));
				 $('#forwardingLetterDate').text(getValue(applicationDetails.forwardingLetterDate));
				 $('#forwardingOfficerName').text(getValue(applicationDetails.forwardingOfficerName));
				 $('#forwardingOfficerDesignation').text(getValue(applicationDetails.forwardingOfficerDesignation));
			     $('#forwardingOfficerOfficeAddress').text(getValue(applicationDetails.forwardingOfficerOfficeAddress));
				 $('#forwardingOfficerTown').text(getValue(applicationDetails.forwardingOfficerTown));
				 $('#forwardingOfficerStateDesc').text(getValue(applicationDetails.forwardingOfficerStateDesc)); 
				 $('#forwardingOfficerDistrictDesc').text(getValue(applicationDetails.forwardingOfficerDistrictDesc));
				 $('#forwardingOfficerPincode').text(getValue(applicationDetails.forwardingOfficerPincode));
				 //status
				 $('#memberCategoryDesc').text(getValue(applicationDetails.memberCategoryDesc));
				 
                   //Hospitality Details
					    $("#hospitality-detail-table").html('');
						$("#hospitality-detail-table").initLocalgrid({
							columndetails : [ {
								title : 'Name of country for which hospitality is to be accepted',
								name : 'hospitalityCountryDesc'
							}, {
								title : 'Name of City in the country:',
								name : 'hospitalityCity'
							}, {
								title : 'Duration of Stay(From Date)',
								name : 'stayFromDate'
							},
							 {
								title : 'Duration of Stay(To Date)',
								name : 'stayToDate'
							},
							{
								title : 'Purpose of Visit:',
								name : 'purposeOfVisit'
							}, {
								title : 'Nature of hospitality to be accepted (In Cash):',
								name : 'hospitalityCash'
							},
							{
								title : 'Nature of hospitality to be accepted (In Cash Currency):',
								name : 'hospitalityCurrencyDesc'
							},
							{
								title : 'Nature of hospitality to be accepted (In Kind):',
								name : 'hospitalityKind'
							},
							{
								title : 'Duration of hospitality to be accepted(From Date)',
								name : 'hospitalityDurationFromDate'
							}, 
							{
								title : 'Duration of hospitality to be accepted(To Date)',
								name : 'hospitalityDurationToDate'
							},
							{
								title : 'Approximate expenditure to be incurred on hospitality',
								name : 'amountOfExpenditure'
							},
							{
								title : 'Amount of Expenditure:',
								name : 'amountOfExpenditure'
							},
							{
								title : 'Remarks:',
								name : 'remarks'
							}],
							
						});
						$("#hospitality-detail-table").addListToLocalgrid(applicationDetails.hospitalityDetails);   
				     
					//Details of host(s)	
					    $("#host-details-table").html('');
						$("#host-details-table").initLocalgrid({
							columndetails : [ {
								title : 'Nature of Host:',
								name : 'natureOfHostDesc'
							}, {
								title : 'Full Name of Individual/Organisation:',
								name : 'nameOfHost'
							}, {
								title : 'Address of Individual/Organisation  with country name',
								name : 'addressOfHost'
							},
							 {
								title : 'Nationality of Individual',
								name : 'nationalityOfHostDesc'
							},
							{
								title : 'Profession of Individual',
								name : 'professionOfHostDesc'
							}, {
								title : 'Passport Number of Individual:',
								name : 'passportNumberOfHost'
							},
							{
								title : 'E-mail address',
								name : 'email'
							},
							{
								title : ' Mobile Number (Including Country Code):',
								name : 'mobile'
							},
							{
								title : 'Fixed Line Telephone Number(with code)',
								name : 'phoneNumber'
							}, 
							{
								title : 'Nature of connection /relationship with the host and/or foreign source extending the hospitality:',
								name : 'relationshipWithHost'
							}
							],
							
						});
						$("#host-details-table").addListToLocalgrid(applicationDetails.hostDetails);   	     
				     
			
		}
	});
	
	
}

function populateGiftContributionForm(applicationDetails,smsDetails,mailDetails) {
	$('#application-details').load('resources/application-details.html #gift-contribution-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			    prepareSMSDetails(smsDetails);
			    prepareEMAILDetails(mailDetails);
	
			    $('#application-track-info').show();
				$('#applicationId-info').text(getValue(applicationDetails.applicationId));
				$('#service-info').text(getValue(applicationDetails.serviceName));
				$('#submitted-info').text(getValue(applicationDetails.submissionDate));			
			   
				//15-01-16
				var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
				'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
				'<span class="glyphicon glyphicon-paperclip"></span></button>';	
				$('#bi-doc').html(docContent);	
				
				var documents=applicationDetails.uploadedDocuments;
				prepareDocuments(documents);
				/////
				var applicantDetails= applicationDetails.applicantDetails;
			     $('#name').text(getValue(applicantDetails.name));
				 $('#dateOfBirth').text(getValue(applicantDetails.dateOfBirth));
				 $('#nameOfFatherSpouse').text(getValue(applicantDetails.nameOfFatherSpouse));
				 $('#address').text(getValue(applicantDetails.address));
		         $('#town').text(getValue(applicantDetails.town));
				 $('#stateDesc').text(getValue(applicantDetails.stateDesc));
				//if (applicantDetails.districtDesc == null || applicantDetails.districtDesc == undefined) $('#district').text('-'); else  $('#district').text(applicantDetails.districtDesc);
				 $('#districtDesc').text(getValue(applicantDetails.districtDesc));
				 $('#pincode').text(getValue(applicantDetails.pincode));
				 $('#email').text(getValue(applicantDetails.email));
				 $('#mobile').text(getValue(applicantDetails.mobile));
				 $('#phoneNumber').text(getValue(applicantDetails.phoneNumber));
				 $('#panNumber').text(getValue(applicantDetails.panNumber));
				
			    	// for contribution details
				 
				 $('#amountReceived').text(getValue(applicationDetails.amountReceived));
				 $('#bankDetails').text(getValue(applicationDetails.bankDetails));
				 $('#relativeName').text(getValue(applicationDetails.relativeName));
				 $('#relativeNationalityDesc').text(getValue(applicationDetails.relativeNationalityDesc));
				 $('#relativeCountryOfResidenceDesc').text(getValue(applicationDetails.relativeCountryOfResidenceDesc));
				 $('#relativeEmail').text(getValue(applicationDetails.relativeEmail));
				 $('#relativePassportNumber').text(getValue(applicationDetails.relativePassportNumber));
				 $('#relativeRelation').text(getValue(applicationDetails.relativeRelation));
				 $('#panNumber').text(getValue(applicationDetails.panNumber));
		}
	});

}

function populateArticleContributionForm(applicationDetails,smsDetails,mailDetails) {
	$('#application-details').load('resources/application-details.html #article-contribution-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			prepareSMSDetails(smsDetails);
			prepareEMAILDetails(mailDetails);
			$('#application-track-info').show();
			$('#applicationId-info').text(getValue(applicationDetails.applicationId));
			$('#service-info').text(getValue(applicationDetails.serviceName));
			$('#submitted-info').text(getValue(applicationDetails.submissionDate));			
			
			//15-01-16
			var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
			'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
			'<span class="glyphicon glyphicon-paperclip"></span></button>';	
			$('#bi-doc').html(docContent);
			
			var documents=applicationDetails.uploadedDocuments;
			prepareDocuments(documents);
			/////
			   
				var applicantDetails= applicationDetails.applicantDetails;
			     $('#name').text(getValue(applicantDetails.name));
				 $('#dateOfBirth').text(getValue(applicantDetails.dateOfBirth));
				 $('#nameOfFatherSpouse').text(getValue(applicantDetails.nameOfFatherSpouse));
				 $('#address').text(getValue(applicantDetails.address));
		         $('#town').text(getValue(applicantDetails.town));
				 $('#stateDesc').text(getValue(applicantDetails.stateDesc));
				//if (applicantDetails.districtDesc == null || applicantDetails.districtDesc == undefined) $('#district').text('-'); else  $('#district').text(applicantDetails.districtDesc);
				 $('#districtDesc').text(getValue(applicantDetails.districtDesc));
				 $('#pincode').text(getValue(applicantDetails.pincode));
				 $('#email').text(getValue(applicantDetails.email));
				 $('#mobile').text(getValue(applicantDetails.mobile));
				 $('#phoneNumber').text(getValue(applicantDetails.phoneNumber));
				 $('#panNumber').text(getValue(applicantDetails.panNumber));
				
			    	// for Article details
				 
				 //$('#fcraRegistrationNumber').text(getValue(applicationDetails.fcraRegistrationNumber));
				 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(applicationDetails.fcraRegistrationNumber)+'\");>'+getValue(applicationDetails.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(applicationDetails.fcraRegistrationDate));
				 $('#receivedDate').text(getValue(applicationDetails.receivedDate));
				 $('#articleName').text(getValue(applicationDetails.articleName));
				 $('#articleDescription').text(getValue(applicationDetails.articleDescription));
				 $('#relativeName').text(getValue(applicationDetails.relativeName));
				 $('#relativeAddress').text(getValue(applicationDetails.relativeAddress));
				 $('#purpose').text(getValue(applicationDetails.purpose));
				 $('#quantity').text(getValue(applicationDetails.quantity));
				 $('#value').text(getValue(applicationDetails.value));
				 $('#modeOfUtilization').text(getValue(applicationDetails.modeOfUtilization));
		}
	});

}


function populateSecurityContributionForm(applicationDetails,smsDetails,mailDetails) {
	$('#application-details').load('resources/application-details.html #security-contribution-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			prepareSMSDetails(smsDetails);
			prepareEMAILDetails(mailDetails);
			$('#application-track-info').show();
			$('#applicationId-info').text(getValue(applicationDetails.applicationId));
			$('#service-info').text(getValue(applicationDetails.serviceName));
			$('#submitted-info').text(getValue(applicationDetails.submissionDate));			
			
			//15-01-16
			var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
			'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
			'<span class="glyphicon glyphicon-paperclip"></span></button>';	
			$('#bi-doc').html(docContent);	
			
			var documents=applicationDetails.uploadedDocuments;
			prepareDocuments(documents);
			/////
			   
				var securityDetail= applicationDetails.applicantDetails;
			     $('#name').text(getValue(securityDetail.name));
				 $('#dateOfBirth').text(getValue(securityDetail.dateOfBirth));
				 $('#nameOfFatherSpouse').text(getValue(securityDetail.nameOfFatherSpouse));
				 $('#address').text(getValue(securityDetail.address));
		         $('#town').text(getValue(securityDetail.town));
				 $('#stateDesc').text(getValue(securityDetail.stateDesc));
				//if (securityDetail.districtDesc == null || securityDetail.districtDesc == undefined) $('#district').text('-'); else  $('#district').text(securityDetail.districtDesc);
				 $('#districtDesc').text(getValue(securityDetail.districtDesc));
				 $('#pincode').text(getValue(securityDetail.pincode));
				 $('#email').text(getValue(securityDetail.email));
				 $('#mobile').text(getValue(securityDetail.mobile));
				 $('#phoneNumber').text(getValue(securityDetail.phoneNumber));
				 $('#panNumber').text(getValue(securityDetail.panNumber));
				
			    	// for Article details
				 
				// $('#fcraRegistrationNumber').text(getValue(applicationDetails.fcraRegistrationNumber));
				 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(applicationDetails.fcraRegistrationNumber)+'\");>'+getValue(applicationDetails.fcraRegistrationNumber)+'</a>');
				 $('#fcraRegistrationDate').text(getValue(applicationDetails.fcraRegistrationDate));
				 $('#receivedDate').text(getValue(applicationDetails.receivedDate));
				 $('#securityNature').text(getValue(applicationDetails.securityNature));
				 $('#articleDescription').text(getValue(applicationDetails.articleDescription));
				 $('#relativeName').text(getValue(applicationDetails.relativeName));
				 $('#relativeAddress').text(getValue(applicationDetails.relativeAddress));
				 $('#nominalValue').text(getValue(applicationDetails.nominalValue));
				 $('#marketValue').text(getValue(applicationDetails.marketValue));
				 $('#rbiPermissionDetails').text(getValue(applicationDetails.rbiPermissionDetails));
				 $('#modeOfUtilization').text(getValue(applicationDetails.modeOfUtilization));
		}
	});	
	
}

function populateElectionContributionForm(applicationDetails,smsDetails,mailDetails) {
	$('#application-details').load('resources/application-details.html #election-contribution-details', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			prepareSMSDetails(smsDetails);
			prepareEMAILDetails(mailDetails);
			$('#application-track-info').show();
			$('#applicationId-info').text(getValue(applicationDetails.applicationId));
			$('#service-info').text(getValue(applicationDetails.serviceName));
			$('#submitted-info').text(getValue(applicationDetails.submissionDate));			

			//15-01-16
			var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
			'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
			'<span class="glyphicon glyphicon-paperclip"></span></button>';	
			$('#bi-doc').html(docContent);	
			
			var documents=applicationDetails.uploadedDocuments;
			prepareDocuments(documents);
			/////
			   
				var electionDetail= applicationDetails.applicantDetails;
			     $('#name').text(getValue(electionDetail.name));
				 $('#dateOfBirth').text(getValue(electionDetail.dateOfBirth));
				 $('#nameOfFatherSpouse').text(getValue(electionDetail.nameOfFatherSpouse));
				 $('#address').text(getValue(electionDetail.address));
		         $('#town').text(getValue(electionDetail.town));
				 $('#stateDesc').text(getValue(electionDetail.stateDesc));
				//if (electionDetail.districtDesc == null || electionDetail.districtDesc == undefined) $('#district').text('-'); else  $('#district').text(electionDetail.districtDesc);
				 $('#districtDesc').text(getValue(electionDetail.districtDesc));
				 $('#pincode').text(getValue(electionDetail.pincode));
				 $('#email').text(getValue(electionDetail.email));
				 $('#mobile').text(getValue(electionDetail.mobile));
				 $('#phoneNumber').text(getValue(electionDetail.phoneNumber));
				 $('#panNumber').text(getValue(electionDetail.panNumber));
				
			    	// for Article details
				 
				 $('#dateOnWhichNominated').text(getValue(applicationDetails.dateOnWhichNominated));
				 $('#natureOfContribution').text(getValue(applicationDetails.natureOfContribution));
				 $('#value').text(getValue(applicationDetails.value));
				 $('#purpose').text(getValue(applicationDetails.purpose));
				 $('#donorName').text(getValue(applicationDetails.donorName));
				 $('#donorAddress').text(getValue(applicationDetails.donorAddress));
				 $('#donorEmail').text(getValue(applicationDetails.donorEmail));
				 //$('#donorNationality').text(getValue(electionDetail.donorNationality));
				 $('#donorNationalityDesc').text(getValue(applicationDetails.donorNationalityDesc));
				 //$('#donorCountryOfResidence').text(getValue(electionDetail.donorCountryOfResidence));
				 $('#donorCountryOfResidenceDesc').text(getValue(applicationDetails.donorCountryOfResidenceDesc));
				 $('#donorRelation').text(getValue(applicationDetails.donorRelation));
				 $('#utilizationDetails').text(getValue(applicationDetails.utilizationDetails));
		}
	});	
	
}

//06-07-17
function populateGrievancesForm (applicationDetails,smsDetails,mailDetails){
	$('#application-details').load('resources/application-details.html #grievances-Detail', function(responseText, textStatus, XMLHttpRequest) {
		if(textStatus == 'success')
		{
			
			prepareSMSDetails(smsDetails);
			prepareEMAILDetails(mailDetails);
			$('#application-track-info').show();
			$('#applicationId-info').text(getValue(applicationDetails.applicationId));
			$('#service-info').text(getValue(applicationDetails.serviceName));
			$('#submitted-info').text(getValue(applicationDetails.submissionDate));			

			
			var docContent='&nbsp;&nbsp;<button type="button" title="Click to see supporting documents"'+ 
			'data-toggle="modal" data-target="#documentModal" class="btn btn-info active btn-sm">'+
			'<span class="glyphicon glyphicon-paperclip"></span></button>';	
			$('#bi-doc').html(docContent);	
			
			var documents=applicationDetails.uploadedDocuments;
			prepareDocuments(documents);
						
			var grievances= applicationDetails.associationDetails;			
		
			 $('#associationName').text(getValue(grievances.associationName));			 
			 $('#address').text(getValue(grievances.address));
	         $('#town').text(getValue(grievances.town));
			 $('#stateDesc').text(getValue(grievances.stateDesc));
			 $('#districtDesc').text(getValue(grievances.districtDesc));
			 $('#pincode').text(getValue(grievances.pincode));				
				 if(applicationDetails.assoRegFlag=='Y')
				 {
					 $('#assoRegFlag').text('Yes');
					 $('#fcraRegistrationNumber-label').show();
					 $('#fcraRegistrationNumber').html('<a onclick=getRegistrationDetails(\"'+getValue(grievances.fcraRegistrationNumber)+'\");>'+getValue(grievances.fcraRegistrationNumber)+'</a>');
				 }	
			
				 else if(applicationDetails.assoRegFlag=='N')
				{				 
					 $('#assoRegFlag').text('No');				 
					 $('#fcraRegistrationNumber-label').hide();
				}
			 //$('#assoRegFlag').text(getYesNoValue(applicationDetails.assoRegFlag));			 
			 $('#grievanceDesc').text(getValue(applicationDetails.grievanceDesc));
			 $('#complainantName').text(getValue(applicationDetails.complainantName));
			 $('#complainantAddress').text(getValue(applicationDetails.complainantAddress));
			 $('#complainantEmail').text(getValue(applicationDetails.complainantEmail));
			 $('#complainantMobile').text(getValue(applicationDetails.complainantMobile));
			 $('#fileCreatedDate').text(getValue(applicationDetails.fileCreatedDate));
			 $('#submissionDate').text(getValue(applicationDetails.submissionDate));
			 
			 //Show Cause notices
			 var showCauseNoticeList=applicationDetails.showCauseNoticeList;
			    $("#show-cause-notices-table").html('');
				$("#show-cause-notices-table").initLocalgrid({
					columndetails : [ {					
						title : 'Notice Body',
						name : 'noticeBody'
					},{
						title : 'Notice Subject',
						name  : 'noticeSubject'
					},{
						title : 'Generated By',
						name : 'generatedBy'
					},{
						title : 'Generated On',
						name : 'generatedDate'
					}],
					
				});
				$("#show-cause-notices-table").addListToLocalgrid(applicationDetails.showCauseNoticeList); 
		     
		
         }
	});	

}



function prepareSMSDetails(smsDetail){	
//	alert(JSON.stringify(smsDetail));
	$("#sms-table").html('');
	if(smsDetail==null || smsDetail=="")
		return;
	$("#sms-table").initLocalgrid({
		columndetails : [ {
			title : 'Mobile',
			name : 'p2'
		}, {
			title : 'Message',
			name : 'p1'
		}, {
			title : 'Dated',
			name : 'p3'
		}, {
			title : 'Status',
			name :  'p5'
			
		}, {
			title : 'Status Date',
			name : 'p4'
		}]
	});
	$("#sms-table").addListToLocalgrid(smsDetail);
	//$('#sms-section').show();
}


function prepareEMAILDetails(mailDetails){
	
	$("#mail-table").html('');
	if(mailDetails==null || mailDetails=="")
		return;
	$("#mail-table").initLocalgrid({
		columndetails : [ {
			title : 'Mail Address',
			name : 'p3'
		}, {
			title : 'Subject',
			name : 'p1'
		}, {
			title : 'Mail Body',
			name : 'p2'
		}, {
			title : 'Dated',
			name : 'p4'
		}, {
			title : 'Status',
			name :  'p6'
		},{
			title : 'Status Date',
			name :  'p5'
		}]
	});
	$("#mail-table").addListToLocalgrid(mailDetails);
}





