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
	<link rel="stylesheet" type="text/css" href="resources/css/services/dashboard/project-dashboard.css" />
	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js"></script>		
	<script type="text/javascript" src="resources/js/services/dashboard/investigation-agency.js"></script>
	<script type="text/javascript" src="resources/js/services/dashboard/nicEdit.js"></script>
	<script type="text/javascript" src="resources/js/utility/uploader.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
	<script type="text/javascript" src="resources/js/bootgrid/bootlocalgrid.js"></script>
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>
	<title>Investigation Agency Report</title>	
</head>

<body id="">	
	<div class="container-fluid">
		<div class="content-section">
			<div class="row">			
			    	<div class="col-xs-12" id="notification-area"></div>			  
		    </div>
			<div style="z-index:1; position: absolute; right: 5px; top: 80px; width: 300px;" id="sticky-notify"></div>
		    <div class="row">
			    <div class="col-xs-12">
			    	<div id="bar-notify"></div>
			    </div>
		    </div>
		    <div class="row">		 
			    	<div class="col-xs-12" id="text-notify"></div>			    
		    </div>
		   <div id="applet-div">
		    <div class="row">		    	  			
    				<div class="col-sm-3" id="appid-search">     					
			           <div class="input-group" id="app-search">
							<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Application Id or Section FileNo to search." placeholder="Section FileNo or Application Id" name="applicationId" id="applicationId" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-info title-b" onclick="javascript:getApplicationList();" title="Click to submit"><span class="fa fa-search"></span>&nbsp;</button>
								</div>																	
							</input>				
						</div>
						
					 </div>     				   				
    			</div> 		         						
		</div>
		
		<div class="row" id="application-list-div" style="display:none;">	
			<div class="bs-callout bs-callout-info">
				<span>Search Result</span>
			</div>	 			 		 						
		 		<div class="col-xs-10 pp-form-field" id="app-list"></div>
		 	</div>
	
	   	
	  	<div class="row" id="app-info" style="display:none;">
	  	<div class="bs-callout bs-callout-info">
					<span>Details of Application</span>
			          </div>	
	  		<div class="col-xs-10 pp-form-field" >	    					  	
		  		<div class="panel panel-info">  
				  <div class="panel-body">
				    <div class="row">
				    	<div class="col-sm-2">
							Application Id<p class="text-danger" id="bi-appId"></p>
						</div>	
						<div class="col-sm-2">
							Temporary File No<p class="text-danger" id="bi-tempFileNo"></p>
						</div>	
						<div class="col-sm-2">
							Section File No<p class="text-danger" id="bi-sectionFileNo"></p>
						</div>	
						<div class="col-sm-2">
							Applicant Name<p class="text-danger" id="bi-appName"></p>
						</div>											
						<div class="col-sm-2">
							Service<p class="text-danger" id="bi-service"></p>
						</div>	
					</div>	
					  <div class="row">		
				    	<div class="col-sm-2">
							Submission Date<p class="text-danger" id="bi-date"></p>
						</div>
						<div class="col-sm-2">
							Status<p class="text-danger" id="bi-phase"></p>
						</div>
						<div class="col-sm-2">
							State<p class="text-danger" id="bi-state"></p>
						</div>
						<div class="col-sm-2">
							District<p class="text-danger" id="bi-district"></p>
						</div>
						<div class="col-sm-2">
							Application Details<p class="text-danger" id="bi-more-options"></p>
						</div>	
					</div>
					</div>
				</div>	
			</div>				
		  	</div>	   		
			<div class="row" id="off-status-info" style="display:none;">		  
				<div class="bs-callout bs-callout-info">				
					<span>Details of Application status in Other offices</span>
				     </div>	
			     <div class="col-xs-6 pp-form-field" id="office-status-list"></div>
					</div>	
			 
			 <div class="row" id="actions" style="display:none;" >
		     <div class="col-xs-12 pp-form-field" >	
				<button type="button" id="add-note-btn" class="btn btn-default active" onclick="clearModal();" data-toggle="modal" data-target="#addInvestigationModal">
			      <span class="glyphicon glyphicon-pencil"></span>&nbsp;Update Investigation Agency Report</button>
	        </div>
			</div>	
			
			<div class="modal fade bs-example-modal-lg" id="addInvestigationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" onclick="resetModal();" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Investigation Agency Report</h4>
		      </div>
		      <div class="modal-body">			    
		        <form id="investigation-received-form" method="post">
			        <div class="container-fluid">
			        	<div class="row">
					  		<div class="col-xs-12">
					  			<div id="addInvestigationModal-error">
					  			</div>
							</div>
				 		</div>      	
			       		
	       		     <div class="col-xs-3 pp-form-field" id="investAgencyListDiv">
						<label for="investAgencyList">Select Office</label> <select
							id="investAgencyList" name="investAgencyList"
							class="form-control clear validate[required] title-l">
							<option value="">Select Office</option>
							<c:forEach items="${officeNameList}" var="nType">								    
								<option value='<c:out value="${nType.k}"/>'>
								<c:out value="${nType.v}" /></option>
							</c:forEach>
						</select>
					 </div>  
                  	 <div class="col-xs-6 pp-form-field" id="reportNoDiv">
							<label for="reportNo">Report Number </label> <input type="text"
								name="reportNo" id="reportNo"
								class="form-control validate[required, maxSize[50] custom[remarkAddress]] clear" style="text-transform: uppercase;"/>
						</div>
			       		
			       		<div class="col-xs-3 pp-form-field">
							<label for="reportDate" class="control-label">Report Date
								</label> <input type="text"
								class="form-control validate[required,custom[date]]"
								name="reportDate" id="reportDate"></input>
						 </div>
			       		
			        </div>    
		      </form>
		      </div>
		      <div class="modal-footer">
		      	<div id="create-user-div">
		      		<button type="button" class="btn btn-default" onclick="resetModal();" id="investigationModal-close-btn" data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>
		        	<button type="button" class="btn btn-success" id="noteModal-submit-btn" onclick="javascript:submitInvestigationReport();">
		        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
		      	</div>					        
		      </div>
		    </div>
		  </div>
		  </div>
		  </div>
		  </div>							        
 </body>
 <script type="text/javascript">		        
 $('#reportDate').datetimepicker({
		lang:'ch',
		timepicker:false,
		format:'d-m-Y',
		formatDate:'dd-mm-yyyy',
		maxDate:'0'
	});
</script>
</html>
