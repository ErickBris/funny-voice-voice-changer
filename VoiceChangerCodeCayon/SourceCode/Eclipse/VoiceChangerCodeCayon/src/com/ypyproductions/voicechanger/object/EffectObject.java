package com.ypyproductions.voicechanger.object;

public class EffectObject {

	private String id;
	private String name;

	private boolean isPlaying;
	private int pitch;
	private float rate;
	private float[] reverb;
	private float[] eq;
	private boolean isFlanger;
	private boolean isReverse;
	private boolean isEcho;

	public EffectObject(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public EffectObject(String id, String name, int pitch, float rate) {
		super();
		this.id = id;
		this.name = name;
		this.pitch = pitch;
		this.rate = rate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float[] getReverb() {
		return reverb;
	}

	public void setReverb(float[] reverb) {
		this.reverb = reverb;
	}

	public boolean isFlanger() {
		return isFlanger;
	}

	public void setFlanger(boolean isFlanger) {
		this.isFlanger = isFlanger;
	}

	public boolean isReverse() {
		return isReverse;
	}

	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

	public boolean isEcho() {
		return isEcho;
	}

	public void setEcho(boolean isEcho) {
		this.isEcho = isEcho;
	}

	public float[] getEq() {
		return eq;
	}

	public void setEq(float[] eq) {
		this.eq = eq;
	}
	
}
