package LBS_PC_Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javafx.application.Application;

@SuppressWarnings("restriction")
public class LaptopLBS_v2 {

	static String API_TOKEN;
	static String LBS_SERVER_URL;
	static String tagId;
	static String serialNumber;
	static String username;
	static String[] ssidInfo;
	static String ssid;
	static String bssid;
	static String ssidState;
	static String lat = "";
	static String lng = "";

	static boolean STARTED;

	public static void main( String[] args ) throws  InterruptedException, FileNotFoundException {		
		API_TOKEN = "7c5d7e81eb85ca";
		LBS_SERVER_URL = "https://locationbasedservices.lab.acs.altran.fr/app-web/client-handling/rest/data"; 
		tagId = "taglaptop";
		serialNumber = LaptopInfo.getSerialNumber();
		username = LaptopInfo.getUsername();
		ssidInfo = LaptopInfo.getSsidInfo();
		ssid = ssidInfo[0];
		bssid = ssidInfo[1];
		ssidState = ssidInfo[2];
		STARTED = false;
		//setLocation();
		Application.launch(LaptopLBSGui.class, args);
	}
		
	public static void postToLBS(String token, String LBS_URL, String tag, String latitude, String longitude) {
		API_TOKEN = token;
		LBS_SERVER_URL = LBS_URL;
		tagId = tag;
		lat = latitude;
		lng = longitude;
		try {
			URL url = new URL (LBS_SERVER_URL);
			HttpURLConnection con;
			
			while(STARTED) {
				con = (HttpURLConnection)url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
				
				try(OutputStream os = con.getOutputStream()) {
					byte[] input = getLaptopData().getBytes("utf-8");
					os.write(input, 0, input.length);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try(BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"))) {
					StringBuilder response = new StringBuilder();
					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
						response.append(responseLine.trim());
					}
					System.out.println(response.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Thread.sleep(1000);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) { }
	}

	private static String getLaptopData() {		
		LaptopJSON laptopLBS_JSON = new LaptopJSON();
		
		Date dt = Calendar.getInstance().getTime();
		laptopLBS_JSON.getTagSensorData().get("temperature").put("value", dt.getHours());
		laptopLBS_JSON.getTagSensorData().get("pressure").put("value", dt.getMinutes());
		laptopLBS_JSON.getTagSensorData().get("humidity").put("value", dt.getSeconds());
		
		laptopLBS_JSON.getGeographic().put("lat", lat);
		laptopLBS_JSON.getGeographic().put("lng", lng);
		
		System.out.println(laptopLBS_JSON);
		return laptopLBS_JSON.toString();
	}
	
	public static void setLocation(String API_TOKEN) {
		if(lat.equals("") && lng.contentEquals("")) {
			System.out.println("pedi localização");
			String[] loc = LaptopInfo.getLocation(API_TOKEN);
			if(loc[0]!=null) {
				lat = loc[0];
				lng = loc[1];
			}
		}
	}
}
