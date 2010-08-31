package com.n4systems.fieldid.viewhelpers;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class BaseActionHelper {

	
	private static final String TRAILING_CHARACTERS_TO_SHOW_MORE_WORDS_EXIST = "...";


	public String replaceCR(String string) {
		return string.replace("\n", "<br />");
	}

	
	public String trimString(String string, int maxNumberOfCharacters) {
		if (string.length() > maxNumberOfCharacters) {
			String trimmedString = string.substring(0, maxNumberOfCharacters - TRAILING_CHARACTERS_TO_SHOW_MORE_WORDS_EXIST.length());
			return trimmedString + TRAILING_CHARACTERS_TO_SHOW_MORE_WORDS_EXIST;
		}
		return string;
	}
	
	
	public String configEntry(String entryName) {
		ConfigEntry configEntry = ConfigEntry.valueOf(entryName);
		return ConfigContext.getCurrentContext().getString(configEntry);
	}
}
