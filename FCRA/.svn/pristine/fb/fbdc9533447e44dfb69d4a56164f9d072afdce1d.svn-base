
function getAssociation(){
	
		var ccode=$('#state').val();
		
		if(ccode!=null || ccode!="" ){
	        if(ccode=='ALL2'){
			   $('#association').empty();
			   $('#association').append($('<option/>', {value:'ALL2', text:'ALL'}));
		   }
		   else{

	var params = 'state='+$('#state').val();
	var action = 'get-association-donor-details';	
	$.ajax({
		url: action,
		method:'POST',
		data:params,
		dataType:'json',
		success: function(data){
			populateSelectBox(data,'Select Association','association');			
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}
}
		
}



function populateSelectBox(list, listHeadText, id) {
	var selectBox = $('#'+id);
	selectBox.empty();	
	selectBox.append($('<option/>', {value:'ALL2', text:'ALL'}));
	$.each(list, function(index, item) {		
		selectBox.append($('<option/>', {value:item.li, text:item.ld}));
		
	});
};






function getSearch(){	
	    $("#donor-list").html('');	
		$('#bar-notify').html('');
		$("#donor-list").bootgrid(
	    		{
		    		title:'',
		    		recordsinpage:'5',
		    		dataobject:'propertyList',    		
		    		dataurl:'get-searchingdata-donor-details?state='+$('#state').val()+'&country='+$('#country').val()+'&association='+$('#association').val()+'&donorName='+$('#donorName').val(),
		    		columndetails:
					[
						{title:'Donor Name', name:'donorName'},					
				          {title:'Country', name:'country'},					
					],
					onRowSelect:function(rowdata){getdata(rowdata);}
	    });	
		$('#donor-list-div').show();
	}

function getdata(rowdata){
	if(rowdata.donorName==null){
		
			$("#donarName").text("-");
		}
		else{
			$("#donarName").text(rowdata.donorName);
		}
	
	
	if(rowdata.donorAddress==null){
		
		$("#donorAddress").text("-");
	}
	else{
		$("#donarAddress").text(rowdata.donorAddress);
	}
	
	if(rowdata.email==null){
		
		$("#email").text("-");
	}
	else{
		$("#email").text(rowdata.email);
	}
	
	if(rowdata.phone==null){
		
		$("#phone").text("-");
	}
	else{
		$("#phone").text(rowdata.phone);
	}
       
	if(rowdata.website==null){
		
		$("#website").text("-");
	}
	else{
		$("#website").text(rowdata.website);
	}
	$("#countryName").text(rowdata.country);
	
	
	
	$("#donor-details-modal").modal({
		show : true
	});
	
}




