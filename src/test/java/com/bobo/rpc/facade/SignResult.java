package com.bobo.rpc.facade;

public class SignResult {

	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public SignResult(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "SignResult [desc=" + desc + "]";
	}
}
