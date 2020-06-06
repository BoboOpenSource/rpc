package com.bobo.rpc2.common;

import io.netty.channel.Channel;

public interface ReconnectListener {

	void notify(Channel newChannel);
}
