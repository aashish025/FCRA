<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<%-- <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js?Version=${version}"></script>
<link rel="stylesheet" type="text/css" href="resources/css/auth/home/notification.css" />
<link rel="stylesheet" type="text/css" href="resources/css/auth/home/home-notification.css" />
<script type="text/javascript" src="resources/js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap-full.min.js"></script>
<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />
<script type="text/javascript"src="resources/js/input-mask/input-mask.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
<script src="resources/js/iframeResizer.contentWindow.min.js" type="text/javascript"></script>
<script type="text/javascript" src="resources/js/forAll.js"></script>

<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>

<link rel="stylesheet"
	href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />


<link rel="stylesheet" href="resources/css/validationEngine.jquery.css"
	type="text/css" />
<script type="text/javascript"
	src="resources/js/services/ComplainReporting.js"></script>
<style>  
.error{color:red}  
</style>  
<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />	
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />	
</head>
<body>
<div>
<h6 style="font-size:23px" >${cid}</h6>	
</div>
<div class="row" id="barGraphDiv11"></div>
<div class="modal fade" id="tirreplymodel" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel">Reply</h4>
		      </div>
		      <div class="modal-body">
<form:form class="master-form-validate-jquery" id="complaintDetails"  commandName ="trireplyform" method="POST" action="trireplyform-issue-track-reporting" enctype="multipart/form-data">
                   <%--    <form:input id="complaintDetails_complaintSubject" path="complaintSubject" class="form-control" maxlength="500"  placeholder="Complaint Subject" autocomplete="off" />
                       --%>
                       <!-- DATA FIELD : complaintId -->
<div class="form-group col-lg-4 form-group-float">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintId" class="form-group-float-label">Complain Id**</label>
					
  						<form:input id="complaintDetails_complaintId" path="complaintId" class="form-control" maxlength="15" placeholder="Complaint Id" readonly="true"/>

					</div> 
					<div class=" col-lg-4 form-group-float">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintSubject" class="form-group-float-label">Complain Subject**</label>
					
  						<form:input id="complaintDetails_complaintSubject" path="complaintSubject" class="form-control" maxlength="500"  placeholder="Complaint Subject" autocomplete="off"/>
                        <form:errors path="complaintSubject" cssClass="error"/>  
					</div>
</div>
<div class="row">					
				<!-- DATA FIELD : complaintSentDescription -->
<div class="col-lg-8 form-group-float">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintSentDescription" class="form-group-float-label">Complaint Sent Description**</label>
<form:textarea path="complaintSentDescription" id="complaintDetails_complaintSentDescription" rows="4" cols="100" class="form-control" maxlength="4000"  placeholder="ComplaintSent Description" autocomplete="off"/>
			 <form:errors path="complaintSentDescription" cssClass="error"/>  
				</div>	
					</div>
					</div>
 
		      </div>
		      <div class="modal-footer">		
		      <!-- "SAVE" button ( SUBMIT button ) -->
						<button type="submit" id="submitButton" class="btn btn-primary" value="submit" >Submit</button>	
						</form:form>				      	
		      		<button type="button" class="btn btn-danger"  data-dismiss="modal">
		      		<span class="fa fa-close"></span>&nbsp;Close</button>  	
		      </div>
		    </div>
		  </div>
		</div>

</body>
</html>