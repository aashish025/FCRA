package dao.services;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import utilities.KVPair;
import utilities.UserSession;
import utilities.lists.List1;
import utilities.lists.List3;
import utilities.lists.List4;
import utilities.lists.List5;

import models.reports.PendencyReport;
import models.services.Notification;
import dao.BaseDao;

public class NotificationDao extends BaseDao<Notification, String, String>{
	private String notificationType;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private MultipartFile attachment;
	private String sessionId;
	private String uploadId;
	
	
	
	public NotificationDao(Connection connection) {
		super(connection);
		
	}
	
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT NOTIFICATION_TYPE_ID,NOTIFICATION_NAME FROM TM_NOTIFICATION_TYPE WHERE NOTIFICATION_TYPE_ID NOT IN(0)");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  notificationTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			notificationTypeList.add(temp);			
		}
		return notificationTypeList;
	}
	
	@Override
	public List<Notification> getAll() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select message_title,message_details,to_char(ENTERED_ON,'dd-mm-yyyy hh24:mi:ss')"
				+ " from t_notification_details where notification_type_id=1 AND "
				+ "(trunc(SYSDATE)-trunc(ENTERED_ON))<=(select NOTICE_BOARD_DAYS FROM TM_DEFAULT_PARAMS)");
		PreparedStatement statement = connection.prepareStatement(query.toString());		
		ResultSet rs = statement.executeQuery();
		List<Notification> notificationList = new ArrayList<Notification>();
		while(rs.next()) {			
			notificationList.add(new Notification(rs.getString(1),rs.getString(2),rs.getString(3),"NEW",""));			
		}
		return notificationList;
	}
	public List<Notification> getSelectedNotification() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select message_title,message_details,to_char(ENTERED_ON,'dd-mm-yyyy hh24:mi:ss'),"+
							"case when (trunc(SYSDATE)-trunc(ENTERED_ON))<=(select NOTICE_BOARD_DAYS FROM TM_DEFAULT_PARAMS) then 'NEW' "+
							" else 'OLD' end,notification_id  from t_notification_details where notification_type_id=? order by ENTERED_ON desc");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, notificationType);		
		ResultSet rs = statement.executeQuery();
		List<Notification> notificationList = new ArrayList<Notification>();
		while(rs.next()) {			
			notificationList.add(new Notification(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));			
		}
		return notificationList;
	}
	public Map<String,String> getNewNotificationCountList() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select  (SELECT COUNT(*) FROM T_NOTIFICATION_DETAILS "+
							 " WHERE (trunc(SYSDATE)-trunc(ENTERED_ON))<=(select NOTICE_BOARD_DAYS FROM TM_DEFAULT_PARAMS) "+ 
							 " AND NOTIFICATION_TYPE_ID=1) as Application,(SELECT COUNT(*) FROM T_NOTIFICATION_DETAILS "+ 
							 " WHERE (trunc(SYSDATE)-trunc(ENTERED_ON))<=(select NOTICE_BOARD_DAYS FROM TM_DEFAULT_PARAMS)"+ 
							 " AND NOTIFICATION_TYPE_ID=2) as Circular,(SELECT COUNT(*) FROM T_NOTIFICATION_DETAILS "+
							 " WHERE (trunc(SYSDATE)-trunc(ENTERED_ON))<=(select NOTICE_BOARD_DAYS FROM TM_DEFAULT_PARAMS) "+ 
							 " AND NOTIFICATION_TYPE_ID=3) as Documents from dual");
		PreparedStatement statement = connection.prepareStatement(query.toString());		
		ResultSet rs = statement.executeQuery();
		Map<String,String> newNotificationCountList = new HashMap<String,String>();
		while(rs.next()) {			
			newNotificationCountList.put("A", rs.getString(1));		
			newNotificationCountList.put("C",rs.getString(2));
			newNotificationCountList.put("D",rs.getString(3));
		}
		return newNotificationCountList;
	}
	public List<KVPair<String, String>> getRcnBarGraphYearWise() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "with t as( "
				+ "select rcn, to_char(reg_date, 'yyyy') year, ROWNUM RN from fc_india), "
				+ "t1 as( "
				+ "select year, count(year)from t where year is not null group by year order by year desc) "
				+ "select t1.*,ROWNUM  from t1  WHERE ROWNUM<=10 order by ROWNUM desc";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  rcnBarGraphYearWise = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			rcnBarGraphYearWise.add(temp);			
		}
		return rcnBarGraphYearWise;	
	}
	public String getCurrentBlockYear() throws Exception {
		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-1+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		String currentBlockYear = null;
		while(rs.next()) {			
			currentBlockYear = rs.getString(1);
		}
		rs.close();
		statement.close();
		return currentBlockYear;
	}

	public List<String> getBlockYearList() throws Exception {
		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-10+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 where rownum <=10 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		List<String> blockYearList = new ArrayList<String>();
		//String currentBlockYear = null;
		while(rs.next()) {			
			blockYearList.add(rs.getString(1));
		}
		rs.close();
		statement.close();
		return blockYearList;
	}	
	
	public List<KVPair<String, String>> getDonorFinYearWiseBarGraph() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-10+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 where rownum <= 10 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		//List<String> blkYear = new ArrayList<String>();
		String currentBlockYear = null;
		String blockYear="";
		
		while(rs.next()) {			
			currentBlockYear = rs.getString(1);
			//blkYear.add(currentBlockYear);
			blockYear+="'"+currentBlockYear+"',";
		}
		blockYear=blockYear.substring(0,blockYear.length()-1);
		rs.close();
		statement.close();

		String query = " WITH T2 AS "
				+ " (SELECT * FROM ( select "
				+ " ft.blkyear as blkyear,fd.dname,fc.ctr_name,nvl(SUM(fp3.amount),0) AS amount "
				+ " from fc_inst_donor fd,tm_country fc,fc_fc3_part3 fp3,fc_fc3_tally ft "
				+ " WHERE fd.dcode = fp3.dcode  and fc.ctr_code = fp3.ctr_code  AND "
				+ " fp3.blkyear =ft.blkyear AND  ft.blkyear in(?)  and 1=1 "
				+ " and fp3.dtype ='1'  and ft.final_submit ='Y'  AND ft.rcn = fp3.rcn "
				+ " GROUP BY ft.blkyear,fp3.dcode,fd.dname,fc.ctr_name "
				+ " UNION ALL "
				+ " select    ft.blkyear as blkyear,fd.donor_name,fc.ctr_name, nvl(SUM(fp3.amount),0) AS amount "
				+ " FROM fc_fc3_donor fd,tm_country fc,fc_fc3_donor_wise fp3,fc_fc3_tally ft "
				+ " WHERE fd.donor_code = fp3.donor_code  and fc.ctr_code = fd.donor_country  AND fp3.blkyear = ft.blkyear "
				+ " AND 1=1  and fd.donor_type ='01'  and  ft.blkyear in(?)  and ft.final_submit ='Y'  AND ft.rcn = fp3.rcn "
				+ " GROUP BY ft.blkyear,fp3.donor_code,fd.donor_name,fc.ctr_name) order by amount desc), "
				+ " t3 as (select dname, amount, blkyear, row_number() over (PARTITION BY blkyear order by amount desc) as id from t2) "
				+ " select dname, amount, blkyear from t3 where id =1 ";
		
		statement = connection.prepareStatement(query);	
		statement.setString(1, blockYear);		
		statement.setString(2, blockYear);
		rs = statement.executeQuery();
		List<KVPair<String, String>>  donorFinYearWiseBarGraph = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			donorFinYearWiseBarGraph.add(temp);			
		}
		return donorFinYearWiseBarGraph;	
	}
	
	public List<KVPair<String, String>> getDonorWiseBarGraph() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-1+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 where rownum = 1 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		String currentBlockYear = null;
		while(rs.next()) {			
			currentBlockYear = rs.getString(1);
		}
		rs.close();
		statement.close();
		String query = " WITH T2 AS "
				+ " (SELECT * FROM ( select "
				+ " ft.blkyear,fd.dname,fc.ctr_name,nvl(SUM(fp3.amount),0) AS amount "
				+ " from fc_inst_donor fd,tm_country fc,fc_fc3_part3 fp3,fc_fc3_tally ft "
				+ " WHERE fd.dcode = fp3.dcode  and fc.ctr_code = fp3.ctr_code  AND "
				+ " fp3.blkyear =ft.blkyear AND  ft.blkyear in(?)  and 1=1 "
				+ " and fp3.dtype ='1'  and ft.final_submit ='Y'  AND ft.rcn = fp3.rcn "
				+ " GROUP BY ft.blkyear,fp3.dcode,fd.dname,fc.ctr_name "
				+ " UNION ALL "
				+ " select    ft.blkyear,fd.donor_name,fc.ctr_name, nvl(SUM(fp3.amount),0) AS amount "
				+ " FROM fc_fc3_donor fd,tm_country fc,fc_fc3_donor_wise fp3,fc_fc3_tally ft "
				+ " WHERE fd.donor_code = fp3.donor_code  and fc.ctr_code = fd.donor_country  AND fp3.blkyear = ft.blkyear "
				+ " AND 1=1  and fd.donor_type ='01'  and  ft.blkyear in(?)  and ft.final_submit ='Y'  AND ft.rcn = fp3.rcn "
				+ " GROUP BY ft.blkyear,fp3.donor_code,fd.donor_name,fc.ctr_name) order by amount desc), "
				+ " T4 as (select dname, amount from t2 where rownum<='10'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=10) SELECT * FROM T3 WHERE RN>=1 ";
		
		statement = connection.prepareStatement(query);	
		statement.setString(1, currentBlockYear);
		statement.setString(2, currentBlockYear);
		rs = statement.executeQuery();
		List<KVPair<String, String>>  donorWiseBarGraph = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			donorWiseBarGraph.add(temp);			
		}
		return donorWiseBarGraph;	
	}

	public List<KVPair<String, String>> getAssoWiseBarGraph() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}

		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-1+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 where rownum = 1 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		String currentBlockYear = null;
		while(rs.next()) {			
			currentBlockYear = rs.getString(1);
		}
		rs.close();
		statement.close();
		

		String query = " WITH TAB1 AS ( select t1.*, (select sname from tm_state where t1.state = scode) state_name, "
				+ " (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name "
				+ " from (  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') "
				+ " submission_date, nvl(for_amt,0) AS foramt,   nvl(for_amt + bk_int + oth_int,0) AS totalamount, "
				+ " substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi "
				+ " where  p1.blkyear in( ? ) AND   substr(TL.rcn,-1,1)='R'  AND fi.rcn=TL.rcn   and (reg_date is null or trunc(reg_date) "
				+ " < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D')   and P1.blkyear = TL.blkyear and "
				+ " TL.final_submit = 'Y' AND TL.rcn = P1.rcn    UNION ALL    SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, "
				+ " nvl(for_amt,0) AS foramt,    nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
				+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi    where  p1.blkyear in( ? ) AND     substr(TL.rcn,-1,1)='P'   and fi.rcn=TL.rcn "
				+ " and P1.blkyear = TL.blkyear     and TL.final_submit = 'Y' and TL.rcn = P1.rcn   union all   SELECT P1.blkyear, tl.rcn, fi.asso_name, "
				+ " to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,    nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, "
				+ " substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district     FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi      WHERE  p1.blkyear in( ? ) AND "
				+ " substr(TL.rcn,-1,1)='R'    AND fi.rcn=TL.rcn   and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D')   and P1.blkyear = TL.blkyear     and TL.final_submit = 'Y' and TL.rcn = P1.rcn    UNION ALL    SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,   nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district    FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi    WHERE  p1.blkyear in('2013-2014') AND     substr(TL.rcn,-1,1)='P'   AND fi.rcn=TL.rcn    and P1.blkyear = TL.blkyear    and TL.final_submit = 'Y' and TL.rcn = P1.rcn   ) t1   ORDER BY blkyear, foramt DESC),T4 as (select * from TAB1 where rownum<='10'), "
				+ " T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=10 ) SELECT asso_name, totalamount FROM T3 WHERE RN>=1 order by totalamount desc ";
				
				

		statement = connection.prepareStatement(query);		
		statement.setString(1, currentBlockYear);
		statement.setString(2, currentBlockYear);
		statement.setString(3, currentBlockYear);
		rs = statement.executeQuery();
		List<KVPair<String, String>>  assoWiseBarGraph = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assoWiseBarGraph.add(temp);			
		}
		return assoWiseBarGraph;	
	}
	
	public List<KVPair<String, String>> getAssoFinYearWiseBarGraph() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}

		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-10+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 where rownum <= 10 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		String currentBlockYear = null;
		String blockYear = "";
		while(rs.next()) {			
			currentBlockYear = rs.getString(1);
			blockYear+="'"+currentBlockYear+"',";
		}
		blockYear=blockYear.substring(0,blockYear.length()-1);
		rs.close();
		statement.close();

		String query = " WITH TAB1 AS ( select t1.*, (select sname from tm_state where t1.state = scode) state_name, "
				+ " (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name "
				+ " from (  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') "
				+ " submission_date, nvl(for_amt,0) AS foramt,   nvl(for_amt + bk_int + oth_int,0) AS totalamount, "
				+ " substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi "
				+ " where  p1.blkyear in( ? ) AND   substr(TL.rcn,-1,1)='R'  AND fi.rcn=TL.rcn   and (reg_date is null or trunc(reg_date) "
				+ " < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D')   and P1.blkyear = TL.blkyear and "
				+ " TL.final_submit = 'Y' AND TL.rcn = P1.rcn    UNION ALL    SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, "
				+ " nvl(for_amt,0) AS foramt,    nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
				+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi    where  p1.blkyear in( ? ) AND     substr(TL.rcn,-1,1)='P'   and fi.rcn=TL.rcn "
				+ " and P1.blkyear = TL.blkyear     and TL.final_submit = 'Y' and TL.rcn = P1.rcn   union all   SELECT P1.blkyear, tl.rcn, fi.asso_name, "
				+ " to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,    nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, "
				+ " substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district     FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi      WHERE  p1.blkyear in( ? ) AND "
				+ " substr(TL.rcn,-1,1)='R'    AND fi.rcn=TL.rcn   and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D')   and P1.blkyear = TL.blkyear     and TL.final_submit = 'Y' and TL.rcn = P1.rcn "
				+ " UNION ALL    SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,   nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
				+ " FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi    WHERE  p1.blkyear in('2013-2014') AND     substr(TL.rcn,-1,1)='P'   AND fi.rcn=TL.rcn    and P1.blkyear = TL.blkyear    and TL.final_submit = 'Y' and TL.rcn = P1.rcn   ) t1   ORDER BY blkyear, foramt DESC), "
				+ " tab3 as (select asso_name, totalamount, blkyear, row_number() over (PARTITION BY blkyear order by totalamount desc) as id from TAB1) "
				+ " select  asso_name , totalamount, blkyear  from tab3 where id=1 ";

		statement = connection.prepareStatement(query);		
		statement.setString(1, blockYear);
		statement.setString(2, blockYear);
		statement.setString(3, blockYear);
