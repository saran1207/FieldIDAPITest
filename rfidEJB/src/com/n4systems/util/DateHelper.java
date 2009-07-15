package com.n4systems.util;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.utils.PlainDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

// TODO change class to use the calendar to properly handle dates.  using millisecond addition does not handle time zones
@SuppressWarnings("serial")
public class DateHelper {
	public static final long millisPerDay = 86400000;
	
	/** Fields for truncate and increment methods */
	public static final int MILLISECOND = 0;
	public static final int SECOND = 1;
	public static final int MINUTE  = 2;
	public static final int HOUR = 3;
	public static final int DAY = 4;
	public static final int WEEK = 5;
	public static final int MONTH = 6;
	public static final int YEAR = 7;

	private static final ThreadLocal<List<StringListingPair>> timeZones = new ThreadLocal<List<StringListingPair>>();
	
	public static List<StringListingPair> getTimeZones() {
		if (timeZones.get() == null) {
			List<StringListingPair> timeZoneList = new ArrayList<StringListingPair>();
			timeZoneList.add(new StringListingPair("America/Adak", "America/Adak (GMT -10:00)"));
			timeZoneList.add(new StringListingPair("America/Anchorage", "America/Anchorage (GMT -9:00)"));
			timeZoneList.add(new StringListingPair("America/Anguilla", "America/Anguilla (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Antigua", "America/Antigua (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Araguaina", "America/Araguaina (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Aruba", "America/Aruba (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Asuncion", "America/Asuncion (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Atikokan", "America/Atikokan (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Atka", "America/Atka (GMT -10:00)"));
			timeZoneList.add(new StringListingPair("America/Bahia", "America/Bahia (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Barbados", "America/Barbados (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Belem", "America/Belem (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Belize", "America/Belize (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Blanc-Sablon", "America/Blanc-Sablon (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Boa_Vista", "America/Boa Vista (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Bogota", "America/Bogota (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Boise", "America/Boise (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Buenos_Aires", "America/Buenos Aires (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Cambridge_Bay", "America/Cambridge Bay (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Campo_Grande", "America/Campo Grande (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Cancun", "America/Cancun (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Caracas", "America/Caracas (GMT -4:30)"));
			timeZoneList.add(new StringListingPair("America/Catamarca", "America/Catamarca (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Cayenne", "America/Cayenne (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Cayman", "America/Cayman (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Chicago", "America/Chicago (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Chihuahua", "America/Chihuahua (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Coral_Harbour", "America/Coral Harbour (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Cordoba", "America/Cordoba (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Costa_Rica", "America/Costa Rica (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Cuiaba", "America/Cuiaba (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Curacao", "America/Curacao (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Danmarkshavn", "America/Danmarkshavn (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("America/Dawson", "America/Dawson (GMT -8:00)"));
			timeZoneList.add(new StringListingPair("America/Dawson_Creek", "America/Dawson Creek (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Denver", "America/Denver (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Detroit", "America/Detroit (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Dominica", "America/Dominica (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Edmonton", "America/Edmonton (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Eirunepe", "America/Eirunepe (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/El_Salvador", "America/El Salvador (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Ensenada", "America/Ensenada (GMT -8:00)"));
			timeZoneList.add(new StringListingPair("America/Fortaleza", "America/Fortaleza (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Fort_Wayne", "America/Fort Wayne (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Glace_Bay", "America/Glace Bay (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Godthab", "America/Godthab (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Goose_Bay", "America/Goose_Bay (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Grand_Turk", "America/Grand Turk (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Grenada", "America/Grenada (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Guadeloupe", "America/Guadeloupe (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Guatemala", "America/Guatemala (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Guayaquil", "America/Guayaquil (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Guyana", "America/Guyana (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Halifax", "America/Halifax (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Havana", "America/Havana (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Hermosillo", "America/Hermosillo (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana", "America/Indiana (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Indianapolis", "America/Indiana/Indianapolis (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Knox", "America/Indiana/Knox (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Marengo", "America/Indiana/Marengo (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Petersburg", "America/Indiana/Petersburg (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Indianapolis", "America/Indianapolis (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Tell_City", "America/Indiana/Tell City (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Vevay", "America/Indiana/Vevay (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Vincennes", "America/Indiana/Vincennes (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Indiana/Winamac", "America/Indiana/Winamac (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Inuvik", "America/Inuvik (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Iqaluit", "America/Iqaluit (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Jamaica", "America/Jamaica (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Jujuy", "America/Jujuy (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Juneau", "America/Juneau (GMT -9:00)"));
			timeZoneList.add(new StringListingPair("America/Kentucky", "America/Kentucky (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("America/Kentucky/Louisville", "America/Kentucky/Louisville (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Kentucky/Monticello", "America/Kentucky/Monticello (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Knox_IN", "America/Knox_IN (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/La_Paz", "America/La Paz (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Lima", "America/Lima (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Los_Angeles", "America/Los_Angeles (GMT -8:00)"));
			timeZoneList.add(new StringListingPair("America/Louisville", "America/Louisville (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Maceio", "America/Maceio (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Managua", "America/Managua (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Manaus", "America/Manaus (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Marigot", "America/Marigot (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Martinique", "America/Martinique (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Mazatlan", "America/Mazatlan (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Mendoza", "America/Mendoza (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Menominee", "America/Menominee (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Merida", "America/Merida (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Mexico_City", "America/Mexico_City (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Miquelon", "America/Miquelon (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Moncton", "America/Moncton (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Monterrey", "America/Monterrey (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Montevideo", "America/Montevideo (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Montreal", "America/Montreal (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Montserrat", "America/Montserrat (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Nassau", "America/Nassau (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/New_York", "America/New York (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Nipigon", "America/Nipigon (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Nome", "America/Nome (GMT -9:00)"));
			timeZoneList.add(new StringListingPair("America/Noronha", "America/Noronha (GMT -2:00)"));
			timeZoneList.add(new StringListingPair("America/North_Dakota", "America/North Dakota (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("America/North_Dakota/Center", "America/North Dakota/Center (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/North_Dakota/New_Salem", "America/North Dakota/New Salem (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Panama", "America/Panama (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Pangnirtung", "America/Pangnirtung (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Paramaribo", "America/Paramaribo (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Phoenix", "America/Phoenix (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/Port-au-Prince", "America/Port-au-Prince (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Porto_Acre", "America/Porto Acre (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Port_of_Spain", "America/Port of Spain (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Porto_Velho", "America/Porto Velho (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Puerto_Rico", "America/Puerto Rico (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Rainy_River", "America/Rainy River (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Rankin_Inlet", "America/Rankin Inlet (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Recife", "America/Recife (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Regina", "America/Regina (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Resolute", "America/Resolute (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Rio_Branco", "America/Rio Branco (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Rosario", "America/Rosario (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Santarem", "America/Santarem (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Santiago", "America/Santiago (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Santo_Domingo", "America/Santo Domingo (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Sao_Paulo", "America/Sao Paulo (GMT -3:00)"));
			timeZoneList.add(new StringListingPair("America/Scoresbysund", "America/Scoresbysund (GMT -1:00)"));
			timeZoneList.add(new StringListingPair("America/Shiprock", "America/Shiprock (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("America/St_Barthelemy", "America/St Barthelemy (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/St_Johns", "America/St Johns (GMT -3:30)"));
			timeZoneList.add(new StringListingPair("America/St_Kitts", "America/St Kitts (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/St_Lucia", "America/St Lucia (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/St_Thomas", "America/St Thomas (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/St_Vincent", "America/St Vincent (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Swift_Current", "America/Swift Current (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Tegucigalpa", "America/Tegucigalpa (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Thule", "America/Thule (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Thunder_Bay", "America/Thunder Bay (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Tijuana", "America/Tijuana (GMT -8:00)"));
			timeZoneList.add(new StringListingPair("America/Toronto", "America/Toronto (GMT -5:00)"));
			timeZoneList.add(new StringListingPair("America/Tortola", "America/Tortola (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Vancouver", "America/Vancouver (GMT -8:00)"));
			timeZoneList.add(new StringListingPair("America/Virgin", "America/Virgin (GMT -4:00)"));
			timeZoneList.add(new StringListingPair("America/Whitehorse", "America/Whitehorse (GMT -8:00)"));
			timeZoneList.add(new StringListingPair("America/Winnipeg", "America/Winnipeg (GMT -6:00)"));
			timeZoneList.add(new StringListingPair("America/Yakutat", "America/Yakutat (GMT -9:00)"));
			timeZoneList.add(new StringListingPair("America/Yellowknife", "America/Yellowknife (GMT -7:00)"));
			timeZoneList.add(new StringListingPair("Australia/ACT", "Australia/ACT (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Adelaide", "Australia/Adelaide (GMT 9:30)"));
			timeZoneList.add(new StringListingPair("Australia/Brisbane", "Australia/Brisbane (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Broken_Hill", "Australia/Broken Hill (GMT 9:30)"));
			timeZoneList.add(new StringListingPair("Australia/Canberra", "Australia/Canberra (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Currie", "Australia/Currie (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Darwin", "Australia/Darwin (GMT 9:30)"));
			timeZoneList.add(new StringListingPair("Australia/Eucla", "Australia/Eucla (GMT 8:45)"));
			timeZoneList.add(new StringListingPair("Australia/Hobart", "Australia/Hobart (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/LHI", "Australia/LHI (GMT 10:30)"));
			timeZoneList.add(new StringListingPair("Australia/Lindeman", "Australia/Lindeman (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Lord_Howe", "Australia/Lord Howe (GMT 10:30)"));
			timeZoneList.add(new StringListingPair("Australia/Melbourne", "Australia/Melbourne (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/North", "Australia/North (GMT 9:30)"));
			timeZoneList.add(new StringListingPair("Australia/NSW", "Australia/NSW (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Perth", "Australia/Perth (GMT 8:00)"));
			timeZoneList.add(new StringListingPair("Australia/Queensland", "Australia/Queensland (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/South", "Australia/South (GMT 9:30)"));
			timeZoneList.add(new StringListingPair("Australia/Sydney", "Australia/Sydney (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Tasmania", "Australia/Tasmania (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/Victoria", "Australia/Victoria (GMT 10:00)"));
			timeZoneList.add(new StringListingPair("Australia/West", "Australia/West (GMT 8:00)"));
			timeZoneList.add(new StringListingPair("Australia/Yancowinna", "Australia/Yancowinna (GMT 9:30)"));
			timeZoneList.add(new StringListingPair("Europe/Amsterdam", "Europe/Amsterdam (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Andorra", "Europe/Andorra (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Athens", "Europe/Athens (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Belfast", "Europe/Belfast (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("Europe/Belgrade", "Europe/Belgrade (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Berlin", "Europe/Berlin (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Bratislava", "Europe/Bratislava (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Brussels", "Europe/Brussels (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Bucharest", "Europe/Bucharest (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Budapest", "Europe/Budapest (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Chisinau", "Europe/Chisinau (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Copenhagen", "Europe/Copenhagen (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Dublin", "Europe/Dublin (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("Europe/Gibraltar", "Europe/Gibraltar (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Guernsey", "Europe/Guernsey (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("Europe/Helsinki", "Europe/Helsinki (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Isle_of_Man", "Europe/Isle of Man (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("Europe/Istanbul", "Europe/Istanbul (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Jersey", "Europe/Jersey (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("Europe/Kaliningrad", "Europe/Kaliningrad (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Kiev", "Europe/Kiev (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Lisbon", "Europe/Lisbon (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("Europe/Ljubljana", "Europe/Ljubljana (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/London", "Europe/London (GMT 0:00)"));
			timeZoneList.add(new StringListingPair("Europe/Luxembourg", "Europe/Luxembourg (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Madrid", "Europe/Madrid (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Malta", "Europe/Malta (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Mariehamn", "Europe/Mariehamn (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Minsk", "Europe/Minsk (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Monaco", "Europe/Monaco (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Moscow", "Europe/Moscow (GMT 3:00)"));
			timeZoneList.add(new StringListingPair("Europe/Nicosia", "Europe/Nicosia (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Oslo", "Europe/Oslo (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Paris", "Europe/Paris (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Podgorica", "Europe/Podgorica (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Prague", "Europe/Prague (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Riga", "Europe/Riga (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Rome", "Europe/Rome (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Samara", "Europe/Samara (GMT 4:00)"));
			timeZoneList.add(new StringListingPair("Europe/San_Marino", "Europe/San Marino (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Sarajevo", "Europe/Sarajevo (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Simferopol", "Europe/Simferopol (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Skopje", "Europe/Skopje (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Sofia", "Europe/Sofia (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Stockholm", "Europe/Stockholm (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Tallinn", "Europe/Tallinn (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Tirane", "Europe/Tirane (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Tiraspol", "Europe/Tiraspol (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Uzhgorod", "Europe/Uzhgorod (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Vaduz", "Europe/Vaduz (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Vatican", "Europe/Vatican (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Vienna", "Europe/Vienna (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Vilnius", "Europe/Vilnius (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Volgograd", "Europe/Volgograd (GMT 3:00)"));
			timeZoneList.add(new StringListingPair("Europe/Warsaw", "Europe/Warsaw (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Zagreb", "Europe/Zagreb (GMT 1:00)"));
			timeZoneList.add(new StringListingPair("Europe/Zaporozhye", "Europe/Zaporozhye (GMT 2:00)"));
			timeZoneList.add(new StringListingPair("Europe/Zurich", "Europe/Zurich (GMT 1:00)"));
			
			timeZones.set(timeZoneList);
		}
		return timeZones.get();
	}
	
	
	/**
	 * Used to convert SimpleDateFormat formats into Unix style formats for use
	 * with the javascript Calendar widget
	 */
	private static final Map<String, String> java2unixConversions = 
		/*
		 * We sort the map in descending order by length, since we need to do match 
		 * the longest ones first.  Otherwise MMMM could become %m%m rather then %B.
		 */
		new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String a, String b) {
				// sort by length in descending order
				return (a.length() > b.length()) ? -1 : 1;
			}
		}) {{
			/*
			 * This sets up the date format in this anonymous private method.  
			 * Bet'ca didn't know you could do this did ya :)  
			 * 
			 * See http://www.dynarch.com/demos/jscalendar/doc/html/reference.html for DHTML Calendar mappings
			 * See http://java.sun.com/j2se/1.5.0/docs/api/index.html for Java mappings
			 */
			put("MM",	"%m");
			put("MMM",	"%b");
			put("MMMM",	"%B");
			put("MMMMM","%B");
			put("EEE",	"%a");
			put("EEEE",	"%A");
			put("dd",	"%d");
			put("yyyy",	"%Y");
			put("yy",	"%y");
			put("HH",	"%H");
			put("mm",	"%M");
			put("ss",	"%S");
			put("a",	"%P");
			put("aa",	"%P");
			put("aaa",	"%P");
			put("hh",	"%I");
		}};
	
	@Deprecated
	public static Date str2SQLDate(String format, String dateStr) {
		return string2Date(format, dateStr);
	}
	
	@Deprecated
	public static String sqlDate2Str(String format, Date date) {
		return date2String(format, date);
	}

	public static Date string2DateTime(String format, String dateStr, TimeZone timeZone) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(timeZone);
		String strDateString = dateStr;  
		   
		
		try {
			return formatter.parse(strDateString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date string2Date(String format, String dateStr) {
		if (format == null || dateStr == null) {
			return null;
		}
		
		if (format.trim().length() == 0 || dateStr.trim().length() == 0) {
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException parseEx) {}
		
		return date;
	}
	
	public static boolean isDateValid(String format, String dateStr) {
		if(format == null || dateStr == null) {
			return false;
		}
		
		boolean success = true;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			sdf.parse(dateStr);
		} catch (ParseException p) {
			success = false;
		}
		
		return success;		
	}
	
	public static String date2String(String format, Date time) {
		return date2String(format, time, TimeZone.getDefault());
	}
	
	public static String date2String(String format, Date date, TimeZone timeZone) {
		if (date == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(timeZone);
		
		return sdf.format(date);
	}
	
	private static Calendar getTodayCalendar() {
		Calendar today = Calendar.getInstance();
		return setToDay(today);
	}
	
	/**
	 * Returns a Date representing today's date as returned by {@link #getTodayCalendar()}.
	 * @return	A {@link Date} object, with all time fields rolled back to zero.
	 */
	public static Date getToday() {
		return getTodayCalendar().getTime();
	}
	
	public static Date getTodayWithTime() {
		Calendar today = Calendar.getInstance();
		return today.getTime();
	}
	
	public static boolean withinTheLastHour( Date target ) {
		// XXX refactor to use increment
		Calendar oneHourAgo = Calendar.getInstance();
		Date now = new Date();
		oneHourAgo.add( Calendar.HOUR, -1 );
		
		return ( target.before( now ) && target.after( oneHourAgo.getTime() ) ); 
		
	}
	
	public static Date getYesterday() {
		// XXX refactor to use increment
		Calendar yesterday = getTodayCalendar();
		yesterday.roll( Calendar.DAY_OF_YEAR, false );
		return yesterday.getTime();
	}
	
	public static Date getTomorrow() {
		// XXX refactor to use increment
		Calendar tomorrow = getTodayCalendar();
		tomorrow.roll( Calendar.DAY_OF_YEAR, true );
		return tomorrow.getTime();
	}
	
	@Deprecated
	public static Long millisToDays(Long millis) {
		return millis / millisPerDay;
	}
	
	public static Date addDaysToDate(Date date, Long days) {
		// refactor to use increment
		return new Date(date.getTime() + (millisPerDay * days));
	}
	
	public static Long getDaysDelta(Date first, Date second) {
		return millisToDays(second.getTime() - first.getTime());
	}
	
	public static Long getDaysFromToday(Date date) {
		return getDaysDelta(getToday(), date);
	}
	
	public static Long getDaysUntilToday(Date date) {
		return getDaysDelta(date, getToday());
	}
	
	public static boolean isEqualIgnoringTime(Date date1, Date date2) {
		Date dateNoTime1 = getDateWithOutTime(date1);
		Date dateNoTime2 = getDateWithOutTime(date2);
		
		return dateNoTime1.equals(dateNoTime2);
	}
	
	/**
	 * Given a Date object, returns a Date representing only the Date portion (time portion is zeroed)
	 * @see #setToDay(Calendar)
	 * @param date	A given date object
	 * @return		The Date only portion of date
	 */
	public static Date getDateWithOutTime( Date date ){
		// XXX refactor to use truncate()
		Calendar d = Calendar.getInstance();
		d.setTime( date );
		return setToDay( d ).getTime();
	}
	
	public static Date getBeginingOfDay(Date date) {
		return getDateWithOutTime(date);
	}
	
	public static Date getEndOfDay(Date date) {
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		setToDay(d);
		d.set(Calendar.HOUR_OF_DAY, 23);
		d.set(Calendar.MINUTE, 59);
		d.set(Calendar.SECOND, 59);
		d.set(Calendar.MILLISECOND, 999);
		return d.getTime();
	}
	/**
	 * Given a Calendar object, zeros out the hour, min, second and millisecond fields. Aka
	 * returns only the day, month, year portion of the date.
	 * @param date	A Calendar date object
	 * @return		The Date only representation of the given calendar object
	 */
	private static Calendar setToDay( Calendar date ){
		// XXX refactor to use truncate()
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		return date;
	}
	
	/**
	 * Converts SimpleDateFormat formats into Unix style date formats
	 *  
	 * @param format	A SimpleDateFormat string date format
	 * @return			A Unix style date format
	 */
	public static String java2Unix(String format) {
		String workingFormat = new String(format);
		for(Map.Entry<String, String> entry: java2unixConversions.entrySet()) {
			workingFormat = workingFormat.replace(entry.getKey(), entry.getValue());
		}
		
		return workingFormat;
	}
	

	public static Date convertToUserTimeZone(Date date, TimeZone timeZone) {
		return convertTimeZone(date, timeZone, TimeZone.getDefault());
	}
	
	private static Date convertTimeZone(Date date, TimeZone inputTimeZone, TimeZone outputTimeZone) {
		if (date == null) { return null; }
		
		if (date instanceof PlainDate) {
			throw new InvalidArgumentException("You are trying to convert the time zone of a plain date you should never do this.");
		}
		
		
		Calendar first = Calendar.getInstance(inputTimeZone);  
		first.setTimeInMillis(date.getTime());  
		   
		// creates a date that is assumed to now be in the given time zone instead of the default one.
		Calendar output = Calendar.getInstance(outputTimeZone);  
		output.set(Calendar.YEAR, first.get(Calendar.YEAR));  
		output.set(Calendar.MONTH, first.get(Calendar.MONTH));  
		output.set(Calendar.DAY_OF_MONTH, first.get(Calendar.DAY_OF_MONTH));  
		output.set(Calendar.HOUR_OF_DAY, first.get(Calendar.HOUR_OF_DAY));  
		output.set(Calendar.MINUTE, first.get(Calendar.MINUTE));  
		output.set(Calendar.SECOND, first.get(Calendar.SECOND));  
		output.set(Calendar.MILLISECOND, first.get(Calendar.MILLISECOND));  
		   
		return output.getTime();  
	}
	
	public static Date convertToUTC(Date date, TimeZone timeZone) {
		return convertTimeZone(date, TimeZone.getDefault(), timeZone);
	}
	
	public static String getTimeZoneShortName(Date date,TimeZone zone) {
		return zone.getDisplayName(zone.inDaylightTime(date), TimeZone.SHORT);
	}
	
	/**
	 * Truncates a date to the precision of the requested calendar field. Sets all smaller fields to their base value.<br />
	 * Examples:
	 * <pre>
	 * date is 'Wednesday May 27, 2009 15:50:32.234'
	 * 
	 * 	DateHelper.truncate(date, DateHelper.MILLISECOND);	// returns Wednesday May 27, 2009 15:50:32.234 (no effect)
	 * 	DateHelper.truncate(date, DateHelper.SECOND);		// returns Wednesday May 27, 2009 15:50:32.0
	 * 	DateHelper.truncate(date, DateHelper.MINUTE);		// returns Wednesday May 27, 2009 15:50:00.0
	 * 	DateHelper.truncate(date, DateHelper.HOUR);		// returns Wednesday May 27, 2009 15:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.DAY);		// returns Wednesday May 27, 2009 00:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.WEEK);		// returns Sunday May 24, 2009 00:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.MONTH);		// returns Friday May 01, 2009 00:00:00.0
	 * 	DateHelper.truncate(date, DateHelper.YEAR);		// returns Thursday January 01, 2009 00:00:00.0
	 * </pre>
	 * @param date
	 * @param calendarField
	 * @return
	 */
	public static Date truncate(Date date, int field) {
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		
		// Each field operates on the next smaller field and continues down the chain.
		switch (field) {
			case YEAR:
				cal.set(Calendar.MONTH, Calendar.JANUARY);
			case MONTH:
			case WEEK:
				/* 
				 * WEEK and MONTH both operate on the day (can't be chained together), 
				 * so we'll handle them in the same block to allow the chaining to flow through.
				 */
				if (field == WEEK) {
					cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				} else {
					// if the YEAR or higher was specified, we need to zero out the day rather then the week
					cal.set(Calendar.DAY_OF_MONTH, 1);
				}
			case DAY:
				cal.set(Calendar.HOUR_OF_DAY, 0);
			case HOUR:
				cal.set(Calendar.MINUTE, 0);
			case MINUTE:
				cal.set(Calendar.SECOND, 0);
			case SECOND:
				cal.set(Calendar.MILLISECOND, 0);
			case MILLISECOND:
				// Date use millisecond as the base value so this does nothing
				break;
			default:
				throw new IllegalArgumentException("Invalid date field: " + field);
		}
		
		return cal.getTime();
	}
	
	/**
	 * Adds (or subtracts when negative) a specified amount to the field of the given date.
	 * @param date		Date to start field calculation from
	 * @param field		Field to operate on
	 * @param ammount	Amount to increment/decrement by
	 * @return			Date post field incrementation
	 */
	public static Date increment(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		
		switch (field) {
			case YEAR:
				cal.add(Calendar.YEAR, amount);
				break;
			case MONTH:
				cal.add(Calendar.MONTH, amount);
				break;
			case WEEK:
				cal.add(Calendar.WEEK_OF_MONTH, amount);
				break;
			case DAY:
				cal.add(Calendar.DAY_OF_MONTH, amount);
				break;
			case HOUR:
				cal.add(Calendar.HOUR_OF_DAY, amount);
				break;
			case MINUTE:
				cal.add(Calendar.MINUTE, amount);
				break;
			case SECOND:
				cal.add(Calendar.SECOND, amount);
				break;
			case MILLISECOND:
				cal.add(Calendar.MILLISECOND, amount);
				break;
			default:
				throw new IllegalArgumentException("Invalid date field: " + field);
		}
		
		return cal.getTime();
	}
	
}
