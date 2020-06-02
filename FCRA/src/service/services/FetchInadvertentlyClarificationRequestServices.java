package service.services;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.services.requests.AbstractRequest;
import models.services.requests.AnnualReturns;
import models.services.requests.ArticleContribution;
import models.services.requests.ChangeOfCommitteeMembers;
import models.services.requests.ChangeOfDetails;
import models.services.requests.ChangeOfFcRecieptCumUtilisationBank;
import models.services.requests.ChangeOfNameAndAddress;
import models.services.requests.ChangeOfOpeningUtilisationBankAccount;
import models.services.requests.ElectionContribution;
import models.services.requests.FundTransfer;
import models.services.requests.GiftContribution;
import models.services.requests.Grievances;
import models.services.requests.Hospitality;
import models.services.requests.PriorPermission;
import models.services.requests.Registration;
import models.services.requests.Renewal;
import models.services.requests.SecurityContribution;
import models.services.requests.ServiceFactory;
import models.services.requests.ServiceType;
import utilities.Commons;
import utilities.ValidationException;
import utilities.lists.List5;
import utilities.lists.List6;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;


public class FetchInadvertentlyClarificationRequestServices extends Commons {
	

	
		private String applicationId;
		private String applicant_name;
		private String tempApplicationId;
		private String serviceId;
		private String serviceName;
		private String creationDate;
		private String sectionFileNumber;
		private String documentId;
		private String applicationDetails;
		private List<List5> smsList=new ArrayList<List5>();
		private List<List6> mailList=new ArrayList<List6>();
		private void fetchBasicApplicationDetails() throws Exception {
			if(connection == null) {
				throw new Exception("Invalid connection");
			}
			
			if(applicationId == null || applicationId.equals("")) {
				throw new ValidationException("Application Id - Invalid entry. Only alphabets and numbers allowed (12 characters max)");
			}
			
			StringBuffer query = new StringBuffer("SELECT APPLICATION_ID, APPLICANT_NAME,TEMP_FILENO, AD.SERVICE_CODE, S.SERVICE_DESC, to_char(CREATED_ON, 'dd-mm-yyyy'), SECTION_FILENO, DOCUMENT_ID FROM V_APPLICATION_DETAILS AD, TM_SERVICE S WHERE APPLICATION_ID=? AND AD.SERVICE_CODE=S.SERVICE_CODE");
			PreparedStatement statement = connection.prepareStatement(query.toString());
			statement.setString(1, applicationId);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				int i=1;
				applicationId = rs.getString(i++);
				applicant_name = rs.getString(i++);
				tempApplicationId = rs.getString(i++);
				serviceId = rs.getString(i++);
				serviceName = rs.getString(i++);
				creationDate = rs.getString(i++);
				sectionFileNumber = rs.getString(i++);
				documentId = rs.getString(i++);
			}
			rs.close();
			statement.close();
			if(serviceId == null || serviceId.equals(""))
				throw new ValidationException("Invalid Application id");	
			
