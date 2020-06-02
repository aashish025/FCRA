<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../common/include.jsp"%>
<html>
<head>
<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
<script type="text/javascript"
	src="resources/js/bootstrap/all/bootstrap.js"></script>
<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css"
	type="text/css" />
<script type="text/javascript" src="resources/js/all.js"></script>
<script type="text/javascript"
	src="resources/js/auth/home/user-notify.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
<script src="resources/js/iframeResizer.contentWindow.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="resources/js/forAll.js"></script>
<!-- <script type="text/javascript"
	src="resources/js/masters/agreement-type.js"></script> -->
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<!-- JS for Password Encryption -->
<script type="text/javascript" src="resources/js/md5.js"
	type="text/javascript"></script>
<script type="text/javascript" src="resources/js/change-password.js"></script>
<!-- CSS and JS for HEADING FONT -->
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/headingfont/style.css" />
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
   				var forcedChange = "${forcedChange}";   				
   				if(forcedChange == "true")
   					window.top.location.href="home?proceed=";
		});
</script>

<script>
$(document).ready(function() {
	$("#changePasswordSubmit").validationEngine({promptPosition: 'bottomLeft'});
});
</script>

<title>Change Password</title>
</head>
<body id="">
	<div class="container-fluid">
		<div class="content-section">
			<div class="row">
				<div class="col-xs-12">
					<div id="notification-area"></div>
				</div>
			</div>
			<div
				style="z-index: 1; position: absolute; right: 5px; top: 80px; width: 300px;"
				id="sticky-notify"></div>
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
			<div id="stickynotification"
				style="display: block; margin: 0; padding: 0; font: 12px Helvetica, Arial, sans-serif;">
				
				<!-- <div class="ui-widget"
					style="padding-left: 10px; margin-right: 20px; margin-bottom: 3px;">
					<p style="font-size: 13px; padding: 4px 4px 4px 4px;"
						class="ui-state-highlight ui-corner-all">
						<span class="ui-icon ui-icon-info"
							style="float: left; margin-right: .3em;"></span> <strong>Info:
							Password length should be between <span style="color: #ff0000;">8
								- 20</span> and should contain atleast one alphabet, digit and a symbol
								and it should contain at least one number, one uppercase letter, one lowercase letter and one special symbol including
							<span style="color: #ff0000;"> . _ % '  -@ ! # ^ * $ </span>
						</strong>
					</p>
				</div> -->
				
				
				
				<div class="alert alert-info">
					<strong>Info:
							Password length should be between <span style="color: #ff0000;">8
								- 20</span> and it should contain at least one number, one uppercase letter, one lowercase letter and one special symbol including
							<span style="color: #ff0000;"> . _ % '  -@ ! # ^ * $ </span>
						</strong> <B class="text-info" id="info-alert-label"></B>
				</div>
				
				
			</div>
			&nbsp; &nbsp;
			<form action="change-password-submit-change-password" id="changePasswordSubmit"
				method="post">
				<input type="hidden" name="currentPasswordWithHash" id="current-password-with-hash">
				<input type="hidden" name="newPasswordWithHash" id="new-password-with-hash">
				<input type="hidden" name="newPasswordConfirmedWithHash" id="newPasswordConfirmed-with-hash">
				<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
				<div class="row">
					<div class="col-xs-3 pp-form-field">
						<label for="current-password-id">Current Password: </label> <input
							type="password" name="currentPassword" id="current-password"
							class="form-control validate[required]" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3 pp-form-field">
						<label for="new-password-id">New Password: </label> <input
							type="password" name="newPassword" id="new-password"
							class="form-control validate[required, custom[passwordC], equals[new-password]] clear" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3 pp-form-field">
						<label for="notification-type-id">Re-type Password: </label> <input
							type="password" name="newPasswordConfirmed"
							id="newPasswordConfirmed" class="form-control validate[required, custom[passwordC], equals[newPasswordConfirmed]] clear" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3 pp-form-field">
						<button class="btn btn-success" type="submit" id="submit-btn"
							onclick="encryptPassword()">
							<span class="fa fa-check"></span> Change Password
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
