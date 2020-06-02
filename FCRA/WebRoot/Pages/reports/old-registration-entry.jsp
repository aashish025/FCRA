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
	<script type="text/javascript" src="resources/js/reports/old-registration-entry.js"></script>
	<link rel="stylesheet" href="resources/css/bootgrid/bootgrid.css" />
	<script type="text/javascript" src="resources/js/bootgrid/bootgrid.js"></script>
    <link rel="stylesheet" href="resources/css/calendar/jquery.datetimepicker.css" type="text/css" />	
	<script type="text/javascript" src="resources/js/calendar/jquery.datetimepicker.full.js"></script>
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />	
	<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>	
	<title>Old Registration Entry</title>
	
	<script type="text/javascript">

	$(document).ready(function(){
		var list=JSON.parse('${assoNatureList}');
	    var id="assoId";
		var name="assoname";			
			var divObj = $('#assoNature-list');
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
										$('<input/>', {'type':'checkbox', 'id':id+'-'+item.k,'name':name,'class':'"validate[required]"','value':item.k}))));	
			});
			divObj.append(table);
	});	

	$(document).on('change', '[id=assoId-1]', function() {		
		  if ($(this).is(':checked')) {
			  getReligionList();
		 }
		  else{
			  $('#assoReligionDiv').hide();
		  }
			  
		});
	</script>
	</head>
	

