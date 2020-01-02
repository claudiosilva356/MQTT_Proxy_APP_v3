package Test;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT_publish_test {
    static MqttClient client;
    static MemoryPersistence persistence = new MemoryPersistence();
    String clientId;
    String [] topics;
    
	public static void main(String[] args) throws MqttException, InterruptedException {
		// TODO Auto-generated method stub


	        client = new MqttClient("tcp://test.mosquitto.org:1883","F0B5D1A4A5040004",persistence);
	        MqttConnectOptions mqOptions=new MqttConnectOptions();
	        mqOptions.setCleanSession(true);
	        client.connect(mqOptions);
	        //client.subscribe("sensor/1"); //subscribing to the topic name  test/topic
	        
	        MqttMessage message = new MqttMessage("2".getBytes());
	        message.setQos(1);
	        while(true) {
	        client.publish("sensor",message);
	        Thread.sleep(1000);
	        }
        
	}

}
