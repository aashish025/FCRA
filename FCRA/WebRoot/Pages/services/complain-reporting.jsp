<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<%-- <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script>
function submitForm () {
    // your code
	document.getElementById("complaintDetails").reset();
	document.getElementById("complaintDetails_complaintSubject").reset();
	document.getElementById("complaintDetails_complaintSubject").empty();
	document.getElementById("complaintDetails_complaintSubjec").value('empty');
}
</script>
<style>  
.error{color:red}  
</style>  
<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />	
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />	
</head>
<body onload="submitForm()">

<form:form class="master-form-validate-jquery" id="complaintDetails"  commandName ="triform" method="POST" action="trisupportform-issue-add-reporting" enctype="multipart/form-data">
	${cid}					
				<h4>Add Complain</h4>
<div class="row">
 		<!-- DATA FIELD : complaintId -->
<%-- <div class="form-group col-lg-4 form-group-float">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintId" class="form-group-float-label"><spring:message code="complaintDetails.complaintId"/></label>
					
  						<form:input id="complaintDetails_complaintId" path="complaintId" class="form-control" maxlength="15"  placeholder="Complaint Id"/>

					</div> --%>

 
				<!-- DATA FIELD : complaintSubject -->
<div class=" col-lg-4 form-group-float">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintSubject" class="form-group-float-label">Technical Issue Subject**</label>
					
  						<form:input id="complaintDetails_complaintSubject" path="complaintSubject" class="form-control" maxlength="500"  placeholder="Complaint Subject" autocomplete="off"/>
                        <form:errors path="complaintSubject" cssClass="error"/>  
					</div>
</div>
 
				<!-- DATA FIELD : complaintCategory -->
<div class="col-lg-4 form-group-float">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintCategory" class="form-group-float-label">Technical Issue Category**</label>

    <form:select path="complaintCategory" id="complaintDetails_complaintCategory"
												data-placeholder="Complaint Category"
												class="form-control form-control-select2">
	<form:option value="Error"><c:out value='Error' /></form:option>
	<form:option value="Support"><c:out value='Support' /></form:option>
	<form:option value="Other"><c:out value='Other' /></form:option>
											</form:select>
											 <form:errors path="complaintCategory" cssClass="error"/>  
					</div>
</div>
 
				<!-- DATA FIELD : complaintRaisedByTo -->
<%-- <div class="form-group col-lg-4 form-group-float">
					<!-- The field label is defined in the messages file (for i18n) -->
					<label for="complaintDetails_complaintSentTo" class="form-group-float-label"><spring:message code="complaintDetails.complaintRaisedBy"/></label>
					
  						<form:input id="complaintDetails_complaintRaisedBy" path="complaintRaisedBy" class="form-control" maxlength="30"  placeholder="Complaint RaisedBy" autocomplete="off"/>

					</div> --%>

 <!-- DATA FIELD : complaintMenuName -->
<div class="col-lg-4 form-group-float">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintMenuName" class="form-group-float-label">Technical Issue Menu Name**</label>
  <form:select path="complaintMenuName" id="complaintDetails_complaintMenuName"
												data-placeholder="Complaint Menu Name "
												class="form-control form-control-select2">
												 <c:forEach var="complaintType" items="${MenuList}"
													varStatus="status">
													<form:option value="${complaintType.getMenuname()}">
														<c:out value='${complaintType.getMenuname()}' />
													</form:option>
												</c:forEach> 
											</form:select>
											 <form:errors path="complaintMenuName" cssClass="error"/>  
					</div>
					</div>
</div>
<div class="row">					
				<!-- DATA FIELD : complaintSentDescription -->
<div class="col-lg-8 form-group-float">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintSentDescription" class="form-group-float-label">Technical Issue Description**</label>
<form:textarea path="complaintSentDescription" id="complaintDetails_complaintSentDescription" rows="4" cols="100" class="form-control" maxlength="4000"  placeholder="ComplaintSent Description" autocomplete="off"/>
			 <form:errors path="complaintSentDescription" cssClass="error"/>  
				</div>	
					</div>
					</div>
					
<div class="row">

	<!-- DATA FIELD : complaintSuggestion -->
<div class="col-lg-6 form-group-float">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
					
					<label for="complaintDetails_complaintSuggestions" class="form-group-float-label">Technical Issue Suggestion</label>
<form:textarea path="complaintSuggestions" id="complaintDetails_complaintSuggestions" rows="2" cols="100" class="form-control" maxlength="4000"  placeholder="Suggestions . . . " autocomplete="off"/>
			</div>		
					</div>
 

															
					<!-- DATA FIELD : attachmentFileName -->
<div class="col-lg-6 form-group-float upload-section" id="attachedFile">
<div class="form-group">
					<!-- The field label is defined in the messages file (for i18n) -->
				
					<label for="noticeBoardMaster_attachmentFileName" class="form-group-float-label">Upload File</label>
	
	<input type="file" class="form-control-uniform"  name="attachedFile1"  placeholder="Upload File" multiple="multiple" accept="image/jpg" autocomplete="off"/>
					</div>
					</div>
					
	</div>				
	<!-- DATA FIELD : complaintRepliedDescription -->
<%-- <div class="form-group col-lg-8 form-group-float">
					<!-- The field label is defined in the messages file (for i18n) -->
					<label for="complaintDetails_complaintRepliedDescription" class="form-group-float-label"><spring:message code="complaintDetails.complaintRepliedDescription"/></label>
<form:textarea path="complaintRepliedDescription" id="complaintDetails_complaintRepliedDescription" rows="2" cols="100" class="form-control" maxlength="4000"  placeholder="To be replied soon" autocomplete="off"/>
					</div> --%>				
			<div align="center">
					<!-- "SAVE" button ( SUBMIT button ) -->
						<button type="submit" id="submitButton" class="btn btn-primary" value="submit" >Submit</button>
					<input type="hidden" name="operationType" id="operationType"  value="Add"/>
    		
						<!-- "CANCEL" button ( HREF link ) -->		 				

		<a role="button" class="btn btn-default" data-dismiss="modal" onclick="submitForm()">Cancel</a>
				
					</div>
						
 
</form:form>

</body>
</html>