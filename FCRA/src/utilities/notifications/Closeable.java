package utilities.notifications;

public enum Closeable {
	TRUE("c"), FALSE("uc");
	private String value;
    private Closeable(String value) {
    	this.value = value;
	}
    public String getCloseable(){
    	return value;
    }
}
