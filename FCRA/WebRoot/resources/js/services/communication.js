var searchType=null;
var blockList = "";
$(document).ready(function(){
	$("#mail-form").validationEngine({promptPosition: 'topRight'});
	$("#sms-form").validationEngine({promptPosition: 'topRight'});
	searchType="R";
	$("#selectAssociation").on('change', associationChange);
/*	var params = '';
	var action = 'delete-upload-cache-communication';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'text',
		success: function(data){	
			$('[up-file-list="mail-upload"] tbody').html('');
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});*/
});
function toggleSearch(){	
	if ( $('#app-search').is(':visible') ){		
		$('#app-search').hide();
		$('#reg-search').show();
		searchType="R";
	}else if($('#reg-search').is(':visible')){		
		$('#app-search').show();	
		$('#reg-search').hide();
		searchType="A";
	}
}

function clearModal(){
	$('#cancelRemark').val('');
	$('#revokeRemark').val('');
	$('#cancellationType').val('');
	$('input[type=checkbox]').each(function () {
        $(this).removeAttr('checked');
	});	
	$('#requestDate').val('');
	$('#bar-notify').html('');
	/*var params = '';
	var action = 'delete-upload-cache-communication';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'text',
		success: function(data){
			$('[up-file-list="mail-upload"] tbody').html('');
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});*/
}
function getApplicationList(){	
	//alert("Ddd33");
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
	    		dataurl:'get-application-list-communication?applicationId='+$('#applicationId').val()+'&registrationNumber='+$('#registrationNumber').val(),
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
function checkCommunicationType(value){
	if(value==0){		
		//$('#actions').show();	
		$('#note-section').show();
		$('#selected-rcn-div').hide();
		$('#clear-association').hide();
		$('#add-association').show();
		$('#individual-section').hide();
	}else if(value==1){
		$('#individual-section').show();	
		$('#actions').hide();
		$('#note-section').hide();
	}
}
function getApplicationListByRCN(){	
	//alert("DDGHN");
	$('#bar-notify').html('');
	var params = 'appId='+$('#registrationNumber').val()+'&searchType='+searchType;
	var action = 'get-application-details-communication';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');						
			initApplicationDetails(JSON.parse(data[0])[0]);			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}
function getApplicationListByAppId(){	
	$('#bar-notify').html('');
	var params = 'appId='+$('#applicationId').val()+'&searchType='+searchType;
	var action = 'get-application-details-communication';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){			
			if(data[1]!=null)
				notifyList(JSON.parse(data[1]),'');						
			initApplicationDetails(JSON.parse(data[0])[0]);			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}
function initApplicationDetails(data){
	/* Initialize Hide and Show for Application Details */
		$('#app-info').show();	
		$('#application-list-div').hide();	
		$('#actions').show();		
	/* Initialize Hide and Show for Application Details */
		prepareBasicInfo(data);	
}
function prepareBasicInfo(data){	
	$('#regnNumber').html(data.applicationId);
	$('#secFileNumber').html(data.sectionFileNo);
	$('#applicantName').html(data.applicantName);
	$('#functionaryName').html(data.chiefFunctionary);	
	$('#state').html(data.state);
	$('#district').html(data.district);
	$('#email').html(data.emailId);
	$('#mobile').html(data.mobile);
}
function getRequestDate(value){
	if(value=="R"){
		$('#request-date-div').show();
	}else{
		$('#request-date-div').hide();		
	}
}
function sendMail(){	
	//$('[up-file-list="rc-upload"] tbody').html('');
	$('#bar-notify').html('');
	if($('#mail-form').validationEngine('validate')){
		$("#mailModal-submit-btn").button('loading');
		if($( 'input[name="communicationType"]:checked' ).val() == 0)
			alert('This may take some time to complete. Please wait patiently.')
		params='mailSubject='+$('#mail-subject').val()+'&mailBody='+$('#mail-body').val()+'&appId='+$('#regnNumber').text()+
		'&communicationType='+$( 'input[name="communicationType"]:checked' ).val()+'&searchType='+searchType+'&requestToken='+$('#requestToken').val();
		var action = 'send-mail-communication';	
		//alert(params);
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[0]=="success"){
					
					$("#selected-rcn").val("");
					$('#regnNumber').text("");
					$("#note-section").hide();
					postWork();
				}else{
					//notifyList(JSON.parse(data[1]),'');
				}
				$('#requestToken').val(data[2]);
				$("#mailModal-submit-btn").button('reset');
				notifyList(JSON.parse(data[1]),'');
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#mailModal-submit-btn").button('reset');
			}
		});
	}
}
function sendSMS(){	
	$('#bar-notify').html('');
	if($('#sms-form').validationEngine('validate')){
		$("#smsModal-submit-btn").button('loading');
		if($( 'input[name="communicationType"]:checked' ).val() == 0)
			alert('This may take some time to complete. Please wait patiently.')

		params='smsBody='+$('#sms-body').val()+'&appId='+$('#regnNumber').html()+'&communicationType='+$('input[name="communicationType"]:checked' ).val()+
		'&searchType='+searchType+'&requestToken='+$('#requestToken').val();
		var action = 'send-sms-communication';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[0]=="success"){
					
					$("#selected-rcn").val("");
					$('#regnNumber').text("");
					$("#note-section").hide();
					postWork();
				}else{
				//	notifyList(JSON.parse(data[1]),'');
				}
				$('#requestToken').val(data[2]);
				$("#smsModal-submit-btn").button('reset');
				notifyList(JSON.parse(data[1]),'');
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#smsModal-submit-btn").button('reset');
			}
		});
	}
}
function postWork(){	
	$('#actions').hide();
	$('#app-info').hide();
	$('#mailModal-close-btn').click();
	$('#smsModal-close-btn').click();
	$('#mail-subject').val('');
	$('#mail-body').val('');
	$('#sms-body').val('');		
	$("input[name='communicationType']").prop('checked',false);
	$('#registrationNumber').val('');
	$('#applicationId').val('');
	/*var params = '';
	var action = 'delete-upload-cache-communication';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'text',
		success: function(data){
		$('[up-file-list="mail-upload"] tbody').html('');			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});*/
	//$("#file-list").html('');
	//$('[up-file-list="rc-upload"] tbody').html('');
}

