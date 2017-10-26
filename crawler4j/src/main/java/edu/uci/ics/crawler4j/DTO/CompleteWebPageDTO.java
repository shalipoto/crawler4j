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
	
	String htmlContents = null; // the contents of the original html file
	
	List <String> webPageFiles = null; // a list of support files needed for offline viewing

	public String getWebPageName() {
		return webPageName;
	}

	public void setWebPageName(String webPageName) {
		this.webPageName = webPageName;
	}

	public String getHtmlContents() {
		return htmlContents;
	}

	public void setHtmlContents(String htmlContents) {
		this.htmlContents = htmlContents;
	}

	public List<String> getWebPageFiles() {
		return webPageFiles;
	}

	public void setWebPageFiles(List<String> webPageFiles) {
		this.webPageFiles = webPageFiles;
	}
	
	
}
