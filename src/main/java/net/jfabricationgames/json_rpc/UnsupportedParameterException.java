package net.jfabricationgames.json_rpc;

public class UnsupportedParameterException extends Exception {
	
	private static final long serialVersionUID = -7246131768926016190L;
	
	public UnsupportedParameterException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnsupportedParameterException(String message) {
		super(message);
	}
}