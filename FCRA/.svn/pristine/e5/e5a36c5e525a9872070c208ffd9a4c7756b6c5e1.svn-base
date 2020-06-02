package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.MemberCategoryType;
import models.master.Office;
import models.master.ReligionType;
import utilities.KVPair;
import utilities.lists.List2;
import dao.BaseDao;

public class MemberCategoryTypeDao extends
BaseDao<MemberCategoryType, String, String> {
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

	public MemberCategoryTypeDao(Connection connection) {
		super(connection);
		
	}
	
	public List<MemberCategoryType> getAliveRecords(Office office)
			throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		if (office == null || office.getOfficeCode() == null
				|| office.getOfficeCode().equals("") == true) {
			throw new Exception("Invalid Offce");
		}
		StringBuffer query = new StringBuffer(
				"SELECT CATEGORY_CODE, CATEGORY_NAME, "
						+ "RECORD_STATUS, "
						+ "CREATED_BY, CREATED_IP, "						
						+ "to_char(CREATED_DATE,'dd-mm-yyyy'), "
						+ "ROWID RID FROM TM_MEMBER_CATEGORY A WHERE RECORD_STATUS=0");
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
		List<MemberCategoryType> memberCategoryTypeList = new ArrayList<MemberCategoryType>();
		while (rs.next()) {
			MemberCategoryType temp = new MemberCategoryType();
			int i = 1;
			temp.setCategoryCode(rs.getString(i++));
			temp.setCategoryName(rs.getString(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));

			memberCategoryTypeList.add(temp);
		}
		return memberCategoryTypeList;
	}
	
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if (sortColumn != null && sortColumn.equals("") == false) {
			if (sortColumn.equals("categoryCode")) {
				orderbyClause.append(" ORDER BY CATEGORY_CODE");
			} else if (sortColumn.equals("categoryName")) {
				orderbyClause.append(" ORDER BY CATEGORY_NAME");
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

	@Override
	public Integer insertRecord(MemberCategoryType memberCategoryType) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"INSERT INTO TM_MEMBER_CATEGORY(CATEGORY_CODE, CATEGORY_NAME, RECORD_STATUS, "
						+ "CREATED_BY, CREATED_DATE, CREATED_IP, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
						+ "VALUES(?, ?, 0, ?, systimestamp, ?, ?, ?, systimestamp) ");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, memberCategoryType.getCategoryCode());
		statement.setString(i++, memberCategoryType.getCategoryName());
		statement.setString(i++, memberCategoryType.getCreatedBy());
		statement.setString(i++, memberCategoryType.getCreatedIp());
		statement.setString(i++, memberCategoryType.getLastModifiedBy());
		statement.setString(i++, memberCategoryType.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public Integer removeRecord(MemberCategoryType memberCategoryType) throws Exception {		

		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_MEMBER_CATEGORY SET RECORD_STATUS = 1 WHERE CATEGORY_CODE = ? ");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, memberCategoryType.getCategoryCode());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public List<MemberCategoryType> getAll() throws Exception {		
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<MemberCategoryType> list) {		
		return null;
	}

	public List<List2> getMemberCategoryType(String categoryCode) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"SELECT CATEGORY_CODE, CATEGORY_NAME FROM TM_MEMBER_CATEGORY WHERE CATEGORY_CODE = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		statement.setString(1, categoryCode);
		ResultSet rs = statement.executeQuery();
		List<List2> requestedDetails = new ArrayList<List2>();
		while (rs.next()) {
			String id = rs.getString(2);
			requestedDetails.add(new List2(rs.getString(1), id));
		}
		return requestedDetails;
	}
	
	public Integer editRecord(MemberCategoryType memberCategoryType) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer(
				"UPDATE TM_MEMBER_CATEGORY SET CATEGORY_NAME = ?, LAST_MODIFIED_BY = ?, LAST_MODIFIED_IP = ?, LAST_MODIFIED_DATE = systimestamp WHERE CATEGORY_CODE = ?");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		int i = 1;
		statement.setString(i++, memberCategoryType.getCategoryName());
		statement.setString(i++, memberCategoryType.getLastModifiedBy());
		statement.setString(i++, memberCategoryType.getLastModifiedIp());
		statement.setString(i++, memberCategoryType.getCategoryCode());
		int rows = statement.executeUpdate();
		return rows;
	}
}
