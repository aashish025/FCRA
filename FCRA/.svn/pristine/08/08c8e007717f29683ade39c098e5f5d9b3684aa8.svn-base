package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.master.AssociationType;
import utilities.KVPair;
import utilities.lists.List2;
import dao.BaseDao;

public class AssociationTypeDao extends BaseDao<AssociationType, String, String> {

	public AssociationTypeDao(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public List<List2> getAssociationTypeList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("Select TYPE_ID,TYPE_NAME from Tm_Association_Type where Record_Status=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<List2>  assoList = new ArrayList<List2>();
		while(rs.next()) {			
			assoList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return assoList;
	} 
	@Override
	public Integer insertRecord(AssociationType object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(AssociationType object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssociationType> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<AssociationType> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
