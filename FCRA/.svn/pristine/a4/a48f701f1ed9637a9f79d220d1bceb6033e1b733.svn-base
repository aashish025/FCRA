


$(document).ready(function (){
	initializeReportList();
	$("#report-type-form").validationEngine({promptPosition: 'bottomLeft'});
});

function initializeReportList() {
	$("#Report-list").html('');
	$("#Report-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-mis-report-detail',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Report Id', name:'reportId', sortable:true}, 
				{title:'Report Name', name:'reportName',sortable:true}, 
			    {title:'Entered On', name:'createdDate',sortable:true}
			],			
			onRowSelect:function(rowdata){getReportDetails(rowdata);}
    		});	
	 
	
}	
	function getReportDetails(rowdata){
		
		$('#reportId').val(rowdata.reportId);
		$('#reportName').val(rowdata.reportName);
		$('#createdDate').val(rowdata.createdDate);
		$('#table-btn').show();
		$('#add-btn').hide();
		$('#details-div').show();
		$('#edit-actions').show();
		$('#add-details-btn').hide();
		$('#Report-list').show();
		
		}	
	function clearForm(){	
		$(document).find('.clear').each(function (){
			$(this).val('');
		});
	}
	function addreportdetails(){
		  $('#sticky-notify').html('');
		 $('#bar-notify').html('');
		if($('#report-type-form').validationEngine('validate')){
		var params = 'reportName='+$('#reportName').val()+'&requestToken='+$('#requestToken').val();
		var action = 'add-mis-report-detail';		
		$.ajax({
			url: action,
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){	
				$.each(JSON.parse(data[0]),function(index,item){
					
					if(item.s=="e"){
						$('#close-modal-btn').click();
						notifyList(JSON.parse(data[0]),'');	
					      var token = JSON.parse(data[2]).li;
							if(token != null && token != '')
							$('#requestToken').val(token);
						    hideAll();
					}
					else if(item.s=="w"){
						$('#close-modal-btn').click();
						notifyList(JSON.parse(data[0]),'');	
					      var token = JSON.parse(data[2]).li;
							if(token != null && token != '')
							$('#requestToken').val(token);
						    hideAll();
					}
					else if(item.s=="s"){
					
						notifyList(JSON.parse(data[0]),'');	
					      var token = JSON.parse(data[2]).li;
							if(token != null && token != '')
							$('#requestToken').val(token);
							$('#reportId').val(JSON.parse(data[1]));
						initReport();
					}
				});		
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		
		
		}
		}
	function initReport(){
		
		$('#details-div').hide();
		$('#Report-list').hide();
		$('#add-btn').hide();
		$('#close-modal-btn').click();
		$('#res-div').show();
		$('#add-details-btn').hide();
		initReportList();
	}
	function initReportList(){
		var params = 'reportId='+$('#reportId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'get-officeassign-mis-report-detail';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){			
				populateOfficeList(JSON.parse(data[0]),JSON.parse(data[1]));			
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});
		clearForm();
	}
	function populateOfficeList(availableList,assignedList){
		
		var avpl = document.getElementById("avpl");
		var aspl = document.getElementById("aspl");
		avpl.length = 0;
		aspl.length = 0;		
	    $.each(availableList, function(index, item) {	    	
	    		avpl[avpl.length] = new Option(item.v, item.k);
	    	});
	    $.each(assignedList, function(index, item) {
	    		aspl[aspl.length] = new Option(item.v, item.k);
	    	}); 		
	}
	function addRemoveOffice(fromObjName, toObjName){	
		var value=$('#'+fromObjName+' option:selected').index();	
	    $('#'+fromObjName+' option:selected').each(function() {
	       	$("#"+toObjName).append($(this).clone());
	       	$(this).remove();
	    });    
	   $('#'+fromObjName+' option:nth('+value+')').attr("selected", "selected");  
	   $('#'+toObjName).each(function() {	   
	     	$('#'+toObjName+' option:selected').removeAttr("selected", "selected");      	
	  });
	}
	
	function saveDetails(flag){	
		
		$('#bar-notify').html('');
	    for (var i=aspl.length - 1; i >= 0; i--){
	    	aspl.options[i].selected = true;
	    } 
	   
	    var params = 'reportId='+$('#reportId').val()+'&aspl='+$('#aspl').val()+'&requestToken='+$('#requestToken').val();
	    var action = 'save-mis-report-detail';	
		$.ajax({
			url: action,
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				
				  notifyList(JSON.parse(data[0]),'');	
			      var token = JSON.parse(data[1]).li;
					if(token != null && token != '')
					$('#requestToken').val(token);
				    if(flag=="FRS")
					hideAll();			
			      	else if(flag=="ERS")			
					$('#res-div').hide();
				    initializeReportList();
				     showview();
				  
					
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		}); 
	
		clearForm();
		}
	function showview(){
		$('#edit-actions').hide();
		$('#Report-list').show();
		$('#add-btn').show();
		$('#res-div').hide();
		$('#table-btn').hide();
		$('#details-div').hide();
		$('#add-details-btn').hide();
		

		}
	function editofficetype(){
		$('#fresh-res-save').hide();
		$('#edit-res-save').show();
		$('#res-div').show();	
		$('#Report-list').hide();
		$('#details-div').hide();
		$('#edit-actions').hide();
		initReportList();
	}
	
	function editreport()
	{	
		  $('#sticky-notify').html('');
		$('#bar-notify').html('');
		if($('#report-type-form').validationEngine('validate'))
		{
	    var params = 'reportName='+$('#reportName').val()+'&reportId='+$('#reportId').val()+'&requestToken='+$('#requestToken').val();
		var action = 'edit-mis-report-detail';
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
				initializeReportList();
				initForm();
				showadd();
				
				
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
				
			}
		});
		clearForm();
	}
	}
	function deletereport()
	{   $('#sticky-notify').html('');
		$('#bar-notify').html('');
		var params = 'reportName='+$('#reportName').val()+'&reportId='+$('#reportId').val()+'&requestToken='+$('#requestToken').val();	
		var action = 'delete-mis-report-detail';
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
				initializeReportList();
			     showadd();
									
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		clearForm();
	}

	

function initForm(){	
	$('#add-btn').hide();		
	$('#add-details-btn').show();
	$('#details-div').show();
	$('#edit-actions').hide();

}

function showadd()
{   $('#table-btn').hide();
	$('#add-details-btn').hide();		
	$('#add-btn').show();
	$('#details-div').hide();
	$('#edit-actions').hide();
}


