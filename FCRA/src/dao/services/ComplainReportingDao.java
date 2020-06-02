package dao.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.BaseDao;
import models.reports.PendencyReport;
import models.services.ComplainReporting;
import models.services.requests.ComplainRegisterBean;
import models.services.requests.ParentMenuName;
import utilities.KVPair;
import utilities.UserSession;

public class ComplainReportingDao extends BaseDao<ComplainReporting, String, String>{
	private String totalRecords;
	
 public ComplainReportingDao(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

List<ParentMenuName> menulist =new ArrayList<ParentMenuName>();
 
	public List<ParentMenuName> getMenuName(HttpServletRequest request) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		//System.out.println("connection-------"+connection);
		
		 HttpSession http = request.getSession();
		 UserSession user=(UserSession)http.getAttribute("user");
		StringBuffer query = new StringBuffer("select distinct SMENU_ID,ROLE_ID , (select tsm.smenu_name  from TM_SUBMENU tsm where tsm.smenu_id=tm_role_menu.SMENU_ID)menu_name from tm_role_menu where ROLE_ID in (select tur.role_id from TM_USER_ROLE tur where tur.USER_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1,user.getUserId());
		ResultSet rs = statement.executeQuery();
		//ParentMenuName pmn=new ParentMenuName();
		List<ParentMenuName> menulist =new ArrayList<ParentMenuName>();
		while(rs.next()) {			
			ParentMenuName pmn=new ParentMenuName();
			//System.out.println("connection-------1111"+rs.getString(3));
			pmn.setMenuname(rs.getString(3));
			menulist.add(pmn);			
		}
		return menulist;
		
	}
	public int getComplaintId() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		int cid = 0;
		StringBuffer query = new StringBuffer("select complaint_id from T_COMPLAINT_DETAILS where complaint_id = (select max(complaint_id) from T_COMPLAINT_DETAILS)");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		ResultSet rs = statement.executeQuery();
		ComplainRegisterBean crs=new ComplainRegisterBean();
		while(rs.next()) {			
			 cid = rs.getInt(1);
			// System.out.println("connection-------1111"+cid);
			 return cid;
		}
		
		return cid;
		
	}
	
	
public int triformSubmit(ComplainRegisterBean complainRegisterBean,HttpServletRequest request) throws Exception {
	if (connection == null) {
		throw new Exception("Invalid connection");
	}
	   ComplainReportingDao crd=new ComplainReportingDao(connection);
	  int cid = crd.getComplaintId()+1;
	StringBuffer query = new StringBuffer("INSERT INTO T_COMPLAINT_DETAILS (COMPLAINT_ID, COMPLAINT_SUBJECT, COMPLAINT_CATEGORY, COMPLAINT_RAISED_BY, COMPLAINT_SENT_DESCRIPTION,"
			+ " SENT_MAC_ADDRESS, SENT_BY, SENT_DATE, COMPLAINT_STATUS, COMPLAINT_MENU_NAME, COMPLAINT_ATTACHMENT_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	PreparedStatement statement = connection.prepareStatement(query.toString());	
	statement.setInt(1, cid);
	statement.setString(2, complainRegisterBean.getComplaintSubject());
	statement.setString(3, complainRegisterBean.getComplaintCategory());
	statement.setString(4, complainRegisterBean.getComplaintRaisedBy());
	statement.setString(5, complainRegisterBean.getComplaintSentDescription());
	statement.setString(6, complainRegisterBean.getSentMacAddress());
	statement.setString(7, complainRegisterBean.getSentBy());
	statement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
	statement.setString(9, complainRegisterBean.getComplaintStatus());
	statement.setString(10, complainRegisterBean.getComplaintMenuName());
	statement.setString(11, complainRegisterBean.getComplaintAttachmentStatus());
	int rows=statement.executeUpdate();
	// System.out.println("row-------"+rows);
	return cid;
	
}
public String getTrackRequestCount(HttpServletRequest req) throws Exception {
	if (connection == null) {
		throw new Exception("Invalid connection");
	}
	
	HttpSession http = req.getSession();
	UserSession user=(UserSession)http.getAttribute("user");
	String countQuery = null;
	 countQuery = new String("select count(*) from T_COMPLAINT_DETAILS where COMPLAINT_RAISED_BY=?");
	PreparedStatement statement = connection.prepareStatement(countQuery);
	statement.setString(1, user.getUserId());
	    System.out.println("countQuery "+countQuery);
	  ResultSet rs = statement.executeQuery(); 
	  if(rs.next())
	 { 
		  
		  totalRecords = rs.getString(1);
	 } 
	  rs.close();
	 statement.close();

	 //System.out.println("totalRecords"+totalRecords);
	return totalRecords;
}
	
public List<ComplainRegisterBean> getTrackRequestList(String pageNum, HttpServletRequest req, String recordsPerPage) throws Exception {
	if (connection == null) {
		throw new Exception("Invalid connection");
	}
	HttpSession http = req.getSession();
	UserSession user=(UserSession)http.getAttribute("user");
	StringBuffer query=null;
	
		query = new StringBuffer("WITH T AS( select * from T_COMPLAINT_DETAILS where COMPLAINT_RAISED_BY in '"+user.getUserId()+"' order by complaint_id desc), T2 AS ( SELECT T.*,ROWNUM RN FROM T) SELECT * FROM T2 where rn between ? and ?");
	
	
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

	List<ComplainRegisterBean> trackrequestlist = new ArrayList<ComplainRegisterBean>();
	DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
	//df.format(rs.getDate("JOINING"))
	while (rs.next()) {
		if(rs.getString(18).equalsIgnoreCase("CLOSED")) {
		trackrequestlist.add(new ComplainRegisterBean(checkNull(rs.getString(1)), checkNull(rs.getString(2)), checkNull(rs.getString(3)),checkNull( rs.getString(4)),
				checkNull(rs.getString(5)), checkNull(rs.getString(6)), checkNull(rs.getString(7)),checkNull(rs.getString(8)),checkNull(rs.getString(9)),checkNull(df.format(rs.getDate(10))),
				checkNull(rs.getString(11)),checkNull(rs.getString(12)),checkNull(rs.getString(13)),checkNull(rs.getString(14)),checkNull(rs.getString(15)),
				checkNull(rs.getString(16)),checkNull(rs.getString(17)),checkNull(rs.getString(18)),checkNull(rs.getString(19)),checkNull(rs.getString(20)),checkNull("<button type=\"button\" data-id="+rs.getString(2)+","+rs.getString(18)+" class=\"btn btn-primary btn-xs dt-edit\" style=\"margin-right:16px;\" onclick=\"getTrackingComplaindId("+rs.getString(2)+");\"title=\"Click to reply\">Reply</span></button>")));
	}
		else
		{
			trackrequestlist.add(new ComplainRegisterBean(checkNull(rs.getString(1)), checkNull(rs.getString(2)), checkNull(rs.getString(3)),checkNull( rs.getString(4)),
					checkNull(rs.getString(5)), checkNull(rs.getString(6)), checkNull(rs.getString(7)),checkNull(rs.getString(8)),checkNull(rs.getString(9)),checkNull(df.format(rs.getDate(10))),
					checkNull(rs.getString(11)),checkNull(rs.getString(12)),checkNull(rs.getString(13)),checkNull(rs.getString(14)),checkNull(rs.getString(15)),
					checkNull(rs.getString(16)),checkNull(rs.getString(17)),checkNull(rs.getString(18)),checkNull(rs.getString(19)),checkNull(rs.getString(20)),checkNull("<button type=\"button\" data-id="+rs.getString(2)+","+rs.getString(18)+" class=\"btn btn-primary btn-xs dt-edit\" style=\"margin-right:16px;\" onclick=\"getTrackingComplaindIdalert("+rs.getString(2)+");\"title=\"Click to reply\">Reply</span></button>")));
		}
	}
	//int count = trackrequestlist.size();
//System.out.println("pendencyReportDegwise-----------"+count);
	return trackrequestlist;
}
public void triformreplySubmit(ComplainRegisterBean complainRegisterBean) throws Exception {
	
	   //ComplainReportingDao crd=new ComplainReportingDao(connection);
	 // int cid = crd.getComplaintId()+1;
	
	StringBuffer query = new StringBuffer("select complaint_subject,complaint_sent_description from T_COMPLAINT_DETAILS where COMPLAINT_RAISED_BY in ? and complaint_id in ?");
	PreparedStatement statement = connection.prepareStatement(query.toString());	
	statement.setString(1, complainRegisterBean.getComplaintRaisedBy());
	statement.setString(2, complainRegisterBean.getComplaintId());
	ResultSet rs = statement.executeQuery();
	while (rs.next()) {
	// System.out.println("row-------"+rows);
		StringBuffer querytosubmit = new StringBuffer("UPDATE T_COMPLAINT_DETAILS SET complaint_subject = ? , complaint_sent_description = ?,complaint_status=? WHERE complaint_id ="+complainRegisterBean.getComplaintId());
		PreparedStatement statementt = connection.prepareStatement(querytosubmit.toString());	
		statementt.setString(1,rs.getString(1)+" 1."+complainRegisterBean.getComplaintSubject());
		statementt.setString(2, rs.getString(2)+". New Response: "+complainRegisterBean.getComplaintSentDescription());
		statementt.setString(3, "ACTIVE");
		int rss = statementt.executeUpdate();
		 System.out.println("row-------"+querytosubmit);
	}
	
}
public String checkNull(String val){
	if(val==null||val=="")
		val="-";
	return val;
}	public String getTotalRecords() {
	return totalRecords;
}
public void setTotalRecords(String totalRecords) {
	this.totalRecords = totalRecords;
}
	@Override
	public List<KVPair<String, String>> getKVList(List<ComplainReporting> list) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer insertRecord(ComplainReporting object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer removeRecord(ComplainReporting object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ComplainReporting> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
