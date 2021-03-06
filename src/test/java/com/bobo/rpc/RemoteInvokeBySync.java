package com.bobo.rpc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc.facade.ISignInService;
import com.bobo.rpc.facade.SignResult;
import com.bobo.rpc.facade.Worker;
import com.bobo.rpc2.client.DefaultRpcClient;
import com.bobo.rpc2.client.RpcClient;
import com.bobo.rpc2.client.RpcConfig;
import com.bobo.rpc2.common.exception.InvokeException;

/**
 * @author bo.wang
 * @desc 同步方式rpc调用
 */
public class RemoteInvokeBySync {

	private static Logger log = LoggerFactory.getLogger(RemoteInvokeBySync.class);

	public static void main(String[] args) throws IOException, InterruptedException {
		RpcClient rpcClient = new DefaultRpcClient();
		rpcClient.start();

		RpcConfig config = RpcConfig.newConfigAsSync("bobo.signService", ISignInService.class, 100);
		ISignInService service = rpcClient.getService(config);
		try {
			SignResult result = service.signIn(new Worker(1, "bobo", "开发部"));
			log.info("rpc invoke response:{}", result.getDesc());
		} catch (InvokeException e) {
			log.info("rpc invoke failed", e);
		}

	}
}
