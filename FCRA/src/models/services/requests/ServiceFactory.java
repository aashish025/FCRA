package models.services.requests;

import java.sql.Connection;

public class ServiceFactory {
	public static AbstractRequest createService(Connection connection, ServiceType type) {
		AbstractRequest service = null;
		switch(type) {
		case REGISTRATION:
			service = new Registration(connection);
			break;
		case PRIOR_PERMISSION:
			service = new PriorPermission(connection);
			break;
		case RENEWAL:
			service = new Renewal(connection);
			break;
		case ANNUAL_RETURNS:
			service = new AnnualReturns(connection);
			break;
		case TRANSFER_OF_FUNDS:
			service = new FundTransfer(connection);
			break;
		case CHANGE_OF_DETAILS:
			service = new ChangeOfDetails(connection);
			break;
		case CHANGE_NAME_ADDRESS:
			service = new ChangeOfNameAndAddress(connection);
			break;			
		case CHANGE_FC_RECEIPT_CUM_UTILISATION_BANK:
			service = new ChangeOfFcRecieptCumUtilisationBank(connection);
			break;
		case CHANGE_OPENING_UTILIZATION_BANK_ACCOUNT:
			service = new ChangeOfOpeningUtilisationBankAccount(connection);
			break;
		case CHANGE_CHANGE_COMMITTEE_MEMBERS:
			service = new ChangeOfCommitteeMembers(connection);
			break;			
		case HOSPITALITY:
			service = new Hospitality(connection);
			break;
		case GIFT_CONTRIBUTION:
			service = new GiftContribution(connection);
			break;
		case ARTICLE_CONTRIBUTION:
			service = new ArticleContribution(connection);
			break;
		case SECURITY_CONTRIBUTION:
			service = new SecurityContribution(connection);
			break;
		case ELECTION_CONTRIBUTION:
			service = new ElectionContribution(connection);
			break;
	   case GRIEVANCES:
			service = new Grievances(connection);
			break;
		}
		return service;
	}
}
