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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.data.CompleteWebPageDTO;
import edu.uci.ics.crawler4j.data.UrlWithFilename;
import edu.uci.ics.crawler4j.savepage.crawlconfig.SaveWebPageCrawlConfig;
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
    SaveWebPageCrawlConfig config = null;
    HashSet<UrlWithFilename<String, String>> setOfAllUrlsWithFilenames = null;
    
    public SaveWebPageServiceImpl(SaveWebPageCrawlConfig config, HashSet<UrlWithFilename<String, String>> setOfAllUrlsWithFilenames) {
		this.config = config;
		this.setOfAllUrlsWithFilenames = setOfAllUrlsWithFilenames;
    }
	/**
	 * @param pageDTO the DTO instantiated and populated by the WebCrawler instance
	 * @param location the crawlStorageFolder named by an argument to main()
	 * @param setOfAllHtmlFilesWithUrls 
	 */
	@Override
	public void saveCompleteWebPage(CompleteWebPageDTO pageDTO, HashSet<UrlWithFilename<String, String>> setOfAllUrlsWithFilenames, Page page) {	
		String location = config.getSavePageFolderName();
		saveHtmlOnly(pageDTO, location, setOfAllUrlsWithFilenames, page);	// delegate html page saving to existing method
		
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
		if (listOfSupportFileBinaryData!= null) { 
			for (SupportFileWithURL<byte[], String> sfWithUrl : listOfSupportFileBinaryData) {
		
			
			File saveBinaryFile = null;
			FileOutputStream fileOutputStream = null;
			// Save the list of binary support files to the generated folder for support files
			//for (SupportFileWithURL<byte[], String> sfWithUrl : sf.getListOfSupportFileBinaryData()) {
				try {
					// Extract and process the filename information
					StringBuffer binaryFileNamePath = new StringBuffer(sfWithUrl.getUrlString());
					String binaryFileName = binaryFileNamePath.substring(binaryFileNamePath.lastIndexOf("/"));
					
					// generate filename with directory as parent					
					saveBinaryFile = new File(supportFileFolder.getPath() + "/" + binaryFileName);
					
					// Create the empty file with filename generated as above
					fileOutputStream = new FileOutputStream(new File(saveBinaryFile.getPath()));
		            logger.debug("Created empty file: " + saveBinaryFile.getPath());
		            
			        /*
			         * Writes a byte object to a file
			         */
					//ObjectOutputStream objStream = new ObjectOutputStream(fileOutputStream); 
		            fileOutputStream.write(sfWithUrl.getDataFile());
		            logger.debug("Saved binary contents to file: " + saveBinaryFile.getPath());
		            
		            // Add the file to the global list of urls paired with filenames to fix broken links
		            addFileToUrlFilenameSet(sfWithUrl.getUrlString(), saveBinaryFile.getPath(), setOfAllUrlsWithFilenames);
				} catch (IOException e) {
		            logger.debug("Error saving binary contents to file: " + sfWithUrl.getUrlString());
		            logger.debug("This file also was not added to the global set: setOfAllFilesWithUrls");
					e.printStackTrace();			
				} finally {
					try {
						if (fileOutputStream != null) fileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			logger.debug("The listOfSupportFileBinaryData list is empty");
		}
		//************************************************************************************************************
		
		
		//************************************************************************************************************
		// Process the list of TEXT contentType files for saving to the file system or data layer
		if (listOfSupportFileTextData != null) {
			for (SupportFileWithURL<String, String> sfWithUrl : listOfSupportFileTextData) {
			
			FileOutputStream fileOutputStream = null;
			ObjectOutputStream objStream = null;
			// Save the list of binary support files to the generated folder for support files
			//for (SupportFileWithURL<byte[], String> sfWithUrl : sf.getListOfSupportFileBinaryData()) {
				try {
					// Extract and process the filename information
					StringBuffer textFileNamePath = new StringBuffer(sfWithUrl.getUrlString());
					String textFileName = textFileNamePath.substring(textFileNamePath.lastIndexOf("/"));
					
					// generate filename with directory as parent					
					File saveTextFile = new File(supportFileFolder.getPath() + "/" + textFileName);
					
					// Create the empty file with filename generated as above
					fileOutputStream = new FileOutputStream(new File(saveTextFile.getPath()));
		            logger.debug("Created empty file: " + saveTextFile.getPath());
		            
			        /*
			         * Writes a serializable object to a file
			         */
					objStream = new ObjectOutputStream(fileOutputStream); 
					objStream.writeObject(sfWithUrl.getDataFile());
		            logger.debug("Saved text contents to file: " + saveTextFile.getPath());
		            
		            // Add the file to the global list of urls paired with filenames to fix broken links
		            addFileToUrlFilenameSet(sfWithUrl.getUrlString(), saveTextFile.getPath(), setOfAllUrlsWithFilenames);	            
				} catch (IOException e) {
					e.printStackTrace();			
				} finally {
					try {
						if (fileOutputStream != null) fileOutputStream.close();
						if (objStream != null) objStream.close();
					} catch (IOException e) {
			            logger.debug("Error saving text contents to file: " + sfWithUrl.getUrlString());
			            logger.debug("This file also was not added to the global set: setOfAllFilesWithUrls");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			logger.debug("The listOfSupportFileTextData list is empty");
		}
		//************************************************************************************************************
		
		//************************************************************************************************************		
		// Process the list of UNKNOWN contentType files for saving to the file system or data layer
		if (listOfSupportFileUnknownType == null) {
			logger.debug("In the SaveWebPageServiceImpl, listOfSupportFileUnknownType is empty");
		} else {
			logger.debug("In the SaveWebPageServiceImpl, printing out the listOfSupportFileUnknownType");
			for (SupportFileWithURL<String, String> sfWithUrl : listOfSupportFileUnknownType) {
				logger.debug(sfWithUrl.getUrlString());
		}
	
		//************************************************************************************************************
		
		/*
		 * The prop file now holds all urls
		 * and now can be used to point all 
		 * hyperlinks to the local file system
		 */
		saveUrlsToPropFile();
		
		System.out.print(""); // A line just to have a valid statement for debugging
		}
	}

	/**
	 * Saves the html file to the local file system
	 */
	@Override
	public void saveHtmlOnly(CompleteWebPageDTO pageDTO, String location, HashSet<UrlWithFilename<String, String>> setOfAllUrlsWithFilenames, Page page) {		
        File folder = new File(location);	// relative to crawler project root
        ObjectOutputStream objStream = null;
        FileOutputStream fileOutputStream = null;
		try {
			// generate filename with directory as parent
			File saveHtmlOnlyFile = new File(folder.getPath() + "/" + pageDTO.getHtmlFileName());
			
			// Create the empty file with filename generated as above
			fileOutputStream = new FileOutputStream(new File(saveHtmlOnlyFile.getPath()));
            logger.debug("Created html file: " + saveHtmlOnlyFile.getPath());
            
	        /*
	         * Writes a serializable object to a file
	         */
			objStream = new ObjectOutputStream(fileOutputStream); 
			objStream.writeObject(pageDTO.getWebPageHtmlContents());
            logger.debug("Saved html contents to html file: " + saveHtmlOnlyFile.getPath());
            
            // Add the file to the global list of urls paired with filenames to fix broken links
            addFileToUrlFilenameSet(page.getWebURL().getURL(), pageDTO.getHtmlFileName(), setOfAllUrlsWithFilenames);
            logger.debug("Saved html file with url to addFileToUrlFilenameSet ");
		} catch (IOException e) {
            logger.debug("Error saving html contents to file: " + pageDTO.getHtmlFileName());
            logger.debug("This file also was not added to the global set: setOfAllFilesWithUrls");
			e.printStackTrace();			
		} finally {
			try {
				if (objStream != null) objStream.close();
				if (fileOutputStream != null) fileOutputStream.close();
			} catch (IOException e) {e.printStackTrace(); }
		}
	}
	 
	@Override
	public void addFileToUrlFilenameSet(String url, String filename, HashSet<UrlWithFilename<String, String>> setOfAllUrlsWithFilenames) {
		UrlWithFilename<String, String> urlWithFilename = new UrlWithFilename<String, String>();
		urlWithFilename.setOriginalUrl(url);
		urlWithFilename.setLocalFilename(filename);
		
		// hashSet object Not synchronized
		if (!setOfAllUrlsWithFilenames.contains(urlWithFilename)) {
			setOfAllUrlsWithFilenames.add(urlWithFilename);
	        logger.debug("Saved file: " + 
	        					filename + 
	        					" having url: " + 
	        					url + 
	        					" to global set: setOfAllUrlsWithFilenames");
		}
		else logger.debug("The setOfAllFilesWithUrls has found a duplicate url: " + url);
	}

	@Override
	public void saveUrlsToPropFile() {
		Properties prop = new Properties();
		FileOutputStream fileOutputStream = null;	
		try {
			fileOutputStream = new FileOutputStream(config.getFilenameAssociationsPropFile_name());
			for (UrlWithFilename<String, String> urlWithFilename : setOfAllUrlsWithFilenames) {
				prop.setProperty(urlWithFilename.getOriginalUrl(), urlWithFilename.getLocalFilename());
				
				// Save the property to the local prop file
				prop.store(fileOutputStream, null);
				logger.debug("");
				logger.debug("url: " + urlWithFilename.getOriginalUrl());
				logger.debug(" having filename: " + urlWithFilename.getLocalFilename());
				logger.debug(" saved to prop file");
				logger.debug("");
			}
		} catch (IOException e) {e.printStackTrace();
		}	finally {
			try {
				if (fileOutputStream != null)
					fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
