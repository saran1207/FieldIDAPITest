package fieldid.web.services;

import java.util.ArrayList;

public interface IMobileUpdaterService {
	
	public String GetManifestList(String deviceId);
	public ArrayList<String> GetSQLUpdates(int lastDatabaseVersion);
}