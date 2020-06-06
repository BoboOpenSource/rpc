package com.bobo.rpc2.client;

import com.bobo.rpc2.transport.RemotingCommand;

public interface RpcCallback {

	void invokeComplete(RemotingCommand response);
}
