package net.jfabricationgames.ws_db_test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.jfabricationgames.db.DatabaseConnection;
import net.jfabricationgames.json_rpc.JsonRpcError;
import net.jfabricationgames.json_rpc.JsonRpcErrorCodes;
import net.jfabricationgames.json_rpc.JsonRpcErrorResponse;
import net.jfabricationgames.json_rpc.JsonRpcResponse;

@Path("/db_test")
public class DatabaseService {
	
	public static final String JSON_RPC = "2.0";
	
	@GET
	@Path("/hello")
	@Produces(MediaType.APPLICATION_JSON)
	public Response processHelloRequestGet() {
		String answer = "Hello from Database-Test service!";
		JsonRpcResponse rpcResponse = new JsonRpcResponse();
		rpcResponse.setId("42");
		rpcResponse.setJsonRpc(JSON_RPC);
		rpcResponse.setResult(answer);
		return Response.status(Status.OK).entity(rpcResponse).build();
	}
	
	@GET
	@Path("/add_entry/{entry}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEntry(@PathParam("entry") String entry) {
		if (entry != null) {
			DatabaseConnection db = DatabaseConnection.getInstance();
			int id = db.addEntry(entry);
			
			JsonRpcResponse response = new JsonRpcResponse();
			response.setId("42");
			response.setJsonRpc(JSON_RPC);
			response.setResult(id);
			
			return Response.status(Status.OK).entity(response).build();
		}
		else {
			JsonRpcErrorResponse error = createErrorResponse("no entry text defined");
			return Response.status(Status.OK).entity(error).build();
		}
	}
	
	@GET
	@Path("/get_entry/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntry(@PathParam("id") String id) {
		if (id != null) {
			int idInt = Integer.parseInt(id);
			DatabaseConnection db = DatabaseConnection.getInstance();
			String entry = db.getEntry(idInt);
			
			JsonRpcResponse response = new JsonRpcResponse();
			response.setId("42");
			response.setJsonRpc(JSON_RPC);
			response.setResult(entry);
			
			return Response.status(Status.OK).entity(response).build();
		}
		else {
			JsonRpcErrorResponse error = createErrorResponse("no id defined");
			return Response.status(Status.OK).entity(error).build();
		}
	}
	
	private JsonRpcErrorResponse createErrorResponse(String string) {
		JsonRpcError error = new JsonRpcError(JsonRpcErrorCodes.UNKNOWN_ERROR, string, null);
		JsonRpcErrorResponse response = new JsonRpcErrorResponse();
		response.setError(error);
		response.setJsonRpc(JSON_RPC);
		return null;
	}
}