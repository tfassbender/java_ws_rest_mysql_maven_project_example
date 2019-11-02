package net.jfabricationgames.json_rpc;

public class JsonRpcErrorResponse {
	
	private String jsonRpc;
	private Object error;
	private String id;
	
	public JsonRpcErrorResponse() {
		//default constructor for JSON serialization
	}
	
	@Override
	public String toString() {
		return "JsonRpcErrorResponse [jsonRpc=" + jsonRpc + ", error=" + error + ", id=" + id + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jsonRpc == null) ? 0 : jsonRpc.hashCode());
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
		JsonRpcErrorResponse other = (JsonRpcErrorResponse) obj;
		if (error == null) {
			if (other.error != null)
				return false;
		}
		else if (!error.equals(other.error))
			return false;
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
		return true;
	}
	
	public String getJsonRpc() {
		return jsonRpc;
	}
	public void setJsonRpc(String jsonRpc) {
		this.jsonRpc = jsonRpc;
	}
	
	public Object getError() {
		return error;
	}
	public void setError(Object error) {
		this.error = error;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
