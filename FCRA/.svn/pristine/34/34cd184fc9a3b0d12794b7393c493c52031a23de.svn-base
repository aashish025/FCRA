package models.services;

import java.util.List;

public class ListPager<T> {
	private String totalRecords;
	private List<T> list;
	
	public ListPager() {
		
	}
	
	public ListPager(List<T> list) {
		this.list = list;
	}

	public ListPager(List<T> list, String totalRecords) {
		this.list = list;
		this.totalRecords = totalRecords ;
	}
	
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	} 
}
