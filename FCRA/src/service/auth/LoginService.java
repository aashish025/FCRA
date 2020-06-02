package service.auth;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import models.master.Country;
import models.master.Office;
import models.master.OfficeType;
import models.master.TimeZone;
import models.master.ParentMenu;
import models.master.Role;
import models.master.SubMenu;
import utilities.Commons;
import utilities.RandomString;
import utilities.UserSession;
import utilities.ValidationException;
import utilities.communication.AutoNotifier;
import utilities.notifications.*;

public class LoginService extends Commons{
	private String captchaQuestion;
	private String captchaAnswer;
	private String captchaId;
	private String clientCaptchaId;
	private String clientCaptchaAnswer;	
	private String userLevel;
	private String userId;
	private String password;
	private String salt;
	private String loginipaddress;
	private String loginmacaddress;
	private String proceed;
	private String otpRefNumber;
	private String otpValue;
	private String newPassword;
	private String confirmNewPassword;
	private List<Integer> jumbledNumbers = new ArrayList<Integer>();
	
	public String login(){
		begin();
		try{
			if(myUser==null){
				resultType="loginFail";
				/* Temp - for development stage only */
					loginipaddress="127.0.0.1";
					loginmacaddress="00-00-00-00-00-00";					
					ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
					HttpSession session = attr.getRequest().getSession(true);
			        salt = (String)session.getAttribute("salt");
					//salt="abcd";					
				/* Temp - for development stage only */
				if(nob(userId) || nob(password) || nob(salt)){
					salt=new RandomString().getRandomString();
				}
				else{
					myUserId=userId;
					boolean captchaResult=checkCaptcha();   
					if(!captchaResult){
						notifyList.add(new Notification("Captcha!", "You answered the question incorrectly.", Status.ERROR, Type.BAR));
					}
					else{
						if(ESAPI.validator().isValidInput("User ID", myUserId, "UserId", 10, false) == false){
							notifyList.add(new Notification("User Id!", "No symbols(except '_') and spaces allowed. (10 Max.)", Status.ERROR, Type.BAR));
							finish();
							return "loginFail";
						}
						if(ESAPI.validator().isValidInput("Password", password, "Password", 40, false) == false){
							notifyList.add(new Notification("Password!", "No symbols(except . _ % ' -) and spaces allowed. (40 Max.)", Status.ERROR, Type.BAR));
							finish();
							return "loginFail";
						}
						
						myUser = new UserSession();
						query=new StringBuffer("SELECT "+dbUserId+"FN_CHECK_CREDENTIAL_SALT(?,?,?) FROM DUAL");
						String[] qv={myUserId,password,salt};
						String ot=pullResultSet(qv, "user", myUserId, "", true, Type.BAR);
						if(ot.equals("all")){
							ot=resultSet.getString(1);							
						}
						if(ot.equals("error")) {
							throw new Exception("Some unexpected error occured!");
						}
						if(!nob(ot)){
							String loginFlag=new String("N");
							StringTokenizer st = new StringTokenizer(ot, "-");
							String dbResult = st.nextToken();
							userLevel = st.nextToken();
							String dbMessage = st.nextToken();
							if(dbResult.equalsIgnoreCase("3")){
								dbResult="0";
							}
							if(dbResult.equals("0")){
								httpSession.invalidate();
								httpSession = attr.getRequest().getSession(true);								
								sessionId=httpSession.getId();
								if(pushUserSession(false)){
									query = new StringBuffer("UPDATE TM_USER SET SESSION_ID=? WHERE USER_ID=?");
									String[] qv_3={sessionId,myUserId};
									push(1, qv_3, "", "", "", false, Type.NONE);
									loginFlag=new String("Y");
									resultType="success";
								}
							}
							else if(dbResult.equalsIgnoreCase("1")){
								notifyList.add(new Notification("Error!", dbMessage, Status.ERROR, Type.BAR));
							}
							else if(dbResult.equalsIgnoreCase("2")){
								httpSession.invalidate();
								httpSession = attr.getRequest().getSession(true);								
								sessionId=httpSession.getId();
								if(pushUserSession(true)){									
									notifyList.add(new Notification("Password has expired!", "Please change the password to continue.", Status.INFORMATION,Type.BAR));
									query = new StringBuffer("UPDATE TM_USER SET SESSION_ID=? WHERE USER_ID=?");
									String[] qv_3={sessionId,myUserId};
									push(1, qv_3, "", "", "", false, Type.NONE);
									loginFlag=new String("Y");
									resultType="changePassword";
								}
							}
							
							String validUser=new String("N");
							query=new StringBuffer("SELECT USER_ID FROM TM_USER WHERE USER_ID=?");
							String[] qv_4={myUserId};
							ot=pullResultSet(qv_4, "", "", "", false, Type.BAR);
							if(ot.equals("all")){
								validUser=new String("Y");
							}					    	
					    	query=new StringBuffer("insert into t_accesslog_details(user_id,client_ip,mac_address,login_time,session_id,valid_user,login_flag) values(?,?,?,sysdate,?,?,?)");
					    	String[] qv_5={myUserId,loginipaddress,loginmacaddress,sessionId,validUser,loginFlag};
					    	
					    	push(1, qv_5, "", "", "", false, Type.NONE);					    	
					    	if(!resultType.equals("loginFail")){
					    		query=new StringBuffer("SELECT A.OFFICE_CODE,A.STATE_NAME,A.COUNTRY_CODE,A.EMAIL_ID,B.OFFICE_ID,B.OFFICE_TYPE FROM TM_OFFICE A,TM_OFFICETYPE B,TM_USER C WHERE C.USER_ID=? AND A.OFFICE_CODE=C.OFFICE_CODE AND B.OFFICE_ID=A.OFFICE_ID");
								String[] qv_6={myUserId};
								ot=pullResultSet(qv_6, "", "", "", false, Type.NONE);
								if(ot.equals("all")){
									Office office=new Office();
									office.setOfficeCode(resultSet.getString(1));
									office.setStateName(resultSet.getString(2));
									office.setCountry(new Country(resultSet.getString(3)));
									office.setEmailId(resultSet.getString(4));
									OfficeType officeType=new OfficeType();
									officeType.setOfficeId(resultSet.getString(5));
									officeType.setOfficeType(resultSet.getString(6));
									office.setOfficeType(officeType);
									/*TimeZone timeZone=new TimeZone();
									timeZone.setZoneName(resultSet.getString(7));
									office.setTimeZone(timeZone);*/
									myUser.setOffice(office);
								}

								List<Role> roleList = new ArrayList<Role>();
								if(!myUser.getForcePasswordChange()){
									roleList=myUser.getRoleList();
								}
								else{
									Short roleId = 0;
									roleList.add(new Role(roleId));
								}
								if(roleList != null) {
									queryFields=new StringBuffer(roleList.get(0).getRoleId().toString());
									for(int i=1;i<roleList.size();i++){
										queryFields.append(","+roleList.get(i).getRoleId());
									}
									query=new StringBuffer("SELECT DISTINCT A.PMENU_ID,A.PMENU_NAME,B.SMENU_ID,B.SMENU_NAME,B.ACTION_PATH,A.DISPLAY_ORDER " +
											"FROM TM_PARENTMENU A,TM_SUBMENU B,TM_ROLE_MENU C WHERE " +
											"C.ROLE_ID IN("+queryFields+") AND C.RECORD_STATUS=0 AND B.SMENU_ID=C.SMENU_ID AND B.RECORD_STATUS=0 AND " +
											"A.PMENU_ID=B.PMENU_ID AND A.RECORD_STATUS=0 ORDER BY A.DISPLAY_ORDER ASC,B.SMENU_NAME ASC");
									String[] qv_7={};
									//spl(query);
									ot=pullResultSet(qv_7, "navigation menu for user", myUserId, "", true, Type.BAR);
									if(ot.equals("all")){
										List<ParentMenu> parentMenuList=new ArrayList<ParentMenu>();
										boolean permit=false;
										do{
											String pMenuId=new String(resultSet.getString(1));
											ParentMenu parentMenu=new ParentMenu();
											parentMenu.setPmenuId(resultSet.getShort(1));
											parentMenu.setPmenuName(resultSet.getString(2));
											List<SubMenu> subMenuList=new ArrayList<SubMenu>();
											do{
												SubMenu subMenu=new SubMenu();
												subMenu.setSmenuId(resultSet.getShort(3));
												subMenu.setSmenuName(resultSet.getString(4));
												subMenu.setActionPath(resultSet.getString(5));
												subMenuList.add(subMenu);
												permit=resultSet.next();
											}while(permit && pMenuId.equals(resultSet.getString(1)));
											parentMenu.setSubMenuList(subMenuList);
											parentMenuList.add(parentMenu);
										}while(permit);
										myUser.setParentMenuList(parentMenuList);
									}
								}
						    	httpSession.setAttribute("user", myUser);
					    	}
					    	//session.put("user", myUser);
						}
					}
				}
			}
			else{
				resultType="success";
			}
		}catch(Exception e){
			errorStatus(Type.BAR);
			ps(e);
		}
		finish();
		return resultType;
	}
	private boolean pushUserSession(boolean forcePasswordChange) throws Exception{
		query=new StringBuffer("SELECT USER_NAME,(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=TM_USER.DESIGNATION_ID),DESIGNATION_ID FROM TM_USER WHERE USER_ID=?");
		String[] qv_1={myUserId};
		String ot=pullResultSet(qv_1, "user", myUserId, "", true, Type.BAR);
		if(ot.equals("all")){
			myUser.setUserId(myUserId);
			myUser.setUserName(resultSet.getString(1));
			myUser.setUserDesignation(resultSet.getString(2));
			myUser.setLevel(userLevel);
			myUser.setForcePasswordChange(forcePasswordChange);
			myUser.setIp(loginipaddress);
			myUser.setMac(loginmacaddress);
			myUser.setUserDesignationId(resultSet.getString(3));
			
			query=new StringBuffer("SELECT A.ROLE_ID FROM TM_USER_ROLE A WHERE A.USER_ID=? AND A.RECORD_STATUS=?");
			String[] qv_2={myUserId,"0"};
			ot=pullResultSet(qv_2, "user", myUserId, "", true, Type.BAR);
			if(ot.equals("all")){
				List<Role> roleList=new ArrayList<Role>();
				do{
					roleList.add(new Role(resultSet.getShort(1)));
				}while(resultSet.next());
				myUser.setRoleList(roleList);
			}
			return true;
		}
		else
			return false;
	}
	public String logout(){
		begin();
		try{
			//session.clear();
			//session.invalidate();
			httpSession.invalidate();
		}catch(Exception e){
			
		}
		return "success";
	}
	private boolean checkCaptcha() throws Exception{
		boolean result=false;
		query = new StringBuffer("SELECT CAPTCHA_ANSWER FROM T_CAPTCHA_LOG WHERE CAPTCHA_ID=? AND CAPTCHA_ANSWER=? AND SESSION_ID=?");
		String[] qv={clientCaptchaId,clientCaptchaAnswer,sessionId};
		String ot=pullResultSet(qv, "", "", "", false, Type.NONE);
		if(ot.equals("all")){
			result=true;
		}
		query = new StringBuffer("DELETE FROM T_CAPTCHA_LOG WHERE SESSION_ID=?");
		String[] qv_1={sessionId};
		push(1, qv_1, "", "", "", false, Type.NONE);
		return result;
	}
	private void jumbleNumbers(int i) throws Exception{
		for(int j=0;j<i;j++)
			jumbledNumbers.add(j);
		Collections.shuffle(jumbledNumbers);
	}
	
	
	
