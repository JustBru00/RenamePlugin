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
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import com.gmail.justbru00.epic.rename.exceptions.EpicRenameOnlineExpiredException;
import com.gmail.justbru00.epic.rename.exceptions.EpicRenameOnlineNotFoundException;
import com.gmail.justbru00.epic.rename.main.v3.Main;

/**
 * Created for issue #105, #106
 * 
 * @author Justin Brubaker
 *
 */
public class EpicRenameOnlineAPI {
	
	private static final String POST_URL = "https://epicrename.com/api/v1/export";

	/**
	 * Attempts to GET raw text from a URL. If the URL is a pastebin link such as
	 * https://pastebin.com/e5mwvrJ8 then the method will convert that link to
	 * https://pastebin.com/raw/e5mwvrJ8 If the URL is not a pastebin link then it
	 * will just attempt to get raw text.
	 * 
	 * This method attempts to connect with HTTPS if possible.
	 * 
	 * @param url
	 * @return The text retrieved from the server
	 * @throws IOException
	 */
	public static Optional<String> getTextFromURL(String url) throws IOException, EpicRenameOnlineExpiredException, EpicRenameOnlineNotFoundException {
		Main.usesEpicRenameOnlineFeatures = true;
		if (url.contains("https://pastebin.com/") && !url.contains("raw/")) {
			String newUrl = "";

			newUrl = url.substring(21, url.length());

			newUrl = "https://pastebin.com/raw/VALUE".replace("VALUE", newUrl);
			url = newUrl;
			Debug.send("[PasteBinAPI] New URL is: " + url);
		}

		URL urlObj = null;
		String textData = null;

		urlObj = new URL(url);

		HttpsURLConnection httpsConn = (HttpsURLConnection) urlObj.openConnection();
		httpsConn.setRequestProperty("Content-Type", "0");
		httpsConn.setRequestMethod("GET");
		httpsConn.setUseCaches(false);
		httpsConn.setConnectTimeout(300);
		httpsConn.setReadTimeout(1000);
		httpsConn.setAllowUserInteraction(false);
		httpsConn.setInstanceFollowRedirects(false);
		httpsConn.setRequestProperty("Connection", "close");
		httpsConn.connect();
		
		BufferedReader in = null;
		String inputLine;
		StringBuffer response = new StringBuffer();

		if (httpsConn.getResponseCode() >= 200 && httpsConn.getResponseCode() < 300) {
			// Attempt to get raw text data
			 in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(), "UTF-8"));
		} else {
			 in = new BufferedReader(new InputStreamReader(httpsConn.getErrorStream(), "UTF-8"));
		}

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();

		textData = response.toString();
		
		if (textData.startsWith("ERROR:")) {
			if (textData.startsWith("ERROR: 404 - Not Found." ) && textData.contains("Link has expired")) {
				// Link expired on EpicRenameOnline server
				throw new EpicRenameOnlineExpiredException();
			} else if (textData.startsWith("ERROR: 404 - Not Found." ) && textData.contains("doesn't exist")) {
				// Link not found on EpicRenameOnline server.
				throw new EpicRenameOnlineNotFoundException();
			}
		}
		
		if(textData.trim().equalsIgnoreCase("") || textData == null) {
			return Optional.ofNullable(null);
		}

		return Optional.ofNullable(textData);
	}

	/**
	 * Pastes the data provided directly to https://epicrename.com/
	 * 
	 * @param data The text to paste.
	 * @return The response from EpicRenameOnline. This can be a link to the paste or an
	 *         error message beginning with "ERROR:"
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static String paste(String data) throws MalformedURLException, IOException {
		String response = post(data);
		Main.usesEpicRenameOnlineFeatures = true;

		return response;
	}

	/**
	 * Posts text data to EpicRenameOnline.
	 * 
	 * @param data
	 * @return If this contains "ERROR: " then the post failed.
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private static String post(String data) throws IOException, MalformedURLException {
		URL formattedUrl = null;
		formattedUrl = new URL(POST_URL);

		Debug.send("[PasteBinAPI] Attempting to POST.");

		HttpsURLConnection httpsCon = (HttpsURLConnection) formattedUrl.openConnection();
		httpsCon.setDoOutput(true);
		httpsCon.setDoInput(true);
		httpsCon.setConnectTimeout(300);
		httpsCon.setReadTimeout(1000);
		httpsCon.setRequestMethod("POST");

		OutputStreamWriter out = new OutputStreamWriter(httpsCon.getOutputStream(), "UTF-8");
		out.write(data);
		out.flush();
		out.close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(httpsCon.getInputStream(), "UTF-8"));
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

		Debug.send("[EpicRenameOnlineAPI] POST response code was: " + httpsCon.getResponseCode());

		if (response.contains("ERROR:")) {
			Debug.send("[EpicRenameOnlineAPI] FAILED TO POST: " + response);
		}

		return response;
	}

}
