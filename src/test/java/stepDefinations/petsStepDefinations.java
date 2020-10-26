package stepDefinations;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONArray;
import org.junit.Assert;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class petsStepDefinations {
	
	private static Response response;
	private static String petStatusurl;
	private static String status;
	private Properties prop = new Properties();
	private Scenario scenerio= null;
	private RequestSpecification httpRequest;
	private String addPetObject;
	private String petID, petStatus;
	
	@Before
	public void setScenerios(Scenario scenerio) throws IOException
	{
		this.scenerio = scenerio;
		InputStream input = new FileInputStream("src/test/resources/config/config.properties");
		prop.load(input);
		httpRequest = RestAssured.given();
		RestAssured.defaultParser = Parser.JSON;
		httpRequest.contentType(ContentType.JSON);	
	}
	
	@Given("^Call petstatus api to find \"([^\\\"]*)\" pets in store$")
	public void creatingTheApi(String arg) throws Throwable {
		petStatusurl = prop.getProperty("url")+prop.getProperty("petStatusurlApi")+arg;
		scenerio.write("pet Status url: "+petStatusurl);
	}

	@When("^hit petstatus api$")
	public void hitPetstatus() throws Throwable {
		response = httpRequest.get(petStatusurl);
	}
	
	@Then("^verify the petstatus api response$")
	public void verifyPetStatus() throws Throwable {		
		
		JSONArray JSONResponseBody = new   JSONArray(response.body().asString());
		for(int i=0; i<JSONResponseBody.length(); i++)
		{
			status = JSONResponseBody.getJSONObject(i).getString("status");
			Assert.assertEquals("Status should be available but its not correct for ID: "+JSONResponseBody.getJSONObject(i).getInt("id") , "available", status);
		}
		Assert.assertEquals(200, response.getStatusCode());
	}

	@Given("^Call pet api to \"([^\\\"]*)\" pets of \"([^\\\"]*)\" id in store and make it \"([^\\\"]*)\"$")
	public void addPetApi(String pet,String id, String status) throws Throwable {
		petID = id;
		petStatus = status;
		petStatusurl = prop.getProperty("url");
		addPetObject ="{\"id\":"+petID+",\"category\":{\"id\":30,\"name\":\"Eq-dog\"},\"name\":\"Postman\",\"photoUrls\":[\"dog/doberman.png\"],\"tags\":[{\"id\":10,\"name\":\"Doberman\"}],\"status\":\""+petStatus+"\"}";
	}
	
	@When("^hit pet api to \"([^\\\"]*)\" pets$")
	public void hitAddPet(String operation) throws Throwable {
		httpRequest.body(addPetObject);
		if(operation.equalsIgnoreCase("add"))
		response = httpRequest.post(petStatusurl);
		else if(operation.equalsIgnoreCase("update"))
		response = httpRequest.put(petStatusurl);
		else if(operation.equalsIgnoreCase("delete"))
			response = httpRequest.delete(petStatusurl+"/"+petID);
	}
	
	@Then("^verify the pet api response and check its availabilty in petStatus api$")
	public void verifyAddPetResponse() throws Throwable {			
		Assert.assertEquals(200, response.getStatusCode());
		
		petStatusurl = prop.getProperty("url")+prop.getProperty("petStatusurlApi")+petStatus;
		response = httpRequest.get(petStatusurl);
		
		JSONArray JSONResponseBody = new   JSONArray(response.body().asString());
		for(int i=0; i<JSONResponseBody.length(); i++)
		{
			if(Integer.toString(JSONResponseBody.getJSONObject(i).getInt("id")).equalsIgnoreCase(petID))
			{
				System.out.println("Pet Successfully "+petStatus +" into store and "+petStatus+" now");
				scenerio.write("Pet Successfully "+petStatus +" into store and "+petStatus+" now");
			}
		}
	}
}
