<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.Date"%>

<head>
<%
	response.setHeader("X-Frame-Options", "SAMEORIGIN");
	response.setHeader("Cache-Control", "must-revalidate");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	
%>
<script type="text/javascript">
 function burstCache() {
 if (!navigator.onLine) {
 document.body.innerHTML = 'Loading...';
 window.location = 'FCRA/Pages/reports/Error.jsp';
 }
 } </script>

<script type="text/javascript"
	src="resources/js/bootstrap/all/bootstrap-full.min.js?Version=${version}"></script>
<link rel="stylesheet" type="text/css"
	href="resources/css/auth/home/notification.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/auth/home/home-notification.css" />
<script type="text/javascript" src="resources/js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
<script type="text/javascript"
	src="resources/js/bootstrap/all/bootstrap-full.min.js"></script>
<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css"
	type="text/css" />
<script type="text/javascript"
	src="resources/js/input-mask/input-mask.js"></script>
<!-- 	<script type="text/javascript" src="resources/js/all.js"></script>	 -->
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
<script src="resources/js/iframeResizer.contentWindow.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="resources/js/forAll.js"></script>
<script type="text/javascript"
	src="resources/js/reports/dash-board-service.js"></script>
<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
<link rel="stylesheet"
	href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
<script type="text/javascript"
	src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
<script src="resources/js/languages/jquery.validationEngine-en.js"
	type="text/javascript" charset="utf-8"></script>
<script src="resources/js/jquery.validationEngine.js"
	type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="resources/css/validationEngine.jquery.css"
	type="text/css" />
<script type="text/javascript"
	src="resources/js/barchart/jquery.jqplot.js"></script>
<script type="text/javascript"
	src="resources/js/barchart/jqplot.barRenderer.js"></script>
<script type="text/javascript"
	src="resources/js/barchart/jqplot.categoryAxisRenderer.js"></script>
<script type="text/javascript"
	src="resources/js/barchart/jqplot.pointLabels.js"></script>
<script type="text/javascript"
	src="resources/js/barchart/jqplot.canvasAxisTickRenderer.js"></script>
<script type="text/javascript"
	src="resources/js/barchart/jqplot.canvasAxisLabelRenderer.js"></script>
<script type="text/javascript"
	src="resources/js/barchart/jqplot.canvasTextRenderer.js"></script>
<!-- <script type="text/javascript" src="js/barchart/jqplot.highlighter.min.js"> </script>  -->
<script type="text/javascript"
	src="resources/js/barchart/jqplot.highlighter.js"> </script>
<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<link rel="stylesheet" href="resources/css/all.css" type="text/css" />
<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
<link rel="stylesheet" href="resources/css/barchart/jquery.jqplot.css" />
<script type="text/javascript" src="resources/js/chartJS/Chart.js"></script>
<script type="text/javascript"
	src="resources/js/chartJS/chartjs_label_plugin.js"></script>
<script type="text/javascript"
	src="resources/js/chartJS/create_graphs.js"></script>
<script type="text/javascript"
	src="resources/js/services/application-tracking.js"></script>
<script>
    var date = new Date();
    date.setDate(date.getDate()-1);
    
    var ydate= date.getDate() + '-' + (date.getMonth()+1) + '-' + date.getFullYear();
</script>
<script>
window.addEventListener( "pageshow", function ( event ) {
	  var historyTraversal = event.persisted || ( typeof window.performance != "undefined" && window.performance.navigation.type === 2 );
	  if ( historyTraversal )
	  {
		  //alert("Wwwww");
	    // Handle page restore.
	    window.location.reload(true);
	  }
	});
</script>
<style>
.showDiv {
	visibility: visible
}

.hidden {
	visibility: hidden
}

.table th {
	text-align: center;
}

.table td {
	font-weight: bold;
}

.content-section .active {
	box-shadow: 0 0 5px #ccc;
	box-shadow: 15px 4px grey;
	border: 2px solid grey;
}

#loading {
	position: fixed;
	top: 50%;
	left: 48%;
}

#loading img {
	width: 50px;
}
/*  #columnHeaderText text{text-align:right;} */
</style>
<title>Dash Board</title>

