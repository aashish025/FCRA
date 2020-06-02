<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
<script type="text/javascript"	src="resources/js/bootstrap/all/bootstrap.js"></script>
<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css"	type="text/css" />
<script type="text/javascript" src="resources/js/all.js"></script>
<script type="text/javascript"	src="resources/js/auth/home/user-notify.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
<script type="text/javascript" src="resources/js/forAll.js"></script>
<script type="text/javascript"	src="resources/js/services/change-registration-validity.js"></script>
<script type="text/javascript" src="resources/js/utility/uploader.js"></script>
<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
<script type="text/javascript" src="resources/js/bootgrid/bootlocalgrid.js"></script>
<script src="resources/js/languages/jquery.validationEngine-en.js"	type="text/javascript" charset="utf-8"></script>
<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="resources/css/validationEngine.jquery.css"	type="text/css" />
<script type="text/javascript"	src="resources/js/input-mask/input-mask.js"></script>
<link rel="stylesheet"	href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
<script type="text/javascript"	src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
<title>Change Registration Validity Type</title>
</head>
<body id="">
	<div class="container-fluid">
		<div class="content-section">
			<div class="row">
				<div class="col-xs-12">
					<div id="notification-area"></div>
				</div>
			</div>
			<div style="z-index: 1; position: absolute; right: 5px; top: 80px; width: 300px;" id="sticky-notify"></div>
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

			<div id="rcn-search-detail">
				<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken" />
				<input type="hidden" name="currentDateString" id="currentDateString" />
				<div class="row">
					<div class="col-sm-3" id="appid-search">
						<div class="input-group">
							<input type="text" class="form-control validate[required, maxSize[15]]" title="Please enter registration number."placeholder="Registration Number" name="applicationId" id="applicationId" style="text-transform: uppercase;">
								<div class="input-group-btn">
									<button class="btn btn-info title-b" onclick="javascript:getApplicationList();"	title="Click to submit">
										<span class="glyphicon glyphicon-download"></span>&nbsp;
									</button>
								</div> </input>
						</div>
					</div>
					<div id="advance-search" style="display: none;">
						<div class="col-sm-2">
							<select id="state" name="state" onchange="javascript:getDistrict(this.value);"	class="form-control clear validate[maxSize[1]] title-l">
								<option value="">Select State</option>
								<c:forEach items="${stateList}" var="gType">
									<option value='<c:out value="${gType.li}"/>'><c:out	value="${gType.ld}" /></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-sm-2">
							<select id="district" name="district" class="form-control clear validate[maxSize[1]] title-l">
								<option value="">Select District</option>
								<c:forEach items="${districtList}" var="gType">
									<option value='<c:out value="${gType.li}"/>'><c:out	value="${gType.ld}" /></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control validate[maxSize[15]] title-b" title="Please enter Association Name to search."	placeholder="Association Name" name="applicationName"
								id="applicationName" style="text-transform: uppercase;"></input>
						</div>
						<div class="col-sm-2">
							<input type="text" class="form-control validate[maxSize[15]] title-b" title="Please enter Functionary Name to search."
								placeholder="Functionary Name" name="functionaryName"
								id="functionaryName" style="text-transform: uppercase;"></input>
						</div>
						<div class="col-sm-1">
							<button class="btn btn-info" onclick="javascript:getAdvanceSearchApplicationList();">
								<span class="fa fa-search"></span>&nbsp;Search
							</button>
						</div>
					</div>
					<div class="col-sm-1" id="adv-search-button-div">
						<button class="btn btn-link" id="toggle-btn" onclick="javascript:toggleSearch();">
							<span class="fa fa-search"></span>&nbsp;Advance Search
						</button>
					</div>
				</div>


				<div class="row" id="application-list-div" style="display: none;">
					<div class="col-xs-12 pp-form-field">
						<div class="bs-callout bs-callout-info">
							<span>Search Result</span>
						</div>
						<div id="app-list"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 pp-form-field" id="app-info"
						style="display: none;">
						<div class="bs-callout bs-callout-info">
							<span id="asso-details-title">Details of Association</span>
						</div>
						<div class="row">
							<div class="col-sm-3">
								Registration/PP Number
								<p class="text-danger" id="regnNumber"></p>
							</div>
							<div class="col-sm-3">
								Registration/PP Date
								<p class="text-danger" id="regnDate"></p>
							</div>
							<div class="col-sm-3">
								Section FileNumber
								<p class="text-danger" id="secFileNumber"></p>
							</div>
							<div class="col-sm-3">
								Current Status
								<p class="text-danger" id="currentStatus"></p>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-3">
								Association Name
								<p class="text-danger" id="applicantName"></p>
							</div>
							<div class="col-sm-3">
								Association Address
								<p class="text-danger" id="assoAddress"></p>
							</div>
							<div class="col-sm-3">
								Association Nature
								<p class="text-danger" id="assoNature"></p>
							</div>

						</div>
						<div class="row">
							<div class="col-sm-3">
								Last Renewed
								<p class="text-danger" id="lastRenewed"></p>
							</div>
							<div class="col-sm-3">
								Valid UpTo
								<p class="text-danger" id="validUpTo"></p>
							</div>
						</div>
						<div class="">
							<div class="bs-callout bs-callout-info">
								<span id="asso-details-title">Details of Bank</span>
							</div>
							<div class="row">
								<div class="col-sm-3">
									Bank Name
									<p class="text-danger" id="assoBank"></p>
								</div>
								<div class="col-sm-3">
									Bank Address
									<p class="text-danger" id="assoBankAddress"></p>
								</div>
								<div class="col-sm-3">
									Account Number
									<p class="text-danger" id="assoAccNumber"></p>
								</div>
							</div>
						</div>
						<div class="bs-callout bs-callout-info">
							<span id="asso-details-title">Change Registration Validity</span>
						</div>
						<form id="changeRegistrationValidity-form">
						<div class="row" id="validity-div" >
						 	<div class="col-xs-2 pp-form-field">
						   		<label for="name" class="control-label">Validity From:</label>						   		
						  		<input type="text" data-mask="99-99-9999" readonly class="form-control clear validate[required]" id="validityFrom" name="validityFrom" />					   		
						   </div>
						   <div class = "col-xs-3 pp-form-field">
						   		<label for="name" class="control-label">Validity to be Extended (in Years):</label>
						   		<input type="text" id = "noOfYrs" name = "noOfYrs" onkeyup="javascript:initValidity();" class="form-control validate[ required,custom[number],min[5]] clear"/>
						   </div>
						   <div class="col-xs-2 pp-form-field" id="validity-div">
						   		<label for="name" class="control-label">Validity UpTo:</label>						   		
						  		<input type="text" data-mask="99-99-9999" readonly class="form-control clear validate[required,custom[date],futureField[currentDateString], futureField[validityFrom]]" id="validityUpTo" name="validityUpTo" />					   		
						   </div>
					     </div> 
					     <div id="submit-actions"  style="display:none;">
						<div class="row"> 
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:submitRegisterationValidity();">
									<span class="fa fa-edit clear"></span> Submit</button>
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
<script  type="text/javascript">		
		/* $('#validityFrom').datetimepicker({
			lang:'ch',
			 minDate: 0,
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
		$('#validityUpTo').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		}); */
	</script>
</html>