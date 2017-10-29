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
import java.io.IOException;
import java.util.ArrayList;
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

import edu.uci.ics.crawler4j.DTO.CompleteWebPageDTO;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.exceptions.ContentFetchException;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.crawler.exceptions.ParseException;
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
	
	/* 
	 *  This DTO holds the information needed to save the
     *  complete web page for persistence via the data layer.
     */
	CompleteWebPageDTO completeWebPageDTO = new CompleteWebPageDTO();
	
	Parser saveWebPageParser = null;
	SaveWebPageCrawlConfig saveWebPageCrawlConfig = null;
	
	/*
	 * A list of the URLs of the support files needed to display 
	 * a web page being saved to the local file system
	 * eg. css, js, png, bmp etc
	 */
	List <WebURL> listOfPageSupportFileURLs = new ArrayList<WebURL>();
			
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
    	super.init(id, crawlController);
        
    	myId = id;
        pageFetcher = crawlController.getPageFetcher();
        setRobotstxtServer(crawlController.getRobotstxtServer());
        setDocIdServer(crawlController.getDocIdServer());
        setFrontier(crawlController.getFrontier());
        myController = crawlController;
        setWaitingForNewURLs(false);
    	
    	saveWebPageCrawlConfig = (SaveWebPageCrawlConfig) crawlController.getConfig();        
        setParser(new SaveWebPageParser(saveWebPageCrawlConfig));
        
        // Initialize the CompleteWebPageDTO location member variable
        Properties prop = new Properties();
        FileInputStream input = null;

    	try {
    		input = new FileInputStream("savewebpage.properties");

    		// load a properties file
    		prop.load(input);

    		// get the property value and save it to the DTO
    		completeWebPageDTO.setWebPageSaveLocation(prop.getProperty("pagestoragelocation"));
            logger.debug("Set the pagestoragelocation property in the completeWebPageDTO" + completeWebPageDTO.getWebPageSaveLocation());
            System.out.println("The CompleteWebPageDTO now has the location member set to : " + completeWebPageDTO.getWebPageSaveLocation());

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
    }   
    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
        	// Add this URL to the list of support file urls
        	listOfPageSupportFileURLs.add(url);
            return false;
        }

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return href.startsWith("http://www.ics.uci.edu/");
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
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

        logger.debug("=============");
    }
    
    @Override
	protected void processPage(WebURL curURL) {
    	
    	
        PageFetchResult fetchResult = null;
        try {
            if (curURL == null) {
                return;
            }
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
                getParser().parse(page, curURL.getURL());

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
                                if (shouldVisit(page, webURL)) {
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
                    /*
                     *  After scheduling this URL now save its HTML contents to memory,
                     *  converting array byte[] ContentData to a String for local storage
                     */ 
                    String htmlContents = new String(page.getContentData());
                    completeWebPageDTO.setHtmlContents(htmlContents);
                    
                    /* 
                     * Get the filename generated in the parsing process
                     * and saved in the config object, and pass it to the DTO.
                     * A Util method is needed to remove bad characters before 
                     * using as a filename
                     */
                    StringBuilder sb = new StringBuilder(Util.NormalizeStringForFilename(saveWebPageCrawlConfig.getSavePageFileName()));
                    sb.append(".html");	// adds the file extension
                    completeWebPageDTO.setHtmlFileName(sb.toString());
                    logger.debug("HTML filename in the DTO is now set to :" + completeWebPageDTO.getHtmlFileName());
                    
                    SaveWebPageServiceImpl saveService = new SaveWebPageServiceImpl();
                    saveService.SaveCompleteWebPage(completeWebPageDTO, null);
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
                    visit(page);
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
    
    public Parser getSaveWebPageParser() {
		return saveWebPageParser;
	}

	public void setSaveWebPageParser(Parser saveWebPageParser) {
		this.saveWebPageParser = saveWebPageParser;
	}
}
