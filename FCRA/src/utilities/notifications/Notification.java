package utilities.notifications;

public class Notification {
	private String h;
	private String d;
	private String s;
	private String t;
	private String c;
	private int ms;
	private String l;
	/*New Notification(String head, String desc,String status, String type, String closeable,String milliseconds, String locationId)*/
	private Notification(String h, String d, String s, String t,String l, String c,
			int ms) throws NotificationException{
		super();
		this.h = h;
		this.d = d;
		this.s = s;
		this.t = t;
		this.c = c;
		this.ms = ms;
		this.l=l;
		if(!t.equalsIgnoreCase("t")){
			//System.out.println("Info: You can use custom locations for notifications of type bar and sticky also, Try other constructors ");
		}
		if(l.equalsIgnoreCase("null")||l.equalsIgnoreCase(null)||l.equals(null)||l.equalsIgnoreCase("")){
			throw new NotificationException("Please provide a valid location(invalid: null,empty string{\"\"},\"null\") for the notification whose description is :"+d);
		}
	}
	public Notification(String h, String d, Status s, Type t) throws NotificationException{
		this(h, d, s.getStatus(), t.getType(),"d", "c", 0);
	}
	public Notification(String h, String d, Status s, Type t,String location) throws NotificationException{
		this(h, d, s.getStatus(), t.getType(),location, "c", 0);
		
	}
	public Notification(String h, String d, Status s, Type t, Closeable c) throws NotificationException{
		this(h, d, s.getStatus(), t.getType(),"d", c.getCloseable(), 0);
	}
	public Notification(String h, String d, Status s, Type t,String location, Closeable c) throws NotificationException{
		this(h, d, s.getStatus(), t.getType(),location, c.getCloseable(), 0);
	}
	public Notification(String h, String d, Status s, Type t, Closeable c, int ms) throws NotificationException{
		this(h, d, s.getStatus(), t.getType(),"d", c.getCloseable(), ms);
	}
	public Notification(String h, String d, Status s, Type t,String location, Closeable c, int ms) throws NotificationException{
		this(h, d, s.getStatus(), t.getType(),location, c.getCloseable(), ms);
	}
	public String getH() {
		return h;
	}
	public void setH(String h) {
		this.h = h;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public int getMs() {
		return ms;
	}
	public void setMs(int ms) {
		this.ms = ms;
	}
}
