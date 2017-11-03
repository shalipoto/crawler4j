package edu.uci.ics.crawler4j.data.DTO;

import java.util.List;

/**
 * This object holds the web page data, generated during
 * default crawler behavior, relevant to other processes 
 * such as mirroring and persistence.
 * 
 * @author saleemhalipoto
 *
 */
public class CompleteWebPageDTO {
	
	String webPageSaveLocation = null;

	String webPageName = null; // the page name as expressed in the original html file
	
	String webPageHtmlContents = null; // the contents of the original html file
	
	List <String> webPageSupportFiles = null; // a list of support files needed for offline viewing
	
	String webPageFolder = null;	// the folder where support files are saved
	
	String htmlFileName = null;	// the filename for the saved page html contents
	
	List<byte[]> listOfSupportFileBinaryData = null; // a list of support files containing binary data eg. jpeg/bmp/png etc
	
	List<String> listOfSupportFileTextData = null; // a list of support files containing text data eg. html/js/css
	
	List<String> listOfSupportFileUnknownType = null; // a list of support files containing text data but of unknown file extension
	
	List<byte[]> listOfSupportFileDefaultCaseSwitchType = null; // a list of support files not identified by FileContentType enum in the parser

	List<ParsedPageSupportFiles> listOfParsedPageSupportFiles = null;
	
	public List<ParsedPageSupportFiles> getListOfParsedPageSupportFiles() {
		return listOfParsedPageSupportFiles;
	}

	public void setListOfParsedPageSupportFiles(List<ParsedPageSupportFiles> listOfParsedPageSupportFiles) {
		this.listOfParsedPageSupportFiles = listOfParsedPageSupportFiles;
	}

	public String getWebPageSaveLocation() {
		return webPageSaveLocation;
	}

	public void setWebPageSaveLocation(String webPageSaveLocation) {
		this.webPageSaveLocation = webPageSaveLocation;
	}

	public void setWebPageHtmlContents(String webPageHtmlContents) {
		this.webPageHtmlContents = webPageHtmlContents;
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

	public String getWebPageFolder() {
		return webPageFolder;
	}

	public void setWebPageFolder(String webPageFolder) {
		this.webPageFolder = webPageFolder;
	}

	public String getHtmlFileName() {
		return htmlFileName;
	}

	public void setHtmlFileName(String htmlFileName) {
		this.htmlFileName = htmlFileName;
	}

	public String getWebPageName() {
		return webPageName;
	}

	public void setWebPageName(String webPageName) {
		this.webPageName = webPageName;
	}

	public String getWebPageHtmlContents() {
		return webPageHtmlContents;
	}

	public void setHtmlContents(String htmlContents) {
		this.webPageHtmlContents = htmlContents;
	}

	public List<String> getWebPageSupportFiles() {
		return webPageSupportFiles;
	}

	public void setWebPageSupportFiles(List<String> webPageFiles) {
		this.webPageSupportFiles = webPageFiles;
	}
	
	
}
