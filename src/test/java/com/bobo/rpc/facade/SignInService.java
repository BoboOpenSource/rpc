package com.bobo.rpc.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignInService implements ISignInService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public SignResult signIn(Worker worker) {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String desc = String.format("id:%d-dept:%s-name:%s:打卡完成", worker.getId(), worker.getDept(), worker.getName());
		log.info(desc);
		return new SignResult(desc);
	}
}
