package artlu;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;



/**
 * Bit encoding/decoding class.
 * The encoding provides add and set methods for building a bit block from various data types.
 * The decoding provides static decode methods for various data types.
 * @author anderse
 *
 */
public class BitCodec {

	private static final int ZERO_ASCII = 48;
	final static int bcdSize = 4;
	public static final String MZ_ULTRA_BITFIELD_ENCODER = "mz.ultra.bitfield.codec";
	public static final boolean useNewBitFieldEncoding = Boolean.getBoolean(MZ_ULTRA_BITFIELD_ENCODER);
	public static final int BYTE_SIZE = 8;
	private final List<Boolean> encodedState = new ArrayList<>();

	/** 
	 * Clear encoded state.
	 */
	public void clear() {
		encodedState.clear();
	}

	/** 
	 * Convert a long value to an array of bytes
	 * @param value
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static byte[] longArrayOf(final long value, final int bitSize, final boolean signed) {
		long v = longBinaryOf(value, bitSize, signed);
		final byte[] ba = new byte[byteSizeOf(bitSize)];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) (v & 0xff);
			v >>= BYTE_SIZE;
		}
		return ba;
	}

	/** 
	 * Convert an int value to an array of bytes
	 * @param value
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static byte[] intArrayOf(final int value, final int bitSize, final boolean signed) {
		int v = intBinaryOf(value, bitSize, signed);
		final byte[] ba = new byte[byteSizeOf(bitSize)];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) (v & 0xff);
			v >>= BYTE_SIZE;
		}
		return ba;
	}

	/** 
	 * Convert a byte value to an array of bytes
	 * @param value
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static byte[] byteArrayOf(final byte value, final int bitSize, final boolean signed) {
		byte v = byteBinaryOf(value, bitSize, signed);
		final byte[] ba = new byte[1];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) (v & 0xff);
			v >>= BYTE_SIZE;
		}
		return ba;
	}

	/** 
	 * Convert a decimal representation to  binary one
	 * @param value
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static long longBinaryOf(final long value, final int bitSize, final boolean signed) {
		if (!signed && value < 0) throw new NumberFormatException();
		final long mask = (1L << ((long)bitSize - 1L));
		if (signed) {
			if (value < 0) {
				return (value + (mask << 1));
			}
		}
		return value;
	}

	/**
	 * Convert a binary representation to a decimal one
	 * @param value
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static long longDecimalOf(final long value, final int bitSize, final boolean signed) {
		if (signed) {
			final long mask = (1L << ((long)bitSize - 1L));
			if ((mask & value) != 0) {
				return (value - (mask << 1));
			}
		}
		return value;
	}

	/**
	 * Convert a byte array to a long value
	 * @param ba
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static long longOf(final byte[] ba, final int bitSize, final boolean signed) {
		long v = 0l;
		for (int i = ba.length - 1; i >= 0; i--) {
			v <<= BYTE_SIZE;
			v |= (ba[i] & 0xff);
		}
		return longDecimalOf(v, bitSize, signed);
	}

	/** 
	 * Convert a decimal representation to  binary one
	 * @param value
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static byte byteBinaryOf(final byte value, final int bitSize, final boolean signed) {
		if (!signed && value < 0) throw new NumberFormatException();
		final int mask = (1 << (bitSize - 1));
		if (signed) {
			if (value < 0) {
				return (byte) (value + (mask << 1));
			}
		}
		return value;
	}

	/**
	 * Convert a binary representation to a decimal one
	 * @param value
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static byte byteDecimalOf(final byte value, final int bitSize, final boolean signed) {
		if (signed) {
			final int mask = (1 << (bitSize - 1));
			if ((mask & value) != 0) {
				return (byte) (value - (mask << 1));
			}
		}
		return value;
	}

	/**
	 * Convert a byte array to a byte value
	 * @param ba
	 * @param bitSize
	 * @param signed
	 * @return
	 */
	public static byte byteOf(final byte[] ba, final int bitSize, final boolean signed) {
		byte v = 0;
		for (int i = ba.length - 1; i >= 0; i--) {
			v <<= BYTE_SIZE;
			v |= (ba[i] & 0xff);
		}
		return byteDecimalOf(v, bitSize, signed);
	}

