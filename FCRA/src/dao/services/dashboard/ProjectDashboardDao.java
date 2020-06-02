package dao.services.dashboard;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.owasp.esapi.ESAPI;
import org.springframework.util.StringUtils;

import service.services.dashboard.reports.DashboardReportService;
import utilities.InformationException;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.communication.AutoNotifier;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import models.master.User;
import models.services.Grievance;
import models.services.RedFlagAssociations;
import models.services.RedFlagDonors;
import models.services.requests.AbstractRequest;
import models.services.requests.Chat;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class ProjectDashboardDao extends BaseDao<ProjectRequest, String, String>{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String applicationId;
	private String applicationName;
	private String searchString;
	private String searchFlag;
	List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
	private List<List2> docList=new ArrayList<List2>();
	private List<List2> uploadedDocList=new ArrayList<List2>();
	private List<List2> nextStageForwardList=new ArrayList<List2>();
	
	private String nextStageId;
	private String daysCount;
	private String runningStatus;
	private String appFinalStatus;
	private String registrationNumber=null;
	private Boolean regSecStatus=false;
	private Boolean pdfStatus=false;
	private String  hosPdfFormat;
	private byte[] pdfBytes;		
	private String validityFrom;
	private String validityUpTo;
	private String currentDate;
	public List<Notification> notifyList = new ArrayList<Notification>();
	private Boolean doExist=false;
	private Boolean doAccept=false;
	private Boolean withMe=false;	
	private Boolean withMeToAnswer=false;
	private Boolean withMeAnswered=false;
	private Boolean withMeToMail=false;
	private Boolean mySection=false;
	private Boolean notWithMe=false;
	private Boolean notWithMeTofetch=false;
	private String finalStatus=null;
	private String redFlagClearingRemarks;
	private String redFlag;
	private String blueFlag;
	private String redFlagREDCategory;
	private String redFlagYELLOWCategory;
	private String redFlagCategory;
	private String redFlagCategoryCode;
	private String grantingOfficalFlag;
	private String processingOfficialFlag;
	private String clarificationOfficialFlag;
	private String documentType;
	private String emailId;
	private String mobile;
	private String ppAmount;
	private String ppAmountCurrency;
	private String ppAmountDesc;
	private String installments;
	private String installmentNumbers;
	private String ppInsFlag;
	private Boolean markOffice= false;
	
	public ProjectDashboardDao(Connection connection) {
		super(connection);
	}	
	
	public Boolean checkUserSection(String userId,String officeCode) throws Exception{
		PreparedStatement statement=null;
		Boolean status=false;
		StringBuffer query = new StringBuffer("SELECT SECTION_ID FROM TM_USER_SECTION WHERE USER_ID=? AND OFFICE_CODE=?");		
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, userId);
		statement.setString(2, officeCode);
		ResultSet rs = statement.executeQuery();
		if(rs.next())
			status=true;
		
		return status;
	}
	
	public void getPendingMailRecords(ProjectRequest obj) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query = new StringBuffer("SELECT A.APPLICATION_ID,B.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=B.SERVICE_CODE) "
				+ " AS SERVICE,B.CURRENT_STAGE,B.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=B.CURRENT_STATUS) AS STATUS ,"
				+ "to_char(B.SUBMISSION_DATE,'dd-mm-yyyy') AS SUB_DATE,B.APPLICANT_NAME,B.TEMP_FILENO,B.SECTION_FILENO FROM T_PC_USER_LEVEL_STATUS A,V_APPLICATION_DETAILS B "
				+ "WHERE A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID=? AND A.USER_ID=?");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());
		statement.setString(1, obj.getCurrentStage());
		statement.setString(2, obj.getToUser());
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}
		rs.close();
		statement.close();		
	}
	
	public List<AbstractRequest> getDashboard(ProjectRequest obj) throws Exception{
		PreparedStatement statement=null;
		StringBuffer stateQuery=new StringBuffer("");
		StringBuffer sectionQuery=new StringBuffer("");
		StringBuffer serviceQuery=new StringBuffer("");
		StringBuffer officeUserQuery=new StringBuffer("");
		StringBuffer stageQuery=new StringBuffer("");
		Boolean stateFlag=false,serviceFlag=false,officeUserFlag=false;String sectionFlag="F";	
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		if(!(obj.getState()==null || obj.getState().equals(""))){			
			stateQuery=new StringBuffer(" AND B.STATE=?");
			stateFlag=true;
		}	
		if(!(obj.getService()==null || obj.getService().equals(""))){			
			serviceQuery=new StringBuffer(" AND B.SERVICE_CODE=?");
			serviceFlag=true;
		}	
		if(!(obj.getSection()==null || obj.getSection().equals("")) && !(obj.getSection().equals("NS"))){	
			sectionFlag="T";
			sectionQuery=new StringBuffer(" AND B.APPLICATION_ID IN(SELECT APPLICATION_ID FROM T_PC_SECTION_DETAILS WHERE OFFICE_CODE=? "
					+ "AND SECTION_ID=?)");
		}
		else if(obj.getSection()==null || obj.getSection().equals("")){		
			sectionFlag="F";
			sectionQuery=new StringBuffer(" AND B.APPLICATION_ID IN(SELECT APPLICATION_ID FROM T_PC_SECTION_DETAILS WHERE OFFICE_CODE=?)");
		}
		else if(obj.getSection().equals("NS")){		
			sectionFlag="NS";			
		}
		if(obj.getCurrentStage().equals("4")){
			stageQuery=new StringBuffer("1=1");
		}else{
			stageQuery=new StringBuffer("B.CURRENT_STATUS NOT IN(9,10,12)");
		}
		if(!(obj.getOfficeUser()==null || obj.getOfficeUser().equals(""))){			
			officeUserQuery=new StringBuffer(" AND C.BY_USERID=?");
			officeUserFlag=true;
		}	
		StringBuffer query = new StringBuffer("SELECT A.APPLICATION_ID,B.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=B.SERVICE_CODE) "
				+ " AS SERVICE,B.CURRENT_STAGE,B.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=B.CURRENT_STATUS) AS STATUS ,"
				+ "to_char(B.SUBMISSION_DATE,'dd-mm-yyyy') AS SUB_DATE,B.APPLICANT_NAME,B.TEMP_FILENO,B.SECTION_FILENO,"
				+ "CASE WHEN C.BY_USERID IS NULL THEN NULL ELSE (SELECT USER_NAME||' [ '||BB.SHORT_DESIGNATION||' ] ' FROM TM_USER AA, TM_DESIGNATION BB WHERE USER_ID=C.BY_USERID "
				+ "AND AA.DESIGNATION_ID=BB.DESIGNATION_ID) ||' [ '|| ( SELECT OFFICE_TYPE||', '||CITY_NAME FROM TM_OFFICE AA, TM_OFFICETYPE BB WHERE OFFICE_CODE=C.BY_OFFICE_CODE "
				+ "AND AA.OFFICE_ID=BB.OFFICE_ID )||' ] ' END LAST_ACTION_BY,TO_CHAR(A.ACTIVITY_ON,'DD-MM-YYYY') AS LAST_ACTION_ON,A.ACTIVITY_ON "
				+ "FROM V_APPLICATION_DETAILS B, T_PC_USER_LEVEL_STATUS A LEFT JOIN T_PC_COMMUNICATION C ON A.CHAT_ID=C.CHAT_ID WHERE A.APPLICATION_ID=B.APPLICATION_ID AND "+stageQuery+" "
				+ "AND STAGE_ID=? AND A.USER_ID=? "+sectionQuery+officeUserQuery+serviceQuery+stateQuery+"");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(stateFlag==false){			
			if(serviceFlag==false){
				if(officeUserFlag==false){
					if(sectionFlag.equals("NS")){		
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());	
					}
				}else{
					if(sectionFlag.equals("NS")){		
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getOfficeUser());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getOfficeUser());
					}
				}				
			}else{
				if(officeUserFlag==false){
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getService());									
					}
					else if(sectionFlag.equals("T")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getService());										
					}else if(sectionFlag.equals("F")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getService());											
					}
				}else{
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getOfficeUser());
						statement.setString(4, obj.getService());						
					}
					else if(sectionFlag.equals("T")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
						statement.setString(6, obj.getService());				
					}else if(sectionFlag.equals("F")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getOfficeUser());
						statement.setString(5, obj.getService());						
					}
				}				
			}
		}else{
			if(serviceFlag==false){
				if(officeUserFlag==false){
					if(sectionFlag.equals("NS")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getState());
					}
					else if(sectionFlag.equals("T")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getState());
					}else if(sectionFlag.equals("F")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getState());
					}
				}else{
					if(sectionFlag.equals("NS")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getOfficeUser());
						statement.setString(4, obj.getState());						
					}
					else if(sectionFlag.equals("T")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
						statement.setString(6, obj.getState());						
					}else if(sectionFlag.equals("F")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getOfficeUser());
						statement.setString(5, obj.getState());
					}
				}				
			}else{
				if(officeUserFlag==false){
					if(sectionFlag.equals("NS")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getService());
						statement.setString(4, obj.getState());				
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getService());
						statement.setString(6, obj.getState());
					}else if(sectionFlag.equals("F")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getService());
						statement.setString(5, obj.getState());
					}
				}else{
					if(sectionFlag.equals("NS")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getOfficeUser());
						statement.setString(4, obj.getService());
						statement.setString(5, obj.getState());				
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
						statement.setString(6, obj.getService());
						statement.setString(7, obj.getState());
					}else if(sectionFlag.equals("F")){	
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToUser());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getOfficeUser());
						statement.setString(5, obj.getService());
						statement.setString(6, obj.getState());
					}
				}
				
			}
		}			
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			if(stateFlag==false){
				if(serviceFlag==false){
					if(officeUserFlag==false){
						if(sectionFlag.equals("NS")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());	
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getOfficeUser());
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getOfficeUser());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
					}					
				}else{
					if(officeUserFlag==false){
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getService());
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getService());	
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getService());		
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getOfficeUser());
							statement.setString(4, obj.getService());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setString(6, obj.getService());	
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getOfficeUser());
							statement.setString(5, obj.getService());		
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
					}
					
				}
			}else{
				if(serviceFlag==false){
					if(officeUserFlag==false){
						if(sectionFlag.equals("NS")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getState());
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getState());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getState());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getOfficeUser());
							statement.setString(4, obj.getState());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setString(6, obj.getState());
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getOfficeUser());
							statement.setString(5, obj.getState());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
					}
					
				}else{
					if(officeUserFlag==false){
						if(sectionFlag.equals("NS")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getState());
							statement.setString(4, obj.getService());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getService());
							statement.setString(6, obj.getState());
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getService());
							statement.setString(5, obj.getState());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getOfficeUser());
							statement.setString(4, obj.getService());
							statement.setString(5, obj.getState());							
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setString(6, obj.getService());
							statement.setString(7, obj.getState());
							statement.setInt(8, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(9, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){	
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToUser());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getOfficeUser());
							statement.setString(5, obj.getService());
							statement.setString(6, obj.getState());
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}
					}					
				}
			}									
		}
		rs = statement.executeQuery();
		List<AbstractRequest> projectList = new ArrayList<AbstractRequest>();
		while(rs.next()) {			
			projectList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)
					,rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),0));			
		}
		return projectList;
	}
	
	public List<AbstractRequest> getFreshOfficeDashboard(ProjectRequest obj) throws Exception{		
		PreparedStatement statement=null;
		StringBuffer stateQuery=new StringBuffer("");
		StringBuffer sectionQuery=new StringBuffer("");
		StringBuffer serviceQuery=new StringBuffer("");
		
		Boolean stateFlag=false,serviceFlag=false;String sectionFlag="F";	
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		if(!(obj.getState()==null || obj.getState().equals(""))){			
			stateQuery=new StringBuffer(" AND A.STATE=?");
			stateFlag=true;
		}	
		if(!(obj.getService()==null || obj.getService().equals(""))){			
			serviceQuery=new StringBuffer(" AND A.SERVICE_CODE=?");
			serviceFlag=true;
		}	
		if(!(obj.getSection()==null || obj.getSection().equals("")) && !(obj.getSection().equals("NS"))){	
			sectionFlag="T";
			sectionQuery=new StringBuffer(" AND A.APPLICATION_ID IN(SELECT APPLICATION_ID FROM T_PC_SECTION_DETAILS WHERE OFFICE_CODE=? "
					+ "AND SECTION_ID=?)");
		}
		else if(obj.getSection()==null || obj.getSection().equals("")){			
			sectionQuery=new StringBuffer(" AND A.APPLICATION_ID IN(SELECT APPLICATION_ID FROM T_PC_SECTION_DETAILS WHERE OFFICE_CODE=?)");
		}
		else if(obj.getSection().equals("NS")){		
			sectionFlag="NS";			
		}		
				
		query = new StringBuffer("SELECT A.APPLICATION_ID,A.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=A.SERVICE_CODE) "
				+ "AS SERVICE,A.CURRENT_STAGE,A.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=A.CURRENT_STATUS) AS STATUS ,"
				+ "TO_CHAR(A.SUBMISSION_DATE,'DD-MM-YYYY'),APPLICANT_NAME,A.TEMP_FILENO,A.SECTION_FILENO, NULL AS LAST_ACTION_BY, NULL AS LAST_ACTION_ON FROM V_APPLICATION_DETAILS A "
				+ "WHERE A.CURRENT_STAGE=1 AND CURRENT_STATUS=2"
				+ " "+sectionQuery+serviceQuery+stateQuery+"");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(stateFlag==false){
			if(serviceFlag==false){
				
				if(sectionFlag.equals("NS")){					
				}
				else if(sectionFlag.equals("T")){					
					statement.setString(1, obj.getToOffice());
					statement.setString(2, obj.getSection());
				}else if(sectionFlag.equals("F")){					
					statement.setString(1, obj.getToOffice());	
				}
			}else{
				if(sectionFlag.equals("NS")){					
					statement.setString(1, obj.getService());									
				}
				else if(sectionFlag.equals("T")){	
					statement.setString(1, obj.getToOffice());
					statement.setString(2, obj.getSection());
					statement.setString(3, obj.getService());										
				}else if(sectionFlag.equals("F")){	
					statement.setString(1, obj.getToOffice());
					statement.setString(2, obj.getService());											
				}
			}
		}else{
			if(serviceFlag==false){
				if(sectionFlag.equals("NS")){					
					statement.setString(1, obj.getState());
				}
				else if(sectionFlag.equals("T")){					
					statement.setString(1, obj.getToOffice());
					statement.setString(2, obj.getSection());
					statement.setString(3, obj.getState());
				}else if(sectionFlag.equals("F")){					
					statement.setString(1, obj.getToOffice());	
					statement.setString(2, obj.getState());
				}
			}else{
				if(sectionFlag.equals("NS")){			
					statement.setString(1, obj.getState());
					statement.setString(2, obj.getService());
				}
				else if(sectionFlag.equals("T")){					
					statement.setString(1, obj.getToOffice());
					statement.setString(2, obj.getSection());
					statement.setString(3, obj.getService());
					statement.setString(4, obj.getState());
				}else if(sectionFlag.equals("F")){					
					statement.setString(1, obj.getToOffice());	
					statement.setString(2, obj.getService());
					statement.setString(3, obj.getState());
				}
			}
		}
		
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			if(stateFlag==false){
				if(serviceFlag==false){
					if(sectionFlag.equals("NS")){			
						statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(2, (pageRequested-1) * pageSize + 1);
					}
					else if(sectionFlag.equals("T")){					
						statement.setString(1, obj.getToOffice());
						statement.setString(2, obj.getSection());
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}else if(sectionFlag.equals("F")){					
						statement.setString(1, obj.getToOffice());	
						statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(3, (pageRequested-1) * pageSize + 1);
					}
				}else{
					if(sectionFlag.equals("NS")){					
						statement.setString(1, obj.getService());		
						statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(3, (pageRequested-1) * pageSize + 1);
					}
					else if(sectionFlag.equals("T")){	
						statement.setString(1, obj.getToOffice());
						statement.setString(2, obj.getSection());
						statement.setString(3, obj.getService());		
						statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(5, (pageRequested-1) * pageSize + 1);
					}else if(sectionFlag.equals("F")){	
						statement.setString(1, obj.getToOffice());
						statement.setString(2, obj.getService());		
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}
				}
			}else{
				if(serviceFlag==false){
					if(sectionFlag.equals("NS")){					
						statement.setString(1, obj.getState());
						statement.setInt(2,(pageRequested-1) * pageSize + pageSize);
						statement.setInt(3, (pageRequested-1) * pageSize + 1);
					}
					else if(sectionFlag.equals("T")){					
						statement.setString(1, obj.getToOffice());
						statement.setString(2, obj.getSection());
						statement.setString(3, obj.getState());
						statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(5, (pageRequested-1) * pageSize + 1);
					}else if(sectionFlag.equals("F")){					
						statement.setString(1, obj.getToOffice());	
						statement.setString(2, obj.getState());
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}
				}else{
					if(sectionFlag.equals("NS")){			
						statement.setString(1, obj.getState());
						statement.setString(2, obj.getService());
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}
					else if(sectionFlag.equals("T")){					
						statement.setString(1, obj.getToOffice());
						statement.setString(2, obj.getSection());
						statement.setString(3, obj.getService());
						statement.setString(4, obj.getState());
						statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(6, (pageRequested-1) * pageSize + 1);
					}else if(sectionFlag.equals("F")){					
						statement.setString(1, obj.getToOffice());	
						statement.setString(2, obj.getService());
						statement.setString(3, obj.getState());
						statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(5, (pageRequested-1) * pageSize + 1);
					}
				}
			}
						
		}		
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		while(rs.next()) {			
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)
					,rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),"","",0));			
		}
		return applicationList;
	}
	
	public List<AbstractRequest> getOfficeDashboard(ProjectRequest obj) throws Exception{
		PreparedStatement statement=null;
		StringBuffer stateQuery=new StringBuffer("");
		StringBuffer sectionQuery=new StringBuffer("");
		StringBuffer serviceQuery=new StringBuffer("");
		StringBuffer stageQuery=new StringBuffer("");
		StringBuffer officeUserQuery=new StringBuffer("");
		Boolean stateFlag=false,serviceFlag=false,officeUserFlag=false;String sectionFlag="F";	
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		if(!(obj.getState()==null || obj.getState().equals(""))){			
			stateQuery=new StringBuffer(" AND B.STATE=?");
			stateFlag=true;
		}
		if(!(obj.getService()==null || obj.getService().equals(""))){			
			serviceQuery=new StringBuffer(" AND B.SERVICE_CODE=?");
			serviceFlag=true;
		}
		if(!(obj.getSection()==null || obj.getSection().equals("")) && !(obj.getSection().equals("NS"))){	
			sectionFlag="T";
			sectionQuery=new StringBuffer(" AND B.APPLICATION_ID IN(SELECT APPLICATION_ID FROM T_PC_SECTION_DETAILS WHERE OFFICE_CODE=? "
					+ "AND SECTION_ID=?)");
		}
		else if(obj.getSection()==null || obj.getSection().equals("")){			
			sectionQuery=new StringBuffer(" AND B.APPLICATION_ID IN(SELECT APPLICATION_ID FROM T_PC_SECTION_DETAILS WHERE OFFICE_CODE=?)");
		}
		else if(obj.getSection().equals("NS")){		
			sectionFlag="NS";			
		}
		if(obj.getCurrentStage().equals("4")){
			stageQuery=new StringBuffer("1=1");
		}else{
			stageQuery=new StringBuffer("B.CURRENT_STATUS NOT IN(9,10,12)");
		}
		
		if(!(obj.getOfficeUser()==null || obj.getOfficeUser().equals(""))){			
			officeUserQuery=new StringBuffer(" AND C.BY_USERID=?");
			officeUserFlag=true;
		}	
		
		query = new StringBuffer("SELECT A.APPLICATION_ID,B.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=B.SERVICE_CODE)AS SERVICE,"
				+ "B.CURRENT_STAGE,B.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=B.CURRENT_STATUS) AS STATUS ,to_char(B.SUBMISSION_DATE,'dd-mm-yyyy') "
				+ "AS SUB_DATE,B.APPLICANT_NAME,B.TEMP_FILENO,B.SECTION_FILENO, "
				+ "CASE WHEN C.BY_USERID IS NULL THEN NULL ELSE (SELECT USER_NAME||' [ '||BB.SHORT_DESIGNATION||' ] ' FROM TM_USER AA, TM_DESIGNATION BB WHERE USER_ID=C.BY_USERID AND "
				+ "AA.DESIGNATION_ID=BB.DESIGNATION_ID)||' [ '|| (SELECT OFFICE_TYPE||', '||CITY_NAME FROM TM_OFFICE AA, TM_OFFICETYPE BB WHERE OFFICE_CODE=C.BY_OFFICE_CODE AND"
				+ " AA.OFFICE_ID=BB.OFFICE_ID)||' ] ' END LAST_ACTION_BY,TO_CHAR(A.ACTIVITY_ON,'DD-MM-YYYY') AS LAST_ACTION_ON,A.ACTIVITY_ON "
				+ " FROM V_APPLICATION_DETAILS B, T_PC_OFFICE_LEVEL_STATUS A left join T_PC_COMMUNICATION C on A.CHAT_ID=C.CHAT_ID WHERE A.APPLICATION_ID=B.APPLICATION_ID  "
				+ " AND "+stageQuery+" AND A.STAGE_ID=? AND A.OFFICE_CODE=? "+sectionQuery+officeUserQuery+serviceQuery+stateQuery+"");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(stateFlag==false){
			if(serviceFlag==false){
				if(officeUserFlag==false){
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());	
					}
				}else{
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getOfficeUser());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getOfficeUser());
					}
				}				
			}else{
				if(officeUserFlag==false){
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());					
						statement.setString(2, obj.getToOffice());		
						statement.setString(3, obj.getService());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());					
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getService());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());					
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getService());
					}
				}else{
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());					
						statement.setString(2, obj.getToOffice());	
						statement.setString(3, obj.getOfficeUser());
						statement.setString(4, obj.getService());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());					
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
						statement.setString(6, obj.getService());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());					
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getOfficeUser());
						statement.setString(5, obj.getService());
					}
				}				
			}
		}else{
			if(serviceFlag==false){
				if(officeUserFlag==false){
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getState());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getState());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getState());
					}
				}else{
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getOfficeUser());
						statement.setString(4, obj.getState());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
						statement.setString(6, obj.getState());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getOfficeUser());
						statement.setString(5, obj.getState());
					}
				}				
			}else{
				if(officeUserFlag==false){					
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());						
						statement.setString(3, obj.getService());
						statement.setString(4, obj.getState());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());						
						statement.setString(5, obj.getService());
						statement.setString(6, obj.getState());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());						
						statement.setString(4, obj.getService());
						statement.setString(5, obj.getState());
					}
				}else{
					if(sectionFlag.equals("NS")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getOfficeUser());
						statement.setString(4, obj.getService());
						statement.setString(5, obj.getState());
					}
					else if(sectionFlag.equals("T")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());
						statement.setString(4, obj.getSection());
						statement.setString(5, obj.getOfficeUser());
						statement.setString(6, obj.getService());
						statement.setString(7, obj.getState());
					}else if(sectionFlag.equals("F")){
						statement.setString(1, obj.getCurrentStage());
						statement.setString(2, obj.getToOffice());
						statement.setString(3, obj.getToOffice());	
						statement.setString(4, obj.getOfficeUser());
						statement.setString(5, obj.getService());
						statement.setString(6, obj.getState());
					}
				}				
			}
		} 
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {		
			if(stateFlag==false){
				if(serviceFlag==false){
					if(officeUserFlag==false){
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());	
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getOfficeUser());
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getOfficeUser());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
					}				
				}else{
					if(officeUserFlag==false){
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());					
							statement.setString(2, obj.getToOffice());		
							statement.setString(3, obj.getService());
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());					
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getService());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());					
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getService());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());					
							statement.setString(2, obj.getToOffice());	
							statement.setString(3, obj.getOfficeUser());
							statement.setString(4, obj.getService());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());					
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setString(6, obj.getService());
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());					
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getOfficeUser());
							statement.setString(5, obj.getService());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
					}				
				}
			}else{
				if(serviceFlag==false){
					if(officeUserFlag==false){
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getState());
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getState());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getState());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getOfficeUser());
							statement.setString(4, obj.getState());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setString(6, obj.getState());
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getOfficeUser());
							statement.setString(5, obj.getState());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
					}				
				}else{
					if(officeUserFlag==false){					
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());							
							statement.setString(3, obj.getService());
							statement.setString(4, obj.getState());
							statement.setInt(5, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(6, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());							
							statement.setString(5, obj.getService());
							statement.setString(6, obj.getState());
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());							
							statement.setString(4, obj.getService());
							statement.setString(5, obj.getState());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
					}else{
						if(sectionFlag.equals("NS")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getOfficeUser());
							statement.setString(4, obj.getService());
							statement.setString(5, obj.getState());
							statement.setInt(6, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(7, (pageRequested-1) * pageSize + 1);
						}
						else if(sectionFlag.equals("T")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());
							statement.setString(4, obj.getSection());
							statement.setString(5, obj.getOfficeUser());
							statement.setString(6, obj.getService());
							statement.setString(7, obj.getState());
							statement.setInt(8, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(9, (pageRequested-1) * pageSize + 1);
						}else if(sectionFlag.equals("F")){
							statement.setString(1, obj.getCurrentStage());
							statement.setString(2, obj.getToOffice());
							statement.setString(3, obj.getToOffice());	
							statement.setString(4, obj.getOfficeUser());
							statement.setString(5, obj.getService());
							statement.setString(6, obj.getState());
							statement.setInt(7, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(8, (pageRequested-1) * pageSize + 1);
						}
					}				
				}
			} 
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		while(rs.next()) {			
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)
					,rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),0));			
		}
		return applicationList;
	}
	
	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }	        
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	public boolean isAlphaNumeric(String s){
	    String pattern= "^[a-zA-Z0-9]*$";
	        if(s.matches(pattern)){
	            return true;
	        }
	        return false;   
	}
	
	public List<AbstractRequest> getApplicationListDetails() throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		String queryField=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		if(searchFlag.equals("name")){
			queryField="UPPER(APPLICANT_NAME) LIKE ('%' || ? || '%')";
		}else if(searchFlag.equals("id")){
			queryField="(TEMP_FILENO=? OR SECTION_FILENO=? OR APPLICATION_ID=?)";
		}	
		query = new StringBuffer("SELECT A.APPLICATION_ID,A.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=A.SERVICE_CODE) "
				+ "AS SERVICE,A.CURRENT_STAGE,A.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=A.CURRENT_STATUS) AS STATUS ,"
				+ "	TO_CHAR(A.SUBMISSION_DATE,'DD-MM-YYYY'),APPLICANT_NAME,A.TEMP_FILENO,A.SECTION_FILENO FROM V_APPLICATION_DETAILS A "
				+ " WHERE "+queryField+"");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(searchFlag.equals("name")){
			statement.setString(1, searchString);
		}else if(searchFlag.equals("id")){
			statement.setString(1, searchString);
			statement.setString(2, searchString);
			statement.setString(3, searchString);
		}
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			if(searchFlag.equals("name")){
				statement.setString(1, searchString);										
				statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(3, (pageRequested-1) * pageSize + 1);
			}else if(searchFlag.equals("id")){
				statement.setString(1, searchString);
				statement.setString(2, searchString);
				statement.setString(3, searchString);										
				statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(5, (pageRequested-1) * pageSize + 1);
			}
			
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		while(rs.next()) {			
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)
					,rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),"","",0));			
		}
		return applicationList;
	}
	
	public void getApplicationDetails(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;	String currentStatus=null,tempAppId=null, serviceId = null;
		query = new StringBuffer("SELECT A.APPLICATION_ID,A.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=A.SERVICE_CODE) "
				+ "AS SERVICE,A.CURRENT_STAGE,A.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=A.CURRENT_STATUS) AS STATUS ,"
				+ "	TO_CHAR(A.SUBMISSION_DATE,'DD-MM-YYYY'),APPLICANT_NAME,A.TEMP_FILENO,A.SECTION_FILENO FROM V_APPLICATION_DETAILS A "
				+ " WHERE (A.APPLICATION_ID=? OR A.TEMP_FILENO=? OR A.SECTION_FILENO=?)");		
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);
		statement.setString(2, appId);
		statement.setString(3, appId);
		rs=statement.executeQuery();		
		if(rs.next()) {		
			doExist=true;
			currentStatus=rs.getString(5);
			tempAppId=rs.getString(1);
			serviceId = rs.getString(2);
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)
					,rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),"","",0));			
		}else{			
			throw new ValidationException("Application Id <b>"+appId+"</b> doesn't exist.Please search with valid applicationId.");
		}			
		if(currentStatus.equals("9")){  // Granted
			finalStatus="G";
			throw new InformationException("Application ID <b>"+appId+"</b> has been granted.Please see details.");
		}else if(currentStatus.equals("10")){	// Denied
			finalStatus="D";
			throw new InformationException("Application ID  <b>"+appId+"</b> has been denied.Please see details.");
		}
		else if(currentStatus.equals("12")){	// Closed
			finalStatus="C";
			throw new InformationException("Application ID  <b>"+appId+"</b> has been closed.Please see details.");
		}
		else if(currentStatus.equals("8")){	// Closed
			finalStatus="CR";
			throw new InformationException("Application ID  <b>"+appId+"</b> has been asked for clarification.Please see details.");
		}
		else{ 	// In Process
				if(currentStatus.equals("2")){ 	// Not yet accepted
					String serviceCode=null;
					doAccept=true;	
					// Getting Service Code 
					query = new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");		
					statement = connection.prepareStatement(query.toString());	
					statement.setString(1, tempAppId);
					rs=statement.executeQuery();
					if(rs.next()){
						serviceCode=rs.getString(1);						
					}
					rs.close();
					statement.close();
					// Checking whether Logged-In User belongs to same section to which requested service belongs
					query = new StringBuffer("SELECT * FROM TM_USER_SECTION WHERE USER_ID=? AND OFFICE_CODE=? AND SECTION_ID IN"
							+ "(SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE  OFFICE_CODE=? AND SERVICE_CODE=?)");		
					statement = connection.prepareStatement(query.toString());	
					statement.setString(1, myUserId);
					statement.setString(2, myOfficeCode);
					statement.setString(3, myOfficeCode);
					statement.setString(4, serviceCode);
					rs=statement.executeQuery();
					if(rs.next()){ // belongs to section assigned to me.
						mySection=true;
						throw new InformationException("Application ID  <b>"+appId+"</b> is not accepted yet.Please accept to process.");						
					}else{
						throw new InformationException("Application ID  <b>"+appId+"</b> is not accepted yet.It can't be accecpted by you as it belongs to"
						+ " some other section.");
					}					
				}else{	// In Processing			
					// Checking whether it is with Logged-In User					
					query = new StringBuffer("SELECT STAGE_ID FROM T_PC_USER_LEVEL_STATUS WHERE USER_ID=(SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE OFFICE_CODE=? "
							+ "AND APPLICATION_ID=?) AND APPLICATION_ID=? AND STAGE_ID IN(1,2,7) AND OFFICE_CODE=? AND USER_ID=?");		
					statement = connection.prepareStatement(query.toString());	
					statement.setString(1, myOfficeCode);
					statement.setString(2, tempAppId);
					statement.setString(3, tempAppId);
					statement.setString(4, myOfficeCode);
					statement.setString(5, myUserId);
					rs=statement.executeQuery();
					if(rs.next()){ // With me						
						withMe=true;
						if(serviceId.equals("01")||serviceId.equals("02")||serviceId.equals("03")||serviceId.equals("05")||serviceId.equals("06")||serviceId.equals("13")||serviceId.equals("15")||serviceId.equals("16")||serviceId.equals("17"))
							markOffice = true;
						if(rs.getString(1).equals("1") || rs.getString(1).equals("2")){
							withMeToAnswer=true;
							throw new InformationException("Application ID  <b>"+appId+"</b> is in <b>Under Process</b>.Currently It's with you.");
						}
						else if(rs.getString(1).equals("7")){
							withMeToMail=true;
							markOffice = false;
							throw new InformationException("Application ID  <b>"+appId+"</b> is in <b>Under Process</b>.Currently It's <b>Pending for Mail.</b>");
						}
					}
					else{ // Not With me	
						notWithMe=true;
						String serviceCode=null,user=null;
						statement.close();
						rs.close();
						query = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE OFFICE_CODE=? AND APPLICATION_ID=?");		
						statement = connection.prepareStatement(query.toString());	
						statement.setString(1, myOfficeCode);
						statement.setString(2, tempAppId);						
						rs=statement.executeQuery();
						if(rs.next()){
							user=rs.getString(1);							
						}	
						rs.close();
						statement.close();
						// Getting Service Code 
						query = new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");		
						statement = connection.prepareStatement(query.toString());	
						statement.setString(1, tempAppId);
						rs=statement.executeQuery();
						if(rs.next()){
							serviceCode=rs.getString(1);						
						}
						rs.close();
						statement.close();
						query = new StringBuffer("SELECT * FROM TM_USER_SECTION WHERE USER_ID=? AND OFFICE_CODE=? AND SECTION_ID IN"
								+ "(SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE  OFFICE_CODE=? AND SERVICE_CODE=?)");		
						statement = connection.prepareStatement(query.toString());	
						statement.setString(1, myUserId);
						statement.setString(2, myOfficeCode);
						statement.setString(3, myOfficeCode);
						statement.setString(4, serviceCode);
						rs=statement.executeQuery();
						if(rs.next()){ // belongs to section assigned to me.													
							if(checkUserRoleForGranting(myUserId)==true){
								notWithMeTofetch=true;	
								throw new InformationException("Application ID  <b>"+appId+"</b> is in under process.It belongs to your section.Currently it's with <b>"+user+"</b>.You need to fetch it to process.");
							}
							else if(checkUserRoleForGranting(myUserId)==false){
								notWithMeTofetch=false;	
								throw new InformationException("Application ID  <b>"+appId+"</b> is in under process.It belongs to your section.Currently it's with <b>"+user+"</b>.You don't have sufficient priviledge to fetch it.");
							}
						}else{
							throw new InformationException("Application ID  <b>"+appId+"</b> is in under process.Currently it's with <b>"+user+"</b>.It can't be processed by you as it belongs to some other section.");
						}
					}
				}				
		}
	}
	
	public void getApplicationDetailsForIBAndRAW(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;	String currentStatus=null,tempAppId=null, serviceId = null;
		query = new StringBuffer("SELECT A.APPLICATION_ID,A.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=A.SERVICE_CODE) "
				+ "AS SERVICE,A.CURRENT_STAGE,A.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=A.CURRENT_STATUS) AS STATUS ,"
				+ "	TO_CHAR(A.SUBMISSION_DATE,'DD-MM-YYYY'),APPLICANT_NAME,A.TEMP_FILENO,A.SECTION_FILENO FROM V_APPLICATION_DETAILS A "
				+ " WHERE (A.APPLICATION_ID=? OR A.TEMP_FILENO=? OR A.SECTION_FILENO=?)");		
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);
		statement.setString(2, appId);
		statement.setString(3, appId);
		rs=statement.executeQuery();		
		if(rs.next()) {		
			doExist=true;
			currentStatus=rs.getString(5);
			tempAppId=rs.getString(1);
			serviceId = rs.getString(2);
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)
					,rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),"","",0));			
		}else{			
			throw new ValidationException("Application Id <b>"+appId+"</b> doesn't exist.Please search with valid applicationId.");
		}	
		rs.close();
		statement.close();
		
		if(currentStatus.equals("8")){	// Closed
			finalStatus="CR";
			throw new InformationException("Application ID  <b>"+appId+"</b> has been asked for clarification.Please see details.");
		}
		
		String officeFinalStatus=null;
		query=new StringBuffer("SELECT STATUS FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, appId);
		statement.setString(2, myOfficeCode);
		rs=statement.executeQuery();		
		if(rs.next()) {	
			officeFinalStatus=rs.getString(1);
		}
		rs.close();
		statement.close();
		
		if(officeFinalStatus!=null && officeFinalStatus.equals("5")){
			throw new InformationException("Application ID  <b>"+appId+"</b> has been <b><i>DISPOSED OFF</i></b>.Please see details.");
		}
		
		if(officeFinalStatus==null){	
			String officeStatus=null;
			query=new StringBuffer("SELECT STAGE_ID FROM T_PC_OFFICE_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, appId);
			statement.setString(2, myOfficeCode);
			rs=statement.executeQuery();
			if(rs.next()) {	
				officeStatus=rs.getString(1);
			}
			rs.close();
			statement.close();
			
			// Checking Section 
			Boolean isSection=false;
			query = new StringBuffer("SELECT * FROM TM_PC_SECTION  WHERE OFFICE_CODE=?");		
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, tempAppId);
			rs=statement.executeQuery();
			if(rs.next()){
				isSection=true;						
			}
			rs.close();
			statement.close();	
			
			if(officeStatus==null){
				if(currentStatus.equals("9")){						
					throw new InformationException("Application ID  <b>"+appId+"</b> has been <b><i>GRANTED</i></b>.Please see details.");
				}else if(currentStatus.equals("10")){
					throw new InformationException("Application ID  <b>"+appId+"</b> has been <b><i>DENIED</i></b>.Please see details.");
				}else if(currentStatus.equals("12")){
					throw new InformationException("Application ID  <b>"+appId+"</b> has been <b><i>CLOSED</i></b>.Please see details.");
				}				
			}
			
			if(officeStatus.equals("1")){	// Not Accepted YET
				String serviceCode=null;
				doAccept=true;
				
				// Getting Service Code 
				query = new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");		
				statement = connection.prepareStatement(query.toString());	
				statement.setString(1, tempAppId);
				rs=statement.executeQuery();
				if(rs.next()){
					serviceCode=rs.getString(1);						
				}
				rs.close();
				statement.close();	
				
				if(isSection==true){
					// Checking whether Logged-In User belongs to same section to which requested service belongs
					query = new StringBuffer("SELECT * FROM TM_USER_SECTION WHERE USER_ID=? AND OFFICE_CODE=? AND SECTION_ID IN"
							+ "(SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE  OFFICE_CODE=? AND SERVICE_CODE=?)");		
					statement = connection.prepareStatement(query.toString());	
					statement.setString(1, myUserId);
					statement.setString(2, myOfficeCode);
					statement.setString(3, myOfficeCode);
					statement.setString(4, serviceCode);
					rs=statement.executeQuery();
					if(rs.next()){ // belongs to section assigned to me.
						mySection=true;
						throw new InformationException("Application ID  <b>"+appId+"</b> is not accepted yet.Please accept to process.");						
					}else{
						throw new InformationException("Application ID  <b>"+appId+"</b> is not accepted yet.It can't be accecpted by you as it belongs to"
						+ " some other section.");
					}
					
				}else{
					mySection=true;
					throw new InformationException("Application ID  <b>"+appId+"</b> is not accepted yet.Please accept to process.");
				}				
			}else if(officeStatus.equals("6")){
				// In Processing			
				// Checking whether it is with Logged-In User					
				query = new StringBuffer("SELECT STAGE_ID FROM T_PC_USER_LEVEL_STATUS WHERE USER_ID=(SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE OFFICE_CODE=? "
						+ "AND APPLICATION_ID=?) AND APPLICATION_ID=? AND STAGE_ID IN(1,2) AND OFFICE_CODE=? AND USER_ID=?");		
				statement = connection.prepareStatement(query.toString());	
				statement.setString(1, myOfficeCode);
				statement.setString(2, tempAppId);
				statement.setString(3, tempAppId);
				statement.setString(4, myOfficeCode);
				statement.setString(5, myUserId);
				rs=statement.executeQuery();
				if(rs.next()){ // With me						
					withMe=true;
					
					if(rs.getString(1).equals("1") || rs.getString(1).equals("2")){
						withMeToAnswer=true;
						throw new InformationException("Application ID  <b>"+appId+"</b> is in <b>Under Process</b>.Currently It's with you.");
					}					
				}else{ // Not With me	
						notWithMe=true;
						String serviceCode=null,user=null;
						statement.close();
						rs.close();
						query = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE OFFICE_CODE=? AND APPLICATION_ID=?");		
						statement = connection.prepareStatement(query.toString());	
						statement.setString(1, myOfficeCode);
						statement.setString(2, tempAppId);						
						rs=statement.executeQuery();
						if(rs.next()){
							user=rs.getString(1);							
						}	
						rs.close();
						statement.close();
						// Getting Service Code 
						query = new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");		
						statement = connection.prepareStatement(query.toString());	
						statement.setString(1, tempAppId);
						rs=statement.executeQuery();
						if(rs.next()){
							serviceCode=rs.getString(1);						
						}
						rs.close();
						statement.close();
						if(isSection==true){
							query = new StringBuffer("SELECT * FROM TM_USER_SECTION WHERE USER_ID=? AND OFFICE_CODE=? AND SECTION_ID IN"
									+ "(SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE  OFFICE_CODE=? AND SERVICE_CODE=?)");		
							statement = connection.prepareStatement(query.toString());	
							statement.setString(1, myUserId);
							statement.setString(2, myOfficeCode);
							statement.setString(3, myOfficeCode);
							statement.setString(4, serviceCode);
							rs=statement.executeQuery();
							if(rs.next()){ // belongs to section assigned to me.
								notWithMeTofetch=true;
								throw new InformationException("Application ID  <b>"+appId+"</b> is in under process.It belongs to your section.Currently it's with <b>"+user+"</b>.You need to fetch it to process.");						
							}else{
								throw new InformationException("Application ID  <b>"+appId+"</b> is in under process.Currently it's with <b>"+user+"</b>.It can't be processed by you as it belongs to some other section.");
							}
						}else{
							notWithMeTofetch=true;
							throw new InformationException("Application ID  <b>"+appId+"</b> is in under process.Currently it's with <b>"+user+"</b>.You need to fetch it to process.");
						}						
					}			
				}
		}		
	}
	
	public List<AbstractRequest> getRequestsInDraftStage(String officeCode, String userId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT APPLICATION_ID, to_char(CREATION_DATE, 'dd-mm-yyyy'), SERVICE_CODE "
				+ "FROM T_APPLICATION_DETAILS WHERE OFFICE_CODE=? AND REQUESTED_BY=? AND CURRENT_STAGE=1 AND CURRENT_STATUS=1");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		PreparedStatement statement = connection.prepareStatement(countQuery.toString());	
		statement.setString(1, officeCode);
		statement.setString(2, userId);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			statement.setString(1, officeCode);
			statement.setString(2, userId);
			statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
			statement.setInt(4, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();
		List<AbstractRequest> requestList = new ArrayList<AbstractRequest>();
		while(rs.next()) {
			int i=1;
			ProjectRequest temp = new ProjectRequest(connection);
			temp.setApplicationId(rs.getString(i++));
			temp.setCreationDate(rs.getString(i++));
			temp.setServiceId(rs.getString(i++));
			requestList.add(temp);
		}
		return requestList;
	}
	
	public void submitApplicationForProcessing(String applicationId, String fromOfficeCode, String fromUserId, String toOfficeCode) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("UPDATE T_APPLICATION_DETAILS SET CURRENT_STAGE=?, CURRENT_STATUS=?, SUB_STAGE=?, SUBMISSION_DATE=sysdate WHERE APPLICATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, "1");
		statement.setString(i++, "2");
		statement.setString(i++, "12");
		statement.setString(i++, applicationId);
		statement.executeUpdate();

		updatePCOfficeLevelStatus(applicationId, fromOfficeCode, "3", null,false);
		updatePCOfficeLevelStatus(applicationId, toOfficeCode, "1", null,false);
		updatePCOfficeUserDetails(applicationId, fromOfficeCode, fromUserId, null);
		updatePCUserLevelStatus(applicationId, fromOfficeCode, fromUserId, null, "3",false);
		updatePCPendingDetails(applicationId, toOfficeCode, null, null);
		updateApplicationStatusDetails(applicationId, "12", fromUserId, null);
		AutoNotifier notifier=new AutoNotifier();
		//notifier.pushAutoNotifications(applicationId, Integer.valueOf(1), "2", toOfficeCode, connection);
	}

	public void cancelApplication(String applicationId) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("UPDATE T_APPLICATION_DETAILS SET CURRENT_STATUS=? WHERE APPLICATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, "3");
		statement.setString(2, applicationId);
		statement.executeUpdate();
	}

	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("applicationId")) {
				orderbyClause.append(" ORDER BY APPLICATION_ID");
			}else if(sortColumn.equals("tempFileNo")) {
				orderbyClause.append(" ORDER BY TEMP_FILENO");
			}else if(sortColumn.equals("sectionFileNo")) {
				orderbyClause.append(" ORDER BY SECTION_FILENO");
			}else if(sortColumn.equals("submissionDate")) {
				orderbyClause.append(" ORDER BY SUBMISSION_DATE");
			}else if(sortColumn.equals("serviceName")) {
				orderbyClause.append(" ORDER BY SERVICE_CODE");
			}else if(sortColumn.equals("lastAccessedBy")) {
				orderbyClause.append(" ORDER BY LAST_ACTION_BY");
			}else if(sortColumn.equals("lastAccessedOn")) {
				orderbyClause.append(" ORDER BY A.ACTIVITY_ON");
			}
			if(orderbyClause.equals("") == false) {
				if(sortOrder != null && sortOrder.equals("") == false) {
					if(sortOrder.equalsIgnoreCase("ASC")) {
						order.append("ASC");
					}
					else if(sortOrder.equalsIgnoreCase("DESC")) {
						order.append("DESC");
					}
				}
				if(order.toString().equals("") == false) {
					orderbyClause.append(" "+order);
				}
				else {
					orderbyClause = null;
				}
			}
		}
		if(orderbyClause != null && orderbyClause.equals("") == false)
			query.append(orderbyClause);
		
		StringBuffer queryForPaging = null;
		if(pageNum == null || recordsPerPage == null)
			queryForPaging = query;
		else
			queryForPaging = new StringBuffer("WITH T1 AS ("+query+"), "
					+ "T2 AS (SELECT T1.*, ROWNUM RN FROM T1 WHERE ROWNUM<=?) SELECT * FROM T2 WHERE RN>=?");
		return queryForPaging.toString();
	}
	
	public List<Chat> getChat(ProjectRequest obj) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT A.BY_OFFICE_CODE,(SELECT OFFICE_ID FROM TM_OFFICE WHERE OFFICE_CODE=A.BY_OFFICE_CODE) AS BY_OFFICE_ID,"
				+ "(SELECT OFFICE_NAME FROM TM_OFFICE WHERE OFFICE_CODE=A.BY_OFFICE_CODE) || ', ' || (SELECT STATE_NAME FROM TM_OFFICE WHERE "
				+ "OFFICE_CODE=A.BY_OFFICE_CODE) AS BY_OFFICE,A.BY_USERID AS BYUSERID,(SELECT USER_NAME FROM TM_USER WHERE USER_ID=A.BY_USERID) AS BY_USERNAME, "
				+ "(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=(SELECT DESIGNATION_ID FROM TM_USER WHERE USER_ID=A.BY_USERID)) "
				+ "AS BY_USER_DEISGNATION,A.TO_OFFICE_CODE,(SELECT OFFICE_ID FROM TM_OFFICE WHERE OFFICE_CODE=A.TO_OFFICE_CODE) AS TO_OFFICE_ID,"
				+ "(SELECT OFFICE_NAME FROM TM_OFFICE WHERE OFFICE_CODE=A.TO_OFFICE_CODE) || ', ' || (SELECT STATE_NAME FROM TM_OFFICE WHERE "
				+ "OFFICE_CODE=A.TO_OFFICE_CODE) AS TO_OFFICE,A.TO_USERID AS TOUSERID,(SELECT USER_NAME FROM TM_USER WHERE USER_ID=A.TO_USERID)  AS TO_USERNAME,"
				+ "(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=(SELECT DESIGNATION_ID FROM TM_USER  WHERE USER_ID=A.TO_USERID)) "
				+ "AS TO_USER_DEISGNATION,A.STATUS_REMARK,to_char(A.STATUS_DATE,'DD-MM-YYYY HH24:Mi:SS') AS STATUS_DATE,A.CHAT_ID,A.STATUS_ID "
				+ "FROM T_PC_COMMUNICATION A WHERE APPLICATION_ID=? ORDER BY A.STATUS_DATE");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());		
		ResultSet rs = statement.executeQuery();
		List<Chat>  chatList = new ArrayList<Chat>();
		while(rs.next()) {	
			// Checking fromOfficeId==toOfficeId
			String toOffice=null;	
			String toOfficeId=null;
			String byOffice=null;	
			String byOfficeId=null;
			
			if(rs.getString(1)==null && rs.getString(4)==null){
				byOffice="APPLICANT";
				byOfficeId="99";
			}else{
				byOffice=rs.getString(3);
				byOfficeId=rs.getString(2);
			}				
			if(rs.getString(7)==null && rs.getString(10)==null){
				toOffice="APPLICANT";
				toOfficeId="99";
			}else{
				toOffice=rs.getString(9);
				toOfficeId=rs.getString(8);
			}
			if(rs.getString(2)==null){
				chatList.add(new Chat(rs.getString(1), byOfficeId, byOffice, rs.getString(4), rs.getString(5), rs.getString(6), 
						rs.getString(7), toOfficeId, toOffice, rs.getString(10), rs.getString(11), 
						rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),rs.getString(16)));
			}else if(rs.getString(2).equals(rs.getString(8))){  
				// checking fromOfficeId == LoggedIn User OfficeId  
				if(rs.getString(2).equals(obj.getOfficeId())){		
					//checking aso designation 
					/*if(obj.getOfficeId().equals("1") && obj.getUserDesignationId().equals("10")){						
						String fromUserId = rs.getString(4); String toUserId = rs.getString(10);
						if(fromUserId.equals(obj.getUserId()) || toUserId.equals(obj.getUserId())){
							chatList.add(new Chat(rs.getString(1), byOfficeId, byOffice, rs.getString(4), rs.getString(5), rs.getString(6), 
									rs.getString(7), toOfficeId, toOffice, rs.getString(10), rs.getString(11), 
									rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),rs.getString(16)));
						}
					}
					else{*/
						chatList.add(new Chat(rs.getString(1), byOfficeId, byOffice, rs.getString(4), rs.getString(5), rs.getString(6), 
								rs.getString(7), toOfficeId, toOffice, rs.getString(10), rs.getString(11), 
								rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),rs.getString(16)));		
				//	}
									
				}
			}
			else{
				chatList.add(new Chat(rs.getString(1), byOfficeId, byOffice, rs.getString(4), rs.getString(5), rs.getString(6), 
						rs.getString(7), toOfficeId, toOffice, rs.getString(10), rs.getString(11), 
						rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15),rs.getString(16)));
			}						
		}
		return chatList;		
	}
	
	public List<Chat> getChatAttachments(ProjectRequest obj) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT  A.APPLICATION_ID,A.CHAT_ID,B.NAME,B.ROWID FROM T_PC_COMMUNICATION A,T_PC_CHAT_ATTACHMENT B WHERE "
				+ "A.CHAT_ID=B.CHAT_ID AND A.APPLICATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());		
		ResultSet rs = statement.executeQuery();
		List<Chat>  chatList = new ArrayList<Chat>();
		while(rs.next()) {			
			chatList.add(new Chat(rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4)));			
		}
		return chatList;
	}
	
	
	
	public List<Chat> getOlderChatAttachments(Chat obj) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT  A.APPLICATION_ID,A.CHAT_ID,B.NAME,B.ROWID FROM T_PC_COMMUNICATION A,T_PC_CHAT_ATTACHMENT B WHERE "
				+ "A.CHAT_ID=B.CHAT_ID AND A.APPLICATION_ID=? AND A.SUB_STAGE_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());
		statement.setString(2, obj.getSubStageId());
		ResultSet rs = statement.executeQuery();
		List<Chat>  chatList = new ArrayList<Chat>();
		while(rs.next()) {			
			chatList.add(new Chat(rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4)));			
		}
		return chatList;
	}
	
	public List<List3> getProjectDocuments(Chat obj) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		StringBuffer query = new StringBuffer("SELECT A.ROWID,(SELECT DOC_TYPE_DESC FROM TM_PROJECT_DOCUMENT_TYPE WHERE DOC_TYPE_ID=A.DOC_TYPE_ID), "

				+ "(SELECT SUB_STAGE_DESC FROM TM_APPLICATION_SUB_STAGE WHERE SUB_STAGE_ID=B.SUB_STAGE_ID) FROM T_PROJECT_DOC_DETAILS A,"
				+ "TM_SUBSTAGE_DOCUMENTS B WHERE A.DOC_TYPE_ID=B.DOC_TYPE_ID AND A.APPLICATION_ID=? AND "
				+ "B.PROPOSAL_TYPE_ID=(SELECT PROPOSAL_ID FROM T_PROJECT_REQUEST WHERE APPLICATION_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());
		statement.setString(2, obj.getApplicationId());
		ResultSet rs = statement.executeQuery();	
		List<List3> projDocList=new ArrayList<List3>();
		while(rs.next()) {			
			projDocList.add(new List3(rs.getString(1), rs.getString(2), rs.getString(3)));	
		}	
		return projDocList;
	}	
	
	public List<List2> forwardUserList(String officeCode,String userLevel,String userId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT A.USER_ID,A.USER_NAME || ' (' || A.USER_ID || ') [' || (SELECT SHORT_DESIGNATION FROM TM_DESIGNATION "
				+ "WHERE DESIGNATION_ID=A.DESIGNATION_ID) || ']' AS DESIGNATION FROM TM_USER A,TM_OFFICE B WHERE A.OFFICE_CODE=? AND "
				+ "A.OFFICE_CODE=B.OFFICE_CODE 	AND A.USER_LEVEL=? AND A.STATUS_ID=0  AND A.USER_ID IN(SELECT USER_ID FROM TM_USER_SECTION WHERE SECTION_ID IN "
				+ "(SELECT SECTION_ID FROM TM_USER_SECTION WHERE OFFICE_CODE=? AND USER_ID=?)) order by A.user_name");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, officeCode);		
		statement.setString(2, userLevel);
		statement.setString(3, officeCode);
		statement.setString(4, userId);
		ResultSet rs = statement.executeQuery();
		List<List2>  userList = new ArrayList<List2>();
		while(rs.next()) {			
			userList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return userList;
	}
	
	public List<List2> forwardUserList(String officeCode,String userLevel,String userId,String appId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		PreparedStatement statement=null;
		StringBuffer query3 = new StringBuffer("SELECT  SERVICE_CODE,STATE,DISTRICT FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
 		statement = connection.prepareStatement(query3.toString()); 			
 		statement.setString(1, appId);
 		ResultSet rs2=statement.executeQuery();
 		String serviceCode=null; 		
 		if(rs2.next()){
 			serviceCode=rs2.getString(1); 			
 		}
 		statement.close();
 		rs2.close();
		StringBuffer query = new StringBuffer("SELECT A.USER_ID,A.USER_NAME || ' (' || A.USER_ID || ') [' || (SELECT SHORT_DESIGNATION FROM TM_DESIGNATION "
				+ "WHERE DESIGNATION_ID=A.DESIGNATION_ID) || ']' AS DESIGNATION FROM TM_USER A,TM_OFFICE B WHERE A.OFFICE_CODE=? AND 	"
				+ "A.OFFICE_CODE=B.OFFICE_CODE AND A.USER_LEVEL=? AND A.STATUS_ID=0 AND A.USER_ID IN (SELECT USER_ID FROM TM_USER_SECTION WHERE SECTION_ID IN "
				+ "(SELECT SECTION_ID FROM TM_USER_SECTION WHERE OFFICE_CODE=? AND USER_ID=?) UNION SELECT USER_ID FROM TM_USER WHERE "
				+ "USER_ID NOT IN(SELECT USER_ID FROM TM_USER_SECTION WHERE OFFICE_CODE=?) UNION SELECT USER_ID FROM TM_USER_SECTION "
				+ "WHERE SECTION_ID IN(SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE SERVICE_CODE=?))");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, officeCode);		
		statement.setString(2, userLevel);
		statement.setString(3, officeCode);
		statement.setString(4, userId);
		statement.setString(5, officeCode);
		statement.setString(6, serviceCode);
		ResultSet rs = statement.executeQuery();
		List<List2>  userList = new ArrayList<List2>();
		while(rs.next()) {			
			userList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return userList;
	}
	
	public List<List2> sectionUserList(String officeCode,String userLevel,String sectionId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT A.USER_ID,A.USER_NAME || ' (' || A.USER_ID || ') [' || (SELECT SHORT_DESIGNATION FROM TM_DESIGNATION "
				+ "WHERE DESIGNATION_ID=A.DESIGNATION_ID) || ']' AS DESIGNATION FROM TM_USER A,TM_OFFICE B WHERE A.OFFICE_CODE=? AND "
				+ "A.OFFICE_CODE=B.OFFICE_CODE 	AND A.USER_LEVEL=? AND A.STATUS_ID=0  AND A.USER_ID IN(SELECT USER_ID FROM TM_USER_SECTION WHERE SECTION_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, officeCode);		
		statement.setString(2, userLevel);
		statement.setString(3, sectionId);		
		ResultSet rs = statement.executeQuery();
		List<List2>  userList = new ArrayList<List2>();
		while(rs.next()) {			
			userList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return userList;
	}
	
	public void getUserRoleInfo(String userid,String officeId) throws Exception{
		StringBuffer query=null;PreparedStatement statement=null;ResultSet rs=null;
		processingOfficialFlag="N";
		grantingOfficalFlag="N";
		clarificationOfficialFlag="N";
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		if(officeId.equals("1")){ // Only for MHA			
			query=new StringBuffer("SELECT ROLE_ID FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID IN(3) AND RECORD_STATUS=0");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, userid);		
			rs=statement.executeQuery();
			if(rs.next()){
				if(rs.getString(1).equals("3")){
					processingOfficialFlag="Y";
				}				
			}	
			rs.close();
			statement.close();
			query=new StringBuffer("SELECT ROLE_ID FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID IN(9) AND RECORD_STATUS=0");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, userid);		
			rs=statement.executeQuery();
			if(rs.next()){				
				if(rs.getString(1).equals("9")){
					grantingOfficalFlag="Y";
				}
			}	
			rs.close();
			statement.close();
			query=new StringBuffer("SELECT ROLE_ID FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID IN(10) AND RECORD_STATUS=0");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, userid);		
			rs=statement.executeQuery();
			if(rs.next()){				
				if(rs.getString(1).equals("10")){
					clarificationOfficialFlag="Y";
				}
			}	
			rs.close();
			statement.close();
		}		
	}
	
	public String checkClarificationReminderStatus(String appId,String officeId) throws Exception{
		String status="N";
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(officeId.equals("1")){  // Only for MHA
			// Checking Logged in User having Red Flag Add Role
			StringBuffer query=new StringBuffer("SELECT * FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=? AND CURRENT_STAGE=2 AND CURRENT_STATUS=8");
			PreparedStatement statement = connection.prepareStatement(query.toString());	
			statement.setString(1,appId);		
			ResultSet rs=statement.executeQuery();
			if(rs.next()){
				status="Y";
			}
		}
		return status;
	}
	
	public void getTimingInfo(ProjectRequest obj) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}		
		StringBuffer query = new StringBuffer("SELECT FN_GET_PROCESSING_TIME(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());		
		ResultSet rs = statement.executeQuery();	
		String result=null;
		if(rs.next()) {	
			result=rs.getString(1);	
		}	
		if(result!=null){
			StringTokenizer st = new StringTokenizer(result,"-");  
			runningStatus=st.nextToken();
		    daysCount=st.nextToken();
		}
	    
	    // Getting Final Status from T_PC_FINAL_STATUS
	    StringBuffer query1 = new StringBuffer("SELECT STATUS FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		PreparedStatement statement1 = connection.prepareStatement(query1.toString());	
		statement1.setString(1, obj.getApplicationId());		
		statement1.setString(2, obj.getToOfficeInfo());		
		ResultSet rs1 = statement1.executeQuery();	
		if(rs1.next())
			appFinalStatus=rs1.getString(1);
		
		// Getting validity Information in case of Renewal
		// GETTING SERVICE CODE 
		StringBuffer query3 = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query3.toString()); 			
		statement.setString(1, obj.getApplicationId());
		ResultSet rs2=statement.executeQuery();
		String serviceCode=null; 		
		if(rs2.next()){
			serviceCode=rs2.getString(1); 			
		}
		if(serviceCode.equals("03")){
			StringBuffer query4 = new StringBuffer("SELECT TO_CHAR((CASE WHEN VALID_TO IS NULL THEN CASE WHEN REG_DATE IS NULL THEN SYSDATE-1 ELSE "
					+ "ADD_MONTHS(REG_DATE,60)-1 END ELSE VALID_TO END)+1,'DD-MM-YYYY') AS VALID_FROM,TO_CHAR(ADD_MONTHS((CASE WHEN VALID_TO IS NULL THEN CASE WHEN REG_DATE IS NULL "
					+ "THEN SYSDATE-1 ELSE ADD_MONTHS(REG_DATE,60)-1 END ELSE VALID_TO END),60),'DD-MM-YYYY') AS VALID_TO,TO_CHAR(SYSDATE,'DD-MM-YYYY') FROM FC_INDIA WHERE "
					+ "RCN=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)");
			statement = connection.prepareStatement(query4.toString()); 			
			statement.setString(1, obj.getApplicationId());
			ResultSet rs3=statement.executeQuery();			 		
			if(rs3.next()){
				//validityFrom=rs3.getString(1); 			
				validityUpTo=rs3.getString(2);
				currentDate=rs3.getString(3);
			}
			
			StringBuffer query5 = new StringBuffer("WITH T1 AS(  SELECT CASE WHEN VALID_TO IS NULL THEN CASE WHEN REG_DATE IS NULL THEN NULL "
								+ "ELSE ADD_MONTHS(REG_DATE,60)-1 END ELSE VALID_TO END AS VALID_TO FROM FC_INDIA WHERE "
								+ "RCN=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?) ) SELECT TO_CHAR(CASE WHEN VALID_TO "
								+ "IS NULL THEN TO_DATE('01-11-2016','DD-MM-YYYY') ELSE CASE WHEN VALID_TO < TRUNC(TO_DATE('30-09-2016','DD-MM-YYYY')) "
								+ "THEN TO_DATE('01-11-2016','DD-MM-YYYY') ELSE VALID_TO+1 END END,'DD-MM-YYYY') AS VALID_FROM,"
								+ "TO_CHAR(ADD_MONTHS((CASE WHEN VALID_TO IS NULL THEN TO_DATE('01-11-2016','DD-MM-YYYY') ELSE CASE WHEN VALID_TO < TRUNC(TO_DATE('30-09-2016','DD-MM-YYYY')) "
								+ "THEN TO_DATE('01-11-2016','DD-MM-YYYY') ELSE VALID_TO+1 END END),60)-1,'DD-MM-YYYY') AS VALID_TO FROM T1");			
			statement = connection.prepareStatement(query5.toString()); 			
			statement.setString(1, obj.getApplicationId());
			ResultSet rs4=statement.executeQuery();			 		
			if(rs4.next()){
				validityFrom=rs4.getString(1);	
				validityUpTo=rs4.getString(2);
			}
		}
		if(serviceCode.equals("02")){
			StringBuffer queryPP = new StringBuffer("SELECT NATUREANDVALUE_VALUE,NATUREANDVALUE_VALUE || ' ' || (SELECT CURR_NAME FROM TM_CURRENCY WHERE CURR_CODE=NATUREANDVALUE_CURRENCY) AS CURRENCY,"
					+ "(SELECT CURR_NAME FROM TM_CURRENCY WHERE CURR_CODE=NATUREANDVALUE_CURRENCY) FROM FC_FC1A_DONOR WHERE UNIQUE_FILENO=?");
			statement = connection.prepareStatement(queryPP.toString()); 			
			statement.setString(1, obj.getApplicationId());
			ResultSet rsPP=statement.executeQuery();			 		
			if(rsPP.next()){				 			
				ppAmount=rsPP.getString(1);		
				ppAmountDesc=rsPP.getString(2);
				ppAmountCurrency=rsPP.getString(3);
			}
		}
	}  
	
	public void getRedFlagStatus(String appId) throws Exception{
		StringBuffer query=null;PreparedStatement statement=null;String scode=null;ResultSet rs=null;
		// Getting RCN
		query=new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			scode=rs.getString(1);
		}		
		if(!(scode.equals("03") || scode.equals("06")))
			return;		
		// Getting RCN from entry tables
		String entryTable=null,fieldName=null,rcn=null;
		if(scode.equals("03")){
			entryTable="FC_FC5_ENTRY_NEW1";
			fieldName="ASSO_FCRA_RCN";
		}else if(scode.equals("06")){
			entryTable="FC_FC6_FORM";
			fieldName="RCN";
		}
		query=new StringBuffer("SELECT "+fieldName+" FROM "+entryTable+" WHERE UNIQUE_FILENO=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			rcn=rs.getString(1);
		}
		// Checking whether in adverse list		
		query=new StringBuffer("SELECT * FROM T_RED_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, rcn);	
		rs=statement.executeQuery();
		if(rs.next()){
			redFlag="YES";
			getRedFlagCategory(rcn);
		}else{
			redFlag="NO";
		}		
		// Checking RED FLAG CATEGORY_TYPE	
		if(redFlag.equals("YES")){
			query=new StringBuffer("SELECT CATEGORY_TYPE FROM TM_RED_FLAG_CATEGORY WHERE CATEGORY_CODE=(SELECT RED_FLAG_CATEGORY FROM T_RED_FLAG_STATUS_HISTORY WHERE RCN=? AND STATUS=0 AND STATUS_DATE=(SELECT MAX(STATUS_DATE) FROM T_RED_FLAG_STATUS_HISTORY WHERE  RCN=? AND STATUS=0))");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, rcn);	
			statement.setString(2, rcn);
			rs=statement.executeQuery();
			if(rs.next()){
				if(rs.getString(1)== null || rs.getString(1).equals("2")){
					redFlagYELLOWCategory="YES";
				}
				else if(rs.getString(1).equals("1")){
					redFlagREDCategory="YES";
				}
			}else{
				redFlagYELLOWCategory="YES";
			}			
		}		
	}
	public void getBlueFlagStatus(String appId) throws Exception{
		StringBuffer query=null;PreparedStatement statement=null;String scode=null;ResultSet rs=null;
		// Getting RCN
		query=new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			scode=rs.getString(1);
		}		
		if(!(scode.equals("03") || scode.equals("06")))
			return;		
		// Getting RCN from entry tables
		String entryTable=null,fieldName=null,rcn=null;
		if(scode.equals("03")){
			entryTable="FC_FC5_ENTRY_NEW1";
			fieldName="ASSO_FCRA_RCN";
		}else if(scode.equals("06")){
			entryTable="FC_FC6_FORM";
			fieldName="RCN";
		}
		query=new StringBuffer("SELECT "+fieldName+" FROM "+entryTable+" WHERE UNIQUE_FILENO=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			rcn=rs.getString(1);
		}
		// Checking whether in adverse list		
		query=new StringBuffer("SELECT * FROM T_BLUE_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, rcn);	
		rs=statement.executeQuery();
		if(rs.next()){
			blueFlag="YES";
			getRedFlagCategory(rcn);
		}else{
			blueFlag="NO";
		}		
				
	}
	
	private void getRedFlagCategory(String rcn) throws Exception{
		StringBuffer query=new StringBuffer("SELECT RED_FLAG_CATEGORY,(SELECT CATEGORY_DESC FROM TM_RED_FLAG_CATEGORY WHERE CATEGORY_CODE=RED_FLAG_CATEGORY) "
				+ "FROM T_RED_FLAG_STATUS_HISTORY WHERE RCN=? AND STATUS_DATE=(SELECT MAX(STATUS_DATE) FROM T_RED_FLAG_STATUS_HISTORY WHERE RCN=?) AND STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, rcn);	
		statement.setString(2, rcn);
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			redFlagCategoryCode=rs.getString(1);
			redFlagCategory=rs.getString(2);
		}
	}
	
	public List<RedFlagAssociations> getRedFlagStatusByName(String appId) throws Exception{
		StringBuffer query=null;PreparedStatement statement=null;String scode=null;ResultSet rs=null;
		String applicantName = null;
		
		query=new StringBuffer("SELECT SERVICE_CODE, APPLICANT_NAME FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			scode = rs.getString(1);
			applicantName = rs.getString(2);
		}
		// Getting redflag list
		List<RedFlagAssociations> redFlagAssociations = new ArrayList<RedFlagAssociations>();
		redFlagAssociations.clear();
		if(scode.equals("01") || scode.equals("02") || scode.equals("03") || scode.equals("06")) {
			/*query=new StringBuffer("SELECT ASSO_ID, ASSO_NAME, ADDRESS, STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=STATE) STATENAME "
					+ "FROM T_RED_FLAG_NGO_NAME where ASSO_ID IN (SELECT * FROM TABLE("
					+ "PKG_RED_FLAG_ALERT_SYSTEM.FN_GET_ALERTS_NAME(?)))");*/
			query=new StringBuffer("WITH T1 AS (SELECT RCN, ASSO_NAME, (CASE WHEN NEW_OLD='N' THEN ASSO_ADDRESS || ',' || ASSO_TOWN_CITY || ASSO_PIN ELSE ADD1||', '|| ADD2 || "
					+ "','||ADD3 || '-'|| ASSO_PIN END) AS ASOADDRESS,SUBSTR(STDIST,1,2) STATE, (SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(STDIST,1,2)) STATENAME "
					+ "FROM FC_INDIA WHERE RCN IN (SELECT RCN FROM T_RED_FLAGGED_ASSOCIATIONS) ) SELECT TO_CHAR(ASSO_ID), ASSO_NAME, ADDRESS, STATE, "
					+ "(SELECT SNAME FROM TM_STATE WHERE SCODE=STATE) STATENAME FROM T_RED_FLAG_NGO_NAME WHERE ASSO_ID IN "
					+ "(SELECT * FROM TABLE(PKG_RED_FLAG_ALERT_SYSTEM.FN_GET_ALERTS_NAME(?)))  "
					+ " UNION "
					+ "SELECT * FROM T1 WHERE UPPER(ASSO_NAME) = UPPER(?)");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, applicantName);	
			statement.setString(2, applicantName);
			rs=statement.executeQuery();
			while(rs.next()){
				RedFlagAssociations temp = new RedFlagAssociations();
				int i = 1;
				
				temp.setAssoId(rs.getString(i++));
				temp.setAssoName(rs.getString(i++));
				temp.setAssoAddress(rs.getString(i++));
				temp.setAssoState(rs.getString(i++));
				temp.setAssoStateName(rs.getString(i++));
				redFlagAssociations.add(temp);
			}
		}		
		return redFlagAssociations;
	}
	
	public List<RedFlagDonors> getRedFlagStatusByDonorName(String appId) throws Exception{
		StringBuffer query=null;PreparedStatement statement=null;String scode=null;ResultSet rs=null;
		String applicantName = null;
		
		query=new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			scode = rs.getString(1);
		}
		// Getting redflag list
		List<RedFlagDonors> redFlagDonors = new ArrayList<RedFlagDonors>();
		redFlagDonors.clear();
		if(scode.equals("02")) {
			query=new StringBuffer("SELECT DONOR_ID, DONOR_NAME, COUNTRY, (SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=COUNTRY) COUNTRYNAME "
					+ "FROM T_RED_FLAG_DONOR_NAME WHERE DONOR_ID IN (SELECT * FROM TABLE("
					+ "PKG_RED_FLAG_ALERT_SYSTEM.FN_GET_ALERTS_DONOR(?)))");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, appId);	
			rs=statement.executeQuery();
			while(rs.next()){
				RedFlagDonors temp = new RedFlagDonors();
				int i = 1;
				
				temp.setDonorId(rs.getInt(i++));
				temp.setDonorName(rs.getString(i++));
				temp.setDonorCountry(rs.getString(i++));
				temp.setDonorCountryName(rs.getString(i++));
				redFlagDonors.add(temp);
			}
		}
		
		return redFlagDonors;
	}
	
	public String getCurrentUser(String appId,String office) throws Exception{
		PreparedStatement statement=null;
		String user=null;
		StringBuffer query = new StringBuffer("SELECT (SELECT USER_NAME FROM TM_USER WHERE USER_ID=T_PC_OFFICE_USER_DETAILS.USER_ID)||'( ' || USER_ID || ' )' FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, appId);		
		statement.setString(2, office);
		ResultSet rs = statement.executeQuery();		
		if(rs.next()) {			
			user=rs.getString(1);
		}else
			user="NOT ASSIGNED YET TO ANYBODY";
		return user;
	}
	
	public List<List2> officeStatusList(ProjectRequest obj) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT (SELECT OFFICE_NAME FROM TM_OFFICE WHERE OFFICE_CODE=A.OFFICE_CODE) "
				+ "|| ' [' || A.OFFICE_CODE || ']' AS OFFICE,A.STATUS FROM T_PC_OFFICE_LEVEL_FINAL_STATUS A WHERE A.APPLICATION_ID=? "
				+ "AND A.OFFICE_CODE NOT IN(SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=1)");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());		
		ResultSet rs = statement.executeQuery();
		List<List2>  statusList = new ArrayList<List2>();
		while(rs.next()) {	
			String status=null;
			String office=rs.getString(1);			
			String statusId=rs.getString(2);
			if(statusId==null)
				status="<span class=\"text-info text-center\"><b>UNDER PROCESS</b></span>";
			else if(statusId.equals("5"))
				status="<span class=\"text-success text-center\"><b>DISPOSED OFF</b></span>";
			else if(statusId.equals("6"))
				status="<span class=\"text-danger text-center\"><b>NOT RECOMMENDED</b></span>";
			else if(statusId.equals("11"))
				status="<span class=\"text-warning text-center\"><b>ON HOLD</b></span>";
			statusList.add(new List2(office, status));			
		}
		return statusList;
	} 
	
	public List<List2> getOfficeSectionList(String officeCode,String userId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SECTION_ID,SECTION_NAME FROM TM_PC_SECTION WHERE OFFICE_CODE=? AND "
				+ "SECTION_ID NOT IN(SELECT SECTION_ID FROM TM_USER_SECTION WHERE OFFICE_CODE=? AND USER_ID=?) AND RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, officeCode);		
		statement.setString(2, officeCode);
		statement.setString(3, userId);
		ResultSet rs = statement.executeQuery();
		List<List2>  sectionList = new ArrayList<List2>();
		while(rs.next()) {			
			sectionList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return sectionList;
	} 
	
	public List<List2> getUserServiceList(String officeCode,String userId,String sectionId) throws Exception{
		StringBuffer query=null;
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		if(sectionId==null || sectionId.equals("")){
			Boolean isSectionExist=false;
			query = new StringBuffer("SELECT count(1) FROM TM_USER_SECTION WHERE OFFICE_CODE=?");
			PreparedStatement statementService = connection.prepareStatement(query.toString());	
			statementService.setString(1, officeCode);	
			ResultSet rsService = statementService.executeQuery();
			if(rsService.next()){
				if(rsService.getInt(1)>0){
					isSectionExist=true;
				}
			}
			rsService.close();
			statementService.close();
			
			if(isSectionExist==true){
				query = new StringBuffer("SELECT DISTINCT SERVICE_CODE,SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE IN(SELECT SERVICE_CODE FROM "
						+ "TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SECTION_ID IN(SELECT SECTION_ID FROM TM_USER_SECTION WHERE OFFICE_CODE=? "
						+ "AND USER_ID=?))");
				statement = connection.prepareStatement(query.toString());	
				statement.setString(1, officeCode);		
				statement.setString(2, officeCode);
				statement.setString(3, userId);
			}else{
				query = new StringBuffer("SELECT DISTINCT SERVICE_CODE,SERVICE_DESC FROM TM_SERVICE WHERE RECORD_STATUS=0");
				statement = connection.prepareStatement(query.toString());			
			}
		}else{
			query = new StringBuffer("SELECT DISTINCT SERVICE_CODE,SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE IN(SELECT SERVICE_CODE FROM "
					+ "TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SECTION_ID=?)");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, officeCode);			
			statement.setString(2, sectionId);
		}
		
		ResultSet rs = statement.executeQuery();
		List<List2>  serviceList = new ArrayList<List2>();
		while(rs.next()) {			
			serviceList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return serviceList;
	} 
	
	public List<List2> getUserSectionList(String officeCode,String userId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SECTION_ID,(SELECT SECTION_NAME FROM TM_PC_SECTION WHERE SECTION_ID=TM_USER_SECTION.SECTION_ID AND "
				+ "OFFICE_CODE=?) AS SECTION_NAME FROM TM_USER_SECTION WHERE USER_ID=? AND OFFICE_CODE=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, officeCode);
		statement.setString(2, userId);
		statement.setString(3, officeCode);		
		ResultSet rs = statement.executeQuery();
		List<List2>  sectionList = new ArrayList<List2>();
		while(rs.next()) {			
			sectionList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return sectionList;
	} 
	
	public List<List2> getOfficeUserList(String officeCode) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT USER_ID,(SELECT USER_NAME||' [ '||BB.SHORT_DESIGNATION||' ] ' FROM TM_USER AA, TM_DESIGNATION BB WHERE USER_ID=A.USER_ID "
							+ "AND AA.DESIGNATION_ID=BB.DESIGNATION_ID) FROM TM_USER A WHERE STATUS_ID=0 AND OFFICE_CODE=? AND USER_ID!=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, officeCode);
		statement.setString(2, officeCode);				
		ResultSet rs = statement.executeQuery();
		List<List2>  userList = new ArrayList<List2>();
		while(rs.next()) {			
			userList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return userList;
	} 
	
	public List<List2> getStateList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SCODE,SNAME FROM TM_STATE WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<List2>  stateList = new ArrayList<List2>();
		while(rs.next()) {			
			stateList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return stateList;
	} 
	
	public List<List2> forwardOfficeList(ProjectRequest obj) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT A.OFFICE_CODE,A.OFFICE_NAME||', ' ||A.STATE_NAME FROM TM_OFFICE A "
				+ "WHERE A.OFFICE_ID IN(1,2,3) AND A.OFFICE_ID!=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getToOffice());		
		ResultSet rs = statement.executeQuery();
		List<List2>  officeList = new ArrayList<List2>();
		while(rs.next()) {			
			officeList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return officeList;
	}
	
	public List<List2> forwardOfficeList(String officeId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_CODE,OFFICE_NAME||', ' ||STATE_NAME FROM TM_OFFICE WHERE OFFICE_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, officeId);		
		ResultSet rs = statement.executeQuery();
		List<List2>  officeList = new ArrayList<List2>();
		while(rs.next()) {			
			officeList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return officeList;
	}
	
	 

	public Boolean checkFlag(String stageid,String appId,String office) throws Exception{
		Boolean flag=false;
		PreparedStatement statement=null;
		StringBuffer query2 = new StringBuffer("SELECT  B.OFFICE_ID FROM TM_PROJECT_DOCUMENT_TYPE A,TM_SUBSTAGE_OFFICETYPE B,"
				+ "TM_SUBSTAGE_DOCUMENTS C WHERE B.PROPOSAL_TYPE_ID=C.PROPOSAL_TYPE_ID AND B.SUB_STAGE_ID=C.SUB_STAGE_ID AND B.SUB_STAGE_ID=? AND "
				+ "B.PROPOSAL_TYPE_ID=(SELECT PROPOSAL_ID FROM T_PROJECT_REQUEST WHERE APPLICATION_ID=?) AND C.DOC_TYPE_ID=A.DOC_TYPE_ID");
		statement = connection.prepareStatement(query2.toString());
		statement.setString(1, stageid);		
		statement.setString(2, appId);
		ResultSet rs2 = statement.executeQuery();		
		while(rs2.next()) {			
			if(rs2.getString(1).equals(office))
				flag=true;
			else
				flag=false;
		}
		return flag;
	}
	
	public String generateChatId(String officeCode) throws Exception{
		String chatId=null;
		StringBuffer query = new StringBuffer("SELECT FN_GEN_CHATID(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1,officeCode);
		ResultSet rs=statement.executeQuery();
		if(rs.next())
			chatId=rs.getString(1);
		return chatId;
	}
	
	public String generateShowCauseId(String officeCode) throws Exception{
		String chatId=null;
		StringBuffer query = new StringBuffer("SELECT FN_GEN_SCN_ID(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1,officeCode);
		ResultSet rs=statement.executeQuery();
		if(rs.next())
			chatId=rs.getString(1);
		return chatId;
	}
	
	private Boolean checkUserRoleForGranting(String userId) throws Exception{
		Boolean status=false;
		PreparedStatement statement=null;		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		// Checking User Role
		StringBuffer query3 = new StringBuffer("SELECT  ROLE_ID FROM TM_USER_ROLE WHERE USER_ID=?");
		statement = connection.prepareStatement(query3.toString()); 			
		statement.setString(1, userId);
		ResultSet rs=statement.executeQuery();		 		
		while(rs.next()){
			 if(rs.getString(1).equals("9")){
				 status=true;
			 }
		}
		return status;		
	}
	
	private Boolean checkUserForProcessing(String userId) throws Exception{
		Boolean status=false;
		PreparedStatement statement=null;		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		// Checking User Role
		StringBuffer query3 = new StringBuffer("SELECT  ROLE_ID FROM TM_USER_ROLE WHERE USER_ID=?");
		statement = connection.prepareStatement(query3.toString()); 			
		statement.setString(1, userId);
		ResultSet rs=statement.executeQuery();		 		
		while(rs.next()){
			 if(rs.getString(1).equals("3") || rs.getString(1).equals("9")){		// Processing Role or Granting Role
				 status=true;
			 }
		}
		return status;		
	}
	
	private Boolean checkUserRoleForClarification(String userId) throws Exception{
		Boolean status=false;
		PreparedStatement statement=null;		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		// Checking User Role
		StringBuffer query3 = new StringBuffer("SELECT  ROLE_ID FROM TM_USER_ROLE WHERE USER_ID=?");
		statement = connection.prepareStatement(query3.toString()); 			
		statement.setString(1, userId);
		ResultSet rs=statement.executeQuery();		 		
		while(rs.next()){
			 if(rs.getString(1).equals("10")){
				 status=true;
			 }
		}
		return status;		
	}
	
	//ACCEPT APPLICATION
	public String acceptApplication(Chat obj) throws Exception{
		PreparedStatement statement=null;		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		String chatId=null;
		// GETTING SERVICE CODE 
		StringBuffer query3 = new StringBuffer("SELECT  SERVICE_CODE,SECTION_FILENO FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query3.toString()); 			
		statement.setString(1, obj.getApplicationId());
		ResultSet rs2=statement.executeQuery();
		String service=null,sectionFileNo=null; 		
		if(rs2.next()){
			service=rs2.getString(1); 	
			sectionFileNo=rs2.getString(2);
		}
		connection.setAutoCommit(false);	    				
	    updatePCOfficeUserDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);		
	    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);	    
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"1",false);	// RECEIVER
	    Boolean flag=flagCheckPCOfficeLevelStatus(obj.getApplicationId(), obj.getToOfficeCode());
	    if(flag==true)
	    	updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,true);
	    else
	    	updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,false);	
	    if(obj.getToOfficeId().equals("1"))
	    	updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getToOfficeCode(),null,chatId,false);
	    
	    if(obj.getToOfficeId().equals("1")){
			    StringBuffer query = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?");
		 		statement = connection.prepareStatement(query.toString()); 			
		 		statement.setString(1, obj.getApplicationId());
		 		ResultSet rs=statement.executeQuery();
		 		String serviceCode=null;
		 		if(rs.next())
		 			serviceCode=rs.getString(1); 		
		 		if(serviceCode.equals("01") || serviceCode.equals("02") || serviceCode.equals("05") || serviceCode.equals("03")){
		 			StringBuffer query2 = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID in(2) AND RECORD_STATUS=0");
		 			statement = connection.prepareStatement(query2.toString()); 					
		 			ResultSet rs1=statement.executeQuery();
		 			String office=null;
		 			while(rs1.next()){
		 				office=rs1.getString(1);		 				
		 			} 				
		 			updatePCOfficeLevelStatus(obj.getApplicationId(),office,"1",chatId,false);	
	 				updatePCOfficeLevelFinalStatus(obj.getApplicationId(),office,null,chatId,false);
	 				
	 				// Getting section for inserting row in to PC_Section
	 				StringBuffer query4 = new StringBuffer("SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SERVICE_CODE="
	 			    		+ "(SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?)");
	 				statement = connection.prepareStatement(query4.toString()); 
	 				statement.setString(1, office);
	 				statement.setString(2, obj.getApplicationId());
	 				ResultSet rs4=statement.executeQuery();	
	 				String sectionId=null;
	 				if(rs4.next())
	 					sectionId=rs4.getString(1);
	 			    updatePCSectionDetails(obj.getApplicationId(), office, sectionId, chatId, false);
	 			    
	 			    // Inserting row for RAW in case of RENEWAL //
		 			    if(serviceCode.equals("01") || serviceCode.equals("02") || serviceCode.equals("03")){ 	// Only Renewal, PP, Registration
		 			    	Boolean proceedFlag=false;
		 			    	StringBuffer queryRen = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID in(3) AND RECORD_STATUS=0");
				 			statement = connection.prepareStatement(queryRen.toString()); 					
				 			ResultSet rsRen=statement.executeQuery();
				 			String officeRAW=null;
				 			while(rsRen.next()){
				 				officeRAW=rsRen.getString(1);		 				
				 			}	
				 			StringBuffer queryForRaw=null;PreparedStatement statementForRaw=null;ResultSet rsForRaw=null;int rowCount=0;
		 			    	if(serviceCode.equals("01")){
		 			    		queryForRaw=new StringBuffer("SELECT COUNT(1) FROM FC_FC8_COMMITTEE WHERE UNIQUE_FILENO=? AND NATIONALITY NOT IN (180)");
		 			    		statementForRaw=connection.prepareStatement(queryForRaw.toString());
		 			    		statementForRaw.setString(1, obj.getApplicationId());
		 			    		rsForRaw=statementForRaw.executeQuery();		 			    		
		 			    		if(rsForRaw.next()){
		 			    			rowCount=rsForRaw.getInt(1);		 			    			
		 			    		}
		 			    		rsForRaw.close();
			 			    	statementForRaw.close();
		 			    	}else if(serviceCode.equals("02")){
		 			    		rowCount=1; // Forwarding all cases
		 			    		
		 			    		/*queryForRaw=new StringBuffer("SELECT COUNT(1) FROM FC_FC1A_COMMITTEE_NEW WHERE UNIQUE_FILENO=? AND NATIONALITY NOT IN (180)");
		 			    		statementForRaw=connection.prepareStatement(queryForRaw.toString());
		 			    		statementForRaw.setString(1, obj.getApplicationId());
		 			    		rsForRaw=statementForRaw.executeQuery();		 			    		
		 			    		if(rsForRaw.next()){
		 			    			rowCount=rsForRaw.getInt(1);
		 			    		}*/
		 			    	}else if(serviceCode.equals("03")){
		 			    		queryForRaw=new StringBuffer("SELECT COUNT(1) FROM FC_FC5_COMMITTEE WHERE UNIQUE_FILENO=? AND NATIONALITY NOT IN (180)");
		 			    		statementForRaw=connection.prepareStatement(queryForRaw.toString());
		 			    		statementForRaw.setString(1, obj.getApplicationId());
		 			    		rsForRaw=statementForRaw.executeQuery();		 			    		
		 			    		if(rsForRaw.next()){
		 			    			rowCount=rsForRaw.getInt(1);
		 			    		}
		 			    		rsForRaw.close();
			 			    	statementForRaw.close();
		 			    	}
		 			    			 			    
		 			    	if(rowCount >= 1) 		// Having foreign members in association 
		 			    		proceedFlag=true;
		 			    	
		 			    	if(proceedFlag == true){
					 			updatePCOfficeLevelStatus(obj.getApplicationId(),officeRAW,"1",chatId,false);	
				 				updatePCOfficeLevelFinalStatus(obj.getApplicationId(),officeRAW,null,chatId,false);
				 				
				 				// Getting section for inserting row in to PC_Section
				 				StringBuffer queryPC = new StringBuffer("SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SERVICE_CODE="
				 			    		+ "(SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?)");
				 				statement = connection.prepareStatement(queryPC.toString()); 
				 				statement.setString(1, officeRAW);
				 				statement.setString(2, obj.getApplicationId());
				 				ResultSet rsPC=statement.executeQuery();	
				 				String sectionRAW=null;
				 				if(rsPC.next())
				 					sectionRAW=rsPC.getString(1);
				 			    updatePCSectionDetails(obj.getApplicationId(), officeRAW, sectionRAW, chatId, false);
		 			    	}
		 			    }	 			    
	 			    
	 			    // Inserting row for RAW in case of RENEWAL //
		 		}
		 		StringBuffer query1 = new StringBuffer("UPDATE T_APPLICATION_STAGE_DETAILS SET CURRENT_STAGE=?,CURRENT_STATUS=? WHERE APPLICATION_ID=?");
		 		statement = connection.prepareStatement(query1.toString());	
		 		statement.setString(1, "2");
		 		statement.setString(2, "4"); 		
		 		statement.setString(3, obj.getApplicationId());
		 		statement.executeUpdate(); 		
		 		updateApplicationStatusNotification(obj.getApplicationId(),service,"4","Your application has been received.");
	    }else{
		    	// Getting section for inserting row in to PC_Section
				StringBuffer query4 = new StringBuffer("SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SERVICE_CODE="
			    		+ "(SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?)");
				statement = connection.prepareStatement(query4.toString()); 
				statement.setString(1, obj.getToOfficeCode());
				statement.setString(2, obj.getApplicationId());
				ResultSet rs4=statement.executeQuery();	
				String sectionId=null;
				if(rs4.next())
					sectionId=rs4.getString(1);
				Boolean flag1=flagCheckPCSectionDetails(obj.getApplicationId(),obj.getToOfficeCode());
				if(flag1==true)
					updatePCSectionDetails(obj.getApplicationId(), obj.getToOfficeCode(), sectionId, chatId, true);
				else
					updatePCSectionDetails(obj.getApplicationId(), obj.getToOfficeCode(), sectionId, chatId, false);
	    }
	 
		connection.commit();
		
		// Sending Mail to Applicant/Association
		if(obj.getToOfficeId().equals("1")){		// Only in case of MHA
			if(service.equals("02") || service.equals("01")){	// Prior Permission
				AutoNotifier notifier=new AutoNotifier(); 	 		 	 		
				getApplicantMail(obj.getApplicationId());		
				
				String mailContent="In terms of Section 12(3), it is submitted that the Application ("+sectionFileNo+") made under FCRA, 2010 has been received and will be examined on receipt "
								+ "of report from field agency.";
				String smsContent="It is submitted that the Application ("+sectionFileNo+") made under FCRA, 2010 has been received and will be examined on receipt "
						+ "of report from field agency.";
				
				if(emailId!=null){ 			
		 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(8), "2", emailId, connection,obj.getToOfficeCode(),mailContent); 			
		 		}
		 		if(mobile!=null){
		 			notifier.setPhoneNumber(mobile);
		 			notifier.setSmsMessage(smsContent);
		 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(8), "1", "", connection,obj.getToOfficeCode(),"");
		 		}
			}	
		}	
		return "success";		
	}
	// Grievance Detail
	public String grievanceApplication(Grievance obj,String appliId) throws Exception{
		String chatId="";
		updatePCOfficeUserDetails(appliId, obj.getMyOfficeCode(), obj.getMyUserId(), chatId);	
	    updatePCPendingDetails(appliId, obj.getMyOfficeCode(), obj.getMyUserId(), chatId); 
			// RECEIVER
		    Boolean flag=flagCheckPCOfficeLevelStatus(appliId, obj.getMyOfficeCode());
		    if(flag==true){
		    	updatePCOfficeLevelStatus(appliId,obj.getMyOfficeCode(),"6",chatId,true);
		        updatePCUserLevelStatus(appliId, obj.getMyOfficeCode(), obj.getMyUserId(),chatId,"1",true);
		    }
		    else
		    	{ 
		    	updatePCOfficeLevelStatus(appliId,obj.getMyOfficeCode(),"6",chatId,false);
		    	updatePCUserLevelStatus(appliId, obj.getMyOfficeCode(), obj.getMyUserId(),chatId,"1",false);
		    	}
		    	updatePCOfficeLevelFinalStatus(appliId,obj.getMyOfficeCode(),null,chatId,false);
		        updateApplicationProcessingTime(appliId, chatId, false);
		        String offCode1 = obj.getOfficevalue();
		        String[] result = offCode1.split(",");
		        for (int i=0; i<result.length; i++) {
		           String OfficeName=result[i];
		            //System.out.println(result[i]);
		            if(!OfficeName.equals("null") && !OfficeName.isEmpty()){
		            	updatePCOfficeLevelStatus(appliId,OfficeName,"1",chatId,false);	
		 				updatePCOfficeLevelFinalStatus(appliId,OfficeName,null,chatId,false);
		 				
		        		String sectionId="";
	 					StringBuffer querySection = new StringBuffer("SELECT  SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SERVICE_CODE='12'");
	 					PreparedStatement stmt1 = connection.prepareStatement(querySection.toString());
	 					stmt1.setString(1, OfficeName);
	 					ResultSet rs1 = stmt1.executeQuery();
	 					while(rs1.next()) {
	 						sectionId=rs1.getString(1);		
	 					}
	 					stmt1.close();
			        	updatePCSectionDetails(appliId,OfficeName, sectionId, chatId,false) ;
		            	
		            }
		          }
		       
		
		    return "success";
			 		
			
	}
	
	
	// PULL APPLICATION
	public String pullApplication(Chat obj) throws Exception{
		PreparedStatement statement=null;	
		String userId=null;
		String chatId=null;
		String stageId=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
		chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId
		StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, obj.getToOfficeCode());
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			userId=rs1.getString(1);	
		
		StringBuffer query2 = new StringBuffer("SELECT STAGE_ID FROM T_PC_USER_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=? AND USER_ID =?");
		statement = connection.prepareStatement(query2.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, obj.getToOfficeCode());
		statement.setString(3, userId);
		ResultSet rs2=statement.executeQuery();		
		if(rs2.next())
			stageId=rs2.getString(1);	
		
		connection.setAutoCommit(false);
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),obj.getToUserId()
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());
	    updatePCOfficeUserDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);		
	    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);				
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),userId,chatId,"5",true);								// Pulled From
	    
	    Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getFromUserId());                  // Action By - 'LOGGED USER'
	    if(flag==true){
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);			
	    }else{
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",false);			
	    }
	    flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId());
	    if(flag==true){
	    	if(stageId.equals("7"))
	    		updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"7",true);			// RECEIVER
	    	else
	    		updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"2",true);			// RECEIVER
	    }
	    else{
	    	if(stageId.equals("7"))
	    		updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"7",false);			// RECEIVER
	    	else
	    		updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"1",false);			// RECEIVER
	    }	    	
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,true);  
	    
		connection.commit();
		return "success";		
	}
	// FORWARD TO USER CASE
	public String submitForwardUserChatDetails(Chat obj) throws Exception{		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserForProcessing(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
		
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	    
	    connection.setAutoCommit(false);
	    updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),obj.getToUserId()
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getToUserId(),chatId);		
	    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);		    	    
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);		// SENDER
	   
	    Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId());
	    if(flag==true)
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"2",true);			// RECEIVER
	    else
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"1",false);			// RECEIVER
	    
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,true);	
	    if(!(obj.getSectionId()==null || obj.getSectionId().equals(""))){
	    	updatePCSectionDetails(obj.getApplicationId(), obj.getToOfficeCode(), obj.getSectionId(), chatId, true);
	    }
	    uploadChatAttachments(chatId,obj.getSessionId());
		connection.commit();
		return "success";
	}
	
	// ADD NOTE CASE
	public String submitUserNoteChatDetails(Chat obj) throws Exception{	
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserForProcessing(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
		
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId
	    connection.setAutoCommit(false);
	    updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),obj.getToUserId()
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getToUserId(),chatId);		
	    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);		    	    
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);		// SENDER
	    Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId());
	    if(flag==true)
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"2",true);			// RECEIVER
	    else
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"1",false);			// RECEIVER
	    
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,true);	
	    uploadChatAttachments(chatId,obj.getSessionId());	    	
		connection.commit();
		return "success";
	}
	
	// ONHOLD CASE
	public String submitUserOnHoldChatDetails(Chat obj) throws Exception{	
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}		
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId
	    connection.setAutoCommit(false);
	    updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),obj.getToUserId()
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getToUserId(),chatId);		
	    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);		    	    
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);		// SENDER
	    Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId());
	    if(flag==true)
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"2",true);			// RECEIVER
	    else
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"1",false);			// RECEIVER
	    
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,true);
	    Boolean timeUdpateFlag=flagCheckOnHold(obj.getApplicationId());
	    if(timeUdpateFlag==false)
			updateApplicationProcessingTime(obj.getApplicationId(), chatId, true);
	    updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getStatusId(),chatId,true);
	    uploadChatAttachments(chatId,obj.getSessionId());	    
		connection.commit();
		return "success";
	}
	
	// RESUME PROCESS CASE
	public String submitResumeProcessChatDetails(Chat obj) throws Exception{	
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId
	    connection.setAutoCommit(false);
	    updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),obj.getToUserId()
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());
	    updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getToOfficeCode(),null,chatId,true);
	    Boolean timeUdpateFlag=flagCheckOnHold(obj.getApplicationId());
	    if(timeUdpateFlag==false)
			updateApplicationProcessingTime(obj.getApplicationId(), chatId, false);
	    
	    uploadChatAttachments(chatId,obj.getSessionId());	    
		connection.commit();
		return "success";
	}
	
	
	private Boolean flagCheckOnHold(String appId) throws Exception{
		PreparedStatement statement=null;Boolean flag=false;
		StringBuffer query = new StringBuffer("SELECT * FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE APPLICATION_ID=? AND STATUS=11");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		ResultSet rs=statement.executeQuery();			
		if(rs.next()){			
			flag=true;
		}
		return flag;
	}	
	
	
	// USER CLARIFICATION CASE
	public String submitUserClarificationChatDetails(Chat obj) throws Exception{	
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForClarification(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	  
	    
	    // GETTING SERVICE CODE  
 		StringBuffer query3 = new StringBuffer("SELECT  SERVICE_CODE,CASE WHEN SECTION_FILENO IS NULL THEN '' ELSE  SECTION_FILENO  END FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
  		statement = connection.prepareStatement(query3.toString()); 			
  		statement.setString(1, obj.getApplicationId());
  		ResultSet rs2=statement.executeQuery();
  		String serviceCode=null; 
  		String secFileNo=null;
  		if(rs2.next()){
  			serviceCode=rs2.getString(1); 
  			secFileNo=rs2.getString(2);
  		}
	    connection.setAutoCommit(false);
	    updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),null,null
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),"3",chatId,true);
	    uploadChatAttachments(chatId,obj.getSessionId());
	    updateApplicationProcessingTime(obj.getApplicationId(), chatId, true);	
	    updateApplicationStatusNotification(obj.getApplicationId(),serviceCode,obj.getStatusId(),obj.getStatusRemark());
	    
	    // Sending Mails to Applicants
	    AutoNotifier notifier=new AutoNotifier(); 	 		 	 		
 		getApplicantMail(obj.getApplicationId());
 		String appId=null;
 		if(secFileNo==null)
 			appId=obj.getApplicationId();
 		else
 			appId=secFileNo;
 		
 		// Getting attachment		
 		byte[] attachment=getClarificationDocument(chatId);
 		
 		String mailContent="With reference to your application <b>"+appId+" </b>, Ministry of Home Affairs has requested for a clarification."
 					+ "<br><br>Please log on to <b>https://fcraonline.nic.in</b> to see the details and take necessary action.<br><br><br>"
					+ "This Email is system generated. Please do not reply to this email ID.<br><br>For any query, please send email at the e-mail IDs mentioned in "
					+ "FCRA website <b>https://fcraonline.nic.in </b></p>"; 		
 		if(emailId!=null){
 			notifier.setAttachment(attachment);
 			notifier.setAttachmentName("Document."+documentType);
 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(2), "2", emailId, connection,obj.getFromOfficeCode(),mailContent); 			
 		}
 		if(mobile!=null){
 			notifier.setPhoneNumber(mobile);
 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(2), "1", "", connection,obj.getFromOfficeCode(),"");
 		}	    
	    // UPDATE CURRENT_STATUS IN T_APPLICATION_DETAILS
		
		StringBuffer query2 = new StringBuffer("UPDATE T_APPLICATION_STAGE_DETAILS SET CURRENT_STATUS=8 WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query2.toString());		
		statement.setString(1, obj.getApplicationId());
		statement.executeUpdate();
		if(notifyList.get(0).getS().equals("e")){			
			connection.rollback();
			throw new ValidationException("Some unexpected error occured.");
		}
		else{
			connection.commit();
		}
		return "success";
	}
	
	// SHOW CAUSE NOTICE CASE
	public String submitShowCauseDetails(Chat obj) throws Exception{	
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	  
	    String scnId=generateShowCauseId(obj.getFromOfficeCode());	// Generating show cause id
	    
	    // GETTING SERVICE CODE  
	    updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),obj.getToUserId()
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getToUserId(),chatId);		
	    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);		    	    
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);		// SENDER
	    Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId());
	    if(flag==true)
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"2",true);			// RECEIVER
	    else
	    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"1",false);			// RECEIVER
	    
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,true);
	    
	    // Inserting in show Cause table
	    StringBuffer query1 = new StringBuffer("INSERT INTO T_SHOW_CAUSE_NOTICES(SCN_ID,GRIEVANCE_ID,CHAT_ID,NOTICE_BODY,GENRATED_DATE,GENERATED_BY,NOTICE_SUBJECT) "
	    		+ "VALUES(?,?,?,?,sysdate,?,?)");
		statement = connection.prepareStatement(query1.toString());
		int i=1;
		statement.setString(i++,scnId);
		statement.setString(i++,obj.getApplicationId());
		statement.setString(i++,chatId);
		statement.setString(i++,obj.getNoticeBody());
		statement.setString(i++,obj.getFromUserId());
		statement.setString(i++,obj.getNoticeSubject());	 
		statement.executeUpdate();
		
		connection.commit();
		return "success";
	}
	
	public String initClarificationReminder(String appId,String remark,String office,String user,String officeId,String sessionId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		PreparedStatement statement=null;
		// Checking MHA USER ROLE
		if(officeId.equals("1")){
			if(checkUserRoleForGranting(user)==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
		// GETTING SERVICE CODE  
 		StringBuffer query3 = new StringBuffer("SELECT CASE WHEN SECTION_FILENO IS NULL THEN '' ELSE  SECTION_FILENO  END FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
  		statement = connection.prepareStatement(query3.toString()); 			
  		statement.setString(1, appId);
  		ResultSet rs2=statement.executeQuery();		  		 
  		String secFileNo=null;
  		if(rs2.next()){		  			 
  			secFileNo=rs2.getString(1);
		}		
  		 String chatId=generateChatId(office);	// Generating chatId
  		// Updating PC Communication
  		updatePCCommunication(appId,chatId,null,null,office,user,null,null,"8",remark,"0",null);
  		uploadChatAttachments(chatId,sessionId);
  		updatePCOfficeUserDetails(appId, office, user, chatId); // Update Logged in User as a Current User 
  		
  		Boolean flag=flagCheckPCUserLevelStatus(appId,office,user);
	    if(flag==true)
	    	updatePCUserLevelStatus(appId,office,user,chatId,"3",true);	 // 
	    else
	    	updatePCUserLevelStatus(appId,office,user,chatId,"3",false); // RECEIVER
  		
		// Sending Mail to Applicants
	    AutoNotifier notifier=new AutoNotifier(); 	 		 	 		
 		getApplicantMail(appId);
 		String uniqueNumber=null;
 		if(secFileNo==null)
 			uniqueNumber=appId;
 		else
 			uniqueNumber=secFileNo;
 		
 		// Getting attachment		
 		byte[] attachment=getClarificationDocument(chatId);
 		String mailContent="With reference to your application <b>"+uniqueNumber+" </b>, Ministry of Home Affairs has requested for a clarification with the following remarks:<br><b>Remarks: </b>"+remark+""
 					+ "<br><br>Please log on to <b>https://fcraonline.nic.in</b> to see the details and take necessary action.<br><br><br>"
					+ "This Email is system generated. Please do not reply to this email ID.<br>For any query, please send email at the e-mail IDs mentioned in "
					+ "FCRA website <b>https://fcraonline.nic.in </b></p>";
 		if(emailId!=null){
 			notifier.setAttachment(attachment);
 			notifier.setAttachmentName("Document."+documentType);
 			notifyList=notifier.pushAutoNotifications(appId, Integer.valueOf(5), "2", emailId, connection,office,mailContent); 			
 		}
 		if(mobile!=null){
 			notifier.setPhoneNumber(mobile);
 			notifyList=notifier.pushAutoNotifications(appId, Integer.valueOf(5), "1", "", connection,office,"");
 		}
		return "success";
	}

	private byte[] getClarificationDocument(String chatId) throws Exception{
		PreparedStatement statement=null;byte[] attachment=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query=new StringBuffer("SELECT DATA,TYPE FROM T_PC_CHAT_ATTACHMENT WHERE CHAT_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, chatId);		
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			attachment=rs.getBytes(1);
			documentType=rs.getString(2);
		}
		return attachment;
	}
	
	// FORWARD TO OFFICE CASE
	public String submitForwardOfficeChatDetails(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String statusId=null;String officeStatusId=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}		
		
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	    
	    StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, obj.getToOfficeCode());
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			toUserId=rs1.getString(1);
		else
			toUserId=null;
		
		StringBuffer query2 = new StringBuffer("SELECT STAGE_ID FROM T_PC_USER_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=? AND USER_ID=?");
		statement = connection.prepareStatement(query2.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, obj.getToOfficeCode());
		statement.setString(3, toUserId);
		ResultSet rs2=statement.executeQuery();		
		if(rs2.next())
			statusId=rs2.getString(1);
		
		StringBuffer query3 = new StringBuffer("SELECT STAGE_ID FROM T_PC_OFFICE_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query3.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, obj.getToOfficeCode());		
		ResultSet rs3=statement.executeQuery();		
		if(rs3.next())
			officeStatusId=rs3.getString(1);
		
		connection.setAutoCommit(false);
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),toUserId
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId);	    
	    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId);
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true); // SENDER
	    
	    if(!(statusId==null || statusId.equals("1"))){
		    Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),toUserId);
		    if(flag==true)
		    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId,"2",true);		// RECEIVER
		   /* else
		    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId,"1",false);		// RECEIVER*/
	    }
	    
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),"3",chatId,true);			// SENDER
	    if(!(officeStatusId==null || officeStatusId.equals("1"))){
	    	Boolean officeFlag=flagCheckPCOfficeLevelStatus(obj.getApplicationId(), obj.getToOfficeCode());
		    if(officeFlag==true)
		    	updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"2",chatId,true);				// RECEIVER
		    else
		    	updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"1",chatId,false);				// RECEIVER
	    }	 	   			
		uploadChatAttachments(chatId,obj.getSessionId());
		
		// UPLOADING DOCUMENTS FOR CURRENT STAGE IN T_PROJECT_DOC_DETAILS
		
	/*	for(int i=0;i<obj.getStageFiles().length && i<obj.getStageFilesIds().length; i++){
			InputStream is = null;String id=null;
			if(obj.getStageFiles()[i] != null)
				is = obj.getStageFiles()[i].getInputStream();
			id=obj.getStageFilesIds()[i];
			StringBuffer query2 = new StringBuffer("INSERT INTO T_PROJECT_DOC_DETAILS VALUES(?,?,?,?)");
			statement = connection.prepareStatement(query2.toString());	
			statement.setString(1,obj.getApplicationId());
			statement.setString(2, id);
			if(is == null)
				statement.setNull(3, java.sql.Types.BLOB);
			else
				statement.setBinaryStream (3, is, (int) obj.getStageFiles()[i].getSize() );
			statement.setString(4, "0");
			statement.executeUpdate();
		} */
		connection.commit();	
		
		// Sending Mails to receiving office
	/*	AutoNotifier notifier=new AutoNotifier();
		notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(2), "2", obj.getToOfficeCode(), connection); */
		return "success";
	}
	
	private Boolean checkProjectDocAvailability(Chat obj) throws Exception{
		PreparedStatement statement=null;
		List<String> docId=new ArrayList<String>();
		Boolean docFlag=false;
		StringBuffer query = new StringBuffer("SELECT DOC_TYPE_ID FROM TM_SUBSTAGE_DOCUMENTS WHERE PROPOSAL_TYPE_ID="
				+ "(SELECT PROPOSAL_ID FROM T_PROJECT_REQUEST WHERE APPLICATION_ID=?) AND SUB_STAGE_ID="
				+ "(SELECT SUB_STAGE FROM T_APPLICATION_DETAILS WHERE APPLICATION_ID=?)");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, obj.getApplicationId());
		ResultSet rs=statement.executeQuery();
		int i=0;
		while(rs.next()){
			docId.add(rs.getString(1));			
		}  
		for(i=0;i<docId.size();i++){
			StringBuffer query1 = new StringBuffer("SELECT * FROM T_PROJECT_DOC_DETAILS WHERE DOC_TYPE_ID =? AND APPLICATION_ID=?");
			statement = connection.prepareStatement(query1.toString());	
			statement.setString(1, docId.get(i));
			statement.setString(2, obj.getApplicationId());	
			ResultSet rs1=statement.executeQuery();
			if(rs1.next())
				docFlag=true;
		}
		return docFlag;
	}
	
	// IN CASE OF NEXT STAGE
	public String submitNextStage(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String subStageId=null;		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(!(obj.getFromOfficeId().equals("7"))){
			Boolean docFlag=checkProjectDocAvailability(obj);
			if(docFlag == false)
				throw new ValidationException("Current stage documents has not been uploaded yet.Plase upload to proceed to next stage.");
		}
		
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId
	    
	    StringBuffer query = new StringBuffer("SELECT SUB_STAGE FROM T_APPLICATION_DETAILS WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, obj.getApplicationId());		
		ResultSet rs=statement.executeQuery();		
		if(rs.next())
			subStageId=rs.getString(1);	    
	   
		connection.setAutoCommit(false);
	    if(obj.getToUserId()==null){	
	    	/* IN CASE WHEN NEXT STAGE FOR OTHER OFFICE */
	    	StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
			statement = connection.prepareStatement(query1.toString());	
			statement.setString(1, obj.getApplicationId());		
			statement.setString(2, obj.getToOfficeCode());
			ResultSet rs1=statement.executeQuery();		
			if(rs1.next())
				toUserId=rs1.getString(1);
			else
				toUserId=null;    	
			
			updatePCCommunication(obj.getApplicationId(),chatId,null,subStageId,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),toUserId
		    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
			updatePCOfficeUserDetails(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId);			
			updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId);			
			updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);	// SENDER	
			Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),toUserId);
			if(flag == true)
				updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId,"2",true);	// RECEIVER
			else
				updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),toUserId,chatId,"1",false);	// RECEIVER
			updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),"3",chatId,true);		// SENDER	
			updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"2",chatId,true);		// RECEIVER
	    }
	    else{     
	    	/* IN CASE WHEN NEXT STAGE FOR SAME OFFICE 	    */	
	    	updatePCCommunication(obj.getApplicationId(),chatId,null,subStageId,obj.getFromOfficeCode(),obj.getFromUserId(),obj.getToOfficeCode(),obj.getToUserId()
		    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());			
	    	updatePCOfficeUserDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);		
		    updatePCPendingDetails(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId);				
		    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"3",true);		// SENDER
		    Boolean flag=flagCheckPCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId());
		    if(flag==true)
		    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"2",true);			// RECEIVER
		    else
		    	updatePCUserLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),obj.getToUserId(),chatId,"1",false);			// RECEIVER
		    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getToOfficeCode(),"6",chatId,true);				   
	    }
	    // INSERTING INTO APPLICATION_STATUS_DETAILS
	    updateApplicationStatusDetails(obj.getApplicationId(),subStageId,obj.getFromUserId(),chatId);
	    
		// UPLOADING DOCUMENTS FOR CURRENT STAGE IN T_PROJECT_DOC_DETAILS
		
		for(int i=0;i<obj.getStageFiles().length && i<obj.getStageFilesIds().length; i++){
			InputStream is = null;String id=null;
			if(obj.getStageFiles()[i] != null)
				is = obj.getStageFiles()[i].getInputStream();
			id=obj.getStageFilesIds()[i];
			StringBuffer query1 = new StringBuffer("INSERT INTO T_PROJECT_DOC_DETAILS VALUES(?,?,?,?)");
			statement = connection.prepareStatement(query1.toString());	
			statement.setString(1,obj.getApplicationId());
			statement.setString(2, id);
			if(is == null)
				statement.setNull(3, java.sql.Types.BLOB);
			else
				statement.setBinaryStream (3, is, (int) obj.getStageFiles()[i].getSize() );
			statement.setString(4, "0");
			statement.executeUpdate();
		}
		
		// UPDATE SUB_STAGE IN T_APPLICATION_DETAILS
		
		StringBuffer query2 = new StringBuffer("UPDATE T_APPLICATION_DETAILS SET SUB_STAGE=? WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query2.toString());	
		statement.setString(1, obj.getNextStageId());
		statement.setString(2, obj.getApplicationId());
		statement.executeUpdate();		
		connection.commit();
		
		// Sending mails to receiving office
		AutoNotifier notifier=new AutoNotifier();
		//notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(3), "2", obj.getToOfficeCode(), connection);
		return "success";
	} 
	
	//APPROVAL
	public String submitApprove(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String officeCode=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId		    
		connection.setAutoCommit(false); 
		
		StringBuffer query = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=?");
		statement = connection.prepareStatement(query.toString());				
		statement.setString(1, obj.getToOfficeId());		
		ResultSet rs=statement.executeQuery();	
		if(rs.next())
			officeCode=rs.getString(1);
		
    	StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, officeCode);
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			toUserId=rs1.getString(1);		
		
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),officeCode,toUserId
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
		updatePCOfficeUserDetails(obj.getApplicationId(),officeCode,toUserId,chatId);			
		updatePCPendingDetails(obj.getApplicationId(),officeCode,toUserId,chatId);			
		updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"4",true);	// SENDER		
		updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"2",true);	// RECEIVER		
		updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),"4",chatId,true);		// SENDER	
		updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"2",chatId,true);		// RECEIVER
		updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getStatusId(),chatId,true);
		
		// Updating red flag clearing remarks
		//--------------------------------------------------------------------------------------------------
		StringBuffer queryTemp = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
 		statement = connection.prepareStatement(queryTemp.toString()); 			
 		statement.setString(1, obj.getApplicationId());
 		ResultSet rsTmp=statement.executeQuery();
 		String sode=null;	 		
 		if(rsTmp.next()){
 			sode=rsTmp.getString(1);	 			
 		}
		if(sode.equals("01") || sode.equals("02") || sode.equals("03")) {
			List<RedFlagAssociations> matchingRedFlagAssociationList;
			List<RedFlagDonors> matchingRedFlagDonorList;
			matchingRedFlagAssociationList = getRedFlagStatusByName(obj.getApplicationId());
			matchingRedFlagDonorList = getRedFlagStatusByDonorName(obj.getApplicationId());
			
			if(matchingRedFlagAssociationList.size() > 0 || matchingRedFlagDonorList.size() > 0)
				updateRedFlagClearingRemarks(obj.getApplicationId(), obj.getFromOfficeCode(), obj.getFromUserId(), redFlagClearingRemarks);
		}
		//--------------------------------------------------------------------------------------------------
	    
		// UPLOADING DOCUMENTS FOR CURRENT STAGE IN T_PROJECT_DOC_DETAILS
		
	/*	for(int i=0;i<obj.getStageFiles().length && i<obj.getStageFilesIds().length; i++){
			InputStream is = null;String id=null;
			if(obj.getStageFiles()[i] != null)
				is = obj.getStageFiles()[i].getInputStream();
			id=obj.getStageFilesIds()[i];
			StringBuffer query2 = new StringBuffer("INSERT INTO T_PROJECT_DOC_DETAILS VALUES(?,?,?,?)");
			statement = connection.prepareStatement(query2.toString());	
			statement.setString(1,obj.getApplicationId());
			statement.setString(2, id);
			if(is == null)
				statement.setNull(3, java.sql.Types.BLOB);
			else
				statement.setBinaryStream (3, is, (int) obj.getStageFiles()[i].getSize() );
			statement.setString(4, "0");
			statement.executeUpdate();
		} */	
		connection.commit();
		
		// Sending mails to receiving office
		/*AutoNotifier notifier=new AutoNotifier();
		notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(4), "2", obj.getToOfficeCode(), connection); */
		return "success";
	}	
	
	public String getServiceCode(String appId) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
 		statement = connection.prepareStatement(query.toString()); 			
 		statement.setString(1, appId);
 		ResultSet rs=statement.executeQuery();
 		String serviceCode=null; 		
 		if(rs.next()){
 			serviceCode=rs.getString(1); 			
 		}
 		return serviceCode;
	}
	
	public void updateRedFlagClearingRemarks(String appId, String officeCode, String userId, String remarks) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query = new StringBuffer("INSERT INTO T_RED_FLAG_CLEARANCE_DETAILS(APPLICATION_ID, OFFICE_CODE, USER_ID, REMARKS, STATUS_DATE) "
				+ "VALUES(?, ?, ?, ?, systimestamp)");
 		statement = connection.prepareStatement(query.toString()); 			
 		statement.setString(1, appId);
 		statement.setString(2, officeCode);
 		statement.setString(3, userId);
 		statement.setString(4, remarks);
 		statement.executeUpdate();
	}
	
	// GRANTED
	
	public String submitGrant(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String officeCode=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}		
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
	
		StringBuffer queryTemp = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
 		statement = connection.prepareStatement(queryTemp.toString()); 			
 		statement.setString(1, obj.getApplicationId());
 		ResultSet rsTmp=statement.executeQuery();
 		String scode=null;	 		
 		if(rsTmp.next()){
 			scode=rsTmp.getString(1);	 			
 		}
	 	if(scode.equals("03")){
	 		//throw new ValidationException("Renewal of Applications has been temporarily blocked until further orders.");
	 	}
	 	// Checking Whether RCN is Under RED FLAG //
	 		if(scode.equals("03")){
	 			getRedFlagStatus(obj.getApplicationId());
	 			String checkRedFlag=redFlag;
		 		if(checkRedFlag.equals("YES")){	
		 			redFlag="YES";	 			
		 			if(redFlagREDCategory!=null && redFlagREDCategory.equals("YES")){
		 				if(redFlagCategoryCode==null || !(redFlagCategoryCode.equals("9"))){
			 				throw new ValidationException("This application can't be approved as it belongs to <b>RED FLAGGED ASSOCIATIONS</b>.");	
			 			}		 			
			 			else{
			 				throw new ValidationException("This application can't be approved as it belongs to <b>RED FLAGGED ASSOCIATIONS</b> under <b><i><span class='text-primary'>"+redFlagCategory.toUpperCase()+"</span></i></b> Category.");
			 			}		
					}else if(redFlagYELLOWCategory!=null && redFlagYELLOWCategory.equals("YES")){
						if(redFlagCategoryCode==null || !(redFlagCategoryCode.equals("9"))){
			 				throw new ValidationException("This application can't be approved as it belongs to <b>YELLOW FLAGGED ASSOCIATIONS</b>.");	
			 			}		 			
			 			else{
			 				throw new ValidationException("This application can't be approved as it belongs to <b>YELLOW FLAGGED ASSOCIATIONS</b> under <b><i><span class='text-primary'>"+redFlagCategory.toUpperCase()+"</span></i></b> Category.");
			 			}	
					}		
		 		}
	 		}
 		// Checking Whether RCN is Under RED FLAG //
	 		
	 	// Checking whether Registration is valid in case of Renewal and Change of Details
	 		if(scode.equals("03") || scode.equals("06")){
	 			String rcnQ=null;
	 			if(scode.equals("03"))
	 				rcnQ="(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)";
	 			else if(scode.equals("06"))
	 				rcnQ="(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)";
	 			StringBuffer queryRCN=new StringBuffer("SELECT CURRENT_STATUS FROM FC_INDIA WHERE RCN="+rcnQ+"");
	 			statement = connection.prepareStatement(queryRCN.toString()); 			
		 		statement.setString(1, obj.getApplicationId());
		 		ResultSet rsRCN=statement.executeQuery();
		 		String cs=null;
		 		if(rsRCN.next()){
		 			cs=rsRCN.getString(1);
		 		}
		 		if(cs==null || !(cs.equals("0"))){
	 				throw new ValidationException("Service couldn't be granted as the FCRA RCN of the Association is not active.");
	 			}	 			
	 		}
	 	// Checking whether Registration is valid in case of Renewal and Change of Details
	 		
	 	
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId		    
		connection.setAutoCommit(false); 
		
		StringBuffer query = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=?");
		statement = connection.prepareStatement(query.toString());				
		statement.setString(1, obj.getToOfficeId());		
		ResultSet rs=statement.executeQuery();	
		if(rs.next())
			officeCode=rs.getString(1);
		
    	StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, officeCode);
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			toUserId=rs1.getString(1);		
		
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),null,null
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());					
		updatePCPendingDetails(obj.getApplicationId(),officeCode,toUserId,chatId);	
		updatePCOfficeLevelFinalStatus(obj.getApplicationId(),officeCode,obj.getStatusId(),chatId,true);
		
		// Updating red flag clearing remarks 
		if(scode.equals("01") || scode.equals("02") || scode.equals("03")) {
			List<RedFlagAssociations> matchingRedFlagAssociationList;
			List<RedFlagDonors> matchingRedFlagDonorList;
			matchingRedFlagAssociationList = getRedFlagStatusByName(obj.getApplicationId());
			matchingRedFlagDonorList = getRedFlagStatusByDonorName(obj.getApplicationId());
			
			if(matchingRedFlagAssociationList.size() > 0 || matchingRedFlagDonorList.size() > 0)
				updateRedFlagClearingRemarks(obj.getApplicationId(), obj.getFromOfficeCode(), obj.getFromUserId(), redFlagClearingRemarks);
		}
		
		// GETTING SERVICE CODE IF REGISTRATION THEN GENERATING REGISTRATION_NUMBER AND SAVING IT TO FC_INDIA
		StringBuffer query3 = new StringBuffer("SELECT  SERVICE_CODE,STATE,DISTRICT FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
 		statement = connection.prepareStatement(query3.toString()); 			
 		statement.setString(1, obj.getApplicationId());
 		ResultSet rs2=statement.executeQuery();
 		String serviceCode=null;
 		String regParam=null;
 		String regNumber=null;
 		if(rs2.next()){
 			serviceCode=rs2.getString(1);
 			regParam=rs2.getString(2)+rs2.getString(3);
 		}
 		
 		if(serviceCode.equals("01")){  // REGISTRATION SERVICE
 			regSecStatus=true;
 			StringBuffer query4 = new StringBuffer("SELECT  FN_RCN_GENERATION(?) FROM DUAL");
 	 		statement = connection.prepareStatement(query4.toString()); 			
 	 		statement.setString(1, regParam);
 	 		ResultSet rs3=statement.executeQuery();	
 	 		if(rs3.next())
 	 			regNumber=rs3.getString(1);
 	 		
 	 		registrationNumber=regNumber; 	 		
 	 		
 	 		
 	 		// INSERTING DATA INTO FC_INDIA 
 	 		StringBuffer query5 = new StringBuffer("INSERT INTO FC_INDIA(RCN,ASSO_NAME,ASSO_ADDRESS,ASSO_TOWN_CITY,ASSO_NATURE,ASSO_RELIGION,REG_DATE,"
 	 				+ "SECTION_FILENO,R_CATGRY,R_DATE,STATUS,NEW_OLD,USERID,STDIST,ASSO_PIN,VALID_FROM,VALID_TO,ASSO_AIMS,LAST_RENEWED_ON, CANCEL_STATUS,CURRENT_STATUS)"
 	 				+ " VALUES (?,(SELECT ASSO_NAME FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),(SELECT ASSO_ADDRESS FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "(SELECT ASSO_TOWN_CITY FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),(SELECT ASSO_NATURE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "(SELECT ASSO_RELIGIOUS FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),sysdate,"
 	 				+ "(SELECT SECTION_FILENO FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "?,?,?,?,(SELECT USERNAME FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),?,(SELECT ASSO_PINCODE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?)"
 	 				+ ",sysdate,(select add_months(sysdate,60)-1 from dual),(SELECT MAIN_AIMS FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),sysdate,?,?)");
 	 		statement = connection.prepareStatement(query5.toString()); 			
 	 		statement.setString(1, regNumber);
 	 		statement.setString(2, obj.getApplicationId());
 	 		statement.setString(3, obj.getApplicationId());
 	 		statement.setString(4, obj.getApplicationId());
 	 		statement.setString(5, obj.getApplicationId());
 	 		statement.setString(6, obj.getApplicationId()); 	 		
 	 		statement.setString(7, obj.getApplicationId());
 	 		statement.setString(8, "N");
 	 		statement.setString(9, null);
 	 		statement.setString(10, "N");
 	 		statement.setString(11, "N");
 	 		statement.setString(12, obj.getApplicationId());
 	 		statement.setString(13, regParam);
 	 		statement.setString(14, obj.getApplicationId());
 	 		statement.setString(15, obj.getApplicationId());
 	 		statement.setString(16, "N");
 	 		statement.setString(17, "0");
 	 		statement.executeUpdate(); 	 
 	 		
 	 		// INSERTING DATA INTO FC_BANK
 	 		StringBuffer query6 = new StringBuffer("INSERT INTO FC_BANK(NEW_OLD,REQUESTDATE,RCN,BANK_NAME,BANK_ADDRESS,BANK_TOWN_CITY,"
 	 				+ "BANK_PIN,ACCOUNT_NO,STATUS,STATUS_DATE,BANK_STDIST,IFSC_CODE) VALUES (?,"
 	 				+ "?,?,(SELECT BANK_NAME FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "(SELECT BANK_ADDRESS FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "(SELECT BANK_TOWNCITY FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "(SELECT BANK_PIN FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "(SELECT ACCOUNT_NO FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),"
 	 				+ "?,?,(SELECT BANK_STATE || BANK_DISTRICT FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?),(SELECT BANK_BRANCH_IFSC_CODE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?))");
 	 		statement = connection.prepareStatement(query6.toString()); 			
 	 		statement.setString(1, "N");
 	 		statement.setString(2, null);
 	 		statement.setString(3, regNumber);
 	 		statement.setString(4, obj.getApplicationId());
 	 		statement.setString(5, obj.getApplicationId());
 	 		statement.setString(6, obj.getApplicationId());
 	 		statement.setString(7, obj.getApplicationId());
 	 		statement.setString(8, obj.getApplicationId());
 	 		statement.setString(9, "Y");	 		
 	 		statement.setString(10, null);
 	 		statement.setString(11, obj.getApplicationId());
 	 		statement.setString(12, obj.getApplicationId());
 	 		statement.executeUpdate(); 	 	
 	 		
 	 		// INSERTING DATA INTO FC_COMMITTEE
 	 		StringBuffer query8=new StringBuffer("INSERT INTO FC_COMMITTEE(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,"
 	 				+ "ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,"
 	 				+ "BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,"
 	 				+ "WHETHER_IN_YN,RECORD_STATUS,SNO) "
 	 				+ "SELECT ?,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,"
 	 				+ "OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,"
 	 				+ "OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
 	 				+ "b.DOB, b.PLACE_BIRTH, b.PASSPORT_NO, b.PER_ADD_FC, b.PIO_ORG_STATUS, b.PIO_NO, b.RES_INDIA_STATUS, b.RES_INDIA_DATE, b.WHETHER_IN_YN,"
 	 				+ "'0', ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY name ) sno FROM FC_FC8_COMMITTEE a left join fc_ec_foreigners b on a.unique_fileno=b.unique_fileno and a.sl_no=b.sl_no WHERE a.UNIQUE_FILENO=?");
 	 		statement = connection.prepareStatement(query8.toString()); 			
 	 		statement.setString(1, regNumber);
 	 		statement.setString(2, obj.getApplicationId());
 	 		statement.executeUpdate(); 	
 	 	
 	 		// INSERTING INTO T_REGISTRATION_SHARING
 	 		
 	 		StringBuffer query9 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
 	 				+ " VALUES (?,?,0,systimestamp,null)");
 	 		statement = connection.prepareStatement(query9.toString()); 			
 	 		statement.setString(1, regNumber);
 	 		statement.setString(2, "FC_COMMITTEE"); 	 		 		
 	 		statement.executeUpdate(); 	
 	 		statement.close();
 	 		
 	 		// INSERTING DATA INTO T_REGISTRATION_STATUS_HISTORY
 	 		StringBuffer query7 = new StringBuffer("INSERT INTO T_REGISTRATION_STATUS_HISTORY(RCN,STATUS,REMARKS,STATUS_DATE,ACTION_BY,REFERENCE_FOR_DETAILS)"
 	 				+ " VALUES (?,?,?,sysdate,?,?)");
 	 		statement = connection.prepareStatement(query7.toString()); 			
 	 		statement.setString(1, regNumber);
 	 		statement.setString(2, "0");
 	 		statement.setString(3, obj.getStatusRemark());
 	 		statement.setString(4, obj.getFromUserId());
 	 		statement.setString(5, null); 	 		
 	 		statement.executeUpdate(); 
 	 		
 	 		// INSERTING INTO FC_ASSO_DETAILS
 	 		
 	 		StringBuffer queryAssoDetails = new StringBuffer("INSERT INTO FC_ASSO_DETAILS(RCN,CHEIF_FUNCTIONARY_NAME,ASSO_OFFICIAL_EMAIL,ASSO_OFFICIAL_WEBSITE,ASSO_CHEIF_TELEPHONE,ASSO_CHEIF_MOBILE,"
 	 				+ "PAN_NO,DARPANID,ASSO_NAME_DARPAN,DARPAN_PAN_NO) (SELECT DISTINCT ? AS RCN, APPLICANT_NAME AS CHEIF_FUNCTIONARY_NAME, ASSO_EMAIL AS ASSO_OFFICIAL_EMAIL, ASSO_WEBSITE "
 	 				+ "AS ASSO_OFFICIAL_WEBSITE, ASSO_CHEIF_TELEPHONE AS ASSO_CHEIF_TELEPHONE,ASSO_MOBILE AS ASSO_CHEIF_MOBILE, PAN_NO, DARPANID,CASE WHEN A.DARPANID IS NULL "
 	 				+ "THEN NULL ELSE A.ASSO_NAME END AS ASSO_NAME_DARPAN, DARPAN_PANNO FROM T_FC8_ENTRY A WHERE A.UNIQUE_FILENO=?)");
 	 		statement = connection.prepareStatement(queryAssoDetails.toString()); 			
 	 		statement.setString(1, regNumber);
 	 		statement.setString(2, obj.getApplicationId()); 	 				
 	 		statement.executeUpdate(); 
 	 		statement.close();
 	 		
 	 		// INSERTING INTO T_REGISTRATION_SHARING
 	 		
 	 		StringBuffer queryRegSharing = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
 	 				+ " VALUES (?,?,0,systimestamp,null)");
 	 		statement = connection.prepareStatement(queryRegSharing.toString()); 			
 	 		statement.setString(1, regNumber);
 	 		statement.setString(2, "FC_ASSO_DETAILS"); 	 		 		
 	 		statement.executeUpdate(); 	
 	 		statement.close();
 		}
 		else if(serviceCode.equals("02")){ // PRIOR PERMISSION			
 			// Handling Installments 
 			if(ppInsFlag!=null && ppInsFlag.equals("YES") && installments!=null){
 				String donorId=null;int totalAmount=0; 				
 				//Getting Donor Id
 				query=new StringBuffer("SELECT ID,NATUREANDVALUE_VALUE FROM FC_FC1A_DONOR WHERE UNIQUE_FILENO=?");
				statement=connection.prepareStatement(query.toString());
				statement.setString(1, obj.getApplicationId());
				rs=statement.executeQuery();
				if(rs.next()){
					donorId=rs.getString(1);
					totalAmount=rs.getInt(2);
				}			
				rs.close();
				statement.close();
				
 				StringTokenizer st = new StringTokenizer(installments,","); 				
 				if(st.countTokens() != Integer.parseInt(installmentNumbers))
 					throw new ValidationException("There is a some discrepency between no. of installments and installment details.");
 				
 				int installmentAmount=0;
 				while(st.hasMoreTokens()){
 					installmentAmount+=Integer.parseInt(st.nextToken());
 				}
 				if(installmentAmount!=totalAmount)
 					throw new ValidationException("Total contribution is not matching with sum of installments. Please re-check the amounts.");
 				
 				StringTokenizer st1 = new StringTokenizer(installments,",");
 				while(st1.hasMoreTokens()){
 					query=new StringBuffer("INSERT INTO t_PP_INSTALLMENT_DETAILS(APPLICATION_ID,DONOR_ID,INSTALLMENT_SNUMBER,INSTALLMENT_AMOUNT,UTILIZATION_FLAG) VALUES(?,"
 							+ "?,(SELECT NVL(MAX(INSTALLMENT_SNUMBER),0)+1 FROM T_PP_INSTALLMENT_DETAILS WHERE APPLICATION_ID=?),?,'N')");
 					statement=connection.prepareStatement(query.toString());
 					statement.setString(1, obj.getApplicationId());
 					statement.setString(2, donorId);
 					statement.setString(3, obj.getApplicationId());
 					statement.setString(4, st1.nextToken());
 					statement.executeUpdate();
 				} 				
 			}
 			
 			// Generating PDF Bytes 
 			pdfStatus=true; 			
 		}
 		else if(serviceCode.equals("03")){ // RENEWAL 
 			String validationDate=null;
 			StringBuffer dateValidationQuery=new StringBuffer("select to_char(add_months(to_date(?,'dd-mm-yyyy'),60)-1,'dd-mm-yyyy') from dual");
 			statement = connection.prepareStatement(dateValidationQuery.toString());
 	 		statement.setString(1, validityFrom);
 	 		ResultSet dateValidationResult=statement.executeQuery();
 	 		if(dateValidationResult.next()){
 	 			validationDate=dateValidationResult.getString(1); // Taking ValidUpTo Date
 	 		}
 	 		
 	 		DateFormat df = new SimpleDateFormat("dd-mm-yyyy");
			Date validUpTo = df.parse(validationDate);
			Calendar localCalendar=Calendar.getInstance();
			String currentDateString = df.format(localCalendar.getTime()); // Current Date
			Date currentDate = df.parse(currentDateString);			
			if(validUpTo.before(currentDate))
				throw new ValidationException("Invalid Date.Validity UpTo can't be a past date.It must be a future date.");
			
 			StringBuffer query5 = new StringBuffer("UPDATE FC_INDIA SET LAST_RENEWED_ON=systimestamp,valid_from=to_date(?,'dd-mm-yyyy'),"
 					+ "valid_to=add_months(to_date(?,'dd-mm-yyyy'),60)-1 where rcn=(select ASSO_FCRA_RCN from fc_fc5_entry_new1 where unique_fileno=?)");
 	 		statement = connection.prepareStatement(query5.toString());
 	 		statement.setString(1, validityFrom);
 	 		statement.setString(2, validityFrom);
 	 		statement.setString(3, obj.getApplicationId()); 	 		
 	 		statement.executeUpdate();
 	 		
 	 		//Checking if record available in fc_bank
 	 		StringBuffer subQuery=new StringBuffer("SELECT * FROM FC_BANK WHERE RCN=(SELECT ASSO_RCN FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
 	 		statement = connection.prepareStatement(subQuery.toString()); 
 	 		statement.setString(1, obj.getApplicationId());
 	 		Boolean flag=false;
 	 		ResultSet rs3=statement.executeQuery();
 	 		if(rs3.next()){
 	 			flag=true;
 	 		}
 	 		if(flag==true){
 	 			StringBuffer query8 = new StringBuffer("UPDATE FC_BANK SET ACCOUNT_NO=(SELECT ACCOUNT_NO FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),"
	 					+ "BANK_NAME=(SELECT BANK_NAME FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),BANK_ADDRESS=(SELECT BANK_ADDRESS FROM FC_FC5_RECEIPT_BANK WHERE "
	 					+ "UNIQUE_FILENO=?),BANK_TOWN_CITY=(SELECT BANK_TOWNCITY FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),BANK_STDIST=("
	 					+ "SELECT BANK_STATE||BANK_DISTRICT FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),BANK_PIN=(SELECT BANK_PIN FROM FC_FC5_RECEIPT_BANK "
	 					+ "WHERE UNIQUE_FILENO=?),NEW_OLD='N',IFSC_CODE=(SELECT BANK_IFSC_CODE FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?) "
	 					+ "WHERE RCN=(SELECT ASSO_RCN FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
	 	 		statement = connection.prepareStatement(query8.toString()); 
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		statement.setString(2, obj.getApplicationId());
	 	 		statement.setString(3, obj.getApplicationId());
	 	 		statement.setString(4, obj.getApplicationId());
	 	 		statement.setString(5, obj.getApplicationId());
	 	 		statement.setString(6, obj.getApplicationId());
	 	 		statement.setString(7, obj.getApplicationId());	
	 	 		statement.setString(8, obj.getApplicationId());
	 	 		statement.executeUpdate();
 	 		}else{
 	 			StringBuffer query8 = new StringBuffer("INSERT INTO FC_BANK(ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWN_CITY,BANK_STDIST,BANK_PIN,NEW_OLD,RCN,STATUS,IFSC_CODE) VALUES("
 	 					+ "(SELECT ACCOUNT_NO FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),"
 	 					+ "(SELECT BANK_NAME FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),"
 	 					+ "(SELECT BANK_ADDRESS FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),"
 	 					+ "(SELECT BANK_TOWNCITY FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),"
 	 					+ "(SELECT BANK_STATE||BANK_DISTRICT FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),"
 	 					+ "(SELECT BANK_PIN FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),'N',"
 	 					+ "(SELECT ASSO_RCN FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?),'Y',(SELECT BANK_IFSC_CODE FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO=?))");
	 	 		statement = connection.prepareStatement(query8.toString()); 
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		statement.setString(2, obj.getApplicationId());
	 	 		statement.setString(3, obj.getApplicationId());
	 	 		statement.setString(4, obj.getApplicationId());
	 	 		statement.setString(5, obj.getApplicationId());
	 	 		statement.setString(6, obj.getApplicationId());
	 	 		statement.setString(7, obj.getApplicationId());	
	 	 		statement.setString(8, obj.getApplicationId());
	 	 		statement.executeUpdate();
 	 		}	 		
 	 		
 	 		// UPDATING DATA INTO FC_COMMITTEE - START
 	 		
 	 		/* 1. Updating record_status '1' in fc_committee_history if available */
 	 		query=new StringBuffer("UPDATE T_FC_COMMITTEE_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)");
 	 		statement = connection.prepareStatement(query.toString()); 
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.executeUpdate();
 	 		
 	 		/* 2. Moving data from fc_committee to t_fc_committee_history */
 	 		query=new StringBuffer("INSERT INTO T_FC_COMMITTEE_HISTORY(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
 	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
 	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,MODIFIED_DATE) "
 	 				+ "SELECT RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
 	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
 	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,systimestamp FROM FC_COMMITTEE a WHERE RCN"
 	 				+ "=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)");
 	 		statement = connection.prepareStatement(query.toString()); 	 		
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.executeUpdate(); 	
 	 		
 	 		/* 3. deleting data from fc_committe */
 	 		query=new StringBuffer("DELETE FROM FC_COMMITTEE WHERE RCN=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)");
 	 		statement = connection.prepareStatement(query.toString()); 	 		
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.executeUpdate(); 
 	 		
 	 		/* 4. inserting into fc_committe */
 	 		StringBuffer query8=new StringBuffer("INSERT INTO FC_COMMITTEE(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,"
 	 				+ "ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,"
 	 				+ "BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,"
 	 				+ "WHETHER_IN_YN,RECORD_STATUS,SNO) "
 	 				+ "SELECT (SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?),NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,"
 	 				+ "OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,"
 	 				+ "OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,b.DOB, b.PLACE_BIRTH, b.PASSPORT_NO, b.PER_ADD_FC, "
 	 				+ "b.PIO_ORG_STATUS, b.PIO_NO, b.RES_INDIA_STATUS, b.RES_INDIA_DATE, b.WHETHER_IN_YN,'0', ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY name ) sno  "
 	 				+ "FROM FC_FC5_COMMITTEE a left join fc_ec5_foreigners b on a.unique_fileno=b.unique_fileno and a.sl_no=b.sl_no WHERE a.UNIQUE_FILENO=?");
 	 		statement = connection.prepareStatement(query8.toString()); 			
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.setString(2, obj.getApplicationId());
 	 		statement.executeUpdate(); 	
 	 		
 	 		// INSERTING INTO T_REGISTRATION_SHARING
 	 		
 	 		StringBuffer query9 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
 	 				+ " VALUES ((SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");
 	 		statement = connection.prepareStatement(query9.toString()); 			
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.setString(2, "FC_COMMITTEE"); 	 		 		
 	 		statement.executeUpdate(); 	
 	 		statement.close();
 			
 	 		// UPDATING DATA INTO FC_COMMITTEE - END
 	 		
 	 		
 	 		// UPDATING DATA INTO FC_UTILIZATION_BANK - START
 	 		
 	 		/* 1. Updating record_status '1' in FC_UTILIZATION_BANK_HISTORY if available */
 	 		query=new StringBuffer("UPDATE T_FC_UTILIZATION_BANK_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)");
 	 		statement = connection.prepareStatement(query.toString()); 
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.executeUpdate();
 	 		
 	 		/* 2. Moving data from FC_UTILIZATION_BANK to t_FC_UTILIZATION_BANK_history */
 	 		query=new StringBuffer("INSERT INTO T_FC_UTILIZATION_BANK_HISTORY(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,MODIFIED_DATE) "
 	 				+ "SELECT RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,systimestamp FROM FC_UTILIZATION_BANK a WHERE RCN"
 	 				+ "=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)");
 	 		statement = connection.prepareStatement(query.toString()); 	 		
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.executeUpdate(); 	
 	 		
 	 		/* 3. deleting data from FC_UTILIZATION_BANK */
 	 		query=new StringBuffer("DELETE FROM FC_UTILIZATION_BANK WHERE RCN=(SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?)");
 	 		statement = connection.prepareStatement(query.toString()); 	 		
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.executeUpdate(); 
 	 		
 	 		/* 4. inserting into FC_UTILIZATION_BANK */
 	 		StringBuffer query10=new StringBuffer("INSERT INTO FC_UTILIZATION_BANK(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS) "
 	 				+ "SELECT (SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?),ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY BANK_NAME ) sno, ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,"
 	 				+ "BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,0 FROM FC_FC5_UTILIZATION_BANK a WHERE a.UNIQUE_FILENO=?");
 	 		statement = connection.prepareStatement(query10.toString()); 			
 	 		statement.setString(1, obj.getApplicationId()); 	 		
 	 		statement.setString(2, obj.getApplicationId());
 	 		statement.executeUpdate(); 	
 	 		
 	 		// INSERTING INTO T_REGISTRATION_SHARING
 	 		
 	 		StringBuffer query11 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
 	 				+ " VALUES ((SELECT ASSO_FCRA_RCN FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");
 	 		statement = connection.prepareStatement(query11.toString()); 			
 	 		statement.setString(1, obj.getApplicationId());
 	 		statement.setString(2, "FC_UTILIZATION_BANK"); 	 		 		
 	 		statement.executeUpdate(); 	
 	 		statement.close();
 			
 	 		// UPDATING DATA INTO FC_UTILIZATION_BANK - END
 	 		
 	 		// Generating PDF Bytes 			
 			pdfStatus=true; 	 	
 		}else if(serviceCode.equals("07")){ // Hospitality
 			// Generating PDF Bytes 
 			pdfStatus=true; 	 	
 		}else if(serviceCode.equals("13")){   //NAME ADDRESS CHANGE or FC6_A
 			Boolean nameFlag=false;
 			Boolean addFlag=false;
 			Boolean recepientBankFlag=false;
 			Boolean natureFlag=false;
 			Boolean memberChangeFlag=false;
 			Boolean utilizationChangeFlag=false;
 			/*StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
 					+ ",ASSO_CHANGE_MEMBER,ASSO_CHANGE_UTILISATION_STATUS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?"); */
 			StringBuffer query5 = new StringBuffer("SELECT IS_NAME_CHANGE,IS_ADDRESS_CHANGE from FC_FC6_NAMEADDRESS_CHANGE where UNIQUE_FILENO=?");
 	 		statement = connection.prepareStatement(query5.toString()); 
 	 		statement.setString(1, obj.getApplicationId());
 	 		ResultSet rs4=statement.executeQuery();
 	 		if(rs4.next()){
 	 			// Name Change
 	 			if(rs4.getString(1)==null){			
 	 			}
 	 			else if(rs4.getString(1).equals("Y")){
 	 				nameFlag=true;
 	 			} 
 	 			// Address Change
 	 			if(rs4.getString(2)==null){			
 	 			}
 	 			else if(rs4.getString(2).equals("Y")){
 	 				addFlag=true;
 	 			}
/* 	 			// Bank Change
 	 			if(rs4.getString(3)==null){			
 	 			}
 	 			else if(rs4.getString(3).equals("Y")){
 	 				recepientBankFlag=true; 	 	 
 	 			}
 	 			// Nature Change
 	 			if(rs4.getString(4)==null){			
 	 			}
 	 			else if(rs4.getString(4).equals("Y")){
 	 				natureFlag=true;
 	 			}
 	 			// Member Change
 	 			if(rs4.getString(5)==null){			
 	 			}
 	 			else if(rs4.getString(5).equals("Y")){
 	 				memberChangeFlag=true;
 	 			}
 	 			// Utilization Bank Change
 	 			if(rs4.getString(6)==null){			
 	 			}
 	 			else if(rs4.getString(6).equals("Y")){
 	 				utilizationChangeFlag=true;
 	 			} */
 	 		}
 	 		if(nameFlag==true){
 	 		/*	StringBuffer query6 = new StringBuffer("UPDATE FC_INDIA SET ASSO_NAME=(SELECT ASSO_NAME FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)"
 	 					+ ",ASSO_AIMS=(SELECT MAIN_AIM FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?) WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE "
 	 					+ "UNIQUE_FILENO=?)"); */
 	 			StringBuffer query6 = new StringBuffer("UPDATE FC_INDIA SET "
 	 					+ "ASSO_NAME=(SELECT CHANGED_TO_ASSONAME FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?)"
 	 					//+ ",ASSO_AIMS=(SELECT MAIN_AIM FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?) "
 	 					+ "WHERE RCN=(SELECT RCN FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?)"); 	 			
 	 	 		statement = connection.prepareStatement(query6.toString()); 
 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 		statement.setString(2, obj.getApplicationId());
 	 	 		//statement.setString(3, obj.getApplicationId());
 	 	 		statement.executeUpdate();
 	 		}
 	 		if(addFlag==true){
/* 	 			StringBuffer query7 = new StringBuffer("UPDATE FC_INDIA SET ASSO_ADDRESS=(SELECT ASSO_ADDRESS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)"
 	 					+ ",ASSO_TOWN_CITY=(SELECT ASSO_TOWN_CITY FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),STDIST=(SELECT ASSO_STATE||ASO_DISTRICT "
 	 					+ " FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),ASSO_PIN=(SELECT ASSO_PIN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),NEW_OLD='N' "
 	 					+ " WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
*/
 	 			StringBuffer query7 = new StringBuffer("UPDATE FC_INDIA SET "
 	 					+ "ASSO_ADDRESS=(SELECT CHANGED_TO_ASSOADDRESS FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?) "
 	 					+ ",ASSO_TOWN_CITY=(SELECT CHANGED_TO_ASSOTOWNCITY FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?)"
 	 					+ ",STDIST=(SELECT CHANGED_TO_ASSOSTATE||CHANGED_TO_ASSODISTRICT FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?)"
 	 					+ ",ASSO_PIN=(SELECT CHANGED_TO_ASSOPIN FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?)"
 	 					+ ",NEW_OLD='N' "
 	 					+ "WHERE RCN=(SELECT RCN FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?)"); 	 	 			
 	 	 		statement = connection.prepareStatement(query7.toString()); 
 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 		statement.setString(2, obj.getApplicationId());
 	 	 		statement.setString(3, obj.getApplicationId());
 	 	 		statement.setString(4, obj.getApplicationId());
 	 	 		statement.setString(5, obj.getApplicationId());
 	 	 		statement.executeUpdate();
 	 	 		statement.close();
 	 		}
 	 		// 	Generating PDF Bytes 
 	 		pdfStatus=true;
 		}else if(serviceCode.equals("15")){ // CHANGE OF  FC RECEIPT-CUM-UTILISATION BANK-----------------------------				 
 			Boolean nameFlag=false;
 			Boolean addFlag=false;
 			Boolean recepientBankFlag=false;
 			Boolean natureFlag=false;
 			Boolean memberChangeFlag=false;
 			Boolean utilizationChangeFlag=false;
 			//StringBuffer query5 = new StringBuffer("SELECT * FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?");
/* 			StringBuffer query5 = new StringBuffer("SELECT * FROM FC_BANK WHERE RCN=(SELECT RCN FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
 	 		statement = connection.prepareStatement(query5.toString()); 
 	 		statement.setString(1, obj.getApplicationId());
 	 		ResultSet rs4=statement.executeQuery();
 	 		if(rs4.next()){
 	 				recepientBankFlag=true; 	 	 
 	 		} */
 	 		//if(recepientBankFlag==true){ 
 	 			//Checking if record available in fc_bank
 	 	 		//StringBuffer subQuery=new StringBuffer("SELECT * FROM FC_BANK WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
 	 	 		StringBuffer subQuery=new StringBuffer("SELECT * FROM FC_BANK WHERE RCN=(SELECT RCN FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
 	 	 		statement = connection.prepareStatement(subQuery.toString()); 
 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 		Boolean flag=false;
 	 	 		ResultSet rs3=statement.executeQuery();
 	 	 		if(rs3.next()){
 	 	 			flag=true;
 	 	 		}
 	 	 		if(flag==true){
	 	 	 		StringBuffer query8 = new StringBuffer("UPDATE FC_BANK SET ACCOUNT_NO=(SELECT CHANGED_ACCOUNT_NO FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),"
	 	 					+ "BANK_NAME=(SELECT CHANGED_BANK_NAME FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),BANK_ADDRESS=(SELECT CHANGED_BANK_ADDRESS FROM FC_FC6_BANK_CHANGE WHERE "
	 	 					+ "UNIQUE_FILENO=?),BANK_TOWN_CITY=(SELECT CHANGED_BANK_TOWN_CITY FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),BANK_STDIST=("
	 	 					+ "SELECT CHANGED_BANK_STATE||CHANGED_BANK_DISTRICT FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),BANK_PIN=(SELECT CHANGED_BANK_PIN FROM FC_FC6_BANK_CHANGE "
	 	 					+ "WHERE UNIQUE_FILENO=?),NEW_OLD='N',IFSC_CODE=(SELECT CHANGED_BANK_IFSC FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?) WHERE "
	 	 					+ "RCN=(SELECT RCN FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
	 	 	 		statement = connection.prepareStatement(query8.toString()); 
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.setString(2, obj.getApplicationId());
	 	 	 		statement.setString(3, obj.getApplicationId());
	 	 	 		statement.setString(4, obj.getApplicationId());
	 	 	 		statement.setString(5, obj.getApplicationId());
	 	 	 		statement.setString(6, obj.getApplicationId());
	 	 	 		statement.setString(7, obj.getApplicationId());
	 	 	 		statement.setString(8, obj.getApplicationId());
	 	 	 		statement.executeUpdate();
 	 	 		}else{
	 	 	 		StringBuffer query8 = new StringBuffer("INSERT INTO FC_BANK(ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWN_CITY,BANK_STDIST,BANK_PIN,NEW_OLD,RCN,STATUS,IFSC_CODE) VALUES("
	 	 					+ "(SELECT CHANGED_ACCOUNT_NO FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),"
	 	 					+ "(SELECT CHANGED_BANK_NAME FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),"
	 	 					+ "(SELECT CHANGED_BANK_ADDRESS FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),"
	 	 					+ "(SELECT CHANGED_BANK_TOWN_CITY FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),"
	 	 					+ "(SELECT CHANGED_BANK_STATE||CHANGED_BANK_DISTRICT FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),"
	 	 					+ "(SELECT CHANGED_BANK_PIN FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),'N',"
	 	 					+ "(SELECT RCN FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?),'Y',(SELECT CHANGED_BANK_IFSC FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?))");
		 	 		statement = connection.prepareStatement(query8.toString()); 
		 	 		statement.setString(1, obj.getApplicationId());
		 	 		statement.setString(2, obj.getApplicationId());
		 	 		statement.setString(3, obj.getApplicationId());
		 	 		statement.setString(4, obj.getApplicationId());
		 	 		statement.setString(5, obj.getApplicationId());
		 	 		statement.setString(6, obj.getApplicationId());
		 	 		statement.setString(7, obj.getApplicationId());	
		 	 		statement.setString(8, obj.getApplicationId());
		 	 		statement.executeUpdate();
 	 	 		} 	 	 			
 	 		//}
 
 	 		
 	 		// Generating PDF Bytes 			
 			pdfStatus=true; 
    	}else if(serviceCode.equals("16")){      		 // Change Details of OPENING OF UTILIZATION BANK ACCOUNT FC6_D----------------------		
 			Boolean nameFlag=false;
 			Boolean addFlag=false;
 			Boolean recepientBankFlag=false;
 			Boolean natureFlag=false;
 			Boolean memberChangeFlag=false;
 			Boolean utilizationChangeFlag=false;
 			//StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE,ASSO_CHANGE_MEMBER,ASSO_CHANGE_UTILISATION_STATUS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");
 			StringBuffer query5 = new StringBuffer("SELECT IS_NEW_ACCOUNT FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?"); 			
 			
 	 		statement = connection.prepareStatement(query5.toString()); 
 	 		statement.setString(1, obj.getApplicationId());
 	 		ResultSet rs4=statement.executeQuery();
 	 		if(rs4.next()){
 
 	 			// Utilization Bank Change
 	 			if(rs4.getString(1)==null){			
 	 			}
 	 			else if(rs4.getString(1).equals("Y")){
 	 				utilizationChangeFlag=true;
 	 			}
 	 		}
 	 		
 	 		if(utilizationChangeFlag == true){
 	 			// UPDATING DATA INTO FC_UTILIZATION_BANK - START
 	 	 		
 	 	 		/* 1. Updating record_status '1' in FC_UTILIZATION_BANK_HISTORY if available */
 	 	 		//query=new StringBuffer("UPDATE T_FC_UTILIZATION_BANK_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
 	 	 		
 	 	 		query=new StringBuffer("UPDATE T_FC_UTILIZATION_BANK_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?)");
 	 	 		statement = connection.prepareStatement(query.toString()); 
 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 		statement.executeUpdate();
 	 	 		
 	 	 		/* 2. Moving data from FC_UTILIZATION_BANK to t_FC_UTILIZATION_BANK_history */
/* 	 	 		query=new StringBuffer("INSERT INTO T_FC_UTILIZATION_BANK_HISTORY(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,MODIFIED_DATE) "
 	 	 				+ "SELECT RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,systimestamp FROM FC_UTILIZATION_BANK a WHERE RCN"
 	 	 				+ "=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");*/
 	 	 		
 	 	 		query=new StringBuffer("INSERT INTO T_FC_UTILIZATION_BANK_HISTORY(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,MODIFIED_DATE) "
 	 	 				+ "SELECT RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,systimestamp FROM FC_UTILIZATION_BANK a WHERE RCN"
 	 	 				+ "=(SELECT RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?)"); 	 	 		
 	 	 		statement = connection.prepareStatement(query.toString()); 	 		
 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 		statement.executeUpdate(); 	
 	 	 		
 	 	 		/* 3. deleting data from FC_UTILIZATION_BANK */
 	 	 		//query=new StringBuffer("DELETE FROM FC_UTILIZATION_BANK WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
 	 	 		
 	 	 		query=new StringBuffer("DELETE FROM FC_UTILIZATION_BANK WHERE RCN=(SELECT RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?)");
 	 	 		statement = connection.prepareStatement(query.toString()); 	 		
 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 		statement.executeUpdate(); 
 	 	 		
 	 	 		/* 4. inserting into FC_UTILIZATION_BANK */
/* 	 	 		StringBuffer query10=new StringBuffer("INSERT INTO FC_UTILIZATION_BANK(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS) "
 	 	 				+ "SELECT (SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY BANK_NAME ) sno, ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,"
 	 	 				+ "BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,0 FROM FC_FC6_UTILIZATION_BANK a WHERE a.UNIQUE_FILENO=?");*/
 	 	 		
 	 	 		StringBuffer query10=new StringBuffer("INSERT INTO FC_UTILIZATION_BANK(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS)	"
 	 	 				+ " SELECT (SELECT RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?),ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY NEW_BANK_NAME ) sno, "
 	 	 				+ " NEW_ACCOUNT_NO,NEW_BANK_NAME,NEW_BANK_ADDRESS,NEW_BANK_TOWN_CITY,NEW_BANK_STATE,NEW_BANK_DISTRICT,NEW_BANK_PIN,NEW_BANK_IFSC,0 FROM FC_FC6_NEW_UTILIZATION_ACCTS a WHERE a.UNIQUE_FILENO=?"); 	 	 		
 	 	 		statement = connection.prepareStatement(query10.toString()); 			
 	 	 		statement.setString(1, obj.getApplicationId()); 	 		
 	 	 		statement.setString(2, obj.getApplicationId());
 	 	 		statement.executeUpdate(); 	
 	 	 		
 	 	 		// INSERTING INTO T_REGISTRATION_SHARING
 	 	 		
 	 	 		/*StringBuffer query11 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
 	 	 				+ " VALUES ((SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");*/
 	 	 		
 	 	 		StringBuffer query11 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
 	 	 				+ " VALUES ((SELECT RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");
 	 	 		statement = connection.prepareStatement(query11.toString()); 			
 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 		statement.setString(2, "FC_UTILIZATION_BANK"); 	 		 		
 	 	 		statement.executeUpdate(); 	
 	 	 		statement.close();
 	 			
 	 	 		// UPDATING DATA INTO FC_UTILIZATION_BANK - END
 	 	 		
 	 		}
 	 		
 	 		// Generating PDF Bytes 			
 			pdfStatus=true; 
 	    		
    	}else if(serviceCode.equals("17")){        		 // Change Details of CHANGE OF COMMITTEE MEMBERS FC_6E----------------------		
			Boolean nameFlag=false;
			Boolean addFlag=false;
			Boolean recepientBankFlag=false;
			Boolean natureFlag=false;
			Boolean memberChangeFlag=false;
			Boolean utilizationChangeFlag=false;
			/*StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
					+ ",ASSO_CHANGE_MEMBER,ASSO_CHANGE_UTILISATION_STATUS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");*/
			StringBuffer query5 = new StringBuffer("SELECT IS_CHANGE_MEMBERS FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO=?");			
	 		statement = connection.prepareStatement(query5.toString()); 
	 		statement.setString(1, obj.getApplicationId());
	 		ResultSet rs4=statement.executeQuery();
	 		if(rs4.next()){
	 			// Member Change
	 			if(rs4.getString(1)==null){			
	 			}
	 			else if(rs4.getString(1).equals("Y")){
	 				memberChangeFlag=true;
	 			}
	 		}

	 		if(memberChangeFlag==true){
	 		// UPDATING DATA INTO FC_COMMITTEE - START
	 	 		
	 	 		/* 1. Updating record_status '1' in fc_committee_history if available */
	 	 		//query=new StringBuffer("UPDATE T_FC_COMMITTEE_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 		
	 	 		query=new StringBuffer("UPDATE T_FC_COMMITTEE_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT RCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO=?)");
	 	 		statement = connection.prepareStatement(query.toString()); 
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		statement.executeUpdate();
	 	 		
	 	 		/* 2. Moving data from fc_committee to t_fc_committee_history */
/*	 	 		query=new StringBuffer("INSERT INTO T_FC_COMMITTEE_HISTORY(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,MODIFIED_DATE) "
	 				+ "SELECT RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,systimestamp FROM FC_COMMITTEE a WHERE RCN"
	 				+ "=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");*/
	 	 		
	 	 		query=new StringBuffer("INSERT INTO T_FC_COMMITTEE_HISTORY(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,MODIFIED_DATE) "
	 				+ "SELECT RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,systimestamp FROM FC_COMMITTEE a WHERE RCN"
	 				+ "=(SELECT RCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO=?)");
	 	 		
	 	 		statement = connection.prepareStatement(query.toString()); 	 		
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		statement.executeUpdate(); 	
	 	 		
	 	 		/* 3. deleting data from fc_committe */
	 	 		//query=new StringBuffer("DELETE FROM FC_COMMITTEE WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 		query=new StringBuffer("DELETE FROM FC_COMMITTEE WHERE RCN=(SELECT RCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO=?)");
	 	 		statement = connection.prepareStatement(query.toString()); 	 		
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		statement.executeUpdate(); 
	 	 		
	 	 		/* 4. inserting into fc_committe */
/*	 	 		StringBuffer query8=new StringBuffer("INSERT INTO FC_COMMITTEE(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,"
	 	 				+ "ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,"
	 	 				+ "BEARERS_RELATIONSHIP_OTHER,DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,"
	 	 				+ "WHETHER_IN_YN,RECORD_STATUS,SNO) "
	 	 				+ "SELECT (SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,"
	 	 				+ "OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,"
	 	 				+ "OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,b.DOB, b.PLACE_BIRTH, b.PASSPORT_NO, b.PER_ADD_FC, "
	 	 				+ "b.PIO_ORG_STATUS, b.PIO_NO, b.RES_INDIA_STATUS, b.RES_INDIA_DATE, b.WHETHER_IN_YN,"
	 	 				+ "'0', ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY name ) sno FROM FC_FC6_COMMITTEE a left join fc_fc6_foreigners b on a.unique_fileno=b.unique_fileno and a.sl_no=b.sl_no WHERE a.UNIQUE_FILENO=?");*/

	 	 		StringBuffer query8=new StringBuffer("INSERT INTO FC_COMMITTEE(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,"
	 	 				+ "ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,"
	 	 				+ "BEARERS_RELATIONSHIP_OTHER,DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,"
	 	 				+ "WHETHER_IN_YN,RECORD_STATUS,SNO) "
	 	 				+ "SELECT (SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,"
	 	 				+ "OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,"
	 	 				+ "OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,DOB, PLACE_BIRTH, PASSPORT_NO, PER_ADD_FC, "
	 	 				+ "PIO_ORG_STATUS, PIO_NO, RES_INDIA_STATUS, RES_INDIA_DATE, WHETHER_IN_YN,"
	 	 				+ "'0', ROW_NUMBER() OVER (PARTITION BY UNIQUE_FILENO ORDER BY name ) sno FROM FC_FC6_MEMBERS WHERE UNIQUE_FILENO=?");	 	 		
	 	 		statement = connection.prepareStatement(query8.toString()); 			
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		statement.setString(2, obj.getApplicationId());
	 	 		statement.executeUpdate(); 	
	 	 		
	 	 		// INSERTING INTO T_REGISTRATION_SHARING
	 	 		
/*	 	 		StringBuffer query9 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
	 	 				+ " VALUES ((SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");*/
	 	 		StringBuffer query9 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
	 	 				+ " VALUES ((SELECT RCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");	 	 		
	 	 		statement = connection.prepareStatement(query9.toString()); 			
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		statement.setString(2, "FC_COMMITTEE"); 	 		 		
	 	 		statement.executeUpdate(); 	
	 	 		statement.close();
	 			
	 	 		// UPDATING DATA INTO FC_COMMITTEE - END
	 		}
	 		// Generating PDF Bytes 			
			pdfStatus=true; 
	    		
   	
    	}else if(serviceCode.equals("06")){ // Change of Details -------------------------OLD CODE STARTS FROM HERE-----------------------------				 
	 			Boolean nameFlag=false;
	 			Boolean addFlag=false;
	 			Boolean recepientBankFlag=false;
	 			Boolean natureFlag=false;
	 			Boolean memberChangeFlag=false;
	 			Boolean utilizationChangeFlag=false;
	 			StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
	 					+ ",ASSO_CHANGE_MEMBER,ASSO_CHANGE_UTILISATION_STATUS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");
	 	 		statement = connection.prepareStatement(query5.toString()); 
	 	 		statement.setString(1, obj.getApplicationId());
	 	 		ResultSet rs4=statement.executeQuery();
	 	 		if(rs4.next()){
	 	 			// Name Change
	 	 			if(rs4.getString(1)==null){			
	 	 			}
	 	 			else if(rs4.getString(1).equals("Y")){
	 	 				nameFlag=true;
	 	 			} 
	 	 			// Address Change
	 	 			if(rs4.getString(2)==null){			
	 	 			}
	 	 			else if(rs4.getString(2).equals("Y")){
	 	 				addFlag=true;
	 	 			}
	 	 			// Bank Change
	 	 			if(rs4.getString(3)==null){			
	 	 			}
	 	 			else if(rs4.getString(3).equals("Y")){
	 	 				recepientBankFlag=true; 	 	 
	 	 			}
	 	 			// Nature Change
	 	 			if(rs4.getString(4)==null){			
	 	 			}
	 	 			else if(rs4.getString(4).equals("Y")){
	 	 				natureFlag=true;
	 	 			}
	 	 			// Member Change
	 	 			if(rs4.getString(5)==null){			
	 	 			}
	 	 			else if(rs4.getString(5).equals("Y")){
	 	 				memberChangeFlag=true;
	 	 			}
	 	 			// Utilization Bank Change
	 	 			if(rs4.getString(6)==null){			
	 	 			}
	 	 			else if(rs4.getString(6).equals("Y")){
	 	 				utilizationChangeFlag=true;
	 	 			}
	 	 		}
	 	 		if(nameFlag==true){
	 	 			StringBuffer query6 = new StringBuffer("UPDATE FC_INDIA SET ASSO_NAME=(SELECT ASSO_NAME FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)"
	 	 					+ ",ASSO_AIMS=(SELECT MAIN_AIM FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?) WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE "
	 	 					+ "UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query6.toString()); 
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.setString(2, obj.getApplicationId());
	 	 	 		statement.setString(3, obj.getApplicationId());
	 	 	 		statement.executeUpdate();
	 	 		}
	 	 		if(addFlag==true){
	 	 			StringBuffer query7 = new StringBuffer("UPDATE FC_INDIA SET ASSO_ADDRESS=(SELECT ASSO_ADDRESS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)"
	 	 					+ ",ASSO_TOWN_CITY=(SELECT ASSO_TOWN_CITY FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),STDIST=(SELECT ASSO_STATE||ASO_DISTRICT "
	 	 					+ " FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),ASSO_PIN=(SELECT ASSO_PIN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),NEW_OLD='N' "
	 	 					+ " WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query7.toString()); 
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.setString(2, obj.getApplicationId());
	 	 	 		statement.setString(3, obj.getApplicationId());
	 	 	 		statement.setString(4, obj.getApplicationId());
	 	 	 		statement.setString(5, obj.getApplicationId());
	 	 	 		statement.executeUpdate();
	 	 		}
	 	 		if(recepientBankFlag==true){ 
	 	 			//Checking if record available in fc_bank
	 	 	 		StringBuffer subQuery=new StringBuffer("SELECT * FROM FC_BANK WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
	 	 	 		statement = connection.prepareStatement(subQuery.toString()); 
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		Boolean flag=false;
	 	 	 		ResultSet rs3=statement.executeQuery();
	 	 	 		if(rs3.next()){
	 	 	 			flag=true;
	 	 	 		}
	 	 	 		if(flag==true){
 	 	 	 		StringBuffer query8 = new StringBuffer("UPDATE FC_BANK SET ACCOUNT_NO=(SELECT ACCOUNT_NO FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),"
 	 	 					+ "BANK_NAME=(SELECT BANK_NAME FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),BANK_ADDRESS=(SELECT BANK_ADDRESS FROM FC_FC6_FORM WHERE "
 	 	 					+ "UNIQUE_FILENO=?),BANK_TOWN_CITY=(SELECT BANK_TOWNCITY FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),BANK_STDIST=("
 	 	 					+ "SELECT BANK_STATE||BANK_DISTRICT FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),BANK_PIN=(SELECT BANK_PIN FROM FC_FC6_FORM "
 	 	 					+ "WHERE UNIQUE_FILENO=?),NEW_OLD='N',IFSC_CODE=(SELECT BANK_BRANCH_IFSC_CODE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?) WHERE "
 	 	 					+ "RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?) AND STATUS='Y'");
 	 	 	 		statement = connection.prepareStatement(query8.toString()); 
 	 	 	 		statement.setString(1, obj.getApplicationId());
 	 	 	 		statement.setString(2, obj.getApplicationId());
 	 	 	 		statement.setString(3, obj.getApplicationId());
 	 	 	 		statement.setString(4, obj.getApplicationId());
 	 	 	 		statement.setString(5, obj.getApplicationId());
 	 	 	 		statement.setString(6, obj.getApplicationId());
 	 	 	 		statement.setString(7, obj.getApplicationId());
 	 	 	 		statement.setString(8, obj.getApplicationId());
 	 	 	 		statement.executeUpdate();
	 	 	 		}else{
 	 	 	 		StringBuffer query8 = new StringBuffer("INSERT INTO FC_BANK(ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWN_CITY,BANK_STDIST,BANK_PIN,NEW_OLD,RCN,STATUS,IFSC_CODE) VALUES("
 	 	 					+ "(SELECT ACCOUNT_NO FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),"
 	 	 					+ "(SELECT BANK_NAME FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),"
 	 	 					+ "(SELECT BANK_ADDRESS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),"
 	 	 					+ "(SELECT BANK_TOWNCITY FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),"
 	 	 					+ "(SELECT BANK_STATE||BANK_DISTRICT FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),"
 	 	 					+ "(SELECT BANK_PIN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),'N',"
 	 	 					+ "(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),'Y',(SELECT BANK_BRANCH_IFSC_CODE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?))");
 		 	 		statement = connection.prepareStatement(query8.toString()); 
 		 	 		statement.setString(1, obj.getApplicationId());
 		 	 		statement.setString(2, obj.getApplicationId());
 		 	 		statement.setString(3, obj.getApplicationId());
 		 	 		statement.setString(4, obj.getApplicationId());
 		 	 		statement.setString(5, obj.getApplicationId());
 		 	 		statement.setString(6, obj.getApplicationId());
 		 	 		statement.setString(7, obj.getApplicationId());	
 		 	 		statement.setString(8, obj.getApplicationId());
 		 	 		statement.executeUpdate();
	 	 	 		} 	 	 			
	 	 		}
	 	 		if(natureFlag==true){ 	 	 			
	 	 			StringBuffer query8 = new StringBuffer("UPDATE FC_INDIA SET ASSO_NATURE=(SELECT ASSO_NATURE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),"
	 	 					+ "ASSO_RELIGION=(SELECT ASSO_RELIGIOUS FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),NEW_OLD='N' WHERE "
	 	 					+ "RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query8.toString()); 
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.setString(2, obj.getApplicationId());
	 	 	 		statement.setString(3, obj.getApplicationId()); 	 	 	 		
	 	 	 		statement.executeUpdate();
	 	 		}
	 	 		if(memberChangeFlag==true){
	 	 		// UPDATING DATA INTO FC_COMMITTEE - START
	 	 	 		
	 	 	 		/* 1. Updating record_status '1' in fc_committee_history if available */
	 	 	 		query=new StringBuffer("UPDATE T_FC_COMMITTEE_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query.toString()); 
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.executeUpdate();
	 	 	 		
	 	 	 		/* 2. Moving data from fc_committee to t_fc_committee_history */
	 	 	 		query=new StringBuffer("INSERT INTO T_FC_COMMITTEE_HISTORY(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
 	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
 	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,MODIFIED_DATE) "
 	 				+ "SELECT RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,"
 	 				+ "OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,MOBILE_CTR_CODE,"
 	 				+ "DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,WHETHER_IN_YN,RECORD_STATUS,SNO,PANNO,AADHAR_CONCENT,systimestamp FROM FC_COMMITTEE a WHERE RCN"
 	 				+ "=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query.toString()); 	 		
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.executeUpdate(); 	
	 	 	 		
	 	 	 		/* 3. deleting data from fc_committe */
	 	 	 		query=new StringBuffer("DELETE FROM FC_COMMITTEE WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query.toString()); 	 		
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.executeUpdate(); 
	 	 	 		
	 	 	 		/* 4. inserting into fc_committe */
	 	 	 		StringBuffer query8=new StringBuffer("INSERT INTO FC_COMMITTEE(RCN,NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,"
	 	 	 				+ "ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,"
	 	 	 				+ "BEARERS_RELATIONSHIP_OTHER,DOB,PLACE_BIRTH,PASSPORT_NO,PER_ADD_FC,PIO_ORG_STATUS,PIO_NO,RES_INDIA_STATUS,RES_INDIA_DATE,"
	 	 	 				+ "WHETHER_IN_YN,RECORD_STATUS,SNO) "
	 	 	 				+ "SELECT (SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),NAME,FATHER_HUSBAND_NAME,NATIONALITY,OCCUPATION,"
	 	 	 				+ "OFFICE_OF_ASSO,BEARERS_RELATIONSHIP,ADDRESS_OF_ASSO,OLD_NEW,ADDRESS_OF_RESID,EMAIL_ID,LANDLINE,MOBILE,AADHAAR,NATIONALITY_OTHER,"
	 	 	 				+ "OCCUPATION_OTHER,OFFICE_OF_ASSO_OTHER,BEARERS_RELATIONSHIP_OTHER,b.DOB, b.PLACE_BIRTH, b.PASSPORT_NO, b.PER_ADD_FC, "
	 	 	 				+ "b.PIO_ORG_STATUS, b.PIO_NO, b.RES_INDIA_STATUS, b.RES_INDIA_DATE, b.WHETHER_IN_YN,"
	 	 	 				+ "'0', ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY name ) sno FROM FC_FC6_COMMITTEE a left join fc_fc6_foreigners b on a.unique_fileno=b.unique_fileno and a.sl_no=b.sl_no WHERE a.UNIQUE_FILENO=?");
	 	 	 		statement = connection.prepareStatement(query8.toString()); 			
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.setString(2, obj.getApplicationId());
	 	 	 		statement.executeUpdate(); 	
	 	 	 		
	 	 	 		// INSERTING INTO T_REGISTRATION_SHARING
	 	 	 		
	 	 	 		StringBuffer query9 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
	 	 	 				+ " VALUES ((SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");
	 	 	 		statement = connection.prepareStatement(query9.toString()); 			
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.setString(2, "FC_COMMITTEE"); 	 		 		
	 	 	 		statement.executeUpdate(); 	
	 	 	 		statement.close();
	 	 			
	 	 	 		// UPDATING DATA INTO FC_COMMITTEE - END
	 	 		}
	 	 		
	 	 		if(utilizationChangeFlag == true){
	 	 			// UPDATING DATA INTO FC_UTILIZATION_BANK - START
	 	 	 		
	 	 	 		/* 1. Updating record_status '1' in FC_UTILIZATION_BANK_HISTORY if available */
	 	 	 		query=new StringBuffer("UPDATE T_FC_UTILIZATION_BANK_HISTORY SET RECORD_STATUS=1 WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query.toString()); 
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.executeUpdate();
	 	 	 		
	 	 	 		/* 2. Moving data from FC_UTILIZATION_BANK to t_FC_UTILIZATION_BANK_history */
	 	 	 		query=new StringBuffer("INSERT INTO T_FC_UTILIZATION_BANK_HISTORY(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,MODIFIED_DATE) "
	 	 	 				+ "SELECT RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS,systimestamp FROM FC_UTILIZATION_BANK a WHERE RCN"
	 	 	 				+ "=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query.toString()); 	 		
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.executeUpdate(); 	
	 	 	 		
	 	 	 		/* 3. deleting data from FC_UTILIZATION_BANK */
	 	 	 		query=new StringBuffer("DELETE FROM FC_UTILIZATION_BANK WHERE RCN=(SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?)");
	 	 	 		statement = connection.prepareStatement(query.toString()); 	 		
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.executeUpdate(); 
	 	 	 		
	 	 	 		/* 4. inserting into FC_UTILIZATION_BANK */
	 	 	 		StringBuffer query10=new StringBuffer("INSERT INTO FC_UTILIZATION_BANK(RCN,SNO,ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,RECORD_STATUS) "
	 	 	 				+ "SELECT (SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),ROW_NUMBER() OVER (PARTITION BY a.UNIQUE_FILENO ORDER BY BANK_NAME ) sno, ACCOUNT_NO,BANK_NAME,BANK_ADDRESS,"
	 	 	 				+ "BANK_TOWNCITY,BANK_STATE,BANK_DISTRICT,BANK_PIN,BANK_IFSC_CODE,0 FROM FC_FC6_UTILIZATION_BANK a WHERE a.UNIQUE_FILENO=?");
	 	 	 		statement = connection.prepareStatement(query10.toString()); 			
	 	 	 		statement.setString(1, obj.getApplicationId()); 	 		
	 	 	 		statement.setString(2, obj.getApplicationId());
	 	 	 		statement.executeUpdate(); 	
	 	 	 		
	 	 	 		// INSERTING INTO T_REGISTRATION_SHARING
	 	 	 		
	 	 	 		StringBuffer query11 = new StringBuffer("INSERT INTO T_REGISTRATION_SHARING(RCN,TABLE_NAME,RECORD_STATUS,RECORD_DATE,INTIMATION_DATE)"
	 	 	 				+ " VALUES ((SELECT RCN FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?),?,0,systimestamp,null)");
	 	 	 		statement = connection.prepareStatement(query11.toString()); 			
	 	 	 		statement.setString(1, obj.getApplicationId());
	 	 	 		statement.setString(2, "FC_UTILIZATION_BANK"); 	 		 		
	 	 	 		statement.executeUpdate(); 	
	 	 	 		statement.close();
	 	 			
	 	 	 		// UPDATING DATA INTO FC_UTILIZATION_BANK - END
	 	 	 		
	 	 		}
	 	 		
	 	 		// Generating PDF Bytes 			
	 			pdfStatus=true; 
	 	}else if(serviceCode.equals("12")){	// Grievances
	 		regSecStatus=false;
	 		pdfStatus=false;
	 	}else{ 			
 			regSecStatus=false; 			
 			// SENDING MAILS TO OTHER THAN REGISTRATION
 			AutoNotifier notifier=new AutoNotifier(); 	 		 	 		
 	 		getApplicantMail(obj.getApplicationId());
 	 		String mailContent="Your application <b>"+obj.getApplicationId()+" </b>has been processed.";
 	 		if(emailId!=null){
 	 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(1), "2", emailId, connection,obj.getFromOfficeCode(),mailContent);
 	 		}
 	 		if(mobile!=null){
 	 			notifier.setPhoneNumber(mobile);
 	 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(1), "1", "", connection,obj.getFromOfficeCode(),"");
 	 		}
 		}	
 		if(regSecStatus ==true || pdfStatus==true){
 			updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"7",true);			
 			updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"7",chatId,true); 			
 		}else{
 			updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"4",true);			
 			updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"4",chatId,true);		
 			updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getStatusId(),chatId,true);	
 			
 			// UPDATE CURRENT_STATUS IN T_APPLICATION_DETAILS
 			
 			StringBuffer query2 = new StringBuffer("UPDATE T_APPLICATION_STAGE_DETAILS SET CURRENT_STATUS=9 WHERE APPLICATION_ID=?");
 			statement = connection.prepareStatement(query2.toString());		
 			statement.setString(1, obj.getApplicationId());
 			statement.executeUpdate();
 		}
 		if(!(serviceCode.equals("12"))){
 				updateApplicationStatusNotification(obj.getApplicationId(),serviceCode,obj.getStatusId(),obj.getStatusRemark());
 		}
		connection.commit();		
		return "success";
	}
	
	
	// REJECT
	public String submitReject(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String officeCode=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
		
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	
	    
	    StringBuffer query = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=?");
		statement = connection.prepareStatement(query.toString());				
		statement.setString(1, obj.getToOfficeId());		
		ResultSet rs=statement.executeQuery();	
		if(rs.next())
			officeCode=rs.getString(1);
		
		
		
	    StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, officeCode);
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			toUserId=rs1.getString(1);
		else
			toUserId=null;
		
		connection.setAutoCommit(false);
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),officeCode,toUserId
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),officeCode,toUserId,chatId);	    
	    updatePCPendingDetails(obj.getApplicationId(),officeCode,toUserId,chatId);
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"4",true); // SENDER
	    updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"2",true);		// RECEIVER
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),"4",chatId,true);			// SENDER
		updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"2",chatId,true);				// RECEIVER		
		updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getStatusId(),chatId,true);
		
		connection.commit();		
		return "success";
	}
	
	private void getApplicantMail(String appId) throws Exception{
		PreparedStatement statement=null;String svcCode=null;String resultField=null,tableName=null;
		// Getting service_code 
		String query="SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?";
		statement = connection.prepareStatement(query); 
		statement.setString(1, appId);
		ResultSet rs=statement.executeQuery();	 
		if(rs.next()){			
			svcCode=rs.getString(1);
		}		
		// Getting emailIds
		if(svcCode.equals("01")){			// Registration			
			tableName="T_FC8_ENTRY";
			resultField="ASSO_EMAIL,ASSO_MOBILE";
		}else if(svcCode.equals("02")){ 	// Prior Permission
			tableName="FC_FC1A_ENTRY";
			resultField="ASSO_EMAIL,ASSO_MOBILE";
		}else if(svcCode.equals("03")){ 	// Renewal
			tableName="FC_FC5_ENTRY_NEW1";
			resultField="ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE";
		}else if(svcCode.equals("04")){		// Annual Returns
			tableName="";
			resultField="";
			emailId=null;
			mobile=null;
		}else if(svcCode.equals("05")){		// Transfer of Fund
			tableName="FC_FC5_FUNDTRANSFERENTRY";
			resultField="RECIPIENT_EMAIL,RECIPIENT_CHEIF_MOBILE";
		}else if(svcCode.equals("06")){		// Change of Details
			tableName="FC_FC6_FORM";
			resultField="ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE";
		}else if(svcCode.equals("07")){		// Hospitality
			tableName="FC_H_ENTRY";
			resultField="APPT_EMAIL,APPT_MOBILE";
		}else if(svcCode.equals("08")){		// Foreign contribution to individual as gift
			tableName="FC_FC1_GIFT_ENTRY";
			resultField="RECIPIENT_EMAIL,RECIPIENT_MOBNO";
		}else if(svcCode.equals("09")){		// Foreign contribution to individual as articles
			tableName="FC_FC1_ARTCL_SECURITIES_ENTRY";
			resultField="APPLICANT_EMAIL,null";
		}else if(svcCode.equals("10")){		// Foreign contribution to individual as security
			tableName="FC_FC1_SECURITIES_ENTRY1";
			resultField="APPLICANT_EMAIL,null";
		}else if(svcCode.equals("11")){		// Foreign contribution to a candidate for election
			tableName="FC_FC1_ARTCL_SECURITIES_ENTRY";
			resultField="APPLICANT_EMAIL,APPLICANT_MOBILE_NO";
		}else if(svcCode.equals("13")){		// Change of name and address Details
			tableName="FC_FC6_NAMEADDRESS_CHANGE";
			resultField="ASSO_OFFICIAL_EMAIL,ASSO_CHIEF_MOBILE";
		}else if(svcCode.equals("15")){		// Change of fc-cum utilization bank account Details
			tableName="FC_FC6_BANK_CHANGE";
			resultField="ASSO_OFFICIAL_EMAIL,ASSO_CHIEF_MOBILE";
		}else if(svcCode.equals("16")){		// Change of utilization bank Details
			tableName="FC_FC6_UTILIZATION_ACCTS_ENTRY";
			resultField="CURRENT_ASSO_EMAIL,CURRENT_ASSO_CHIEF_MOBILE";
		}else if(svcCode.equals("17")){		// Change of committee members Details
			tableName="FC_FC6_MEMBERS_CHANGE_ENTRY";
			resultField="ASSO_OFFICIAL_EMAIL,ASSO_CHIEF_MOBILE";
		}
		
		String query1="SELECT "+resultField+" FROM "+tableName+" WHERE UNIQUE_FILENO=?";
		statement = connection.prepareStatement(query1); 
		statement.setString(1, appId);
		ResultSet rs1=statement.executeQuery();	 
		if(rs1.next()){			
			emailId=rs1.getString(1);
			mobile=rs1.getString(2);			
		}		
	}
	
	// DENIED
	public String submitDenied(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String officeCode=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
		
		// TEMPORARY //
			StringBuffer queryTemp = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
	 		statement = connection.prepareStatement(queryTemp.toString()); 			
	 		statement.setString(1, obj.getApplicationId());
	 		ResultSet rsTmp=statement.executeQuery();
	 		String sode=null;	 		
	 		if(rsTmp.next()){
	 			sode=rsTmp.getString(1);	 			
	 		}
	 		if(sode.equals("03")){
	 			//throw new ValidationException("Disposal of the Renewal Services has been suspended till further orders.");
	 		}
 		// TEMPORARY //
		
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	
	    
	    // GETTING SERVICE CODE 
		StringBuffer query3 = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query3.toString()); 			
		statement.setString(1, obj.getApplicationId());
		ResultSet rs2=statement.executeQuery();
		String serviceCode=null; 		
		if(rs2.next()){
			serviceCode=rs2.getString(1); 			
		}
	    
	    StringBuffer query = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=?");
		statement = connection.prepareStatement(query.toString());				
		statement.setString(1, obj.getToOfficeId());		
		ResultSet rs=statement.executeQuery();	
		if(rs.next())
			officeCode=rs.getString(1);
		
	    StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, officeCode);
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			toUserId=rs1.getString(1);
		else
			toUserId=null;
		
		connection.setAutoCommit(false);
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),null,null
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),officeCode,toUserId,chatId);	    
	    updatePCPendingDetails(obj.getApplicationId(),officeCode,toUserId,chatId);	   						
		updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getStatusId(),chatId,true);
		if(!(serviceCode.equals("12"))){
			updateApplicationStatusNotification(obj.getApplicationId(),serviceCode,obj.getStatusId(),obj.getStatusRemark());
		}
		
		if(serviceCode.equals("01")){
			pdfStatus=true;
		}else if(serviceCode.equals("02")){
			pdfStatus=true;
		}else if(serviceCode.equals("12")){
			pdfStatus=false;
		}else{
			pdfStatus=false;
			// SENDING MAILS TO OTHER THAN REGISTRATION
 			AutoNotifier notifier=new AutoNotifier(); 	 		 	 		
 	 		getApplicantMail(obj.getApplicationId());
 	 		String mailContent="Your application "+obj.getApplicationId()+" has been refused due to following reasons:<br><b>"+obj.getStatusRemark()+"</b>";
 	 		if(emailId!=null)
 	 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(3), "2", emailId, connection,obj.getFromOfficeCode(),mailContent);
 	 		
 	 		if(mobile!=null){
 	 			notifier.setPhoneNumber(mobile);
 	 			notifyList=notifier.pushAutoNotifications(obj.getApplicationId(), Integer.valueOf(3), "1", "", connection,obj.getFromOfficeCode(),"");
 	 		}
		}
		// UPDATE CURRENT_STATUS IN T_APPLICATION_DETAILS
		if(pdfStatus==true){
 			updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"7",true);			
 			updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"7",chatId,true); 			
 		}else{
 			updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"4",true);			
 			updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"4",chatId,true);			
 			
 			// UPDATE CURRENT_STATUS IN T_APPLICATION_DETAILS 			
 			StringBuffer query2 = new StringBuffer("UPDATE T_APPLICATION_STAGE_DETAILS SET CURRENT_STATUS=10 WHERE APPLICATION_ID=?");
 			statement = connection.prepareStatement(query2.toString());		
 			statement.setString(1, obj.getApplicationId());
 			statement.executeUpdate();
 		}	
		connection.commit();	
		
		return "success";
	}

	// CLOSE
	public String submitClose(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String officeCode=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	
	    
	    // GETTING SERVICE CODE 
		StringBuffer query3 = new StringBuffer("SELECT  SERVICE_CODE FROM V_APPLICATION_DETAILS2 WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query3.toString()); 			
		statement.setString(1, obj.getApplicationId());
		ResultSet rs2=statement.executeQuery();
		String serviceCode=null; 		
		if(rs2.next()){
			serviceCode=rs2.getString(1); 			
		}
	    
	    StringBuffer query = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=?");
		statement = connection.prepareStatement(query.toString());				
		statement.setString(1, obj.getToOfficeId());		
		ResultSet rs=statement.executeQuery();	
		if(rs.next())
			officeCode=rs.getString(1);
		
	    StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, officeCode);
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			toUserId=rs1.getString(1);
		else
			toUserId=null;
		
		connection.setAutoCommit(false);
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),null,null
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),officeCode,toUserId,chatId);	    
	    updatePCPendingDetails(obj.getApplicationId(),officeCode,toUserId,chatId);	   						
		updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getStatusId(),chatId,true);
		if(!(serviceCode.equals("12"))){
			updateApplicationStatusNotification(obj.getApplicationId(),serviceCode,obj.getStatusId(),obj.getStatusRemark());
		}
		updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"4",true);			
		updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"4",chatId,true);			
		
		// UPDATE CURRENT_STATUS IN T_APPLICATION_DETAILS 			
		StringBuffer query2 = new StringBuffer("UPDATE T_APPLICATION_STAGE_DETAILS SET CURRENT_STATUS=12 WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query2.toString());		
		statement.setString(1, obj.getApplicationId());
		statement.executeUpdate();		
		connection.commit();		
		return "success";
	}

	public String submitCloseForIBRAW(Chat obj) throws Exception{
		PreparedStatement statement=null;String toUserId=null;String officeCode=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		
		// Checking MHA USER ROLE
		if(obj.getFromOfficeId().equals("1")){
			if(checkUserRoleForGranting(obj.getFromUserId())==false){
				throw new ValidationException("You are not authorized to peform this operation.");
			}
		}
		
	    String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId	
	    
	    StringBuffer query = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=?");
		statement = connection.prepareStatement(query.toString());				
		statement.setString(1, obj.getToOfficeId());		
		ResultSet rs=statement.executeQuery();	
		if(rs.next())
			officeCode=rs.getString(1);	
		
		
	    StringBuffer query1 = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, obj.getApplicationId());		
		statement.setString(2, officeCode);
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			toUserId=rs1.getString(1);
		else
			toUserId=null;
		
		connection.setAutoCommit(false);
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),officeCode,toUserId
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());				
	    updatePCOfficeUserDetails(obj.getApplicationId(),officeCode,toUserId,chatId);	    
	    updatePCPendingDetails(obj.getApplicationId(),officeCode,toUserId,chatId);
	    updatePCUserLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getFromUserId(),chatId,"4",true); // SENDER
	    updatePCUserLevelStatus(obj.getApplicationId(),officeCode,toUserId,chatId,"2",true);		// RECEIVER
	    updatePCOfficeLevelStatus(obj.getApplicationId(),obj.getFromOfficeCode(),"4",chatId,true);			// SENDER
		updatePCOfficeLevelStatus(obj.getApplicationId(),officeCode,"2",chatId,true);				// RECEIVER		
		updatePCOfficeLevelFinalStatus(obj.getApplicationId(),obj.getFromOfficeCode(),obj.getStatusId(),chatId,true);
		
		connection.commit();		
		return "success";
	}
	
	private void uploadChatAttachments(String chatId,String sessionId) throws Exception{
		PreparedStatement statement=null;
		
		// Inserting Attachments into T_NOTIFICATION_DOCUMENT from T_UPLOAD_CACHE		
		StringBuffer query1 = new StringBuffer("INSERT INTO  T_PC_CHAT_ATTACHMENT(CHAT_ID,ATTACHMENT_ID,NAME,TYPE,DATA,DATED,RECORD_STATUS) "+
							"(SELECT ?,UPLOAD_ID+1,NAME,TYPE,DOCUMENT,sysdate,? FROM T_UPLOAD_CACHE WHERE SESSION_ID=?)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, chatId);				
			statement.setInt(2, 0);							
			statement.setString(3, sessionId);
			statement.executeUpdate();
			statement.close();	
		
		// DELETE FROM T_UPLOAD CACHE
		StringBuffer query2=new StringBuffer("DELETE FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
			statement = connection.prepareStatement(query2.toString());
			statement.setString(1, sessionId);	
			statement.executeUpdate();
			statement.close();
	}
	
	public String deleteUploadCache(String sessionId) throws Exception{
		PreparedStatement statement=null;
		// DELETE FROM T_UPLOAD CACHE
		StringBuffer query2=new StringBuffer("DELETE FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
		statement = connection.prepareStatement(query2.toString());
		statement.setString(1, sessionId);	
		statement.executeUpdate();
		statement.close();
		return "success";
	}
	
	public void updatePCCommunication(String appId,String chatId,String parentChatId,String subStageId,String fromOfficeCode,String fromUserId,
			String toOfficeCode,String toUserId,String statusId,String statusRemark,String recordStatus,String otherRemark) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query = new StringBuffer("INSERT INTO T_PC_COMMUNICATION VALUES(?,?,?,?,"
				+ "?,?,?,?,?,sysdate,?,?,?)");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		statement.setString(2, chatId);
		statement.setString(3, parentChatId);
		statement.setString(4, subStageId);
		statement.setString(5, fromOfficeCode);		
		statement.setString(6, fromUserId);
		statement.setString(7, toOfficeCode);
		statement.setString(8, toUserId);
		statement.setString(9, statusId);
		statement.setString(10, statusRemark);
		statement.setString(11, recordStatus);
		if(otherRemark == null){
			otherRemark="";
	    }	    else{
	    	if(!otherRemark.endsWith(".")){
	    		otherRemark=otherRemark+".";	
	    	}
	    }
		statement.setString(12, otherRemark);
		statement.executeUpdate();
	}
	public void updatePCOfficeUserDetails(String appId,String officeCode,String userId,String chatId) throws Exception{
		PreparedStatement statement=null;		
		StringBuffer query = new StringBuffer("SELECT * FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);
		statement.setString(2, officeCode);
		ResultSet rs=statement.executeQuery();
		Boolean existFlag=false;
		if(rs.next())
			existFlag=true;
		else
			existFlag=false;
		if(existFlag==true){
			StringBuffer query1 = new StringBuffer("UPDATE T_PC_OFFICE_USER_DETAILS SET USER_ID=?,CHAT_ID=? WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
			statement = connection.prepareStatement(query1.toString());	
			statement.setString(1, userId);
			statement.setString(2, chatId);
			statement.setString(3, appId);
			statement.setString(4, officeCode);
			statement.executeUpdate();
		}else{
			StringBuffer query1 = new StringBuffer("INSERT INTO T_PC_OFFICE_USER_DETAILS VALUES(?,?,?,?)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);
			statement.setString(2, officeCode);
			statement.setString(3, userId);		
			statement.setString(4, chatId);
			statement.executeUpdate();
		}

	}
	
	public void updatePCPendingDetails(String appId,String officeCode,String userId,String chatId) throws Exception{
		PreparedStatement statement=null;
		Boolean existFlag=false;
		StringBuffer query = new StringBuffer("SELECT * FROM T_PC_PENDING_DETAILS WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		ResultSet rs1=statement.executeQuery();		
		if(rs1.next())
			existFlag=true;
		else
			existFlag=false;
		if(existFlag==true){
			StringBuffer query1 = new StringBuffer("UPDATE T_PC_PENDING_DETAILS SET USER_ID=?,CHAT_ID=?,OFFICE_CODE=? WHERE APPLICATION_ID=?");
			statement = connection.prepareStatement(query1.toString());	
			statement.setString(1, userId);
			statement.setString(2, chatId);
			statement.setString(3, officeCode);
			statement.setString(4, appId);		
			statement.executeUpdate();
		}else{
			StringBuffer query1 = new StringBuffer("INSERT INTO T_PC_PENDING_DETAILS VALUES(?,?,?,?)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);
			statement.setString(2, officeCode);
			statement.setString(3, userId);				
			statement.setString(4, chatId);
			statement.executeUpdate();
		}
	}
	
	public Boolean flagCheckPCUserLevelStatus(String appId,String officeCode,String userId) throws Exception{
		PreparedStatement statement=null;
		Boolean existFlag=false;
		StringBuffer query = new StringBuffer("SELECT * FROM T_PC_USER_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=? AND USER_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);
		statement.setString(2, officeCode);
		statement.setString(3, userId);		
		ResultSet rs2=statement.executeQuery();
		if(rs2.next())
			existFlag=true;
		else
			existFlag=false;
		return existFlag;
	}
	
	public Boolean flagCheckPCSectionDetails(String appId,String officeCode) throws Exception{
		PreparedStatement statement=null;
		Boolean existFlag=false;
		StringBuffer query = new StringBuffer("SELECT * FROM T_PC_SECTION_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);
		statement.setString(2, officeCode);				
		ResultSet rs2=statement.executeQuery();
		if(rs2.next())
			existFlag=true;
		else
			existFlag=false;
		return existFlag;
	}
	
	public void updatePCUserLevelStatus(String appId,String officeCode,String userId,String chatId,String stageId,Boolean existFlag) throws Exception{
		PreparedStatement statement=null;		
		if(existFlag==true){
			StringBuffer query1 = new StringBuffer("UPDATE T_PC_USER_LEVEL_STATUS SET STAGE_ID=?,CHAT_ID=?,ACTIVITY_ON=sysdate WHERE APPLICATION_ID=? AND OFFICE_CODE=? AND USER_ID=?");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, stageId);
			statement.setString(2, chatId);
			statement.setString(3, appId);
			statement.setString(4, officeCode);
			statement.setString(5, userId);		
			statement.executeUpdate();
		}else{
			StringBuffer query1 = new StringBuffer("INSERT INTO T_PC_USER_LEVEL_STATUS VALUES(?,?,?,?,?,sysdate)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);
			statement.setString(2, officeCode);
			statement.setString(3, userId);		
			statement.setString(4, stageId);
			statement.setString(5, chatId);
			statement.executeUpdate();
		}
	}
	
	
	
	public Boolean flagCheckPCOfficeLevelStatus(String appId,String officeCode) throws Exception{
		PreparedStatement statement=null;
		Boolean existFlag=false;		
		StringBuffer query = new StringBuffer("SELECT * FROM T_PC_OFFICE_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);
		statement.setString(2, officeCode);				
		ResultSet rs4=statement.executeQuery();		
		if(rs4.next())
			existFlag=true;
		else
			existFlag=false;
		return existFlag;
	}
	
	public void updatePCOfficeLevelStatus(String appId,String officeCode,String stageId,String chatId,Boolean existFlag) throws Exception{
		PreparedStatement statement=null;		
		if(existFlag==true){
			StringBuffer query1 = new StringBuffer("UPDATE T_PC_OFFICE_LEVEL_STATUS SET STAGE_ID=?,CHAT_ID=?,ACTIVITY_ON=sysdate WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, stageId);
			statement.setString(2, chatId);
			statement.setString(3, appId);
			statement.setString(4, officeCode);					
			statement.executeUpdate();
		}else{
			StringBuffer query1 = new StringBuffer("INSERT INTO T_PC_OFFICE_LEVEL_STATUS VALUES(?,?,?,?,sysdate)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);
			statement.setString(2, officeCode);					
			statement.setString(3, stageId);
			statement.setString(4, chatId);
			statement.executeUpdate();
		}
	}
	
	public void updatePCOfficeLevelFinalStatus(String appId,String officeCode,String statusId,String chatId,Boolean existFlag) throws Exception{
		PreparedStatement statement=null;		
		if(existFlag==true){
			StringBuffer query1 = new StringBuffer("UPDATE T_PC_OFFICE_LEVEL_FINAL_STATUS SET STATUS=?,CHAT_ID=?,ACTIVITY_ON=sysdate WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, statusId);
			statement.setString(2, chatId);
			statement.setString(3, appId);
			statement.setString(4, officeCode);					
			statement.executeUpdate();
		}else{
			StringBuffer query1 = new StringBuffer("INSERT INTO T_PC_OFFICE_LEVEL_FINAL_STATUS VALUES(?,?,?,?,sysdate)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);
			statement.setString(2, officeCode);					
			statement.setString(3, statusId);
			statement.setString(4, chatId);
			statement.executeUpdate();
		}
	}
	
	public void updatePCSectionDetails(String appId,String officeCode,String sectionId,String chatId,Boolean existFlag) throws Exception{
		PreparedStatement statement=null;		
		if(existFlag==true){
			StringBuffer query1 = new StringBuffer("UPDATE T_PC_SECTION_DETAILS SET SECTION_ID=?,CHAT_ID=? WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, sectionId);
			statement.setString(2, chatId);
			statement.setString(3, appId);
			statement.setString(4, officeCode);					
			statement.executeUpdate();
		}else{
			StringBuffer query1 = new StringBuffer("INSERT INTO T_PC_SECTION_DETAILS VALUES(?,?,?,?)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);
			statement.setString(2, officeCode);					
			statement.setString(3, sectionId);
			statement.setString(4, chatId);
			statement.executeUpdate();
		}
	}
	
	public void updateApplicationProcessingTime(String appId,String chatId,Boolean existFlag) throws Exception{
		PreparedStatement statement=null;		
		if(existFlag==true){
			StringBuffer query1 = new StringBuffer("UPDATE T_APPLICATION_PROCESSING_TIME SET REPLY_SENT=sysdate,CHAT_ID=?,RECORD_STATUS=1 WHERE APPLICATION_ID=? "
					+ "AND REPLY_SENT IS NULL");
			statement = connection.prepareStatement(query1.toString());		
			statement.setString(1, chatId);
			statement.setString(2, appId);								
			statement.executeUpdate();
		}else{
			StringBuffer query1 = new StringBuffer("INSERT INTO T_APPLICATION_PROCESSING_TIME(APPLICATION_ID,REPLY_RECEIVED,RECORD_STATUS,CHAT_ID) "
					+ "VALUES(?,sysdate,0,?)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);			
			statement.setString(2, chatId);
			statement.executeUpdate();
		}
	}
	
	public void updateApplicationStatusNotification(String appId,String serviceCode,String statusId,String statusRemark) throws Exception{		
			PreparedStatement statement=null;
			StringBuffer query1 = new StringBuffer("INSERT INTO T_APPLICATION_STATUS_NOTIFN(APPLICATION_ID,SERVICE_CODE,APPLICATION_STATUS,"
					+ "STATUS_REMARK,NOTIFIED_ON,RECORD_STATUS) "
					+ "VALUES(?,?,?,?,sysdate,0)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, appId);			
			statement.setString(2, serviceCode);
			statement.setString(3, statusId);
			statement.setString(4, statusRemark);			
			statement.executeUpdate();
		
	}
	
	public void updateApplicationStatusDetails(String appId, String subStageId, String userId, String chatId) throws Exception{
		StringBuffer query1 = new StringBuffer("INSERT INTO T_APPLICATION_STATUS_DETAILS(APPLICATION_ID, SUB_STAGE_ID, STAGE_COMPLETION_DATE, "
				+ "CHAT_ID, USER_ID, RECORD_STATUS) VALUES(?, ?, sysdate, ?, ?, 0)");
		PreparedStatement statement = connection.prepareStatement(query1.toString());
		statement.setString(1, appId);
		statement.setString(2, subStageId);
		statement.setString(3, chatId);
		statement.setString(4, userId);
		statement.executeUpdate();		
	}
	
	public List<List2> officeMarkList(ProjectRequest obj) throws Exception{
		List<List2>  markList = null; StringBuffer query = null; PreparedStatement statement = null; String serviceCode = "";
		if(connection == null) {
			throw new Exception("Invalid connection");
		}		
		query = new StringBuffer("SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID = ?");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, obj.getApplicationId());		
		ResultSet rs1 = statement.executeQuery();
		if(rs1.next())
		{
			serviceCode = rs1.getString(1);
		}
		statement.close();
		rs1.close();
		if(serviceCode.equals("01")||serviceCode.equals("02")||serviceCode.equals("03")||serviceCode.equals("05")||serviceCode.equals("06")||serviceCode.equals("13")||serviceCode.equals("15")||serviceCode.equals("16")||serviceCode.equals("17")){
			query = new StringBuffer("SELECT (SELECT OFFICE_NAME FROM TM_OFFICE WHERE OFFICE_CODE=A.OFFICE_CODE) "
					+ " || ' [' || A.OFFICE_CODE || ']' AS OFFICE, A.OFFICE_ID FROM TM_OFFICE A WHERE RECORD_STATUS = 0 AND OFFICE_ID NOT IN (0,1) "
					+ "AND OFFICE_CODE NOT IN (SELECT OFFICE_CODE FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE APPLICATION_ID = ?)");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, obj.getApplicationId());		
			ResultSet rs = statement.executeQuery();
			markList = new ArrayList<List2>();
			while(rs.next()) {	
				String office=rs.getString(1);	
				String officeId = rs.getString(2);
				markList.add(new List2(office, officeId));			
			}
		}
		return markList;
	}	
	
	public void markToOffice(Chat obj) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement statement = null; 
		StringBuffer query2 = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID in(?) AND RECORD_STATUS=0");
		statement = connection.prepareStatement(query2.toString()); 					
		statement.setString(1, obj.getToOfficeId());
		ResultSet rs1=statement.executeQuery();
		String office=null;
		while(rs1.next()){
			office=rs1.getString(1);		 				
		} 	

		String chatId=generateChatId(obj.getFromOfficeCode());	// Generating chatId
		updatePCCommunication(obj.getApplicationId(),chatId,null,null,obj.getFromOfficeCode(),obj.getFromUserId(),office,null
	    		,obj.getStatusId(),obj.getStatusRemark(),obj.getRecordStatus(),obj.getOtherRemark());	
		updatePCOfficeLevelStatus(obj.getApplicationId(),office,"1",chatId,false);	
		updatePCOfficeLevelFinalStatus(obj.getApplicationId(),office,null,chatId,false);

		// Getting section for inserting row in to PC_Section
		StringBuffer query4 = new StringBuffer("SELECT SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SERVICE_CODE="
				+ "(SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?)");
		statement = connection.prepareStatement(query4.toString()); 
		statement.setString(1, office);
		statement.setString(2, obj.getApplicationId());
		ResultSet rs4=statement.executeQuery();	
		String sectionId=null;
		if(rs4.next())
			sectionId=rs4.getString(1);
		updatePCSectionDetails(obj.getApplicationId(), office, sectionId, chatId, false);

	}

	
	@Override
	public Integer insertRecord(ProjectRequest object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(ProjectRequest object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProjectRequest> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<ProjectRequest> list) {
		// TODO Auto-generated method stub
		return null;
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

	public List<List2> getDocList() {
		return docList;
	}

	public void setDocList(List<List2> docList) {
		this.docList = docList;
	}

	public List<List2> getNextStageForwardList() {
		return nextStageForwardList;
	}

	public void setNextStageForwardList(List<List2> nextStageForwardList) {
		this.nextStageForwardList = nextStageForwardList;
	}

	public String getNextStageId() {
		return nextStageId;
	}

	public void setNextStageId(String nextStageId) {
		this.nextStageId = nextStageId;
	}

	public List<List2> getUploadedDocList() {
		return uploadedDocList;
	}

	public void setUploadedDocList(List<List2> uploadedDocList) {
		this.uploadedDocList = uploadedDocList;
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

	public String getAppFinalStatus() {
		return appFinalStatus;
	}

	public void setAppFinalStatus(String appFinalStatus) {
		this.appFinalStatus = appFinalStatus;
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

	public List<Notification> getNotifyList() {
		return notifyList;
	}

	public void setNotifyList(List<Notification> notifyList) {
		this.notifyList = notifyList;
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

	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
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

	public Boolean getNotWithMeTofetch() {
		return notWithMeTofetch;
	}

	public void setNotWithMeTofetch(Boolean notWithMeTofetch) {
		this.notWithMeTofetch = notWithMeTofetch;
	}

	public Boolean getNotWithMe() {
		return notWithMe;
	}

	public void setNotWithMe(Boolean notWithMe) {
		this.notWithMe = notWithMe;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getRedFlagClearingRemarks() {
		return redFlagClearingRemarks;
	}

	public void setRedFlagClearingRemarks(String redFlagClearingRemarks) {
		this.redFlagClearingRemarks = redFlagClearingRemarks;
	}

	public String getRedFlag() {
		return redFlag;
	}

	public void setRedFlag(String redFlag) {
		this.redFlag = redFlag;
	}

	public String getRedFlagCategory() {
		return redFlagCategory;
	}

	public void setRedFlagCategory(String redFlagCategory) {
		this.redFlagCategory = redFlagCategory;
	}

	public String getRedFlagCategoryCode() {
		return redFlagCategoryCode;
	}

	public void setRedFlagCategoryCode(String redFlagCategoryCode) {
		this.redFlagCategoryCode = redFlagCategoryCode;
	}

	public String getGrantingOfficalFlag() {
		return grantingOfficalFlag;
	}

	public void setGrantingOfficalFlag(String grantingOfficalFlag) {
		this.grantingOfficalFlag = grantingOfficalFlag;
	}

	public String getProcessingOfficialFlag() {
		return processingOfficialFlag;
	}

	public void setProcessingOfficialFlag(String processingOfficialFlag) {
		this.processingOfficialFlag = processingOfficialFlag;
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

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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


	public Boolean getMarkOffice() {
		return markOffice;
	}

	public void setMarkOffice(Boolean markOffice) {
		this.markOffice = markOffice;
	}


	public String getClarificationOfficialFlag() {
		return clarificationOfficialFlag;
	}

	public void setClarificationOfficialFlag(String clarificationOfficialFlag) {
		this.clarificationOfficialFlag = clarificationOfficialFlag;
	}

	public String getBlueFlag() {
		return blueFlag;
	}

	public void setBlueFlag(String blueFlag) {
		this.blueFlag = blueFlag;
	}
}
