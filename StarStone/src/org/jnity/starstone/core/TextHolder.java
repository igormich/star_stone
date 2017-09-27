package org.jnity.starstone.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class TextHolder {
	private static final Map<String,String> text= new HashMap<>();
	private static final String DESCRIPTION = "_DESK";
	private static final String ABOUT = "_ABOUT";
	
	public static void load(String fileName) throws IOException{
		text.clear();
		int n=1;
		for(String line:Files.readAllLines(new File(fileName).toPath(),Charset.defaultCharset())){
			try{
			String[] keyValue = line.split("=");
			text.put(keyValue[0], keyValue[1]);
			}catch (Exception e) {
				Debug.print("Can't parse "+n+ " line , from "+fileName);
				Debug.print(line);
			}
			n++;
		}
	}
	public static String getName(String ID) {
		return text.getOrDefault(ID, ID);
	}
	public static String getDescription(String ID) {
		return text.getOrDefault(ID+DESCRIPTION, ID+DESCRIPTION);
	}
	public static String getAbout(String ID) {
		return text.getOrDefault(ID+ABOUT, ID+ABOUT);
	}
}
