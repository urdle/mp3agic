package com.mpatric.mp3agic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.TreeMap;

public class APEv2Tag implements APEv2 {

	private final Map<String, APEv2TagItem> frameSets;
	//private APEv2Encloser header = null;
	//private APEv2Encloser footer = null;
	private APEv2Encloser encloser = null;
	boolean isAtTheEnd;
	
	public APEv2Encloser getEncloser() {
		return encloser;
	}

	public void setEncloser(APEv2Encloser encloser) {
		this.encloser = encloser;
	}

	public boolean isAtTheEnd() {
		return isAtTheEnd;
	}

	public void setAtTheEnd(boolean isAtTheEnd) {
		this.isAtTheEnd = isAtTheEnd;
	}

	public APEv2Tag() {
		frameSets = new TreeMap<String, APEv2TagItem>();
	}

	/*public APEv2Tag(byte[] bytes) throws NoSuchTagException,
			UnsupportedTagException, InvalidDataException {
		frameSets = new TreeMap<String, APEv2TagItem>();
		unpackTag(bytes);
	}*/

	/*
	 * public APEv2Tag(RandomAccessFile file) throws NoSuchTagException,
	 * IOException, UnsupportedTagException, InvalidDataException { frameSets =
	 * new TreeMap<String, APEv2FrameSet>(); try {
	 * header=isAPEv2HeaderStart(file,0); } isAPEv2HeaderStart(file,0);
	 * isAPEv2HeaderStart(file,id3v2Tag.getLength());
	 * isAPEv2FooterEnd(file,(int)file.length()-ID3v1Tag.TAG_LENGTH);
	 * isAPEv2FooterEnd(file,(int)file.length());
	 * 
	 * }
	 */
	public APEv2Tag(RandomAccessFile file, int id3v2EndOffset,
			int id3v1StartOffset) throws NoSuchTagException, IOException,
			UnsupportedTagException, InvalidDataException {
		frameSets = new TreeMap<String, APEv2TagItem>();
		findEnclosers(file, id3v2EndOffset, id3v1StartOffset);

	}

	public void findEnclosers(RandomAccessFile file, int id3v2EndOffset,
			int id3v1StartOffset) throws NoSuchTagException, IOException,
			UnsupportedTagException, InvalidDataException {
		long firstFrameOffset=0;
		
		try {
			encloser = new APEv2Encloser(file, id3v2EndOffset, false);
			isAtTheEnd=false;
			firstFrameOffset = id3v2EndOffset + APEv2Encloser.ENCLOSURE_SIZE ;
		} catch (InvalidDataException e1) {
			// System.out.println("ape not ahead : " + e.getMessage());
			try {
				encloser = new APEv2Encloser(file, id3v1StartOffset, true);
				isAtTheEnd=true;
				firstFrameOffset = id3v1StartOffset - encloser.tagSize;
			} catch (InvalidDataException e2) {
				// System.out.println("ape not at the end : " + e.getMessage());
				encloser = null;
			}
		}
		if (encloser == null) {
			throw new NoSuchTagException("No APEv2 Header/Footer found.");
		}
		
/*		if (encloser.isHeader) {
			header = encloser;
			if (header.hasFooter) {
				try {
					footer = new APEv2Encloser(file, id3v2EndOffset
							+ encloser.tagSize, false);
				} catch (InvalidDataException e) {
					// System.out.println("ape not ahead : " + e.getMessage());
					footer = null;
					header.hasFooter = false;
					System.err.println("footer announced but not found. Please fix that tag :" + e.getMessage());
				}
			}
		} else { //encloser.isHeader false
			footer = encloser;
			if (footer.hasHeader) {
				try {
					header = new APEv2Encloser(file, id3v1StartOffset
							- encloser.tagSize, true);
				} catch (InvalidDataException e) {
					// System.out.println("ape not ahead : " + e.getMessage());
					header = null;
					footer.hasHeader = false;
					System.err.println("header announced but not found. Please fix that tag :" + e.getMessage());
				}
			}
			
		}
*/		
		ReadItemSet(file, firstFrameOffset, encloser.itemCount, encloser.tagSize);
	}

	private void ReadItemSet(RandomAccessFile file, long firstFrameOffset,
			int itemCount, int tagSize) throws IOException, InvalidDataException {
		// TODO Auto-generated method stub
		file.seek(firstFrameOffset);
		frameSets.clear();
		byte[] bytes = new byte[tagSize];

		int bytesRead = file.read(bytes, 0, tagSize);
		if (bytesRead < tagSize)
			throw new IOException("Not enough bytes read");

		int itemOffset=0;
		for (int i=0; i<itemCount;i++) {
			APEv2TagItem newItem1=new APEv2TagItem(bytes,itemOffset);
			itemOffset+=newItem1.getLength();
			frameSets.put(newItem1.getId(), newItem1);
			System.out.println(newItem1.toString());
		}
	}

	public byte[] toBytes() throws NotSupportedException {
		byte[] bytes = new byte[getLength()];
		packTag(bytes, 0);
		return bytes;
	}

	private int getLength() {
		if (encloser.flags.hasHeader() && encloser.flags.hasFooter()) {
			return encloser.tagSize + APEv2Encloser.ENCLOSURE_SIZE;
		}
		return encloser.tagSize;
	}

	public void toBytes(byte[] bytes, int offset) throws NotSupportedException {
		packTag(bytes, offset);
	}

	
private void packTag(byte[] bytes, int offset) {
		// TODO Auto-generated method stub
		
	}

	/*	private void unpackTag(byte[] bytes) throws NoSuchTagException,
			UnsupportedTagException, InvalidDataException {
		// TODO Auto-generated method stub
	}
*/
	@Override
	public Map<String, APEv2TagItem> getFrameSets() {
		// TODO Auto-generated method stub
		return frameSets;
	}

}
