package dao.reports.concretereports;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import com.lowagie.text.pdf.PRAcroForm;

import utilities.GeneratePdfVirtualizer;
import dao.reports.MISReportGenerator;

public class MiscReportGenerator extends MISReportGenerator {

	public MiscReportGenerator(Connection connection) {
		super(connection);
	}

	
	
	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		String tsPath = "/Reports/test.jrxml";
		String fileName = "HelloTestReport.pdf";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void generateExcel() throws Exception {
	}

	@Override
	protected void generateHTML() throws Exception {
		/*Write Codew For Html Display*/
		
	}

	@Override
	protected void generateChart() throws Exception {
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap) throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);//("reportType");
			reportFormat = parameterMap.get("reportFormat").get(0);//("reportFormat");
		}		
	}



	@Override
	protected void generateCSV() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
