package net.jfabricationgames.json_rpc;

public class JsonRpcResponse {
	
	private String jsonRpc;
	private Object result;
	private String id;
	
	public JsonRpcResponse() {
		//default constructor for JSON serialization
	}
	
	@Override
	public String toString() {
		return "JsonRpcResponse [jsonRpc=" + jsonRpc + ", result=" + result + ", id=" + id + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jsonRpc == null) ? 0 : jsonRpc.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
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
		JsonRpcResponse other = (JsonRpcResponse) obj;
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
		if (result == null) {
			if (other.result != null)
				return false;
		}
		else if (!result.equals(other.result))
			return false;
		return true;
	}
	
	public String getJsonRpc() {
		return jsonRpc;
	}
	public void setJsonRpc(String jsonRpc) {
		this.jsonRpc = jsonRpc;
	}
	
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}