package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.SubStage;
import utilities.KVPair;
import dao.BaseDao;

public class SubStageOfficeTypeDao extends BaseDao<SubStage, String, String> {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String sessionId;
	public SubStageOfficeTypeDao(Connection connection) {
		super(connection);
	}

	@Override
	public Integer insertRecord(SubStage object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		Boolean existFlag=false;
		StringBuffer query3=new StringBuffer("SELECT * FROM TM_SUBSTAGE_OFFICETYPE WHERE PROPOSAL_TYPE_ID=? AND SUB_STAGE_ID=? AND OFFICE_ID=?");
		PreparedStatement	stmt = connection.prepareStatement(query3.toString());
		stmt.setString(1, object.getProposalTypeId());
		stmt.setString(2, object.getSubStageId());
		stmt.setString(3, object.getOfficeId());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()==true){
				existFlag=true;
			}
			stmt.close();
		if(existFlag)
		{
			return null;
		}
		else
		{
		StringBuffer query = new StringBuffer("INSERT INTO TM_SUBSTAGE_OFFICETYPE(OFFICE_ID, SUB_STAGE_ID, PROPOSAL_TYPE_ID, RECORD_STATUS, CREATED_BY, CREATED_IP, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES(?, ?, ?, 0, ?, ?, sysdate, ?, ?, sysdate) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getOfficeId());
		statement.setString(i++, object.getSubStageId());
		statement.setString(i++, object.getProposalTypeId());
		statement.setString(i++, object.getCreatedBy());
		statement.setString(i++, object.getCreatedIp());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}
	}

	@Override
	public Integer removeRecord(SubStage object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("UPDATE TM_SUBSTAGE_OFFICETYPE SET RECORD_STATUS=1 WHERE ROWID=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getRowId());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public Integer editRecord(SubStage object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		
		Boolean existFlag=false;
		StringBuffer query3=new StringBuffer("SELECT * FROM TM_SUBSTAGE_OFFICETYPE WHERE PROPOSAL_TYPE_ID=? AND SUB_STAGE_ID=? AND OFFICE_ID=?");
		PreparedStatement	stmt = connection.prepareStatement(query3.toString());
		stmt.setString(1, object.getProposalTypeId());
		stmt.setString(2, object.getSubStageId());
		stmt.setString(3, object.getOfficeId());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()==true){
				existFlag=true;
			}
			stmt.close();
		if(existFlag)
		{
			return null;
		}
		else
		{
		StringBuffer query = new StringBuffer("UPDATE TM_SUBSTAGE_OFFICETYPE SET  PROPOSAL_TYPE_ID=?, SUB_STAGE_ID=?, OFFICE_ID=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE ROWID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;		
		statement.setString(i++, object.getProposalTypeId());
		statement.setString(i++, object.getSubStageId());
		statement.setString(i++, object.getOfficeId());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		statement.setString(i++, object.getRowId());
		int rows = statement.executeUpdate();
		return rows;
		}
	}
	
	public List<SubStage> getMainSubStage() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 StringBuffer query = new StringBuffer("SELECT a.PROPOSAL_TYPE_ID, a.SUB_STAGE_ID, a.OFFICE_ID,  b.PROPOSAL_DESC, c.SUB_STAGE_DESC, d.OFFICE_TYPE, a.ROWID RID FROM TM_SUBSTAGE_OFFICETYPE a, TM_PROJECT_PROPOSAL_TYPE b, TM_APPLICATION_SUB_STAGE c, TM_OFFICETYPE d WHERE a.RECORD_STATUS = 0 AND a.PROPOSAL_TYPE_ID=b.PROPOSAL_ID AND a.SUB_STAGE_ID=c.SUB_STAGE_ID AND a.OFFICE_ID=d.OFFICE_ID");
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
		 List<SubStage> submenuList = new ArrayList<SubStage>();
		 while(rs.next()) {	
					 submenuList.add(new SubStage(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));	
		 }
		 return submenuList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("proposalDesc")) {
						orderbyClause.append(" ORDER BY b.PROPOSAL_DESC");
					}else if(sortColumn.equals("subStageDesc")) {
						orderbyClause.append(" ORDER BY c.SUB_STAGE_DESC");
					}else if(sortColumn.equals("officeType")) {
						orderbyClause.append(" ORDER BY d.OFFICE_TYPE");
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
	public List<SubStage> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<SubStage> list) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
