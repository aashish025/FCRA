package dao.services.downloaders;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;

public abstract class Downloader {
	protected Connection connection = null;
	protected OutputStream outputStream = null;
	protected DocumentType documentType=null;
	
	
	public Downloader(Connection connection, OutputStream outputStream) {
		this.connection = connection;
		this.outputStream = outputStream;
	}
	public Downloader(Connection connection) {
		this.connection = connection;		
	}
	
	public abstract void getFile() throws Exception;
	
	protected void copyData(InputStream inputStream) throws Exception {
		if(outputStream == null) {
			throw new Exception("Invalid Output Stream");
		}
		if(inputStream == null) {
			throw new Exception("Invalid Input Stream");
		}
			
		byte[] buf = new byte[20];
		for(int br = inputStream.read(buf);br > -1;br=inputStream.read(buf)){
			outputStream.write(buf, 0 , br);
		}
	}
	

	

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
}
