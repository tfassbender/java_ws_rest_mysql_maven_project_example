package net.jfabricationgames.json_rpc;

import java.util.Map;

/**
 * Error codes:
 *
 * Ranges Types
 * 
 * error -10000 to -19999 <br>
 * warning 10000 to 19999 <br>
 * info 20000 to 29999
 * 
 * Groups
 * 
 * within every range there are some areas
 * 
 * 0-999 Global <br>
 * 1000-1499 Initialization <br>
 * 1500-1999 Order Picking
 * 
 */
public class JsonRpcError {
	
	private int code;
	private String message;
	private Object data;
	
	public static final JsonRpcError OK = new JsonRpcError(0, "", null);
	
	public JsonRpcError() {
		//default constructor for JSON serialization
	}
	
	public JsonRpcError(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public JsonRpcError(JsonRpcErrorCode code, String message, Object data) {
		this(code.getCode(), message, data);
	}
	
	@Override
	public String toString() {
		return "JsonRpcError [code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonRpcError other = (JsonRpcError) obj;
		if (code != other.code)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		}
		else if (!data.equals(other.data))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		}
		else if (!message.equals(other.message))
			return false;
		return true;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public static JsonRpcError fromParameters(Object params) {
		if (params == null) {
			return null;
		}
		
		//assume the params Object is a Map, because it's deserialized this way
		@SuppressWarnings("unchecked")
		Map<String, Object> parameterMap = (Map<String, Object>) params;
		
		JsonRpcError error = new JsonRpcError();
		error.setCode((Integer) parameterMap.get("code"));
		error.setMessage((String) parameterMap.get("message"));
		error.setData(parameterMap.get("data"));
		return error;
	}
}