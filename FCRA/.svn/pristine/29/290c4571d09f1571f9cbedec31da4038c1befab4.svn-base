package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utilities.KVPair;
import models.master.Service;
import dao.BaseDao;

public class ServicesDao extends BaseDao<Service, String, String>{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String servicesDesc;
	private String servicesCode;
	
	public ServicesDao(Connection connection) {
		super(connection);
	}


	@Override
	public Integer insertRecord(Service object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	

			
			StringBuffer query = new StringBuffer("INSERT INTO TM_SERVICE(SERVICE_CODE,SERVICE_DESC,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE)"
					+ "VALUES((SELECT NVL(MAX(SERVICE_CODE), 0)+1 FROM TM_SERVICE),?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());				
			statement1.setString(1,object.getServicesDesc());
			statement1.setString(2, object.getCreatedBy());
			statement1.setString(3, object.getCreatedIp());	
			statement1.setString(4,object.getLastModifiedBy());
			statement1.setString(5, object.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;
			
	}
	public Integer editRecord(Service object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_SERVICE SET SERVICE_DESC=?,LAST_MODIFIED_BY=?"
					+ ",LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE SERVICE_CODE=?");
		
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1,object.getServicesDesc());					
				statement1.setString(2,object.getLastModifiedBy());
				statement1.setString(3, object.getLastModifiedIp());
				statement1.setString(4, object.getServicesCode());
				int rows=statement1.executeUpdate();				
				statement1.close();
				return rows;			
	}


	@Override
	public Integer removeRecord(Service object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  RECORD_STATUS in TM_Designation	
			StringBuffer query = new StringBuffer("UPDATE TM_SERVICE SET RECORD_STATUS=1 WHERE SERVICE_CODE=?");
				statement1 = connection.prepareStatement(query.toString());					
				statement1.setString(1, object.getServicesCode());
				int rows=statement1.executeUpdate();				
				statement1.close();
				
				return rows;
	}

	@Override
	public List<Service> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SERVICE_CODE,SERVICE_DESC ,CREATED_DATE FROM TM_SERVICE");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  servicesTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			servicesTypeList.add(temp);			
		}
		return servicesTypeList;
	}
	
	public List<String> getServiceList(String selectedServices) throws Exception{
		List<String>  servicesTypeList = new ArrayList<String>();
		if (selectedServices.contains("ALL")){
			servicesTypeList.add("ALL");
			 return servicesTypeList;
		}
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE IN ("+selectedServices+")";
		PreparedStatement statement = connection.prepareStatement(query);				
		ResultSet rs = statement.executeQuery();
		
		while(rs.next()) {			
			servicesTypeList.add(rs.getString(1));			
		}
		return servicesTypeList;
	}
	public List<Service> gettable() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SERVICE_CODE,SERVICE_DESC,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_SERVICE WHERE RECORD_STATUS=0");
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
		
		List<Service> servicestypeList = new ArrayList<Service>();
		while(rs.next()) {			
			servicestypeList.add(new Service(rs.getString(1),rs.getString(2),rs.getString(3)));			
		}
		return servicestypeList;
	}
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("servicesCode")) {
				orderbyClause.append(" ORDER BY SERVICE_CODE");
			}else if(sortColumn.equals("servicesDesc")) {
				orderbyClause.append(" ORDER BY SERVICE_DESC");
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


	public String getServicesDesc() {
		return servicesDesc;
	}


	public void setServicesDesc(String servicesDesc) {
		this.servicesDesc = servicesDesc;
	}


	public String getServicesCode() {
		return servicesCode;
	}


	public void setServicesCode(String servicesCode) {
		this.servicesCode = servicesCode;
	}


	@Override
	public List<KVPair<String, String>> getKVList(List<Service> list) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
