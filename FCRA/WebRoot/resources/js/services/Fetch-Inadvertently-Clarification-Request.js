$(document).ready(function() {
	$("#applicationId").keyup(function(event) {
		if (event.which == 13) {
			event.preventDefault();
			getApplicationDetails();
		}
	}).keydown(function(event) {
		if (event.which == 13) {
			event.preventDefault();
		}
	});

});

function showNotifications(notificationList) {
	$("#bar-notify").empty();
	$("#text-notify").empty();
	notifyList(notificationList);
}

function getApplicationDetails() {
	 $('#application-track-info').hide('');
	$("#app-form").validationEngine({
		promptPosition : 'bottomRight'
	});
	if ($('#app-form').validationEngine('validate') == true) {
		var action = 'pull-basic-details-Fetch-Inadvertently-Clarification-Request';
		var params = 'applicationId=' + $('#applicationId').val();
		var aa = $('#applicationId').val();
		$('#application-id').val(aa);
		$.ajax({
			url : action,
			method : 'GET',
			data : params,
			dataType : 'json',

			success : function(data) {
			//	var status = JSON.parse(data[0]).li;
				//var notificationList = JSON.parse(data[1]);
			
				var applicationDetails = JSON.parse(data[0]);
				alert("44 "+applicationDetails.applicationId);
				var htmlContent='<table class="table table-bordered" cellpadding="10000"><thead style="background-color: #9E9EB9;"><tr><th>Application Id</th><th>Applicant Name</th><th>Submission Date</th><th>Link</th></tr></thead>';
				htmlContent+='<tr><td><span class="text-danger">'+applicationDetails.applicationId+'</span></td><td><span class="text-danger">'+applicationDetails.applicantName+'</span></td><td><span class="text-danger">'+applicationDetails.submissionDate+'</span></td><td><a class="" href="javascript:getApplicationDocument(\''+applicationDetails.applicationId+'\');"> <span class="glyphicon glyphicon-cloud-download" title="Affidavit Document"></span></a></td></tr>';
				htmlContent+='</table>';	
				$('#application-details').html(htmlContent);
				
				//var smsDetails = JSON.parse(data[3]);
			//	var mailDetails =JSON.parse(data[4]);
		        //alert(JSON.stringify(mailDetails));
				//alert(JSON.stringify(data[2]));
						
				//$('#application-details').empty();
				//showNotifications(notificationList);
				
				/*if(status == 'error') {
					
				}
				else {
					populateForm(applicationDetails,smsDetails,mailDetails);
				}*/
			},
			error : function(textStatus, errorThrown) {
			}
		});

	}
 }
