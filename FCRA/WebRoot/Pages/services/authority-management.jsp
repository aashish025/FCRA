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
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<!-- JS for Password Encryption -->
<script type="text/javascript" src="resources/js/md5.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="resources/js/services/authority-management.js"></script>
<!-- CSS and JS for HEADING FONT -->
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/headingfont/style.css" />
<script type="text/javascript"
	src="resources/js/bootgrid/bootlocalgrid.js"></script>
<script src="resources/js/languages/jquery.validationEngine-en.js"
	type="text/javascript" charset="utf-8"></script>
<script src="resources/js/jquery.validationEngine.js"
	type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="resources/css/validationEngine.jquery.css"
	type="text/css" />
<title>Application Authority Management</title>
<style type="text/css">
#table-wrapper {
	position: relative;
}

#table-scroll {
	height: auto !important;
	min-height: 30px;
	max-height: 350px;
	overflow: auto;
	margin-top: 20px;
}

#table-wrapper table {
	width: 100%;
}

#table-wrapper table thead th .text {
	position: absolute;
	top: -20px;
	z-index: 2;
	height: 40px;
	width: inherit;
	/*  border:1px solid ; */
}
</style>



</head>
<body id="">
	<div class="container-fluid">
		<div class="content-section">
			<div class="row">
				<div class="col-xs-12">
					<div id="notification-area"></div>

				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div id="custom-notification-area"></div>

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
			<!-- <div class="row">
				<div class="col-xs-11 pp-form-field" id="office-list"> </div>
		    </div> -->
			<div class="form-group">
				<!-- <div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Office</button>					
					</div>
				</div> -->
			</div>
<ul class="nav nav-pills">
    <li  class="active" ><a a data-toggle="tab" href="#userToUser" onclick="javascript:hideSearchDiv();"><b>User to User</b></a></li>
    <li><a data-toggle="tab"  href="#freshCaseToUser"  onclick="javascript:hideSearchDivOfficeToUser();"><b>Assign fresh cases to User</b></a></li>
  </ul>
 <div id="form-div">
  <div class="tab-content">
 <div id="userToUser" class="tab-pane fade in active">
 <form id="authority-mgt-form" action=""
					enctype="multipart/form-data" method="post">

					<!-- </form> -->
					<input type="hidden" class="selected-applicationId-list"
						id="selected-applicationId-list" />
					<div id="toUser-selection-div">
						<div class="row">
							<div class="col-xs-3 pp-form-field" id="userDiv">
								<label for="userId">Select User</label> <select id="userId"
									name="userId" class="form-control  validate[required] clear">
									<option value="">Select User</option>
									<c:forEach items="${userList}" var="nType">
										<option value='<c:out value="${nType.k}"/>'><c:out
												value="${nType.v}" /></option>
									</c:forEach>
								</select>
							</div>
							<div class="col-xs-3 pp-form-field" id="serviceTypeDiv">
								<label for="serviceType">Select Service</label> <select
									id="serviceType" name="serviceType"
									class="form-control  validate[required] clear">
									<option value="">Select Service Type</option>
									<c:forEach items="${serviceTypeList}" var="nType">
										<option value='<c:out value="${nType.k}"/>'><c:out
												value="${nType.v}" /></option>
									</c:forEach>
								</select>
							</div>
							
							<div class="col-xs-3 pp-form-field" id="stateListDiv">
								<label for="stateList">Select State</label>
								 <select id="stateList" name="stateList" class="form-control  validate[required] clear">
									<option value="All" selected="selected">All</option>
									 <c:forEach items="${stateList}" var="nType">
										<option value='<c:out value="${nType.k}"/>'><c:out
												value="${nType.v}" /></option>
									</c:forEach>
								</select>
							</div>
							
							
							<div class="col-xs-2 pp-form-field" id="get-detail-btn"
								style="padding-top: 112px; margin-top: 22px;">
								<button type="button" class="btn btn-primary" id="search-button"
									onclick="javascript:getApplicationDetail();">
									<!--   -->
									<span class="fa fa-plus"></span> Get Details
								</button>
							</div>
						</div>
					</div>

					<div class="row" id="applicationId-detail-table"
						style="display: none">

						<div class="col-xs-12">
							<div class="bs-callout bs-callout-info" id="table-info-to-user-selection">

								<!-- 	<span >Application Detail Table for </span> -->
							</div>
							</br>
							<div id="table-wrapper">
								<div id="table-scroll">
									<table class="table table-striped">
										<thead>
											<tr>
											    
												<th><span class="text"><input type="checkbox"
														id="headCheckBox" onclick="javascript:changeEffect();" /></span></th>
														<th><span class="text">S.No.</span></th>
												<th><span class="text">Application Id</span></th>
												<th><span class="text">Applicant / Association Name</span></th>
												<th><span class="text" >State</span></th>
												<th><span class="text" style="margin-left: 30px;">Service</span></th>
												<th><span class="text" style="margin-left: 160px;">Status</span></th>
											</tr>
										</thead>
										<tbody id="tableBody">
										</tbody>
									</table>
								</div>
							</div>
						</div>

					</div>


					<div id="user-forwarding-detail" style="display: none">
						<div class="bs-callout bs-callout-info">
							<span>Forwarding Details </span>
						</div>
						<div class="row">
							<div class="col-xs-3 pp-form-field" id="forwardToUserDiv">
								<label for="forwardToUser">Forward To</label> <select
									id="forwardToUser" name="forwardToUser"
									class="form-control  validate[required] clear">
									<option value="">Select User</option>
									<c:forEach items="${userListForward}" var="nType">
										<option value='<c:out value="${nType.k}"/>'><c:out
												value="${nType.v}" /></option>
									</c:forEach>
								</select>
							</div>
							<div class="col-xs-5 pp-form-field" id="">
								<label for="userRemark">Remark : </label>
								<textarea class="form-control  validate[required] clear"
									rows="3" id="userRemark" name="userRemark"></textarea>
							</div>
							<div class="col-xs-2" id="add-details-btn"><!-- style="margin-top: 26px;" -->
								<button type="button" class="btn btn-primary" style=" margin-bottom: -4.65em !important;"
									onclick="javascript:forwardApplicationId();">
									<span class="fa fa-plus"></span> Submit Details
								</button>
							</div>

						</div>
					</div>

				</form>

				
 </div>
 <input type="hidden" class="selected-applicationId-list-office-to-user" id="selected-applicationId-list-office-to-user" />
