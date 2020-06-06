package com.bobo.rpc2.transport;

public class URI {

	private String host;

	private int port;

	private String type;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public URI(String type, String host, int port) {
		this.type = type;
		this.host = host;
		this.port = port;
	}

	public static URI createURI(String uri) {
		try {
			int idx = uri.indexOf("//");
			if (idx < 0) {
				throw new RuntimeException("uri无法识别");
			}
			String type = uri.substring(0, idx - 1);
			int idx1 = uri.lastIndexOf(":");
			if (idx == idx1) {
				throw new RuntimeException("uri无法识别");
			}
			String host = uri.substring(idx + 2, idx1);
			int port = Integer.parseInt(uri.substring(idx1 + 1));
			System.out.println(type + "," + host + "," + port);
			return new URI(type, host, port);
		} catch (Exception e) {
			throw new RuntimeException("uri无法识别");
		}
	}

	public static URI createURI(String type, String host, int port) {
		return new URI(type, host, port);
	}

	public String toUriStr() {
		return String.format("%s://%s:%s", type, host, port);
	}

	@Override
	public String toString() {
		return "URI [host=" + host + ", port=" + port + ", type=" + type + "]";
	}
}
