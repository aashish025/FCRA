<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css?Version=${version}" type="text/css" />	
	<script type="text/javascript" src="resources/js/all.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/auth/home/user-notify.js?Version=${version}"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/common.css?Version=${version}" />
	<script src="resources/js/iframeResizer.contentWindow.min.js?Version=${version}" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/reports/blue-flagged-rcn.js?Version=${version}"></script>	
	<script type="text/javascript" src="resources/js/utility/uploader.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css?Version=${version}" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css?Version=${version}" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js?Version=${version}"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
        <script type="text/javascript" src="resources/js/bootgrid/bootlocalgrid.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css?Version=${version}" type="text/css"/>	
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css?Version=${version}" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js?Version=${version}"></script>
	<title>Blue Flagged RCNs</title>
</head>
<body id="">
	<div class="container-fluid">
			<input type="hidden" name="roleIddeleteGrantBlue" value="${roleIddeleteGrantBlue}" id="roleIddeleteGrantBlue"/> 
		<div class="content-section">
			<div style="z-index:1; position: absolute; right: 5px; top: 80px; width: 300px;" id="sticky-notify"></div>
		    <div class="row">
			    <div class="col-xs-12">
			    	<div id="bar-notify"></div>
			    </div>
		    </div>
		    <div class="row" id="report-button">
			    <div class="col-xs-12">
			    	<div id="text-notify"></div>
			    	<br />
			    	<div  align="right" >
			   			   <button type="button" class="btn btn-link btn-sm"  title="Download details as PDF" onclick="javascript:PrintPdf();">
      						<span class="text-danger fa fa-file-pdf-o fa-2x" ></span> </button>
      						
      						<button type="button" class="btn btn-link btn-sm"  title="Download details as Excel file" onclick="javascript:PrintExcel();">
      						<span class="text-success fa fa-file-excel-o fa-2x"></span> </button>
      						
      						<button type="button" class="btn btn-link btn-sm" title="Download details as Csv file" onclick="javascript:PrintCSV();">
      						<span class="text-info fa fa-file-excel-o fa-2x" ></span>  </button>
      						
      						<c:if test="${roleIddeleteGrantBlue==14}"> 
      						<button type="button" class="btn btn-link btn-sm" id="options" title="More Options " onclick="javascript:showOptions(); " >
      						<span class="glyphicon glyphicon-menu-hamburger rotate" style="padding-top:10px; padding-bottom:10px;font-size:30px; cursor:pointer;"></span>  </button>
      						</c:if>
     						</div>
      						
      						
   					 
			    </div>
		    </div>
		    <div class="form-group" id ="red-flag-div" style="display: block;">
		    <div class="bs-callout bs-callout-info" id="headingforredyellowflag">
						<span>Registered Associations which are already listed  in Blue Flag</span>
					</div>
		   
			   	<div class="row">
					<div class="col-xs-11 pp-form-field" id="blue-flagged-list">
					</div>
			    </div>
		     </div>
		    <div class="">
		<form action="" id="report-print-form" method="post">
		<div class="form-group">
			<div class="row">
				<div class="col-xs-2 pp-form-field" id="add-btn" style="display:none;">
					<button type="button" class="btn btn-primary" onclick="javascript:addDetails();">
						 Search Rcns	</button>
				</div>
			</div>
		</div>
		</form>
	</div>
		<div class="row" style="display:none;" id="back-section">
				<div class="col-xs-11 pp-form-field">
					<button type="button" class="btn btn-default active"  id="go-back-btn" onclick="javascript:goBack();"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;Back</button>
				</div>

			</div></br>
					
	<div class="row" style="display:none;" id="searching-onclick-button">
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
									
						<div class="col-sm-1">
							<button class="btn btn-info" onclick="javascript:getAdvanceSearchApplicationList();"><span class="fa fa-search"></span>&nbsp;Search</button>
						</div>
	    			</div>  
	    			<div class="col-sm-1" id="adv-search-button-div">
	    				<button class="btn btn-link" id="toggle-btn" onclick="javascript:toggleSearch();"><span class="fa fa-search"></span>&nbsp;Advance Search</button>
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
			  	<div class="col-xs-12 pp-form-field" id="app-info" style="display:none;">	
			  		<div class="bs-callout bs-callout-info">
						<span id="asso-details-title">Details of Association</span>
					</div>
						<form action="" id="report-print-form22" method="post">
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
					</div>
					</form>
					<div id="red-flag-details" style="display:none;">
						<div class="bs-callout bs-callout-danger">
							<span id="asso-details-title">Blue Flagging Details</span>
						</div>	
						<div class="row" id="red-flag-details-2">
							<div class="col-sm-3">
									Action On<p class="text-danger" id="statusDate"></p>
							</div>	
							<div class="col-sm-5">
									Remarks<p class="text-danger" id="remark"></p>
							</div>								
						</div>
					</div>	
					<div id="annual-status-details">
						<div class="bs-callout bs-callout-info">
							<span id="asso-details-title">Annual Return Status</span>
						</div>	
						 	<div class="row">
					<div class="col-xs-5 pp-form-field" id="annual-status-list">
					</div>
						</div>
						
						</div>
					<form action="" id="submit-button-form" method="post">
					<div id="delete-blueFlag" class="form-inline" style="display:none;">		
						<div class="row">
						<c:if test="${roleIddeleteGrantBlue==14}">
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:removeListforModal('2');">
									<span class="fa fa-close"></span>Remove From Blue Flag List</button>					
							</div>
							</c:if>
							
						</div>											
					</div>
					</form>
					<div class="modal fade bs-example-modal-lg" id="removeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
					  <div class="modal-dialog modal-lg" role="document">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="javascript:resetAll();"><span aria-hidden="true">&times;</span></button>
					        <h4 class="modal-title" id="exampleModalLabel">Remove
									Rcns Details from Blue Flag List</h4>
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
					        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Remove</button>
					      	</div>					        
					      </div>
					    </div>
					  </div>
					</div>
				
					
		</div>
	</div>
			<div class="modal fade bs-example-modal-lg" id="annualModal"
				tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close" onclick="javascript:resetAll();">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="exampleModalLabel1"> Blue Flagged Associations who has filed Annual Returns</h4>
						</div>
						<div class="modal-body">
							<form id="add1-form" method="post">
								<div class="container-fluid">
									<div class="row">
										<div class="col-xs-12">
											<div id="addModal1-error"></div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-12 pp-form-field" id="annual1-status-list">
									</div>
								</div>
							</form>
						</div>
						<div class="modal-footer">
							<div id="create-user-div">
								<button type="button" class="btn btn-default"
									id="addModal1-close-btn" data-dismiss="modal" onclick="">
									<span class="fa fa-close"></span>&nbsp;Close
								</button>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
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

 