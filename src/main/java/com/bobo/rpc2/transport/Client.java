package com.bobo.rpc2.transport;

import io.netty.channel.Channel;

public interface Client {
	Channel connect(URI uri);
}
