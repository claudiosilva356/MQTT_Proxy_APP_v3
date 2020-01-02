package LBS_PC_Client;

import java.util.HashMap;

import com.google.gson.Gson;

public class LaptopJSON {

	private String tagId;
	private HashMap<String, HashMap<String, Object>> tagSensorData;
	private HashMap<String, Object> geographic;
	
	public LaptopJSON() {
		tagId = "taglaptop";
		
		tagSensorData = new HashMap<String, HashMap<String, Object>>();
		
		tagSensorData.put("temperature", (new HashMap<String, Object>()));		
		tagSensorData.put("pressure", (new HashMap<String, Object>()));
		tagSensorData.put("humidity", (new HashMap<String, Object>()));
		
		geographic = new HashMap<String, Object>();
		
		geographic.put("techno", "GPS");
		geographic.put("lat", 0);
		geographic.put("lng", 0);
		geographic.put("address", null);
	}
		
	public String getTagId() {
		return tagId;
	}
	
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	public HashMap<String, HashMap<String, Object>> getTagSensorData() {
		return tagSensorData;
	}
	
	public void setTagSensorData(HashMap<String, HashMap<String, Object>> tagSensorData) {
		this.tagSensorData = tagSensorData;
	}
	
	public HashMap<String, Object> getGeographic() {
		return geographic;
	}
	
	public void setGeographic(HashMap<String, Object> geographic) {
		this.geographic = geographic;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this); 
	}	
}
