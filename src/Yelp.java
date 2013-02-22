/*
Example code based on code from Nicholas Smith at http://imnes.blogspot.com/2011/01/how-to-use-yelp-v2-from-java-including.html
For a more complete example (how to integrate with GSON, etc) see the blog post above.
*/

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
* Example for accessing the Yelp API.
*/
public class Yelp {

  OAuthService service;
  Token accessToken;

  /**
* Setup the Yelp API OAuth credentials.
*
* OAuth credentials are available from the developer site, under Manage API access (version 2 API).
*
* @param consumerKey Consumer key
* @param consumerSecret Consumer secret
* @param token Token
* @param tokenSecret Token secret
*/
  public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
  }

  /**
* Search with term and location.
*
* @param term Search term
* @param latitude Latitude
* @param longitude Longitude
* @return JSON string response
*/
/*  public String search(String term, double latitude, double longitude) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    request.addQuerystringParameter("limit", "2");
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }*/
  
  public String search(String cuisine, String term,  String location) {
	    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
	    request.addQuerystringParameter("term", cuisine + " " + term);
	    request.addQuerystringParameter("location", location);
	    request.addQuerystringParameter("limit", "5");
	    this.service.signRequest(this.accessToken, request);
	    Response response = request.send();
	    return response.getBody();
	  }

  // CLI
  public static void main(String[] args) {
    // Update tokens here from Yelp developers site, Manage API access.
    String consumerKey = "0Zn9dxuhMUyXoNB5WErI1w";
    String consumerSecret = "nMYqpY1ZqSelxzGCT69WnoswNjU";
    String token = "BVSsPFNspS-GWA3OmeeDreIGqK-KTFST";
    String tokenSecret = "TijS_myGG1-EIlzNmY8LOxepETI";

    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
    // String response = yelp.search("Chinese Restaurant", 30.361471, -87.164326);
    String response = yelp.search("French", "Restaurant", "Orlando Fl");

    System.out.println(response);
    try{
    	JSONObject mainJson = new JSONObject(response);
    	JSONArray businesses = mainJson.getJSONArray("businesses");
    	for (int i = 0; i < businesses.length(); i++) {
    		JSONObject business = businesses.getJSONObject(i);
    		System.out.println("name: " + business.getString("name"));
    		System.out.println("phone: " + business.getString("phone"));
    		JSONArray address = business.getJSONObject("location").getJSONArray("display_address");
    		String address_string = "";
    		for (int j=0; j < address.length(); j++)
    			address_string = address_string + address.getString(j) + " ";
    		System.out.println("address: " + address_string);
    		System.out.println();
    	}	
    }
    catch(Exception e){System.out.println(e);}
  }
}
