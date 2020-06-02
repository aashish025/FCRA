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
<script type="text/javascript" src="resources/js/services/application-tracking.js"></script>
<!-- CSS and JS for HEADING FONT -->
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/headingfont/style.css" />
<script type="text/javascript" src="resources/js/bootgrid/bootlocalgrid.js"></script>
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
   				var pId = '${applicationId}';
				var u = window.location.href.split('/').pop();
				if (u.substring(0, 6) == "popup-") {
					$('#applicationId').val(pId);
					$("#application-search").hide();
					getApplicationDetails();
				}
		});
</script>

<script>
$(document).ready(function() {
	$("#changePasswordSubmit").validationEngine({promptPosition: 'bottomLeft'});
});
</script>

<title>Application Tracking</title>
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
			</div>
			&nbsp; &nbsp;
			<div class="row">
				<div id="application-search" class="col-xs-3">
					<form id="app-form" method="post">
					<div class="input-group">
							<input type="text"
								class="form-control validate[required ,maxSize[15] ]"
								placeholder="Application Id" name="applicationId" id="applicationId" style="text-transform: uppercase;">
								<span class="input-group-btn">
									<button class="btn btn-info" type="button"
										onclick="javascript:getApplicationDetails();">
										<span class="fa fa-search"></span>&nbsp;
									</button>
							</span>
						
					</div>
					</form>
					
				</div>
			</div>

<!-- 			&nbsp; &nbsp;
	      <div class="alert alert-info" id="application-track-info"
						style="padding-left: 50px; border-bottom-width: 0px; padding-bottom: 16px; display:none">
						Application Id :<B class="text-info" id="applicationId-info" style="padding-left:20px; align="left"></B>&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
						Service Name :<B class="text-info" id="service-info" style="padding-left:20px;"></B>&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;
						Submitted On :<B class="text-info" id="submitted-info" style="padding-left:20px;" ></B>
					</div> -->
	<div id="application-details">
</div>
	
		</div><!--Div close For content-section  -->
	</div><!--Div Close For container-fluid  -->
</body>
</html>
