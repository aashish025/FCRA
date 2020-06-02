package utilities;

public class InformationException extends Exception {
	public InformationException() { 
		super(); 
	}
	public InformationException(String message) { 
		super(message); 
	}
	public InformationException(String message, Throwable cause) { 
		super(message, cause); 
	}
	public InformationException(Throwable cause) { 
		super(cause); 
	}
}
