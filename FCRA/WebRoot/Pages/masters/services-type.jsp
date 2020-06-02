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
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>		
	<script type="text/javascript" src="resources/js/masters/services-type.js"></script>	
	<title>Services Details</title>
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
		       <div class="col-xs-8 pp-form-field" id="services-list">
				</div>
		    </div>
			<div class="form-group">
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Services Description</button>					
					</div>
					</div>
				</div>				
		    </div>
		    
		    <div id="form-div">
		    	<form name= "services-type-form" action="" method="get" id="services-type-form">
		    		<input type="hidden" name="servicesCode" id="servicesCode"/>
		    		<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
		    		<div id="details-div" style="display:none">
		    			<div class="row" >						
							<div class="col-xs-4 pp-form-field" id="">
									<label for="servicesDesc">Services Description : </label>
									<input type="text" name="servicesDesc" id="servicesDesc" class="form-control validate[required, custom[onlyLetterNumber]] clear"/>
							</div>
						</div>
		    		</div>
		    		<div id="add-details-btn" style="display:none;">
		    		 <div class="row">
							<div class="col-xs-2 pp-form-field">
								<button type="button" class="btn btn-primary " onclick="javascript:addservicesdetails();">
								<span class="fa fa-plus clear"></span> Add Details</button>					
							</div>
						</div>
		    		</div>				
					<div id="edit-actions"  style="display:none;">
						<div class="row"> 
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:editservice();">
									<span class="fa fa-edit clear"></span> Edit</button>
									<button type="button" class="btn btn-danger" onclick="javascript:deleteservice();">
									<span class="fa fa-close clear"></span> Delete</button>					
							</div>	
						</div>											
					</div>												
			      </form>
		    </div> 
			
		</div>

      

</body>
</html>
