package com.beroe.prerequisites;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.text.StrSubstitutor;


public class FormatterTest {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String input = "${Server_IP1}:${ServerPort} added";
	
		Map prop = new Properties();
		
		prop.put("Server_IP", "192.168.201.16");
		prop.put("ServerPort", "8080");
		
//		String output = MessageFormat.format(input,)

				
				 StrSubstitutor sub = new StrSubstitutor(prop);
				 System.out.println(sub.replace(input));

//				
//				
//				Map valuesMap = HashMap();
//		 valuesMap.put("animal", "quick brown fox");
//		 valuesMap.put("target", "lazy dog");
//		 String templateString = "The ${animal} jumped over the ${target}.";
//		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
//		 String resolvedString = sub.replace(templateString);
//		 
		

	}

}