<div id="freshCaseToUser" class="tab-pane fade">
    <div id="offoce-to-user-fetch-fresh-data-filter">
						<div class="row">
							<div class="col-xs-3 pp-form-field" id="serviceTypeDiv">
								<label for="serviceType-office-to-user">Select Service</label>
								 <select id="serviceType-office-to-user" name="serviceType-office-to-user" class="form-control  validate[required] clear">
									<option value="All" selected="selected">All</option>
									<c:forEach items="${serviceTypeList}" var="nType">
										<option value='<c:out value="${nType.k}"/>'><c:out
												value="${nType.v}" /></option>
									</c:forEach>
								</select>
							</div>
                  	<div class="col-xs-3 pp-form-field" id="stateListDiv">
								<label for="stateList">Select State</label>
								 <select id="stateList" name="stateList" class="form-control  validate[required] clear">
									<option value="All" selected="selected">All</option>
									 <c:forEach items="${stateList}" var="nType">
										<option value='<c:out value="${nType.k}"/>'><c:out
												value="${nType.v}" /></option>
									</c:forEach>
								</select>
						</div>
							<div class="col-xs-2 pp-form-field" id="get-detail-btn"
								style="padding-top: 112px; margin-top: 22px;">
								<button type="button" class="btn btn-primary" id="search-button"  onclick="javascript:getFreshApplicationDetail();" ><!-- -->
								<span class="fa fa-plus"></span> Get Details</button>
							</div>
						</div>
						</div>
				<div class="row" id="office-to-user-applicationId-detail-table" style="display: none"  > <!---->

						<div class="col-xs-12">
							<div class="bs-callout bs-callout-info" id="table-info-selection">

								<!-- 	<span >Application Detail Table for </span> -->
							</div>
							</br>
							<div id="table-wrapper">
								<div id="table-scroll">
									<table class="table table-striped">
										<thead>
											<tr>
											    
												<th><span class="text"><input type="checkbox" id="headCheckBox-office-to-user" onclick="javascript:changeEffectOfficeToUser();" /></span></th>
												<th><span class="text">S.No.</span></th>
												<th><span class="text">Application Id</span></th>
												<th><span class="text">Section File No</span></th>
												<th><span class="text">Applicant/Association Name</span></th>
												<th><span class="text">Service</span></th>
												
											</tr>
										</thead>
										<tbody id="freshtableBody">
										</tbody>
									</table>
								</div>
							</div>
						</div>

					</div>
						
						<form id="authority-mgt-office-to-user-forwarding-form" action="">
						<div id="user-forwarding-detail-office-to-user" style="display: none"><!--  -->
							<div class="bs-callout bs-callout-info">
								<span>Forwarding Details </span>
							</div>
							<div class="row">
								<div class="col-xs-3 pp-form-field" id="forwardToUserDiv">
									<label for="forwardToUserFresh">Forward To</label> <select
										id="forwardToUserFresh" name="forwardToUserFresh"
										class="form-control  validate[required] clear">
										<option value="">Select User</option>
										<c:forEach items="${userList}" var="nType">
											<option value='<c:out value="${nType.k}"/>'><c:out
													value="${nType.v}" /></option>
										</c:forEach>
									</select>
								</div>
								<div class="col-xs-2" id="add-details-btn">
									<!-- style="margin-top: 26px;" -->
									<button type="button" class="btn btn-primary"
										style="margin-bottom: -4.65em !important;" onclick="javascript:forwardApplicationIdOfficeToUser();"><!--  -->
										<span class="fa fa-plus"></span> Submit Details
									</button>
								</div>

							</div>
						</div>
                </form>

					</div>
    </div>			
			</div>

		</div>
	</div>
</body>
</html>
