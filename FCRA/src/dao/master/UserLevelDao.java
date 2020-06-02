package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utilities.KVPair;
import models.master.UserLevel;
import dao.BaseDao;

public class UserLevelDao extends BaseDao<UserLevel, String, String> {
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String userlavel;
	private String userlevelid;

	public UserLevelDao(Connection connection) {
		super(connection);
	}
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT USER_LEVEL_ID,USER_LEVEL_NAME,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_USER_LEVEL WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  userList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			userList.add(temp);			
		}
		return userList;
	}

	public List<UserLevel> getMasteracquisition() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT USER_LEVEL_ID,USER_LEVEL_NAME,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_USER_LEVEL WHERE RECORD_STATUS=0");
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
		
		List<UserLevel> userlavelList = new ArrayList<UserLevel>();
		while(rs.next()) {			
			userlavelList.add(new UserLevel(rs.getInt(1),rs.getString(2),rs.getString(3)));			
		}
		return userlavelList;
	}
	
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("userlevelid")) {
				orderbyClause.append(" ORDER BY USER_LEVEL_ID");
			}else if(sortColumn.equals("userlevelName")) {
				orderbyClause.append(" ORDER BY USER_LEVEL_NAME");
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

		public Integer editRecord(UserLevel object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_USER_LEVEL SET USER_LEVEL_NAME=?, LAST_MODIFIED_BY=?"
					+ ", LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE USER_LEVEL_ID=?");
		
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1,object.getUserlevelName());					
				statement1.setString(2,object.getLastModifiedBy());
				statement1.setString(3, object.getLastModifiedIp());
				statement1.setInt(4, object.getUserlevelid());
				int rows=statement1.executeUpdate();				
				statement1.close();
				return rows;			
	}	
		
	@Override
	public Integer insertRecord(UserLevel object) throws Exception {
		PreparedStatement statement1=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}	
	
				
				StringBuffer query = new StringBuffer("INSERT INTO TM_USER_LEVEL(USER_LEVEL_ID,USER_LEVEL_NAME,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
						+ " VALUES((SELECT NVL(MAX(USER_LEVEL_ID), 0)+1 FROM TM_USER_LEVEL),?,0,0,?,?,sysdate,?,?,sysdate)");
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1,object.getUserlevelName());
				statement1.setString(2, object.getCreatedBy());
				statement1.setString(3, object.getCreatedIp());	
				statement1.setString(4,object.getLastModifiedBy());
				statement1.setString(5, object.getLastModifiedIp());
				int rows=statement1.executeUpdate();
				statement1.close();
				return rows;
	}

	@Override
	public Integer removeRecord(UserLevel object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  RECORD_STATUS in TM_ROOM_TYPE			
			StringBuffer query = new StringBuffer("UPDATE TM_USER_LEVEL SET RECORD_STATUS=1 WHERE USER_LEVEL_ID=?");
				statement1 = connection.prepareStatement(query.toString());					
				statement1.setInt(1, object.getUserlevelid());
				int rows=statement1.executeUpdate();				
				statement1.close();
				
				return rows;
	}

	@Override
	public List<UserLevel> getAll() throws Exception {		
		
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<UserLevel> list) {

		return null;
	}


	public List<UserLevel> getAliveRecords() throws Exception {
	
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

		public String getAcquisitiontype() {
			return userlavel;
		}

		public void setAcquisitiontype(String userlavel) {
			this.userlavel = userlavel;
		}

		public String getAcquisitionid() {
			return userlevelid;
		}

		public void setAcquisitionid(String userlevelid) {
			this.userlevelid = userlevelid;
		}
}
