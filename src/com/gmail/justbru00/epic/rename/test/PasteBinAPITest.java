package com.gmail.justbru00.epic.rename.test;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gmail.justbru00.epic.rename.utils.v3.PasteBinAPI;

public class PasteBinAPITest {
	public static void main(String[] args) throws MalformedURLException, IOException {
		System.out.println(PasteBinAPI.paste("This is a test of the Pastebin api"));
	}
}
