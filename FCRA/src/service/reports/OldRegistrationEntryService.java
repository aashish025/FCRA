package service.reports;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import utilities.Commons;
import utilities.ValidationException;
import models.reports.OldRegistrationEntryDtl;

import org.owasp.esapi.ESAPI;

import dao.master.AssociationNatureDao;
import dao.master.BanksDao;
import dao.master.DistrictDao;
import dao.master.ReligionTypeDao;
import dao.master.StateDao;
import dao.reports.OldRegistrationEntryDao;
import utilities.KVPair;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;



public class OldRegistrationEntryService extends Commons{
private String rcn;	
private String assoName;
private String assoTownCity;
private String assoAddress;
private String assoNature;
private String state;
private String district;
private String assoPin;
private String assoReligion;
private String assoAims;
private String regDate;
private String validFrom;
private String validTo;
private String userId;
private String accountNumber;
private String bankName;
private String bankAddress;
private String bankState;
private String bankDistrict;
private String bankZipCode;
private String bankTownCity;

private String oldregRemark;
private String actionBy;
private String status;
//private String myUserId;


private List<KVPair<String, String>> stateList;
private List<List2> districtList;
private List<List2> religionList;
private List<KVPair<String, String>> assoNatureList;
private List<KVPair<String, String>> banknameList;
//public String requestToken;		
private int rcnStatus;
		
public String execute() {
   String result = "error";
   begin();
	try {
		initStateList();
		initAssoNatureList();
		initBankNameList();
		result = "success";
	} catch(Exception e){ps(e);
	try {
		notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,
				Type.BAR));
	} catch (Exception ex) {
      }
	}
	finally{
		finish();
	}	
	
	return result;
	//return "success";
}
 
public String initAddOldRegEntry() throws NotificationException {
	String result = "error";	
	begin();
	try {
		addOldRegEntryDetails();
	} catch(Exception e){
		notifyList.add(new Notification("Error!!", "We are sorry! Some unexpected error occured. Please try again later.", Status.ERROR, Type.BAR));
		try {
			connection.rollback();
		} catch (SQLException e1) {				
			e1.printStackTrace();
		}
		spl(e);
	}
	finally{
		finish();
	}	
	return "success";
}


public String initPrintDetails() throws Exception{
	String result="error";
	begin();
	try {
		    checkPrintRcnDetails();
	}
	 catch(ValidationException ve){
			notifyList.add(new Notification("Error!",ve.getMessage(), Status.ERROR, Type.BAR));
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	catch(Exception e){
		notifyList.add(new Notification("Error!",e.getMessage(), Status.ERROR, Type.BAR,"printCertificateModal-error"));
	}
	finally{
		finish();
	}
	return "success";
}



private void checkPrintRcnDetails() throws Exception {
	// TODO Auto-generated method stub
	OldRegistrationEntryDao oldRegistrationEntryDao=new OldRegistrationEntryDao(connection);
	oldRegistrationEntryDao.setRcn(rcn);
	rcnStatus = oldRegistrationEntryDao.checkRcnNumber();
	/*if(rcnStatus<1){
		notifyList.add(new Notification("Warning!","Specified RCN is new. Kindly Reprint Registration Certificate from Workspace" , Status.WARNING, Type.BAR,"printCertificateModal-error"));		
	}*/	
	
}

public Boolean validateParameters() throws Exception{	
	
	
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", assoName, "WordS",100, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Association Name. Only 100 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", assoAddress, "WordSS",150, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Association Address. Only 150 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", assoTownCity, "WordS",50, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Association City. Only 50 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", state, "Word",5, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid State", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", district, "Word",5, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid district", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", assoPin, "Num",6, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Pincode. Only 6 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	String anature=assoNature.replace(",","");
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", anature, "Word",5, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Nature of Assocation.", Status.ERROR, Type.BAR));
		return false;
	}	
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", assoReligion, "Word",1, true) == false){
		notifyList.add(new Notification("Error!!", "Invalid Religion.", Status.ERROR, Type.BAR));
		return false;
	}
	
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", regDate, "Date", 20, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Registration date. Should be in dd-mm-yyyy format", Status.ERROR, Type.BAR));
		return false;
	}
	/*SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");
	Date date1 = sdf.parse("12-12-2015");
	Date date2 = sdf.parse(regDate);
	if(date1.before(date2)){
		notifyList.add(new Notification("Error!!", "Invalid Registration date. Should be on or before 12-12-2015", Status.ERROR, Type.BAR));
		return false;
	}*/
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", validFrom, "Date",20, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid ValidFrom Date - Should be in dd-mm-yyyy format", Status.ERROR, Type.BAR));
		return false;
	}
	
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", validTo, "Date",20, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Valid To date - Should be in dd-mm-yyyy format", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", assoAims, "WordSS",2000, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Association Aims. Only 2000 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", userId, "WordSS",20, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid User Id. Only 20 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", bankName, "WordS",60, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Bank Name", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", bankAddress, "WordSS",150, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Bank Address. Only 150 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", bankState, "Word",5, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Bank State", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", bankDistrict, "Word",5, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Bank District", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", bankTownCity, "WordS",50, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Bank City. Only 50 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", bankZipCode, "Num",6, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Bank Zipcode. Only 6 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", accountNumber, "Word",30, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Account Number. Only 30 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	if(ESAPI.validator().isValidInput("OldRegistrationEntryDtl", oldregRemark, "WordSS",2000, false) == false){
		notifyList.add(new Notification("Error!!", "Invalid Remark. Only 2000 Characters allowed", Status.ERROR, Type.BAR));
		return false;
	}
	return true;
}	



