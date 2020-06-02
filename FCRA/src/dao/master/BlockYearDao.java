package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.BlockYear;
import utilities.KVPair;
import dao.BaseDao;

public class BlockYearDao extends BaseDao<BlockYear, String, String> {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String sessionId;
	public BlockYearDao(Connection connection) {
		super(connection);
	}

	public Integer insertRecord(BlockYear object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		char s=object.getStatus().charAt(0); //(object.getStatus().substring(0));
	if(s!='O')	{
		StringBuffer query = new StringBuffer("INSERT INTO TM_BLOCK_YEAR(BLKYR, STATUS, DISPLAY_ORDER, RECORD_STATUS, CREATED_BY, CREATED_IP, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES(?, ?, 0, 0, ?, ?, sysdate, ?, ?, sysdate) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getBlkyr());
		statement.setString(i++, object.getStatus());
		statement.setString(i++, object.getCreatedBy());
		statement.setString(i++, object.getCreatedIp());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;	
	}
	else
	{
		Boolean existFlag=false;
		StringBuffer query3=new StringBuffer("SELECT * FROM TM_BLOCK_YEAR WHERE STATUS='O' AND RECORD_STATUS!=1");
		PreparedStatement	stmt = connection.prepareStatement(query3.toString());
				ResultSet rs = stmt.executeQuery();
			if(rs.next()==true ){
		
				existFlag=true;
			}
			stmt.close();
		if(existFlag)
		{
			return null;
		}
		else 
		{
		StringBuffer query = new StringBuffer("INSERT INTO TM_BLOCK_YEAR(BLKYR, STATUS, DISPLAY_ORDER, RECORD_STATUS, CREATED_BY, CREATED_IP, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES(?, ?, 0, 0, ?, ?, sysdate, ?, ?, sysdate) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getBlkyr());
		statement.setString(i++, object.getStatus());
		statement.setString(i++, object.getCreatedBy());
		statement.setString(i++, object.getCreatedIp());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}
	}
	}

	public Integer removeRecord(BlockYear object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("UPDATE TM_BLOCK_YEAR SET RECORD_STATUS=1 WHERE BLKYR=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getBlkyr());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public Integer editRecord(BlockYear object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		char s=object.getStatus().charAt(0); //(object.getStatus().substring(0));
		if(s!='O')	{
		StringBuffer query = new StringBuffer("UPDATE TM_BLOCK_YEAR SET STATUS=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE BLKYR=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;		
		statement.setString(i++, object.getStatus());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		statement.setString(i++, object.getBlkyr());
		int rows = statement.executeUpdate();
		return rows;
		}
		else
		{
			Boolean existFlag=false;
			StringBuffer query3=new StringBuffer("SELECT * FROM TM_BLOCK_YEAR WHERE STATUS='O' AND RECORD_STATUS!=1");
			PreparedStatement	stmt = connection.prepareStatement(query3.toString());
					ResultSet rs = stmt.executeQuery();
				if(rs.next()==true ){
			
					existFlag=true;
				}
				stmt.close();
			if(existFlag)
			{
				return null;
			}
			else 
			{
				StringBuffer query = new StringBuffer("UPDATE TM_BLOCK_YEAR SET STATUS=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE BLKYR=?");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				int i=1;		
				statement.setString(i++, object.getStatus());
				statement.setString(i++, object.getLastModifiedBy());
				statement.setString(i++, object.getLastModifiedIp());
				statement.setString(i++, object.getBlkyr());
				int rows = statement.executeUpdate();
				return rows;
			}}
	}
	
	public List<BlockYear> getMainBlockYear() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 StringBuffer query = new StringBuffer("SELECT BLKYR, STATUS FROM TM_BLOCK_YEAR WHERE RECORD_STATUS = 0");
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
		 List<BlockYear> districtList = new ArrayList<BlockYear>();
		 while(rs.next()) {	
					 districtList.add(new BlockYear(rs.getString(1),rs.getString(2)));	
		 }
		 return districtList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("blkyr")) {
						orderbyClause.append(" ORDER BY BLKYR");
					}else if(sortColumn.equals("status")) {
						orderbyClause.append(" ORDER BY STATUS");
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

	public List<BlockYear> getAliveRecords() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		StringBuffer query = new StringBuffer("SELECT BLKYR, STATUS, DISPLAY_ORDER, RECORD_STATUS, "
				+ "CREATED_BY, CREATED_IP, LAST_MODIFIED_BY, LAST_MODIFIED_IP, ROWID "
				+ "FROM TM_BLOCK_YEAR WHERE RECORD_STATUS=0 ORDER BY BLKYR");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		List<BlockYear> blockYearList = new ArrayList<BlockYear>();
		while(rs.next()) {
			BlockYear temp = new BlockYear();
			int i=1;
			temp.setBlkyr(rs.getString(i++));
			temp.setStatus(rs.getString(i++));
			temp.setDisplayOrder(rs.getShort(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			//temp.setCreatedDate(rs.getString(i++));
			temp.setLastModifiedBy(rs.getString(i++));
			temp.setLastModifiedIp(rs.getString(i++));
			//temp.setLastModifiedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));
			
			blockYearList.add(temp);
		}
		return blockYearList;
	}
	
	@Override
	public List<BlockYear> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<BlockYear> list) {
		int i=0;
		
		List<KVPair<String, String>> blockYearKVList = new ArrayList<KVPair<String, String>>();
		for(i=0; i<list.size(); i++) {
			BlockYear tempBlockYear = list.get(i);
			KVPair<String, String> temp = new KVPair<String, String>(tempBlockYear.getBlkyr(), tempBlockYear.getBlkyr());
			blockYearKVList.add(temp);
		}
		return blockYearKVList;
	}

}

