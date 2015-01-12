
import org.json.*;
public class Entry {
	private String title;
	private String description;
	private String link;
	private String cover;
	private String time;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public Entry(String title, String description, String link, String cover,String time) {
		super();
		this.title = title;
		this.description = description;
		this.link = link;
		this.cover = cover;
		this.time=time;
	}
	public Entry() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		JSONObject object = new JSONObject();
		try {
			object.put("title", this.title);
			object.put("description", this.description);
			object.put("cover", this.cover);
			object.put("link", this.link);
			object.put("time", this.time);
			object.put("categoryId",1);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object.toString();
	}
	
	
	

}