		  //  smsList=retrieveSMSDetails(applicationId);
			//mailList=retrieveMailDetails(applicationId);
			
		}
		


		/**
		 * @return
		 */
		public String pullApplicationDetails() {
			String result = "error";
			
			begin();
			try {
				fetchBasicApplicationDetails();
				ServiceType type = null;
				if(serviceId.equals("01"))
					type = ServiceType.REGISTRATION;
				else if(serviceId.equals("02"))
					type = ServiceType.PRIOR_PERMISSION;
				else if(serviceId.equals("03"))
					type = ServiceType.RENEWAL;
				else if(serviceId.equals("04"))
					type = ServiceType.ANNUAL_RETURNS;
				else if(serviceId.equals("05"))
					type = ServiceType.TRANSFER_OF_FUNDS;
				else if(serviceId.equals("06"))
					type = ServiceType.CHANGE_OF_DETAILS;
				else if(serviceId.equals("07"))
					type = ServiceType.HOSPITALITY;
				else if(serviceId.equals("08"))
					type = ServiceType.GIFT_CONTRIBUTION;
				else if(serviceId.equals("09"))
					type = ServiceType.ARTICLE_CONTRIBUTION;
				else if(serviceId.equals("10"))
					type = ServiceType.SECURITY_CONTRIBUTION;
				else if(serviceId.equals("11"))
					type = ServiceType.ELECTION_CONTRIBUTION;
				else if(serviceId.equals("12"))
					type = ServiceType.GRIEVANCES;
				else if(serviceId.equals("13"))
					type = ServiceType.CHANGE_NAME_ADDRESS;
				else if(serviceId.equals("15"))
					type = ServiceType.CHANGE_FC_RECEIPT_CUM_UTILISATION_BANK;
				else if(serviceId.equals("16"))
					type = ServiceType.CHANGE_OPENING_UTILIZATION_BANK_ACCOUNT;	
				else if(serviceId.equals("17"))
					type = ServiceType.CHANGE_CHANGE_COMMITTEE_MEMBERS;				
				
				AbstractRequest service = ServiceFactory.createService(connection, type);
				service.setApplicationId(applicationId);
				service.setTempApplicationId(tempApplicationId);
				service.setServiceId(serviceId);
				service.setServiceName(serviceName);
				service.setCreationDate(creationDate);
				service.setSectionFileNumber(sectionFileNumber);
				service.setDocumentId(documentId);
				service.getDetails();
				ObjectMapper mapper = new ObjectMapper();
				switch(type) {
				case REGISTRATION:
					applicationDetails = mapper.writeValueAsString((Registration)service);
					break;
				case PRIOR_PERMISSION:
					applicationDetails = mapper.writeValueAsString((PriorPermission)service);				
					break;
				case RENEWAL:
					applicationDetails = mapper.writeValueAsString((Renewal)service);
					break;
				case ANNUAL_RETURNS:
					applicationDetails = mapper.writeValueAsString((AnnualReturns)service);
					break;
				case TRANSFER_OF_FUNDS:
					applicationDetails = mapper.writeValueAsString((FundTransfer)service);
					break;
				case CHANGE_OF_DETAILS:
					applicationDetails = mapper.writeValueAsString((ChangeOfDetails)service);
					break;
				case HOSPITALITY:
					applicationDetails = mapper.writeValueAsString((Hospitality)service);
					break;
				case GIFT_CONTRIBUTION:
					applicationDetails = mapper.writeValueAsString((GiftContribution)service);
					break;
				case ARTICLE_CONTRIBUTION:
					applicationDetails = mapper.writeValueAsString((ArticleContribution)service);
					break;
				case SECURITY_CONTRIBUTION:
					applicationDetails = mapper.writeValueAsString((SecurityContribution)service);
					break;
				case ELECTION_CONTRIBUTION:
					applicationDetails = mapper.writeValueAsString((ElectionContribution)service);
					break;
				case GRIEVANCES:
					applicationDetails = mapper.writeValueAsString((Grievances)service);
					break;
				case CHANGE_NAME_ADDRESS:
					applicationDetails = mapper.writeValueAsString((ChangeOfNameAndAddress)service);
					break;					
				case CHANGE_FC_RECEIPT_CUM_UTILISATION_BANK:
					applicationDetails = mapper.writeValueAsString((ChangeOfFcRecieptCumUtilisationBank)service);
					break;	
				case CHANGE_OPENING_UTILIZATION_BANK_ACCOUNT:
					applicationDetails = mapper.writeValueAsString((ChangeOfOpeningUtilisationBankAccount)service);
					break;	
				case CHANGE_CHANGE_COMMITTEE_MEMBERS:
					applicationDetails = mapper.writeValueAsString((ChangeOfCommitteeMembers)service);
					break;					
				}
				result = "success";
			}catch(ValidationException ve) {
				try {
					notifyList.add(new Notification("Error!",ve.getMessage(), Status.ERROR, Type.BAR));
				}catch(Exception ex) {}
			}catch(Exception e){
				try {
					notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR, Type.BAR));
					if(connection != null)
						connection.rollback();
				}catch(SQLException e2){
					ps(e2);
				}catch(Exception e2){
					ps(e2);
				}
				ps(e);			
			}
			finish();
			return result;
		}

		private List<List6> retrieveMailDetails(String applicationId) throws SQLException {
			// TODO Auto-generated method stub
			PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;

			query =new StringBuffer("SELECT b.MAIL_SUBJECT, " +
					"  b.MAIL_BODY, " +
					"  b.TO_ADDRESS, " +
					"  to_char(b.DATED,'dd/mm/yyyy') as DATED," +
					"  to_char(b.STATUS_DATE,'dd/mm/yyyy') as STATUS_DATE," +
					"  CASE WHEN b.STATUS_ID=0 THEN 'PENDING' WHEN b.STATUS_ID=1 THEN 'SENT' ELSE 'ERROR' END STATUS " +
					"  FROM T_APPLICATION_AUTOMAIL_DETAILS a, " +
					"  T_MAIL_DETAILS b " +
					"  WHERE a.MAIL_ID = b.MAIL_ID " +
					"  AND a.APPLICATION_ID= ? " +
					"  ORDER BY b.DATED DESC ");

			statement = connection.prepareStatement(query.toString());	
			statement.setString(1,applicationId);
			rs = statement.executeQuery();		
			while(rs.next()){		
				mailList.add(new List6(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
			}
			rs.close();
			statement.close();
			return mailList;		
		}


		private List<List5> retrieveSMSDetails(String applicationId) throws SQLException {
			// TODO Auto-generated method stub
			PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
			
			//SMS Details
			String fileNo = null;
			if(tempApplicationId != null && tempApplicationId.equals("") == false)
				fileNo = tempApplicationId;
			else
				fileNo = applicationId;

			query =new StringBuffer("SELECT b.MESSAGE,"
					+ " b.TO_ADDRESS,"
					+ " to_char(b.DATED,'dd/mm/yyy') as DATED,"
					+ " to_char(b.STATUS_DATE,'dd/mm/yyyy') as STATUS_DATE,"
					+ " CASE WHEN b.STATUS_ID=0 THEN 'PENDING' WHEN b.STATUS_ID=1 THEN 'SENT' ELSE 'ERROR' END STATUS "
					+ " FROM T_APPLICATION_AUTOSMS_DETAILS a, "
					+ " T_SMS_DETAILS b "
					+ " WHERE a.SMS_ID =b.SMS_ID "
					+ " AND a.APPLICATION_ID=? "
					+ " ORDER BY b.DATED DESC ");

			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, applicationId);
			rs = statement.executeQuery();		
			while(rs.next()){
				smsList.add(new List5(new StringBuffer(rs.getString(1)),new StringBuffer(rs.getString(2)),new StringBuffer(rs.getString(3)),new StringBuffer(rs.getString(4)),new StringBuffer(rs.getString(5))));
			}
			rs.close();
			statement.close();
			return smsList;
		}

		
		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public String getApplicant_name() {
			return applicant_name;
		}



		public void setApplicant_name(String applicant_name) {
			this.applicant_name = applicant_name;
		}



		public String getTempApplicationId() {
			return tempApplicationId;
		}

		public void setTempApplicationId(String tempApplicationId) {
			this.tempApplicationId = tempApplicationId;
		}

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

		public String getApplicationDetails() {
			return applicationDetails;
		}

		public void setApplicationDetails(String applicationDetails) {
			this.applicationDetails = applicationDetails;
		}



		public List<List5> getSmsList() {
			return smsList;
		}



		public void setSmsList(List<List5> smsList) {
			this.smsList = smsList;
		}



		public List<List6> getMailList() {
			return mailList;
		}



		public void setMailList(List<List6> mailList) {
			this.mailList = mailList;
		}
		
		
		
		
		
		
	}

	
