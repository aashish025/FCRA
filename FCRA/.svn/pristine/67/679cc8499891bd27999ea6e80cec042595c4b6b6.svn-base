<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />	
	<script type="text/javascript" src="resources/js/all.js"></script>
	<script type="text/javascript" src="resources/js/auth/home/user-notify.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>	
	<script type="text/javascript" src="resources/js/masters/report.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
	<title>Report</title>
</head><body id="">
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
		       <div class="col-xs-8 pp-form-field" id="Report-list">
				</div>
		    </div>
		    <div id="res-div" style="display:none;">
					<br/>
					<p class="text-danger"><strong>Please Assign/Edit office type to Report.</strong></p>
				
					<div class="row">
							<div class="toggle" id="privileges-list">						   
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Available Office Type:</label>
							   		<select id="avpl"  ondblclick="javascript:addRemoveOffice('avpl','aspl');" multiple="multiple" name="avpl" size="10"  class="form-control"> </select>					   							   		 
							   </div>					  
							   <div class="col-xs-1" align="center">
							   		<br/><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveOffice('avpl','aspl');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-right"></span></button><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveOffice('aspl','avpl');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-left"></span></button>
							   </div>
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Assigned Office Type:</label>						   		
							  		<select id="aspl" ondblclick="javascript:addRemoveOffice('aspl','avpl');" multiple="multiple" name="aspl" size="10" class="form-control clear"> </select>					   		
							   </div>					  							
							</div>
						</div>				
						<div class="row" id="fresh-res-save">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:saveDetails('FRS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
						<div class="row" id="edit-res-save" style="display:none;">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:saveDetails('ERS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
							
				</div>		
			<div class="form-group">
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Report Name</button>					
					</div>
					</div>
				</div>				
		    
		    
		    <div id="form-div">
		    	<form action="report-type-form" method="get" id="report-type-form">
		    		<input type="hidden" name="reportId" id="reportId"/>
		    		 <input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
		    		 <div class="row" >
					<div class="col-xs-2 pp-form-field" id="table-btn" style="display:none;">
						<button type="button" class="btn btn-link" onclick="javascript:showview();">
						<span class="fa fa-table"></span> View Table</button>					
					</div>
				</div>	
		    		<div id="details-div" style="display:none">
		    			<div class="row" >						
							<div class="col-xs-3 pp-form-field" id="">
									<label for="reportTitle"> Report Name: </label>
									<input type="text" name="reportName" id="reportName" class="form-control validate[required, custom[onlyLetterNumberSpadv] maxSize[50]] clear "/>
							</div>
						</div>
		    		</div>
		    		<div id="add-details-btn" style="display:none;">
		    		 <div class="row">
							<div class="col-xs-2 pp-form-field">
								<button type="button" class="btn btn-primary " onclick="javascript:addreportdetails();">
								<span class="fa fa-plus clear"></span> Add  Report</button>					
							</div>
						</div>
		    		</div>				
					<div id="edit-actions"  style="display:none;">
						<div class="row"> 
							<div class="col-xs-4 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:editreport();">
									<span class="fa fa-edit clear"></span> Edit</button>
									<button type="button" class="btn btn-primary" onclick="javascript:editofficetype();">
									<span class="fa fa-edit clear"></span> Assign Office Type</button>
									<button type="button" class="btn btn-danger" onclick="javascript:deletereport();">
									<span class="fa fa-close clear"></span> Delete</button>					
							</div>	
						</div>											
					</div>												
			      </form>
		    </div> 
			</div>
		</div>

      

</body>
</html>