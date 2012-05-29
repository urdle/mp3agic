package com.mpatric.mp3agic;

import java.util.ArrayList;
import java.util.List;

public class APEv2FrameSet {

	private String id;
	private ArrayList<APEv2Frame> frames;
	
	public APEv2FrameSet(String id) {
		this.id = id;
		frames = new ArrayList<APEv2Frame>();
	}

	public String getId() {
		return id;
	}

	public void clear() {
		frames.clear();		
	}
	
	public void addFrame(APEv2Frame frame) {
		frames.add(frame);
	}

	public List<APEv2Frame> getFrames() {
		return frames;
	}
	
	@Override
	public String toString() {
		return this.id + ": " + frames.size();
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof APEv2FrameSet)) return false;
		if (super.equals(obj)) return true;
		APEv2FrameSet other = (APEv2FrameSet) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (other.id == null) return false;
		else if (! id.equals(other.id)) return false;
		if (frames == null) {
			if (other.frames != null) return false;
		} else if (other.frames == null) return false;
		else if (! frames.equals(other.frames)) return false;
		return true;
	}

}
