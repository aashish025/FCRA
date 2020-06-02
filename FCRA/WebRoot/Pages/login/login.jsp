<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ include file="../common/include.jsp" %>
<html lang="en">
	<head>
		<title>FCRA Portal | Login Panel</title>
		<script type="text/javascript">
			if(window.name=="body-frame"){
				parent.window.location.href=parent.window.location.href;
			}
		</script>
		<!-- JS Start -->
		<script src="resources/js/jquery-1.7.1.min.js" type="text/javascript"></script>
		<script src="resources/js/md5.js" type="text/javascript"></script>	
		<script src="resources/js/auth/login.js" type="text/javascript"></script>
		<script src="resources/js/bootstrap/all/bootstrap.js" type="text/javascript"></script>
		<script src="resources/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
    	<script src="resources/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
    	<script src="resources/js/forAll.js" type="text/javascript"></script>
		<!-- JS End -->
		<!-- CSS Start-->
		<link rel="stylesheet" href="resources/css/auth/login.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="resources/css/validationEngine.jquery.css" type="text/css"/>
		<!-- CSS End -->
		<script>
			if(window.name=="body-frame"){
				parent.window.location.href=parent.window.location.href;
			}
		 	$(document).ready(function(){
		 		//alert('hi');
		 		var x=[
   				       <c:forEach items="${notifyList}" var="ntn" varStatus="status">
   				       	{'d':'${ntn.d}',
   				       		'h':'${ntn.h}',
   				       		'l':'${ntn.l}',
   				       		'ms':'${ntn.ms}',
   				       		's':'${ntn.s}',
   				       		't':'${ntn.t}'
   				       		}
   				     		<c:if test="${!status.last}">    
			   			      ,    
			   			    </c:if>
   				       	</c:forEach>
   				       ];
		 		//alert(JSON.stringify(x));
   				if(x.length > 0) {
	   				notifyList(x,'');
   				}
			});	 
	</script>
	</head>
	<body>
		<div style="max-width: 0px; max-height: 0px; visibility: hidden;" id="illogical-area">
			<applet code="localfile.class">
				<param name="cache_option" value="plugin"/>
				<param name="cache_archive" value="<%=request.getContextPath()%>/resources/jar/ipmac.jar" />
				<param name="cache_version" value="1.0.0.0"/>
			</applet>
		</div>
		<div id="container">
			<div id="header">
				<div id="header-bg"  class="head">
				</div>
			</div>
			<div id="content">
				<img src="resources/img/auth/home/ashoka-black.png" alt="Ministry of External Affairs" style="position:absolute; left:10px; top:73px; z-index:1; height:27 px !important;" height="40px"/>
				<img src="resources/img/auth/login/mha-logo.png" alt="Ministry of External Affairs" style="position:absolute; left:40px; top:80px; z-index:1; height:27 px !important;" height="35px"/>
				<img src="resources/img/auth/login/nic-logo.png" alt="NIC Logo" style="position:absolute; right:10px; top:79px; width:125px; height:27 px; z-index:1;" />				
				<div id="login-div" class="container">
					<!--[if IE]>
						<script type="text/javascript">
							ieFlag="1";
						</script>
						<div class="alert alert-danger">
							To access this application, kindly use any other browser than "Internet Explorer". We recommend Mozilla Firefox. <a class="alert-link" href="resources/exe/mozilla-firefox.exe">Download it's latest version here.</a>
						</div>
					<![endif]-->
					<div id="java-error-div" class="col-lg-6">
						<div id="download-java" style="display:none;" class="alert alert-danger">
							<a class="alert-link" href="resources/exe/jre.exe">Download &amp; install our version of JRE/Java here.</a>
						</div>
						<div id="java-error" style="display:none;" class="panel panel-primary">
							<div class="panel-heading">Please do the following checks: </div>
							<div class="panel-body">
								<div id="java-error-1">
									<span class="text-danger"><i class="glyphicon glyphicon-asterisk"></i>&nbsp;<b>Have you disabled Java (Java Runtime Environment)? </b></span><br>
									<span  class="text-danger">
										If you have disabled Java, you must re-enable it from <b>Tools &raquo; Add-ons</b> to use this page. <b><a href="resources/exe/jre.exe">If you don't have Java installed, download &amp; install it here</a></b>.
									</span><br><br>
								</div>
								<span class="text-warning"><i class="glyphicon glyphicon-asterisk"></i>&nbsp;<b>Have you accepted the Java Security Certificate?</b></span><br>
								<span class="text-warning">
									When security prompt for java is shown, kindly <b>'Accept' &raquo; 'Always Trust' &raquo; 'Run'</b> to continue.
								</span><br><br>
								<span class="text-info"><i class="glyphicon glyphicon-asterisk"></i>&nbsp;<b>Still facing issues, contact your system administrator or support centre.</b></span>
							</div>
						</div>
						<div class="row">
						    <div class="col-xs-12">
						    	<div id="bar-notify"></div>
						    </div>
					    </div>	
					    <div class="row">
						    <div class="col-xs-12">
						    	<div id="sticky-notify"></div>
						    </div>
					    </div>							
					</div>
					<form class="login-form" id="login-form" action="login" onsubmit="return login()" method="post">
						 <div id="log-in-div">
						 	<input class="form-control validate[required]" type="text" name="userId" id="userId" style="text-transform: uppercase;" placeholder="User Id" disabled/>
							<input class="form-control validate[required]" type="password" name="password" id="password" placeholder="Password" disabled/>
							<input type="hidden" id="loginipaddress"  name="loginipaddress" value="10.2.16.56"/>
							<input type="hidden" id="loginmacaddress" name="loginmacaddress" value="00:0a:95:9d:68:16"/>	
							<input type="hidden" name="requestToken" value="${requestToken}" id="requestToken"/>	
							<input type="hidden" id="otpRefNumber"  name="otpRefNumber" />					
							<p>
								<span style="font-size:15px;" class="glyphicon glyphicon-copyright-mark"></span>
								<font id="clientCaptchaQues" class="text-danger">...</font>
								<a class="btn btn-xs pull-right" id="pull-captcha-btn" style="display:none;" onclick="javascript:pullCaptcha();" tabindex="-1" href="#" title="Reload Question"><span class="glyphicon glyphicon-refresh"></span></a>
								<input name="clientCaptchaId" id="clientCaptchaId" type="hidden" value="" />
								<input class="form-control small-in validate[required]" name="clientCaptchaAnswer" style="text-transform: uppercase;" id="clientCaptchaAnswer" placeholder="Answer?" disabled/>
							</p>
							<button class="btn btn-lg btn-primary btn-block" style="display:none;" data-loading-text="Signing you in..." id="login-btn" type="submit">Sign in</button>
							<a class="btn btn-primary btn-link" style="display:none;" onclick="javascript:initResetPassword();" id="forget-pwd-btn">Forgot Password?</a>
						 </div>
						 <div id="reset-pwd-div" style="display:none;">						  						
								<input class="form-control validate[required]" type="text" name="otpUser" id="otpUser" style="text-transform: uppercase;" placeholder="Enter User Id"/>
								<label class="control-label" id="otpRefLabel"></label>							
								<input class="form-control validate[required]" type="text" name="otpValue" id="otpValue" style="text-transform: uppercase;display:none;" placeholder="enter OTP"/>
								<br><a class="btn btn-lg btn-primary btn-block" id="init-otp-btn" onclick="javascript:initOTP();" id="otp-btn">Generate OTP</a>
									<a class="btn btn-lg btn-success btn-block" id="validate-otp-btn" style="display:none;" onclick="javascript:validateOTP();">Validate OTP</a>
								<br><a class="btn btn-success btn-link" id="regen-otp-btn" style="display:none;" onclick="javascript:reGenerateOTP();">Regenerate OTP?</a>
								<button type="button" id="password-modal-btn" style="display:none;" data-toggle="modal" data-target="#passwordModal"></button>
						</div>	
						<span class="text-primary" id="auth-wait">
							<small>
								<b>Waiting for authentication</b>
							</small>
							<img alt="..." width="242px" height="4px" src="data:image/gif;base64,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"/>
						</span>
					</form>
				</div>
			</div>
			<div class="modal fade" id="passwordModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="exampleModalLabel">Reset Password</h4>
			      </div>
			      <div class="modal-body">				      
			       <form id="reset-password-form" method="post">			       		
				        <div class="container-fluid">	
				        	<div class="row">
						  		<div class="col-xs-12">
						  			<div id="passwordModal-error">
						  			</div>
								</div>
					 		</div>							        	
				        	<div class="row">
           						<div class="col-md-8 pp-form-field">
           							<label for="newPassword" class="control-label">Enter new password:</label>
						            <input class="form-control validate[required,custom[passwordC]]" type="password" name="newPassword" id="newPassword" autocomplete="off"/>
           						</div>		            						
           					</div>					
							<div class="row">
           						<div class="col-sm-8 pp-form-field">
           							<label for="confirmNewPassword" class="control-label">Confirm password:</label>
						           <input class="form-control validate[required,custom[passwordC]]" type="password" name="confirmNewPassword" id="confirmNewPassword" autocomplete="off"/>	
           						</div>		            						
           					</div>
				        </div>    
			      </form>
			      </div>
			      <div class="modal-footer">
			      	<div id="create-user-div">
			      		<button type="button" class="btn btn-default" id="resetModal-close-btn" data-dismiss="modal" onclick="javascript:clearModal();">
			      		<span class="fa fa-close"></span>&nbsp;Close</button>
			        	<button type="button" class="btn btn-success" id="officeModal-submit-btn" onclick="javascript:submitResetPassword();">
			        	<span class="glyphicon glyphicon-ok"></span>&nbsp;Submit</button>
			      	</div>					        
			      </div>
			    </div>
			  </div>
			</div>
			<div id="footer">
				Designed &amp; Maintained by <a href="http://www.nic.in"  target="_new">National Informatics Centre, Govt. of India</a> &copy; 2015
			</div>
		</div>
	</body>
</html>