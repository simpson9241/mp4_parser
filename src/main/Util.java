package main;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Util {
	public static long ByteArrayToLong(byte[] bytes) {
		long value=0;
		for(int i=0;i<bytes.length;i++) {
			value=(value<<8)+(bytes[i]&0xff);
		}
		return value;
	}
	
	public static String ByteArrayToHex(byte[] bytes) {
		StringBuilder sb=new StringBuilder();
		for(final byte b: bytes) {
			sb.append(String.format("%02x ", b&0xff));
		}
		return sb.toString();
	}
	
}
