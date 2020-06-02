package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.master.CommitteeRelation;
import utilities.KVPair;
import dao.BaseDao;

public class CommitteeRelationDao extends BaseDao<CommitteeRelation, String, String> {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String sessionId;

	public CommitteeRelationDao(Connection connection) {
		super(connection);
	}

	public Integer insertRecord(CommitteeRelation object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("INSERT INTO TM_COMMITTEE_RELATION(RELATION_CODE, RELATION_NAME, DISPLAY_ORDER, RECORD_STATUS, "
				+ "CREATED_BY, CREATED_IP, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES((SELECT NVL(MAX(RELATION_CODE) , 0)+1 FROM TM_COMMITTEE_RELATION WHERE RELATION_CODE!='99'), ?, 0, 0, ?, ?, sysdate, ?, ?, sysdate) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getRelationName());
		statement.setString(i++, object.getCreatedBy());
		statement.setString(i++, object.getCreatedIp());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}

	public Integer removeRecord(CommitteeRelation object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("UPDATE TM_COMMITTEE_RELATION SET RECORD_STATUS=1 WHERE RELATION_CODE=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setInt(i++, object.getRelationCode());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public Integer editRecord(CommitteeRelation object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		StringBuffer query = new StringBuffer("UPDATE TM_COMMITTEE_RELATION SET  RELATION_NAME=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE RELATION_CODE=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;		
		statement.setString(i++, object.getRelationName());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		statement.setInt(i++, object.getRelationCode());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public List<CommitteeRelation> getMainComRelation() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 StringBuffer query = new StringBuffer("SELECT RELATION_CODE, RELATION_NAME, to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_COMMITTEE_RELATION WHERE RECORD_STATUS = 0");
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
		 else
		 {
		 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
		 statement.setInt(2, (pageRequested-1) * pageSize + 1);
		 }
		 rs = statement.executeQuery();
		 List<CommitteeRelation> userstatusList = new ArrayList<CommitteeRelation>();
		 while(rs.next()) {	
					 userstatusList.add(new CommitteeRelation(rs.getInt(1),rs.getString(2),rs.getString(3)));	
		 }
		 return userstatusList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("relationCode")) {
						orderbyClause.append(" ORDER BY RELATION_CODE");
					}else if(sortColumn.equals("relationName")) {
						orderbyClause.append(" ORDER BY RELATION_NAME");
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

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public List<CommitteeRelation> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<CommitteeRelation> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	}

