<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>[Page Head]</title>	
		<script src="resources/js/jquery-1.11.3.js"></script>	
		<script type="text/javascript" src="resources/js/all.js"></script>
		<script type="text/javascript" src="resources/js/forAll.js"></script>
		<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script>	
		<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
    	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
    	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
		<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />	
		<!-- include this to make your page width fixed to about 800px; -->
		<!--<link rel="stylesheet" href="resources/css/bootstrap/all/non-responsive.css" type="text/css" />	-->
		<link rel="stylesheet" href="resources/css/all.css" type="text/css" />
		<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>
	</head>
	<body id="body">
		<div class="container">
			<span style="position:absolute; right:3px; top: 3px; z-index: 1;" id="process-time" class="label label-warning"> seconds</span>
			<!-- No Page Head Required - page head will be same as link name in menu-->
			<!-- <div class="row" id="page-head">
			    <div class="col-xs-12">
			    	<h1 class="RBN">[Page Head]</h1>
			    	<hr/>
			    </div>
		    </div>-->
		    <div style="z-index:2; position: absolute; bottom: 10px; width: 30%;" id="sticky-notify"></div>
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
		    <!-- Start your work from here ONLY!! -->
		    <div class="row">
				<div class="col-xs-12">
				</div>
		    </div>
		</div>	
	</body>
</html>

