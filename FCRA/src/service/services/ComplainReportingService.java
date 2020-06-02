package service.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;

import dao.services.ComplainReportingDao;
import dao.services.NotificationDao;
import models.services.requests.ComplainRegisterBean;
import models.services.requests.ParentMenuName;
import utilities.Commons;

public class ComplainReportingService extends Commons {
	private List<ComplainRegisterBean> TrackRequestList;
	private String TrackRequestCount;
	private MultiValueMap<String, String> parameterMap;
	public List<ParentMenuName> populatemenuname(HttpServletRequest request) throws Exception{
		begin();
		try {
			ComplainReportingDao crd=new ComplainReportingDao(connection);
			List<ParentMenuName> menuname = crd.getMenuName(request);	
			return menuname;
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
		return null;		
	}
	public int populatetriform(ComplainRegisterBean complainRegisterBean,HttpServletRequest request) throws Exception{
		begin();
		int cid=0;
		try {
			ComplainReportingDao crd=new ComplainReportingDao(connection);
			 cid = crd.triformSubmit(complainRegisterBean,request);
			return cid;
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
		return cid;		
	}
	public String pullTrackRequest(HttpServletRequest req){
		String ret = "success";
		begin();
		try {
			populateTrackRequestCount(req);
			populateTrackRequestList(req);
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	public int populatetrireplyform(ComplainRegisterBean complainRegisterBean) throws Exception{
		begin();
		int cid=0;
		try {
			ComplainReportingDao crd=new ComplainReportingDao(connection);
			  crd.triformreplySubmit(complainRegisterBean);
			return cid;
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
		return cid;		
	}
	private void populateTrackRequestCount(HttpServletRequest req) throws Exception{
		ComplainReportingDao ndao=new ComplainReportingDao(connection);
		 
		TrackRequestCount=ndao.getTrackRequestCount(req); 
				}
	
	private void populateTrackRequestList(HttpServletRequest req) throws Exception{
		ComplainReportingDao ndao=new ComplainReportingDao(connection);
		
			
			TrackRequestList=ndao.getTrackRequestList(getParameterMap().get("pageNum").get(0), req, getParameterMap().get("recordsPerPage").get(0));
			
	}
	public List<ComplainRegisterBean> getTrackRequestList() {
		return TrackRequestList;
	}
	public void setTrackRequestList(List<ComplainRegisterBean> trackRequestList) {
		TrackRequestList = trackRequestList;
	}
	public String getTrackRequestCount() {
		return TrackRequestCount;
	}
	public void setTrackRequestCount(String trackRequestCount) {
		TrackRequestCount = trackRequestCount;
	}
	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}
	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}
	
	 }
	

