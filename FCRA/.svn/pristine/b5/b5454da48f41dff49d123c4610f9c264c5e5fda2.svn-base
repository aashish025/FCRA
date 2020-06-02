var barContent=[];

$(document).ready(function (){
	initializeExemptionRenewalTable();	
	$("#submission-form").validationEngine({promptPosition: 'topRight'});
});


function initializeExemptionRenewalTable() {
	$('#rcn-detail-data-div').show();
	$("#exemption-renewal-table").html('');
	$("#exemption-renewal-table").bootgrid(
    		{
    		title:'',
    		recordsinpage:'10',
    		dataobject:'propertyList', 
    		defaultsortcolumn:'actionDate',
    		defaultsortorder:'desc',
    		dataurl:'get-list-exemption-renewal-blocking',
    		columndetails:
			[
				
				{title:'Registration Number', name:'rcn', sortable:true}, 				
				{title:'Association Name', name:'assoName',sortable:true},
				{title:'Remarks', name:'remarks'},
				{title:'Action Date', name:'actionDate',sortable:true},
				{title:'Exemption Days', name:'exemptionDays'},
				
			],
			
    		});	
	
}

function resetAll(){
	
	$('#bar-notify').html('');
	$('#state').val('');
	$('#district').val('');
	$('#applicationName').val('');
	$('#applicationId').val('');
}


function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});

}


function toggleSearch(){	
	resetAll();
	if ( $('#appid-search').is(':visible') ){
		$('#table-btn').hide();		
		$('#appid-search').hide();
		$('#advance-search').show();
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
	var action = 'get-district-exemption-renewal-blocking';	
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
		    		dataurl:'get-advance-rcn-list-exemption-renewal-blocking?state='+$('#state').val()+'&district='+$('#district').val()+'&applicationName='+$('#applicationName').val()+'&functionaryName='+$('#functionaryName').val(),
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
	var action='get-rcn-details-exemption-renewal-blocking';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			//alert(data[0]!=null);
			if(data[0]!=null)
				//notifyList(JSON.parse(data[1]),'');			
			         initApplicationDetails(JSON.parse(data[0])[0]);	
		},
		error: function(textStatus,errorThrown){
			//roplr();
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

function openSearch(){
	
	$('#rcn-detail-data-div').hide();
	$('#rcn-search-detail').show();	
}

function showTable(){
	
	$('#rcn-search-detail').hide();	
	$('#rcn-detail-data-div').show();
	
}


function showSearchTable(){
	
	$('#appid-search').show();
	$("#applicationId").val('');
	$('#adv-search-button-div').show();	
	$('#table-btn').show();
	$('#advance-search').hide();
	
}

function getApplicationList() {

	$('#bar-notify').html('');
	  var action = 'get-application-list-exemption-renewal-blocking';	
		var params = 'applicationId='+$('#applicationId').val();
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){
				notifyList(JSON.parse(data[1]),'');
				//notifyList(JSON.parse(data[2]),'');
				//alert(JSON.stringify(data));
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
		
			var action = 'get-rcn-details-exemption-renewal-blocking';	
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',
				success: function(data){
					if(data[0]!=null)
						//notifyList(JSON.parse(data[1]),'');			
					         initApplicationDetails(JSON.parse(data[0])[0]);	
				},
				error: function(textStatus,errorThrown){
					//roplr();
					alert('error');			
				}
			});	
}
			         
function initApplicationDetails(data){			
			$('#app-info').show();	
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
	
}	


function adddExemptionDetails(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	//if($('#exemptionRenewal-form').validationEngine('validate')) 
	
	var validationResult = $('#submission-form').validationEngine('validate');

if(validationResult==true) {

				var  params = 'applicationId='+$('#applicationId').val()+'&exemptionDays='+$('#exemptionDays').val()+'&remarks='+$('#remarks').val()+'&requestToken='+$('#requestToken').val();
		// alert(params);
				var action = 'add-rcn-details-exemption-renewal-blocking';		
				$.ajax({
					url: action,
					method:'GET',
					data:params,
					dataType:'json',
					success: function(data){	
						notifyList(JSON.parse(data[0]),'');
						var token = JSON.parse(data[1]).li;
						if(token != null && token != '')
						$('#requestToken').val(token);
						initializeExemptionRenewalTable();
						clearForm1();	
						showSearchTable();
					},
					error: function(textStatus,errorThrown){
						alert('error');				
					}
				});	
}
							
		}

	

function clearForm1(){
	$('#table-btn').hide();
	$('#appid-search').hide();
	$('#adv-search-button-div').hide();
	$('#app-info').hide();	
	$('#exemptionDays').val('');
	$('#remarks').val('');	
	$('#rcn-search-detail').hide();	
	//$('#bar-notify').html('');
	$('#rcn-detail-data-div').show();

}
