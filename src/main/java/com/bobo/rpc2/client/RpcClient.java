package com.bobo.rpc2.client;

import com.bobo.rpc2.common.Startup;
import com.bobo.rpc2.transport.Client;

public interface RpcClient extends Client,Startup{
	<T> T getService(RpcConfig rpcConfig);
}
