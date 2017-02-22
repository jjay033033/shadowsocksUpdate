package priv.lmoon.shadowsupdate.vo;

public class ServerConfigVo {
	
	private String id;
	
	private String url;
	
	private String begin;
	
	private String end;
	
	private int type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ServerConfigVo [id=" + id + ", url=" + url + ", begin=" + begin + ", end=" + end + ", type=" + type
				+ "]";
	}

	
}