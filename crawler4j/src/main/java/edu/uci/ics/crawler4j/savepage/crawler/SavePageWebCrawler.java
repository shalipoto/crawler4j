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

package edu.uci.ics.crawler4j.savepage.crawler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.exceptions.ContentFetchException;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.crawler.exceptions.ParseException;
import edu.uci.ics.crawler4j.data.CompleteWebPageDTO;
import edu.uci.ics.crawler4j.data.UrlWithFilename;
import edu.uci.ics.crawler4j.data.ParsedPageSupportFiles;
import edu.uci.ics.crawler4j.data.SupportFileWithURL;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.NotAllowedContentException;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.savepage.crawlconfig.SaveWebPageCrawlConfig;
import edu.uci.ics.crawler4j.savepage.parser.SaveWebPageParser;
import edu.uci.ics.crawler4j.savepage.services.SaveWebPageServiceImpl;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.Util;
import edu.uci.ics.crawler4j.util.Util.FileContentType;

/**
 * This crawler's supeclass implements Runnable() and it's 
 * thread is created and managed by the crawlcontroller.
 * This crawler also adapts the original webcrawler behavior to handle the 
 * "save web page" use case rather than mining various files for specific data.
 * 
 * @author Saleem Halipoto
 * 
 * Yasser Ganjisaffar coded the superclass: WebCrawler and 
 * subclass: BasicCrawlController from which this controller gets its
 * starting code base.
 */
