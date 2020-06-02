$(document).ready(function (){
	$("#changeRegistrationValidity-form").validationEngine({promptPosition: 'bottomRight'});
	 var d = new Date();
	 var dd = d.getDate();
	    var mm = d.getMonth()+1; //January is 0!
	    var yyyy = d.getFullYear();
	    var validity = dd+'-'+mm+'-'+yyyy;	
     $('#validityFrom').val(validity);
    // $('#noOfYrs').change();
   //  checkDateValidity();
});

function toggleSearch(){	
	resetAll();
	if ( $('#appid-search').is(':visible') ){	
		$('#appid-search').hide();
		$('#advance-search').show();
		$('#toggle-btn').html('<span class="fa fa-search"></span>&nbsp;Basic Search');	
	}else if($('#advance-search').is(':visible')){		
		$('#appid-search').show();	
		$('#advance-search').hide();
		$('#toggle-btn').html('<span class="fa fa-search"></span>&nbsp;Advance Search');
	}
}
function resetAll(){
	
	$('#bar-notify').html('');
	$('#state').val('');
	$('#district').val('');
	$('#applicationName').val('');
	$('#applicationId').val('');
}
function getDistrict(){
	var params = 'state='+$('#state').val();
	var action = 'district-change-registration-validity';	
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
	// Initialize Hide and Show for Application Details 
		$("#app-list").html('');	
		$('#bar-notify').html('');
		$('#app-info').hide();
		$('#actions').hide();
		$("#app-list").bootgrid(
	    		{
		    		title:'',
		    		recordsinpage:'10',
		    		dataobject:'propertyList',    		
		    		dataurl:'advance-rcn-list-change-registration-validity?state='+$('#state').val()+'&district='+$('#district').val()+'&applicationName='+$('#applicationName').val()+'&functionaryName='+$('#functionaryName').val(),
		    		columndetails:
					[
						{title:'Registration Number', name:'assoRegnNumber',sortable:true},					
						{title:'Applicant / Association Name', name:'applicantName'},					
					],
					onRowSelect:function(rowdata){getApplicationDetails(rowdata);}
	    });	
		$('#application-list-div').show();
}


function getApplicationDetails(data){
	$('#bar-notify').html('');
	barContent=[];
	var regn=data.assoRegnNumber;
	var params = 'applicationId='+data.assoRegnNumber;
	$('#applicationId').val(data.assoRegnNumber);
	var action='rcn-details-change-registration-validity';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			if(data[0]!=null)		
			         initApplicationDetails(JSON.parse(data[0])[0]);
		},      
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}


function initApplicationDetails(data){			
	$('#app-info').show();
	$('#submit-actions').show();
	prepareBasicInfo(data);	
}

function prepareBasicInfo(data){
$('#regnNumber').html(data.assoRegnNumber);
$('#regnDate').html(data.assoRegnDate);
$('#secFileNumber').html(data.sectionFileNo);
$('#applicantName').html(data.applicantName);
$('#currentStatus').html(data.assoRegnStatusDesc);	
$('#assoAddress').html(data.assoAddress);
$('#assoNature').html(data.assoNature);
$('#lastRenewed').html(data.lastRenewed);
$('#validUpTo').html(data.validUpTo);
$('#assoBank').html(data.assoBank);	
$('#assoBankAddress').html(data.assoBankAddress);
$('#assoAccNumber').html(data.assoAccNumber);	
$('#application-list-div').hide();
$("#assocType").val(data.assocType);

}	
function getApplicationList() {

	$('#bar-notify').html('');
	  var action = 'application-list-change-registration-validity';	
		var params = 'applicationId='+$('#applicationId').val();
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){
				notifyList(JSON.parse(data[1]),'');
				populateDatatable(JSON.parse(data[0]));				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	

}

function populateDatatable(data){
	if(data==''||data==null){
		$("#application-list-div").hide();
		$("#applicationId").val('');
	}
	else{
		$("#application-list-div").show();
		$("#app-list").html('');
		$("#app-list").initLocalgrid({
			columndetails:[			 
			               {
			            	   title:'RCN Number', 
			            	   name:'li',
			               },
			               {
			            	   title:'Association Name', 
			            	   name:'ld'
			               }
			               ],
			               onRowSelect:function(id,rowdata){populateRcnDetail(rowdata);}
		});	
		$("#app-list").addListToLocalgrid(data);
	}
}


function populateRcnDetail(data){
	$('#bar-notify').html('');
	barContent=[];
	var regn=data.li;
	var params = 'applicationId='+data.li;
	var action = 'rcn-details-change-registration-validity';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			if(data[0]!=null)		
			         initApplicationDetails(JSON.parse(data[0])[0]);
						
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}



function submitRegisterationValidity(){
	checkDateValidity();
	if($('#changeRegistrationValidity-form').validationEngine('validate')) {
		$('#sticky-notify').html('');
		$('#bar-notify').html('');
		/*alert($('#changeRegistrationValidity-form').validationEngine('validate'));
	if($('#changeRegistrationValidity-form').validationEngine('validate'))
	{*/	
		var  params = 'rcn='+$('#regnNumber').text()+'&validityFrom='+$('#validityFrom').val()+'&validityUpTo='+$('#validityUpTo').val()+'&noOfYrs='+$('#noOfYrs').val()+'&requestToken='+$('#requestToken').val();

		var action = 'submit-change-validity-change-registration-validity';		
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				$.each(JSON.parse(data[0]),function(index,item){
					if(item.s=="e"){
						notifyList(JSON.parse(data[0]),'');
						var token = JSON.parse(data[1]).li;
						if(token != null && token != '')
							$('#requestToken').val(token);

					}
					else if(item.s="s"){
						notifyList(JSON.parse(data[0]),'');
						var token = JSON.parse(data[1]).li;
						if(token != null && token != '')
							$('#requestToken').val(token);
						ShowSearch();
						$('#noOfYrs').val(''); 
						$('#validityUpTo').val('');
					}

				});
			},
			error: function(textStatus,errorThrown){
				alert('error');				
			}
		});	
	}
}
function ShowSearch(){
	$('#appid-search').show();
	$('#adv-search-button-div').show();
	$('#advance-search').hide();
	$('#app-info').hide();	
	$('#submit-actions').hide();
	resetAll();	
	
}	

function resetAll(){
	$('#state').val('');
	$('#district').val('');
	$('#applicationName').val('');
	$('#applicationId').val('');
}

function initValidity(){
	var from=parseDate($('#validityFrom').val());	
	var d = new Date(from.setMonth((from.getMonth() + $('#noOfYrs').val()*12))-1);		
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

function checkDateValidity(){
		var validityFrom = $('#validityFrom').val();
		var action = 'date-validity-change-registration-validity';
		var params = 'validityFrom='+validityFrom+'&noOfYrs='+$('#noOfYrs').val();
		 $.ajax({
				url: action,
				method:'POST',
				data:params,
				dataType:'json',
				success: function(data){
					notifyList(JSON.parse(data[0]),'');
					$("#currentDateString").val(JSON.parse(data[1]));
				},
				error: function(textStatus,errorThrown){
					alert('error');
				}
			});
	}	

