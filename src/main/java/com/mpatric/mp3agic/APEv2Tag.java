package com.mpatric.mp3agic;

import java.util.Map;
import java.util.TreeMap;

public class APEv2Tag implements APEv2 {

	private final Map<String, APEv2FrameSet> frameSets;

	public APEv2Tag() {
		frameSets = new TreeMap<String, APEv2FrameSet>();
	}

	public APEv2Tag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		frameSets = new TreeMap<String, APEv2FrameSet>();
		unpackTag(bytes);
	}

	private void unpackTag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
/*		ID3v2TagFactory.sanityCheckTag(bytes);
		int offset = unpackHeader(bytes);
		try {
			if (extendedHeader) {
				offset = unpackExtendedHeader(bytes, offset);
			}
			int framesLength = dataLength;
			if (footer) framesLength -= 10;
			offset = unpackFrames(bytes, offset, framesLength);
			if (footer) {
				offset = unpackFooter(bytes, dataLength);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new InvalidDataException("Premature end of tag", e);
		}
*/	}


	@Override
	public Map<String, ID3v2FrameSet> getFrameSets() {
		// TODO Auto-generated method stub
		return null;
	}

}
