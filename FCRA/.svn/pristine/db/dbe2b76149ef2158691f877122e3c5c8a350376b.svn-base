<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />	
	<script type="text/javascript" src="resources/js/all.js"></script>
	<script type="text/javascript" src="resources/js/auth/home/user-notify.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
	<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>	
	<script type="text/javascript" src="resources/js/masters/User-Audit.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
	<style>
		.redcolor
		 {
		   color: red;
		}
	</style>
		
	<title>User Audit</title>
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
		    	<div class="row" style="display:none; padding-left: 29px;" id="back-section-audit">
					<div class="col-xs-11 pp-form-field">
						<button type="button" class="btn btn-default active" onclick="javascript:goBack();"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;Back</button>
					</div>
	
				</div>
		  		 <div id="output-section-audit"  style="display:none; padding-left: 29px;">	
					<div class="bs-callout bs-callout-info" id="output-bs">
						<span>Output</span>
					</div>	
					<div style="padding-left: 10px;" class="row" >
						<div class="col-xs-11 pp-form-field" id="userAudit-list"></div>
				    </div>
				</div>
		     </div>
		    
		    <div id="form-div-audit"  >
		    	<form action="" method="get" id="user-audit-form" style="padding-left: 29px;">
				     <div class="row">
				   	 <div class="col-xs-3 pp-form-field">
							<label for="audit" id="lgc">User Name :  </label>
							<select id="auditId" name="auditId"  multiple="multiple" class="form-control validate[required]"  >
							 	<option value="ALL1">ALL</option>
						 		<c:forEach items="${userAuditList}" var="nType">
						 		<!-- START -->
							 		<c:set var="message" value="${nType.v}"/> 
							 		<c:choose>
							 			 <c:when test="${fn:containsIgnoreCase(message,'DELETE')}" >
												<option  class="redcolor" value='<c:out value="${nType.k}"/>'>
									 		  		<c:out value="${nType.v}"/></option> 
	       											 <br />
	   									 </c:when>
		   								<c:otherwise>
		    								 <option abc="aaa" value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option> 
		        						<br />
		   								</c:otherwise>
									</c:choose>
							 	</c:forEach>
							</select>
						</div> 
						<div id="form-to-date">								
				             <div class="col-xs-3 pp-form-field" id="from-date-div">
								<label for="from-date">From Date
								</label> <input type="text" name="from-date" id="fromDate"
									class="form-control validate[required] clear" />
							</div>
							<div class="col-xs-3 pp-form-field" id="to-date-div">
								<label for="to-date">To Date
								</label> <input type="text" name="to-date" id="toDate" class="form-control validate[required,date,futureEqualField[fromDate]] clear" />
							</div>
						</div>
					</div> 
					<div class="row">
						<div id="add-details-btn">
							<div class="col-xs-3 pp-form-field">
								<button type="button" class="btn btn-primary " onclick="javascript:submituser();">
								<span class="fa fa-plus clear"></span> Submit</button>					
							</div>
			    		</div>
					</div>								
		      </form>
		    </div> 
			
		</div>

      

</body>

<script>
$('#fromDate').datetimepicker({
	lang:'ch',
	timepicker:false,
	format:'d-m-Y',
	formatDate:'dd-mm-yyyy'	
});

$('#toDate').datetimepicker({
	lang:'ch',
	timepicker:false,
	format:'d-m-Y',
	formatDate:'dd-mm-yyyy'	
});
</script>

</html>
