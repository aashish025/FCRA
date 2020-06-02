package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.PendencyReport;

import org.springframework.util.MultiValueMap;

import dao.master.ServicesDao;
import dao.reports.MISReportGenerator;
import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;

public class PendencyReportGenerator extends MISReportGenerator {
     
	private List<PendencyReport> pendencyReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private List<String> selectServiceList;
	private List<String> selectYearList;
	private String reportDisplayType;
	private String loginOfficeName;
	private String loginUserName;
	private String loginOfficeCode;
	private int virtualizationMaxSize = 200;
	public PendencyReportGenerator(Connection connection) {
		super(connection);
	}
   
	
	
	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
	
		parameters.put("selectYearList",selectYearList.toString().replace("[", "").replace("]", ""));
		parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);
		String reportQuery="";
		// static report
        String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	    String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	   
	    if(selectedServices.equals("ALL") && selectedServices.contains("ALL") ){
	   parameters.put("selectServiceList","ALL");
	    }
	    else{
	    	ServicesDao sdao=new ServicesDao(connection);
	    	parameters.put("selectServiceList",sdao.getServiceList(selectedServices).toString().replace("[", "").replace("]", ""));
	    }
	    		
	     String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
	    	 subQueryServices=" SERVICE in("+selectedServices+")";
	     if((!selectedYear.trim().equals("ALL") ) && (!selectedYear.contains("ALL")) ){
	    	 if(reportDisplayType.equalsIgnoreCase("s"))  
	    	 subQueryYear="YEAR in ("+selectedYear+")"; 
	    	 else
	    		 subQueryYear="to_char(s_date, 'yyyy') in ("+selectedYear+")"; 
	     }
	      
	     if(reportDisplayType.equalsIgnoreCase("s")){
	    /*	 reportQuery ="with t as (SELECT distinct A.application_id, "
	 				+ "CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) and a.application_id=b.application_id) THEN NULL "
	 				+ "ELSE  "
	 				+ " CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) AND status IN (5,6) and a.application_id=b.application_id) THEN 1  "
	 				+ " ELSE 0  "
	 				+ "END  "
	 				+ "END IB_STATUS, "
	 				+ "CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) and a.application_id=b.application_id) THEN NULL  "
	 				+ "ELSE  "
	 				+ " CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) AND status IN (5,6) and a.application_id=b.application_id) THEN 1  "
	 				+ " ELSE 0  "
	 				+ " END  "
	 				+ "END RAW_STATUS "
	 				+ "FROM t_pc_office_level_final_status A ), t2 AS ( "
	 				+ "select t.application_id, case when submission_date is null then created_on else submission_date end as s_date, b.service_code service,  "
	 				+ "CASE WHEN b.current_status=8 THEN 1 "
	 				+ "when (b.current_stage=1 and b.current_status=2) THEN 0 "
	 				+ "else  "
	 				+ " case when t.ib_status is null then  "
	 				+ "  case when t.raw_status=0 then 1  "
	 				+ "  else 0 "
	 				+ "  end "
	 				+ "  else  "
	 				+ "   case when t.ib_status=1 then  "
	 				+ "     case when t.raw_status=0 then 1 "
	 				+ "     else 0 "
	 				+ "     end "
	 				+ "   else 1 "
	 				+ "    end "
	 				+ "  end "
	 				+ "end "
	 				+ "MHA_STATUS,t.IB_status, t.raw_status, CASE WHEN b.current_status=8 THEN 0 ELSE 1 END applicant_status "
	 				+ "FROM v_application_details b LEFT JOIN t ON t.application_id=b.application_id "
	 				+ "where ((b.current_stage = 2 AND b.current_status IN (4, 8)) or (b.current_stage = 1 AND b.current_status=2)) "
	 				+ ") "
	 				+ ", t3 as ( "
	 				+ "SELECT * FROM ( "
	 				+ "SELECT to_char(s_date, 'yyyy') year, service, count(MHA_STATUS) pending, 'MHA' office  FROM t2 WHERE mha_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ " UNION "
	 				+ " SELECT to_char(s_date, 'yyyy'), service, count(IB_STATUS), 'IB' office  FROM t2 WHERE ib_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ " UNION "
	 				+ " SELECT to_char(s_date, 'yyyy'), service, count(RAW_STATUS), 'RAW' office  FROM t2 WHERE raw_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ "   UNION "
	 				+ " SELECT to_char(s_date, 'yyyy'), service, count(APPLICANT_STATUS), 'APPLICANT' office  FROM t2 WHERE applicant_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ " ) where "+subQueryServices+" ORDER BY YEAR, office, service)  "

	 				+ " SELECT * FROM (SELECT YEAR, service_desc, pending, office FROM t3, tm_service s WHERE t3.service=s.service_code) "
	 				+ " pivot (sum(pending) FOR(office) IN ('MHA' AS MHAS, 'IB' AS IBS, 'RAW' AS RAWS, 'APPLICANT' AS APPLICANTS))  where "+subQueryYear+" "
	 				+ "  ORDER BY YEAR, service_desc  ";*/
	    	 
	    	 reportQuery ="WITH t AS " +
	    			 "  (SELECT DISTINCT A.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END IB_STATUS, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END RAW_STATUS " +
	    			 "  FROM t_pc_office_level_final_status A " +
	    			 "  ), " +
	    			 "  t2 AS " +
	    			 "  (SELECT b.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN submission_date IS NULL " +
	    			 "      THEN created_on " +
	    			 "      ELSE submission_date " +
	    			 "    END AS s_date, " +
	    			 "    b.service_code service, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 1 " +
	    			 "      WHEN (b.current_stage=1 " +
	    			 "      AND b.current_status =2) " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN t.ib_status IS NULL " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN t.raw_status=0 " +
	    			 "              THEN 1 " +
	    			 "              ELSE 0 " +
	    			 "            END " +
	    			 "          ELSE " +
	    			 "            CASE " +
	    			 "              WHEN t.ib_status=1 " +
	    			 "              THEN " +
	    			 "                CASE " +
	    			 "                  WHEN t.raw_status=0 " +
	    			 "                  THEN 1 " +
	    			 "                  ELSE 0 " +
	    			 "                END " +
	    			 "              ELSE 1 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END MHA_STATUS, " +
	    			 "    t.IB_status, " +
	    			 "    t.raw_status, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 0 " +
	    			 "      ELSE 1 " +
	    			 "    END applicant_status, " +
	    			 "    CASE " +
	    			 "      WHEN current_stage=1 " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN current_stage=2 " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN current_status=4 " +
	    			 "              THEN 1 " +
	    			 "              WHEN current_status=9 " +
	    			 "              THEN 2 " +
	    			 "              WHEN current_status=10 " +
	    			 "              THEN 3 " +
	    			 "              WHEN current_status=12 " +
	    			 "              THEN 4 " +
	    			 "              WHEN current_status=8 " +
	    			 "              THEN 5 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END status " +
	    			 "  FROM v_application_details b " +
	    			 "  LEFT JOIN t " +
	    			 "  ON t.application_id =b.application_id " +
	    			 "  ) , " +
	    			 "  t4 AS " +
	    			 "  (SELECT t2.*, " +
	    			 "    a.user_id, " +
	    			 "    (SELECT designation_name " +
	    			 "    FROM tm_designation c " +
	    			 "    WHERE c.designation_id= " +
	    			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "      ) " +
	    			 "    ) designation, " +
	    			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "    ) designation_id " +
	    			 "  FROM t2 " +
	    			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
	    			 "  ON t2.application_id=a.application_id " +
	    			 "  AND a.office_code   ='MHA01' " +
	    			 "  ) , " +
	    			 "  t3 AS " +
	    			 "  (SELECT * " +
	    			 "  FROM " +
	    			 "    (SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'Total' office " +
	    			 "    FROM t4 " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'Pending' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE status IN (0,1) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(MHA_STATUS) pending, " +
	    			 "      'MHA' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status=0 " +
	    			 "    AND status     IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(IB_STATUS), " +
	    			 "      'IB' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE ib_status=0 " +
	    			 "    AND status    IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(RAW_STATUS), " +
	    			 "      'RAW' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE raw_status=0 " +
	    			 "    AND status     IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(APPLICANT_STATUS), " +
	    			 "      'APPLICANT' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE applicant_status=0 " +
	    			 "    AND status           IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'ASO' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=10 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'SO' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=9 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'US' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=8 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'DIR' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=6 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'OTHERS' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status       =0 " +
	    			 "    AND (user_id          IS NULL " +
	    			 "    OR designation_id     IS NULL " +
	    			 "    OR designation_id NOT IN (6,8,9,10)) " +
	    			 "    AND status            IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    ) " +
	    			 "  WHERE "+subQueryServices+" " +
	    			 "  ORDER BY YEAR, " +
	    			 "    office, " +
	    			 "    service " +
	    			 "  ) " +
	    			 "SELECT * " +
	    			 "FROM " +
	    			 "  (SELECT YEAR, " +
	    			 "    service_desc, " +
	    			 "    pending, " +
	    			 "    office " +
	    			 "  FROM t3, " +
	    			 "    tm_service s " +
	    			 "  WHERE t3.service                   =s.service_code " +
	    			 "  ) pivot (SUM(pending) FOR(office) IN ('Total' AS Total, 'Pending' AS Pending, 'MHA' AS MHAS, 'IB' AS IBS, 'RAW' AS RAWS, 'APPLICANT' AS APPLICANTS, 'ASO' AS ASO, 'SO' AS SO, 'US' AS US, 'DIR' AS DIR, 'OTHERS' AS OTHERS)) " +
	    			 "WHERE  "+subQueryYear+" " +
	    			 "ORDER BY YEAR, " +
	    			 "  service_desc " ;
	    	 
	    	 
	    	 
	    	 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE", ds);
	    	 
	     }
	     else{
	    
				/*reportQuery = "with t as (SELECT distinct A.application_id, "
						+ " CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) and a.application_id=b.application_id) THEN NULL  "
						+ " ELSE "
						+ "  CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) AND status IN (5,6) and a.application_id=b.application_id) THEN 1 "
						+ "  ELSE 0 "
						+ "  END "
						+ " END IB_STATUS, "
						+ " CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) and a.application_id=b.application_id) THEN NULL "
						+ " ELSE  "
						+ "  CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) AND status IN (5,6) and a.application_id=b.application_id) THEN 1 "
						+ "  ELSE 0  "
						+ "  END  "
						+ " END RAW_STATUS  "
						+ " FROM t_pc_office_level_final_status A ), t2 AS (  "
						+ " select b.application_id, b.applicant_name,b.section_fileno,(select SNAME from TM_STATE where SCODE=b.state) as state,(select distname from TM_DISTRICT where distcode=b.district) as district, case when submission_date is null then created_on else submission_date end as s_date, b.service_code service,  "
						+ " CASE WHEN b.current_status=8 THEN 1  "
						+ " when (b.current_stage=1 and b.current_status=2) THEN 0  "
						+ " else  "
						+ "  case when t.ib_status is null then  "
						+ "    case when t.raw_status=0 then 1  "
						+ "  else 0  "
						+ "   end  "
						+ "  else   "
						+ "   case when t.ib_status=1 then  "
						+ "    case when t.raw_status=0 then 1  "
						+ "   else 0  "
						+ "    end  "
						+ "  else 1 "
						+ "  end "
						+ "  end "
						+ " END "
						+ " MHA_STATUS,t.IB_status, t.raw_status, CASE WHEN b.current_status=8 THEN 0 ELSE null END applicant_status "
						+ " FROM v_application_details b LEFT JOIN t ON t.application_id=b.application_id "
						+ " WHERE ((b.current_stage = 2 AND b.current_status IN (4, 8)) OR (b.current_stage = 1 AND b.current_status=2)) "
						+ " ) "

						+ " SELECT t2.application_id, t2.applicant_name, to_char(trunc(s_date)) as s_date, to_char(s_date, 'yyyy') year, service_desc, CASE WHEN mha_status=0 THEN 'Pending' ELSE NULL END AS mha,  "
						+ " CASE WHEN ib_status=0 THEN 'Pending' ELSE NULL END AS ib, "
						+ " CASE WHEN raw_status=0 THEN 'Pending' ELSE NULL END AS raw1, "
						+ " case when applicant_status=0 then 'Pending' else null end as applicant,t2.state,t2.section_fileno,t2.district from t2, tm_service s WHERE t2.service=s.service_code and  "
						+ subQueryYear + "  and " + subQueryServices + "  "
						+ "  ORDER BY YEAR, service_desc"; */
	    	 
	    	 reportQuery = "WITH t AS " +
	    			 "  (SELECT DISTINCT A.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END IB_STATUS, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END RAW_STATUS " +
	    			 "  FROM t_pc_office_level_final_status A " +
	    			 "  ), " +
	    			 "  t2 AS " +
	    			 "  (SELECT b.application_id, " +
	    			 "    b.applicant_name, " +
	    			 "    b.section_fileno, " +
	    			 "    (SELECT SNAME FROM TM_STATE WHERE SCODE=b.state " +
	    			 "    ) AS state, " +
	    			 "    (SELECT distname FROM TM_DISTRICT WHERE distcode=b.district " +
	    			 "    ) AS district, " +
	    			 "    CASE " +
	    			 "      WHEN submission_date IS NULL " +
	    			 "      THEN created_on " +
	    			 "      ELSE submission_date " +
	    			 "    END AS s_date, " +
	    			 "    b.service_code service, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 1 " +
	    			 "      WHEN (b.current_stage=1 " +
	    			 "      AND b.current_status =2) " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN t.ib_status IS NULL " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN t.raw_status=0 " +
	    			 "              THEN 1 " +
	    			 "              ELSE 0 " +
	    			 "            END " +
	    			 "          ELSE " +
	    			 "            CASE " +
	    			 "              WHEN t.ib_status=1 " +
	    			 "              THEN " +
	    			 "                CASE " +
	    			 "                  WHEN t.raw_status=0 " +
	    			 "                  THEN 1 " +
	    			 "                  ELSE 0 " +
	    			 "                END " +
	    			 "              ELSE 1 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END MHA_STATUS, " +
	    			 "    t.IB_status, " +
	    			 "    t.raw_status, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 0 " +
	    			 "      ELSE NULL " +
	    			 "    END applicant_status " +
	    			 "  FROM v_application_details b " +
	    			 "  LEFT JOIN t " +
	    			 "  ON t.application_id     =b.application_id " +
	    			 "  WHERE ((b.current_stage = 2 " +
	    			 "  AND b.current_status   IN (4, 8)) " +
	    			 "  OR (b.current_stage     = 1 " +
	    			 "  AND b.current_status    =2)) " +
	    			 "  ), " +
	    			 "  t3 AS " +
	    			 "  (SELECT t2.*, " +
	    			 "    a.user_id, " +
	    			 "    (SELECT user_name FROM tm_user um WHERE um.user_id=a.user_id " +
	    			 "    ) user_name, " +
	    			 "    (SELECT designation_name " +
	    			 "    FROM tm_designation c " +
	    			 "    WHERE c.designation_id= " +
	    			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "      ) " +
	    			 "    ) designation, " +
	    			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "    ) designation_id " +
	    			 "  FROM t2 " +
	    			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
	    			 "  ON t2.application_id=a.application_id " +
	    			 "  AND a.office_code   = '"+loginOfficeCode+"' " +
	    			 "  ) " +
	    			 "SELECT t2.state, " +
	    			 "  t2.district, " +
	    			 "  TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "  TO_CHAR(TRUNC(s_date)) AS s_date, " +
	    			 "  service_desc, " +
	    			 "  t2.application_id, " +
	    			 "  t2.applicant_name, " +
	    			 "  t2.section_fileno, " +
	    			 "  CASE " +
	    			 "    WHEN mha_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS mha, " +
	    			 "  CASE " +
	    			 "    WHEN ib_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS ib, " +
	    			 "  CASE " +
	    			 "    WHEN raw_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS raw1, " +
	    			 "  CASE " +
	    			 "    WHEN applicant_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS applicant, " +
	    			 "  user_id " +
	    			 "  ||' [' " +
	    			 "  ||user_name " +
	    			 "  ||']' AS userName , " +
	    			 "  designation, " +
	    			 "  (TRUNC(sysdate)-TRUNC(s_date)) pending " +
	    			 "FROM t3 t2, " +
	    			 "  tm_service s " +
	    			 "WHERE t2.service             =s.service_code " +
	    			 "AND "+subQueryYear+" " +
	    			 "AND  "+subQueryServices+" " +
	    			 "ORDER BY YEAR, " +
	    			 "  state, " +
	    			 "  service_desc, " +
	    			 "  s_date" ; 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("REPORT_DATA_SOURCE_DETAILED", ds);
	     
	     }
		String tsPath = "/Reports/PendencyReportStatstics.jrxml";
		String fileName = "PendencyReport.pdf";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void generateExcel() throws Exception {

		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		
		parameters.put("selectYearList",selectYearList.toString().replace("[", "").replace("]", ""));
		parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);
		String reportQuery="";
		// static report
        String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	     String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	   
	    if(selectedServices.equals("ALL") && selectedServices.contains("ALL")){
	   parameters.put("selectServiceList","ALL");
	    }
	    else{
	    	ServicesDao sdao=new ServicesDao(connection);
	    	parameters.put("selectServiceList",sdao.getServiceList(selectedServices).toString());
	    }
	    		
	     String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")) )
	    	 subQueryServices=" SERVICE in("+selectedServices+")";
	     if((!selectedYear.trim().equals("ALL") ) && (!selectedYear.contains("ALL")) ){
	    	 if(reportDisplayType.equalsIgnoreCase("s"))  
	    	 subQueryYear="YEAR in ("+selectedYear+")"; 
	    	 else
	    		 subQueryYear="to_char(s_date, 'yyyy') in ("+selectedYear+")"; 
	     }
	      
	     if(reportDisplayType.equalsIgnoreCase("s")){
	    	/* reportQuery ="with t as (SELECT distinct A.application_id, "
	 				+ "CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) and a.application_id=b.application_id) THEN NULL "
	 				+ "ELSE  "
	 				+ " CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) AND status IN (5,6) and a.application_id=b.application_id) THEN 1  "
	 				+ " ELSE 0  "
	 				+ "END  "
	 				+ "END IB_STATUS, "
	 				+ "CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) and a.application_id=b.application_id) THEN NULL  "
	 				+ "ELSE  "
	 				+ " CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) AND status IN (5,6) and a.application_id=b.application_id) THEN 1  "
	 				+ " ELSE 0  "
	 				+ " END  "
	 				+ "END RAW_STATUS "
	 				+ "FROM t_pc_office_level_final_status A ), t2 AS ( "
	 				+ "select t.application_id, case when submission_date is null then created_on else submission_date end as s_date, b.service_code service,  "
	 				+ "CASE WHEN b.current_status=8 THEN 1 "
	 				+ "when (b.current_stage=1 and b.current_status=2) THEN 0 "
	 				+ "else  "
	 				+ " case when t.ib_status is null then  "
	 				+ "  case when t.raw_status=0 then 1  "
	 				+ "  else 0 "
	 				+ "  end "
	 				+ "  else  "
	 				+ "   case when t.ib_status=1 then  "
	 				+ "     case when t.raw_status=0 then 1 "
	 				+ "     else 0 "
	 				+ "     end "
	 				+ "   else 1 "
	 				+ "    end "
	 				+ "  end "
	 				+ "end "
	 				+ "MHA_STATUS,t.IB_status, t.raw_status, CASE WHEN b.current_status=8 THEN 0 ELSE 1 END applicant_status "
	 				+ "FROM v_application_details b LEFT JOIN t ON t.application_id=b.application_id "
	 				+ "where ((b.current_stage = 2 AND b.current_status IN (4, 8)) or (b.current_stage = 1 AND b.current_status=2)) "
	 				+ ") "
	 				+ ", t3 as ( "
	 				+ "SELECT * FROM ( "
	 				+ "SELECT to_char(s_date, 'yyyy') year, service, count(MHA_STATUS) pending, 'MHA' office  FROM t2 WHERE mha_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ " UNION "
	 				+ " SELECT to_char(s_date, 'yyyy'), service, count(IB_STATUS), 'IB' office  FROM t2 WHERE ib_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ " UNION "
	 				+ " SELECT to_char(s_date, 'yyyy'), service, count(RAW_STATUS), 'RAW' office  FROM t2 WHERE raw_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ "   UNION "
	 				+ " SELECT to_char(s_date, 'yyyy'), service, count(APPLICANT_STATUS), 'APPLICANT' office  FROM t2 WHERE applicant_status=0 GROUP BY to_char(s_date, 'yyyy'), service "
	 				+ " ) where "+subQueryServices+" ORDER BY YEAR, office, service)  "

	 				+ " SELECT * FROM (SELECT YEAR, service_desc, pending, office FROM t3, tm_service s WHERE t3.service=s.service_code) "
	 				+ " pivot (sum(pending) FOR(office) IN ('MHA' AS MHAS, 'IB' AS IBS, 'RAW' AS RAWS, 'APPLICANT' AS APPLICANTS))  where "+subQueryYear+" "
	 				+ "  ORDER BY YEAR, service_desc  ";*/
	    	 
	    	 reportQuery ="WITH t AS " +
	    			 "  (SELECT DISTINCT A.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END IB_STATUS, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END RAW_STATUS " +
	    			 "  FROM t_pc_office_level_final_status A " +
	    			 "  ), " +
	    			 "  t2 AS " +
	    			 "  (SELECT b.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN submission_date IS NULL " +
	    			 "      THEN created_on " +
	    			 "      ELSE submission_date " +
	    			 "    END AS s_date, " +
	    			 "    b.service_code service, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 1 " +
	    			 "      WHEN (b.current_stage=1 " +
	    			 "      AND b.current_status =2) " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN t.ib_status IS NULL " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN t.raw_status=0 " +
	    			 "              THEN 1 " +
	    			 "              ELSE 0 " +
	    			 "            END " +
	    			 "          ELSE " +
	    			 "            CASE " +
	    			 "              WHEN t.ib_status=1 " +
	    			 "              THEN " +
	    			 "                CASE " +
	    			 "                  WHEN t.raw_status=0 " +
	    			 "                  THEN 1 " +
	    			 "                  ELSE 0 " +
	    			 "                END " +
	    			 "              ELSE 1 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END MHA_STATUS, " +
	    			 "    t.IB_status, " +
	    			 "    t.raw_status, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 0 " +
	    			 "      ELSE 1 " +
	    			 "    END applicant_status, " +
	    			 "    CASE " +
	    			 "      WHEN current_stage=1 " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN current_stage=2 " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN current_status=4 " +
	    			 "              THEN 1 " +
	    			 "              WHEN current_status=9 " +
	    			 "              THEN 2 " +
	    			 "              WHEN current_status=10 " +
	    			 "              THEN 3 " +
	    			 "              WHEN current_status=12 " +
	    			 "              THEN 4 " +
	    			 "              WHEN current_status=8 " +
	    			 "              THEN 5 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END status " +
	    			 "  FROM v_application_details b " +
	    			 "  LEFT JOIN t " +
	    			 "  ON t.application_id =b.application_id " +
	    			 "  ) , " +
	    			 "  t4 AS " +
	    			 "  (SELECT t2.*, " +
	    			 "    a.user_id, " +
	    			 "    (SELECT designation_name " +
	    			 "    FROM tm_designation c " +
	    			 "    WHERE c.designation_id= " +
	    			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "      ) " +
	    			 "    ) designation, " +
	    			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "    ) designation_id " +
	    			 "  FROM t2 " +
	    			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
	    			 "  ON t2.application_id=a.application_id " +
	    			 "  AND a.office_code   ='MHA01' " +
	    			 "  ) , " +
	    			 "  t3 AS " +
	    			 "  (SELECT * " +
	    			 "  FROM " +
	    			 "    (SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'Total' office " +
	    			 "    FROM t4 " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'Pending' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE status IN (0,1) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(MHA_STATUS) pending, " +
	    			 "      'MHA' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status=0 " +
	    			 "    AND status     IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(IB_STATUS), " +
	    			 "      'IB' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE ib_status=0 " +
	    			 "    AND status    IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(RAW_STATUS), " +
	    			 "      'RAW' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE raw_status=0 " +
	    			 "    AND status     IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(APPLICANT_STATUS), " +
	    			 "      'APPLICANT' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE applicant_status=0 " +
	    			 "    AND status           IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'ASO' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=10 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'SO' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=9 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'US' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=8 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'DIR' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=6 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'OTHERS' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status       =0 " +
	    			 "    AND (user_id          IS NULL " +
	    			 "    OR designation_id     IS NULL " +
	    			 "    OR designation_id NOT IN (6,8,9,10)) " +
	    			 "    AND status            IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    ) " +
	    			 "  WHERE "+subQueryServices+" " +
	    			 "  ORDER BY YEAR, " +
	    			 "    office, " +
	    			 "    service " +
	    			 "  ) " +
	    			 "SELECT * " +
	    			 "FROM " +
	    			 "  (SELECT YEAR, " +
	    			 "    service_desc, " +
	    			 "    pending, " +
	    			 "    office " +
	    			 "  FROM t3, " +
	    			 "    tm_service s " +
	    			 "  WHERE t3.service                   =s.service_code " +
	    			 "  ) pivot (SUM(pending) FOR(office) IN ('Total' AS Total, 'Pending' AS Pending, 'MHA' AS MHAS, 'IB' AS IBS, 'RAW' AS RAWS, 'APPLICANT' AS APPLICANTS, 'ASO' AS ASO, 'SO' AS SO, 'US' AS US, 'DIR' AS DIR, 'OTHERS' AS OTHERS)) " +
	    			 "WHERE  "+subQueryYear+" " +
	    			 "ORDER BY YEAR, " +
	    			 "  service_desc " ;
	    	 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE", ds);
	    	 
	     }
	     else{
	    
	    	 /*reportQuery = "with t as (SELECT distinct A.application_id, "
						+ " CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) and a.application_id=b.application_id) THEN NULL  "
						+ " ELSE "
						+ "  CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=2) AND status IN (5,6) and a.application_id=b.application_id) THEN 1 "
						+ "  ELSE 0 "
						+ "  END "
						+ " END IB_STATUS, "
						+ " CASE WHEN NOT EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) and a.application_id=b.application_id) THEN NULL "
						+ " ELSE  "
						+ "  CASE WHEN EXISTS (SELECT b.application_id FROM t_pc_office_level_final_status b WHERE office_code IN (SELECT office_code FROM tm_office WHERE office_id=3) AND status IN (5,6) and a.application_id=b.application_id) THEN 1 "
						+ "  ELSE 0  "
						+ "  END  "
						+ " END RAW_STATUS  "
						+ " FROM t_pc_office_level_final_status A ), t2 AS (  "
						+ " select b.application_id, b.applicant_name,b.section_fileno,(select SNAME from TM_STATE where SCODE=b.state) as state,(select distname from TM_DISTRICT where distcode=b.district) as district, case when submission_date is null then created_on else submission_date end as s_date, b.service_code service,  "
						+ " CASE WHEN b.current_status=8 THEN 1  "
						+ " when (b.current_stage=1 and b.current_status=2) THEN 0  "
						+ " else  "
						+ "  case when t.ib_status is null then  "
						+ "    case when t.raw_status=0 then 1  "
						+ "  else 0  "
						+ "   end  "
						+ "  else   "
						+ "   case when t.ib_status=1 then  "
						+ "    case when t.raw_status=0 then 1  "
						+ "   else 0  "
						+ "    end  "
						+ "  else 1 "
						+ "  end "
						+ "  end "
						+ " END "
						+ " MHA_STATUS,t.IB_status, t.raw_status, CASE WHEN b.current_status=8 THEN 0 ELSE null END applicant_status "
						+ " FROM v_application_details b LEFT JOIN t ON t.application_id=b.application_id "
						+ " WHERE ((b.current_stage = 2 AND b.current_status IN (4, 8)) OR (b.current_stage = 1 AND b.current_status=2)) "
						+ " ) "

						+ " SELECT t2.application_id, t2.applicant_name, to_char(trunc(s_date)) as s_date, to_char(s_date, 'yyyy') year, service_desc, CASE WHEN mha_status=0 THEN 'Pending' ELSE NULL END AS mha,  "
						+ " CASE WHEN ib_status=0 THEN 'Pending' ELSE NULL END AS ib, "
						+ " CASE WHEN raw_status=0 THEN 'Pending' ELSE NULL END AS raw1, "
						+ " case when applicant_status=0 then 'Pending' else null end as applicant,t2.state,t2.section_fileno,t2.district from t2, tm_service s WHERE t2.service=s.service_code and  "
						+ subQueryYear + "  and " + subQueryServices + "  "
						+ "  ORDER BY YEAR, service_desc";*/
	    	 reportQuery = "WITH t AS " +
	    			 "  (SELECT DISTINCT A.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END IB_STATUS, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END RAW_STATUS " +
	    			 "  FROM t_pc_office_level_final_status A " +
	    			 "  ), " +
	    			 "  t2 AS " +
	    			 "  (SELECT b.application_id, " +
	    			 "    b.applicant_name, " +
	    			 "    b.section_fileno, " +
	    			 "    (SELECT SNAME FROM TM_STATE WHERE SCODE=b.state " +
	    			 "    ) AS state, " +
	    			 "    (SELECT distname FROM TM_DISTRICT WHERE distcode=b.district " +
	    			 "    ) AS district, " +
	    			 "    CASE " +
	    			 "      WHEN submission_date IS NULL " +
	    			 "      THEN created_on " +
	    			 "      ELSE submission_date " +
	    			 "    END AS s_date, " +
	    			 "    b.service_code service, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 1 " +
	    			 "      WHEN (b.current_stage=1 " +
	    			 "      AND b.current_status =2) " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN t.ib_status IS NULL " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN t.raw_status=0 " +
	    			 "              THEN 1 " +
	    			 "              ELSE 0 " +
	    			 "            END " +
	    			 "          ELSE " +
	    			 "            CASE " +
	    			 "              WHEN t.ib_status=1 " +
	    			 "              THEN " +
	    			 "                CASE " +
	    			 "                  WHEN t.raw_status=0 " +
	    			 "                  THEN 1 " +
	    			 "                  ELSE 0 " +
	    			 "                END " +
	    			 "              ELSE 1 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END MHA_STATUS, " +
	    			 "    t.IB_status, " +
	    			 "    t.raw_status, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 0 " +
	    			 "      ELSE NULL " +
	    			 "    END applicant_status " +
	    			 "  FROM v_application_details b " +
	    			 "  LEFT JOIN t " +
	    			 "  ON t.application_id     =b.application_id " +
	    			 "  WHERE ((b.current_stage = 2 " +
	    			 "  AND b.current_status   IN (4, 8)) " +
	    			 "  OR (b.current_stage     = 1 " +
	    			 "  AND b.current_status    =2)) " +
	    			 "  ), " +
	    			 "  t3 AS " +
	    			 "  (SELECT t2.*, " +
	    			 "    a.user_id, " +
	    			 "    (SELECT user_name FROM tm_user um WHERE um.user_id=a.user_id " +
	    			 "    ) user_name, " +
	    			 "    (SELECT designation_name " +
	    			 "    FROM tm_designation c " +
	    			 "    WHERE c.designation_id= " +
	    			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "      ) " +
	    			 "    ) designation, " +
	    			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "    ) designation_id " +
	    			 "  FROM t2 " +
	    			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
	    			 "  ON t2.application_id=a.application_id " +
	    			 "  AND a.office_code   = '"+loginOfficeCode+"' " +
	    			 "  ) " +
	    			 "SELECT t2.state, " +
	    			 "  t2.district, " +
	    			 "  TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "  TO_CHAR(TRUNC(s_date)) AS s_date, " +
	    			 "  service_desc, " +
	    			 "  t2.application_id, " +
	    			 "  t2.applicant_name, " +
	    			 "  t2.section_fileno, " +
	    			 "  CASE " +
	    			 "    WHEN mha_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS mha, " +
	    			 "  CASE " +
	    			 "    WHEN ib_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS ib, " +
	    			 "  CASE " +
	    			 "    WHEN raw_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS raw1, " +
	    			 "  CASE " +
	    			 "    WHEN applicant_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS applicant, " +
	    			 "  user_id " +
	    			 "  ||' [' " +
	    			 "  ||user_name " +
	    			 "  ||']' AS userName , " +
	    			 "  designation, " +
	    			 "  (TRUNC(sysdate)-TRUNC(s_date)) pending " +
	    			 "FROM t3 t2, " +
	    			 "  tm_service s " +
	    			 "WHERE t2.service             =s.service_code " +
	    			 "AND "+subQueryYear+" " +
	    			 "AND  "+subQueryServices+" " +
	    			 "ORDER BY YEAR, " +
	    			 "  state, " +
	    			 "  service_desc, " +
	    			 "  s_date" ; 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("REPORT_DATA_SOURCE_DETAILED", ds);
	     
	     }
		String tsPath = "/Reports/PendencyReportStatstics.jrxml";
		String fileName = "PendencyReport";
		//GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	
	}

	@Override
	protected void generateHTML() throws Exception {
		/*Write Codew For Html Display*/
	if(reportDisplayType.equalsIgnoreCase("s"))
        {
        pendencyReport=getPendencyReportStatatics();	
		totalRecords=getTotalRecords();	
		
	     }
	else {
		pendencyReport=getPendencyReportDeatils();
		totalRecords=getTotalRecords();	
		
		
	}
		
		
	}

	@Override
	protected void generateChart() throws Exception {
	}

	
	public List<PendencyReport> getPendencyReportStatatics() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}

		// static report
         String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	     String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
			
	     String  subQueryServices="1=1";
			String subQueryYear="1=1";
			 if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
	    	 subQueryServices=" SERVICE in("+selectedServices+")";
	     if((!selectedYear.trim().equals("ALL")) && (!selectedYear.contains("ALL")) )
	         subQueryYear="YEAR in ("+selectedYear+")";
	     
		StringBuffer countQuery = new StringBuffer("WITH t AS " +
   			 "  (SELECT DISTINCT A.application_id, " +
   			 "    CASE " +
   			 "      WHEN NOT EXISTS " +
   			 "        (SELECT b.application_id " +
   			 "        FROM t_pc_office_level_final_status b " +
   			 "        WHERE office_code IN " +
   			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
   			 "          ) " +
   			 "        AND a.application_id=b.application_id " +
   			 "        ) " +
   			 "      THEN NULL " +
   			 "      ELSE " +
   			 "        CASE " +
   			 "          WHEN EXISTS " +
   			 "            (SELECT b.application_id " +
   			 "            FROM t_pc_office_level_final_status b " +
   			 "            WHERE office_code IN " +
   			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
   			 "              ) " +
   			 "            AND status         IN (5,6) " +
   			 "            AND a.application_id=b.application_id " +
   			 "            ) " +
   			 "          THEN 1 " +
   			 "          ELSE 0 " +
   			 "        END " +
   			 "    END IB_STATUS, " +
   			 "    CASE " +
   			 "      WHEN NOT EXISTS " +
   			 "        (SELECT b.application_id " +
   			 "        FROM t_pc_office_level_final_status b " +
   			 "        WHERE office_code IN " +
   			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
   			 "          ) " +
   			 "        AND a.application_id=b.application_id " +
   			 "        ) " +
   			 "      THEN NULL " +
   			 "      ELSE " +
   			 "        CASE " +
   			 "          WHEN EXISTS " +
   			 "            (SELECT b.application_id " +
   			 "            FROM t_pc_office_level_final_status b " +
   			 "            WHERE office_code IN " +
   			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
   			 "              ) " +
   			 "            AND status         IN (5,6) " +
   			 "            AND a.application_id=b.application_id " +
   			 "            ) " +
   			 "          THEN 1 " +
   			 "          ELSE 0 " +
   			 "        END " +
   			 "    END RAW_STATUS " +
   			 "  FROM t_pc_office_level_final_status A " +
   			 "  ), " +
   			 "  t2 AS " +
   			 "  (SELECT b.application_id, " +
   			 "    CASE " +
   			 "      WHEN submission_date IS NULL " +
   			 "      THEN created_on " +
   			 "      ELSE submission_date " +
   			 "    END AS s_date, " +
   			 "    b.service_code service, " +
   			 "    CASE " +
   			 "      WHEN b.current_status=8 " +
   			 "      THEN 1 " +
   			 "      WHEN (b.current_stage=1 " +
   			 "      AND b.current_status =2) " +
   			 "      THEN 0 " +
   			 "      ELSE " +
   			 "        CASE " +
   			 "          WHEN t.ib_status IS NULL " +
   			 "          THEN " +
   			 "            CASE " +
   			 "              WHEN t.raw_status=0 " +
   			 "              THEN 1 " +
   			 "              ELSE 0 " +
   			 "            END " +
   			 "          ELSE " +
   			 "            CASE " +
   			 "              WHEN t.ib_status=1 " +
   			 "              THEN " +
   			 "                CASE " +
   			 "                  WHEN t.raw_status=0 " +
   			 "                  THEN 1 " +
   			 "                  ELSE 0 " +
   			 "                END " +
   			 "              ELSE 1 " +
   			 "            END " +
   			 "        END " +
   			 "    END MHA_STATUS, " +
   			 "    t.IB_status, " +
   			 "    t.raw_status, " +
   			 "    CASE " +
   			 "      WHEN b.current_status=8 " +
   			 "      THEN 0 " +
   			 "      ELSE 1 " +
   			 "    END applicant_status, " +
   			 "    CASE " +
   			 "      WHEN current_stage=1 " +
   			 "      THEN 0 " +
   			 "      ELSE " +
   			 "        CASE " +
   			 "          WHEN current_stage=2 " +
   			 "          THEN " +
   			 "            CASE " +
   			 "              WHEN current_status=4 " +
   			 "              THEN 1 " +
   			 "              WHEN current_status=9 " +
   			 "              THEN 2 " +
   			 "              WHEN current_status=10 " +
   			 "              THEN 3 " +
   			 "              WHEN current_status=12 " +
   			 "              THEN 4 " +
   			 "              WHEN current_status=8 " +
   			 "              THEN 5 " +
   			 "            END " +
   			 "        END " +
   			 "    END status " +
   			 "  FROM v_application_details b " +
   			 "  LEFT JOIN t " +
   			 "  ON t.application_id =b.application_id " +
   			 "  ) , " +
   			 "  t4 AS " +
   			 "  (SELECT t2.*, " +
   			 "    a.user_id, " +
   			 "    (SELECT designation_name " +
   			 "    FROM tm_designation c " +
   			 "    WHERE c.designation_id= " +
   			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
   			 "      ) " +
   			 "    ) designation, " +
   			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
   			 "    ) designation_id " +
   			 "  FROM t2 " +
   			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
   			 "  ON t2.application_id=a.application_id " +
   			 "  AND a.office_code   ='MHA01' " +
   			 "  ) , " +
   			 "  t3 AS " +
   			 "  (SELECT * " +
   			 "  FROM " +
   			 "    (SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(application_id) pending, " +
   			 "      'Total' office " +
   			 "    FROM t4 " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(application_id) pending, " +
   			 "      'Pending' office " +
   			 "    FROM t4 " +
   			 "    WHERE status IN (0,1) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(MHA_STATUS) pending, " +
   			 "      'MHA' office " +
   			 "    FROM t4 " +
   			 "    WHERE mha_status=0 " +
   			 "    AND status     IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
   			 "      service, " +
   			 "      COUNT(IB_STATUS), " +
   			 "      'IB' office " +
   			 "    FROM t4 " +
   			 "    WHERE ib_status=0 " +
   			 "    AND status    IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
   			 "      service, " +
   			 "      COUNT(RAW_STATUS), " +
   			 "      'RAW' office " +
   			 "    FROM t4 " +
   			 "    WHERE raw_status=0 " +
   			 "    AND status     IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
   			 "      service, " +
   			 "      COUNT(APPLICANT_STATUS), " +
   			 "      'APPLICANT' office " +
   			 "    FROM t4 " +
   			 "    WHERE applicant_status=0 " +
   			 "    AND status           IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(application_id) pending, " +
   			 "      'ASO' office " +
   			 "    FROM t4 " +
   			 "    WHERE mha_status  =0 " +
   			 "    AND designation_id=10 " +
   			 "    AND status       IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(application_id) pending, " +
   			 "      'SO' office " +
   			 "    FROM t4 " +
   			 "    WHERE mha_status  =0 " +
   			 "    AND designation_id=9 " +
   			 "    AND status       IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(application_id) pending, " +
   			 "      'US' office " +
   			 "    FROM t4 " +
   			 "    WHERE mha_status  =0 " +
   			 "    AND designation_id=8 " +
   			 "    AND status       IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(application_id) pending, " +
   			 "      'DIR' office " +
   			 "    FROM t4 " +
   			 "    WHERE mha_status  =0 " +
   			 "    AND designation_id=6 " +
   			 "    AND status       IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    UNION " +
   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
   			 "      service, " +
   			 "      COUNT(application_id) pending, " +
   			 "      'OTHERS' office " +
   			 "    FROM t4 " +
   			 "    WHERE mha_status       =0 " +
   			 "    AND (user_id          IS NULL " +
   			 "    OR designation_id     IS NULL " +
   			 "    OR designation_id NOT IN (6,8,9,10)) " +
   			 "    AND status            IN (0,1,5) " +
   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
   			 "      service " +
   			 "    ) " +
   			 "  WHERE "+subQueryServices+" " +
   			 "  ORDER BY YEAR, " +
   			 "    office, " +
   			 "    service " +
   			 "  ) " +
   			 " SELECT count(*) " +
   			 "FROM " +
   			 "  (SELECT YEAR, " +
   			 "    service_desc, " +
   			 "    pending, " +
   			 "    office " +
   			 "  FROM t3, " +
   			 "    tm_service s " +
   			 "  WHERE t3.service                   =s.service_code " +
   			 "  ) pivot (SUM(pending) FOR(office) IN ('Total' AS Total, 'Pending' AS Pending, 'MHA' AS MHAS, 'IB' AS IBS, 'RAW' AS RAWS, 'APPLICANT' AS APPLICANTS, 'ASO' AS ASO, 'SO' AS SO, 'US' AS US, 'DIR' AS DIR, 'OTHERS' AS OTHERS)) " +
   			 "WHERE  "+subQueryYear+" " +
   			 "ORDER BY YEAR, " +
   			 "  service_desc " );
		StringBuffer query = new StringBuffer("WITH t AS " +
	   			 "  (SELECT DISTINCT A.application_id, " +
	   			 "    CASE " +
	   			 "      WHEN NOT EXISTS " +
	   			 "        (SELECT b.application_id " +
	   			 "        FROM t_pc_office_level_final_status b " +
	   			 "        WHERE office_code IN " +
	   			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
	   			 "          ) " +
	   			 "        AND a.application_id=b.application_id " +
	   			 "        ) " +
	   			 "      THEN NULL " +
	   			 "      ELSE " +
	   			 "        CASE " +
	   			 "          WHEN EXISTS " +
	   			 "            (SELECT b.application_id " +
	   			 "            FROM t_pc_office_level_final_status b " +
	   			 "            WHERE office_code IN " +
	   			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
	   			 "              ) " +
	   			 "            AND status         IN (5,6) " +
	   			 "            AND a.application_id=b.application_id " +
	   			 "            ) " +
	   			 "          THEN 1 " +
	   			 "          ELSE 0 " +
	   			 "        END " +
	   			 "    END IB_STATUS, " +
	   			 "    CASE " +
	   			 "      WHEN NOT EXISTS " +
	   			 "        (SELECT b.application_id " +
	   			 "        FROM t_pc_office_level_final_status b " +
	   			 "        WHERE office_code IN " +
	   			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
	   			 "          ) " +
	   			 "        AND a.application_id=b.application_id " +
	   			 "        ) " +
	   			 "      THEN NULL " +
	   			 "      ELSE " +
	   			 "        CASE " +
	   			 "          WHEN EXISTS " +
	   			 "            (SELECT b.application_id " +
	   			 "            FROM t_pc_office_level_final_status b " +
	   			 "            WHERE office_code IN " +
	   			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
	   			 "              ) " +
	   			 "            AND status         IN (5,6) " +
	   			 "            AND a.application_id=b.application_id " +
	   			 "            ) " +
	   			 "          THEN 1 " +
	   			 "          ELSE 0 " +
	   			 "        END " +
	   			 "    END RAW_STATUS " +
	   			 "  FROM t_pc_office_level_final_status A " +
	   			 "  ), " +
	   			 "  t2 AS " +
	   			 "  (SELECT b.application_id, " +
	   			 "    CASE " +
	   			 "      WHEN submission_date IS NULL " +
	   			 "      THEN created_on " +
	   			 "      ELSE submission_date " +
	   			 "    END AS s_date, " +
	   			 "    b.service_code service, " +
	   			 "    CASE " +
	   			 "      WHEN b.current_status=8 " +
	   			 "      THEN 1 " +
	   			 "      WHEN (b.current_stage=1 " +
	   			 "      AND b.current_status =2) " +
	   			 "      THEN 0 " +
	   			 "      ELSE " +
	   			 "        CASE " +
	   			 "          WHEN t.ib_status IS NULL " +
	   			 "          THEN " +
	   			 "            CASE " +
	   			 "              WHEN t.raw_status=0 " +
	   			 "              THEN 1 " +
	   			 "              ELSE 0 " +
	   			 "            END " +
	   			 "          ELSE " +
	   			 "            CASE " +
	   			 "              WHEN t.ib_status=1 " +
	   			 "              THEN " +
	   			 "                CASE " +
	   			 "                  WHEN t.raw_status=0 " +
	   			 "                  THEN 1 " +
	   			 "                  ELSE 0 " +
	   			 "                END " +
	   			 "              ELSE 1 " +
	   			 "            END " +
	   			 "        END " +
	   			 "    END MHA_STATUS, " +
	   			 "    t.IB_status, " +
	   			 "    t.raw_status, " +
	   			 "    CASE " +
	   			 "      WHEN b.current_status=8 " +
	   			 "      THEN 0 " +
	   			 "      ELSE 1 " +
	   			 "    END applicant_status, " +
	   			 "    CASE " +
	   			 "      WHEN current_stage=1 " +
	   			 "      THEN 0 " +
	   			 "      ELSE " +
	   			 "        CASE " +
	   			 "          WHEN current_stage=2 " +
	   			 "          THEN " +
	   			 "            CASE " +
	   			 "              WHEN current_status=4 " +
	   			 "              THEN 1 " +
	   			 "              WHEN current_status=9 " +
	   			 "              THEN 2 " +
	   			 "              WHEN current_status=10 " +
	   			 "              THEN 3 " +
	   			 "              WHEN current_status=12 " +
	   			 "              THEN 4 " +
	   			 "              WHEN current_status=8 " +
	   			 "              THEN 5 " +
	   			 "            END " +
	   			 "        END " +
	   			 "    END status " +
	   			 "  FROM v_application_details b " +
	   			 "  LEFT JOIN t " +
	   			 "  ON t.application_id =b.application_id " +
	   			 "  ) , " +
	   			 "  t4 AS " +
	   			 "  (SELECT t2.*, " +
	   			 "    a.user_id, " +
	   			 "    (SELECT designation_name " +
	   			 "    FROM tm_designation c " +
	   			 "    WHERE c.designation_id= " +
	   			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	   			 "      ) " +
	   			 "    ) designation, " +
	   			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	   			 "    ) designation_id " +
	   			 "  FROM t2 " +
	   			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
	   			 "  ON t2.application_id=a.application_id " +
	   			 "  AND a.office_code   ='MHA01' " +
	   			 "  ) , " +
	   			 "  t3 AS " +
	   			 "  (SELECT * " +
	   			 "  FROM " +
	   			 "    (SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(application_id) pending, " +
	   			 "      'Total' office " +
	   			 "    FROM t4 " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(application_id) pending, " +
	   			 "      'Pending' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE status IN (0,1) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(MHA_STATUS) pending, " +
	   			 "      'MHA' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE mha_status=0 " +
	   			 "    AND status     IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service, " +
	   			 "      COUNT(IB_STATUS), " +
	   			 "      'IB' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE ib_status=0 " +
	   			 "    AND status    IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service, " +
	   			 "      COUNT(RAW_STATUS), " +
	   			 "      'RAW' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE raw_status=0 " +
	   			 "    AND status     IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service, " +
	   			 "      COUNT(APPLICANT_STATUS), " +
	   			 "      'APPLICANT' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE applicant_status=0 " +
	   			 "    AND status           IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(application_id) pending, " +
	   			 "      'ASO' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE mha_status  =0 " +
	   			 "    AND designation_id=10 " +
	   			 "    AND status       IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(application_id) pending, " +
	   			 "      'SO' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE mha_status  =0 " +
	   			 "    AND designation_id=9 " +
	   			 "    AND status       IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(application_id) pending, " +
	   			 "      'US' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE mha_status  =0 " +
	   			 "    AND designation_id=8 " +
	   			 "    AND status       IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(application_id) pending, " +
	   			 "      'DIR' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE mha_status  =0 " +
	   			 "    AND designation_id=6 " +
	   			 "    AND status       IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    UNION " +
	   			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	   			 "      service, " +
	   			 "      COUNT(application_id) pending, " +
	   			 "      'OTHERS' office " +
	   			 "    FROM t4 " +
	   			 "    WHERE mha_status       =0 " +
	   			 "    AND (user_id          IS NULL " +
	   			 "    OR designation_id     IS NULL " +
	   			 "    OR designation_id NOT IN (6,8,9,10)) " +
	   			 "    AND status            IN (0,1,5) " +
	   			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	   			 "      service " +
	   			 "    ) " +
	   			 "  WHERE "+subQueryServices+" " +
	   			 "  ORDER BY YEAR, " +
	   			 "    office, " +
	   			 "    service " +
	   			 "  ),t5 as ( " +
	   			 " SELECT * " +
	   			 "FROM " +
	   			 "  (SELECT YEAR, " +
	   			 "    service_desc, " +
	   			 "    pending, " +
	   			 "    office " +
	   			 "  FROM t3, " +
	   			 "    tm_service s " +
	   			 "  WHERE t3.service                   =s.service_code " +
	   			 "  ) pivot (SUM(pending) FOR(office) IN ('Total' AS Total, 'Pending' AS Pending, 'MHA' AS MHAS, 'IB' AS IBS, 'RAW' AS RAWS, 'APPLICANT' AS APPLICANTS, 'ASO' AS ASO, 'SO' AS SO, 'US' AS US, 'DIR' AS DIR, 'OTHERS' AS OTHERS)) " +
	   			 "WHERE  "+subQueryYear+" " +
	   			 "ORDER BY YEAR, " +
	   			 "  service_desc) , T6 AS (SELECT t5.*, ROWNUM RN FROM t5 ) SELECT * FROM T6 WHERE RN between ? and ? " );
	
		  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
		  ResultSet rs = statement.executeQuery(); if(rs.next())
		     { 
			  totalRecords = rs.getString(1);
		      } 
		  rs.close();
		 statement.close();
	
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
	      statement = connection.prepareStatement(query
				.toString());
		if (pageNum == null || recordsPerPage == null) {

		} else {
			statement.setInt(1, (pageRequested - 1) * pageSize + 1);
			statement.setInt(2, (pageRequested - 1) * pageSize + pageSize);
			
		}
		System.out.println("html--"+query);
		 rs = statement.executeQuery();

		List<PendencyReport> pendencyReportList = new ArrayList<PendencyReport>();
	
		while (rs.next()) {
			
			pendencyReportList.add(new PendencyReport(checkNull(rs.getString("YEAR")),checkNull( rs
					.getString("service_desc")),checkNull( rs.getString("MHAS")), checkNull(rs.getString("IBS")),checkNull( rs
					.getString("RAWS")),checkNull( rs.getString("APPLICANTS")),checkNull( rs.getString("Total")),checkNull( rs.getString("Pending")),checkNull( rs.getString("ASO")),checkNull( rs.getString("SO")),checkNull( rs.getString("US")),checkNull( rs.getString("DIR")),checkNull( rs.getString("OTHERS"))));
		}
	
		return pendencyReportList;
	}
	
	//Pendency Report Details
	public List<PendencyReport> getPendencyReportDeatils() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		// static report
        String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	     String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	     
	        String  subQueryServices="1=1";
			String subQueryYear="1=1";
			 if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
	    	 subQueryServices=" service in ("+selectedServices+")";
	     if((!selectedYear.trim().equals("ALL")) && (!selectedYear.contains("ALL")) )
	         subQueryYear=" to_char(s_date, 'yyyy') in("+selectedYear+")";
	     
		StringBuffer countQuery = new StringBuffer("WITH t AS " +
				"  (SELECT DISTINCT A.application_id, " +
				"    CASE " +
				"      WHEN NOT EXISTS " +
				"        (SELECT b.application_id " +
				"        FROM t_pc_office_level_final_status b " +
				"        WHERE office_code IN " +
				"          (SELECT office_code FROM tm_office WHERE office_id=2 " +
				"          ) " +
				"        AND a.application_id=b.application_id " +
				"        ) " +
				"      THEN NULL " +
				"      ELSE " +
				"        CASE " +
				"          WHEN EXISTS " +
				"            (SELECT b.application_id " +
				"            FROM t_pc_office_level_final_status b " +
				"            WHERE office_code IN " +
				"              (SELECT office_code FROM tm_office WHERE office_id=2 " +
				"              ) " +
				"            AND status         IN (5,6) " +
				"            AND a.application_id=b.application_id " +
				"            ) " +
				"          THEN 1 " +
				"          ELSE 0 " +
				"        END " +
				"    END IB_STATUS, " +
				"    CASE " +
				"      WHEN NOT EXISTS " +
				"        (SELECT b.application_id " +
				"        FROM t_pc_office_level_final_status b " +
				"        WHERE office_code IN " +
				"          (SELECT office_code FROM tm_office WHERE office_id=3 " +
				"          ) " +
				"        AND a.application_id=b.application_id " +
				"        ) " +
				"      THEN NULL " +
				"      ELSE " +
				"        CASE " +
				"          WHEN EXISTS " +
				"            (SELECT b.application_id " +
				"            FROM t_pc_office_level_final_status b " +
				"            WHERE office_code IN " +
				"              (SELECT office_code FROM tm_office WHERE office_id=3 " +
				"              ) " +
				"            AND status         IN (5,6) " +
				"            AND a.application_id=b.application_id " +
				"            ) " +
				"          THEN 1 " +
				"          ELSE 0 " +
				"        END " +
				"    END RAW_STATUS " +
				"  FROM t_pc_office_level_final_status A " +
				"  ), " +
				"  t2 AS " +
				"  (SELECT b.application_id, " +
				"    b.applicant_name, " +
				"    b.section_fileno, " +
				"    (SELECT SNAME FROM TM_STATE WHERE SCODE=b.state " +
				"    ) AS state, " +
				"    (SELECT distname FROM TM_DISTRICT WHERE distcode=b.district " +
				"    ) AS district, " +
				"    CASE " +
				"      WHEN submission_date IS NULL " +
				"      THEN created_on " +
				"      ELSE submission_date " +
				"    END AS s_date, " +
				"    b.service_code service, " +
				"    CASE " +
				"      WHEN b.current_status=8 " +
				"      THEN 1 " +
				"      WHEN (b.current_stage=1 " +
				"      AND b.current_status =2) " +
				"      THEN 0 " +
				"      ELSE " +
				"        CASE " +
				"          WHEN t.ib_status IS NULL " +
				"          THEN " +
				"            CASE " +
				"              WHEN t.raw_status=0 " +
				"              THEN 1 " +
				"              ELSE 0 " +
				"            END " +
				"          ELSE " +
				"            CASE " +
				"              WHEN t.ib_status=1 " +
				"              THEN " +
				"                CASE " +
				"                  WHEN t.raw_status=0 " +
				"                  THEN 1 " +
				"                  ELSE 0 " +
				"                END " +
				"              ELSE 1 " +
				"            END " +
				"        END " +
				"    END MHA_STATUS, " +
				"    t.IB_status, " +
				"    t.raw_status, " +
				"    CASE " +
				"      WHEN b.current_status=8 " +
				"      THEN 0 " +
				"      ELSE NULL " +
				"    END applicant_status " +
				"  FROM v_application_details b " +
				"  LEFT JOIN t " +
				"  ON t.application_id     =b.application_id " +
				"  WHERE ((b.current_stage = 2 " +
				"  AND b.current_status   IN (4, 8)) " +
				"  OR (b.current_stage     = 1 " +
				"  AND b.current_status    =2)) " +
				"  ), " +
				"  t3 AS " +
				"  (SELECT t2.*, " +
				"    a.user_id, " +
				"    (SELECT user_name FROM tm_user um WHERE um.user_id=a.user_id " +
				"    ) user_name, " +
				"    (SELECT designation_name " +
				"    FROM tm_designation c " +
				"    WHERE c.designation_id= " +
				"      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
				"      ) " +
				"    ) designation, " +
				"    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
				"    ) designation_id " +
				"  FROM t2 " +
				"  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
				"  ON t2.application_id=a.application_id " +
				"  AND a.office_code   = '"+loginOfficeCode+"' " +
				"  ) , " +
				"  t4 AS " +
				"  (SELECT t2.state, " +
				"    t2.district, " +
				"    TO_CHAR(s_date, 'yyyy') YEAR, " +
				"    TO_CHAR(TRUNC(s_date)) AS s_date, " +
				"    service_desc, " +
				"    t2.application_id, " +
				"    t2.applicant_name, " +
				"    t2.section_fileno, " +
				"    CASE " +
				"      WHEN mha_status=0 " +
				"      THEN 'X' " +
				"      ELSE NULL " +
				"    END AS mha, " +
				"    CASE " +
				"      WHEN ib_status=0 " +
				"      THEN 'X' " +
				"      ELSE NULL " +
				"    END AS ib, " +
				"    CASE " +
				"      WHEN raw_status=0 " +
				"      THEN 'X' " +
				"      ELSE NULL " +
				"    END AS raw1, " +
				"    CASE " +
				"      WHEN applicant_status=0 " +
				"      THEN 'X' " +
				"      ELSE NULL " +
				"    END AS applicant, " +
				"    user_id " +
				"    ||' [' " +
				"    ||user_name " +
				"    ||']' AS userName , " +
				"    designation, " +
				"    (TRUNC(sysdate)-TRUNC(s_date)) pending " +
				"  FROM t3 t2, " +
				"    tm_service s " +
				"  WHERE t2.service =s.service_code " +
				"  AND  "+subQueryServices+" " +
				"  AND "+subQueryYear+" " +
				"  ORDER BY YEAR, " +
				"    state, " +
				"    service_desc, " +
				"    s_date " +
				"  )  " +
				" SELECT count(*) FROM T4 "
				);
