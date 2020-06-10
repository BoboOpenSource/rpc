package com.bobo.rpc;

import com.bobo.rpc2.namesrv.NamesrvServer;

public class NamesrvStarter {

	public static void main(String[] args) {
		new NamesrvServer().start();
	}
}
