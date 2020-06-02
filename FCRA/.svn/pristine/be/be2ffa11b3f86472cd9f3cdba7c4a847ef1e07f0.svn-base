package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dao.BaseDao;
import models.master.SubstageDocument;
import utilities.KVPair;

public class SubstageDocumentDao extends BaseDao<SubstageDocument, String, String>{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String proposalDesc;
   	private String substageDesc;
	
	
	public SubstageDocumentDao(Connection connection) {
		super(connection);
	}
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT PROPOSAL_TYPE_ID,SUB_STAGE_ID, DOC_TYPE_ID FROM TM_SUBSTAGE_DOCUMENTS");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  processOrderTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			processOrderTypeList.add(temp);			
		}
		return processOrderTypeList;
	}
	public List<SubstageDocument> getsubtable() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer(" SELECT a.SUB_STAGE_ID,a.SUB_STAGE_DESC, b.PROPOSAL_ID,b.PROPOSAL_DESC ,c.DOC_TYPE_ID,c.DOC_TYPE_DESC ,d.ROWID RID FROM TM_APPLICATION_SUB_STAGE a,TM_PROJECT_PROPOSAL_TYPE b,TM_PROJECT_DOCUMENT_TYPE c,TM_SUBSTAGE_DOCUMENTS d WHERE a.SUB_STAGE_ID=d.SUB_STAGE_ID AND b.PROPOSAL_ID=d.PROPOSAL_TYPE_ID AND c.DOC_TYPE_ID=d.DOC_TYPE_ID AND d.RECORD_STATUS=0 ");
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
		
		List<SubstageDocument> substagedocumentTypeList = new ArrayList<SubstageDocument>();
		while(rs.next()) {			
			substagedocumentTypeList.add(new SubstageDocument(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
		}
		return substagedocumentTypeList;
	}
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("substageDesc")) {
				orderbyClause.append(" ORDER BY a.SUB_STAGE_DESC");
			}else if(sortColumn.equals("proposalDesc")) {
				orderbyClause.append(" ORDER BY b.PROPOSAL_DESC");
			}else if(sortColumn.equals("documentDesc")) {
				orderbyClause.append(" ORDER BY c.DOC_TYPE_DESC");
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
	public Integer insertRecord(SubstageDocument object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	

			
			StringBuffer query = new StringBuffer("INSERT INTO TM_SUBSTAGE_DOCUMENTS(SUB_STAGE_ID,PROPOSAL_TYPE_ID,DOC_TYPE_ID,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE )VALUES(?,?,?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());	
			statement1.setString(1, object.getSubstageDesc());
			statement1.setString(2, object.getProposalDesc());
			statement1.setString(3,object.getDocumentDesc());
			statement1.setString(4, object.getCreatedBy());
			statement1.setString(5, object.getCreatedIp());
			statement1.setString(6,object.getLastModifiedBy());
			statement1.setString(7, object.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;

	}
	public Integer editRecord(SubstageDocument object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_SUBSTAGE_DOCUMENTS SET SUB_STAGE_ID=?,PROPOSAL_TYPE_ID=?,DOC_TYPE_ID=?,LAST_MODIFIED_BY=?,LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE ROWID=?");
		
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1, object.getSubstageDesc());
				statement1.setString(2, object.getProposalDesc());
				statement1.setString(3,object.getDocumentDesc());
				statement1.setString(4,object.getLastModifiedBy());
				statement1.setString(5,object.getLastModifiedIp());
				statement1.setString(6, object.getRowIdentifier());
				int rows=statement1.executeUpdate();				
				statement1.close();
				return rows;			
	}
	

	
	public Integer deleteRecord(SubstageDocument object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
				
			StringBuffer query = new StringBuffer("UPDATE TM_SUBSTAGE_DOCUMENTS SET RECORD_STATUS=1 WHERE ROWID=?");
				statement1 = connection.prepareStatement(query.toString());					
			    statement1.setString(1, object.getRowIdentifier());
				int rows=statement1.executeUpdate();				
				statement1.close();
				
				return rows;
	}
	
	


	@Override
	public List<SubstageDocument> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<KVPair<String, String>> getKVList(List<SubstageDocument> list) {
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
	public String getProposalDesc() {
		return proposalDesc;
	}
	public void setProposalDesc(String proposalDesc) {
		this.proposalDesc = proposalDesc;
	}
	public String getSubstageDesc() {
		return substageDesc;
	}
	public void setSubstageDesc(String substageDesc) {
		this.substageDesc = substageDesc;
	}
	@Override
	public Integer removeRecord(SubstageDocument object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