StringBuffer query = new StringBuffer("WITH t AS " +
		"  (SELECT DISTINCT A.application_id, " +
		"    CASE " +
		"      WHEN NOT EXISTS " +
		"        (SELECT b.application_id " +
		"        FROM t_pc_office_level_final_status b " +
		"        WHERE office_code IN " +
		"          (SELECT office_code FROM tm_office WHERE office_id=2 " +
		"          ) " +
		"        AND a.application_id=b.application_id " +
		"        ) " +
		"      THEN NULL " +
		"      ELSE " +
		"        CASE " +
		"          WHEN EXISTS " +
		"            (SELECT b.application_id " +
		"            FROM t_pc_office_level_final_status b " +
		"            WHERE office_code IN " +
		"              (SELECT office_code FROM tm_office WHERE office_id=2 " +
		"              ) " +
		"            AND status         IN (5,6) " +
		"            AND a.application_id=b.application_id " +
		"            ) " +
		"          THEN 1 " +
		"          ELSE 0 " +
		"        END " +
		"    END IB_STATUS, " +
		"    CASE " +
		"      WHEN NOT EXISTS " +
		"        (SELECT b.application_id " +
		"        FROM t_pc_office_level_final_status b " +
		"        WHERE office_code IN " +
		"          (SELECT office_code FROM tm_office WHERE office_id=3 " +
		"          ) " +
		"        AND a.application_id=b.application_id " +
		"        ) " +
		"      THEN NULL " +
		"      ELSE " +
		"        CASE " +
		"          WHEN EXISTS " +
		"            (SELECT b.application_id " +
		"            FROM t_pc_office_level_final_status b " +
		"            WHERE office_code IN " +
		"              (SELECT office_code FROM tm_office WHERE office_id=3 " +
		"              ) " +
		"            AND status         IN (5,6) " +
		"            AND a.application_id=b.application_id " +
		"            ) " +
		"          THEN 1 " +
		"          ELSE 0 " +
		"        END " +
		"    END RAW_STATUS " +
		"  FROM t_pc_office_level_final_status A " +
		"  ), " +
		"  t2 AS " +
		"  (SELECT b.application_id, " +
		"    b.applicant_name, " +
		"    b.section_fileno, " +
		"    (SELECT SNAME FROM TM_STATE WHERE SCODE=b.state " +
		"    ) AS state, " +
		"    (SELECT distname FROM TM_DISTRICT WHERE distcode=b.district " +
		"    ) AS district, " +
		"    CASE " +
		"      WHEN submission_date IS NULL " +
		"      THEN created_on " +
		"      ELSE submission_date " +
		"    END AS s_date, " +
		"    b.service_code service, " +
		"    CASE " +
		"      WHEN b.current_status=8 " +
		"      THEN 1 " +
		"      WHEN (b.current_stage=1 " +
		"      AND b.current_status =2) " +
		"      THEN 0 " +
		"      ELSE " +
		"        CASE " +
		"          WHEN t.ib_status IS NULL " +
		"          THEN " +
		"            CASE " +
		"              WHEN t.raw_status=0 " +
		"              THEN 1 " +
		"              ELSE 0 " +
		"            END " +
		"          ELSE " +
		"            CASE " +
		"              WHEN t.ib_status=1 " +
		"              THEN " +
		"                CASE " +
		"                  WHEN t.raw_status=0 " +
		"                  THEN 1 " +
		"                  ELSE 0 " +
		"                END " +
		"              ELSE 1 " +
		"            END " +
		"        END " +
		"    END MHA_STATUS, " +
		"    t.IB_status, " +
		"    t.raw_status, " +
		"    CASE " +
		"      WHEN b.current_status=8 " +
		"      THEN 0 " +
		"      ELSE NULL " +
		"    END applicant_status " +
		"  FROM v_application_details b " +
		"  LEFT JOIN t " +
		"  ON t.application_id     =b.application_id " +
		"  WHERE ((b.current_stage = 2 " +
		"  AND b.current_status   IN (4, 8)) " +
		"  OR (b.current_stage     = 1 " +
		"  AND b.current_status    =2)) " +
		"  ), " +
		"  t3 AS " +
		"  (SELECT t2.*, " +
		"    a.user_id, " +
		"    (SELECT user_name FROM tm_user um WHERE um.user_id=a.user_id " +
		"    ) user_name, " +
		"    (SELECT designation_name " +
		"    FROM tm_designation c " +
		"    WHERE c.designation_id= " +
		"      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
		"      ) " +
		"    ) designation, " +
		"    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
		"    ) designation_id " +
		"  FROM t2 " +
		"  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
		"  ON t2.application_id=a.application_id " +
		"  AND a.office_code   = '"+loginOfficeCode+"' " +
		"  ) , " +
		"  t4 AS " +
		"  (SELECT t2.state, " +
		"    t2.district, " +
		"    TO_CHAR(s_date, 'yyyy') YEAR, " +
		"    TO_CHAR(TRUNC(s_date)) AS s_date, " +
		"    service_desc, " +
		"    t2.application_id, " +
		"    t2.applicant_name, " +
		"    t2.section_fileno, " +
		"    CASE " +
		"      WHEN mha_status=0 " +
		"      THEN 'X' " +
		"      ELSE NULL " +
		"    END AS mha, " +
		"    CASE " +
		"      WHEN ib_status=0 " +
		"      THEN 'X' " +
		"      ELSE NULL " +
		"    END AS ib, " +
		"    CASE " +
		"      WHEN raw_status=0 " +
		"      THEN 'X' " +
		"      ELSE NULL " +
		"    END AS raw1, " +
		"    CASE " +
		"      WHEN applicant_status=0 " +
		"      THEN 'X' " +
		"      ELSE NULL " +
		"    END AS applicant, " +
		"    user_id " +
		"    ||' [' " +
		"    ||user_name " +
		"    ||']' AS userName , " +
		"    designation, " +
		"    (TRUNC(sysdate)-TRUNC(s_date)) pending " +
		"  FROM t3 t2, " +
		"    tm_service s " +
		"  WHERE t2.service =s.service_code " +
		"  AND  "+subQueryServices+" " +
		"  AND "+subQueryYear+" " +
		"  ORDER BY YEAR, " +
		"    state, " +
		"    service_desc, " +
		"    s_date " +
		"  ) , " +
		"  T5 AS " +
		"  (SELECT T4.*, ROWNUM RN FROM T4 " +
		"  ) " +
		"SELECT * FROM T5 WHERE RN BETWEEN ? AND ?"
		);
	
		    PreparedStatement statement = connection.prepareStatement(countQuery.toString());
		  ResultSet rs = statement.executeQuery(); if(rs.next())
		 { 
			  totalRecords = rs.getString(1);
		 } 
		  rs.close();
		 statement.close();
	
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
	      statement = connection.prepareStatement(query.toString());
		if (pageNum == null || recordsPerPage == null) {

		} else {
			statement.setInt(1, (pageRequested - 1) * pageSize + 1);
			statement.setInt(2, (pageRequested - 1) * pageSize + pageSize);
			
		}
		System.out.println(query);
		 rs = statement.executeQuery();

		List<PendencyReport> pendencyReportList = new ArrayList<PendencyReport>();
	
		while (rs.next()) {
			
			pendencyReportList.add(new PendencyReport(checkNull(rs.getString("application_id")), checkNull(rs.getString("applicant_name")), checkNull(rs.getString("s_date")),checkNull( rs.getString("YEAR")),
					checkNull(rs.getString("service_desc")), checkNull(rs.getString("mha")), checkNull(rs.getString("ib")),checkNull(rs.getString("raw1")),checkNull(rs.getString("applicant")),checkNull(rs.getString("state")),checkNull(rs.getString("section_fileno")),checkNull(rs.getString("district")),checkNull(rs.getString("userName")),checkNull(rs.getString("designation")),checkNull(rs.getString("pending"))));
		}
	
		return pendencyReportList;
	}
	
	public String checkNull(String val){
		if(val==null||val=="")
			val="-";
		return val;
	}
	
	
	
	public List<PendencyReport> getPendencyReport() {
		return pendencyReport;
	}



	public void setPendencyReport(List<PendencyReport> pendencyReport) {
		this.pendencyReport = pendencyReport;
	}



	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}



	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
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



	public String getTotalRecords() {
		return totalRecords;
	}



	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}



	public List<String> getSelectServiceList() {
		return selectServiceList;
	}



	public void setSelectServiceList(List<String> selectServiceList) {
		this.selectServiceList = selectServiceList;
	}



	public List<String> getSelectYearList() {
		return selectYearList;
	}



	public void setSelectYearList(List<String> selectYearList) {
		this.selectYearList = selectYearList;
	}



	public String getReportDisplayType() {
		return reportDisplayType;
	}



	public void setReportDisplayType(String reportDisplayType) {
		this.reportDisplayType = reportDisplayType;
	}



	public String getLoginOfficeName() {
		return loginOfficeName;
	}



	public void setLoginOfficeName(String loginOfficeName) {
		this.loginOfficeName = loginOfficeName;
	}



	public String getLoginUserName() {
		return loginUserName;
	}



	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}



	public String getLoginOfficeCode() {
		return loginOfficeCode;
	}



	public void setLoginOfficeCode(String loginOfficeCode) {
		this.loginOfficeCode = loginOfficeCode;
	}



	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap) throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);//("reportType");
			reportFormat = parameterMap.get("reportFormat").get(0);//("reportFormat");
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
		reportDisplayType=parameterMap.get("reportDisplyType").get(0);
		selectServiceList=parameterMap.get("service-type");
		selectYearList=parameterMap.get("statusYear-List");
		loginOfficeName=parameterMap.get("loginOfficeName").get(0);
		loginUserName=parameterMap.get("loginUserName").get(0);
	     loginOfficeCode=parameterMap.get("myLoginOfficCode").get(0);
		}		
	}



	@Override
	protected void generateCSV() throws Exception {


		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		
		parameters.put("selectYearList",selectYearList.toString().replace("[", "").replace("]", ""));
		parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);
		String reportQuery="";
		// static report
        String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	     String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	   
	    if(selectedServices.equals("ALL") && selectedServices.contains("ALL")){
	       parameters.put("selectServiceList","ALL");
	    }
	    else{
	    	ServicesDao sdao=new ServicesDao(connection);
	    	parameters.put("selectServiceList",sdao.getServiceList(selectedServices).toString());
	    }
	    		
	     String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
	    	 subQueryServices=" SERVICE in("+selectedServices+")";
	     if((!selectedYear.trim().equals("ALL") ) && (!selectedYear.contains("ALL")) ){
	    	 if(reportDisplayType.equalsIgnoreCase("s"))  
	    	 subQueryYear="YEAR in ("+selectedYear+")"; 
	    	 else
	    		 subQueryYear="to_char(s_date, 'yyyy') in ("+selectedYear+")"; 
	     }
	      
	     if(reportDisplayType.equalsIgnoreCase("s")){
	    	 reportQuery ="WITH t AS " +
	    			 "  (SELECT DISTINCT A.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END IB_STATUS, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END RAW_STATUS " +
	    			 "  FROM t_pc_office_level_final_status A " +
	    			 "  ), " +
	    			 "  t2 AS " +
	    			 "  (SELECT b.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN submission_date IS NULL " +
	    			 "      THEN created_on " +
	    			 "      ELSE submission_date " +
	    			 "    END AS s_date, " +
	    			 "    b.service_code service, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 1 " +
	    			 "      WHEN (b.current_stage=1 " +
	    			 "      AND b.current_status =2) " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN t.ib_status IS NULL " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN t.raw_status=0 " +
	    			 "              THEN 1 " +
	    			 "              ELSE 0 " +
	    			 "            END " +
	    			 "          ELSE " +
	    			 "            CASE " +
	    			 "              WHEN t.ib_status=1 " +
	    			 "              THEN " +
	    			 "                CASE " +
	    			 "                  WHEN t.raw_status=0 " +
	    			 "                  THEN 1 " +
	    			 "                  ELSE 0 " +
	    			 "                END " +
	    			 "              ELSE 1 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END MHA_STATUS, " +
	    			 "    t.IB_status, " +
	    			 "    t.raw_status, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 0 " +
	    			 "      ELSE 1 " +
	    			 "    END applicant_status, " +
	    			 "    CASE " +
	    			 "      WHEN current_stage=1 " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN current_stage=2 " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN current_status=4 " +
	    			 "              THEN 1 " +
	    			 "              WHEN current_status=9 " +
	    			 "              THEN 2 " +
	    			 "              WHEN current_status=10 " +
	    			 "              THEN 3 " +
	    			 "              WHEN current_status=12 " +
	    			 "              THEN 4 " +
	    			 "              WHEN current_status=8 " +
	    			 "              THEN 5 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END status " +
	    			 "  FROM v_application_details b " +
	    			 "  LEFT JOIN t " +
	    			 "  ON t.application_id =b.application_id " +
	    			 "  ) , " +
	    			 "  t4 AS " +
	    			 "  (SELECT t2.*, " +
	    			 "    a.user_id, " +
	    			 "    (SELECT designation_name " +
	    			 "    FROM tm_designation c " +
	    			 "    WHERE c.designation_id= " +
	    			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "      ) " +
	    			 "    ) designation, " +
	    			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "    ) designation_id " +
	    			 "  FROM t2 " +
	    			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
	    			 "  ON t2.application_id=a.application_id " +
	    			 "  AND a.office_code   ='MHA01' " +
	    			 "  ) , " +
	    			 "  t3 AS " +
	    			 "  (SELECT * " +
	    			 "  FROM " +
	    			 "    (SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'Total' office " +
	    			 "    FROM t4 " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'Pending' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE status IN (0,1) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(MHA_STATUS) pending, " +
	    			 "      'MHA' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status=0 " +
	    			 "    AND status     IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(IB_STATUS), " +
	    			 "      'IB' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE ib_status=0 " +
	    			 "    AND status    IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(RAW_STATUS), " +
	    			 "      'RAW' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE raw_status=0 " +
	    			 "    AND status     IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service, " +
	    			 "      COUNT(APPLICANT_STATUS), " +
	    			 "      'APPLICANT' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE applicant_status=0 " +
	    			 "    AND status           IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'ASO' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=10 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'SO' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=9 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'US' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=8 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'DIR' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status  =0 " +
	    			 "    AND designation_id=6 " +
	    			 "    AND status       IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    UNION " +
	    			 "    SELECT TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "      service, " +
	    			 "      COUNT(application_id) pending, " +
	    			 "      'OTHERS' office " +
	    			 "    FROM t4 " +
	    			 "    WHERE mha_status       =0 " +
	    			 "    AND (user_id          IS NULL " +
	    			 "    OR designation_id     IS NULL " +
	    			 "    OR designation_id NOT IN (6,8,9,10)) " +
	    			 "    AND status            IN (0,1,5) " +
	    			 "    GROUP BY TO_CHAR(s_date, 'yyyy'), " +
	    			 "      service " +
	    			 "    ) " +
	    			 "  WHERE "+subQueryServices+" " +
	    			 "  ORDER BY YEAR, " +
	    			 "    office, " +
	    			 "    service " +
	    			 "  ) " +
	    			 "SELECT * " +
	    			 "FROM " +
	    			 "  (SELECT YEAR, " +
	    			 "    service_desc, " +
	    			 "    pending, " +
	    			 "    office " +
	    			 "  FROM t3, " +
	    			 "    tm_service s " +
	    			 "  WHERE t3.service                   =s.service_code " +
	    			 "  ) pivot (SUM(pending) FOR(office) IN ('Total' AS Total, 'Pending' AS Pending, 'MHA' AS MHAS, 'IB' AS IBS, 'RAW' AS RAWS, 'APPLICANT' AS APPLICANTS, 'ASO' AS ASO, 'SO' AS SO, 'US' AS US, 'DIR' AS DIR, 'OTHERS' AS OTHERS)) " +
	    			 "WHERE  "+subQueryYear+" " +
	    			 "ORDER BY YEAR, " +
	    			 "  service_desc " ;
	    	 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE", ds);
	    	 
	     }
	     else{
	    
	    	 reportQuery = "WITH t AS " +
	    			 "  (SELECT DISTINCT A.application_id, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=2 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END IB_STATUS, " +
	    			 "    CASE " +
	    			 "      WHEN NOT EXISTS " +
	    			 "        (SELECT b.application_id " +
	    			 "        FROM t_pc_office_level_final_status b " +
	    			 "        WHERE office_code IN " +
	    			 "          (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "          ) " +
	    			 "        AND a.application_id=b.application_id " +
	    			 "        ) " +
	    			 "      THEN NULL " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN EXISTS " +
	    			 "            (SELECT b.application_id " +
	    			 "            FROM t_pc_office_level_final_status b " +
	    			 "            WHERE office_code IN " +
	    			 "              (SELECT office_code FROM tm_office WHERE office_id=3 " +
	    			 "              ) " +
	    			 "            AND status         IN (5,6) " +
	    			 "            AND a.application_id=b.application_id " +
	    			 "            ) " +
	    			 "          THEN 1 " +
	    			 "          ELSE 0 " +
	    			 "        END " +
	    			 "    END RAW_STATUS " +
	    			 "  FROM t_pc_office_level_final_status A " +
	    			 "  ), " +
	    			 "  t2 AS " +
	    			 "  (SELECT b.application_id, " +
	    			 "    b.applicant_name, " +
	    			 "    b.section_fileno, " +
	    			 "    (SELECT SNAME FROM TM_STATE WHERE SCODE=b.state " +
	    			 "    ) AS state, " +
	    			 "    (SELECT distname FROM TM_DISTRICT WHERE distcode=b.district " +
	    			 "    ) AS district, " +
	    			 "    CASE " +
	    			 "      WHEN submission_date IS NULL " +
	    			 "      THEN created_on " +
	    			 "      ELSE submission_date " +
	    			 "    END AS s_date, " +
	    			 "    b.service_code service, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 1 " +
	    			 "      WHEN (b.current_stage=1 " +
	    			 "      AND b.current_status =2) " +
	    			 "      THEN 0 " +
	    			 "      ELSE " +
	    			 "        CASE " +
	    			 "          WHEN t.ib_status IS NULL " +
	    			 "          THEN " +
	    			 "            CASE " +
	    			 "              WHEN t.raw_status=0 " +
	    			 "              THEN 1 " +
	    			 "              ELSE 0 " +
	    			 "            END " +
	    			 "          ELSE " +
	    			 "            CASE " +
	    			 "              WHEN t.ib_status=1 " +
	    			 "              THEN " +
	    			 "                CASE " +
	    			 "                  WHEN t.raw_status=0 " +
	    			 "                  THEN 1 " +
	    			 "                  ELSE 0 " +
	    			 "                END " +
	    			 "              ELSE 1 " +
	    			 "            END " +
	    			 "        END " +
	    			 "    END MHA_STATUS, " +
	    			 "    t.IB_status, " +
	    			 "    t.raw_status, " +
	    			 "    CASE " +
	    			 "      WHEN b.current_status=8 " +
	    			 "      THEN 0 " +
	    			 "      ELSE NULL " +
	    			 "    END applicant_status " +
	    			 "  FROM v_application_details b " +
	    			 "  LEFT JOIN t " +
	    			 "  ON t.application_id     =b.application_id " +
	    			 "  WHERE ((b.current_stage = 2 " +
	    			 "  AND b.current_status   IN (4, 8)) " +
	    			 "  OR (b.current_stage     = 1 " +
	    			 "  AND b.current_status    =2)) " +
	    			 "  ), " +
	    			 "  t3 AS " +
	    			 "  (SELECT t2.*, " +
	    			 "    a.user_id, " +
	    			 "    (SELECT user_name FROM tm_user um WHERE um.user_id=a.user_id " +
	    			 "    ) user_name, " +
	    			 "    (SELECT designation_name " +
	    			 "    FROM tm_designation c " +
	    			 "    WHERE c.designation_id= " +
	    			 "      (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "      ) " +
	    			 "    ) designation, " +
	    			 "    (SELECT designation_id FROM tm_user b WHERE b.user_id=a.user_id " +
	    			 "    ) designation_id " +
	    			 "  FROM t2 " +
	    			 "  LEFT JOIN T_PC_OFFICE_USER_DETAILS a " +
	    			 "  ON t2.application_id=a.application_id " +
	    			 "  AND a.office_code   = '"+loginOfficeCode+"' " +
	    			 "  ) " +
	    			 "SELECT t2.state, " +
	    			 "  t2.district, " +
	    			 "  TO_CHAR(s_date, 'yyyy') YEAR, " +
	    			 "  TO_CHAR(TRUNC(s_date)) AS s_date, " +
	    			 "  service_desc, " +
	    			 "  t2.application_id, " +
	    			 "  t2.applicant_name, " +
	    			 "  t2.section_fileno, " +
	    			 "  CASE " +
	    			 "    WHEN mha_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS mha, " +
	    			 "  CASE " +
	    			 "    WHEN ib_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS ib, " +
	    			 "  CASE " +
	    			 "    WHEN raw_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS raw1, " +
	    			 "  CASE " +
	    			 "    WHEN applicant_status=0 " +
	    			 "    THEN 'X' " +
	    			 "    ELSE NULL " +
	    			 "  END AS applicant, " +
	    			 "  user_id " +
	    			 "  ||' [' " +
	    			 "  ||user_name " +
	    			 "  ||']' AS userName , " +
	    			 "  designation, " +
	    			 "  (TRUNC(sysdate)-TRUNC(s_date)) pending " +
	    			 "FROM t3 t2, " +
	    			 "  tm_service s " +
	    			 "WHERE t2.service             =s.service_code " +
	    			 "AND "+subQueryYear+" " +
	    			 "AND  "+subQueryServices+" " +
	    			 "ORDER BY YEAR, " +
	    			 "  state, " +
	    			 "  service_desc, " +
	    			 "  s_date" ; 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("REPORT_DATA_SOURCE_DETAILED", ds);
	     
	     }
		String tsPath = "/Reports/PendencyReportCSV.jrxml";
		String fileName = "PendencyReport";
		//GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	
	
		
	}

}
