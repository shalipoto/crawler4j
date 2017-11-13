package edu.uci.ics.crawler4j.savepage.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.data.CompleteWebPageDTO;
import edu.uci.ics.crawler4j.data.HtmlUrlWithFilename;
import edu.uci.ics.crawler4j.data.ParsedPageSupportFiles;
import edu.uci.ics.crawler4j.data.SupportFileWithURL;

/**
 * This implements the SaveWebPageService interface and 
 * is intended to save a single web page (and support files) to the 
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
	 * @param setOfAllHtmlFilesWithUrls 
	 */
	@Override
	public void SaveCompleteWebPage(CompleteWebPageDTO pageDTO, String location, HashSet<HtmlUrlWithFilename<String, String>> setOfAllHtmlFilesWithUrls) {	
		SaveHtmlOnly(pageDTO, location, setOfAllHtmlFilesWithUrls);	// delegate html page saving to existing method
		
		// Get the list of support files for the CompleteWebPage
		List<SupportFileWithURL<byte[], String>> listOfSupportFileBinaryData = pageDTO.getParsedPageSupportFiles().getListOfSupportFileBinaryData();
		List<SupportFileWithURL<String, String>> listOfSupportFileTextData = pageDTO.getParsedPageSupportFiles().getListOfSupportFileTextData();
		List<SupportFileWithURL<String, String>> listOfSupportFileUnknownType = pageDTO.getParsedPageSupportFiles().getListOfSupportFileUnknownType();
		List<SupportFileWithURL<String, String>> listOfSupportFileDefaultCaseSwitchType = pageDTO.getParsedPageSupportFiles().getListOfSupportFileDefaultCaseSwitchType();
		
		// Trim the file extension from the htmlfilename
		StringBuilder sb = new StringBuilder(pageDTO.getHtmlFileName());
		String fileNameWithoutExtension = new String(sb.substring(0, sb.lastIndexOf(".")));
				
		// generate folder name for the list of support files and create it at the named location
		File supportFileFolder = new File(location + "/" + fileNameWithoutExtension + "_files");
		
		// Create the folder on the file system
        if (!supportFileFolder.exists()) {
            if (supportFileFolder.mkdirs())
                logger.debug("Created folder: " + supportFileFolder.getPath());
            else {
            	File testFolder = new File(location + "/" + "test" + "_files");
            	testFolder.mkdir();
            	logger.debug("Service had no choice but to make a test folder instead");
            }
        } else {
        	logger.debug("Folder already exists at path: " + supportFileFolder.getAbsolutePath());
        }
		
		logger.debug("SaveWebPageServiceImpl has created a support file folder named: " + supportFileFolder);
		logger.debug("The full path of the support file folder is: " + supportFileFolder.getAbsolutePath());
		
		//************************************************************************************************************
		// Process the list of BINARY contentType files for saving to the file system or data layer
		for (SupportFileWithURL<byte[], String> sfWithUrl : listOfSupportFileBinaryData) {
			
			FileOutputStream fileOutputStream = null;
			// Save the list of binary support files to the generated folder for support files
			//for (SupportFileWithURL<byte[], String> sfWithUrl : sf.getListOfSupportFileBinaryData()) {
				try {
					// Extract and process the filename information
					StringBuffer binaryFileNamePath = new StringBuffer(sfWithUrl.getUrlString());
					String binaryFileName = binaryFileNamePath.substring(binaryFileNamePath.lastIndexOf("/"));
					
					// generate filename with directory as parent					
					File saveBinaryFile = new File(supportFileFolder.getPath() + "/" + binaryFileName);
					
					// Create the empty file with filename generated as above
					fileOutputStream = new FileOutputStream(new File(saveBinaryFile.getPath()));
		            logger.debug("Created empty file: " + saveBinaryFile.getPath());
		            
			        /*
			         * Writes a byte object to a file
			         */
					//ObjectOutputStream objStream = new ObjectOutputStream(fileOutputStream); 
		            fileOutputStream.write(sfWithUrl.getDataFile());
		            logger.debug("Saved html contents to file: " + saveBinaryFile.getPath());
				} catch (IOException e) {
					e.printStackTrace();			
				} finally {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
		//************************************************************************************************************
		
		
		//************************************************************************************************************
		// Process the list of TEXT contentType files for saving to the file system or data layer
		for (SupportFileWithURL<String, String> sfWithUrl : listOfSupportFileTextData) {
			
			FileOutputStream fileOutputStream = null;
			// Save the list of binary support files to the generated folder for support files
			//for (SupportFileWithURL<byte[], String> sfWithUrl : sf.getListOfSupportFileBinaryData()) {
				try {
					// Extract and process the filename information
					StringBuffer binaryFileNamePath = new StringBuffer(sfWithUrl.getUrlString());
					String binaryFileName = binaryFileNamePath.substring(binaryFileNamePath.lastIndexOf("/"));
					
					// generate filename with directory as parent					
					File saveBinaryFile = new File(supportFileFolder.getPath() + "/" + binaryFileName);
					
					// Create the empty file with filename generated as above
					fileOutputStream = new FileOutputStream(new File(saveBinaryFile.getPath()));
		            logger.debug("Created empty file: " + saveBinaryFile.getPath());
		            
			        /*
			         * Writes a serializable object to a file
			         */
					ObjectOutputStream objStream = new ObjectOutputStream(fileOutputStream); 
					objStream.writeObject(sfWithUrl.getDataFile());
		            logger.debug("Saved html contents to file: " + saveBinaryFile.getPath());
		            
		            //Util.associateUrlWithFilename(sfWithUrl.getUrlString(), binaryFileName);
		            
				} catch (IOException e) {
					e.printStackTrace();			
				} finally {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
		//************************************************************************************************************
		
		//************************************************************************************************************		
		// Process the list of TEXT contentType files for saving to the file system or data layer
		logger.debug("In the SaveWebPageServiceImpl, printing out the listOfSupportFileUnknownType");
		for (SupportFileWithURL<String, String> sfWithUrl : listOfSupportFileUnknownType) {
			logger.debug(sfWithUrl.getUrlString());
		}
		
		
		
		
		
		
		
		
		
		
		//************************************************************************************************************
		
		System.out.print(""); // A line just to have a valid statement for debugging
	}

	/**
	 * Saves the html file to the local file system
	 */
	@Override
	public void SaveHtmlOnly(CompleteWebPageDTO pageDTO, String location, HashSet<HtmlUrlWithFilename<String, String>> setOfAllHtmlFilesWithUrls) {		
        File folder = new File(location);	// relative to crawler project root
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

	@Override
	public void addFileToUrlFilenameSet() {
		// TODO Auto-generated method stub
		
	}
}
