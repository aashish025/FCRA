package dao.reports;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.validation.ValidationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import models.reports.OldRegistrationEntryDtl;
import utilities.GeneratePdfVirtualizer;
import utilities.KVPair;
import dao.BaseDao;

public class OldRegistrationEntryDao extends BaseDao<OldRegistrationEntryDtl, String, String>{
	private String rcn;
	private String myUserId;
	private String myOfficeCode;
	private String tsPath=null;
	private String fileName=null;
	private Map  parameters = new HashMap();
	public String  reportQuery     = "";
	private int virtualizationMaxSize = 200;

	public OldRegistrationEntryDao(Connection connection) {
		super(connection);
	}

	private String generateApplicationId(OldRegistrationEntryDtl object) throws Exception {
		String a="";
		StringBuffer query = new StringBuffer("SELECT FN_RCN_GENERATION(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1,object.getAssoState()+object.getAssoDistrict());
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			a = rs.getString(1);
		}			
		return a;
	}
	
	
	@Override
	public Integer insertRecord(OldRegistrationEntryDtl object)
			throws Exception {
		
		 String RCN=generateApplicationId(object);
		 object.setRcn(RCN);		
		 PreparedStatement statement1 = null;
         int rows1=0,rows2=0,rows3=0;
          
		StringBuffer query = new StringBuffer(
				"INSERT INTO FC_INDIA(RCN,ASSO_NAME,STDIST,ASSO_ADDRESS,ASSO_TOWN_CITY,ADD1,ADD2,ADD3,ASSO_PIN,ASSO_NATURE,ASSO_RELIGION,SECTION_FILENO "
				+ ",REG_DATE,R_CATGRY,R_DATE,STATUS,NEW_OLD,USERID,CANCEL_STATUS,CANCEL_DATE,CANCEL_REASON,CANCEL_USER,CANCEL_REMARKS "
				+ ",CANCEL_REQ_DATE,CANCEL_TYPE,CANCEL_REVOKED,REVOKE_USER,REVOKE_REASON,REVOKE_DATE,PFMS_STATUS,LAST_RENEWED_ON "
				+ ",VALID_FROM,VALID_TO,ASSO_AIMS,CURRENT_STATUS)"
				+ " VALUES(?,?,?,?,?,null,null,null,?,?,?,null,to_timestamp(?,'dd-mm-yyyy HH24:MI:SS'),'N',null,'N','N',?,'N',null,null,null,null,null,null,null,null,null,null,null,to_timestamp(?,'dd-mm-yyyy HH24:MI:SS'),to_date(?,'dd-mm-yyyy'),to_date(?,'dd-mm-yyyy'),?,0)");
		statement1 = connection.prepareStatement(query.toString());
		statement1.setString(1, RCN);	
		statement1.setString(2, object.getAssoName());//
		statement1.setString(3, object.getAssoState()+object.getAssoDistrict());// statedistrict
		statement1.setString(4, object.getAssoAddress());
		statement1.setString(5, object.getAssoTownCity());
		
		statement1.setString(6, object.getAssoPin());
		String a=object.getAssoNature().replace(",","");
		statement1.setString(7,a);
		statement1.setString(8, object.getAssoReligion());
		
		statement1.setString(9, object.getRegDate());
		
		statement1.setString(10, object.getUserId());
		
		statement1.setString(11, object.getRegDate());
		statement1.setString(12, object.getValidFrom());
		statement1.setString(13,object.getValidTo());
		
		statement1.setString(14, object.getAssoAims());
		rows1 = statement1.executeUpdate();
		statement1.close();
	
		if (rows1 > 0) {
			StringBuffer query1 = new StringBuffer("INSERT INTO FC_BANK(NEW_OLD,REQUESTDATE,RCN,BANK_NAME,BANK_ADDRESS,BANK_TOWN_CITY,BANK_ADD1"
					                + ",BANK_ADD2,BANK_ADD3,BANK_PIN,BANK_STDIST,ACCOUNT_NO,STATUS,STATUS_DATE) "
							        + "VALUES('N',null,?,?,?,?,null,null,null,?,?,?,'Y',null)");
			
			statement1 = connection.prepareStatement(query1.toString());
			statement1.setString(1, RCN);
			statement1.setString(2, object.getBankName());
			statement1.setString(3, object.getBankAddress());
			statement1.setString(4, object.getBankTownCity());
			statement1.setString(5, object.getBankZipCode());
			statement1.setString(6, object.getBankState()+object.getBankDistrict());
			statement1.setString(7, object.getAccountNumber());
			rows2 = statement1.executeUpdate();
			statement1.close();
		}
		
		if (rows2 > 0) {
			StringBuffer query2 = new StringBuffer("INSERT INTO T_REGISTRATION_STATUS_HISTORY(RCN,STATUS,REMARKS,STATUS_DATE,ACTION_BY,REFERENCE_FOR_DETAILS)"
							        + "VALUES(?,2,?,sysdate,?,null)");
			
			statement1 = connection.prepareStatement(query2.toString());
			statement1.setString(1, RCN);
			statement1.setString(2, object.getOldregRemark());
			statement1.setString(3, object.getMyUserId());	
			rows3 = statement1.executeUpdate(); 
			statement1.close();
		}
	    setRcn(RCN);
		return rows3;

	}
	
