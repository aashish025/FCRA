package dao.services.downloaders;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.core.io.FileSystemResource;

public class PropertyDocumentsDownloader extends Downloader {
	private String propertyId;
	
	public PropertyDocumentsDownloader(Connection connection, OutputStream outputStream) {
		super(connection, outputStream);
	}
	
	public void getFile() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(documentType == null) {
			throw new Exception("Invalid document type");
		}
		if(propertyId == null) {
			throw new Exception("Invalid property id");
		}
		
		getPropertyDocument();
	}
	private void getPropertyDocument() throws Exception {
		InputStream inputStream = null;
		try {
			String query = getQuery();
			PreparedStatement statement = connection.prepareStatement(query.toString());
			statement.setString(1, propertyId);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				inputStream = rs.getBinaryStream(1);
				copyData(inputStream);
			}
		}catch (Exception e) {
			throw e;
		}finally {
			if(inputStream != null)
				inputStream.close();
			if(outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}
	
	private String getQuery() throws Exception {
		StringBuffer query = null;
		switch (documentType) {
		case LEASE_DEED:
			query = new StringBuffer("SELECT LEASE_DEED FROM T_LEASE_DETAILS WHERE PROPERTY_ID=? AND RECORD_STATUS=0");
			break;
		case PURCHASE_DEED:
			query = new StringBuffer("SELECT PURCHASE_DEED FROM T_PURCHASE_DETAILS WHERE PROPERTY_ID=? AND RECORD_STATUS=0");
			break;
		case LAND_AREA_MAP:
			query = new StringBuffer("SELECT AREA_MAP FROM T_LAND_DETAILS WHERE PROPERTY_ID=? AND RECORD_STATUS=0");
			break;
		case SURVEY_SITE_PLAN:
			query = new StringBuffer("SELECT SURVEY_SITE_PLAN FROM T_LAND_DETAILS WHERE PROPERTY_ID=? AND RECORD_STATUS=0");
			break;
		case PROPERTY_IMAGE:
			query = new StringBuffer("SELECT PROPERTY_IMAGE FROM T_PROPERTY_DETAILS WHERE PROPERTY_ID=? AND RECORD_STATUS=0");
			break;
		}
		return query.toString();
	}
	
	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

}