private void addOldRegEntryDetails() throws Exception {
	// TODO Auto-generated method stub
	if(validateParameters()==true){
	OldRegistrationEntryDao oldRegistrationEntryDao=new OldRegistrationEntryDao(connection);

	OldRegistrationEntryDtl oredto=new OldRegistrationEntryDtl();
	oredto.setRcn(rcn);
	oredto.setAssoName(assoName);
	oredto.setAssoAddress(assoAddress);
	oredto.setAssoTownCity(assoTownCity);						                       
	oredto.setAssoState(state);								
	oredto.setAssoDistrict(district);							 
	oredto.setAssoPin(assoPin);							                          
	oredto.setAssoNature(assoNature);                                 			
	oredto.setAssoReligion(assoReligion);								
	oredto.setRegDate(regDate);	
	oredto.setValidFrom(validFrom);								
	oredto.setValidTo(validTo);
	oredto.setAssoAims(assoAims);
	oredto.setUserId(userId);
	
	oredto.setBankName(bankName);									
	oredto.setBankAddress(bankAddress);	
	oredto.setBankState(bankState);
	oredto.setBankDistrict(bankDistrict);
	oredto.setBankTownCity(bankTownCity);
	oredto.setBankZipCode(bankZipCode);
	oredto.setAccountNumber(accountNumber);
	
	oredto.setMyUserId(myUserId);
	oredto.setStatus(status);
	oredto.setOldregRemark(oldregRemark);
	

    int status=	oldRegistrationEntryDao.insertRecord(oredto);
	if(status>0)
		notifyList.add(new Notification("Success!!", "Registration  Details added successfully. Please note your Reg. No. <b>"+oredto.getRcn()+"</b> for future reference" , Status.SUCCESS, Type.BAR));
		setRcn(oldRegistrationEntryDao.getRcn());
	
	}
	
}




public void initAssoNatureList() throws Exception{
	AssociationNatureDao andao=new AssociationNatureDao(connection);	
	assoNatureList=andao.getKVList1();
}
public void initStateList() throws Exception{
	StateDao pdd=new StateDao(connection);	
	stateList=pdd.getKVList();	
}
public void initBankNameList() throws Exception{
	BanksDao bnkDao=new BanksDao(connection);
	banknameList=bnkDao.getKVList();	
}


			
public void initDistrictList(){
			begin();
			try {
				getDistrict();				
			} catch(Exception e){
				try {
						connection.rollback();
					} catch (Exception e1) {				
					e1.printStackTrace();
				}
				ps(e);
			}
			finally{
				finish();
			}		
		}
	private void getDistrict() throws Exception{
		DistrictDao ddao=new DistrictDao(connection);	
			districtList=ddao.getDistrictList(state);	
		}
	