	public static int intBinaryOf(final int value, final int bitSize, final boolean signed) {
		if (!signed && value < 0) throw new NumberFormatException();
		final int mask = (1 << (bitSize - 1));
		if (signed) {
			if (value < 0) {
				return (value + (mask << 1));
			}
		}
		return value;
	}

	public static int intDecimalOf(final int value, final int bitSize, final boolean signed) {
		if (signed) {
			final int mask = (1 << (bitSize - 1));
			if ((mask & value) != 0) {
				return (value - (mask << 1));
			}
		}
		return value;
	}

	public static int intOf(final byte[] ba, final int bitSize, final boolean signed) {
		int v = 0;
		for (int i = ba.length - 1; i >= 0; i--) {
			v <<= BYTE_SIZE;
			v |= (ba[i] & 0xff);
		}
		return intDecimalOf(v, bitSize, signed);
	}
	
	public static byte[] bytearrayOf(final byte[] ba, final int bitSize, final boolean signed) {
		return ba;
	}
	public static String stringOf(final String value, final int bitSize, final boolean signed) {
		return value;
	}
	public static byte byteOf(final byte value, final int bitSize, final boolean signed) {
		return value;
	}
	public static short shortOf(final short value, final int bitSize, final boolean signed) {
		return value;
	}
	public static long longOf(final long value, final int bitSize, final boolean signed) {
		return value;
	}
	public static int intOf(final int value, final int bitSize, final boolean signed) {
		return value;
	}
	
	public static BigInteger bigintOf(final BigInteger value, final int bitSize, final boolean signed) {
		return value;
	}

	public void setBcdEncode(final String v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed, final int align) {
		addBcdEncode(v, bitSize, bytePadding, signed, align); //TODO
	}
	
	public BitCodec addBcdEncode(final String v, final int bitSize, final int bytePadding, final boolean signed, final int align) {
		final byte[] ba = v.getBytes(); //TODO encoding
		final int n = bitSize / bcdSize;
		final int startIx = startIxOf(align, n, ba.length);
		int j = 0;
		final int bp = bytePadding & 0b1111;
		for (int i = 0; i < n; i++) {
			if (i >= startIx && i < startIx + ba.length) {
				addByteEncode((byte) (ba[j] - ZERO_ASCII), bcdSize, bytePadding, signed);
				j++;
			} else {
				addByteEncode((byte) bp, bcdSize, bytePadding, signed);
			}
		}
		return this;
	}

	public int startIxOf(final int align, final int totalSize, final int strSize) {
		return align == 1 ? 0: totalSize - strSize;
	}

	public BitCodec setByteArrayEncode(final byte[] v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(reverse(v), bitOffset, bitSize, bytePadding); //TODO reverse performance
	}
	public BitCodec addByteArrayEncode(final byte[] v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(reverse(v), bitSize, bytePadding); //TODO reverse performance
	}

	public BitCodec setDoubleEncode(final double v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(longArrayOf(Double.doubleToRawLongBits(v), bitSize, signed), bitOffset, bitSize, bytePadding);
	}
	public BitCodec addDoubleEncode(final double v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(longArrayOf(Double.doubleToRawLongBits(v), bitSize, signed), bitSize, bytePadding);
	}
	public BitCodec setFloatEncode(final float v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(intArrayOf(Float.floatToRawIntBits(v), bitSize, signed), bitOffset, bitSize, bytePadding);
	}
	public BitCodec addFloatEncode(final float v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(intArrayOf(Float.floatToRawIntBits(v), bitSize, signed), bitSize, bytePadding);
	}
	public BitCodec setByteEncode(final byte v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(byteArrayOf(v, bitSize, signed), bitOffset, bitSize, bytePadding);
	}
	public BitCodec addByteEncode(final byte v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(byteArrayOf(v, bitSize, signed), bitSize, bytePadding);
	}

	public BitCodec setShortEncode(final short v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(intArrayOf(v, bitSize, signed), bitOffset, bitSize, bytePadding);
	}
	public BitCodec addShortEncode(final short v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(intArrayOf(v, bitSize, signed), bitSize, bytePadding);
	}
	public BitCodec setIntEncode(final int v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(intArrayOf(v, bitSize, signed), bitOffset, bitSize, bytePadding);
	}
	public BitCodec addIntEncode(final int v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(intArrayOf(v, bitSize, signed), bitSize, bytePadding);
	}

