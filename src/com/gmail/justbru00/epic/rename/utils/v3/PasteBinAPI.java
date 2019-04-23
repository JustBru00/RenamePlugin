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

/**
 * Created for issue #105, #106
 * @author Justin Brubaker
 *
 */
public class PasteBinAPI {
	
	private static final String DEV_KEY = "c577908005909793b051b84c01254a82";
	private static final String POST_URL = "https://pastebin.com/api/api_post.php?api_option=paste&api_dev_key=" + DEV_KEY + "&api_paste_code={DATA}&api_paste_format=yaml&api_paste_expire_date=1M";

	/**
	 * Pastes the data provided directly to https://pastebin.com/api
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
	
	private static String post(String data) throws IOException, MalformedURLException {
		URL formattedUrl = null;
		formattedUrl = new URL(POST_URL.replace("{DATA}", data));
		
		
		System.out.println("[PasteBinAPI] Attempting to POST.");		
		
			HttpsURLConnection httpsCon = (HttpsURLConnection) formattedUrl.openConnection();
			httpsCon.setDoOutput(true);
			httpsCon.setDoInput(true);
			httpsCon.setRequestMethod("POST");
			
			OutputStreamWriter out = new OutputStreamWriter(
			    httpsCon.getOutputStream());
			out.write(data);
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
					
			System.out.println("[PasteBinAPI] POST response code was: " + httpsCon.getResponseCode());
			
			if (response.contains("Bad API request,"))	{
				System.out.println("[PasteBinAPI] FAILED TO POST: " + response);
			}
			
			return response;
	}
	
}
