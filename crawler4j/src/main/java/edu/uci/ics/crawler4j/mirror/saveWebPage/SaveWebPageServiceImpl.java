package edu.uci.ics.crawler4j.mirror.saveWebPage;

import edu.uci.ics.crawler4j.DTO.CompleteWebPageDTO;

/**
 * This implements the SaveWebPageService interface and 
 * is intended to save a web page (and support files) to the 
 * local file system just as browser would when saving a page
 * 
 * @author saleemhalipoto
 *
 */
public class SaveWebPageServiceImpl implements SaveWebPageService{

	
	/**
	 * @param page, the page object generated in the parser
	 */
	@Override
	public void SaveCompleteWebPage(CompleteWebPageDTO pageDTO) {

		// Save the html file to the local file system
		
		// Get the list of support files for the CompleteWebPage
		
		// Save the list of support files to the local file system
		
		
	}

	@Override
	public void SaveHtmlOnly() {
		// TODO Auto-generated method stub
		
	}

}
