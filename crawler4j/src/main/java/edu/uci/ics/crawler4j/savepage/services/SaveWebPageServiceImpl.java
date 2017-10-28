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

	@Override
	public void SaveHtmlOnly(CompleteWebPageDTO pageDTO, String location) {
		
		// Save the html file to the local file system,  in the crawlStorageFolder
		
		
        File folder = new File("tempfolder");
        
        if (!folder.exists()) {
            if (folder.mkdirs()) 
                logger.debug("Created folder: " + folder.getAbsolutePath());
        } else {
        	logger.debug("Folder already exists at path: " + folder.getAbsolutePath());
        }
		try {
			FileOutputStream file = new FileOutputStream("tempfolder/tempfile.html");
            logger.debug("Created file: " + "/tempfolder/tempfile.html");
            
			ObjectOutputStream objStream = new ObjectOutputStream(file);
			objStream.writeObject(pageDTO.getWebPageHtmlContents());
            logger.debug("Saved html contents to file: " + "/tempfolder/tempfile.html");

		} catch (IOException e) {
			e.printStackTrace();
			
		}		
	}
}
