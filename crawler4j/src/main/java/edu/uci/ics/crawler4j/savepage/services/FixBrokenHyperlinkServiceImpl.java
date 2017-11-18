package edu.uci.ics.crawler4j.savepage.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.savepage.controller.SaveWebPageMain;
import edu.uci.ics.crawler4j.savepage.crawlconfig.SaveWebPageCrawlConfig;

public class FixBrokenHyperlinkServiceImpl implements FixBrokenHyperlinkService {

    private static final Logger logger = LoggerFactory.getLogger(FixBrokenHyperlinkServiceImpl.class);
	SaveWebPageCrawlConfig config = null;
	Properties prop = new Properties();
	FileInputStream input = null;
	File currentHtmlFile = null;
	List <String> directoryListofFIles = null;
	Iterator<Object> listOfPageUrls = null;
	Collection<Object> listOfFilenames = null;
	
	public FixBrokenHyperlinkServiceImpl(SaveWebPageCrawlConfig config) {
		this.config = config;
	}
	
	@Override
	public void PutOriginalUrlInComments() {
		// TODO Auto-generated method stub

	}

	@Override
	public void PointHyperlinksToLocal() {
		String propDirectory = config.getSavePageFolderName();
		String propFileName = config.getFilenameAssociationsPropFile_name();
		try {
			input = new FileInputStream("./" + propFileName);// below root dir
			prop.load(input);
			listOfPageUrls = (Iterator<Object>) prop.keys();
			while (listOfPageUrls.hasNext()) {
				String filename = (String) prop.get(listOfPageUrls.next()); // get value (filename) using key
				if (filename.contains(".htm")) {
				currentHtmlFile = new File(filename);
				editTheFile(filename);
				logger.debug("edited the file: " + filename);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.debug("Could not close input file from the FixBrokenHyperlinkServiceImpl service");
				e.printStackTrace();
			}
		}


		// search and replace urls with the corresponding filename for all the html files
		// close all resources
		

	}
	
	private void editTheFile(String filename) {
		String nextUrl = null;
		String htmlFileString = null;
		String filenameValue = null;
		FileWriter fw = null;
		FileReader fr = null;
		try {
	        File file = new File("fixedlinks");
	        if (!file.exists()) {
	            if (file.mkdir()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }
			// Load the string with the html file contents
			htmlFileString = new String();
			String s = new String();
			fr = new FileReader("./" + config.getSavePageFolderName() + "/" + currentHtmlFile);
			try (BufferedReader br = new BufferedReader(fr)) {
				while ((s = br.readLine()) != null) {
					htmlFileString += s; // whole html file is into one string
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Iterator<Object> listOfPageUrls = (Iterator<Object>) prop.keys(); // method scope needed
			/*
			 *  Search current html file for each url of other pages
			 *  and replace the url with the filename
			 */
			while (listOfPageUrls.hasNext()) {
				nextUrl = (String) listOfPageUrls.next(); // property file key
				filenameValue = prop.getProperty(nextUrl); // property file value
				htmlFileString = htmlFileString.replace(nextUrl, filenameValue);
			}		
			fw = new FileWriter("./" + "fixedlinks" + "/" + currentHtmlFile);
			fw.write(htmlFileString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fw.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
}
