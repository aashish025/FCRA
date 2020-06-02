package dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utilities.KVPair;
import utilities.ValidationException;
import utilities.communication.mail.MailScheduler;
import utilities.lists.List1;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.lists.List4;
import utilities.lists.List5;
import utilities.lists.List7;
import utilities.lists.List8;
import utilities.lists.List9;
import utilities.notifications.Notification;
import models.master.BlockYear;
import models.master.RedFlagCategory;
import models.reports.AnnualStatusDetailsReport;
import models.reports.BlueFlaggedRcns;
import models.reports.RedFlaggedRcns;
import models.services.CommitteeMember;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class BlueFlaggedRcnsDao extends BaseDao<RedFlaggedRcns, String, String> {
	public BlueFlaggedRcnsDao(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	private String appId;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pageNum;
	private String searchString;
	private String functionaryName;
	private String searchFlag;
	private String state;
	private String district;
	private List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
	private List<List9> returnList = new ArrayList<List9>();
	private List<List4> historyList=new ArrayList<List4>();
	private List<List8> mailList=new ArrayList<List8>();
	private List<List5> smsList=new ArrayList<List5>();
	private List<List3> blueFlagDetailsList = new ArrayList<List3>();
	private List<CommitteeMember> committeeMembers = new ArrayList<CommitteeMember>();
	private List<Notification> notifyList = new ArrayList<Notification>();
	private List<List4> regCancDetails=new ArrayList<List4>();
	private String blueFlag;
	private String blueFlagRemoveRole;

	

	public List<BlueFlaggedRcns> getList() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
	
		 StringBuffer query = new StringBuffer("  with temp as( SELECT  "
		 		+ "(row_number() over (partition by a.rcn order by  C.STATUS_DATE desc)) as rn ,A.RCN,B.ASSO_NAME,"
		 		+ " (select sname from tm_state where scode=substr(b.stdist,1,2)), "
		 		+ "(select distname from tm_district where distcode=substr(b.stdist,-3,3))  as district,"
		 		+ " to_char(C.STATUS_DATE,'dd-mm-yyyy HH24:MI:SS'),"
		 		+ "  c.REMARKS "
		 		+ " FROM T_BLUE_FLAGGED_ASSOCIATIONS A, fc_india B, T_BLUE_FLAG_STATUS_HISTORY C  "
		 		+ "  WHERE c.status=0 AND  "
		 		+ " A.RCN=B.RCN AND A.RCN=C.RCN   "
		 		+ "  ) select * from temp where rn=1  ");
		
		 StringBuffer	 query1 = new StringBuffer(" WITH temp AS  (SELECT (row_number() over (partition BY a.rcn order by C.STATUS_DATE DESC)) AS rn ,   A.RCN,    B.ASSO_NAME,  "
					+ " (SELECT sname FROM tm_state WHERE scode=SUBSTR(b.stdist,1,2)) as state,(SELECT distname FROM tm_district WHERE distcode=SUBSTR(b.stdist,-3,3) )  as district,"
					+ " TO_CHAR(C.STATUS_DATE,'dd-mm-yyyy HH24:MI:SS') as statusdate ,"
					+ "  C.STATUS_DATE as statusdate1, c.REMARKS"
					+ "  FROM T_BLUE_FLAGGED_ASSOCIATIONS A,  fc_india B, T_BLUE_FLAG_STATUS_HISTORY C   WHERE c.status =0  AND"
					+ " A.RCN   =B.RCN  AND A.RCN  =C.RCN   ");
			
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
		 String queryForPaging = preparePagingQuery(query1);	
		 statement = connection.prepareStatement(queryForPaging);
         if(pageNum == null || recordsPerPage == null) {

		 }
		 else
		 {
		 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
		 statement.setInt(2, (pageRequested-1) * pageSize + 1);
		 }
		 rs = statement.executeQuery();
		 List<BlueFlaggedRcns> blueflaggedList = new ArrayList<BlueFlaggedRcns>();
		 while(rs.next()) {	
		
			 blueflaggedList.add(new BlueFlaggedRcns(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));	
		 }
		 return blueflaggedList;
		 }
	
	// Annual Model Open Query 
	public List<BlueFlaggedRcns> getAnnualList() throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
	
		 StringBuffer query = new StringBuffer(" WITH temp AS (SELECT (row_number() over (partition BY a.rcn order by C.STATUS_DATE DESC)) AS rn ,"
		 		+ " A.RCN, B.ASSO_NAME,(SELECT sname FROM tm_state WHERE scode=SUBSTR(b.stdist,1,2) ) AS state,"
		 		+ "(SELECT distname FROM tm_district WHERE distcode=SUBSTR(b.stdist,-3,3)), TO_CHAR(C.STATUS_DATE,'dd-mm-yyyy HH24:MI:SS') AS statusdate,"
		 		+ "( SELECT DISTINCT c.ACTION_BY ||'('|| e.user_name ||')'  FROM TM_User E WHERE c.action_by=E.USER_id ) AS action ,"
		 		+ " C.STATUS_DATE AS statusdate1, c.REMARKS FROM T_BLUE_FLAGGED_ASSOCIATIONS A, fc_india B, t_blue_flag_status_history C , TM_User e"
		 		+ "  WHERE c.status =0 AND A.RCN      =B.RCN  AND A.RCN      =C.RCN  ORDER BY statusdate1 DESC ),  t2 AS  (SELECT * FROM temp WHERE rn=1 ),"
		 		+ "  t3 AS (SELECT i.rcn, i.asso_name, (SELECT sname FROM tm_state WHERE scode=SUBSTR(stdist,1,2)) state_name, (SELECT DISTNAME  FROM tm_district"
		 		+ "  WHERE scode =SUBSTR(stdist,1,2)  AND DISTCODE=SUBSTR(stdist,-3,3)) district_name,  NVL(TO_CHAR(reg_date,'dd-mm-yyyy'),'---')  AS REGISTRERED_ON,"
		 		+ "  NVL(TO_CHAR(LAST_RENEWED_ON,'dd-mm-yyyy'),'---') AS LAST_RENEWED_ON,   NVL(TO_CHAR(valid_to,'dd-mm-yyyy'),'---')        AS EXPIRY_ON,   "
		 		+ " CASE    WHEN reg_date IS NULL   THEN 2006  ELSE   CASE  WHEN to_number(TO_CHAR(reg_date,'yyyy'))<2006   THEN 2006    ELSE"
		 		+ "  CASE       WHEN TRUNC(reg_date) >= to_date('01-APR-'||TO_CHAR(reg_date,'yyyy')) THEN to_number(TO_CHAR(reg_date,'yyyy'))+1"
		 		+ "  ELSE to_number(TO_CHAR(reg_date,'yyyy'))   END   END  END start_year,  to_number(TO_CHAR(sysdate, 'YYYY'))-1 end_year FROM fc_india i,  t2"
		 		+ "  WHERE i.rcn=t2.rcn), t4 AS( SELECT t3.*,(end_year-start_year) to_be_uploaded FROM t3 ), t5 AS  (SELECT a.rcn, COUNT(blkyear) AS uploaded"
		 		+ "  FROM t4, fc_fc3_tally a WHERE t4.rcn=a.rcn  AND to_number(SUBSTR(blkyear,1,4)) BETWEEN start_year AND end_year  GROUP BY a.rcn ),"
		 		+ " t6 AS (SELECT t4.*,NVL(t5.uploaded,0) uploaded FROM t4 LEFT JOIN t5 ON t5.rcn=t4.rcn ), t7 AS"
		 		+ " (SELECT * FROM t6 WHERE to_be_uploaded<=uploaded ORDER BY ASSO_NAME ASC) , T8 AS(SELECT T7.*, ROWNUM RN FROM T7 )"
		 		+ "SELECT count(*) FROM T8  ");
		 StringBuffer query1 = new StringBuffer(" WITH temp AS (SELECT (row_number() over (partition BY a.rcn order by C.STATUS_DATE DESC)) AS rn ,"
			 		+ " A.RCN, B.ASSO_NAME,(SELECT sname FROM tm_state WHERE scode=SUBSTR(b.stdist,1,2) ) AS state,"
			 		+ "(SELECT distname FROM tm_district WHERE distcode=SUBSTR(b.stdist,-3,3)), TO_CHAR(C.STATUS_DATE,'dd-mm-yyyy HH24:MI:SS') AS statusdate,"
			 		+ "( SELECT DISTINCT c.ACTION_BY ||'('|| e.user_name ||')'  FROM TM_User E WHERE c.action_by=E.USER_id ) AS action ,"
			 		+ " C.STATUS_DATE AS statusdate1, c.REMARKS FROM T_BLUE_FLAGGED_ASSOCIATIONS A, fc_india B, t_blue_flag_status_history C , TM_User e"
			 		+ "  WHERE c.status =0 AND A.RCN      =B.RCN  AND A.RCN      =C.RCN  ORDER BY statusdate1 DESC ),  t2 AS  (SELECT * FROM temp WHERE rn=1 ),"
			 		+ "  t3 AS (SELECT i.rcn, i.asso_name, (SELECT sname FROM tm_state WHERE scode=SUBSTR(stdist,1,2)) state_name, (SELECT DISTNAME  FROM tm_district"
			 		+ "  WHERE scode =SUBSTR(stdist,1,2)  AND DISTCODE=SUBSTR(stdist,-3,3)) district_name,  NVL(TO_CHAR(reg_date,'dd-mm-yyyy'),'---')  AS REGISTRERED_ON,"
			 		+ "  NVL(TO_CHAR(LAST_RENEWED_ON,'dd-mm-yyyy'),'---') AS LAST_RENEWED_ON,   NVL(TO_CHAR(valid_to,'dd-mm-yyyy'),'---')        AS EXPIRY_ON,   "
			 		+ " CASE    WHEN reg_date IS NULL   THEN 2006  ELSE   CASE  WHEN to_number(TO_CHAR(reg_date,'yyyy'))<2006   THEN 2006    ELSE"
			 		+ "  CASE       WHEN TRUNC(reg_date) >= to_date('01-APR-'||TO_CHAR(reg_date,'yyyy')) THEN to_number(TO_CHAR(reg_date,'yyyy'))+1"
			 		+ "  ELSE to_number(TO_CHAR(reg_date,'yyyy'))   END   END  END start_year,  to_number(TO_CHAR(sysdate, 'YYYY'))-1 end_year FROM fc_india i,  t2"
			 		+ "  WHERE i.rcn=t2.rcn), t4 AS( SELECT t3.*,(end_year-start_year) to_be_uploaded FROM t3 ), t5 AS  (SELECT a.rcn, COUNT(blkyear) AS uploaded"
			 		+ "  FROM t4, fc_fc3_tally a WHERE t4.rcn=a.rcn  AND to_number(SUBSTR(blkyear,1,4)) BETWEEN start_year AND end_year  GROUP BY a.rcn ),"
			 		+ " t6 AS (SELECT t4.*,NVL(t5.uploaded,0) uploaded FROM t4 LEFT JOIN t5 ON t5.rcn=t4.rcn ), t7 AS"
			 		+ " (SELECT * FROM t6 WHERE to_be_uploaded<=uploaded ORDER BY ASSO_NAME ASC) , T8 AS(SELECT T7.*, ROWNUM RN FROM T7 )"
			 		+ "SELECT * FROM T8 WHERE  rn between ? and ? ");
		 PreparedStatement statement = connection.prepareStatement(query.toString());	
		 ResultSet rs = statement.executeQuery();
		 if(rs.next()) {
		 totalRecords = rs.getString(1);
		 }
		 rs.close();
		statement.close();
		
		 Integer pageRequested = Integer.parseInt(pageNum);
		 Integer pageSize = Integer.parseInt(recordsPerPage);
		 String queryForPaging1 = preparePagingQuery2(query1);	
		 statement = connection.prepareStatement(queryForPaging1);
        if(pageNum == null || recordsPerPage == null) {

		 }
		 else
		 {
		 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
		 statement.setInt(1, (pageRequested-1) * pageSize + 1);
		 }
		 rs = statement.executeQuery();
		 List<BlueFlaggedRcns> blueflaggedList = new ArrayList<BlueFlaggedRcns>();
		 while(rs.next()) {	
		
			 blueflaggedList.add(new BlueFlaggedRcns(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11)));	
		 }
		 return blueflaggedList;
		 }
	
	
	
	
	
	
	
	
	
	
	
	// End 
	public List<BlueFlaggedRcns> getAnnualStatusList(String annualrcn) throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 StringBuffer query = new StringBuffer("with t1 as (select (case when reg_date is null then 2005 else case when trunc(reg_date) >= to_date('01-APR-'||to_char(reg_date, 'YYYY')) then to_number(to_char(reg_date, 'YYYY'))-1 "
		 		+ " else to_number(to_char(reg_date, 'YYYY'))-2 end  end)+1 st_year from fc_india where rcn=?), t2 as (select case when (select st_year from t1)"
		 		+ " < 2006 then 2006 else (select st_year from t1) end as start_year from dual),t3 as (select(select start_year from t2) + LEVEL - 1 yr from dual connect by LEVEL<=(to_number(to_char(sysdate, 'YYYY'))-2 ) - ((select start_year from t2)) + 1)select yr||'-'||(yr+1) blkyear from t3 "
		 		+ "where yr <= (to_number(to_char(sysdate, 'YYYY'))-1)");
			PreparedStatement statement = connection.prepareStatement(query.toString());
			statement.setString(1, annualrcn );			
			ResultSet rs = statement.executeQuery();
			 List<BlueFlaggedRcns> yearList = new ArrayList<BlueFlaggedRcns>();
			 StringBuffer query1; 
			 while(rs.next()) {	
				
				// status = "No";
				 query1 = new StringBuffer("select (Case when trunc(reg_date) >= to_date('01-APR-'||substr(?,-4,4)) then '-' else "
				 		+ "(select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' "
				 		+ "and t.blkyear = ? and t.rcn = ? )end ) as status from fc_india where rcn = ? ");
					PreparedStatement statement1 = connection.prepareStatement(query1.toString());
					statement1.setString(1, rs.getString(1) );
					statement1.setString(2, rs.getString(1) );
					statement1.setString(3, annualrcn );
					statement1.setString(4, annualrcn );
					ResultSet rs1 = statement1.executeQuery();
					if(rs1.next()){
						 yearList.add(new BlueFlaggedRcns(rs.getString(1), rs1.getString(1)));	
					}
				 
			 }

		return yearList;
	}
	public String getExistRcn(String rcn) throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		 StringBuffer query = new StringBuffer(" WITH temp AS (SELECT (row_number() over (partition BY a.rcn order by C.STATUS_DATE DESC)) AS rn ,"
			 		+ " A.RCN, B.ASSO_NAME,(SELECT sname FROM tm_state WHERE scode=SUBSTR(b.stdist,1,2) ) AS state,"
			 		+ "(SELECT distname FROM tm_district WHERE distcode=SUBSTR(b.stdist,-3,3)), TO_CHAR(C.STATUS_DATE,'dd-mm-yyyy HH24:MI:SS') AS statusdate,"
			 		+ "( SELECT DISTINCT c.ACTION_BY ||'('|| e.user_name ||')'  FROM TM_User E WHERE c.action_by=E.USER_id ) AS action ,"
			 		+ " C.STATUS_DATE AS statusdate1, c.REMARKS FROM T_BLUE_FLAGGED_ASSOCIATIONS A, fc_india B, t_blue_flag_status_history C , TM_User e"
			 		+ "  WHERE c.status =0 AND A.RCN      =B.RCN  AND A.RCN      =C.RCN  ORDER BY statusdate1 DESC ),  t2 AS  (SELECT * FROM temp WHERE rn=1 ),"
			 		+ "  t3 AS (SELECT i.rcn, i.asso_name, (SELECT sname FROM tm_state WHERE scode=SUBSTR(stdist,1,2)) state_name, (SELECT DISTNAME  FROM tm_district"
			 		+ "  WHERE scode =SUBSTR(stdist,1,2)  AND DISTCODE=SUBSTR(stdist,-3,3)) district_name,  NVL(TO_CHAR(reg_date,'dd-mm-yyyy'),'---')  AS REGISTRERED_ON,"
			 		+ "  NVL(TO_CHAR(LAST_RENEWED_ON,'dd-mm-yyyy'),'---') AS LAST_RENEWED_ON,   NVL(TO_CHAR(valid_to,'dd-mm-yyyy'),'---')        AS EXPIRY_ON,   "
			 		+ " CASE    WHEN reg_date IS NULL   THEN 2006  ELSE   CASE  WHEN to_number(TO_CHAR(reg_date,'yyyy'))<2006   THEN 2006    ELSE"
			 		+ "  CASE       WHEN TRUNC(reg_date) >= to_date('01-APR-'||TO_CHAR(reg_date,'yyyy')) THEN to_number(TO_CHAR(reg_date,'yyyy'))+1"
			 		+ "  ELSE to_number(TO_CHAR(reg_date,'yyyy'))   END   END  END start_year,  to_number(TO_CHAR(sysdate, 'YYYY'))-1 end_year FROM fc_india i,  t2"
			 		+ "  WHERE i.rcn=t2.rcn), t4 AS( SELECT t3.*,(end_year-start_year) to_be_uploaded FROM t3 ), t5 AS  (SELECT a.rcn, COUNT(blkyear) AS uploaded"
			 		+ "  FROM t4, fc_fc3_tally a WHERE t4.rcn=a.rcn  AND to_number(SUBSTR(blkyear,1,4)) BETWEEN start_year AND end_year  GROUP BY a.rcn ),"
			 		+ " t6 AS (SELECT t4.*,NVL(t5.uploaded,0) uploaded FROM t4 LEFT JOIN t5 ON t5.rcn=t4.rcn ), t7 AS"
			 		+ " (SELECT * FROM t6 WHERE to_be_uploaded<=uploaded ORDER BY ASSO_NAME ASC) , T8 AS(SELECT T7.*, ROWNUM RN FROM T7 )"
			 		+ "SELECT count(*) FROM T8 WHERE  rcn=? ");
			PreparedStatement statement = connection.prepareStatement(query.toString());
			statement.setString(1, rcn );	
			
			ResultSet rs = statement.executeQuery();
			String recordrcn="No";
			
		     while(rs.next())
		     {
		    	 int count=rs.getInt(1);
		    	 if(count!=0)
		    	 {
		    	 recordrcn="YES";
		    	 }
		    	 
		     }

		return recordrcn;
	}
		
	 private String preparePagingQuery(StringBuffer query) throws Exception {
			StringBuffer orderbyClause = new StringBuffer("");
			StringBuffer order = new StringBuffer("");
			if(sortColumn != null && sortColumn.equals("") == false) {
				if(sortColumn.equals("redflaggedOn")) {
					orderbyClause.append(" ORDER BY statusdate1");
				}else if(sortColumn.equals("associationName")) {
					orderbyClause.append(" ORDER BY b.asso_name");
				}else if(sortColumn.equals("state")) {
					orderbyClause.append(" ORDER BY state");
				}else if(sortColumn.equals("district")) {
					orderbyClause.append(" ORDER BY district");
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
			//	queryForPaging = query;
				queryForPaging = query.append("),t1 AS(SELECT * FROM temp WHERE rn=1 ), T2 AS (SELECT T1.*, ROWNUM RN1 FROM T1 WHERE ROWNUM<=?)"
						+ "SELECT * FROM T2 WHERE RN1>=? ");
			else
				queryForPaging = query.append("),t1 AS(SELECT * FROM temp WHERE rn=1 ), T2 AS (SELECT T1.*, ROWNUM RN1 FROM T1 WHERE ROWNUM<=?)"
						+ "SELECT * FROM T2 WHERE RN1>=? ");
				/*new StringBuffer(" WITH temp AS  (SELECT (row_number() over (partition BY a.rcn order by C.STATUS_DATE DESC)) AS rn ,   A.RCN,    B.ASSO_NAME,  "
						+ " (SELECT sname FROM tm_state WHERE scode=SUBSTR(b.stdist,1,2)),(SELECT distname FROM tm_district WHERE distcode=SUBSTR(b.stdist,-3,3) ),"
						+ " TO_CHAR(C.STATUS_DATE,'dd-mm-yyyy HH24:MI:SS') as statusdate, C.STATUS_DATE as statusdate1, C.ACTION_BY ||'('|| e.user_name  ||')' ,"
						+ " (SELECT CATEGORY_DESC  FROM tm_red_flag_category  WHERE CATEGORY_CODE=c.red_flag_category), "
						+ "( CASE  WHEN c.FLAG_TYPE ='2' THEN 'Yellow' ELSE  CASE WHEN c.FLAG_TYPE='1'  THEN 'Red' END END ) AS catogery_type"
						+ "  FROM T_RED_FLAGGED_ASSOCIATIONS A,  fc_india B, t_red_flag_status_history C ,  TM_User e  WHERE c.status =0  AND"
						+ " A.RCN   =B.RCN  AND A.RCN      =C.RCN  AND c.action_by=E.USER_id ),t1 as(SELECT * FROM temp WHERE rn=1  ),"
						+ "T2 AS (SELECT T1.*, ROWNUM RN1 FROM T1 WHERE ROWNUM<=?)	SELECT * FROM T2 WHERE RN1>=?");*/
			return queryForPaging.toString();
		}
	
	@Override
	public Integer insertRecord(RedFlaggedRcns object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(RedFlaggedRcns object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RedFlaggedRcns> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<RedFlaggedRcns> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	//Searching 
	
	
	public List<AbstractRequest> getApplicationListDetails() throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		String queryField=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM (SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS,A.STDIST FROM FC_INDIA A )  A  WHERE RCN=?");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		statement.setString(1, appId);		
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}		
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery1(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {			
				statement.setString(1, appId);
				statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(3, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		String statusDesc=null;
		while(rs.next()) {	
			if(rs.getString(3)==null || rs.getString(3).equals("") || rs.getString(3).equals("0"))
				statusDesc="ALIVE";
			else if(rs.getString(3).equals("1"));
				statusDesc="CANCELLED";
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),"",rs.getString(3),statusDesc,"","","","","","","",""));			
		}
		return applicationList;
	}
	private String preparePagingQuery1(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("applicationId")) {
				orderbyClause.append(" ORDER BY APPLICATION_ID");
			}else if(sortColumn.equals("tempFileNo")) {
				orderbyClause.append(" ORDER BY TEMP_FILENO");
			}else if(sortColumn.equals("sectionFileNo")) {
				orderbyClause.append(" ORDER BY SECTION_FILENO");
			}else if(sortColumn.equals("submissionDate")) {
				orderbyClause.append(" ORDER BY SUBMISSION_DATE");
			}else if(sortColumn.equals("serviceName")) {
				orderbyClause.append(" ORDER BY SERVICE_CODE");
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
	private String preparePagingQuery2(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("associationName")) {
				orderbyClause.append(" ORDER BY ASSO_NAME");
			}else if(sortColumn.equals("state")) {
				orderbyClause.append(" ORDER BY state_name");
			}else if(sortColumn.equals("district")) {
				orderbyClause.append(" ORDER BY district_name");
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
			queryForPaging =query;
		return queryForPaging.toString();
	}
	public List<AbstractRequest> getAdvanceApplicationListDetails(String flag) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		StringBuffer queryField=null;
		Boolean searchStringFlag=false,stateFlag=false,stateDistrictFlag=false,functionary=false;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(!(searchString==null || searchString.equals(""))){			
			queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%')");
			searchStringFlag=true;
		}	
		if(!(functionaryName==null || functionaryName.equals(""))){	
			if(searchStringFlag==true)
				queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%'))");
			else
				queryField=new StringBuffer("A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%'))");
			functionary=true;
		}	
		if(!(state==null || state.equals(""))){	
			if(searchStringFlag==true){
				if(functionary==true)
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND SUBSTR(A.STDIST,0,2)=?");
				else
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND SUBSTR(A.STDIST,0,2)=?");
			}
			else{
				if(functionary==true)
					queryField=new StringBuffer(" A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND SUBSTR(A.STDIST,0,2)=? ");
				else
					queryField=new StringBuffer(" SUBSTR(A.STDIST,0,2)=?");
			}				
			stateFlag=true;			
		}	
		if(!(state==null || state.equals("")) && !(district==null || district.equals(""))){	
			if(searchStringFlag==true){
				if(functionary==true)
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND A.STDIST=?");
				else
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.STDIST=?");
			}				
			else{
				if(functionary==true)
					queryField=new StringBuffer(" A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND  A.STDIST=?");
				else				
					queryField=new StringBuffer(" A.STDIST=?");
			}			
			stateDistrictFlag=true;		
			stateFlag=false;		
		}		
		if(flag.equals("0")){			// Registered
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM (SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS,A.STDIST FROM FC_INDIA A) A WHERE "+queryField+"");
		}else if(flag.equals("1")){		// PP
			//query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM (SELECT A.RCN,A.ASSO_NAME,null,A.STDIST FROM FC_PP_INDIA A ) A  WHERE "+queryField+"");
		}else{							// ALL
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM "
					+ "(SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS,A.STDIST FROM FC_INDIA A ) A  "
					+ "WHERE "+queryField+"");
		}		
				
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(searchStringFlag==false){
			if(functionary==false){
				if(stateFlag==false){
					if(stateDistrictFlag==false){					
					}else{
						statement.setString(1, state+district);
					}
				}else{
					statement.setString(1, state);
				}
			}else{
				if(stateFlag==false){
					if(stateDistrictFlag==false){	
						statement.setString(1, functionaryName);
					}else{
						statement.setString(1, functionaryName);
						statement.setString(2, state+district);
					}
				}else{
					statement.setString(1, functionaryName);
					statement.setString(2, state);
				}
			}			
		}else{
			if(functionary==false){
				if(stateFlag==false){
					if(stateDistrictFlag==false){
						statement.setString(1, searchString);
					}else{
						statement.setString(1, searchString);
						statement.setString(2, state+district);
					}
				}else{
					statement.setString(1, searchString);
					statement.setString(2, state);
				}
			}else{
				if(stateFlag==false){
					if(stateDistrictFlag==false){
						statement.setString(1, searchString);
						statement.setString(2, functionaryName);
					}else{
						statement.setString(1, searchString);
						statement.setString(2, functionaryName);
						statement.setString(3, state+district);
					}
				}else{
					statement.setString(1, searchString);
					statement.setString(2, functionaryName);
					statement.setString(3, state);
				}
			}			
		}
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}		
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery1(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			if(searchStringFlag==false){
				if(functionary==false){
					if(stateFlag==false){
						if(stateDistrictFlag==false){	
							statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(2, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, state+district);
							statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(3, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, state);
						statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(3, (pageRequested-1) * pageSize + 1);
					}
				}else{
					if(stateFlag==false){
						if(stateDistrictFlag==false){	
							statement.setString(1, functionaryName);
							statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(3, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, functionaryName);
							statement.setString(2, state+district);
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, functionaryName);
						statement.setString(2, state);
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}
				}			
			}else{
				if(functionary==false){
					if(stateFlag==false){
						if(stateDistrictFlag==false){
							statement.setString(1, searchString);
							statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(3, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, searchString);
							statement.setString(2, state+district);
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, searchString);
						statement.setString(2, state);
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}
				}else{
					if(stateFlag==false){
						if(stateDistrictFlag==false){
							statement.setString(1, searchString);
							statement.setString(2, functionaryName);
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, searchString);
							statement.setString(2, functionaryName);
							statement.setString(3, state+district);
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, searchString);
						statement.setString(2, functionaryName);
						statement.setString(3, state);
						statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(5, (pageRequested-1) * pageSize + 1);
					}
				}			
			}			
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		String statusDesc=null;
		while(rs.next()) {	
			if(rs.getString(3)==null || rs.getString(3).equals("") || rs.getString(3).equals("0"))
				statusDesc="ALIVE";
			else if(rs.getString(3).equals("1"));
				statusDesc="CANCELLED";
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),"",rs.getString(3),statusDesc,"","","","","","","","s"));			
		}
		return applicationList;
	}
	public void removeFromRedFlagList(String appId,String remark,String user,String myOfficeCode) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		//connection.setAutoCommit(false);
		query = new StringBuffer("DELETE FROM T_BLUE_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		statement.executeUpdate();	
		statement.close();
		
		
		query = new StringBuffer("INSERT INTO T_BLUE_FLAG_STATUS_HISTORY(RCN,STATUS,REMARKS,STATUS_DATE,ACTION_BY,ACTION_BY_OFFICE) "
				+ " VALUES(?,?,?,systimestamp,?,?)");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		statement.setString(2, "1");
		statement.setString(3, remark);
		statement.setString(4, user);
		statement.setString(5, myOfficeCode);
		statement.executeUpdate();		
		//connection.commit();
		statement.close();
		
		ResultSet rs=null;
		query = new StringBuffer("SELECT USER_ID||'(' ||USER_NAME || ')[' ||(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=TM_USER.DESIGNATION_ID) "
				+ "|| ']' AS USERNAME  FROM TM_USER WHERE USER_ID=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, user);
		rs=statement.executeQuery();
		String userInfo=null;
		if(rs.next()){
			userInfo=rs.getString(1);
		}
		rs.close();
		statement.close();
		
		query = new StringBuffer("SELECT EMAIL_ID,to_char(sysdate,'dd-mm-yyyy hh24:mi:ss') FROM TM_OFFICE WHERE OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, myOfficeCode);
		rs=statement.executeQuery();
		String email=null,time=null;
		if(rs.next()){
			email=rs.getString(1);
			time=rs.getString(2);
		}
		rs.close();
		statement.close();
		
		query = new StringBuffer("SELECT ASSO_NAME,CASE WHEN NEW_OLD='N' THEN ASSO_ADDRESS ELSE ADD1||ADD2||ADD3||' - '||ASSO_PIN END FROM FC_INDIA WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		rs=statement.executeQuery();
		String assoName=null,assoAdd=null;
		if(rs.next()){
			assoName=rs.getString(1);
			assoAdd=rs.getString(2);
		}
		rs.close();
		statement.close();
		
		// Sending Mail to JSF	 	
		String mailContent="This is to inform you that the following association has been removed from adverse list by <b>"+userInfo+"</b> on <b>"+time+".</b><br/><br/>"
				+ "Asoociation Name: <b>"+assoName+"</b><br/> RCN: <b>"+appId+"</b> <br/> Address: <b>"+assoAdd+"</b> <br/> Remark: <b>"+remark+"</b> <br/><br/> "
				+ "Note:This Email is system generated. Please do not reply to this email ID";		
		MailScheduler.schedule("no-reply-MHAFCRA@nic.in", email, "Red Flag Entry Removal - Intimation under FCRA", mailContent, null, null, null, connection);
	} 
	public Boolean checkAddbLUEFlagRole(String userId) throws Exception{
		Boolean status=false;
		// Checking Logged in User having Red Flag Add Role
		StringBuffer query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=14 AND RECORD_STATUS = 0");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, userId);		
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			status=true;
		}
		return status;
	}

	 public Boolean checkRemoveBlueFlagRole(String userId,String appId) throws Exception{
			Boolean status=false;StringBuffer query=null;PreparedStatement statement=null;ResultSet rs=null;		
			
				// Checking Logged in User having Red Flag Yellow Remove Role
				query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=14 and RECORD_STATUS=0");
				statement = connection.prepareStatement(query.toString());	
				statement.setString(1, userId);		
				rs=statement.executeQuery();
				if(rs.next()){
					status=true;
				}
				rs.close();
				statement.close();
				return status;
			}
				
		
	
	public void getApplicationDetails(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
		if(appId.substring(appId.length()-1,appId.length()).equals("R")){
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,B.NEW_OLD,(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=B.BANK_NAME),B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
					+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,to_char(A.REG_DATE,'dd-mm-yyyy'),(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=A.ASSO_RELIGION),"
					+ "to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),to_char(VALID_TO,'dd-mm-yyyy'), case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired "
					+ "FROM FC_INDIA A LEFT JOIN FC_BANK B ON A.RCN=B.RCN AND B.STATUS='Y' WHERE A.RCN=?");
		}else if(appId.substring(appId.length()-1,appId.length()).equals("P")){
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,null,null,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,null,null,null,null,null,null,null,null,to_char(A.PP_DATE,'dd-mm-yyyy'),(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=A.ASSO_RELIGION),null,null,null FROM FC_PP_INDIA A  WHERE A.RCN=?");
		}				
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		rs=statement.executeQuery();	
		String statusDesc=null;
		String assoNewOldFlag=null;
		String bankNewOldFlag=null;
		String assoAddress=null;
		String bankAdress=null;
		String assoNature=null;
		String assoReligion=null;
		String assoNatureDesc="";
		String assoStatus=null;
		if(rs.next()) {
			
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} 
			else if(rs.getString(4) != null && rs.getString(4).equals("3")) {
				statusDesc="SUSPENDED";
			}else if(rs.getString(24) != null && rs.getString(24).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else {
				statusDesc="ALIVE";
			}
			
			
			
			assoNewOldFlag=rs.getString(10);
			if(assoNewOldFlag.equals("N")){
				assoAddress=rs.getString(5)+", "+rs.getString(6);
			}else if(assoNewOldFlag.equals("O")){
				assoAddress=rs.getString(7)+", "+rs.getString(8)+", "+rs.getString(9);
			}
			bankNewOldFlag=rs.getString(12);			
			if(bankNewOldFlag != null) {
				if(bankNewOldFlag.equals("N")){
					bankAdress=rs.getString(14)+", "+rs.getString(15);
				}else if(bankNewOldFlag.equals("O")){
					bankAdress=rs.getString(16)+", "+rs.getString(17)+", "+rs.getString(18);
				}
			}
			assoNature=rs.getString(11);
			assoReligion=rs.getString(21);			
			if(assoNature != null) {	 			
	 			StringBuffer query1 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
	 			PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 		for(int j=0;j<assoNature.length();j++){
		 			Boolean religious=false;
		 			String natureCode = assoNature.substring(j, j+1);
		 			if(natureCode.equals("1")){
		 				religious=true;
		 			}
		 			statement1.setString(1, natureCode);
		 			String delim=(j==0 ? "" : ",");
		 			ResultSet rs5 = statement1.executeQuery();
		 			if(rs5.next()){		 				
		 				assoNatureDesc=assoNatureDesc + delim + rs5.getString(1);
		 				if(religious==true){
			 				   assoNatureDesc=assoNatureDesc+" ("+assoReligion+")";
			 			}
		 			}
		 			rs5.close();
		 		}
		 		statement1.close();
			}		   
		  applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),statusDesc,assoAddress,
				   assoNatureDesc,rs.getString(13),bankAdress,rs.getString(20),rs.getString(19),rs.getString(22),rs.getString(23)));
		  
		  // Getting Cancellation Details
		  if(assoStatus!=null && assoStatus.equals("1")){
			  query=new StringBuffer("with t3 as (select rcn, status,status_date, remarks, REFERENCE_FOR_DETAILS from (select a.*, row_number() over (partition by rcn order by status_date desc ) rn "
			  		+ "from T_REGISTRATION_STATUS_HISTORY a) where rn=1 ),t4 as (select a.rcn,status,status_date,remarks,case when CANCELLATION_TYPE='V' then 'Violation' else case when CANCELLATION_TYPE='R' "
			  		+ "then 'On Request' else null end end as cancel_type,(select listagg(REASON_DESC,'; ') within group(order by REASON_ID) from TM_REGN_CANCELLATION_REASONS where REASON_ID in "
			  		+ "(select regexp_substr(b.CANCELLATION_REASON, '(.{1,2})', 1, level) from dual connect by level <= ceil(length(b.CANCELLATION_REASON)/2))) reason from t3 A left join "
			  		+ "T_REGN_CANCELLATION_DETAILS b on a.REFERENCE_FOR_DETAILS = b.REFERENCE_FOR_DETAILS) select to_char(t4.status_date,'dd-mm-yyyy') as cancelled_date,  t4.cancel_type, "
			  		+ "t4.remarks as cancelled_remarks,t4.reason from  t4 where rcn=?");
			  statement = connection.prepareStatement(query.toString());	
			  statement.setString(1, appId);		  	
			  rs=statement.executeQuery();		   
			  if(rs.next()){
				  regCancDetails.add(new List4(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			  }
		  }
		}else{			
			throw new ValidationException("Registration Number <b>"+appId+"</b> doesn't exist.Please search with valid registration number.");
		}	
		rs.close();
		statement.close();
		
		// Checking whether in adverse list		
		query=new StringBuffer("SELECT * FROM T_BLUE_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			blueFlag="YES";
		}else{
			blueFlag="NO";
		}
		
		query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=14  AND RECORD_STATUS = 0");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, myUserId);		
		rs=statement.executeQuery();
		if(rs.next()){
			blueFlagRemoveRole="YES";
		}else{
			blueFlagRemoveRole="NO";
		}
		
		
		//if(blueFlag.equals("YES") && (blueFlagRemoveRole.equals("YES"))){
			query=new StringBuffer("SELECT REMARKS,to_char(STATUS_DATE,'dd-mm-yyyy'),ACTION_BY || ' [' || (SELECT USER_NAME FROM TM_USER WHERE USER_ID=ACTION_BY) || ' ]'"
					+ " FROM T_Blue_FLAG_STATUS_HISTORY WHERE RCN=? "
					+ "AND STATUS_DATE=(SELECT MAX(STATUS_DATE) FROM T_Blue_FLAG_STATUS_HISTORY WHERE RCN=?) ");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, appId);	
			statement.setString(2, appId);
			rs=statement.executeQuery();
			if(rs.next()){
				blueFlagDetailsList.add(new List3(rs.getString(1), rs.getString(2), rs.getString(3)));
			//}
		}
		
		// Return Details
		query=new StringBuffer("WITH T1 AS ("
				+ "SELECT DISTINCT FT.BLKYEAR, "
				+ "NVL(SUM(FP3.AMOUNT),0) AS AMOUNT FROM FC_INDIA FI,FC_FC3_PART3 FP3,FC_FC3_TALLY FT "
				+ "WHERE (FI.REG_DATE IS NULL OR TRUNC(FI.REG_DATE) < TRUNC(TO_DATE('01-04-'||SUBSTR(FT.BLKYEAR,-4,4),'DD-MM-YYYY'))) AND (FI.STATUS <> 'D')   AND FI.RCN=FP3.RCN "
				+ "AND 1=1 AND SUBSTR(FP3.RCN,-1,1)='R' AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y'  AND FT.RCN = FP3.RCN AND FT.RCN=? "
				+ "GROUP BY FT.BLKYEAR "
				+ "UNION ALL "
				+ "SELECT DISTINCT FT.BLKYEAR, "
				+ "NVL(SUM(FP3.AMOUNT),0) AS AMOUNT  FROM FC_PP_INDIA FPI,FC_FC3_PART3 FP3,FC_FC3_TALLY FT "
				+ "WHERE FPI.RCN=FP3.RCN  AND FP3.BLKYEAR=FPI.BLKYEAR AND 1=1 "
				+ "AND SUBSTR(FP3.RCN,-1,1)='P' AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y'  AND FT.RCN = FP3.RCN AND FT.RCN=? "
				+ "GROUP BY FT.BLKYEAR "
				+ "UNION ALL "
				+ "SELECT DISTINCT FT.BLKYEAR, NVL(SUM(FP3.AMOUNT),0) AS AMOUNT FROM FC_INDIA FI,FC_FC3_DONOR_WISE FP3,FC_FC3_TALLY FT, FC_FC3_DONOR FD  "
				+ "WHERE (FI.REG_DATE IS NULL OR TRUNC(FI.REG_DATE) < TRUNC(TO_DATE('01-04-'||SUBSTR(FT.BLKYEAR,-4,4),'DD-MM-YYYY'))) AND (FI.STATUS <> 'D')   "
				+ "AND FI.RCN=FP3.RCN    AND 1=1 AND SUBSTR(FP3.RCN,-1,1)='R'  AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y' AND FT.RCN = FP3.RCN AND FT.RCN=?  "
				+ "AND FI.RCN=FD.RCN  AND FT.BLKYEAR=FP3.BLKYEAR AND FP3.DONOR_CODE=FD.DONOR_CODE GROUP BY FT.BLKYEAR "
				+ "UNION ALL "
				+ "SELECT DISTINCT FP3.BLKYEAR, NVL(SUM(FP3.AMOUNT),0) AS AMOUNT  FROM FC_PP_INDIA FPI,FC_FC3_DONOR_WISE FP3,FC_FC3_TALLY FT,FC_FC3_DONOR FD  WHERE  "
				+ "FPI.RCN=FP3.RCN  AND FP3.BLKYEAR=FPI.BLKYEAR  AND 1=1 AND SUBSTR(FP3.RCN,-1,1)='P'  AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y'  "
				+ "AND FT.RCN = FP3.RCN AND FT.RCN=?  AND FPI.RCN=FD.RCN  AND FT.BLKYEAR=FP3.BLKYEAR  AND FP3.DONOR_CODE=FD.DONOR_CODE GROUP BY FP3.BLKYEAR )"
				+ ", T2 AS ( "
				+ "SELECT FP.BLKYEAR,FOR_AMT+BK_INT+OTH_INT AS TOTAMOUNT,FOR_AMT,(BK_INT+OTH_INT) INTEREST,0 AS LOCAL_AMT, TO_CHAR(FINAL_SUBMISSION_DATE, 'DD-MM-YYYY'),"
				+ "FT.UNIQUE_FILENO FROM FC_FC3_PART1 FP,FC_FC3_TALLY FT WHERE FP.RCN=? AND FT.RCN=FP.RCN AND FP.BLKYEAR=FT.BLKYEAR AND FP.BLKYEAR NOT IN ('2005-2006') "
				+ "AND FT.FINAL_SUBMIT='Y' "
				+ "UNION SELECT FP.BLKYEAR,BK_INT+SOURCE_FOR_AMT+SOURCE_LOCAL_AMT AS AMOUNT,SOURCE_FOR_AMT,BK_INT,SOURCE_LOCAL_AMT, TO_CHAR(FINAL_SUBMISSION_DATE, 'DD-MM-YYYY'),"
				+ "FT.UNIQUE_FILENO FROM FC_FC3_PART1_NEW FP,FC_FC3_TALLY FT WHERE FP.RCN=? AND FT.RCN=FP.RCN AND FP.BLKYEAR=FT.BLKYEAR AND FP.BLKYEAR NOT IN ('2005-2006') "
				+ "AND FT.FINAL_SUBMIT='Y')"
				+ " SELECT T2.*,NVL(T1.AMOUNT,0) DONORWISEAMT, (T2.TOTAMOUNT-NVL(T1.AMOUNT,0)) DIFF FROM T2 LEFT JOIN T1 ON T2.BLKYEAR=T1.BLKYEAR ORDER BY T2.BLKYEAR DESC");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		statement.setString(2, appId);
		statement.setString(3, appId);		
		statement.setString(4, appId);
		statement.setString(5, appId);		
		statement.setString(6, appId);
		rs=statement.executeQuery();
		while(rs.next()){
			String date=null;
			if(rs.getString(6)==null)
				date="";
			else
				date=rs.getString(6);
			String amount=null;
			if(rs.getString(2)==null)
				amount="";
			else
				amount=rs.getString(2);
			returnList.add(new List9(rs.getString(1),amount,rs.getString(3),rs.getString(4),rs.getString(5),date,rs.getString(7),rs.getString(8),
					rs.getString(9)));
		}			
		// Committee Members Details		
		 query = new StringBuffer("SELECT NAME, FATHER_HUSBAND_NAME, NATIONALITY, "
				+ "(SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=NATIONALITY), "
				+ "AADHAAR, OCCUPATION, CASE WHEN OCCUPATION=99 THEN  (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=OCCUPATION) || ' (' ||OCCUPATION_OTHER || ')' ELSE  (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=OCCUPATION) END OCC, OCCUPATION_OTHER, "
				+ "CASE WHEN OLD_NEW='O' THEN OFFICE_OF_ASSO ELSE CASE WHEN OFFICE_OF_ASSO=99 THEN  (SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE to_char(DESIG_CODE)=OFFICE_OF_ASSO) || ' (' ||OFFICE_OF_ASSO_OTHER || ')' ELSE  (SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE to_char(DESIG_CODE)=OFFICE_OF_ASSO) END END DESIG, "
				+ "OFFICE_OF_ASSO_OTHER, CASE WHEN OLD_NEW='O' THEN BEARERS_RELATIONSHIP ELSE CASE WHEN BEARERS_RELATIONSHIP=99 THEN  (SELECT RELATION_NAME FROM TM_COMMITTEE_RELATION WHERE to_char(RELATION_CODE)=BEARERS_RELATIONSHIP) || ' (' ||BEARERS_RELATIONSHIP_OTHER || ')' ELSE  (SELECT RELATION_NAME FROM TM_COMMITTEE_RELATION WHERE to_char(RELATION_CODE)=BEARERS_RELATIONSHIP) END END RELN, "
				+ "BEARERS_RELATIONSHIP_OTHER, ADDRESS_OF_ASSO, "
				+ "ADDRESS_OF_RESID, EMAIL_ID, LANDLINE, MOBILE, MOBILE_CTR_CODE, TO_CHAR(DOB, 'dd-mm-yyyy'), PLACE_BIRTH, PASSPORT_NO, PER_ADD_FC, "
				+ "CASE WHEN WHETHER_IN_YN IS NULL THEN '' ELSE CASE WHEN WHETHER_IN_YN='Y' THEN 'Yes' ELSE 'No' END END, CASE WHEN WHETHER_IN_YN IS NULL THEN '' ELSE CASE WHEN WHETHER_IN_YN='Y' THEN PIO_NO ELSE '' END END, CASE WHEN RES_INDIA_STATUS IS NULL THEN '' ELSE CASE WHEN RES_INDIA_STATUS='Y' THEN 'Yes' ELSE 'No' END END, TO_CHAR(RES_INDIA_DATE, 'dd-mm-yyyy') "
				+ "FROM fc_committee WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, appId);
		rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			CommitteeMember member = new CommitteeMember();			
			member.setName(rs.getString(i++));
			member.setNameOfFatherSpouse(rs.getString(i++));
			member.setNationality(rs.getString(i++));
			member.setNationalityDesc(rs.getString(i++));
			//nationalityOther;
			member.setAadhaarNumber(rs.getString(i++));
			member.setOccupation(rs.getString(i++));
			member.setOccupationDesc(rs.getString(i++));
			member.setOccupationOther(rs.getString(i++));
			member.setDesignationInAssociation(rs.getString(i++));
			//member.setDesignationInAssociationDesc(rs.getString(i++));
			member.setDesignationInAssociationOther(rs.getString(i++));
			member.setRelationWithOfficeBearers(rs.getString(i++));
			//member.setRelationWithOfficeBearersDesc(rs.getString(i++));
			member.setRelationWithOfficeBearersOther(rs.getString(i++));
			member.setOfficeAddress(rs.getString(i++));
			member.setResidenceAddress(rs.getString(i++));
			member.setEmail(rs.getString(i++));
			member.setPhoneNumber(rs.getString(i++));
			String mobile = rs.getString(i++);
			String mobileCode = rs.getString(i++);
			member.setMobile(mobileCode+"-"+mobile);
			
			member.setDateOfBirth(rs.getString(i++));
			member.setPlaceOfBirth(rs.getString(i++));
			member.setPassportNumber(rs.getString(i++));
			member.setAddressInForeignCountry(rs.getString(i++));
			member.setPersonOfIndianOrigin(rs.getString(i++));
			member.setPioOciCardNumber(rs.getString(i++));
			member.setResidentInIndia(rs.getString(i++));
			member.setDateFromWhichResidingInIndia(rs.getString(i++));
			committeeMembers.add(member);
		}		
		// History
		query=new StringBuffer("SELECT APPLICATION_ID,TO_CHAR(SUBMISSION_DATE,'DD-MM-YYYY'),"
								+ " (SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=V.SERVICE_CODE) SERVICE,"
								+ " (SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=V.CURRENT_STATUS) STATUS"
								+ " FROM V_APPLICATION_DETAILS V WHERE APPLICATION_ID IN (SELECT UNIQUE_FILENO FROM ("
								+ " SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN "
								+ " FROM FC_FC5_ENTRY_NEW1 UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM UNION"
								+ " SELECT UNIQUE_FILENO, RCN FROM T_FC8_ENTRY A, FC_INDIA B WHERE A.SECTION_FILENO=B.SECTION_FILENO ) "
								+ " WHERE UPPER(RCN)=UPPER('"+appId+"')) ORDER BY SUBMISSION_DATE");
		statement = connection.prepareStatement(query.toString());		
		rs = statement.executeQuery();		
		while(rs.next()){
			historyList.add(new List4(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
		}
		// History	
		
		getCommunicationDetailsWithApplicant(appId);
	}
	
	private void getCommunicationDetailsWithApplicant(String rcn) throws Exception{
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
		// MAIL
		query=new StringBuffer("with t1 as (SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN  FROM FC_FC5_ENTRY_NEW1 UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM UNION SELECT UNIQUE_FILENO, RCN FROM T_FC8_ENTRY A,"
				+ " FC_INDIA B WHERE A.SECTION_FILENO=B.SECTION_FILENO), t2 as (select a.application_id, mail_id,sent_on from t_application_automail_details a WHERE APPLICATION_ID IN "
				+ "(SELECT UNIQUE_FILENO FROM t1 WHERE UPPER(RCN)=UPPER(?)) union select a.application_id, mail_id,sent_on from t_application_automail_details a WHERE "
				+ "APPLICATION_ID =UPPER(?) ) SELECT A.mail_id,A.TO_ADDRESS,A.MAIL_SUBJECT,A.MAIL_BODY,to_char(B.SENT_ON,'dd-mm-yyyy hh24:mi:ss') as sent_on, CASE WHEN A.STATUS_ID=0 THEN 'PENDING' WHEN "
				+ "A.STATUS_ID=1 THEN 'SENT' ELSE 'ERROR' END STATUS ,to_char(A.STATUS_DATE,'dd-mm-yyyy hh24:mi:ss') as statusdate,(SELECT  listagg(DOCUMENT_NAME,',') within group (order by document_name)"
				+ " FROM T_DOCUMENT_DETAILS WHERE DOCUMENT_ID in ( select DOCUMENT_ID FROM T_MAIL_ATTACHMENT_DETAILS WHERE MAIL_ID=A.MAIL_ID))"
				+ " ATTACHMENT from T_MAIL_DETAILS A, t2 B WHERE B.MAIL_ID=A.MAIL_ID order by status_date desc,SENT_ON desc");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, rcn);
			statement.setString(2, rcn);
			rs = statement.executeQuery();		
			while(rs.next()){
				mailList.add(new List8(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
			}
			rs.close();
			statement.close();
			
		// SMS
		query=new StringBuffer("with t1 as (SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN FROM FC_FC5_ENTRY_NEW1 UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM UNION SELECT UNIQUE_FILENO, RCN FROM T_FC8_ENTRY A, "
				+ "FC_INDIA B WHERE A.SECTION_FILENO=B.SECTION_FILENO ), t2 as (select a.application_id, sms_id,sent_on from t_application_autosms_details a WHERE APPLICATION_ID IN "
				+ "(SELECT UNIQUE_FILENO FROM t1 WHERE UPPER(RCN)=UPPER(?))   union  select a.application_id, sms_id,sent_on from t_application_autosms_details a "
				+ "WHERE APPLICATION_ID =UPPER(?) ) SELECT A.TO_ADDRESS,A.MESSAGE,to_char(B.SENT_ON,'dd-mm-yyyy hh24:mi:ss'),CASE WHEN A.STATUS_ID=0 THEN 'PENDING' WHEN A.STATUS_ID=1 THEN 'SENT' ELSE 'ERROR' END STATUS ,"
				+ "to_char(A.STATUS_DATE,'dd-mm-yyyy hh24:mi:ss') as statusdate FROM T_SMS_DETAILS A, t2 B WHERE A.SMS_ID IN(SELECT SMS_ID FROM T2) order by A.STATUS_DATE desc,sent_on desc");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, rcn);
			statement.setString(2, rcn);
			rs = statement.executeQuery();		
			while(rs.next()){
				smsList.add(new List5(new StringBuffer(rs.getString(1)),new StringBuffer(rs.getString(2)),new StringBuffer(rs.getString(3)),new StringBuffer(rs.getString(4)),new StringBuffer(rs.getString(5))));
			}
			rs.close();
			statement.close();
	}



	public int blueremoveRole(String userId) throws SQLException{
		StringBuffer query = new StringBuffer("SELECT  ROLE_ID FROM TM_USER_ROLE where USER_ID=? AND  ROLE_ID=14 and RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, userId);
		ResultSet rs=statement.executeQuery();
		int removeRoleId=0;
		if(rs.next()){
			removeRoleId=rs.getInt(1);
		}
	
	  return removeRoleId;	
	}
	public int blueroleRole(String userId) throws SQLException{
		StringBuffer query = new StringBuffer("SELECT  ROLE_ID FROM TM_USER_ROLE where USER_ID=? AND  ROLE_ID=14 and RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, userId);
		ResultSet rs=statement.executeQuery();
		int removeRoleId=0;
		if(rs.next()){
			removeRoleId=rs.getInt(1);
		}
	
	  return removeRoleId;	
	}
	public String getRecordsPerPage() {
		return recordsPerPage;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}


	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getSearchString() {
		return searchString;
	}


	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}


	public String getFunctionaryName() {
		return functionaryName;
	}


	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}


	public String getSearchFlag() {
		return searchFlag;
	}


	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getDistrict() {
		return district;
	}


	public void setDistrict(String district) {
		this.district = district;
	}





	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}


	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}


	public List<List9> getReturnList() {
		return returnList;
	}


	public void setReturnList(List<List9> returnList) {
		this.returnList = returnList;
	}


	public List<List4> getHistoryList() {
		return historyList;
	}


	public void setHistoryList(List<List4> historyList) {
		this.historyList = historyList;
	}


	public List<List8> getMailList() {
		return mailList;
	}


	public void setMailList(List<List8> mailList) {
		this.mailList = mailList;
	}


	public List<List5> getSmsList() {
		return smsList;
	}


	public void setSmsList(List<List5> smsList) {
		this.smsList = smsList;
	}





	public List<CommitteeMember> getCommitteeMembers() {
		return committeeMembers;
	}


	public void setCommitteeMembers(List<CommitteeMember> committeeMembers) {
		this.committeeMembers = committeeMembers;
	}


	public List<Notification> getNotifyList() {
		return notifyList;
	}


	public void setNotifyList(List<Notification> notifyList) {
		this.notifyList = notifyList;
	}


	public List<List4> getRegCancDetails() {
		return regCancDetails;
	}


	public void setRegCancDetails(List<List4> regCancDetails) {
		this.regCancDetails = regCancDetails;
	}

	public List<List3> getBlueFlagDetailsList() {
		return blueFlagDetailsList;
	}

	public void setBlueFlagDetailsList(List<List3> blueFlagDetailsList) {
		this.blueFlagDetailsList = blueFlagDetailsList;
	}

	public String getBlueFlag() {
		return blueFlag;
	}

	public void setBlueFlag(String blueFlag) {
		this.blueFlag = blueFlag;
	}

	public String getBlueFlagRemoveRole() {
		return blueFlagRemoveRole;
	}

	public void setBlueFlagRemoveRole(String blueFlagRemoveRole) {
		this.blueFlagRemoveRole = blueFlagRemoveRole;
	}




}
