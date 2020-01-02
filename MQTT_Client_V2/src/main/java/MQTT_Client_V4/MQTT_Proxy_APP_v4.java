package MQTT_Client_V4;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import com.google.gson.JsonObject;

import javafx.event.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.GaugeDesign.GaugeBackground;
import eu.hansolo.medusa.skins.GaugeSkin;
import eu.hansolo.medusa.Gauge;

import javafx.fxml.FXML;

@SuppressWarnings("restriction")
public class MQTT_Proxy_APP_v4 extends Application{ 
	//Control variables
	String broker = "tcp://test.mosquitto.org:1883"; 
	String LBSUrl = "https://locationbasedservices.lab.acs.altran.fr/app-web/client-handling/rest/data";
	String OtherUrl = "";
	String ClientId = "F0B5D1A4A5040000";
	String topicNameX = "F0B5D1A4A5040000/SENSOR/1";
	String topicNameY = "F0B5D1A4A5040000/SENSOR/2";
	String topicNameZ = "F0B5D1A4A5040000/SENSOR/0";
	String LocationLat = "";
	String LocationLng = "";
	
	//Gui Components
	Scene scene = new Scene(new Group(), 525, 450);
	String tagId = "tagbeanair";
	TextField TagIdInputBox;
	TextField BrokerInputBox;
	TextField ClientInputBox;
	TextField TopicInputBoxX;
	TextField TopicInputBoxY;
	TextField TopicInputBoxZ;
	TextField LBSUrlInputBox;
	TextField OtherUrlInputBox;
	TextField LocationLatInputBox;
	TextField LocationLngInputBox;
	ComboBox<String> emailComboBox;
	ComboBox<String> dataTypeComboBox;
    Gauge gaugeX = GaugeBuilder.create()
            .title("X Axis")
            .unit("Gram")
            .build();

    Gauge gaugeY = GaugeBuilder.create()
            .title("Y Axis")
            .unit("Gram")
            .build();
    Gauge gaugeZ = GaugeBuilder.create()
            .title("Z Axis")
            .unit("Gram")
            .build();
    

    public static void main( String[] args ) throws  InterruptedException, MqttException
    {
    	launch(args);
}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ClientId = MqttClient.generateClientId();
		//Initialize Components
		TagIdInputBox = new TextField();
		TagIdInputBox.setPrefWidth(200);
		BrokerInputBox = new TextField();
		BrokerInputBox.setPrefWidth(200);
		ClientInputBox = new TextField();
		ClientInputBox.setPrefWidth(200);
		TopicInputBoxX = new TextField();
		TopicInputBoxX.setPrefWidth(200);
		TopicInputBoxY = new TextField();
		TopicInputBoxY.setPrefWidth(200);
		TopicInputBoxZ = new TextField();
		TopicInputBoxZ.setPrefWidth(200);
		LBSUrlInputBox = new TextField();
		LBSUrlInputBox.setPrefWidth(200);
		OtherUrlInputBox = new TextField();
		OtherUrlInputBox.setPrefWidth(200);
		LocationLatInputBox = new TextField();
		LocationLatInputBox.setPrefWidth(200);
		LocationLatInputBox.setDisable(true);
		LocationLatInputBox.setText("");
		LocationLngInputBox = new TextField();
		LocationLngInputBox.setPrefWidth(200);
		LocationLngInputBox.setDisable(true);
		LocationLngInputBox.setText("");

    	Button btn = new Button();
        btn.setText("Start");
        btn.setEffect(new DropShadow());
        
