$(document).ready(function(){
	$('.toggle').each(function(){
		$(this).addClass('hide');
	});
	getAvailableRoles($("#userId").val());	
});

function getAvailableRoles(userId){	
	if(userId==""){	
		$('.toggle').each(function(){
			$(this).addClass('hide');
		});
	}else{
		var params = 'userId='+ userId;		
		oplr();
		$.pullJSON('get-previleges-user-role', params, function(data) {			
			var avpl = document.getElementById("avpl");
			var aspl = document.getElementById("aspl");
			avpl.length = 0;
			aspl.length = 0;		
		    $.each(data.availablePrevilegesList, function(index, item) {	    	
		    		avpl[avpl.length] = new Option(item.ld, item.li);
		    	});
		    $.each(data.assignedPrevilegesList, function(index, item) {
		    		aspl[aspl.length] = new Option(item.ld, item.li);
		    	});
		    roplr();
		});	
		$('.toggle').each(function(){
			$(this).removeClass('hide');
		});
	}	
}
function addRemoveRoles(fromObjName, toObjName){	
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
function saveDetails(){	
    for (var i=aspl.length - 1; i >= 0; i--){
    	aspl.options[i].selected = true;
    }    
    document.forms.UserRole.submit();    
}