function addAssociations(){
	//alert(1);
	$("#bulk-application-list").css({"overflow-y":"hidden", "overflow-x":"hidden", "height":"0px"});
	$("#selectAssociation").val("");
	$("#assoState-div").hide();
	$("#assoDistrict-div").hide();
	$("#assoBlockYear-div").hide();
	$("#search-div").hide();
	$("#bulk-application-list").html('');
	$("#add-association-footer-div").hide();
}

function associationChange(e){
	//alert("DDd");
	$("#add-association-footer-div").hide();
	$("#bulk-application-list").css({"overflow-y":"hidden", "overflow-x":"hidden", "height":"0px"});
	$("#bulk-application-list").html('');
	$("#assoState").val("");
	$("#assoDistrict").val("");
	$("#assoBlockYear").val("");
	if($(this).val()=="1"||$(this).val()=="3"){
		$("#assoState-div").show();
		$("#assoDistrict-div").show();
		$("#assoBlockYear-div").hide();
		$("#search-div").show();
	}
	else if($(this).val()=="2"){
		$("#assoState-div").show();
		$("#assoDistrict-div").show();
		$("#assoBlockYear-div").show();
		$("#search-div").show();
	}
	else if($(this).val()=="4"){
		$("#assoState-div").hide();
		$("#assoDistrict-div").hide();
		$("#assoBlockYear-div").hide();
		$("#search-div").show();
	}
}

function getDistrict(){
	$("#bulk-application-list").hide();
	$("#add-association-footer-div").hide();
	if($('#assoState').val()==""){
		$("#assoDistrict").empty().append('<option value="" selected="selected" >All District</option>');
	}
	else{
		var params = 'state='+$('#assoState').val();
		var action = 'get-district-communication';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				populateSelectBox(data,'All District','assoDistrict');	
				//$("#add-association-footer-div").hide();
				//$("#add-associtation-btn").show();  $("#add-associtation-close-btn").show();  $("#add-all-associtation-btn").show();
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	
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

function getAdvanceSearchApplicationList(){
	$('#searchAssociation').button('loading');
	$("#addAssociation-form").validationEngine();
	$("#add-association-footer-div").hide();
	$("#bulk-application-list").css({"overflow-y":"hidden", "overflow-x":"hidden", "height":"0px"});
	$("#bulk-application-list").html('');
	//	if($('#assoState').val()==""&&$('#assoDistrict').val()=="")
		//	alert('This may take some time to complete. Please wait patiently.')
			var params='associationType='+$('#selectAssociation').val()+'&assoState='+$('#assoState').val()+'&assoDistrict='+$('#assoDistrict').val()+'&assoBlockYear='+$('#assoBlockYear').val();
		var action = 'get-association-communication';	
		if($("#addAssociation-form").validationEngine('validate')) {
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',

				success: function(data){
					$("#bulk-application-list").show();
					$("#bulk-application-list").css({"overflow-y":"scroll", "overflow-x":"scroll", "height":"200px"});
					$("#add-association-footer-div").hide();
					blockList = JSON.parse(data[0]);
					populatedataTable(blockList);
					$("#add-association-footer-div").show();
					$("#add-all-associtation-btn").show();
					$("#add-associtation-btn").hide();  $("#add-associtation-close-btn").hide();  $("#add-all-associtation-btn").show();
					if(blockList.length == 0){
						$("#bulk-application-list").hide();
						$("#bulk-application-list").css({"overflow-y":"hidden", "overflow-x":"hidden", "height":"0px"});
						$("#add-associtation-btn").hide();  $("#add-associtation-close-btn").hide();  $("#add-all-associtation-btn").hide();
					}
				},
				error: function(textStatus,errorThrown){
					alert('error');
					$('#searchAssociation').button('reset');
				}
			});
		}
	else{
		$('#searchAssociation').button('reset');
	}
}