	// OTP Functions
		public String initOTP(){
			begin();
			try {
					String otpValidity=null;
					String otpDate=null;
					String toAddress=null;
					query=new StringBuffer("select email_id from tm_user where user_id=?");
					statement=connection.prepareStatement(query.toString());
					statement.setString(1, userId);
					resultSet=statement.executeQuery();
					if(resultSet.next()){
						toAddress = resultSet.getString(1);
					}
					statement.close();
					if(checkUserValidity()==true){						
						// Getting Standard OTP validity 
						query=new StringBuffer("SELECT OTP_VALIDITY FROM TM_DEFAULT_PARAMS");	
						statement=connection.prepareStatement(query.toString());
						resultSet=statement.executeQuery();
						if(resultSet.next())
							otpValidity=resultSet.getString(1);
						resultSet.close();
						statement.close();
						
						// current OTP date and time
						query=new StringBuffer("SELECT OTP_DATE,OTP_REFERENCE FROM T_OTP_DETAILS WHERE USER_ID=? AND RECORD_STATUS=0");
						statement=connection.prepareStatement(query.toString());
						statement.setString(1, userId);
						resultSet=statement.executeQuery();
						Boolean existFlag=false;
						if(resultSet.next()){
							existFlag=true;
							otpDate=resultSet.getString(1);						
							otpRefNumber=resultSet.getString(2);
						}			
						if(existFlag==true){ // Checking OTP validity against existing User
							// Checking active OTP validity
							long validityTime=getHourDifference(otpDate);
							if(validityTime<Long.parseLong(otpValidity)){
								notifyList.add(new Notification("", "OTP has already been sent to your mail.Please use it to authenticate.", Status.WARNING, Type.BAR));
							}else{
								// Generating new OTP
									otpValue=generateOTP();
									otpRefNumber=generateOTPRef();
								// Inserting Otp row
									query = new StringBuffer("INSERT INTO T_OTP_DETAILS (USER_ID,OTP,OTP_REFERENCE,OTP_DATE,RECORD_STATUS) VALUES (?,?,?,sysdate,0)");
									String[] qv={userId,otpValue,otpRefNumber};
									push(1, qv, "", "", "", false, Type.NONE);
								// Sending OTP on mail to User					
									AutoNotifier notifier=new AutoNotifier();
									notifier.setOtpValue(otpValue);
									//notifier.pushAutoNotifications(otpRefNumber, Integer.valueOf(8), "2", userId, connection);	
									notifier.pushAutoNotifications(otpRefNumber, Integer.valueOf(4), "2", toAddress, connection, "", "");
									notifyList.add(new Notification("", "Please check your email for a message with your OTP.", Status.WARNING, Type.BAR));
							}	
						}else{ // Fresh User
							// Generating new OTP
								otpValue=generateOTP();
								otpRefNumber=generateOTPRef();
							// Inserting Otp row
								query = new StringBuffer("INSERT INTO T_OTP_DETAILS (USER_ID,OTP,OTP_REFERENCE,OTP_DATE,RECORD_STATUS) VALUES (?,?,?,sysdate,0)");
								String[] qv={userId,otpValue,otpRefNumber};
								push(1, qv, "", "", "", false, Type.NONE);
							// Sending OTP on mail to User					
								AutoNotifier notifier=new AutoNotifier();
								notifier.setOtpValue(otpValue);
								//notifier.pushAutoNotifications(otpRefNumber, Integer.valueOf(8), "2", userId, connection);
								notifier.pushAutoNotifications(otpRefNumber, Integer.valueOf(4), "2", toAddress, connection, "", "");
								notifyList.add(new Notification("", "Please check your email for a message with your OTP.", Status.WARNING, Type.BAR));
						}
					}else{
						notifyList.add(new Notification("Error !!", "Invalid User Id.", Status.ERROR, Type.BAR));
						return "error";
					}		
				}catch(ValidationException ve){
					try {						
							notifyList.add(new Notification("Error !!",ve.getMessage(), Status.ERROR, Type.BAR));
							return "error";
					}catch(Exception ex) {}
				}catch (Exception e) {
					ps(e);
					errorStatus(Type.BAR);
					return "error";
				}finally{
					finish();
				}	
				return "success";
		}
		
