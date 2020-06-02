<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

	<script type="text/javascript" src="resources/js/jquery-1.11.3.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js?Version=${version}"></script>	
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css?Version=${version}" type="text/css" />			
	<script type="text/javascript" src="resources/js/input-mask/input-mask.js?Version=${version}"></script> 
	<script type="text/javascript" src="resources/js/all.js?Version=${version}"></script>	
	<link rel="stylesheet" type="text/css" href="resources/css/common.css?Version=${version}" />	
	<script src="resources/js/iframeResizer.contentWindow.min.js?Version=${version}" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js?Version=${version}"></script>	
	<script type="text/javascript" src="resources/js/services/communication.js"></script>
	<script type="text/javascript" src="resources/js/utility/uploader.js"></script>
	<!-- <script type="text/javascript" src="resources/js/services/dashboard/nicEdit.js"></script>	 -->
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/bootgrid/bootlocalgrid.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css?Version=${version}" />
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css?Version=${version}" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css?Version=${version}"/>	
	<script src="resources/js/languages/jquery.validationEngine-en.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css?Version=${version}" type="text/css"/>	
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js?Version=${version}"></script>
	
	<title>FCRA Dashboard</title>
	<script>
		var myDetails = '${myDetails}';		
		var recordsPendingForMail = '${recordsPendingForMail}';				
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
		    <div id="communication-type-section">
		    	<h4>Please select one of the option(s).</h4> 
			    <div class="row">		    	 	
			    	<div class="col-sm-3">
			    		<label class="radio-inline">
						  <input type="radio" name="communicationType" onclick="checkCommunicationType(this.value);" id="bulk" value="0">
						  <i class="fa fa-users" aria-hidden="true"></i>&nbsp;<strong><span class="text-danger">Bulk</span></strong></input>
						</label>
						<label class="radio-inline">
						  <input type="radio" name="communicationType" onclick="checkCommunicationType(this.value);" id="individual" value="1">
						  <i class="fa fa-male" aria-hidden="true"></i>&nbsp;<strong><span class="text-danger">Individual</span></strong></input>
						</label>							
			    	</div>			    	
			    </div>	
		    </div>
		    <br />
		    <div id="note-section" style="display: none;" class = "row">
		    	<!-- <h4>Note:<small>This option will send Email/SMS to all <strong>ACTIVE</strong> association(s)/user(s) having Email Id's and Mobile Numbers.</small></h4> -->
		    	<div class = "col-sm-6" id = "selected-rcn-div" style="display: none">
		    	<textarea id = "selected-rcn" name = "selected-rcn" readonly="readonly" style="resize:vertical;" class="form-control text-uppercase validate[required, custom[remarkAddress]]"></textarea> </div>
		    	 <div class = "col-sm-2"> <button type="button" id="add-association" class="btn btn-info" onclick="addAssociations();" data-toggle="modal" data-target="#addAssociationModal">
			      <span class="fa fa-plus"></span>&nbsp;Select Associations</button></div>
			      <div class = "col-sm-2"> <button type="button" id="clear-association" class="btn btn-danger" onclick="clearAssociations();" style="display: none;">
			      <span class="fa fa-minus"></span>&nbsp;Clear Selections</button></div>
		    </div>
		    <div id="individual-section" style="display: none;">
		    	<br />	  		  
				<div class="row">
					<div class="col-sm-3" id="appid-search">     					
			           <div class="input-group" id="reg-search">
							<input type="text" 	class="form-control validate[maxSize[15]]" title="Please enter registration number." placeholder="Registration Number" name="registrationNumber" id="registrationNumber" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-default active" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>
									<button class="btn btn-info title-b" onclick="javascript:getApplicationListByRCN();" title="Click to submit"><span class="fa fa-search"></span>&nbsp;</button>
								</div>																	
							</input>				
						</div>
						 <div class="input-group" id="app-search" style="display:none;">
							<input type="text" 	class="form-control validate[maxSize[15]]" title="Please enter Application Id" placeholder="Application Id" name="applicationId" id="applicationId" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-default active title-b" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="glyphicon glyphicon-chevron-right"></span>&nbsp;</button>
									<button class="btn btn-info" onclick="javascript:getApplicationListByAppId();"><span class="fa fa-search"></span>&nbsp;</button>
								</div>																								
							</input>				
						</div>
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
					  	Association Details				  	
					  </div>				  
					  <div class="panel-body">
					    <div class="row">
					    	<div class="col-sm-2">
								RCN / Application Id<p class="text-danger" id="regnNumber"></p>
							</div>											
							<div class="col-sm-2">
								Section FileNumber<p class="text-danger" id="secFileNumber"></p>
							</div>											
					    	<div class="col-sm-4">
								Association/Applicant Name<p class="text-danger" id="applicantName"></p>
							</div>	
							<div class="col-sm-3">
								Chief Functionary Name<p class="text-danger" id="functionaryName"></p>
							</div>																						
						</div>
						<div class="row">
							<div class="col-sm-2">
								State<p class="text-danger" id="state"></p>
							</div> 
							<div class="col-sm-2">
								District<p class="text-danger" id="district"></p>
							</div> 
							<div class="col-sm-3">
								Email Id<p class="text-danger" id="email"></p>
							</div> 
							<div class="col-sm-2">
								Mobile<p class="text-danger" id="mobile"></p>
							</div> 
						</div>							
					  </div>
					</div>
			  	</div>			  	
		 	</div>		
		 	 <div class="row">
	     	<br/><br/>
		     <div class="col-xs-12 pp-form-field" id="actions" style="display:none;">						          
			      <button type="button" id="cancel-btn" class="btn btn-danger" onclick="openMail();" data-toggle="modal" data-target="#mailModal">
			      <span class="fa fa-envelope"></span>&nbsp;Mail</button>
			      <button type="button" id="revoke-btn" class="btn btn-success" onclick="clearModal();" data-toggle="modal" data-target="#smsModal">
			      <span class="fa fa-comment"></span>&nbsp;SMS</button>			      
		     </div>	
		     <div class="modal fade bs-example-modal-lg" id="mailModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">Mail Details</h4>
			      </div>
			      <div class="modal-body">				      
			        <form id="mail-form" method="post">	
			        	<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>		        	
				        <div class="container-fluid">	
				        	<div class="row">
						  		<div class="col-xs-12">
						  			<div id="mailModal-error">
						  			</div>
								</div>
					 		</div>  
					 		<div class="row">							 	
							   <div class="col-xs-7 pp-form-field">
							   		<label for="name" class="control-label">Subject:</label>						   		
							  		<input type="text" class="form-control validate[required,date]" id="mail-subject" name="mailSubject" />					   		
							   </div>
						     </div>						     	
				        	<div class="row">			        			
          						<div class="col-sm-10 pp-form-field">
          							<label for="" class="control-label">Mail Body:</label>
					            	<textarea class="form-control validate[required,maxSize[2000]] title-t" rows="3" id="mail-body" name="mailBody"></textarea>	
          						</div>		            						
	          				</div>
	          				<div upload-plugin="mail-upload" class="row pp-form-field"  up-file-types="pdf,doc,docx,xls,xlsx" up-file-size="1000000" >
								<div up-file-selector="mail-upload" class="col-xs-2"></div>
								<br/>
								<div up-file-list="mail-upload" id="file-list" class="col-xs-8"></div>
							</div>
				        </div>    
			      </form>
			      </div>
			      <div class="modal-footer">
			      	<div id="create-user-div">
			      		<button type="button" class="btn btn-default" id="mailModal-close-btn" data-dismiss="modal" onclick= "javascript:clearModal();">
			      		<span class="fa fa-close"></span>&nbsp;Close</button>
			        	<button type="button" class="btn btn-success" id="mailModal-submit-btn" data-loading-text="Submitting..." onclick="javascript:sendMail();">
			        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
			      	</div>					        
			      </div>
			    </div>
			  </div>
			</div>		
			<div class="modal fade bs-example-modal-lg" id="smsModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">SMS Details</h4>
			      </div>
			      <div class="modal-body">				      
			        <form id="sms-form" method="post">			        	
				        <div class="container-fluid">	
				        	<div class="row">
						  		<div class="col-xs-12">
						  			<div id="smsModal-error">
						  			</div>
								</div>
					 		</div>				 							     	
				        	<div class="row">			        			
          						<div class="col-sm-10 pp-form-field">
          							<label for="" class="control-label">SMS Body:</label>
					            	<textarea class="form-control validate[required,maxSize[160]] title-t" rows="3" id="sms-body" name="smsBody"></textarea>	
          						</div>		            						
	          				</div>	          				
				        </div>    
			      </form>
			      </div>
			      <div class="modal-footer">
			      	<div id="create-user-div">
			      		<button type="button" class="btn btn-default" id="smsModal-close-btn" data-dismiss="modal">
			      		<span class="fa fa-close"></span>&nbsp;Close</button>
			        	<button type="button" class="btn btn-success" id="smsModal-submit-btn" data-loading-text="Submitting..." onclick="javascript:sendSMS();">
			        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
			      	</div>					        
			      </div>
			    </div>
			  </div>
			</div>
			<div class="modal fade bs-example-modal-lg" id="addAssociationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">Add Association</h4>
			      </div>
			      <div class="modal-body">				      
			        <form id="addAssociation-form" method="post">	
			        	<input type="hidden" name="selected-hidden-rcn"  id="selected-hidden-rcn"/>		        	
				        <div class="container-fluid">	
				        	<div class="row">
						  		<div class="col-xs-12">
						  			<div id="addAssociationModal-error">
						  			</div>
								</div>
					 		</div>  
					 		<div class="row">							 	
							   <div class="col-xs-5 pp-form-field" id="association-div">
									<label for="selectAssociation">Select Association : </label> 
									<select	id="selectAssociation" name="selectAssociation"	class="form-control">
										<option value="">Select </option>
								 		<option value="1">Active associations</option>
								 		<option value="2">Associations not filed ARs</option>
								 		<option value="3">Suspended Associations</option>
								 		<option value="4">Active associations with All State (COVID-19)</option>
								 	</select>
								</div>
						   </div>	
						   <br />					     	
				        	<div class = "row">
				        	<div class="col-sm-3" style = "display:none;" id = "assoState-div">		    
				        	<label for="assoState">State : </label> 			    					
				            		<select id="assoState" name="assoState" onchange="javascript:getDistrict(this.value);"  
				            			class="form-control clear  title-l">
				            			<option value="">All State</option>
							 			<c:forEach items="${stateList}" var="gType">
							 			<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
							 			</c:forEach>			            													 													 	
									</select>	
	    					</div>
	    					<div class="col-sm-3" style = "display:none;" id = "assoDistrict-div">     	
	    					<label for="assoDistrict">District : </label> 						
				            	<select id="assoDistrict" name="assoDistrict" class="form-control clear  title-l" onchange= "javascript:showHide();">	
				            		<option value="">All District</option>
						 			<c:forEach items="${districtList}" var="gType">
						 			<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
						 			</c:forEach>											 													 	
								</select>	
	    					</div> 
	    					<div class="col-sm-3" id = "assoBlockYear-div" style = "display:none;">     
	    					<label for="assoBlockYear">Block Year : </label>					
				            	<select id="assoBlockYear" name="assoBlockYear" class="form-control clear validate[required,maxSize[9]] title-l" onchange= "javascript:showHide();">	
				            		<option value="">Select Block Year</option>
						 			<c:forEach items="${blockYearList}" var="gType">
						 			<option value='<c:out value="${gType.k}"/>'><c:out value="${gType.v}"/></option>
						 			</c:forEach>											 													 	
								</select>	
	    					</div>
	    					<div class="col-sm-1" style = "display:none;" id = "search-div">
	    					<br />
								<button class="btn btn-info" id = "searchAssociation"onclick="javascript:getAdvanceSearchApplicationList();" type = "button" data-loading-text="Searching....."><span class="fa fa-search"></span>&nbsp;Search</button>
							</div>
				        	</div>
				        	<br />
				        	<div id = "bulk-application-list" class = "row" ></div>
				        </div>    
			      </form>
			      </div>
			      <div class="modal-footer" id="add-association-footer-div" style = "display:none;">
			      		<button type="button" class="btn btn-default" id="add-associtation-close-btn" onclick = "javascript:clearSelection();" style = "display:none" >
			      		<span class="fa fa-close"></span>&nbsp;Clear Selection</button>
			        	<button type="button" class="btn btn-info" id="add-associtation-btn" data-loading-text="Adding..." onclick="javascript:addAssociation();" style = "display:none">
			        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Add Selected Items to List</button>	 
			        	<button type="button" class="btn btn-success" id="add-all-associtation-btn" data-loading-text="Adding..." onclick="javascript:addAllAssociation();" style = "display:none">
			        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Add All Items to List</button>	       
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
