package com.bobo.rpc;

import com.bobo.rpc2.namesrv.NamesrvStartup;

import junit.framework.TestCase;

public class RemoteInvokeTest extends TestCase {

	public static void main(String[] args) {

		new NamesrvStartup().start();

	}
}
