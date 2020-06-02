package dao.master;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.master.Currency;
import utilities.KVPair;
import dao.BaseDao;

public class CurrencyDao extends BaseDao<Currency, String, String> {
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	
	public CurrencyDao(Connection connection) {
		super(connection);
	}
	
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT CURR_CODE,CURR_NAME,CREATED_DATE FROM TM_CURRENCY");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  currencyTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			currencyTypeList.add(temp);			
		}
		return currencyTypeList;
	}
	public List<Currency> gettable() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT CURR_CODE,CURR_NAME,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_CURRENCY WHERE RECORD_STATUS=0");
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
		
		List<Currency> currencytypeList = new ArrayList<Currency>();
		while(rs.next()) {			
			currencytypeList.add(new Currency(rs.getString(1),rs.getString(2),rs.getString(3)));			
		}
		return currencytypeList;
	}
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("currencyCode")) {
				orderbyClause.append(" ORDER BY CURR_CODE");
			}else if(sortColumn.equals("currencyName")) {
				orderbyClause.append(" ORDER BY CURR_NAME");
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
	public Integer insertRecord(Currency object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	

			
			StringBuffer query = new StringBuffer("INSERT INTO TM_CURRENCY(CURR_CODE,CURR_NAME,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE)VALUES(?,?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());				
			statement1.setString(1, object.getCurrencyCode());
			statement1.setString(2,object.getCurrencyName());
			statement1.setString(3, object.getCreatedBy());
			statement1.setString(4, object.getCreatedIp());	
			statement1.setString(5,object.getLastModifiedBy());
			statement1.setString(6, object.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;

	}
	public Integer editRecord(Currency object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_CURRENCY SET CURR_NAME=?, "
					+ "LAST_MODIFIED_BY=?,LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE CURR_CODE=?");
		
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1,object.getCurrencyName());
				statement1.setString(2,object.getLastModifiedBy());
				statement1.setString(3, object.getLastModifiedIp());
				statement1.setString(4, object.getCurrencyCode());
				int rows=statement1.executeUpdate();				
				statement1.close();
				return rows;			
	}	
	@Override
	public Integer removeRecord(Currency object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  RECORD_STATUS in TM_Designation	
			StringBuffer query = new StringBuffer("UPDATE TM_CURRENCY SET RECORD_STATUS=1 WHERE CURR_CODE=?");
				statement1 = connection.prepareStatement(query.toString());					
				statement1.setString(1, object.getCurrencyCode());
				int rows=statement1.executeUpdate();				
				statement1.close();
				
				return rows;
	
	}

	@Override
	public List<Currency> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<Currency> list) {
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
