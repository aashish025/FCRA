package utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.activation.DataSource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.output.ByteArrayOutputStream;


import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizationHelper;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class GeneratePdfVirtualizer {	   
    private static String exportReportToPDF(String jrxmlPath, Map parameters,Connection connection, String fileName,String embedCode,boolean print)
        throws JRException
        {
    		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletResponse response = attr.getResponse();  
            ServletContext context=attr.getRequest().getSession().getServletContext();
            
            ServletOutputStream servletOutputStream=null;
            JRSwapFileVirtualizer virtualizer =null;
            try
            {              	
               
               servletOutputStream = response.getOutputStream();        
               virtualizer = new JRSwapFileVirtualizer(100, new JRSwapFile((context.getRealPath("/Reports/ReportTemp")), 1024, 1024) , true);
               JRVirtualizationHelper.setThreadVirtualizer(virtualizer);
               virtualizer.setReadOnly(false);
               parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
               
               JasperDesign jd = JRXmlLoader.load(context.getRealPath(jrxmlPath));
               JasperReport reportCompiled = JasperCompileManager.compileReport(jd);
                 
               
               
               JasperPrint jasperPrint = JasperFillManager.fillReport(reportCompiled,parameters,   new JREmptyDataSource());
                
                JRPdfExporter exporter = new JRPdfExporter();       
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                if(print==true){
                    exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT,"this.print({bUI: true,bSilent: false,bShrinkToFit: true});");
                }
                exporter.exportReport();
                response.setContentType("application/pdf");
                response.setContentLength(baos.size());
                response.setHeader("Content-Disposition", embedCode+";filename=\"" + fileName + ".pdf\"");
                if(baos.size()>2097152){
                    ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
                    byte[] buf = new byte[20];
                    for(int br = in.read(buf); br > -1; br = in.read(buf))
                    {
                        servletOutputStream.write(buf, 0 , br);
                        servletOutputStream.flush();
                    }
                }
                else{
                    baos.writeTo(servletOutputStream);
                }
                servletOutputStream.flush();
                servletOutputStream.close();
                baos.close();
                virtualizer.cleanup();
                connection.close();
                
            }
            catch(JRException jre)
            {
                System.out.println("JR Exception");
                System.out.println(jre.getMessage());
                jre.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            catch(Throwable e)
            {
                e.printStackTrace();
            }
            finally
            {
            	if (virtualizer != null) virtualizer.cleanup();   
            }
            return "success";
        }
    private static String exportReportToPDFWithDB(String jrxmlPath, Map parameters,Connection connection, String fileName,String embedCode,boolean print)
            throws JRException
            {
        		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletResponse response = attr.getResponse();  
                ServletContext context=attr.getRequest().getSession().getServletContext();
                
                ServletOutputStream servletOutputStream=null;          
                try
                {              	
                   
                   servletOutputStream = response.getOutputStream();        
                   JRSwapFileVirtualizer virtualizer = new JRSwapFileVirtualizer(100, new JRSwapFile((context.getRealPath("/Reports/ReportTemp")), 1024, 1024) , true);
                   JRVirtualizationHelper.setThreadVirtualizer(virtualizer);
                   virtualizer.setReadOnly(false);
                   parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
                   
                   JasperDesign jd = JRXmlLoader.load(context.getRealPath(jrxmlPath));
                   JasperReport reportCompiled = JasperCompileManager.compileReport(jd);
                     
                   
                   
                   JasperPrint jasperPrint = JasperFillManager.fillReport(reportCompiled,parameters,  connection);
                    
                    JRPdfExporter exporter = new JRPdfExporter();       
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                    if(print==true){
                        exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT,"this.print({bUI: true,bSilent: false,bShrinkToFit: true});");
                    }
                    exporter.exportReport();
                    response.setContentType("application/pdf");
                    response.setContentLength(baos.size());
                    response.setHeader("Content-Disposition", embedCode+";filename=\"" + fileName + ".pdf\"");
                    if(baos.size()>2097152){
                        ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
                        byte[] buf = new byte[20];
                        for(int br = in.read(buf); br > -1; br = in.read(buf))
                        {
                            servletOutputStream.write(buf, 0 , br);
                            servletOutputStream.flush();
                        }
                    }
                    else{
                        baos.writeTo(servletOutputStream);
                    }
                    servletOutputStream.flush();
                    servletOutputStream.close();
                    baos.close();
                    virtualizer.cleanup();
                    connection.close();
                    
                }
                catch(JRException jre)
                {
                    System.out.println("JR Exception");
                    System.out.println(jre.getMessage());
                    jre.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                catch(Throwable e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    
                }
                return "success";
            }
    private static byte[] exportReportToBytes(String jrxmlPath, Map parameters,Connection connection, String fileName,String embedCode,boolean print)
            throws JRException
            {
    			ByteArrayOutputStream baos=null;
        		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletResponse response = attr.getResponse();  
                ServletContext context=attr.getRequest().getSession().getServletContext();
                
                ServletOutputStream servletOutputStream=null;          
                try
                {              	
                   
                   servletOutputStream = response.getOutputStream();        
                   JRSwapFileVirtualizer virtualizer = new JRSwapFileVirtualizer(100, new JRSwapFile((context.getRealPath("/Reports/ReportTemp")), 1024, 1024) , true);
                   JRVirtualizationHelper.setThreadVirtualizer(virtualizer);
                   virtualizer.setReadOnly(false);
                   parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
                   
                   JasperDesign jd = JRXmlLoader.load(context.getRealPath(jrxmlPath));
                   JasperReport reportCompiled = JasperCompileManager.compileReport(jd);                    
                   
                   
                   JasperPrint jasperPrint = JasperFillManager.fillReport(reportCompiled,parameters,  new JREmptyDataSource());
                    
                    JRPdfExporter exporter = new JRPdfExporter();       
                    baos = new ByteArrayOutputStream();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                    if(print==true){
                        exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT,"this.print({bUI: true,bSilent: false,bShrinkToFit: true});");
                    }
                    exporter.exportReport();
                   /* response.setContentType("application/pdf");
                    response.setContentLength(baos.size());
                    response.setHeader("Content-Disposition", embedCode+";filename=\"" + fileName + ".pdf\"");
                    if(baos.size()>2097152){
                        ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
                        byte[] buf = new byte[20];
                        for(int br = in.read(buf); br > -1; br = in.read(buf))
                        {
                            servletOutputStream.write(buf, 0 , br);
                            servletOutputStream.flush();
                        }
                    }
                    else{
                        baos.writeTo(servletOutputStream);
                    }
                    servletOutputStream.flush();
                    servletOutputStream.close();*/
                    baos.close();
                    virtualizer.cleanup();            
                    
                }
                catch(JRException jre)
                {
                    System.out.println("JR Exception");
                    System.out.println(jre.getMessage());
                    jre.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                catch(Throwable e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    
                }
                return baos.toByteArray();
            }
    
    public  static String asInline(String jrxmlPath, Map parameters,Connection connection, String fileName)
    throws JRException
    {
        exportReportToPDF(jrxmlPath,parameters,connection,fileName,"inline",false);
        return "success";
        
    }
    public  static String asInlineWithDB(String jrxmlPath, Map parameters,Connection connection, String fileName)
    	    throws JRException
    	    {
    	        exportReportToPDFWithDB(jrxmlPath,parameters,connection,fileName,"inline",false);
    	        return "success";
    	        
    	    }
    public static String asInlinePrint(String jrxmlPath, Map parameters,Connection connection, String fileName)
    throws JRException
    {
        exportReportToPDF(jrxmlPath,parameters,connection,fileName,"inline",true);
        return "success";
        
    }
    public static byte[] asAttachmentBytes(String jrxmlPath, Map parameters,Connection connection,String fileName)
    throws JRException
    {
        byte[] pdfBytes=exportReportToBytes(jrxmlPath,parameters,connection,fileName,"attachment",false);
        return pdfBytes;
        
    }
    public static String asAttachment(String jrxmlPath, Map parameters,Connection connection,String fileName)
    	    throws JRException
    	    {
    	        exportReportToPDF(jrxmlPath,parameters,connection,fileName,"attachment",false);
    	        return "success";
    	        
    	    }
    public static String asAttachmentWithDB(String jrxmlPath, Map parameters,Connection connection,String fileName)
    	    throws JRException
    	    {
    	        exportReportToPDFWithDB(jrxmlPath,parameters,connection,fileName,"attachment",false);
    	        return "success";
    	        
    	    }
    public static  String asAttachmentPrint(String jrxmlPath, Map parameters,Connection connection, String fileName)
    throws JRException
    {
        exportReportToPDF(jrxmlPath,parameters,connection,fileName,"attachment",true);
        return "success";
        
    }
    public static String asDefaultPdf(String jrxmlPath, Map parameters,Connection connection, String fileName)
    throws JRException
    {
        exportReportToPDF(jrxmlPath,parameters,connection,fileName,"attachment",false);
        return "success";
        
    }

}