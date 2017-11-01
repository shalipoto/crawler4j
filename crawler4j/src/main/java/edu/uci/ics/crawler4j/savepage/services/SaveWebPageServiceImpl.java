package edu.uci.ics.crawler4j.savepage.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.DTO.CompleteWebPageDTO;
import edu.uci.ics.crawler4j.crawler.CrawlController;

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

    static final Logger logger = LoggerFactory.getLogger(SaveWebPageServiceImpl.class);
	/**
	 * @param pageDTO the DTO instantiated and populated by the WebCrawler instance
	 * @param location the crawlStorageFolder named by an argument to main()
	 */
	@Override
	public void SaveCompleteWebPage(CompleteWebPageDTO pageDTO, String location) {	
		SaveHtmlOnly(pageDTO, location);	// delegate html page saving to existing method
		
		// Get the list of support files for the CompleteWebPage
		
		// Save the list of support files to the local file system
	}

	/**
	 * Saves the html file to the local file system
	 */
	@Override
	public void SaveHtmlOnly(CompleteWebPageDTO pageDTO, String location) {		
        File folder = new File(location);	// relative to crawler project root
        
        if (!folder.exists()) {
            if (folder.mkdirs())
                logger.debug("Created folder: " + folder.getPath());
        } else {
        	logger.debug("Folder already exists at path: " + folder.getAbsolutePath());
        }
		try {
			// generate filename with directory as parent
			File saveHtmlOnlyFile = new File(folder.getPath() + "/" + pageDTO.getHtmlFileName());
			
			// Create the empty file with filename generated as above
			FileOutputStream fileOutputStream = new FileOutputStream(new File(saveHtmlOnlyFile.getPath()));
            logger.debug("Created file: " + saveHtmlOnlyFile.getPath());
            
	        /*
	         * Writes a serializable object to a file
	         */
			ObjectOutputStream objStream = new ObjectOutputStream(fileOutputStream); 
			objStream.writeObject(pageDTO.getWebPageHtmlContents());
            logger.debug("Saved html contents to file: " + saveHtmlOnlyFile.getPath());
		} catch (IOException e) {
			e.printStackTrace();			
		}		
	}
}
