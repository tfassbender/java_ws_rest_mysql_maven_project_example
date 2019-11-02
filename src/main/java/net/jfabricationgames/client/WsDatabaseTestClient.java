package net.jfabricationgames.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.jfabricationgames.json_rpc.JsonRpcErrorResponse;
import net.jfabricationgames.json_rpc.JsonRpcRequest;
import net.jfabricationgames.json_rpc.JsonRpcResponse;
import net.jfabricationgames.ws_db_test.DatabaseService;

public class WsDatabaseTestClient {
	
	private static final String SERVER_URI = "http://localhost:8080/";
	private static final String RESOURCE_PATH = "WsDatabaseTest/db_test/db_test/";
	
	private static int id;
	
	private static boolean debug;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		new WsDatabaseTestClient();
	}
	
	public WsDatabaseTestClient() throws IOException, InterruptedException {
		try (InputStreamReader isr = new InputStreamReader(System.in);//
				BufferedReader reader = new BufferedReader(isr)) {
			boolean stop = false;
			do {
				System.out.println("Options:\n(1)       Show all IDs\n(2)       Add entry\n(3)       Show entry\n(exit)    to exit the programm\n"
						+ "(debug)   to enable debugging mode");
				System.out.print("\nPlease choose: ");
				String in = reader.readLine();
				
				if (in.equals("exit")) {
					stop = true;
				}
				else if (in.equals("debug")) {
					debug = true;
					System.out.println("Debug mode enabled\n");
					Thread.sleep(1000);
				}
				else {
					try {
						int option = Integer.parseInt(in);
						switch (option) {
							case 1:
								showAllIds();
								break;
							case 2:
								addEntry(reader);
								break;
							case 3:
								getEntry(reader);
								break;
							default:
								System.out.println("Invalid option: " + option + "\nPlease try again.");
								break;
						}
						
						Thread.sleep(2000);
					}
					catch (NumberFormatException nfe) {
						System.out.println("Input could not be interpreted. Please try again.");
					}
				}
			} while (!stop);
		}
	}
	
	private String getId() {
		int currentId = id;
		id++;
		return Integer.toString(currentId);
	}
	
	private void showAllIds() {
		JsonRpcRequest request = buildGenericRequest();
		request.setMethod("getAllIds");
		request.setParams(null);
		
		Response response = sendRequest(request);
		printContent(response);
	}
	
	private void addEntry(BufferedReader reader) throws IOException {
		System.out.print("Insert entry text: ");
		String entry = reader.readLine();
		
		JsonRpcRequest request = buildGenericRequest();
		request.setMethod("addEntry");
		request.setParams(entry);
		
		Response response = sendRequest(request);
		printContent(response);
	}
	
	private void getEntry(BufferedReader reader) throws IOException {
		System.out.print("Insert entry id: ");
		String entry = reader.readLine();
		
		int entryId;
		try {
			entryId = Integer.parseInt(entry);			
		}
		catch (NumberFormatException nfe) {
			System.out.println("[ERROR] The id has to be an integer.");
			return;
		}
		
		JsonRpcRequest request = buildGenericRequest();
		request.setMethod("getEntry");
		request.setParams(entryId);
		
		Response response = sendRequest(request);
		printContent(response);
	}
	
	/**
	 * Get a JsonRpcResponse from a Response object. (Deserializes JSON)
	 */
	private JsonRpcResponse getJsonRpcResponse(String responseText) throws IllegalStateException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			//"manually" parse JSON to Object
			JsonRpcResponse resp = mapper.readValue(responseText, JsonRpcResponse.class);
			return resp;
		}
		catch (IOException e) {
			//e.printStackTrace();
			throw new IllegalStateException("The response could not be read or parsed: " + responseText);
		}
	}
	/**
	 * Get a JsonRpcErrorResponse from a Response object. (Deserializes JSON)
	 */
	private JsonRpcErrorResponse getJsonRpcErrorResponse(String responseText) throws IllegalStateException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			//"manually" parse JSON to Object
			JsonRpcErrorResponse resp = mapper.readValue(responseText, JsonRpcErrorResponse.class);
			return resp;
		}
		catch (IOException e) {
			//e.printStackTrace();
			throw new IllegalStateException("The response could not be read or parsed: " + responseText);
		}
	}
	
	private Response sendRequest(JsonRpcRequest request) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(SERVER_URI).path(RESOURCE_PATH);
		
		//convert to JSON
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json;
		try {
			json = ow.writeValueAsString(request);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
		
		Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(json, MediaType.APPLICATION_JSON));
		int responseCode = response.getStatus();
		
		if (debug) {
			System.out.println("[DEBUG] Response code: " + responseCode);
			System.out.println("[DEBUG] Response entity:\n" + response.getEntity());
		}
		
		//check whether the response was OK or an error code
		if (responseCode != Response.Status.OK.getStatusCode()) {
			throw new IllegalStateException("HTTP error code: " + responseCode);
		}
		else if (response.hasEntity()) {
			return response;
		}
		else {
			throw new IllegalStateException("The response was expected to contain data, but it's empty");
		}
	}
	
	private void printContent(Response response) {
		String responseText = response.readEntity(String.class);
		try {
			//try to parse the response as JsonRpcResponse
			JsonRpcResponse content = getJsonRpcResponse(responseText);
			
			System.out.println("Response from server:\n" + content.getResult());
			System.out.println("\n");
		}
		catch (IllegalStateException ise) {
			try {
				//if the response is no JsonRpcResponse try to parse it as JsonRpcErrorResponse
				JsonRpcErrorResponse error = getJsonRpcErrorResponse(responseText);
				
				System.out.println("[ERROR] Server responded with an error:\n" + error.getError());
				System.out.println("\n");
			}
			catch (IllegalStateException ise2) {
				ise2.printStackTrace();
			}
		}
	}
	
	private JsonRpcRequest buildGenericRequest() {
		JsonRpcRequest request = new JsonRpcRequest();
		request.setId(getId());
		request.setJsonRpc(DatabaseService.JSON_RPC);
		return request;
	}
}