package utilities.lists;

public class List2{
	private String li;
	private String ld;
	public List2(StringBuffer li, StringBuffer ld) {
		this.li = li.toString();
		this.ld = ld.toString();
	}
	public List2(String li, String ld) {
		this.li = li;
		this.ld = ld;
	}
	public String getLi() {
		return li;
	}
	public void setLi(String li) {
		this.li = li;
	}
	public String getLd() {
		return ld;
	}
	public void setLd(String ld) {
		this.ld = ld;
	}
}

