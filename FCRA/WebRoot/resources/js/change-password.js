function encryptPassword() {		
	if($('#changePasswordSubmit').validationEngine('validate')) {			
		var oldPassword = document.getElementById('current-password').value;
		var newPassword1 = document.getElementById('new-password').value;
		var newPassword2 = document.getElementById('newPasswordConfirmed').value;
		var hashOldPwd = hex_md5(oldPassword);
		var hashNewPwd1 = hex_md5(newPassword1);
		var hashNewPwd2 = hex_md5(newPassword2);		
		document.getElementById('current-password-with-hash').value = hashOldPwd;
		document.getElementById('new-password-with-hash').value = hashNewPwd1;
		document.getElementById('newPasswordConfirmed-with-hash').value = hashNewPwd2;		
		$('#changePasswordSubmit').submit();
	}	
}