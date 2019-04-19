package test.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TestWS {
	
	public static void main(String[] args) throws IOException {
		StringBuilder result = new StringBuilder();
		URL url = new URL("http://services.groupkt.com/country/get/iso2code/IN");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      
	      String jsonRes = result.toString();
	      Gson gson = new Gson();
	      JsonObject json = gson.fromJson(jsonRes, JsonObject.class);
	      JsonElement jsonEle = json.get("RestResponse").getAsJsonObject().get("result").getAsJsonObject().get("alpha3_code");
	      String finalResult = jsonEle.getAsString();
//	      System.out.println(finalResult);
	      
	}

}
