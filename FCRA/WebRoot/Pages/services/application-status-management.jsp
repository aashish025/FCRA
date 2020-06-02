<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
<!-- <script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script> -->
<script type="text/javascript"
	src="resources/js/bootstrap/all/bootstrap-full.min.js"></script>
<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css"
	type="text/css" />
<script type="text/javascript"
	src="resources/js/input-mask/input-mask.js"></script>
<script type="text/javascript" src="resources/js/all.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
<script src="resources/js/iframeResizer.contentWindow.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="resources/js/forAll.js"></script>
<script type="text/javascript"
	src="resources/js/services/application-status-management.js"></script>
<script type="text/javascript"
	src="resources/js/services/dashboard/nicEdit.js"></script>
<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
<link rel="stylesheet"
	href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
<script type="text/javascript"
	src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
<script src="resources/js/languages/jquery.validationEngine-en.js"
	type="text/javascript" charset="utf-8"></script>
<script src="resources/js/jquery.validationEngine.js"
	type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="resources/css/validationEngine.jquery.css"
	type="text/css" />
<title>FCRA Dashboard</title>
<script>
		var myDetails = '${myDetails}';		
		var recordsPendingForMail = '${recordsPendingForMail}';
		//bkLib.onDomLoaded(function() { nicEditors.allTextAreas() });	
	</script>
</head>

<body id="">
	<div class="container-fluid">
		<div class="content-section">
	
				<input type="hidden" name="serviceCode" id="serviceCode"/>
			
		<input type="hidden" name="appStatusId" id="appStatusId"/>
	
			<div class="row">
				<div class="col-xs-12">
					<div id="notification-area"></div>
				</div>
			</div>
			<div style="z-index: 1; position: absolute; right: 5px; top: 80px; width: 300px;"
				id="sticky-notify"></div>
			<div class="row">
				<div class="col-xs-12">
					<div id="bar-notify"></div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div id="text-notify"></div>
				</div>
			</div>
			
				
			<div class="row">
				<div class="col-sm-3" id="appid-search">
					<div class="input-group" id="app-search">
						<input type="text" class="form-control validate[maxSize[15]]"
							title="Please enter Application Id." placeholder="Application Id"
							name="applicationId" id="applicationId"
							style="text-transform: uppercase;">
							<div class="input-group-btn">
								<button class="btn btn-info title-b"
									onclick="javascript:getApplicationManagementList();"
									title="Click to submit">
									<span class="fa fa-search"></span>&nbsp;
								</button>
							</div> 
							</input>
					</div>

				</div>
			</div>
			<div class="row" id="application-list-div" style="display: none;">
				<div class="col-xs-12 pp-form-field">
					<div class="bs-callout bs-callout-info">
						<span>Search Result</span>
					</div>
					<div id="app-list"></div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 pp-form-field" id="app-info"
					style="display: none;">
					<div class="panel panel-info">
						<div class="panel-heading">Application Details</div>
						<div class="panel-body">
							<div class="row">
									<div class="col-sm-2">
										Application Id
										<p class="text-danger" id="appId"></p>
									</div>
									<div class="col-sm-2">
										Section File No
										<p class="text-danger" id="sectionFile"></p>
									</div>
									<div class="col-sm-2">
										Temp File No
										<p class="text-danger" id="TempFile"></p>
									</div>
									<div class="col-sm-3">
										Applicant / Association Name
										<p class="text-danger" id="appName"></p>
									</div>
									
									
								</div>
								<div class="row">
									<div class="col-sm-2">
										Service Name
										<p class="text-danger" id="serviceName"></p>
									</div>
									<div class="col-sm-2">
										Current Status
										<p class="text-danger" id="currentStatus"></p>
									</div>
									<div class="col-sm-2">
										Submission Date
										<p class="text-danger" id="SubmissionDate"></p>
									</div>
									
								</div>
							</div>
							<!-- <div class="row">
							<span class="badge"></span>
						</div>	 -->
						</div>
					</div>
				</div>
			</div>
			<div class="row">				<br />
			
				<div class="col-sm-8">
					<div id="reopen-div" style="display: none;">
						<button type="button" class="btn btn-success" id="re-open" data-toggle="modal" data-target="#reopenModal">
							<span class="fa fa-unlock" ></span>&nbsp;Reopen
						</button>
					</div>
				
				
				<div id="cancel-div" style="display: none;">
                    <button type="button" class="btn btn-danger glyphicon glyphicon-remove" id="cancel"
					confirm-box="top" 		onclick="javascript:Cancel();">
							<span ></span>&nbsp;Cancel
						</button>
				</div>
			</div>
			</div>
			<div class="modal fade bs-example-modal-lg" id="reopenModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="clearForm();"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="close-form" method="post">
			        <div class="container-fluid">	
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="reopenModal-error">
					  			</div>
							</div>
				 		</div>  
				 		
			        	<div class="row">
				        	<div class="col-xs-3 pp-form-field" id="userlistDiv">
								<label for="userlist">Forward To Whom: </label>
									<select id="userlist" name="userlist" class="form-control  validate[required] clear">
									</select>
						  	 	</div>	
          						<div class="col-sm-10 pp-form-field">
          						<label for="" class="control-label">Status Remark:</label>
					            <textarea class="form-control validate[required]" rows="3" id="statusRemark" name="statusRemark"></textarea>	
          						</div>		            						
       					</div>
          					
          					
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" id="closeModal-close-btn" data-dismiss="modal" onclick="clearForm();">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="reopenModal-submit-btn" onclick="javascript:reopen();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>


		</div>
	</div>
</body>

</html>
