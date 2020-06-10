package com.bobo.rpc;

import com.bobo.rpc.facade.SignInService;
import com.bobo.rpc2.server.DefaultRpcServer;

public class RpcServerStarter {

	public static void main(String[] args) {
		DefaultRpcServer rpcServer = new DefaultRpcServer();
		rpcServer.start();
		rpcServer.registerService("bobo.signService", new SignInService());
	}
}
