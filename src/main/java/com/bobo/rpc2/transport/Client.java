package com.bobo.rpc2.transport;

import java.io.Closeable;

import io.netty.channel.Channel;

public interface Client extends Closeable{
	Channel connect(URI uri);
}
