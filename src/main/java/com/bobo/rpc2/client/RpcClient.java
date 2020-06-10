package com.bobo.rpc2.client;

import com.bobo.rpc2.common.LifeLine;
import com.bobo.rpc2.transport.Client;

public interface RpcClient extends Client, LifeLine {
	<T> T getService(RpcConfig rpcConfig);
}
