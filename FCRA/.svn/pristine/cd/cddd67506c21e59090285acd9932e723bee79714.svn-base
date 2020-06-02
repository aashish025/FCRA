$(document).ready(function(){	
	$('#misReportMaster').validationEngine({promptPosition: 'bottomRight'});	
	pull();
});
function pull(){
	$("#mis-reports").html('');
	var params='';
	$.getJSON('pull-mis-master', params, function(data){		
		$('#mis-reports').append(data.misReportsTable);		
	});
}
function pullDetails(id){	
	var params='id='+id;
	$.getJSON('pull-details-mis-master', params, function(data){		
		$('#name').val(data.name);		
		$('#type').val(data.type);
		$('#displayOrder').val(data.displayOrder);
		$('#id').val(id);
	});
	$('#edit').removeClass('hide');
	$('#add').addClass('hide');
}
function add(){	
	if(jQuery('#misReportMaster').validationEngine('validate')==true){		
		$('#misReportMaster').submit();		
	}	
}
function edit(){
	var params='id='+$('#id').val()+'&name='+$('#name').val()+'&type='+$('#type').val()+'&displayOrder='+$('#displayOrder').val();
	if(jQuery('#misReportMaster').validationEngine('validate')==true){
		$.getJSON('edit-mis-master', params, function(data){	
			pull();		
		});
		clear();
	}	
}
function del(){	
	var params='id='+$('#id').val();
	$.getJSON('delete-mis-master', params, function(data){	
		pull();		
	});
	clear();
}
function cancel(){	
	clear();
	$('#add').removeClass('hide');
	$('#edit').addClass('hide');
}
function clear(){
	$('.clear').each(function(){
		$(this).val('');
	});
}