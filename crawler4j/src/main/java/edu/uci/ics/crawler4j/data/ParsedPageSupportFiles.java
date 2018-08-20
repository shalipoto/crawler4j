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



	List<SupportFileWithURL<byte[], String>> listOfSupportFileBinaryData = new ArrayList<>(); 
    List<SupportFileWithURL<String, String>> listOfSupportFileTextData = new ArrayList<>();
    List<SupportFileWithURL<String, String>> listOfSupportFileUnknownType = new ArrayList<>();
    List<SupportFileWithURL<String, String>> listOfSupportFileDefaultCaseSwitchType = new ArrayList<>();
    
	public ParsedPageSupportFiles(List<SupportFileWithURL<byte[], String>> listOfSupportFileBinaryData, 
								  List<SupportFileWithURL<String, String>> listOfSupportFileTextData,  
								  List<SupportFileWithURL<String, String>> listOfSupportFileUnknownType, 
								  List<SupportFileWithURL<String, String>> listOfSupportFileDefaultCaseSwitchType
								  ) {		
		this.listOfSupportFileBinaryData = listOfSupportFileBinaryData;
		this.listOfSupportFileTextData = listOfSupportFileTextData;
		this.listOfSupportFileUnknownType = listOfSupportFileUnknownType;
		this.listOfSupportFileDefaultCaseSwitchType = listOfSupportFileDefaultCaseSwitchType;
	}
	
	public String ExtractFileExtensionFromFilename(WebURL webURL) {
		StringBuilder path = new StringBuilder(webURL.getPath());
		int index = path.lastIndexOf(".");
		String extension = path.substring(index);
		return extension;		
	}
	
    public List<SupportFileWithURL<byte[], String>> getListOfSupportFileBinaryData() {
		return listOfSupportFileBinaryData;
	}

	public void setListOfSupportFileBinaryData(List<SupportFileWithURL<byte[], String>> listOfSupportFileBinaryData) {
		this.listOfSupportFileBinaryData = listOfSupportFileBinaryData;
	}

	public List<SupportFileWithURL<String, String>> getListOfSupportFileTextData() {
		return listOfSupportFileTextData;
	}

	public void setListOfSupportFileTextData(List<SupportFileWithURL<String, String>> listOfSupportFileTextData) {
		this.listOfSupportFileTextData = listOfSupportFileTextData;
	}

	public List<SupportFileWithURL<String, String>> getListOfSupportFileUnknownType() {
		return listOfSupportFileUnknownType;
	}

	public void setListOfSupportFileUnknownType(List<SupportFileWithURL<String, String>> listOfSupportFileUnknownType) {
		this.listOfSupportFileUnknownType = listOfSupportFileUnknownType;
	}

	public List<SupportFileWithURL<String, String>> getListOfSupportFileDefaultCaseSwitchType() {
		return listOfSupportFileDefaultCaseSwitchType;
	}
	
	public void setListOfSupportFileDefaultCaseSwitchType(
			List<SupportFileWithURL<String, String>> listOfSupportFileDefaultCaseSwitchType) {
		this.listOfSupportFileDefaultCaseSwitchType = listOfSupportFileDefaultCaseSwitchType;
	}
}
