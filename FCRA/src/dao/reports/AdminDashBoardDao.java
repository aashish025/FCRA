package dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utilities.lists.List2;


import dao.BaseDao;


public class AdminDashBoardDao extends BaseDao{
	private List<List2> returnList = new ArrayList<List2>();
	
	public AdminDashBoardDao(Connection connection) {
		super(connection);
	}
	
	public void getPendingDetails() throws Exception{		
		
		PreparedStatement statement=null;	
		StringBuffer query =null;
		ResultSet rs=null;
		query = new StringBuffer("with t as("
				+ "select 1 as pending_type,count(1) from t_mail_details where status_id=0"
				+ " union all "
				+ "select 2 as pending_type,count(1) from t_registration_sharing where record_status=0 "
				+ " union all "
				+ "select 3 as pending_type,count(1)  from t_application_status_notifn where record_status=0"
				+ ")"
				+ "select * from t ");
		statement = connection.prepareStatement(query.toString());	
		rs=statement.executeQuery();
		while(rs.next()){
			
			//System.out.println(rs.getString(1)+","+rs.getString(2));
			//System.out.println(new List2(rs.getString(1),rs.getString(2)));
			returnList.add(new List2(rs.getString(1),rs.getString(2)));	
		}
	}
	public List<List2> getReturnList() {
		return returnList;
	}
	public void setReturnList(List<List2> returnList) {
		this.returnList = returnList;
	}

	@Override
	public Integer insertRecord(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getKVList(List list) {
		// TODO Auto-generated method stub
		return null;
	}
}
