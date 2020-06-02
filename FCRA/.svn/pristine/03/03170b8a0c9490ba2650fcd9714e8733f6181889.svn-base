package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import utilities.KVPair;
import models.master.PcSection;
import dao.BaseDao;

public class PcSectionDao extends BaseDao<PcSection, String, String> {
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String sectionId;
	private String sectionName;
	private String assignedService;
    private String myOfficeCode;
	public PcSectionDao(Connection connection) {
		super(connection);
	}
	
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SECTION_ID,SECTION_NAME ,CREATED_DATE FROM TM_PC_SECTION");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  pcsectionTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			pcsectionTypeList.add(temp);			
		}
		return pcsectionTypeList;
	}
	
	public List<PcSection> gettable() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SECTION_ID,SECTION_NAME,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_PC_SECTION WHERE RECORD_STATUS=0 AND OFFICE_CODE=?");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		PreparedStatement statement = connection.prepareStatement(countQuery.toString());	
		statement.setString(1, myOfficeCode);
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
			statement.setString(1, myOfficeCode);
			statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
			statement.setInt(3, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();
		
		List<PcSection> pcsectiontypeList = new ArrayList<PcSection>();
		while(rs.next()) {			
			pcsectiontypeList.add(new PcSection(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));			
		}
		return pcsectiontypeList;
	}
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("sectionId")) {
				orderbyClause.append(" ORDER BY SECTION_ID");
			}else if(sortColumn.equals("sectionName")) {
				orderbyClause.append(" ORDER BY SECTION_NAME");
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
					+ "T2 AS (SELECT * FROM (SELECT T1.*, ROWNUM RN FROM T1) WHERE RN<=?) SELECT * FROM T2 WHERE RN>=?");
		return queryForPaging.toString();
	}
	
	
	
	private String generatesectionId() throws Exception {
		String val=null;
		StringBuffer query = new StringBuffer("SELECT NVL(MAX(SECTION_ID), 0)+1 FROM TM_PC_SECTION");
		PreparedStatement statement = connection.prepareStatement(query.toString());		
		ResultSet rs = statement.executeQuery();
		if(rs.next())
			 val=rs.getString(1);
		return val;
	}

	@Override
	public Integer insertRecord(PcSection pcsection) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		sectionId=generatesectionId();	
	    setSectionId(sectionId);
		StringBuffer query = new StringBuffer("INSERT INTO TM_PC_SECTION(SECTION_ID,SECTION_NAME,OFFICE_CODE,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE)VALUES(?,?,?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());	
			statement1.setString(1, sectionId);
			statement1.setString(2, pcsection.getSectionName());
			statement1.setString(3, pcsection.getMyOfficeCode());
			statement1.setString(4, pcsection.getCreatedBy());
			statement1.setString(5, pcsection.getCreatedIp());	
			statement1.setString(6,pcsection.getLastModifiedBy());
			statement1.setString(7, pcsection.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;
}
	public List<KVPair<String, String>> getAvailableServices() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SERVICE_CODE, SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE NOT IN (SELECT SERVICE_CODE FROM TM_PC_SERVICE_SECTION WHERE SECTION_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, sectionId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  availableserviceList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			availableserviceList.add(temp);			
		}
		return availableserviceList;
	}
      public List<KVPair<String, String>> getAssignedServices() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SERVICE_CODE,(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=TM_PC_SERVICE_SECTION.SERVICE_CODE) FROM TM_PC_SERVICE_SECTION WHERE SECTION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, sectionId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  assignedServiceList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assignedServiceList.add(temp);			
		}
		return assignedServiceList;
	}
      
      
      public String saveService(PcSection object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		if((assignedService.equals("null"))){
				StringBuffer query=new StringBuffer("DELETE FROM TM_PC_SERVICE_SECTION WHERE SECTION_ID=?");
				statement = connection.prepareStatement(query.toString());
				statement.setString(1, sectionId);
				statement.executeUpdate();
				statement.close();
			}else{
			StringTokenizer ids = new StringTokenizer(assignedService, ",");
			List<String> updatedServiceList = new ArrayList<String>();
			int i;
			while (ids.hasMoreTokens()) {
				String menuId = ids.nextToken().trim();
				updatedServiceList.add(menuId);
			}	
		
			StringBuffer query=new StringBuffer("DELETE FROM TM_PC_SERVICE_SECTION WHERE SECTION_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, sectionId);
			statement.executeUpdate();
			statement.close();
			StringBuffer query1 = new StringBuffer("INSERT INTO TM_PC_SERVICE_SECTION(OFFICE_CODE,SECTION_ID,SERVICE_CODE) VALUES(?,?,?)");
			int count=updatedServiceList.size();
			for(i=0;i<count;i++){
				 statement = connection.prepareStatement(query1.toString());
				 statement.setString(1, object.getMyOfficeCode());
				 statement.setString(2, sectionId);	
				 statement.setString(3,updatedServiceList.get(i));
				 statement.executeUpdate();
			}	
			connection.commit();
		}		 
		return "success";			
		
	}
      public Integer editRecord(PcSection object) throws Exception {
    		PreparedStatement statement1=null;
    		if(connection == null) {
    			throw new Exception("Invalid connection");
    		}			
    			StringBuffer query = new StringBuffer("UPDATE TM_PC_SECTION SET SECTION_NAME=?, LAST_MODIFIED_BY=?"
    					+ ", LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE SECTION_ID=?");
    		
    				statement1 = connection.prepareStatement(query.toString());				
    				statement1.setString(1,object.getSectionName());					
    				statement1.setString(2,object.getLastModifiedBy());
    				statement1.setString(3, object.getLastModifiedIp());
    				statement1.setString(4, object.getSectionId());
    				int rows=statement1.executeUpdate();				
    				statement1.close();
    				return rows;			
    	}	

	

	public String deleterole(PcSection object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query=new StringBuffer("UPDATE TM_PC_SECTION SET RECORD_STATUS=1 WHERE SECTION_ID=?");
	    statement = connection.prepareStatement(query.toString());
	    statement.setString(1, sectionId);
		statement.executeUpdate();
		statement.close();	
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query1=new StringBuffer("DELETE FROM TM_PC_SERVICE_SECTION WHERE SECTION_ID=?");
		statement1 = connection.prepareStatement(query1.toString());
        statement1.setString(1, sectionId);
		statement1.executeUpdate();
		statement1.close();	
		return "success";			
}	
	
	@Override
	public Integer removeRecord(PcSection object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PcSection> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<PcSection> list) {
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

	


	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getAssignedService() {
		return assignedService;
	}

	public void setAssignedService(String assignedService) {
		this.assignedService = assignedService;
	}

	public String getMyOfficeCode() {
		return myOfficeCode;
	}

	public void setMyOfficeCode(String myOfficeCode) {
		this.myOfficeCode = myOfficeCode;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}


	
	
	
}
