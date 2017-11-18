package edu.uci.ics.crawler4j.data;

import java.util.List;

import edu.uci.ics.crawler4j.crawler.Page;

/**
 * This object holds the web page data, generated during
 * default crawler behavior, relevant to other processes 
 * such as mirroring and persistence.
 * 
 * @author saleemhalipoto
 *
 */
public class CompleteWebPageDTO {
	
	Page page = null;
	
	String webPageSaveLocation = null;

	String webPageName = null; // the page name as expressed in the original html file
	
	String webPageHtmlContents = null; // the contents of the original html file
	
	List <String> webPageSupportFiles = null; // a list of support files needed for offline viewing
	
	String webPageFolder = null;	// the folder where support files are saved
	
	String htmlFileName = null;	// the filename for the saved page html contents
	
	//List<ParsedPageSupportFiles> listOfParsedPageSupportFiles = null;
	
	ParsedPageSupportFiles parsedPageSupportFiles = null;

	public String getWebPageSaveLocation() {
		return webPageSaveLocation;
	}

	public void setWebPageSaveLocation(String webPageSaveLocation) {
		this.webPageSaveLocation = webPageSaveLocation;
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

	public void setWebPageHtmlContents(String webPageHtmlContents) {
		this.webPageHtmlContents = webPageHtmlContents;
	}

	public List<String> getWebPageSupportFiles() {
		return webPageSupportFiles;
	}

	public ParsedPageSupportFiles getParsedPageSupportFiles() {
		return parsedPageSupportFiles;
	}

	public void setParsedPageSupportFiles(ParsedPageSupportFiles parsedPageSupportFiles) {
		this.parsedPageSupportFiles = parsedPageSupportFiles;
	}

	public void setWebPageSupportFiles(List<String> webPageSupportFiles) {
		this.webPageSupportFiles = webPageSupportFiles;
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
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
