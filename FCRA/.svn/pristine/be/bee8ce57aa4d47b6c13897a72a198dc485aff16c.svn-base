
package dao;

import java.sql.Connection;
import java.util.List;

import utilities.KVPair;

/* C - Type of Model Class
 * K - Type of Key for KV Pair
 * V - Type of Value for KV Pair
 */
public abstract class BaseDao<C, K, V> {
	protected Connection connection = null;
	
	/* Advisable not to commit anything inside this function */
	public abstract Integer insertRecord(C object) throws Exception;
	
	/* Advisable not to commit anything inside this function */
	public abstract Integer removeRecord(C object) throws Exception;
	
	public abstract List<C> getAll() throws Exception;
	
	public abstract List<KVPair<K, V>> getKVList(List<C> list);
	
	public BaseDao(Connection connection) {
		this.connection = connection;
	}
	
	public final void save(C object) throws Exception
	{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		try {
			insertRecord(object);
			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}
	}
	
	public final void delete(C object) throws Exception
	{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
	
		try {
			removeRecord(object);
			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}
	}
}