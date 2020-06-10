package com.bobo.rpc2.namesrv;

import com.bobo.rpc2.common.Stopable;
import com.bobo.rpc2.server.ServiceURI;
import com.bobo.rpc2.transport.Client;

public interface NamesrvClient extends Client, Stopable {
	void registerService(ServiceURI uri);

	ServiceURI lookupService(String serviceName);
}
