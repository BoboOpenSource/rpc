package com.bobo.rpc2.transport;

public enum RemotingCommandType {
	REQUEST_COMMAND(1), RESPONSE_COMMAND(2);
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private RemotingCommandType(int type) {
		this.type = type;
	}

}
