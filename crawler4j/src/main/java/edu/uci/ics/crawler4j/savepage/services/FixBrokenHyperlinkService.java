package edu.uci.ics.crawler4j.savepage.services;

/**
 * This service will perform edits
 * to the web page html files similar to
 * the way a browser does when saving a 
 * web page to disc.
 * 
 * @author saleemhalipoto
 *
 */
public interface FixBrokenHyperlinkService {

	/**
	 * Adds a comment to top of html
	 * file mentioning source url for 
	 * save web page
	 */
	void PutOriginalUrlInComments();
	
	/**
	 * Changes the href attirbute in the html
	 * page for each support file to point to 
	 * the file with is new filename and location
	 * on local file system
	 */
	void PointHyperlinksToLocal();
}
