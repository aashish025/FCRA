package web.interceptors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import models.master.ParentMenu;
import models.master.SubMenu;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import utilities.DatabaseConnection;
import utilities.UserSession;

@Component
public class Authentication extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session=request.getSession();
		UserSession user=(UserSession)session.getAttribute("user");
		String applicationContext=request.getContextPath();
		String[] loginURL={applicationContext+"/",applicationContext+"/login",applicationContext};
		String[] logoutURL={applicationContext+"/logout"};
		String[] captchaURL={applicationContext+"/pull-captcha-login"};
		String[] docsURL={applicationContext+"/docs/",applicationContext+"/previousdocs/"};
		String[] resetURL={"reset-password"};
		String[] exemptedURL={"home","reset-password"};
		String cURL=request.getRequestURI();
		String actionName = request.getServletPath().substring(1); /* action name without .action extension*/
		String action = actionName + ".action";
		boolean loginFlag=false;
		boolean logoutFlag=false;
		boolean captchaFlag=false;
		boolean resetFlag=false;
		boolean exemptedFlag=false;
		for(String url:loginURL){
			if(cURL.equalsIgnoreCase(url)){
				loginFlag=true;
				break;
			}
		}
		for(String url:logoutURL){
			if(cURL.equalsIgnoreCase(url)){
				logoutFlag=true;
				break;
			}
		}
		for(String url:captchaURL){
			if(cURL.equalsIgnoreCase(url)){
				captchaFlag=true;
				break;
			}
		}
		for(String url:resetURL){
			if(action.endsWith(url) == true || actionName.endsWith(url) == true){
				resetFlag=true;
				break;
			}
		}
		for(String url:exemptedURL){
			//if(cURL.equalsIgnoreCase(url) || cURL.endsWith(url))
			if(action.endsWith(url) == true || actionName.endsWith(url) == true)
			{
				exemptedFlag=true;
				break;
			}
		}
		for(String url:docsURL){
			//if(cURL.equalsIgnoreCase(url) || cURL.endsWith(url))
			if(cURL.contains(url) == true || actionName.contains(url) == true)
			{
				exemptedFlag=true;
				break;
			}
		}
		if(user==null && loginFlag==true){
			//continue
		}else if(user!=null && logoutFlag==true){
			//continue;
		}else{
			if(user==null){
				//no session
				//redirect to login page or send status code if ajax request.
				if(isAjax(request)){
					if(captchaFlag == false && resetFlag == false)
						response.sendError(401, "Unauthorized");
			 	}else{
			 		response.sendRedirect("login");
			 		return false;
			 	}
			}else{//user present //check wether authorized to access URL in cURL
				if(loginFlag==true || logoutFlag==true || captchaFlag == true) {
					//continue
				}
				else if(exemptedFlag == false) {
					String result = isAuthorized(request, user);
					if(result == null) {
						// Not authorized
						System.out.println(user.getUserId() + " is not authorized to access  " + request.getRequestURI());
						/* */
						response.sendError(403, "Forbidden");
						return false;
						/* */
					} else if(result.equals("dualLogin")) {
						// Dual Login
						session.setAttribute("user", null);
						if(isAjax(request)){
							if(captchaFlag == false)
								response.sendError(401, "Unauthorized");
					 	}else{
					 		response.sendRedirect("login");
					 		return false;
					 	}
					} else if(result.equals("error")) {
						// Error
					}
				}
			}
		}
		System.out.println("GreetingInterceptor: REQUEST Intercepted for URI: "
				+ request.getRequestURI());
		request.setAttribute("greeting", "Happy Diwali!");
		return true;
	}
	private boolean isAjax(HttpServletRequest request) {//only works when AJAX request is made through jquery functions
	    String requestedWithHeader = request.getHeader("X-Requested-With");
	    return "XMLHttpRequest".equals(requestedWithHeader);
	}
	private String isAuthorized(HttpServletRequest request, UserSession myUser) {
		Connection connection = null;
		String action = null;
		String actionName = null;
		String responseType = null;
		String action_link="action_link";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer query = null;
		
		try {
			actionName = request.getServletPath().substring(1); /* action name without .action extension*/
			action = actionName + ".action";
			
			connection = DatabaseConnection.getConnection(true);
			
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession httpSession = attr.getRequest().getSession(true);
		    String sessionId=httpSession.getId();							
			
			query=new StringBuffer("SELECT SESSION_ID FROM TM_USER WHERE USER_ID=?");
			pstmt = connection.prepareStatement(query.toString());
			pstmt.setString(1, myUser.getUserId());
			rs=pstmt.executeQuery();
			if(rs.next()){
				if(!rs.getString(1).equals(sessionId)){
					responseType = "dualLogin";
				}
			}
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
			
			if(responseType == null) {
				List<ParentMenu> parentMenuList=myUser.getParentMenuList();
				for(int j=0;j<parentMenuList.size();j++){
					List<SubMenu> subMenuList=parentMenuList.get(j).getSubMenuList();
					for(int i=0;i<subMenuList.size();i++){
						SubMenu subMenu=subMenuList.get(i);
						if(action.endsWith(subMenu.getActionPath()) == true || actionName.endsWith(subMenu.getActionPath()) == true){
							responseType = "success";
							if(actionName.equals(subMenu.getActionPath()) || action.equals(subMenu.getActionPath())){
								//actionParams=context.getParameters();
								//param=(String[])actionParams.get("oper");
								query=new StringBuffer("SELECT  action_link FROM (SELECT * FROM t_actionlog_details where user_id=? and session_id=? ORDER BY action_time DESC) WHERE  ROWNUM = 1");
								/*
								String[] qv_1={myUserId,ServletActionContext.getRequest().getSession().getId()};
								String ot=pullResultSet(qv_1, "", "", "", false, Type.NONE);
								if(ot.equals("all")){
									action_link=resultSet.getString("action_link");
								}
								*/
								pstmt = connection.prepareStatement(query.toString());
								//sessionId=ServletActionContext.getRequest().getSession().getId();
								if(sessionId.lastIndexOf(".")!=-1)
									sessionId=sessionId.substring(0, sessionId.lastIndexOf("."));
								pstmt.setString(1, myUser.getUserId());
						    	pstmt.setString(2, sessionId);
						    	
								rs=pstmt.executeQuery();
								if(rs.next())
								{
									action_link=rs.getString("action_link");
								}
								rs.close();
								rs = null;
								pstmt.close();
								pstmt = null;
								
								query=new StringBuffer("insert into  t_actionlog_details(user_id,session_id,action_time,action_link) values(?,?,sysdate,?)");
						    	if(!actionName.equals(action_link)){
						    		//if (param!=null){
							    	//	actionName=actionName+"?opr="+param[0];
							    	//}
						    		/*
						    		String[] qv_2={myUserId,ServletActionContext.getRequest().getSession().getId(),actionName};
									push(1, qv_2, "", "", "", false, Type.NONE);
									*/
							    	pstmt = connection.prepareStatement(query.toString());
							    	
							    	pstmt.setString(1, myUser.getUserId());
							    	pstmt.setString(2, sessionId);
							    	pstmt.setString(3, actionName);
							    
							    	pstmt.executeUpdate();
							    	pstmt.close();
						    		pstmt=null;
						    	}
							}
							break;
						}
					}
				}
			}
			connection.commit();
			connection.close();
			connection = null;			
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(rs != null) {
					rs.close();
					rs = null;
				}
				if(pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
					
				if(connection != null) {
					connection.rollback();
					connection.close();
					connection = null;
				}
			} catch(Exception ef){}
			responseType="error";
		} finally {
			try{
				if(rs != null) {
					rs.close();
					rs = null;
				}
				if(pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch(Exception e){}
		}
		return responseType;
	}
}