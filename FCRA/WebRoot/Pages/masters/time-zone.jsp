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
<script type="text/javascript"
	src="resources/js/masters/time-zone.js"></script>
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
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
	$("#add-time-zone-form").validationEngine({promptPosition: 'bottomLeft'});
	$("#time-zone-form").validationEngine({promptPosition: 'bottomLeft'});
});

</script>

<title>Insert title here</title>
</head>
<body>
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
		
			<div class="row">
				<div class="col-xs-8 pp-form-field"
					id="time-zone-type-list"></div>
			</div>
			<div class="cols-xs-2">
				<button type="button" class="btn btn-primary"
					id="add-time-zone">
					<span class="fa fa-plus"></span> Add Time Zone
				</button>
			</div>						
		<form action="time-zone-type-details-time-zone-details"
				id="add-time-zone-form" method="post"
				style="display: none;">
				<div class="row">								
					<div class="col-xs-3 pp-form-field">
						<label for="notification-type-id">Country: </label> <select
							name="country" class="form-control validate[required] clear">
							<option value="">Country</option>
							<c:forEach items="${countryTypeList}"
								var="countryTypeList">
								<option value="${countryTypeList.countryCode}">${countryTypeList.countryName}</option>
							</c:forEach>
						</select>
					</div>	
								
					<div class="col-xs-3 pp-form-field">
						<label for="notification-type-id">Zone Name: </label> <input
							type="text" name="zoneName" id="zone-name"
							class="form-control validate[required] clear" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3 pp-form-field">
						<button class="btn btn-success" onclick="validateTimeZoneType();" type="submit" id="submit-btn">
							<span class="fa fa-check"></span> Submit
						</button>
						<button class="btn btn-danger" onclick="hideTimeZoneValidationMsg();" type="button" id="cancel-btn">
							<span class="fa fa-close"></span> Cancel
						</button>
					</div>
				</div>
			</form>
			<div id="form-div" style="display: none;">
				<form id="time-zone-form"
					action="time-zone-type-edit-time-zone-details" method="post">
					<div class="row">
						<div class="col-xs-3 pp-form-field" style="display:none;">
						<label for="notification-id">Time Zone ID: </label>						
						<input type="text" name="timeZoneTypeId" id="timeZoneId" class="form-control clear" readonly="readonly"/> 
						</div>
					
					
						<div class="col-xs-3 pp-form-field">
							<label for="notification-type-id">Country: </label> <select
							name="country" id="abcd" class="form-control validate[required] clear">
							<option value="">Country</option>
							<c:forEach items="${countryTypeList}"
								var="countryTypeList">
								<option value="${countryTypeList.countryCode}">${countryTypeList.countryName}</option>
							</c:forEach>
						</select>
						</div>
						
						<div class="col-xs-4 pp-form-field" id="">
							<label for="notificationTitle">Zone Name </label> <input
								type="text" name="zoneName" id="abcde" class="form-control validate[required] clear" />
						</div>						
					</div>
					<div id="edit-actions" class="form-inline" style="display: none;">
						<div class="row">
							<div class="col-xs-3 pp-form-field">
								<button class="btn btn-success" onclick="validateEditTimeZoneType();" type="submit" id="submit-btn2">
									<span class="fa fa-check"></span> Edit
								</button>
								<button type="button" class="btn btn-danger"
									onclick="javascript:deleteTimeZoneType();">
									<span class="fa fa-close"></span> Delete
								</button>
							</div>
						</div>
					</div>
				</form>
			</div>			
		</div>
	</div>
</body>
</html>