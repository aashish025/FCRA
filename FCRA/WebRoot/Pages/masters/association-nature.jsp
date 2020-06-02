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
	<script type="text/javascript" src="resources/js/masters/association-nature.js"></script>	
	
  
	<title>Association Nature</title>
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
		       <div class="col-xs-8 pp-form-field" id="nature-list">
				</div>
		    </div>
			<div class="form-group">
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Nature Description </button>					
					</div>
					</div>
				</div>				
		    </div>
		    
		    <div id="form-div">
		    	<form name="nature-type-form" action="" method="get" id="nature-type-form">
		    	<div id="details-div" style="display:none">
		    	<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
		    			<div class="row" >	
		    			<div class="col-xs-3 pp-form-field" id="code">
									<label for="natureCode">Nature Code: </label>
									<input type="text" name="natureCode" id="natureCode" class="form-control validate[required, maxSize[2],custom[onlyNumberSp]] clear"/><br></br>
									
							</div>
		    						
							<div class="col-xs-3 pp-form-field" id="">
									<label for="naturename">Nature  Description: </label>
									<input type="text" name="natureName" id="natureName" class="form-control validate[required, custom[onlyLetterSp]] clear"/><br></br>
									
							</div>
							
						</div>
		    		</div>
		    		<div id="add-details-btn" style="display:none;">
		    		 <div class="row">
							<div class="col-xs-2 pp-form-field">
								<button type="button" class="btn btn-primary " onclick="javascript:addnaturedetails();">
								<span class="fa fa-plus clear"></span> Add Details</button>					
							</div>
						</div>
		    		</div>				
					<div id="edit-actions"  style="display:none;">
						<div class="row"> 
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:editnaturetype();">
									<span class="fa fa-edit clear"></span> Edit</button>
									<button type="button" class="btn btn-danger" onclick="javascript:deletenaturetype();">
									<span class="fa fa-close clear"></span> Delete</button>					
							</div>	
						</div>											
					</div>												
			      </form>
		    </div> 
			
		</div>

      

</body>
</html>
