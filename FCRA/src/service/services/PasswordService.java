package service.services;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.master.ParentMenu;
import models.master.Role;
import models.master.SubMenu;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.UserSession;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class PasswordService extends Commons {
	public String changePassword(String currentPassword, String newPassword) {
		begin();
		try {
			StringBuffer query = new StringBuffer(
					"SELECT fn_change_password(?,?,?) FROM DUAL");
			PreparedStatement statement = connection.prepareStatement(query
					.toString());
			statement.setString(1, myUserId);
			statement.setString(2, currentPassword);
			statement.setString(3, newPassword);
			statement.executeUpdate();
		} catch (Exception e) {
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException e2) {
				ps(e2);
			}
			ps(e);
		}
		finish();
		return "success";
	}

	public String execute(String oldPassword, String newPassword1,
			String newPassword2) {
		String result = "error";
		try {
			begin();
			if (ESAPI.validator().isValidInput("Password", oldPassword,
					"Password", 40, false) == false) {
				notifyList
						.add(new Notification(
								"Error :",
								"Password should contain alphabets, numbers, and special symbols.",
								Status.ERROR, Type.BAR));
				return result;
			}
			if (ESAPI.validator().isValidInput("Password", newPassword1,
					"Password", 40, false) == false) {
				notifyList
						.add(new Notification(
								"Error :",
								"Password should contain alphabets, numbers, and special symbols.",
								Status.ERROR, Type.BAR));
				return result;
			}
			if (ESAPI.validator().isValidInput("Password", newPassword2,
					"Password", 40, false) == false) {
				notifyList
						.add(new Notification(
								"Error :",
								"Password should contain alphabets, numbers, and special symbols.",
								Status.ERROR, Type.BAR));
				return result;
			}
			if (newPassword1.equals(newPassword2) == false) {
				notifyList.add(new Notification("Error :",
						"Re-typed password does not match with new password.",
						Status.ERROR, Type.BAR));
				return result;
			}
		} catch (Exception e) {
			try {
				notifyList.add(new Notification("Error :",
						"Server Error. Please try after sometime.",
						Status.ERROR, Type.BAR));
			} catch (Exception ex) {
			}
			return result;
		}
		UserSession authuser = null;
		authuser = myUser;
		if (authuser == null) {
			return "authFail";
		}
		try {
			Integer status = null;
			query = new StringBuffer(
					"SELECT fn_change_password(?,?,?) FROM DUAL");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, myUserId);
			statement.setString(2, oldPassword);
			statement.setString(3, newPassword1);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				status = resultSet.getInt(1);
			}
			switch (status) {
			case 0:
				notifyList.add(new Notification("Success :",
						"Password changed Successfully.", Status.SUCCESS,
						Type.BAR));
				result = "passwordchanged";
				break;
			case 1:
				notifyList.add(new Notification("Error :",
						"Recently Used Password.", Status.ERROR, Type.BAR));
				result = "error";
				break;
			case 2:
				notifyList.add(new Notification("Error :",
						"Current Password is wrong.", Status.ERROR, Type.BAR));
				result = "error";
				break;
			default:
				notifyList.add(new Notification("Error :",
						"Server Error. Please contact administrator.",
						Status.ERROR, Type.BAR));
				result = "error";
			}
			if (result.equals("passwordchanged") == true) {
				if (authuser.getForcePasswordChange()) {
					List<Role> roleList = authuser.getRoleList();
					queryFields = new StringBuffer(roleList.get(0).getRoleId()
							.toString());
					for (int i = 1; i < roleList.size(); i++) {
						queryFields.append("," + roleList.get(i).getRoleId());
					}
					query = new StringBuffer(
							"SELECT DISTINCT A.PMENU_ID,A.PMENU_NAME,B.SMENU_ID,B.SMENU_NAME,B.ACTION_PATH,A.DISPLAY_ORDER "
									+ "FROM TM_PARENTMENU A,TM_SUBMENU B,TM_ROLE_MENU C WHERE "
									+ "C.ROLE_ID IN("
									+ queryFields
									+ ") AND C.RECORD_STATUS=0 AND B.SMENU_ID=C.SMENU_ID AND B.RECORD_STATUS=0 AND "
									+ "A.PMENU_ID=B.PMENU_ID AND A.RECORD_STATUS=0 ORDER BY A.DISPLAY_ORDER ASC,B.SMENU_NAME ASC");
					String[] qv_7 = {};
					String ot = pullResultSet(qv_7, "navigation menu for user",
							myUserId, "", true, Type.BAR);
					if (ot.equals("all")) {
						List<ParentMenu> parentMenuList = new ArrayList<ParentMenu>();
						boolean permit = false;
						do {
							String pMenuId = new String(resultSet.getString(1));
							ParentMenu parentMenu = new ParentMenu();
							parentMenu.setPmenuId(resultSet.getShort(1));
							parentMenu.setPmenuName(resultSet.getString(2));
							List<SubMenu> subMenuList = new ArrayList<SubMenu>();
							do {
								SubMenu subMenu = new SubMenu();
								subMenu.setSmenuId(resultSet.getShort(3));
								subMenu.setSmenuName(resultSet.getString(4));
								subMenu.setActionPath(resultSet.getString(5));
								subMenuList.add(subMenu);
								permit = resultSet.next();
							} while (permit
									&& pMenuId.equals(resultSet.getString(1)));
							parentMenu.setSubMenuList(subMenuList);
							parentMenuList.add(parentMenu);
						} while (permit);
						authuser.setParentMenuList(parentMenuList);
					}
					authuser.setForcePasswordChange(false);
					httpSession.setAttribute("user", authuser);
					result = "passwordForcefullyChanged";
				}
			}
		} catch (Exception e) {
			errorStatus(Type.BAR);
			ps(e);
		}
		finish();
		return result;
	}

	/**
	 * This method checks whether the user password has expired or not. If the
	 * user password has expired, then the user gets a notification to change
	 * his password.
	 * 
	 * @return
	 */
	public String checkPasswordStatus() {
		begin();
		String result = "error";
		UserSession authuser = null;
		authuser = myUser;
		try {
			if (authuser.getForcePasswordChange()) {
				notifyList
						.add(new Notification(
								"",
								"Your password has been expired. Please change to continue.",
								Status.INFORMATION, Type.BAR));
				result = "error";
			}
		} catch (Exception e) {
			errorStatus(Type.BAR);
			ps(e);
		}
		finish();
		return result;
	}
}