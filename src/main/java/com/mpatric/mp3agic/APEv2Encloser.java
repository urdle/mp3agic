package com.mpatric.mp3agic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* Extract from http://audacity.googlecode.com/svn/audacity-src/trunk/lib-src/taglib/taglib/ape/ape-tag-format.txt
 * ================================================================================
 * = 2 - APE Tag Header / Footer Format
 * ================================================================================
 *
 * Contains number, length and attributes of all tag items; in LITTLE ENDIAN
 *
 * Header and Footer are different in 1 bit in the Tags Flags to distinguish
 * between them.
 *
 * Member of APE Tag 2.0
 *
 * /===========================================================================\
 * | Preamble       | 8 bytes | { 'A', 'P', 'E', 'T', 'A', 'G', 'E', 'X' }     |  0-7
 * |----------------|---------|------------------------------------------------|
 * | Version Number | 4 bytes | 1000 = Version 1.000, 2000 = Version 2.000     |  8-11
 * |----------------|---------|------------------------------------------------|
 * | Tag Size       | 4 bytes | Tag size in bytes including footer and all tag |  12-15
 * |                |         | items excluding the header (for 1.000          |
 * |                |         | compatibility)                                 |
 * |----------------|---------|------------------------------------------------|
 * | Item Count     | 4 bytes | Number of items in the tag                     |  16-19
 * |----------------|---------|------------------------------------------------|
 * | Tag Flags      | 4 bytes | Global flags                                   |  20-23
 * |----------------|---------|------------------------------------------------|
 * | Reserved       | 8 bytes | Must be zeroed                                 |  24-31
 * \===========================================================================/
 * 
 * ================================================================================
 * = 3 - APE Tag Flags
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

public class APEv2Encloser {
	public static final int ENCLOSURE_SIZE = 32;
	int version;
	int tagSize;
	int itemCount;
	boolean hasHeader;
	boolean hasFooter;
	boolean isHeader;
	int encoding;
	boolean readOnly;

	/*
	 * public APEv2Encloser(byte[] bytes) throws InvalidDataException { if
	 * (!isValidEncloser(bytes)) { throw new
	 * InvalidDataException("Invalid Header"); } }
	 */

	public APEv2Encloser(RandomAccessFile file, int offset, boolean isEndOffset)
			throws IOException, UnsupportedTagException, InvalidDataException {

		if (isEndOffset) {
			offset -= ENCLOSURE_SIZE;
		}
		if (offset < 0) {
			throw new InvalidDataException("Invalid Header : offset = "
					+ offset);
		}

		file.seek(offset);

		byte[] bytes = new byte[ENCLOSURE_SIZE];

		int bytesRead = file.read(bytes, 0, ENCLOSURE_SIZE);
		if (bytesRead < ENCLOSURE_SIZE)
			throw new IOException("Not enough bytes read");

		if (!isValidEncloser(bytes)) {
			throw new InvalidDataException("Invalid Header/Footer at "
					+ offset + " : not starting with APETAGEX.");
		}

		unpack(bytes);

		/*
		 * file.seek(offset-tagSize); bytesRead = file.read(bytes, 0,
		 * ENCLOSURE_SIZE);
		 * 
		 * if (!isValidEncloser(bytes)) { throw new
		 * InvalidDataException("Invalid Header/Footer again at " + +offset +
		 * " : not starting with APETAGEX."); } unpack(bytes);
		 */
	}

	static public boolean isValidEncloser(byte[] bytes) {
		String Preambule = new String(bytes, 0, 8);
		return (Preambule.equals("APETAGEX"));
	}

