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
		<script type="text/javascript" src="resources/js/masters/sub-menu.js"></script>	
		<script type="text/javascript" src="resources/js/utility/uploader.js"></script>
		<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
		<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
		<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
		<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
		<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
		<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
		<title>Sub Menu</title>
	</head>
	<body id="">
		<div class="container-fluid">
			<div class="content-section">
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
					<div class="col-xs-11 pp-form-field" id="submenu-list"></div>
			    </div>
				<div class="form-group">
					<div class="row" >
						<div class="col-xs-2 pp-form-field clear" id="add-btn">
							<button type="button" class="btn btn-primary" onclick="javascript:initForm5();">
							<span class="fa fa-plus"></span> Add Sub Menu</button>					
						</div>
					</div>				
			    </div>
			    <div id="form-div" style="display:none;">
				    <form action=""  method="get" id="submenu">
				    	<input type="hidden" name="smenuId" id="smenuId"/>
				    	<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
				    	<div class="row">
							<div class="col-xs-4 pp-form-field " >
								<label for="localityName">Sub Menu Name : </label>
								<input type="text" name="smenuName" id="smenuName" class=" form-control validate[required, custom[onlyLetterNumberSpadv] clear"/>
							</div>
							<div class="col-xs-4 pp-form-field " >
								<label for="localityName">Action Path : </label>
								<input type="text" name="actionPath" id="actionPath" class=" form-control validate[required, custom[onlyLetterNumberSpadv] clear"/>
							</div>
							<div class="col-xs-3 pp-form-field">
								<label for="notificationType" id="lgc">Parent Menu Name :  </label>
								<select id="pMenuId" name="pMenuId"  class="form-control validate[required] clear">
								 	<option value="">Select Parent Menu Name</option>
							 		<c:forEach items="${pmenuNameList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
								 	</c:forEach>
								</select>
							</div>
						</div>
						<div class="row" id="details-div">
							<div class="col-xs-2 pp-form-field " id="add-details-btn">
								<button type="button" class="btn btn-primary" onclick="javascript:addSubMenu();">
								<span class="fa fa-plus"></span> Add Details</button>					
							</div>
						</div>					
						<div id="edit-actions" class="form-inline" style="display:none;">
							<div class="row"> 
								<div class="col-xs-3 pp-form-field  " >
									<button type="button" class="btn btn-primary" onclick="javascript:editSubMenu();">
									<span class="fa fa-edit"></span> Edit</button>
									<button type="button" class="btn btn-danger" onclick="javascript:deleteSubMenu();">
									<span class="fa fa-close"></span> Delete</button>					
								</div>	
							</div>											
						</div>												
			    	 </form>
			    </div> 
			</div>
		</div> 
	 </body>
</html>