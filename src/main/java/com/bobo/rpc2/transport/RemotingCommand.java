package com.bobo.rpc2.transport;

import java.util.concurrent.atomic.AtomicInteger;

public class RemotingCommand {

	private int type;
	private int code;
	private int commandId;
	private int version;
	private byte[] body;

	protected static final int DEFAULT_VERSION = 1;

	private static final AtomicInteger atomicInteger = new AtomicInteger();

	public static int nextId() {
		return atomicInteger.incrementAndGet();
	}

	public static RemotingCommand newCommand(int type, int code, int commandId, int version) {
		return newCommand(type, code, commandId, version, null);
	}

	public static RemotingCommand newCommand(int type, int code, int commandId, int version, byte[] body) {
		RemotingCommand requestCommand = new RemotingCommand();
		requestCommand.setType(type);
		requestCommand.setCode(code);
		requestCommand.setCommandId(commandId);
		requestCommand.setVersion(version);
		requestCommand.setBody(body);
		return requestCommand;
	}

	public static RemotingCommand newRequestCommand(int code, int commandId, byte[] body) {
		return newCommand(RemotingCommandType.REQUEST_COMMAND.getType(), code, commandId, DEFAULT_VERSION, body);
	}

	public static RemotingCommand newResponseCommand(int code, int commandId) {
		return newCommand(RemotingCommandType.RESPONSE_COMMAND.getType(), code, commandId, DEFAULT_VERSION, null);
	}

	public static RemotingCommand newResponseCommand(int code, int commandId, byte[] body) {
		return newCommand(RemotingCommandType.RESPONSE_COMMAND.getType(), code, commandId, DEFAULT_VERSION, body);
	}

	public boolean isReponse() {
		return RemotingCommandType.RESPONSE_COMMAND.getType() == type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}
}
