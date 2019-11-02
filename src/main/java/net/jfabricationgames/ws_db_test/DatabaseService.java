package net.jfabricationgames.ws_db_test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.jfabricationgames.db.DatabaseConnection;
import net.jfabricationgames.json_rpc.JsonRpcError;
import net.jfabricationgames.json_rpc.JsonRpcErrorCode;
import net.jfabricationgames.json_rpc.JsonRpcErrorResponse;
import net.jfabricationgames.json_rpc.JsonRpcRequest;
import net.jfabricationgames.json_rpc.JsonRpcResponse;
import net.jfabricationgames.json_rpc.UnsupportedParameterException;
import net.jfabricationgames.json_rpc.util.JsonRpcErrorUtil;

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
			int id;
			try {
				id = db.addEntry(entry);
			}
			catch (SQLException sqle) {
				sqle.printStackTrace();
				return createErrorResponseFromException(sqle);
			}
			
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
			String entry;
			try {
				entry = db.getEntry(idInt);
			}
			catch (SQLException sqle) {
				sqle.printStackTrace();
				return createErrorResponseFromException(sqle);
			}
			
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
	
	@GET
	@Path("/get_all_ids")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllIds() {
		DatabaseConnection db = DatabaseConnection.getInstance();
		List<Integer> allIds;
		try {
			allIds = db.getIds();
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			return createErrorResponseFromException(sqle);
		}
		
		JsonRpcResponse response = new JsonRpcResponse();
		response.setId("42");
		response.setJsonRpc(JSON_RPC);
		response.setResult(allIds);
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callJsonRpc(JsonRpcRequest request) {
		return processRequest(request);
	}
	
	/**
	 * Process any request by delegating it to the method that is to be called.
	 * 
	 * @return Returns a {@link Response} to the processed request.
	 */
	private Response processRequest(JsonRpcRequest request) {
		//execute the requested method via reflection
		try {
			DatabaseTestServiceProvider provider = new DatabaseTestServiceProvider();
			Class<?> clazz = provider.getClass();
			Method method = clazz.getMethod(request.getMethod(), Object.class);
			Object obj = method.invoke(provider, request.getParams());
			
			JsonRpcResponse rpcResponse = new JsonRpcResponse();
			rpcResponse.setId(request.getId());
			rpcResponse.setJsonRpc(JSON_RPC);
			rpcResponse.setResult(obj);
			
			//build the response and send it back to the client
			Response response = Response.status(Status.OK).entity(rpcResponse).build();
			return response;
		}
		catch (NoSuchMethodException | SecurityException e) {
			return JsonRpcErrorUtil.createMethodNotFoundErrorResponse(request.getId(), request.getMethod());
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if (e.getCause() instanceof UnsupportedParameterException) {
				return JsonRpcErrorUtil.createIllegalParameterErrorResponse(request.getId(), request.getParams());
			}
			else {
				return JsonRpcErrorUtil.createMethodCouldNotBeInvocedErrorResponse(request.getId(), request.getMethod());	
			}			
		}
	}
	
	private Response createErrorResponseFromException(Throwable throwable) {
		return createErrorResponseFromException(throwable, JsonRpcErrorCode.UNKNOWN_ERROR);
	}
	private Response createErrorResponseFromException(Throwable throwable, JsonRpcErrorCode errorCode) {
		JsonRpcErrorResponse error = createErrorResponse(throwable.getMessage(), errorCode);
		return Response.status(Status.OK).entity(error).build();
	}

	private JsonRpcErrorResponse createErrorResponse(String message) {
		return createErrorResponse(message, JsonRpcErrorCode.UNKNOWN_ERROR);
	}
	private JsonRpcErrorResponse createErrorResponse(String message, JsonRpcErrorCode errorCode) {
		JsonRpcError error = new JsonRpcError(errorCode, message, null);
		JsonRpcErrorResponse response = new JsonRpcErrorResponse();
		response.setError(error);
		response.setJsonRpc(JSON_RPC);
		return response;
	}
}