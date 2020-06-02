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
	<script type="text/javascript" src="resources/js/reports/mis-reports.js"></script>		
	<script type="text/javascript" src="resources/js/multi-select/bootstrap-multiselect.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/multi-select/bootstrap-multiselect.css" />
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
	<script type="text/javascript" src="resources/js/utility/uploader.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>	
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>
<script>
		$(document).ready(function(){
			$('#project-detail-tab').show();
   				var x=[
   				       <c:forEach items="${notifyList}" var="ntn" varStatus="status">
   				       	{'d':'${ntn.d}',
   				       		'h':'${ntn.h}',
   				       		'l':'${ntn.l}',
   				       		'ms':'${ntn.ms}',
   				       		's':'${ntn.s}',
   				       		't':'${ntn.t}'
   				       		}
   				     	
   				       	</c:forEach>
   				       ];
   				if(x.length > 0) {
   					notifyList(x,'');
   					$('#project-detail-tab').hide();
   				}
   				
   				$("#report-form").validationEngine({promptPosition: 'topRight'});
   				
		});
    </script>

	
	<title>MIS Reports</title>
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
			<div id="report-input-section">
					<form id="report-form" >
						<div class="row">
							<div class="col-xs-4 pp-form-field " id="report-selection">
								<label >Report Type: </label>
								<select id="report-type" name="reportType" class="form-control validate[required]">
									<option value="">Select Report</option>
								 	<c:forEach items="${reportTypeList}" var="pType">
								 		<option value='<c:out value="${pType.k}"/>'><c:out value="${pType.v}"/></option>
								 	</c:forEach>
								</select>
							</div>
						</div>
						<div id="report-parameters-section" style="display: none;">
							<div class="bs-callout bs-callout-info">
								<span>Report Paramters</span>
							</div>
							<div id="report-parameters">
							</div>
						</div>
					
						<div id="report-submission" style="display: none;">
							</br><div class="bs-callout bs-callout-info">
								<span>Output Format</span>
							</div>
							<div class="row">
								<div class="col-xs-10 pp-form-field ">
									<label >Report Format: </label>
									<table class="checkbox-table">
										<tbody>
											<tr>
												<td>
													<label class="radio-inline">
														<input type="radio" id="report-format-1" val="1" name="reportFormat" class="validate[required]"/> PDF
													</label>
												</td>
												<td>
													<label class="radio-inline">
														<input type="radio" id="report-format-2" val="2" name="reportFormat" class="validate[required]"/> Excel
													</label>
												</td>
												<td>
													<label class="radio-inline">
														<input type="radio" id="report-format-3" val="3" name="reportFormat" class="validate[required]"/> HTML
													</label>
												</td>
												 <td >
													<label class="radio-inline">
														<input type="radio" id="report-format-4" val="4" name="reportFormat" class="validate[required]"  />CSV
													</label>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							
							<div class="row">
								<div class="col-xs-6 pp-form-field">
									<button type="button" class="btn btn-default" id="cancel-button">Cancel</button>
									<button class="btn btn-primary" type="button" data-loading-text="..." id="generate-button"><span class="fa fa-check"></span>Generate</button>
								</div>
							</div>						
						</div>
					</form>
			</div>	
			<div class="row" style="display:none;" id="back-section">
				<div class="col-xs-11 pp-form-field">
					<button type="button" class="btn btn-default active"  id="go-back-btn" onclick="javascript:goBack();"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;Back</button>
				</div>

			</div>
			<div id="output-section"  style="display:none;">	
				<div class="bs-callout bs-callout-info" id="output-bs">
					<span>Report Output</span>
				</div>	
				<div class="row">			
					<div id="report-data" class="col-xs-11 pp-form-field">
					</div>			
				</div>	
			</div>		
			<form action="" id="report-download-form" method="post">
			</form>
			
	    </div>
	</div>        
</body>
	
</html>
