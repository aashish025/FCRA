$(document).ready(function (){
	
	$("#user-audit-form").validationEngine({promptPosition: 'bottomReft'});
});

function submituser() {
	$('#sticky-notify').html('');

	if($('#user-audit-form').validationEngine('validate')){
		$('#bar-notify').html('');
	
	var params = 'userAudit='+$('#auditId').val()+'&fromDate='+$('#fromDate').val()+'&toDate='+$('#toDate').val();
	//alert(params);
	$('#output-section-audit').show();
	$('#back-section-audit').show();
	
	$("#userAudit-list").html('');
	$("#userAudit-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'details-user-audit?'+params,
    		defaultsortcolumn:'userName',
    		defaultsortorder:'desc',
    		columndetails:
			[
			 	{title:'User Name', name:'userName',sortable:true},
			 	
				{title:'RoleId - RoleName', name:'roleName',sortable:true},
															
				{title:'TIME STAMP', name:'timeStamp',sortable:true},
				
				{title:'Ip Address', name:'createdIp',sortable:true},
				
				{title:'Action', name:'flag',sortable:true}
			],
			
			
    		});	
	home();
	}
}
function home()
{
	$('#form-div-audit').hide();
}

function goBack()
{
	
	$('#output-section-audit').hide();
	$('#back-section-audit').hide();
	$('#form-div-audit').show();
}