package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDao;
import models.master.OfficeType;
import utilities.KVPair;
import utilities.lists.List3;

public class OfficeTypeDao extends BaseDao<OfficeType, String, String> {

    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String officetype;
	private String officeid;
	private String officeName;
	

public OfficeTypeDao(Connection connection) {
		super(connection);
	}
	
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_ID,OFFICE_TYPE FROM TM_OFFICETYPE");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  genderTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			genderTypeList.add(temp);			
		}
		return genderTypeList;
	}
	
	
	public List<KVPair<String, String>> getKVListOfficeId(String myOfficeId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_ID,OFFICE_TYPE FROM TM_OFFICETYPE WHERE office_id=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1,myOfficeId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  genderTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			genderTypeList.add(temp);			
		}
		return genderTypeList;
	}
	
	public List<OfficeType> getMasteroffice() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_ID,OFFICE_TYPE,OFFICE_NAME,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_OFFICETYPE WHERE RECORD_STATUS=0");
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
		
		List<OfficeType> gendertypeList = new ArrayList<OfficeType>();
		while(rs.next()) {	
			gendertypeList.add(new OfficeType(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));			
		}
		return gendertypeList;
	}

	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("officeId")) {
				orderbyClause.append(" ORDER BY OFFICE_ID");
			}else if(sortColumn.equals("officeType")) {
				orderbyClause.append(" ORDER BY OFFICE_TYPE");
			}else if(sortColumn.equals("officeName")) {
			    orderbyClause.append("ORDER BY OFFICE_NAME");
			}else if(sortColumn.equals("enteredOn")) {
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
	

	public Integer removeRecord(OfficeType object) throws Exception {
		
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  RECORD_STATUS in TM_ROOM_TYPE			
			StringBuffer query = new StringBuffer("UPDATE TM_OFFICETYPE SET RECORD_STATUS=1 WHERE OFFICE_ID=?");
				statement1 = connection.prepareStatement(query.toString());	
				statement1.setString(1, object.getOfficeId());
				
				int rows=statement1.executeUpdate();				
				statement1.close();
                return rows;
				
		}
	public Integer editRecord(OfficeType object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_OFFICETYPE SET OFFICE_TYPE=?, OFFICE_NAME=?, LAST_MODIFIED_BY=?"
					+ ", LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE OFFICE_ID=?");
		
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1,object.getOfficeType());
				statement1.setString(2,object.getOfficeName());	
				statement1.setString(3,object.getLastModifiedBy());
				statement1.setString(4, object.getLastModifiedIp());
				statement1.setString(5, object.getOfficeId());
			
				int rows=statement1.executeUpdate();				
				statement1.close();
				return rows;			
	}	
	

		@Override
	public Integer insertRecord(OfficeType object) throws Exception 
	{
		PreparedStatement statement1=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}	
	

				StringBuffer query = new StringBuffer("INSERT INTO TM_OFFICETYPE(OFFICE_ID,OFFICE_TYPE,OFFICE_NAME,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
						+ " VALUES((SELECT NVL(MAX(OFFICE_ID), 0)+1 FROM TM_OFFICETYPE),?, ?, 0, 0, ?, ?,sysdate, ?, ?,sysdate)");
				statement1 = connection.prepareStatement(query.toString());	
				statement1.setString(1,object.getOfficeType());
				statement1.setString(2,object.getOfficeName());
		      	statement1.setString(3, object.getCreatedBy());
				statement1.setString(4, object.getCreatedIp());	
				statement1.setString(5,object.getLastModifiedBy());
				statement1.setString(6, object.getLastModifiedIp());
				int rows=statement1.executeUpdate();
				statement1.close();
				return rows;
				
	}
		@Override
		public List<OfficeType> getAll() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<KVPair<String, String>> getKVList(List<OfficeType> list) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<List3> getgendertype(String genderType) {
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
		public String getOfficetype() {
			return officetype;
		}

		public void setOfficetype(String officetype) {
			this.officetype = officetype;
		}

		public String getOfficeid() {
			return officeid;
		}

		public void setOfficeid(String officeid) {
			this.officeid = officeid;
		}

		public String getOfficeName() {
			return officeName;
		}

		public void setOfficeName(String officeName) {
			this.officeName = officeName;
		}

	


	
	

		
		
		
	

}
