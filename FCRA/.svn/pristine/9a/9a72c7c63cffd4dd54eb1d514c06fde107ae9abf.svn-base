package dao.reports;

import java.sql.Connection;
import java.util.Map;

import org.owasp.esapi.ESAPI;
import org.springframework.util.MultiValueMap;

import utilities.ValidationException;

public abstract class MISReportGenerator {
	protected Connection connection = null;
	protected String reportType;
	protected String reportFormat;
	protected String reportDisplayType;
	protected String reportData;

	public MISReportGenerator(Connection connection) {
		this.connection = connection;
	}
	
	protected abstract void generatePDF() throws Exception;
	protected abstract void generateExcel() throws Exception;
	protected abstract void generateHTML() throws Exception;
	protected abstract void generateChart() throws Exception;
	protected abstract void generateCSV() throws Exception;
	protected abstract void assignParameters(MultiValueMap<String,String> parameterMap) throws Exception;
		
	
	public void generateReport(MultiValueMap<String,String> parameterMap) throws Exception {
		assignParameters(parameterMap);		
		if(ESAPI.validator().isValidInput("reportFormat", reportFormat, "Num", 1, false) == false) {
			throw new ValidationException("Invalid Report Format");
		}
		if(reportFormat.equals("1")) {
			generatePDF();
		} else if(reportFormat.equals("2")) {
			generateExcel();
		} else if(reportFormat.equals("3")) {
			generateHTML();
		} else if(reportFormat.equals("4")) {
			generateCSV();
		}
		/*else if(reportFormat.equals("5")) {
			generateChart();
		}*/else {
			throw new ValidationException("Invalid Report Format");
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportFormat() {
		return reportFormat;
	}

	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getReportDisplayType() {
		return reportDisplayType;
	}

	public void setReportDisplayType(String reportDisplayType) {
		this.reportDisplayType = reportDisplayType;
	}
}
