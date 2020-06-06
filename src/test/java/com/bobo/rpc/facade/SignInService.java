package com.bobo.rpc.facade;

public class SignInService implements ISignInService {

	@Override
	public SignResult signIn(Worker worker) {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String desc = String.format("id:%d-dept:%s-name:%s:打卡完成", worker.getId(), worker.getDept(), worker.getName());
		System.out.println(desc);
		return new SignResult(desc);
	}
}
