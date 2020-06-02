var barContent=[];

$(document).ready(function (){
	$("#exemptionDays").val('15');
	initializeExemptionAnnualTable();	
	$("#submission-form").validationEngine({promptPosition: 'topRight'});
});


function initializeExemptionAnnualTable() {
	$('#rcn-detail-data-div').show();
	$("#exemption-annual-table").html('');
	$("#exemption-annual-table").bootgrid(
    		{
    		title:'',
    		recordsinpage:'10',
    		dataobject:'propertyList', 
    		defaultsortcolumn:'actionDate',
    		defaultsortorder:'desc',
    		dataurl:'table-exemption-annual-return-penalty',
    		columndetails:
			[
				
				{title:'Registration Number', name:'rcn', sortable:true}, 				
				{title:'Association Name', name:'assoName',sortable:true},
				{title:'Block Year', name:'blockYear'},
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
	var action = 'district-exemption-annual-return-penalty';	
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
		    		dataurl:'advance-rcn-list-exemption-annual-return-penalty?state='+$('#state').val()+'&district='+$('#district').val()+'&applicationName='+$('#applicationName').val()+'&functionaryName='+$('#functionaryName').val(),
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
	var action='rcn-details-exemption-annual-return-penalty';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			if(data[0]!=null)		
			         initApplicationDetails(JSON.parse(data[0])[0]);
					 populateBlkyr(JSON.parse(data[1]));
					
		},      
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}


function populateBlkyr(blkyrlist){
	$('#tableBody').html('');
	var  content  = '';
	content = "<div class = 'row'>"
		for(var i = 0; i < blkyrlist.length; i++){
			var data1=blkyrlist[i];
			var count=i+1;
			content += '<div class = "col-sm-4">' +'<input type="checkbox" class="tdCheckBox validate[minCheckbox[1]]" name="blkYear"  id="'+data1+'" value = "'+data1+'"/>'+data1+'</div>';
			}
			content +=  "</div>"
		$('#tableBody').append(content) ;
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
function openSearch(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
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
	  var action = 'application-list-exemption-annual-return-penalty';	
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
			var action = 'rcn-details-exemption-annual-return-penalty';	
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',
				success: function(data){
					if(data[0]!=null)		
					         initApplicationDetails(JSON.parse(data[0])[0]);
								populateBlkyr(JSON.parse(data[1]));	
				},
				error: function(textStatus,errorThrown){
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
	var validationResult = $('#submission-form').validationEngine('validate');
	if(validationResult==true) { 
		var nnn='';
		$.each($("input[name='blkYear']:checked"), function(){            
        nnn+= $(this).val()+",";
    });
				var  params = 'applicationId='+$('#applicationId').val()+'&exemptionDays='+$('#exemptionDays').val()+'&remarks='+$('#remarks').val()+'&blockYear='+nnn.slice(0,-1)+'&requestToken='+$('#requestToken').val();
				var action = 'add-rcn-details-exemption-annual-return-penalty';		
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
								notifyList(JSON.parse(data[2]),'');
								var token = JSON.parse(data[1]).li;
								if(token != null && token != '')
								$('#requestToken').val(token);
								initializeExemptionAnnualTable();
								clearForm1();	
								showSearchTable();
								
							}
							
						});
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
	$('#remarks').val('');	
	$('#rcn-search-detail').hide();	
	$('#rcn-detail-data-div').show();

}
