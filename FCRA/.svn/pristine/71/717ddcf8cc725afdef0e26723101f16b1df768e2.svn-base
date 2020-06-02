package service.reports;

import java.util.List;

import dao.reports.AdminDashBoardDao;
import utilities.Commons;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class AdminDashBoardService extends Commons {
	private List<List2> returnList;
	
	public String initGetPendingDetails(){
		begin();
		try {
				getPendingDetails();							
			}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}		
		return "success";
	}
	private void getPendingDetails() throws Exception{
		
		AdminDashBoardDao pdd=new AdminDashBoardDao(connection);		
			pdd.getPendingDetails();
		returnList=pdd.getReturnList();
	
	}
	public List<List2> getReturnList() {
		return returnList;
	}
	public void setReturnList(List<List2> returnList) {
		this.returnList = returnList;
	}
}
