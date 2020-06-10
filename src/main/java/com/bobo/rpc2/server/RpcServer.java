package com.bobo.rpc2.server;

import com.bobo.rpc2.common.LifeLine;

public interface RpcServer extends LifeLine {
	void registerService(String serviceName, Object o);
}
