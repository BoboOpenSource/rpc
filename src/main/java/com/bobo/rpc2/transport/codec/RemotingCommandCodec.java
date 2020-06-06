package com.bobo.rpc2.transport.codec;

import java.util.List;

import com.bobo.rpc2.transport.RemotingCommand;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class RemotingCommandCodec extends ByteToMessageCodec<RemotingCommand> {

	@Override
	protected void encode(ChannelHandlerContext ctx, RemotingCommand msg, ByteBuf out) throws Exception {
		int type = msg.getType();
		int version = msg.getVersion();
		int code = msg.getCode();
		int commandId = msg.getCommandId();
		out.writeInt(type);
		out.writeInt(version);
		out.writeInt(code);
		out.writeInt(commandId);
		byte[] body = msg.getBody();
		int bodyLen = body == null ? 0 : body.length;
		out.writeInt(bodyLen);
		if (bodyLen > 0) {
			out.writeBytes(body);
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int type = in.readInt();
		int version = in.readInt();
		int code = in.readInt();
		int commandId = in.readInt();
		RemotingCommand command = RemotingCommand.newCommand(type, code, commandId, version);
		int bodyLen = in.readInt();
		if (bodyLen > 0) {
			byte[] body = new byte[bodyLen];
			in.readBytes(body);
			command.setBody(body);
		}
		out.add(command);
	}
}
