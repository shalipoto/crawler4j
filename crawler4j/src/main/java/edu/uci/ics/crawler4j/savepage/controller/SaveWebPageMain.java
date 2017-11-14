/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.savepage.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.savepage.crawlconfig.SaveWebPageCrawlConfig;
import edu.uci.ics.crawler4j.savepage.crawler.SavePageWebCrawler;

/**
 * This is the main entry point for the crawler application
 * 
 * @author Saleem Halipoto
 * 
 * CrawlController class authored by Yasser Ganjisaffar
 * This class starts with the codebase of BasicCrawlController, 
 * also authored by Yasser Ganjisaffar
 */
public class SaveWebPageMain {
    private static final Logger logger = LoggerFactory.getLogger(SaveWebPageMain.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            logger.info("Needed parameters: ");
            logger.info("\t rootFolder (it will contain intermediate crawl data)");
            logger.info("\t numberOfCralwers (number of concurrent threads)");
            return;
        }

    /*
     * crawlStorageFolder is a folder where intermediate crawl data is
     * stored.
     */
        String crawlStorageFolder = args[0];

    /*
     * numberOfCrawlers shows the number of concurrent threads that should
     * be initiated for crawling.
     */
        int numberOfCrawlers = Integer.parseInt(args[1]);

        SaveWebPageCrawlConfig config = new SaveWebPageCrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);
        
    /* 
     * The savePageFolderName in the SaveWebPageCrawlConfig file
     * holds the folder name where saved web pages will be stored.
     * The name is set in a properties file located at the project root
     */
        Properties prop = new Properties();
        FileInputStream input = null;
    	try {
    		input = new FileInputStream("savewebpage.properties");

    		// load a properties file
    		prop.load(input);
    		
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	config.setSavePageFolderName(prop.getProperty("pagestoragelocation"));
    	
    /*
     * 
     * 
     */
        File folder = new File(config.getSavePageFolderName());	// relative to crawler project root

        if (!folder.exists()) {
            if (folder.mkdirs())
                logger.debug("Created folder: " + folder.getPath());
            else logger.error("Error in creating the page storage folder");
        } else {
        	logger.debug("Folder already exists at path: " + folder.getAbsolutePath());
        }  	
    	
    /*
     * Create a properties file to hold a list of 
     * key value pairs associating a url's with 
     * local filenames. Save its name in the 
     * config file.
     */
		File filenameAssociationsPropFile = new File(config.getSavePageFolderName() + "/" + "urltofilenamelookup.properties");
		
		// Create the properties file on the file system
        if (!filenameAssociationsPropFile.exists()) {
            if (filenameAssociationsPropFile.createNewFile()) {
                config.setFilenameAssociationsPropFile_name("urltofilenamelookup");
                logger.debug("Created properties file: " + filenameAssociationsPropFile.getPath());
            }
            else {
            	logger.error("Could not create properties file: ", filenameAssociationsPropFile.getName());
            }
        } else {
        	logger.debug("Properties file already exists at path: " + filenameAssociationsPropFile.getAbsolutePath());
        }

    /*
     * Be polite: Make sure that we don't send more than 1 request per
     * second (1000 milliseconds between requests).
     */
        config.setPolitenessDelay(1000);

    /*
     * You can set the maximum crawl depth here. The default value is -1 for
     * unlimited depth
     */
        config.setMaxDepthOfCrawling(4);

    /*
     * You can set the maximum number of pages to crawl. The default value
     * is -1 for unlimited number of pages
     */
        config.setMaxPagesToFetch(1000);

        /**
         * Do you want crawler4j to crawl also binary data ?
         * example: the contents of pdf, or the metadata of images etc
         */
        config.setIncludeBinaryContentInCrawling(true);

    /*
     * Do you need to set a proxy? If so, you can use:
     * config.setProxyHost("proxyserver.example.com");
     * config.setProxyPort(8080);
     *
     * If your proxy also needs authentication:
     * config.setProxyUsername(username); config.getProxyPassword(password);
     */

    /*
     * This config parameter can be used to set your crawl to be resumable
     * (meaning that you can resume the crawl from a previously
     * interrupted/crashed crawl). Note: if you enable resuming feature and
     * want to start a fresh crawl, you need to delete the contents of
     * rootFolder manually.
     */
        config.setResumableCrawling(false);

    /*
     * Instantiate the controller for this crawl.
     */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    /*
     * For each crawl, you need to add some seed urls. These are the first
     * URLs that are fetched and then the crawler starts following links
     * which are found in these pages
     */
        
        //controller.addSeed("https://www.scifigeeks.com/");
        //controller.addSeed("http://www.ics.uci.edu/");
        //controller.addSeed("http://www.ics.uci.edu/~lopes/");
	    //controller.addSeed("http://www.ics.uci.edu/~welling/");
	    //controller.addSeed("http://shop.storiedthreads.com/main.sc");
	    //controller.addSeed("https://www.etsy.com/market/iron_on_patch");   
	    //controller.addSeed("https://docs.docker.com/get-started/");   
	    //controller.addSeed("https://www.popularpatch.com/");
	    //controller.addSeed("https://www.heropatches.com/");	    
	    //controller.addSeed("http://www.robewares.com/");
	    controller.addSeed("http://www.trs-80.com/wordpress/trs-80-computer-line/coco/");
	    //controller.addSeed("https://archive.org/details/computermagazines?sort=&and[]=collection%3A%22rainbowmagazine%22");  
        
        
	            

    /*
     * Start the crawl. This is a blocking operation, meaning that your code
     * will reach the line after this only when crawling is finished.
     */
        controller.start(SavePageWebCrawler.class, numberOfCrawlers);
    }
    // Call the services needed to get all hyperlinks in all the saved
    // web pages working correctly
    
}
