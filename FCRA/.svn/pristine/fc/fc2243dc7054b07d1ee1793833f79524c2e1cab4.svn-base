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
	<script type="text/javascript" src="resources/js/masters/Admin-User.js"></script>	
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
	
	<title>Admin User</title>
</head>
<body id="">
	<div class="container-fluid">
		<div class="content-section">
		<input type="hidden" name="userid" id="userid"/>
		<input type="hidden" name="statusId" id="statusId"/>
		<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
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
				<div class="col-xs-9 pp-form-field" id="admin-list">	
				</div>
		    </div>
		    <div class="form-group">
		    <div class="row" >
					<div class="col-xs-2 pp-form-field" id="table-btn" style="display:none;">
						<button type="button" class="btn btn-link" onclick="javascript:showTable();">
						<span class="fa fa-table"></span> View Table</button>					
					</div>
				</div>	
				<div id="user-info-div" style="display:none;">
					<div class="row" id="user-info-head-div"></div>
					<div class="row">
						<div class="col-sm-2">
							Office Name:<p class="text-danger" id="p-officeName"></p>
						</div>
						<div class="col-sm-2">
							Office Type:<p class="text-danger" id="p-officeType"></p>
						</div>
						<div class="col-sm-2">
							Country Code:<p class="text-danger" id="p-countryCode"></p>
						</div>	
					</div>
				</div>
				<div class="row">
					<br/>
					<div class="col-sm-8">
							<div id="edit-actions" style="display:none;">											        	
				        	
				        	<button type="button" class="btn btn-warning" id="reset-usr-btn" confirm-box="top" onclick="javascript:resetPassword();">
				        	<span class="fa fa-reply"></span>&nbsp;Reset Password</button>
				        	<button type="button" class="btn btn-info" style="display:none" id="unlock-adm-btn" onclick="javascript:unlockAdmin();">
				        	<span class="fa fa-unlock"></span>&nbsp;Unlock</button>
		        		</div>
					</div>					
				</div>	
		</div>
		</div>		
	</div>	       
</body>
</html>
