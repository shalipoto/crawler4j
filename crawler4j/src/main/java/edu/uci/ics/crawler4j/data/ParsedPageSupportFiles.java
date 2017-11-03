package edu.uci.ics.crawler4j.data;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.crawler4j.url.WebURL;

/**
 * This data class holds relevant information
 * about a parsed page support file namely lists
 * of the different file types (js/css/jpg etc) and
 * the WebURL information needed for later processing
 * and saving to the local file system or data layer
 * 
 * @author saleemhalipoto
 *
 */
public class ParsedPageSupportFiles {
	
    List<byte[]> listOfSupportFileBinaryData = new ArrayList<>(); 
    List<String> listOfSupportFileTextData = new ArrayList<>();
    List<String> listOfSupportFileUnknownType = new ArrayList<>();
    List<byte[]> listOfSupportFileDefaultCaseSwitchType = new ArrayList<>();
    WebURL webURL = null;
    
	public ParsedPageSupportFiles(List<byte[]> listOfSupportFileBinaryData, 
								 List<String> listOfSupportFileTextData,  
								 List<String> listOfSupportFileUnknownType, 
								 List<byte[]> listOfSupportFileDefaultCaseSwitchType, 
								 WebURL webURL) {		
		this.listOfSupportFileBinaryData = listOfSupportFileBinaryData;
		this.listOfSupportFileTextData = listOfSupportFileTextData;
		this.listOfSupportFileUnknownType = listOfSupportFileUnknownType;
		this.listOfSupportFileDefaultCaseSwitchType = listOfSupportFileDefaultCaseSwitchType;
		this.webURL = webURL;
	}
	
	public String ExtractFileExtensionFromFilename(WebURL webURL) {
		StringBuilder path = new StringBuilder(webURL.getPath());
		int index = path.lastIndexOf(".");
		String extension = path.substring(index);
		return extension;		
	}

	public List<byte[]> getListOfSupportFileBinaryData() {
		return listOfSupportFileBinaryData;
	}

	public void setListOfSupportFileBinaryData(List<byte[]> listOfSupportFileBinaryData) {
		this.listOfSupportFileBinaryData = listOfSupportFileBinaryData;
	}

	public List<String> getListOfSupportFileTextData() {
		return listOfSupportFileTextData;
	}

	public void setListOfSupportFileTextData(List<String> listOfSupportFileTextData) {
		this.listOfSupportFileTextData = listOfSupportFileTextData;
	}

	public List<String> getListOfSupportFileUnknownType() {
		return listOfSupportFileUnknownType;
	}

	public void setListOfSupportFileUnknownType(List<String> listOfSupportFileUnknownType) {
		this.listOfSupportFileUnknownType = listOfSupportFileUnknownType;
	}

	public List<byte[]> getListOfSupportFileDefaultCaseSwitchType() {
		return listOfSupportFileDefaultCaseSwitchType;
	}

	public void setListOfSupportFileDefaultCaseSwitchType(List<byte[]> listOfSupportFileDefaultCaseSwitchType) {
		this.listOfSupportFileDefaultCaseSwitchType = listOfSupportFileDefaultCaseSwitchType;
	}

	public WebURL getWebURL() {
		return webURL;
	}

	public void setWebURL(WebURL webURL) {
		this.webURL = webURL;
	}
}
