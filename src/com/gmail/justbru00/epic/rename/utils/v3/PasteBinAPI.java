/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.gmail.justbru00.epic.rename.main.v3.Main;

/**
 * Created for issue #105, #106
 * @author Justin Brubaker
 *
 */
public class PasteBinAPI {
	
	private static final String DEV_KEY = "c577908005909793b051b84c01254a82";
	private static final String POST_URL = "https://pastebin.com/api/api_post.php";
	private static final String POST_PARAMETERS = "api_option=paste&api_dev_key=" + DEV_KEY + "&api_paste_name=EpicRename v" + Main.PLUGIN_VERISON + " /export&api_paste_code={DATA}&api_paste_format=yaml&api_paste_expire_date=1M";
	
	/**
	 * Pastes the data provided directly to https://pastebin.com/
	 * @param data The text to paste.
	 * @return The response from pastebin. This can be a link to the paste or an error message beginning with 
	 * "Bad API request," 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static String paste(String data) throws MalformedURLException, IOException {
		String response = post(data);
		
		return response;
	}
	
	/**
	 * Posts text data to PasteBin.
	 * @param data
	 * @return If this contains "Bad API request," then the post failed.
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private static String post(String data) throws IOException, MalformedURLException {
		URL formattedUrl = null;
		formattedUrl = new URL(POST_URL);
		
		String formattedData = POST_PARAMETERS.replace("{DATA}", data);
		
		
		Debug.send("[PasteBinAPI] Attempting to POST.");		
		
			HttpsURLConnection httpsCon = (HttpsURLConnection) formattedUrl.openConnection();
			httpsCon.setDoOutput(true);
			httpsCon.setDoInput(true);
			httpsCon.setRequestMethod("POST");
			
			OutputStreamWriter out = new OutputStreamWriter(
			    httpsCon.getOutputStream());
			out.write(formattedData);
			out.flush();
			out.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpsCon.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				if (builder.length() > 0) {
					builder.append('\n');
				}
				builder.append(line);
			}
			reader.close();
			
			String response = builder.toString();
					
			Debug.send("[PasteBinAPI] POST response code was: " + httpsCon.getResponseCode());
			
			if (response.contains("Bad API request,"))	{
				Debug.send("[PasteBinAPI] FAILED TO POST: " + response);
			}
			
			return response;
	}
	
}
