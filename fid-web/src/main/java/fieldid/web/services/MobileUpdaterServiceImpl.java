package fieldid.web.services;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;



public class MobileUpdaterServiceImpl implements IMobileUpdaterService {
	
	
	public String GetManifestList(String deviceId){
		
		String beginString = "http://localhost/fieldid/";
		String xmlString = "";
		//get a different manifest for each device type
		if(deviceId.equals("WM5"))
			xmlString = xmlFile2String(beginString + "applications/WM5-ManifestList.xml");
		else if (deviceId.equals("WM6"))
			xmlString = xmlFile2String(beginString + "applications/WM6-ManifestList.xml");
		else
			xmlString = xmlFile2String(beginString + "applications/WM5-ManifestList.xml");
		
		return xmlString;
	}
	
	public ArrayList<String> GetSQLUpdates(int lastDatabaseVersion) {
		return new ArrayList<String>();
	}
	
	  private String xmlFile2String(String fileName)
	  {
	    try{
	      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	      InputSource inputSource = new InputSource(fileName);
	      Document document = documentBuilderFactory.newDocumentBuilder().parse(inputSource);
	      StringWriter sw = new StringWriter();
	      Transformer serializer = TransformerFactory.newInstance().newTransformer();
	      serializer.transform(new DOMSource(document), new StreamResult(sw));
	      return sw.toString();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return "";
	  }
	
	
}