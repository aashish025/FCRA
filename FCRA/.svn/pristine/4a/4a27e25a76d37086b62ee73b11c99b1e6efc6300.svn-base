package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utilities.KVPair;
import models.master.DesignationType;
import models.master.Office;
import dao.BaseDao;

public class DesignationDao  extends BaseDao<DesignationType, String, String> {
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String designationId;
	private String designationName;
	private String shortdesignation;
	private String assignedDesc;
	

	public DesignationDao(Connection connection) {
		super(connection);
	}
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT DESIGNATION_ID,DESIGNATION_NAME ,CREATED_DATE FROM TM_DESIGNATION");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  designationTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			designationTypeList.add(temp);			
		}
		return designationTypeList;
	}
	
	public List<DesignationType> getMastertype() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT DESIGNATION_ID,DESIGNATION_NAME,SHORT_DESIGNATION,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_DESIGNATION WHERE RECORD_STATUS=0");
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
		
		List<DesignationType> designationtypeList = new ArrayList<DesignationType>();
		while(rs.next()) {			
			designationtypeList.add(new DesignationType(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));			
		}
		return designationtypeList;
	}
	
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("designationId")) {
				orderbyClause.append(" ORDER BY DESIGNATION_ID");
			}else if(sortColumn.equals("designationName")) {
				orderbyClause.append(" ORDER BY DESIGNATION_NAME");
			}else if(sortColumn.equals("enteredOn")) {
				orderbyClause.append(" ORDER BY CREATED_DATE");
			}else if(sortColumn.equals("shortDesignation")) {
				orderbyClause.append(" ORDER BY SHORT_DESIGNATION");
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
	private String generatedesignationId() throws Exception {
		String val=null;
		StringBuffer query = new StringBuffer("SELECT NVL(MAX(DESIGNATION_ID), 0)+1 FROM TM_DESIGNATION");
		PreparedStatement statement = connection.prepareStatement(query.toString());		
		ResultSet rs = statement.executeQuery();
		if(rs.next())
			 val=rs.getString(1);
		return val;
	}
	

	@Override
	public Integer insertRecord(DesignationType object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	

		designationId=generatedesignationId();	
	    setDesignationId(designationId);
	    
			StringBuffer query = new StringBuffer("INSERT INTO TM_DESIGNATION(DESIGNATION_ID,DESIGNATION_NAME,SHORT_DESIGNATION,RECORD_STATUS,CREATED_BY"
					+ ",CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE)"
					+ "VALUES(?,?,?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());	
			statement1.setString(1, designationId);
			statement1.setString(2, object.getDesignationName());
			statement1.setString(3, object.getShortDesignation());
			statement1.setString(4, object.getEnteredBy());
			statement1.setString(5, object.getCreatedIp());	
			statement1.setString(6,object.getLastModifiedBy());
			statement1.setString(7, object.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;
	}
	public List<KVPair<String, String>> getAvailableDes() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_ID, OFFICE_NAME FROM TM_OFFICETYPE WHERE OFFICE_ID NOT IN (SELECT OFFICE_ID FROM TM_DESIGNATION_OFFICETYPE WHERE DESIGNATION_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, designationId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  availableDesList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			availableDesList.add(temp);			
		}
		return availableDesList;
	}
      public List<KVPair<String, String>> getAssignedDes() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT OFFICE_ID,(SELECT OFFICE_NAME FROM TM_OFFICETYPE WHERE OFFICE_ID=TM_DESIGNATION_OFFICETYPE.OFFICE_ID) FROM TM_DESIGNATION_OFFICETYPE WHERE DESIGNATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, designationId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  assignedDesList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assignedDesList.add(temp);			
		}
		return assignedDesList;
	}
      public String saveDes(DesignationType object) throws Exception {
  		PreparedStatement statement=null;
  		if(connection == null) {
  			throw new Exception("Invalid connection");
  		}	
  		connection.setAutoCommit(false);
  		System.out.println("assignedDesc::::::    "+assignedDesc);
  		if((assignedDesc==null)){
  			StringBuffer query=new StringBuffer("DELETE FROM TM_DESIGNATION_OFFICETYPE WHERE DESIGNATION_ID=?");
  			statement = connection.prepareStatement(query.toString());
  			statement.setString(1, designationId);
  			statement.executeUpdate();
  			statement.close();
  		}else{
  			StringTokenizer ids = new StringTokenizer(assignedDesc, ",");
  			List<Short> updatedAssigendList = new ArrayList<Short>();
  			int i;
  			while (ids.hasMoreTokens()) {
  				Short menuId = Short.parseShort(ids.nextToken().trim());
  				updatedAssigendList.add(menuId);
  			}	
  			StringBuffer query=new StringBuffer("DELETE FROM TM_DESIGNATION_OFFICETYPE WHERE DESIGNATION_ID=?");
  			statement = connection.prepareStatement(query.toString());
  			statement.setString(1, designationId);
  			statement.executeUpdate();
  			statement.close();
  			StringBuffer query1 = new StringBuffer("INSERT INTO TM_DESIGNATION_OFFICETYPE(DESIGNATION_ID,OFFICE_ID,RECORD_STATUS,CREATED_BY,"
  					+ "CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
  					+ " VALUES(?,?,0,?,?,sysdate,?,?,sysdate)");
  			int count=updatedAssigendList.size();
  			for(i=0;i<count;i++){
  				 statement = connection.prepareStatement(query1.toString());		 
  				 statement.setString(1, designationId);	
  				 statement.setShort(2,updatedAssigendList.get(i));
  				 statement.setString(3, object.getEnteredBy());
  				 statement.setString(4, object.getCreatedIp());
  				 statement.setString(5,object.getLastModifiedBy());
  				 statement.setString(6, object.getLastModifiedIp());
  				 statement.executeUpdate();
  			}	
  			connection.commit();
  		}		 
  		return "success";			
  		
  	}

	public Integer editRecord(DesignationType object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_DESIGNATION SET DESIGNATION_NAME=?,SHORT_DESIGNATION=?, "
					+ "LAST_MODIFIED_BY=?,LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE DESIGNATION_ID=?");
		
				statement1 = connection.prepareStatement(query.toString());				
				statement1.setString(1,object.getDesignationName());
				statement1.setString(2,object.getShortDesignation());
				statement1.setString(3,object.getLastModifiedBy());
				statement1.setString(4, object.getLastModifiedIp());
				statement1.setString(5, object.getDesignationId());
				int rows=statement1.executeUpdate();				
				statement1.close();
				return rows;			
	}	

	@Override
	public Integer removeRecord(DesignationType object) throws Exception {
		return null;
				
				
	}
	
	public String deletetable(DesignationType object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query=new StringBuffer("UPDATE TM_DESIGNATION SET RECORD_STATUS=1 WHERE DESIGNATION_ID=?");
	    statement = connection.prepareStatement(query.toString());
	    statement.setString(1, designationId);
		statement.executeUpdate();
		statement.close();	
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query1=new StringBuffer("UPDATE TM_DESIGNATION_OFFICETYPE SET RECORD_STATUS=1 WHERE DESIGNATION_ID=?");
		statement1 = connection.prepareStatement(query1.toString());
	    statement1.setString(1, designationId);
		statement1.executeUpdate();
		statement1.close();	
	return "success";		
	}

	@Override
	public List<DesignationType> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<DesignationType> list) {
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
	public String getDesignationId() {
		return designationId;
	}
	public void setDesignationid(String designationid) {
		this.designationId = designationid;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public String getShortdesignation() {
		return shortdesignation;
	}
	public void setShortdesignation(String shortdesignation) {
		this.shortdesignation = shortdesignation;
	}


	public String getAssignedDesc() {
		return assignedDesc;
	}
	public void setAssignedDesc(String assignedDesc) {
		this.assignedDesc = assignedDesc;
	}
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	public List<DesignationType> getVisibleAliveRecords(Office office) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}		
		if(office == null || office.getOfficeCode() == null || office.getOfficeCode().equals("") == true) {
			throw new Exception("Invalid Office");
		}				
		StringBuffer query = new StringBuffer("SELECT A.DESIGNATION_ID, A.DESIGNATION_NAME, A.RECORD_STATUS, A.CREATED_BY, A.CREATED_IP, A.CREATED_DATE, A.ROWID RID, B.OFFICE_ID "
				+ "FROM TM_DESIGNATION A, TM_DESIGNATION_OFFICETYPE B, TM_OFFICETYPE C "
				+ "WHERE A.DESIGNATION_ID = B.DESIGNATION_ID "	 
				+ "AND B.OFFICE_ID = C.OFFICE_ID "	
				+ "AND C.OFFICE_ID = 3");
		PreparedStatement statement = connection.prepareStatement(query.toString());		
		ResultSet rs = statement.executeQuery();		
		List<DesignationType> disgnationTypeList = new ArrayList<DesignationType>();
		while(rs.next()) {
			DesignationType temp = new DesignationType();
			int i=1;
			temp.setDesignationId(rs.getString(i++));
			temp.setDesignationName(rs.getString(i++));
			temp.setRecordStatus(rs.getString(i++));			
			temp.setEnteredBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));				
			temp.setEnteredOn(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));			
			disgnationTypeList.add(temp);
		}		
		return disgnationTypeList;
	}
}
