package service.services.dashboard;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.owasp.esapi.ESAPI;
import org.springframework.web.multipart.MultipartFile;

import dao.services.dashboard.ProjectDashboardDao;
import models.services.AssociationDetails;
import models.services.BankDetails;
import models.services.CommitteeMember;
import models.services.DonorDetails;
import models.services.RedFlagAssociations;
import models.services.RedFlagDonors;
import models.services.requests.AbstractRequest;
import models.services.requests.AnnualReturns;
import models.services.requests.ChangeOfDetails;
import models.services.requests.Chat;
import models.services.requests.Hospitality;
import models.services.requests.PriorPermission;
import models.services.requests.ProjectRequest;
import models.services.requests.Registration;
import models.services.requests.Renewal;
import utilities.Commons;
import utilities.InformationException;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.notifications.Closeable;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class ProjectDashboardService extends Commons{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String stage;
	private List<AbstractRequest> applicationList;
	private List<Chat> chatList;
	private List<Chat> chatAttachmentList;
	private List<List2> officeStatusList;
	private List<List2> officeMarkList;
	private List<List2> officeSectionList;
	private List<List2> officeUserList;
	private List<List2> officeServiceList;
	private List<List2> stateList;
	private List<Chat> olderSubStageList;
	private List<Chat> olderLatestSubStageList;
	private List<List2> forwardUserList;
	private List<List2> forwardOtherUserList=new ArrayList<List2>();;
	private List<List2> forwardOfficeList;
	private List<List2> nextStageForwardList;
	private List<List3> myDetails=new ArrayList<List3>();
	private List<List2> docList;
	private List<List2> uploadedDocList;
	private List<List3> projDocList;
	private String nextStageId;
	private String subStageId;
	private String appId;
	private String appName;
	private String forwardUser;
	private String forwardOffice;
	private String forwardAgent;
	private String statusRemark;
	private MultipartFile[] stageFiles;
	private String[] stageFilesIds;	
	private String state;
	private String service;
	private String officeUser;
	private String sectionId;
	private String daysCount;
	private String runningStatus;
	private String applicationFinalStatus;	
	private String grantStatus;
	private String deniedStatus;
	private String registrationNumber;
	private Boolean regSecStatus;
	private Boolean pdfStatus;
	private String hosPdfFormat;
	private String validityFrom;
	private String validityUpTo;
	private String currentDate;
	private byte[] pdfBytes;
	public List<Notification> emailNotifyList = new ArrayList<Notification>();
	private Boolean doExist;
	private Boolean doAccept;
	private Boolean withMe;
	private Boolean withMeToAnswer=false;
	private Boolean withMeAnswered=false;
	private Boolean withMeToMail=false;
	private Boolean notWithMe=false;
	private Boolean notWithMeToFetch=false;
	private String finalStatus=null;
	private Boolean mySection;
	private Object obj=null;
	private String currentUser;
	private String redFlag=null;
	private List<RedFlagAssociations> matchingRedFlagAssociationList = new ArrayList<RedFlagAssociations>();
	private List<RedFlagDonors> matchingRedFlagDonorList = new ArrayList<RedFlagDonors>();
	private String redFlagClearingRemarks;
	private String processingOfficialFlag;
	private String grantingOfficialFlag;
	private String clarificationOfficialFlag;
	private String clarificationReminderFlag;
	private String redFlagREDCategory;
	private String redFlagYELLOWCategory;
	private String noticeSubject;
	private String noticeBody;
	private String ppAmount;
	private String ppAmountCurrency;
	private String ppAmountDesc;
	private String installments;
	private String installmentNumbers;
	private String ppInsFlag;
	private Boolean markOffice;
	private String blueFlag;
	private String otherRemark;//For service id 01 
	public void initDashboardAction(){
		begin();
		try {
				populateDashboardAction();
				prepareBasicDetails();
				checkPendingMailCount();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	public void initDashboard(){
		begin();
		try {
				populateDashboard();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	public void initOfficeDashboard(){
		begin();
		try {
				populateOfficeDashboard();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	public void initApplicationListDetails(){
		begin();
		try {
				populateApplicationListDetails();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	public String initGetApplicationDetails(){
		begin();
		try {
				if(myOfficeId.equals("1") || myOfficeId.equals("0"))
					getApplicationDetails();
				else if(myOfficeId.equals("2") || myOfficeId.equals("3"))
					getApplicationDetailsForIBAndRAW();
			}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(InformationException ve){			
			try {
				notifyList.add(new Notification("INFO !!", ve.getMessage(), Status.WARNING, Type.BAR));		
				ProjectDashboardDao pdaoObj=(ProjectDashboardDao) obj;
				applicationList=pdaoObj.getApplicationList();
				doExist=pdaoObj.getDoExist();
				doAccept=pdaoObj.getDoAccept();
				withMe=pdaoObj.getWithMe();
				withMeAnswered=pdaoObj.getWithMeAnswered();
				withMeToAnswer=pdaoObj.getWithMeToAnswer();
				withMeToMail=pdaoObj.getWithMeToMail();
				mySection=pdaoObj.getMySection();	
				notWithMe=pdaoObj.getNotWithMe();
				notWithMeToFetch=pdaoObj.getNotWithMeTofetch();
				finalStatus=pdaoObj.getFinalStatus();
				markOffice = pdaoObj.getMarkOffice();
			} catch (NotificationException e) {				
				e.printStackTrace();
			}		
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}		
		return "success";
	}
	
	public void initChat(){
		begin();
		try {
				getChat();				
				getAdditionalResources();	
				prepareBasicDetails();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	public void initOfficeResources(){
		begin();
		try {
				getOfficeResources();				
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	public void initSubStageChat(){
		begin();
		try {
			//	getSubStageChat();				
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	private void prepareBasicDetails(){
		myDetails.add(new List3(myUserId, myOfficeCode, myOfficeId));
	}
	private void checkPendingMailCount() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		ProjectRequest pr=new ProjectRequest(connection);		
		pr.setCurrentStage("7");		
		pr.setToUser(myUserId);
		pr.setToOffice(myOfficeCode);
		pdd.getPendingMailRecords(pr);
		totalRecords=pdd.getTotalRecords();
	}
	public String initAcceptApplication(){
		begin();
		try {
				acceptApplication();
				notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been accecpted and forwarded to <b>"+forwardUser+"</b> "
						+ "successfully.",Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public String initPullApplication(){
		begin();
		try {
				pullApplication();	
				notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been fetched and forwarded to "
						+ "<b>"+forwardUser+"</b> successfully.",Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"fetchModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"fetchModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public String initSubmitForwardUserChatDetails(){
		begin();
		try {					
				submitForwardUserChatDetails();
				notifyList.add(new Notification("Success !!", "The Application <b>"+appId+"</b> has been forwarded to "
						+ "<b>"+forwardUser+"</b> successfully.",Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"userModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"userModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	public String initSubmitUserNoteChatDetails(){
		begin();
		try {					
				submitUserNoteChatDetails();
				notifyList.add(new Notification("Success !!", "The details for <b>"+appId+"</b> has been noted successfully.",
						Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"noteModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"noteModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	public String initSubmitUserClarificationChatDetails(){
		begin();
		try {					
				submitUserClarificationChatDetails();
				notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been sent to applicant for "
						+ "clarification.",Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"clarificationModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"clarificationModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	public String initSubmitShowCause(){
		begin();
		try {					
				submitShowCause();
				notifyList.add(new Notification("Success !!", "Show cause notice has been generated for Application <b>"+appId+"</b>.",
				Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"showCauseModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"showCauseModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	public String initSubmitUserOnHoldChatDetails(){
		begin();
		try {					
				submitUserOnHoldChatDetails();
				notifyList.add(new Notification("Success !!", "The details for application <b>"+appId+"</b> has been saved successfully.",
						Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"on-hold-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"clarificationModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public String initSubmitResumeProcessDetails(){
		begin();
		try {					
				submitResumeProcessDetails();
				notifyList.add(new Notification("Success !!", "The  process for application <b>"+appId+"</b> has been resumed successfully.",
						Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"resume-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"clarificationModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public String initSubmitForwardOfficeChatDetails(){
		begin();
		try {				
				submitForwardOfficeChatDetails();
				notifyList.add(new Notification("Success !!", "The application <b>"+appId+"</b> has been forwarded to <b>"+forwardOffice+"</b> "
						+ "successfully.", Status.SUCCESS, Type.BAR));
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"officeModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"officeModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public String initSubmitNextStage(){
		begin();
		try {
				submitNextStage();	
				notifyList.add(new Notification("Success !!", "Reply has been successfully sent to <b>"+forwardAgent+"</b>.", Status.SUCCESS, Type.BAR));				
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"nextStageModal-error"));
			} catch (NotificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"nextStageModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public String initSubmitReject(){
		begin();
		try {
				submitReject();				
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"rejectModal-error"));
			} catch (NotificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"rejectModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public String initSubmitClose(){
		begin();
		try {
				submitClose();				
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"closeModal-error"));
			} catch (NotificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"closeModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	public String initSubmitReminder(){
		begin();
		try {
				submitReminder();				
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"reminderModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"reminderModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	public String initSubmitApprove(){
		begin();
		try {
				submitApprove();				
		}catch(ValidationException ve){			
			try {
					ProjectDashboardDao pdaoObj=(ProjectDashboardDao) obj;
					redFlag=pdaoObj.getRedFlag();
					notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"nextStageModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"nextStageModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}
		return "success";
	}
	public String initDeleteUploadCache(){
		begin();
		try {
				deleteUploadCache();				
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}
		return "success";
	}
	public void initProjectDocuments(){
		begin();
		try {
				getProjectDocuments();				
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
	public void initSectionUsers(){
		begin();
		try {
				getSectionUsers();				
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
	
	public void initSectionFilteredService(){
		begin();
		try {
				getSectionFilteredService();				
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
	
	private void deleteUploadCache() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);		
		pdd.deleteUploadCache(sessId);	
	}
	
	private void getSectionFilteredService() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		officeServiceList=pdd.getUserServiceList(myOfficeCode,myUserId,sectionId);	
	}
	
	public void populateDashboardAction() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		officeSectionList=pdd.getUserSectionList(myOfficeCode,myUserId);	
		officeServiceList=pdd.getUserServiceList(myOfficeCode,myUserId,null);
		stateList=pdd.getStateList();
		officeUserList=pdd.getOfficeUserList(myOfficeCode);
	}
	
	private void getProjectDocuments() throws Exception{
		Chat chat=new Chat();		
		chat.setApplicationId(appId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		projDocList=pdd.getProjectDocuments(chat);
	}
	private void acceptApplication() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		if(ESAPI.validator().isValidInput("forwardUser", forwardUser, "Word", 10, false) == false){
			throw new ValidationException("Invalid User.");
		}
		Chat chat=new Chat();		
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);
		chat.setToOfficeCode(myOfficeCode);
		chat.setToOfficeId(myOfficeId);
		chat.setToUserId(forwardUser);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.acceptApplication(chat);		
	}
	
	private void pullApplication() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		if(ESAPI.validator().isValidInput("forwardUser", forwardUser, "Word", 10, false) == false){
			throw new ValidationException("Invalid User.");
		}
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}	
		Chat chat=new Chat();		
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);
		chat.setFromOfficeId(myOfficeId);
		chat.setToOfficeCode(myOfficeCode);
		chat.setToUserId(forwardUser);
		chat.setStatusId("7");
		chat.setRecordStatus("0");
		chat.setStatusRemark(statusRemark);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.pullApplication(chat);		
	}
	
	
	private void submitForwardUserChatDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		if(ESAPI.validator().isValidInput("forwardUser", forwardUser, "Word", 10, false) == false){
			throw new ValidationException("Invalid User.");
		}
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromOfficeId(myOfficeId);
		chat.setFromUserId(myUserId);
		chat.setToOfficeCode(myOfficeCode);
		chat.setToUserId(forwardUser);
		chat.setStatusRemark(statusRemark);
		chat.setSectionId(sectionId);
		chat.setStatusId("7");
		chat.setRecordStatus("0");
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		chat.setSessionId(sessId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.submitForwardUserChatDetails(chat);		
	}
	private void submitUserNoteChatDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}		
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);
		chat.setToOfficeCode(myOfficeCode);
		chat.setFromOfficeId(myOfficeId);
		chat.setToUserId(myUserId);
		chat.setStatusRemark(statusRemark);
		chat.setStatusId("7");
		chat.setRecordStatus("0");
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		chat.setSessionId(sessId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.submitUserNoteChatDetails(chat);		
	}
	private void submitUserClarificationChatDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}		
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);	
		chat.setFromOfficeId(myOfficeId);
		chat.setStatusRemark(statusRemark);		
		chat.setStatusId("8");
		chat.setRecordStatus("0");
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		chat.setSessionId(sessId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.submitUserClarificationChatDetails(chat);		
	}	
	private void submitShowCause() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("noticeSubject", noticeSubject, "WordSS", 500, false) == false){
			throw new ValidationException("Invalid Subject.Only 500 characters are allowed.");
		}
		if(ESAPI.validator().isValidInput("noticeBody", noticeBody, "WordSS", 3000, false) == false){
			throw new ValidationException("Invalid body.Only 3000 characters are allowed.");
		}
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);
		chat.setToOfficeCode(myOfficeCode);
		chat.setFromOfficeId(myOfficeId);
		chat.setToUserId(myUserId);
		chat.setStatusRemark("Show cause notice generated.");
		chat.setNoticeSubject(noticeSubject);
		chat.setNoticeBody(noticeBody);
		chat.setStatusId("13");
		chat.setRecordStatus("0");
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		chat.setSessionId(sessId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.submitShowCauseDetails(chat);		
	}
	private void submitReminder() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}				
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		pdd.initClarificationReminder(appId,statusRemark,myOfficeCode,myUserId,myOfficeId,sessId);
		notifyList.add(new Notification("Success !!", "Reminder for clarification asked for Application <b>"+appId+"</b> has been sent to applicant."
		,Status.SUCCESS, Type.BAR));
	}
	private void submitUserOnHoldChatDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}		
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);
		chat.setToOfficeCode(myOfficeCode);
		chat.setFromOfficeId(myOfficeId);
		chat.setToUserId(myUserId);
		chat.setStatusRemark(statusRemark);
		chat.setStatusId("11");
		chat.setRecordStatus("0");
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		chat.setSessionId(sessId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.submitUserOnHoldChatDetails(chat);		
	}
	private void submitResumeProcessDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}		
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);
		chat.setFromOfficeId(myOfficeId);
		chat.setToOfficeCode(myOfficeCode);
		chat.setToUserId(myUserId);
		chat.setStatusRemark(statusRemark);
		chat.setStatusId("7");
		chat.setRecordStatus("0");
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		chat.setSessionId(sessId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.submitResumeProcessChatDetails(chat);		
	}
	private void submitForwardOfficeChatDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		if(ESAPI.validator().isValidInput("forwardOffice", forwardOffice, "Word", 5, false) == false){
			throw new ValidationException("Invalid User.");
		}
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}		
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);	
		chat.setFromOfficeId(myOfficeId);
		chat.setToOfficeCode(forwardOffice);
		chat.setStatusRemark(statusRemark);
		chat.setStatusId("7");
		chat.setRecordStatus("0");
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		chat.setSessionId(sessId);
		chat.setStageFiles(stageFiles);
		chat.setStageFilesIds(stageFilesIds);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		pdd.submitForwardOfficeChatDetails(chat);
					
	}
	
	private void submitNextStage() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		if(ESAPI.validator().isValidInput("forwardOffice", forwardAgent, "Word", 10, false) == false){
			throw new ValidationException("Invalid User.");
		}
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}		
		Chat chat=new Chat();		
		chat.setApplicationId(appId);
		chat.setFromOfficeId(myOfficeId);
		chat.setFromOfficeCode(myOfficeCode);		
		chat.setFromUserId(myUserId);
		chat.setNextStageId(nextStageId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		Boolean flag=pdd.checkFlag(nextStageId,appId,myOfficeId);
		if(flag==true){
			chat.setToOfficeCode(myOfficeCode);
			chat.setToUserId(forwardAgent);
		}else{
			chat.setToOfficeCode(forwardAgent);
		}		
		chat.setStatusRemark(statusRemark);
		chat.setStatusId("7");
		chat.setRecordStatus("0");	
		chat.setStageFiles(stageFiles);
		chat.setStageFilesIds(stageFilesIds);
		pdd.submitNextStage(chat);	
	}
	
	public String getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(String daysCount) {
		this.daysCount = daysCount;
	}

	public String getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(String runningStatus) {
		this.runningStatus = runningStatus;
	}

	private void submitReject() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);	
		chat.setFromOfficeId(myOfficeId);
		chat.setStatusRemark(statusRemark);		
		chat.setRecordStatus("0");		
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		if(myOfficeId.equals("1")){
			 chat.setStatusId("10");
			 chat.setToOfficeId("1");
			 pdd.submitDenied(chat);
			 pdfStatus=pdd.getPdfStatus();
			 if(pdfStatus==true){
					notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been denied. Details has been sent to applicant.",
					Status.SUCCESS, Type.BAR));			
					deniedStatus="Y";					
			}
			else{				
				notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been denied.Details has been sent to applicant.",
						Status.SUCCESS, Type.BAR));
				emailNotifyList=pdd.getNotifyList();
			}		
		}
		else if(myOfficeId.equals("2") || myOfficeId.equals("3")){
			chat.setStatusId("6");
			chat.setToOfficeId("1");
			pdd.submitReject(chat);	
			notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been rejected.", 
					Status.SUCCESS, Type.BAR));
		}						
	}
	
	private void submitClose() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);		
		chat.setFromOfficeId(myOfficeId);
		chat.setStatusRemark(statusRemark);
		chat.setStatusId("12");
		chat.setRecordStatus("0");		
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);		
		chat.setToOfficeId("1");
		if(myOfficeId.equals("1")){
			pdd.submitClose(chat);
		}else if(myOfficeId.equals("2") || myOfficeId.equals("3")){
			pdd.submitCloseForIBRAW(chat);
		}
		notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been closed.", Status.SUCCESS, Type.BAR));							
	}
	
	
	
	private void submitApprove() throws Exception{		
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}
		if(ESAPI.validator().isValidInput("otherRemark", otherRemark, "WordSS", 1000, true) == false){
			throw new ValidationException("Invalid Other Remark.Only 1000 characters are allowed.");
		}
		Chat chat=new Chat();		
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);	
		chat.setFromOfficeId(myOfficeId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);	
		obj=pdd;
		String svcCode=pdd.getServiceCode(appId);
		
		if(svcCode.equals("03")){
			if(ESAPI.validator().isValidInput("validityFrom", validityFrom, "Date", 10, false) == false){				
				throw new ValidationException("Invalid Date.Invalid date.");
			}			
		}else if(svcCode.equals("02")){
			if(ppInsFlag.equals("YES")){
				if(installmentNumbers!=null && (Integer.parseInt(installmentNumbers)>0 && Integer.parseInt(installmentNumbers)<=10)){
					if(installments == null)
						throw new ValidationException("Invalid installments.");
				}
			}
		}
		// Check for red flag list and if there is any case, then redflag clearing remark is mandatory
		//---------------------------------------------------------------------------------------------
		matchingRedFlagAssociationList = pdd.getRedFlagStatusByName(appId);
		matchingRedFlagDonorList = pdd.getRedFlagStatusByDonorName(appId);
		if(matchingRedFlagAssociationList.size() > 0 || matchingRedFlagDonorList.size() > 0) {
			if(ESAPI.validator().isValidInput("redFlagClearingRemarks", redFlagClearingRemarks, "WordSS", 2000, false) == false){
				throw new ValidationException("Remarks on Red Flag clearance is mandatory as there are matching elements in red flag list.Only 2000 characters are allowed.");
			}				
		}
		pdd.setRedFlagClearingRemarks(redFlagClearingRemarks);
		//--------------------------------------------------------------------------------------------
		chat.setStatusRemark(statusRemark);
		chat.setOtherRemark(otherRemark);
		chat.setRecordStatus("0");	
		if(myOfficeId.equals("1")){
			chat.setStatusId("9");
			chat.setToOfficeId("1");
			pdd.setHosPdfFormat(hosPdfFormat);
			pdd.setValidityFrom(validityFrom);	
			pdd.setInstallments(installments);
			pdd.setInstallmentNumbers(installmentNumbers);
			pdd.setPpInsFlag(ppInsFlag);
			pdd.submitGrant(chat);
			regSecStatus=pdd.getRegSecStatus();
			pdfStatus=pdd.getPdfStatus();
			
			if(regSecStatus==true){
				notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been granted with <b>Reg.No. "+pdd.getRegistrationNumber()+"</b>. "
						+ "Details has been sent to applicant.",Status.SUCCESS, Type.BAR));			
				grantStatus="Y";
				registrationNumber=pdd.getRegistrationNumber();				
			}else if(pdfStatus==true){
				notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been granted. Details has been sent to applicant.",
				Status.SUCCESS, Type.BAR));			
				grantStatus="Y";				
			}
			else{				
				notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been granted.Details has been sent to applicant.",
						Status.SUCCESS, Type.BAR));
				emailNotifyList=pdd.getNotifyList();
			}			
		}
		else if(myOfficeId.equals("2") || myOfficeId.equals("3")){
			chat.setStatusId("5");
			chat.setToOfficeId("1");
			pdd.submitApprove(chat);
			notifyList.add(new Notification("Success !!", "Application <b>"+appId+"</b> has been approved.", 
					Status.SUCCESS, Type.BAR));
		}						
	}
	
	private void getApplicationDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);	
		obj=pdd;
		pdd.getApplicationDetails(appId,myOfficeCode,myUserId);	
		applicationList=pdd.getApplicationList();
		doExist=pdd.getDoExist();
		doAccept=pdd.getDoAccept();
		withMe=pdd.getWithMe();
		withMeAnswered=pdd.getWithMeAnswered();
		withMeToAnswer=pdd.getWithMeToAnswer();
		withMeToMail=pdd.getWithMeToMail();
		notWithMe=pdd.getNotWithMe();
		notWithMeToFetch=pdd.getNotWithMeTofetch();
		finalStatus=pdd.getFinalStatus();
		mySection=pdd.getMySection();
	}
	
	private void getApplicationDetailsForIBAndRAW() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);	
		obj=pdd;
		pdd.getApplicationDetailsForIBAndRAW(appId,myOfficeCode,myUserId);	
		applicationList=pdd.getApplicationList();
		doExist=pdd.getDoExist();
		doAccept=pdd.getDoAccept();
		withMe=pdd.getWithMe();
		withMeAnswered=pdd.getWithMeAnswered();
		withMeToAnswer=pdd.getWithMeToAnswer();
		withMeToMail=pdd.getWithMeToMail();
		notWithMe=pdd.getNotWithMe();
		notWithMeToFetch=pdd.getNotWithMeTofetch();
		finalStatus=pdd.getFinalStatus();
		mySection=pdd.getMySection();
	}
	
	private void getChat() throws Exception{
		ProjectRequest pr=new ProjectRequest(connection);		
		pr.setApplicationId(appId);
		pr.setOfficeId(myOfficeId);
		pr.setUserId(myUserId);
		pr.setUserDesignationId(myUserDesignationId);
	
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		chatList=pdd.getChat(pr);		
		chatAttachmentList=pdd.getChatAttachments(pr);
	}
	
	private void getOfficeResources() throws Exception{		
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		if(myOfficeId.equals("1"))
			forwardUserList=pdd.forwardUserList(myOfficeCode,"2",myUserId,appId);
		else if(myOfficeId.equals("2"))
			forwardUserList=pdd.forwardUserList(myOfficeCode,"4",myUserId,appId);
		else if(myOfficeId.equals("3"))
			forwardUserList=pdd.forwardUserList(myOfficeCode,"6",myUserId,appId);
		
		ProjectRequest pr=new ProjectRequest(connection);		
		pr.setApplicationId(appId);
		pr.setOfficeId(myOfficeId);
		pr.setUserId(myUserId);
		chatList=pdd.getChat(pr);		
		chatAttachmentList=pdd.getChatAttachments(pr);
	}
	
/*	private void getSubStageChat() throws Exception{
		Chat chat=new Chat();
		chat.setApplicationId(appId);
		chat.setSubStageId(subStageId);
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		chatList=pdd.getSubStageChat(chat);	
		chatAttachmentList=pdd.getOlderChatAttachments(chat);
	}*/
	
	private void getAdditionalResources() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		ProjectRequest pr=new ProjectRequest(connection);		
		pr.setApplicationId(appId);
		pr.setToOffice(myOfficeId);
		pr.setToOfficeInfo(myOfficeCode);		
		if(myOfficeId.equals("1")){
			officeStatusList=pdd.officeStatusList(pr);
			officeMarkList = pdd.officeMarkList(pr);
			forwardUserList=pdd.forwardUserList(myOfficeCode,"2",myUserId);
		}
		else if(myOfficeId.equals("2"))
			forwardUserList=pdd.forwardUserList(myOfficeCode,"4",myUserId);
		else if(myOfficeId.equals("3"))
			forwardUserList=pdd.forwardUserList(myOfficeCode,"6",myUserId);
		
		int i=0;	
		if(forwardUserList!=null){
			while(i<forwardUserList.size()){
				if(!(forwardUserList.get(i).getLi().equals(myUserId)))
					forwardOtherUserList.add(forwardUserList.get(i));
				i++;
			}	
		}			
		forwardOfficeList=pdd.forwardOfficeList(pr); 			// GETTING OFFICE LIST		
		officeSectionList=pdd.getOfficeSectionList(myOfficeCode,myUserId);		
		pdd.getTimingInfo(pr);		
		daysCount=pdd.getDaysCount();
		runningStatus=pdd.getRunningStatus();	
		applicationFinalStatus=pdd.getAppFinalStatus();
		ppAmount=pdd.getPpAmount();
		ppAmountDesc=pdd.getPpAmountDesc();
		ppAmountCurrency=pdd.getPpAmountCurrency();
		pdd.getUserRoleInfo(myUserId,myOfficeId); 									// Getting User Role
		processingOfficialFlag=pdd.getProcessingOfficialFlag();
		grantingOfficialFlag=pdd.getGrantingOfficalFlag();
		clarificationOfficialFlag=pdd.getClarificationOfficialFlag();
		validityFrom=pdd.getValidityFrom();
		validityUpTo=pdd.getValidityUpTo();
		currentDate=pdd.getCurrentDate();
		currentUser=pdd.getCurrentUser(appId, myOfficeCode);
		pdd.getRedFlagStatus(appId);
		redFlag=pdd.getRedFlag();
		pdd.getBlueFlagStatus(appId);
		blueFlag=pdd.getBlueFlag();
		redFlagREDCategory=pdd.getRedFlagREDCategory();
		redFlagYELLOWCategory=pdd.getRedFlagYELLOWCategory();		
		if(redFlag==null){			
		}
		else if(redFlag.equals("YES")){
			if(redFlagREDCategory!=null && redFlagREDCategory.equals("YES")){
				if(pdd.getRedFlagCategoryCode()==null || !(pdd.getRedFlagCategoryCode().equals("9"))){
					notifyList.add(new Notification("Warning !!", "This association belongs to <b><span class='blink_me_red'>RED FLAGGED ASSOCIATIONS</span></b>.Please proceed accordingly."
							,	Status.ERROR, Type.BAR));
				}else{
					notifyList.add(new Notification("Warning !!", "This association belongs to <b><span class='blink_me_red'>RED FLAGGED ASSOCIATIONS</span></b> under <b><i><span class='text-primary'>"+pdd.getRedFlagCategory().toUpperCase()+"</span></i></b> Category."
							,	Status.ERROR, Type.BAR));
				}	
			}else if(redFlagYELLOWCategory!=null && redFlagYELLOWCategory.equals("YES")){
				if(pdd.getRedFlagCategoryCode()==null || !(pdd.getRedFlagCategoryCode().equals("9"))){
					notifyList.add(new Notification("Warning !!", "This association belongs to <b><span class='blink_me_yellow'>YELLOW FLAGGED ASSOCIATIONS</span></b>.Please proceed accordingly."
							,	Status.WARNING, Type.BAR));
				}else{
					notifyList.add(new Notification("Warning !!", "This association belongs to <b><span class='blink_me_yellow'>YELLOW FLAGGED ASSOCIATIONS</span></b> under <b><i><span class='text-primary'>"+pdd.getRedFlagCategory().toUpperCase()+"</span></i></b> Category."
							,	Status.WARNING, Type.BAR));
				}	
			}
			
		}
		if(blueFlag!=null && blueFlag.equalsIgnoreCase("Yes")){
			notifyList.add(new Notification("Warning !!", "This association belongs to <b><span class='blink_me_blue'>BLUE FLAGGED ASSOCIATIONS</span></b>.Please proceed accordingly."
					,	Status.INFORMATION, Type.BAR));
			
		}
		matchingRedFlagAssociationList = pdd.getRedFlagStatusByName(appId);
		matchingRedFlagDonorList = pdd.getRedFlagStatusByDonorName(appId);
		clarificationReminderFlag=pdd.checkClarificationReminderStatus(appId,myOfficeId);
	}
	
	private void getSectionUsers() throws Exception{	
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		if(myOfficeId.equals("1"))		
			forwardUserList=pdd.sectionUserList(myOfficeCode,"2",sectionId);		
		else if(myOfficeId.equals("2"))
			forwardUserList=pdd.sectionUserList(myOfficeCode,"4",sectionId);
		else if(myOfficeId.equals("3"))
			forwardUserList=pdd.sectionUserList(myOfficeCode,"6",sectionId);
		
		int i=0;		
		while(i<forwardUserList.size()){
			if(!(forwardUserList.get(i).getLi().equals(myUserId)))
				forwardOtherUserList.add(forwardUserList.get(i));
			i++;
		}	
	}
	
	private void populateDashboard() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		ProjectRequest pr=new ProjectRequest(connection);		
		pr.setCurrentStage(stage);
		pr.setState(state);
		pr.setService(service);
		pr.setOfficeUser(officeUser);
		Boolean sectionCheck=pdd.checkUserSection(myUserId,myOfficeCode);
		if(sectionCheck==false)
			pr.setSection("NS");
		else
			pr.setSection(sectionId);
		pr.setToUser(myUserId);
		pr.setToOffice(myOfficeCode);		
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		applicationList=pdd.getDashboard(pr);
		totalRecords=pdd.getTotalRecords();
	}
	private void populateOfficeDashboard() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);
		ProjectRequest pr=new ProjectRequest(connection);		
		pr.setCurrentStage(stage);
		pr.setState(state);
		pr.setService(service);
		pr.setOfficeUser(officeUser);
		Boolean sectionCheck=pdd.checkUserSection(myUserId,myOfficeCode);
		if(sectionCheck==false)
			pr.setSection("NS");
		else
			pr.setSection(sectionId);
		pr.setToUser(myUserId);
		pr.setToOffice(myOfficeCode);		
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		if(stage.equals("1") && myOfficeId.equals("1"))
			applicationList=pdd.getFreshOfficeDashboard(pr);
		else
			applicationList=pdd.getOfficeDashboard(pr);
		totalRecords=pdd.getTotalRecords();
	}
	
	private void populateApplicationListDetails() throws Exception{
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);						
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		if(nob(appId)){
			pdd.setSearchFlag("name");
			pdd.setSearchString(appName);
		}			
		else{
			pdd.setSearchFlag("id");
			pdd.setSearchString(appId);
		}					
		applicationList=pdd.getApplicationListDetails();
		totalRecords=pdd.getTotalRecords();
	}

	public String initSubmitMarkOffice(String markOfficeId) {
		begin();
		try {
				submitMarkOffice(markOfficeId);				
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"markOfficeModal-error"));
			} catch (NotificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"markOfficeModal-error"));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void submitMarkOffice(String markOfficeId) throws Exception{
		// TODO Auto-generated method stub
		Chat chat=new Chat();		
		chat.setApplicationId(appId);
		chat.setFromOfficeCode(myOfficeCode);
		chat.setFromUserId(myUserId);
		//chat.setToOfficeCode(myOfficeCode);
		chat.setToOfficeId(markOfficeId);
	//	chat.setToUserId(forwardUser);
		chat.setStatusId("7");
		chat.setStatusRemark(statusRemark);
		chat.setRecordStatus("0");
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);						
		pdd.markToOffice(chat);
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

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		if(appId!=null)
			this.appId = appId.trim();
	}

	public List<Chat> getChatList() {
		return chatList;
	}

	public void setChatList(List<Chat> chatList) {
		this.chatList = chatList;
	}

	public List<List2> getForwardUserList() {
		return forwardUserList;
	}

	public void setForwardUserList(List<List2> forwardUserList) {
		this.forwardUserList = forwardUserList;
	}

	public List<List2> getForwardOfficeList() {
		return forwardOfficeList;
	}

	public void setForwardOfficeList(List<List2> forwardOfficeList) {
		this.forwardOfficeList = forwardOfficeList;
	}

	public String getForwardUser() {
		return forwardUser;
	}

	public void setForwardUser(String forwardUser) {
		this.forwardUser = forwardUser;
	}

	public String getStatusRemark() {
		return statusRemark;
	}

	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}

	public String getForwardOffice() {
		return forwardOffice;
	}

	public void setForwardOffice(String forwardOffice) {
		this.forwardOffice = forwardOffice;
	}

	public List<List2> getNextStageForwardList() {
		return nextStageForwardList;
	}

	public void setNextStageForwardList(List<List2> nextStageForwardList) {
		this.nextStageForwardList = nextStageForwardList;
	}

	public List<List2> getDocList() {
		return docList;
	}

	public void setDocList(List<List2> docList) {
		this.docList = docList;
	}

	public String getNextStageId() {
		return nextStageId;
	}

	public void setNextStageId(String nextStageId) {
		this.nextStageId = nextStageId;
	}

	public MultipartFile[] getStageFiles() {
		return stageFiles;
	}

	public void setStageFiles(MultipartFile[] stageFiles) {
		this.stageFiles = stageFiles;
	}

	public String[] getStageFilesIds() {
		return stageFilesIds;
	}

	public void setStageFilesIds(String[] stageFilesIds) {
		this.stageFilesIds = stageFilesIds;
	}

	public String getForwardAgent() {
		return forwardAgent;
	}

	public void setForwardAgent(String forwardAgent) {
		this.forwardAgent = forwardAgent;
	}

	public List<List3> getMyDetails() {
		return myDetails;
	}

	public void setMyDetails(List<List3> myDetails) {
		this.myDetails = myDetails;
	}

	public List<Chat> getOlderSubStageList() {
		return olderSubStageList;
	}

	public void setOlderSubStageList(List<Chat> olderSubStageList) {
		this.olderSubStageList = olderSubStageList;
	}

	public String getSubStageId() {
		return subStageId;
	}

	public void setSubStageId(String subStageId) {
		this.subStageId = subStageId;
	}
	public List<Chat> getChatAttachmentList() {
		return chatAttachmentList;
	}
	public void setChatAttachmentList(List<Chat> chatAttachmentList) {
		this.chatAttachmentList = chatAttachmentList;
	}
	public List<List2> getForwardOtherUserList() {
		return forwardOtherUserList;
	}
	public void setForwardOtherUserList(List<List2> forwardOtherUserList) {
		this.forwardOtherUserList = forwardOtherUserList;
	}
	public List<List2> getUploadedDocList() {
		return uploadedDocList;
	}
	public void setUploadedDocList(List<List2> uploadedDocList) {
		this.uploadedDocList = uploadedDocList;
	}
	
	public void setProjDocList(List<List3> projDocList) {
		this.projDocList = projDocList;
	}
	public List<List3> getProjDocList() {
		return projDocList;
	}
	
	public List<Chat> getOlderLatestSubStageList() {
		return olderLatestSubStageList;
	}
	public void setOlderLatestSubStageList(List<Chat> olderLatestSubStageList) {
		this.olderLatestSubStageList = olderLatestSubStageList;
	}
	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}
	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}


	public List<List2> getOfficeStatusList() {
		return officeStatusList;
	}


	public void setOfficeStatusList(List<List2> officeStatusList) {
		this.officeStatusList = officeStatusList;
	}


	public List<List2> getOfficeSectionList() {
		return officeSectionList;
	}


	public void setOfficeSectionList(List<List2> officeSectionList) {
		this.officeSectionList = officeSectionList;
	}


	public String getSectionId() {
		return sectionId;
	}


	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public List<List2> getStateList() {
		return stateList;
	}

	public void setStateList(List<List2> stateList) {
		this.stateList = stateList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getApplicationFinalStatus() {
		return applicationFinalStatus;
	}

	public void setApplicationFinalStatus(String applicationFinalStatus) {
		this.applicationFinalStatus = applicationFinalStatus;
	}

	public String getGrantStatus() {
		return grantStatus;
	}

	public void setGrantStatus(String grantStatus) {
		this.grantStatus = grantStatus;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Boolean getRegSecStatus() {
		return regSecStatus;
	}

	public void setRegSecStatus(Boolean regSecStatus) {
		this.regSecStatus = regSecStatus;
	}

	public byte[] getPdfBytes() {
		return pdfBytes;
	}

	public void setPdfBytes(byte[] pdfBytes) {
		this.pdfBytes = pdfBytes;
	}

	public List<Notification> getEmailNotifyList() {
		return emailNotifyList;
	}

	public void setEmailNotifyList(List<Notification> emailNotifyList) {
		this.emailNotifyList = emailNotifyList;
	}

	public Boolean getPdfStatus() {
		return pdfStatus;
	}

	public void setPdfStatus(Boolean pdfStatus) {
		this.pdfStatus = pdfStatus;
	}

	public String getHosPdfFormat() {
		return hosPdfFormat;
	}

	public void setHosPdfFormat(String hosPdfFormat) {
		this.hosPdfFormat = hosPdfFormat;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public List<List2> getOfficeServiceList() {
		return officeServiceList;
	}

	public void setOfficeServiceList(List<List2> officeServiceList) {
		this.officeServiceList = officeServiceList;
	}

	public String getValidityFrom() {
		return validityFrom;
	}

	public void setValidityFrom(String validityFrom) {
		this.validityFrom = validityFrom;
	}

	public String getValidityUpTo() {
		return validityUpTo;
	}

	public void setValidityUpTo(String validityUpTo) {
		this.validityUpTo = validityUpTo;
	}

	public String getDeniedStatus() {
		return deniedStatus;
	}

	public void setDeniedStatus(String deniedStatus) {
		this.deniedStatus = deniedStatus;
	}

	public Boolean getDoAccept() {
		return doAccept;
	}

	public void setDoAccept(Boolean doAccept) {
		this.doAccept = doAccept;
	}

	public Boolean getWithMe() {
		return withMe;
	}

	public void setWithMe(Boolean withMe) {
		this.withMe = withMe;
	}

	public Boolean getMySection() {
		return mySection;
	}

	public void setMySection(Boolean mySection) {
		this.mySection = mySection;
	}

	public Boolean getDoExist() {
		return doExist;
	}

	public void setDoExist(Boolean doExist) {
		this.doExist = doExist;
	}

	public Boolean getWithMeToAnswer() {
		return withMeToAnswer;
	}

	public void setWithMeToAnswer(Boolean withMeToAnswer) {
		this.withMeToAnswer = withMeToAnswer;
	}

	public Boolean getWithMeAnswered() {
		return withMeAnswered;
	}

	public void setWithMeAnswered(Boolean withMeAnswered) {
		this.withMeAnswered = withMeAnswered;
	}

	public Boolean getWithMeToMail() {
		return withMeToMail;
	}

	public void setWithMeToMail(Boolean withMeToMail) {
		this.withMeToMail = withMeToMail;
	}

	public Boolean getNotWithMe() {
		return notWithMe;
	}

	public void setNotWithMe(Boolean notWithMe) {
		this.notWithMe = notWithMe;
	}

	public Boolean getNotWithMeToFetch() {
		return notWithMeToFetch;
	}

	public void setNotWithMeToFetch(Boolean notWithMeToFetch) {
		this.notWithMeToFetch = notWithMeToFetch;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getRedFlag() {
		return redFlag;
	}

	public void setRedFlag(String redFlag) {
		this.redFlag = redFlag;
	}

	public List<RedFlagAssociations> getMatchingRedFlagAssociationList() {
		return matchingRedFlagAssociationList;
	}

	public void setMatchingRedFlagAssociationList(
			List<RedFlagAssociations> matchingRedFlagAssociationList) {
		this.matchingRedFlagAssociationList = matchingRedFlagAssociationList;
	}

	public List<RedFlagDonors> getMatchingRedFlagDonorList() {
		return matchingRedFlagDonorList;
	}

	public void setMatchingRedFlagDonorList(
			List<RedFlagDonors> matchingRedFlagDonorList) {
		this.matchingRedFlagDonorList = matchingRedFlagDonorList;
	}

	public String getRedFlagClearingRemarks() {
		return redFlagClearingRemarks;
	}

	public void setRedFlagClearingRemarks(String redFlagClearingRemarks) {
		this.redFlagClearingRemarks = redFlagClearingRemarks;
	}

	public String getProcessingOfficialFlag() {
		return processingOfficialFlag;
	}

	public void setProcessingOfficialFlag(String processingOfficialFlag) {
		this.processingOfficialFlag = processingOfficialFlag;
	}

	public String getGrantingOfficialFlag() {
		return grantingOfficialFlag;
	}

	public void setGrantingOfficialFlag(String grantingOfficialFlag) {
		this.grantingOfficialFlag = grantingOfficialFlag;
	}

	public String getClarificationReminderFlag() {
		return clarificationReminderFlag;
	}

	public void setClarificationReminderFlag(String clarificationReminderFlag) {
		this.clarificationReminderFlag = clarificationReminderFlag;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getRedFlagREDCategory() {
		return redFlagREDCategory;
	}

	public void setRedFlagREDCategory(String redFlagREDCategory) {
		this.redFlagREDCategory = redFlagREDCategory;
	}

	public String getRedFlagYELLOWCategory() {
		return redFlagYELLOWCategory;
	}

	public void setRedFlagYELLOWCategory(String redFlagYELLOWCategory) {
		this.redFlagYELLOWCategory = redFlagYELLOWCategory;
	}

	public String getNoticeSubject() {
		return noticeSubject;
	}

	public void setNoticeSubject(String noticeSubject) {
		this.noticeSubject = noticeSubject;
	}

	public String getNoticeBody() {
		return noticeBody;
	}

	public void setNoticeBody(String noticeBody) {
		this.noticeBody = noticeBody;
	}

	public String getPpAmount() {
		return ppAmount;
	}

	public void setPpAmount(String ppAmount) {
		this.ppAmount = ppAmount;
	}

	public String getPpAmountDesc() {
		return ppAmountDesc;
	}

	public void setPpAmountDesc(String ppAmountDesc) {
		this.ppAmountDesc = ppAmountDesc;
	}

	public String getPpAmountCurrency() {
		return ppAmountCurrency;
	}

	public void setPpAmountCurrency(String ppAmountCurrency) {
		this.ppAmountCurrency = ppAmountCurrency;
	}

	public String getInstallments() {
		return installments;
	}

	public void setInstallments(String installments) {
		this.installments = installments;
	}

	public String getInstallmentNumbers() {
		return installmentNumbers;
	}

	public void setInstallmentNumbers(String installmentNumbers) {
		this.installmentNumbers = installmentNumbers;
	}

	public String getPpInsFlag() {
		return ppInsFlag;
	}

	public void setPpInsFlag(String ppInsFlag) {
		this.ppInsFlag = ppInsFlag;
	}

	public List<List2> getOfficeMarkList() {
		return officeMarkList;
	}
	
	public List<List2> getOfficeUserList() {
		return officeUserList;
	}

	public void setOfficeUserList(List<List2> officeUserList) {
		this.officeUserList = officeUserList;
	}

	public String getOfficeUser() {
		return officeUser;
	}

	public void setOfficeUser(String officeUser) {
		this.officeUser = officeUser;
	}

	public String getClarificationOfficialFlag() {
		return clarificationOfficialFlag;
	}

	public void setClarificationOfficialFlag(String clarificationOfficialFlag) {
		this.clarificationOfficialFlag = clarificationOfficialFlag;
	}

	

	public void setOfficeMarkList(List<List2> officeMarkList) {
		this.officeMarkList = officeMarkList;
	}

	public Boolean getMarkOffice() {
		return markOffice;
	}

	public void setMarkOffice(Boolean markOffice) {
		this.markOffice = markOffice;
	}


	public String getBlueFlag() {
		return blueFlag;
	}

	public void setBlueFlag(String blueFlag) {
		this.blueFlag = blueFlag;
	}


	public String getOtherRemark() {
		return otherRemark;
	}

	public void setOtherRemark(String otherRemark) {
		this.otherRemark = otherRemark;
	}

}