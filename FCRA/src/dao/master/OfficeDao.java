package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDao;
import models.master.Office;
import models.master.OfficeType;
import utilities.KVPair;
import utilities.lists.List13;
import utilities.lists.List3;

public class OfficeDao extends BaseDao<Office,String, String> {

    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String acquisitiontype;
	private String acquisitionid;

	public OfficeDao(Connection connection) {
		super(connection);
	}

	public List<KVPair<String, String>> getKVList2() throws Exception{
		if(connection == null) {
		throw new Exception("Invalid connection");
		}	
		StringBuffer query = new StringBuffer("SELECT OFFICE_CODE, OFFICE_NAME  FROM TM_OFFICE WHERE OFFICE_ID!='0' AND OFFICE_ID!='7' AND RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  genderTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {	
		KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
		genderTypeList.add(temp);	
		}
		return genderTypeList;
		}

	
	public List<Office> getMasterOffice() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT a.OFFICE_CODE,a.OFFICE_NAME,b.CTR_NAME,a.ADDRESS,a.CONTACT_NO,a.EMAIL_ID FROM TM_OFFICE a, TM_COUNTRY b WHERE a.COUNTRY_CODE=b.CTR_CODE AND a.RECORD_STATUS=0");
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
		
		List<Office> officeList = new ArrayList<Office>();
		while(rs.next()) {			
			officeList.add(new Office(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));			
		}
		return officeList;
	}
	
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("officeCode")) {
				orderbyClause.append(" ORDER BY a.OFFICE_CODE");
			}else if(sortColumn.equals("officeName")) {
				orderbyClause.append(" ORDER BY a.OFFICE_NAME");
			}else if(sortColumn.equals("country")) {
				orderbyClause.append(" ORDER BY b.CTR_NAME");
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

		public Integer editRecord(Office object) throws Exception {
		PreparedStatement statement1=null;
		int rows=0;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
			StringBuffer query = new StringBuffer("UPDATE TM_OFFICE SET OFFICE_NAME=?,ADDRESS=?,CITY_NAME=?,STATE_NAME=?,COUNTRY_CODE=?,ZIPCODE=?,CONTACT_NO=?,EMAIL_ID=?,CURRENCY_CODE=?,SIGNATORY=?,LAST_MODIFIED_BY=?,LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE OFFICE_CODE=?");
		
			statement1 = connection.prepareStatement(query.toString());						
		
			statement1.setString(1, object.getOfficeName());//				OFFICE_NAME
			statement1.setString(2,object.getAddress());//				    ADDRESS
			System.out.println("Address   "+ object.getAddress());
			statement1.setString(3,object.getCityName());//				    CITY_NAME
			statement1.setString(4, object.getStateName());//				STATE_NAME
			statement1.setString(5, object.getCountryName());//				COUNTRY_CODE
			if(object.getZipcode()==null) statement1.setNull(6,  java.sql.Types.INTEGER); else statement1.setFloat(6, object.getZipcode());//				    ZIPCODE 
			statement1.setString(7,object.getContactNo());//				CONTACT_NO
			statement1.setString(8, object.getEmailId());//				    EMAIL_ID
			statement1.setString(9, object.getOfficeCurrency());//		    CURRENCY_CODE
			statement1.setString(10, object.getSignatory());//				SIGNATORY
			statement1.setString(11, object.getLastModifiedBy());//			LAST_MODIFIED_BY
			statement1.setString(12, object.getLastModifiedIp());//			LAST_MODIFIED_IP
			statement1.setString(13, object.getOfficeCode());//				OFFICE_CODE
			int rows1=statement1.executeUpdate();				
			statement1.close();
			if(rows1>0){
				StringBuffer query1 = new StringBuffer("UPDATE TM_USER SET EMAIL_ID=? WHERE USER_ID=?");
				 statement1 = connection.prepareStatement(query1.toString());					
				 statement1.setString(1, object.getEmailId());
				 statement1.setString(2, object.getOfficeCode());
				 rows=statement1.executeUpdate();				
				 statement1.close();	
			}
			
				return rows;			
	}	
		

	    /**
	     * Method For Convert Plain Text to MD5 format.
	     *
	     * @param  Password String In Plain text format
	     */
		private String generateMD5Pwd(String pwd) throws Exception {
			String val = null;
			StringBuffer query = new StringBuffer("SELECT FN_GEN_MD5(?) FROM DUAL");
			PreparedStatement statement = connection.prepareStatement(query
					.toString());
			statement.setString(1, pwd);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				val = rs.getString(1);
			}
			return val;
		}	
	    /**
	     * Method For Insert Record into Database.
	     *
	     * @param  Object Of Office 
	     */	
		
	public Integer insertRecord(Office object) throws Exception {
		if ( object.getOfficeId() > 7 || object.getOfficeId()==0)
			return -2;
	
		else 
		{
		String password=""; int rows1=0; int rows2=0;
		int rows=0; String user_name="";
		PreparedStatement statement1=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}	
			connection.setAutoCommit(false);
			StringBuffer queryForPwd = new StringBuffer("SELECT OFFICE_CODE FROM TM_OFFICE WHERE OFFICE_CODE=?");
			PreparedStatement statement = connection.prepareStatement(queryForPwd.toString());
			statement.setString(1, object.getOfficeCode());
			rows1=statement.executeUpdate();
			if(rows1>0)
			 return -1;
           /*password insert in TM_USER*/
			
			password = object.getOfficeCode() + "123";
			password = generateMD5Pwd(password);
			
			StringBuffer queryUserName = new StringBuffer("SELECT OFFICE_TYPE FROM TM_OFFICETYPE WHERE OFFICE_ID=? ");
			PreparedStatement stmt = connection.prepareStatement(queryUserName.toString());
			stmt.setInt(1, object.getOfficeId());
			ResultSet rs1 = stmt.executeQuery();
			while(rs1.next()) {
		   user_name=rs1.getNString(1);		
			}
			stmt.close();
			user_name=user_name+" "+object.getCityName();
			
			
				StringBuffer query = new StringBuffer("INSERT INTO TM_OFFICE(OFFICE_ID,OFFICE_CODE,OFFICE_NAME,ADDRESS,CITY_NAME,STATE_NAME,COUNTRY_CODE,ZIPCODE,CONTACT_NO,EMAIL_ID,CURRENCY_CODE,SIGNATORY,TIMEZONE_ID,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
						+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,0,0,?,?,sysdate,?,?,sysdate)");
				statement1 = connection.prepareStatement(query.toString());		
				statement1.setInt(1,object.getOfficeId());//				
				statement1.setString(2, object.getOfficeCode());//				OFFICE_CODE
				statement1.setString(3, object.getOfficeName());//				OFFICE_NAME
				statement1.setString(4,object.getAddress());//				ADDRESS
				statement1.setString(5,object.getCityName());//				CITY_NAME
				statement1.setString(6, object.getStateName());//				STATE_NAME
				statement1.setString(7, object.getCountryName());//				COUNTRY_CODE
				 if(object.getZipcode()==null) statement1.setNull(8,  java.sql.Types.INTEGER); else statement1.setFloat(8, object.getZipcode());
				//statement1.setInt(8, object.getZipcode());//				ZIPCODE
				statement1.setString(9,object.getContactNo());//				CONTACT_NO
				statement1.setString(10, object.getEmailId());//				EMAIL_ID
				statement1.setString(11, object.getOfficeCurrency());//				CURRENCY_CODE
				statement1.setString(12, object.getSignatory());//				SIGNATORY
				statement1.setNull(13, java.sql.Types.INTEGER);//statement1.setInt(13, Integer.parseInt(object.getTimeZoneId()));//				TIMEZONE_ID
				statement1.setString(14, object.getCreatedBy());               //				CREATED_BY
				statement1.setString(15, object.getCreatedIp());//				CREATED_IP
				statement1.setString(16, object.getLastModifiedBy());//				LAST_MODIFIED_BY
				statement1.setString(17, object.getLastModifiedIp());//				LAST_MODIFIED_IP
          	    rows1=statement1.executeUpdate();
          	    statement1.close();
          	    if(rows1>0){
    				StringBuffer query1 = new StringBuffer("INSERT INTO TM_USER(USER_ID,USERPWD,EMAIL_ID,OFFICE_CODE,USER_LEVEL,USER_NAME,DESIGNATION_ID,"
    						+ "PWD_DATE,LOGIN_FLAG,STATUS_ID,WRONG_PWD_COUNT,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
    						+ " VALUES(?,?,?,?,?,?,?,sysdate-31,'Y',0,0,?,?,sysdate,?,?,sysdate)");
    				statement1 = connection.prepareStatement(query1.toString());		
    				statement1.setString(1,object.getOfficeCode());				
    				statement1.setString(2, password);
    				statement1.setString(3, object.getEmailId());
    				statement1.setString(4,object.getOfficeCode());
        			if ( object.getOfficeId() == 1 || object.getOfficeId() == 2 || object.getOfficeId() == 3 || object.getOfficeId() == 4 || object.getOfficeId() == 5 || object.getOfficeId() == 6)
        				statement1.setInt(5, 1);
        			else if (object.getOfficeId() == 7)
        				statement1.setInt(5, 3);
    				statement1.setString(6, user_name);
    				if ( object.getOfficeId() == 1 || object.getOfficeId() == 2 || object.getOfficeId() == 3 || object.getOfficeId() == 4 || object.getOfficeId() == 5 || object.getOfficeId() == 6)
    					statement1.setInt(7, 3);
    				else if (object.getOfficeId() == 7)
    					statement1.setInt(7, 1);
    				statement1.setString(8, object.getCreatedBy());            
    				statement1.setString(9, object.getCreatedIp());//				
    				statement1.setString(10, object.getLastModifiedBy());//				
    				statement1.setString(11, object.getLastModifiedIp());//				
              	    rows2=statement1.executeUpdate();
              	    statement1.close();
          	    }
          	  /*assign role/ insert data into TM_USER_ROLE*/
        		if (rows2 > 0) {
        			StringBuffer query2 = new StringBuffer("INSERT INTO TM_USER_ROLE(USER_ID,ROLE_ID,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE) "
        							+ " VALUES(?,?,0,0,?,?,sysdate,?,?,sysdate)");	
        			statement1 = connection.prepareStatement(query2.toString());
        			statement1.setString(1, object.getOfficeCode());
        			if ( object.getOfficeId() == 1 || object.getOfficeId() == 2 || object.getOfficeId() == 3 || object.getOfficeId() == 4 || object.getOfficeId() == 5 || object.getOfficeId() == 6)
        				statement1.setInt(2, 2);
        			else if (object.getOfficeId() == 7)
        				statement1.setInt(2, 4);
                 	statement1.setString(3, object.getCreatedBy()); 
        			statement1.setString(4, object.getCreatedIp());
        			statement1.setString(5, object.getLastModifiedBy());
        			statement1.setString(6, object.getLastModifiedIp());
        			rows= statement1.executeUpdate();
        		}
          	    
          	    connection.commit();
				statement1.close();
				return rows;
	}
	}

    /**
     * Method use For Remove Record  .
     * Set Record Status 1 in TM_OFFICE,TM_USER,TM_USER_ROLE against OFFICE_CODE
     * @param  Object Of Office 
     */
	public Integer removeRecord(Office object) throws Exception {
		PreparedStatement statement1=null; int rows=0; int rows1=0; int rows2=0;
		if(connection == null) {
			throw new Exception("Invalid connection");
			
		}	
		connection.setAutoCommit(false);
			// Updating  RECORD_STATUS in TM_ROOM_TYPE			
			StringBuffer query = new StringBuffer("UPDATE TM_OFFICE SET RECORD_STATUS=1 WHERE OFFICE_CODE=?");
				statement1 = connection.prepareStatement(query.toString());					
				statement1.setString(1, object.getOfficeCode());
				 rows1=statement1.executeUpdate();				
				statement1.close();
				if(rows1>0){
					StringBuffer query1 = new StringBuffer("UPDATE TM_USER SET STATUS_ID=1 WHERE USER_ID=?");
					statement1 = connection.prepareStatement(query1.toString());					
					statement1.setString(1, object.getOfficeCode());
					 rows2=statement1.executeUpdate();				
					statement1.close();	
				}
				if (rows2 > 0) {
					StringBuffer query1 = new StringBuffer("UPDATE TM_USER_ROLE SET RECORD_STATUS=1 WHERE USER_ID=?");
					statement1 = connection.prepareStatement(query1.toString());
					statement1.setString(1, object.getOfficeCode());
					rows = statement1.executeUpdate();
					statement1.close();
				}
				connection.commit();
				return rows;
	}

	@Override
	public List<Office> getAll() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<Office> list) {
		int i=0;
		
		List<KVPair<String, String>> acquisitionTypeKVList = new ArrayList<KVPair<String, String>>();
		for(i=0; i<list.size(); i++) {
			Office tempPpty = list.get(i);
			KVPair<String, String> temp = new KVPair<String, String>(tempPpty.getOfficeCode(), tempPpty.getOfficeName());
			acquisitionTypeKVList.add(temp);
		}
		return acquisitionTypeKVList;
	}

	public List<Office> getOffices(OfficeType officeType) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		StringBuffer query = new StringBuffer("SELECT a.OFFICE_ID, a.OFFICE_CODE,a.OFFICE_NAME,b.COUNTRY_NAME,a.ADDRESS,a.CONTACT_NO,a.EMAIL_ID "
				+ "FROM TM_OFFICE a, TM_COUNTRY b "
				+ "WHERE a.OFFICE_ID=? AND a.COUNTRY_CODE=b.COUNTRY_CODE AND a.RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officeType.getOfficeId());
		ResultSet rs = statement.executeQuery();
		
		List<Office> officeList = new ArrayList<Office>();
		while(rs.next()) {
			int i=1;
			
			Office office = new Office();
			office.setOfficeId(rs.getInt(i++));
			office.setOfficeCode(rs.getString(i++));
			office.setOfficeName(rs.getString(i++));
			office.setCountryName(rs.getString(i++));
			office.setAddress(rs.getString(i++));
			office.setContactNo(rs.getString(i++));
			office.setEmailId(rs.getString(i++));
			
			officeList.add(office);			
		}
		return officeList;
	}

	public List<Office> getAliveRecords() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
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

		public String getAcquisitiontype() {
			return acquisitiontype;
		}

		public void setAcquisitiontype(String acquisitiontype) {
			this.acquisitiontype = acquisitiontype;
		}

		public String getAcquisitionid() {
			return acquisitionid;
		}

		public void setAcquisitionid(String acquisitionid) {
			this.acquisitionid = acquisitionid;
		}

		public List<List13> getEditOfficeDetail(Office object) throws Exception {
			if(connection == null) {
				throw new Exception("Invalid connection");
			}				
			StringBuffer query = new StringBuffer("SELECT a.OFFICE_NAME,b.OFFICE_CODE,b.OFFICE_NAME,b.ADDRESS,b.CITY_NAME,b.STATE_NAME,b.COUNTRY_CODE,b.ZIPCODE,b.CONTACT_NO,b.EMAIL_ID,b.CURRENCY_CODE,b.SIGNATORY,b.CREATED_BY FROM TM_OFFICETYPE a ,TM_OFFICE b  "
								+ " WHERE  b.OFFICE_CODE=? AND b.OFFICE_ID=a.OFFICE_ID ");
			PreparedStatement statement = connection.prepareStatement(query.toString());
			System.out.println("qwertyyyyyyyyyyyyyy   "+object.getOfficeCode());
			statement.setString(1,object.getOfficeCode());
			ResultSet rs = statement.executeQuery();
			List<List13> requestedDetails = new ArrayList<List13>();
			while(rs.next()) {
				String sig="";String officename="";String city="";String state="";String zipcode="";String contact="";String email="";String add="";
				if(rs.getString(12)==null) sig=""; else sig=rs.getString(12);
				if(rs.getString(3)==null) officename=""; else officename=rs.getString(3);
				if(rs.getString(5)==null) city=""; else city=rs.getString(5);
				if(rs.getString(6)==null) state=""; else state=rs.getString(6);
				if(rs.getString(8)==null) zipcode=""; else zipcode=rs.getString(8);
				if(rs.getString(9)==null) contact=""; else contact=rs.getString(9);
				if(rs.getString(10)==null) email=""; else email=rs.getString(10);
				if(rs.getString(4)==null) add=""; else add=rs.getString(4);
				if(rs.getString(12)==null) sig=""; else sig=rs.getString(12);
				if(rs.getString(12)==null) sig=""; else sig=rs.getString(12);
      		System.out.println("  1.OFFICE_NAME      "+rs.getString(1)+"  2.ADDRESS      "+rs.getString(2)+" 3.CITY_NAME         "+rs.getString(3)+"  4.STATE_NAME        "+rs.getString(4)+"   5.COUNTRY_NAME   "+rs.getString(5)+"  6.ZIPCODE     "+rs.getString(6)+"  7.CONTACT_NO      "+rs.getString(7)+"  8.EMAIL_ID     "+rs.getString(8)+"  9.CURRENCY_NAME    "+rs.getString(9)+"   10.SIGNATORY     "+rs.getString(10)+"   11.ZONE_NAME     "+rs.getString(10));
				requestedDetails.add(new List13(rs.getString(1),rs.getString(2),officename,add,city,state,rs.getString(7),zipcode.trim(),contact,email,rs.getString(11),sig,rs.getString(13)));
			}
			return requestedDetails;		
		}
      public List<String> getOfiiceName(String officeCode) throws Exception{
    	  if(connection == null) {
				throw new Exception("Invalid connection");
    	  }
				StringBuffer query = new StringBuffer("select office_name||','||City_name from TM_OFFICE where office_code=?");
	   PreparedStatement statement = connection.prepareStatement(query.toString());
       statement.setString(1,officeCode);
	   ResultSet rs = statement.executeQuery();
  	  List<String> officeName= new ArrayList<String>();
  	  if(rs.next()){
  		officeName.add(rs.getString(1)); 
  	  }
    	  return officeName;
      }
}
