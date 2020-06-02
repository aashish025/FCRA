var ieFlag="0";
var ipAddress='';
var macAddress='';
var certiSrno='';
var certiName='';
var salt='';
$(document).ready(function(){	
	if(ieFlag=="1"){
		$('#auth-wait').hide();
	}
	else{
		//setipmac('10.2.16.60','00:0a:95:9d:68:16');
		window.setTimeout('checkIpMac()',500);
	}
});
function pullCaptcha(){	
	$('#clientCaptchaQues').html('...');
	var params = '';
	$.ajax({
		url: 'pull-captcha-login',
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			$('#clientCaptchaQues').html(data[0]);					
			$('#clientCaptchaId').val(data[1]);
			salt=data[2];
			$('#clientCaptchaQues').show();
		},
		error: function(textStatus,errorThrown){
			alert('error');			
		}
	});	
}
function encryptPwd(){
	var password = $('#password').val();
	var ran = salt;
	var hash = hex_md5(password); 
	var saltedRan = hex_md5(ran);
	var finalHash = hex_md5(saltedRan + hash);
	$('#password').val(finalHash);
}
function login(){
	if($('#login-form').validationEngine('validate')==true){
		$('#login-btn').button('loading');
		$("#loginipaddress").val(ipAddress);
		$("#loginmacaddress").val(macAddress);
		encryptPwd();
		return true;
	}
	else{
		return false;
	}
}
// For developement Purpose Only
/*function checkIpMac(){
	if(navigator.javaEnabled()==false){
		$('#java-error').show();
		$('#java-error-1').show();
		$('#download-java').show();
		window.setTimeout("checkIpMac()",500);
	}
	else if(ipAddress=='' && macAddress==''){
		$('#java-error').show();
		$('#download-java').show();
		$('#java-error-1').hide();
		window.setTimeout("checkIpMac()",500);
	}
	else{
		enableLogin();
		$('#java-error').hide();
		$('#download-java').hide();
		$('#userId').focus();
		pullCaptcha();
		$('#login-form').validationEngine({promptPosition : "bottomLeft"});
	}
}*/
function checkIpMac(){   
	enableLogin();
	pullCaptcha();
}
function disableLogin(){
	$("#userId").attr('disabled',true);
	$("#password").attr('disabled',true);
	$("#clientCaptchaAnswer").attr('disabled',true);
	$("#login-btn").hide();
	$("#forget-pwd-btn").hide();
	$("#pull-captcha-btn").hide();
	$('#auth-wait').show();
}
function enableLogin(){
	$("#userId").removeAttr('disabled');
	$("#password").removeAttr('disabled');
	$("#clientCaptchaAnswer").removeAttr('disabled');
	$('#auth-wait').hide();
	$("#login-btn").show();
	$("#forget-pwd-btn").show();
	$("#pull-captcha-btn").show();
}
//Reset Password //
function initResetPassword(){
	$('#auth-wait').hide();
	$('#log-in-div').hide();
	$('#reset-pwd-div').show();
	$('#otpValue').val('');
	$('#otpUser').val('');
	$('#otpRefLabel').html('');
	$('#newPassword').val('');
	$('#confirmNewPassword').val('');
	$('#otpUser').show();
	$('#init-otp-btn').show();
	$('#validate-otp-btn').hide();
	$('#regen-otp-btn').hide();
	$('#otpRefNumber').hide();
	$('#otpValue').hide();
}
function initOTP(){	
	$('#bar-notify').html('');	
	$('#login-form').validationEngine({promptPosition : "bottomLeft"});
	var params = 'userId='+$('#otpUser').val()+'&requestToken='+$('#requestToken').val();
	if($('#login-form').validationEngine('validate')==true){
		$.ajax({
			url: 'generate-otp-reset-password',
			type:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[0]=="success"){	
					notifyList(JSON.parse(data[1]),'');
					$('#otpRefLabel').html('Enter OTP for refernece number <span class="text-danger"><b>'+data[2]+'</b></span>');	
					$('#otpRefNumber').val(data[2]);
					$('#otpUser').hide();
					$('#init-otp-btn').hide();
					$('#validate-otp-btn').show();
					$('#regen-otp-btn').show();
					$('#otpRefNumber').show();
					$('#otpValue').show();
					$('#requestToken').val(data[3]);
				}					
				else{					
					notifyList(JSON.parse(data[1]),'');
					$('#requestToken').val(data[3]);
				}					
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	
	}	
}
function reGenerateOTP(){
	$('#bar-notify').html('');
	$('#otpValue').val('');
	$('#otpValue').removeClass('form-control validate[required]').addClass('form-control');
	var params = 'userId='+$('#otpUser').val()+'&requestToken='+$('#requestToken').val();
	if($('#login-form').validationEngine('validate')==true){
		$.ajax({
			url: 'regenerate-otp-reset-password',
			type:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				if(data[0]=="success"){	
					notifyList(JSON.parse(data[1]),'');
					$('#otpRefLabel').html('Enter OTP for refernece number <span class="text-danger"><b>'+data[2]+'</b></span>');	
					$('#otpRefNumber').val(data[2]);	
					$('#requestToken').val(data[3]);
				}					
				else{				
					notifyList(JSON.parse(data[1]),'');
					$('#requestToken').val(data[3]);
				}					
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	
	}
}
function validateOTP(){
	$('#bar-notify').html('');
	$('#login-form').validationEngine({promptPosition : "topLeft"});
	var params = 'otpValue='+$('#otpValue').val()+'&otpRefNumber='+$('#otpRefNumber').val()+'&requestToken='+$('#requestToken').val();
	if($('#login-form').validationEngine('validate')==true){
		$.ajax({
			url: 'validate-otp-reset-password',
			type:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[0]=="success"){				
					$('#password-modal-btn').click();
					$('#requestToken').val(data[2]);
				}					
				else{
					notifyList(JSON.parse(data[1]),'');
					$('#requestToken').val(data[2]);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	
	}	
}
function clearModal(){
	$('#log-in-div').show();
	$('#reset-pwd-div').hide();
}
function submitResetPassword(){	
	$('#reset-password-form').validationEngine({promptPosition : "bottomLeft"});
	var hashNewPwd = hex_md5($('#newPassword').val());
	var hashNewConfirmPwd = hex_md5($('#confirmNewPassword').val());
	var params = 'newPassword='+hashNewPwd+'&confirmNewPassword='+hashNewConfirmPwd+'&otpUser='+$('#otpUser').val()+'&requestToken='+$('#requestToken').val()
	+'&otpValue='+$('#otpValue').val()+'&otpRefNumber='+$('#otpRefNumber').val();
	if($('#reset-password-form').validationEngine('validate')==true){
		$.ajax({
			url: 'submit-reset-password',
			type:'POST',
			data:params,
			dataType:'json',
			success: function(data){				
				if(data[0]=="success"){					
					$('#resetModal-close-btn').click();
					notifyList(JSON.parse(data[1]),'');
					$('#log-in-div').show();
					$('#reset-pwd-div').hide();
					$('#requestToken').val(data[2]);
				}					
				else{
					notifyList(JSON.parse(data[1]),'');
					$('#requestToken').val(data[2]);
				}				
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	
	}	
}
//Reset Password //
function setipmac(ipaddress,macaddress){
	ipAddress=ipaddress;
	macAddress=macaddress;
}