        //Start Button Handle
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            public void handle(ActionEvent event) {
            	try {
            		
            		if(btn.getText().equals("Stop"))
            			System.exit(0);
            		
            		tagId = TagIdInputBox.getText();
            		broker = BrokerInputBox.getText();
            		topicNameX = TopicInputBoxX.getText();
            		topicNameY = TopicInputBoxY.getText();
            		topicNameZ = TopicInputBoxZ.getText();
            		OtherUrl = OtherUrlInputBox.getText();
            		LBSUrl = LBSUrlInputBox.getText();
            		ClientId = ClientInputBox.getText();
            		
            		String topicArr[] = {topicNameX, topicNameY, topicNameZ}; //topic X, topicY, topicZ
            		MQTT_Listenner mqtt_obj = new MQTT_Listenner(broker,ClientId, topicArr,tagId,emailComboBox.getValue(),LocationLatInputBox.getText(),LocationLngInputBox.getText(),LBSUrl,OtherUrl,dataTypeComboBox.getValue());
					mqtt_obj.InitGauges(gaugeX, gaugeY, gaugeZ);
					mqtt_obj.Connect();
            		btn.setText("Stop");
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 
            }
        });
        

        
        GridPane grid = new GridPane();
        GridPane GaugeGrid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(5, 5, 5, 5));
        
        grid.add(new Label("Broker URL:"), 0, 0);
        BrokerInputBox.setText(broker);
        grid.add(new Label("Client Id:"), 0, 1);
        ClientInputBox.setText(ClientId);
        grid.add(new Label("TopicX:"), 0, 2);
        TopicInputBoxX.setText(topicNameX);
        grid.add(new Label("TopicY:"), 0, 3);
        TopicInputBoxY.setText(topicNameY);
        grid.add(new Label("TopicZ:"), 0, 4);
        TopicInputBoxZ.setText(topicNameZ);
        grid.add(new Label("LBS TagId:"), 0, 5);
        TagIdInputBox.setText(tagId);
        grid.add(new Label("LBS Url:"), 0, 6);
        LBSUrlInputBox.setText(LBSUrl);
        grid.add(new Label("Other Url:"), 0, 7);
        OtherUrlInputBox.setText(OtherUrl);
        grid.add(new Label("Latitude"), 0, 9);
        LocationLatInputBox.setText(LocationLat);
        grid.add(new Label("Longitude:"), 0, 10);
        LocationLngInputBox.setText(LocationLng);

        
        grid.add(BrokerInputBox, 1, 0);
        grid.add(ClientInputBox, 1, 1);
        grid.add(TopicInputBoxX, 1, 2);
        grid.add(TopicInputBoxY, 1, 3);
        grid.add(TopicInputBoxZ, 1, 4);
        grid.add(TagIdInputBox, 1, 5);
        grid.add(LBSUrlInputBox, 1, 6);
        grid.add(OtherUrlInputBox, 1, 7);
        
        emailComboBox = new ComboBox<String>();
        emailComboBox.getItems().addAll("Yes","No","Send Manually");
        emailComboBox.getSelectionModel().select("No");
        grid.add(new Label("Send Location:"), 0, 8);
        grid.add(emailComboBox, 1, 8);
        
        dataTypeComboBox = new ComboBox<String>();
        dataTypeComboBox.getItems().addAll("BeanAir Frame","Other - Direct Value");
        dataTypeComboBox.getSelectionModel().select("BeanAir Device");
        grid.add(new Label("Frame Type:"), 0, 11);
        grid.add(dataTypeComboBox, 1, 11);
        
        grid.add(LocationLatInputBox, 1, 9);
        grid.add(LocationLngInputBox, 1, 10);
        
        // emailComboBox handler
        emailComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(emailComboBox.getSelectionModel().getSelectedItem().equals("Send Manually")) {
					LocationLatInputBox.setDisable(false);
					LocationLngInputBox.setDisable(false);
				}else {
					LocationLatInputBox.setDisable(true);
					LocationLngInputBox.setDisable(true);
				}
			}
        });
        
        //Start Button
        grid.add(btn, 1, 12);
        
        gaugeX.setPrefSize(150, 150);
        gaugeX.setMinValue(-5);
        gaugeX.setMaxValue(5);

        

        gaugeY.setPrefSize(150, 150);
        gaugeY.setMinValue(-5);
        gaugeY.setMaxValue(5);
        		

        gaugeZ.setPrefSize(150, 150);
        gaugeZ.setMinValue(-5);
        gaugeZ.setMaxValue(5);
//        
        GaugeGrid.add(gaugeX, 0, 1);
        GaugeGrid.add(gaugeY, 0, 2);
        GaugeGrid.add(gaugeZ, 0, 3);
        GaugeGrid.setLayoutX(350); 
        
        
        ImageView img1 = new ImageView(new Image(getClass().getResourceAsStream("Altran_logo.png")));
        grid.add(img1, 1, 13);
        
        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);
        root.getChildren().add(GaugeGrid);
        //((Group)scene.getRoot()).getChildren().add(grid);
        //((Group)scene.getRoot()).getChildren().add(GaugeGrid);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("MQTT Proxy APP v2");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        
	}
}
