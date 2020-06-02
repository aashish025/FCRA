package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.Office;
import models.master.OfficeFacilityType;
import utilities.KVPair;
import utilities.lists.List2;
import dao.BaseDao;

public class OfficeFacilityTypeDao extends
		BaseDao<OfficeFacilityType, String, String> {
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

	public OfficeFacilityTypeDao(Connection connection) {
		super(connection);
	}

	@Override
	public Integer insertRecord(OfficeFacilityType officeFacilityType)
			throws Exception {
		StringBuffer query = new StringBuffer(
				"INSERT INTO TM_OFFICE_FACILITIES(FACILITY_ID, FACILITY_DESC, RECORD_STATUS, "
						+ "CREATED_BY, CREATED_DATE, CREATED_IP, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
						+ "VALUES(?, ?, 0, ?, systimestamp, ?, ?, ?, systimestamp) ");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setInt(i++, officeFacilityType.getFacilityId());
		statement.setString(i++, officeFacilityType.getFacilityDesc());
		statement.setString(i++, officeFacilityType.getCreatedBy());
		statement.setString(i++, officeFacilityType.getCreatedIp());
		statement.setString(i++, officeFacilityType.getLastModifiedBy());
		statement.setString(i++, officeFacilityType.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public Integer removeRecord(OfficeFacilityType officeFacilityType)
			throws Exception {
		int rows = 0;
		try {
			PreparedStatement statement = null;
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			StringBuffer query = new StringBuffer(
					"UPDATE TM_OFFICE_FACILITIES SET RECORD_STATUS = 1 WHERE FACILITY_ID = ?");
			statement = connection.prepareStatement(query.toString());
			statement.setInt(1, officeFacilityType.getFacilityId());
			rows = statement.executeUpdate();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public List<OfficeFacilityType> getAll() throws Exception {		
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<OfficeFacilityType> list) {		
		return null;
	}

	public List<OfficeFacilityType> getVisibleAliveRecords(Office office)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}

		if (office == null || office.getOfficeCode() == null
				|| office.getOfficeCode().equals("") == true) {
			throw new Exception("Invalid Office");
		}

		StringBuffer query = new StringBuffer(
				"SELECT FACILITY_ID, FACILITY_DESC, "
						+ "RECORD_STATUS, "
						+ "CREATED_BY, CREATED_IP, "
						+ "CREATED_DATE, "
						+ "ROWID RID FROM TM_OFFICE_FACILITIES A WHERE RECORD_STATUS = 0");

		PreparedStatement statement = connection.prepareStatement(query
				.toString());

		ResultSet rs = statement.executeQuery();

		List<OfficeFacilityType> officeFacilityTypeList = new ArrayList<OfficeFacilityType>();
		while (rs.next()) {
			OfficeFacilityType temp = new OfficeFacilityType();
			int i = 1;
			temp.setFacilityId(rs.getInt(i++));
			temp.setFacilityDesc(rs.getString(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));

			officeFacilityTypeList.add(temp);
		}
		return officeFacilityTypeList;
	}

	public List<OfficeFacilityType> getAliveRecords(Office office)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		if (office == null || office.getOfficeCode() == null
				|| office.getOfficeCode().equals("") == true) {
			throw new Exception("Invalid Offce");
		}
		StringBuffer query = new StringBuffer(
				"SELECT FACILITY_ID, FACILITY_DESC, "
						+ "RECORD_STATUS, "
						+ "CREATED_BY, CREATED_IP, "
						/*+ "CREATED_DATE, "*/
						+ "to_char(CREATED_DATE,'dd-mm-yyyy'), "
						+ "ROWID RID FROM TM_OFFICE_FACILITIES A WHERE RECORD_STATUS=0");
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
		List<OfficeFacilityType> officeFacilityTypeList = new ArrayList<OfficeFacilityType>();
		while (rs.next()) {
			OfficeFacilityType temp = new OfficeFacilityType();
			int i = 1;
			temp.setFacilityId(rs.getInt(i++));
			temp.setFacilityDesc(rs.getString(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));

			officeFacilityTypeList.add(temp);
		}
		return officeFacilityTypeList;
	}

	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if (sortColumn != null && sortColumn.equals("") == false) {
			if (sortColumn.equals("facilityId")) {
				orderbyClause.append(" ORDER BY FACILITY_ID");
			} else if (sortColumn.equals("facilityDesc")) {
				orderbyClause.append(" ORDER BY FACILITY_DESC");
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

	public List<List2> getOfficeFacilityType(String officeFacilityType)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"SELECT FACILITY_DESC, FACILITY_ID FROM TM_OFFICE_FACILITIES WHERE FACILITY_ID = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		statement.setString(1, officeFacilityType);
		ResultSet rs = statement.executeQuery();
		List<List2> requestedDetails = new ArrayList<List2>();
		while (rs.next()) {
			String id = Integer.toString(rs.getInt(2));
			requestedDetails.add(new List2(rs.getString(1), id));
		}
		return requestedDetails;
	}

	public Integer editRecord(OfficeFacilityType officeFacilityType)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_OFFICE_FACILITIES SET FACILITY_DESC = ?, LAST_MODIFIED_BY = ?, LAST_MODIFIED_IP = ?, LAST_MODIFIED_DATE = systimestamp WHERE FACILITY_ID = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, officeFacilityType.getFacilityDesc());
		statement.setString(i++, officeFacilityType.getLastModifiedBy());
		statement.setString(i++, officeFacilityType.getLastModifiedIp());
		statement.setInt(i++, officeFacilityType.getFacilityId());
		int rows = statement.executeUpdate();
		return rows;
	}
}