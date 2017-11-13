package edu.uci.ics.crawler4j.savepage.services;

import java.util.HashSet;

import edu.uci.ics.crawler4j.data.CompleteWebPageDTO;
import edu.uci.ics.crawler4j.data.HtmlUrlWithFilename;

/**
 * This interface provides the services for 
 * the crawler to gather the files needed to 
 * save a web page for offline viewing
 * 
 * @author saleemhalipoto
 *
 */
public interface SaveWebPageService {

	void SaveCompleteWebPage(CompleteWebPageDTO pageDTO, String location, HashSet<HtmlUrlWithFilename<String, String>> setOfAllHtmlFilesWithUrls);
	
	void SaveHtmlOnly(CompleteWebPageDTO pageDTO, String location, HashSet<HtmlUrlWithFilename<String, String>> setOfAllHtmlFilesWithUrls);
	
	/**
	 * Add a file to the set of all urls
	 * associated with a crawling session
	 * for processing of the hyperlinks
	 */
	void addFileToUrlFilenameSet ();

}
