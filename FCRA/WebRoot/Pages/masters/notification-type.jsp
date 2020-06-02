<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp"%>
<html>
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
	<script type="text/javascript" src="resources/js/masters/notification-type.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>

<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	





<script type="text/javascript">
		$(document).ready(function(){
   				var x=[
   				       <c:forEach items="${notifyList}" var="ntn" varStatus="status">
   				       	{'d':'${ntn.d}',
   				       		'h':'${ntn.h}',
   				       		'l':'${ntn.l}',
   				       		'ms':'${ntn.ms}',
   				       		's':'${ntn.s}',
   				       		't':'${ntn.t}'
   				       		}
   				     <c:if test="${!status.last}">    
	   			      ,    
	   			    </c:if>
   				       	</c:forEach>
   				       ];
   				if(x.length > 0) {
	   				notifyList(x,'');
   				}
		});
</script>

<script>
$(document).ready(function() {
	$("#add-notification-type-form").validationEngine({promptPosition: 'bottomLeft'});
	$("#notification-form").validationEngine({promptPosition: 'bottomLeft'});
});

</script>

<title>Notification Type</title>
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
				<div class="col-xs-8 pp-form-field" id="notification-type-list">
				</div>
		</div>
		<div class="cols-xs-2">
			<button type="button" class="btn btn-primary" id="add-notification-type" ><span class="fa fa-plus"></span> Add Notification Type</button>					
		</div>
		
		<form action="notification-type-details" id="add-notification-type-form" method="post" style="display: none;">
		<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>				
				<div class="row">
					<div class="col-xs-3 pp-form-field">
						<label for="notification-type-id">Notification Name: </label>
						<!-- <input type="text" name="notificationTypeName" id="notification-type-name" class="validate[required]" class="form-control"/> -->
						<input type="text" name="notificationTypeName" id="notification-type-name" class="form-control validate[required] clear"/> 
					</div>													
				</div>
				
				<div class="row">
					<div class="col-xs-3 pp-form-field">					
						<button class="btn btn-success" onclick="validateNotificationType();" type="submit" id="submit-btn"><span class="fa fa-check"></span> Submit</button>
						<button class="btn btn-danger" type="button" id="cancel-btn" onclick="hideValidationMsg();"><span class="fa fa-close"></span> Cancel</button>
					</div>					
				</div>
		</form>
				 
		 <div id="form-div" style="display:none;">
		    	<form id="notification-form" action="notification-type-edit-notification-type-details" method="post">	
		    	<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>	    		
		    		<div class="row"> 
						<div class="col-xs-3 pp-form-field" style="display:none;">
							<label for="notification-id">Notification ID: </label>						
							<input type="text" name="notificationTypeId" id="notificationIdForEdit" class="form-control" readonly="readonly"/> 
						</div>
						
						<div class="col-xs-4 pp-form-field" id="">
						<span class="span3">
							<label for="notificationTitle">Notification Name: </label>
							<!-- <input type="text" name="notificationTitle" id="notificationNameForEdit" class="form-control"/> -->
							<input type="text" name="notificationTitle" id="notificationNameForEdit" class="form-control validate[required] clear"/>
							</span>
						</div>
					</div>								
					<div id="edit-actions" class="form-inline" style="display:none;">
						<div class="row"> 
							<div class="col-xs-3 pp-form-field">									
								<button class="btn btn-success" onclick="validateEditNotificationType();" type="submit" id="submit-btn2"><span class="fa fa-check"></span> Edit</button>									
								<button type="button" class="btn btn-danger" onclick="javascript:deleteNotificationType();">
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