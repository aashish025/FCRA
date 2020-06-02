package dao.reports;

import java.sql.Connection;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;
import dao.reports.concretereports.AnuualStatusReportGenerator;
import dao.reports.concretereports.ApplicationPendancyTimeRangewiseReportGenerator;
import dao.reports.concretereports.AssociationsDetailsReportGenerator;
import dao.reports.concretereports.AssociationsNotFiledAnnualReturnsReportGenerator;
import dao.reports.concretereports.BlueFlaggedCategoryReportGenrator;
import dao.reports.concretereports.BlueFlaggedRcnsReportGenerator;
import dao.reports.concretereports.CountryPurposeDonorReportGenerator;
import dao.reports.concretereports.CountryStateReportGenerator;
import dao.reports.concretereports.CountryWiseReceiptReportGenerator;
import dao.reports.concretereports.Covid19EmergencyNotRespondReportGenerator;
import dao.reports.concretereports.Covid19EmergencyReportGenerator;
import dao.reports.concretereports.Covid19EmergencyRespondReportGenerator;
import dao.reports.concretereports.DisposalDetailsReportGenerator;
import dao.reports.concretereports.DisrictDonorReceiptReportGenerator;
import dao.reports.concretereports.DonorListReportGenerator;
import dao.reports.concretereports.MiscReportGenerator;
import dao.reports.concretereports.PendencyReportGenerator;
import dao.reports.concretereports.PurposeWiseReportGenerator;
import dao.reports.concretereports.RedFlaggedRcnsReportGenerator;
import dao.reports.concretereports.RegistrationExpiryReportGenerator;
import dao.reports.concretereports.RegistrationTrackingReportGenerator;
import dao.reports.concretereports.ReligionWiseReportGenerator;
import dao.reports.concretereports.ReturnFiledReportGenerator;
import dao.reports.concretereports.StatusReportGenerator;
import dao.reports.concretereports.SuddenRiseInIncomeReportGenrator;
import dao.reports.concretereports.RedFlaggedCategoryReportGenrator;
import dao.reports.concretereports.UserActivityReportGenerator;
import dao.reports.concretereports.DisposalDetailsReportGenerator;

public class MISReportGeneratorFactory {
	
	public static MISReportGenerator getReportGenerator(String reportType, Connection connection,String covidreportTypewise) throws Exception{
		MISReportGenerator report = null;
		if((!(reportType.equals("tracking"))) && (!(reportType.equals("redflagged")))&& (!(reportType.equals("blueflagged")))){
			if(ESAPI.validator().isValidInput("reportFormat", reportType, "Num", 2, false) == false) {
				throw new ValidationException("Invalid Report Type");
			}	
		}			
		if(reportType.equals("1")) {
			//copy from 3 rs
		report = new PendencyReportGenerator(connection);	
		} else if(reportType.equals("2")) {
			report = new StatusReportGenerator(connection);	
		} else if(reportType.equals("3")){
			report = new ReturnFiledReportGenerator(connection);
		}
		 else if(reportType.equals("4")){
				report = new PurposeWiseReportGenerator(connection);
		 }
		 else if(reportType.equals("6")){
				report = new CountryPurposeDonorReportGenerator(connection);
		 }
		 else if(reportType.equals("8")){
				report = new DisrictDonorReceiptReportGenerator(connection);
		 }
		 else if(reportType.equals("5")){
				report = new CountryWiseReceiptReportGenerator(connection);
		 }
		 else if(reportType.equals("7")){
				report = new ReligionWiseReportGenerator(connection);
		 }
		 else if(reportType.equals("9")){
				report = new CountryStateReportGenerator(connection);
		 }
		 else if(reportType.equals("10")){
				report = new DonorListReportGenerator(connection);
		 }
		 else if(reportType.equals("11")){
				report = new UserActivityReportGenerator(connection);
		 }
		 else if(reportType.equals("12")){
				report = new SuddenRiseInIncomeReportGenrator(connection);
		 }
		 else if(reportType.equals("13")){
				report = new RedFlaggedCategoryReportGenrator(connection);
		 }
		 else if(reportType.equals("27")){
				report = new BlueFlaggedCategoryReportGenrator(connection);
		 }
		 else if(reportType.equals("14")){
			 	report = new AssociationsNotFiledAnnualReturnsReportGenerator(connection);
		 }
		 else if(reportType.equals("15")){
			 	report = new ApplicationPendancyTimeRangewiseReportGenerator(connection); 
		 }
		 else if(reportType.equals("16")){
			 	report = new AnuualStatusReportGenerator(connection); 
		 }
		 else if(reportType.equals("17")){
			 	report = new RegistrationExpiryReportGenerator(connection); 
		 }
		 else if(reportType.equals("18")){
			 	report = new DisposalDetailsReportGenerator(connection);
		 }		
		 else if(reportType.equals("26")){
			 	report = new AssociationsDetailsReportGenerator(connection);
		 }
		 else if(reportType.equals("28")){
			 	report = new Covid19EmergencyReportGenerator(connection);
		 }
		 else if(reportType.equals("29")){
			 System.out.println("reportTypewise-------1111 "+covidreportTypewise);
			 if(covidreportTypewise.equalsIgnoreCase("Response")) {
			 	report = new Covid19EmergencyRespondReportGenerator(connection);
			 }
			 else {
				 report = new Covid19EmergencyNotRespondReportGenerator(connection);
			 }
		 }
		 else if(reportType.equals("tracking")){
				report = new RegistrationTrackingReportGenerator(connection);
		 }
		 else if(reportType.equals("redflagged")){
				report = new RedFlaggedRcnsReportGenerator(connection);
		 }
		 else if(reportType.equals("blueflagged")){
				report = new BlueFlaggedRcnsReportGenerator(connection);
		 }
		
		 else {
			throw new ValidationException("Invalid Report Type");
		}		
		return report;
	}

}
