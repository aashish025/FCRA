package service.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;

import dao.services.NotificationDao;
import models.reports.PendencyReport;
import models.services.Notification;
import utilities.Commons;
import utilities.KVPair;
import utilities.lists.List3;
import utilities.lists.List4;
import utilities.lists.List5;

public class HomeNotificationDetailsService extends Commons{
	private String notificationType;
	private List<Notification> notificationList;
	private List<Notification> notificationAttachmentList;
	private Map<String,String> newNotificationList;
	private List<KVPair<String,String>> rcnBarGraphYearWise;
	private List<KVPair<String,String>> donorWiseBarGraph;
	private List<KVPair<String,String>> donorFinYearWiseBarGraph;
	private List<KVPair<String,String>> assoWiseBarGraph;
	private List<KVPair<String,String>> assoFinYearWiseBarGraph;
	private List<KVPair<String,String>> countryWiseBarGraph;
	private List<KVPair<String,String>> countryFinYearWiseBarGraph;
	private List<KVPair<String, String>>suddenRiseIncomeWiseBarGraph;
	private String currentBlockYear;
	private List<String> blockYearList;
	private String graphType;
	private List<List3> applicationStatistics;
	private List<List3> applicationServiceStatistics;
	private List<List5> ApplicationOrganisationStatistics;
	private List<List5> ApplicationOrganisationStatisticsmiddle;
	private List<List5> ApplicationOrganisationStatisticslast;
	private List<List5> ApplicationDesignationStatistics;
	private List<List5> ApplicationDesignationStatisticsmiddle;
	private List<List5> ApplicationDesignationStatisticslast;
	private List<List5> ApplicationListStatistics;
	private List<List3> PriorPermissionDesignationStatistics;
	private List<List5> PriorPermissionDesignationStatisticsmiddle;
	private List<List5> PriorPermissionDesignationStatisticslast;
	private List<List5> RenewalOrganisationStatistics;
	private List<List5> PriorPermissionListStatistics;
	private List<List3> RenewalDesignationStatistics;
	private List<List5> RenewalDesignationStatisticsmiddle;
	private List<List5> RenewalDesignationStatisticslast;
	private List<List5> RenewaListStatistics;
	private List<List4> DashboardServiceStatistics;
	private List<List3> RegistrationDesignationStatistics;
	private List<List5> PeriorPermissionOrganisationStatistics;
	private List<List3> PeriorPermissionDesignationStatistics;
	private List<List5> HospitalityOrganisationStatistics;
	private List<List3> HospitalityDesignationStatistics;
	private MultiValueMap<String, String> parameterMap;
	//private List<PendencyReport> pendencyReportList;
	private String PendencyCountReportDegwise;
	private List<PendencyReport> PendencyReportDegwise;
	public String pullNewNotificationCountList(){
		String ret = "success";
		begin();
		try {
			populateNewNotificationCountList();
			populateRCNBarGraphYearWisde();
			populateApplicationStatisticsBarGraph();
			populateApplicationServiceWise();
			populateDashboardServiceStatistics();
			populateApplicationOrganisationWise();
			//populateApplicationOrganisationWisemiddle();
			//populateApplicationOrganisationWiselast();
			//populateApplicationDesignationStatisticslast();
			//populateApplicationDesignationStatisticsmiddle();
			//populateRegistrationDesignationStatistics();
			//populatePeriorPermissionOrganisationStatistics();
			//populateRenewalOrganisationStatistics();
			//populateApplicationDesignationStatistics();
			//populateApplicationListStatistics();
			//populatePriorPermissionDesignationStatistics();
			//populatePriorPermissionDesignationStatisticsmiddle();
			//populatePriorPermissionDesignationStatisticslast();
			//populatePriorPermissionListStatistics();
			//populateRenewalDesignationStatistics();
			//populateRenewalDesignationStatisticsmiddle();
			//populateRenewalDesignationStatisticslast();
			//populateRenewaListStatistics();
			//populateHospitalityOrganisationStatistics();
			//populateHospitalityDesignationStatistics();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}

	public String pullDashboard(HttpServletRequest req){
		String ret = "success";
		begin();
		try {
			populateDashboardServiceStatistics();
			populateApplicationOrganisationWise();
			populateRegistrationDesignationStatistics(req);
		//	populatePeriorPermissionOrganisationStatistics();
		//	populateRenewalOrganisationStatistics();
		//	populateApplicationDesignationStatistics();
		//	populatePriorPermissionDesignationStatistics();
		//	populateRenewalDesignationStatistics();
		//	populateHospitalityOrganisationStatistics();
		//	populateHospitalityDesignationStatistics();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	public String pullPriorpermission(HttpServletRequest req){
		String ret = "success";
		begin();
		try {
		//	populateDashboardServiceStatistics();
		//	populateApplicationOrganisationWise();
		//	populateRegistrationDesignationStatistics();
			populatePeriorPermissionOrganisationStatistics();
		//	populateRenewalOrganisationStatistics();
		//	populateApplicationDesignationStatistics();
			populatePriorPermissionDesignationStatistics(req);
		//	populateRenewalDesignationStatistics();
		//	populateHospitalityOrganisationStatistics();
		//	populateHospitalityDesignationStatistics();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	public String pullRenewal(HttpServletRequest req){
		String ret = "success";
		begin();
		try {
		//	populateDashboardServiceStatistics();
		//	populateApplicationOrganisationWise();
		//	populateRegistrationDesignationStatistics();
		//	populatePeriorPermissionOrganisationStatistics();
			populateRenewalOrganisationStatistics();
		//	populateApplicationDesignationStatistics();
		//	populatePriorPermissionDesignationStatistics();
			populateRenewalDesignationStatistics(req);
		//	populateHospitalityOrganisationStatistics();
		//	populateHospitalityDesignationStatistics();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	public String pullHospitality(HttpServletRequest req){
		String ret = "success";
		begin();
		try {
			//populateDashboardServiceStatistics();
			//populateApplicationOrganisationWise();
		//	populateRegistrationDesignationStatistics();
		//	populatePeriorPermissionOrganisationStatistics();
		//	populateRenewalOrganisationStatistics();
		//	populateApplicationDesignationStatistics();
			//populatePriorPermissionDesignationStatistics();
			//populateRenewalDesignationStatistics();
			populateHospitalityOrganisationStatistics();
			populateHospitalityDesignationStatistics(req);
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	public String pullBarGraphCountList(){
		String ret = "success";
		begin();
		try {
			populateDonorWiseBarGraph();
			populateAssoWiseBarGraph();
			populateCountryWiseBarGraph();
			populateBlockYearForBarGraph();
			populateDonorFinYearWiseBarGraph();
			populateAssoFinYearWiseBarGraph();
			populateCountryFinYearWiseBarGraph();
			populateBlockYearListForBarGraph();			
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}	
	
	public String pullNotificationList() {
		String ret = "success";
		begin();
		try {
			populateNotificationList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	public String pullSelectedNotificationList() {
		String ret = "success";
		begin();
		try {
			populateSelectedNotificationList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	public String pullNotificationAttachmentList(){
		String ret = "success";
		begin();
		try {
			populateNotificationAttachmentList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	public String pullRegistrationList() {
		// TODO Auto-generated method stub

		String ret = "success";
		begin();
		try {
			populateRCNBarGraphYearWisde();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	
	}

	public String pullDonorList() {
		// TODO Auto-generated method stub

		String ret = "success";
		begin();
		try {
			populateDonorWiseBarGraph();
			populateBlockYearForBarGraph();
			populateDonorFinYearWiseBarGraph();
			populateBlockYearListForBarGraph();		
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	
	}

	public String pullCountryList() {
		// TODO Auto-generated method stub

		String ret = "success";
		begin();
		try {
			populateCountryWiseBarGraph();
			populateBlockYearForBarGraph();
			populateCountryFinYearWiseBarGraph();
			populateBlockYearListForBarGraph();		
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	
	}

	public String pullAssociationList() {
		// TODO Auto-generated method stub

		String ret = "success";
		begin();
		try {
			populateAssoWiseBarGraph();
			populateBlockYearForBarGraph();
			populateAssoFinYearWiseBarGraph();
			populateBlockYearListForBarGraph();		
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	
	}

	public String pullApplicationList() {
		// TODO Auto-generated method stub

		String ret = "success";
		begin();
		try {
			populateApplicationStatisticsBarGraph();
			populateApplicationServiceWise();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	
	}
	public String pendencyreportdegwise(HttpServletRequest req) throws Exception {
		String ret = "success";
		begin();
		try {
			//populateApplicationStatisticsBarGraph();
			//populateApplicationServiceWise();
			populatePendencyReportDegwise();
			populatePendencyCountReportDegwise(req);
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
		
	}
	private void populateNewNotificationCountList() throws Exception {
		NotificationDao ndao=new NotificationDao(connection);
		ndao.setNotificationType(notificationType);
		newNotificationList=ndao.getNewNotificationCountList();
	}
   private void populateRCNBarGraphYearWisde() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   rcnBarGraphYearWise=ndao.getRcnBarGraphYearWise();
   }
   
   private void populateBlockYearForBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   currentBlockYear = ndao.getCurrentBlockYear();
   }

   private void populateBlockYearListForBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   blockYearList = ndao.getBlockYearList();
   }   
   
   private void populateDonorWiseBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   donorWiseBarGraph = ndao.getDonorWiseBarGraph();
   } 

   private void populateDonorFinYearWiseBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   donorFinYearWiseBarGraph = ndao.getDonorFinYearWiseBarGraph();
   } 
   
   private void populateAssoWiseBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   assoWiseBarGraph = ndao.getAssoWiseBarGraph();
   }   
   private void populateAssoFinYearWiseBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   assoFinYearWiseBarGraph = ndao.getAssoFinYearWiseBarGraph();
   }   
   
   private void populateCountryWiseBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   //assoWiseBarGraph = ndao.getAssoWiseBarGraph();
	   countryWiseBarGraph = ndao.getCountryWiseBarGraph();
   }  
   private void populateCountryFinYearWiseBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   //dono=ndao.getRcnBarGraphYearWise();
	   //assoWiseBarGraph = ndao.getAssoWiseBarGraph();
	   countryFinYearWiseBarGraph = ndao.getCountryFinYearWiseBarGraph();
   }   
 
   private void populateApplicationStatisticsBarGraph() throws Exception{
	   NotificationDao ndao=new NotificationDao(connection);
	   applicationStatistics=ndao.getApplicationStatistics();   
   }
	private void populateNotificationList() throws Exception {
		NotificationDao ndao=new NotificationDao(connection);
		ndao.setNotificationType(notificationType);
		notificationList=ndao.getAll();
	}
	
	private void populateSelectedNotificationList() throws Exception {
		NotificationDao ndao=new NotificationDao(connection);
		ndao.setNotificationType(notificationType);
		notificationList=ndao.getSelectedNotification();
	}
	
	private void populateNotificationAttachmentList() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		ndao.setNotificationType(notificationType);
		notificationAttachmentList=ndao.getNotificationAttachment();
	}
	
	 private void populateApplicationServiceWise() throws Exception{
		   NotificationDao ndao=new NotificationDao(connection);
		   applicationServiceStatistics=ndao.getApplicationServiceStatistics();   
		   ApplicationOrganisationStatistics=ndao.getApplicationOrganisationStatistics();  
	 }
	 private void populateApplicationOrganisationWise() throws Exception{
		   NotificationDao ndao=new NotificationDao(connection);
		   ApplicationOrganisationStatistics=ndao.getApplicationOrganisationStatistics();  
	 }
	 private void populateApplicationDesignationStatistics() throws Exception{
		   NotificationDao ndao=new NotificationDao(connection);
		   ApplicationDesignationStatistics=ndao.getApplicationDesignationStatistics();  
	 }
	
	 private void populatePriorPermissionDesignationStatistics(HttpServletRequest req) throws Exception{
		   NotificationDao ndao=new NotificationDao(connection);
		   PriorPermissionDesignationStatistics=ndao.getPriorPermissionDesignationStatistics(req);  
	 }
	
	 private void populateRenewalDesignationStatistics(HttpServletRequest req) throws Exception{
		   NotificationDao ndao=new NotificationDao(connection);
		   RenewalDesignationStatistics=ndao.getRenewalDesignationStatistics(req);  
	 }
	
		private void populateDashboardServiceStatistics() throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
			   DashboardServiceStatistics=ndao.getDashboardServiceStatistics();  
		 }
		private void populateRegistrationDesignationStatistics(HttpServletRequest req) throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
			   RegistrationDesignationStatistics=ndao.getRegistrationDesignationStatistics(req);  
		 }
		private void populatePeriorPermissionOrganisationStatistics() throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
			   PeriorPermissionOrganisationStatistics=ndao.getPeriorPermissionOrganisationStatistics();  
		 }
		private void populateRenewalOrganisationStatistics() throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
			   RenewalOrganisationStatistics=ndao.getRenewalOrganisationStatistics();  
		 }
		private void populateHospitalityOrganisationStatistics() throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
			   HospitalityOrganisationStatistics=ndao.getHospitalityOrganisationStatistics();  
		 }
		private void populateHospitalityDesignationStatistics(HttpServletRequest req) throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
			   HospitalityDesignationStatistics=ndao.getHospitalityDesignationStatistics(req);  
		 }
		private void populatePendencyReportDegwise() throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
				String DegCode=getParameterMap().get("DegCode").get(0);
				String ranges=getParameterMap().get("clickedDatasetIndex").get(0);
				if(DegCode.equalsIgnoreCase("JS")) {
				PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Joint Secretary", getParameterMap().get("recordsPerPage").get(0),ranges);
		 }
				else if(DegCode.equalsIgnoreCase("AS")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Additional Secretary", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("US")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Under Secretary", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("DS")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Deputy Secretary", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("DIR")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Director", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("DD")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Deputy Director", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("JDD")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Joint Deputy Director", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("SO")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Section Officer", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("ASO")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Assistant Section Officer", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("SSA")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Senior Secrectariat Assistant", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else if(DegCode.equalsIgnoreCase("PA")){
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"Personal Assistant", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
				else {
					PendencyReportDegwise=ndao.getPendencyReportDegwise(getParameterMap().get("pageNum").get(0), getParameterMap().get("ServiceId").get(0),"null", getParameterMap().get("recordsPerPage").get(0),ranges);
				}
		}
		private void populatePendencyCountReportDegwise(HttpServletRequest req) throws Exception{
			   NotificationDao ndao=new NotificationDao(connection);
			   String ranges=getParameterMap().get("clickedDatasetIndex").get(0);
			   String DegCode=getParameterMap().get("DegCode").get(0);
			   if(DegCode.equalsIgnoreCase("JS")) {
				   PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0),"Joint Secretary",ranges,req);
			 }
					else if(DegCode.equalsIgnoreCase("AS")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0),"Additional Secretary",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("US")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise( getParameterMap().get("ServiceId").get(0),"Under Secretary",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("DS")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0),"Deputy Secretary",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("DIR")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0),"Director",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("DD")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise( getParameterMap().get("ServiceId").get(0),"Deputy Director",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("JDD")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise( getParameterMap().get("ServiceId").get(0),"Joint Deputy Director",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("SO")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise( getParameterMap().get("ServiceId").get(0),"Section Officer",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("ASO")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise( getParameterMap().get("ServiceId").get(0),"Assistant Section Officer",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("SSA")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0),"Senior Secrectariat Assistant",ranges,req);
					}
					else if(DegCode.equalsIgnoreCase("PA")){
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0),"Personal Assistant",ranges,req);
					}
					else {
						PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0),"null",ranges,req); 
					}
			  // PendencyCountReportDegwise=ndao.getPendencyCountReportDegwise(getParameterMap().get("ServiceId").get(0)); 
		 }
	public List<List3> getApplicationServiceStatistics() {
		return applicationServiceStatistics;
	}

	public void setApplicationServiceStatistics(
			List<List3> applicationServiceStatistics) {
		this.applicationServiceStatistics = applicationServiceStatistics;
	}
	public List<List5> getApplicationOrganisationStatistics() {
		return ApplicationOrganisationStatistics;
	}

	public void setApplicationOrganisationStatistics(
			List<List5> applicationOrganisationStatistics) {
		this.ApplicationOrganisationStatistics = applicationOrganisationStatistics;
	}
	public List<List5> getApplicationOrganisationStatisticsmiddle() {
		return ApplicationOrganisationStatisticsmiddle;
	}

	public void setApplicationOrganisationStatisticsmiddle(
			List<List5> applicationOrganisationStatisticsmiddle) {
		this.ApplicationOrganisationStatisticsmiddle = applicationOrganisationStatisticsmiddle;
	}
	public List<List5> getApplicationOrganisationStatisticslast() {
		return ApplicationOrganisationStatisticslast;
	}

	public void setApplicationOrganisationStatisticslast(
			List<List5> applicationOrganisationStatisticslast) {
		this.ApplicationOrganisationStatisticslast = applicationOrganisationStatisticslast;
	}
	public List<List5> getApplicationDesignationStatistics() {
		return ApplicationDesignationStatistics;
	}

	public void setApplicationDesignationStatistics(List<List5> applicationDesignationStatistics) {
		ApplicationDesignationStatistics = applicationDesignationStatistics;
	}

	public List<List5> getApplicationDesignationStatisticsmiddle() {
		return ApplicationDesignationStatisticsmiddle;
	}

	public void setApplicationDesignationStatisticsmiddle(List<List5> applicationDesignationStatisticsmiddle) {
		ApplicationDesignationStatisticsmiddle = applicationDesignationStatisticsmiddle;
	}

	public List<List5> getApplicationDesignationStatisticslast() {
		return ApplicationDesignationStatisticslast;
	}

	public void setApplicationDesignationStatisticslast(List<List5> applicationDesignationStatisticslast) {
		ApplicationDesignationStatisticslast = applicationDesignationStatisticslast;
	}
	
	public List<List5> getApplicationListStatistics() {
		return ApplicationListStatistics;
	}
	public void setApplicationListStatistics(List<List5> applicationListStatistics) {
		ApplicationListStatistics = applicationListStatistics;
	}
	
	public List<List3> getPriorPermissionDesignationStatistics() {
		return PriorPermissionDesignationStatistics;
	}



	public void setPriorPermissionDesignationStatistics(List<List3> priorPermissionDesignationStatistics) {
		PriorPermissionDesignationStatistics = priorPermissionDesignationStatistics;
	}



	public List<List5> getPriorPermissionDesignationStatisticsmiddle() {
		return PriorPermissionDesignationStatisticsmiddle;
	}



	public void setPriorPermissionDesignationStatisticsmiddle(List<List5> priorPermissionDesignationStatisticsmiddle) {
		PriorPermissionDesignationStatisticsmiddle = priorPermissionDesignationStatisticsmiddle;
	}



	public List<List5> getPriorPermissionDesignationStatisticslast() {
		return PriorPermissionDesignationStatisticslast;
	}



	public void setPriorPermissionDesignationStatisticslast(List<List5> priorPermissionDesignationStatisticslast) {
		PriorPermissionDesignationStatisticslast = priorPermissionDesignationStatisticslast;
	}



	public List<List5> getPriorPermissionListStatistics() {
		return PriorPermissionListStatistics;
	}



	public void setPriorPermissionListStatistics(List<List5> priorPermissionListStatistics) {
		PriorPermissionListStatistics = priorPermissionListStatistics;
	}

	public List<List3> getRenewalDesignationStatistics() {
		return RenewalDesignationStatistics;
	}



	public void setRenewalDesignationStatistics(List<List3> renewalDesignationStatistics) {
		RenewalDesignationStatistics = renewalDesignationStatistics;
	}



	public List<List5> getRenewalDesignationStatisticsmiddle() {
		return RenewalDesignationStatisticsmiddle;
	}



	public void setRenewalDesignationStatisticsmiddle(List<List5> renewalDesignationStatisticsmiddle) {
		RenewalDesignationStatisticsmiddle = renewalDesignationStatisticsmiddle;
	}



	public List<List5> getRenewalDesignationStatisticslast() {
		return RenewalDesignationStatisticslast;
	}



	public void setRenewalDesignationStatisticslast(List<List5> renewalDesignationStatisticslast) {
		RenewalDesignationStatisticslast = renewalDesignationStatisticslast;
	}



	public List<List5> getRenewaListStatistics() {
		return RenewaListStatistics;
	}



	public void setRenewaListStatistics(List<List5> renewaListStatistics) {
		RenewaListStatistics = renewaListStatistics;
	}

	
	public List<List4> getDashboardServiceStatistics() {
		return DashboardServiceStatistics;
	}



	public void setDashboardServiceStatistics(List<List4> dashboardServiceStatistics) {
		DashboardServiceStatistics = dashboardServiceStatistics;
	}
	public List<List3> getRegistrationDesignationStatistics() {
		return RegistrationDesignationStatistics;
	}



	public void setRegistrationDesignationStatistics(List<List3> registrationDesignationStatistics) {
		RegistrationDesignationStatistics = registrationDesignationStatistics;
	}
	public List<List5> getPeriorPermissionOrganisationStatistics() {
		return PeriorPermissionOrganisationStatistics;
	}

	public void setPeriorPermissionDesignationStatistics(List<List3> periorPermissionDesignationStatistics) {
		PeriorPermissionDesignationStatistics = periorPermissionDesignationStatistics;
	}

	public List<List5> getRenewalOrganisationStatistics() {
		return RenewalOrganisationStatistics;
	}



	public void setRenewalOrganisationStatistics(List<List5> renewalOrganisationStatistics) {
		RenewalOrganisationStatistics = renewalOrganisationStatistics;
	}

	public List<List5> getHospitalityOrganisationStatistics() {
		return HospitalityOrganisationStatistics;
	}

	public void setHospitalityOrganisationStatistics(List<List5> hospitalityOrganisationStatistics) {
		HospitalityOrganisationStatistics = hospitalityOrganisationStatistics;
	}



	public List<List3> getHospitalityDesignationStatistics() {
		return HospitalityDesignationStatistics;
	}



	public void setHospitalityDesignationStatistics(List<List3> hospitalityDesignationStatistics) {
		HospitalityDesignationStatistics = hospitalityDesignationStatistics;
	}



	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	

	public String getPendencyCountReportDegwise() {
		return PendencyCountReportDegwise;
	}

	public void setPendencyCountReportDegwise(String pendencyCountReportDegwise) {
		PendencyCountReportDegwise = pendencyCountReportDegwise;
	}

	public List<PendencyReport> getPendencyReportDegwise() {
		return PendencyReportDegwise;
	}

	public void setPendencyReportDegwise(List<PendencyReport> pendencyReportDegwise) {
		PendencyReportDegwise = pendencyReportDegwise;
	}

	public List<String> getBlockYearList() {
		return blockYearList;
	}

	public void setBlockYearList(List<String> blockYearList) {
		this.blockYearList = blockYearList;
	}

	public List<KVPair<String, String>> getCountryFinYearWiseBarGraph() {
		return countryFinYearWiseBarGraph;
	}

	public void setCountryFinYearWiseBarGraph(
			List<KVPair<String, String>> countryFinYearWiseBarGraph) {
		this.countryFinYearWiseBarGraph = countryFinYearWiseBarGraph;
	}
	

	public List<KVPair<String, String>> getAssoFinYearWiseBarGraph() {
		return assoFinYearWiseBarGraph;
	}

	public void setAssoFinYearWiseBarGraph(
			List<KVPair<String, String>> assoFinYearWiseBarGraph) {
		this.assoFinYearWiseBarGraph = assoFinYearWiseBarGraph;
	}

	public List<KVPair<String, String>> getDonorFinYearWiseBarGraph() {
		return donorFinYearWiseBarGraph;
	}

	public void setDonorFinYearWiseBarGraph(
			List<KVPair<String, String>> donorFinYearWiseBarGraph) {
		this.donorFinYearWiseBarGraph = donorFinYearWiseBarGraph;
	}

	public String getCurrentBlockYear() {
		return currentBlockYear;
	}

	public void setCurrentBlockYear(String currentBlockYear) {
		this.currentBlockYear = currentBlockYear;
	}

	public String getGraphType() {
		return graphType;
	}

	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}

	public List<KVPair<String, String>> getSuddenRiseIncomeWiseBarGraph() {
		return suddenRiseIncomeWiseBarGraph;
	}

	public void setSuddenRiseIncomeWiseBarGraph(
			List<KVPair<String, String>> suddenRiseIncomeWiseBarGraph) {
		this.suddenRiseIncomeWiseBarGraph = suddenRiseIncomeWiseBarGraph;
	}

	public List<KVPair<String, String>> getAssoWiseBarGraph() {
		return assoWiseBarGraph;
	}

	public void setAssoWiseBarGraph(List<KVPair<String, String>> assoWiseBarGraph) {
		this.assoWiseBarGraph = assoWiseBarGraph;
	}

	public List<KVPair<String, String>> getCountryWiseBarGraph() {
		return countryWiseBarGraph;
	}

	public void setCountryWiseBarGraph(
			List<KVPair<String, String>> countryWiseBarGraph) {
		this.countryWiseBarGraph = countryWiseBarGraph;
	}

	public List<KVPair<String, String>> getDonorWiseBarGraph() {
		return donorWiseBarGraph;
	}

	public void setDonorWiseBarGraph(List<KVPair<String, String>> donorWiseBarGraph) {
		this.donorWiseBarGraph = donorWiseBarGraph;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public List<Notification> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<Notification> notificationList) {
		this.notificationList = notificationList;
	}

	public Map<String, String> getNewNotificationList() {
		return newNotificationList;
	}

	public void setNewNotificationList(Map<String, String> newNotificationList) {
		this.newNotificationList = newNotificationList;
	}

	public List<Notification> getNotificationAttachmentList() {
		return notificationAttachmentList;
	}

	public void setNotificationAttachmentList(
			List<Notification> notificationAttachmentList) {
		this.notificationAttachmentList = notificationAttachmentList;
	}

	public List<KVPair<String, String>> getRcnBarGraphYearWise() {
		return rcnBarGraphYearWise;
	}

	public void setRcnBarGraphYearWise(
			List<KVPair<String, String>> rcnBarGraphYearWise) {
		this.rcnBarGraphYearWise = rcnBarGraphYearWise;
	}

	public List<List3> getApplicationStatistics() {
		return applicationStatistics;
	}

	public void setApplicationStatistics(List<List3> applicationStatistics) {
		this.applicationStatistics = applicationStatistics;
	}

	}
