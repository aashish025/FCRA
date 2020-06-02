<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js?Version=${version}"></script>	
	<!-- <script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script> -->
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css?Version=${version}" type="text/css" />			
	<script type="text/javascript" src="resources/js/input-mask/input-mask.js?Version=${version}"></script> 
	<script type="text/javascript" src="resources/js/all.js?Version=${version}"></script>	
	<link rel="stylesheet" type="text/css" href="resources/css/common.css?Version=${version}" />
	<link rel="stylesheet" type="text/css" href="resources/css/services/dashboard/project-dashboard.css?Version=${version}" />
	<script src="resources/js/iframeResizer.contentWindow.min.js?Version=${version}" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js?Version=${version}"></script>		
	<script type="text/javascript" src="resources/js/services/dashboard/project-dashboard.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/services/dashboard/nicEdit.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/utility/uploader.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css?Version=${version}" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css?Version=${version}" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/bootgrid/bootlocalgrid.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css?Version=${version}" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js?Version=${version}"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css?Version=${version}" type="text/css"/>	
	<title>FCRA Dashboard</title>
	<script>
		var myDetails = '${myDetails}';		
		var recordsPendingForMail = '${recordsPendingForMail}';
		//bkLib.onDomLoaded(function() { nicEditors.allTextAreas() });	
	</script>
	<style>
	.inline{display:inline-block;}
	</style>		
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
		   <div id="applet-div">
		    <div class="row">		    	
		    		<div class="col-sm-2">		    			    					
			            <select id="stateFilter" name="stateFilter" onchange="javascript:getFilteredDashboard();" title="Please select state as a filter. It's optional." 
			            	class="form-control clear validate[maxSize[1]] title-l">
			            	<option value="">Select State</option>
						 	<c:forEach items="${stateList}" var="gType">
						 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
						 	</c:forEach>			            													 													 	
						</select>	
    				</div>
    				<div class="col-sm-2">     					
			            <select id="sectionFilter" name="sectionFilter" onchange="javascript:getSectionFilteredService();" title="Please select section as a filter. It's optional." 
			            class="form-control clear validate[maxSize[1]] title-l">	
			            <option value="">Select Section</option>
					 	<c:forEach items="${sectionList}" var="gType">
					 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
					 	</c:forEach>											 													 	
						</select>	
    				</div>
    				 <div class="col-sm-2">     					
			            <select id="serviceFilter" name="serviceFilter" onchange="javascript:getFilteredDashboard();" title="Please select service as a filter. It's optional." 
			            class="form-control clear validate[maxSize[1]] title-l">	
			            <option value="">Select Service</option>
					 	<c:forEach items="${serviceList}" var="gType">
					 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
					 	</c:forEach>											 													 	
						</select>	
    				</div> 	
    				<div class="col-sm-2">     					
			            <select id="userFilter" name="userFilter" onchange="javascript:getFilteredDashboard();" title="Please select user as a filter. It's optional." 
			            class="form-control clear validate[maxSize[1]] title-l">	
			            <option value="">Select User</option>
					 	<c:forEach items="${officeUserList}" var="gType">
					 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
					 	</c:forEach>											 													 	
						</select>	
    				</div> 	
    				<div class="col-sm-4" id="appid-search">     					
			           <div class="input-group" id="app-search">
							<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Application Id or Section FileNo to search." placeholder="Section FileNo or Application Id" name="applicationId" id="applicationId" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-default active title-b" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>
									<button class="btn btn-info title-b" onclick="javascript:getApplicationList();" title="Click to submit"><span class="fa fa-search"></span>&nbsp;</button>
								</div>																	
							</input>				
						</div>
						 <div class="input-group" id="name-search" style="display:none;">
							<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Applicant/Association Name to search." placeholder="Association / Applicant Name" name="applicationName" id="applicationName" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-default active title-b" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>
									<button class="btn btn-info" onclick="javascript:getApplicationList();"><span class="fa fa-search"></span>&nbsp;</button>
								</div>																								
							</input>				
						</div>
    				</div>     				   				
    			</div> 	
		    		    	
			<div class="row">
				<div class="col-xs-12" id="tab-div">
					<br/>
					 <ul class="nav nav-pills">
					  	<li class="active"><a href="#my-tab" id="myTab" onclick="javascript:initDashboard('1','f');" data-toggle="tab">My Dashboard</a></li>						  
					  	<li class="pull-right"><a href="#office-tab" id="officeTab" onclick="initOfficeDashboard('1','o-f');" data-toggle="tab">To Office</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="my-tab">							
							 <div class="row">
							 	<div class="col-xs-12 pp-form-field" id="my-tab-details">			 		
							 			<ul class="nav nav-pills">
										  <li class="active"><a href="#f" onclick="javascript:initDashboard('1','f');" data-toggle="tab">Fresh</a></li>
										  <li><a href="#r-r" onclick="javascript:initDashboard('2','r-r');" data-toggle="tab">Reply Received</a></li>							  
										  <li><a href="#r-s" onclick="javascript:initDashboard('3','r-s');" data-toggle="tab">Reply Sent</a></li>							  
										  <li><a href="#o-r" onclick="javascript:initDashboard('5','o-r');" data-toggle="tab">Over Ruled</a></li>
										  <li id="pfm-li"><a href="#p-f-m"  onclick="javascript:initDashboard('7','p-f-m');" data-toggle="tab">Pending for Mail</a></li>
										  <li><a href="#d" onclick="javascript:initDashboard('4','d');"  data-toggle="tab">Disposed</a></li>
										</ul>
										<div class="tab-content">
										 	<div class="tab-pane active" id="f"></div>
										 	<div class="tab-pane" id="r-r"></div>							 	
										 	<div class="tab-pane" id="r-s"></div>							 	
										 	<div class="tab-pane" id="o-r"></div>
										 	<div class="tab-pane" id="p-f-m"></div>
										 	<div class="tab-pane" id="d"></div>
										</div>
								</div>
							 </div>
						</div>							 						
						<div class="tab-pane" id="office-tab">							
							<div class="row">								
							 	<div class="col-xs-12 pp-form-field" id="office-tab-details">			 		
							 			<ul class="nav nav-pills pull-right">
										  <li class="active"><a href="#o-f" onclick="javascript:initOfficeDashboard('1','o-f');" data-toggle="tab">Fresh</a></li>
										  <li><a href="#o-r-r" onclick="javascript:initOfficeDashboard('2','o-r-r');" data-toggle="tab">Reply Received</a></li>							  
										  <li><a href="#o-u-p" onclick="javascript:initOfficeDashboard('6','o-u-p');" data-toggle="tab">Under Process</a></li>							  
										  <li><a href="#o-r-s" onclick="javascript:initOfficeDashboard('3','o-r-s');" data-toggle="tab">Reply Sent</a></li>										  
										  <li><a href="#o-d" onclick="javascript:initOfficeDashboard('4','o-d');"  data-toggle="tab">Disposed</a></li>
										</ul><br/><br/>
										<div class="tab-content">
										 	<div class="tab-pane active" id="o-f"></div>
										 	<div class="tab-pane" id="o-r-r"></div>							 	
										 	<div class="tab-pane" id="o-u-p"></div>							 	
										 	<div class="tab-pane" id="o-r-s"></div>										 	
										 	<div class="tab-pane" id="o-d"></div>
										</div>
								</div>
							 </div>							 							 									 
						</div>						
				</div>
		    </div> 		    
		</div>
		<div class="row">
		  	<div class="col-xs-10 pp-form-field" id="app-info" style="display:none;">						  	
		  		<div class="panel panel-info">
				  <div class="panel-heading">
				  	<button type="button" id="back-btn" class="btn btn-primary btn-xs" title="Back to application list" onclick="javascript:initBack();">
			    	<span class="fa fa-arrow-circle-left fa-lg"></span>&nbsp;</button>	
			    	<button type="button" id="reset-btn" class="btn btn-primary btn-xs" title="Back to application" onclick="javascript:resetApplication();">
			    	<span class="fa fa-arrow-circle-left fa-lg"></span>&nbsp;</button>				  				  	
				  	&nbsp;&nbsp;<span id="basic-header"></span>				  	
				  </div>				  
				  <div class="panel-body">
				    <div class="row">
				    	<div class="col-sm-2">
							Application Id<p class="text-danger" id="bi-appId"></p>
						</div>											
						<div class="col-sm-1">
							Service<p class="text-danger" id="bi-service"></p>
						</div>			
				    	<div class="col-sm-2">
							Submission Date<p class="text-danger" id="bi-date"></p>
						</div>
						<div class="col-sm-1">
							Status<p class="text-danger" id="bi-phase"></p>
						</div>
						
						<div class="col-sm-2">
							Application Details<p class="text-danger" id="bi-more-options"></p>
						</div>
						<div class="col-sm-2">
							Documents Detail<br><p class="text-danger" id="bi-doc" style="display: inline;"></p>
							<div class="inline" id="cert-divv" style="display:none;">
							<p class="text-danger" id="bi-docc"></p>
							</div>
							<div class="inline" id="del-divv" style="display:none;">
							<p class="text-danger" id="del-docc"></p>
							</div> 
						</div>
						<div class="col-sm-2" id="cert-div" style="display:none;">
							View Certificate<p class="text-danger" id="bi-cert"></p>
						</div>		
					</div>	
					<div class="row">
						<span class="badge"></span>
					</div>	
				  </div>
				</div>
		  	</div>
		  	<div class="col-xs-2 pp-form-field" style="display:none;" id="timer-info">	
		  		<div style="background: url('resources/css/services/dashboard/images/blank-calendar.png') no-repeat; height:95%;width:71%; text-align: center;">
		  			<span style="color:#fff"><span class="fa fa-clock-o fa-lg" style="padding-top:5px;">
		  			</span></span><h2 class="text-default"><b><span id="timer-text"></span></b></h2><small id="timer-status"></small></br></br>	
		    	</div>
		 	</div>
		 </div>		
		 <div class="row" id="application-list-div" style="display:none;">		 			 	
		 	<div class="col-xs-12 pp-form-field">
		 		<div class="bs-callout bs-callout-info">
					<span>Search Result</span>
				</div>				
		 		<div id="app-list"></div>
		 		<button type="button" id="back-to-workspace-btn" class="btn btn-info" onclick="javascript:backToWorkSpace();">
			    <span class="fa fa-arrow-circle-left fa-lg"></span>&nbsp;</button>		 		
		 	</div>
		 </div> 
		 <div class="row" id="process-info">
		     <div class="col-xs-9 pp-form-field" id="chat-list">    
		     </div>							    
		    <!--  <div class="col-xs-3 pp-form-field" id="office-status-list">							     
		     </div> -->
		     <div class = "col-xs-3 pp-form-field">
		     <div id="office-status-list" class = "row"></div>
		     <div id="office-mark-list" class = "row" style="display:none;"></div>
		     </div>
	     </div>	     					     
		 <div class="row">
	     	<br/><br/>
		     <div class="col-xs-12 pp-form-field" id="actions" style="display:none;">						          
			      <button type="button" id="forward-user-btn" class="btn btn-primary" onclick="clearModal();" data-toggle="modal" data-target="#userModal" style="display:none;">
			      <span class="fa fa-user"></span>&nbsp;Forward to User</button>
			      <button type="button" id="forward-office-btn" class="btn btn-warning" onclick="clearModal();" data-toggle="modal" data-target="#officeModal" style="display:none;">
			      <span class="fa fa-institution"></span>&nbsp;Forward to Office</button>
			      <button type="button" id="add-note-btn" class="btn btn-default active" onclick="clearModal();" data-toggle="modal" data-target="#noteModal" style="display:none;">
			      <span class="glyphicon glyphicon-pencil"></span>&nbsp;Add Note</button>
			      <button type="button" id="onhold-btn"  class="btn btn-danger" onclick="clearModal();" data-toggle="modal" data-target="#holdModal" style="display:none;">
			      <span class="glyphicon glyphicon-warning-sign"></span>&nbsp;On Hold</button>
			      <button type="button" id="clarification-btn" class="btn btn-info" onclick="clearModal();" data-toggle="modal" data-target="#clarificationModal" style="display:none;">
			      <span class="glyphicon glyphicon-edit"></span>&nbsp;Request Clarification to Applicant</button>
			      <button type="button" id="show-cause-btn" class="btn btn-info" onclick="clearModal();" data-toggle="modal" data-target="#showCauseModal" style="display: none;">
			      <span class="glyphicon glyphicon-edit"></span>&nbsp;Issue Show Cause Notice</button>			      
			      <button type="button" id="resume-btn" style="display:none;" class="btn btn-success" onclick="clearModal();" data-toggle="modal" data-target="#resumeModal" style="display:none;">
		      	 <span class="glyphicon glyphicon-repeat"></span>&nbsp;Resume Processing</button>								      
			      <!-- <button type="button" id="next-stage-btn" class="btn btn-success" onclick="clearModal();" data-toggle="modal" data-target="#nextStageModal">  -->
			      <button type="button" id="next-stage-btn" class="btn btn-success" onclick="proceedToApprovalModal();" style="display:none;">
			      <span class="glyphicon glyphicon-ok"></span>&nbsp;Approve</button>
			      <button type="button" id="reject-btn" class="btn btn-danger" onclick="clearModal();" data-toggle="modal" data-target="#rejectModal" style="display:none;">
			      <span class="fa fa-close"></span>&nbsp;Refuse</button>
			      <button type="button" id="close-btn" class="btn btn-default active" onclick="clearModal();" data-toggle="modal" data-target="#closeModal" style="display:none;">
			      <span class="fa fa-close"></span>&nbsp;Reject / Close</button>			      
		     </div>		
		     <div class="col-xs-12 pp-form-field" style="display: none;" id="reminder-div">
		     	<button type="button"  class="btn btn-warning" onclick="clearModal();"  data-toggle="modal" data-target="#reminderModal">
	      	 	<span class="fa fa-bell-o"></span>&nbsp;Send Reminder</button>
		     </div>	     
	     </div>
	     <div class="row">
		   <div class="col-xs-3 pp-form-field" id="hos-pdf-format-div" style="display:none;"></br>
		   	<form id="hos-pdf-format-form" method="post">
		   		<label for="name" class="control-label">Select Pdf Format:</label>						   		
		  		<select id="hos-pdf-format" class="form-control validate[required,masSize[2]]">
		  			<option value="">Select format</option>
		  			<option value="1">Letter</option>
		  			<option value="2">Office Memorandum</option>
		  		</select>
		  	</form>					   		
		   </div>
	     </div>  	
	     <div class="row">
	     	<div class="col-xs-12 pp-form-field" id="mail-actions" style="display:none;">						          
			      <button type="button" id="mail-action-btn" class="btn btn-success" onclick="initPreviewCertificate();">
			      <span class="glyphicon glyphicon-envelope"></span>&nbsp;Sign Certificate and Send</button>								      
		     </div>							    
	     </div>
	     <div class="modal fade bs-example-modal-lg" id="userModal"  tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="forward-user-form" method="post">	    	
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="userModal-error">
					  			</div>
							</div>
				 		</div>							        	
			        	<div class="row">
          						<div class="col-sm-6 pp-form-field">
          							<label for="forwardUser" class="control-label">Select User:</label>
						            <select id="forwardUser" name="forwardUser" title="Please select user to forward. It's required." 
						            class="form-control clear validate[required,maxSize[10]] title-t">												 													 	
									</select>	
          						</div>
          						<div class="col-sm-3 pp-form-field col-sm-offset-1"></br>
          							<button type="button" class="btn btn-default active title-t" id="section-btn" onclick="getSectionList();"
          							title="Clicking this button enables you to select user belongs to other sections. It's optional.">
		      						<span class="glyphicon glyphicon-edit"></span>&nbsp;Change Section
		      					</button>							      					
					            <select id="userSection" style="display:none;" name="userSection" 
					            	title="Please select section." onchange="getSectionUsers(this.value);"
					            	class="form-control clear validate[maxSize[2]] title-t">												 													 	
								</select>
          						</div>		            						
          					</div>
          					<div upload-plugin="user-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx" up-file-size="1000000" >
							<div up-file-selector="user-upload" class="col-xs-2"></div>
							<br/>
							<div up-file-list="user-upload" id="file-list" class="col-xs-8"></div>
						</div>											
       					<div class="row">
       						<div class="col-sm-10">
	        					<label for="forwardUserRemark" class="control-label">Status Remark:</label>
					            <textarea  class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
					            title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" cols="70" id="forwardUserRemark" name="forwardUserRemark"></textarea>	
       						</div>		            						
       					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="userModal-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="userModal-submit-btn" onclick="javascript:submitForwardUser();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="noteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Noting Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="user-note-form" method="post">
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="noteModal-error">
					  			</div>
							</div>
				 		</div>      	
			       		<div upload-plugin="note-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx" up-file-size="1000000" >
							<div up-file-selector="note-upload" class="col-xs-2"></div>
							<br/>
							<div up-file-list="note-upload" id="file-list" class="col-xs-8"></div>
						</div>											
          					<div class="row">
          						<div class="col-sm-10">
          							<label for="userNote" class="control-label">Note:</label>
					            <textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
					             title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" id="userNote" name="userNote"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="noteModal-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="noteModal-submit-btn" onclick="javascript:submitUserNote();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="clarificationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Clarification Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="clarification-form" method="post">
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="clarificationModal-error">
					  			</div>
							</div>
				 		</div>      	
			       		<div upload-plugin="clarification-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx" up-file-size="1000000" >
							<div up-file-selector="clarification-upload" class="col-xs-2"></div>
							<br/>
							<div up-file-list="clarification-upload" id="file-list" class="col-xs-8"></div>
						</div>											
       					<div class="row">
       						<div class="col-sm-10">
       							<label for="userNote" class="control-label">Remark:</label>
					            <textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
					             title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" id="clarificationRemark" name="clarificationRemark"></textarea>	
       						</div>		            						
       					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="clarificationModal-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="clarificationModal-submit-btn" onclick="javascript:submitUserClarification();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		
		
		<div class="modal fade bs-example-modal-lg" id="showCauseModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Show Cause Notice Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="showcause-form" method="post">
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="showCauseModal-error">
					  			</div>
							</div>
				 		</div>     	
			       		
						<div class="row">
							<div class="col-sm-8 pp-form-field">
								<label for="" class="control-label">Notice Subject:</label>
								<input type="text"  class="form-control clear validate[required,maxSize[500],custom[remarkAddress]]" id="noticeSubject" name="noticeSubject" />
							</div>
						</div>											
       					<div class="row">
       						<div class="col-sm-12 pp-form-field">
       							<label for="" class="control-label">Notice Body:</label>
       							<p class="help-block"><span class="text-danger">Dear Sir/Madam,</span></p>
					            <textarea class="form-control validate[required,maxSize[3000],custom[remarkAddress]] title-t" 
					             title="Please enter notice body. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 3000 characters." rows="7" id="noticeBody" name="noticeBody"></textarea>
					             <p class="help-block pull-right pp-form-field"><span class="text-danger">Your's faithfully</span></p>	
       						</div>		            						
       					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="showCauseModal-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="showCauseModal-submit-btn" onclick="javascript:submitShowCause();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		
		<div class="modal fade bs-example-modal-lg" id="holdModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">OnHold Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="hold-form" method="post">
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="on-hold-error">
					  			</div>
							</div>
				 		</div>      	
			       		<div upload-plugin="onhold-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx" up-file-size="1000000" >
							<div up-file-selector="onhold-upload" class="col-xs-2"></div>
							<br/>
							<div up-file-list="onhold-upload" id="file-list" class="col-xs-8"></div>
						</div>											
          					<div class="row">
          						<div class="col-sm-10">
          						<label for="userNote" class="control-label">Remark:</label>
					            <textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
					             title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" id="onholdRemark" name="onholdRemark"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="onhold-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="onhold-submit-btn" onclick="javascript:submitUserOnHold();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="resumeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Resume Processing Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="resume-form" method="post">
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="resume-error">
					  			</div>
							</div>
				 		</div>      	
			       		<div upload-plugin="resume-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx" up-file-size="1000000" >
							<div up-file-selector="resume-upload" class="col-xs-2"></div>
							<br/>
							<div up-file-list="resume-upload" id="file-list" class="col-xs-8"></div>
						</div>											
          					<div class="row">
          						<div class="col-sm-10">
          						<label for="userNote" class="control-label">Remark:</label>
					            <textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
					             title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" id="resumeRemark" name="resumeRemark"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="resume-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="resume-submit-btn" onclick="javascript:submitResumeProcess();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade  bs-example-modal-lg" id="officeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="forward-office-form" method="post">
		            <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="officeModal-error">
					  			</div>
							</div>
				 		</div>								        	
			        	<div class="row">
          						<div class="col-md-6 pp-form-field">
          							<label for="forwardOffice" class="control-label">Select Office:</label>
					            <select id="forwardOffice" name="forwardOffice" class="form-control clear validate[required,maxSize[5]]">												 													 	
								</select>	
          						</div>		            						
          					</div>
          					<div upload-plugin="office-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx" up-file-size="1000000" >
							<div up-file-selector="office-upload" class="col-xs-2"></div>
							<br/>
							<div up-file-list="office-upload" id="file-list" class="col-xs-8"></div>
						</div>											
          					<div class="row">
          						<div class="col-sm-10 pp-form-field">
          							<label for="forwardOfficeRemark" class="control-label">Status Remark:</label>
					            <textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" rows="3"
					             title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." id="forwardOfficeRemark" name="forwardOfficeRemark"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="officeModal-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="officeModal-submit-btn" onclick="javascript:submitForwardOffice();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="nextStageModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="next-stage-form" method="post">
		        	<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
		        	<input type="hidden" name="matchFound" id="match-found" />
		        	<input type="hidden" name="redFlagClearingRemarks" id="red-flag-clearing-remarks" />
		            <input type="hidden" name="nextStageId" id="nextStageId" />
		            <input type="hidden" name="nextStageStatus" id="nextStageStatus" />
		            <input type="hidden" name="pdfFormat" id="pdfFormat" />
		            <input type="hidden" name="svcCode" id="svcCode" />
		            <input type="hidden" name="validFrom" id="validFrom" />							          
		            <input type="hidden" name="ppAmount" id="ppAmount" />
		            <input type="hidden" name="ppInsFlag" id="ppInsFlag" />
		            <input type="hidden" name="ppAmountCurrency" id="ppAmountCurrency" />
		            <input type="hidden" name="currentDateString" id="currentDateString" />
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="nextStageModal-error">
					  			</div>
							</div>
				 		</div> 
				 		<div class="row">
						 	<div class="col-xs-3 pp-form-field" id="pdf-format-div" style="display:none;">
						   		<label for="name" class="control-label">Select Pdf Format:</label>						   		
						  		<select id="pdf-Format" class="form-control validate[required,masSize[2]]">
						  			<option value="">Select format</option>
						  			<option value="1">Letter</option>
						  			<option value="2">Office Memorandum</option>
						  		</select>					   		
						   </div>
					     </div>
					     <div class="row" id="validity-div" style="display:none;">
						 	<div class="col-xs-2 pp-form-field">
						   		<label for="name" class="control-label">Validity From:</label>						   		
						  		<input type="text" data-mask="99-99-9999" onchange="javascript:initValidity();" class="form-control clear validate[required,date,futureEqualField[validFrom]]" id="validityFrom" name="validityFrom" />					   		
						   </div>
						   <div class="col-xs-2 pp-form-field" id="validity-div">
						   		<label for="name" class="control-label">Validity UpTo:</label>						   		
						  		<input type="text" data-mask="99-99-9999" readonly class="form-control clear validate[required,date,futureField[currentDateString]]" id="validityUpTo" name="validityUpTo" />					   		
						   </div>
					     </div> 
					     <div class="row" id="pp-amount-section" style="display:none;">
					     	<div class="col-xs-6">
						   		<label for="name" class="control-label">Total contribution from Donor: <span id="ppAmountDesc" class="label label-warning" style="font-size: 15px;"></span></label>				   								  							   		
						   </div>						   
					     </div> 
					     <div class="row">
					     	<div class="col-xs-6 pp-form-field" id="pp-amount-flag-div" style="display:none;">
					     		<label><input type="checkbox" id="pp-amount-flag" onclick="initPPInstallmentFlag();"/><span class="text-danger"><strong>&nbsp;proceed with installments?</strong></span></label>
					     	</div>
					     	<div class="col-xs-3" id="pp-installment-div" style="display:none;">
						   		<label for="name" class="control-label">No. of Installments</label>						   		
						  		<input type="text"  onkeyup="javascript:initPPInstallments();" class="form-control clear validate[custom[integer] min[1] max[10]]" id="installmentNumbers" name="installmentNumbers" />					   		
						   </div>						   
					     </div>  
					     <div class="row">
					     	<div class="col-xs-5 pp-form-field">
						   		<div id="installment-table"></div>					   		
						   </div>
					     </div>	
					     <div class="row">
					    	 <div class="col-sm-10 pp-form-field"  id="otherRemark-div" style="display: none;">
          							<label for="otherRemark"  class="control-label"><p id="other-remark-text"></p></label>
					            	<textarea class="form-control validate[maxSize[999],custom[remarkAddress]] title-t" rows="3"
					             		title="Please enter remark. It's required for Registraion. It is alphanumeric filed. Special characters are also allowed.
					             		Max. 1000 characters." id="otherRemark" name="otherRemark">
					             	</textarea>	
          						</div>
					     </div>
						<div class="row">												
          						<div class="col-sm-10 pp-form-field">
          							<label for="forwardAgentRemark" class="control-label">Status Remark:</label>
					            	<textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" rows="3"
					             		title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             		Max. 2000 characters." id="forwardAgentRemark" name="forwardAgentRemark">
					             	</textarea>	
          						</div>		            						
       					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" id="nextStageModal-close-btn" onclick="javascript:resetModal();" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="nextStageModal-submit-btn" onclick="javascript:submitNextStage();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="rejectModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="reject-form" method="post">
			        <div class="container-fluid">	
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="rejectModal-error">
					  			</div>
							</div>
				 		</div> 			 		
			        	<div class="row">
							<div class="col-sm-10 pp-form-field">
       							<label for="forwardOfficeRemark" class="control-label">Status Remark:</label>
		            			<textarea class="form-control validate[required]" rows="3" id="rejectRemark" name="rejectRemark"></textarea>	
       						</div>		            						
       					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" id="rejectModal-close-btn" onclick="javascript:resetModal();" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="officeModal-submit-btn" onclick="javascript:submitReject();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="closeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="close-form" method="post">
			        <div class="container-fluid">	
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="closeModal-error">
					  			</div>
							</div>
				 		</div>  	
			        	<div class="row">
          						<div class="col-sm-10 pp-form-field">
          						<label for="forwardOfficeRemark" class="control-label">Status Remark:</label>
					            <textarea class="form-control validate[required]" rows="3" id="closeRemark" name="closeRemark"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" id="closeModal-close-btn" onclick="javascript:resetModal();" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="closeModal-submit-btn" onclick="javascript:submitClose();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="reminderModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Reminder Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="close-form" method="post">
			        <div class="container-fluid">	
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="reminderModal-error">
					  			</div>
							</div>
				 		</div> 
				 		<div upload-plugin="reminder-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx" up-file-size="1000000" >
							<div up-file-selector="reminder-upload" class="col-xs-2"></div>
							<br/>
							<div up-file-list="reminder-upload" id="file-list" class="col-xs-8"></div>
						</div> 	
			        	<div class="row">
          						<div class="col-sm-10 pp-form-field">
          						<label for="forwardOfficeRemark" class="control-label">Remark:</label>
          						 <p class="help-block"><span class="text-danger">Please note that this remark will be sent as the mail content to the applicant.</span></p>
					            <textarea class="form-control validate[required]" rows="3" id="reminderRemark" name="reminderRemark"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" id="reminderModal-close-btn" onclick="javascript:resetModal();" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="reminderModal-submit-btn" onclick="javascript:submitCLarificationReminder();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade bs-example-modal-lg" id="fetchModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Forwarding Details</h4>
		      </div>
		      <div class="modal-body">				      
		        <form id="reject-form" method="post">
			        <div class="container-fluid">	
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="fetchModal-error">
					  			</div>
							</div>
				 		</div>  	
			        	<div class="row">
          						<div class="col-sm-10 pp-form-field">
          							<label for="forwardOfficeRemark" class="control-label">Pull Remark:</label>
					            	<textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
					            title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
					             Max. 2000 characters." rows="3" id="pullRemark" name="pullRemark"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" id="fetchModal-close-btn" onclick="javascript:resetModal();" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="fetchModal-submit-btn" onclick="javascript:pullApplication();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade" id="documentModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Supporting Documents</h4>
		      </div>
		      <div class="modal-body">
		        <div class="row">
        			<div class="col-sm-12 pp-form-field">
			            <div id="project-doc-list">
          					</div>			
          				</div>
          			</div>	
		      </div>
		      <div class="modal-footer">							      	
		      		<button type="button" class="btn btn-danger"  data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>  	
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade" id="affidavitModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">UPLOADED AFFIDAVIT OF EACH KEY FUNCTIONARY</h4>
		      </div>
		      <div class="modal-body">
		        <div class="row">
        			<div class="col-sm-12 pp-form-field">
			            <div id="affidavit-doc-list">
          					</div>			
          				</div>
          			</div>	
		      </div>
		      <div class="modal-footer">							      	
		      		<button type="button" class="btn btn-danger"  data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>  	
		      </div>
		    </div>
		  </div>
		</div>
		<div class="modal fade" id="deletedocumentModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">DELETED DOCUMENT OF EACH KEY FUNCTIONARY</h4>
		      </div>
		      <div class="modal-body">
		        <div class="row">
        			<div class="col-sm-12 pp-form-field">
			            <div id="delete-doc-list">
          					</div>			
          				</div>
          			</div>	
		      </div>
		      <div class="modal-footer">							      	
		      		<button type="button" class="btn btn-danger"  data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>  	
		      </div>
		    </div>
		  </div>
		</div>
		</div>
		<div id="applet-container"></div>	
	 </div>	 
		 <form id="office-tab-form" method="post">
		 	<div class="row">
			 	<div class="col-xs-3 pp-form-field" id="office-tab-user-list-div" style="display:none;">
			   		<label for="name" class="control-label">Select User:</label>						   		
			  		<select id="officeTabForwardUser" name="officeTabForwardUser" class="form-control validate[required,masSize[10]]"></select>					   		
			   </div>
		     </div>		
		     <div class="row">							 					     	
			     <div class="col-xs-10 pp-form-field" id="office-tab-actions" style="display:none;">								     								     	  						          
				      <button type="button" id="off-tab-accept-app-btn" onclick="javascript:acceptApplication();" class="btn btn-success">
				      <span class="fa fa-check-circle-o"></span>&nbsp;Accept</button>
				      <button type="button" id="off-tab-reject-app-btn" class="btn btn-danger" style="display: none;">
				      <span class="fa fa-close"></span>&nbsp;Reject</button>
				      <button type="button" id="off-tab-pull-app-btn"  class="btn btn-success" data-toggle="modal" data-target="#fetchModal">
				      <span class="fa fa-arrow-circle-o-down"></span>&nbsp;Fetch Application</button>								      
			     </div>
		     </div>
		 </form>	
		<form method="get" id="chat-attachment-form">
	    	<input type="hidden" name="rowId" id="rowId"/>
	    </form>		    
	    <form method="get" id="final-order-form">
	    	<input type="hidden" name="appId" id="appId"/>
	    	<input type="hidden" name="regNumber" id="regNumber"/>
	    	<input type="hidden" name="svcCode" id="svcCode"/>
	    </form>
    	<div class="modal fade bs-example-modal-lg" id="pdfModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">			      
		      <div class="modal-body">
				<iframe id="PrintFrame" onload="javascript:EnableConfirmationPane();" align="top"  src="" style="zoom:0.60" frameborder="2"  height="500" width="99.6%"></iframe>
			  </div>
		      <div class="modal-footer">
			      	<div id="sign-btn" style="display:none;">	
			      		<button type="button" class="btn btn-primary"  onclick="javascript:initCertificateSignAndMail();" data-dismiss="modal">
			      		<span class="fa fa-close"></span>&nbsp;Sign Certificate and Send</button>						      	
			      		<button type="button" id="pdfModal-close" class="btn btn-danger"  data-dismiss="modal">
			      		<span class="fa fa-close"></span>&nbsp;Close</button>
			      	</div>  	
		      </div>
		    </div>
		  </div>
		</div>
		<button type="button" class="btn btn-primary" id="pending-mail-btn" data-toggle="modal" style="display:none;" data-target="#mailAlertModal"></button>
		<div class="modal fade" id="mailAlertModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;<b><span class="text-warning">Warning</span></b></h4>
		      </div>			      
		      <div class="modal-body" id="mailAlertMoadl-body">
				
			  </div>
		      <div class="modal-footer">		      								      	
		      		<button type="button" id="" class="btn btn-default"  data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>  	
		      </div>
		    </div>
		  </div>
		</div>
	</div>
	<form action="" id="signed-pdf-form" method="post">
	</form>	
	
	<div class="modal fade" id="red-flag-list-modal" tabindex="-1" role="dialog" aria-labelledby="red-flag-list-modal-label" data-keyboard="false" data-backdrop="static">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="red-flag-list-modal-label">Matching Red-flag list</h4>
	      </div>
	      <div class="modal-body">
	        <div class="row">
       			<div class="col-sm-12 pp-form-field">
       				<div id="red-flag-list-header" class="alert alert-danger">
						<strong>Warning !!</strong>&nbsp;We have got the following matches against <strong>red-flagged associations</strong>. Please cross-check before proceeding further.<br>       				
       				</div>
		            <div id="red-flag-list-table">
         			</div>			
         		</div>
         		<div class="col-sm-12 pp-form-field">
       				<div id="red-flag-list-donor-header" class="alert alert-warning">
						<strong>Warning !!</strong>&nbsp;We have got the following matches against <strong>red-flagged donors</strong>. Please cross-check before proceeding further.<br>       				
       				</div>
		            <div id="red-flag-list-donor-table">
         			</div>			
         		</div>
         	</div>	
	      </div>
	      <div class="modal-footer" id="red-flag-footer">
	      		<form action="" id="pre-approval-form" method="post">
	      		<div class="row">
		      		<div class="col-sm-6">
			      		<label for="name" class="control-label">Do you want to proceed for approval?:</label>
			      		<label class="radio-inline">
							<input type="radio" name="proceedToApproval" id="proceed-to-approval-1" class="validate[required]" value="Y"> Yes
						</label>
						<label class="radio-inline">
							<input type="radio" name="proceedToApproval" id="proceed-to-approval-2" class="validate[required]" value="N"> No
						</label>
					</div>
					<div class="col-sm-4" style="display: none;" id="red-flag-remarks-section">
		           		<textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]]" placeholder="Enter remarks" rows="3" id="red-flag-remarks" name="redFlagRemarks">
		           		</textarea>	
					</div>		            						
					
					<!-- 
		      		<button type="button" class="btn btn-primary" onclick="openApprovalModal();" data-dismiss="modal">
		      		<span class="glyphicon glyphicon-ok"></span>&nbsp;Proceed to approval</button>							      	
		      		<button type="button" class="btn btn-default"  data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		      		 -->
		      		 <div class="col-sm-2 pull-right">
			      		<button type="button" class="btn btn-primary" onclick="finalizeProceedToApproval();">
			      		<span class="glyphicon glyphicon-ok"></span>&nbsp;Ok</button>
		      		</div>							      	
	      		</div>
	      		</form>   	
	      </div>
	    </div>
	  </div>
	</div>
	
	 <div class="modal fade bs-example-modal-lg" id="markOfficeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Mark </h4>
		      </div>
		      <div class="modal-body">	
		       <input type="hidden" name="markOfficeId" id="markOfficeId" value="" />			      
		        <form id="markOfficeModal-form" method="post">
			        <div class="container-fluid">	
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="markOfficeModal-error">
					  			</div>
							</div>
				 		</div>  	
			        	<div class="row">
          						<div class="col-sm-10 pp-form-field">
          						<label for="markOfficeRemark" class="control-label">Remark:</label>
					            <textarea class="form-control validate[required]" rows="3" id="markRemark" name="markRemark"></textarea>	
          						</div>		            						
          					</div>
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	
		      		<button type="button" class="btn btn-default" id="markOfficeModal-close-btn" onclick="javascript:resetModal();" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="markOfficeModal-submit-btn" onclick="javascript:submitMarkRemark();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      						        
		      </div>
		    </div>
		  </div>
		</div>      
 </body>
 <script  type="text/javascript">		
		$('#validityFrom').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
		$('#validityUpTo').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
	</script>
</html>