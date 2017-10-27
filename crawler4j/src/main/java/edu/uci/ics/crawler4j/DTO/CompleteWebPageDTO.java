package edu.uci.ics.crawler4j.DTO;

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
	
	String webPageName = null; // the page name as expressed in the original html file
	
	String webPageHtmlContents = null; // the contents of the original html file
	
	List <String> webPageFiles = null; // a list of support files needed for offline viewing
	
	String webPageFolder = null;	// the folder where support files are saved
	
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

	String htmlFileName = null;	// the filename for the saved page html contents

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

	public List<String> getWebPageFiles() {
		return webPageFiles;
	}

	public void setWebPageFiles(List<String> webPageFiles) {
		this.webPageFiles = webPageFiles;
	}
	
	
}
