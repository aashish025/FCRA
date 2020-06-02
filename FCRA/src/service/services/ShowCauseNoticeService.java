package service.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import utilities.Commons;
import utilities.GeneratePdfVirtualizer;

public class ShowCauseNoticeService extends Commons{

	private String appId;
	private String chatId;
	private Map  parameters = new HashMap();
	private String tsPath = null;
	private String fileName = null;
	
	public void GetShowCauseNotice(HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		begin();
		String query = null;
		ResultSet rs;
		query="SELECT USER_NAME,(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=(SELECT DESIGNATION_ID FROM TM_USER WHERE "
				+ "USER_ID=?)) AS DESG,(SELECT CONTACT_NO FROM TM_OFFICE WHERE OFFICE_CODE=?) AS CONTACT ,(SELECT ADDRESS FROM TM_OFFICE WHERE OFFICE_CODE=?) AS address,"
				+ " (SELECT CITY_NAME FROM TM_OFFICE WHERE OFFICE_CODE=?) AS city ,( select ZIPCODE FROM TM_OFFICE WHERE OFFICE_CODE=?) AS pinCode "
				+ "FROM TM_USER WHERE USER_ID=?";
		statement=connection.prepareStatement(query);
		statement.setString(1, myUserId);
		statement.setString(2, myOfficeCode);
		statement.setString(3, myOfficeCode);
		statement.setString(4, myOfficeCode);
		statement.setString(5, myOfficeCode);
		statement.setString(6, myUserId);
		rs=statement.executeQuery();
		while(rs.next()){
			parameters.put("officerName", rs.getString(1));
			parameters.put("officerDesg", rs.getString(2));
			parameters.put("officerContact", rs.getString(3));
			parameters.put("officeAddress", rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6));
		}	
		statement.close();
		rs.close();
		query="SELECT ASSO_REGISTERED_FLAG, ASSO_RCN, ASSO_NAME, ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE= ASSO_STATE)||', ' || "
				+ "	(SELECT DISTNAME FROM TM_DISTRICT  WHERE DISTCODE=ASSO_DISTRICT and scode =ASSO_STATE)||', ' || ASSO_PIN FROM T_GRIEVANCES WHERE APPLICATION_ID =?";
		statement = connection.prepareStatement(query); 	
		statement.setString(1, appId);
		rs=statement.executeQuery();	 
		if(rs.next()){		
			if(rs.getString(1).equals("Y")){
				String rcn = rs.getString(2);
				query = " SELECT ASSO_NAME, CASE WHEN NEW_OLD='N' THEN NVL(ASSO_ADDRESS,' ')||','||NVL(ASSO_TOWN_CITY,' ')|| NVL(ASSO_PIN,'') ELSE NVL(ADD1,' ')||', '||NVL(ADD2,' ')||','||NVL(ADD3,' ')||'-'||  NVL(ASSO_PIN,'') END ADDR FROM FC_INDIA WHERE RCN =?";
				statement = connection.prepareStatement(query);
				statement.setString(1, rcn);
				ResultSet resultSet = statement.executeQuery();
				if(resultSet.next()){
					parameters.put("assoName", resultSet.getString(1));			
					parameters.put("assoAddress", resultSet.getString(2));	
				}
			}
			else{
				parameters.put("assoName", rs.getString(3));			
				parameters.put("assoAddress", rs.getString(4));	
			}
		}
		parameters.put("appId", appId);
		parameters.put("chatId", chatId);
		tsPath="/Reports/ShowCauseNotice.jrxml";
		fileName = "Show Cause Notice";
		//GeneratePdfVirtualizer.asInlineWithDB(tsPath, parameters, connection, fileName);
		GeneratePdfVirtualizer.asAttachmentWithDB(tsPath, parameters, connection, fileName);
	}
	
	public String getAppId() {
		return appId;
	}
	public String getChatId() {
		return chatId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	
}
