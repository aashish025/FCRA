<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js"></script>	
	<!-- <script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script> -->
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />			
	<script type="text/javascript" src="resources/js/input-mask/input-mask.js"></script> 
	<script type="text/javascript" src="resources/js/all.js"></script>	
	<link rel="stylesheet" type="text/css" href="resources/css/common.css" />	
	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js"></script>		
	<script type="text/javascript" src="resources/js/services/dashboard/application-status.js?Version=2.0"></script>
	<script type="text/javascript" src="resources/js/services/dashboard/nicEdit.js"></script>	
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />	
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
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
			<div class="row">
			    <div class="col-xs-12">
			    	<div id="notification-area"></div>
			    </div>
		    </div>
			<div style="z-index:1; position: absolute; right: 5px; top: 80px; width: 300px;" id="sticky-notify"></div>
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
							<input type="text" 	class="form-control validate[maxSize[15]]" title="Please enter registration number." placeholder="Registration Number" name="applicationId" id="applicationId" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-default active" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>
									<button class="btn btn-info title-b" onclick="javascript:getApplicationList();" title="Click to submit"><span class="fa fa-search"></span>&nbsp;</button>
								</div>																	
							</input>				
						</div>
						 <div class="input-group" id="name-search" style="display:none;">
							<input type="text" 	class="form-control validate[maxSize[15]]" title="Please enter Applicant/Association Name to search." placeholder="Association Name" name="applicationName" id="applicationName" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-default active title-b" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>
									<button class="btn btn-info" onclick="javascript:getApplicationList();"><span class="fa fa-search"></span>&nbsp;</button>
								</div>																								
							</input>				
						</div>
    				</div>    
			</div>
			<div class="row" id="application-list-div" style="display:none;">		 			 	
			 	<div class="col-xs-12 pp-form-field">
			 		<div class="bs-callout bs-callout-info">
						<span>Search Result</span>
					</div>				
			 		<div id="app-list"></div>	 				 		
			 	</div>
		 	</div> 
		 	<div class="row">
			  	<div class="col-xs-10 pp-form-field" id="app-info" style="display:none;">						  	
			  		<div class="panel panel-info">
					  <div class="panel-heading">
					  	Registration Details				  	
					  </div>				  
					  <div class="panel-body">
					    <div class="row">
					    	<div class="col-sm-2">
								Registration Number<p class="text-danger" id="regnNumber"></p>
							</div>											
							<div class="col-sm-2">
								Section FileNumber<p class="text-danger" id="secFileNumber"></p>
							</div>
							<div class="col-sm-2">
								Current Status<p class="text-danger" id="currentStatus"></p>
							</div>				
					    	<div class="col-sm-3">
								Association Name<p class="text-danger" id="applicantName"></p>
							</div>
							<div class="col-sm-3">
								Association Address<p class="text-danger" id="assoAddress"></p>
							</div>									
						</div>	
						<div class="row">					    											
							<div class="col-sm-3">
								Association Nature<p class="text-danger" id="assoNature"></p>
							</div>
							<div class="col-sm-6">
								Recipient Bank Account Details<p class="text-danger" id="assoBank"></p>
							</div>	
							<div id="document-container" class="col-sm-3" style = "display:none;">
								Registeration Cancellation Certificate</br>
								<a href="javascript:getCancelReport();" id="applicant-pdf-btn" title="Click to get Cancel Report">
								<span class="fa fa-2x fa-file-pdf-o text-danger"></span></a>
							</div>		    	
						</div>	
						<div class="row">
							<span class="badge"></span>
						</div>	
					  </div>
					</div>
			  	</div>			  	
		 	</div>		
		 	 <div class="row">
	     	<br/><br/>
		     <div class="col-xs-12 pp-form-field" id="actions" style="display:none;">						          
			      <button type="button" id="cancel-btn" class="btn btn-danger" onclick="clearModal();" data-toggle="modal" data-target="#cancelModal">
			      <span class="glyphicon glyphicon-remove"></span>&nbsp;Cancel Registration</button>
			      <button type="button" id="revoke-btn" class="btn btn-success" onclick="clearModal();" data-toggle="modal" data-target="#revokeModal">
			      <span class="glyphicon glyphicon-ok"></span>&nbsp;Revoke Cancellation</button>			      
		     </div>	
		     <div class="modal fade bs-example-modal-lg" id="cancelModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
			      </div>
			      <div class="modal-body">				      
			        <form id="cancel-form" method="post">			        	
				        <div class="container-fluid">	
				        	<div class="row">
						  		<div class="col-xs-12">
						  			<div id="cancelModal-error">
						  			</div>
								</div>
					 		</div>  
					 		<div class="row">
							 	<div class="col-xs-5 pp-form-field">
							   		<label for="name" class="control-label">Cancellation type:</label>						   		
							  		<select id="cancellationType"  name="cancellationType" class="form-control validate[required,masSize[1]]" onchange="javascript:getRequestDate(this.value);">
							  			<option value="">Select cancellation type</option>
							  			<option value="R">Request</option>
							  			<option value="V">Violation</option>
							  		</select>					   		
							   </div>
							   <div class="col-xs-2 pp-form-field" id="request-date-div" style="display:none;">
							   		<label for="name" class="control-label">Request Date:</label>						   		
							  		<input type="text" class="form-control validate[required,date]" id="requestDate" name="requestDate" />					   		
							   </div>
						     </div>						     			
						     <div class="row">
							 	<div class="col-xs-11 pp-form-field">
							   		<label for="name" class="control-label">Cancellation reason:</label>						   		
							  		<!-- <select id="cancellationReason" name="cancellationReason" class="form-control validate[required,masSize[10]]" multiple></select> -->
							  		<div id="cancellationReason-div"></div>					   		
							   </div>							   
						     </div>		
				        	<div class="row">			        			
          						<div class="col-sm-10 pp-form-field">
          							<label for="forwardOfficeRemark" class="control-label">Remark:</label>
					            	<textarea class="form-control validate[required,maxSize[2000],custom[remark]] title-t" 
					            title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" id="cancelRemark" name="remark"></textarea>	
          						</div>		            						
	          				</div>
				        </div>    
			      </form>
			      </div>
			      <div class="modal-footer">
			      	<div id="create-user-div">
			      		<button type="button" class="btn btn-default" id="cancelModal-close-btn" data-dismiss="modal">
			      		<span class="fa fa-close"></span>&nbsp;Close</button>
			        	<button type="button" class="btn btn-success" id="fetchModal-submit-btn" onclick="javascript:cancelRegistration1();">
			        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
			      	</div>					        
			      </div>
			    </div>
			  </div>
			</div>		
			<div class="modal fade bs-example-modal-lg" id="revokeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
			      </div>
			      <div class="modal-body">				      
			        <form id="revoke-form" method="post">			        	
				        <div class="container-fluid">	
				        	<div class="row">
						  		<div class="col-xs-12">
						  			<div id="revokeModal-error">
						  			</div>
								</div>
					 		</div>    			
						    
				        	<div class="row">			        			
          						<div class="col-sm-10 pp-form-field">
          							<label for="forwardOfficeRemark" class="control-label">Remark:</label>
					            	<textarea class="form-control validate[required,maxSize[2000],custom[remark]] title-t" 
					            title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" id="revokeRemark" name="remark"></textarea>	
          						</div>		            						
	          				</div>
				        </div>    
			      </form>
			      </div>
			      <div class="modal-footer">
			      	<div id="create-user-div">
			      		<button type="button" class="btn btn-default" id="revokeModal-close-btn" data-dismiss="modal">
			      		<span class="fa fa-close"></span>&nbsp;Close</button>
			        	<button type="button" class="btn btn-success" id="fetchModal-submit-btn" onclick="javascript:revokeRegistration();">
			        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
			      	</div>					        
			      </div>
			    </div>
			  </div>
			</div>				    
	     </div>
		</div>
	</div>	       
 </body> 
 <script  type="text/javascript">		
		$('#requestDate').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});		
	</script>
</html>
