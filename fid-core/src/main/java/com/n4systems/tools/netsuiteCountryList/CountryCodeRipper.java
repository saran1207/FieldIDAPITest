package com.n4systems.tools.netsuiteCountryList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Takes a file containing a list of countries and their country code in a line separated format of <country code>:<country name>
 * It then loads our current CountryList.xml resource and applies the country code to the list where the names match
 * and resaves the list.
 * 
 * If a country in the CountryList.xml is not found in the country code file its current country code will be left as is. 
 * 
 * @author Jesse Miller
 *
 */
public class CountryCodeRipper {
	
	private final String inputCountryFileName;
	private CountryList countryList;
	private Map<String, String> nameToCode = new HashMap<String, String>();
	
	public CountryCodeRipper(String inputCountryFileName) {
		this.inputCountryFileName = inputCountryFileName;
		parseCountryCodes();
		countryList = CountryList.getInstance();
	}	
	
	public void populateCountryCodes() {		

		for (Country country : countryList.getCountries()) {
			if (nameToCode.get(country.getName()) != null) {
				System.out.println("Setting "+country.getName()+" to have a country code of "+nameToCode.get(country.getName()));
				country.setCode(nameToCode.get(country.getName()));
			}
		}
		
		saveCountryList();
	}
	
	private void saveCountryList() {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(CountryList.class);

		try {
			FileOutputStream out = new FileOutputStream(CountryList.class.getSimpleName() + ".xml");
			xstream.toXML(countryList, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void parseCountryCodes() {
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new FileReader(inputCountryFileName));
			
			String line = in.readLine();
			
			while (line != null) {
				String[] parsedLine = line.split(":");
				nameToCode.put(parsedLine[1], parsedLine[0]);	
				line = in.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public static void main(String args[]) {
		CountryCodeRipper countryCodeRipper = new CountryCodeRipper("netsuiteCountryList.txt");
		countryCodeRipper.populateCountryCodes();
	}
}
