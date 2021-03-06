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
import com.bobo.rpc2.transport.seria.SerializerSupport;

/**
 * @author bo.wang
 * @desc 回调方式rpc调用
 */
public class RemoteInvokeByAsync {

	private static Logger log = LoggerFactory.getLogger(RemoteInvokeBySync.class);

	public static void main(String[] args) throws IOException {
		RpcClient rpcClient = new DefaultRpcClient();
		rpcClient.start();

		RpcConfig config = RpcConfig.newConfigAsAsync("bobo.signService", ISignInService.class, 100, (response) -> {
			log.info("rpc invoke response:{}",
					SerializerSupport.deSerialize(response.getBody(), SignResult.class).getDesc());
		});
		ISignInService service = rpcClient.getService(config);
		try {
			service.signIn(new Worker(1, "bobo", "开发部"));
		} catch (InvokeException e) {
			log.error("rpc invoke failed", e);
		}
	}
}
