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
	<script type="text/javascript" src="resources/js/masters/user-console.js"></script>	
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>
	
	<title>User Console</title>
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
				<div class="col-xs-9 pp-form-field" id="user-list">	
				</div>
		    </div>
		    <div class="form-group">
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="table-btn" style="display:none;">
						<button type="button" class="btn btn-link" onclick="javascript:showTable();">
						<span class="fa fa-table"></span> View Table</button>					
					</div>
				</div>	
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" id="addBtn" data-toggle="modal" data-target="#exampleModal">
						<span class="fa fa-plus"></span> Add User</button>					
					</div>
				</div>
				<div id="user-info-div" style="display:none;">
					<div class="row" id="user-info-head-div"></div>
					<div class="row">
						<div class="col-sm-2">
							Name:<p class="text-danger" id="p-name"></p>
						</div>
						<div class="col-sm-2">
							Gender:<p class="text-danger" id="p-gender"></p>
						</div>
						<div class="col-sm-2">
							Designation:<p class="text-danger" id="p-designation"></p>
						</div>
						<div class="col-sm-2">
							Email Id:<p class="text-danger" id="p-email"></p>
						</div>						
					</div>
				</div>
				<div class="row">
					<br/>
					<div class="col-sm-8">
							<div id="edit-actions" style="display:none;">											        	
				        	<button type="button" class="btn btn-primary" id="edit-usr-btn" onclick="javascript:openEditUserModel();">
				        	<span class="fa fa-edit"></span>&nbsp;Edit Details</button>
				        	<button type="button" class="btn btn-success" id="edit-role-btn" onclick="javascript:editRoles();">
				        	<span class="fa fa-edit"></span>&nbsp;Edit Roles</button>
				        	<button type="button" class="btn btn-primary" style="display:none" id="edit-sec-btn"  onclick="javascript:editSection();">
				        	<span class="fa fa-edit"></span>&nbsp;Edit Section</button>
				        	<button type="button" class="btn btn-warning" id="reset-usr-btn" confirm-box="top" onclick="javascript:resetPassword();">
				        	<span class="fa fa-reply"></span>&nbsp;Reset Password</button>
				        	<button type="button" class="btn btn-info" style="display:none" id="unlock-usr-btn" onclick="javascript:unlockUser();">
				        	<span class="fa fa-unlock"></span>&nbsp;Unlock</button>				        	
				        	<button type="button" class="btn btn-danger" id="delete-usr-btn" confirm-box="right" onclick="javascript:deleteUser();">
				        	<span class="fa fa-close"></span>&nbsp;Delete</button>				        				        	
		        		</div>
					</div>					
				</div>				
				<div id="role-div" style="display:none;">
					<br/>
					<p class="text-danger"><strong>Please Assign/Edit roles to user.</strong></p>
					<form  id="user-role-form" name="user-role-form">
					<input type="hidden" name="userid" id="userid"/>
					<div class="row">
							<div class="toggle" id="privileges-list">						   
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Available Roles:</label>
							   		<select id="avpl"  ondblclick="javascript:addRemoveRoles('avpl','aspl');" multiple="multiple" name="avpl" size="10"  class="form-control"> </select>					   							   		 
							   </div>					  
							   <div class="col-xs-1" align="center">
							   		<br/><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveRoles('avpl','aspl');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-right"></span></button><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveRoles('aspl','avpl');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-left"></span></button>
							   </div>
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Assigned Roles:</label>						   		
							  		<select id="aspl" ondblclick="javascript:addRemoveRoles('aspl','avpl');" multiple="multiple" name="aspl" size="10" class="form-control"> </select>					   		
							   </div>					  							
							</div>
						</div>				
						<div class="row" id="fresh-role-save">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:saveDetails('FRS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
						<div class="row" id="edit-role-save" style="display:none;">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:saveDetails('ERS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
					</form>				
				</div>	
				<div id="sec-div" style="display:none;">
					<br/>
					<p class="text-danger"><strong>Please Assign/Edit Section to User .</strong></p>
				
					<input type="hidden" name="userid" id="userid"/>
					<div class="row">
							<div class="toggle" id="sec-list">						   
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Available Section:</label>
							   		<select id="avpl1"  ondblclick="javascript:addRemoveSection('avpl1','aspl1');" multiple="multiple" name="avpl1" size="10"  class="form-control"> </select>					   							   		 
							   </div>					  
							   <div class="col-xs-1" align="center">
							   		<br/><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveSection('avpl1','aspl1');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-right"></span></button><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveSection('aspl1','avpl1');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-left"></span></button>
							   </div>
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Assigned Section:</label>						   		
							  		<select id="aspl1" ondblclick="javascript:addRemoveSection('aspl1','avpl1');" multiple="multiple" name="aspl1" size="10" class="form-control clear"> </select>					   		
							   </div>					  							
							</div>
						</div>				
						<div class="row" id="fresh-sec-save">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:savesectionDetails('FRS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
						<div class="row" id="edit-sec-save" style="display:none;">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:savesectionDetails('ERS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
							
				</div>					
				<div class="modal fade bs-example-modal-lg" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
				  <div class="modal-dialog modal-lg" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title" id="exampleModalLabel">User Details</h4>
				      </div>
				      <div class="modal-body">				      
				        <form id="user-form">
					        <div class="container-fluid">
					        	<div class="row">
							  		<div class="col-xs-12">
							  			<div id="user-error">
							  			</div>
									</div>
						 		</div>	
					        	<div class="row">
					        		<div class="col-md-8">
					        			<label for="name" class="control-label">Name:</label>
				            			<input type="text" class="form-control clear validate[required,maxSize[50]]" id="name" name="name" />
					        		</div>					        		
					        	</div><br/>
					        	<div class="row">
					        		<div class="col-md-8">
					        			<label for="email" class="control-label">Email:</label>
					            		<input type="text" class="form-control clear validate[required,custom[email]]" id="email" name="email" />
					        		</div>
					        	</div><br/>
					        	<div class="row">
            						<div class="col-md-6">
            							 <label for="gender" class="control-label">Gender:</label>
							            <select id="gender" name="gender" class="form-control clear validate[required]">
										 	<option value="">Select Gender</option>
										 	<c:forEach items="${genderList}" var="gType">
										 		<option value='<c:out value="${gType.k}"/>'><c:out value="${gType.v}"/></option>
										 	</c:forEach>
										</select>	
            						</div>
            						<div class="col-md-6">
            							<label for="designation" class="control-label">Designation:</label>
							            <select id="designation" name="designation" class="form-control clear validate[required]">
										 	<option value="">Select Designation</option>
										 	<c:forEach items="${designationList}" var="dType">
										 		<option value='<c:out value="${dType.k}"/>'><c:out value="${dType.v}"/></option>
										 	</c:forEach>
										</select>	
            						</div>
            					</div><br/>
					        </div>    
				      </form>
				      </div>
				      <div class="modal-footer">
				      	<div id="create-user-div">
				      		<button type="button" class="btn btn-default" id="close-modal-btn" data-dismiss="modal">
				      		<span class="fa fa-close"></span>&nbsp;Close</button>
				        	<button type="button" class="btn btn-primary" id="create-usr-btn" onclick="javascript:createUser();">
				        	<span class="fa fa-user"></span>&nbsp;Create User</button>
				      	</div>   
				        <div id="edit-user-div" style="display:none;">
			        		<button type="button"  class="btn btn-default" id="close-modal-btn" data-dismiss="modal">
				      		<span class="fa fa-close"></span>&nbsp;Close</button>
				        	<button type="button" class="btn btn-primary" id="edit-usr-btn" onclick="javascript:editUser();">
				        	<span class="fa fa-edit"></span>&nbsp;Edit</button>
				        </div>
				      </div>
				    </div>
				  </div>
				</div>				
		    
		</div>
		</div>		
	</div>	       
</body>
</html>
