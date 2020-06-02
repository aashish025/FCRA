<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
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
	<script type="text/javascript" src="resources/js/masters/office.js"></script>	
	<script type="text/javascript" src="resources/js/utility/uploader.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>
	
	<title>Office Details</title>
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
				    
	<div class="row">
				<div class="col-xs-11 pp-form-field" id="office-list">
				</div>
		    </div>
			<div class="form-group">
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="table-btn" style="display:none;">
						<button type="button" class="btn btn-link" onclick="javascript:showTable();">
						<span class="fa fa-table"></span> View Table</button>					
					</div>
				</div>	
				<div class ="row">
				 <div class="col-xs-9 pp-form-field" id="officeCodeDiv-edit" style="display: none">
							 <p class="text-info" id="officeCodeLebel" style="font-size:25px"></p></div>
						</div>
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Office</button>					
					</div>
				</div>				
		    </div>
		    <div id="form-div" style="display:none;">
		    	<form id="office-form" action="" enctype="multipart/form-data" method="post">
		    		<input type="hidden" name="officeId" id="officeId"/>
			    	<div class="row" >
						<div class="col-xs-3 pp-form-field" id="officeTypeDiv">
							<label for="officeType">Office Type: </label>
							<select id="officeType" name="officeType" class="form-control  validate[required] clear">
							 	<option value="">Select Office Type</option>
							 	<c:forEach items="${officeTypeList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach>
							</select>
					   </div>	
        		<div class="col-xs-3 pp-form-field" id="officeCodeDiv">
								<label for="officeCode">Office Code: </label>
								<input type="text" name="officeCode" id="officeCode" class="form-control  validate[required,maxSize[5] custom[onlyLetterNumber]] clear"/>
						</div>
						
						
<!-- 						 <div class="col-xs-3 pp-form-field " id="officeTypeDiv-edit" style="display: none" >
						 <label for="officeName">Office Type: </label>
							<h4><p  class="text-info" id=officeTypeLebel></p></h4> </div>  -->

				     <div class="col-xs-3 pp-form-field" id="">
								<label for="officeName">Office Name: </label>
								<input type="text" name="officeName" id="officeName" class="form-control  validate[custom[onlyLetterNumberSp]] clear"/>
						</div>
					</div>
				<div class="row" >
						<div class="col-xs-3 pp-form-field" id="">
								<label for="officeCity">City: </label>
								<input type="text" name="officeCity" id="officeCity" class="form-control  validate[maxSize[30] custom[onlyLetterNumberSp]] clear"/>
						</div>
			            <div class="col-xs-3 pp-form-field" id="">
								<label for="officeState">State: </label>
								<input type="text" name="officeState" id="officeState" class="form-control  validate[maxSize[30] custom[onlyLetterNumberSp]] clear"/>
						</div>
			     <div class="col-xs-3 pp-form-field">
							<label for="officeCountry">Country: </label>
							<select id="officeCountry" name="officeCountry" class="form-control  validate[required] clear">
							 	<option value="">Select Country</option>
							 	<c:forEach items="${countryTypeList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
			            <div class="col-xs-3 pp-form-field" id="">
								<label for="officeZipCode">Zip Code: </label>
								<input type="text" name="officeZipCode" id="officeZipCode" class="form-control  validate[maxSize[6] custom[integer]] clear"/>
						</div>						
                <div class="col-xs-3 pp-form-field" id="">
								<label for="officeContact">Contact No.: </label>
								<input type="text" name="officeContact" id="officeContact" class="form-control  validate[maxSize[30] custom[phoneSpace]] clear"/>
						</div>
	                    <div class="col-xs-3 pp-form-field" id="">
								<label for="officeEmail">Email Id: </label>
								<input type="text" name="officeEmail" id="officeEmail" class="form-control validate[custom[email]] clear"/>
						</div>		
					</div>	
									
					<!-- <div class="row">
						<div class="col-xs-3 pp-form-field ">								
								<button type="button" class="btn btn-warning" onclick="javascript:openFile();" title="click to attch files">
								<span class="glyphicon glyphicon-paperclip"></span></button>	
								<input type="file" name="attachment" id="attachment" style="display:none;"/>
						</div>
					</div> -->
									<div class="row" >
			  <%--    <div class="col-xs-3 pp-form-field">
							<label for="currencyType">Currency: </label>
							<select id="currencyType" name="currencyType" class="form-control  validate[required] clear">
							 	<option value="">Select Currency</option>
							 	<c:forEach items="${currencyTypeList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach>
							</select>
						</div> --%>
					<%--   <div class="col-xs-3 pp-form-field">
							<label for="officeTimeZone">Time Zone: </label>
							<select id="officeTimeZone" name="officeTimeZone" class="form-control  validate[required] clear">
							 	<option value="">Select Time Zone</option>
							 	<c:forEach items="${timeZoneList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach>
							</select>
						</div> --%>
			            <div class="col-xs-3 pp-form-field" id="officeSigntorydiv">
								<label for="officeSigntory">Signatory: </label>
								<input type="text" name="officeSigntory" id="officeSigntory" class="form-control  validate[custom[onlyLetterNumberSp]] clear"/>
						</div> 

					</div>
					<div class="row">
						<div class="col-xs-7 pp-form-field" id="">
								<label for="officeAdd">Address: </label>
								<textarea class="form-control  validate[maxSize[150] custom[remarkAddress]] clear" rows="3" id="officeAdd" name="officeAdd"></textarea>
						</div>
					
					</div>
					<div class="row">
						<div class="col-xs-2 pp-form-field" id="add-details-btn">
							<button type="button" class="btn btn-primary" onclick="javascript:addOffice();">
							<span class="fa fa-plus"></span> Add Details</button>					
						</div>
					</div>					
					<div id="edit-actions" class="form-inline" style="display:none;">
						<div class="row"> 
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:editoffice();">
									<span class="fa fa-edit"></span> Edit</button>
									<button type="button" class="btn btn-danger" onclick="javascript:deleteOffice();">
									<span class="fa fa-close"></span> Delete</button>					
							</div>	
						</div>											
					</div>												
			    </form>	    
		    </div> 
			
		</div>
	</div>      
</body>
</html>
