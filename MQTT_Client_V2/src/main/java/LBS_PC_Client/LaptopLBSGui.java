package LBS_PC_Client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.*;
import javafx.concurrent.*;

@SuppressWarnings("restriction")
public class LaptopLBSGui extends Application { 
		
	Scene scene;
	TextField APITokenInputBox;
	TextField LBSServerInputBox;
	TextField TagIdInputBox;
	TextField SerialNumber;
	TextField Username;
	TextField SSID;
	TextField BSSID;
	TextField SSIDState;
	TextField LatitudeInputBox;
	TextField LongitudeInputBox;
	
	Task<Void> task;
	Thread thread;
		
	@Override
	public void start(Stage primaryStage) throws Exception {
		scene = new Scene(new Group(), 305, 380);
		
		APITokenInputBox = new TextField();
		LBSServerInputBox = new TextField();
		LBSServerInputBox.setPrefWidth(200);
		TagIdInputBox = new TextField();
		TagIdInputBox.setPrefWidth(200);
		
		SerialNumber = new TextField();
		SerialNumber.setPrefWidth(200);
		SerialNumber.setEditable(false);
		SerialNumber.setStyle("-fx-text-inner-color: grey;");
		Username = new TextField();
		Username.setPrefWidth(200);
		Username.setEditable(false);
		Username.setStyle("-fx-text-inner-color: grey;");
		SSID = new TextField();
		SSID.setPrefWidth(200);
		SSID.setEditable(false);
		SSID.setStyle("-fx-text-inner-color: grey;");
		BSSID = new TextField();
		BSSID.setPrefWidth(200);
		BSSID.setEditable(false);
		BSSID.setStyle("-fx-text-inner-color: grey;");
		SSIDState = new TextField();
		SSIDState.setPrefWidth(200);
		SSIDState.setEditable(false);
		SSIDState.setStyle("-fx-text-inner-color: grey;");
		
		LatitudeInputBox = new TextField();
		LatitudeInputBox.setPrefWidth(200);
		LongitudeInputBox = new TextField();
		LongitudeInputBox.setPrefWidth(200);
		
		Button btn = new Button();
		btn.setText("START");
		btn.setEffect(new DropShadow());
		
		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				try {
					if(btn.getText().equals("START")
						&& !APITokenInputBox.getText().isEmpty()
						&& !LBSServerInputBox.getText().isEmpty()
						&& !TagIdInputBox.getText().isEmpty()
						//&& !LatitudeInputBox.getText().isEmpty()
						//&& !LongitudeInputBox.getText().isEmpty()
					) {
						btn.setText("STOP");
						LaptopLBS_v2.STARTED = true;
						
						task = new Task<Void>() {
				            @Override
				            protected Void call() {
				            	if(LatitudeInputBox.getText().equals("") && LongitudeInputBox.getText().equals("")) {
					            	LaptopLBS_v2.setLocation(APITokenInputBox.getText());
					        		LatitudeInputBox.setText(LaptopLBS_v2.lat);
					        		LongitudeInputBox.setText(LaptopLBS_v2.lng);
				            	}
				        		
				            	LaptopLBS_v2.postToLBS(
				            		APITokenInputBox.getText(),
				            		LBSServerInputBox.getText(),
				            		TagIdInputBox.getText(),
				            		LatitudeInputBox.getText(),
				            		LongitudeInputBox.getText()
				            	);
				                return null;
				            }
				        };
						thread = new Thread(task);
				        thread.setDaemon(true);
				        thread.start();
					} else {
						btn.setText("START");
						LaptopLBS_v2.STARTED = false;
						LaptopLBS_v2.lat = "";
						LaptopLBS_v2.lng = "";
						if(task != null)
							task.cancel();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		GridPane grid = new GridPane();
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(5, 5, 5, 5));
		
		grid.add(new Label("API Token:"), 0, 0);
		APITokenInputBox.setText(LaptopLBS_v2.API_TOKEN);
		grid.add(new Label("LBS Server:"), 0, 1);
		LBSServerInputBox.setText(LaptopLBS_v2.LBS_SERVER_URL);
		grid.add(new Label("LBS TagId:"), 0, 2);
		TagIdInputBox.setText(LaptopLBS_v2.tagId);
		grid.add(new Label("Serial Number:"), 0, 3);
		SerialNumber.setText(LaptopLBS_v2.serialNumber);
		grid.add(new Label("Username:"), 0, 4);
		Username.setText(LaptopLBS_v2.username);
		grid.add(new Label("SSID:"), 0, 5);
		SSID.setText(LaptopLBS_v2.ssid);
		grid.add(new Label("BSSID:"), 0, 6);
		BSSID.setText(LaptopLBS_v2.bssid);
		grid.add(new Label("SSID State:"), 0, 7);
		SSIDState.setText(LaptopLBS_v2.ssidState);
		grid.add(new Label("Latitude:"), 0, 8);
		LatitudeInputBox.setText(LaptopLBS_v2.lat);
		grid.add(new Label("Longitude:"), 0, 9);
		LongitudeInputBox.setText(LaptopLBS_v2.lng);
		
		grid.add(APITokenInputBox, 1, 0);
		grid.add(LBSServerInputBox, 1, 1);
		grid.add(TagIdInputBox, 1, 2);
		grid.add(SerialNumber, 1, 3);
		grid.add(Username, 1, 4);
		grid.add(SSID, 1, 5);
		grid.add(BSSID, 1, 6);
		grid.add(SSIDState, 1, 7);
		grid.add(LatitudeInputBox, 1, 8);
		grid.add(LongitudeInputBox, 1, 9);
		
		grid.add(btn, 1, 10);
		
        ImageView img1 = new ImageView(new Image(getClass().getResourceAsStream("Altran_logo.png")));
        grid.add(img1, 1, 11);

		Group root = (Group)scene.getRoot();
		root.getChildren().add(grid);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Laptop LBS APP v2");
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			System.exit(0);
		});
	}
}
