package io.github.foxitdog.alltoserver.netty.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.Arrays;

public class BitUtils {

	/**
	 * CRC16 字典
	 */
	private static final int[] CRC16_TABLE = { 0x0000, 0x1189, 0x2312, 0x329B, 0x4624, 0x57AD, 0x6536, 0x74BF, 0x8C48,
			0x9DC1, 0xAF5A, 0xBED3, 0xCA6C, 0xDBE5, 0xE97E, 0xF8F7, 0x1081, 0x0108, 0x3393, 0x221A, 0x56A5, 0x472C,
			0x75B7, 0x643E, 0x9CC9, 0x8D40, 0xBFDB, 0xAE52, 0xDAED, 0xCB64, 0xF9FF, 0xE876, 0x2102, 0x308B, 0x0210,
			0x1399, 0x6726, 0x76AF, 0x4434, 0x55BD, 0xAD4A, 0xBCC3, 0x8E58, 0x9FD1, 0xEB6E, 0xFAE7, 0xC87C, 0xD9F5,
			0x3183, 0x200A, 0x1291, 0x0318, 0x77A7, 0x662E, 0x54B5, 0x453C, 0xBDCB, 0xAC42, 0x9ED9, 0x8F50, 0xFBEF,
			0xEA66, 0xD8FD, 0xC974, 0x4204, 0x538D, 0x6116, 0x709F, 0x0420, 0x15A9, 0x2732, 0x36BB, 0xCE4C, 0xDFC5,
			0xED5E, 0xFCD7, 0x8868, 0x99E1, 0xAB7A, 0xBAF3, 0x5285, 0x430C, 0x7197, 0x601E, 0x14A1, 0x0528, 0x37B3,
			0x263A, 0xDECD, 0xCF44, 0xFDDF, 0xEC56, 0x98E9, 0x8960, 0xBBFB, 0xAA72, 0x6306, 0x728F, 0x4014, 0x519D,
			0x2522, 0x34AB, 0x0630, 0x17B9, 0xEF4E, 0xFEC7, 0xCC5C, 0xDDD5, 0xA96A, 0xB8E3, 0x8A78, 0x9BF1, 0x7387,
			0x620E, 0x5095, 0x411C, 0x35A3, 0x242A, 0x16B1, 0x0738, 0xFFCF, 0xEE46, 0xDCDD, 0xCD54, 0xB9EB, 0xA862,
			0x9AF9, 0x8B70, 0x8408, 0x9581, 0xA71A, 0xB693, 0xC22C, 0xD3A5, 0xE13E, 0xF0B7, 0x0840, 0x19C9, 0x2B52,
			0x3ADB, 0x4E64, 0x5FED, 0x6D76, 0x7CFF, 0x9489, 0x8500, 0xB79B, 0xA612, 0xD2AD, 0xC324, 0xF1BF, 0xE036,
			0x18C1, 0x0948, 0x3BD3, 0x2A5A, 0x5EE5, 0x4F6C, 0x7DF7, 0x6C7E, 0xA50A, 0xB483, 0x8618, 0x9791, 0xE32E,
			0xF2A7, 0xC03C, 0xD1B5, 0x2942, 0x38CB, 0x0A50, 0x1BD9, 0x6F66, 0x7EEF, 0x4C74, 0x5DFD, 0xB58B, 0xA402,
			0x9699, 0x8710, 0xF3AF, 0xE226, 0xD0BD, 0xC134, 0x39C3, 0x284A, 0x1AD1, 0x0B58, 0x7FE7, 0x6E6E, 0x5CF5,
			0x4D7C, 0xC60C, 0xD785, 0xE51E, 0xF497, 0x8028, 0x91A1, 0xA33A, 0xB2B3, 0x4A44, 0x5BCD, 0x6956, 0x78DF,
			0x0C60, 0x1DE9, 0x2F72, 0x3EFB, 0xD68D, 0xC704, 0xF59F, 0xE416, 0x90A9, 0x8120, 0xB3BB, 0xA232, 0x5AC5,
			0x4B4C, 0x79D7, 0x685E, 0x1CE1, 0x0D68, 0x3FF3, 0x2E7A, 0xE70E, 0xF687, 0xC41C, 0xD595, 0xA12A, 0xB0A3,
			0x8238, 0x93B1, 0x6B46, 0x7ACF, 0x4854, 0x59DD, 0x2D62, 0x3CEB, 0x0E70, 0x1FF9, 0xF78F, 0xE606, 0xD49D,
			0xC514, 0xB1AB, 0xA022, 0x92B9, 0x8330, 0x7BC7, 0x6A4E, 0x58D5, 0x495C, 0x3DE3, 0x2C6A, 0x1EF1, 0x0F78 };

