package service.reports;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.reports.DonorDetail;
import models.services.DonorDetails;
import models.services.requests.AbstractRequest;

import org.owasp.esapi.ESAPI;

import dao.reports.DonorDetailDao;
import dao.reports.RegistrationTrackingDao;
import dao.services.dashboard.ApplicationStatusDao;
import dao.services.dashboard.ProjectDashboardDao;
import utilities.Commons;
import utilities.InformationException;
import utilities.ValidationException;
import utilities.lists.List1;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.lists.List4;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;


public class DonorDetailService extends Commons{
		private String pageNum;
		private String recordsPerPage;
		private String sortColumn;
		private String sortOrder;
		private String totalRecords;	
		private List<List2> stateList;
		private List<List2> countryList;
		private List<List2> assocationList;
		private String state;
		private String country;
		private String donorName;
		private String association;
		private List<DonorDetail> donorList;
		
		
	
		
		
		public void initDonorAction(){
			begin();
			try {
					populatestateaction();	
					populatecountryaction();
			} catch(Exception e){
				ps(e);
			}
			finally{
				finish();
			}		
		}
		public void populatestateaction() throws Exception{
			DonorDetailDao pdd=new DonorDetailDao(connection);		
			stateList=pdd.getStateList();
		}
		public void populatecountryaction() throws Exception{
			DonorDetailDao pdd=new DonorDetailDao(connection);		
			countryList=pdd.getCountryList();
		}
		
		public void initAssociation(){
			begin();
			try {
				getAssociation();				
			} catch(Exception e){
				try {
						connection.rollback();
					} catch (Exception e1) {				
					e1.printStackTrace();
				}
				ps(e);
			}
			finally{
				finish();
			}		
		}
		private void getAssociation() throws Exception{
			DonorDetailDao pdd=new DonorDetailDao(connection);	
			assocationList=pdd.getAssociationList(state);	
		}
		
		
		public void initAdvanceApplicationListDetails(){
			begin();
			try {
					populateAdvanceApplicationListDetails();
			} catch(Exception e){
				ps(e);
			}
			finally{
				finish();
			}		
		}
		
		private void populateAdvanceApplicationListDetails() throws Exception{
			DonorDetailDao pdd=new DonorDetailDao(connection);					
			pdd.setPageNum(pageNum);
			pdd.setRecordsPerPage(recordsPerPage);
			pdd.setSortColumn(sortColumn);
			pdd.setSortOrder(sortOrder);
			pdd.setSearchString(donorName);
			pdd.setDonorName(donorName);
			pdd.setCountry(country);
			pdd.setState(state);
	     	pdd.setAssociation(association);
			donorList=pdd.getSearchListDetails();
			totalRecords=pdd.getTotalRecords();
		}
		
		
		
		
		
		
		
		
		public String getPageNum() {
			return pageNum;
		}
		public void setPageNum(String pageNum) {
			this.pageNum = pageNum;
		}
		public String getRecordsPerPage() {
			return recordsPerPage;
		}
		public void setRecordsPerPage(String recordsPerPage) {
			this.recordsPerPage = recordsPerPage;
		}
		public String getSortColumn() {
			return sortColumn;
		}
		public void setSortColumn(String sortColumn) {
			this.sortColumn = sortColumn;
		}
		public String getSortOrder() {
			return sortOrder;
		}
		public void setSortOrder(String sortOrder) {
			this.sortOrder = sortOrder;
		}
		public String getTotalRecords() {
			return totalRecords;
		}
		public void setTotalRecords(String totalRecords) {
			this.totalRecords = totalRecords;
		}
		public List<List2> getStateList() {
			return stateList;
		}
		public void setStateList(List<List2> stateList) {
			this.stateList = stateList;
		}
		public List<List2> getCountryList() {
			return countryList;
		}
		public void setCountryList(List<List2> countryList) {
			this.countryList = countryList;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public List<List2> getAssocationList() {
			return assocationList;
		}
		public void setAssocationList(List<List2> assocationList) {
			this.assocationList = assocationList;
		}
		public String getCountry() {
			return country;
		}
		public String getDonorName() {
			return donorName;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public void setDonorName(String donorName) {
			this.donorName = donorName;
		}
		public void setAssociation(String association) {
			this.association = association;
		}
		public List<DonorDetail> getDonorList() {
			return donorList;
		}
		public void setDonorList(List<DonorDetail> donorList) {
			this.donorList = donorList;
		}
		
	
		
		

}
