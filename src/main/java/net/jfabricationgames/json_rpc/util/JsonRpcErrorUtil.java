package net.jfabricationgames.json_rpc.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.jfabricationgames.json_rpc.JsonRpcError;
import net.jfabricationgames.json_rpc.JsonRpcErrorCode;
import net.jfabricationgames.json_rpc.JsonRpcErrorResponse;
import net.jfabricationgames.ws_db_test.DatabaseService;

/**
 * Creates errors that are often used
 */
public abstract class JsonRpcErrorUtil {
	
	/**
	 * Create a default error response (HTTP code is 200, but the returned JsonRpcResponse contains a JsonRpcError with an error-code of -10000)
	 */
	public static Response createErrorResponse(String id) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(JsonRpcErrorCode.UNKNOWN_ERROR, "Unknown error occured", null));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create a response that informs about problems on login request
	 */
	public static Response createLoginErrorResponse(String id) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(JsonRpcErrorCode.LOGIN_ERROR, "Login was not successful", null));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create a response that informs about illegal or unexpected parameters in a request
	 */
	public static Response createIllegalParameterErrorResponse(String id, Object parameters) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(JsonRpcErrorCode.UNEXPECTED_PARAMETERS_ERROR, "Unexpected parameters in request", parameters));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create a response that informs that the requested method (the method parameter in the request) is unknown
	 */
	public static Response createMethodNotFoundErrorResponse(String id, String methodName) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(JsonRpcErrorCode.UNKNOWN_METHOD_ERROR, "Unknown method", methodName));
		
		return Response.status(Status.OK).entity(response).build();
	}
	/**
	 * Create a response that informs that the requested method (the method parameter in the request) is unknown
	 */
	public static Response createMethodCouldNotBeInvocedErrorResponse(String id, String methodName) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(JsonRpcErrorCode.METHOD_INVOKE_ERROR, "Method could not be invoked", methodName));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create a response that informs that the requested method (the method parameter in the request) is unknown
	 */
	public static Response createExecutionErrorResponse(String id, String methodName) {
		JsonRpcErrorResponse response = createEmptyErrorResponse(id);
		response.setError(new JsonRpcError(JsonRpcErrorCode.EXECUTION_ERROR, "Error while executing the request", methodName));
		
		return Response.status(Status.OK).entity(response).build();
	}
	
	/**
	 * Create an empty response with only an id and the default jsonRpc fields set
	 */
	private static JsonRpcErrorResponse createEmptyErrorResponse(String id) {
		JsonRpcErrorResponse response = new JsonRpcErrorResponse();
		response.setId(id);
		response.setJsonRpc(DatabaseService.JSON_RPC);
		return response;
	}
}