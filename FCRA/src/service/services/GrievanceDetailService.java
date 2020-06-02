package service.services;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import models.master.Office;
import models.master.UserLevel;
import models.services.Grievance;
import models.services.requests.AbstractRequest;
import utilities.Commons;
import utilities.KVPair;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.DistrictDao;
import dao.master.OfficeDao;
import dao.master.StateDao;
import dao.master.UserLevelDao;
import dao.reports.RegistrationTrackingDao;
import dao.services.GrievanceDao;
import dao.services.dashboard.ProjectDashboardDao;


public class GrievanceDetailService  extends Commons {
	private String state;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String appName;
	private String district;
	private String assoName;
	private String assoAddress;
	private String townCity;
	private String assoPincode;
	private String comName;
	private String comAddress;
	private String comEmail;
	private String comMobile;
	private String complaint;
	private String user;
	private String checkedvalue;
	private String registrationId;
	private String applicationId;
	private String officevalue;
	private MultipartFile documentFile;
	
    private List<AbstractRequest> applicationList;
	private List<List2> districtList;
	List<KVPair<String, String>> stateList;
	List<List2> userList;
	List<KVPair<String, String>> IbList;
	
	
	public void populateList(){
		begin();
		try {
				populatestateList();	
				populateUserList();
				populateIbList();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	public void populateIbList() throws Exception{
		GrievanceDao pdd=new GrievanceDao(connection);		
		IbList=pdd.IbListfromOffice();
	}
	public void populatestateList() throws Exception{
		StateDao pdd=new StateDao(connection);		
		stateList=pdd.getKVList();
	}
	public void populateUserList() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		if(myOfficeId.equals("1")){
			userList=pdd.forwardUserList(myOfficeCode,"2",myUserId);
		}
		else if(myOfficeId.equals("2"))
			userList=pdd.forwardUserList(myOfficeCode,"4",myUserId);
		else if(myOfficeId.equals("3"))
			userList=pdd.forwardUserList(myOfficeCode,"6",myUserId);
		
	}
	public void initDistrict(){
		begin();
		try {
			fetchDistrict();				
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
	private void fetchDistrict() throws Exception{
		DistrictDao pdd=new DistrictDao(connection);	
		districtList=pdd.getDistrictList(state);	
	}
	public void initAdvanceApplicationListDetails(){
		begin();
		try {
				populateAdvanceApplicationListDetails();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	private void populateAdvanceApplicationListDetails() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);						
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setSearchString(appName);
		pdd.setState(state);
		pdd.setDistrict(district);
		applicationList=pdd.getAdvanceApplicationListDetails("0");
		totalRecords=pdd.getTotalRecords();
	}
	public Boolean validateYes() throws Exception{		
		if(ESAPI.validator().isValidInput("Association Name", assoName, "Word", 100, true) == false){
			notifyList.add(new Notification("Error!!", "Association Name - Only Alphabet  allowed (100 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("State", state, "Word", 2, true) == false){
			notifyList.add(new Notification("Error!!", "State - Only alphabet and numbers allowed (2 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("District", district, "Word", 3, true) == false){
			notifyList.add(new Notification("Error!!", "District - Only alphabet and numbers allowed (3 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Association Address", assoAddress, "Address", 150, true) == false){
			notifyList.add(new Notification("Error!!", "State - Only alphabet and numbers allowed (150 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Association Town City", townCity, "Word", 50, true) == false){
			notifyList.add(new Notification("Error!!", "Association Town City - Only alphabet and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Association PinCode", assoPincode, "Pincode", 6, true) == false){
			notifyList.add(new Notification("Error!!", "Association PinCode - Only numbers allowed (6 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Complainant  Name", comName, "Word", 100, false) == false){
			notifyList.add(new Notification("Error!!", "Association Name - Only Alphabet  allowed (100 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Complainant Mobile", comMobile, "Phone", 20, true) == false){
			notifyList.add(new Notification("Error!!", "State - Only  numbers allowed (20 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Complainant Email", comEmail, "Email", 100, true) == false){
			notifyList.add(new Notification("Error!!", "District - Only alphabet and numbers allowed (100 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Complainant Address", comAddress, "Address", 150, false) == false){
			notifyList.add(new Notification("Error!!", "Complainant Address - Only alphabet and numbers allowed (150 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("Complainant ", complaint, "WordSS", 3000, false) == false){
			notifyList.add(new Notification("Error!!", "Complainant  - Only alphabet and numbers allowed (3000 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public String addDetails(HttpServletRequest request) {
		begin();
		try {
			addGrievanceDetail(request);
		} catch(Exception e){
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
	

	public void addGrievanceDetail(HttpServletRequest request) throws Exception{
		GrievanceDao rdao=new GrievanceDao(connection);
		ProjectDashboardDao ppd=new ProjectDashboardDao(connection);
		Grievance grievance=new Grievance();
		if(validateYes()==true){
		grievance.setRegistrationId(registrationId);
		 grievance.setAssoName(assoName);
		 grievance.setState(state);
		 grievance.setDistrict(district);
		 grievance.setAssoAddress(assoAddress);
		 grievance.setTownCity(townCity);
		 grievance.setAssoPincode(assoPincode);
		 grievance.setComName(comName);
		 grievance.setComAddress(comAddress);
		 grievance.setComEmail(comEmail);
		 grievance.setComMobile(comMobile);
		 grievance.setComplaint(complaint);
		 grievance.setUser(user);	
		 grievance.setMyOfficeCode(myOfficeCode);
		 grievance.setMyUserId(myUserId);
		 grievance.setCheckedvalue(checkedvalue);
		 grievance.setMyOfficeId(myOfficeId);
		 grievance.setOfficevalue(officevalue);
		 grievance.setDocumentFile(documentFile);
		
		int status=rdao.insertRecord(grievance);
				if(status>0){
					String appliId="";
				    appliId	=setApplicationId(rdao.getApplicationId());	
				   String finaldata=ppd.grievanceApplication(grievance,appliId);
					{ 
						if(finaldata.equals("success"))
						notifyList.add(new utilities.notifications.Notification("Success!!", "Grievance  service with Application Id <b>"+appliId+"</b> has been created successfully.", Status.SUCCESS, Type.BAR));	
					}
				
				}
		}
			
	}
/*	public Integer uploadSupportingDocs() throws Exception {
		// TODO Auto-generated method stub
		String str = myUserId;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		InputStream is = null;
		try 
		{
			String queryUploading = "Insert into  t_supporting_documents_death (F_DEATH_ID, DOCS, UPLOADED_BY, UPLOADED_DATE, UPLOADED_IP,RECORD_STATUS) " +
					"values(?,?,?,sysdate,?,0)";
			
			PreparedStatement statement = connection.prepareStatement(queryUploading);
			statement.setString(1, fDeathId);
			if(documentFile!=null)
				is = documentFile.getInputStream();
			if(is==null)
				statement.setNull(2, java.sql.Types.BLOB);
			else 
				statement.setBinaryStream(2, is, (int) documentFile.getSize());
			statement.setString(3, myUserId);
			statement.setString(4, myIpAddress);
			int rowsUpdated = statement.executeUpdate();
			statement.close();
			return rowsUpdated;
        }
		finally
		{
			//fos.close();
			is.close();
		}
		
	}*/
	
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
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getRecordsPerPage() {
		return recordsPerPage;
	}
	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}
	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}
	public List<List2> getUserList() {
		return userList;
	}
	public void setUserList(List<List2> userList) {
		this.userList = userList;
	}
	public String getAssoName() {
		return assoName;
	}
	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}
	public String getAssoAddress() {
		return assoAddress;
	}
	public void setAssoAddress(String assoAddress) {
		this.assoAddress = assoAddress;
	}
	public String getTownCity() {
		return townCity;
	}
	public void setTownCity(String townCity) {
		this.townCity = townCity;
	}
	public String getAssoPincode() {
		return assoPincode;
	}
	public void setAssoPincode(String assoPincode) {
		this.assoPincode = assoPincode;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getComAddress() {
		return comAddress;
	}
	public void setComAddress(String comAddress) {
		this.comAddress = comAddress;
	}
	public String getComEmail() {
		return comEmail;
	}
	public void setComEmail(String comEmail) {
		this.comEmail = comEmail;
	}
	public String getComMobile() {
		return comMobile;
	}
	public void setComMobile(String comMobile) {
		this.comMobile = comMobile;
	}
	public String getComplaint() {
		return complaint;
	}
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCheckedvalue() {
		return checkedvalue;
	}
	public void setCheckedvalue(String checkedvalue) {
		this.checkedvalue = checkedvalue;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public MultipartFile getDocumentFile() {
		return documentFile;
	}
	public void setDocumentFile(MultipartFile documentFile) {
		this.documentFile = documentFile;
	}
	public List<KVPair<String, String>> getIbList() {
		return IbList;
	}
	public void setIbList(List<KVPair<String, String>> ibList) {
		IbList = ibList;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public String setApplicationId(String applicationId) {
		return this.applicationId = applicationId;
	}
	public String getOfficevalue() {
		return officevalue;
	}
	public void setOfficevalue(String officevalue) {
		this.officevalue = officevalue;
	}


}
