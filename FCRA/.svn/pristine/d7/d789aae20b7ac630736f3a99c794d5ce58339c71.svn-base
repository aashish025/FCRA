<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js"></script>	
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />			
	<script type="text/javascript" src="resources/js/input-mask/input-mask.js"></script> 
	<script type="text/javascript" src="resources/js/all.js"></script>	
	<link rel="stylesheet" type="text/css" href="resources/css/common.css" />	
	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js"></script>		
	<script type="text/javascript" src="resources/js/reports/dash-board.js"></script>
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
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
	 <!-- <script type="text/javascript" src="js/barchart/jqplot.highlighter.min.js"> </script>  -->
	 <script type="text/javascript" src="resources/js/barchart/jqplot.highlighter.js"> </script>
	 <link rel="stylesheet" href="resources/css/barchart/jquery.jqplot.css" />
	 <style> .showDiv{visibility : visible} .hidden{visibility :hidden}</style>	
	<title>Dash Board</title></head>

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
			<div class="row" id="barGraphDiv">
			<div class="col-xs-5">
				<div id="graphRegYearWise" class="regStats graph2" style="position: relative; height: 300px;margin-top: 34px; "></div>	
 				<div id="graphDonorwise" class="donorWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div>	
				<div id="graphAssowise" class="assoWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div>
				<div id="graphCountrywise" class="countryWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div> 
				<div id="graphApplicationwise" class="applicationWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div> 
			</div>
			<div class="col-xs-5">
				<div id="graphRegYearWise1" class="regStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div>	
				<div id="graphFinDonorwise" class="donorWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div>	
				<div id="graphFinAssowise" class="assoWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div>
				<div id="graphFinCountrywise" class="countryWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div>
				<div id="graphFinApplicationwise" class="applicationWiseStats graph2" style="position: relative; height: 300px;margin-top: 34px;"></div> 
			</div>
			<br/><br/><br/><br/><br/>
			<div class="col-xs-2" >
			 		<!-- <input type="radio" name="graphType" class="graph" value="r" id="registrationWise" onclick="javascript:defaultGraph();" checked > Registration Statistics year-wise <br> --> 
			 		<input type="radio" name="graphType" class="graph" value="r" id="registrationWise" onclick="javascript:showGraph(1);" checked = "checked" /> Registration Statistics year-wise<br/>
					<input type="radio" name="graphType" class="graph" value="s" id="applicationWise" onclick="javascript:showGraph(5);" /> Application Statistics<br/>
					<input type="radio" name="graphType" class="graph" value="d" id="donorWise" onclick="javascript:showGraph(2);" /> Top Donor-wise<br/>
					<input type="radio" name="graphType" class="graph" value="c" id="countryWise" onclick="javascript:showGraph(3);"/> Top Country-wise<br/>
					<input type="radio" name="graphType" class="graph" value="a" id="associationWise" onclick="javascript:showGraph(4);"/> Top Association-wise		  			
			</div>
			
 			</div>    
	    	
	    	<br/>
 			<div class = "row" >
 				<div class = "col-xs-5"></div>
 				<div class = "col-xs-7" style="display: none;"  id="yaxisDesc"></div>
 			</div>		
		 </div>	
	
		
	</div>
		 	 	 
		      
 </body> 
 
</html>
