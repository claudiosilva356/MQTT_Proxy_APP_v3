package MQTT_Client_V4;

import java.util.Properties;

import javax.mail.*;

public class email {
	// Recipient's email ID needs to be mentioned.
    String to = "claudiosilva356@gmail.com";

    // Sender's email ID needs to be mentioned
    String from = "claudiosilva356@gmail.com";

    // Assuming you are sending email from localhost
    String host = "localhost";

    // Get system properties
    Properties properties = System.getProperties();
    
    static Session getMailSession;

    // Setup mail server

    
    public email() {
    	
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session=session.getDefaultInstance(properties,null); 
    }

}