	public int checkRcnNumber() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer query=null;
		int checkRcn=0;  boolean validRcn=false;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query1 = new StringBuffer("SELECT RCN FROM FC_INDIA WHERE RCN=? AND CURRENT_STATUS=0");
		PreparedStatement stmt1=connection.prepareStatement(query1.toString());
		stmt1.setString(1,rcn);
        ResultSet rs=stmt1.executeQuery();
        if (rs.next()) {
        	validRcn = true;
          }
	    stmt1.close();
	    rs.close();
	    if(validRcn == false)
	    	throw new ValidationException("Invalid/Cancelled Registration Number.");
	    query = new StringBuffer("select count(*) from (SELECT RCN FROM FC_INDIA WHERE SECTION_FILENO IS NULL AND RCN=? AND CURRENT_STATUS=0)");
		PreparedStatement stmt=connection.prepareStatement(query.toString());
		stmt.setString(1,rcn);
        ResultSet rsq=stmt.executeQuery();
        if (rsq.next()) {
        	checkRcn = rsq.getInt(1);
          }
        if(checkRcn == 0)
        	throw new ValidationException("Specified Registration Number is new. Kindly re-print Registration Certificate from Workspace");
        return checkRcn;	
		  
	}
	
	
	
	
	public String GenerateReport() throws SQLException, JRException{
		
		String tsPath = "/Reports/OldRegistrationCertificate.jrxml";
		Map parameters = new HashMap();
		StringBuffer query= new StringBuffer("SELECT USER_NAME,(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=(SELECT DESIGNATION_ID FROM TM_USER WHERE "
				+ "USER_ID=?)) AS DESG,(SELECT CONTACT_NO FROM TM_OFFICE WHERE OFFICE_CODE=?) AS CONTACT "
				+ "FROM TM_USER WHERE USER_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1,myUserId);						
		statement.setString(2,myOfficeCode);
		statement.setString(3,myUserId);	
		ResultSet rs=statement.executeQuery();
		while(rs.next()){
			parameters.put("officerName", rs.getString(1));
			parameters.put("officerDesg", rs.getString(2));
			parameters.put("officerContact", rs.getString(3));
		}
		statement.close();
		rs.close();
		StringBuffer query2 =new StringBuffer("SELECT ASSO_NAME,ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE " 
                                 + " SCODE=(SELECT SUBSTR(FC_INDIA.STDIST,1,2) STATE FROM FC_INDIA WHERE SECTION_FILENO IS NULL AND RCN='"+rcn+"'))||', ' || (SELECT DISTNAME FROM TM_DISTRICT "      
                                 + " WHERE DISTCODE=(SELECT SUBSTR(FC_INDIA.STDIST,-3,3)DISTRICT FROM FC_INDIA WHERE SECTION_FILENO IS NULL AND RCN='"+rcn+"'))||', ' || ASSO_PIN,to_char(REG_DATE,'dd-mm-yyyy') as REG_DATE,"
                                 + " (SELECT ACCOUNT_NO FROM FC_BANK WHERE FC_BANK.RCN='"+rcn+"' AND STATUS='Y') AS ACC_NO,"
                                 + " (SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=(SELECT BANK_NAME FROM FC_BANK WHERE RCN='"+rcn+"' AND STATUS='Y')) || ', ' || (SELECT BANK_ADDRESS FROM FC_BANK "      
                                 + " WHERE RCN='"+rcn+"' AND STATUS='Y') || ', ' || (SELECT BANK_TOWN_CITY FROM FC_BANK WHERE RCN='"+rcn+"' AND STATUS='Y') || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=(SELECT SUBSTR(FC_BANK.BANK_STDIST,1,2) BNKSTATE "
                                 + " FROM FC_BANK WHERE RCN='"+rcn+"' AND STATUS='Y')) || ', ' ||" 
                                 + " (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(SELECT SUBSTR(FC_BANK.BANK_STDIST,-3,3)DISTRICT FROM FC_BANK WHERE RCN='"+rcn+"' AND STATUS='Y')) || ', ' ||" 
                                 + " (SELECT BANK_PIN FROM FC_BANK WHERE RCN='"+rcn+"' AND STATUS='Y') BANK,RCN,USERID "
                                 + " FROM FC_INDIA WHERE RCN='"+rcn+"' ");
		PreparedStatement statement1 = connection.prepareStatement(query2.toString()); 		
		ResultSet rsReg=statement1.executeQuery();	 
		if(rsReg.next()){			
			parameters.put("assoName", rsReg.getString(1));			
			parameters.put("assoAddress", rsReg.getString(2));
			parameters.put("regDate", rsReg.getString(3));			
			parameters.put("accNumber", rsReg.getString(4));
			parameters.put("bankDetails", rsReg.getString(5));
			parameters.put("rcn", rsReg.getString(6).substring(0, rsReg.getString(6).length()-1));
			parameters.put("userName",rsReg.getString(7));
		}
		statement1.close();
		rsReg.close();
		StringBuffer query7 = new StringBuffer("SELECT ASSO_NATURE FROM FC_INDIA WHERE RCN='"+rcn+"' AND SECTION_FILENO IS NULL");
		PreparedStatement statement3 = connection.prepareStatement(query7.toString()); 			
		ResultSet rs4=statement3.executeQuery();
		String assoNature=null;
		if(rs4.next()){
			assoNature=rs4.getString(1);
		} 
		statement3.close();
		rs4.close();
		String assoNatureDesc="";
		ResultSet rs5=null;
		StringBuffer query8 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
		PreparedStatement statement5 = connection.prepareStatement(query8.toString());
		for(int i=0;i<assoNature.length();i++){
			String natureCode = assoNature.substring(i, i+1);
			statement5.setString(1, natureCode);
			String delim=(i==0?"":",");
			rs5 = statement5.executeQuery();
			if(rs5.next()){
				assoNatureDesc=assoNatureDesc+delim+rs5.getString(1);
			}
		}
		statement5.close();
		rs5.close();
		parameters.put("assoNature", assoNatureDesc);  
		return GeneratePdfVirtualizer.asAttachment(tsPath,parameters,connection,"Old-Registration-Certificate");

}
	
	
	
	@Override
	public Integer removeRecord(OldRegistrationEntryDtl object)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OldRegistrationEntryDtl> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(
			List<OldRegistrationEntryDtl> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRcn() {
		return rcn;
	}

	public void setRcn(String rcn) {
		this.rcn = rcn;
	}

	public String getMyUserId() {
		return myUserId;
	}

	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}

	public String getMyOfficeCode() {
		return myOfficeCode;
	}

	public void setMyOfficeCode(String myOfficeCode) {
		this.myOfficeCode = myOfficeCode;
	}


	

}
