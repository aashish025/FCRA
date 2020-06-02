package dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.owasp.esapi.ESAPI;

import utilities.KVPair;
import models.reports.ReportType;
import dao.BaseDao;

public class ReportTypeDao extends BaseDao<ReportType, String, String> {
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String reportName;
	private Integer reportId;
	private String assignedOffice;

	public ReportTypeDao(Connection connection) {
		super(connection);
	}

	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT REPORT_ID,REPORT_NAME ,CREATED_DATE FROM TM_REPORTS WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  reportTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			reportTypeList.add(temp);			
		}
		return reportTypeList;
	}
	public List<ReportType> getMasterreport() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("SELECT REPORT_ID,REPORT_NAME,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_REPORTS WHERE RECORD_STATUS=0");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("
				+ query + ")");
		PreparedStatement statement = connection.prepareStatement(countQuery
				.toString());
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
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
			statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
			statement.setInt(2, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();
	
		
		List<ReportType> reporttypeList = new ArrayList<ReportType>();
		while (rs.next()) {
			reporttypeList.add(new ReportType(rs.getInt(1), rs.getString(2), rs
					.getString(3)));
		}
		return reporttypeList;
	}

	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("reportId")) {
				orderbyClause.append(" ORDER BY REPORT_ID");
			}else if(sortColumn.equals("reportName")) {
				orderbyClause.append(" ORDER BY REPORT_NAME");
			}else if(sortColumn.equals("createdDate")) {
				orderbyClause.append(" ORDER BY CREATED_DATE");
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

	public Integer editRecord(ReportType object) throws Exception {
		PreparedStatement statement1 = null;
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_REPORTS SET REPORT_NAME=?, LAST_MODIFIED_BY=?"
						+ ", LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE REPORT_ID=?");

		statement1 = connection.prepareStatement(query.toString());
		statement1.setString(1, object.getReportName());
		statement1.setString(2, object.getLastModifiedBy());
		statement1.setString(3, object.getLastModifiedIp());
		statement1.setInt(4, object.getReportId());
		int rows = statement1.executeUpdate();
		statement1.close();
		return rows;
	}

	private Integer generateReportId() throws Exception {
		Integer val=null;
		StringBuffer query = new StringBuffer("SELECT NVL(MAX(REPORT_ID), 0)+1 FROM TM_REPORTS");
		PreparedStatement statement = connection.prepareStatement(query.toString());		
		ResultSet rs = statement.executeQuery();
		if(rs.next())
			 val=rs.getInt(1);
		return val;
	}
	@Override
	public Integer insertRecord(ReportType object) throws Exception {
		PreparedStatement statement1 = null;
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		reportId=generateReportId();	
	    setReportId(reportId);
		StringBuffer query = new StringBuffer(
				"INSERT INTO TM_REPORTS(REPORT_ID,REPORT_NAME,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
						+ " VALUES(?,?,0,0,?,?,sysdate,?,?,sysdate)");
		statement1 = connection.prepareStatement(query.toString());
		statement1.setInt(1, reportId);
		statement1.setString(2, object.getReportName());
		statement1.setString(3, object.getCreatedBy());
		statement1.setString(4, object.getCreatedIp());
		statement1.setString(5, object.getLastModifiedBy());
		statement1.setString(6, object.getLastModifiedIp());
		int rows = statement1.executeUpdate();
		statement1.close();
		return rows;
	}
	
	
	public List<KVPair<String, String>> getAvailableOffice() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_ID, OFFICE_TYPE FROM TM_OFFICETYPE WHERE RECORD_STATUS=0 AND OFFICE_ID NOT IN (SELECT OFFICE_ID FROM TM_REPORTS_OFFICETYPE WHERE REPORT_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setInt(1, reportId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  availableOfficeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			availableOfficeList.add(temp);			
		}
		return availableOfficeList;
	}
      public List<KVPair<String, String>> getAssignedOffice() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_ID,(SELECT OFFICE_TYPE FROM TM_OFFICETYPE WHERE OFFICE_ID=TM_REPORTS_OFFICETYPE.OFFICE_ID) FROM TM_REPORTS_OFFICETYPE WHERE REPORT_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setInt(1, reportId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  assignedOfficeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assignedOfficeList.add(temp);			
		}
		return assignedOfficeList;
	}
    
      public String saveOffice(ReportType object) throws Exception {
    		PreparedStatement statement=null;
    		if(connection == null) {
    			throw new Exception("Invalid connection");
    		}	
    		connection.setAutoCommit(false);
    		
    		if((assignedOffice.equals("null"))){
    			StringBuffer query=new StringBuffer("DELETE FROM TM_REPORTS_OFFICETYPE WHERE REPORT_ID=?");
    			statement = connection.prepareStatement(query.toString());
    			statement.setInt(1, reportId);
    			statement.executeUpdate();
    			statement.close();
    		}else{
    			StringTokenizer ids = new StringTokenizer(assignedOffice, ",");
    			List<Short> updatedAssigendList = new ArrayList<Short>();
    			int i;
    			while (ids.hasMoreTokens()) {
    				Short menuId = Short.parseShort(ids.nextToken().trim());
    				updatedAssigendList.add(menuId);
    			}	
    			StringBuffer query=new StringBuffer("DELETE FROM TM_REPORTS_OFFICETYPE WHERE REPORT_ID=?");
    			statement = connection.prepareStatement(query.toString());
    			statement.setInt(1, reportId);
    			statement.executeUpdate();
    			statement.close();
    			StringBuffer query1 = new StringBuffer("INSERT INTO TM_REPORTS_OFFICETYPE(REPORT_ID,OFFICE_ID,RECORD_STATUS,CREATED_BY,"
    					+ "CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
    					+ " VALUES(?,?,0,?,?,sysdate,?,?,sysdate)");
    			int count=updatedAssigendList.size();
    			for(i=0;i<count;i++){
    				 statement = connection.prepareStatement(query1.toString());		 
    				statement.setInt(1, reportId);
    				 statement.setShort(2,updatedAssigendList.get(i));
    				 statement.setString(3, object.getCreatedBy());
    				 statement.setString(4, object.getCreatedIp());
    				 statement.setString(5,object.getLastModifiedBy());
    				 statement.setString(6, object.getLastModifiedIp());
    				 statement.executeUpdate();
    			}	
    			connection.commit();
    		}		 
    		return "success";			
    		
    	}
  	
	
	
	public String deletetable(ReportType object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query=new StringBuffer("UPDATE TM_REPORTS SET RECORD_STATUS=1 WHERE REPORT_ID=?");
	    statement = connection.prepareStatement(query.toString());
	    statement.setInt(1, reportId);
		statement.executeUpdate();
		statement.close();	
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query1=new StringBuffer("UPDATE TM_REPORTS_OFFICETYPE SET RECORD_STATUS=1 WHERE REPORT_ID=?");
		statement1 = connection.prepareStatement(query1.toString());
		statement1.setInt(1, reportId);
		statement1.executeUpdate();
		statement1.close();	
	return "success";		
	}


	@Override
	public Integer removeRecord(ReportType object) throws Exception {
		PreparedStatement statement1 = null;
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		// Updating RECORD_STATUS in TM_ROOM_TYPE
		StringBuffer query = new StringBuffer(
				"UPDATE TM_REPORTS SET RECORD_STATUS=1 WHERE REPORT_ID=?");
		statement1 = connection.prepareStatement(query.toString());
		statement1.setInt(1, object.getReportId());
		int rows = statement1.executeUpdate();
		statement1.close();

		return rows;
	}

	@Override
	public List<ReportType> getAll() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}

		StringBuffer query = new StringBuffer(
				"SELECT REPORT_ID, REPORT_NAME, DISPLAY_ORDER, RECORD_STATUS, "
						+ "CREATED_BY, CREATED_IP, to_char(CREATED_DATE,'dd-mm-yyyy'), LAST_MODIFIED_BY, LAST_MODIFIED_IP, to_char(LAST_MODIFIED_DATE,'dd-mm-yyyy'), ROWID "
						+ "FROM TM_REPORTS");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		ResultSet rs = statement.executeQuery();
		List<ReportType> reportTypeList = new ArrayList<ReportType>();
		while (rs.next()) {
			ReportType temp = new ReportType();
			int i = 1;
			temp.setReportId(rs.getInt(i++));
			temp.setReportName(rs.getString(i++));
			temp.setDisplayOrder(rs.getInt(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setLastModifiedBy(rs.getString(i++));
			temp.setLastModifiedIp(rs.getString(i++));
			temp.setLastModifiedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));

			reportTypeList.add(temp);
		}
		return reportTypeList;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<ReportType> list) {
		int i = 0;

		List<KVPair<String, String>> reportTypeKVList = new ArrayList<KVPair<String, String>>();
		for (i = 0; i < list.size(); i++) {
			ReportType tempPpty = list.get(i);
			KVPair<String, String> temp = new KVPair<String, String>(tempPpty
					.getReportId().toString(), tempPpty.getReportName());
			reportTypeKVList.add(temp);
		}
		return reportTypeKVList;
	}

	
	public String checkNull(String val){
		if(val==null||val=="")
			val="-";
		return val;
	}
	
	
	
	
	
	public List<ReportType> getAliveRecords(String officeTypeId) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		if(ESAPI.validator().isValidInput("OfficeType", officeTypeId, "Num", 2, false) == false){
			throw new Exception("Office Type Id - Only Number allowed (2 characters max).");
		}

		StringBuffer query = new StringBuffer(
				"SELECT A.REPORT_ID, REPORT_NAME, DISPLAY_ORDER, A.RECORD_STATUS, A.CREATED_BY, A.CREATED_IP, to_char(A.CREATED_DATE,'dd-mm-yyyy'),"
				+ " A.LAST_MODIFIED_BY, A.LAST_MODIFIED_IP, to_char(A.LAST_MODIFIED_DATE,'dd-mm-yyyy'), A.ROWID "
				+ " FROM TM_REPORTS A, TM_REPORTS_OFFICETYPE B "
				+ " WHERE B.OFFICE_ID=? AND A.REPORT_ID=B.REPORT_ID AND A.RECORD_STATUS=0 AND B.RECORD_STATUS=0");
		
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officeTypeId);
		ResultSet rs = statement.executeQuery();
		List<ReportType> reportTypeList = new ArrayList<ReportType>();
		while (rs.next()) {
			ReportType temp = new ReportType();
			int i = 1;
			temp.setReportId(rs.getInt(i++));
			temp.setReportName(rs.getString(i++));
			temp.setDisplayOrder(rs.getInt(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setLastModifiedBy(rs.getString(i++));
			temp.setLastModifiedIp(rs.getString(i++));
			temp.setLastModifiedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));

			reportTypeList.add(temp);
		}
		return reportTypeList;
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

	public String getReportName() {
		return reportName;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public void setAssignedOffice(String assignedOffice) {
		this.assignedOffice = assignedOffice;
	}

	
	
}
