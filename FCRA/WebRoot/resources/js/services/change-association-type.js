$(document).ready(function (){
	$("#changeAsso-type-form").validationEngine({promptPosition: 'bottomRight'});
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
	var action = 'district-change-association-type';	
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
		    		dataurl:'advance-rcn-list-change-association-type?state='+$('#state').val()+'&district='+$('#district').val()+'&applicationName='+$('#applicationName').val()+'&functionaryName='+$('#functionaryName').val(),
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
	var action='rcn-details-change-association-type';
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
	  var action = 'application-list-change-association-type';	
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
	var action = 'rcn-details-change-association-type';	
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



function submitAssociationType(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	var aa=$('#assocType').val();
	if($('#changeAsso-type-form').validationEngine('validate'))
	{
		if(aa!=null && aa!=""){
	var  params = 'applicationId='+$('#applicationId').val()+'&assocType='+$('#assocType').val()+'&requestToken='+$('#requestToken').val();
	var action = 'add-change-association-type';		
	$.ajax({
		url: action,
		method:'GET',
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
					
				}
				
			});
		},
		error: function(textStatus,errorThrown){
			alert('error');				
		}
	});	
}
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


