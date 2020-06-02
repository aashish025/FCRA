<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript" src="resources/js/jquery-1.11.3.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css?Version=${version}" type="text/css" />	
	<script type="text/javascript" src="resources/js/all.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/auth/home/user-notify.js?Version=${version}"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/common.css?Version=${version}" />
	<script src="resources/js/iframeResizer.contentWindow.min.js?Version=${version}" type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/forAll.js?Version=${version}"></script>
	<script type="text/javascript" src="resources/js/reports/red-flag-donors.js?Version=${version}"></script>	
	<script type="text/javascript" src="resources/js/utility/uploader.js?Version=${version}"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css?Version=${version}" />
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css?Version=${version}" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js?Version=${version}"></script>
	<script src="resources/js/languages/jquery.validationEngine-en.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js?Version=${version}" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css?Version=${version}" type="text/css"/>
	<script type="text/javascript" src="resources/js/input-mask/input-mask.js?Version=${version}"></script>
	<!-- <script src="resources/js/calendar/calendar.js" type="text/javascript"></script>
    <link href="resources/css/calendar/calendar.css" media="screen" rel="stylesheet" type="text/css" /> -->
    	<link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css?Version=${version}" type="text/css" />
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js?Version=${version}"></script>
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
            <div class="">
            <!-- <div class="row">
					<label for="headingforredyellowflag" id='headingforredyellowflag'>Donor which are captured in Yellow/Red Flag </label>
				</div> -->
				<div class="bs-callout bs-callout-info" id="headingforredyellowflag">
						<span>Donor which are already listed  in Yellow/Red Flag</span>
					</div>
				<div class="row">
				<div class="col-xs-10" id="red-flag-donors-table"></div>
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
					<div class="col-xs-2 pp-form-field" id="add-btn" >
						<button type="button" class="btn btn-primary" onclick="javascript:initForm();">
						<span class="fa fa-plus"></span> Add Donor to Red/Yellow Flag</button>					
					</div>				
				</div>				
		  
		    <div id="form-div" style="display:none;">
		    	<form id="red-flag-donors" action="" enctype="multipart/form-data" method="post">
		    		<input type="hidden" name="donorId" id="donorId"/>
		    		<input type="hidden" name="currentDate" id="currentDate" value="${currentDate}" />
		    	 	<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>
		    	 	<input type="hidden" name="roleIdAddGrant" value="${roleIdAddGrant}" id="roleIdAddGrant"/>
					<input type="hidden" name="roleIdDeleteGrant" value="${roleIdDeleteGrant}" id="roleIdDeleteGrant"/>
					<input type="hidden" name="roleIdAddGrantYellow" value="${roleIdAddGrantyellow}" id="roleIdAddGrantYellow"/>
					<input type="hidden" name="roleIdDeleteGrantYellow" value="${roleIdDeleteGrantyellow}" id="roleIdDeleteGrantYellow"/>
							<div class="row">
						<div class="col-xs-4 pp-form-field" id="donorName-div">
							<label for="donorName">Donor Name</label> <input type="text" name="donorName" id="donorName" class="form-control  validate[required,maxSize[100] custom[remarkAddress]] clear" />
						</div>
					
						<div class="col-xs-4 pp-form-field" id="officeTypeDiv">
							<label for="donorCountry">Country</label>
							<select id="donorCountry" name="donorCountry" class="form-control  clear">
							 	<option value="">Select Country:</option>
							 	 <c:forEach items="${countryList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach> 
							</select>
					   </div>
					  <div class="col-xs-4 pp-form-field" id="">
								<label for="originator_office">Flagged By</label>
								<input type="text" name="originator_office" id="originator_office" class="form-control  validate[maxSize[50] custom[onlyLetterNumberSp]] clear"/>
						</div> 
					   
					</div>
					<div class="row" >
						
			            <div class="col-xs-4 pp-form-field" id="">
								<label for="originatorOrderNo">Originator Order / File No.</label>
								<input type="text" name="originatorOrderNo" id="originatorOrderNo" class="form-control  validate[maxSize[50] custom[remarkAddress]] clear"/>
						</div>
			     <!-- <div class="col-xs-4 pp-form-field">
							<label for="originatorOrderDate">Originator Order Date </label>
							<input type="text" name="originatorOrderDate" id="originatorOrderDate" class="form-control  validate[maxSize[50] custom[onlyLetterNumber]] clear"/>
						</div> -->
					<div class="col-xs-4 pp-form-field" id="">
							<label for="originatorOrderDate">Flagged On 
							</label> <input type="text" name="originatorOrderDate" id="originatorOrderDate"
								class="form-control validate[custom[date], pastEqualField[currentDate]]  clear tcal" />
							
						</div>	
						
						<div class="col-xs-4 pp-form-field" id="">
							<label for="rbiCircularIssueDate">RBI circular issue date
							</label> <input type="text" name="rbiCircularIssueDate" id="rbiCircularIssueDate"
								class="form-control validate[custom[date], pastEqualField[currentDate]]  clear tcal" />
						</div>
						
						
					</div>
					<div class="row">
						<div class="col-xs-4 pp-form-field" id="officeTypeDiv">
							<label for="categoryDesc">Reason For Flagging :</label>
							<select id="categoryDesc" name="categoryDesc" class="form-control validate[required]  clear">
							 	<option value="">Select Reason :</option>
							 	 <c:forEach items="${categoryList}" var="nType">
							 		<option value='<c:out value="${nType.k}"/>'><c:out value="${nType.v}"/></option>
							 	</c:forEach> 
							</select>
					   </div>	
					
			     		<div class="col-xs-4 pp-form-field" id="">
							<label for="remarkOriginatorOffice">Remarks :</label>
							<textarea class="form-control  validate[required,maxSize[2000] custom[remarkAddress]] clear" rows="3" id="remarkOriginatorOffice" name="remarkOriginatorOffice"></textarea>
						</div>
					</div>					
					<div class="row">
						<div class="col-xs-4 pp-form-field" id="add-details-btn">
						<c:if test="${roleIdAddGrant == 16}">
						<button type="button" id="add-red-flag-btn"   onclick="javascript:addRedFlagDonors('1');" class="btn btn-danger">
			      			<span class="glyphicon glyphicon-plus"></span>&nbsp;Add to  RED Flag List</button>
			      			<button type="button" id="add-yellow-flag-btn"   onclick="javascript:addRedFlagDonors('2');" class="btn btn-warning">
			      			<span class="glyphicon glyphicon-plus"></span>&nbsp;Add to  YELLOW Flag List</button>
			      			</c:if>
			      			<c:if test="${roleIdAddGrantyellow == 15}">
			      			<c:if test="${roleIdAddGrant != 16}">
			      			<button type="button" id="add-yellow-flag-btn"   onclick="javascript:addRedFlagDonors('2');" class="btn btn-warning">
			      			<span class="glyphicon glyphicon-plus"></span>&nbsp;Add to  YELLOW Flag List</button>
			      			</c:if>
			      			</c:if>
						</div>
					
					</div>	
							<div id="delete-actions" class="form-inline" style="display:none;">		
						<div class="row">
							<c:if test="${roleIdDeleteGrant==17}">
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-danger" onclick="javascript:deleteRemarkModelDonors('1');">
									<span class="fa fa-close"></span>Remove From RED Flag List</button>					
							</div>
							</c:if>	
						</div>											
					</div>
					
						<div id="delete-YellowFlag-donor" class="form-inline" style="display:none;">		
						<div class="row">
						<c:if test="${roleIdDeleteGrantyellow==18}">
							<div class="col-xs-3 pp-form-field">
									<button type="button" class="btn btn-warning" onclick="javascript:deleteRemarkModelDonors('2');">
									<span class="fa fa-close"></span>Remove From YELLOW Flag List</button>					
							</div>
							</c:if>	
							<c:if test="${roleIdAddGrant == 16}">
								<div class="col-xs-2 pp-form-field">
							
							<button type="button" id="add-red-flag-btn1"   onclick="javascript:RemoveYellowaddModel();" class="btn btn-danger">
			      			<span class="glyphicon glyphicon-plus"></span>&nbsp;Add to  RED Flag List</button></div>
			      			</c:if>
						</div>											
					</div>
														
			    </form>	    
		    </div> 
			
		</div>
		
		<div class="modal fade" id="delete-remark-details-modal" tabindex="-1" role="dialog" aria-labelledby="donor-modal-label" data-keyboard="false" data-backdrop="static">
			<input type="hidden" name="flagdelete" value="${flagdelete}" id="flagdelete"/>		
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="donar-modal-label">Remove Donor Details from Red/Yellow Flag List</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<form name="delete-remark-form" id="delete-remark-form">
							<input type="hidden" id="delete-donorId"  name="delete-donorId" /> 
							<div class="row">
								<div class="col-xs-6">
									<div id="modal-notification-area"></div>
								</div>
							</div>
							<div class="row">
							<div class="col-xs-4 pp-form-field" id="">
								<label for="delete-originator_office">Flagged By</label>
								<input type="text" name="delete-originator_office" id="delete-originator_office" class="form-control  validate[maxSize[50] custom[onlyLetterNumberSp]] clear"/>
						</div>
			            <div class="col-xs-3 pp-form-field" id="">
								<label for="delete-originatorOrderNo">Originator Order / File No.</label>
								<input type="text" name="delete-originatorOrderNo" id="delete-originatorOrderNo" class="form-control  validate[maxSize[50] custom[remarkAddress]] clear"/>
						</div>
			     <!-- <div class="col-xs-4 pp-form-field">
							<label for="originatorOrderDate">Originator Order Date </label>
							<input type="text" name="originatorOrderDate" id="originatorOrderDate" class="form-control  validate[maxSize[50] custom[onlyLetterNumber]] clear"/>
						</div> -->
					   <div class="col-xs-3 pp-form-field" id="">
							<label for="delete-originatorOrderDate">Flagged On
							</label> <input type="text" name="delete-originatorOrderDate" id="delete-originatorOrderDate"
								class="form-control validate[custom[date], pastEqualField[currentDate]] clear tcal" />
						</div>	
							
							
							 </div>
						     <div class="row">
			     		<div class="col-xs-8 pp-form-field" id="">
							<label for="delete-remarkOriginatorOffice">Remarks :</label>
							<textarea class="form-control  validate[required,maxSize[2000] custom[remarkAddress]] clear" rows="3" id="delete-remarkOriginatorOffice" name="delete-remarkOriginatorOffice"></textarea>
						</div> 
						     </div>
						          
					</form>
					</div>
					<div class="modal-footer">
				      	
				      	<div class="row">
										<button type="button" class="btn btn-danger" onclick="javascript:deleteRedFlagDonors();">
											<span class="fa fa-close"></span>&nbsp;Remove
										</button>
										<button type="button" class="btn"
											data-dismiss="modal" >
											<span class="fa fa-close"></span>&nbsp;Close
										</button>
									</div>					        
				      </div>
				</div>
				
			</div>
		</div>
		</div>



			<div class="modal fade" id="add-remark-details-modal" tabindex="-1"		role="dialog" aria-labelledby="donor-modal-label"				data-keyboard="false" data-backdrop="static">
				<input type="hidden" name="flagaddred" value="${flagaddred}"	id="flagaddred" />
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="donar-modal-label">Add to RedFlag List</h4>
						</div>
						<div class="modal-body">
							<div class="container-fluid">
								<form name="add-remark-form" id="add-remark-form">
									<input type="hidden" id="add-donorId" name="add-donorId" />
									<div class="row">
										<div class="col-xs-6">
											<div id="modal-notification-area"></div>
										</div>
									</div>
									<div class="row">
										<div class="col-xs-4 pp-form-field" id="">
											<label for="originator_office">Flagged By</label>
											 <input	type="text" name="originator_officeadd"	id="originator_officeadd"
												class="form-control  validate[maxSize[50] custom[onlyLetterNumberSp]] clear" />
										</div>

										<div class="col-xs-4 pp-form-field" id="">
											<label for="originatorOrderNo">Originator Order / File No.</label>
											<input type="text" name="originatorOrderNoadd" id="originatorOrderNoadd"
												class="form-control  validate[maxSize[50] custom[remarkAddress]] clear" />
										</div>

										<div class="col-xs-4 pp-form-field" id="">
											<label for="originatorOrderDate">Flagged On </label> <input type="text" name="originatorOrderDateadd"	id="originatorOrderDateadd"
												class="form-control validate[custom[date], pastEqualField[currentDate]]  clear tcal" />

										</div>

									</div>
									<div class="row">
											<div class="col-xs-4 pp-form-field" id="">
												<label for="rbiCircularIssueDate">RBI circular issue date </label>
												 <input type="text" name="rbiCircularIssueDateadd"	id="rbiCircularIssueDateadd" class="form-control validate[custom[date], pastEqualField[currentDate]]  clear tcal" />
											</div>
											<div class="col-xs-4 pp-form-field" id="officeTypeDiv">
											<label for="categoryDesc">Reason For Flagging :</label>
											 <select id="categoryDescadd" name="categoryDescadd" class="form-control validate[required]  clear">
												<option value="">Select Reason :</option>
												<c:forEach items="${categoryListadd}" var="nType">
													<option value='<c:out value="${nType.k}"/>'><c:out
															value="${nType.v}" /></option>
												</c:forEach>
											</select>
										</div>
										

									</div>
									<div class="row">
										<div class="col-xs-4 pp-form-field" id="">
											<label for="remarkOriginatorOffice">Remarks :</label>
											<textarea
												class="form-control  validate[required,maxSize[2000] custom[remarkAddress]] clear"
												rows="3" id="remarkOriginatorOfficeadd"
												name="remarkOriginatorOfficeadd"></textarea>
										</div>
										

										
									</div>


								</form>
							</div>
							<div class="modal-footer">

								<div class="row">
									<button type="button" class="btn btn-danger"
										onclick="javascript:RemoveYellowaddRedFlagDonors('1');">
										<span class="fa fa-close"></span>&nbsp;Add to Red Flag List
									</button>
									<button type="button" class="btn" data-dismiss="modal">
										<span class="fa fa-close"></span>&nbsp;Close
									</button>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	    </div>  
</body>
 <script  type="text/javascript">		
		$('#delete-originatorOrderDate').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
		$('#originatorOrderDate').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
		$('#rbiCircularIssueDate').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
		$('#originatorOrderDateadd').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});

		$('#rbiCircularIssueDateadd').datetimepicker({
			lang:'ch',
			timepicker:false,
			format:'d-m-Y',
			formatDate:'dd-mm-yyyy'	
		});
		
		
	</script>
</html>