	public BitCodec setLongEncode(final long v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(longArrayOf(v, bitSize, signed), bitOffset, bitSize, bytePadding);
	}
	public BitCodec addLongEncode(final long v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(longArrayOf(v, bitSize, signed), bitSize, bytePadding);
	}
	public BitCodec setBigintEncode(final BigInteger v, final int bitOffset, final int bitSize, final int bytePadding, final boolean signed) {
		return setEncode(longArrayOf(v.longValue(), bitSize, signed), bitOffset, bitSize, bytePadding);
	}
	public BitCodec addBigintEncode(final BigInteger v, final int bitSize, final int bytePadding, final boolean signed) {
		return addEncode(longArrayOf(v.longValue(), bitSize, signed), bitSize, bytePadding);
	}

	public BitCodec addEncode(final byte[] ba, final int bitSize, final int bytePadding) {
		final boolean[] e = encode(ba, bitSize, bytePadding);
		for (final boolean b : e)
			encodedState.add(b);
		return this;
	}
	public BitCodec setEncode(final byte[] ba, final int bitOffset, final int bitSize, final int bytePadding) {
		while (encodedState.size() < (bitOffset + bitSize)) encodedState.add(false);
		final boolean[] e = encode(ba, bitSize, bytePadding);
		for (int i = 0; i < bitSize; i++) {
			encodedState.set(i + bitOffset, e[i]);
		}
		return this;
	}

	public static boolean[] encode(final byte[] ba, final int bitSize, final int bytePadding) {
		return reverse(flatten(toPartitionedArray(ba, bitSize)));
	}
	
	public static boolean[] reverse(final boolean[] ba) {
		final boolean[] result = new boolean[ba.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = ba[result.length - i - 1];
		}
		return result;
	}
	
	public static byte[] reverse(final byte[] ba) {
		return reverse(ba, 0, ba.length);
	}
	
	public static byte[] reverse(final byte[] ba, final int offset, final int size) {
		final byte[] result = new byte[size];
		for (int i = 0; i < result.length; i++) {
			result[i] = ba[offset + size - i - 1];
		}
		return result;
	}

	public byte[] toByteArray() {
		return toByteArray(encodedState, true);
	}

	public static byte[] toByteArray(final List<Boolean> l, final boolean reverse) {
		final boolean[] result = new boolean[l.size()];
		for (int i = 0; i < l.size(); i++) {
			result[i] = l.get(i);
		}
		return toByteArray(result, reverse);
	}

	public static byte[] toByteArray(final boolean[] ba, final boolean reverse) {
		return toByteArray(partition(BYTE_SIZE, ba), reverse);
	}