/*		statement.setString(1, currentBlockYear);
		statement.setString(2, currentBlockYear);
		statement.setString(3, currentBlockYear);*/
		rs = statement.executeQuery();
		List<KVPair<String, String>>  assoFinYearWiseBarGraph = new ArrayList<KVPair<String, String>>();
		//List<List<String>>  assoFinYearWiseBarGraph = new ArrayList<List<String>>();
		while(rs.next()) {	
			//List<String> temp = new ArrayList<String>();
			//List<KVPair<String, String>>  temp = new ArrayList<KVPair<String, String>>();
/*			temp.add(rs.getString(1));
			temp.add(rs.getString(2));
*/			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assoFinYearWiseBarGraph.add(temp);			
		}
		return assoFinYearWiseBarGraph;	
	}
		
	
	public List<KVPair<String, String>> getCountryWiseBarGraph() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-1+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 where rownum = 1 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		String currentBlockYear = null;
		while(rs.next()) {			
			currentBlockYear = rs.getString(1);
		}
		rs.close();
		statement.close();

		
		String query = " WITH TAB1 AS ( select tab.blkyear, tab.ctr_name,sum(tab.amount) amt from "
				+ " (    SELECT A.blkyear, ctr_name, b.ctr_code, sum(amount) amount FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d "
				+ " WHERE  A.blkyear in( ? )    AND b.blkyear =  A.blkyear  AND A.final_submit='Y' AND b.ctr_code = c.ctr_code "
				+ " AND A.rcn=b.rcn AND A.rcn=d.rcn and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and "
				+ " (d.status <> 'D')    AND substr(A.rcn,-1,1)='R'  AND 1=1 GROUP BY A.blkyear, ctr_name,b.ctr_code UNION ALL  SELECT a.blkyear, ctr_name, b.ctr_code, sum(amount) amount "
				+ " FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d      where  A.blkyear in( ? ) and b.blkyear =  a.blkyear and "
				+ " a.final_submit='Y' and b.ctr_code = c.ctr_code and     A.rcn=b.rcn "
				+ " AND A.rcn=d.rcn    AND d.blkyear=a.blkyear   AND substr(A.rcn,-1,1)='P' "
				+ " AND 1=1 group by a.blkyear, ctr_name, b.ctr_code   union all  SELECT A.blkyear, ctr_name, donor_country, sum(amount) amount "
				+ " FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd WHERE  A.blkyear in( ? ) "
				+ " AND b.blkyear =  A.blkyear    AND A.final_submit='Y' AND fd.donor_country = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn "
				+ " and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D') "
				+ " AND substr(A.rcn,-1,1)='R'   AND 1=1 AND fd.donor_type='01'    AND d.rcn=fd.rcn 	and b.donor_code=fd.donor_code "
				+ " GROUP BY A.blkyear, ctr_name, donor_country UNION ALL   SELECT a.blkyear, ctr_name, donor_country,  sum(amount) amount "
				+ " FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd   where  A.blkyear in( ? ) and "
				+ " b.blkyear =  a.blkyear and      a.final_submit='Y' and fd.donor_country = c.ctr_code and   A.rcn=b.rcn    AND A.rcn=d.rcn "
				+ " AND d.blkyear=a.blkyear     AND substr(A.rcn,-1,1)='P'     AND 1=1 AND fd.donor_type='01'     AND d.rcn=fd.rcn "
				+ " and b.donor_code=fd.donor_code GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name) "
				+ " tab where 1=1 GROUP BY tab.blkyear, tab.ctr_name  ORDER BY blkyear, amt DESC), "
				+ " T4 as (select ctr_name,amt from TAB1 where rownum<=10),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=10 ) SELECT * FROM T3 WHERE RN>=1 ";
		
		statement = connection.prepareStatement(query);
		statement.setString(1, currentBlockYear);
		statement.setString(2, currentBlockYear);
		statement.setString(3, currentBlockYear);
		statement.setString(4, currentBlockYear);
		rs = statement.executeQuery();
		List<KVPair<String, String>>  countryWiseBarGraph = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			countryWiseBarGraph.add(temp);			
		}
		return countryWiseBarGraph;	
	}
	
	public List<KVPair<String, String>> getCountryFinYearWiseBarGraph() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		String subQuery = " with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ " to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-10+1 start_year from dual),t2 as (select "
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ " then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1) "
				+ " select yr||'-'||(yr+1) blkyear from t2 where rownum <= 10 ";

		PreparedStatement statement = connection.prepareStatement(subQuery);		
		ResultSet rs = statement.executeQuery();
		String currentBlockYear = null;
		String blockYear="";
		while(rs.next()) {			
			currentBlockYear = rs.getString(1);
			blockYear+="'"+currentBlockYear+"',";
		}
		blockYear=blockYear.substring(0,blockYear.length()-1);
		
		rs.close();
		statement.close();

		
		String query = " WITH TAB1 AS ( select tab.blkyear, tab.ctr_name,sum(tab.amount) amt from "
				+ " (  SELECT A.blkyear, ctr_name, b.ctr_code, sum(amount) amount FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d "
				+ " WHERE  A.blkyear in( ?)    AND b.blkyear =  A.blkyear  AND A.final_submit='Y' AND b.ctr_code = c.ctr_code "
				+ " AND A.rcn=b.rcn AND A.rcn=d.rcn and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and "
				+ " (d.status <> 'D')    AND substr(A.rcn,-1,1)='R'  AND 1=1 GROUP BY A.blkyear, ctr_name,b.ctr_code UNION ALL  SELECT a.blkyear, ctr_name, b.ctr_code, sum(amount) amount "
				+ " FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d      where  A.blkyear in( ? ) and b.blkyear =  a.blkyear and "
				+ " a.final_submit='Y' and b.ctr_code = c.ctr_code and     A.rcn=b.rcn "
				+ " AND A.rcn=d.rcn    AND d.blkyear=a.blkyear   AND substr(A.rcn,-1,1)='P' "
				+ " AND 1=1 group by a.blkyear, ctr_name, b.ctr_code   union all  SELECT A.blkyear, ctr_name, donor_country, sum(amount) amount "
				+ " FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd WHERE  A.blkyear in( ? ) "
				+ " AND b.blkyear =  A.blkyear    AND A.final_submit='Y' AND fd.donor_country = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn "
				+ " and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D') "
				+ " AND substr(A.rcn,-1,1)='R'   AND 1=1 AND fd.donor_type='01'    AND d.rcn=fd.rcn 	and b.donor_code=fd.donor_code "
				+ " GROUP BY A.blkyear, ctr_name, donor_country UNION ALL   SELECT a.blkyear, ctr_name, donor_country,  sum(amount) amount "
				+ " FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd   where  A.blkyear in( ? ) and "
				+ " b.blkyear =  a.blkyear and      a.final_submit='Y' and fd.donor_country = c.ctr_code and   A.rcn=b.rcn    AND A.rcn=d.rcn "
				+ " AND d.blkyear=a.blkyear     AND substr(A.rcn,-1,1)='P'     AND 1=1 AND fd.donor_type='01'     AND d.rcn=fd.rcn "
				+ " and b.donor_code=fd.donor_code GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name) "
				+ " tab where 1=1 GROUP BY tab.blkyear, tab.ctr_name  ORDER BY blkyear, amt DESC), "
				+ " tab2 as (select ctr_name, amt, blkyear, row_number() over (PARTITION BY blkyear order by amt desc) as id from tab1) "
				+ " select ctr_name, amt, blkyear from tab2 where id=1 ";
		
		statement = connection.prepareStatement(query);
		statement.setString(1, blockYear);
		statement.setString(2, blockYear);
		statement.setString(3, blockYear);
		statement.setString(4, blockYear);
		rs = statement.executeQuery();
		List<KVPair<String, String>>  countryFinYearWiseBarGraph = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			countryFinYearWiseBarGraph.add(temp);			
		}
		return countryFinYearWiseBarGraph;	
	}
	
	public List<List3> getApplicationStatistics() throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "WITH t1 AS ( "
				+ "SELECT application_id, case when submission_date is null then created_on else submission_date end AS s_date, service_code service, "
				+ " CASE WHEN current_stage=1 THEN 1  "
				+ "ELSE  "
				+ "CASE WHEN current_stage=2 then"
				+ " CASE WHEN current_status=4 THEN 1  WHEN current_status=9 THEN 2  "
				+ "WHEN current_status=10 THEN 2 "
				+ " WHEN current_status=12 THEN 2 "
				+ "WHEN current_status=8 THEN 1   "
				+ "END "
				+ " END  "
				+ "END  "
				+ "status "
				+ "FROM v_application_details "
				+ "), t2 AS ( "
				+ "SELECT to_char(s_date, 'yyyy') YEAR, service, status FROM t1 where service not in ('04')  "
				+ "), t3 as "
				+ "(SELECT  * FROM (SELECT YEAR, status FROM t2) "
				+ "pivot (count(status) FOR(status) IN (1 AS Pending, 2 AS Disposed)) "
				+ " order by year DESC ) select * from t3 order by year ASC";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List3> applicationStatistics =new ArrayList<List3>(); 
		while(rs.next()) {
			applicationStatistics.add(new List3(rs.getString(1),rs.getString(2), rs.getString(3)));			
		}
		return applicationStatistics;	
	}
	public List<Notification> getNotificationAttachment() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select a.notification_id,a.rowid from t_notification_document a,t_notification_details b "+
							" where a.notification_id=b.notification_id and b.notification_type_id=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, notificationType);		
		ResultSet rs = statement.executeQuery();
		List<Notification> notificationList = new ArrayList<Notification>();
		while(rs.next()) {			
			notificationList.add(new Notification(rs.getString(1),rs.getString(2)));			
		}
		return notificationList;
	}
	public List<Notification> getMasterNotification() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT NOTIFICATION_ID,(SELECT NOTIFICATION_NAME FROM TM_NOTIFICATION_TYPE "
							+ "WHERE NOTIFICATION_TYPE_ID=T_NOTIFICATION_DETAILS.NOTIFICATION_TYPE_ID) AS NOTIFICATION_TYPE,"+
							"MESSAGE_TITLE,MESSAGE_DETAILS,to_char(ENTERED_ON,'dd-mm-yyyy') FROM T_NOTIFICATION_DETAILS WHERE RECORD_STATUS=0");
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
		
		List<Notification> notificationList = new ArrayList<Notification>();
		while(rs.next()) {			
			notificationList.add(new Notification(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),""));			
		}
		return notificationList;
	}
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("notificationId")) {
				orderbyClause.append(" ORDER BY NOTIFICATION_ID");
			}else if(sortColumn.equals("notificationTypeId")) {
				orderbyClause.append(" ORDER BY NOTIFICATION_TYPE");
			}else if(sortColumn.equals("enteredDate")) {
				orderbyClause.append(" ORDER BY ENTERED_ON");
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
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	@Override
	public Integer insertRecord(Notification object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Inserting  in T_NOTIFICATION_DETAILS
			String notificationId=generateNotificationId(object.getSource_office());
			StringBuffer query = new StringBuffer("INSERT INTO T_NOTIFICATION_DETAILS(NOTIFICATION_ID,NOTIFICATION_TYPE_ID,MESSAGE_TITLE,MESSAGE_DETAILS"
								+ ",ENTERED_BY,ENTERED_ON,RECORD_STATUS) VALUES(?,?,?,?,?,sysdate,0)");
				statement = connection.prepareStatement(query.toString());
				statement.setString(1, notificationId);
				statement.setString(2, object.getNotificationTypeId());
				statement.setString(3, object.getMessageTitle());
				statement.setString(4, object.getMessageDetails());
				statement.setString(5, object.getEnteredBy());				
				int rows=statement.executeUpdate();
				statement.close();
			
				Boolean existFlag=false;
				StringBuffer query3=new StringBuffer("SELECT * FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
					statement = connection.prepareStatement(query3.toString());
					statement.setString(1, sessionId);	
					ResultSet rs = statement.executeQuery();
					if(rs.next()==true){
						existFlag=true;
					}
					statement.close();
				if(existFlag==true){
					// Inserting Attachments into T_NOTIFICATION_DOCUMENT from T_UPLOAD_CACHE		
					StringBuffer query1 = new StringBuffer("INSERT INTO  T_NOTIFICATION_DOCUMENT(NOTIFICATION_ID,NOTIFICATION_DOCUMENT,RECORD_STATUS) "+
										"(SELECT ?,DOCUMENT,? FROM T_UPLOAD_CACHE WHERE SESSION_ID=?)");
						statement = connection.prepareStatement(query1.toString());
						statement.setString(1, notificationId);				
						statement.setInt(2, 0);							
						statement.setString(3, sessionId);
						statement.executeUpdate();
						statement.close();	
					
					// DELETE FROM T_UPLOAD CACHE
					StringBuffer query2=new StringBuffer("DELETE FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
						statement = connection.prepareStatement(query2.toString());
						statement.setString(1, sessionId);	
						statement.executeUpdate();
						statement.close();
				}				
		return rows;			
	}	
	private String generateNotificationId(String officeCode) throws Exception {
		String val=null;
		StringBuffer query = new StringBuffer("SELECT FN_GEN_NOTIFICATIONID(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officeCode);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			 val = rs.getString(1);
		}	
		return val;
	}
	public String uploadNotificationDocument() throws Exception{
		PreparedStatement statement=null;
		InputStream is = null;
		if(attachment != null)
			is = attachment.getInputStream();
		StringBuffer query1 = new StringBuffer("INSERT INTO T_UPLOAD_CACHE(SESSION_ID,UPLOAD_ID,DOCUMENT) VALUES(?,?,?)");
			statement = connection.prepareStatement(query1.toString());
			statement.setString(1, sessionId);
			statement.setString(2, uploadId);			
			if(is == null)
				statement.setNull(3, java.sql.Types.BLOB);
			else
				statement.setBinaryStream (3, is, (int) attachment.getSize() );										
			statement.executeUpdate();
			statement.close();
			return null;
	}
	public String deleteNotificationDocument() throws Exception{
		PreparedStatement statement=null;		
		StringBuffer query = new StringBuffer("DELETE FROM T_UPLOAD_CACHE WHERE SESSION_ID=? AND UPLOAD_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, sessionId);
			statement.setString(2, uploadId);											
			statement.executeUpdate();
			statement.close();
			return null;
	}
	
	public String deleteAvailableNotificationDocument(Notification object) throws Exception{
		PreparedStatement statement=null;		
		StringBuffer query = new StringBuffer("DELETE FROM T_NOTIFICATION_DOCUMENT WHERE ROWID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, object.getRowId());														
			statement.executeUpdate();
			statement.close();
			return null;
	}
	
	public List<List3> getNotification(Notification object) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT NOTIFICATION_TYPE_ID,MESSAGE_TITLE,MESSAGE_DETAILS FROM T_NOTIFICATION_DETAILS "
							+ " WHERE NOTIFICATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1,object.getNotificationId());
		ResultSet rs = statement.executeQuery();
		List<List3> requestedDetails = new ArrayList<List3>();
		while(rs.next()) {			
			requestedDetails.add(new List3(rs.getString(1),rs.getString(2),rs.getString(3)));
		}
		return requestedDetails;		
	}
	public List<List1> getNotificationAttachment(Notification object) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT ROWID FROM T_NOTIFICATION_DOCUMENT "
							+ " WHERE NOTIFICATION_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1,object.getNotificationId());
		ResultSet rs = statement.executeQuery();
		List<List1> requestedAttachmentList = new ArrayList<List1>();
		while(rs.next()) {			
			requestedAttachmentList.add(new List1(rs.getString(1)));
		}
		return requestedAttachmentList;		
	}
	public Integer editRecord(Notification object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  in T_NOTIFICATION_DETAILS			
			StringBuffer query = new StringBuffer("UPDATE T_NOTIFICATION_DETAILS SET NOTIFICATION_TYPE_ID=?,MESSAGE_TITLE=?,MESSAGE_DETAILS=?"
								+ ",ENTERED_BY=?,ENTERED_ON=sysdate WHERE NOTIFICATION_ID=?");
				statement = connection.prepareStatement(query.toString());				
				statement.setString(1, object.getNotificationTypeId());
				statement.setString(2, object.getMessageTitle());
				statement.setString(3, object.getMessageDetails());
				statement.setString(4, object.getEnteredBy());	
				statement.setString(5, object.getNotificationId());
				int rows=statement.executeUpdate();				
				statement.close();
				
			Boolean existFlag=false;
			StringBuffer query3=new StringBuffer("SELECT * FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
				statement = connection.prepareStatement(query3.toString());
				statement.setString(1, sessionId);	
				ResultSet rs = statement.executeQuery();
				if(rs.next()==true){
					existFlag=true;
				}
				statement.close();
			if(existFlag==true){
				// Inserting Attachments into T_NOTIFICATION_DOCUMENT from T_UPLOAD_CACHE		
				StringBuffer query1 = new StringBuffer("INSERT INTO  T_NOTIFICATION_DOCUMENT(NOTIFICATION_ID,NOTIFICATION_DOCUMENT,RECORD_STATUS) "+
									"(SELECT ?,DOCUMENT,? FROM T_UPLOAD_CACHE WHERE SESSION_ID=?)");
					statement = connection.prepareStatement(query1.toString());
					statement.setString(1, object.getNotificationId());				
					statement.setInt(2, 0);							
					statement.setString(3, sessionId);
					statement.executeUpdate();
					statement.close();	
				
				// DELETE FROM T_UPLOAD CACHE
				StringBuffer query2=new StringBuffer("DELETE FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
					statement = connection.prepareStatement(query2.toString());
					statement.setString(1, sessionId);	
					statement.executeUpdate();
					statement.close();
			}			
		return rows;			
	}	
	@Override
	public Integer removeRecord(Notification object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  RECORD_STATUS in T_NOTIFICATION_DETAILS			
			StringBuffer query = new StringBuffer("UPDATE T_NOTIFICATION_DETAILS SET RECORD_STATUS=1 WHERE NOTIFICATION_ID=?");
				statement = connection.prepareStatement(query.toString());					
				statement.setString(1, object.getNotificationId());
				int rows=statement.executeUpdate();				
				statement.close();
				
			// Updating  RECORD_STATUS in T_NOTIFICATION_DOCUMENT			
			StringBuffer query1 = new StringBuffer("UPDATE T_NOTIFICATION_DOCUMENT SET RECORD_STATUS=1 WHERE NOTIFICATION_ID=?");
				statement = connection.prepareStatement(query1.toString());					
				statement.setString(1, object.getNotificationId());	
				statement.executeUpdate();
				statement.close();		
		return rows;
	}
	
	public List<List3> getApplicationServiceStatistics() throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "WITH t1 AS (SELECT application_id, CASE WHEN submission_date IS NULL THEN created_on ELSE submission_date END AS s_date, service_code service, CASE WHEN current_stage=1 THEN 1 ELSE CASE WHEN current_stage=2 THEN CASE WHEN current_status=4 THEN 1 WHEN current_status=9 THEN 2 WHEN current_status=10 THEN 2 WHEN current_status=12 THEN 2 WHEN current_status=8 THEN 1 END END END status FROM v_application_details where service_code in (01,02,03,07) ), t2 AS (SELECT TO_CHAR(s_date, 'yyyy') YEAR, service, status FROM t1 WHERE service NOT IN ('04') ), t3 AS ( SELECT * FROM t2 ), t4 AS (SELECT * FROM (SELECT service_desc, status FROM t3, TM_SERVICE a WHERE t3.service =a.service_code ) pivot (COUNT(status) FOR(status) IN (1 AS Pending, 2 AS Disposed)) ORDER BY service_desc ) SELECT * FROM t4";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List3> applicationServiceStatistics =new ArrayList<List3>(); 
		while(rs.next()) {
			applicationServiceStatistics.add(new List3(rs.getString(1),rs.getString(2),rs.getString(3)));			
		}
		return applicationServiceStatistics;	
	}
	public List<List4> getDashboardServiceStatistics() throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		
		/*DashboardSessionBean dbsb=new DashboardSessionBean();
		System.out.println("userID "+dbsb.getUserId());
		System.out.println("sessionID "+dbsb.getSessionId());*/
		String query = "select * from DBOARD_GRAPENREG_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List4> dashboardServiceStatistics =new ArrayList<List4>(); 
		while(rs.next()) {
			dashboardServiceStatistics.add(new List4(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));			
		}
		return dashboardServiceStatistics;	
	}
	public List<List5> getApplicationOrganisationStatistics() throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "select * from DBOARD_REG_ORG_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List5> applicationOrganisationStatistics =new ArrayList<List5>(); 
		while(rs.next()) {
			applicationOrganisationStatistics.add(new List5(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));			
		}
		return applicationOrganisationStatistics;	
	}
	public List<List3> getRegistrationDesignationStatistics(HttpServletRequest req) throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		HttpSession http = req.getSession();
		UserSession user=(UserSession)http.getAttribute("user");
		StringBuffer query1=new StringBuffer("insert into  t_actionlog_details(user_id,session_id,action_time,action_link) values(?,?,sysdate,?)");
		PreparedStatement pstmt = connection.prepareStatement(query1.toString());
    	
    	pstmt.setString(1, user.getUserId());
    	pstmt.setString(2, (String) http.getAttribute("SessionId"));
    	pstmt.setString(3, "dashboard/Registration");
    	 pstmt.executeUpdate();
		String query = "select * from DBOARD_REG_DES_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List3> registrationDesignationStatistics =new ArrayList<List3>(); 
		while(rs.next()) {
			if(rs.getString(1)==null) {
			registrationDesignationStatistics.add(new List3("null",rs.getString(2),rs.getString(3)));			
			}
			else {
				registrationDesignationStatistics.add(new List3(rs.getString(1),rs.getString(2),rs.getString(3)));			
			}
			}
		return registrationDesignationStatistics;	
	}
	public List<List5> getPeriorPermissionOrganisationStatistics() throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "select * from DBOARD_PRP_ORG_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List5> periorPermissionOrganisationStatistics =new ArrayList<List5>(); 
		while(rs.next()) {
			periorPermissionOrganisationStatistics.add(new List5(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));			
		}
		return periorPermissionOrganisationStatistics;	
	}
	public List<List3> getPriorPermissionDesignationStatistics(HttpServletRequest req) throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		HttpSession http = req.getSession();
		UserSession user=(UserSession)http.getAttribute("user");
		StringBuffer query1=new StringBuffer("insert into  t_actionlog_details(user_id,session_id,action_time,action_link) values(?,?,sysdate,?)");
		PreparedStatement pstmt = connection.prepareStatement(query1.toString());
    	
    	pstmt.setString(1, user.getUserId());
    	pstmt.setString(2, (String) http.getAttribute("SessionId"));
    	pstmt.setString(3, "dashboard/Prior Permission");
    	 pstmt.executeUpdate();
		String query = "select * from DBOARD_PRP_DES_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List3> priorPermissionDesignationStatistics =new ArrayList<List3>(); 
		while(rs.next()) {
			if(rs.getString(1)==null) {
				priorPermissionDesignationStatistics.add(new List3("null",rs.getString(2),rs.getString(3)));			
			}
			else {
				priorPermissionDesignationStatistics.add(new List3(rs.getString(1),rs.getString(2),rs.getString(3)));			
			}
			}
		return priorPermissionDesignationStatistics;	
	}
	public List<List5> getRenewalOrganisationStatistics() throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "select * from DBOARD_REN_ORG_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List5> renewalOrganisationStatistics =new ArrayList<List5>(); 
		while(rs.next()) {
			renewalOrganisationStatistics.add(new List5(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));			
		}
		
		return renewalOrganisationStatistics;	
	}
	public List<List3> getRenewalDesignationStatistics(HttpServletRequest req) throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		HttpSession http = req.getSession();
		UserSession user=(UserSession)http.getAttribute("user");
		StringBuffer query1=new StringBuffer("insert into  t_actionlog_details(user_id,session_id,action_time,action_link) values(?,?,sysdate,?)");
		PreparedStatement pstmt = connection.prepareStatement(query1.toString());
    	
    	pstmt.setString(1, user.getUserId());
    	pstmt.setString(2, (String) http.getAttribute("SessionId"));
    	pstmt.setString(3, "dashboard/Renewal");
    	 pstmt.executeUpdate();
		String query = "select * from DBOARD_REN_DES_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List3> renewalDesignationStatistics =new ArrayList<List3>(); 
		while(rs.next()) {
			if(rs.getString(1)==null) {
				renewalDesignationStatistics.add(new List3("null",rs.getString(2),rs.getString(3)));			
			}
			else {
				renewalDesignationStatistics.add(new List3(rs.getString(1),rs.getString(2),rs.getString(3)));			
			}
			}
		return renewalDesignationStatistics;	
	}
	public List<List5> getHospitalityOrganisationStatistics() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "select * from DBOARD_HOS_ORG_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List5> hospitalityOrganisationStatistics =new ArrayList<List5>(); 
		while(rs.next()) {
			hospitalityOrganisationStatistics.add(new List5(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));			
		}
		return hospitalityOrganisationStatistics;	
	}
	public List<List3> getHospitalityDesignationStatistics(HttpServletRequest req) throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		HttpSession http = req.getSession();
		UserSession user=(UserSession)http.getAttribute("user");
		StringBuffer query1=new StringBuffer("insert into  t_actionlog_details(user_id,session_id,action_time,action_link) values(?,?,sysdate,?)");
		PreparedStatement pstmt = connection.prepareStatement(query1.toString());
    	
    	pstmt.setString(1, user.getUserId());
    	pstmt.setString(2, (String) http.getAttribute("SessionId"));
    	pstmt.setString(3, "dashboard/Hospitality");
    	 pstmt.executeUpdate();
		String query = "select * from DBOARD_HOS_DES_WISE_APPS_COUNT";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		System.out.println(rs);
		List<List3> hospitalityDesignationStatistics =new ArrayList<List3>(); 
		while(rs.next()) {
			if(rs.getString(1)==null) {
				hospitalityDesignationStatistics.add(new List3("null",rs.getString(2),rs.getString(3)));			
			}
			else {
				hospitalityDesignationStatistics.add(new List3(rs.getString(1),rs.getString(2),rs.getString(3)));			
			}
			}
		return hospitalityDesignationStatistics;	
		
	}
	
	public List<List5> getApplicationDesignationStatistics() throws Exception {

		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		String query = "WITH t AS (SELECT DISTINCT A.application_id, CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2 ) AND a.application_id=b.application_id ) THEN NULL ELSE CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2 ) AND status IN (5,6) AND a.application_id=b.application_id ) THEN 1 ELSE 0 END END IB_STATUS, CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3 ) AND a.application_id=b.application_id ) THEN NULL ELSE CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3 ) AND status IN (5,6) AND a.application_id=b.application_id ) THEN 1 ELSE 0 END END RAW_STATUS FROM t_pc_office_level_final_status A ), t2 AS (SELECT b.application_id, b.applicant_name, b.section_fileno, (SELECT SNAME FROM TM_STATE WHERE SCODE=b.state ) AS state, (SELECT distname FROM TM_DISTRICT WHERE distcode=b.district ) AS district, CASE WHEN submission_date IS NULL THEN created_on ELSE submission_date END AS s_date, b.service_code service, CASE WHEN b.current_status=8 THEN 1 WHEN (b.current_stage=1 AND b.current_status =2) THEN 0 ELSE CASE WHEN t.ib_status IS NULL THEN CASE WHEN t.raw_status=0 THEN 1 ELSE 0 END ELSE CASE WHEN t.ib_status=1 THEN CASE WHEN t.raw_status=0 THEN 1 ELSE 0 END ELSE 1 END END END MHA_STATUS, t.IB_status, t.raw_status, CASE WHEN b.current_status=8 THEN 0 ELSE NULL END applicant_status FROM v_application_details b LEFT JOIN t ON t.application_id =b.application_id WHERE ((b.current_stage = 2 AND b.current_status IN (4, 8)) OR (b.current_stage = 1 AND b.current_status =2)) ), t3 AS (SELECT t2.*, a.user_id, (SELECT user_name FROM tm_user um WHERE um.user_id=a.user_id ) user_name, (SELECT designation_name FROM tm_designation c WHERE c.designation_id= (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id ) ) designation, (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id ) designation_id FROM t2 LEFT JOIN T_PC_OFFICE_USER_DETAILS a ON t2.application_id=a.application_id AND a.office_code in ('MHA01') ) , t4 AS (SELECT t2.state, t2.district, TO_CHAR(s_date, 'yyyy') YEAR, TO_CHAR(TRUNC(s_date)) AS s_date, service_desc, t2.application_id, t2.applicant_name, t2.section_fileno, CASE WHEN mha_status=0 THEN 1 ELSE 0 END AS mha, CASE WHEN ib_status=0 THEN 1 ELSE 0 END AS ib, CASE WHEN raw_status=0 THEN 1 ELSE 0 END AS raw1, CASE WHEN applicant_status=0 THEN 1 ELSE 0 END AS applicant, user_id ||' [' ||user_name ||']' AS userName , designation, (TRUNC(sysdate)-TRUNC(s_date)) pending FROM t3 t2, tm_service s WHERE t2.service =s.service_code  AND service IN (01) AND 1 =1 ORDER BY YEAR, state, service_desc, s_date ) , T5 AS (SELECT T4.*, ROWNUM RN FROM T4 ) SELECT designation, sum(mha)mha, sum(ib)ib, sum(raw1)raw1, sum(applicant)applicant FROM T5 where pending between 0 and 30 group by designation";
		PreparedStatement statement = connection.prepareStatement(query);		
		ResultSet rs = statement.executeQuery();
		List<List5> applicationDesignationStatistics =new ArrayList<List5>(); 
		while(rs.next()) {
			applicationDesignationStatistics.add(new List5(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));			
		}
		return applicationDesignationStatistics;	
	}
	public String getPendencyCountReportDegwise(String ServiceId,String DegCode,String ranges,HttpServletRequest req) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		
		HttpSession http = req.getSession();
		UserSession user=(UserSession)http.getAttribute("user");
		StringBuffer query1=new StringBuffer("insert into  t_actionlog_details(user_id,session_id,action_time,action_link) values(?,?,sysdate,?)");
		System.out.println("ServiceId "+ServiceId);
		String actionLink = null;
		if(ServiceId.equals("01")) {
			actionLink="dashboard/Registration/"+DegCode;
		}
		if(ServiceId.equals("02")) {
			actionLink="dashboard/PriorPermission/"+DegCode;
		}
		if(ServiceId.equals("03")) {
			actionLink="dashboard/Renewal/"+DegCode;
		}
		if(ServiceId.equals("07")) {
			actionLink="dashboard/Hospitality/"+DegCode;
		}
		
		PreparedStatement pstmt = connection.prepareStatement(query1.toString());
    	
    	pstmt.setString(1, user.getUserId());
    	pstmt.setString(2, (String) http.getAttribute("SessionId"));
    	pstmt.setString(3, actionLink);
    	 pstmt.executeUpdate();
		String countQuery = null;
		//System.out.println("Total ServiceId "+ServiceId+"DegCode"+DegCode+"ranges"+ranges);
		if(DegCode.equalsIgnoreCase("null")) {
		 countQuery = new String("select count(*) from DBOARD_GRAPENREG_APPLIST where MHA=1 and SERVICE_CODE="+ServiceId+" and DESIGNATION is null and ranges="+ranges+""
				);
		}
		else {
			countQuery = new String("select count(*) from DBOARD_GRAPENREG_APPLIST where MHA=1 and SERVICE_CODE="+ServiceId+" and DESIGNATION='"+DegCode+"' and ranges="+ranges+"");
		}
		PreparedStatement statement = connection.prepareStatement(countQuery);
		    System.out.println("countQuery "+countQuery);
		  ResultSet rs = statement.executeQuery(); 
		  if(rs.next())
		 { 
			  
			  totalRecords = rs.getString(1);
		 } 
		  rs.close();
		 statement.close();
	
		 System.out.println("totalRecords"+totalRecords);
		return totalRecords;
	}
	public List<PendencyReport> getPendencyReportDegwise(String pageNum, String ServiceId, String DegCode, String recordsPerPage,String ranges) throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query=null;
		if(DegCode.equalsIgnoreCase("null")) {
         query = new StringBuffer("WITH T AS( select * from DBOARD_GRAPENREG_APPLIST where MHA=1 and SERVICE_CODE="+ServiceId+" and DESIGNATION is null and ranges="+ranges+" order by pending desc ), T2 AS ( SELECT T.*,ROWNUM RN FROM T) SELECT * FROM T2 where rn between ? and ?");
		
		}
		else {
			query = new StringBuffer("WITH T AS( select * from DBOARD_GRAPENREG_APPLIST where MHA=1 and SERVICE_CODE="+ServiceId+" and DESIGNATION='"+DegCode+"' and ranges="+ranges+" order by pending desc ), T2 AS ( SELECT T.*,ROWNUM RN FROM T) SELECT * FROM T2 where rn between ? and ?");
		
		}
         Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
		//System.out.println("Total ServiceId "+ServiceId+"DegCode"+DegCode+"pageNum"+pageNum+"recordsPerPage"+recordsPerPage);
		 PreparedStatement  statement = connection.prepareStatement(query.toString());
		if (pageNum == null || recordsPerPage == null) {

		} else {
			statement.setInt(1, (pageRequested - 1) * pageSize + 1);
			statement.setInt(2, (pageRequested - 1) * pageSize + pageSize);
			
		}
		System.out.println(query);
		ResultSet rs = statement.executeQuery();

		List<PendencyReport> pendencyReportDegwise = new ArrayList<PendencyReport>();
	
		while (rs.next()) {
			
			pendencyReportDegwise.add(new PendencyReport(checkNull(rs.getString("application_id")), checkNull(rs.getString("applicant_name")), checkNull(rs.getString("SERVICE_DESC")),checkNull( rs.getString("S_DATE")),
					checkNull(rs.getString("USERNAME")), checkNull(rs.getString("DESIGNATION")), checkNull(rs.getString("PENDING")),"null","null","null","null","null","null","null","null"));
		}
		int count = pendencyReportDegwise.size();
	//System.out.println("pendencyReportDegwise-----------"+count);
		return pendencyReportDegwise;
	}
	public String checkNull(String val){
		if(val==null||val=="")
			val="-";
		return val;
	}
	@Override
	public List<KVPair<String, String>> getKVList(List<Notification> list) {
		// TODO Auto-generated method stub
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

	
	public MultipartFile getAttachment() {
		return attachment;
	}

	public void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
}
