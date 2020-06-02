package dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utilities.KVPair;
import utilities.lists.List2;
import models.master.ApplicationStage;
import models.reports.DonorDetail;
import models.reports.PurposeWiseReport;
import models.reports.ReportType;
import models.services.DonorDetails;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class DonorDetailDao extends BaseDao<DonorDetailDao, String, String>{
	private String searchString;
	private String state;
	private String country;
	private String association;
	private String donorName;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	
	
	public DonorDetailDao(Connection connection) {
		super(connection);
	}

	public List<List2> getStateList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SCODE,SNAME FROM TM_STATE WHERE RECORD_STATUS=0 order by SNAME");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<List2>  stateList = new ArrayList<List2>();
		while(rs.next()) {			
			stateList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return stateList;
	} 
	public List<List2> getCountryList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT CTR_CODE,CTR_NAME FROM TM_COUNTRY WHERE RECORD_STATUS=0 order by CTR_NAME");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<List2>  countryList = new ArrayList<List2>();
		while(rs.next()) {			
			countryList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return countryList;
	} 
	
	
	
	public List<List2> getAssociationList(String state) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		String stateList="1=1";
		 String selectedStateList=state.toString().replace("[", "").replace("]", "").replace(", ",",");
		 if(!selectedStateList.trim().equals("ALL2")){
	    	   stateList= "substr(stdist, 1, 2)  IN ("+selectedStateList+")";
	    	   
	    	 }
		StringBuffer query = new StringBuffer("select rcn, asso_name from fc_india where "+stateList+"  order by asso_name");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
	//	statement.setString(1,  stateList);
		ResultSet rs = statement.executeQuery();
		List<List2>  assocationList = new ArrayList<List2>();
		while(rs.next()) {			
			assocationList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return assocationList;
	} 
	
	
	
	
	public List<DonorDetail> getSearchListDetails() throws Exception {
	if(connection == null) {
			throw new Exception("Invalid connection");
		}
		String stateList="1=1";
		
		if(!(state.equals("null") || state.equals("")|| state.equals("ALL2"))){
		 String selectedStateList=state.toString().replace("," , "','").trim();
		if(!selectedStateList.trim().equals("ALL2")){
	    	   stateList= " state in  ('"+selectedStateList+"')";
		}
		}
		String countryList="1=1";
		
		if(!(country.equals("null") || country.equals("")|| country.equals("ALL2"))){
		 String selectedCountryList=country.toString().replace("[", "").replace("]", "").replace("," , "','");
		countryList= " donor_country in ('"+selectedCountryList+"')";
		}
		
		String donorList1="1=1";
		if(!(donorName.equals("null") || donorName.equals("")))
			donorList1 = "upper(donor_name) LIKE upper('%"+donorName+"%')";
		
		String associationList="1=1";
		if(!(association.equals("null")  || association.equals("")|| association.equals("ALL2"))){
			String associationList1=association.toString().replace("[", " ").replace("]", "").replace("," , "','");
			associationList= " rcn in ('"+associationList1+"')";
		}
	
	StringBuffer query = new StringBuffer("with t1 as (SELECT (fd.dcode||'O') donor_code,"
			+ " dname donor_name,trim(fd.add1)||', '||trim(fd.add2)||', '||trim(fd.add3)||',"
			+ "Pin :- '||fd.pin AS donor_address, fd.ctr_code donor_country, fp3.rcn, substr(fi.stdist,1,2) state"
			+ "	FROM fc_fc3_part3 fp3,fc_inst_donor fd,fc_india fi   "
			+ "WHERE fp3.dcode = fd.dcode  AND dtype = '1' and fp3.rcn=fi.rcn "
			+ "UNION ALL"
			+ "	SELECT (fd.donor_code||'N') donor_code, fd.donor_name,"
			+ " trim(donor_address) donor_address, fd.donor_country donor_country, fdw.rcn, substr(fi.stdist,1,2) state "
			+ "FROM fc_fc3_donor fd, fc_fc3_donor_wise fdw,fc_india fi  "
			+ "WHERE fd.rcn=fdw.rcn"
			+ "	AND fd.donor_code=fdw.donor_code  "
			+ "AND fd.donor_type='01' "
			+ "AND fdw.rcn=fi.rcn "
			+ "), t2 AS ("
			+ " SELECT fid.dcode||'O'  dcode, fid.dname AS dname,"
			+ " trim(fid.add1)||', '||trim(fid.add2)||', '||trim(fid.add3)||', "
			+ "Pin :- '||fid.pin AS donor_address, fid.ctr_code donor_country,"
			+ " email, phone, null as website"
			+ " FROM fc_inst_donor fid "
			+ "UNION ALL "
			+ "SELECT fd.DONOR_CODE||'N' dcode, fd.DONOR_NAME AS dname,"
			+ " trim(donor_address) donor_address, "
			+ "fd.donor_country donor_country, donor_emailid email,"
			+ " null as phone, donor_website website"
			+ " FROM fc_fc3_donor fd WHERE fd.DONOR_TYPE ='01'"
			+ ")"
			+ " select count(*) from (SELECT dcode,dname,donor_address, email,"
			+ " phone,website,(select CTR_NAME from TM_COUNTRY where CTR_CODE = DONOR_COUNTRY) as CNAME  FROM t2 "
			+ "WHERE dcode IN("
			+ "SELECT donor_code FROM t1 "
			+ "WHERE  "+donorList1+"  and "+countryList+" and "+associationList+" and "+stateList+" "
			+ "))");
	StringBuffer query1 = new StringBuffer(" with t1 as  "
						+  "(SELECT (fd.dcode||'O') donor_code, dname donor_name,trim(fd.add1)||', '||trim(fd.add2)||', '||trim(fd.add3)||',Pin :- '||fd.pin AS donor_address, "
						+  "fd.ctr_code donor_country, fp3.rcn, substr(fi.stdist,1,2) state	FROM fc_fc3_part3 fp3,fc_inst_donor fd,fc_india fi "
				+  "WHERE fp3.dcode = fd.dcode  AND dtype = '1' and fp3.rcn=fi.rcn "
				+  "UNION ALL	"
				+  "SELECT (fd.donor_code||'N') donor_code, fd.donor_name, trim(donor_address) donor_address, fd.donor_country donor_country, fdw.rcn, substr(fi.stdist,1,2)  "
				+  "state FROM fc_fc3_donor fd, fc_fc3_donor_wise fdw,fc_india fi  WHERE fd.rcn=fdw.rcn	AND fd.donor_code=fdw.donor_code  AND fd.donor_type='01' "
				+  "AND fdw.rcn=fi.rcn ), t2 AS ( SELECT fid.dcode||'O'  dcode, fid.dname AS dname, trim(fid.add1)||', '||trim(fid.add2)||', '||trim(fid.add3)||',  "
				+  "Pin :- '||fid.pin AS donor_address, fid.ctr_code donor_country, email, phone, null as website FROM fc_inst_donor fid  "
				+  "UNION ALL  "
				+  "SELECT fd.DONOR_CODE||'N' dcode, fd.DONOR_NAME AS dname, trim(donor_address) donor_address,"
				+ " fd.donor_country donor_country, donor_emailid email, "
				+  "null as phone, donor_website website FROM fc_fc3_donor fd WHERE fd.DONOR_TYPE ='01'), "
				+  "P1 as ( "
				+  "SELECT dcode,dname,donor_address, email, phone,website,"
				+ "(select CTR_NAME from TM_COUNTRY where CTR_CODE = DONOR_COUNTRY) as"
				+ "CNAME  FROM t2 WHERE dcode IN(SELECT donor_code FROM t1 "
				+  "WHERE  "+donorList1+" and "+countryList+" and "+associationList+" and "+stateList+" )),"
				+  "P2 AS (SELECT P1.*, ROWNUM RN FROM P1 WHERE ROWNUM<=?) SELECT * FROM P2 WHERE RN>=?");									 	
              
			    PreparedStatement statement = connection.prepareStatement(query.toString());		
				  ResultSet rs = statement.executeQuery();
				if(rs.next()) {
					totalRecords = rs.getString(1);
				}
				rs.close();
				statement.close();
				Integer pageRequested = Integer.parseInt(pageNum);
				Integer pageSize = Integer.parseInt(recordsPerPage);
				
				//String queryForPaging = preparePagingQuery(query);	
				statement = connection.prepareStatement(query1.toString());
				if(pageNum == null || recordsPerPage == null) {
					
				}
				else {
					
					statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
					statement.setInt(2, (pageRequested-1) * pageSize + 1);
				}
				rs = statement.executeQuery();
				
				 List<DonorDetail> donorList = new ArrayList<DonorDetail>();
					while(rs.next()) {
						donorList.add(new DonorDetail(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6) ,rs.getString(7))); 
					}
				return donorList;
			}
			
	
	
	
				


	
	
	
	
	@Override
	public Integer insertRecord(DonorDetailDao object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(DonorDetailDao object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonorDetailDao> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<DonorDetailDao> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	

	public String getDonorName() {
		return donorName;
	}

	public String getPageNum() {
		return pageNum;
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

	public void setState(String state) {
		this.state = state;
	}

	public void setCountry(String country) {
		this.country = country;
	}



	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
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

	public String getAssociation() {
		return association;
	}

	public void setAssociation(String association) {
		this.association = association;
	}


}
