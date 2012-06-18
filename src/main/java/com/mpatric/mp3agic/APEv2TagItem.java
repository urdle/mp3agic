package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/*
// ================================================================================
// = 4 - APE Tag Item Format
// ================================================================================
//
// APE Tag Items are stored as key-value pairs.  APE Tags Item Key are case
// sensitive, however it is illegal to use keys which only differ in case and
// it is recommended that tag reading not be case sensitive.
//
// Every key can only occur (at most) once. It is not possible to repeat a key
// to signify updated contents.
//
// Tags can be partially or completely repeated in the streaming format.  This
// makes it possible to display an artist and / or title if it was missed at the
// beginning of the stream.  It is recommended that the important information like
// artist, album and title should occur approximately every 2 minutes in the
// stream and again 5 to 10 seconds before the end.  However, care should be tak
// en not to replicate this information too often or during passages with high
// bitrate demands to avoid unnecessary drop-outs.
//
// /==============================================================================\
// | Content Size   | 4 bytes       | Length of the value in bytes                |
// |----------------|---------------|---------------------------------------------|
// | Flags          | 4 bytes       | Item flags (same as the header)             |
// |----------------|---------------|---------------------------------------------|
// | Key            | 2 - 255 bytes | Item key                                    |
// |----------------|---------------|---------------------------------------------|
// | Key Terminator | 1 byte        | Null byte that indicates the end of the key |
// |----------------|---------------|---------------------------------------------|
// | Value          | variable      | Content (formatted according to the flags)  |
// \==============================================================================/
//
 */

// http://wiki.hydrogenaudio.org/index.php?title=APE_Tag_Item

public class APEv2TagItem {

	private static final int HEADER_LENGTH = 10;
	private static final int ID_OFFSET = 8;
	protected static final int DATA_LENGTH_OFFSET = 0;

	protected String id;
	protected int dataLength = 0;
	protected byte[] data = null;
	APEv2TagFlag flags;

	public APEv2TagItem(byte[] buffer, int offset) throws InvalidDataException {
		unpackFrame(buffer, offset);
	}

	public APEv2TagItem(String id, byte[] data) {
		this.id = id;
		this.data = data;
		dataLength = data.length;
	}

	protected int unpackFrame(byte[] buffer, int offset) throws InvalidDataException {
		ByteBuffer b = ByteBuffer.wrap(buffer, offset, 4);
		b.order(ByteOrder.LITTLE_ENDIAN);

		dataLength = b.getInt();
		flags=new APEv2TagFlag(buffer, offset+4);
		int idOffset=ID_OFFSET+offset;
		
		int strEndOffset=idOffset;
		while (buffer[strEndOffset] != 0x00) {
			strEndOffset++;
		}
		
		id=new String(buffer,idOffset,strEndOffset-idOffset);
		data=new byte[dataLength];
		System.arraycopy(buffer, strEndOffset+1, data, 0, dataLength);
		return dataLength+ID_OFFSET+id.length()+1;
	}

	public byte[] toBytes() throws NotSupportedException {
		byte[] bytes = new byte[getLength()];
		packFrame(bytes, 0);
		return bytes;
	}
	
	public void toBytes(byte[] bytes, int offset) throws NotSupportedException {
		packFrame(bytes, offset);
	}
	
	public void packFrame(byte[] bytes, int offset) throws NotSupportedException {
		//packHeader(bytes, offset);
		// TODO : Make it for real
		BufferTools.copyIntoByteBuffer(data, 0, data.length, bytes, offset + HEADER_LENGTH);
	}

	public String getId() {
		return id;
	}

	public int getDataLength() {
		return dataLength;
	}

	public int getLength() {
		return dataLength + ID_OFFSET + id.length() + 1;
	}

	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
		if (data == null) dataLength = 0;
		else dataLength = data.length;
	}

	public String getCharacterSet() {
		// TODO : Should depend according to «encoding»
		return "UTF-8";
	}

	public String getDataString() {
		try {
			return new String(data,getCharacterSet());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public boolean isReadOnly() {
		return flags.isReadOnly();
	}

	public boolean equals(Object obj) {
		if (! (obj instanceof APEv2TagItem)) return false;
		if (super.equals(obj)) return true;
		APEv2TagItem other = (APEv2TagItem) obj;
		if (dataLength != other.dataLength) return false;
		if (flags != other.flags) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (other.id == null) return false;
		else if (! id.equals(other.id)) return false;
		if (data == null) {
			if (other.data != null) return false;
		} else if (other.data == null) return false;
		else if (! Arrays.equals(data, other.data)) return false; 
		return true;
	}

	@Override
	public String toString() {
		return "APEv2TagItem [id=" + id + ", dataLength=" + dataLength
				+ ", data=" +  getDataString().split("\n")[0]
				+ ", flags=" + flags + "]\n";
	}
	
	
}
