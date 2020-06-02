<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js"></script>	
	<!-- <script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script> -->
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />			
	
	<script type="text/javascript" src="resources/js/all.js"></script>	
	<link rel="stylesheet" type="text/css" href="resources/css/common.css" />	
	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />	
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>
	<script type="text/javascript" src="resources/js/barchart/jquery.jqplot.js"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.barRenderer.js"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.categoryAxisRenderer.js"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.pointLabels.js"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.canvasAxisTickRenderer.js"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.canvasAxisLabelRenderer.js"></script>
	<script type="text/javascript" src="resources/js/barchart/jqplot.canvasTextRenderer.js"></script>	
	<script type="text/javascript" src="resources/js/reports/admin-dashboard.js"></script>	
	<link rel="stylesheet" href="resources/css/barchart/jquery.jqplot.css" />	
</head>
<body>
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
	<div class="col-sm-7 pp-form-field col-sm-offset-1">
		<div id="chart-div" class="jqplot-target" style="height: 250px; width:500px;"></div>					  						
<!-- 		<div id="chart-div"></div>						   
 -->	</div>
</body>

</html>