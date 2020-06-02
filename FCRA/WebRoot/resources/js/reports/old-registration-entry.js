
$(document).ready(function(){
	$('#old-registration-detailsDiv').show();
	$('#oldregistrationEntry-form').hide();
	$('#form-div').hide();
	$("#oldReg-Certificateprint-form").validationEngine({promptPosition: 'topRight'});
});	



function showOldRegistrationEntryForm(){
	
	$('#form-div').show();	
	$('#oldregistrationEntry-form').show();
	$('#backDisplay').show();
	$("#oldregistrationEntry-form").validationEngine({promptPosition: 'topRight'});
	$("#state").on('change', getDistrictListForState);	
	$("#bankstate").on('change', getDistrictListForBank);
	$("#fcraregDate").on('change', setValidFromToDate);
	hideDetails();
}


function hideDetails(){
	
	$('#old-registration-detailsDiv').hide();
}

function homeScreen(){
	$('#old-registration-detailsDiv').show();	
	$('#oldregistrationEntry-form').hide();
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	$('#form-div').hide();
	$('#backDisplay').hide();
}


function closeAll(){
	
	homeScreen();
}

function openOldRegistrationModal(){
	
	
}

function dateString2Date(dateString) {  
	var dt  = dateString.split(/\-|\s/);
	var dd = new Date();
	return new Date(dt[2],dt[1]-1,dt[0]);
	
}



function setValidFromToDate(){
	var fcraDate=$("#fcraregDate").val();
	$("#validfromDate").val(fcraDate);
	var selectedDate=$("#fcraregDate").val();
	
	var selectedtodate=dateString2Date(selectedDate);	 
    var validToDate = selectedtodate.setFullYear(selectedtodate.getFullYear() + 5);
    var date1 = new Date(validToDate);
    
    var dd = date1.getDate();
    var mm = date1.getMonth()+1; 
    var yyyy = date1.getFullYear();
    if(dd < 10)
    {
        dd = '0'+ dd;
    }
    if(mm < 10)
    {
        mm = '0' + mm;
    }
    var validtodate = dd+'-'+mm+'-'+yyyy;
    
    var date0=dateString2Date(validtodate);
    var fromDate2=date0.setDate(date0.getDate() - 1);
    var date2 = new Date(fromDate2);
    var dd = date2.getDate();
    var mm = date2.getMonth()+1; 
    var yyyy = date2.getFullYear();
    if(dd < 10)
    {
        dd = '0'+ dd;
    }
    if(mm < 10)
    {
        mm = '0' + mm;
    }
   
    var fromdate3 = dd+'-'+mm+'-'+yyyy;
   
    if(fromdate3=='NaN-NaN-NaN'){
    	 $('#validToDate').val('');
    }
    else
    $('#validToDate').val(fromdate3);	
	
}

function getDistrictListForState(){
    var params = 'state='+$('#state').val();
	var action = 'getdistrict-old-registration-entry';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			//alert(JSON.stringify(data));			
			populateSelectBox(data,'Select District','district');			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}

function getDistrictListForBank(){

    var params = 'state='+$('#bankstate').val();
	var action = 'getdistrict-old-registration-entry';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			populateSelectBox(data,'Select District','bankdistrict');			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}



