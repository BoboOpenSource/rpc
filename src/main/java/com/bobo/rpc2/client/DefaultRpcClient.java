package com.bobo.rpc2.client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobo.rpc2.common.ConnectHelper;
import com.bobo.rpc2.namesrv.DefaultNamesrvClient;
import com.bobo.rpc2.server.ServiceURI;
import com.bobo.rpc2.transport.Transport;
import com.bobo.rpc2.transport.URI;
import com.bobo.rpc2.transport.URIType;
import com.bobo.rpc2.transport.codec.RemotingCommandCodec;
import com.bobo.rpc2.transport.handler.netty.ClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class DefaultRpcClient implements RpcClient {

	private static final Logger log = LoggerFactory.getLogger(DefaultRpcClient.class);

	private static final String NAMESRV_HOST = System.getProperty("namesrv.host");
	private static final int NAMESRV_PORT = Integer.parseInt(System.getProperty("namesrv.port"));

	private final DefaultNamesrvClient namesrvClient;
	private static Map<String, Transport> serviceTransportMap = new ConcurrentHashMap<>();

	public DefaultRpcClient() {
		this.namesrvClient = new DefaultNamesrvClient();
	}

	@Override
	public void start() {
		namesrvClient.connect(URI.createURI(URIType.TYPE_TCP, NAMESRV_HOST, NAMESRV_PORT));
	}

	@Override
	public <T> T getService(RpcConfig rpcConfig) {
		ServiceURI serviceURI = namesrvClient.lookupService(rpcConfig.getServiceName());
		return StubFactory.createStub(rpcConfig, getTransport(serviceURI));
	}

	@Override
	public Channel connect(URI uri) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		final Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_REUSEADDR, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConnectHelper.CONNECT_TIMEOUT)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ch.pipeline().addLast(new RemotingCommandCodec())
								.addLast(new IdleStateHandler(0, 0, ConnectHelper.IDLE_TIMEOUT))
								.addLast(new ClientHandler(bootstrap, uri, (newChannel) -> {
									if (newChannel == null) {
										removeTransport(uri);
									} else {
										resetChannel(uri, newChannel);
									}
								}));
					}
				});
		return ConnectHelper.connect(bootstrap, uri);
	}

	private Transport getTransport(URI uri) {
		String uriStr = uri.toUriStr();
		Transport transport = serviceTransportMap.get(uriStr);
		if (transport == null) {
			synchronized (uriStr) {
				if (serviceTransportMap.get(uriStr) == null) {
					transport = createTransport(uri);
					serviceTransportMap.put(uriStr, transport);
				}
			}
		}
		return transport;
	}

	private void removeTransport(URI uri) {
		serviceTransportMap.remove(uri.toUriStr());
	}

	private void resetChannel(URI uri, Channel channel) {
		log.info("reset transport channel,uri:{}", uri.toString());
		serviceTransportMap.get(uri.toUriStr()).setChannel(channel);
	}

	private Transport createTransport(URI uri) {
		return new Transport(connect(uri));
	}

	@Override
	public void close() throws IOException {
		if (namesrvClient != null) {
			namesrvClient.close();
		}
		serviceTransportMap.entrySet().forEach((entry) -> {
			entry.getValue().close();
		});
	}
}
