package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.DonorType;
import models.master.Office;
import models.master.ReligionType;
import utilities.KVPair;
import utilities.lists.List2;
import dao.BaseDao;

public class DonorTypeDao extends
BaseDao<DonorType, String, String> {	
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	
	public DonorTypeDao(Connection connection) {
		super(connection);		
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
	
	@Override
	public Integer insertRecord(DonorType donorType) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"INSERT INTO TM_DONOR_TYPE(DONOR_ID, DONOR_TNAME, RECORD_STATUS, "
						+ "CREATED_BY, CREATED_DATE, CREATED_IP, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
						+ "VALUES(?, ?, 0, ?, systimestamp, ?, ?, ?, systimestamp) ");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, donorType.getDonorId());
		statement.setString(i++, donorType.getDonorName());
		statement.setString(i++, donorType.getCreatedBy());
		statement.setString(i++, donorType.getCreatedIp());
		statement.setString(i++, donorType.getLastModifiedBy());
		statement.setString(i++, donorType.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;		
	}
	@Override
	public Integer removeRecord(DonorType donorType) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_DONOR_TYPE SET RECORD_STATUS = 1 WHERE DONOR_ID = ? ");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, donorType.getDonorId());
		int rows = statement.executeUpdate();
		return rows;
		
	}
	@Override
	public List<DonorType> getAll() throws Exception {
		
		return null;
	}
	@Override
	public List<KVPair<String, String>> getKVList(List<DonorType> list) {
		
		return null;
	}
	
	public List<DonorType> getAliveRecords(Office office)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		if (office == null || office.getOfficeCode() == null
				|| office.getOfficeCode().equals("") == true) {
			throw new Exception("Invalid Offce");
		}
		StringBuffer query = new StringBuffer(
				"SELECT DONOR_ID, DONOR_TNAME, "
						+ "RECORD_STATUS, "
						+ "CREATED_BY, CREATED_IP, "						
						+ "to_char(CREATED_DATE,'dd-mm-yyyy'), "
						+ "ROWID RID FROM TM_DONOR_TYPE A WHERE RECORD_STATUS=0");
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
		List<DonorType> donorTypeList = new ArrayList<DonorType>();
		while (rs.next()) {
			DonorType temp = new DonorType();
			int i = 1;
			temp.setDonorId(rs.getString(i++));
			temp.setDonorName(rs.getString(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));

			donorTypeList.add(temp);
		}
		return donorTypeList;
	}
	
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if (sortColumn != null && sortColumn.equals("") == false) {
			if (sortColumn.equals("donorId")) {
				orderbyClause.append(" ORDER BY DONOR_ID");
			} else if (sortColumn.equals("donorName")) {
				orderbyClause.append(" ORDER BY DONOR_TNAME");
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
	
	public List<List2> getDonorType(String donorCode) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"SELECT DONOR_ID, DONOR_TNAME FROM TM_DONOR_TYPE WHERE DONOR_ID = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		statement.setString(1, donorCode);
		ResultSet rs = statement.executeQuery();
		List<List2> requestedDetails = new ArrayList<List2>();
		while (rs.next()) {
			String id = rs.getString(2);
			requestedDetails.add(new List2(rs.getString(1), id));
		}
		return requestedDetails;
	}
	
	public Integer editRecord(DonorType donorType) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_DONOR_TYPE SET DONOR_TNAME = ?, LAST_MODIFIED_BY = ?, LAST_MODIFIED_IP = ?, LAST_MODIFIED_DATE = systimestamp WHERE DONOR_ID = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, donorType.getDonorName());
		statement.setString(i++, donorType.getLastModifiedBy());
		statement.setString(i++, donorType.getLastModifiedIp());
		statement.setString(i++, donorType.getDonorId());
		int rows = statement.executeUpdate();
		return rows;
	}
	
}
