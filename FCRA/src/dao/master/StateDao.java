package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.master.State;
import utilities.KVPair;
import dao.BaseDao;

public class StateDao extends BaseDao<State, String, String> {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String sessionId;

	public StateDao(Connection connection) {
		super(connection);
	}

	@Override
	public Integer insertRecord(State object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		StringBuffer query = new StringBuffer("INSERT INTO TM_STATE(SCODE, SNAME, LCODE, DISPLAY_ORDER, RECORD_STATUS, "
				+ "CREATED_BY, CREATED_IP, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_IP, LAST_MODIFIED_DATE) "
				+ "VALUES(?, ?, ?, 0, 0, ?, ?, sysdate, ?, ?, sysdate) ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getScode());
		statement.setString(i++, object.getSname());
		statement.setInt(i++, object.getLcode());
		statement.setString(i++, object.getCreatedBy());
		statement.setString(i++, object.getCreatedIp());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		int rows = statement.executeUpdate();
		return rows;
	}

	@Override
	public Integer removeRecord(State object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		StringBuffer query = new StringBuffer("UPDATE TM_STATE SET RECORD_STATUS=1 WHERE SCODE=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;
		statement.setString(i++, object.getScode());
		int rows = statement.executeUpdate();
		return rows;
	}
	public Integer editRecord(State object) throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		StringBuffer query = new StringBuffer("UPDATE TM_STATE SET  SNAME=?, LCODE=?, LAST_MODIFIED_BY=?, LAST_MODIFIED_IP=?, LAST_MODIFIED_DATE=sysdate WHERE SCODE=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		int i=1;		
		statement.setString(i++, object.getSname());
		statement.setInt(i++, object.getLcode());
		statement.setString(i++, object.getLastModifiedBy());
		statement.setString(i++, object.getLastModifiedIp());
		statement.setString(i++, object.getScode());
		int rows = statement.executeUpdate();
		return rows;
	}


	
	public List<State> getMainState() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		
		
		 StringBuffer query = new StringBuffer("SELECT SCODE, SNAME, LCODE, to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_STATE WHERE RECORD_STATUS = 0");
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
		 List<State> userstatusList = new ArrayList<State>();
		 while(rs.next()) {	
		
			 userstatusList.add(new State(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4)));	
		 }
		 return userstatusList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("scode")) {
						orderbyClause.append(" ORDER BY SCODE");
					}else if(sortColumn.equals("sname")) {
						orderbyClause.append(" ORDER BY SNAME");
					}else if(sortColumn.equals("lcode")) {
						orderbyClause.append(" ORDER BY LCODE");
					
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
		 public List<KVPair<String, String>> getKVList() throws Exception{
				if(connection == null) {
					throw new Exception("Invalid connection");
				}				
				StringBuffer query = new StringBuffer("SELECT SCODE, SNAME FROM TM_STATE  WHERE RECORD_STATUS=0 order by SNAME ");
				PreparedStatement statement = connection.prepareStatement(query.toString());				
				ResultSet rs = statement.executeQuery();
				List<KVPair<String, String>>  stateList = new ArrayList<KVPair<String, String>>();
				while(rs.next()) {			
					KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
					stateList.add(temp);			
				}
				return stateList;
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

	public List<State> getAliveRecords() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
				
		StringBuffer query = new StringBuffer("SELECT SCODE, SNAME, LCODE, RECORD_STATUS, "
				+ "CREATED_BY, CREATED_IP, to_char(CREATED_DATE,'dd-mm-yyyy'), LAST_MODIFIED_BY, LAST_MODIFIED_IP, to_char(LAST_MODIFIED_DATE,'dd-mm-yyyy'), ROWID "
				+ "FROM TM_STATE WHERE RECORD_STATUS=0 ORDER BY SNAME");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		List<State> stateList = new ArrayList<State>();
		while(rs.next()) {
			State temp = new State();
			int i=1;
			temp.setScode(rs.getString(i++));
			temp.setSname(rs.getString(i++));
			temp.setLcode(rs.getInt(i++));
			//temp.setDisplayOrder(rs.getShort(i++));
			temp.setRecordStatus(rs.getBoolean(i++));
			temp.setCreatedBy(rs.getString(i++));
			temp.setCreatedIp(rs.getString(i++));
			temp.setCreatedDate(rs.getString(i++));
			temp.setLastModifiedBy(rs.getString(i++));
			temp.setLastModifiedIp(rs.getString(i++));
			temp.setLastModifiedDate(rs.getString(i++));
			temp.setRowIdentifier(rs.getString(i++));
			
			stateList.add(temp);
		}
		return stateList;
	}
	

	@Override
	public List<State> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<State> list) {
		int i=0;
		
		List<KVPair<String, String>> stateKVList = new ArrayList<KVPair<String, String>>();
		for(i=0; i<list.size(); i++) {
			State tempState = list.get(i);
			KVPair<String, String> temp = new KVPair<String, String>(tempState.getScode().toString(), tempState.getSname());
			stateKVList.add(temp);
		}
		return stateKVList;
	}
	/**
	 *  For MIS Report  
	 * @param selecteStateList  (State Code Comma Seperated)
	 * @return A String of State Name Seperated by Comma
	 * @throws Exception
	 */
	public String getState(String selecteStateList) throws Exception {
   if(selecteStateList.contains("ALL")){
	   return "ALL";
   }
   else{	if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer stateList=new StringBuffer();		
		StringBuffer query = new StringBuffer("SELECT SNAME FROM TM_STATE WHERE SCODE IN("+selecteStateList+")");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		ResultSet rs = statement.executeQuery();
		while(rs.next()){
			stateList=stateList.append(rs.getString(1)+",");
		}
		return stateList.toString();
	
	  }
	 }
	}

