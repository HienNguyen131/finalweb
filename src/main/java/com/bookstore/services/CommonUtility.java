package com.bookstore.services;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;



public class CommonUtility {
	public static void generateCountryList(HttpServletRequest request) {
		String[] countryCodes = Locale.getISOCountries();
		
		Map<String, String> mapCountries = new TreeMap<>();
		
		for (String countryCode : countryCodes) {
			Locale locale = new Locale("", countryCode);
			String code = locale.getCountry();
			String name = locale.getDisplayCountry();
			
			mapCountries.put(name, code);
		}
		
		request.setAttribute("mapCountries", mapCountries);
	}	
}
