<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js"></script>	
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />			
	<script type="text/javascript" src="resources/js/input-mask/input-mask.js"></script> 
	<script type="text/javascript" src="resources/js/all.js"></script>	
	<link rel="stylesheet" type="text/css" href="resources/css/common.css" />	
	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js"></script>		
	<script type="text/javascript" src="resources/js/reports/donor-detail.js"></script>
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />	
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
	<title>Donor Details</title></head>

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
					
	   					<div class="col-sm-3">		    			    					
				            <select id="country" name="country"   
				            	class="form-control clear " multiple="multiple">
				            	<option value="" disabled>Select Country</option>
				            	<option value="ALL2">ALL</option>
							 	<c:forEach items="${countryList}" var="gType">
							 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
							 	</c:forEach>			            													 													 	
							</select>	
	    				</div>
	    				<div>
	    				
	    				</div>
	   					<div class="col-sm-2">		    			    					
				            <select id="state" name="state"  onchange="javascript:getAssociation(this.value);"   
				            	class="form-control validate[required] clear " multiple="multiple">
				            	<option value="" disabled>Select State  </option>
				            		<option value="ALL2">ALL</option>
							 	<c:forEach items="${stateList}" var="gType">
							 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
							 	</c:forEach>			            													 													 	
							</select>	
	    				</div>
	    				<div class="col-sm-3">     					
				            <select id="association" name="association" class="form-control clear " multiple="multiple">	
				            <option value="" disabled>Select Association</option>
						 	<c:forEach items="${associationList}" var="gType">
						 		<option value='<c:out value="${gType.li}"/>'><c:out value="${gType.ld}"/></option>
						 	</c:forEach>											 													 	
							</select>	
	    				</div>  					
				          	<div class="col-sm-2">						
							<input type="text" 	class="form-control validate[maxSize[15]] title-b" title="Please enter Donar Name to search." placeholder="Donor Name" name="donorName" id="donorName" style="text-transform: uppercase;"></input>				
						</div>	
						<div class="col-sm-1">
							<button class="btn btn-info" onclick="javascript:getSearch();"><span class="fa fa-search"></span>&nbsp;Search</button>
						</div>				 
	   				
	    				</div>    
	    				<div id="donor-list-div"  style="display:none;" >
	    				<div class="bs-callout bs-callout-info">
						<span>Search Result</span>
					</div>	 			 	
			 	       <div class="row" >
			 						
			 		<div id="donor-list" class="col-xs-7 pp-form-field"></div>	 				 		
			 	</div>
		 	</div> 
		 	
		 </div>	
	
		<div class="modal fade" id="donor-details-modal" tabindex="-1"
		role="dialog" aria-labelledby="donor-modal-label" data-keyboard="false"
		data-backdrop="static">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="donar-modal-label">Donor Details</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<form name="donor-details-form" id="donor-details-form">
							<input type="hidden" id="selectedRowId" value=""
								name="selectedRowId" /> <input type="hidden" id="donorid"
								value="" name="donarid" />
							<div class="row">
								<div class="col-xs-6">
									<div id="modal-notification-area"></div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-4">
								<strong>Donor Name:</strong> <p class="text-danger" id="donarName"></p>
						          </div>
						          <div class="col-sm-4">
								<strong> Donor Address: </strong> <p class="text-danger" id="donarAddress"></p>
						          </div>
						            <div class="col-sm-4">
								<strong> Country: </strong> <p class="text-danger" id="countryName"></p>
						          </div>
						          </div>
						          <div class="row">
						          <div class="col-sm-4">
								<strong> Email: </strong> <p class="text-danger" id="email"></p>
						          </div>
						           <div class="col-sm-4">
							     <strong>Phone: </strong><p class="text-danger" id="phone"></p>
						          </div>
						           <div class="col-sm-4">
								<strong> Website: </strong><p class="text-danger" id="website"></p>
						          </div>
						          </div>
						          
								
							</div>
						</form>
					</div>
					<div class="modal-footer">
				      	<div >
				      		<button type="button" class="btn btn-default" data-dismiss="modal">
				      		<span class="fa fa-close"></span>&nbsp;Close</button>
				      	</div>					        
				      </div>
				</div>
				
			</div>
		</div>
	</div>
		 	 	 
		      
 </body> 
 
</html>
