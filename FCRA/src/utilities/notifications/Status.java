package utilities.notifications;

public enum Status {
	INFORMATION("i"), ERROR("e"), SUCCESS("s"), WARNING("w");
	private String value;
    private Status(String value) {
    	this.value = value;
	}
    public String getStatus(){
    	return value;
    }
}
