$(document).ready(function(){
	showReportPendencyReport();
});

function showReportPendencyReport() {
	$("#barGraphDiv11").html('');
			$("#barGraphDiv11").bootgrid(
							{
								title : 'Track Request List',
								recordsinpage : '10',
								dataobject : 'propertyList',
								dataurl : 'lisi-issue-track-reporting',
								columndetails : [

									{
										title : 'Technical Issue Id',
										name : 'complaintSubject'
									},
									{
										title : 'Status',
										name : 'complaintMenuName'
									},
									{
										title : 'Technical Issue Subject',
										name : 'complaintCategory',
									},
									
									{
										title : 'Technical Issue Menu Name',
										name : 'complaintAttachmentStatus'
									},
									{
										title : 'Technical Issue Category',
										name : 'complaintRaisedBy',
									},
									{
										title : 'Technical Issue Description',
										name: 'sentMacAddress',
									},
									
									{
										title : 'Technical Issue Reply Description',
										name : 'attachedFile',
									},
									{
										title : 'Issue Submission Date',
										name : 'complaintRepliedDescription',
									},
									{
										title : 'Reply',
										name : 'recordStatus',
									},
									
										],
							});$('#barGraphDiv11').on('click', 'tbody td', function() {
								  //get textContent of the TD
								//alert("TD cell textContent : "+ this.td);
								//getApplicationDetails11(this.textContent);
								  //get the value of the TD using the API 
								// alert("value by API : "+ table.cell({ row: this.parentNode.rowIndex, column : this.cellIndex }).data());
								})
			
	}

function getTrackingComplaindId(cid){
	 $('#tirreplymodel').modal();
	 //document.getElementById("delete-doc-list1").value = cid;
	 document.getElementById("complaintDetails_complaintId").value = cid;
	 var my_id_value = $(this).data('tirreplymodel');
	
}


function getTrackingComplaindIdalert(cid){
	alert("ACTIVE");
	
}