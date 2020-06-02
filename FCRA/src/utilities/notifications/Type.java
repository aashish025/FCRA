package utilities.notifications;

public enum Type {
	BAR("b"),STICKY("s"), TEXT("t"), NONE("n");
	private String value;
    private Type(String value) {
    	this.value = value;
	}
    public String getType(){
    	return value;
    }
}

