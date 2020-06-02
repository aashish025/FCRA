package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.Office;
import models.master.OfficeFacilityType;
import models.master.TimeZone;
import utilities.KVPair;
import utilities.lists.List2;
import utilities.lists.List3;
import dao.BaseDao;

public class TimeZoneTypeDao extends BaseDao<TimeZone, String, String> { 
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
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
	@Override
	public Integer insertRecord(TimeZone timeZone) throws Exception {		
		StringBuffer query = new StringBuffer("INSERT INTO TM_TIMEZONE(ZONE_ID, COUNTRY_CODE, ZONE_NAME, RECORD_STATUS, "
				+ "CREATED_BY, CREATED_DATE, CREATED_IP, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES(?, ?, ?, 0, ?, systimestamp, ?, ?, ?, systimestamp) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setInt(i++, timeZone.getZoneId());
		statement.setString(i++, timeZone.getCountryName());
		statement.setString(i++, timeZone.getZoneName());
		statement.setString(i++, timeZone.getCreatedBy());
		statement.setString(i++, timeZone.getCreatedIp());				
		statement.setString(i++, timeZone.getLastModifiedBy());
		statement.setString(i++, timeZone.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}
	@Override
	public Integer removeRecord(TimeZone timeZone)
			throws Exception {
		int rows = 0;
		try {
			PreparedStatement statement = null;
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			StringBuffer query = new StringBuffer(
					"UPDATE TM_TIMEZONE SET RECORD_STATUS = 1 WHERE ZONE_ID = ?");
			statement = connection.prepareStatement(query.toString());
			statement.setInt(1, timeZone.getZoneId());
			rows = statement.executeUpdate();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}
	@Override
	public List<TimeZone> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<KVPair<String, String>> getKVList(List<TimeZone> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT ZONE_ID,ZONE_NAME FROM TM_TIMEZONE");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  timeZoneTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			timeZoneTypeList.add(temp);			
		}
		return timeZoneTypeList;
	}
	
	public TimeZoneTypeDao(Connection connection) {
		super(connection);
	}
	
	public List<TimeZone> getAliveRecords(Office office) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		if(office == null || office.getOfficeCode() == null || office.getOfficeCode().equals("") == true) {
			throw new Exception("Invalid Office");
		}		
		StringBuffer query = new StringBuffer("SELECT A.ZONE_ID, B.COUNTRY_NAME, A.ZONE_NAME, "
				+ "A.RECORD_STATUS, "
				+ "A.CREATED_BY, A.CREATED_IP, "				
				+ "A.CREATED_DATE "				
				+ " FROM TM_TIMEZONE A, TM_COUNTRY B WHERE A.COUNTRY_CODE = B.COUNTRY_CODE AND A.RECORD_STATUS = 0");
		
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");		
		PreparedStatement statement = connection.prepareStatement(countQuery.toString());		
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
			statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
			statement.setInt(2, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();		
		List<TimeZone> timeZoneTypeList = new ArrayList<TimeZone>();
		while(rs.next()) {
			TimeZone temp = new TimeZone();
			int i = 1;
			temp.setZoneId(rs.getShort(i++));
			temp.setCountryName(rs.getString(i++));	
			temp.setZoneName(rs.getString(i++));	
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));			
			temp.setCreatedDate(rs.getDate(i++));				
			timeZoneTypeList.add(temp);
		}
		return timeZoneTypeList;
	}
	
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("zoneId")) {
				orderbyClause.append(" ORDER BY ZONE_ID");
			} else if(sortColumn.equals("countryName")) {
				orderbyClause.append(" ORDER BY COUNTRY_NAME");
			}			
			else if(sortColumn.equals("createdDate")) {
				orderbyClause.append(" ORDER BY CREATED_DATE");
			}
			else if(sortColumn.equals("zoneName")) {
				orderbyClause.append(" ORDER BY ZONE_NAME");
			}
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
		if(orderbyClause != null && orderbyClause.equals("") == false)
			query.append(orderbyClause);		
		StringBuffer queryForPaging = null;
		if(pageNum == null || recordsPerPage == null)
			queryForPaging = query;
		else
			queryForPaging = new StringBuffer("WITH T1 AS ("+query+"), T2 AS (SELECT T1.*, ROWNUM RN FROM T1 WHERE ROWNUM<=?) SELECT * FROM T2 WHERE RN>=?");
		return queryForPaging.toString();
	}
	
	/*public List<List2> getTimeZoneType(String timeZone)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"SELECT COUNTRY_CODE, ZONE_NAME FROM TM_TIMEZONE WHERE ZONE_ID = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		statement.setString(1, timeZone);
		ResultSet rs = statement.executeQuery();
		List<List2> requestedDetails = new ArrayList<List2>();
		while (rs.next()) {			
			requestedDetails.add(new List2(rs.getString(1), rs.getString(2)));
		}
		return requestedDetails;
	}*/
	
	public List<List3> getTimeZoneType(String timeZone)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"SELECT COUNTRY_CODE, ZONE_NAME, ZONE_ID FROM TM_TIMEZONE WHERE ZONE_ID = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		statement.setString(1, timeZone);
		ResultSet rs = statement.executeQuery();
		List<List3> requestedDetails = new ArrayList<List3>();
		while (rs.next()) {			
			requestedDetails.add(new List3(rs.getString(1), rs.getString(2), rs.getString(3)));
		}
		return requestedDetails;
	}
	
	public Integer editRecord(TimeZone timeZone)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_TIMEZONE SET COUNTRY_CODE = ?, ZONE_NAME = ?, LAST_MODIFIED_BY = ?, LAST_MODIFIED_IP = ?, LAST_MODIFIED_DATE = systimestamp WHERE ZONE_ID = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, timeZone.getCountryName());
		statement.setString(i++, timeZone.getZoneName());
		statement.setString(i++, timeZone.getLastModifiedBy());
		statement.setString(i++, timeZone.getLastModifiedIp());
		statement.setInt(i++, timeZone.getZoneId());		
		int rows = statement.executeUpdate();
		return rows;
	}
}