		public String regenerateOTP(){
			begin();
			try {
					String toAddress=null;
					query=new StringBuffer("select email_id from tm_user where user_id=?");
					statement=connection.prepareStatement(query.toString());
					statement.setString(1, userId);
					resultSet=statement.executeQuery();
					if(resultSet.next()){
						toAddress = resultSet.getString(1);
					}
					statement.close();
					if(checkUserValidity()==true){
						// Making all available active OTPs inactive
						query = new StringBuffer("UPDATE T_OTP_DETAILS SET RECORD_STATUS = 1 WHERE USER_ID=?");
						statement = connection.prepareStatement(query.toString());				
						statement.setString(1, userId);				
						statement.executeUpdate();
						connection.commit();
						
						// Generating new OTP
						otpValue=generateOTP();
						otpRefNumber=generateOTPRef();			
						
						// Inserting Otp row
						query = new StringBuffer("INSERT INTO T_OTP_DETAILS (USER_ID,OTP,OTP_REFERENCE,OTP_DATE,RECORD_STATUS) VALUES (?,?,?,sysdate,0)");
						String[] qv={userId,otpValue,otpRefNumber};
						push(1, qv, "", "", "", false, Type.NONE);
						// Sending OTP on mail to User					
						AutoNotifier notifier=new AutoNotifier();
						notifier.setOtpValue(otpValue);
						//notifier.pushAutoNotifications(otpRefNumber, Integer.valueOf(8), "2", userId, connection);	
						notifier.pushAutoNotifications(otpRefNumber, Integer.valueOf(4), "2", toAddress, connection, "", "");
						notifyList.add(new Notification("", "Please check your email for a message with your OTP.", 
								Status.WARNING, Type.BAR));
					}else{
							notifyList.add(new Notification("Error !!", "Invalid User Id.", Status.ERROR, Type.BAR));
							return "error";
					}		
				}catch(ValidationException ve){
					try {						
							notifyList.add(new Notification("Error !!",ve.getMessage(), Status.ERROR, Type.BAR));
							return "error";
					}catch(Exception ex) {}
				}catch (Exception e) {
					ps(e);
					errorStatus(Type.BAR);
					return "error";
				}finally{
					finish();
				}	
				return "success";
		}
		
