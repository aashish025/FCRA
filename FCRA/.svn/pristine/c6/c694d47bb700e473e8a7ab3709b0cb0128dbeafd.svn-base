<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
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
	<script src="resources/js/iframeResizer.contentWindow.min.js?Version=${version}" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js?Version=${version}"></script>		
	<script type="text/javascript" src="resources/js/reports/registration-tracking.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/services/dashboard/nicEdit.js?Version=${version}"></script>	
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css?Version=${version}" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js?Version=${version}"></script>		
	<script type="text/javascript" src="resources/js/bootgrid/bootlocalgrid.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css?Version=${version}" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css?Version=${version}" />	
	<script src="resources/js/languages/jquery.validationEngine-en.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css?Version=${version}" type="text/css"/>
	<script type="text/javascript" src="resources/js/barchart/jquery.jqplot.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.barRenderer.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.categoryAxisRenderer.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.pointLabels.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.canvasAxisTickRenderer.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.canvasAxisLabelRenderer.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.canvasTextRenderer.js?Version=${version}"></script>	
	<link rel="stylesheet" href="resources/css/barchart/jquery.jqplot.css?Version=${version}" />	
	<title>FCRA Dashboard</title>
	<script>
		var myDetails = '${myDetails}';		
		var recordsPendingForMail = '${recordsPendingForMail}';
		//bkLib.onDomLoaded(function() { nicEditors.allTextAreas() });	
	</script>	
	
		<script type="text/javascript">
			$(document).ready(function() {
				var rId = '${registrationId}';
				var u = window.location.href.split('/').pop();
				if (u.substring(0, 6) == "popup-") {
					$('#applicationId').val(rId);
					$("#appid-search").hide();
				    $("#adv-search-button-div").hide();
					$("#app-list").hide();
					 getRegistrationDetails(rId);
				}

			});
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
				           <div class="input-group">
								<input type="text" 	class="form-control validate[maxSize[15]]" title="Please enter registration number." placeholder="Registration Number" name="applicationId" id="applicationId" style="text-transform: uppercase;">
									<div class="input-group-btn">									
										<button class="btn btn-info title-b" onclick="javascript:getApplicationList();" title="Click to submit"><span class="glyphicon glyphicon-download"></span>&nbsp;</button>
									</div>																	
								</input>				
							</div>						 
	   				</div>
	   				<div id="advance-search" style="display:none;">
	   					<div class="col-sm-2">		    			    					
				            <select id="state" name="state" onchange="javascript:getDistrict(this.value);"  
				            	class="form-control clear validate[maxSize[1]] title-l">
				            	<option value="">Select State</option>
							 	<c:forEach items="${stateList}" var="gType">
							 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
							 	</c:forEach>			            													 													 	
							</select>	
	    				</div>
	    				<div class="col-sm-2">     					
				            <select id="district" name="district" class="form-control clear validate[maxSize[1]] title-l">	
				            <option value="">Select District</option>
						 	<c:forEach items="${districtList}" var="gType">
						 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
						 	</c:forEach>											 													 	
							</select>	
	    				</div> 
	    				 <div class="col-sm-3">						
							<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Association Name to search." placeholder="Association Name" name="applicationName" id="applicationName" style="text-transform: uppercase;"></input>				
						 </div> 
						 <div class="col-sm-2">						
							<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Functionary Name to search." placeholder="Functionary Name" name="functionaryName" id="functionaryName" style="text-transform: uppercase;"></input>				
						 </div> 					
						<!-- <div class="col-sm-4" id="text-input-search-div">     					
				           <div class="input-group" id="association-search">
								<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Association Name to search." placeholder="Association Name" name="applicationName" id="applicationName" style="text-transform: uppercase;">
									<div class="input-group-btn">
										<button class="btn btn-default active title-b" id="" title="toggle to search with Association Name or Functionary Name" onclick="javascript:toggleSearchByInput();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>										
									</div>																	
								</input>				
							</div>
							 <div class="input-group" id="functionary-search" style="display:none;">
								<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Functionary Name to search." placeholder="Functionary Name" name="functionaryName" id="functionaryName" style="text-transform: uppercase;">
									<div class="input-group-btn">
										<button class="btn btn-default active title-b" id="" title="toggle to search with Association Name or Functionary Name" onclick="javascript:toggleSearchByInput();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>										
									</div>																								
								</input>				
							</div>
	    				</div>  	 -->				
						<div class="col-sm-1">
							<button class="btn btn-info" onclick="javascript:getAdvanceSearchApplicationList();"><span class="fa fa-search"></span>&nbsp;Search</button>
						</div>
	    			</div>  
	    			<div class="col-sm-1" id="adv-search-button-div">
	    				<button class="btn btn-link" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="fa fa-search"></span>&nbsp;Advance Search</button>
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
			  	<div class="col-xs-12 pp-form-field" id="app-info" style="display:none;">	
			  		<div class="bs-callout bs-callout-info">
						<span id="asso-details-title">Details of Association</span>
					</div>
			  		<div class="row">
					    	<div class="col-sm-3">
								Registration/PP Number<p class="text-danger" id="regnNumber"></p>
							</div>		
							<div class="col-sm-3">
								Registration/PP Date<p class="text-danger" id="regnDate"></p>
							</div>											
							<div class="col-sm-3">
								Section FileNumber<p class="text-danger" id="secFileNumber"></p>
							</div>
							<div class="col-sm-3">
								Current Status<p class="text-danger" id="currentStatus"></p>
							</div>					    															
					</div>	
					<div id="reg-cancellation-details" style="display: none;">
						<div class="row">
							<div class="col-sm-3">
									Cancelled Date<p class="text-danger" id="canc-date"></p>
							</div>	
							<div class="col-sm-3">
									Cancel Type<p class="text-danger" id="canc-type"></p>
							</div>					    											
							<div class="col-sm-3">
									Remark(If any)<p class="text-danger" id="canc-remark"></p>
							</div>	
							<div class="col-sm-3">
									Reason<p class="text-danger" id="canc-reason"></p>
							</div>	
						</div>	
					</div>
					<div class="row">
						<div class="col-sm-3">
								Association Name<p class="text-danger" id="applicantName"></p>
						</div>	
						<div class="col-sm-3">
								Association Address<p class="text-danger" id="assoAddress"></p>
						</div>					    											
						<div class="col-sm-3">
								Association Nature<p class="text-danger" id="assoNature"></p>
						</div>	
						<div class="col-sm-3">
								Download Details<p class="text-danger" id="bi-doc"></p>
						</div>					
					</div>
					<div class="row">
						<div class="col-sm-3">
								Last Renewed<p class="text-danger" id="lastRenewed"></p>
						</div>	
						<div class="col-sm-3">
								Valid UpTo<p class="text-danger" id="validUpTo"></p>
						</div>	
						<div id="exemptedDiv" style="display:none;">
						<div class="col-sm-4">
						       Exempted Association? <p class="text-danger" id="exempted"></p>
						</div>
						</div>
							
					</div>
									
					<div id="red-flag-details" style="display:none;">
						<div class="bs-callout bs-callout-danger">
							<span id="asso-details-title">Adverse Details</span>
						</div>	
						<div class="row">
							<div class="col-sm-3">
									Flagged By<p class="text-danger" id="orgOffice"></p>
							</div>	
							<div class="col-sm-3">
									Originator Order / File No.<p class="text-danger" id="orgOrderNumber"></p>
							</div>					    											
							<div class="col-sm-3">
									Flagged On<p class="text-danger" id="orgOrderDate"></p>
							</div>	
							<div class="col-sm-3">
									Action By<p class="text-danger" id="actionBy"></p>
							</div>					
						</div>	
						<div class="row" id="red-flag-details-2">
							<div class="col-sm-3">
									Reason For Flagging<p class="text-danger" id="category"></p>
							</div>
							<div class="col-sm-3">
									Action On<p class="text-danger" id="statusDate"></p>
							</div>	
							<div class="col-sm-5">
									Remarks<p class="text-danger" id="remark"></p>
							</div>								
						</div>
					</div>	
					<!-- <div class="row">						
						<div class="col-sm-6">
							<button type="button" id="remove-red-flag-btn" style="display:none;" data-toggle="modal" data-target="#removeModal" class="btn btn-danger">
			      			<span class="glyphicon glyphicon-remove"></span>&nbsp;Remove From Adverse List</button>
			      			<button type="button" id="add-red-flag-btn" style="display:none;" data-toggle="modal" data-target="#addModal" class="btn btn-danger">
			      			<span class="glyphicon glyphicon-plus"></span>&nbsp;Add to Adverse List</button>
						</div>										
					</div> --></br>					
					<div class="bs-callout bs-callout-info">
						<span>Bank Details</span>
					</div>	
					<div class="row">
						<div class="col-sm-3">
								Bank Name<p class="text-danger" id="assoBank"></p>
						</div>	
						<div class="col-sm-3">
								Bank Address<p class="text-danger" id="assoBankAddress"></p>
						</div>					    											
						<div class="col-sm-3">
								Account Number<p class="text-danger" id="assoAccNumber"></p>
						</div>											
					</div>
					<div class="bs-callout bs-callout-info" id="committee-section" style="display:none;">
						<span>Committee Members</span>
					</div>
					<div class="row">
						<div class="col-sm-11 pp-form-field" id="executive-committee-table">																				 
						</div>	
					</div>
					<button type="button" id="comm-mem-modal-btn" class="btn btn-primary" style="display:none;" data-toggle="modal" data-target="#committeeModal">
			      	</button>
			      	<div class="modal fade bs-example-modal-lg" id="committeeModal"  tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
					  <div class="modal-dialog modal-lg" role="document">
					    <div class="modal-content">
					      <div class="modal-header">					       
					        <h4 class="modal-title" id="exampleModalLabel">Committee Member Details</h4>
					      </div>
					      <div class="modal-body" id="committee-modal-body">				      
					       		<div class="row">
							    	<div class="col-sm-3">
										Name<p class="text-danger" id="name"></p>
									</div>		
									<div class="col-sm-3">
										Name of Father/Husband<p class="text-danger" id="fhName"></p>
									</div>											
									<div class="col-sm-3">
										Nationality<p class="text-danger" id="nationality"></p>
									</div>	
									<div class="col-sm-3">
										Aadhar No. If Any<p class="text-danger" id="aadhar"></p>
									</div>														    															
							</div>	
							<div class="row">
									<div class="col-sm-3">
											Occupation<p class="text-danger" id="occupation"></p>
									</div>	
									<div class="col-sm-3">
											Designation in Association<p class="text-danger" id="assoDesig"></p>
									</div>					    											
									<div class="col-sm-3">
											RelationShip with Office Bearers<p class="text-danger" id="relnWthBrer"></p>
									</div>	
									<div class="col-sm-3">
											Office Address<p class="text-danger" id="offAdd"></p>
									</div>					
							</div>	
							<div class="row">
									<div class="col-sm-3">
											Residence Address<p class="text-danger" id="resiAdd"></p>
									</div>	
									<div class="col-sm-3">
											Email<p class="text-danger" id="email"></p>
									</div>					    											
									<div class="col-sm-3">
											Phone<p class="text-danger" id="phone"></p>
									</div>	
									<div class="col-sm-3">
											Mobile No.<p class="text-danger" id="mobile"></p>
									</div>					
							</div>
							<div class="row">
									<div class="col-sm-3">
											Date of Birth<p class="text-danger" id="dob"></p>
									</div>	
									<div class="col-sm-3">
											Place of Birth<p class="text-danger" id="pob"></p>
									</div>					    											
									<div class="col-sm-3">
											Passport Number<p class="text-danger" id="passport"></p>
									</div>	
									<div class="col-sm-3">
											Address in Foreign Country<p class="text-danger" id="addFrgn"></p>
									</div>					
							</div>	
							<div class="row">
									<div class="col-sm-3">
											Person of Indian Origin?<p class="text-danger" id="indianOrigin"></p>
									</div>	
									<div class="col-sm-3">
											PIO/OCI Card Number<p class="text-danger" id="pici"></p>
									</div>	
									<div class="col-sm-3">
											Resident in India<p class="text-danger" id="resiIndia"></p>
									</div>
									<div class="col-sm-3">
											Date from which residing in India<p class="text-danger" id="dateResiIndia"></p>
									</div>					
							</div>			
					      </div>
					      <div class="modal-footer">					      						        
					      </div>
					    </div>
					  </div>
					</div>
					<div class="modal fade bs-example-modal-lg" id="addModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
					  <div class="modal-dialog modal-lg" role="document">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="javascript:resetAll();"><span aria-hidden="true">&times;</span></button>
					        <h4 class="modal-title" id="exampleModalLabel">Submit Details</h4>
					      </div>
					      <div class="modal-body">				      
					        <form id="add-form" method="post">
						        <div class="container-fluid">	
						        	<div class="row">
								  		<div class="col-xs-12">
								  			<div id="addModal-error">
								  			</div>
										</div>
							 		</div> 
							 		<div class="row">
		          						<div class="col-sm-5 pp-form-field">
		          								<label for="forwardOfficeRemark" class="control-label">Select Reason For Flagging:</label>
		          								<select id="redFlagCategory" name="redFlagCategory" class="form-control clear validate[required,maxSize[1]] title-l">
									            	<option value="">Select Reason</option>												 			            													 													 	
												</select>	 
		          						</div>
		          						<div class="col-sm-6 pp-form-field">
		          								<label for="forwardOfficeRemark" class="control-label">Flagged By:</label>
		          							 	<input type="text" class="form-control validate[required,maxSize[50]]" name="originatorOffice" id="originatorOffice" style="text-transform: uppercase;"></input> 
		          						</div>
		          					</div>							 		
		          					<div class="row">
		          						<div class="col-sm-5 pp-form-field">
		          								<label for="forwardOfficeRemark" class="control-label">Originator Order / File No.:</label>
		          							 	<input type="text" class="form-control validate[required,maxSize[50]]" name="orderNumber" id="orderNumber"></input> 
		          						</div>
		          						<div class="col-sm-2 pp-form-field">
		          								<label for="forwardOfficeRemark" class="control-label">Flagged On:</label>
		          							 	<input type="text"  data-mask="99-99-9999" class="form-control validate[required,maxSize[50]]" name="orderDate" id="orderDate"></input> 
		          						</div>
		          					</div> 	
						        	<div class="row">
		          						<div class="col-sm-10 pp-form-field">
		          							<label for="forwardOfficeRemark" class="control-label">Remarks:</label>
							            	<textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
							            title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
							             Max. 2000 characters." rows="3" id="addStatusRemark" name="addStatusRemark"></textarea>	
		          						</div>		            						
		          					</div>
						        </div>    
					      </form>
					      </div>
					      <div class="modal-footer">
					      	<div id="create-user-div">
					      		<button type="button" class="btn btn-default" id="addModal-close-btn" data-dismiss="modal" onclick="">
					      		<span class="fa fa-close"></span>&nbsp;Close</button>
					        	<button type="button" class="btn btn-success" id="addModal-submit-btn" onclick="javascript:addToRedFlagList();">
					        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
					      	</div>					        
					      </div>
					    </div>
					  </div>
					</div>
					<div class="modal fade bs-example-modal-lg" id="removeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
					  <div class="modal-dialog modal-lg" role="document">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="javascript:resetAll();"><span aria-hidden="true">&times;</span></button>
					        <h4 class="modal-title" id="exampleModalLabel">Submit Details</h4>
					      </div>
					      <div class="modal-body">				      
					        <form id="remove-form" method="post">
						        <div class="container-fluid">	
						        	<div class="row">
								  		<div class="col-xs-12">
								  			<div id="removeModal-error">
								  			</div>
										</div>
							 		</div> 
							 		<div class="row">		          						
		          						<div class="col-sm-6 pp-form-field">
		          								<label for="forwardOfficeRemark" class="control-label">Flagged By:</label>
		          							 	<input type="text" class="form-control validate[required,maxSize[50]]" name="originatorOffice" id="originatorOfficeR" style="text-transform: uppercase;"></input> 
		          						</div>
		          					</div>							 		
		          					<div class="row">
		          						<div class="col-sm-5 pp-form-field">
		          								<label for="forwardOfficeRemark" class="control-label">Originator Order / File No.:</label>
		          							 	<input type="text" class="form-control validate[required,maxSize[50]]" name="orderNumber" id="orderNumberR"></input> 
		          						</div>
		          						<div class="col-sm-2 pp-form-field">
		          								<label for="forwardOfficeRemark" class="control-label">Flagged On:</label>
		          							 	<input type="text"  data-mask="99-99-9999" class="form-control validate[required,maxSize[50]]" name="orderDate" id="orderDateR"></input> 
		          						</div>
		          					</div> 	 	
						        	<div class="row">
		          						<div class="col-sm-10 pp-form-field">
		          							<label for="forwardOfficeRemark" class="control-label">Remarks:</label>
							            	<textarea class="form-control validate[required,maxSize[2000],custom[remarkAddress]] title-t" 
							            	title="Please enter remark. It's required. It is alphanumeric filed. Special characters are also allowed.
							             	Max. 2000 characters." rows="3" id="removeStatusRemark" name="removeStatusRemark"></textarea>	
		          						</div>		            						
			          				</div>
						        </div>    
					      </form>
					      </div>
					      <div class="modal-footer">
					      	<div id="create-user-div">
					      		<button type="button" class="btn btn-default" id="removeModal-close-btn" data-dismiss="modal" onclick="">
					      		<span class="fa fa-close"></span>&nbsp;Close</button>
					        	<button type="button" class="btn btn-success" id="removeModal-submit-btn" onclick="javascript:removeFromRedFlagList();">
					        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
					      	</div>					        
					      </div>
					    </div>
					  </div>
					</div>
					<div class="bs-callout bs-callout-info" id="annual-return-section" style="display:none;">
						<span>Annual Return Details</span>
					</div>	
					<div class="row">
						<div class="col-sm-12 pp-form-field" id="return-table">																				 
						</div>										 						 		
			  		</div>
			  		<div class="row">				  		
			  			<div class="col-sm-8 pp-form-field">						  						
				  			  <div id="chart-div"></div>						   
					  	</div>
					  	<div class="col-sm-1 pp-form-field">
					  		  <button type="button" title="Click to see bar graph" class="btn btn-link btn-sm" id="bar-chart-btn" onclick="javascript:prepareBarchart();">
					  		  		<span class="text-info fa fa-bar-chart fa-2x"></span>
					  		  </button>		
					  		  <button type="button" title="Click to see line graph" style="display:none;" class="btn btn-link btn-sm" id="line-chart-btn" onclick="javascript:prepareTrendchart();">
					  		  		<span class="text-danger fa fa-line-chart fa-2x"></span>
					  		  </button>						   
					  	</div>
				 	</div>	
			  		<div class="bs-callout bs-callout-info" id="rcn-history-section" style="display:none;">
						<span>RCN History Details</span>
					</div>	
					<div class="row">
						<div class="col-sm-8 pp-form-field" id="history-table">																				 
						</div>	
					</div>			
					
					<div class="bs-callout bs-callout-info" id="sms-section" style="display:none;">
						<span>SMS Details</span>
					</div>	
					<div class="row">
						<div class="col-sm-11 pp-form-field" id="sms-table">																				 
						</div>	
					</div>	
					
					<div class="bs-callout bs-callout-info" id="mail-section" style="display:none;">
						<span>MAIL Details</span>
					</div>	
					<div class="row">
						<div class="col-sm-11 pp-form-field" id="mail-table">																				 
						</div>	
					</div>	
					<div class="modal fade bs-example-modal-lg" id="mailBodyModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
					  <div class="modal-dialog modal-lg" role="document">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="javascript:resetAll();"><span aria-hidden="true">&times;</span></button>
					        <h4 class="modal-title" id="exampleModalLabel">Mail Details</h4>
					      </div>
					      <div class="modal-body">   
					        <div id="mail-body-section">
					        </div>
					      </div>
					      <div class="modal-footer">					      	
					      		<button type="button" class="btn btn-default" id="addModal-close-btn" data-dismiss="modal"><span class="fa fa-close"></span>&nbsp;Close</button>   	
					      </div>
					    </div>
					  </div>
					</div>
					<div class="bs-callout bs-callout-info" id="grievance-detail-section" style="display:none;">
						<span>Grievances Details</span>
					</div>	
					<div class="row">
						<div class="col-sm-12 pp-form-field" id="grievances-table">																				 
						</div>										 						 		
			  		</div>	  		
			  		
				</div>							  	
		 	</div>		 	 
		</div>	
		<form action="" id="report-download-form" method="post">
		</form>       
 </body> 
 <script  type="text/javascript">		
		$('#orderDate').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});	
		$('#orderDateR').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});	
	</script>
</html>
