package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.BlockYear;
import models.master.Office;
import models.master.ReligionType;
import utilities.KVPair;
import utilities.lists.List2;
import dao.BaseDao;

public class ReligionTypeDao extends
BaseDao<ReligionType, String, String> {
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	
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

	public ReligionTypeDao(Connection connection) {
		super(connection);
	}

	@Override
	public Integer insertRecord(ReligionType religionType) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"INSERT INTO TM_RELIGION(RELIGION_CODE, RELIGION_DESC, RECORD_STATUS, "
						+ "CREATED_BY, CREATED_DATE, CREATED_IP, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
						+ "VALUES(?, ?, 0, ?, systimestamp, ?, ?, ?, systimestamp) ");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, religionType.getReligionCode());
		statement.setString(i++, religionType.getReligionDesc());
		statement.setString(i++, religionType.getCreatedBy());
		statement.setString(i++, religionType.getCreatedIp());
		statement.setString(i++, religionType.getLastModifiedBy());
		statement.setString(i++, religionType.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public Integer removeRecord(ReligionType religionType) throws Exception {
		
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_RELIGION SET RECORD_STATUS = 1 WHERE RELIGION_CODE = ? ");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, religionType.getReligionCode());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public List<ReligionType> getAll() throws Exception {
		
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<ReligionType> list) {
		
		return null;
	}

	public List<ReligionType> getAliveRecords(Office office)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		if (office == null || office.getOfficeCode() == null
				|| office.getOfficeCode().equals("") == true) {
			throw new Exception("Invalid Offce");
		}
		StringBuffer query = new StringBuffer(
				"SELECT RELIGION_CODE, RELIGION_DESC, "
						+ "RECORD_STATUS, "
						+ "CREATED_BY, CREATED_IP, "
						/*+ "CREATED_DATE, "*/
						+ "to_char(CREATED_DATE,'dd-mm-yyyy'), "
						+ "ROWID RID FROM TM_RELIGION A WHERE RECORD_STATUS=0");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("
				+ query + ")");
		PreparedStatement statement = connection.prepareStatement(countQuery
				.toString());
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			totalRecords = rs.getString(1);
		}
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
		String queryForPaging = preparePagingQuery(query);
		statement = connection.prepareStatement(queryForPaging);
		if (pageNum == null || recordsPerPage == null) {
		} else {
			statement.setInt(1, (pageRequested - 1) * pageSize + pageSize);
			statement.setInt(2, (pageRequested - 1) * pageSize + 1);
		}
		rs = statement.executeQuery();
		List<ReligionType> religionTypeList = new ArrayList<ReligionType>();
		while (rs.next()) {
			ReligionType temp = new ReligionType();
			int i = 1;
			temp.setReligionCode(rs.getString(i++));
			temp.setReligionDesc(rs.getString(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));

			religionTypeList.add(temp);
		}
		return religionTypeList;
	}
	
	
	public List<KVPair<String, String>> getReligionRecords()
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		
		StringBuffer query = new StringBuffer(
				"SELECT RELIGION_CODE, RELIGION_DESC FROM TM_RELIGION  WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>> religionTypeList = new  ArrayList<KVPair<String,String>>();
		while (rs.next()) {
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1),rs.getString(2).trim());
			religionTypeList.add(temp);
		}

		return religionTypeList;
	}
	
	
	
	public String getReligion(String selectedReligionList) throws Exception {
		   if(selectedReligionList.contains("ALL")){
			   return "ALL";
		   }
		   else{	if(connection == null) {
					throw new Exception("Invalid connection");
				}
				StringBuffer religionList=new StringBuffer();		
				StringBuffer query = new StringBuffer("SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE IN("+selectedReligionList+")");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					religionList=religionList.append(rs.getString(1)+",");
				}
				return religionList.toString();
			
			  }
			 }
	
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if (sortColumn != null && sortColumn.equals("") == false) {
			if (sortColumn.equals("religionCode")) {
				orderbyClause.append(" ORDER BY RELIGION_CODE");
			} else if (sortColumn.equals("religionDesc")) {
				orderbyClause.append(" ORDER BY RELIGION_DESC");
			} else if (sortColumn.equals("createdDate")) {
				orderbyClause.append(" ORDER BY CREATED_DATE");
			}
		}
		if (orderbyClause.equals("") == false) {
			if (sortOrder != null && sortOrder.equals("") == false) {
				if (sortOrder.equalsIgnoreCase("ASC")) {
					order.append("ASC");
				} else if (sortOrder.equalsIgnoreCase("DESC")) {
					order.append("DESC");
				}
			}
			if (order.toString().equals("") == false) {
				orderbyClause.append(" " + order);
			} else {
				orderbyClause = null;
			}
		}
		if (orderbyClause != null && orderbyClause.equals("") == false)
			query.append(orderbyClause);
		StringBuffer queryForPaging = null;
		if (pageNum == null || recordsPerPage == null)
			queryForPaging = query;
		else
			queryForPaging = new StringBuffer(
					"WITH T1 AS ("
							+ query
							+ "), T2 AS (SELECT T1.*, ROWNUM RN FROM T1 WHERE ROWNUM<=?) SELECT * FROM T2 WHERE RN>=?");
		return queryForPaging.toString();
	}
	
	public List<List2> getReligionType(String religionCode) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"SELECT RELIGION_DESC, RELIGION_CODE FROM TM_RELIGION WHERE RELIGION_CODE = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		statement.setString(1, religionCode);
		ResultSet rs = statement.executeQuery();
		List<List2> requestedDetails = new ArrayList<List2>();
		while (rs.next()) {
			String id = Integer.toString(rs.getInt(2));
			requestedDetails.add(new List2(rs.getString(1), id));
		}
		return requestedDetails;
	}
	
	public Integer editRecord(ReligionType religionType) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_RELIGION SET RELIGION_DESC = ?, LAST_MODIFIED_BY = ?, LAST_MODIFIED_IP = ?, LAST_MODIFIED_DATE = systimestamp WHERE RELIGION_CODE = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, religionType.getReligionDesc());
		statement.setString(i++, religionType.getLastModifiedBy());
		statement.setString(i++, religionType.getLastModifiedIp());
		statement.setString(i++, religionType.getReligionCode());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	
	public List<List2> getReligionType() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		String query = 
				"SELECT  RELIGION_CODE,RELIGION_DESC FROM TM_RELIGION  Where RECORD_STATUS=0";
		PreparedStatement statement = connection.prepareStatement(query);
	
		ResultSet rs = statement.executeQuery();
		List<List2> religionList = new ArrayList<List2>();
		while (rs.next()) {
			
			religionList.add(new List2(rs.getString(1), rs.getString(2)));
		}
		return religionList;
	}
	
}
