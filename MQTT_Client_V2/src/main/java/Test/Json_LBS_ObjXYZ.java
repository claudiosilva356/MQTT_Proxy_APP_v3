package Test;

import java.util.HashMap;

import com.google.gson.Gson;

//Default LBS JSON Structure
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

public class Json_LBS_ObjXYZ{

	public String tagId;
	public HashMap<String, HashMap<String,Object>> tagSensorData;
	public HashMap<String, Object> geographic;
	
	public Json_LBS_ObjXYZ() {
		tagId = "tagbeanair";
		
		tagSensorData = new HashMap<String, HashMap<String,Object>>();
		tagSensorData.put("temperature", (new HashMap<String, Object>()));
		tagSensorData.get("temperature").put("value",11.1);
		
		tagSensorData.put("pressure", (new HashMap<String, Object>()));
		tagSensorData.get("pressure").put("value",1024);
		
		tagSensorData.put("humidity", (new HashMap<String, Object>()));
		tagSensorData.get("humidity").put("value",25);
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this); 

	}
}
