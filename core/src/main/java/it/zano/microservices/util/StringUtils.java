package it.zano.microservices.util;

import java.util.Iterator;
import java.util.List;


public class StringUtils {

	public static String lowerCaseFirstLetter(String wordToCapitalize){
		return wordToCapitalize != null ? wordToCapitalize.replaceFirst(wordToCapitalize.substring(0, 1),
				wordToCapitalize.substring(0, 1).toLowerCase()) : wordToCapitalize;
	}
	
	public static String toHexListString(List<Long> listToConvert) {
		StringBuilder builder = new StringBuilder();
		Iterator<Long> it = listToConvert.iterator();
		while(it.hasNext()) {
			builder.append(Long.toHexString(it.next().longValue()));
			if (it.hasNext()) {
				builder.append("->");    
			}
		}
		return builder.toString()!=null?builder.toString():null;
	}
}
