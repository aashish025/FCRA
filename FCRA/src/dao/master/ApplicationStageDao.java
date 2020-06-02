package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.master.ApplicationStage;
import utilities.KVPair;
import dao.BaseDao;

public class ApplicationStageDao extends BaseDao<ApplicationStage, String, String>
{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	
	public ApplicationStageDao(Connection connection) {
		super(connection);
	}
	
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT STAGE_ID,STAGE_DESC, CREATED_DATE FROM TM_APPLICATION_STAGE WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  applicationTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			applicationTypeList.add(temp);			
		}
		return applicationTypeList;
	}
	public List<ApplicationStage> gettable() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT STAGE_ID,STAGE_DESC,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_APPLICATION_STAGE WHERE RECORD_STATUS=0");
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
		
		List<ApplicationStage> applicationtypeList = new ArrayList<ApplicationStage>();
		while(rs.next()) {			
			applicationtypeList.add(new ApplicationStage(rs.getString(1),rs.getString(2),rs.getString(3)));			
		}
		return applicationtypeList;
	}
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("stageId")) {
				orderbyClause.append(" ORDER BY STAGE_ID");
			}else if(sortColumn.equals("stageDesc")) {
				orderbyClause.append(" ORDER BY STAGE_DESC");
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

	@Override
	public Integer insertRecord(ApplicationStage object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	

			
			StringBuffer query = new StringBuffer("INSERT INTO TM_APPLICATION_STAGE(STAGE_ID,STAGE_DESC,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE)"
					+ "VALUES((SELECT NVL(MAX(STAGE_ID), 0)+1 FROM TM_APPLICATION_STAGE),?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());				
			statement1.setString(1,object.getStageDesc());
			statement1.setString(2, object.getCreatedBy());
			statement1.setString(3, object.getCreatedIp());	
			statement1.setString(4,object.getLastModifiedBy());
			statement1.setString(5, object.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;
	}
	public Integer editRecord(ApplicationStage object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_APPLICATION_STAGE SET STAGE_DESC=?, LAST_MODIFIED_BY=?,"
					+ "LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE STAGE_ID=?");
		
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1,object.getStageDesc());					
				statement1.setString(2,object.getLastModifiedBy());
				statement1.setString(3, object.getLastModifiedIp());
				statement1.setString(4, object.getStageId());
				int rows=statement1.executeUpdate();				
				statement1.close();
				return rows;			
	}	

	@Override
	public Integer removeRecord(ApplicationStage object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  RECORD_STATUS in TM_Boundary_Direction		
			StringBuffer query = new StringBuffer("UPDATE TM_APPLICATION_STAGE SET RECORD_STATUS=1 WHERE STAGE_ID=?");
				statement1 = connection.prepareStatement(query.toString());					
				statement1.setString(1, object.getStageId());
				int rows=statement1.executeUpdate();				
				statement1.close();
				
				return rows;
	}

	@Override
	public List<ApplicationStage> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<ApplicationStage> list) {
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

	
	

}
