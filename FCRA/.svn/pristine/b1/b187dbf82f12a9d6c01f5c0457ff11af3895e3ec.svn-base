package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utilities.KVPair;
import models.master.RedFlagCategory;
import dao.BaseDao;

public class RedFlagCategoryDao  extends BaseDao<RedFlagCategory, String, String>  {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	
	public RedFlagCategoryDao(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	public List<RedFlagCategory> getList() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
	
		 StringBuffer query = new StringBuffer("SELECT CATEGORY_CODE, CATEGORY_DESC, "
		 		                               + "to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_RED_FLAG_CATEGORY WHERE RECORD_STATUS = 0");
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
		 List<RedFlagCategory> redflagList = new ArrayList<RedFlagCategory>();
		 while(rs.next()) {	
		
			 redflagList.add(new RedFlagCategory(rs.getInt(1),rs.getString(2),rs.getString(3)));	
		 }
		 return redflagList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("categoryCode")) {
						orderbyClause.append(" ORDER BY CATEGORY_CODE");
					}else if(sortColumn.equals("categoryName")) {
						orderbyClause.append(" ORDER BY CATEGORY_DESC");
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
	public Integer insertRecord(RedFlagCategory object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	

			
			StringBuffer query = new StringBuffer("INSERT INTO TM_RED_FLAG_CATEGORY(CATEGORY_CODE,CATEGORY_DESC,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE)"
					+ "VALUES((SELECT NVL(MAX(CATEGORY_CODE) , 0)+1 FROM TM_RED_FLAG_CATEGORY WHERE CATEGORY_CODE!='99'),?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());	
		
			statement1.setString(1,object.getCategoryName());
			statement1.setString(2, object.getCreatedBy());
			statement1.setString(3, object.getCreatedIp());	
			statement1.setString(4,object.getLastModifiedBy());
			statement1.setString(5, object.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;
}

	

	@Override
	public Integer removeRecord(RedFlagCategory object) throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		StringBuffer query = new StringBuffer("UPDATE TM_RED_FLAG_CATEGORY SET RECORD_STATUS=1 WHERE CATEGORY_CODE=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setInt(i++, object.getCategoryCode());
		int rows = statement.executeUpdate();
		return rows;
	
	}
	public Integer editRecord(RedFlagCategory object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		StringBuffer query = new StringBuffer("UPDATE TM_RED_FLAG_CATEGORY SET  CATEGORY_DESC=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE CATEGORY_CODE=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;		
		statement.setString(i++, object.getCategoryName());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		statement.setInt(i++, object.getCategoryCode());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public List<RedFlagCategory> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<RedFlagCategory> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 public List<KVPair<String, String>> getKVList() throws Exception{
			if(connection == null) {
				throw new Exception("Invalid connection");
			}				
			StringBuffer query = new StringBuffer("select CATEGORY_CODE,CATEGORY_DESC from TM_RED_FLAG_CATEGORY WHERE RECORD_STATUS=0");
			PreparedStatement statement = connection.prepareStatement(query.toString());				
			ResultSet rs = statement.executeQuery();
			List<KVPair<String, String>>  categoryList = new ArrayList<KVPair<String, String>>();
			while(rs.next()) {			
				KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
				categoryList.add(temp);			
			}
			return categoryList;
		}
	public String getRecordsPerPage() {
		return recordsPerPage;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public String getTotalRecords() {
		return totalRecords;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

}
