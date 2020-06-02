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
import models.services.RedFlagAssociations;
import models.services.RedFlagDonors;
import models.services.requests.AbstractRequest;
import models.services.requests.Chat;
import models.services.requests.InvestigationAgency;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class InvestigationAgencyReportDao extends BaseDao<ProjectRequest, String, String>{
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
	public List<Notification> notifyList = new ArrayList<Notification>();
	private Boolean doExist=false;
	private String finalStatus=null;
	List<KVPair<String, String>> officeNameList;
	
	
	public InvestigationAgencyReportDao(Connection connection) {
		super(connection);
	}	
			
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_CODE,OFFICE_NAME||'['||OFFICE_CODE||']' AS OFFICE_NAME FROM TM_OFFICE WHERE OFFICE_ID IN (2,3) AND RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  officeNameList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			officeNameList.add(temp);			
		}
		return officeNameList;
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
	
	public List<AbstractRequest> getApplicationDetails(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;	String currentStatus=null,tempAppId=null;
		query = new StringBuffer("SELECT A.APPLICATION_ID,A.SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=A.SERVICE_CODE) "
				+ "AS SERVICE,A.CURRENT_STAGE,A.CURRENT_STATUS,(SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=A.CURRENT_STATUS) AS STATUS ,"
				+ " TO_CHAR(A.SUBMISSION_DATE,'DD-MM-YYYY'),APPLICANT_NAME,A.TEMP_FILENO,A.SECTION_FILENO,(SELECT SNAME FROM TM_STATE WHERE SCODE=A.STATE)AS STATE,"
                + "(SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=A.DISTRICT)AS DISTRICT FROM V_APPLICATION_DETAILS A "
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
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)
					,rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12)));			
		}
		return applicationList;
	}

	public List<List2> officeStatusList(String appId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT (SELECT OFFICE_NAME FROM TM_OFFICE WHERE OFFICE_CODE=A.OFFICE_CODE) "
				+ "|| ' [' || A.OFFICE_CODE || ']' AS OFFICE,A.STATUS FROM T_PC_OFFICE_LEVEL_FINAL_STATUS A WHERE A.APPLICATION_ID=? "
				+ "AND A.OFFICE_CODE NOT IN(SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_ID=1)");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
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

	public int insertRecord(InvestigationAgency ia) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement stmt,stmt1,statement,statement1,statement2,statement3,statement4,statement5,statement6,statement7,statement8= null,statement9;
		ResultSet rs1=null;
		int row1=0;
		StringBuffer query = null ;
		
		
		query=new StringBuffer("SELECT APPLICATION_ID,REPORT_FROM FROM T_INVESTIGATION_AGENCY_REPORT WHERE APPLICATION_ID=? AND REPORT_FROM=?");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");		
		stmt=connection.prepareStatement(countQuery.toString());
		stmt.setString(1,ia.getApplicationId());
		stmt.setString(2,ia.getReportFrom());
		ResultSet rse = stmt.executeQuery();
		int abc=0;
		if(rse.next()){		 
		 abc=rse.getInt(1);
		}
		rse.close();
		stmt.close();
		if(abc>0){			
				query=new StringBuffer("UPDATE T_INVESTIGATION_AGENCY_REPORT SET REPORT_NUMBER=?,REPORT_DATE=TO_DATE(?,'DD-MM-YYYY'),REPORT_DOC_SNO=NULL,ENTERED_BY=?,ENTERED_ON=SYSDATE WHERE APPLICATION_ID=? AND REPORT_FROM=?");
				stmt=connection.prepareStatement(query.toString());
				stmt.setString(1, ia.getReportNumber());
				stmt.setString(2, ia.getReportDate());
				stmt.setString(3,ia.getMyUserId());
				stmt.setString(4, ia.getApplicationId());
				stmt.setString(5, ia.getReportFrom());
				stmt.executeUpdate();
				stmt.close();		
		}		
		else{
				query = new StringBuffer("INSERT INTO T_INVESTIGATION_AGENCY_REPORT(APPLICATION_ID,REPORT_FROM,REPORT_NUMBER,REPORT_DATE,REPORT_DOC_SNO,ENTERED_BY,ENTERED_ON)"
					+ " VALUES(?,?,?,TO_DATE(?,'DD-MM-YYYY'),NULL,?,sysdate)");		
				statement=connection.prepareStatement(query.toString());
				statement.setString(1,ia.getApplicationId());
				statement.setString(2,ia.getReportFrom());
				statement.setString(3, ia.getReportNumber());
				statement.setString(4,ia.getReportDate());
				statement.setString(5,ia.getMyUserId());
				statement.executeUpdate();
				statement.close();
		}
		
		query = new StringBuffer("SELECT STATUS FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement1 = connection.prepareStatement(query.toString());
		statement1.setString(1, ia.getApplicationId());
		statement1.setString(2, ia.getReportFrom());
		ResultSet rs = statement1.executeQuery();
		while(rs.next()){
			if(rs.getString(1)==null){
				query = new StringBuffer("UPDATE T_PC_OFFICE_LEVEL_FINAL_STATUS SET STATUS=? WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
				statement2 = connection.prepareStatement(query.toString());
				statement2.setString(1, "5");
				statement2.setString(2, ia.getApplicationId());
				statement2.setString(3, ia.getReportFrom());
				statement2.executeUpdate();
				statement2.close();
		        
				query = new StringBuffer("SELECT STAGE_ID FROM T_PC_OFFICE_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
				statement3 = connection.prepareStatement(query.toString());
				statement3.setString(1, ia.getApplicationId());
				statement3.setString(2, ia.getReportFrom());
				rs1 = statement3.executeQuery();
				if(rs1.next()){			
					query = new StringBuffer("UPDATE T_PC_OFFICE_LEVEL_STATUS SET STAGE_ID=? WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
					statement4 = connection.prepareStatement(query.toString());
					statement4.setString(1, "4");
					statement4.setString(2, ia.getApplicationId());
					statement4.setString(3, ia.getReportFrom());	
					statement4.executeUpdate();
					statement4.close();
				}			
			
				statement3.close();
				String fromuser="";
				query = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
				statement5 = connection.prepareStatement(query.toString());
				statement5.setString(1, ia.getApplicationId());
				statement5.setString(2, ia.getReportFrom());		
				rs1 = statement5.executeQuery();		
				if(rs1.next()){
					fromuser=rs1.getString(1);
				}
				rs1.close();
				statement5.close();
				query = new StringBuffer("UPDATE T_PC_USER_LEVEL_STATUS SET STAGE_ID=? WHERE APPLICATION_ID=? AND OFFICE_CODE=? AND USER_ID=?");
				statement6 = connection.prepareStatement(query.toString());
				statement6.setString(1, "5");
				statement6.setString(2, ia.getApplicationId());
				statement6.setString(3, ia.getReportFrom());	
				statement6.setString(4, fromuser);
				statement6.executeUpdate();
				statement6.close();
				
				
				StringBuffer query1 = new StringBuffer("SELECT CURRENT_STATUS FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=? ");
				statement9 = connection.prepareStatement(query1.toString());
				statement9.setString(1, ia.getApplicationId());
				rs1 = statement9.executeQuery();
				String status=null;
				if(rs1.next()){
					status=rs1.getString(1);
				}
				rs1.close();
				statement9.close();
				if(!status.equals(8)){
			    String touser="";
				query = new StringBuffer("SELECT USER_ID FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
				statement7 = connection.prepareStatement(query.toString());
				statement7.setString(1, ia.getApplicationId());
				statement7.setString(2, ia.getMyOfficeCode());		
				rs1 = statement7.executeQuery();		
				if(rs1.next()){
					touser=rs1.getString(1);
				}
				rs1.close();
				statement7.close();
				/// t_applicationstagedetails- current status 8  no 
				query = new StringBuffer("UPDATE T_PC_USER_LEVEL_STATUS SET STAGE_ID=? WHERE APPLICATION_ID=? AND OFFICE_CODE=? AND USER_ID=? AND STAGE_ID=3");
				statement8 = connection.prepareStatement(query.toString());
				statement8.setString(1, "2");
				statement8.setString(2, ia.getApplicationId());
				statement8.setString(3, ia.getMyOfficeCode());	
				statement8.setString(4, touser);
				row1 = statement8.executeUpdate();					
				statement8.close();
			}
			}
		}
		rs.close();
		statement1.close();		
		return row1;
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

	public List<KVPair<String, String>> getOfficeNameList() {
		return officeNameList;
	}

	public void setOfficeNameList(List<KVPair<String, String>> officeNameList) {
		this.officeNameList = officeNameList;
	}

	
	
	
	
}
