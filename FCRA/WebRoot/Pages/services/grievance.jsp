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
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>	
	<script type="text/javascript" src="resources/js/services/grievance.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
	<title>Grievance Entry</title>
	<script type="text/javascript">
	
	$(document).ready(function(){
		var list=JSON.parse('${IbList}');
	    var id="checkId";
		var name="checkname";			
			var divObj = $('#office-list');
			var table = $('<table/>', {'class':'checkbox-table'}).append($('<tbody/>', {}));
			var tbody = table.find('tbody');
			var numCols = 5;
			divObj.empty();
			$.each(list, function(index, item){
				if(index%numCols == 0)
					tbody.append($('<tr/>', {}));
				tbody.find('tr:last').append(
						$('<td/>', {}).append(
								$('<label/>', {'class':'checkbox-inline', 'text':item.v}).prepend(
										$('<input/>', {'type':'checkbox', 'id':id+'-'+item.k,'name':name,'value':item.k}))));	
			});
			divObj.append(table);
	});	
	
	</script>
	</head>
	<body id="">	
	<div class="container-fluid">
		<div class="content-section">
			 <input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
			 <form action="grievance-type-form" method="get" id="grievance-type-form" enctype="multipart/form-data">
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
		    <div id="grievance-type">
			    <div class="row">		    	 	
			    	<div class="col-xs-3 pp-form-field" id="radio-details-div">
			    			<label >Is Association  Registered ? </label>
							<table class="checkbox-table">
								<tbody>
									<tr>
										<td>
											<label class="radio-inline" >
			    		          <input type="radio" onchange="checkGrievanceType(this.value);" id="grievance-n" name="grievance"  value="N" >No </input></label>
										</td>
										
										<td>
											<label class="radio-inline">
						   <input type="radio" onchange="checkGrievanceType(this.value);" id="grievance-y" name="grievance" value="Y" >YES</input></label>
										</td>
										
									</tr>
								</tbody>
							</table>
			    
			    	</div>			    	
			    </div>	
		    </div>
		   <!--  If NOT registered -->
		      <div id="details-div" style="display:none">
		          <div class="bs-callout bs-callout-info">
						<span>Association Details</span>
					</div>
		    		<div class="row" >						
						<div class="col-xs-3 pp-form-field" id="">
									<label for="assoName">Association Name: </label>
									<input type="text" name="assoName" id="assoName" class="form-control validate[ required,custom[onlyLetterSp],maxSize[100]] clear"/>
						</div>
						<div class="col-xs-3 pp-form-field">
							<label for="state">State: </label>
							<select id="state" name="state"  onchange="javascript:fetchdistrict(this.value);" class="form-control validate[required] clear">
							 	<option value="">Select State</option>
							 	<c:forEach items="${stateList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach>
							</select>
						</div>
					<div class="col-xs-3 pp-form-field">
							<label for="district"> District: </label>
							<select id="district" name="district" class="form-control validate[required]  clear">
							 	<option value="">Select District</option>
							 	<c:forEach items="${districtList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach>
							</select>
						</div> 
						</div>
						<div class="row">
							<div class="col-xs-3 pp-form-field" id="">
									<label for="assoAddress" >Association Address: </label>
									<textarea name="assoAddress" id="assoAddress" rows="3" class="form-control validate[required,custom[remarkAddress],maxSize[150]] clear"></textarea>
							</div>
							<div class="col-xs-3 pp-form-field" id="">
									<label for="assoTownCity" >Association Town City: </label>
									<input type="text" name="townCity" id="townCity" class="form-control validate[custom[onlyLetterSp],maxSize[50]] clear"/>
							</div>
							<div class="col-xs-3 pp-form-field" id="">
									<label for="assoPinCode" >Association Pincode: </label>
									<input type="text" name="assoPincode" id="assoPincode" class="form-control validate[custom[onlyNumberSp],maxSize[6]] clear"/>
							</div>
                           </div>
                        
						</div>
						<!-- // IF ALREAY REGISTRED -->
						 <div id="grievance-yes" class="row" style="display:none">
						<div class="col-xs-4 pp-form-field" id="" >
						<label for="registrationNo">Registration Number: </label>
						<div class="input-group">
							<input type="text" class="form-control uppercase validate[required, custom[onlyLetterNumber], maxSize[12]] clear " id="registrationId" placeholder="Registration Number" name="registrationNo">
							<span class="input-group-btn">
								<button class="btn btn-info" id="registration-search-btn" onclick="javascript:searchRegistrationDetails();" type="button"><span class="fa fa-search"></span>&nbsp;</button>
								<button class="btn btn-default" id="registration-view-btn" onclick="javascript:viewRegistrationDetails();" type="button"><span class="fa fa-eye"></span>&nbsp;</button>
					    	</span>
						</div>				
					</div>
		    		</div>
		    		
		    		<!-- Common field -->
		    		 <div id="comm-div" style="display:none">
		                   <div class="bs-callout bs-callout-info">
						    <span>Complainant Details</span>
					    </div>
		    		 <div class="row">
                         <div class="col-xs-3 pp-form-field" id="">
									<label for="comName" >Complainant Name: </label>
									<input type="text" name="comName" id="comName" class="form-control validate[required, custom[onlyLetterSp],maxSize[100]] clear"/>
							</div>
							<div class="col-xs-3 pp-form-field" id="">
									<label for="comAddress" >Complainant Address: </label>
									<textarea name="comAddress" id="comAddress" row="3" class="form-control validate[required,custom[remarkAddress],maxSize[150]] clear"></textarea>
							</div>
							<div class="col-xs-3 pp-form-field" id="">
									<label for="comEmail" >Complainant Email: </label>
									<input type="text" name="comEmail" id="comEmail" class="form-control validate[custom[email]] clear"/>
							</div>
                         </div>
                         <div class="row">
                           <div class="col-xs-3 pp-form-field" id="">
									<label for="comMobile" >Complainant Mobile No.: </label>
									<input type="text" name="comMobile" id="comMobile" class="form-control validate[custom[phone]] clear"/>
							</div>
							</div>
							 <div id="" >
									 <div class="bs-callout bs-callout-info">
								              <span>Complaint Details</span>
							          </div>
				   				<div class="row">
									   <div class="col-xs-8 pp-form-field " id="">
											 <label for="grievanceDesc" >Complaint : </label>
								          <textarea	class="form-control  validate[required,maxSize[3000] custom[remarkAddress]] clear"rows="6" id="complaint" name="complaint"></textarea>
									  </div>
								  <div class="col-xs-4 pp-form-field">
					              <label for="officevalue">Forward To </label>
						             	<div id="office-list" class="clear" >
							    </div>						 
					 </div>
			                    	</div>	
							 </div>
						 <div class="row">
								<div class="col-xs-3 pp-form-field">
								<label for="user">Assign User: </label>
								<select id="user" name="user"   class="form-control validate[required] clear">
								 	<option value="" >Assign User</option>
								 	<c:forEach items="${userList}" var="nType">
								 		<option value='<c:out value="${nType.li}"/>'><c:out value="${nType.ld}"/></option>
								 	</c:forEach>
								</select>
						    </div>
					   <div class="col-xs-3 pp-form-field" id ="fileUpload" >
					       <label for="documentfile">Upload Supporting Docs <font color="red" style="font-size:20px;vertical-align:baseline"> *</font>
				          	<br> <font color="red"> ( Should be in pdf format ) </font></label>
 						  <input style="background-color: #FEFCFF;" type="file" name="documentFile"  id="documentFile"  class = "span12 validate[required]"  required ="required"  class = "span12 "  title="File Extention Should be .pdf"/>
      				  </div>
						    
						 </div> 
						 <div id="add-details-btn">
								<div class="row">
									<div class="col-xs-2 pp-form-field">
										<button type="button" class="btn btn-primary "	onclick="javascript:SubmitDetails();">
											<span class="fa fa-plus clear"></span> Submit
										</button>
									</div>
								</div>
							</div>
						 </div>
						 </form>
                      </div>
                         
		    	<!-- 	modal for application Id -->
			
			<div class="modal fade" id="grievance-add-modal" tabindex="-1" role="dialog" aria-labelledby="grievance-add-modal-label" data-keyboard="false" data-backdrop="static">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="grievance-add-modal-label">Search Registration No.</h4>
			      </div>
			      <div class="modal-body">
				      <div class="container-fluid">
					 		<div class="row">
						  		<div class="col-xs-10">
						  			<div id="grievance-modal-notification-area">
						  			</div>
								</div>
					 		</div>
					 	 <div id="modal-div" style="display:none">
							<div class="row">
								<div class="col-xs-3" >
									<label for="stateSearch">State: </label>
									<select id="stateSearch" name="stateSearch"  onchange="javascript:fetchdistrictmodal(this.value);" class="form-control validate[required] clear">
									 	<option value="">Select State</option>
									 	<c:forEach items="${stateList}" var="nType">
									 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
									 	</c:forEach>
									</select>
								</div>
								  <div class="col-xs-3">
										<label for="districtSearch"> District: </label>
										<select id="districtSearch" name="districtSearch" class="form-control validate[required] clear">
										 	<option value="">Select District</option>
										 	<c:forEach items="${districtList}" var="nType">
										 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
										 	</c:forEach>
										</select>
						         </div> 
						         <div class="col-xs-3 ">	
						         		<label for="associationName">Association Name</label>			
							         <input type="text" class="form-control validate[maxSize[15]] clear"  name="applicationName" id="applicationName" style="text-transform: uppercase;"></input>				
						        </div> 
						         <div class="col-xs-1 pp-inline-button">
							      <button class="btn btn-info pp-inline-button"  onclick="javascript:searchDetail();" type="button"><span class="fa fa-search"></span>&nbsp;Search</button>
						        </div>
						     </div>
						     <div class="row pp-form-field">
							<div id="reg-list"></div>
						        </div>
							</div>
						</div>
			    </div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					
				</div>
				</div>
			  </div>
			 </div>
			
			</div>
		
	

	</body>
	
	</html>
	
	