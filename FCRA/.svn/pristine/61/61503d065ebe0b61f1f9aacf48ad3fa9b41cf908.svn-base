package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.District;
import utilities.KVPair;
import utilities.lists.List2;
import dao.BaseDao;

public class DistrictDao extends BaseDao<District, String, String> {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String sessionId;
	public DistrictDao(Connection connection) {
		super(connection);
	}

	@Override
	public Integer insertRecord(District object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("INSERT INTO TM_DISTRICT(DISTCODE, DISTNAME, SCODE, DISPLAY_ORDER, RECORD_STATUS, CREATED_BY, CREATED_IP, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES(?, ?, ?, 0, 0, ?, ?, sysdate, ?, ?, sysdate) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setInt(i++, object.getDcode());
		statement.setString(i++, object.getDname());
		statement.setInt(i++, object.getScode());
		statement.setString(i++, object.getCreatedBy());
		statement.setString(i++, object.getCreatedIp());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public Integer removeRecord(District object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("UPDATE TM_DISTRICT SET RECORD_STATUS=1 WHERE DISTCODE=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setInt(i++, object.getDcode());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public Integer editRecord(District object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		StringBuffer query = new StringBuffer("UPDATE TM_DISTRICT SET  DISTNAME=?, SCODE=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE DISTCODE=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;		
		statement.setString(i++, object.getDname());
		statement.setInt(i++, object.getScode());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		statement.setInt(i++, object.getDcode());
		int rows = statement.executeUpdate();
		return rows;
	}
	
	public List<District> getMainDistrict() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 StringBuffer query = new StringBuffer("SELECT a.DISTCODE, a.DISTNAME, a.SCODE, b.SNAME FROM TM_DISTRICT a, TM_STATE b WHERE a.RECORD_STATUS = 0 AND a.SCODE=b.SCODE");
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
		 List<District> districtList = new ArrayList<District>();
		 while(rs.next()) {	
					 districtList.add(new District(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4)));	
		 }
		 return districtList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("dcode")) {
						orderbyClause.append(" ORDER BY a.DISTCODE");
					}else if(sortColumn.equals("dname")) {
						orderbyClause.append(" ORDER BY a.DISTNAME");
					}else if(sortColumn.equals("sname")) {
						orderbyClause.append(" ORDER BY b.SNAME");
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
		 
		 /**
		  * Method Call for Old Registration Entry Services
		  * @param state
		  * @return
		  * @throws Exception
		  */
		 public  List<List2> getDistrictList(String state) throws Exception{
				if(connection == null) {
					throw new Exception("Invalid connection");
				}				
				String query = "select DISTCODE,DISTNAME  from TM_DISTRICT where SCODE=? and  RECORD_STATUS=0";
				PreparedStatement statement = connection.prepareStatement(query);				
				statement.setString(1, state);
				ResultSet rs = statement.executeQuery();
				 List<List2>  districtList = new ArrayList<List2>();
				while(rs.next()) {			
				districtList.add(new List2(rs.getString(1), rs.getString(2)));		
				}
				return districtList;
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
	public List<District> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<District> list) {
		// TODO Auto-generated method stub
		return null;
	}
}

