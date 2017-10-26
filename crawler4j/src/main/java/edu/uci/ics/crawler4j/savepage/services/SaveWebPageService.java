package edu.uci.ics.crawler4j.savepage.services;

import edu.uci.ics.crawler4j.DTO.CompleteWebPageDTO;

/**
 * This interface provides the services for 
 * the crawler to gather the files needed to 
 * save a web page for offline viewing
 * 
 * @author saleemhalipoto
 *
 */
public interface SaveWebPageService {

	void SaveCompleteWebPage(CompleteWebPageDTO pageDTO, String location);
	
	void SaveHtmlOnly();
}