		public String validateOTP(){		
			begin();
			try {
					if(ESAPI.validator().isValidInput("OTP", otpValue, "Num", 6, false) == false){
						notifyList.add(new Notification("Error !!", " Invalid OTP.", Status.ERROR, Type.BAR));
						finish();					
						return "error";
					}
					if(ESAPI.validator().isValidInput("OTP Reference Number", otpRefNumber, "Num", 5, false) == false){
						notifyList.add(new Notification("Error !!", "Invalid OTP Reference Number.", Status.ERROR, Type.BAR));
						finish();					
						return "error";
					}
					String otpValidity=null;
					String otpDate=null;
					String otp=null;
					// Getting Standard OTP validity 
					query=new StringBuffer("SELECT OTP_VALIDITY FROM TM_DEFAULT_PARAMS");	
					statement=connection.prepareStatement(query.toString());
					resultSet=statement.executeQuery();
					if(resultSet.next())
						otpValidity=resultSet.getString(1);
					resultSet.close();
					statement.close();
					
					// current OTP date and time
					query=new StringBuffer("SELECT OTP_DATE,OTP FROM T_OTP_DETAILS WHERE OTP_REFERENCE=? AND RECORD_STATUS=0");
					statement=connection.prepareStatement(query.toString());
					statement.setString(1, otpRefNumber);
					resultSet=statement.executeQuery();
					if(resultSet.next()){
						otpDate=resultSet.getString(1);
						otp=resultSet.getString(2);
					}				
					long validityTime=getHourDifference(otpDate);
					if(validityTime<Long.parseLong(otpValidity)){
						if(otp.equals(otpValue)){
							
						}else{
							notifyList.add(new Notification("Error !!", "OTP entered is invalid.", Status.ERROR, Type.BAR));
							return "error";
						}
					}else{
						notifyList.add(new Notification("Error !!", "OTP has been expired. Please regenerate OTP.", Status.ERROR, Type.BAR));
						return "error";
					}						
			}catch (Exception e) {
				ps(e);			
				return "error";
			}finally{
				finish();
			}		
			return "success";
		}
		private long getHourDifference(String date) throws Exception{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			Date startDate = dateFormat.parse(date);
			Date endDate=cal.getTime();
			long diff = endDate.getTime() - startDate.getTime();
			long diffHours = diff / (60 * 60 * 1000);		
			return diffHours;
		}
		private String generateOTP(){		
			RandomString random = new RandomString();
			return random.getRandomNumber(6);
		}
		private String generateOTPRef(){		
			RandomString random = new RandomString();
			return random.getRandomNumber(5);
		}
		private Boolean checkUserValidity() throws Exception{
			if(ESAPI.validator().isValidInput("User ID", userId, "UserId", 10, false) == false){				
				throw new ValidationException("Invalid userId. No symbols(except '_') and spaces allowed. (10 Max.)");			
			}
			query=new StringBuffer("SELECT * FROM TM_USER WHERE USER_ID=?");
			statement=connection.prepareStatement(query.toString());
			statement.setString(1, userId);
			resultSet=statement.executeQuery();
			if(resultSet.next())		
				return true;
			else
				return false;
		}
		public String submitResetPassword(){		
			try {
					String status=validateOTP();
					if(status.equals("error")){
						notifyList.add(new Notification("Error!! ","Invalid request.Please try later.", Status.ERROR, Type.BAR,"passwordModal-error"));
						return "error";
					}
					begin();						
					validateParams(newPassword, confirmNewPassword);				
					query = new StringBuffer("UPDATE TM_USER SET PWD_DATE = sysdate ,USERPWD = ? WHERE USER_ID = ?");
					statement = connection.prepareStatement(query.toString());
					statement.setString(1, newPassword);
					statement.setString(2, userId);				
					statement.executeUpdate();
					
					query = new StringBuffer("UPDATE T_OTP_DETAILS SET RECORD_STATUS = 1 WHERE USER_ID=?");
					statement = connection.prepareStatement(query.toString());				
					statement.setString(1, userId);				
					statement.executeUpdate();
					connection.commit();
					
					notifyList.add(new Notification("","Password changed Successfully.Please try to login.", Status.SUCCESS,Type.BAR));				
					//ActionStatus.updateActionStatus("Password changed Successfully.",request,userId);					
				} catch(ValidationException ve){
					try {
							connection.rollback();
							//ActionStatus.updateActionStatus("Error in execution",request,userId);
							notifyList.add(new Notification("Error!! ",ve.getMessage(), Status.ERROR, Type.BAR,"passwordModal-error"));
							return "error";
					}catch(Exception ex) {}
				} catch (Exception e) {
					try {
							connection.rollback();
							//ActionStatus.updateActionStatus("Error in execution",request,userId);
							notifyList.add(new Notification("Error!! ","Some unexpected error occured.", Status.ERROR, Type.BAR,"passwordModal-error"));
							return "error";
					}catch(Exception ex) {}			
					ps(e);
				}finally{
					finish();
			}
			return "success";
		}
		private void validateParams(String newPassword,String confirmNewPassword) throws Exception {		
			if (ESAPI.validator().isValidInput("Password", newPassword,"Password", 40, false) == false) {
				throw new ValidationException("Password should contain at least one number, one uppercase letter, one lowercase letter and one special character including . _ % ' - @ ! # ^ * $ .The size should be in the range of 8-20");
			}
			if (ESAPI.validator().isValidInput("Password", confirmNewPassword,"Password", 40, false) == false) {
				throw new ValidationException("Password should contain at least one number, one uppercase letter, one lowercase letter and one special character including . _ % ' - @ ! # ^ * $ .The size should be in the range of 8-20");
			}
			if(ESAPI.validator().isValidInput("User ID", userId, "UserId", 10, false) == false){				
				throw new ValidationException("Invalid userId !! No symbols(except '_') and spaces allowed. (10 Max.)");			
			}
			if (newPassword.equals(confirmNewPassword) == false) {
				throw new ValidationException("Re-typed password does not match with new password");
			}		
		}
		// OTP Functions
	
