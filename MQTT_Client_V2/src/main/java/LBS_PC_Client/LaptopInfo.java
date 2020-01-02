package LBS_PC_Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

public class LaptopInfo {

	private static String[] ssidInfo;
	
	public static String[] getSsidInfo() {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan show interfaces");
		builder.redirectErrorStream(true);
		Process p;
		try {
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			ssidInfo = new String[3];
			while ((line = r.readLine()) != null) {
				if (line != null && (line.contains("SSID") || line.contains("State"))) {
					line = line.replace(" ", "");
					setSsidInfo(line);
				}
			}			
		} catch(IOException e) {
			e.printStackTrace();
		}
		//return ssidInfo[0].isEmpty() ? null : ssidInfo;
		return ssidInfo;
	}
	
	private static void setSsidInfo(String line) {
		if(line.startsWith("SSID")) {
			ssidInfo[0] = line.substring(line.indexOf(":")+1);
		} else {
			if(line.startsWith("BSSID")) {
				ssidInfo[1] = line.substring(line.indexOf(":")+1);
			} else {
				if(line.startsWith("State")) {
					ssidInfo[2] = line.substring(line.indexOf(":")+1);
				}
			}
		}
	}
	
	public static String getSerialNumber() {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "wmic bios get serialnumber");
		builder.redirectErrorStream(true);
		Process p;
		try {
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				if(line.length() > 0 && !line.contains("SerialNumber"))
					return line.trim();
			}			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getUsername() {
		return System.getProperty("user.name");
	}
	
	public static String[] getLocation(String token) {
		IPInfo ipInfo = IPInfo.builder().setToken(token).build();
		try {
            IPResponse response = ipInfo.lookupIP("");
            return new String[] {response.getLatitude(), response.getLongitude()};
        } catch (RateLimitedException ex) { }		
		return null;
	}

}
