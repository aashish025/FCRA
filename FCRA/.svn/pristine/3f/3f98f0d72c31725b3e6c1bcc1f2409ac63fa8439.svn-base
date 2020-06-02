package service.auth;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import utilities.Commons;

public class LogoutService extends Commons{
	public void initLogout(){
		doLogout();		
	}
	private void doLogout(){
		try {
				String status=null;
			  	begin();
			  	StringBuffer query = new StringBuffer("SELECT FN_LOGOUT_USER(?) FROM DUAL");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				statement.setString(1, myUserId);
				ResultSet rs = statement.executeQuery();
				if(rs.next()==true){
					status=rs.getString(1);
					// status=0-Success
				}
				if(!nob(status)){
					StringTokenizer st = new StringTokenizer(status, "-");
					String dbResult = st.nextToken();					
					if(dbResult.equals("0")){						
						query=new StringBuffer("update t_accesslog_details set logout_time=sysdate where session_id=? and user_id=?");
						statement=connection.prepareStatement(query.toString());
						statement.setString(1, sessionId);
						statement.setString(2, myUserId);
						statement.executeUpdate();
						doInvalidate();
					}else if(dbResult.equals("1")){
						//
					}					
				}								
		} catch (Exception e) {			
			e.printStackTrace();
		}
		finally{
			finish();
		}
	}
	private void doInvalidate(){
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		httpSession = attr.getRequest().getSession(true);		
		httpSession.removeAttribute("user");
		httpSession.invalidate();
	}
}
