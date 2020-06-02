package service.auth;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import models.master.ParentMenu;
import models.master.SubMenu;
import utilities.Commons;

public class HomeService extends Commons{
	private String menuString;
	private String userName;
	private String userDesignation;
	private String userId;
	private String proceed;
	private String officeAddress;
	List<ParentMenu> menuList;

	private void generateMenu() throws Exception{
		List<ParentMenu> parentMenuList=myUser.getParentMenuList();
		menuList = parentMenuList;
		/* *
		StringBuffer menu=new StringBuffer("<ul class=\"navigation\">");
		for(int i=0;i<parentMenuList.size();i++){
			List<SubMenu> subMenuList=parentMenuList.get(i).getSubMenuList();
			menu.append("<li class=\"openable\">" +
							"<a style=\"cursor:pointer;\"><span class=\"isw-grid isw-"+parentMenuList.get(i).getPmenuId()+"\"></span><span class=\"text\">"+parentMenuList.get(i).getPmenuName()+"</span></a>" +
								"<ul>");
			for(int j=0;j<subMenuList.size();j++){
				SubMenu subMenu=subMenuList.get(j);
				menu.append("<li>" +
								"<a style=\"cursor:pointer;\" id=\""+subMenu.getActionPath()+"\" onclick=\"javascript:switchContent('"+subMenu.getActionPath()+"');\">" +
									"<span class=\"icon-comment icon-"+subMenu.getSmenuId()+"\"></span>" +
									"<span class=\"text\">"+subMenu.getSmenuName()+"</span>" +
								"</a>" +
							"</li>");
			}
			menu.append("</ul>" +
					"</li>");
		}
		menu.append("</ul>");
		* */
		StringBuffer menu=new StringBuffer("<ul class=\'nav bs-docs-sidenav\'>");
		for(int i=0;i<parentMenuList.size();i++){
			List<SubMenu> subMenuList=parentMenuList.get(i).getSubMenuList();
			menu.append("<li class=\'active\'>" +
							"<a >"+parentMenuList.get(i).getPmenuName()+"</a>" +
								"<ul class=\'nav \'>");
			for(int j=0;j<subMenuList.size();j++){
				SubMenu subMenu=subMenuList.get(j);
				menu.append("<li>" +
								"<a id=\'"+subMenu.getActionPath()+"\' onclick=\'javascript:switchContent(\'"+subMenu.getActionPath()+"\');\'>" +
									subMenu.getSmenuName() +
								"</a>" +
							"</li>");
			}
			menu.append("</ul>" +
					"</li>");
		}
		menu.append("</ul>");
		menuString=menu.toString();
	}
	public String home(){
		begin();
		try{
			generateMenu();
			userName=myUser.getUserName();
			userDesignation=myUser.getUserDesignation();
			userId=myUser.getUserId();
			officeAddress=myUser.getOffice().getOfficeCode()+", "+myUser.getOffice().getStateName()+", "+myUser.getOffice().getCountry()+"\n Email: "+
						myUser.getOffice().getEmailId()+" Time Zone: "+myUser.getOffice().getTimeZone();
		}catch(Exception e){
			ps(e);
		}
		finish();
		return "success";
	}
	private void timeZones(){
		String[] ai=TimeZone.getAvailableIDs();
		for(int i=0;i<ai.length;i++){
			if(ai[i].equals(myUser.getOffice().getTimeZone().getZoneName())){
				TimeZone tz=TimeZone.getTimeZone(ai[i]);
				Calendar c = Calendar.getInstance(tz);
				spl(ai[i]+" - "+c.get(Calendar.DAY_OF_MONTH)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.YEAR)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));
			}
		}
	}
	
	public String getMenuString() {
		return menuString;
	}
	public void setMenuString(String menuString) {
		this.menuString = menuString;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProceed() {
		return proceed;
	}
	public void setProceed(String proceed) {
		this.proceed = proceed;
	}
	public String getMyDateTime() {
		return myDateTime;
	}
	public void setMyDateTime(String myDateTime) {
		this.myDateTime = myDateTime;
	}
	public String getIstDateTime() {
		return istDateTime;
	}
	public void setIstDateTime(String istDateTime) {
		this.istDateTime = istDateTime;
	}
	public long getMyDateTimeL() {
		return myDateTimeL;
	}
	public void setMyDateTimeL(long myDateTimeL) {
		this.myDateTimeL = myDateTimeL;
	}
	public long getIstDateTimeL() {
		return istDateTimeL;
	}
	public void setIstDateTimeL(long istDateTimeL) {
		this.istDateTimeL = istDateTimeL;
	}
	public long getIstOffset() {
		return istOffset;
	}
	public void setIstOffset(long istOffset) {
		this.istOffset = istOffset;
	}
	public long getLocalOffset() {
		return localOffset;
	}
	public void setLocalOffset(long localOffset) {
		this.localOffset = localOffset;
	}
	public List<ParentMenu> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<ParentMenu> menuList) {
		this.menuList = menuList;
	}
	public String getUserDesignation() {
		return userDesignation;
	}
	public void setUserDesignation(String userDesignation) {
		this.userDesignation = userDesignation;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
}