<body id="">
	<div class="container-fluid">
	
	  <div id="old-registration-detailsDiv">
	  
	  
	                   <div class=""></div>
			        <div class="row pp-form-field">
			    	<div class="col-xs-6">

					<button type="button" class="btn btn-primary "
						onclick="javascript:showOldRegistrationEntryForm();">
						<span class="fa fa-lg fa-plus clear"></span> Add Old Registration
						Details
					</button>

					<button type="button" class="btn btn-default active"
						onclick="clearModal();" data-toggle="modal"
						data-target="#printCertificateModal">
						<span class="fa fa-lg fa-print "></span>&nbsp;Reprint Old
						Registration Certificate
					</button>

				</div>


			</div>
		</div>

		<div class="modal fade bs-example-modal-lg" id="printCertificateModal"
			tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="exampleModalLabel">Reprint Old
							Registration Certificate</h4>
					</div>

					<div class="modal-body">
						<form id="oldReg-Certificateprint-form" method="post">					
							<div class="container-fluid">							
								<div class="row">
									<div class="col-xs-12">
										<div id="printCertificateModal-error"></div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-4 pp-form-field" id="generatedRcn">
										<label for="generatedRcn">RCN:</label> <input
											name="rcn" id="rcn"
											class="form-control  validate[required] clear" />
									</div>
								</div>
								<div class=""></div>
								<br></br>
								<div class="row">
									<div class="modal-footer">
										<div id="modalCloseDiv">
											<button type="button" id="modalReprintButton"
												class="btn btn-success" id="noteModal-submit-btn"
												onclick="javascript:printOldRegistrationCerificate();">
												<span class="fa fa-fa fa-print"></span>&nbsp;Print
											</button>

											<button type="button" class="btn btn-default"
												id="edit-batch-close-btn" data-dismiss="modal">
												<span class="fa fa-close"></span>&nbsp;Close
											</button>
										</div>
									</div>
								</div>

							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

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
		
		  
					    		
		   <div id="form-div">

				<form id="oldregistrationEntry-form" action=""
					enctype="multipart/form-data" method="post">
					
			   <div  class="row" id ="backDisplay" style="display:block">
				 <div class="col-md-3 pp-form-field">
					<button type="button" class=" btn-link pull-left" id="backButton" onclick="javascript:homeScreen();"><i class="fa fa-lg fa-1x fa-arrow-circle-left  "></i>&nbsp;Go Back</button>
				</div>
			     </div>
					
					<div class="bs-callout bs-callout-info">
						<span>Old Registration Details</span>
					</div>
					<div class="row">
						<div class="col-xs-3 pp-form-field" id="assoNameDiv">
							<label for="assoName">Association Name </label> 	
							<input type="hidden" name="rcn" id="rcn"/>
							<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>						
							<input type="text" name="assoName" id="assoName"
								class="form-control  validate[required,maxSize[100] custom[remarkAddress]] clear" />
						</div>
						
						<div class="col-xs-3 pp-form-field" id="assoAddDiv">
							<label for="assoAdd">Address </label>
							<textarea
								class="form-control  validate[required, maxSize[150] custom[remarkAddress]] clear"
								rows="3" id="assoAdd" name="assoAdd"></textarea>
						</div>

						<div class="col-xs-3 pp-form-field" id="assoCityDiv">
							<label for="assoCity">City </label> <input type="text"
								name="assoCity" id="assoCity"
								class="form-control  validate[required, maxSize[50] custom[onlyLetterNumberSp]] clear" />
						</div>
					</div>

					<div class="row">
						 <div class="col-xs-3 pp-form-field">
							<label for="state">Select State </label> <select id="state"
								name="state" class="form-control  validate[required] clear">
								<option value="">Select State</option>
								<c:forEach items="${stateList}" var="nType">
									<option value='<c:out value="${nType.k}"/>'><c:out
											value="${nType.v}" /></option>
								</c:forEach>
							</select>
						</div>

						<div class="col-xs-3 pp-form-field">
							<label for="district">Select District </label> <select
								id="district" name="district"
								class="form-control clear validate[required] title-l">
								<option value="">Select District</option>
							</select>
						</div>
						
						<div class="col-xs-3 pp-form-field" id="zipcodeDiv">
							<label for="assoZipCode">Zip Code </label> <input type="text"
								name="assoZipCode" id="assoZipCode"
								class="form-control  validate[required, maxSize[6],minSize[6] custom[integer]] clear" />
					    </div>
				</div>

				<div class="row">
					 <div class="col-xs-6 pp-form-field"> 
					  	<label for="assoNature">Association Nature </label>
							<div id="assoNature-list" >
							</div>						 
					 </div>

						<div class="col-xs-3 pp-form-field" id="assoReligionDiv"
							style="display: none">
							<label for="assoReligion">Select Religion </label> <select
								id="assoReligion" name="assoReligion"
								class="form-control validate[required,maxSize[1]] clear">
								<option value="">Select Religion</option>
								<c:forEach items="${assoReligionList}" var="gType">
									<option value='<c:out value="${nType.k}"/>'><c:out
											value="${nType.v}" /></option>
								</c:forEach>
							</select>
						</div>
						
					</div>	
						<div class="row">
						<div class="col-xs-3 pp-form-field" id="assoAimsDiv">
							<label for="assoAims">Aims of Association </label>
							<textarea
								class="form-control  validate[required,maxSize[150] custom[remarkAddress]] clear"
								rows="3" id="assoAims" name="assoAims"></textarea>
						</div>
						
						<div class="col-xs-3 pp-form-field" id="assoUseridDiv">
							<label for="assoUserid">Association User ID </label> <input
								type="text" name="assoUserid" id="assoUserid"
								class="form-control  validate[required,maxSize[100] custom[remarkAddress]] clear" />
						</div>
					</div>


					<div class="row">
						<div class="col-xs-3 pp-form-field">
							<label for="fcraregDate" class="control-label">FCRA
								Registration Date </label> <input type="text"
								class="form-control validate[required,custom[date]]"
								name="fcraregDate" id="fcraregDate" placeholder="dd-mm-yyyy" ></input>
						</div>
						<div class="col-xs-3 pp-form-field">
							<label for="validfromDate" class="control-label">Registration
								Valid From </label> <input type="text"
								class="form-control validate[required,custom[date]]"
								name="validfromDate" id="validfromDate" disabled="disabled"></input>
						</div>

						<div class="col-xs-3 pp-form-field">
							<label for="validToDate" class="control-label">Registration
								Valid To </label> <input type="text" 
								class="form-control validate[required,custom[date]]"
								name="validToDate" id="validToDate" disabled="disabled"></input>
						</div>						
					</div>

					<div class="bs-callout bs-callout-info">
						<span>Recipient Bank Details</span>
					</div>

					<div class="row">
						<div class="col-xs-3 pp-form-field" id="accNumberDiv">
							<label for="accNumber">Account Number</label> <input type="text"
								name="accNumber" id="accNumber"
								class="form-control  validate[required,maxSize[30] custom[remarkAddress]] clear" />
					</div>


						<div class="col-xs-3 pp-form-field" id="bankNameDiv">
							<label for="bankName">Select Bank Name</label> <select
								id="bankName" name="bankName"
								class="form-control clear validate[required] title-l">
								<option value="">Select Bank Name</option>
								<c:forEach items="${banknameList}" var="nType">								    
									<option value='<c:out value="${nType.k}"/>'>
									<c:out value="${nType.v}" /></option>
								</c:forEach>
							</select>
						</div>


						<div class="col-xs-3 pp-form-field" id="bankAddDiv">
							<label for="bankAdd">Bank Address </label>
							<textarea
								class="form-control  validate[required, maxSize[150] custom[remarkAddress]] clear"
								rows="3"  name="bankAdd" id="bankAdd"></textarea> 						
						</div>
					</div>


					<div class="row">
						<div class="col-xs-3 pp-form-field" id="bankCityDiv">
							<label for="bankCity">City </label> <input type="text"
								name="bankCity" id="bankCity"
								class="form-control  validate[required, maxSize[50] custom[onlyLetterNumberSp]] clear" />
						</div>


						<div class="col-xs-3 pp-form-field">
							<label for="bankstate">Select State </label> <select
								id="bankstate" name="bankstate"
								class="form-control  validate[required] clear">
								<option value="">Select State</option>
								<c:forEach items="${stateList}" var="nType">
									<option value='<c:out value="${nType.k}"/>'><c:out
											value="${nType.v}" /></option>
								</c:forEach>
							</select>
						</div>

						<div class="col-xs-3 pp-form-field">
							<label for="bankdistrict">Select District </label> <select
								id="bankdistrict" name="bankdistrict"
								class="form-control clear validate[required] title-l">
								<option value="">Select District</option>
								<option value='<c:out value="${nType.k}"/>'><c:out
										value="${nType.v}" /></option>
							</select>
						</div>

					</div>

					<div class="row">
						<div class="col-xs-3 pp-form-field" id="bankzipcodeDiv">
							<label for="bankZipCode">Zip Code </label> <input type="text"
								name="bankZipCode" id="bankZipCode"
								class="form-control  validate[required,maxSize[6],minSize[6] custom[integer]] clear" />
						</div>
				
								  
		          	<div class="col-sm-6 pp-form-field">
		          	<label for="oldregRemark" class="control-label">Remark</label>
					<textarea class="form-control validate[required,maxSize[2000]] clear" 
				    rows="3" id="oldregRemark" name="oldregRemark"></textarea>	
		          	</div>	
		          		            						
			         </div>
                 

					<div id="add-details-btn">
						
						 <div class="row pp-form-field">
							<div class="col-xs-6">
								<button type="button" class="btn btn-success "
									onclick="javascript:adddOldregDetails();">
									<span class="glyphicon glyphicon-ok"></span> Submit
								</button>
							
								<button type="button" class="btn btn-default" onclick="javascript:closeAll();">
								<span class="fa fa-close clear"></span> Close</button>				
							
							</div>
						</div>						
				  </div>
				</form>
			</div>
	  </div>
  </div>		     
</body> 
<!-- <script type="text/javascript">		        
     $('#fcraregDate').datetimepicker({
        	timepicker:false,
        	format:'d-m-Y',
        	formatDate:'dd-mm-yyyy',
        	maxDate:new Date
           /*  maxDate: new Date(2015,11,12),
            yearEnd: 2015,
    		monthStart: 0,
    		monthEnd: 11,
            scrollMonth : false,
        	scrollInput : false  */
        });  			
</script>  -->
<script type="text/javascript">		        
 $('#fcraregDate').datetimepicker({
		lang:'ch',
		timepicker:false,
		format:'d-m-Y',
		formatDate:'dd-mm-yyyy',
		maxDate:'0'
	});
</script>

</html>
