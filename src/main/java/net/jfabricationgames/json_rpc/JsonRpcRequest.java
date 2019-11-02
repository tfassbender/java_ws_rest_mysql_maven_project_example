package net.jfabricationgames.json_rpc;

public class JsonRpcRequest {
	
	private String jsonRpc;
	private String method;
	private Object params;
	private String id;
	
	public JsonRpcRequest() {
		//default constructor for JSON serialization
	}
	
	@Override
	public String toString() {
		return "JsonRpcRequest [jsonRpc=" + jsonRpc + ", method=" + method + ", params=" + params + ", id=" + id + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jsonRpc == null) ? 0 : jsonRpc.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
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
		JsonRpcRequest other = (JsonRpcRequest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (jsonRpc == null) {
			if (other.jsonRpc != null)
				return false;
		}
		else if (!jsonRpc.equals(other.jsonRpc))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		}
		else if (!method.equals(other.method))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		}
		else if (!params.equals(other.params))
			return false;
		return true;
	}
	
	public String getJsonRpc() {
		return jsonRpc;
	}
	public void setJsonRpc(String jsonRpc) {
		this.jsonRpc = jsonRpc;
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}