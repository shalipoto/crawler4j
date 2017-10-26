package edu.uci.ics.crawler4j.savepage.services;

import edu.uci.ics.crawler4j.DTO.CompleteWebPageDTO;

/**
 * This implements the SaveWebPageService interface and 
 * is intended to save a web page (and support files) to the 
 * local file system just as browser would when saving a 
 * complete web page.
 * 
 * @author Saleem Halipoto
 *
 */
public class SaveWebPageServiceImpl implements SaveWebPageService{

	
	/**
	 * @param pageDTO the DTO instantiated and populated by the WebCrawler instance
	 * @param location the crawlStorageFolder named by an argument to main()
	 */
	@Override
	public void SaveCompleteWebPage(CompleteWebPageDTO pageDTO, String location) {

		// Save the html file to the local file system,  in the crawlStorageFolder
		
		// Get the list of support files for the CompleteWebPage
		
		// Save the list of support files to the local file system
				
	}

	@Override
	public void SaveHtmlOnly() {
		// TODO implement when needed	
	}

}