	public static double doubleDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return doubleDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	public static double doubleDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return Double.longBitsToDouble(longDecode(data, byteOffset, bitOffset, bitSize, signed));
	}
	public static float floatDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return floatDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	public static float floatDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return Float.intBitsToFloat(intDecode(data, byteOffset, bitOffset, bitSize, signed));
	}
	public static String bcdDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return bcdDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	
	public static String bcdDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < bitSize / bcdSize; i++) {
			final int bitIx = i * bcdSize + byteOffset * BYTE_SIZE + bitOffset;
			if (bitIx >= data.length * BYTE_SIZE) break;
			buf.append((char)(byteDecode(data, bitIx, bcdSize, signed) + ZERO_ASCII));
		}
		return buf.toString();
	}
	public static byte[] bytearrayDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return reverse(decode(data, byteOffset, bitOffset, bitSize));
		//TODO reverse performance
	}
	public static byte byteDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return byteDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	public static byte byteDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return byteOf(decode(data, byteOffset, bitOffset, bitSize), bitSize, signed);
	}

	public static short shortDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return shortDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	public static short shortDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return (short) intOf(decode(data, byteOffset, bitOffset, bitSize), bitSize, signed);
	}
	public static int intDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return intDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	public static int intDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return intOf(decode(data, byteOffset, bitOffset, bitSize), bitSize, signed);
	}

	public static BigInteger bigintDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return bigintDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	public static BigInteger bigintDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return BigInteger.valueOf(longOf(decode(data, byteOffset, bitOffset, bitSize), bitSize, signed));
	}
	public static long longDecode(final byte[] data, final int bitOffset, final int bitSize,
			final boolean signed) {
		return longDecode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize, signed);
	}
	public static long longDecode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize,
			final boolean signed) {
		return longOf(decode(data, byteOffset, bitOffset, bitSize), bitSize, signed);
	}

	public static byte[] decode(final byte[] data, final int bitOffset, final int bitSize) {
		return decode(data, bitOffset / BYTE_SIZE, bitOffset % BYTE_SIZE, bitSize);
	}

	public static byte[] decode(final byte[] data, final int byteOffset, final int bitOffset, final int bitSize) {
		final boolean[] br = new boolean[bitSize];
		final boolean[] fr = flatten(toPartitionedArray(reverseBitsInBytes(data), data.length * BYTE_SIZE));
		System.arraycopy(fr, byteOffset * BYTE_SIZE + bitOffset, br, 0, br.length);
		return toByteArray(reverse(br), false);
	}

	public static byte[] reverseBitsInBytes(final byte[] ba) {
		final byte[] result = new byte[ba.length];
		for (int i = 0; i < ba.length; i++) {
			result[i] = reverseBits(ba[i]);
		}
		return result;
	}
	
	public static byte reverseBits(final byte b) {
		byte y = 0;
		int x = (b & 0xff);
		for (int position = BYTE_SIZE - 1; position >= 0; position--) {
			y += ((x & 1) << position);
			x >>= 1;
		}
		return y;
	}

	public static byte[] toByteArray(final boolean[][] partArr, final boolean reverse) {
		final byte[] result = new byte[partArr.length];
		for (int i = 0; i < result.length; i++) {
			final boolean[] ba = partArr[i];
			for (int j = 0; j < ba.length; j++) {
				final int v = (ba[j] ? 1 : 0) << (reverse ? (BYTE_SIZE - 1 - j): j);
				result[i] |= v;
			}
		}
		return result;
	}

	public static int byteSizeOf(final int len) {
		return (len + BYTE_SIZE - 1) / BYTE_SIZE;
	}
	
	public static int sizeOf(final int len, final int by) {
		return (len + by - 1) / by;
	}

	public static boolean[][] partition(final int by, final boolean[] arr) {
		final boolean[][] partArr = new boolean[sizeOf(arr.length, by)][];
		for (int i = 0; i < partArr.length; i++) {
			final boolean[] subArr = new boolean[lenOf(by, i + 1, arr.length)];
			partArr[i] = subArr;
			for (int j = 0; j < subArr.length; j++) {
				subArr[j] = arr[i * by + j];
			}
		}
		return partArr;
	}

	public static boolean[] flatten(final boolean[][] part) {
		final boolean[] result = new boolean[lenOf(part)];
		int ix = 0;
		for (int i = 0; i < part.length; i++) {
			final boolean[] p = part[i];
			for (int j = 0; j < p.length; j++) {
				result[ix] = p[j];
				ix++;
			}
		}
		return result;
	}

	public static int lenOf(final boolean[][] part) {
		int n = 0;
		for (int i = 0; i < part.length; i++) {
			n += part[i].length;
		}
		return n;
	}

	public static int lenOf(final int by, final int i, final int length) {
		return i * by > length ? length % by : by;
	}

	public static boolean[][] toPartitionedArray(final byte[] ba, final int bitSize) {
		final boolean[][] result = new boolean[byteSizeOf(bitSize)][];
		for (int i = 0; i < result.length; i++) {
			final boolean[] subArr = new boolean[lenOf(BYTE_SIZE, i + 1, bitSize)];
			result[i] = subArr;
			if (i < ba.length) {
				int bv = (ba[i] & 0xff);
				for (int j = 0; j < subArr.length; j++) {
					subArr[j] = (bv & 1) != 0;
					bv >>= 1;
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return encodedState.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof BitCodec) {
			final BitCodec bsu = (BitCodec)obj;
			return encodedState.equals(bsu.encodedState);
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return encodedState.hashCode();
	}
}