	/**
	 * byte 字节
	 */
	private static final byte BYTECACHE[] = new byte[-(-128) + 127 + 1];

	static {
		for (int i = 0; i < BYTECACHE.length; i++) {
			BYTECACHE[i] = new Byte((byte) (i - 128));
		}
	}

	/**
	 * 获取指定的byte
	 * 
	 * @param b
	 *            byte大小
	 * @return 指定的byte
	 */
	public static byte getByte(int b) {
		Assert.state(b < 128 && b >= -128, "希望的数值为-128 ~ 127,实际是 " + b);
		int offset = 128;
		return BYTECACHE[b + offset];
	}

	/**
	 * 获取int指定位置的byte
	 *
	 * @param src
	 *            int值
	 * @param position
	 *            位置
	 * @return byte
	 */
	public static byte getTargetByte(int src, int position) {
		byte target;
		switch (position) {
		case 1:
			target = (byte) ((src & 0xff000000) >> 24);
			break;
		case 2:
			target = (byte) ((src & 0xff0000) >> 16);
			break;
		case 3:
			target = (byte) ((src & 0xff00) >> 8);
			break;
		case 4:
			target = (byte) (src & 0xff);
			break;
		default:
			target = 0x00;
			break;
		}
		return target;
	}

	/**
	 * 以字节数组的形式返回指定的 32 位有符号整数值。
	 *
	 * @param data
	 *            要转换的数字。
	 * @return 长度为 4 的字节数组。
	 */
	public static byte[] getIntBytes(int data) {
		return getIntBytes(data, 4);
	}

	/**
	 * 以字节数组的形式返回指定的 32 位有符号整数值。
	 *
	 * @param data
	 *            要转换的数字。
	 * @param i
	 *            字节长度
	 * @return 长度为 i 的字节数组。
	 */
	public static byte[] getIntBytes(int data, int i) {
		byte[] bytes = new byte[i];
		int index = 0;
		switch (i) {
		case 4:
			bytes[index++] = (byte) ((data & 0xff000000) >> 24);
		case 3:
			bytes[index++] = (byte) ((data & 0xff0000) >> 16);
		case 2:
			bytes[index++] = (byte) ((data & 0xff00) >> 8);
		case 1:
			bytes[index++] = (byte) (data & 0xff);
		}
		return bytes;
	}

	/**
	 * **注意点** 0xff*(byte) 变成int值。。。
	 * 
	 * @param bytes
	 * @return
	 */
	public static int toInt(byte... bytes) {
		Assert.isTrue(bytes.length <= 4 || bytes.length > 0, "bytes长度在1-4之间");
		int a = 0, i = 0;
		switch (bytes.length) {
		case 4:
			a |= 0xff000000 & (bytes[i++] << 32);
		case 3:
			a |= 0xff0000 & (bytes[i++] << 16);
		case 2:
			a |= 0xff00 & (bytes[i++] << 8);
		case 1:
			a |= 0xff & bytes[i++];
		}
		return a;
	}

	/**
	 * CRC 16/CCITT 校验
	 *
	 * @param buffer
	 *            字节数组
	 * @return
	 */
	public static int crc(byte[] buffer) {
		return crc16Ccitt(buffer, 0, buffer.length);
	}

	/**
	 * CRC校验
	 *
	 * @param buffer
	 *            字节数组
	 * @param offset
	 *            起始地址
	 * @param length
	 *            校验长度
	 * @return
	 */
	public static int crc16Ccitt(byte[] buffer, int offset, int length) {
		int crc = 0;
		for (int i = offset; i < offset + length; i++) {
			crc = (crc >> 8) ^ CRC16_TABLE[(crc ^ buffer[i]) & 0xFF];
		}
		return crc;
	}

	/**
	 * byte转16进制字符串,不带"0x"前缀
	 * 
	 * @param b
	 */
	public static String toHex(byte b) {
		String hexStr = Integer.toHexString(b & 0xFF).toUpperCase();
		hexStr = hexStr.length() == 2 ? hexStr : "0" + hexStr;
		return hexStr;
	}

	/**
	 * byte转16进制字符串,带"0x"前缀
	 * 
	 * @param b
	 */
	public static String toHexString(byte b) {
		return "0x" + toHex(b);
	}

	/**
	 * byte[]转16进制字符串,前后带"[]",每个byte之间有空格 e.g. [0x01 0x02]
	 */
	public static String toHexString(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int iMax = bytes.length - 1;
		for (int i = 0;; i++) {
			sb.append(toHexString(bytes[i]));
			if (i == iMax) {
				return sb.append(']').toString();
			}
			sb.append(' ');
		}
	}

