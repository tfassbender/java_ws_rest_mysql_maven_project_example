package net.jfabricationgames.json_rpc;

public enum JsonRpcErrorCode {
	
	UNKNOWN_ERROR(-10000),//
	LOGIN_ERROR(-10100),//
	UNEXPECTED_PARAMETERS_ERROR(-11000),//
	UNKNOWN_METHOD_ERROR(-12000),//
	METHOD_INVOKE_ERROR(-12100),//
	EXECUTION_ERROR(-13000);//
	
	private final int code;
	
	private JsonRpcErrorCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}