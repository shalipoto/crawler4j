package edu.uci.ics.crawler4j.savepage.crawlconfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.conn.DnsResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.message.BasicHeader;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.authentication.AuthInfo;

public class SaveWebPageCrawlConfig extends CrawlConfig {

    /**
     * user-agent string that is used for representing your crawler to web
     * servers. See http://en.wikipedia.org/wiki/User_agent for more details
     */
    private String userAgentString = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:10.0) Gecko/20100101 Firefox/10.0";
   
	/**
     * The parser can store the filename
     * for the saved web page in this variable
     */
    private String savePageFileName = null;
    
    /**
     * The parser can store the folder's name
     * where saved web pages will be stored
     */
    private String savePageFolderName = null;
    
    /**
     * This is the name of the properties file that
     * holds the relationship between a web page's
     * links and their finenames on the local file
     * system
     */
    private String filenameAssociationsPropFile_name;
    
    private String seedURL = null;
    
    public String getSeedURL() {
		return seedURL;
	}

	public void setSeedURL(String seedURL) {
		this.seedURL = seedURL;
	}

	public String getUserAgentString() {
		return userAgentString;
	}

	public void setUserAgentString(String userAgentString) {
		this.userAgentString = userAgentString;
	}
    
	public String getSavePageFileName() {
		return savePageFileName;
	}

	public void setSavePageFileName(String savePageFileName) {
		this.savePageFileName = savePageFileName;
	}
    
    public String getSavePageFolderName() {
		return savePageFolderName;
	}

	public void setSavePageFolderName(String savePageFolderName) {
		this.savePageFolderName = savePageFolderName;
	}
	
	public String getFilenameAssociationsPropFile_name() {
		return filenameAssociationsPropFile_name;
	}

	public void setFilenameAssociationsPropFile_name(String filenameAssociationsPropFile_name) {
		this.filenameAssociationsPropFile_name = filenameAssociationsPropFile_name;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Crawl storage folder: " + getCrawlStorageFolder() + "\n");       
        sb.append("savePageFileName: " + getSavePageFileName() + "\n");
        sb.append("savePageFolderName: " + getSavePageFolderName() + "\n");
        sb.append("seedURL: " + getSeedURL() + "\n");
        sb.append("filenameAssociationsPropFile_name: " + getFilenameAssociationsPropFile_name() + "\n"); 
        sb.append("Resumable crawling: " + isResumableCrawling() + "\n");
        sb.append("Max depth of crawl: " + getMaxDepthOfCrawling() + "\n");
        sb.append("Max pages to fetch: " + getMaxPagesToFetch() + "\n");
        sb.append("User agent string: " + getUserAgentString() + "\n");
        sb.append("Include https pages: " + isIncludeHttpsPages() + "\n");
        sb.append("Include binary content: " + isIncludeBinaryContentInCrawling() + "\n");
        sb.append("Max connections per host: " + getMaxConnectionsPerHost() + "\n");
        sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
        sb.append("Socket timeout: " + getSocketTimeout() + "\n");
        sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
        sb.append("Max outgoing links to follow: " + getMaxOutgoingLinksToFollow() + "\n");
        sb.append("Max download size: " + getMaxDownloadSize() + "\n");
        sb.append("Should follow redirects?: " + isFollowRedirects() + "\n");
        sb.append("Proxy host: " + getProxyHost() + "\n");
        sb.append("Proxy port: " + getProxyPort() + "\n");
        sb.append("Proxy username: " + getProxyUsername() + "\n");
        sb.append("Proxy password: " + getProxyPassword() + "\n");
        sb.append("Thread monitoring delay: " + getThreadMonitoringDelaySeconds() + "\n");
        sb.append("Thread shutdown delay: " + getThreadShutdownDelaySeconds() + "\n");
        sb.append("Cleanup delay: " + getCleanupDelaySeconds() + "\n");
        sb.append("Cookie policy: " + getCookiePolicy() + "\n");
        sb.append("Respect nofollow: " + isRespectNoFollow() + "\n");
        sb.append("Respect noindex: " + isRespectNoIndex() + "\n");
        return sb.toString();
    }

}
