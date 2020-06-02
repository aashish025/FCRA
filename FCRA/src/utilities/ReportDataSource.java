package utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ReportDataSource implements JRDataSource
 
{
	private ResultSet rsReportData;
		
	public ReportDataSource(Map parameters ,String query, Connection connection, int maxSizeP ) throws SQLException
	{ 
		System.out.println("ReportQ: "+query);
		connection.setAutoCommit( false );
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setFetchSize( maxSizeP );
		this.rsReportData = statement.executeQuery();
	}
	public Object getFieldValue( JRField jrField ) throws JRException
	{
		try
		{
		Object field = rsReportData.getObject( jrField.getName() );
			return field;
		}
		catch( SQLException e )
		{
			throw new JRException( e );
		}
	}
	private int iNext = 0;
	public boolean next() throws JRException
	{	
		try
		{
			return rsReportData.next();
			
		}
		catch( SQLException e )
		{
			throw new JRException( e );
		}
	}

}