public void initReligionList(){
		begin();
		try {
			getReligion();				
		} catch(Exception e){
			try {
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
		}
		finally{
			finish();
		}		
	}



private void getReligion() throws Exception{
	ReligionTypeDao rdao=new ReligionTypeDao(connection);
		religionList=rdao.getReligionType();	
	}





public String getOldRegistrationCertification(String rcn){
	if(rcn == null){
		return "error";
	}	
	try{
	begin();
	OldRegistrationEntryDao oldRegistrationEntryDao=new OldRegistrationEntryDao(connection);
	oldRegistrationEntryDao.setMyUserId(myUserId);
	oldRegistrationEntryDao.setMyOfficeCode(myOfficeCode);
	oldRegistrationEntryDao.setRcn(rcn);
	oldRegistrationEntryDao.GenerateReport();
	}catch(Exception e){
		ps(e);
	}finally{
		finish();
	}
	return "success";
}


    public List<KVPair<String, String>> getStateList() {
		return stateList;
	}
	
	public void setStateList(List<KVPair<String, String>> stateList) {
		this.stateList = stateList;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public List<List2> getDistrictList() {
		return districtList;
	}


	public void setDistrictList(List<List2> districtList) {
		this.districtList = districtList;
	}

	public String getAssoNature() {
		return assoNature;
	}


	public void setAssoNature(String assoNature) {
		this.assoNature = assoNature;
	}


	public List<KVPair<String, String>> getAssoNatureList() {
		return assoNatureList;
	}


	public void setAssoNatureList(List<KVPair<String, String>> assoNatureList) {
		this.assoNatureList = assoNatureList;
	}



	public List<List2> getReligionList() {
		return religionList;
	}



	public void setReligionList(List<List2> religionList) {
		this.religionList = religionList;
	}



	public List<KVPair<String, String>> getBanknameList() {
		return banknameList;
	}



	public void setBanknameList(List<KVPair<String, String>> banknameList) {
		this.banknameList = banknameList;
	}

	public String getAssoName() {
		return assoName;
	}

	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}

	
	public String getAssoReligion() {
		return assoReligion;
	}

	public void setAssoReligion(String assoReligion) {
		this.assoReligion = assoReligion;
	}

	public String getAssoAims() {
		return assoAims;
	}

	public void setAssoAims(String assoAims) {
		this.assoAims = assoAims;
	}

	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	
	public String getBankZipCode() {
		return bankZipCode;
	}

	public void setBankZipCode(String bankZipCode) {
		this.bankZipCode = bankZipCode;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAssoTownCity() {
		return assoTownCity;
	}

	public void setAssoTownCity(String assoTownCity) {
		this.assoTownCity = assoTownCity;
	}

	public String getAssoPin() {
		return assoPin;
	}

	public void setAssoPin(String assoPin) {
		this.assoPin = assoPin;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankState() {
		return bankState;
	}

	public void setBankState(String bankState) {
		this.bankState = bankState;
	}

	public String getBankDistrict() {
		return bankDistrict;
	}

	public void setBankDistrict(String bankDistrict) {
		this.bankDistrict = bankDistrict;
	}

	public String getBankTownCity() {
		return bankTownCity;
	}

	public void setBankTownCity(String bankTownCity) {
		this.bankTownCity = bankTownCity;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAssoAddress() {
		return assoAddress;
	}

	public void setAssoAddress(String assoAddress) {
		this.assoAddress = assoAddress;
	}

	public String getRcn() {
		return rcn;
	}

	public void setRcn(String rcn) {
		this.rcn = rcn;
	}

	public String getOldregRemark() {
		return oldregRemark;
	}

	public void setOldregRemark(String oldregRemark) {
		this.oldregRemark = oldregRemark;
	}

	public String getActionBy() {
		return actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMyUserId() {
		return myUserId;
	}

	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}

	public int getRcnStatus() {
		return rcnStatus;
	}

	public void setRcnStatus(int rcnStatus) {
		this.rcnStatus = rcnStatus;
	}
    
	
	
/*	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}*/




	

		

}