function populatedataTable(blockList){
	//alert(blockList.length);
	$("#bulk-application-list").initLocalgrid(
			{
				columndetails:[

				               {title:'RCN', name:'p1', formatter : function(rowdata) {
				   				var apfcraRcnid=rowdata.p1;
								
								var	link = '<a onclick=getRegistrationDetails(\"'+apfcraRcnid+'\");>'+apfcraRcnid+'</a>';
								
								return link;
						  }},
				               {title:'Association Name', name:'p2'},
				               {title:'Association Email Id', name:'p3'},
				               {title:'Association Mobile No.', name:'p4'}
				             
				               ],
				               checkboxrequired:true,
				               onCheckboxSelect:function(status, rowdata){
				            	  var temp = "";
				            	   var checkedLength=$("#bulk-application-list").getSelectedRows().length;	
				            	   /*if(blockList.length==checkedLength){
				            		   for(i=0;i<blockList.length;i++){
				            			   temp+=blockList[i].p1+','
				            		   }
				            	   }
				            	   else{*/
				            		   for(i=0;i<checkedLength;i++){
				            			   temp+=$("#bulk-application-list").getSelectedRows()[i].p1+','
				            		   }				            	 
				            	  // }
				            	   if(checkedLength == 0){
				            		   $("#add-association-footer-div").show();$("#add-associtation-btn").hide(); $("#add-associtation-close-btn").hide(); $("#add-all-associtation-btn").show();}
				            	   else{
				            		   $("#add-association-footer-div").show(); $("#add-associtation-btn").show(); $("#add-associtation-close-btn").show(); $("#add-all-associtation-btn").hide();
				            	   }
				            	   $('#selected-hidden-rcn').val(temp.substring(0,(temp.length)-1));
				               }

			});
	$("#bulk-application-list").addListToLocalgrid(blockList); 
	   $("#bulk-application-list-row-checkbox-all").hide();
	   $('#searchAssociation').button('reset');
		
}

function  getRegistrationDetails(fcraRcn){
	 var url = 'popup-registration-tracking?appId='+fcraRcn;
	 openLink(url);
}

function addAssociation(){
	//alert("eee");
	$("#clear-association").show();
	$("#addAssociationModal").modal("hide");
	if($('#selected-rcn').val()==""){
		 $('#selected-rcn').val(  $('#selected-hidden-rcn').val());
	}
	else {
		$('#selected-rcn').val( $('#selected-rcn').val()+","+ $('#selected-hidden-rcn').val());
	}
	$("#regnNumber").html($('#selected-rcn').val());
	$("#selected-rcn-div").show();
	$('#actions').show();	
}
function addAllAssociation(){
	var temp= "";
	$("#clear-association").show();
	$("#addAssociationModal").modal("hide");
	for(i=0;i<blockList.length;i++){
		temp+=blockList[i].p1+','
	}
	var selectedRcn = temp.substring(0,(temp.length)-1);
	$('#selected-rcn').val(selectedRcn)
	$("#regnNumber").html($('#selected-rcn').val());
	$("#selected-rcn-div").show();
	$('#actions').show();	
}
function clearAssociations(){
	$('#selected-rcn').val("");
	$("#selected-rcn-div").hide();
	$("#clear-association").hide();
	$("#actions").hide();
}
function clearSelection(){
	$('input:checkbox').prop('checked', false);
	$("#add-association-footer-div").show();$("#add-associtation-btn").hide(); $("#add-associtation-close-btn").hide(); $("#add-all-associtation-btn").show();
}

function showHide(){
	$("#bulk-application-list").hide();
	$("#add-association-footer-div").hide();
}
function openMail(){
	$("#mailModal").modal('show');
	var params = '';
	var action = 'delete-upload-cache-communication';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'text',
		success: function(data){	
			$('[up-file-list="mail-upload"] tbody').html('');
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});
}