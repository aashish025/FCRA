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
	<script type="text/javascript" src="resources/js/masters/role.js"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
	
	<title>Role</title>
</head><body id="">
	<div class="container-fluid">
		<div class="content-section">
		<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
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
		       <div class="col-xs-8 pp-form-field" id="Role-list">
				</div>
		    </div>
			<div class="form-group">
			<div class="row" >
					<div class="col-xs-2 pp-form-field" id="table-btn" style="display:none;">
						<button type="button" class="btn btn-link" onclick="javascript:showView();">
						<span class="fa fa-table"></span> View Table</button>					
					</div>
				</div>	
				<div class="row" >
					<div class="col-xs-2 pp-form-field" id="add-btn">
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Role</button>					
					</div>
					</div>
					
					<div id="role-div" style="display:none;">
					<br/>
					<p class="text-danger"><strong>Please Assign/Edit SubMenu to Role.</strong></p>
					
					<input type="hidden" name="roleId" id="roleId"/>
					<div class="row">
							<div class="toggle" id="privileges-list">						   
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Available Menu:</label>
							   		<select id="avpl"  ondblclick="javascript:addRemoveRoles('avpl','aspl');" multiple="multiple" name="avpl" size="10"  class="form-control"> </select>					   							   		 
							   </div>					  
							   <div class="col-xs-1" align="center">
							   		<br/><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveRoles('avpl','aspl');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-right"></span></button><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveRoles('aspl','avpl');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-left"></span></button>
							   </div>
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Assigned Menu:</label>						   		
							  		<select id="aspl" ondblclick="javascript:addRemoveRoles('aspl','avpl');" multiple="multiple" name="aspl" size="10" class="form-control clear"> </select>					   		
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
					
				</div>
				<div id="user-div" style="display:none;">
					<br/>
					<p class="text-danger"><strong>Please Assign/Edit User Level to Role .</strong></p>
				
					<input type="hidden" name="roleId" id="roleId"/>
					<div class="row">
							<div class="toggle" id="user-list">						   
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Available User Level:</label>
							   		<select id="avpl1"  ondblclick="javascript:addRemoveUser('avpl1','aspl1');" multiple="multiple" name="avpl1" size="10"  class="form-control"> </select>					   							   		 
							   </div>					  
							   <div class="col-xs-1" align="center">
							   		<br/><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveUser('avpl1','aspl1');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-right"></span></button><br/><br/>
						   			<button type="button" onclick="javascript:addRemoveUser('aspl1','avpl1');" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-left"></span></button>
							   </div>
							   <div class="col-xs-3">
							   		<label for="name" class="control-label">Assigned User Level:</label>						   		
							  		<select id="aspl1" ondblclick="javascript:addRemoveUser('aspl1','avpl1');" multiple="multiple" name="aspl1" size="10" class="form-control clear"> </select>					   		
							   </div>					  							
							</div>
						</div>				
						<div class="row" id="fresh-user-save">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:saveuserDetails('FRS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
						<div class="row" id="edit-user-save" style="display:none;">
							<br/>
							<div class="col-xs-2">
								<button type="button" onclick="javascript:saveuserDetails('ERS');" class="btn btn-primary"><span class="glyphicon glyphicon-save"></span>&nbsp;Save Details</button>
							</div>
						</div>	
							
				</div>		
		    		
		    
		    
		
		    		<div id="details-div" style="display:none">
		    		 <form name="role-type-form" action="" method="get" id="role-type-form">
		    		
		    			<div class="row" >						
							<div class="col-xs-4 pp-form-field" id="">
									<label for="roleTitle"> Role Name: </label>
									<input type="text" name="roleName" id="roleName" class="form-control validate[required, custom[onlyLetterSp]] clear"/>
							</div>
						</div>
						</form>
						
		    		</div>
		    		<div id="add-details-btn" style="display:none;">
		    		 <div class="row">
							<div class="col-xs-2 pp-form-field">
								<button type="button" class="btn btn-primary " onclick="javascript:Createrole();">
								<span class="fa fa-plus clear"></span> Submit</button>					
							</div>
						</div>
		    		</div>				
					<div id="edit-actions"  style="display:none;">
						<div class="row"> 
							<div class="col-xs-8 pp-form-field">
									<button type="button" class="btn btn-primary" onclick="javascript:editrole();">
									<span class="fa fa-edit clear"></span> Edit Role Name</button>
									<button type="button" class="btn btn-primary" onclick="javascript:editassignrole();">
									<span class="fa fa-edit clear"></span> Assign SubMenu</button>
								     <button type="button" class="btn btn-primary" onclick="javascript:editassignlevel();">
									<span class="fa fa-edit clear"></span>Assign User Level </button>
								   <button type="button" class="btn btn-danger" onclick="javascript:deleterole();">
									<span class="fa fa-close clear"></span> Delete</button>	
													
							</div>	
						</div>											
					</div>	
				
					</div>
					
					</div>										
			 </div>	
		  
	

      

</body>
</html>