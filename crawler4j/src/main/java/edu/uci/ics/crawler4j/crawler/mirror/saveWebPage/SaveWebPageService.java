package edu.uci.ics.crawler4j.crawler.mirror.saveWebPage;

/**
 * This interface provides the services for 
 * the crawler to gather the files needed to 
 * save a web page for offline viewing
 * 
 * @author saleemhalipoto
 *
 */
public interface SaveWebPageService {

	void SaveCompleteWebPage();
	
	void SaveHtmlOnly();
}
