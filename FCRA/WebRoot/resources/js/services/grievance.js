
$(document).ready(function(){

	$("#grievance-type-form").validationEngine({promptPosition: 'topRight'});
	
});	
function checkGrievanceType(value){
	if(value=='N'){		
		$('#details-div').show();	
		$('#grievance-yes').hide();
        $('#comm-div').show();
      
	}else if(value=='Y'){
		$('#details-div').hide();
		$('#grievance-yes').show();
		 $('#comm-div').show();
		
	}

}

function fetchdistrict(){
	var params = 'state='+$('#state').val();
	var action = 'district-grievance-entry';	
	$.ajax({
		url: action,
		method:'GET',
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
function fetchdistrictmodal(){
	var params = 'state='+$('#stateSearch').val();
	var action = 'district-grievance-entry';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			populateSelectBox(data,'Select District','districtSearch');			
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
		
function searchRegistrationDetails(){
	$('#modal-div').show();
	$("#grievance-add-modal").modal({
		show : true
	});
	
}
function searchDetail()
{
	    $("#reg-list").html('');	
		$("#reg-list").bootgrid(
	    		{
		    		title:'',
		    		recordsinpage:'5',
		    		dataobject:'propertyList',    		
		    		dataurl:'advance-list-grievance-entry?state='+$('#stateSearch').val()+'&district='+$('#districtSearch').val()+'&applicationName='+$('#applicationName').val(),
		    		columndetails:
					[
					 {title:'Registration Number', name:'assoRegnNumber',sortable:true},					
					 {title:'Applicant / Association Name', name:'applicantName'},			
					 	
					],
					onRowSelect:function(rowdata){DetailsOfRegi(rowdata.assoRegnNumber);}
	    });
		
		
}
function SubmitDetails()
{
	$('#bar-notify').html('');
	if($('#grievance-type-form').validationEngine('validate'))	
	{
	var checkedvalue= $('input[name="grievance"]:checked').val();
	 var officevalue = [];
     $.each($("input[name='checkname']:checked"), function(){            
         officevalue.push($(this).val());

     });
 	var formData = new FormData();
 	var document = $("#documentFile").get(0).files[0];
 	formData.append('documentFile',document);
 	formData.append('requestToken',$('#requestToken').val());
 	formData.append('district',$('#district').val())
 	formData.append('state',$('#state').val());
	formData.append('assoName',$('#assoName').val());
	formData.append('assoAddress',$('#assoAddress').val());
	formData.append('assoPincode',$('#assoPincode').val());
	formData.append('townCity',$('#townCity').val());
	formData.append('comName',$('#comName').val());
	formData.append('comAddress',$('#comAddress').val());
	formData.append('comEmail',$('#comEmail').val());
	formData.append('comMobile',$('#comMobile').val());
	formData.append('complaint',$('#complaint').val());
	formData.append('user',$('#user').val());
	formData.append('registrationId',$('#registrationId').val());
	formData.append('checkedvalue',checkedvalue);
	formData.append('officevalue',officevalue);
 	
	/*	var params = 'assoName='+$('#assoName').val()+'&state='+$('#state').val()
		+'&district='+$('#district').val()+'&assoAddress='+$('#assoAddress').val()+'&townCity='+$('#townCity').val()+'&assoPincode='+$('#assoPincode').val()
		+'&comName='+$('#comName').val()+'&comAddress='+$('#comAddress').val()+'&comEmail='+$('#comEmail').val()+'&comMobile='+$('#comMobile').val()
		+'&complaint='+$('#complaint').val()+'&user='+$('#user').val()+'&checkedvalue='+checkedvalue+'&requestToken='+$('#requestToken').val()
		+'&registrationId='+$('#registrationId').val()+'&officevalue='+officevalue+'&documentFile='+document;*/
		
		//alert(params);
		var action = 'submit-grievance-entry';
		$.ajax({
			url: action,
			method:'POST',
			data:formData,
			dataType:'json',
			cache: false,													
	        contentType: false,	
	        enctype: 'multipart/form-data',
	        processData: false,
			success: function(data){			
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				showInfo();
				clearForm('#grievance-type-form');
				
			},
			error: function(textStatus,errorThrown){
				alert('error');				
			}
		});
		
	}

	}

function DetailsOfRegi(assoRegnNumber)
{
	$("#registrationId").val(assoRegnNumber);
	$("#grievance-add-modal").modal('hide');
	clearForm('#grievance-add-modal');
	$('#reg-list').empty();
			
}
function viewRegistrationDetails(registrationId)
{       var rcn = $("#registrationId").val();
		viewResDetails(rcn);
			
}
function viewResDetails(rcn) {
	var url = 'popup-registration-tracking?appId='+rcn;
	openLink(url);

}
function showInfo(){
	$('#grievance-type').show();
	$('#grievance-yes').hide();
    $('#comm-div').hide();
    $('#details-div').hide();
    //$('input:radio[name="grievance"]').prop('checked', false);
    clearForm('#grievance-type-form');
	
}
/*function checkFile(fieldObj){
	alert(56);
    var FileName  = fieldObj.value;
    var FileExt = FileName.substr(FileName.lastIndexOf('.')+1);
    var FileSize = fieldObj.files[0].size;
   var inByte = $('#documentFile').val() * 1024 * 1024;
   var FileSizeMB = (FileSize / (1024*1024))
   if ( (FileExt != "pdf" && FileExt != "PDF") || FileSizeMB>$('#documentFile').val())
    {
        var error = "File type is : "+ FileExt+"\n\n";
        error += "Size is : " + FileSizeMB + " MB \n\n";
        error += "Please make sure your file is in pdf format and less than equal" +$('#documentFile').val()+" MB.\n\n";
        jAlert('<strong>'+error+'</strong>');
        $("#documentfile").get(0).val('');
        return false;
    }
  }*/
function prepareCheckboxList(list, containerId, id, name) {
	var divObj = $('#'+containerId);
	var table = $('<table/>', {'class':'checkbox-table'}).append($('<tbody/>', {}));
	var tbody = table.find('tbody');
	var numCols = 3;

	divObj.empty();
	$.each(list, function(index, item){
		if(index%numCols == 0)
			tbody.append($('<tr/>', {}));
		tbody.find('tr:last').append(
				$('<td/>', {}).append(
						$('<label/>', {'class':'checkbox-inline', 'text':item.v}).prepend(
								$('<input/>', {'type':'checkbox', 'id':id+'-'+item.k, 'name':name,'value':item.k}))));
	});
	divObj.append(table);
}