	public String pullCaptcha(){
		begin();
		try {
			generateCaptcha();
			System.out.println(captchaAnswer+" - "+captchaQuestion);
			query = new StringBuffer("INSERT INTO T_CAPTCHA_LOG (CAPTCHA_ID,SESSION_ID,CAPTCHA_ANSWER) VALUES (?,?,?)");
			String[] qv={captchaId,sessionId,captchaAnswer.toUpperCase()};
			push(1, qv, "", "", "", false, Type.NONE);
		}catch (Exception e) {
			ps(e);
			errorStatus(Type.BAR);
		}
		finish();
		return "success";
	}	
	private void generateCaptcha() throws Exception{
		Random random = new Random();
		SecureRandom secureRandom = new SecureRandom();
		captchaId=new BigInteger(130, secureRandom).toString(32);
		System.out.println("ques: "+captchaId);
		char [] alphabet ={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		String [] alphabetSerial={"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25"};
		char [] numbersAsc={'0','1','2','3','4','5','6','7','8','9'};
		char [] numbersDesc={'9','8','7','6','5','4','3','2','1','0'};
		String[] colorQues={"sky","sun","leaf","moon","blood","skin"};
		String[] colorAns={"BLUE","YELLOW","GREEN","WHITE","RED","SKIN"};
		String[] spellQA={"nic","mea","jnb","india","delhi","ganga","everest","ashoka","computer","laptop","akbar","yamuna","nisci","indian"};
		int captchaCase = random.nextInt(5);		
		int c=0;
		if(captchaCase==c++){  // Comparing two numbers
			int num1=random.nextInt(100);
			int num2=random.nextInt(100);
			int type=random.nextInt(2);
			if(type==1){ // Less
				if(num1<num2)
					captchaAnswer=String.valueOf(num1);					
				else if(num1==num2)
					generateCaptcha();
				else
					captchaAnswer=String.valueOf(num2);
				captchaQuestion="Which is smaller <em><strong>"+num1+"</strong></em> or <em><strong>"+num2+"</strong></em> ?";
			}
			else{
				if(num1>num2)
					captchaAnswer=String.valueOf(num1);					
				else if(num1==num2)
					generateCaptcha();
				else
					captchaAnswer=String.valueOf(num2);
				captchaQuestion="Which is greater <em><strong>"+num1+"</strong></em> or <em><strong>"+num2+"</strong></em> ?";
			}
		}
		if(captchaCase==c++){	// Reverse Numbers			
			int inc=0;
			int i=0;
			captchaQuestion="Reverse <em><strong>";
			captchaAnswer="";
			jumbleNumbers(9);
			for(i=0;i<5;i++){
				captchaAnswer+=jumbledNumbers.get(i);
			}
			
			int length1 = captchaAnswer.length();
			for (i = length1 - 1 ; i >= 0 ; i-- )
				captchaQuestion = captchaQuestion + captchaAnswer.charAt(i);
			
			captchaQuestion+="</strong></em>.";
		}
		if(captchaCase==c++){ //Reverse Alphabets
			int inc=0;
			int i=0;
			captchaQuestion="Reverse <em><strong>";
			captchaAnswer="";
			jumbleNumbers(25);
			for(i=0;i<5;i++){
				captchaAnswer+=alphabetSerial[i];
			}
			--i;
			for(int j=i;j>=0;j--){
				captchaQuestion+=captchaAnswer.charAt(j);
			}
			captchaQuestion+="</strong></em>.";
		}
		if(captchaCase==c++){ //Add and Minus
			int num1 = random.nextInt(20) + 1; 
			int num2 = random.nextInt(20) + 1; 	
			/*int type=random.nextInt(2);
			if(type==1){*/// Sum					
				captchaAnswer=String.valueOf(num1+num2);		
	     		captchaQuestion="Solve: <em><strong>"+num1+"</strong></em> plus <em><strong>"+num2+"</strong></em>";
	     	/*}
			else{
				captchaAnswer=String.valueOf(num1-num2);		
	     		captchaQuestion="Solve: <em><strong>"+num1+"</strong></em> minus <em><strong>"+num2+"</strong></em>";
			}*/
	    }
		if(captchaCase==c++){ //Spelling questions
			captchaQuestion="How do you spell <em><strong>";
			int type=random.nextInt(spellQA.length);
			captchaQuestion+=spellQA[type].toUpperCase()+"</strong></em> ?";
			captchaAnswer=spellQA[type];
		}
		if(captchaCase==c++){ //Arrange ascending or descending
			int inc=0;
			int i=0;
			captchaQuestion="Arrange <em><strong>";
			captchaAnswer="";
			int type=random.nextInt(2);
			if(type==1){//Ascending
				for(i=0;i<5;i++){
					inc+=random.nextInt(2)+1;
					if(inc<=9){
						captchaAnswer+=numbersAsc[inc];
					}
					else
						break;
				}
				jumbleNumbers(i);
				for(int j=0;j<i;j++){
					captchaQuestion+=captchaAnswer.charAt(jumbledNumbers.get(j));
				}
				captchaQuestion+="</strong></em> in ascending order.";
			}
			else{//Descending
				for(i=0;i<5;i++){
					inc+=random.nextInt(2)+1;
					if(inc<=9){
						captchaAnswer+=numbersDesc[inc];
					}
					else
						break;
				}
				jumbleNumbers(i);
				for(int j=0;j<i;j++){
					captchaQuestion+=captchaAnswer.charAt(jumbledNumbers.get(j));
				}
				captchaQuestion+="</strong></em> in descending order.";
			}
			
		}
		if(captchaCase==c++){ // in alphabetical order
			int inc=0;
			int i=0;
			captchaQuestion="Arrange <em><strong>";
			captchaAnswer="";
			for(i=0;i<3;i++){
				inc+=random.nextInt(6)+1;
				if(inc<=25){
					captchaAnswer+=alphabet[inc];
				}
				else
					break;
			}
			jumbleNumbers(i);				
			for(int j=0;j<i;j++){
				captchaQuestion+=captchaAnswer.charAt(jumbledNumbers.get(j));
			}
			captchaQuestion+="</strong></em> in alphabetical order.";
		}
		if(captchaCase==c++){ //Color questions
			captchaQuestion="What color is <em><strong>";
			int type=random.nextInt(colorQues.length);
			captchaQuestion+=colorQues[type]+"</strong></em> of ?";
			captchaAnswer=colorAns[type];
		}
	}
	public void generateSalt(){
		RandomString rs=new RandomString();
		salt=rs.getRandomString(20);
		httpSession.setAttribute("salt", salt);
	}
	
	/* Common - Start*/
	public String getProcessTime() {
		return processTime;
	}
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId.toUpperCase();
	}
	public String getFunctionFlag() {
		return functionFlag;
	}
	public void setFunctionFlag(String functionFlag) {
		this.functionFlag = functionFlag;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public List<Notification> getNotifyList() {
		return notifyList;
	}
	public void setNotifyList(List<Notification> notifyList) {
		this.notifyList = notifyList;
	}
	/*Common - End*/
	public String getCaptchaQuestion() {
		return captchaQuestion;
	}
	public void setCaptchaQuestion(String captchaQuestion) {
		this.captchaQuestion = captchaQuestion;
	}
	public String getCaptchaAnswer() {
		return captchaAnswer;
	}
	public void setCaptchaAnswer(String captchaAnswer) {
		this.captchaAnswer = captchaAnswer;
	}
	public String getCaptchaId() {
		return captchaId;
	}
	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}
	public String getClientCaptchaId() {
		return clientCaptchaId;
	}
	public void setClientCaptchaId(String clientCaptchaId) {
		this.clientCaptchaId = clientCaptchaId;
	}
	public String getClientCaptchaAnswer() {
		return clientCaptchaAnswer;
	}
	public void setClientCaptchaAnswer(String clientCaptchaAnswer) {
		this.clientCaptchaAnswer = clientCaptchaAnswer.toUpperCase();
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		if(userId==null){}
		else if(!userId.equalsIgnoreCase("nic"))
			this.userId = userId.toUpperCase();
		else
			this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getLoginipaddress() {
		return loginipaddress;
	}
	public void setLoginipaddress(String loginipaddress) {
		this.loginipaddress = loginipaddress;
	}
	public String getLoginmacaddress() {
		return loginmacaddress;
	}
	public void setLoginmacaddress(String loginmacaddress) {
		this.loginmacaddress = loginmacaddress;
	}
	public String getProceed() {
		return proceed;
	}
	public void setProceed(String proceed) {
		this.proceed = proceed;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
	public String getOtpRefNumber() {
		return otpRefNumber;
	}
	public void setOtpRefNumber(String otpRefNumber) {
		this.otpRefNumber = otpRefNumber;
	}
	public String getOtpValue() {
		return otpValue;
	}
	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}
}
