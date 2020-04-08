import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

public class Ipv4Address {
	private static final int IP_PARTS = 4;
	private static final int IP_PART_LENGTH = 8;
	private static final int IP_LENGTH = 32;
	private static final int IP_MAX_PART_VALUE = 255;

	public static void main(String[] args) {
		String r = nextIp("255.255.255.255");
		System.out.println(r);
		String r2 = nextIp("10.0.0.255");
		System.out.println(r2);
	}

	private static String nextIp(String ipAddr) {
		try {
			var binary = getBinary(ipAddr);
			binary = increment(binary);
			String[] ipParts = mergeTo32Bits(binary);
			return getFormattedIp(ipParts);
		} catch (RuntimeException exc) {
			return null;
		}
	}

	private static BigInteger getBinary(String ip) {
		StringBuilder bits32 = new StringBuilder();
		for (String part : getIpParts(ip)) {
			bits32.append(getPartAsBinary(part));
		}
		return new BigInteger(bits32.toString(), 2);
	}

	private static String getPartAsBinary(String part) {
		var intValue = Integer.parseInt(part);
		if (intValue > IP_MAX_PART_VALUE) {
			throw new RuntimeException("Ip is invalid.");
		}
		String binaryInt = Integer.toBinaryString(intValue);
		return StringUtils.leftPad(binaryInt, IP_PART_LENGTH, "0");
	}

	private static String[] getIpParts(String ip) {
		String[] ipParts = ip.split("\\.");
		if (ipParts.length != IP_PARTS) {
			throw new RuntimeException("Ip is invalid.");
		}
		return ipParts;
	}

	private static BigInteger increment(BigInteger binary) {
		return binary.add(new BigInteger("1", 2));
	}

	private static String[] mergeTo32Bits(BigInteger b) {
		String bits32 = StringUtils.leftPad(b.toString(2), IP_LENGTH, "0");
		String[] ipParts = new String[IP_PARTS];
		int index = 0;
		while (bits32.length() >= IP_PART_LENGTH) {
			ipParts[index++] = bits32.substring(0, IP_PART_LENGTH);
			bits32 = bits32.substring(IP_PART_LENGTH);
		}
		return ipParts;
	}

	private static String getFormattedIp(String[] ipParts) {
		StringBuilder formattedIp = new StringBuilder();
		for (int i = 0; i < IP_PARTS; i++) {
			formattedIp.append(new BigInteger(ipParts[i], 2).toString());
			if (i < ipParts.length - 1) {
				formattedIp.append(".");
			}
		}
		return formattedIp.toString();
	}
}
