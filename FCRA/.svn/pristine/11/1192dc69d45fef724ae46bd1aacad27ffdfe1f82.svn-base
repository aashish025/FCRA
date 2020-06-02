package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utilities.KVPair;
import models.master.District;
import models.master.UserAudit;
import dao.BaseDao;

public class UserAuditDao extends BaseDao<District, String, String> {
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String userAudit;
	private String fromDate;
	private String toDate;
	
	
	
	
	public List<UserAudit> getMainUserAudit() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 String Audit=userAudit.toString().replace("[", "'").replace("]", "'").replace("," , "','").trim();
		 String AllAudit="1=1";
		 if(!Audit.trim().equals("ALL1")){
			 AllAudit= "a.userid in ('"+Audit+"')";
      	   
			}
 
		 
		 StringBuffer query = new StringBuffer("select (select user_name||'['||user_id||']' from tm_user where user_id=a.userid)as username, "
		 		+ " a.role_id||'' ||'-'||''||(select role_name from tm_role where role_id=a.role_id)as rolename, to_char(a.timestamp,'dd-mm-yyyy HH24:MI:SS.FF9'), a.created_ip, "
		 		+ " (CASE WHEN a.change_flag='I' THEN 'INSERT' ELSE 'DELETE' END) as flag from t_audit_userrole_master_log a where "+AllAudit+" "
		 		+ " and trunc(a.timestamp)  BETWEEN trunc(to_date('"+fromDate+"','dd-mm-yyyy')) and trunc(to_date('"+toDate+"','dd-mm-yyyy'))");
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
		 List<UserAudit> auditList = new ArrayList<UserAudit>();
		 while(rs.next()) {	
			 auditList.add(new UserAudit(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));	
		 }
		 return auditList;
		 }

		 private String preparePagingQuery(StringBuffer query) throws Exception {
				StringBuffer orderbyClause = new StringBuffer("");
				StringBuffer order = new StringBuffer("");
				if(sortColumn != null && sortColumn.equals("") == false) {
					if(sortColumn.equals("userName")) {
						orderbyClause.append(" ORDER BY username");
					}else if(sortColumn.equals("roleName")) {
						orderbyClause.append(" ORDER BY rolename");
					}else if(sortColumn.equals("timeStamp")) {
						orderbyClause.append(" ORDER BY a.timestamp");
					
					}else if(sortColumn.equals("createdIp")) {
						orderbyClause.append(" ORDER BY a.created_ip");
					}else if(sortColumn.equals("flag")) {
						orderbyClause.append(" ORDER BY flag");
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

	public UserAuditDao(Connection connection) {
		super(connection);
	}
	@Override
	public Integer insertRecord(District object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer removeRecord(District object) throws Exception {
		// TODO Auto-generated method stub
		return null;
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

	public String getUserAudit() {
		return userAudit;
	}

	public void setUserAudit(String userAudit) {
		this.userAudit = userAudit;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	
	}
