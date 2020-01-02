package MQTT_Client_V4;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import Test.Json_LBS_ObjLocation;
import Test.Json_LBS_ObjXYZ;
import eu.hansolo.medusa.Gauge;
import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

import java.nio.ByteBuffer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class MQTT_Listenner implements IMqttMessageListener, MqttCallback {
	MqttClient client;
	String tagId;
	String sendLocation;
	String topicX;
	String topicY;
	String topicZ;
	String valueX;
	String valueY;
	String valueZ;
	int count= 0;
	String LBSUrl;
	String OtherUrl;
	boolean LocationSent;
	String Lat;
	String Lng;
	Gauge gaugeX;
	Gauge gaugeY;
	Gauge gaugeZ;
	String host;
	String ClientId;
	String[] topics;
	String dataType;
	double broker_value;

	//inicialize some variables
	public MQTT_Listenner(String host,String ClientId, String[] topics,String tagId,String sendLocation, String Lat, String Lng, String LBSUrl, String OtherUrl, String dataType){
		//String topicArr[] = {topicX, topicY, topicZ};
    	this.host = host;
    	this.ClientId = ClientId;
        this.tagId = tagId;
        this.topics = topics;
        this.topicX = topics[0];
        this.topicY = topics[1];
        this.topicZ = topics[2];
        this.LBSUrl = LBSUrl;
        this.OtherUrl = OtherUrl;
        this.sendLocation = sendLocation;
        this.LocationSent = false;
        this.Lat = Lat;
        this.Lng = Lng;
        this.dataType = dataType;
    }
	
	public void InitGauges(Gauge gaugeX,Gauge gaugeY,Gauge gaugeZ) {
        this.gaugeX = gaugeX;
        this.gaugeY = gaugeY;
        this.gaugeZ = gaugeZ;
	}
	
	public void Connect()  throws MqttException  {
		client = new MqttClient(host,ClientId);
        client.setCallback(this);
        MqttConnectOptions mqOptions=new MqttConnectOptions();
        mqOptions.setCleanSession(false);
        client.connect(mqOptions);      //connecting to broker 
        client.subscribe(this.topics); //subscribing to the topic name  test/topic
	}

    public void messageArrived(String topic, MqttMessage mm) {
        System.out.println("Mensagem recebida:");
        System.out.println("\tTópico: " + topic);
        System.out.println("\tMensagem: " + new String(mm.getPayload()));
        byte arrByte [] = mm.getPayload();

        
       try {
        
    	if(dataType.equals("BeanAir Device")){ //BeanAir Frame Type
	    	//parse the sensor value
	        broker_value = ((double)Long.parseLong(String.format("%02x", arrByte[8])+String.format("%02x", arrByte[7]), 16))/1000;
	    	if(String.valueOf(arrByte[9]).equals("-128"))
	        	broker_value = broker_value*-1;
	        
    	}else{ //Sensor Value sent directly to broker, no parse needed


    		broker_value = Double.parseDouble(new String(mm.getPayload()));
    		
    	}
        //keep the data from each sensor before send it
        if(topic.equals(topicX)) {
		        this.valueX = broker_value+"";
		        this.gaugeX.setValue(broker_value);
        }
	        if(topic.equals(topicY)) {
	        	this.valueY = broker_value+"";
	        	 this.gaugeY.setValue(broker_value);
	        }
	        if(topic.equals(topicZ)) {
	        	this.valueZ = broker_value+"";
	        	 this.gaugeZ.setValue(broker_value);
	       	}
	        ++count;
	        
	        //if we already have data from 3 axis send to LBS
	        if(count == 3) {
		        count = 0;
		        
		        //Contruct Json Object for update sensor data
		        Json_LBS_ObjXYZ JsonObjXYZ = new Json_LBS_ObjXYZ();
		        ((Json_LBS_ObjXYZ)JsonObjXYZ).tagId = this.tagId;
		        ((Json_LBS_ObjXYZ)JsonObjXYZ).tagSensorData.get("temperature").put("value",valueX);
		        ((Json_LBS_ObjXYZ)JsonObjXYZ).tagSensorData.get("pressure").put("value",valueY);
		        ((Json_LBS_ObjXYZ)JsonObjXYZ).tagSensorData.get("humidity").put("value",valueZ);
		        System.out.println("JsonObjXYZ Sent: "+JsonObjXYZ);
		    	
		    	//Contruct Json Object for Geolocation
		    	Json_LBS_ObjLocation JsonObjGeo = new Json_LBS_ObjLocation();
		    	 if(sendLocation.equals("Yes")) { //get location from ipInfo
		 	        IPInfo ipInfo = IPInfo.builder().setToken("56eae7ddf20576").build();
		 	        IPResponse ipInforesponse = ipInfo.lookupIP("");
		 	       ((Json_LBS_ObjLocation)JsonObjGeo).geographic.put("lat", ipInforesponse.getLatitude());
		 	       ((Json_LBS_ObjLocation)JsonObjGeo).geographic.put("lng", ipInforesponse.getLongitude());
		         }
		    	 if(sendLocation.equals("Send Manually")) { //get location from ipInfo
			 	        IPInfo ipInfo = IPInfo.builder().setToken("56eae7ddf20576").build();
			 	        IPResponse ipInforesponse = ipInfo.lookupIP("");
			 	       ((Json_LBS_ObjLocation)JsonObjGeo).geographic.put("lat", this.Lat);
			 	       ((Json_LBS_ObjLocation)JsonObjGeo).geographic.put("lng", this.Lng);
			      }
			    	
		    	 //Set HTTP connection and send the Json sensor data Object
			    	HttpClient httpClient = HttpClientBuilder.create().build();
			    	HttpPost post = new HttpPost(LBSUrl);
			    	StringEntity postingString = new StringEntity(JsonObjXYZ.toString());//gson.tojson() converts your pojo to json
			    	post.setEntity(postingString);
			    	post.setHeader("Content-type", "application/json");
			    	HttpResponse  response = httpClient.execute(post);
			    	System.out.println("Http Response Sent: "+response.toString());
			    	
			    	//Send Json Object to "Other" URL if different blank 
			    	if(!OtherUrl.equals("")) {
			    		HttpClient httpClient_ = HttpClientBuilder.create().build();
				    	HttpPost post_ = new HttpPost(OtherUrl);
				    	StringEntity postingString_ = new StringEntity(JsonObjXYZ.toString());//gson.tojson() converts your pojo to json
				    	post.setEntity(postingString_);
				    	post.setHeader("Content-type", "application/json");
				    	HttpResponse  response_ = httpClient_.execute(post_);	
			    		System.out.println("Http Response Sent: "+response_.toString());
			    	}
			    	
			    	
			    	//send Json Object location if necessary and not sent yet 
			    	//This object will only be sent once during the program
			    	if(!LocationSent && sendLocation.equals("Yes")) {
			    		postingString = new StringEntity(JsonObjGeo.toString());
			    		post.setEntity(postingString);
			    		response = httpClient.execute(post);
			    		LocationSent = true;
			    	}else if(!LocationSent && sendLocation.equals("Send Manually")) {
			    		postingString = new StringEntity(JsonObjGeo.toString());
			    		post.setEntity(postingString);
			    		response = httpClient.execute(post);
			    		LocationSent = true;
			    	}
	    	// }
	    	
        }
    	
       }catch(Exception Ie) {
    	   Ie.printStackTrace();

       }
    }

	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
}