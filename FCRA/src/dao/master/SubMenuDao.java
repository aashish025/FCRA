package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.master.SubMenu;
import utilities.KVPair;
import dao.BaseDao;

public class SubMenuDao extends BaseDao<SubMenu, String, String> {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String sessionId;
	public SubMenuDao(Connection connection) {
		super(connection);
	}

	@Override
	public Integer insertRecord(SubMenu object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("INSERT INTO TM_SUBMENU(SMENU_ID, SMENU_NAME, ACTION_PATH, PMENU_ID, DISPLAY_ORDER, RECORD_STATUS, CREATED_BY, CREATED_IP, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES((SELECT NVL(MAX(SMENU_ID) , 0)+1 FROM TM_SUBMENU WHERE SMENU_ID!='99'  ), ?, ?, ?, 0, 0, ?, ?, sysdate, ?, ?, sysdate) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getSmenuName());
		statement.setString(i++, object.getActionPath());
		statement.setString(i++, object.getpMenuId());
		statement.setString(i++, object.getCreatedBy());
		statement.setString(i++, object.getCreatedIp());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public Integer removeRecord(SubMenu object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("UPDATE TM_SUBMENU SET RECORD_STATUS=1 WHERE SMENU_ID=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setShort(i++, object.getSmenuId());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public Integer editRecord(SubMenu object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		StringBuffer query = new StringBuffer("UPDATE TM_SUBMENU SET  SMENU_NAME=?, ACTION_PATH=?, PMENU_ID=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE SMENU_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;		
		statement.setString(i++, object.getSmenuName());
		statement.setString(i++, object.getActionPath());
		statement.setString(i++, object.getpMenuId());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		statement.setShort(i++, object.getSmenuId());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public List<SubMenu> getMainSubMenu() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 StringBuffer query = new StringBuffer("SELECT a.SMENU_ID, a.SMENU_NAME, a.ACTION_PATH, a.PMENU_ID, b.PMENU_NAME FROM TM_SUBMENU a, TM_PARENTMENU b WHERE a.RECORD_STATUS = 0 AND a.PMENU_ID=b.PMENU_ID");
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
		 List<SubMenu> submenuList = new ArrayList<SubMenu>();
		 while(rs.next()) {	
					 submenuList.add(new SubMenu(rs.getShort(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));	
		 }
		 return submenuList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("smenuName")) {
						orderbyClause.append(" ORDER BY a.SMENU_NAME");
					}else if(sortColumn.equals("actionPath")) {
						orderbyClause.append(" ORDER BY a.ACTION_PATH");
					}else if(sortColumn.equals("pMenuName")) {
						orderbyClause.append(" ORDER BY b.PMENU_NAME");
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

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public List<SubMenu> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<SubMenu> list) {
		// TODO Auto-generated method stub
		return null;
	}
}
