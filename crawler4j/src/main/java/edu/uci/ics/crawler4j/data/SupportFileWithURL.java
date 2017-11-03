package edu.uci.ics.crawler4j.data;

/**
 * This generic class pairs the content of 
 * a page support file (jpg/js/css etc) with
 * its URL to enable saving the file using a
 * naming similar to the original
 * 
 * @author saleemhalipoto
 *
 */
public class SupportFileWithURL <R, S> {
	private R dataFile;
	private S urlString;
	
	public R getDataFile() {
		return dataFile;
	}
	public void setDataFile(R dataFile) {
		this.dataFile = dataFile;
	}
	public S getUrlString() {
		return urlString;
	}
	public void setUrlString(S urlString) {
		this.urlString = urlString;
	}
	
}
