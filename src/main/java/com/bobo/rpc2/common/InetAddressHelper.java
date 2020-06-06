package com.bobo.rpc2.common;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class InetAddressHelper {

	/**
	 * @desc 获取本机eth0网卡的IP地址(也就是网卡一)
	 * @param
	 */
	public static String getLocalIp() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> e1 = NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = e1.nextElement();
				if (!ni.getName().equals("eth0")) {
					continue;
				} else {
					Enumeration<InetAddress> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = e2.nextElement();
						if (ia instanceof Inet6Address) {
							continue;
						}
						ip = ia.getHostAddress();
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ip == "" || ip == null) {
			ip = "localhost";
		}
		return ip;
	}
}
