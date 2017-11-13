package edu.uci.ics.crawler4j.data;

/**
 * This generic class pairs an html file's 
 * original url with its local filename
 * enabling future processing to enable
 * all hyperlinks across a given 
 * seed url to work properly. Its scope
 * will be for an entire crawling session
 * for a given seed url
 * 
 * @author saleem halipoto
 *
 */
public class HtmlUrlWithFilename<R, S> {
	private R originalUrl;
	private S localFilename;
	
	public R getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(R originalUrl) {
		this.originalUrl = originalUrl;
	}
	public S getUrlString() {
		return localFilename;
	}
	public void setLocalFilename(S localFilename) {
		this.localFilename = localFilename;
	}
	
}