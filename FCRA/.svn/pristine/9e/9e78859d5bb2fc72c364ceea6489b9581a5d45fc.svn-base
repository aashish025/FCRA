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
	<script type="text/javascript" src="resources/js/masters/notification.js"></script>	
	<script type="text/javascript" src="resources/js/utility/uploader.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
	<title>Notification Details</title>
</head>

<body id="">
	<div class="container-fluid">
		<div class="content-section">
			<div class="row">
				<div class="col-xs-11 pp-form-field" id="notification-list">
				</div>
		    </div>
			<div class="form-group">
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="table-btn" style="display:none;">
						<button type="button" class="btn btn-link" onclick="javascript:showTable();">
						<span class="fa fa-table"></span>View Table</button>					
					</div>
				</div>	
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Notification</button>					
					</div>
				</div>				
		    </div>
		    <div id="form-div" style="display:none;">
		    	<form id="notification-form" action="" enctype="multipart/form-data" method="post">
		    		<input type="hidden" name="notificationId" id="notificationId"/>
			    	<div class="row" >
						<div class="col-xs-3 pp-form-field">
							<label for="notificationType">Notification Type: </label>
							<select id="notificationType" name="notificationType" class="form-control validate[required]">
							 	<option value="">Select Notification Type</option>
							 	<c:forEach items="${notificationTypeList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach>
							</select>
						</div>
						<div class="col-xs-4 pp-form-field" id="">
								<label for="notificationTitle">Notification Title: </label>
								<input type="text" name="notificationTitle" id="notificationTitle" class="form-control validate[required,maxSize[200]]"/>
						</div>
					</div>
					<div class="row">						
						<div class="col-xs-7 pp-form-field" id="">
								<label for="notificationDetails">Notification Details: </label>
								<textarea class="form-control validate[required,maxSize[1000]]" rows="3" id="notificationDetails" name="notificationDetail"></textarea>
						</div>
						<div class="col-xs-2 pp-form-field" id="attachment-list">
						</div>
					</div>					
					<!-- <div class="row">
						<div class="col-xs-3 pp-form-field ">								
								<button type="button" class="btn btn-warning" onclick="javascript:openFile();" title="click to attch files">
								<span class="glyphicon glyphicon-paperclip"></span></button>	
								<input type="file" name="attachment" id="attachment" style="display:none;"/>
						</div>
					</div> -->
					<div upload-plugin="rc-upload" class="row pp-form-field"  up-file-types="jpg,png,pdf" up-file-size="10000000" >
						<div up-file-selector="rc-upload" class="col-xs-2"></div>
						<br/>
						<div up-file-list="rc-upload" id="file-list" class="col-xs-4"></div>
					</div>
					<div class="row">
						<div class="col-xs-2 pp-form-field" id="add-details-btn">
							<button type="button" class="btn btn-primary" onclick="javascript:addNotification();">
							<span class="fa fa-plus"></span> Add Details</button>					
						</div>
					</div>					
					<div id="edit-actions" class="form-inline" style="display:none;">
						<div class="row"> 
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:editNotification();">
									<span class="fa fa-edit"></span> Edit</button>
									<button type="button" class="btn btn-danger" onclick="javascript:deleteNotification();">
									<span class="fa fa-close"></span> Delete</button>					
							</div>	
						</div>											
					</div>												
			    </form>	    
		    </div> 
			
		</div>
	</div> 
	<form action="" id="attachment-download-form">
	<input type="hidden" name="rowId" id="rowId"/>
	</form>       
</body>
</html>