</head>
<body id="" onload="burstCache()">
	<%-- <div style="vertical-align: middle">
		<span><img src="resources/img/auth/home/ashoka-black.png"/></span>
		<span style="padding-left: 10px;"><img src="resources/img/auth/login/mha-logo.png"/></span> 
		<span><img src="resources/img/auth/home/seprator.jpg"/></span>
		 <span><img src="resources/img/auth/home/app-title.png"/></span>
		<!-- <span class="RBN">Estates Management Portal </span> -->
	</div>
	<div id="pageHeading"
		style="z-index: 100; background-color: #4184f3; color: #FFF; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.26); box-sizing: border-box;">
		<span style="font-size: 24px;">&nbsp;&nbsp;Dashboard Service</span> <a
			href="Logout"><span
			style="font-size: 15px; color: #FFF; float: right; margin: 6px;"><%= session.getAttribute("fullName") %>&nbsp;(Logout)
		</span></a>
	</div> --%>
	<div class="container-fluid">
		<div class="content-section">			
				<!-- <div id="box1" class="col-xs-3"
					onclick="javascript:getdashboardServiceStatus();">

					<div class="notification-box bgm-teal">
						<div class="notification-summary">

							<div class="image">
								<i class="glyphicon glyphicon-registration-mark"
									style="font-size: 48px"></i>
							</div>
							<div class="count">
								<h2>Registration</h2>
								<br />
							</div>
							<div class="count">
								<h2>Registration</h2><br/> 
								Data Available Since : 14-12-2014<br /> Applications Received :
								<span id="tc-3"></span><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Granted : <span
									id="gr-3"></span>&nbsp;&nbsp;(<span id="regg-3"></span>&nbsp;%)<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Under Process : <span
									id="pc-3"></span>&nbsp;&nbsp;(<span id="regu-3"></span>&nbsp;%)<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    Rejected / Closed : <span
									id="cc-3"></span>&nbsp;&nbsp;(<span id="regr-3"></span>&nbsp;%)<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data Available Till :
								<script>document.writeln(ydate)</script>
								<br /> <a class="getregistrationdetail"
									onclick="javascript:getdashboardServiceStatus();">more...</a>
							</div>
							<div class="clear"></div>
						</div>
					</div>
				</div>

				
				<div id="box2" class="col-xs-3"
					onclick="getdashboardPriorPermissionServiceStatus();">
					<div class="notification-box bgm-orange">
						<div class="notification-summary">
							<div class="image">
								<i class="fas fa-parking" style="font-size:48px"></i>
								<i class="fas fa-paragraph" style="font-size:48px"></i>
								<i class="fas fa-parking" style="font-size: 48px"></i>
							</div>
							<div class="count">
								<h2>Prior Permission</h2>
								<br />
							</div>
							<div class="count">
								<h2>Prior Permission</h2><br /> 
								Data Available Since : 14-12-2014<br />Applications Received :
								<span id="tc-2"></span><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Granted : <span
									id="gr-2"></span>&nbsp;(&nbsp;<span id="regg-2"></span>&nbsp;%)<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Under Process : <span
									id="pc-2"></span>&nbsp;(&nbsp;<span id="regu-2"></span>&nbsp;%)<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   Rejected / Closed : <span
									id="cc-2"></span>&nbsp;(&nbsp;<span id="regr-2"></span>&nbsp;%)<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Data Available Till :
								<script>document.writeln(ydate)</script>
								<br /> <a class="getperiorpermissiondetail"
									onclick="getdashboardPriorPermissionServiceStatus();">more...</a>
							</div>
							<div class="clear"></div>
						</div>
					</div>
				</div>
				<div class="col-xs-3">
					<div class="notification-box bgm-lightgreen"
						onclick="javascript:getdashboardRenewalServiceStatus();">
						<div class="notification-summary">
							<div class="image">
								<i class="fas fa-registered" style="font-size: 48px"></i>
							</div>
							<div class="count">
								<h2>Renewal</h2>
								<br />
							</div>
							<div class="count">
									<h2>Renewal</h2><br /> 
								Data Available Since : 14-12-2014<br /> Applications Received :
								<span id="tc-4"></span><br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Granted : <span
									id="gr-4"></span>&nbsp;(&nbsp;<span id="regg-4"></span>&nbsp;%)<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Under Process : <span
									id="pc-4"></span>&nbsp;(&nbsp;<span id="regu-4"></span>&nbsp;%)<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Rejected / Closed : <span
									id="cc-4"></span>&nbsp;(&nbsp;<span id="regr-4"></span>&nbsp;%)<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data Available Till :
								<script>document.writeln(ydate)</script>
								<br /> <a class="getregistrationdetail"
									onclick="javascript:getdashboardRenewalServiceStatus();">more...</a>
							</div>
							<div class="clear"></div>
						</div>
					</div>
				</div>
				<div class="col-xs-3">
					<div class="notification-box bgm-cyan"
						onclick="javascript:getdashboardHospitalityServiceStatus();">
						<div class="notification-summary">
							<div class="image">
								<i class="fas fa-hospital-symbol" style="font-size:48px"></i>
								<i class="fas fa-hospital-symbol" style="font-size: 48px"></i>
							</div>
							<div class="count">
								<h2>Hospitality</h2>
								<br />

							</div>
							<div class="count">
									<h2>Hospitality</h2><br/> 
								Data Available Since : 14-12-2014<br /> Applications Received :
								<span id="tc-1"></span><br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Granted : <span
									id="gr-1"></span>&nbsp;(&nbsp;<span id="regg-1"></span>&nbsp;%)<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Under Process : <span
									id="pc-1"></span>&nbsp;(&nbsp;<span id="regu-1"></span>&nbsp;%)<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Rejected / Closed : <span
									id="cc-1"></span>&nbsp;(&nbsp;<span id="regr-1"></span>&nbsp;%)<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Data Available Till :
								<script>document.writeln(ydate)</script>
								<br /> <a class="getregistrationdetail"
									onclick="javascript:getdashboardHospitalityServiceStatus();"><span
									style="text-align: right;">more...</span></a>
							</div>
							<div class="clear"></div>
						</div>
					</div>
				</div>
			</div> -->
                 <!--  <div class="row" id="barGraphDivRegistration1">
				<div class="col-xs-5">
					<div id="graphApplicationwise" style="position: relative; height: 300px;margin-top: 34px;" ></div> 
					<canvas id="getDashboardPieReg" width="15" height="15"></canvas>
					<canvas id="getDashboardPiePri" width="15" height="15"></canvas>
					<canvas id="getDashboardPieRen" width="20" height="20"></canvas>
					<canvas id="getDashboardPieHos" width="20" height="20"></canvas>

				</div>
				<div class="col-xs-7">
					<div id="graphApplicationwise1" style="position: relative; height: 300px;margin-top: 34px;"></div> 
					<canvas id="getDashboardPiePri" width="15" height="15"></canvas>
					
				</div>
			</div> -->
			<div class="row">
				<div class="col-xs-6">
			       <canvas id="getDashboardPieReg" width="10" height="10"></canvas>
			    </div>
			    <div class="col-xs-6">
					<canvas id="getDashboardPiePri" width="10" height="10"></canvas>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6">
					<canvas id="getDashboardPieRen" width="10" height="10"></canvas>
				</div>
				<div class="col-xs-6">
					<canvas id="getDashboardPieHos" width="10" height="10"></canvas>
				</div>
			</div>
			<div id="barGraphDivRegistration">
			<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- <div id="graphApplicationwise" style="position: relative; height: 300px;margin-top: 34px;" ></div>  -->
					
					<canvas id="graphApplicationwise" width="700" height="400"></canvas>

				</div>
			</div>
			<br /><br/>
			<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- <div id="graphApplicationwise1" style="position: relative; height: 300px;margin-top: 34px;"></div>  -->
					
					<canvas id="graphApplicationwise1" width="700" height="305"></canvas>
				</div>
			</div>
			</div>
			<div class="row" id="barGraphDivPriorPermission">
			<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- <div id="createBarGraphPriorPermission1" style="position: relative; height: 300px;margin-top: 34px; "></div>  -->
					
					<canvas id="createBarGraphPriorPermission1" width="700"
						height="400"></canvas>
				</div>
				</div>
				<br /><br/>
				<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- <div id="createBarGraphPriorPermission2" style="position: relative; height: 300px;margin-top: 34px; "></div> -->
					
					<canvas id="createBarGraphPriorPermission2" width="700"
						height="305"></canvas>
				</div>
				</div>
			</div>
			<div class="row" id="barGraphDivRenewal">
			<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- <div id="createBarGraphRenewal1" style="position: relative; height: 300px;margin-top: 34px; "></div>  -->
					<canvas id="createBarGraphRenewal1" width="700" height="400"></canvas>
				</div>
				</div>
				<br /><br/>
				<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- <div id="createBarGraphRenewal2" style="position: relative; height: 300px;margin-top: 34px; "></div>  -->
					<canvas id="createBarGraphRenewal2" width="700" height="305"></canvas>
				</div>
				</div>
			</div>
			<div class="row" id="barGraphDivHospitality">
			<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- 	<div id="createBarGraphHospitality1" style="position: relative; height: 300px;margin-top: 34px; "></div>  -->
					<canvas id="createBarGraphHospitality1" width="700" height="400"></canvas>
				</div>
				</div>
				<br /><br/>
				<div class="row">
				<div class="col-xs-offset-2 col-xs-8">
					<!-- <div id="createBarGraphHospitality2" style="position: relative; height: 300px;margin-top: 34px; "></div>  -->
					<canvas id="createBarGraphHospitality2" width="700" height="305"></canvas>
				</div>
				</div>
			</div>

			<div class="row" id="barGraphDiv11"></div>
			<!-- 	<div class="row" id="barGraphDiv22">
		</div>
		<div class="row" id="barGraphDiv33">
		</div>
		<div class="row" id="barGraphDiv44">
		</div> -->
		</div>

	</div>
	<!-- <div id="application-details" >
</div> -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="exampleModalLabel">Modal</h4>
				</div>
				<div class="modal-body" id="application-details">Modal
					content</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id="loading">
		<p>
			<img src="resources/img/auth/home/loading.webp" />
		</p>
	</div>
</body>

</html>