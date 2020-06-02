package utilities.lists;

public class ListTimeline {
	private String title;
	private String description;
	private String subtitle;
	private String date;
	
	public ListTimeline(String title, String description, String subtitle, String date) {
		this.title = title;
		this.description = description;
		this.subtitle = subtitle;
		this.date = date;
	}
	
	public ListTimeline(StringBuffer title, StringBuffer description, StringBuffer subtitle, StringBuffer date) {
		this.title = title.toString();
		this.description = description.toString();
		this.subtitle = subtitle.toString();
		this.date = date.toString();
	}
	public String gettitle() {
		return title;
	}
	public void settitle(String title) {
		this.title = title;
	}
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.description = description;
	}
	public String getsubtitle() {
		return subtitle;
	}
	public void setsubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getdate() {
		return date;
	}
	public void setdate(String date) {
		this.date = date;
	}
}