function getReligionList(){
var assoNatureSelected=$('#assoNature').val();
    var params = 'assoNature='+$('#assoNature').val();
	var action = 'get-asso-nature-old-registration-entry';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			
			populateSelectBox(data,'Select Religion','assoReligion');	
			$('#assoReligionDiv').show();
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



function adddOldregDetails(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	if($('#oldregistrationEntry-form').validationEngine('validate'))
       {
        var assoNature = [];
        $.each($("input[name='assoname']:checked"), function(){            

        	assoNature.push($(this).val());

        });
        
				var  params = 'assoName='+$('#assoName').val()+'&assoTownCity='+$('#assoCity').val()+'&assoAddress='+$('#assoAdd').val()+
				 '&state='+$('#state').val()+'&district='+$('#district').val()+'&assoPin='+$('#assoZipCode').val()+'&assoNature='+assoNature+
				 '&assoReligion='+$('#assoReligion').val()+'&assoAims='+$('#assoAims').val()+'&regDate='+$('#fcraregDate').val()+			
				 '&validFrom='+$('#validfromDate').val()+'&validTo='+$('#validToDate').val()+'&userId='+$('#assoUserid').val()+
				 '&accNumber='+$('#accNumber').val()+'&bankName='+$('#bankName').val()+'&bankAddress='+$('#bankAdd').val()+
				 '&bankTownCity='+$('#bankCity').val()+'&bankState='+$('#bankstate').val()+'&bankDistrict='+$('#bankdistrict').val()+
				 '&accountNumber='+$('#accNumber').val()+'&bankZipCode='+$('#bankZipCode').val()+'&oldregRemark='+$('#oldregRemark').val()+'&requestToken='+$('#requestToken').val();
				 
                // alert(params);
				var action = 'add-old-registration-entry';		
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
					//alert(JSON.stringify(data[2]));
						var rcn = JSON.parse(data[2]);
						
						var status=JSON.parse(data[3]).li;
						if(status = "success"){
							var tUrl = 'generatecertificate-old-registration-entry?rcn='+rcn;
							openLink(tUrl);								
							//window.open("generatecertificate-old-registration-entry?rcn="+rcn, '_blank');
							}
					},
					error: function(textStatus,errorThrown){
						alert('error');				
					}
				});
				
				$('#assoReligionDiv').hide();
				$('#oldregistrationEntry-form').trigger("reset");
				clearForm();				
		}
	}
	



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


function clearModal(){
	$('#sticky-notify').html('');
	$('#bar-notify').html('');
	$('#printCertificateModal-error').html('');	
	$("#rcn").val("");
}


function showNotifications(notificationList){
	$("#sticky-notify").empty();
	$("#text-notify").empty();
	$("#bar-notify").empty();
	notifyList(notificationList);	
}




function printOldRegistrationCerificate(){
 $('#sticky-notify').html('');
 $('#bar-notify').html('');
 $('#printCertificateModal-error').html('');
 
 if($("#oldReg-Certificateprint-form").validationEngine('validate')){
	var params = 'rcn='+$('#rcn').val();
	//alert('params-'+params);
	var action = 'generateReprintcertificate-old-registration-entry';	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			//alert(data);
			notifyList(JSON.parse(data[0]),'');
			var status=JSON.parse(data[1]).li;
			var rcnStatus = JSON.parse(data[2]);
			//alert(rcnStatus);
			if(rcnStatus=='1'){
				var tUrl = 'generatecertificate-old-registration-entry?rcn='+$('#rcn').val();
				openLink(tUrl);		
			}else{
				showNotifications(notificationList);
				modalNotification("printCertificateModal-error",notificationList);
				clearModal();
				$("#printCertificateModal").modal("show");	
				
			}
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}
}	






function modalNotification(text,list){
	$.each(list, function(index, item) {
		if(item.s == "i")
			$("#"+text).html("<div class = 'alert alert-info alert-dismissable'>  <button type = 'button' class = 'close' data-dismiss = 'alert' aria-hidden = 'true'>&times; </button>"+item.d+"</div>");
		else if (item.s == "e")
			$("#"+text).html("<div class = 'alert alert-danger alert-dismissable'>  <button type = 'button' class = 'close' data-dismiss = 'alert' aria-hidden = 'true'>&times; </button>"+item.d+"</div>");
		else if (item.s == "s")
			$("#"+text).html("<div class = 'alert alert-success alert-dismissable'>  <button type = 'button' class = 'close' data-dismiss = 'alert' aria-hidden = 'true'>&times; </button>"+item.d+"</div>");
		else if(item.s == "w")
			$("#"+text).html("<div class = 'alert alert-warning alert-dismissable'>  <button type = 'button' class = 'close' data-dismiss = 'alert' aria-hidden = 'true'>&times; </button>"+item.d+"</div>");
	});

}
