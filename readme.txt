to run tests,follow the steps below:
1.run the main method of NamesrvStarter
2.run the main method of RpcServerStarter with vm arguements -Dnamesrv.host=localhost -Dnamesrv.port=8888
3.run the main method of RemoteInvokeBySync or RemoteInvokeByAsync with vm arguements -Dnamesrv.host=localhost -Dnamesrv.port=8888
	
At present, two remote invoke methods are provided, 
which are synchronous invoke and asynchronous invoke.
The asynchronous invoke is performed in a callback manner.