	/**
	 * 转换
	 * 
	 * @param Ba
	 * @return
	 */
	public static byte[] convert(Byte[] Ba) {
		byte[] ba = new byte[Ba.length];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = Ba[i];
		}
		return ba;
	}

	/**
	 * byte[]转16进制字符串,前后和中间没有任何分隔符
	 */
	public static String toNextToHexString(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int iMax = bytes.length - 1;
		for (int i = 0;; i++) {
			String tempHexStr = Integer.toHexString(bytes[i] & 0xFF).toUpperCase();
			tempHexStr = tempHexStr.length() == 2 ? tempHexStr : "0" + tempHexStr;
			sb.append(tempHexStr);
			if (i == iMax) {
				return sb.toString();
			}
		}
	}

	/**
	 * 16进制转字节数组
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return new byte[0];
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 16进制字符转byte
	 * 
	 * @param c
	 * @return
	 */
	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 左填充
	 * 
	 * @param src
	 *            填充的原始数组
	 * @param len
	 *            长度
	 * @param model
	 *            字节模型
	 * @return 字节数组
	 */
	public static byte[] leftSupply(byte[] src, int len, byte model) {
		int supply = len - src.length;
		byte[] dest = Arrays.copyOf(getCopiedBytes(supply, model), len);
		System.arraycopy(src, 0, dest, supply, src.length);
		return dest;
	}

	/**
	 * 右填充
	 * 
	 * @param src
	 *            填充的原始数组
	 * @param len
	 *            长度
	 * @param model
	 *            字节模型
	 * @return 字节数组
	 */
	public static byte[] rightSupply(byte[] src, int len, byte model) {
		byte[] dest = Arrays.copyOf(src, len);
		int supply = len - src.length;
		System.arraycopy(getCopiedBytes(supply, model), 0, dest, src.length, dest.length);
		return dest;
	}

	/**
	 * byte数组转float 大端
	 */
	public static float byte2float(byte[] b) {
		int l;
		l = b[3];
		l &= 0xff;
		l |= ((long) b[2] << 8);
		l &= 0xffff;
		l |= ((long) b[1] << 16);
		l &= 0xffffff;
		l |= ((long) b[0] << 24);
		return Float.intBitsToFloat(l);
	}

	/**
	 * 判断src是否以dest开头 <br/>
	 * <code>
	 *  src(hex) : 01 02 03 04 <br/>
	 *  dest(hex): 01 02 03 <br/>
	 *  startWith(src,dest) = true
	 *  </code>
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static boolean startWith(byte[] src, byte[] dest) {
		if (dest.length > src.length) {
			return false;
		}
		if (src == dest) {
			return true;
		}
		for (int i = 0; i < dest.length; i++) {
			if (dest[i] != src[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * byte数组转float
	 */
	public static String byte2float4string(byte[] b) {
		return String.format("%.2f", byte2float(b));
	}

	/**
	 * 获得指定长度的buffer
	 * 
	 * @param len
	 *            长度
	 * @return
	 */
	public static ByteBuff getByteBuffer(int len) {
		return new ByteBuff(len);
	}

	/**
	 * 获取指定长度的某个字节
	 * 
	 * @param i
	 *            长度
	 * @param model
	 *            模版
	 * @return
	 */
	public static byte[] getCopiedBytes(int i, byte model) {
		byte[] bs = new byte[i];
		for (int j = 0; j < i; j++) {
			bs[j] = model;
		}
		return bs;
	}

	/**
	 * 获取指定长度的某个字节
	 * 
	 * @param i
	 *            长度
	 * @return 字节数组
	 */
	public static byte[] getCopiedBytes(int i) {
		return getCopiedBytes(i, (byte) 0x01);
	}

	/**
	 * byte buffer
	 */
	public static class ByteBuff {
		/**
		 * buffer中的数据
		 */
		@Getter
		@Setter
		private byte[] data;

		private int len;

		private int mark = 0;

		public ByteBuff(int init) {
			this.len = init;
			this.data = new byte[init];
		}

        /**
         * 写入指定的byte数组
         * @param bs
         */
		public void write(byte[] bs) {
			Assert.isTrue((bs.length + mark) <= len, "长度超过限制长度: " + len);
			for (int i = 0; i < bs.length; i++) {
				data[mark++] = bs[i];
			}
		}

        /**
         * 写入指定的byte
         * @param b
         */
		public void write(byte b) {
			Assert.isTrue((1 + mark) <= len, "长度超过限制长度: " + len);
			data[mark++] = b;
		}

        /**
         * 剩下所有填充 b
         * @param b
         */
		public void padding(byte b) {
			while (mark < len) {
				data[mark++] = b;
			}
		}

		public void clear() {
			data = new byte[len];
			mark = 0;
		}
	}
}