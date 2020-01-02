package Test;

import java.util.HashMap;

import com.google.gson.Gson;

// Default LBS JSON Structure
//data_dict = {
//        "tagId": "taglaptop",
//        "tagSensorData": {
//            "batteryLevel": {"value": 8},
//            "temperature": {"value": 11.1},
//            "pressure": {"value": 1024},
//            "humidity": {"value": 25},
//            "power": {"value": 18}
//        },
//        "geographic": {
//            "techno": "GPS",
//            "lat": 38.7167,
//            "lng": -9.1333,
//            "address": "null"
//        }
//    }

public class Json_LBS_ObjLocation {

	public String tagId;
	public HashMap<String, HashMap<String,Object>> tagSensorData;
	public HashMap<String, Object> geographic;
	
	public Json_LBS_ObjLocation() {
		tagId = "tagbeanair";
		
		geographic = new HashMap<String, Object>();
		geographic.put("techno", "GPS");
		geographic.put("lat", "38.7167");
		geographic.put("lng", "9.1333");
		geographic.put("address", "null");
		
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this); 

	}
	
	
}