public class SavePageWebCrawler extends WebCrawler {	
	
	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png|css|svg)$");
	private static final Pattern HTML_EXTENSIONS = Pattern.compile(".*\\.(htm|html)$");

    SaveWebPageParser saveWebPageParser = null;
    SaveWebPageServiceImpl saveService = null;
	
	//Parser saveWebPageParser = null;
	SaveWebPageCrawlConfig saveWebPageCrawlConfig = null;
	
	/*
	 * A list of the URLs of the support files needed to display 
	 * a web page being saved to the local file system
	 * eg. css, js, png, bmp etc
	 */
	List <WebURL> listOfPageSupportFileURLs = null;
	
	/**
	 * This variable member will hold all the files with their
	 * original urls collected within a crawling session for processing
	 * later on where all hyperlinks will point to local files
	 */
	HashSet<UrlWithFilename<String, String>> setOfAllUrlsWithFilenames = new HashSet<UrlWithFilename<String, String>>();
			
    /**
     * Initializes the current instance of the crawler
     *
     * @param id the id of this crawler instance
     *            
     * @param crawlController the controller that manages this crawling session
     *            
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
	@Override
    public void init(int id, CrawlController crawlController) throws InstantiationException, IllegalAccessException {		
		logger.debug("In the init method of the SavePageWebCrawler");
    	myId = id;
        pageFetcher = crawlController.getPageFetcher();
        setRobotstxtServer(crawlController.getRobotstxtServer());
        setDocIdServer(crawlController.getDocIdServer());
        setFrontier(crawlController.getFrontier());
        myController = crawlController;
        setWaitingForNewURLs(false);

        /* 
         * Bring in the crawlconfig's subclass, namely 
         * saveWebPageCrawlConfig and set the 
         * parser to the SaveWebPageParser
         */
    	saveWebPageCrawlConfig = (SaveWebPageCrawlConfig) crawlController.getConfig();        
        setParser(new SaveWebPageParser(saveWebPageCrawlConfig));
        saveWebPageParser = (SaveWebPageParser) getParser();       
        saveService = new SaveWebPageServiceImpl(saveWebPageCrawlConfig, setOfAllUrlsWithFilenames);
    }   
	
    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     * 
     * Here if a URL matches the IMAGE_EXTENSIONS pattern then it is also
     * added to a list of URLs for support files of the parent page.
     * @param listOfPageSupportFileURLs
     */
    public boolean shouldVisit(Page referringPage, WebURL url, List<WebURL> listOfPageSupportFileURLs) {
        String href = url.getURL().toLowerCase();
        String charSet = referringPage.getContentCharset(); // Needed to identify html pages not using .html or .htm extensions
        
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
        	listOfPageSupportFileURLs.add(url);	// Add this URL to the list of support file urls
        	logger.debug("Added this URL to the listOfPageSupportFileURLs: " + href);
            return false;
        } else if (HTML_EXTENSIONS.matcher(href).matches() | charSet.contains("html") | charSet.contains("HTML")) {
        	logger.debug("This url is included in \"should visit\": " + href);
        	
            // Only accept the url if it is in the "https://docs.docker.com" domain and protocol is "https".
            return href.startsWith("http://www.trs-80.com/");
        } else if (href.contains(".htm") | href.contains(".html")) {
        	logger.debug("This url is included in \"should visit\": " + href);
        	
            // Only accept the url if it is in the "https://docs.docker.com" domain and protocol is "https".
            return href.startsWith("http://www.trs-80.com/");
        } else { // Catches all non-matching URLs and will be treated as pages to visit
        	//listOfPageSupportFileURLs.add(url);	// Add this URL to the list of support file urls
        	logger.debug("Not matching any existing criteria, considering this url to visit anyway: " + href);
            return href.startsWith("http://www.trs-80.com/");
        }
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program. This is an overload of the superclass visit() method
     * @param listOfPageSupportFileURLs2 
     */
    public void visit(Page page, List<WebURL> listOfPageSupportFileURLs) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        logger.debug("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.debug("Domain: '{}'", domain);
        logger.debug("Sub-domain: '{}'", subDomain);
        logger.debug("Path: '{}'", path);
        logger.debug("Parent page: {}", parentUrl);
        logger.debug("Anchor text: {}", anchor);
        logger.debug("In the visit() method of the SavePageWebCrawler");

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
            logger.debug("Response headers:");
            for (Header header : responseHeaders) {
                logger.debug("\t{}: {}", header.getName(), header.getValue());
            }
        }
    	/* 
    	 *  This DTO holds the information needed to save the
         *  complete web page for persistence via the data layer.
         */
    	CompleteWebPageDTO completeWebPageDTO = new CompleteWebPageDTO();

        logger.debug("The pagestoragelocation property is: " + saveWebPageCrawlConfig.getSavePageFolderName());
        System.out.println("The CompleteWebPageDTO now has the location member set to : " + completeWebPageDTO.getWebPageSaveLocation());


        // Get the web page parse data
        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
        
        /* 
         * Generate the filename for the html file.
         * Process and pass it to the DTO.
         * A Util method filters bad characters from the title string 
         */
        StringBuilder sb = new StringBuilder(Util.NormalizeStringForFilename(htmlParseData.getTitle()));
        sb.append(".html");	// adds the file extension
        completeWebPageDTO.setHtmlFileName(sb.toString());
        logger.debug("HTML filename in the DTO is now set to :" + completeWebPageDTO.getHtmlFileName());
        
        // Save the HTML contents of the web page to the DTO
        String htmlContents = new String(page.getContentData());
        completeWebPageDTO.setWebPageHtmlContents(htmlContents);
        
        List<SupportFileWithURL<byte[], String>> listOfSupportFileBinaryData = new ArrayList<>(); 
        List<SupportFileWithURL<String, String>> listOfSupportFileTextData = new ArrayList<>();
        List<SupportFileWithURL<String, String>> listOfSupportFileUnknownType = new ArrayList<>();
        List<SupportFileWithURL<String, String>> listOfSupportFileDefaultCaseSwitchType = new ArrayList<>();
        ParsedPageSupportFiles parsedPageSupportFiles = null;
        
        for (WebURL webURL : listOfPageSupportFileURLs) { // Each URL here is a support file, not a complete page
            PageFetchResult fetchResult = null;
			try {
				fetchResult = pageFetcher.fetchPage(webURL);
			} catch (InterruptedException | IOException | PageBiggerThanMaxSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            int statusCode = fetchResult.getStatusCode();
            FileContentType contentType = FileContentType.UNKNOWN;
        	// If statusCode is success, parse the url
            if (statusCode == 200) {       	
            	Page supportFilePage = new Page(webURL);
	        	try {
	                if (!fetchResult.fetchContent(supportFilePage,
                            myController.getConfig().getMaxDownloadSize())) {
	                		throw new ContentFetchException();
	                }
	        		// Populates the supportFilePage object with data from the URL
	        		contentType = saveWebPageParser.parse(supportFilePage, webURL.getURL(), FileContentType.UNKNOWN);
	        		logger.debug("Parsed the support file URL: "+ webURL.getURL());
				} catch (NotAllowedContentException | ParseException | ContentFetchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	// Select the file by contentType and store into a List<> object in memory
	        	  switch(contentType){  
	        	    case BINARY		: {
											logger.debug("The URL " + webURL.getURL() + " was found to be a BINARY");
	        	    						SupportFileWithURL<byte[], String> sfUrl = new SupportFileWithURL<>();
	        	    						sfUrl.setDataFile(supportFilePage.getContentData());
	        	    						sfUrl.setUrlString(webURL.getURL());
	        	    						listOfSupportFileBinaryData.add(sfUrl); break;
	        	    }
	        	    case TEXT		: {
											logger.debug("The URL " + webURL.getURL() + " was found to be a TEXT");
	        	    						SupportFileWithURL<String, String> sfUrl = new SupportFileWithURL<String, String>();
	        	    						sfUrl.setDataFile(new String(supportFilePage.getContentData()));
	        	    						sfUrl.setUrlString(webURL.getURL());
											listOfSupportFileTextData.add(sfUrl); break;
	        	    }
	        	    case UNKNOWN	: {
											logger.debug("The URL " + webURL.getURL() + " was found to be an UNKNOWN");
	        	    						logger.debug("The parser did not assign the content type for this file");
	        	    						SupportFileWithURL<String, String> sfUrl = new SupportFileWithURL<String, String>();
	        	    						if (supportFilePage.getContentType().contains("html")) 
	        	    							logger.debug("This file at URL: " + webURL.getURL() + "is an html file but not recognized");
											sfUrl.setDataFile(new String(supportFilePage.getContentData()));
	        	    						sfUrl.setUrlString(webURL.getURL());
											listOfSupportFileUnknownType.add(sfUrl); break;
	        	    }
	        	    default			: {
	        	    						logger.debug("switch statement placed this file into the listOfSupportFileDefaultCaseSwitchType list");
	        	    						SupportFileWithURL<String, String> sfUrl = new SupportFileWithURL<String, String>();
											sfUrl.setDataFile(new String(supportFilePage.getContentData()));
	        	    						sfUrl.setUrlString(webURL.getURL());
											listOfSupportFileUnknownType.add(sfUrl);
	        	    }
	        	  }	        	
            } else { // handle non successful statuses here
            	logger.debug("The support file URL: " + webURL.getURL() + " had a status code: " + statusCode);
            }
        }
        
	    // Create and populqte the support file data object here
	    parsedPageSupportFiles = new ParsedPageSupportFiles(
	    			 listOfSupportFileBinaryData, 
					 listOfSupportFileTextData,  
					 listOfSupportFileUnknownType, 
					 listOfSupportFileDefaultCaseSwitchType
					 );
       
        // Save the newly created support file data object into the DTO
	    completeWebPageDTO.setParsedPageSupportFiles(parsedPageSupportFiles);
        
        // Invoke the saveWebPageService
        saveService.saveCompleteWebPage(completeWebPageDTO, setOfAllUrlsWithFilenames, page);
        
        logger.debug("=============");
    }
    /*
     * Called from the WebCrawler.run() method
     * (non-Javadoc)
     * @see edu.uci.ics.crawler4j.crawler.WebCrawler#processPage(edu.uci.ics.crawler4j.url.WebURL)
     */
    @Override
	protected void processPage(WebURL curURL) {
    	
    	
        PageFetchResult fetchResult = null;
        try {
            if (curURL == null) {
                return;
            }
	        // To hold the list of page support files (css, js ...)
            final List<WebURL> listOfPageSupportFileURLs = new ArrayList<>();
            
            fetchResult = pageFetcher.fetchPage(curURL);
            int statusCode = fetchResult.getStatusCode();
            handlePageStatusCode(curURL, statusCode,
                                 EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode,
                                                                               Locale.ENGLISH));
            // Finds the status reason for all known statuses

            Page page = new Page(curURL);
            page.setFetchResponseHeaders(fetchResult.getResponseHeaders());
            page.setStatusCode(statusCode);
            if (statusCode < 200 ||
                statusCode > 299) { // Not 2XX: 2XX status codes indicate success
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY ||
                    statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                    statusCode == HttpStatus.SC_MULTIPLE_CHOICES ||
                    statusCode == HttpStatus.SC_SEE_OTHER ||
                    statusCode == HttpStatus.SC_TEMPORARY_REDIRECT ||
                    statusCode == 308) { // is 3xx  todo
                    // follow https://issues.apache.org/jira/browse/HTTPCORE-389

                    page.setRedirect(true);

                    String movedToUrl = fetchResult.getMovedToUrl();
                    if (movedToUrl == null) {
                        logger.warn("Unexpected error, URL: {} is redirected to NOTHING",
                                    curURL);
                        return;
                    }
                    page.setRedirectedToUrl(movedToUrl);
                    onRedirectedStatusCode(page);

                    if (myController.getConfig().isFollowRedirects()) {
                        int newDocId = getDocIdServer().getDocId(movedToUrl);
                        if (newDocId > 0) {
                            logger.debug("Redirect page: {} is already seen", curURL);
                            return;
                        }
                        WebURL webURL = new WebURL();
                        webURL.setURL(movedToUrl);
                        webURL.setParentDocid(curURL.getParentDocid());
                        webURL.setParentUrl(curURL.getParentUrl());
                        webURL.setDepth(curURL.getDepth());
                        webURL.setDocid(-1);
                        webURL.setAnchor(curURL.getAnchor());
                        if (shouldVisit(page, webURL)) {
                            if (!shouldFollowLinksIn(webURL) || getRobotstxtServer().allows(webURL)) {
                                webURL.setDocid(getDocIdServer().getNewDocID(movedToUrl));
                                getFrontier().schedule(webURL);
                            } else {
                                logger.debug(
                                    "Not visiting: {} as per the server's \"robots.txt\" policy",
                                    webURL.getURL());
                            }
                        } else {
                            logger.debug("Not visiting: {} as per your \"shouldVisit\" policy",
                                         webURL.getURL());
                        }
                    }
                } else { // All other http codes other than 3xx & 200
                    String description =
                        EnglishReasonPhraseCatalog.INSTANCE.getReason(fetchResult.getStatusCode(),
                                                                      Locale.ENGLISH); // Finds
                    // the status reason for all known statuses
                    String contentType = fetchResult.getEntity() == null ? "" :
                                         fetchResult.getEntity().getContentType() == null ? "" :
                                         fetchResult.getEntity().getContentType().getValue();
                    onUnexpectedStatusCode(curURL.getURL(), fetchResult.getStatusCode(),
                                           contentType, description);
                }
            } else { // if status code is 200      	
                if (!curURL.getURL().equals(fetchResult.getFetchedUrl())) {
                    if (getDocIdServer().isSeenBefore(fetchResult.getFetchedUrl())) {
                        logger.debug("Redirect page: {} has already been seen", curURL);
                        return;
                    }
                    curURL.setURL(fetchResult.getFetchedUrl());
                    curURL.setDocid(getDocIdServer().getNewDocID(fetchResult.getFetchedUrl()));
                }

                if (!fetchResult.fetchContent(page,
                                              myController.getConfig().getMaxDownloadSize())) {
                    throw new ContentFetchException();
                }

                if (page.isTruncated()) {
                    logger.warn(
                        "Warning: unknown page size exceeded max-download-size, truncated to: " +
                        "({}), at URL: {}",
                        myController.getConfig().getMaxDownloadSize(), curURL.getURL());
                }
                /* 
                 * Parsing is done at this point and all file types 
                 * (binary, text, html etc) can arrive here   
                 */
                FileContentType contentType = saveWebPageParser.parse(page, curURL.getURL(), FileContentType.UNKNOWN);
                             
                if (shouldFollowLinksIn(page.getWebURL())) {
                    ParseData parseData = page.getParseData();
                    List<WebURL> toSchedule = new ArrayList<>();                    
                    int maxCrawlDepth = myController.getConfig().getMaxDepthOfCrawling();
                    for (WebURL webURL : parseData.getOutgoingUrls()) { // OutgoingUrls includes links, css/js/png files etc
                        webURL.setParentDocid(curURL.getDocid());
                        webURL.setParentUrl(curURL.getURL());
                        int newdocid = getDocIdServer().getDocId(webURL.getURL());
                        if (newdocid > 0) {
                            // This is not the first time that this Url is visited. So, we set the
                            // depth to a negative number.
                            webURL.setDepth((short) -1);
                            webURL.setDocid(newdocid);
                        } else {
                            webURL.setDocid(-1);
                            webURL.setDepth((short) (curURL.getDepth() + 1));
                            if ((maxCrawlDepth == -1) || (curURL.getDepth() < maxCrawlDepth)) {
                                if (shouldVisit(page, webURL, listOfPageSupportFileURLs)) {
                                    if (getRobotstxtServer().allows(webURL)) {
                                        webURL.setDocid(getDocIdServer().getNewDocID(webURL.getURL()));
                                        toSchedule.add(webURL);
                                        

                                    } else {
                                        logger.debug(
                                            "Not visiting: {} as per the server's \"robots.txt\" " +
                                            "policy", webURL.getURL());
                                    }
                                } else {
                                    logger.debug(
                                        "Not visiting: {} as per your \"shouldVisit\" policy",
                                        webURL.getURL());
                                }
                            }
                        }
                    }

                    

                    

                    getFrontier().scheduleAll(toSchedule);
                } else {
                    logger.debug("Not looking for links in page {}, "
                                 + "as per your \"shouldFollowLinksInPage\" policy",
                                 page.getWebURL().getURL());
                }

                boolean noIndex = myController.getConfig().isRespectNoIndex() &&
                    page.getContentType() != null &&
                    page.getContentType().contains("html") &&
                    ((HtmlParseData)page.getParseData())
                        .getMetaTagValue("robots").
                        contains("noindex");

                if (!noIndex) {
                    visit(page, listOfPageSupportFileURLs);
                }
            }
        } catch (PageBiggerThanMaxSizeException e) {
            onPageBiggerThanMaxSize(curURL.getURL(), e.getPageSize());
        } catch (ParseException pe) {
            onParseError(curURL);
        } catch (ContentFetchException cfe) {
            onContentFetchError(curURL);
        } catch (NotAllowedContentException nace) {
            logger.debug(
                "Skipping: {} as it contains binary content which you configured not to crawl",
                curURL.getURL());
        } catch (Exception e) {
            onUnhandledException(curURL, e);
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
    }
    
    /**
     * This method saves the global set of filenames
     * and associated urls collected during a crawling
     * session, to the loca file system
     */
	public void onBeforeExit() {
    	logger.debug("The onBeforeExit() method has executed");
    	
    	/**
    	 * Open stream to properties file urltofilenamelookup.properties
    	 * and populate the file with the global set of urls
    	 */
        Properties prop = new Properties();
        FileOutputStream output = null;
    	try {
    		output = new FileOutputStream(saveWebPageCrawlConfig.getSavePageFolderName() + 
    				"/" + 
    				"urltofilenamelookup.properties"); // Overwrites exiting file
    		
        	// Populate the properties object with the global set of urls
        	// and associated local filenames
        	for (UrlWithFilename<String, String> urlWithFilename: setOfAllUrlsWithFilenames) {
        		prop.put(urlWithFilename.getOriginalUrl(), urlWithFilename.getLocalFilename());
        	}

    		// Save data to the properties file
    		prop.store(output, null);
        	logger.debug("The urltofilenamelookup.properties file has stored the url/filename information");
    	} catch (IOException ex) {
           	logger.error("There was an error saving url information to urltofilenamelookup.properties file");
    		ex.printStackTrace();
    	} finally {
    		if (output != null) {
    			try {
    				output.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
}