	private void unpack(byte[] bytes) {
		// System.out.println("whole header: " +
		// java.util.Arrays.toString(bytes));

		ByteBuffer b = ByteBuffer.wrap(bytes, 8, 16);
		b.order(ByteOrder.LITTLE_ENDIAN);

		version = b.getInt();
		tagSize = b.getInt();
		itemCount = b.getInt();

		// Warning : Because of Little-Endian, bits are ordered :
		// 7-0 / 15-8 / 23-16 / 31-24

		hasHeader = ((bytes[23] & 0x80) != 0);
		hasFooter = ((bytes[23] & 0x40) == 0);
		isHeader = ((bytes[23] & 0x20) != 0);

		encoding = (bytes[20] & 0x06);
		readOnly = ((bytes[20] & 0x01) != 0);

		/*
		 * byte[] buffer=new byte[4]; buffer[0]=0; tagFlags =
		 * java.nio.ByteBuffer.allocate(4).putInt(b.getInt()).array();
		 * 
		 * System.arraycopy(bytes, 8, buffer, 0, 4); version
		 * =BufferTools.unpackInteger(buffer[0], buffer[1], buffer[2],
		 * buffer[3]); System.out.println("vE: " + new
		 * java.math.BigInteger(buffer).toString(16)); System.out.println("vE: "
		 * + new java.math.BigInteger(buffer).toString(16)); tagSize
		 * =java.lang.Integer.reverseBytes(new
		 * java.math.BigInteger(buffer).intValue()); java.nio.ByteBuffer buf =
		 * java.nio.ByteBuffer.wrap(buffer); System.out.println("vE: " +
		 * java.nio
		 * .ByteBuffer.wrap(buffer).order(java.nio.ByteOrder.LITTLE_ENDIAN
		 * ).getInt()); System.out.println("vE: " +
		 * java.nio.ByteBuffer.wrap(buffer).getDouble());
		 * 
		 * 
		 * java.nio.ByteBuffer
		 * b=java.nio.ByteBuffer.wrap(bytes,8,12).order(java.
		 * nio.ByteOrder.LITTLE_ENDIAN); version= b.getInt(); tagSize
		 * =java.lang.
		 * Integer.reverseBytes(java.nio.ByteBuffer.wrap(bytes,12,4).getInt());
		 * tagSize
		 * =java.nio.ByteBuffer.wrap(bytes,12,4).order(java.nio.ByteOrder
		 * .LITTLE_ENDIAN).getInt(); tagSize =b.getInt(); java.math.BigInteger
		 * bi = new java.math.BigInteger(buffer); System.out.println("tsE: " +
		 * java.util.Arrays.toString(buffer)); System.out.println("vE: " + new
		 * java.math.BigInteger(buffer).toString(16)); System.out.println("vE: "
		 * + new java.math.BigInteger(buffer).toString(10));
		 * System.out.println("vE: " + );
		 * 
		 * System.arraycopy(bytes, 16, buffer, 0, 4); itemCount
		 * =BufferTools.unpackInteger(buffer[0], buffer[1], buffer[2],
		 * buffer[3]); itemCount =java.lang.Integer.reverseBytes(new
		 * java.math.BigInteger(buffer).intValue()); System.out.println("icE: "
		 * + java.util.Arrays.toString(buffer)); itemCount =b.getInt();
		 * 
		 * byte[] dd = new byte[]{(byte)0x12, (byte)0x34, (byte)0x56,
		 * (byte)0x78}; System.arraycopy(dd, 0, buffer, 1, 4); version
		 * =BufferTools.unpackInteger(buffer[0], buffer[1], buffer[2],
		 * buffer[3]); System.out.println("vE: " + version);
		 * System.out.println("vTF: " + new
		 * java.math.BigInteger(tagFlags).toString(2));
		 * 
		 * System.out.format("tagFlags:"); 
		 * 
		 * for (byte bb : tagFlags) { System.out.format("0x%x ", bb); } 
		 * System.out.println("");
		 * 
		 */

		System.out.println(this.toString());
		// System.out.println("tagFlags: " +
		// java.util.Arrays.toString(tagFlags));
		// System.out.println("tagFlags: " + b.getInt());
	}

	@Override
	public String toString() {
		return "APEv2Encloser [version=" + version + ", tagSize=" + tagSize
				+ ", itemCount=" + itemCount + ", hasHeader=" + hasHeader
				+ ", hasFooter=" + hasFooter + ", isHeader=" + isHeader
				+ ", encoding=" + encoding + ", readOnly=" + readOnly + "]";
	}

}
