package com.ypyproductions.voicechanger.object;

import java.util.Date;

import org.json.JSONObject;

public class SongObject {
	private String name;
	private Date date;
	private String path;
	private boolean isPlaying;

	public SongObject(String name, Date date, String path) {
		super();
		this.name = name;
		this.date = date;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public JSONObject toJson() {
		try {
			JSONObject mJsonObject = new JSONObject();
			mJsonObject.put("name", name);
			mJsonObject.put("data", date);
			return mJsonObject;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
