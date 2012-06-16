package com.mpatric.mp3agic;

/* * Note Urdle : Order is LITTLE-ENDIAN, so bit are ordered 7-0,15-8,23-16,31-24
 * ================================================================================
 *  APE Tag Flags Version 2.0
 *  Source : http://wiki.hydrogenaudio.org/index.php?title=Ape_Tags_Flags
 * ===============================================================================
 * 
 * Contains attribute of the tag (bit 31...) and of a item (bit 0...)
 *
 * Member of APE Tags Header, Footer or Tag item
 * 
 * Note: APE Tags 1.0 do not use any of the APE Tag flags. All are set to zero on creation and ignored on reading.
 * 
 * |Bit 31   || 0: Tag contains no header
 *              1: Tag contains a header
 * |-            
 * |Bit 30    || 0: Tag contains a footer
 *               1: Tag contains no footer
 * |-
 * |Bit 29    || 0: This is the footer, not the header
 *               1: This is the header, not the footer
 * |-
 * |Bit 28...3|| Undefined, must be zero
 * |-
 * |Bit 2...1 || 0: Item contains text information coded in UTF-8
 *               1: Item contains binary information*
 *               2: Item is a locator of external stored information**
 *               3: reserved
 * |-
 * |Bit 0     || 0: Tag or Item is Read/Write 
 *               1: Tag or Item is Read Only
 * |-
 * ================================================================================
 * = 3 - APE Tag Flags
 * Extract from http://audacity.googlecode.com/svn/audacity-src/trunk/lib-src/taglib/taglib/ape/ape-tag-format.txt
 * ================================================================================
 * 
 * The general flag structure for either items or headers / footers is the same.
 * Bits 31, 30 and 29 are specific to headers / footers, whereas 2 through 0 are
 * item specific.
 * 
 *  Note: APE Tags from Version 1.0 do not use any of the following.  All flags in
 * that version are zeroed and ignored when reading.
 * 
 * /=================================================================\
 * | Contains Header | Bit 31      | 1 - has header | 0 - no header  |
 * |-----------------|-------------|---------------------------------|
 * | Contains Footer | Bit 30      | 1 - NO footer | 0 - HAS footer  |
 * |-----------------|-------------|---------------------------------|
 * | Is Header       | Bit 29      | 1 - is header  | 0 - is footer  |
 * |-----------------|-------------|---------------------------------|
 * | Undefined       | Bits 28 - 3 | Undefined, must be zeroed       |
 * |-----------------|-------------|---------------------------------|
 * | Encoding        | Bits 2 - 1  | 00 - UTF-8                      |
 * |                 |             | 01 - Binary Data *              |
 * |                 |             | 10 - External Reference **      |
 * |                 |             | 11 - Reserved                   |
 * |-----------------|-------------|---------------------------------|
 * | Read Only       | Bit 0       | 1 - read only  | 0 - read/write |
 * \=================================================================/
 * 
 */

public class APEv2TagFlag {

	public static final byte STRING_DATA = 0x00;
	public static final byte BINARY_DATA = 0x02;
	public static final byte LINK_DATA = 0x04;
	public static final byte RESERVED_DATA = 0x06;
	public static final byte DATATYPE_MASK = 0x06;

	boolean hasHeader;
	boolean hasFooter;
	boolean isHeader;
	boolean readOnly;
	byte dataType;

	public APEv2TagFlag(byte[] buffer, int offset) /* throws InvalidDataException */{
		unpackFlags(buffer[offset + 3], buffer[offset + 0]);
	}

	protected void unpackFlags(byte b1, byte b4) /*throws InvalidDataException*/ {
		hasHeader = ((b1 & 0x80) != 0);
		hasFooter = ((b1 & 0x40) == 0);
		isHeader = ((b1 & 0x20) != 0);

		dataType = (byte) (b4 & 0x06);
		readOnly = ((b4 & 0x01) != 0);
	}

	public byte[] toBytes() throws NotSupportedException {
		byte[] bytes = new byte[getLength()];
		packFlags(bytes, 0);
		return bytes;
	}

	public void toBytes(byte[] bytes, int offset) throws NotSupportedException {
		packFlags(bytes, offset);
	}

	public void packFlags(byte[] bytes, int offset)
			throws NotSupportedException {
		byte b1 = 0;
		if (hasHeader) {
			b1 |= 0x80;
		}
		if (!hasFooter) {
			b1 |= 0x40;
		}
		if (isHeader) {
			b1 |= 0x20;
		}
		byte b4 = dataType;
		if (readOnly) {
			b4 |= 0x01;
		}

		bytes[offset + 3] = b1;
		bytes[offset + 2] = 0;
		bytes[offset + 1] = 0;
		bytes[offset + 0] = b4;
	}

	public int getLength() {
		return 4;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof APEv2TagFlag))
			return false;
		if (super.equals(obj))
			return true;
		APEv2TagFlag other = (APEv2TagFlag) obj;
		if (hasHeader != other.hasHeader)
			return false;
		if (hasFooter != other.hasFooter)
			return false;
		if (isHeader != other.isHeader)
			return false;
		if (readOnly != other.readOnly)
			return false;
		if (dataType != other.dataType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "APEv2TagFlag [hasHeader=" + hasHeader + ", hasFooter="
				+ hasFooter + ", isHeader=" + isHeader + ", readOnly="
				+ readOnly + ", dataType=" + dataType + "]";
	}

	public boolean hasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public boolean hasFooter() {
		return hasFooter;
	}

	public void setHasFooter(boolean hasFooter) {
		this.hasFooter = hasFooter;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public byte getDataType() {
		return dataType;
	}

	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

}
